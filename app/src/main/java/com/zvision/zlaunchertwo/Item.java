package com.zvision.zlaunchertwo;

import android.graphics.drawable.Drawable;

public class Item  {

    public CharSequence __packgeName; // packge name
    public CharSequence __appName; //App name
    public Drawable __appIcon; //App icon

    public Item(CharSequence packge, CharSequence app, Drawable icon){
        __packgeName =packge;
        __appName = app;
        __appIcon = icon;
    }
}
