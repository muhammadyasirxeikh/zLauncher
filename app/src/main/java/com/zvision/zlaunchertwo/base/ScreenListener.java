package com.zvision.zlaunchertwo.base;

import android.view.KeyEvent;
import android.view.View;

public interface ScreenListener {
    abstract public void handleKeys(int keyCode, KeyEvent event);
    abstract public void onClick(View v);
    abstract public void cleanup();
    abstract public void init();
}
