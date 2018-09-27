package com.gkzxhn.autoespresso.code;

import com.gkzxhn.autoespresso.config.Config;
import com.gkzxhn.autoespresso.operate.TDrawer;

import java.lang.reflect.Method;

/**
 * Created by Raleigh.Luo on 18/3/14.
 */

public class DrawerCode extends BaseCode {
    /**
     * 打开抽屉
     * @param procedureName
     * @return
     */
    public static String open_closed_drawer(String procedureName) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TDrawer.open_closed_drawer()"+END_LINE;
        return String.format(format,procedureName);
    }

    /**
     * 关闭抽屉
     * @param procedureName
     * @return
     */
    public static String close_opened_drawer(String procedureName) {
        String format=TABS_LINE+"//%s"+ END_LINE
                +TABS_LINE+"TDrawer.close_opened_drawer()"+END_LINE;
        return String.format(format,procedureName);
    }

    /**
     *打开抽屉
     * @param procedureName
     * @param gravity  eg: 数字／Grivity.center
     * @return
     */
    public static String open_closed_drawer_gravity(String procedureName,String gravity) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TDrawer.open_closed_drawer(%s)"+END_LINE;
        return String.format(format,procedureName,gravity);
    }
    /**
     * 关闭抽屉
     * @param procedureName 测试步骤
     * @param gravity Gravity.
     */
    public static String close_opened_drawer_gravity(String procedureName,String gravity) {
        String format=TABS_LINE+"//%s"+END_LINE
                +TABS_LINE+"TDrawer.close_opened_drawer(%s)"+END_LINE;
        return String.format(format,procedureName,gravity);
    }
}
