package com.gkzxhn.autoespresso.code;


import com.gkzxhn.autoespresso.config.ClassConfig;
import com.gkzxhn.autoespresso.config.Config;
import com.gkzxhn.autoespresso.config.TableConfig;
import com.gkzxhn.autoespresso.operate.TPermissions;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by Raleigh.Luo on 18/3/14.
 */

public class PermissionCode extends BaseCode {
    /**
     * shell命令获取权限
     * @param permissions
     * @return
     */
    public static String getPermissionShell(String... permissions){
        StringBuffer sb=new StringBuffer();
        sb.append(Config.TABS_LINE+Config.TABS_LINE+"if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {"+Config.WRAP);
        String format1=TABS_LINE+Config.TABS_LINE+"TPermissions.get_permission_shell(%s)"+END_LINE;
        String format2=TABS_LINE+Config.TABS_LINE+"TPermissions.get_permission_shell(\"%s\")"+END_LINE;
        for(String permission : permissions) {
            if(permission.contains("Manifest.permission")){
                sb.append(String.format(format1,permission));
            }else{
                sb.append(String.format(format2,permission));
            }
        }
        sb.append(Config.TABS_LINE+Config.TABS_LINE+"}"+Config.WRAP);
        return sb.toString();
    }

    /**
     * shell命令获取权限
     * @return
     */
    public static String getSharedpreference(String putExtra,String key,String value){
        String extra="";
        if(key.length()>0) {
            switch (putExtra) {
                case TableConfig.PUT_STRING:
                    extra = String.format("edit.%s(\"%s\",\"%s\")", putExtra, key, value);
                    break;
                case TableConfig.PUT_BOOLEAN:
                    extra = String.format("edit.%s(\"%s\",%s)", putExtra, key, value.isEmpty()?false:value);
                    break;
                case TableConfig.PUT_INT:
                    extra = String.format("edit.%s(\"%s\",%s)", putExtra, key, value.isEmpty()?0:value);
                    break;
                case TableConfig.PUT_FLOAT:
                    extra = String.format("edit.%s(\"%s\",%sf)", putExtra, key, value.isEmpty()?0:value);
                    break;
                case TableConfig.PUT_LONG:
                    extra = String.format("edit.%s(\"%s\",%sL)", putExtra, key, value.isEmpty()?0:value);
                    break;
            }
            extra = Config.TABS_LINE + Config.TABS_LINE + extra + Config.END_LINE;
        }
        return extra;
    }

    /**
     * shell命令获取权限
     * @return
     */
    public static String getIntentExtras(String putExtra,String key,String value){
        String extra="";
        if(key.length()>0) {
            switch (putExtra) {
                case TableConfig.PUT_STRING:
                    extra = String.format("intent.putExtra(\"%s\",\"%s\")", key, value);
                    break;
                case TableConfig.PUT_BOOLEAN:
                    extra = String.format("edit.%s(\"%s\",%s)", putExtra, key, value.isEmpty()?false:value);
                    break;
                case TableConfig.PUT_INT:
                    extra = String.format("intent.putExtra(\"%s\",%s)", key, value.isEmpty()?0:value);
                    break;
                case TableConfig.PUT_FLOAT:
                    extra = String.format("intent.putExtra(\"%s\",%sf)", key, value.isEmpty()?0:value);
                    break;
                case TableConfig.PUT_LONG:
                    extra = String.format("intent.putExtra(\"%s\",%sL)", key, value.isEmpty()?0:value);
                    break;
            }
            extra = Config.TABS_LINE + Config.TABS_LINE + Config.TABS_LINE + extra + Config.END_LINE;
        }
        return extra;
    }

}
