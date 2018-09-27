package com.gkzxhn.autoespresso.operate;

import static android.support.test.espresso.contrib.DrawerActions.close;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.Espresso.onView;

/**
 * Created by Raleigh.Luo on 18/3/13.
 */

public class TDrawer {
    public static void open_closed_drawer(){
        onView(isClosed()).perform(open());
    }
    public static void  close_opened_drawer(){
        onView(isOpen()).perform(close());
    }
    public static void open_closed_drawer(int gravity){
        onView(isClosed(gravity)).perform(open(gravity));
    }
    public static void close_opened_drawer(int gravity){
        onView( isOpen(gravity)).perform(close(gravity));
    }
}
