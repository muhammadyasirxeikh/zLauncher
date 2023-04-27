package com.zvision.zlaunchertwo.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.zvision.zlaunchertwo.shared_storage.SharedPreferenceStorage;
import com.zvision.zlaunchertwo.support.ApplicationStorage;

public class BaseActivity extends AppCompatActivity {

    public ApplicationStorage app = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = ApplicationStorage.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        app = ApplicationStorage.getInstance();

        if (!app.isinittask()) { // inittask will execute once
            if (SharedPreferenceStorage.getBooleanValue(this, Constants.Storage.MY_FIRST_TIME.name(), true)) {
                try {
                    SharedPreferenceStorage.setBooleanValue(this, Constants.Storage.MY_FIRST_TIME.name(), false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
            Constants.DEVICE_SCREEN_WIDTH = dm.widthPixels;
            Constants.DEVICE_SCREEN_HEIGHT = dm.heightPixels;
            dm = null;
            app.setinittask(true);
        }
        init();
    }

    public void init() {
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 24:
                return super.onKeyUp(keyCode, event);
        }
        handleKeys(keyCode, event);
        return false;
    }

    public void handleKeys(int keyCode, KeyEvent event) {
    }

    @Override
    public void onDestroy() {
        if (app.isFinishApp()) {
            app.onTerminate();
            app = null;
        }
        System.gc();
        super.onDestroy();
    }

    public ProgressDialog mProgressDialog;

    public void showLoading(final String mProgressMsg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mProgressDialog == null)
                        mProgressDialog = new ProgressDialog(BaseActivity.this);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.setMessage(mProgressMsg);
                    if (!isFinishing() && !mProgressDialog.isShowing())
                        mProgressDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void showLoadingBtn(final String mProgressMsg, final DialogInterface.OnClickListener listener) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mProgressDialog == null)
                        mProgressDialog = new ProgressDialog(BaseActivity.this);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    if (listener != null)
                        mProgressDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "CANCEL", listener);
                    mProgressDialog.setMessage(mProgressMsg);
                    if (!isFinishing() && !mProgressDialog.isShowing())
                        mProgressDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void removeLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                } catch (Exception e) {
                }
            }
        });
    }

    public void startLockedActivity(final String appLockingStorageKey, final Intent mIntent) {
        if (SharedPreferenceStorage.getBooleanValue(this, Constants.Storage.APPS_LOCK_ENABLED.name(),false)
                && SharedPreferenceStorage.getBooleanValue(this, appLockingStorageKey,true)) {
            HelperUtils.onActionAppOpening(this, new HelperUtils.OnAppOpeningPasswordValidateListener() {
                @Override
                public void OnAppLockStatus(boolean allowOpening) {
                    if (allowOpening) {
                        startActivity(mIntent);
                    } else {
                        HelperUtils.showMessageInfo(BaseActivity.this, "Wrong password entered. Please try again!");
                    }
                }
            });
        } else {
            startActivity(mIntent);
        }
    }
}
