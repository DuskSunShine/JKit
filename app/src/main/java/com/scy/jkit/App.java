package com.scy.jkit;

import android.app.Application;
import com.scy.core.common.JKit;

/**
 * @author: SCY
 * @date: 2020/11/25   14:16
 * @version:
 * @desc:
 */
public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        JKit.setDebug(true);
    }


}
