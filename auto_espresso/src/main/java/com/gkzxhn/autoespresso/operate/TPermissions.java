package com.gkzxhn.autoespresso.operate;

import android.os.Build;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.permission.PermissionRequester;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;

/**
 * Created by Raleigh.Luo on 18/3/13.
 */

public class TPermissions {
    /**使用shell命令获取权限
     * @param permissions 权限 eg:android.permission.CALL_PHONE
     */
    public static void get_permission_shell(String... permissions){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for(String permission : permissions) {
                getInstrumentation().getUiAutomation().executeShellCommand(
                        "pm grant " + getTargetContext().getPackageName()
                                + " " + permission);
            }
        }
    }

    /**使用shell命令获取权限
     * @param permissions 权限 eg:android.permission.CALL_PHONE
     */
    public static void request_permissionsByGrant(String... permissions){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for(String permission : permissions){
                GrantPermissionRule.grant(permission);
            }
        }
    }
    public static void request_permissions(String... permissions){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionRequester permissionRequester  = new  PermissionRequester();
            for(String permission : permissions){
                permissionRequester.addPermissions(permission);
            }
            permissionRequester.requestPermissions();
        }
    }



}
