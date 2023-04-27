package com.zvision.zlaunchertwo.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zvision.zlaunchertwo.R;
import com.zvision.zlaunchertwo.part.DownloadImage;
import com.zvision.zlaunchertwo.part.DownloadNewUpdate;
import com.zvision.zlaunchertwo.part.HelpInfoOrAdsButtonInfo;
import com.zvision.zlaunchertwo.shared_storage.SharedPreferenceStorage;

import org.json.JSONObject;

import java.net.URL;

public class HelperUtils {

    public static int haveNetworkConnection(BaseActivity mBase) {
        try {
            final ConnectivityManager cm = (ConnectivityManager) mBase.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (cm != null) {
                if (Build.VERSION.SDK_INT < 23) {
                    final NetworkInfo ni = cm.getActiveNetworkInfo();

                    if (ni != null) {
                        return (ni.isConnected() && (ni.getType() == ConnectivityManager.TYPE_WIFI || ni.getType() == ConnectivityManager.TYPE_MOBILE)) ? 1 : 0;
                    }
                } else {
                    final Network n = cm.getActiveNetwork();

                    if (n != null) {
                        final NetworkCapabilities nc = cm.getNetworkCapabilities(n);
                        if (nc.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                            return 2;
                        } else {
                            return (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                                    || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                                    || nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) ? 1 : 0;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getVerticalRatio(int pt) {
        int ptm = (int) (((double) pt / (double) 480) * Constants.DEVICE_SCREEN_HEIGHT);
        if (ptm == 0) {
            ptm = 1;
        }
        return ptm;
    }

    public static int getHorizontalRatio(int pt) {
        int ptm = (int) (((double) pt / (double) 320) * Constants.DEVICE_SCREEN_WIDTH);
        if (ptm == 0) {
            ptm = 1;
        }
        return ptm;
    }

    public static float getTextRatio(int pt) {
        float ptm = (float) (((double) pt / (double) 320) * Constants.DEVICE_SCREEN_WIDTH);
        if (ptm == 0.0f) {
            ptm = 1;
        }
        return ptm;
    }

    public interface OnActivatedListener {
        void onResult();
    }

    public static void firstTimeInstallAskForActivationCode(final BaseActivity mBase, final OnActivatedListener mOnActivatedListener) {
        mBase.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (SharedPreferenceStorage.getStringValue(mBase, Constants.Storage.ACTIVE_VERSION_NAME.name(), "0").equalsIgnoreCase("0")) {
                        Constants.IS_FIRST_LAUNCH_SETTINGS_FETCH_DONE = true;
                        final AlertDialog[] alertDialog = {null};
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mBase);
                        LayoutInflater inflater = mBase.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.text_inpu_password, null);
                        dialogBuilder.setView(dialogView);
                        final EditText editText = (EditText) dialogView.findViewById(R.id.etPassword);
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        editText.requestFocus();
                        final TextView tvSubmitBtn = dialogView.findViewById(R.id.tvPwdBtn);
                        final ImageView ivWifiStateBtn = dialogView.findViewById(R.id.ivWifiStateBtn);
                        ivWifiStateBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                launchNetworkSettings(mBase);
                            }
                        });
                        tvSubmitBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mBase.showLoading("Validating Code...");
                                final String input = editText.getText().toString().trim();
                                if (input.length() > 0) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                DownloadImage.doFetchUpdateDetails(true, mBase, new DownloadImage.OnJSONLoadListener() {
                                                    @Override
                                                    public void OnComplete(boolean isNeedToDownloadIcon, boolean isInstantAppUpdateRequire) {
                                                        mBase.runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                if (input.equalsIgnoreCase(Constants.VERSION.Box1.getVerVal())) {
                                                                    Constants.ACTIVE_VERSION = Constants.VERSION.Box1.name();
                                                                    SharedPreferenceStorage.setStringValue(mBase, Constants.Storage.ACTIVE_VERSION_NAME.name(), Constants.VERSION.Box1.name());
                                                                    SharedPreferenceStorage.setStringValue(mBase, Constants.Storage.UPDATE_DETAILS_SETTINGS_URL.name(), Constants.SETTINGS_URL_BOX1);
                                                                    if (alertDialog[0] != null)
                                                                        alertDialog[0].dismiss();
                                                                    if (mOnActivatedListener != null)
                                                                        doShowSuccessfullyActivatedDialog(mBase, mOnActivatedListener);
                                                                } else if (input.equalsIgnoreCase(Constants.VERSION.Box2.getVerVal())) {
                                                                    Constants.ACTIVE_VERSION = Constants.VERSION.Box2.name();
                                                                    SharedPreferenceStorage.setStringValue(mBase, Constants.Storage.ACTIVE_VERSION_NAME.name(), Constants.VERSION.Box2.name());
                                                                    SharedPreferenceStorage.setStringValue(mBase, Constants.Storage.UPDATE_DETAILS_SETTINGS_URL.name(), Constants.SETTINGS_URL_BOX2);
                                                                    if (alertDialog[0] != null)
                                                                        alertDialog[0].dismiss();
                                                                    if (mOnActivatedListener != null)
                                                                        doShowSuccessfullyActivatedDialog(mBase, mOnActivatedListener);
                                                                } else if (input.equalsIgnoreCase(Constants.VERSION.Box3.getVerVal())) {
                                                                    Constants.ACTIVE_VERSION = Constants.VERSION.Box3.name();
                                                                    SharedPreferenceStorage.setStringValue(mBase, Constants.Storage.ACTIVE_VERSION_NAME.name(), Constants.VERSION.Box3.name());
                                                                    SharedPreferenceStorage.setStringValue(mBase, Constants.Storage.UPDATE_DETAILS_SETTINGS_URL.name(), Constants.SETTINGS_URL_BOX3);
                                                                    if (alertDialog[0] != null)
                                                                        alertDialog[0].dismiss();
                                                                    if (mOnActivatedListener != null)
                                                                        doShowSuccessfullyActivatedDialog(mBase, mOnActivatedListener);
                                                                } else if (input.equalsIgnoreCase(Constants.VERSION.Box4.getVerVal())) {
                                                                    Constants.ACTIVE_VERSION = Constants.VERSION.Box4.name();
                                                                    SharedPreferenceStorage.setStringValue(mBase, Constants.Storage.ACTIVE_VERSION_NAME.name(), Constants.VERSION.Box4.name());
                                                                    SharedPreferenceStorage.setStringValue(mBase, Constants.Storage.UPDATE_DETAILS_SETTINGS_URL.name(), Constants.SETTINGS_URL_BOX4);
                                                                    if (alertDialog[0] != null)
                                                                        alertDialog[0].dismiss();
                                                                    if (mOnActivatedListener != null)
                                                                        doShowSuccessfullyActivatedDialog(mBase, mOnActivatedListener);
                                                                } else if (input.equalsIgnoreCase(Constants.VERSION.Box5.getVerVal())) {
                                                                    Constants.ACTIVE_VERSION = Constants.VERSION.Box5.name();
                                                                    SharedPreferenceStorage.setStringValue(mBase, Constants.Storage.ACTIVE_VERSION_NAME.name(), Constants.VERSION.Box5.name());
                                                                    SharedPreferenceStorage.setStringValue(mBase, Constants.Storage.UPDATE_DETAILS_SETTINGS_URL.name(), Constants.SETTINGS_URL_BOX5);
                                                                    if (alertDialog[0] != null)
                                                                        alertDialog[0].dismiss();
                                                                    if (mOnActivatedListener != null)
                                                                        doShowSuccessfullyActivatedDialog(mBase, mOnActivatedListener);
                                                                } else if (input.equalsIgnoreCase(Constants.VERSION.Box6.getVerVal())) {
                                                                    Constants.ACTIVE_VERSION = Constants.VERSION.Box6.name();
                                                                    SharedPreferenceStorage.setStringValue(mBase, Constants.Storage.ACTIVE_VERSION_NAME.name(), Constants.VERSION.Box6.name());
                                                                    SharedPreferenceStorage.setStringValue(mBase, Constants.Storage.UPDATE_DETAILS_SETTINGS_URL.name(), Constants.SETTINGS_URL_BOX6);
                                                                    if (alertDialog[0] != null)
                                                                        alertDialog[0].dismiss();
                                                                    if (mOnActivatedListener != null)
                                                                        doShowSuccessfullyActivatedDialog(mBase, mOnActivatedListener);
                                                                } else {
                                                                    editText.setError("Please Enter valid code!");
                                                                    Toast.makeText(mBase, "Wrong Activation Code!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        mBase.removeLoading();
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                }
                            }
                        });

                        alertDialog[0] = dialogBuilder.create();
                        alertDialog[0].setCancelable(false);
                        alertDialog[0].show();
                    } else {
                        Constants.ACTIVE_VERSION = SharedPreferenceStorage.getStringValue(mBase, Constants.Storage.ACTIVE_VERSION_NAME.name(), null);
                        if (mOnActivatedListener != null)
                            mOnActivatedListener.onResult();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void doShowSuccessfullyActivatedDialog(final BaseActivity mBase, final OnActivatedListener mOnActivatedListener) {
        try {
            mBase.showLoading("Your Launcher has been activated successfully!");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        mBase.showLoading("LOADING...");
                        mOnActivatedListener.onResult();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromDrawable(Drawable drawable, boolean isFocused) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return addWhiteBorder(bitmapDrawable.getBitmap(), isFocused);
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return addWhiteBorder(bitmap, isFocused);
    }

    public static Bitmap addWhiteBorder(Bitmap bmp, boolean isFocused) {
        int borderSize = getHorizontalRatio(7);
        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(Color.parseColor(isFocused ? "#C1292F" : "#F1EFEF"));
        canvas.drawBitmap(bmp, borderSize, borderSize, null);
        return bmpWithBorder;
    }

    public static boolean checkNetworkStatus(BaseActivity mBase, int deviceId) {
        boolean status = false;
        try {
            ConnectivityManager connMgr = (ConnectivityManager) mBase.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connMgr == null) {
                status = false;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network network = connMgr.getActiveNetwork();
                if (network == null)
                    status = false;
                NetworkCapabilities capabilities = connMgr.getNetworkCapabilities(network);
                if (capabilities != null) {
                    if (deviceId == 1 && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN))) {
                        status = true;
                    } else if (deviceId == 2 && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        status = true;
                    } else if (deviceId == 3 && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
                        status = true;
                    } else {
                        status = false;
                    }
                } else {
                    status = false;
                }
            } else {
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo.isConnected() && networkInfo.getType() == (deviceId == 1 ? ConnectivityManager.TYPE_WIFI : (deviceId == 2 ? ConnectivityManager.TYPE_ETHERNET : ConnectivityManager.TYPE_BLUETOOTH)))
                    status = true;
                else
                    status = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public interface OnAppOpeningPasswordValidateListener {
        void OnAppLockStatus(boolean allowOpening);
    }

    public static void onActionAppOpening(final BaseActivity mBase, final OnAppOpeningPasswordValidateListener mOnAppOpeningPasswordValidateListener) {
        mBase.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (SharedPreferenceStorage.getBooleanValue(mBase, Constants.Storage.APPS_LOCK_ENABLED.name(), false)) {
                        final AlertDialog[] alertDialog = {null};
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mBase);
                        LayoutInflater inflater = mBase.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.text_inpu_password, null);
                        dialogBuilder.setView(dialogView);
                        final TextView tvLabel = dialogView.findViewById(R.id.tvLabel);
                        tvLabel.setText(R.string.please_enter_password_lbl);
                        final EditText editText = (EditText) dialogView.findViewById(R.id.etPassword);
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                        editText.setHint("Enter password here...");
                        final TextView tvSubmitBtn = dialogView.findViewById(R.id.tvPwdBtn);
                        tvSubmitBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final String input = editText.getText().toString().trim();
                                alertDialog[0].dismiss();
                                if (input.length() > 0) {
                                    mOnAppOpeningPasswordValidateListener.OnAppLockStatus(input.equalsIgnoreCase(SharedPreferenceStorage.getStringValue(mBase, Constants.Storage.APP_LOCK_PASSWORD.name(), Constants.APP_LOCK_BLANK_PASSWORD)));
                                }
                            }
                        });

                        tvSubmitBtn.setOnKeyListener(new View.OnKeyListener() {
                            @Override
                            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
                                    tvSubmitBtn.performClick();
                                    return true;
                                }
                                return false;
                            }
                        });
                        alertDialog[0] = dialogBuilder.create();
                        alertDialog[0].setCancelable(true);
                        alertDialog[0].show();
                    } else {
                        mOnAppOpeningPasswordValidateListener.OnAppLockStatus(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public interface OnPasswordCreateUpdateListener {
        void onPasswordCreateOrUpdated();
    }

    public static void doShowPasswordCreatingDialog(final BaseActivity mBase, final boolean isPasswordUpdateDialog, final OnPasswordCreateUpdateListener mOnPasswordCreateUpdateListener) {
        mBase.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final AlertDialog[] alertDialog = {null};
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mBase);
                    LayoutInflater inflater = mBase.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.text_inpu_password, null);
                    dialogBuilder.setView(dialogView);
                    final TextView tvLabel = dialogView.findViewById(R.id.tvLabel);
                    final EditText editText = (EditText) dialogView.findViewById(R.id.etPassword);
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                    final EditText editTextConfirm = (EditText) dialogView.findViewById(R.id.etPasswordConfirm);
                    editTextConfirm.setVisibility(View.VISIBLE);
                    editTextConfirm.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                    final EditText editTextOldPassword = (EditText) dialogView.findViewById(R.id.etPasswordCurrent);
                    if (isPasswordUpdateDialog) {
                        tvLabel.setText("Change your password");

                        editTextOldPassword.setVisibility(View.VISIBLE);
                        editTextOldPassword.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

                        editTextOldPassword.setHint("Enter current password here...");
                        editText.setHint("Enter New password here...");
                        editTextConfirm.setHint("Re-enter New password here...");
                    } else {
                        tvLabel.setText("Create your password");
                        editText.setHint("Enter password here...");
                        editTextConfirm.setHint("Re-enter password here...");
                    }
                    final TextView tvSubmitBtn = dialogView.findViewById(R.id.tvPwdBtn);
                    tvSubmitBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isPasswordUpdateDialog) {
                                if (!SharedPreferenceStorage.getStringValue(mBase, Constants.Storage.APP_LOCK_PASSWORD.name(), Constants.APP_LOCK_BLANK_PASSWORD).equalsIgnoreCase(editTextOldPassword.getText().toString().trim())) {
                                    editTextOldPassword.setError("Wrong Password Entered!");
                                    return;
                                }
                            }

                            final String input1 = editText.getText().toString().trim();
                            if (input1.length() > 0) {
                                if (editTextConfirm.getText().toString().trim().equalsIgnoreCase(input1)) {
                                    SharedPreferenceStorage.setStringValue(mBase, Constants.Storage.APP_LOCK_PASSWORD.name(), input1);
                                    if (mOnPasswordCreateUpdateListener != null) {
                                        mOnPasswordCreateUpdateListener.onPasswordCreateOrUpdated();
                                    } else {
                                        showMessageInfo(mBase, "Your Password has been Stored Successfully. If App lock is Active, You need to enter this password when ever you want to launch any app.");
                                    }
                                    alertDialog[0].dismiss();
                                } else {
                                    editTextConfirm.setError("Password does not matched!");
                                }
                            } else {
                                editText.setError("This field must not be blank!");
                            }
                        }
                    });

                    alertDialog[0] = dialogBuilder.create();
                    alertDialog[0].setCancelable(true);
                    alertDialog[0].show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface OnAppLockListener {
        void getAppLockStatus(boolean isAppLockEnable);
    }

    public static void showAppLockActivateDeActivateDialog(final BaseActivity mBase, final boolean doActivateAppLock, final OnAppLockListener mOnAppLockListener) {
        mBase.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    AlertDialog.Builder changeAppDialog = new AlertDialog.Builder(mBase);
                    changeAppDialog.setMessage(doActivateAppLock ?
                            "App lock will be activated and every time when you want to open any app you will need to enter the app lock password to open the app." :
                            "App lock will be deactivated and no longer need to enter the app lock password to open any app.")
                            .setTitle(doActivateAppLock ? "Do you want to Activate app lock?" : "Do you want to Deactivate app lock?")
                            .setCancelable(true)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    SharedPreferenceStorage.setBooleanValue(mBase, Constants.Storage.APPS_LOCK_ENABLED.name(), doActivateAppLock);
                                    if (doActivateAppLock) {
                                        showMessageInfo(mBase, "App lock has been successfully Activated.");
                                    } else {
                                        SharedPreferenceStorage.setStringValue(mBase, Constants.Storage.APP_LOCK_PASSWORD.name(), Constants.APP_LOCK_BLANK_PASSWORD);
                                        showMessageInfo(mBase, "App lock has been successfully Deactivated.");
                                    }
                                    dialog.cancel();
                                    if (mOnAppLockListener != null)
                                        mOnAppLockListener.getAppLockStatus(doActivateAppLock);
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = changeAppDialog.create();
                    if (!mBase.isFinishing())
                        alertDialog.show();

                    Button negativeBtn = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                    if (negativeBtn != null) {
                        negativeBtn.setBackgroundResource(R.drawable.border_for_focus);
                    }

                    Button positiveBtn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    if (positiveBtn != null) {
                        positiveBtn.setBackgroundResource(R.drawable.border_for_focus);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static void showMessageInfo(final BaseActivity mBase, final String descriptionTxt) {
        mBase.runOnUiThread(new Thread() {
            public void run() {
                try {
                    final AlertDialog.Builder customeDialog = new AlertDialog.Builder(mBase);
                    customeDialog
                            .setMessage(descriptionTxt)
                            .setCancelable(true)
                            /*.setPositiveButton(mBase.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })*/;
                    final AlertDialog alertDialog = customeDialog.create();
                    if (!mBase.isFinishing())
                        alertDialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (alertDialog != null)
                                alertDialog.dismiss();
                        }
                    }, 1400);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        boolean isAppInstalled = false;
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            isAppInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
        }
        if (packageName.equalsIgnoreCase("ALL_APPS_ACTIVITY") || packageName.equalsIgnoreCase("com.android.tv.settings")) {
            return true;
        } else {
            return isAppInstalled;
        }
    }

    public static void goAppDetailsDeviceSettingsScreen(BaseActivity mBase, String appPackageName) {
        try {
            Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + appPackageName));
            myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
            myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mBase.startActivity(myAppSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void doPlayVideo(BaseActivity mBase, HelpInfoOrAdsButtonInfo mHelpInfoOrAdsButtonInfo) {
        try {
            Uri uri = Uri.parse(mHelpInfoOrAdsButtonInfo.getHelpFileUrl()); //your file URI
            Intent vlcIntent = new Intent(Intent.ACTION_VIEW);
            vlcIntent.setPackage("org.videolan.vlc");
            vlcIntent.setDataAndTypeAndNormalize(uri, "video/*");
            vlcIntent.putExtra("title", mHelpInfoOrAdsButtonInfo.getTitle());
            vlcIntent.putExtra("from_start", true);
            mBase.startActivity(vlcIntent);
        } catch (Exception e) {
            new DownloadNewUpdate(mBase, mHelpInfoOrAdsButtonInfo.getBtnId().concat(Constants.UpdatableAppNameKey._tutorial.name()),0,0, mHelpInfoOrAdsButtonInfo.getTutorialType(), null).execute(mHelpInfoOrAdsButtonInfo.getHelpFileUrl());
            e.printStackTrace();
        }
    }

    public interface OnSettingsLikAddListener {
        void onLinkAddStatus(boolean isLoaded);
    }

    public static void showSettingsFileLinkAddingDialog(final BaseActivity mBase, final OnSettingsLikAddListener mOnSettingsLikAddListener) {
        if (HelperUtils.haveNetworkConnection(mBase) > 0) {
            mBase.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final AlertDialog[] alertDialog = {null};
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mBase);
                        View dialogView = mBase.getLayoutInflater().inflate(R.layout.text_inpu_settings_url, null);
                        dialogBuilder.setView(dialogView);
                        final EditText editText = (EditText) dialogView.findViewById(R.id.etUrl);
                        editText.setHint("https://...");
                        final TextView tvSubmitBtn = dialogView.findViewById(R.id.tvPwdBtn);
                        tvSubmitBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final String input = editText.getText().toString().trim();
                                alertDialog[0].dismiss();
                                if (input.length() > 0 && (input.startsWith("http://") || input.startsWith("https://"))) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                JSONObject jsonObject = new JSONObject(DownloadImage.convertStreamToString(new URL(input).openConnection().getInputStream()));
                                                System.out.println("!--!@jsonObject = " + jsonObject);
                                                boolean isRightFile = false;
                                                if (jsonObject != null
                                                        && jsonObject.length() > 0
                                                        && jsonObject.has(Constants.UpdatableAppNameKey.button_appids.name())
                                                        && jsonObject.has(Constants.UpdatableAppNameKey.buttons_images.name())
                                                        && jsonObject.has(Constants.UpdatableAppNameKey.button_tutorial.name())
                                                        && jsonObject.has(Constants.UpdatableAppNameKey.background_image.name())
                                                        && jsonObject.has(Constants.UpdatableAppNameKey.help_background_image.name())) {
                                                    SharedPreferenceStorage.setStringValue(mBase, Constants.Storage.UPDATE_DETAILS_SETTINGS_URL.name(), Constants.SETTINGS_URL_BOX2);
                                                    isRightFile = true;
                                                }
                                                if (mOnSettingsLikAddListener != null)
                                                    mOnSettingsLikAddListener.onLinkAddStatus(isRightFile);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                if (mOnSettingsLikAddListener != null)
                                                    mOnSettingsLikAddListener.onLinkAddStatus(false);
                                            }
                                        }
                                    }).start();
                                } else {
                                    showMessageInfo(mBase, "Please enter valid url... Url must start with http:// or https://");
                                }
                            }
                        });

                        tvSubmitBtn.setOnKeyListener(new View.OnKeyListener() {
                            @Override
                            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
                                    tvSubmitBtn.performClick();
                                    return true;
                                }
                                return false;
                            }
                        });
                        alertDialog[0] = dialogBuilder.create();
                        alertDialog[0].setCancelable(true);
                        alertDialog[0].show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            showMessageInfo(mBase, "No internet connection!");
        }
    }

    public static Bitmap getTransparentBitMap(Bitmap bit) {
        int width = bit.getWidth();
        int height = bit.getHeight();
        Bitmap myBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int[] allpixels = new int[myBitmap.getHeight() * myBitmap.getWidth()];
        bit.getPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
        myBitmap.setPixels(allpixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < myBitmap.getHeight() * myBitmap.getWidth(); i++) {
            allpixels[i] = Color.alpha(Color.TRANSPARENT);
        }

        myBitmap.setPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
        return myBitmap;
    }

    public static Bitmap addRedBorder(Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, source.getConfig());
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setStrokeWidth(HelperUtils.getHorizontalRatio(1));
        paint.setColor(Color.parseColor("#D50000"));

        canvas.drawLine(0, 0, width, 0, paint);
        canvas.drawLine(width, 0, width, height, paint);
        canvas.drawLine(width, height, 0, height, paint);
        canvas.drawLine(0, height, 0, 0, paint);
        canvas.drawBitmap(source, 0, 0, null);

        return bitmap;
    }


    public static void launchNetworkSettings(BaseActivity mBase) {
        /*try {
            final Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            final ComponentName cn = new ComponentName("com.android.tv.settings", "android.settings.WIRELESS_SETTINGS");
            intent.setComponent(cn);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mBase.startActivity(intent);
        } catch (Exception e) */
        {
            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
            mBase.startActivity(intent);
        }
    }

}
