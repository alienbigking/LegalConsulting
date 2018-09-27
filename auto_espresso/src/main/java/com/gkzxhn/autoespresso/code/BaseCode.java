package com.gkzxhn.autoespresso.code;

import com.gkzxhn.autoespresso.config.Config;

/**
 * Created by Raleigh.Luo on 18/3/2.
 */

public class BaseCode {
    protected static final String TABS_LINE= Config.TEST_CLASS_SUFFIX.equals(Config.TEST_CLASS_SUFFIX_JAVA)?
            (Config.TABS_LINE+ Config.TABS_LINE):(Config.TABS_LINE+ Config.TABS_LINE+ Config.TABS_LINE);
    protected static final String END_LINE= Config.END_LINE;
}
