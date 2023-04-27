package com.zvision.zlaunchertwo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zvision.zlaunchertwo.base.BaseActivity;
import com.zvision.zlaunchertwo.base.Constants;
import com.zvision.zlaunchertwo.base.HelperUtils;
import com.zvision.zlaunchertwo.part.DownloadImage;
import com.zvision.zlaunchertwo.part.DownloadNewUpdate;
import com.zvision.zlaunchertwo.part.HelpInfoOrAdsButtonInfo;
import com.zvision.zlaunchertwo.part.ImageManager;
import com.zvision.zlaunchertwo.part.LongPressOptionDialog;
import com.zvision.zlaunchertwo.part.LongPressTopOptionsDilaog;
import com.zvision.zlaunchertwo.part.ParentalControlDialog;
import com.zvision.zlaunchertwo.root_utils.APKManager;
import com.zvision.zlaunchertwo.root_utils.RootTask;
import com.zvision.zlaunchertwo.shared_storage.SharedPreferenceStorage;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;
import com.zvision.zlaunchertwo.support.Version;

import org.json.JSONArray;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, View.OnFocusChangeListener {

    private static final String TAG = "HomeActivity";
    private ImageView ivButtonTop, ivButtonTopNew, ivButton4, ivButton6, ivButton3, ivButton7, ivButton8,
            ivButton1, ivButton9, ivButton10, ivButton5, ivButton11, ivWifiState, ivAddRemoveBtn, ivVpnBtn,
            ivLanState, ivBlueState, ivBgBackground, ivButton2, ivPoweBtn, ivAppLockBtn, ivHelpBtn,
            ivButton1_up_req, ivButton2_up_req, ivButton3_up_req, ivButton4_up_req, ivButton5_up_req, ivButton6_up_req, ivButton7_up_req, ivButton8_up_req, ivButton9_up_req, ivButton10_up_req, ivButton11_up_req;
    private RecyclerView rvDockItemRecyclerVew;
    public TextView tvDownloadProgress;
    public TextView btn1_appname, btn2_appname, btn3_appname, btn4_appname, btn5_appname, btn6_appname, btn7_appname, btn8_appname, btn9_appname, btn10_appname, btn11_appname;
    private View __updatingAppViewDuringPermissionRequest = null;

    private AlertDialog.Builder changeAppDialog;
    private boolean isRefresh = true;
    ImageView no_internet, news_btn;
    private ShimmerFrameLayout sfDefIconContainer;
    private LongPressOptionDialog mLongPressOptionDialog;

    private LongPressTopOptionsDilaog mLongPressTopOptionDialog;
    private ParentalControlDialog mParentalControlDialog;
    private WebView mWebViewHtmlRssFeedShort;
    private RelativeLayout rlWebViewRssContainer;
    ImageView btn_left_top;
    ImageView btn_left_top_right;
    RelativeLayout btn_left_settings;
    ImageView btn_left1;
    ImageView btn_right1;
    ImageView btn_right2;
    ImageView btn_right3;
    ImageView btn_left2;
    ImageView btn_left3;
//    Dialog dialog;

    TextView run_apk, play_vlc, show_png, open_chrome;

    ImageView ivUpdateBtn;
    ImageView ivMiniButton1, ivMiniButton2, ivMiniButton3, ivMiniButton4, ivMiniButton5, ivMiniButton6,
            ivMiniButton7, ivMiniButton8, ivMiniButton9, ivMiniButton10, ivMiniButton11, ivMiniButton12, ivMiniButton13;
    ProgressDialog pd;
    int time_for_refresh_time;
    int time;
    Boolean is_show = false;
    Handler handler;
    Runnable r;

    ProgressDialog p1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        p1 = new ProgressDialog(HomePageActivity.this);
        p1.setMessage("Refreshing...");
        p1.setCancelable(true);
        p1.setCancelable(false);
        p1.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                p1.dismiss();//dismiss dialog

            }
        });


        pd = new ProgressDialog(HomePageActivity.this);


//        dialog = new Dialog(HomePageActivity.this);
//
//        dialog.setContentView(R.layout.single_press_new_dialog);

        sfDefIconContainer = findViewById(R.id.sfDefIconConainer);
        rlWebViewRssContainer = findViewById(R.id.rlWebViewRssContainer);
        rlWebViewRssContainer.setOnFocusChangeListener(this);
        rlWebViewRssContainer.setOnClickListener(this);
        RelativeLayout rlWebViewRssClickBtn = findViewById(R.id.rlWebViewRssClickBtn);
        rlWebViewRssClickBtn.setOnFocusChangeListener(this);
        rlWebViewRssClickBtn.setOnClickListener(this);
        mWebViewHtmlRssFeedShort = findViewById(R.id.webviewRss);
        mWebViewHtmlRssFeedShort.setFocusable(false);
        mWebViewHtmlRssFeedShort.setClickable(false);
        ivBgBackground = findViewById(R.id.ivBackgroundImage);
        rvDockItemRecyclerVew = findViewById(R.id.rv_dock_item_recyclerview);
        ivPoweBtn = findViewById(R.id.ivPoweBtn);
        ivPoweBtn.setOnClickListener(this);
        ivPoweBtn.setOnLongClickListener(this);
        ivPoweBtn.setOnFocusChangeListener(this);

        ivVpnBtn = findViewById(R.id.ivVpnBtn);
        ivVpnBtn.setOnClickListener(this);
        ivVpnBtn.setOnLongClickListener(this);
        ivVpnBtn.setOnFocusChangeListener(this);


        ivHelpBtn = findViewById(R.id.ivHelpBtn);
        ivHelpBtn.setOnClickListener(this);
        ivHelpBtn.setOnLongClickListener(this);
        ivHelpBtn.setOnFocusChangeListener(this);

        btn_left_settings = findViewById(R.id.btn_settings);
        btn_left_settings.setOnClickListener(this);
        btn_left_settings.setOnLongClickListener(this);
        btn_left_settings.setOnFocusChangeListener(this);

        ivAppLockBtn = findViewById(R.id.ivAppLockBtn);
        ivAppLockBtn.setOnClickListener(this);
        ivAppLockBtn.setOnFocusChangeListener(this);
        ivAppLockBtn.setOnLongClickListener(this);

        ivWifiState = findViewById(R.id.ivWifiState);
        ivWifiState.setOnClickListener(this);
        ivWifiState.setOnFocusChangeListener(this);
        ivWifiState.setOnLongClickListener(this);

        ivBlueState = findViewById(R.id.ivBluetoothState);
        ivBlueState.setOnClickListener(this);
        ivBlueState.setOnFocusChangeListener(this);
        ivBlueState.setOnLongClickListener(this);

        ivUpdateBtn = findViewById(R.id.ivUpdateBtn);
        ivUpdateBtn.setOnClickListener(this);
        ivUpdateBtn.setOnFocusChangeListener(this);

        ivLanState = findViewById(R.id.ivLanState);
        ivLanState.setOnClickListener(this);
        ivLanState.setOnFocusChangeListener(this);
//        ivLanState.setOnLongClickListener(this);

        RelativeLayout rlTextClock = findViewById(R.id.rlTextClock);
        rlTextClock.setOnClickListener(this);

        ivButton1_up_req = findViewById(R.id.btn1_update_req);
        ivButton2_up_req = findViewById(R.id.btn2_update_req);
        ivButton3_up_req = findViewById(R.id.btn3_update_req);
        ivButton4_up_req = findViewById(R.id.btn4_update_req);
        ivButton5_up_req = findViewById(R.id.btn5_update_req);
        ivButton6_up_req = findViewById(R.id.btn6_update_req);
        ivButton7_up_req = findViewById(R.id.btn7_update_req);
        ivButton8_up_req = findViewById(R.id.btn8_update_req);
        ivButton9_up_req = findViewById(R.id.btn9_update_req);
        ivButton10_up_req = findViewById(R.id.btn10_update_req);
        ivButton11_up_req = findViewById(R.id.btn11_update_req);


        btn1_appname = findViewById(R.id.btn1_appname);
        btn2_appname = findViewById(R.id.btn2_appname);
        btn3_appname = findViewById(R.id.btn3_appname);
        btn4_appname = findViewById(R.id.btn4_appname);
        btn5_appname = findViewById(R.id.btn5_appname);
        btn6_appname = findViewById(R.id.btn6_appname);
        btn7_appname = findViewById(R.id.btn7_appname);
        btn8_appname = findViewById(R.id.btn8_appname);
        btn9_appname = findViewById(R.id.btn9_appname);
        btn10_appname = findViewById(R.id.btn10_appname);
        btn11_appname = findViewById(R.id.btn11_appname);


        ivButton1 = findViewById(R.id.ivButton1);
        ivButton1.setOnClickListener(this);
        ivButton1.setOnLongClickListener(this);
        ivButton1.setOnFocusChangeListener(this);

        ivButton2 = findViewById(R.id.ivButton2);
        ivButton2.setOnClickListener(this);
        ivButton2.setOnLongClickListener(this);
        ivButton2.setOnFocusChangeListener(this);

        ivButton3 = findViewById(R.id.ivButton3);
        ivButton3.setOnLongClickListener(this);
        ivButton3.setOnClickListener(this);
        ivButton3.setOnFocusChangeListener(this);

        ivButton4 = findViewById(R.id.ivButton4);
        ivButton4.setOnClickListener(this);
        ivButton4.setOnLongClickListener(this);
        ivButton4.setOnFocusChangeListener(this);


        no_internet = findViewById(R.id.ivButtonTop_noitnternet);


        ivButtonTop = findViewById(R.id.ivButtonTop);
        ivButtonTop.setOnClickListener(this);
        ivButtonTop.setOnLongClickListener(this);
        ivButtonTop.setOnFocusChangeListener(this);


        news_btn = findViewById(R.id.news_btn);
        news_btn.setOnClickListener(this);
        news_btn.setOnFocusChangeListener(this);

        ivButton5 = findViewById(R.id.ivButton5);
        ivButton5.setOnClickListener(this);
        ivButton5.setOnLongClickListener(this);
        ivButton5.setOnFocusChangeListener(this);

        ivButton6 = findViewById(R.id.ivButton6);
        ivButton6.setOnClickListener(this);
        ivButton6.setOnLongClickListener(this);
        ivButton6.setOnFocusChangeListener(this);

        ivButton7 = findViewById(R.id.ivButton7);
        ivButton7.setOnClickListener(this);
        ivButton7.setOnLongClickListener(this);
        ivButton7.setOnFocusChangeListener(this);

        ivButton8 = findViewById(R.id.ivButton8);
        ivButton8.setOnClickListener(this);
        ivButton8.setOnLongClickListener(this);
        ivButton8.setOnFocusChangeListener(this);

        ivButton9 = findViewById(R.id.ivButton9);
        ivButton9.setOnClickListener(this);
        ivButton9.setOnLongClickListener(this);
        ivButton9.setOnFocusChangeListener(this);

        ivButton10 = findViewById(R.id.ivButton10);
        ivButton10.setOnClickListener(this);
        ivButton10.setOnLongClickListener(this);
        ivButton10.setOnFocusChangeListener(this);

        ivButton11 = findViewById(R.id.ivButton11);
        ivButton11.setOnClickListener(this);
        ivButton11.setOnLongClickListener(this);
        ivButton11.setOnFocusChangeListener(this);

        ivAddRemoveBtn = findViewById(R.id.ivBottomAddRemoveBtn);
        ivAddRemoveBtn.setOnClickListener(this);
        ivAddRemoveBtn.setOnLongClickListener(this);
        ivAddRemoveBtn.setOnFocusChangeListener(this);
        tvDownloadProgress = findViewById(R.id.tvDownloadProgress);


        btn_left_top = findViewById(R.id.btn_left_top);
        btn_left_top.setOnClickListener(this);
        btn_left_top.setOnFocusChangeListener(this);
        btn_left_top.setOnLongClickListener(this);

        btn_left_top_right = findViewById(R.id.btn_left_top_right);
        btn_left_top_right.setOnClickListener(this);
        btn_left_top_right.setOnFocusChangeListener(this);
        btn_left_top_right.setOnLongClickListener(this);

        btn_left1 = findViewById(R.id.btn_left1);
        btn_left1.setOnClickListener(this);
        btn_left1.setOnFocusChangeListener(this);
        btn_left1.setOnLongClickListener(this);


        btn_left2 = findViewById(R.id.btn_left2);
        btn_left2.setOnClickListener(this);
        btn_left2.setOnFocusChangeListener(this);
        btn_left2.setOnLongClickListener(this);


        btn_left3 = findViewById(R.id.btn_left3);
        btn_left3.setOnClickListener(this);
        btn_left3.setOnFocusChangeListener(this);
        btn_left3.setOnLongClickListener(this);

        ivMiniButton1 = findViewById(R.id.ivMiniButton1);
        ivMiniButton1.setOnClickListener(this);
        ivMiniButton1.setOnLongClickListener(this);
        ivMiniButton1.setOnFocusChangeListener(this);

        ivMiniButton2 = findViewById(R.id.ivMiniButton2);
        ivMiniButton2.setOnClickListener(this);
        ivMiniButton2.setOnLongClickListener(this);
        ivMiniButton2.setOnFocusChangeListener(this);

        ivMiniButton3 = findViewById(R.id.ivMiniButton3);
        ivMiniButton3.setOnClickListener(this);
        ivMiniButton3.setOnLongClickListener(this);
        ivMiniButton3.setOnFocusChangeListener(this);

        ivMiniButton4 = findViewById(R.id.ivMiniButton4);
        ivMiniButton4.setOnClickListener(this);
        ivMiniButton4.setOnLongClickListener(this);
        ivMiniButton4.setOnFocusChangeListener(this);

        ivMiniButton5 = findViewById(R.id.ivMiniButton5);
        ivMiniButton5.setOnClickListener(this);
        ivMiniButton5.setOnLongClickListener(this);
        ivMiniButton5.setOnFocusChangeListener(this);

        ivMiniButton6 = findViewById(R.id.ivMiniButton6);
        ivMiniButton6.setOnClickListener(this);
        ivMiniButton6.setOnLongClickListener(this);
        ivMiniButton6.setOnFocusChangeListener(this);

        ivMiniButton7 = findViewById(R.id.ivMiniButton7);
        ivMiniButton7.setOnClickListener(this);
        ivMiniButton7.setOnLongClickListener(this);
        ivMiniButton7.setOnFocusChangeListener(this);

        ivMiniButton8 = findViewById(R.id.ivMiniButton8);
        ivMiniButton8.setOnClickListener(this);
        ivMiniButton8.setOnLongClickListener(this);
        ivMiniButton8.setOnFocusChangeListener(this);

        ivMiniButton9 = findViewById(R.id.ivMiniButton9);
        ivMiniButton9.setOnClickListener(this);
        ivMiniButton9.setOnLongClickListener(this);
        ivMiniButton9.setOnFocusChangeListener(this);

        ivMiniButton10 = findViewById(R.id.ivMiniButton10);
        ivMiniButton10.setOnClickListener(this);
        ivMiniButton10.setOnLongClickListener(this);
        ivMiniButton10.setOnFocusChangeListener(this);

        ivMiniButton11 = findViewById(R.id.ivMiniButton11);
        ivMiniButton11.setOnClickListener(this);
        ivMiniButton11.setOnLongClickListener(this);
        ivMiniButton11.setOnFocusChangeListener(this);

        ivMiniButton12 = findViewById(R.id.ivMiniButton12);
        ivMiniButton12.setOnClickListener(this);
        ivMiniButton12.setOnLongClickListener(this);
        ivMiniButton12.setOnFocusChangeListener(this);


        btn_right1 = findViewById(R.id.btn_right1);
        btn_right1.setOnClickListener(this);
        btn_right1.setOnLongClickListener(this);
        btn_right1.setOnFocusChangeListener(this);


        btn_right2 = findViewById(R.id.btn_right2);
        btn_right2.setOnClickListener(this);
        btn_right2.setOnLongClickListener(this);
        btn_right2.setOnFocusChangeListener(this);


        btn_right3 = findViewById(R.id.btn_right3);
        btn_right3.setOnClickListener(this);
        btn_right3.setOnLongClickListener(this);
        btn_right3.setOnFocusChangeListener(this);

//        p1.show();

        if (sfDefIconContainer != null)
            sfDefIconContainer.startShimmer();
    }

    @Override
    public void init() {
        if (!isNetworkConnected()) {

            Log.i(TAG, "init: yes");
            ivButtonTop.setVisibility(View.INVISIBLE);
            no_internet.setVisibility(View.VISIBLE);
            no_internet.bringToFront();
            no_internet.setBackgroundResource(R.drawable.internet_offline);

        } else {

            ivButtonTop.setVisibility(View.VISIBLE);
            no_internet.setVisibility(View.GONE);
            Log.i(TAG, "init: no");
        }

        if (checkPermission()) {
            // Your Permission granted already .Do next code

        } else {
            requestPermission(null); // for auto Update view = null
        }
        String push_launcher_update = SharedPreferenceStorage.getStringValue(this, "push_launcher_update", "");


        if (push_launcher_update.equalsIgnoreCase("y")) {
            new DownloadNewUpdate(HomePageActivity.this, AppName.zLauncher.name(), 0, 0, Constants.FILE_TYPE.apk, null).execute(Constants.UPDATE_ZLAUNCHER_URL);

        }
        String startupvideo_status = SharedPreferenceStorage.getStringValue(this, "startupvideo_status", "");
        String isshow = SharedPreferenceStorage.getStringValue(this, "isshow", "");
        String link = SharedPreferenceStorage.getStringValue(this, "link", "");
        int video_count = SharedPreferenceStorage.getIntegerValue(this, "video_count", 0);
        if (isshow.equalsIgnoreCase("yes") && video_count < Integer.parseInt(startupvideo_status)) {
            SharedPreferenceStorage.setIntegerValue(this, "video_count", video_count + 1);
            showVideoDialog(link);
        }


        Log.i(TAG, "init: " + SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button0", ""));


        String lengt = SharedPreferenceStorage.getStringValue(this, "delete_pkg_length", "");
        if (!lengt.equals("")) {
            Log.i(TAG, "init: dodeleteapps" + lengt);
            for (int i = 0; i < Integer.parseInt(lengt); i++) {
                String appPackageName = SharedPreferenceStorage.getStringValue(this, i + "", "");
                Log.i(TAG, "init: dodeleteapps" + appPackageName);
                if (HelperUtils.isAppInstalled(HomePageActivity.this, appPackageName)) {

                    doDeleteApps(appPackageName);
                    setOrRefreshAppIcons(appPackageName);

                }
            }
        }

        if (isRefresh) {
            mWebViewHtmlRssFeedShort.setBackgroundColor(Color.TRANSPARENT);
            if (DownloadImage.isAllImageExistInStorage(this)) {
                getHomePageMainApps();
            }
            doFetchAndUpdateAppIdAndIcons();
            checkAndRetryBackgroundImageAndRss();

            mLongPressOptionDialog = new LongPressOptionDialog(this);
            mLongPressTopOptionDialog = new LongPressTopOptionsDilaog(this);
            mParentalControlDialog = new ParentalControlDialog(this);
            showAndUpdateBackgroundImage();
        }

        getHomePageDockApps();
        doSetOrRefreshAllAppIcons();
//        setNetworkIcon();
        setMiniAndAdIcons();
        compareAppVersion();

        if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "idlerefreshtime", "").equals("")) {
            time_for_refresh_time = 15;
        } else {
            time_for_refresh_time = Integer.parseInt(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "idlerefreshtime", "15"));
        }
        handler = new Handler();
        r = new Runnable() {


            @Override
            public void run() {
                // TODO Auto-generated method stub
//                Toast.makeText(HomePageActivity.this, "user is inactive from last 5 minutes",Toast.LENGTH_SHORT).show();
                refreshapps();
            }
        };
        startHandler();
    }

    private void setMiniAndAdIcons() {
        Log.i(TAG, "setMiniAndAdIcons: "+Constants.icon_minibutton1);
        ivMiniButton1.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton1.name())));
        ivMiniButton2.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton2.name())));
        ivMiniButton3.setImageDrawable(new BitmapDrawable(getResources(), Constants.icon_minibutton3));
        ivMiniButton4.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton4.name())));
        ivMiniButton5.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton5.name())));
        ivMiniButton6.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton6.name())));
        ivMiniButton7.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton7.name())));
        ivUpdateBtn.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton8.name())));
        ivMiniButton9.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton9.name())));
        ivMiniButton10.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton10.name())));
        ivMiniButton11.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton11.name())));
        ivMiniButton12.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton12.name())));
        news_btn.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton13.name())));


        if (isNetworkConnected()){
            ivWifiState.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_minibutton1.name())));
            no_internet.setVisibility(View.GONE);
            ivButtonTop.setVisibility(View.VISIBLE);
        }else{
            ivWifiState.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton1.name())));
            ivButtonTop.setVisibility(View.INVISIBLE);
            no_internet.setVisibility(View.VISIBLE);
            no_internet.bringToFront();
            no_internet.setBackgroundResource(R.drawable.internet_offline);
        }
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Log.i(TAG, "run: Bluetooth  not supported");

            ivBlueState.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton4.name())));
            // Device does not support Bluetooth
        } else if (!mBluetoothAdapter.isEnabled()) {
            // Bluetooth is not enabled :)
            Log.i(TAG, "run: Bluetooth  not enabled");

            ivBlueState.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton4.name())));
        } else {
            Log.i(TAG, "run: Bluetooth  enabled");
            // Bluetooth is enabled

            ivBlueState.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_minibutton4.name())));
        }
        if (HelperUtils.haveNetworkConnection(HomePageActivity.this) == 2) {
            ivMiniButton3.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_minibutton3.name())));


        } else {
            ivMiniButton3.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton3.name())));

        }
        if (HelperUtils.checkNetworkStatus(HomePageActivity.this, 2)) {


            ivLanState.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_minibutton2.name())));
            no_internet.setVisibility(View.GONE);
            ivButtonTop.setVisibility(View.VISIBLE);
        } else {
            Log.i(TAG, "init: back press yes");
            if (!isNetworkConnected()) {
                ivButtonTop.setVisibility(View.INVISIBLE);
                no_internet.setVisibility(View.VISIBLE);
                no_internet.bringToFront();
                no_internet.setBackgroundResource(R.drawable.internet_offline);

            } else {
                no_internet.setVisibility(View.GONE);
                ivButtonTop.setVisibility(View.VISIBLE);
            }

            ivLanState.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton2.name())));

        }
//        setNetworkIcon();

//        ivMiniButton13.setBackground(new BitmapDrawable(getResources(), Constants.icon_minibutton13));

//        if (!SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_update_button", "").equals("")) {
//            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_update_button", "")).into(ivUpdateBtn);
//        }
//        if (!SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton1", "").equals("")) {
//            Log.i(TAG, "init: minicion if" + SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton1", ""));
//            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton1", "")).into(ivWifiState);
//        } else {
//            Log.i(TAG, "init: minicion else" + SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton6", ""));
//        }
//        if (!SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton2", "").equals("")) {
//            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton2", "")).into(ivLanState);
//        }
//        if (!SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton3", "").equals("")) {
//            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton3", "")).into(ivMiniButton3);
//        }
//        if (!SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton4", "").equals("")) {
//            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton4", "")).into(ivBlueState);
//        }
//        if (!SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton5", "").equals("")) {
//            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton5", "")).into(ivAppLockBtn);
//        }
//        if (!SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton6", "").equals("")) {
//            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton6", "")).into(ivMiniButton6);
//        }
//        if (!SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton7", "").equals("")) {
//            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton7", "")).into(ivMiniButton7);
//        }
//        if (!SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton8", "").equals("")) {
//            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton8", "")).into(ivUpdateBtn);
//        }
//        if (!SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton9", "").equals("")) {
//            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton9", "")).into(ivMiniButton9);
//        }
//        if (!SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton10", "").equals("")) {
//            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton10", "")).into(ivMiniButton10);
//        }
//        if (!SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton11", "").equals("")) {
//            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton11", "")).into(ivMiniButton11);
//        }
//        if (!SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton12", "").equals("")) {
//            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton12", "")).into(ivMiniButton12);
//        }
//        if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button0", "").equals("")) {
//
//        } else {
//            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button0", "")).into(ivButtonTop);
//        }
//        if (!SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left_top", "").equals("")) {
//
//            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left_top", "")).into(btn_left_top);
//        }
//        if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left_top_right", "").equals("")) {
//
//        } else {
//
//            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left_top_right", "")).into(btn_left_top_right);
//        }
//        if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left1", "").equals("")) {
//
//        } else {
//
//            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left1", "")).into(btn_left1);
//        }
//        if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left2", "").equals("")) {
//
//        } else {
//
//            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left2", "")).into(btn_left2);
//        }
//        if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left3", "").equals("")) {
//
//        } else {
//
//            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left3", "")).into(btn_left3);
//        }
//        if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_right1", "").equals("")) {
//
//        } else {
//
//            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_right1", "")).into(btn_right1);
//        }
//        if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_right2", "").equals("")) {
//
//        } else {
//
//            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_right2", "")).into(btn_right2);
//        }
//        if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_right3", "").equals("")) {
//
//        } else {
//
//            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_right3", "")).into(btn_right3);
//        }
//        if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton13", "").equals("")) {
//
//        } else {
//
//            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton13", "")).into(news_btn);
//
//        }
        pd.dismiss();
    }

    private void showVideoDialog(String link) {
        final AlertDialog[] alertDialog = {null};
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.videoview, null);
        dialogBuilder.setView(dialogView);
        VideoView videoView = dialogView.findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse(link));
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                alertDialog[0].dismiss();
            }
        });


        alertDialog[0] = dialogBuilder.create();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog[0].getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        alertDialog[0].show();
        alertDialog[0].getWindow().setAttributes(lp);
        alertDialog[0].setCancelable(true);
        alertDialog[0].show();


    }

    private void doDeleteApps(String packageName) {

//        startActivity(new Intent(Intent.ACTION_UNINSTALL_PACKAGE, Uri.parse("package:" + packageName));

        if (packageName == null || packageName.isEmpty()) {

            Log.i(TAG, "doDeleteApps: " + packageName);

        } else {
            Log.i(TAG, "doDeleteApps: " + packageName);
            boolean success = false;
            final Uri uri = Uri.fromParts("package", packageName, null);
            final Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, uri);
            try {//  ww w.jav a  2  s  . co m
                startActivity(uninstallIntent);
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    private void showAndUpdateBackgroundImage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    ivBgBackground.setImageBitmap(ImageManager.getImgFromFiles(HomePageActivity.this, Constants.IMAGE_NAME));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Timer mTimer;

    private void checkAndRetryBackgroundImageAndRss() {
        try {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (Constants.IS_FIRST_LAUNCH_SETTINGS_FETCH_DONE) {
                        if (mTimer != null) {
                            mTimer.cancel();
                            mTimer.cancel();
                        }
                    } else {
                        doFetchAndUpdateAppIdAndIcons();
                    }
                }
            }, 10000, 10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getHomePageDockApps() {
        try {
            String allSavedPackage = SharedPreferenceStorage.getStringValue(this, Constants.Storage.HOMEPAGE_CUSTOM_APP.name(), null);
            if (allSavedPackage != null) {
                String[] packgeArray = allSavedPackage.split(Constants.STRING_SPLITTER);
                app.getHomePageDockAppList().clear();
                for (String appPackage : packgeArray) {
                    app.getHomePageDockAppList().add(appPackage);
                }

                DockAppListAdapter dockAppListAdapter = new DockAppListAdapter(this);
                rvDockItemRecyclerVew.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                rvDockItemRecyclerVew.setAdapter(dockAppListAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onFocusChange(final View view, final boolean isFocused) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    switch (view.getId()) {
                        case R.id.ivButtonTop:
                            if (SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "0" + Constants.Storage._isAppHidden.name(), false) || SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "0" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                                ivButtonTop.setBackground(getResources().getDrawable(R.drawable.invisible_selector));
                            } else {
                                if (isFocused) {
                                    Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button0", "")).into(ivButtonTop);
                                    Log.i(TAG, "run app icon: " + Constants.icon_button_top);

                                } else {
                                    Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button0", "")).into(ivButtonTop);
                                }
                            }
                            break;
                        case R.id.ivMiniButton1:
                            if (isFocused) {
                                ivMiniButton1.setBackgroundResource(R.drawable.border_red);
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton1", "")).into(ivMiniButton1);

                            } else {
                                ivMiniButton1.setBackgroundColor(getResources().getColor(R.color.transparent));
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_pressed_minibutton1", "")).into(ivMiniButton1);
                            }

                            break;
                        case R.id.ivMiniButton2:
                            if (isFocused) {
                                ivMiniButton2.setBackgroundResource(R.drawable.border_red);
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton2", "")).into(ivMiniButton2);

                            } else {
                                ivMiniButton2.setBackgroundColor(getResources().getColor(R.color.transparent));
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_pressed_minibutton2", "")).into(ivMiniButton2);
                            }

                            break;
                        case R.id.ivMiniButton3:
                            if (isFocused) {
                                ivMiniButton3.setBackgroundResource(R.drawable.border_red);
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton3", "")).into(ivMiniButton3);

                            } else {
                                ivMiniButton3.setBackgroundColor(getResources().getColor(R.color.transparent));
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_pressed_minibutton3", "")).into(ivMiniButton3);
                            }

                            break;
                        case R.id.ivMiniButton4:
                            if (isFocused) {
                                ivMiniButton4.setBackgroundResource(R.drawable.border_red);
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton4", "")).into(ivMiniButton4);

                            } else {
                                ivMiniButton4.setBackgroundColor(getResources().getColor(R.color.transparent));
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_pressed_minibutton4", "")).into(ivMiniButton4);
                            }

                            break;
                        case R.id.ivMiniButton5:
                            if (isFocused) {
                                ivMiniButton5.setBackgroundResource(R.drawable.border_red);
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton5", "")).into(ivMiniButton5);

                            } else {
                                ivMiniButton5.setBackgroundColor(getResources().getColor(R.color.transparent));
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_pressed_minibutton5", "")).into(ivMiniButton5);
                            }

                            break;
                        case R.id.ivMiniButton6:
                            if (isFocused) {
                                ivMiniButton6.setBackgroundResource(R.drawable.border_red);
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton6", "")).into(ivMiniButton6);

                            } else {
                                ivMiniButton6.setBackgroundColor(getResources().getColor(R.color.transparent));
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_pressed_minibutton6", "")).into(ivMiniButton6);
                            }

                            break;
                        case R.id.ivMiniButton7:
                            if (isFocused) {
                                ivMiniButton7.setBackgroundResource(R.drawable.border_red);
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton7", "")).into(ivMiniButton7);

                            } else {
                                ivMiniButton7.setBackgroundColor(getResources().getColor(R.color.transparent));
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_pressed_minibutton7", "")).into(ivMiniButton7);
                            }

                            break;
                        case R.id.ivMiniButton8:
                            if (isFocused) {
                                ivMiniButton8.setBackgroundResource(R.drawable.border_red);
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton8", "")).into(ivMiniButton8);

                            } else {
                                ivMiniButton8.setBackgroundColor(getResources().getColor(R.color.transparent));
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_pressed_minibutton8", "")).into(ivMiniButton8);
                            }

                            break;
                        case R.id.ivMiniButton9:
                            if (isFocused) {
                                ivMiniButton9.setBackgroundResource(R.drawable.border_red);
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton9", "")).into(ivMiniButton9);

                            } else {
                                ivMiniButton9.setBackgroundColor(getResources().getColor(R.color.transparent));
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_pressed_minibutton9", "")).into(ivMiniButton9);
                            }

                            break;
                        case R.id.ivMiniButton10:
                            if (isFocused) {
                                ivMiniButton10.setBackgroundResource(R.drawable.border_red);
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton10", "")).into(ivMiniButton10);

                            } else {
                                ivMiniButton10.setBackgroundColor(getResources().getColor(R.color.transparent));
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_pressed_minibutton10", "")).into(ivMiniButton10);
                            }

                            break;
                        case R.id.ivMiniButton11:
                            if (isFocused) {
                                ivMiniButton11.setBackgroundResource(R.drawable.border_red);
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton11", "")).into(ivMiniButton11);

                            } else {
                                ivMiniButton11.setBackgroundColor(getResources().getColor(R.color.transparent));
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_pressed_minibutton11", "")).into(ivMiniButton11);
                            }

                            break;
                        case R.id.ivMiniButton12:
                            if (isFocused) {

                                ivMiniButton12.setBackgroundResource(R.drawable.border_red);
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton12", "")).into(ivMiniButton12);

                            } else {
                                ivMiniButton12.setBackgroundColor(getResources().getColor(R.color.transparent));
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_pressed_minibutton12", "")).into(ivMiniButton12);
                            }

                            break;

                        case R.id.ivUpdateBtn:


                            if (isFocused) {
                                ivUpdateBtn.setBackgroundResource(R.drawable.border_red);
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_pressed_update_button", "")).into(ivUpdateBtn);

                            } else {
                                ivUpdateBtn.setBackgroundColor(getResources().getColor(R.color.red));
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_update_button", "")).into(ivUpdateBtn);
                            }

                            break;

                        case R.id.ivButton1:
                            if (SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "1" + Constants.Storage._isAppHidden.name(), false) || SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "1" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                                ivButton1.setBackground(getResources().getDrawable(R.drawable.invisible_selector));
                            } else {
                                if (isFocused) {
                                    ivButton1.setBackground(new BitmapDrawable(getResources(), Constants.icon_pressed_button1));
                                } else {
                                    ivButton1.setBackground(new BitmapDrawable(getResources(), Constants.icon_button1));
                                }
                            }
                            break;
                        case R.id.ivTopBtnNew:
                            if (SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "23" + Constants.Storage._isAppHidden.name(), false) || SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "23" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                                ivButtonTopNew.setBackground(getResources().getDrawable(R.drawable.invisible_selector));
                            } else {
                                if (isFocused) {
                                    ivButtonTopNew.setBackground(new BitmapDrawable(getResources(), Constants.icon_pressed_button_top_new));
                                } else {
                                    ivButtonTopNew.setBackground(new BitmapDrawable(getResources(), Constants.icon_button_top_new));
                                }
                            }
                            break;
                        case R.id.ivButton2:
                            if (SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "2" + Constants.Storage._isAppHidden.name(), false) || SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "2" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                                ivButton2.setBackground(getResources().getDrawable(R.drawable.invisible_selector));
                            } else {
                                if (isFocused) {
                                    ivButton2.setBackground(new BitmapDrawable(getResources(), Constants.icon_pressed_button2));
                                } else {
                                    ivButton2.setBackground(new BitmapDrawable(getResources(), Constants.icon_button2));
                                }
                            }
                            break;
                        case R.id.ivButton3:
                            if (SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "3" + Constants.Storage._isAppHidden.name(), false) || SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "3" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                                ivButton3.setBackground(getResources().getDrawable(R.drawable.invisible_selector));
                            } else {
                                if (isFocused) {
                                    ivButton3.setBackground(new BitmapDrawable(getResources(), Constants.icon_pressed_button3));
                                } else {
                                    ivButton3.setBackground(new BitmapDrawable(getResources(), Constants.icon_button3));
                                }
                            }
                            break;
                        case R.id.ivButton4:
                            if (SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "4" + Constants.Storage._isAppHidden.name(), false) || SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "4" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                                ivButton4.setBackground(getResources().getDrawable(R.drawable.invisible_selector));
                            } else {
                                if (isFocused) {
                                    ivButton4.setBackground(new BitmapDrawable(getResources(), Constants.icon_pressed_button4));
                                } else {
                                    ivButton4.setBackground(new BitmapDrawable(getResources(), Constants.icon_button4));
                                }
                            }
                            break;
                        case R.id.ivButton5:
                            if (SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "5" + Constants.Storage._isAppHidden.name(), false) || SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "5" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                                ivButton5.setBackground(getResources().getDrawable(R.drawable.invisible_selector));
                            } else {
                                if (isFocused) {
                                    ivButton5.setBackground(new BitmapDrawable(getResources(), Constants.icon_pressed_button5));
                                } else {
                                    ivButton5.setBackground(new BitmapDrawable(getResources(), Constants.icon_button5));
                                }
                            }
                            break;
                        case R.id.ivButton6:
                            if (SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "6" + Constants.Storage._isAppHidden.name(), false) || SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "6" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                                if (isFocused) {
                                    ivButton6.setBackground(new BitmapDrawable(getResources(), /*HelperUtils.addRedBorder(*/HelperUtils.getTransparentBitMap(Constants.icon_button6)))/*)*/;
                                } else {
                                    ivButton6.setBackground(new BitmapDrawable(getResources(), HelperUtils.getTransparentBitMap(Constants.icon_button6)));
                                }
                            } else {
                                if (isFocused) {
                                    ivButton6.setBackground(new BitmapDrawable(getResources(), Constants.icon_pressed_button6));
                                } else {
                                    ivButton6.setBackground(new BitmapDrawable(getResources(), Constants.icon_button6));
                                }
                            }
                            break;
                        case R.id.ivButton7:
                            if (SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "7" + Constants.Storage._isAppHidden.name(), false) || SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "7" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                                ivButton7.setBackground(getResources().getDrawable(R.drawable.invisible_selector));
                            } else {
                                if (isFocused) {
                                    ivButton7.setBackground(new BitmapDrawable(getResources(), Constants.icon_pressed_button7));
                                } else {
                                    ivButton7.setBackground(new BitmapDrawable(getResources(), Constants.icon_button7));
                                }
                            }
                            break;
                        case R.id.ivButton8:
                            if (SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "8" + Constants.Storage._isAppHidden.name(), false) || SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "8" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                                ivButton8.setBackground(getResources().getDrawable(R.drawable.invisible_selector));
                            } else {
                                if (isFocused) {
                                    ivButton8.setBackground(new BitmapDrawable(getResources(), Constants.icon_pressed_button8));
                                } else {
                                    ivButton8.setBackground(new BitmapDrawable(getResources(), Constants.icon_button8));
                                }
                            }
                            break;
                        case R.id.ivButton9:
                            if (SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "9" + Constants.Storage._isAppHidden.name(), false) || SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "9" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                                ivButton9.setBackground(getResources().getDrawable(R.drawable.invisible_selector));
                            } else {
                                if (isFocused) {
                                    ivButton9.setBackground(new BitmapDrawable(getResources(), Constants.icon_pressed_button9));
                                } else {
                                    ivButton9.setBackground(new BitmapDrawable(getResources(), Constants.icon_button9));
                                }
                            }
                            break;
                        case R.id.ivButton10:
                            if (SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "10" + Constants.Storage._isAppHidden.name(), false) || SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "10" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                                ivButton10.setBackground(getResources().getDrawable(R.drawable.invisible_selector));
                            } else {
                                if (isFocused) {
                                    ivButton10.setBackground(new BitmapDrawable(getResources(), Constants.icon_pressed_button10));
                                } else {
                                    ivButton10.setBackground(new BitmapDrawable(getResources(), Constants.icon_button10));
                                }
                            }
                            break;
                        case R.id.ivButton11:
                            if (SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "11" + Constants.Storage._isAppHidden.name(), false) || SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "11" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                                ivButton11.setBackground(getResources().getDrawable(R.drawable.invisible_selector));
                            } else {
                                if (isFocused) {
                                    ivButton11.setBackground(new BitmapDrawable(getResources(), Constants.icon_pressed_button11));
                                } else {
                                    ivButton11.setBackground(new BitmapDrawable(getResources(), Constants.icon_button11));
                                }
                            }
                            break;
                        case R.id.btn_left_top:

                            if (isFocused) {
                                btn_left_top.setBackground(getResources().getDrawable(R.drawable.border_red));
                            } else {
                                btn_left_top.setBackgroundColor(getResources().getColor(R.color.transparent));
                            }

                            break;
                        case R.id.btn_left_top_right:

                            if (isFocused) {
                                btn_left_top_right.setBackground(getResources().getDrawable(R.drawable.border_red));
                            } else {
                                btn_left_top_right.setBackgroundColor(getResources().getColor(R.color.transparent));
                            }

                            break;
                        case R.id.btn_left1:

                            if (isFocused) {
                                btn_left1.setBackground(getResources().getDrawable(R.drawable.border_red));
                            } else {
                                btn_left1.setBackgroundColor(getResources().getColor(R.color.transparent));
                            }

                            break;
                        case R.id.btn_left2:

                            if (isFocused) {
                                btn_left2.setBackground(getResources().getDrawable(R.drawable.border_red));
                            } else {
                                btn_left2.setBackgroundColor(getResources().getColor(R.color.transparent));
                            }

                            break;
                        case R.id.btn_left3:

                            if (isFocused) {
                                btn_left3.setBackground(getResources().getDrawable(R.drawable.border_red));
                            } else {
                                btn_left3.setBackgroundColor(getResources().getColor(R.color.transparent));
                            }

                            break;
                        case R.id.btn_right1:

                            if (isFocused) {
                                btn_right1.setBackground(getResources().getDrawable(R.drawable.border_red));
                            } else {
                                btn_right1.setBackgroundColor(getResources().getColor(R.color.transparent));
                            }

                            break;
                        case R.id.btn_right2:

                            if (isFocused) {
                                btn_right2.setBackground(getResources().getDrawable(R.drawable.border_red));
                            } else {
                                btn_right2.setBackgroundColor(getResources().getColor(R.color.transparent));
                            }

                            break;
                        case R.id.btn_right3:

                            if (isFocused) {
                                btn_right3.setBackground(getResources().getDrawable(R.drawable.border_red));
                            } else {
                                btn_right3.setBackgroundColor(getResources().getColor(R.color.transparent));
                            }

                            break;

                        case R.id.ivBottomAddRemoveBtn:
                            if (SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "12" + Constants.Storage._isAppHidden.name(), false) || SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "12" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                                ivAddRemoveBtn.setBackground(getResources().getDrawable(R.drawable.invisible_selector));
                                rvDockItemRecyclerVew.setVisibility(View.INVISIBLE);
                            } else {
                                if (isFocused) {
                                    ivAddRemoveBtn.setBackground(new BitmapDrawable(getResources(), Constants.icon_pressed_dock_button));
                                } else {
                                    ivAddRemoveBtn.setBackground(new BitmapDrawable(getResources(), Constants.icon_dock_button));
                                }
                                rvDockItemRecyclerVew.setVisibility(View.VISIBLE);
                            }
                            break;
                        case R.id.ivPoweBtn:
                            if (SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "13" + Constants.Storage._isAppHidden.name(), false) || SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "13" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                                ivPoweBtn.setBackground(getResources().getDrawable(R.drawable.invisible_selector));
                            } else {
                                if (isFocused) {
                                    ivPoweBtn.setBackground(new BitmapDrawable(getResources(), Constants.icon_pressed_power_button));
                                } else {
                                    ivPoweBtn.setBackground(new BitmapDrawable(getResources(), Constants.icon_power_button));
                                }
                            }
                            break;
                        case R.id.ivVpnBtn:
//                            if (SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "14" + Constants.Storage._isAppHidden.name(), false) || SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, "14" + Constants.Storage._isAppHiddenByServer.name(), false)) {
//                                ivVpnBtn.setBackground(getResources().getDrawable(R.drawable.invisible_selector));
//                            } else {
                            if (isFocused) {
                                //ivVpnBtn.setBackground(new BitmapDrawable(getResources(), Constants.icon_pressed_vpn_button));
                                ivVpnBtn.setBackground(getResources().getDrawable(R.drawable.border_for_focus));
                            } else {
                                //ivVpnBtn.setBackground(new BitmapDrawable(getResources(), Constants.icon_vpn_button));
                                ivVpnBtn.setBackgroundColor(Color.TRANSPARENT);
                            }
//                            }
                            break;
                        case R.id.ivHelpBtn:
                            if (isFocused) {
                                ivHelpBtn.setBackground(new BitmapDrawable(getResources(), Constants.icon_pressed_help_button));
                            } else {
                                ivHelpBtn.setBackground(new BitmapDrawable(getResources(), Constants.icon_help_button));
                            }
                            break;
                        case R.id.rlWebViewRssContainer:
                            if (isFocused) {
                                rlWebViewRssContainer.setBackgroundColor(getResources().getColor(R.color.main_red_500));
                            } else {
                                rlWebViewRssContainer.setBackgroundColor(Color.TRANSPARENT);
                            }
                            break;
                        case R.id.ivBluetoothState:
                            if (isFocused) {
                                ivBlueState.setBackground(getResources().getDrawable(R.drawable.border_red));
                            } else {
                                ivBlueState.setBackgroundColor(Color.TRANSPARENT);
                            }
                            break;
                        case R.id.ivWifiState:
                            if (isFocused) {
                                ivWifiState.setBackground(getResources().getDrawable(R.drawable.border_red));
                            } else {
                                ivWifiState.setBackgroundColor(Color.TRANSPARENT);
                            }
                            break;
                        case R.id.news_btn:
//                            if (isFocused) {
//                                news_btn.setBackground(getResources().getDrawable(R.drawable.news_button_selector));
//                            } else {
//                                news_btn.setBackground(getResources().getDrawable(R.drawable.news_btn_off_selector));
//                            }
                            if (isFocused) {
                                news_btn.setBackground(getResources().getDrawable(R.drawable.border_red));
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton13", "")).into(news_btn);

                            } else {
                                news_btn.setBackgroundColor(getResources().getColor(R.color.transparent));
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_pressed_minibutton13", "")).into(news_btn);
                            }
                            break;
                        case R.id.ivAppLockBtn:
//                            if (isFocused) {
//                                ivAppLockBtn.setBackground(getResources().getDrawable(R.drawable.app_lock_on_selector));
//                            } else {
//                                ivAppLockBtn.setBackground(getResources().getDrawable(R.drawable.app_lock_off_selector));
//                            }
                            if (isFocused) {
                                ivAppLockBtn.setBackground(getResources().getDrawable(R.drawable.border_red));
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton5", "")).into(ivAppLockBtn);

                            } else {
                                ivAppLockBtn.setBackgroundColor(getResources().getColor(R.color.transparent));
//                                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_pressed_minibutton5", "")).into(ivAppLockBtn);
                            }
                            break;
                        case R.id.ivLanState:
                            if (isFocused) {
                                ivLanState.setBackground(getResources().getDrawable(R.drawable.border_red));
                            } else {
                                ivLanState.setBackgroundColor(Color.TRANSPARENT);
                            }
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        try {

            Intent launchIntent = null;
            switch (view.getId()) {
                case R.id.ivVpnBtn:
                    doShowCustomDynamicAds(Constants.APPID_VPN_BUTTON);
                    if (SharedPreferenceStorage.getBooleanValue(this, "14" + Constants.Storage._isDeleted.name(), false)) {
                        Toast.makeText(this, R.string.this_app_uninstalled, Toast.LENGTH_SHORT).show();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "14" + Constants.Storage._isAppHidden.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "14" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {
                        String type = SharedPreferenceStorage.getStringValue(this, "btn_vpn_type", "apk");
                        if (type.equalsIgnoreCase("apk")) {
                            launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_VPN_BUTTON);
                            startLockedActivity(Constants.Storage.LOCK_STATUS_VPN_BTN.name(), launchIntent);
                            launchIntent = null;
                        } else if (type.equalsIgnoreCase("url")) {
                            String url = SharedPreferenceStorage.getStringValue(this, "btn_vpn_help_url", "");
                            openFullWebView(url);
                        } else if (type.equalsIgnoreCase("png")) {
                            String url = SharedPreferenceStorage.getStringValue(this, "btn_vpn_help_url", "");
                            new DownloadNewUpdate(this, Constants.APPID_VPN_BUTTON, 0, 0, Constants.FILE_TYPE.png, null).openImage(url);
                        } else if (type.equalsIgnoreCase("mov")) {
                            try {
                                String url = SharedPreferenceStorage.getStringValue(this, "btn_vpn_help_url", "");
                                Uri uri = Uri.parse(url); //your file URI
                                Intent vlcIntent = new Intent(Intent.ACTION_VIEW);
                                vlcIntent.setPackage("org.videolan.vlc");
                                vlcIntent.setDataAndTypeAndNormalize(uri, "video/*");
                                vlcIntent.putExtra("title", SharedPreferenceStorage.getStringValue(this, "btn_vpn_title", ""));
                                vlcIntent.putExtra("from_start", true);
                                this.startActivity(vlcIntent);
                            } catch (Exception e) {
                                new DownloadNewUpdate(this, Constants.APPID_VPN_BUTTON, 0, 0, Constants.FILE_TYPE.png, null).execute();
                                e.printStackTrace();
                            }

                        }
                    }
//
//                        launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_VPN_BUTTON);
//                        startLockedActivity(Constants.Storage.LOCK_STATUS_VPN_BTN.name(), launchIntent);
//                        launchIntent = null;

                    break;
                case R.id.news_btn:
//
                    String news_url = SharedPreferenceStorage.getStringValue(this, "news_button_url", "");
                    openFullWebView(news_url);

                    break;

                case R.id.ivMiniButton1:
                    doShowCustomDynamicAds(Constants.APPID_MiniButton1);
                    if (SharedPreferenceStorage.getBooleanValue(this, "23" + Constants.Storage._isDeleted.name(), false)) {
                        Toast.makeText(this, R.string.this_app_uninstalled, Toast.LENGTH_SHORT).show();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "23" + Constants.Storage._isAppHidden.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "23" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {
//                        String type = SharedPreferenceStorage.getStringValue(this, "btn_vpn_type", "apk");
//                        if (type.equalsIgnoreCase("apk")) {
                        launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_MiniButton1);
                        startLockedActivity(Constants.Storage.LOCK_STATUS_MiniButton1.name(), launchIntent);
                        launchIntent = null;
//                        } else if (type.equalsIgnoreCase("url")) {
//                            String url = SharedPreferenceStorage.getStringValue(this, "btn_vpn_help_url", "");
//                            openFullWebView(url);
//                        } else if (type.equalsIgnoreCase("png")) {
//                            String url = SharedPreferenceStorage.getStringValue(this, "btn_vpn_help_url", "");
//                            new DownloadNewUpdate(this, Constants.APPID_VPN_BUTTON, Constants.FILE_TYPE.png, null).openImage(url);
//                        } else if (type.equalsIgnoreCase("mov")) {
//                            try {
//                                String url = SharedPreferenceStorage.getStringValue(this, "btn_vpn_help_url", "");
//                                Uri uri = Uri.parse(url); //your file URI
//                                Intent vlcIntent = new Intent(Intent.ACTION_VIEW);
//                                vlcIntent.setPackage("org.videolan.vlc");
//                                vlcIntent.setDataAndTypeAndNormalize(uri, "video/*");
//                                vlcIntent.putExtra("title", SharedPreferenceStorage.getStringValue(this, "btn_vpn_title", ""));
//                                vlcIntent.putExtra("from_start", true);
//                                this.startActivity(vlcIntent);
//                            } catch (Exception e) {
//                                new DownloadNewUpdate(this, Constants.APPID_VPN_BUTTON, Constants.FILE_TYPE.png, null).execute();
//                                e.printStackTrace();
//                            }
//
//                        }
                    }
//
//                        launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_VPN_BUTTON);
//                        startLockedActivity(Constants.Storage.LOCK_STATUS_VPN_BTN.name(), launchIntent);
//                        launchIntent = null;

                    break;
                case R.id.ivMiniButton2:
                    doShowCustomDynamicAds(Constants.APPID_MiniButton2);
                    if (SharedPreferenceStorage.getBooleanValue(this, "24" + Constants.Storage._isDeleted.name(), false)) {
                        Toast.makeText(this, R.string.this_app_uninstalled, Toast.LENGTH_SHORT).show();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "24" + Constants.Storage._isAppHidden.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "24" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {
//                        String type = SharedPreferenceStorage.getStringValue(this, "btn_vpn_type", "apk");
//                        if (type.equalsIgnoreCase("apk")) {
                        launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_MiniButton2);
                        startLockedActivity(Constants.Storage.LOCK_STATUS_MiniButton2.name(), launchIntent);
                        launchIntent = null;
                    }
                    break;
                case R.id.ivMiniButton3:
                    doShowCustomDynamicAds(Constants.APPID_MiniButton3);
                    if (SharedPreferenceStorage.getBooleanValue(this, "25" + Constants.Storage._isDeleted.name(), false)) {
                        Toast.makeText(this, R.string.this_app_uninstalled, Toast.LENGTH_SHORT).show();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "25" + Constants.Storage._isAppHidden.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "25" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {
//                        String type = SharedPreferenceStorage.getStringValue(this, "btn_vpn_type", "apk");
//                        if (type.equalsIgnoreCase("apk")) {
                        launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_MiniButton3);
                        startLockedActivity(Constants.Storage.LOCK_STATUS_MiniButton3.name(), launchIntent);
                        launchIntent = null;

                    }

                    break;
                case R.id.ivMiniButton4:
                    doShowCustomDynamicAds(Constants.APPID_MiniButton4);
                    if (SharedPreferenceStorage.getBooleanValue(this, "26" + Constants.Storage._isDeleted.name(), false)) {
                        Toast.makeText(this, R.string.this_app_uninstalled, Toast.LENGTH_SHORT).show();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "26" + Constants.Storage._isAppHidden.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "26" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {
//                        String type = SharedPreferenceStorage.getStringValue(this, "btn_vpn_type", "apk");
//                        if (type.equalsIgnoreCase("apk")) {
                        launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_MiniButton4);
                        startLockedActivity(Constants.Storage.LOCK_STATUS_MiniButton4.name(), launchIntent);
                        launchIntent = null;
                    }
                    break;
                case R.id.ivMiniButton5:
                    doShowCustomDynamicAds(Constants.APPID_MiniButton5);
                    if (SharedPreferenceStorage.getBooleanValue(this, "27" + Constants.Storage._isDeleted.name(), false)) {
                        Toast.makeText(this, R.string.this_app_uninstalled, Toast.LENGTH_SHORT).show();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "27" + Constants.Storage._isAppHidden.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "27" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {
//                        String type = SharedPreferenceStorage.getStringValue(this, "btn_vpn_type", "apk");
//                        if (type.equalsIgnoreCase("apk")) {
                        launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_MiniButton5);
                        startLockedActivity(Constants.Storage.LOCK_STATUS_MiniButton5.name(), launchIntent);
                        launchIntent = null;

                    }
                    break;
                case R.id.ivMiniButton6:
                    doShowCustomDynamicAds(Constants.APPID_MiniButton6);
                    if (SharedPreferenceStorage.getBooleanValue(this, "28" + Constants.Storage._isDeleted.name(), false)) {
                        Toast.makeText(this, R.string.this_app_uninstalled, Toast.LENGTH_SHORT).show();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "28" + Constants.Storage._isAppHidden.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "28" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {
//                        String type = SharedPreferenceStorage.getStringValue(this, "btn_vpn_type", "apk");
//                        if (type.equalsIgnoreCase("apk")) {
                        launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_MiniButton6);
                        startLockedActivity(Constants.Storage.LOCK_STATUS_MiniButton6.name(), launchIntent);
                        launchIntent = null;
                    }

                    break;
                case R.id.ivMiniButton7:
                    doShowCustomDynamicAds(Constants.APPID_MiniButton7);
                    if (SharedPreferenceStorage.getBooleanValue(this, "29" + Constants.Storage._isDeleted.name(), false)) {
                        Toast.makeText(this, R.string.this_app_uninstalled, Toast.LENGTH_SHORT).show();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "29" + Constants.Storage._isAppHidden.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "29" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {
//                        String type = SharedPreferenceStorage.getStringValue(this, "btn_vpn_type", "apk");
//                        if (type.equalsIgnoreCase("apk")) {
                        launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_MiniButton7);
                        startLockedActivity(Constants.Storage.LOCK_STATUS_MiniButton7.name(), launchIntent);
                        launchIntent = null;
                    }
                    break;
                case R.id.ivMiniButton8:
                    doShowCustomDynamicAds(Constants.APPID_MiniButton8);
                    if (SharedPreferenceStorage.getBooleanValue(this, "30" + Constants.Storage._isDeleted.name(), false)) {
                        Toast.makeText(this, R.string.this_app_uninstalled, Toast.LENGTH_SHORT).show();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "30" + Constants.Storage._isAppHidden.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "30" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {
//                        String type = SharedPreferenceStorage.getStringValue(this, "btn_vpn_type", "apk");
//                        if (type.equalsIgnoreCase("apk")) {
                        launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_MiniButton8);
                        startLockedActivity(Constants.Storage.LOCK_STATUS_MiniButton8.name(), launchIntent);
                        launchIntent = null;

                    }
                    break;
                case R.id.ivMiniButton9:
                    doShowCustomDynamicAds(Constants.APPID_MiniButton9);
                    if (SharedPreferenceStorage.getBooleanValue(this, "31" + Constants.Storage._isDeleted.name(), false)) {
                        Toast.makeText(this, R.string.this_app_uninstalled, Toast.LENGTH_SHORT).show();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "31" + Constants.Storage._isAppHidden.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "31" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {
//                        String type = SharedPreferenceStorage.getStringValue(this, "btn_vpn_type", "apk");
//                        if (type.equalsIgnoreCase("apk")) {
                        launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_MiniButton9);
                        startLockedActivity(Constants.Storage.LOCK_STATUS_MiniButton9.name(), launchIntent);
                        launchIntent = null;
                    }
                    break;
                case R.id.ivMiniButton10:
                    doShowCustomDynamicAds(Constants.APPID_MiniButton10);
                    if (SharedPreferenceStorage.getBooleanValue(this, "32" + Constants.Storage._isDeleted.name(), false)) {
                        Toast.makeText(this, R.string.this_app_uninstalled, Toast.LENGTH_SHORT).show();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "32" + Constants.Storage._isAppHidden.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "32" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {
//                        String type = SharedPreferenceStorage.getStringValue(this, "btn_vpn_type", "apk");
//                        if (type.equalsIgnoreCase("apk")) {
                        launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_MiniButton10);
                        startLockedActivity(Constants.Storage.LOCK_STATUS_MiniButton10.name(), launchIntent);
                        launchIntent = null;
                    }
                    break;
                case R.id.ivMiniButton11:
                    doShowCustomDynamicAds(Constants.APPID_MiniButton11);
                    if (SharedPreferenceStorage.getBooleanValue(this, "33" + Constants.Storage._isDeleted.name(), false)) {
                        Toast.makeText(this, R.string.this_app_uninstalled, Toast.LENGTH_SHORT).show();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "33" + Constants.Storage._isAppHidden.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "33" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {
//                        String type = SharedPreferenceStorage.getStringValue(this, "btn_vpn_type", "apk");
//                        if (type.equalsIgnoreCase("apk")) {
                        launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_MiniButton11);
                        startLockedActivity(Constants.Storage.LOCK_STATUS_MiniButton11.name(), launchIntent);
                        launchIntent = null;
                    }
                    break;
                case R.id.ivMiniButton12:
                    doShowCustomDynamicAds(Constants.APPID_MiniButton12);
                    if (SharedPreferenceStorage.getBooleanValue(this, "34" + Constants.Storage._isDeleted.name(), false)) {
                        Toast.makeText(this, R.string.this_app_uninstalled, Toast.LENGTH_SHORT).show();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "34" + Constants.Storage._isAppHidden.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "34" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {
//                        String type = SharedPreferenceStorage.getStringValue(this, "btn_vpn_type", "apk");
//                        if (type.equalsIgnoreCase("apk")) {
                        launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_MiniButton12);
                        startLockedActivity(Constants.Storage.LOCK_STATUS_MiniButton12.name(), launchIntent);
                        launchIntent = null;
                    }
                    break;

                case R.id.ivPoweBtn:
                    if (SharedPreferenceStorage.getBooleanValue(this, "13" + Constants.Storage._isDeleted.name(), false)) {
                        Toast.makeText(this, R.string.this_app_uninstalled, Toast.LENGTH_SHORT).show();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "13" + Constants.Storage._isAppHidden.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "13" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {

                        String type = SharedPreferenceStorage.getStringValue(this, "btn_pwr_type", "apk");
                        if (type.equalsIgnoreCase("apk")) {
                            launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_PWR_BUTTON);
                            startLockedActivity(Constants.Storage.LOCK_STATUS_POWER_BTN.name(), launchIntent);
                            launchIntent = null;
                        } else if (type.equalsIgnoreCase("url")) {
                            String url = SharedPreferenceStorage.getStringValue(this, "btn_pwr_help_url", "");
                            openFullWebView(url);
                        } else if (type.equalsIgnoreCase("png")) {
                            String url = SharedPreferenceStorage.getStringValue(this, "btn_pwr_help_url", "");
                            new DownloadNewUpdate(this, Constants.APPID_PWR_BUTTON, 0, 0, Constants.FILE_TYPE.png, null).openImage(url);
                        } else if (type.equalsIgnoreCase("mp3")) {
                            try {
                                String url = SharedPreferenceStorage.getStringValue(this, "btn_pwr_help_url", "");
                                Uri uri = Uri.parse(url); //your file URI
                                Intent vlcIntent = new Intent(Intent.ACTION_VIEW);
                                vlcIntent.setPackage("org.videolan.vlc");
                                vlcIntent.setDataAndTypeAndNormalize(uri, "video/*");
                                vlcIntent.putExtra("title", SharedPreferenceStorage.getStringValue(this, "btn_pwr_title", ""));
                                vlcIntent.putExtra("from_start", true);
                                this.startActivity(vlcIntent);
                            } catch (Exception e) {
                                new DownloadNewUpdate(this, Constants.APPID_PWR_BUTTON, 0, 0, Constants.FILE_TYPE.png, null).execute();
                                e.printStackTrace();
                            }

                        }
                    }

//                        launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_PWR_BUTTON);
//                        startLockedActivity(Constants.Storage.LOCK_STATUS_POWER_BTN.name(), launchIntent);
//                        launchIntent = null;


                    break;
                case R.id.ivButtonTop:


//                    if (SharedPreferenceStorage.getBooleanValue(this, "0" + Constants.Storage._isDeleted.name(), false)) {
//                        Toast.makeText(this, R.string.this_app_uninstalled, Toast.LENGTH_SHORT).show();
//                    } else if (SharedPreferenceStorage.getBooleanValue(this, "0" + Constants.Storage._isAppHidden.name(), false)
//                            || SharedPreferenceStorage.getBooleanValue(this, "0" + Constants.Storage._isAppHiddenByServer.name(), false)) {
//                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
//                    } else {
//                        String type = SharedPreferenceStorage.getStringValue(this, "btn0_type", "apk");
//                        if (type.equalsIgnoreCase("apk")) {
//                            launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BUTTON_TOP);
//                            startLockedActivity(Constants.Storage.LOCK_STATUS_BUTTON0.name(), launchIntent);
//                            launchIntent = null;
//                        } else if (type.equalsIgnoreCase("url")) {
//                            String url = SharedPreferenceStorage.getStringValue(this, "btn0_help_url", "");
//                            openFullWebView(url);
//                        } else if (type.equalsIgnoreCase("png")) {
//                            String url = SharedPreferenceStorage.getStringValue(this, "btn0_help_url", "");
//                            new DownloadNewUpdate(this, Constants.APPID_BUTTON_TOP, Constants.FILE_TYPE.png, null).openImage(url);
//                        } else if (type.equalsIgnoreCase("mp3")) {
//                            try {
//                                String url = SharedPreferenceStorage.getStringValue(this, "btn0_help_url", "");
//                                Uri uri = Uri.parse(url); //your file URI
//                                Intent vlcIntent = new Intent(Intent.ACTION_VIEW);
//                                vlcIntent.setPackage("org.videolan.vlc");
//                                vlcIntent.setDataAndTypeAndNormalize(uri, "video/*");
//                                vlcIntent.putExtra("title", SharedPreferenceStorage.getStringValue(this, "btn0_title", ""));
//                                vlcIntent.putExtra("from_start", true);
//                                this.startActivity(vlcIntent);
//                            } catch (Exception e) {
//                                new DownloadNewUpdate(this, Constants.APPID_BUTTON_TOP, Constants.FILE_TYPE.png, null).execute();
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }
//                        launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BUTTON_TOP);
//                        startLockedActivity(Constants.Storage.LOCK_STATUS_BUTTON0.name(), launchIntent);
//                        showdialog(Constants.APPID_BUTTON_TOP, Constants.Storage.LOCK_STATUS_BUTTON0.name(), Constants.APPID_BUTTON_TOP, ivButtonTop);
                    launchIntent = null;

                    break;
                case R.id.ivButton1:

                    String type1 = SharedPreferenceStorage.getStringValue(this, "btn1_type", "");
                    Log.i(TAG, "onClick: button1" + type1);
                    if (type1.equalsIgnoreCase("apk")) {
                        launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BUTTON1);
                        startLockedActivity(Constants.Storage.LOCK_STATUS_BUTTON1.name(), launchIntent);
                    } else if (type1.equalsIgnoreCase("png")) {
                        final AlertDialog[] alertDialog = {null};
                        String url = SharedPreferenceStorage.getStringValue(this, "btn1_help_url", "");
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                        LayoutInflater inflater = this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.image_viewer, null);
                        dialogBuilder.setView(dialogView);
                        final ImageView imageViewer = dialogView.findViewById(R.id.ivImageViewer);
                        Picasso.get().load(url).into(imageViewer);
//                            imageViewer.setOnKeyListener(new View.OnKeyListener() {
//                                @Override
//                                public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                                    alertDialog[0].dismiss();
//                                    return true;
//                                }
//                            });
                        alertDialog[0] = dialogBuilder.create();
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(alertDialog[0].getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                        alertDialog[0].getWindow().setAttributes(lp);
                        alertDialog[0].setCancelable(true);
                        alertDialog[0].show();
                        imageViewer.requestFocus();
                    }

//                        doShowCustomDynamicAds(Constants.APPID_BUTTON1);

//                        dialog.show();
//                        showdialog(Constants.APPID_BUTTON1, Constants.Storage.LOCK_STATUS_BUTTON1.name(), Constants.DEF_APPID_BUTTON1, ivButton1);

                    launchIntent = null;

                    break;
                case R.id.ivTopBtnNew:

                    String typenew = SharedPreferenceStorage.getStringValue(this, "btn_top_new_type", "");
                    Log.i(TAG, "onClick: button1" + typenew);
                    if (typenew.equalsIgnoreCase("apk")) {
                        launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BUTTON_TOP_NEW);
                        startLockedActivity(Constants.Storage.LOCK_STATUS_BUTTON_TOP_NEW.name(), launchIntent);
                    } else if (typenew.equalsIgnoreCase("png")) {
                        final AlertDialog[] alertDialog = {null};
                        String url = SharedPreferenceStorage.getStringValue(this, "btn_top_new_help_url", "");
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                        LayoutInflater inflater = this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.image_viewer, null);
                        dialogBuilder.setView(dialogView);
                        final ImageView imageViewer = dialogView.findViewById(R.id.ivImageViewer);
                        Picasso.get().load(url).into(imageViewer);
//                            imageViewer.setOnKeyListener(new View.OnKeyListener() {
//                                @Override
//                                public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                                    alertDialog[0].dismiss();
//                                    return true;
//                                }
//                            });
                        alertDialog[0] = dialogBuilder.create();
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(alertDialog[0].getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                        alertDialog[0].getWindow().setAttributes(lp);
                        alertDialog[0].setCancelable(true);
                        alertDialog[0].show();
                        imageViewer.requestFocus();
                    }

//                        doShowCustomDynamicAds(Constants.APPID_BUTTON1);

//                        dialog.show();
//                        showdialog(Constants.APPID_BUTTON1, Constants.Storage.LOCK_STATUS_BUTTON1.name(), Constants.DEF_APPID_BUTTON1, ivButton1);

                    launchIntent = null;

                    break;
                case R.id.ivButton2:

                    String type2 = SharedPreferenceStorage.getStringValue(this, "btn2_type", "");

                    if (type2.equalsIgnoreCase("apk")) {
                        launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BUTTON2);
                        startLockedActivity(Constants.Storage.LOCK_STATUS_BUTTON2.name(), launchIntent);
                    } else if (type2.equalsIgnoreCase("png")) {
                        final AlertDialog[] alertDialog = {null};
                        String url = SharedPreferenceStorage.getStringValue(this, "btn2_help_url", "");
                        Log.i(TAG, "onClick: button2" + url);
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.image_viewer, null);
                        dialogBuilder.setView(dialogView);
                        final ImageView imageViewer = dialogView.findViewById(R.id.ivImageViewer);
                        Picasso.get().load(url).into(imageViewer);
                        alertDialog[0] = dialogBuilder.create();
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(alertDialog[0].getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                        alertDialog[0].getWindow().setAttributes(lp);
                        alertDialog[0].setCancelable(true);
                        alertDialog[0].show();
                        imageViewer.requestFocus();

//                        launchIntent  = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BUTTON2);
//                        startLockedActivity(Constants.Storage.LOCK_STATUS_BUTTON2.name(), launchIntent );
//                        dialog.show();
//                        showdialog(Constants.APPID_BUTTON2, Constants.Storage.LOCK_STATUS_BUTTON2.name(), Constants.DEF_APPID_BUTTON2, ivButton2);
                        launchIntent = null;
                    }
                    break;
                case R.id.ivButton3:
                    if (SharedPreferenceStorage.getBooleanValue(this, "3" + Constants.Storage._isDeleted.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "3" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        Log.i("TAG", "onClick: button3     : if");
                        Toast.makeText(this, R.string.this_app_uninstalled, Toast.LENGTH_SHORT).show();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "3" + Constants.Storage._isAppHidden.name(), false)) {
                        Log.i("TAG", "onClick: button3     : else if");
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {
                        String type = SharedPreferenceStorage.getStringValue(this, "btn3_type", "apk");
                        if (type.equalsIgnoreCase("apk")) {
                            launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BUTTON3);
                            startLockedActivity(Constants.Storage.LOCK_STATUS_BUTTON3.name(), launchIntent);
                        } else if (type.equalsIgnoreCase("png")) {
                            final AlertDialog[] alertDialog = {null};
                            String url = SharedPreferenceStorage.getStringValue(this, "btn3_help_url", "");
                            Log.i(TAG, "onClick: button3" + url);
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.image_viewer, null);
                            dialogBuilder.setView(dialogView);
                            final ImageView imageViewer = dialogView.findViewById(R.id.ivImageViewer);
                            Picasso.get().load(url).into(imageViewer);
                            alertDialog[0] = dialogBuilder.create();
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(alertDialog[0].getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                            alertDialog[0].getWindow().setAttributes(lp);
                            alertDialog[0].setCancelable(true);
                            alertDialog[0].show();
                            imageViewer.requestFocus();
                        }
                    }
                    launchIntent = null;

                    break;
                case R.id.ivButton4:
                    if (SharedPreferenceStorage.getBooleanValue(this, "4" + Constants.Storage._isDeleted.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "4" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        Log.i("TAG", "onClick: button4     : if");
                        Toast.makeText(this, R.string.this_app_uninstalled, Toast.LENGTH_SHORT).show();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "4" + Constants.Storage._isAppHidden.name(), false)) {
                        Log.i("TAG", "onClick: button4     : else if");
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {


                        String type = SharedPreferenceStorage.getStringValue(this, "btn4_type", "apk");
                        if (type.equalsIgnoreCase("apk")) {
                            launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BUTTON4);
                            startLockedActivity(Constants.Storage.LOCK_STATUS_BUTTON4.name(), launchIntent);
                        } else if (type.equalsIgnoreCase("png")) {
                            final AlertDialog[] alertDialog = {null};
                            String url = SharedPreferenceStorage.getStringValue(this, "btn4_help_url", "");
                            Log.i(TAG, "onClick: button4" + url);
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.image_viewer, null);
                            dialogBuilder.setView(dialogView);
                            final ImageView imageViewer = dialogView.findViewById(R.id.ivImageViewer);
                            Picasso.get().load(url).into(imageViewer);
                            alertDialog[0] = dialogBuilder.create();
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(alertDialog[0].getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                            alertDialog[0].getWindow().setAttributes(lp);
                            alertDialog[0].setCancelable(true);
                            alertDialog[0].show();
                            imageViewer.requestFocus();
                        }

//                        Log.i("TAG", "onClick: button4     : else");
//                        launchIntent  = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BUTTON4);
//                        startLockedActivity(Constants.Storage.LOCK_STATUS_BUTTON4.name(), launchIntent );
//                        dialog.show();

//                        showdialog(Constants.APPID_BUTTON4, Constants.Storage.LOCK_STATUS_BUTTON4.name(), Constants.DEF_APPID_BUTTON4, ivButton4);
                        launchIntent = null;
                    }
                    break;
                case R.id.ivButton5:
                    if (SharedPreferenceStorage.getBooleanValue(this, "5" + Constants.Storage._isDeleted.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "5" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        Toast.makeText(this, R.string.this_app_uninstalled, Toast.LENGTH_SHORT).show();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "5" + Constants.Storage._isAppHidden.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {

                        String type = SharedPreferenceStorage.getStringValue(this, "btn5_type", "apk");
                        if (type.equalsIgnoreCase("apk")) {
                            launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BUTTON5);
                            startLockedActivity(Constants.Storage.LOCK_STATUS_BUTTON5.name(), launchIntent);
                        } else if (type.equalsIgnoreCase("png")) {
                            final AlertDialog[] alertDialog = {null};
                            String url = SharedPreferenceStorage.getStringValue(this, "btn5_help_url", "");
                            Log.i(TAG, "onClick: button5" + url);
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.image_viewer, null);
                            dialogBuilder.setView(dialogView);
                            final ImageView imageViewer = dialogView.findViewById(R.id.ivImageViewer);
                            Picasso.get().load(url).into(imageViewer);
                            alertDialog[0] = dialogBuilder.create();
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(alertDialog[0].getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                            alertDialog[0].getWindow().setAttributes(lp);
                            alertDialog[0].setCancelable(true);
                            alertDialog[0].show();
                            imageViewer.requestFocus();
                        }
//                        launchIntent  = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BUTTON5);
//                        startLockedActivity(Constants.Storage.LOCK_STATUS_BUTTON5.name(), launchIntent );
//                        dialog.show();
//                        showdialog(Constants.APPID_BUTTON5, Constants.Storage.LOCK_STATUS_BUTTON5.name(), Constants.DEF_APPID_BUTTON5, ivButton5);
                        launchIntent = null;
                    }
                    break;
                case R.id.ivButton7:
                    if (SharedPreferenceStorage.getBooleanValue(this, "7" + Constants.Storage._isDeleted.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "7" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        Toast.makeText(this, R.string.this_app_uninstalled, Toast.LENGTH_SHORT).show();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "7" + Constants.Storage._isAppHidden.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {

                        String type = SharedPreferenceStorage.getStringValue(this, "btn7_type", "apk");
                        if (type.equalsIgnoreCase("apk")) {
                            launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BUTTON7);
                            startLockedActivity(Constants.Storage.LOCK_STATUS_BUTTON7.name(), launchIntent);
                        } else if (type.equalsIgnoreCase("png")) {
                            final AlertDialog[] alertDialog = {null};
                            String url = SharedPreferenceStorage.getStringValue(this, "btn7_help_url", "");
                            Log.i(TAG, "onClick: button7" + url);
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.image_viewer, null);
                            dialogBuilder.setView(dialogView);
                            final ImageView imageViewer = dialogView.findViewById(R.id.ivImageViewer);
                            Picasso.get().load(url).into(imageViewer);
                            alertDialog[0] = dialogBuilder.create();
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(alertDialog[0].getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                            alertDialog[0].getWindow().setAttributes(lp);
                            alertDialog[0].setCancelable(true);
                            alertDialog[0].show();
                            imageViewer.requestFocus();
                        }
//                        launchIntent  = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BUTTON7);
//                        startLockedActivity(Constants.Storage.LOCK_STATUS_BUTTON7.name(), launchIntent );
//                        dialog.show();
//                        showdialog(Constants.APPID_BUTTON7, Constants.Storage.LOCK_STATUS_BUTTON7.name(), Constants.DEF_APPID_BUTTON7, ivButton7);
                        launchIntent = null;
                    }
                    break;
                case R.id.ivButton8:
                    if (SharedPreferenceStorage.getBooleanValue(this, "8" + Constants.Storage._isDeleted.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "8" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        Toast.makeText(this, R.string.this_app_uninstalled, Toast.LENGTH_SHORT).show();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "8" + Constants.Storage._isAppHidden.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {

                        String type = SharedPreferenceStorage.getStringValue(this, "btn8_type", "apk");
                        if (type.equalsIgnoreCase("apk")) {
                            launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BUTTON8);
                            startLockedActivity(Constants.Storage.LOCK_STATUS_BUTTON8.name(), launchIntent);
                        } else if (type.equalsIgnoreCase("png")) {
                            final AlertDialog[] alertDialog = {null};
                            String url = SharedPreferenceStorage.getStringValue(this, "btn8_help_url", "");
                            Log.i(TAG, "onClick: button8" + url);
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.image_viewer, null);
                            dialogBuilder.setView(dialogView);
                            final ImageView imageViewer = dialogView.findViewById(R.id.ivImageViewer);
                            Picasso.get().load(url).into(imageViewer);
                            alertDialog[0] = dialogBuilder.create();
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(alertDialog[0].getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                            alertDialog[0].getWindow().setAttributes(lp);
                            alertDialog[0].setCancelable(true);
                            alertDialog[0].show();
                            imageViewer.requestFocus();
                        }

//                        launchIntent  = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BUTTON8);
//                        startLockedActivity(Constants.Storage.LOCK_STATUS_BUTTON8.name(), launchIntent );
//                        dialog.show();
//                        showdialog(Constants.APPID_BUTTON8, Constants.Storage.LOCK_STATUS_BUTTON8.name(), Constants.DEF_APPID_BUTTON8, ivButton8);
                        launchIntent = null;
                    }
                    break;
                case R.id.ivButton9:
                    if (SharedPreferenceStorage.getBooleanValue(this, "9" + Constants.Storage._isDeleted.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "9" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        Toast.makeText(this, R.string.this_app_uninstalled, Toast.LENGTH_SHORT).show();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "9" + Constants.Storage._isAppHidden.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {

                        String type = SharedPreferenceStorage.getStringValue(this, "btn9_type", "apk");
                        Log.i(TAG, "onClick: button9" + type);
                        if (type.equalsIgnoreCase("apk")) {
                            launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BUTTON9);
                            startLockedActivity(Constants.Storage.LOCK_STATUS_BUTTON9.name(), launchIntent);
                        } else if (type.equalsIgnoreCase("png")) {
                            final AlertDialog[] alertDialog = {null};
                            String url = SharedPreferenceStorage.getStringValue(this, "btn9_help_url", "");
                            Log.i(TAG, "onClick: button9" + url);
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.image_viewer, null);
                            dialogBuilder.setView(dialogView);
                            final ImageView imageViewer = dialogView.findViewById(R.id.ivImageViewer);
                            Picasso.get().load(url).into(imageViewer);
                            alertDialog[0] = dialogBuilder.create();
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(alertDialog[0].getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                            alertDialog[0].getWindow().setAttributes(lp);
                            alertDialog[0].setCancelable(true);
                            alertDialog[0].show();
                            imageViewer.requestFocus();
                        }
//                        launchIntent  = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BUTTON9);
//                        startLockedActivity(Constants.Storage.LOCK_STATUS_BUTTON9.name(), launchIntent );
//                        dialog.show();
//                        showdialog(Constants.APPID_BUTTON9, Constants.Storage.LOCK_STATUS_BUTTON9.name(), Constants.DEF_APPID_BUTTON9, ivButton9);
                        launchIntent = null;
                    }
                    break;
                case R.id.ivButton10:
                    if (SharedPreferenceStorage.getBooleanValue(this, "10" + Constants.Storage._isDeleted.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "10" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        Toast.makeText(this, R.string.this_app_uninstalled, Toast.LENGTH_SHORT).show();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "10" + Constants.Storage._isAppHidden.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {

                        String type = SharedPreferenceStorage.getStringValue(this, "btn10_type", "apk");
                        if (type.equalsIgnoreCase("apk")) {
                            launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BUTTON10);
                            startLockedActivity(Constants.Storage.LOCK_STATUS_BUTTON10.name(), launchIntent);
                        } else if (type.equalsIgnoreCase("png")) {
                            final AlertDialog[] alertDialog = {null};
                            String url = SharedPreferenceStorage.getStringValue(this, "btn10_help_url", "");
                            Log.i(TAG, "onClick: button10" + url);
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.image_viewer, null);
                            dialogBuilder.setView(dialogView);
                            final ImageView imageViewer = dialogView.findViewById(R.id.ivImageViewer);
                            Picasso.get().load(url).into(imageViewer);
                            alertDialog[0] = dialogBuilder.create();
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(alertDialog[0].getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                            alertDialog[0].getWindow().setAttributes(lp);
                            alertDialog[0].setCancelable(true);
                            alertDialog[0].show();
                            imageViewer.requestFocus();
                        }
//
//                       launchIntent  = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BUTTON10);
//                        startLockedActivity(Constants.Storage.LOCK_STATUS_BUTTON10.name(), launchIntent );
//                        dialog.show();
//                        showdialog(Constants.APPID_BUTTON10, Constants.Storage.LOCK_STATUS_BUTTON10.name(), Constants.DEF_APPID_BUTTON10, ivButton10);
                        launchIntent = null;
                    }
                    break;
                case R.id.ivButton11:
                    if (SharedPreferenceStorage.getBooleanValue(this, "11" + Constants.Storage._isDeleted.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "11" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        Toast.makeText(this, R.string.this_app_uninstalled, Toast.LENGTH_SHORT).show();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "11" + Constants.Storage._isAppHidden.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {

                        String type = SharedPreferenceStorage.getStringValue(this, "btn11_type", "apk");
                        if (type.equalsIgnoreCase("apk")) {
                            launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BUTTON11);
                            startLockedActivity(Constants.Storage.LOCK_STATUS_BUTTON11.name(), launchIntent);
                        } else if (type.equalsIgnoreCase("png")) {
                            final AlertDialog[] alertDialog = {null};
                            String url = SharedPreferenceStorage.getStringValue(this, "btn11_help_url", "");
                            Log.i(TAG, "onClick: button11" + url);
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.image_viewer, null);
                            dialogBuilder.setView(dialogView);
                            final ImageView imageViewer = dialogView.findViewById(R.id.ivImageViewer);
                            Picasso.get().load(url).into(imageViewer);
                            alertDialog[0] = dialogBuilder.create();
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(alertDialog[0].getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                            alertDialog[0].getWindow().setAttributes(lp);
                            alertDialog[0].setCancelable(true);
                            alertDialog[0].show();
                            imageViewer.requestFocus();
                        }
//                      launchIntent  = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BUTTON11);
//                        startLockedActivity(Constants.Storage.LOCK_STATUS_BUTTON11.name(), launchIntent );
//                        dialog.show();
//                        showdialog(Constants.APPID_BUTTON11, Constants.Storage.LOCK_STATUS_BUTTON11.name(), Constants.DEF_APPID_BUTTON11, ivButton11);
                        launchIntent = null;
                    }
                    break;
                case R.id.ivButton6:
                    if (SharedPreferenceStorage.getBooleanValue(this, "6" + Constants.Storage._isDeleted.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "6" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        Toast.makeText(this, R.string.this_app_uninstalled, Toast.LENGTH_SHORT).show();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "6" + Constants.Storage._isAppHidden.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {
                        if (Constants.APPID_BUTTON6.equalsIgnoreCase(Constants.DEF_APPID_BUTTON6)) {
                            Intent appAppsPageIntent = new Intent(this, AllAppsActivity.class);
                            appAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), false);
                            startActivity(appAppsPageIntent);
                        } else {

                            String type = SharedPreferenceStorage.getStringValue(this, "btn6_type", "apk");
                            if (type.equalsIgnoreCase("apk")) {
                                launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BUTTON6);
                                startLockedActivity(Constants.Storage.LOCK_STATUS_BUTTON6.name(), launchIntent);
                            } else if (type.equalsIgnoreCase("png")) {
                                final AlertDialog[] alertDialog = {null};
                                String url = SharedPreferenceStorage.getStringValue(this, "btn6_help_url", "");
                                Log.i(TAG, "onClick: button6" + url);
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                                LayoutInflater inflater = getLayoutInflater();
                                View dialogView = inflater.inflate(R.layout.image_viewer, null);
                                dialogBuilder.setView(dialogView);
                                final ImageView imageViewer = dialogView.findViewById(R.id.ivImageViewer);
                                Picasso.get().load(url).into(imageViewer);
                                alertDialog[0] = dialogBuilder.create();
                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(alertDialog[0].getWindow().getAttributes());
                                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                                alertDialog[0].getWindow().setAttributes(lp);
                                alertDialog[0].setCancelable(true);
                                alertDialog[0].show();
                                imageViewer.requestFocus();
                            }
//                            launchIntent  = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BUTTON6);
//                            startLockedActivity(Constants.Storage.LOCK_STATUS_BUTTON6.name(), launchIntent );
//                            dialog.show();
//                            showdialog(Constants.APPID_BUTTON6, Constants.Storage.LOCK_STATUS_BUTTON6.name(), Constants.DEF_APPID_BUTTON6, ivButton6);

                            launchIntent = null;
                        }
                    }
                    break;
                case R.id.ivBottomAddRemoveBtn:
                    if (Constants.APPID_DOCK.equalsIgnoreCase(Constants.DEF_APPID_DOCK)) {
                        Intent appAppsPageIntentNew = new Intent(this, AllAppsActivity.class);
                        appAppsPageIntentNew.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                        startActivity(appAppsPageIntentNew);
                    } else {
//                        launchIntent  = getPackageManager().getLaunchIntentForPackage(Constants.APPID_DOCK);
//                        startLockedActivity(Constants.Storage.LOCK_STATUS_ADD_REMOVE.name(), launchIntent );
//                        dialog.show();
//                        showdialog(Constants.APPID_DOCK, Constants.Storage.LOCK_STATUS_ADD_REMOVE.name(), Constants.DEF_APPID_DOCK, ivAddRemoveBtn);
                        launchIntent = null;
                    }
                    break;
                case R.id.ivAppLockBtn:
                    if (SharedPreferenceStorage.getBooleanValue(this, "21" + Constants.Storage._isDeleted.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "21" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        showAppLockSettingsDialog();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "21" + Constants.Storage._isAppHidden.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();

                    } else {
                        if (Constants.APPID_PARENTAL_BUTTON.equals("PARENTAL_BUTTON")) {
                            showAppLockSettingsDialog();
                        } else {
                            launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_PARENTAL_BUTTON);
                            startLockedActivity(Constants.Storage.LOCK_STATUS_ADD_REMOVE.name(), launchIntent);
                        }
//                        launchIntent  = getPackageManager().getLaunchIntentForPackage(Constants.APPID_PARENTAL_BUTTON);
//                        startLockedActivity(Constants.Storage.LOCK_STATUS_ADD_REMOVE.name(), launchIntent );
//                        HelperUtils.launchBluetoothSettings();
                    }

                    break;
                case R.id.ivUpdateBtn:
//                    downloadAllApps();
                    checkAllApps();
                    break;
                case R.id.ivHelpBtn:
//                    String url = SharedPreferenceStorage.getStringValue(this,"btn0_help_url", "");;
//
//                    Log.i(TAG, "onClick: show url "+url);
                    if (SharedPreferenceStorage.getBooleanValue(this, "22" + Constants.Storage._isDeleted.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "22" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        startActivity(new Intent(this, HelpActivity.class));
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "22" + Constants.Storage._isAppHidden.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {
                        if (Constants.APPID_HELP_BUTTON.equals("HELP_BUTTON")) {
                            startActivity(new Intent(this, HelpActivity.class));
                        } else {

                            launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_HELP_BUTTON);
                            startLockedActivity(Constants.Storage.LOCK_STATUS_ADD_REMOVE.name(), launchIntent);
                        }
//                        launchIntent  = getPackageManager().getLaunchIntentForPackage(Constants.APPID_HELP_BUTTON);
//                        startLockedActivity(Constants.Storage.LOCK_STATUS_ADD_REMOVE.name(), launchIntent );
//                        HelperUtils.launchBluetoothSettings();
                    }

                    break;
                case R.id.rlWebViewRssContainer:
                case R.id.rlWebViewRssClickBtn:
                    if (app.getHtmlRssFeedInfo() != null && app.getHtmlRssFeedInfo().getFull_rss_link() != null) {
                        openFullWebView(app.getHtmlRssFeedInfo().getFull_rss_link());
                    }
                    break;

                case R.id.btn_left1:
                    doShowCustomDynamicAds(Constants.UpdatableAppNameKey.btn_left1.name());
                    break;
                case R.id.btn_left2:
                    doShowCustomDynamicAds(Constants.UpdatableAppNameKey.btn_left2.name());
                    break;
                case R.id.btn_left3:
                    doShowCustomDynamicAds(Constants.UpdatableAppNameKey.btn_left3.name());
                    break;
                case R.id.btn_right1:
                    doShowCustomDynamicAds(Constants.UpdatableAppNameKey.btn_right1.name());
                    break;
                case R.id.btn_right2:
                    doShowCustomDynamicAds(Constants.UpdatableAppNameKey.btn_right2.name());
                    break;
                case R.id.btn_right3:
                    doShowCustomDynamicAds(Constants.UpdatableAppNameKey.btn_right3.name());
                    break;
                case R.id.btn_left_top:
                    doShowCustomDynamicAds(Constants.UpdatableAppNameKey.btn_left_top.name());
                    break;
                case R.id.btn_left_top_right:
                    doShowCustomDynamicAds(Constants.UpdatableAppNameKey.btn_left_top.name());
                    break;
                case R.id.ivBluetoothState:
                    if (SharedPreferenceStorage.getBooleanValue(this, "20" + Constants.Storage._isDeleted.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "20" + Constants.Storage._isAppHiddenByServer.name(), false)) {
                        launchBluetoothSettings();
                    } else if (SharedPreferenceStorage.getBooleanValue(this, "20" + Constants.Storage._isAppHidden.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {
                        if (Constants.APPID_BLUETOOTH_BUTTON.equals("BLUETOOTH_BUTTON")) {
                            launchBluetoothSettings();
                        } else {
                            launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BLUETOOTH_BUTTON);
                            startLockedActivity(Constants.Storage.LOCK_STATUS_ADD_REMOVE.name(), launchIntent);
                        }
//                        launchIntent  = getPackageManager().getLaunchIntentForPackage(Constants.APPID_BLUETOOTH_BUTTON);
//                        startLockedActivity(Constants.Storage.LOCK_STATUS_ADD_REMOVE.name(), launchIntent );
//                        HelperUtils.launchBluetoothSettings();
                    }

                    Toast.makeText(this, "bluetooth", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.ivWifiState:
                    if (SharedPreferenceStorage.getBooleanValue(this, "19" + Constants.Storage._isDeleted.name(), false)
                            || SharedPreferenceStorage.getBooleanValue(this, "19" + Constants.Storage._isAppHiddenByServer.name(), false)) {

                    } else if (SharedPreferenceStorage.getBooleanValue(this, "19" + Constants.Storage._isAppHidden.name(), false)) {
                        Toast.makeText(this, R.string.this_app_hidden, Toast.LENGTH_SHORT).show();
                    } else {
                        if (Constants.APPID_WIFI_BUTTON.equals("WIFI_BUTTON")) {
                            HelperUtils.launchNetworkSettings(this);
                        } else {
                            launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_WIFI_BUTTON);
                            startLockedActivity(Constants.Storage.LOCK_STATUS_ADD_REMOVE.name(), launchIntent);
                        }
//                        launchIntent  = getPackageManager().getLaunchIntentForPackage(Constants.APPID_WIFI_BUTTON);
//                        startLockedActivity(Constants.Storage.LOCK_STATUS_ADD_REMOVE.name(), launchIntent );
//                        HelperUtils.launchBluetoothSettings();
                    }
                case R.id.ivLanState:
                    HelperUtils.launchNetworkSettings(this);
                    break;
                case R.id.rlTextClock:
                    launchDateSettings();
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(this, getResources().getString(R.string.app_is_not_installed), Toast.LENGTH_LONG).show();
            //e.printStackTrace();
        }
    }

    //    public void showdialog(final String s1, final String s2, final String pakage_name, final ImageView ivButtonTop) {
//        dialog = new Dialog(HomePageActivity.this);
//        dialog.setContentView(R.layout.single_press_new_dialog);
//        dialog.show();
//        final Intent[] launchIntent = {null};
//        run_apk = dialog.findViewById(R.id.run_apk);
//        play_vlc = dialog.findViewById(R.id.play_vlc);
//        show_png = dialog.findViewById(R.id.display_png);
//        open_chrome = dialog.findViewById(R.id.open_url_chrome);
//        run_apk.setOnFocusChangeListener(this);
//        play_vlc.setOnFocusChangeListener(this);
//        open_chrome.setOnFocusChangeListener(this);
//        show_png.setOnFocusChangeListener(this);
//
//        run_apk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    launchIntent[0] = getPackageManager().getLaunchIntentForPackage(s1);
//                    startLockedActivity(s2, launchIntent[0]);
////                    dialog.dismiss();
//
//                } catch (Exception e) {
////                    dialog.dismiss();
//                    Toast.makeText(HomePageActivity.this, getResources().getString(R.string.app_is_not_installed), Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//        show_png.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PackageManager manager = getApplicationContext().getPackageManager();
//                PackageInfo info = null;
//                try {
//                    info = manager.getPackageInfo(
//                            pakage_name, 0);
//                    String version = info.versionName;
//
//                    final AlertDialog[] alertDialog = {null};
//                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HomePageActivity.this);
//                    LayoutInflater inflater = LayoutInflater.from(HomePageActivity.this);
//                    View dialogView = inflater.inflate(R.layout.image_viewer, null);
//                    dialogBuilder.setView(dialogView);
//
//                    final ImageView imageViewer = dialogView.findViewById(R.id.ivImageViewer);
//                    try {
//                        imageViewer.setImageDrawable(getIconFormPkgName(pakage_name));
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//
//                    alertDialog[0] = dialogBuilder.create();
//                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                    lp.copyFrom(alertDialog[0].getWindow().getAttributes());
//                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//                    alertDialog[0].getWindow().setAttributes(lp);
//                    alertDialog[0].setCancelable(true);
//                    alertDialog[0].show();
////                    dialog.dismiss();
//
//                } catch (PackageManager.NameNotFoundException e) {
//
////                    dialog.dismiss();
//                    Toast.makeText(HomePageActivity.this, getResources().getString(R.string.app_is_not_installed), Toast.LENGTH_SHORT).show();
//                }
//
//
//
//
//            }
//        });
//
//    }
    private void compareAppVersion() {
        pd.dismiss();
        is_show = true;

        try {

            if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button0", "").equals("")) {

            } else {

                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button0", "")).into(ivButtonTop);
            }
            if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left_top", "").equals("")) {

            } else {

                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left_top", "")).into(btn_left_top);
            }
            if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left_top_right", "").equals("")) {

            } else {

                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left_top_right", "")).into(btn_left_top_right);
            }
            if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left1", "").equals("")) {

            } else {

                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left1", "")).into(btn_left1);
            }
            if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left2", "").equals("")) {

            } else {

                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left2", "")).into(btn_left2);
            }
            if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left3", "").equals("")) {

            } else {

                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left3", "")).into(btn_left3);
            }

            if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_right1", "").equals("")) {

            } else {

                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_right1", "")).into(btn_right1);
            }
            if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_right2", "").equals("")) {

            } else {

                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_right2", "")).into(btn_right2);
            }
            if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_right3", "").equals("")) {

            } else {

                Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_right3", "")).into(btn_right3);
            }


            PackageManager manager = getApplicationContext().getPackageManager();
            PackageInfo info = null;

            try {
                info = manager.getPackageInfo(
                        Constants.APPID_BUTTON1, 0);
                String version = info.versionName;
                Version a = new Version(version);
                Version b = new Version(SharedPreferenceStorage.getStringValue(this, "btn1_version", ""));
                if (a.compareTo(b) < 0) {
                    ivButton1_up_req.setVisibility(View.VISIBLE);
                    ivButton1_up_req.setBackgroundResource(R.drawable.b_updateavailable);
                } else {
                    ivButton1_up_req.setVisibility(View.GONE);
                }

            } catch (PackageManager.NameNotFoundException e) {
            }
            try {
                info = manager.getPackageInfo(
                        Constants.APPID_BUTTON2, 0);
                String version = info.versionName;
                Version a = new Version(version);
                Version b = new Version(SharedPreferenceStorage.getStringValue(this, "btn2_version", ""));
                if (a.compareTo(b) < 0) {
                    ivButton2_up_req.setVisibility(View.VISIBLE);
                    ivButton2_up_req.setBackgroundResource(R.drawable.b_updateavailable);
                } else {
                    ivButton2_up_req.setVisibility(View.GONE);
                }
            } catch (PackageManager.NameNotFoundException e) {
            }
            try {
                info = manager.getPackageInfo(
                        Constants.APPID_BUTTON3, 0);
                String version = info.versionName;
                Version a = new Version(version);
                Version b = new Version(SharedPreferenceStorage.getStringValue(this, "btn3_version", ""));
                if (a.compareTo(b) < 0) {
                    ivButton3_up_req.setVisibility(View.VISIBLE);
                    ivButton3_up_req.setBackgroundResource(R.drawable.b_updateavailable);
                } else {
                    ivButton3_up_req.setVisibility(View.GONE);
                }
            } catch (PackageManager.NameNotFoundException e) {
            }
            try {
                info = manager.getPackageInfo(
                        Constants.APPID_BUTTON4, 0);
                String version = info.versionName;

                Version a = new Version(version);
                Version b = new Version(SharedPreferenceStorage.getStringValue(this, "btn4_version", ""));
                if (a.compareTo(b) < 0) {
                    ivButton4_up_req.setVisibility(View.VISIBLE);
                    ivButton4_up_req.setBackgroundResource(R.drawable.b_updateavailable);
                } else {
                    ivButton4_up_req.setVisibility(View.GONE);
                }
            } catch (PackageManager.NameNotFoundException e) {
            }
            try {
                info = manager.getPackageInfo(
                        Constants.APPID_BUTTON5, 0);
                String version = info.versionName;
                Version a = new Version(version);
                Version b = new Version(SharedPreferenceStorage.getStringValue(this, "btn5_version", ""));
//                Version a=new Version("3.0.9.1");
//                Version b=new Version("3.0.9.1");
                if (a.compareTo(b) < 0) {
                    ivButton5_up_req.setVisibility(View.VISIBLE);
                    ivButton5_up_req.setBackgroundResource(R.drawable.b_updateavailable);
                } else {
                    ivButton5_up_req.setVisibility(View.GONE);
                }
            } catch (PackageManager.NameNotFoundException e) {
            }
            try {
                info = manager.getPackageInfo(
                        Constants.APPID_BUTTON6, 0);
                String version = info.versionName;
                Version a = new Version(version);
                Version b = new Version(SharedPreferenceStorage.getStringValue(this, "btn6_version", ""));
                if (a.compareTo(b) < 0) {
                    ivButton6_up_req.setVisibility(View.VISIBLE);
                    ivButton6_up_req.setBackgroundResource(R.drawable.b_updateavailable);
                } else {
                    ivButton6_up_req.setVisibility(View.GONE);
                }
            } catch (PackageManager.NameNotFoundException e) {
            }
            try {
                info = manager.getPackageInfo(
                        Constants.APPID_BUTTON7, 0);
                String version = info.versionName;
                Version a = new Version(version);
                Version b = new Version(SharedPreferenceStorage.getStringValue(this, "btn7_version", ""));
                if (a.compareTo(b) < 0) {
                    ivButton7_up_req.setVisibility(View.VISIBLE);
                    ivButton7_up_req.setBackgroundResource(R.drawable.b_updateavailable);
                } else {
                    ivButton7_up_req.setVisibility(View.GONE);
                }
            } catch (PackageManager.NameNotFoundException e) {
            }
            try {
                info = manager.getPackageInfo(
                        Constants.APPID_BUTTON8, 0);
                String version = info.versionName;
                Version a = new Version(version);
                Version b = new Version(SharedPreferenceStorage.getStringValue(this, "btn8_version", ""));
                if (a.compareTo(b) < 0) {
                    ivButton8_up_req.setVisibility(View.VISIBLE);
                    ivButton8_up_req.setBackgroundResource(R.drawable.b_updateavailable);
                } else {
                    ivButton8_up_req.setVisibility(View.GONE);
                }
            } catch (PackageManager.NameNotFoundException e) {
            }
            try {
                info = manager.getPackageInfo(
                        Constants.APPID_BUTTON9, 0);
                String version = info.versionName;
                Version a = new Version(version);
                Log.i(TAG, "compareAppVersion: " + version);
                Version b = new Version(SharedPreferenceStorage.getStringValue(this, "btn9_version", ""));

                if (a.compareTo(b) < 0) {
                    ivButton9_up_req.setVisibility(View.VISIBLE);
                    ivButton9_up_req.setBackgroundResource(R.drawable.b_updateavailable);
                } else {
                    ivButton9_up_req.setVisibility(View.GONE);
                }
            } catch (PackageManager.NameNotFoundException e) {
            }
            try {
                info = manager.getPackageInfo(
                        Constants.APPID_BUTTON10, 0);
                String version = info.versionName;
                Version a = new Version(version);
                Version b = new Version(SharedPreferenceStorage.getStringValue(this, "btn10_version", "0"));
                if (a.compareTo(b) < 0) {
                    ivButton10_up_req.setVisibility(View.VISIBLE);
                    ivButton10_up_req.setBackgroundResource(R.drawable.b_updateavailable);
                } else {
                    ivButton10_up_req.setVisibility(View.GONE);
                }
            } catch (PackageManager.NameNotFoundException e) {
            }
            try {
                info = manager.getPackageInfo(
                        Constants.APPID_BUTTON11, 0);
                String version = info.versionName;
                Version a = new Version(version);
                Version b = new Version(SharedPreferenceStorage.getStringValue(this, "btn11_version", "0"));
                if (a.compareTo(b) < 0) {
                    ivButton11_up_req.setVisibility(View.VISIBLE);
                    ivButton11_up_req.setBackgroundResource(R.drawable.b_updateavailable);
                } else {
                    ivButton11_up_req.setVisibility(View.GONE);
                }
            } catch (PackageManager.NameNotFoundException e) {
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void checkAllApps() {
        int totalUninstalledPkgs = 0;
        PackageManager manager = getApplicationContext().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    Constants.APPID_BUTTON1, 0);
            String version = info.versionName;
            Log.i(TAG, "checkAllApps: " + version);
            totalUninstalledPkgs = 0;

        } catch (PackageManager.NameNotFoundException e) {

            Log.i(TAG, "checkAllApps: crash 1 ");
            totalUninstalledPkgs = 1;
        }

        try {
            info = manager.getPackageInfo(
                    Constants.APPID_BUTTON2, 0);
            String version = info.versionName;
            Log.i(TAG, "checkAllApps: " + version);
            totalUninstalledPkgs = totalUninstalledPkgs;

        } catch (PackageManager.NameNotFoundException e) {
            Log.i(TAG, "checkAllApps: crash 2 ");
            totalUninstalledPkgs = totalUninstalledPkgs + 1;
        }


        try {
            info = manager.getPackageInfo(
                    Constants.APPID_BUTTON3, 0);
            String version = info.versionName;
            Log.i(TAG, "checkAllApps: " + version);
            totalUninstalledPkgs = totalUninstalledPkgs;

        } catch (PackageManager.NameNotFoundException e) {

            Log.i(TAG, "checkAllApps: crash 3 ");
            totalUninstalledPkgs = totalUninstalledPkgs + 1;
        }


        try {
            info = manager.getPackageInfo(
                    Constants.APPID_BUTTON4, 0);
            String version = info.versionName;
            Log.i(TAG, "checkAllApps: " + version);
            totalUninstalledPkgs = totalUninstalledPkgs;

        } catch (PackageManager.NameNotFoundException e) {
            Log.i(TAG, "checkAllApps: crash 4 ");
            totalUninstalledPkgs = totalUninstalledPkgs + 1;
        }


        try {
            info = manager.getPackageInfo(
                    Constants.APPID_BUTTON5, 0);
            String version = info.versionName;
            Log.i(TAG, "checkAllApps: " + version);
            totalUninstalledPkgs = totalUninstalledPkgs;

        } catch (PackageManager.NameNotFoundException e) {
            Log.i(TAG, "checkAllApps: crash 5 ");
            totalUninstalledPkgs = totalUninstalledPkgs + 1;
        }


        try {
            info = manager.getPackageInfo(
                    Constants.APPID_BUTTON6, 0);
            String version = info.versionName;
            Log.i(TAG, "checkAllApps: " + version);
            totalUninstalledPkgs = totalUninstalledPkgs;

        } catch (PackageManager.NameNotFoundException e) {
            Log.i(TAG, "checkAllApps: crash 6 ");
            totalUninstalledPkgs = totalUninstalledPkgs + 1;
        }


        try {
            info = manager.getPackageInfo(
                    Constants.APPID_BUTTON7, 0);
            String version = info.versionName;
            Log.i(TAG, "checkAllApps: " + version);
            totalUninstalledPkgs = totalUninstalledPkgs;

        } catch (PackageManager.NameNotFoundException e) {
            Log.i(TAG, "checkAllApps: crash 7 ");
            totalUninstalledPkgs = totalUninstalledPkgs + 1;
        }


        try {
            info = manager.getPackageInfo(
                    Constants.APPID_BUTTON8, 0);
            String version = info.versionName;
            Log.i(TAG, "checkAllApps: " + version);
            totalUninstalledPkgs = totalUninstalledPkgs;

        } catch (PackageManager.NameNotFoundException e) {
            Log.i(TAG, "checkAllApps: crash 8 ");
            totalUninstalledPkgs = totalUninstalledPkgs + 1;
        }


        try {
            info = manager.getPackageInfo(
                    Constants.APPID_BUTTON9, 0);
            String version = info.versionName;
            Log.i(TAG, "checkAllApps: " + version);
            totalUninstalledPkgs = totalUninstalledPkgs;

        } catch (PackageManager.NameNotFoundException e) {
            Log.i(TAG, "checkAllApps: crash 9 ");
            totalUninstalledPkgs = totalUninstalledPkgs + 1;
        }


        try {
            info = manager.getPackageInfo(
                    Constants.APPID_BUTTON10, 0);
            String version = info.versionName;
            Log.i(TAG, "checkAllApps: " + version);
            totalUninstalledPkgs = totalUninstalledPkgs;

        } catch (PackageManager.NameNotFoundException e) {
            Log.i(TAG, "checkAllApps: crash 10 ");
            totalUninstalledPkgs = totalUninstalledPkgs + 1;
        }


        try {
            info = manager.getPackageInfo(
                    Constants.APPID_BUTTON11, 0);
            String version = info.versionName;
            Log.i(TAG, "checkAllApps: " + version);
            totalUninstalledPkgs = totalUninstalledPkgs;

        } catch (PackageManager.NameNotFoundException e) {
            Log.i(TAG, "checkAllApps: crash 11 ");
            totalUninstalledPkgs = totalUninstalledPkgs + 1;
        }

        Log.i(TAG, "checkAllApps: TOTAL PKG " + totalUninstalledPkgs);
        SharedPreferenceStorage.setStringValue(HomePageActivity.this, "totalUninstalledPkgs", totalUninstalledPkgs + "");
        downloadAllApps(totalUninstalledPkgs);


    }

    private void downloadAllApps(int totalUninstalledPkgs) {
        PackageManager manager = getApplicationContext().getPackageManager();
        PackageInfo info = null;
        int installedPkgs = 0;

        try {
            info = manager.getPackageInfo(
                    Constants.APPID_BUTTON1, 0);
            String version = info.versionName;

        } catch (PackageManager.NameNotFoundException e) {

            if (HelperUtils.haveNetworkConnection(HomePageActivity.this) > 0) {

                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        // Your Permission granted already .Do next code
                        Log.i("TAG", "onClick: if");
                        installedPkgs = installedPkgs + 1;
                        doDownloadAndInstallToNewVersion(ivButton1, installedPkgs, totalUninstalledPkgs);

                    } else {
                        Log.i("TAG", "onClick: else");
                        requestPermission(ivButton1); // Code for permission
                    }
                } else {
                    Log.i("TAG", "onClick: else else");
                    installedPkgs = installedPkgs + 1;
                    doDownloadAndInstallToNewVersion(ivButton1, installedPkgs, totalUninstalledPkgs);

                    // Do next code
                }
            }
        }
        try {
            info = manager.getPackageInfo(
                    Constants.APPID_BUTTON_TOP_NEW, 0);
            String version = info.versionName;

        } catch (PackageManager.NameNotFoundException e) {

            if (HelperUtils.haveNetworkConnection(HomePageActivity.this) > 0) {

                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        // Your Permission granted already .Do next code
                        Log.i("TAG", "onClick: if");
                        installedPkgs = installedPkgs + 1;
                        doDownloadAndInstallToNewVersion(ivButtonTopNew, installedPkgs, totalUninstalledPkgs);

                    } else {
                        Log.i("TAG", "onClick: else");
                        requestPermission(ivButtonTopNew); // Code for permission
                    }
                } else {
                    Log.i("TAG", "onClick: else else");
                    installedPkgs = installedPkgs + 1;
                    doDownloadAndInstallToNewVersion(ivButtonTopNew, installedPkgs, totalUninstalledPkgs);

                    // Do next code
                }
            }
        }
        try {
            info = manager.getPackageInfo(
                    Constants.APPID_BUTTON2, 0);
            String version = info.versionName;

        } catch (PackageManager.NameNotFoundException e) {

            if (HelperUtils.haveNetworkConnection(HomePageActivity.this) > 0) {

                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        // Your Permission granted already .Do next code
                        Log.i("TAG", "onClick: if");
                        installedPkgs = installedPkgs + 1;
                        doDownloadAndInstallToNewVersion(ivButton2, installedPkgs, totalUninstalledPkgs);

                    } else {
                        Log.i("TAG", "onClick: else");
                        requestPermission(ivButton2); // Code for permission
                    }
                } else {
                    Log.i("TAG", "onClick: else else");
                    installedPkgs = installedPkgs + 1;
                    doDownloadAndInstallToNewVersion(ivButton2, installedPkgs, totalUninstalledPkgs);

                    // Do next code
                }
            }
        }
        try {
            info = manager.getPackageInfo(
                    Constants.APPID_BUTTON3, 0);
            String version = info.versionName;

        } catch (PackageManager.NameNotFoundException e) {

            if (HelperUtils.haveNetworkConnection(HomePageActivity.this) > 0) {

                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        // Your Permission granted already .Do next code
                        Log.i("TAG", "onClick: if");
                        installedPkgs = installedPkgs + 1;
                        doDownloadAndInstallToNewVersion(ivButton3, installedPkgs, totalUninstalledPkgs);

                    } else {
                        Log.i("TAG", "onClick: else");
                        requestPermission(ivButton3); // Code for permission
                    }
                } else {
                    Log.i("TAG", "onClick: else else");
                    installedPkgs = installedPkgs + 1;
                    doDownloadAndInstallToNewVersion(ivButton3, installedPkgs, totalUninstalledPkgs);

                    // Do next code
                }
            }
        }
        try {
            info = manager.getPackageInfo(
                    Constants.APPID_BUTTON4, 0);
            String version = info.versionName;

        } catch (PackageManager.NameNotFoundException e) {

            if (HelperUtils.haveNetworkConnection(HomePageActivity.this) > 0) {

                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        // Your Permission granted already .Do next code
                        Log.i("TAG", "onClick: if");
                        installedPkgs = installedPkgs + 1;
                        doDownloadAndInstallToNewVersion(ivButton4, installedPkgs, totalUninstalledPkgs);

                    } else {
                        Log.i("TAG", "onClick: else");
                        requestPermission(ivButton4); // Code for permission
                    }
                } else {
                    Log.i("TAG", "onClick: else else");
                    installedPkgs = installedPkgs + 1;
                    doDownloadAndInstallToNewVersion(ivButton4, installedPkgs, totalUninstalledPkgs);

                    // Do next code
                }
            }
        }
        try {
            info = manager.getPackageInfo(
                    Constants.APPID_BUTTON5, 0);
            String version = info.versionName;

        } catch (PackageManager.NameNotFoundException e) {

            if (HelperUtils.haveNetworkConnection(HomePageActivity.this) > 0) {

                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        // Your Permission granted already .Do next code
                        Log.i("TAG", "onClick: if");
                        installedPkgs = installedPkgs + 1;
                        doDownloadAndInstallToNewVersion(ivButton5, installedPkgs, totalUninstalledPkgs);

                    } else {
                        Log.i("TAG", "onClick: else");
                        requestPermission(ivButton5); // Code for permission
                    }
                } else {
                    Log.i("TAG", "onClick: else else");
                    installedPkgs = installedPkgs + 1;
                    doDownloadAndInstallToNewVersion(ivButton5, installedPkgs, totalUninstalledPkgs);

                    // Do next code
                }
            }
        }
        try {
            info = manager.getPackageInfo(
                    Constants.APPID_BUTTON6, 0);
            String version = info.versionName;

        } catch (PackageManager.NameNotFoundException e) {

            if (HelperUtils.haveNetworkConnection(HomePageActivity.this) > 0) {

                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        // Your Permission granted already .Do next code
                        Log.i("TAG", "onClick: if");
                        installedPkgs = installedPkgs + 1;
                        doDownloadAndInstallToNewVersion(ivButton6, installedPkgs, totalUninstalledPkgs);

                    } else {
                        Log.i("TAG", "onClick: else");
                        requestPermission(ivButton6); // Code for permission
                    }
                } else {
                    Log.i("TAG", "onClick: else else");
                    installedPkgs = installedPkgs + 1;
                    doDownloadAndInstallToNewVersion(ivButton6, installedPkgs, totalUninstalledPkgs);

                    // Do next code
                }
            }
        }
        try {
            info = manager.getPackageInfo(
                    Constants.APPID_BUTTON7, 0);
            String version = info.versionName;

        } catch (PackageManager.NameNotFoundException e) {

            if (HelperUtils.haveNetworkConnection(HomePageActivity.this) > 0) {

                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        // Your Permission granted already .Do next code
                        Log.i("TAG", "onClick: if");
                        installedPkgs = installedPkgs + 1;
                        doDownloadAndInstallToNewVersion(ivButton7, installedPkgs, totalUninstalledPkgs);

                    } else {
                        Log.i("TAG", "onClick: else");
                        requestPermission(ivButton7); // Code for permission
                    }
                } else {
                    Log.i("TAG", "onClick: else else");
                    installedPkgs = installedPkgs + 1;
                    doDownloadAndInstallToNewVersion(ivButton7, installedPkgs, totalUninstalledPkgs);

                    // Do next code
                }
            }
        }
        try {
            info = manager.getPackageInfo(
                    Constants.APPID_BUTTON8, 0);
            String version = info.versionName;

        } catch (PackageManager.NameNotFoundException e) {

            if (HelperUtils.haveNetworkConnection(HomePageActivity.this) > 0) {

                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        // Your Permission granted already .Do next code
                        Log.i("TAG", "onClick: if");
                        installedPkgs = installedPkgs + 1;
                        doDownloadAndInstallToNewVersion(ivButton8, installedPkgs, totalUninstalledPkgs);

                    } else {
                        Log.i("TAG", "onClick: else");
                        requestPermission(ivButton8); // Code for permission
                    }
                } else {
                    Log.i("TAG", "onClick: else else");
                    installedPkgs = installedPkgs + 1;
                    doDownloadAndInstallToNewVersion(ivButton8, installedPkgs, totalUninstalledPkgs);

                    // Do next code
                }
            }
        }
        try {
            info = manager.getPackageInfo(
                    Constants.APPID_BUTTON9, 0);
            String version = info.versionName;

        } catch (PackageManager.NameNotFoundException e) {

            if (HelperUtils.haveNetworkConnection(HomePageActivity.this) > 0) {

                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        // Your Permission granted already .Do next code
                        Log.i("TAG", "onClick: if");
                        installedPkgs = installedPkgs + 1;
                        doDownloadAndInstallToNewVersion(ivButton9, installedPkgs, totalUninstalledPkgs);

                    } else {
                        Log.i("TAG", "onClick: else");
                        requestPermission(ivButton9); // Code for permission
                    }
                } else {
                    Log.i("TAG", "onClick: else else");
                    installedPkgs = installedPkgs + 1;
                    doDownloadAndInstallToNewVersion(ivButton9, installedPkgs, totalUninstalledPkgs);

                    // Do next code
                }
            }
        }
        try {
            info = manager.getPackageInfo(
                    Constants.APPID_BUTTON10, 0);
            String version = info.versionName;

        } catch (PackageManager.NameNotFoundException e) {

            if (HelperUtils.haveNetworkConnection(HomePageActivity.this) > 0) {

                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        // Your Permission granted already .Do next code
                        Log.i("TAG", "onClick: if");
                        installedPkgs = installedPkgs + 1;
                        doDownloadAndInstallToNewVersion(ivButton10, installedPkgs, totalUninstalledPkgs);

                    } else {
                        Log.i("TAG", "onClick: else");
                        requestPermission(ivButton10); // Code for permission
                    }
                } else {
                    Log.i("TAG", "onClick: else else");
                    installedPkgs = installedPkgs + 1;
                    doDownloadAndInstallToNewVersion(ivButton10, installedPkgs, totalUninstalledPkgs);

                    // Do next code
                }
            }
        }
        try {
            info = manager.getPackageInfo(
                    Constants.APPID_BUTTON11, 0);
            String version = info.versionName;

        } catch (PackageManager.NameNotFoundException e) {

            if (HelperUtils.haveNetworkConnection(HomePageActivity.this) > 0) {


                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        // Your Permission granted already .Do next code
                        Log.i("TAG", "onClick: if");
                        installedPkgs = installedPkgs + 1;
                        doDownloadAndInstallToNewVersion(ivButton11, installedPkgs, totalUninstalledPkgs);

                    } else {
                        Log.i("TAG", "onClick: else");
                        requestPermission(ivButton11); // Code for permission
                    }
                } else {
                    Log.i("TAG", "onClick: else else");
                    installedPkgs = installedPkgs + 1;
                    doDownloadAndInstallToNewVersion(ivButton11, installedPkgs, totalUninstalledPkgs);

                    // Do next code
                }
            }
        }

    }

    private Drawable getIconFormPkgName(String pkg) {
        try {
            Drawable icon = getApplicationContext().getPackageManager().getApplicationIcon(pkg);
            return icon;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //return null;
        return null;
    }


    @Override
    public boolean onLongClick(final View view) {
        initSendInfo(view, System.currentTimeMillis());
        return true;
    }

    private void initSendInfo(final View view, final long startTime) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if (view.isPressed() && System.currentTimeMillis() - startTime >= (view.getId() == R.id.btn_settings ? 7000 : Constants.LONG_CLICK_DELAY)) {
                    Animation anim = AnimationUtils.loadAnimation(HomePageActivity.this, R.anim.zoom_out_edit);
                    view.startAnimation(anim);
                    anim.setFillAfter(true);
                    if (view.getId() == R.id.ivButtonTop) {
//                        showLongPressDialogTop(view);\
                        showDialogTop(HomePageActivity.this);
                    } else {

                        showLongPressDialog(view);
                    }

                    return;
                } else if (!view.isPressed()) {
                    return;
                }
            }
        }, (view.getId() == R.id.btn_settings ? 7000 : Constants.LONG_CLICK_DELAY));

    }

    public void showDialogTop(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.longpress2dialog);

        final TextView update_new_version = (TextView) dialog.findViewById(R.id.updateNewVersionBtn);
        final TextView clean_data = (TextView) dialog.findViewById(R.id.cleanAppDataBtn);
        final TextView choose_url = (TextView) dialog.findViewById(R.id.choose_url);

        update_new_version.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    update_new_version.setTextColor(getResources().getColor(R.color.white));
                    update_new_version.setBackgroundColor(getResources().getColor(R.color.grey));
                } else {
                    update_new_version.setTextColor(getResources().getColor(R.color.black));
                    update_new_version.setBackground(getResources().getDrawable(R.drawable.list_divider));
                }
            }
        });
        choose_url.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    choose_url.setTextColor(getResources().getColor(R.color.white));
                    choose_url.setBackgroundColor(getResources().getColor(R.color.grey));
                } else {
                    choose_url.setTextColor(getResources().getColor(R.color.black));
                    choose_url.setBackground(getResources().getDrawable(R.drawable.list_divider));
                }
            }
        });
        clean_data.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    clean_data.setTextColor(getResources().getColor(R.color.white));
                    clean_data.setBackgroundColor(getResources().getColor(R.color.grey));
                } else {
                    clean_data.setTextColor(getResources().getColor(R.color.black));
                    clean_data.setBackground(getResources().getDrawable(R.drawable.list_divider));
                }
            }
        });

        update_new_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateLauncherDialog();
                dialog.dismiss();
            }
        });
        choose_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doShowUpdateSettingsFileDialog();
                dialog.dismiss();
            }
        });
        clean_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearApplicationData();
                dialog.dismiss();

            }
        });


        dialog.show();

    }

    public void clearApplicationData() {
        SharedPreferences pref = SharedPreferenceStorage.getPreferences(HomePageActivity.this);
        pref.edit().clear().commit();
        File cacheDirectory = getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()) {
            String[] fileNames = applicationDirectory.list();
            for (String fileName : fileNames) {
                if (!fileName.equals("lib")) {
                    deleteFile(new File(applicationDirectory, fileName));
                }
            }
        }
    }

    public boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++) {
                    deletedAll = deleteFile(new File(file, children[i])) && deletedAll;
                }

            } else {
                deletedAll = file.delete();
            }
            finish();
        }

        return deletedAll;
    }

    private void showChangeDefaultIconDialog(final View view) {
        try {
            changeAppDialog = new AlertDialog.Builder(this);
            changeAppDialog.setMessage("Do you want to change Default app?")
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Animation anim = AnimationUtils.loadAnimation(HomePageActivity.this, R.anim.zoom_in_edit);
                            view.startAnimation(anim);
                            anim.setFillAfter(true);

                            dialog.cancel();
                            changeAppDialog = null;
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Animation anim = AnimationUtils.loadAnimation(HomePageActivity.this, R.anim.zoom_in_edit);
                            view.startAnimation(anim);
                            anim.setFillAfter(true);
                            dialog.cancel();
                            changeAppDialog = null;
                        }
                    });

            AlertDialog alertDialog = changeAppDialog.create();
            if (!isFinishing())
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

    private void goToAppSelectionActivity(View view) {
        try {
            Intent allAppsPageIntent = null;
            switch (view.getId()) {
                case R.id.ivVpnBtn:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_VPN_BUTTON);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_VPN_BUTTON);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivWifiState:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_WIFI_BUTTON);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_wifi_BUTTON);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivBluetoothState:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_BLUETOOTH_BUTTON);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_BLUETOOTH_BUTTON);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivAppLockBtn:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_PARENTAL_BUTTON);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_PARENTAL_BUTTON);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivHelpBtn:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_HELP_BUTTON);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_HELP_BUTTON);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivPoweBtn:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_PWR_BUTTON);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_PWR_BUTTON);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivMiniButton1:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_MiniButton1);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_MiniBUTTON1);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivMiniButton2:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_MiniButton2);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_MiniBUTTON2);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivMiniButton3:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_MiniButton3);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_MiniBUTTON3);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivMiniButton4:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_MiniButton4);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_MiniBUTTON4);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivMiniButton5:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_MiniButton5);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_MiniBUTTON5);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivMiniButton6:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_MiniButton6);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_MiniBUTTON6);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivMiniButton7:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_MiniButton7);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_MiniBUTTON7);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivMiniButton8:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_MiniButton8);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_MiniBUTTON8);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivMiniButton9:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_MiniButton9);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_MiniBUTTON9);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivMiniButton10:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_MiniButton10);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_MiniBUTTON10);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivMiniButton11:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_MiniButton11);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_MiniBUTTON11);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivMiniButton12:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_MiniButton12);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_MiniBUTTON12);
                    startActivity(allAppsPageIntent);
                    break;

                case R.id.ivButtonTop:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_BUTTON_TOP);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_BUTTON_TOP);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivTopBtnNew:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_BUTTON_TOP_NEW);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_BUTTON_TOP_NEW);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivButton4:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_BUTTON4);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_BUTTON4);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivButton3:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_BUTTON3);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_BUTTON3);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivButton1:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_BUTTON1);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_BUTTON1);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivButton2:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_BUTTON2);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_BUTTON2);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivButton5:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_BUTTON5);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_BUTTON5);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivButton7:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_BUTTON7);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_BUTTON7);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivButton9:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_BUTTON9);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_BUTTON9);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivButton8:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_BUTTON8);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_BUTTON8);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivButton10:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_BUTTON10);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_BUTTON10);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivButton11:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_BUTTON11);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_BUTTON11);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivButton6:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_BUTTON6);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_BUTTON6);
                    startActivity(allAppsPageIntent);
                    break;
                case R.id.ivBottomAddRemoveBtn:
                    allAppsPageIntent = new Intent(this, AllAppsActivity.class);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), true);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name(), Constants.APPID_DOCK);
                    allAppsPageIntent.putExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name(), Constants.DEF_APPID_DOCK);
                    startActivity(allAppsPageIntent);
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Sorry, Error Occured!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void setOrRefreshAppIcons(final String packageName) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (packageName != null && packageName.length() > 1) {
                        if (packageName.equalsIgnoreCase(Constants.APPID_BUTTON1)) {
                            if (Constants.APPID_BUTTON1.equalsIgnoreCase(Constants.DEF_APPID_BUTTON1)) {
                                if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn1_type", "").equals("apk")) {
                                    if (HelperUtils.isAppInstalled(HomePageActivity.this, packageName)) {
                                        Constants.icon_button1 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button1.name());
                                        Constants.icon_pressed_button1 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button1.name());
                                        btn1_appname.setVisibility(View.GONE);
                                    } else {
                                        Constants.icon_button1 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_blank_button.name());
                                        Constants.icon_pressed_button1 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_blank_button.name());
                                        btn1_appname.setVisibility(View.VISIBLE);
                                        btn1_appname.setText(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn1_title", ""));
                                    }
                                } else {
                                    Constants.icon_button1 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button1.name());
                                    Constants.icon_pressed_button1 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button1.name());
                                }
                            } else {
                                Drawable appBanner = getPackageManager().getApplicationIcon(Constants.APPID_BUTTON1);
                                if (appBanner == null)
                                    appBanner = getPackageManager().getApplicationBanner(Constants.APPID_BUTTON1);
                                Constants.icon_button1 = HelperUtils.getBitmapFromDrawable(appBanner, false);
                                Constants.icon_pressed_button1 = HelperUtils.getBitmapFromDrawable(appBanner, true);
                            }
                            onFocusChange(ivButton1, false);
                        } else if (packageName.equalsIgnoreCase(Constants.APPID_BUTTON2)) {
                            if (Constants.APPID_BUTTON2.equalsIgnoreCase(Constants.DEF_APPID_BUTTON2)) {
                                if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn2_type", "").equals("apk")) {
                                    if (HelperUtils.isAppInstalled(HomePageActivity.this, packageName)) {
                                        Constants.icon_button2 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button2.name());
                                        Constants.icon_pressed_button2 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button2.name());
                                        btn2_appname.setVisibility(View.GONE);
                                    } else {
                                        Constants.icon_button2 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_blank_button.name());
                                        Constants.icon_pressed_button2 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_blank_button.name());
                                        btn2_appname.setVisibility(View.VISIBLE);
                                        btn2_appname.setText(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn2_title", ""));
                                    }
                                } else {
                                    Constants.icon_button2 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button2.name());
                                    Constants.icon_pressed_button2 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button2.name());
                                }

                            } else {
                                Drawable appBanner = getPackageManager().getApplicationIcon(Constants.APPID_BUTTON2);
                                if (appBanner == null)
                                    appBanner = getPackageManager().getApplicationBanner(Constants.APPID_BUTTON2);
                                Constants.icon_button2 = HelperUtils.getBitmapFromDrawable(appBanner, false);
                                Constants.icon_pressed_button2 = HelperUtils.getBitmapFromDrawable(appBanner, true);
                            }
                            onFocusChange(ivButton2, false);
                        } else if (packageName.equalsIgnoreCase(Constants.APPID_BUTTON_TOP_NEW)) {
                            if (Constants.APPID_BUTTON_TOP_NEW.equalsIgnoreCase(Constants.DEF_APPID_BUTTON_TOP_NEW)) {
                                if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn2_type", "").equals("apk")) {
                                    if (HelperUtils.isAppInstalled(HomePageActivity.this, packageName)) {
                                        Constants.icon_button_top_new = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button_top_new.name());
                                        Constants.icon_pressed_button_top_new = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button_top_new.name());
                                    } else {
                                        Constants.icon_button_top_new = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_blank_button.name());
                                        Constants.icon_pressed_button_top_new = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_blank_button.name());
                                    }
                                } else {
                                    Constants.icon_button_top_new = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button_top_new.name());
                                    Constants.icon_pressed_button_top_new = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button_top_new.name());
                                }

                            } else {
                                Drawable appBanner = getPackageManager().getApplicationIcon(Constants.APPID_BUTTON_TOP_NEW);
                                if (appBanner == null)
                                    appBanner = getPackageManager().getApplicationBanner(Constants.APPID_BUTTON_TOP_NEW);
                                Constants.icon_button_top_new = HelperUtils.getBitmapFromDrawable(appBanner, false);
                                Constants.icon_pressed_button_top_new = HelperUtils.getBitmapFromDrawable(appBanner, true);
                            }
                            onFocusChange(ivButton2, false);
                        } else if (packageName.equalsIgnoreCase(Constants.APPID_BUTTON3)) {
                            if (Constants.APPID_BUTTON3.equalsIgnoreCase(Constants.DEF_APPID_BUTTON3)) {
                                if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn3_type", "").equals("apk")) {
                                    if (HelperUtils.isAppInstalled(HomePageActivity.this, packageName)) {
                                        Constants.icon_button3 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button3.name());
                                        Constants.icon_pressed_button3 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button3.name());

                                        btn3_appname.setVisibility(View.GONE);
                                    } else {
                                        Constants.icon_button3 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_blank_button.name());
                                        Constants.icon_pressed_button3 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_blank_button.name());
                                        btn3_appname.setVisibility(View.VISIBLE);
                                        btn3_appname.setText(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn3_title", ""));
                                    }
                                } else {
                                    Constants.icon_button3 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button3.name());
                                    Constants.icon_pressed_button3 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button3.name());
                                }
                            } else {
                                Drawable appBanner = getPackageManager().getApplicationIcon(Constants.APPID_BUTTON3);
                                if (appBanner == null)
                                    appBanner = getPackageManager().getApplicationBanner(Constants.APPID_BUTTON3);
                                Constants.icon_button3 = HelperUtils.getBitmapFromDrawable(appBanner, false);
                                Constants.icon_pressed_button3 = HelperUtils.getBitmapFromDrawable(appBanner, true);
                            }
                            onFocusChange(ivButton3, false);
                        } else if (packageName.equalsIgnoreCase(Constants.APPID_BUTTON4)) {
                            if (Constants.APPID_BUTTON4.equalsIgnoreCase(Constants.DEF_APPID_BUTTON4)) {
                                if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn4_type", "").equals("apk")) {
                                    if (HelperUtils.isAppInstalled(HomePageActivity.this, packageName)) {
                                        Constants.icon_button4 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button4.name());
                                        Constants.icon_pressed_button4 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button4.name());
                                        btn4_appname.setVisibility(View.GONE);
                                    } else {
                                        Constants.icon_button4 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_doubleblank_button.name());
                                        Constants.icon_pressed_button4 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_doubleblank_button.name());
                                        btn4_appname.setVisibility(View.VISIBLE);
                                        btn4_appname.setText(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn4_title", ""));
                                    }
                                } else {
                                    Constants.icon_button4 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button4.name());
                                    Constants.icon_pressed_button4 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button4.name());
                                }
                            } else {
                                Drawable appBanner = getPackageManager().getApplicationIcon(Constants.APPID_BUTTON4);
                                if (appBanner == null)
                                    appBanner = getPackageManager().getApplicationBanner(Constants.APPID_BUTTON4);
                                Constants.icon_button4 = HelperUtils.getBitmapFromDrawable(appBanner, false);
                                Constants.icon_pressed_button4 = HelperUtils.getBitmapFromDrawable(appBanner, true);
                            }
                            onFocusChange(ivButton4, false);
                        } else if (packageName.equalsIgnoreCase(Constants.APPID_BUTTON_TOP)) {
                            /*if (Constants.APPID_BUTTON_TOP.equalsIgnoreCase(Constants.DEF_APPID_BUTTON_TOP)) {*/
                            if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn0_type", "").equals("apk")) {
                                if (HelperUtils.isAppInstalled(HomePageActivity.this, packageName)) {
                                    Constants.icon_button_top = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button0.name());
                                    Constants.icon_pressed_button_top = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button0.name());
                                } else {
                                    Constants.icon_button_top = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_doubleblank_button.name());
                                    Constants.icon_pressed_button_top = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_doubleblank_button.name());

                                }
                            } else {
                                Constants.icon_button_top = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button0.name());
                                Constants.icon_pressed_button_top = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button0.name());
                            }
                            /*} else {
                                Drawable appBanner = getPackageManager().getApplicationIcon(Constants.APPID_BUTTON_TOP);
                                if (appBanner == null)
                                    appBanner = getPackageManager().getApplicationBanner(Constants.APPID_BUTTON_TOP);
                                Constants.icon_button_top = HelperUtils.getBitmapFromDrawable(appBanner, false);
                                Constants.icon_pressed_button_top = HelperUtils.getBitmapFromDrawable(appBanner, true);
                            }*/
                            ivButtonTop.setAlpha(1f);
                            onFocusChange(ivButtonTop, false);
                        } else if (packageName.equalsIgnoreCase(Constants.APPID_BUTTON_TOP_NEW)) {
                            /*if (Constants.APPID_BUTTON_TOP.equalsIgnoreCase(Constants.DEF_APPID_BUTTON_TOP)) {*/
                            if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn0_type", "").equals("apk")) {
                                if (HelperUtils.isAppInstalled(HomePageActivity.this, packageName)) {
                                    Constants.icon_button_top_new = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button_top_new.name());
                                    Constants.icon_pressed_button_top_new = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button_top_new.name());
                                } else {
                                    Constants.icon_button_top_new = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_doubleblank_button.name());
                                    Constants.icon_pressed_button_top_new = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_doubleblank_button.name());


                                }
                            } else {
                                Constants.icon_button_top_new = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button_top_new.name());
                                Constants.icon_pressed_button_top_new = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button_top_new.name());
                            }
                            /*} else {
                                Drawable appBanner = getPackageManager().getApplicationIcon(Constants.APPID_BUTTON_TOP);
                                if (appBanner == null)
                                    appBanner = getPackageManager().getApplicationBanner(Constants.APPID_BUTTON_TOP);
                                Constants.icon_button_top = HelperUtils.getBitmapFromDrawable(appBanner, false);
                                Constants.icon_pressed_button_top = HelperUtils.getBitmapFromDrawable(appBanner, true);
                            }*/
                            ivButtonTop.setAlpha(1f);
                            onFocusChange(ivButtonTop, false);
                        } else if (packageName.equalsIgnoreCase(Constants.APPID_BUTTON5)) {
                            if (Constants.APPID_BUTTON5.equalsIgnoreCase(Constants.DEF_APPID_BUTTON5)) {
                                if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn5_type", "").equals("apk")) {
                                    if (HelperUtils.isAppInstalled(HomePageActivity.this, packageName)) {
                                        Constants.icon_button5 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button5.name());
                                        Constants.icon_pressed_button5 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button5.name());
                                        btn5_appname.setVisibility(View.GONE);
                                    } else {
                                        Constants.icon_button5 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_blank_button.name());
                                        Constants.icon_pressed_button5 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_blank_button.name());
                                        btn5_appname.setVisibility(View.VISIBLE);
                                        btn5_appname.setText(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn5_title", ""));
                                    }
                                } else {
                                    Constants.icon_button5 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button5.name());
                                    Constants.icon_pressed_button5 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button5.name());
                                }
                            } else {
                                Drawable appBanner = getPackageManager().getApplicationIcon(Constants.APPID_BUTTON5);
                                if (appBanner == null)
                                    appBanner = getPackageManager().getApplicationBanner(Constants.APPID_BUTTON5);
                                Constants.icon_button5 = HelperUtils.getBitmapFromDrawable(appBanner, false);
                                Constants.icon_pressed_button5 = HelperUtils.getBitmapFromDrawable(appBanner, true);
                            }
                            onFocusChange(ivButton5, false);
                        } else if (packageName.equalsIgnoreCase(Constants.APPID_BUTTON6)) {
                            if (Constants.APPID_BUTTON6.equalsIgnoreCase(Constants.DEF_APPID_BUTTON6)) {
                                if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn6_type", "").equals("apk")) {
                                    if (HelperUtils.isAppInstalled(HomePageActivity.this, packageName)) {
                                        Constants.icon_button6 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button6.name());
                                        Constants.icon_pressed_button6 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button6.name());
                                        btn6_appname.setVisibility(View.GONE);
                                    } else {
                                        Constants.icon_button6 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_blank_button.name());
                                        Constants.icon_pressed_button6 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_blank_button.name());
                                        btn6_appname.setVisibility(View.VISIBLE);
                                        btn6_appname.setText(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn6_title", ""));
                                    }
                                } else {
                                    Constants.icon_button6 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button6.name());
                                    Constants.icon_pressed_button6 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button6.name());
                                }
                            } else {
                                Drawable appBanner = getPackageManager().getApplicationIcon(Constants.APPID_BUTTON6);
                                if (appBanner == null)
                                    appBanner = getPackageManager().getApplicationBanner(Constants.APPID_BUTTON6);
                                Constants.icon_button6 = HelperUtils.getBitmapFromDrawable(appBanner, false);
                                Constants.icon_pressed_button6 = HelperUtils.getBitmapFromDrawable(appBanner, true);
                            }
                            onFocusChange(ivButton6, false);
                        } else if (packageName.equalsIgnoreCase(Constants.APPID_BUTTON7)) {
                            if (Constants.APPID_BUTTON7.equalsIgnoreCase(Constants.DEF_APPID_BUTTON7)) {
                                if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn7_type", "").equals("apk")) {
                                    if (HelperUtils.isAppInstalled(HomePageActivity.this, packageName)) {
                                        Constants.icon_button7 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button7.name());
                                        Constants.icon_pressed_button7 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button7.name());
                                        btn7_appname.setVisibility(View.GONE);
                                    } else {
                                        Constants.icon_button7 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_blank_button.name());
                                        Constants.icon_pressed_button7 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_blank_button.name());
                                        btn7_appname.setVisibility(View.VISIBLE);
                                        btn7_appname.setText(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn7_title", ""));
                                    }
                                } else {
                                    Constants.icon_button7 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button7.name());
                                    Constants.icon_pressed_button7 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button7.name());
                                }
                            } else {
                                Drawable appBanner = getPackageManager().getApplicationIcon(Constants.APPID_BUTTON7);
                                if (appBanner == null)
                                    appBanner = getPackageManager().getApplicationBanner(Constants.APPID_BUTTON7);
                                Constants.icon_button7 = HelperUtils.getBitmapFromDrawable(appBanner, false);
                                Constants.icon_pressed_button7 = HelperUtils.getBitmapFromDrawable(appBanner, true);
                            }
                            onFocusChange(ivButton7, false);
                        } else if (packageName.equalsIgnoreCase(Constants.APPID_BUTTON8)) {
                            if (Constants.APPID_BUTTON8.equalsIgnoreCase(Constants.DEF_APPID_BUTTON8)) {
                                if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn8_type", "").equals("apk")) {
                                    if (HelperUtils.isAppInstalled(HomePageActivity.this, packageName)) {
                                        Constants.icon_button8 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button8.name());
                                        Constants.icon_pressed_button8 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button8.name());
                                        btn8_appname.setVisibility(View.GONE);
                                    } else {
                                        Constants.icon_button8 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_blank_button.name());
                                        Constants.icon_pressed_button8 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_blank_button.name());
                                        btn8_appname.setVisibility(View.VISIBLE);
                                        btn8_appname.setText(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn8_title", ""));
                                    }
                                } else {
                                    Constants.icon_button8 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button8.name());
                                    Constants.icon_pressed_button8 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button8.name());
                                }
                            } else {
                                Drawable appBanner = getPackageManager().getApplicationIcon(Constants.APPID_BUTTON8);
                                if (appBanner == null)
                                    appBanner = getPackageManager().getApplicationBanner(Constants.APPID_BUTTON8);
                                Constants.icon_button8 = HelperUtils.getBitmapFromDrawable(appBanner, false);
                                Constants.icon_pressed_button8 = HelperUtils.getBitmapFromDrawable(appBanner, true);
                            }
                            onFocusChange(ivButton8, false);
                        } else if (packageName.equalsIgnoreCase(Constants.APPID_BUTTON9)) {
                            if (Constants.APPID_BUTTON9.equalsIgnoreCase(Constants.DEF_APPID_BUTTON9)) {
                                if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn9_type", "").equals("apk")) {
                                    if (HelperUtils.isAppInstalled(HomePageActivity.this, packageName)) {
                                        Constants.icon_button9 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button9.name());
                                        Constants.icon_pressed_button9 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button9.name());
                                        btn9_appname.setVisibility(View.GONE);
                                    } else {
                                        Constants.icon_button9 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_blank_button.name());
                                        Constants.icon_pressed_button9 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_blank_button.name());
                                        btn9_appname.setVisibility(View.VISIBLE);
                                        btn9_appname.setText(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn9_title", ""));
                                    }
                                } else {
                                    Constants.icon_button9 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button9.name());
                                    Constants.icon_pressed_button9 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button9.name());
                                }
                            } else {
                                Drawable appBanner = getPackageManager().getApplicationIcon(Constants.APPID_BUTTON9);
                                if (appBanner == null)
                                    appBanner = getPackageManager().getApplicationBanner(Constants.APPID_BUTTON9);
                                Constants.icon_button9 = HelperUtils.getBitmapFromDrawable(appBanner, false);
                                Constants.icon_pressed_button9 = HelperUtils.getBitmapFromDrawable(appBanner, true);
                            }
                            onFocusChange(ivButton9, false);
                        } else if (packageName.equalsIgnoreCase(Constants.APPID_BUTTON10)) {
                            if (Constants.APPID_BUTTON10.equalsIgnoreCase(Constants.DEF_APPID_BUTTON10)) {
                                if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn10_type", "").equals("apk")) {
                                    if (HelperUtils.isAppInstalled(HomePageActivity.this, packageName)) {
                                        Constants.icon_button10 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button10.name());
                                        Constants.icon_pressed_button10 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button10.name());
                                        btn10_appname.setVisibility(View.GONE);

                                    } else {
                                        Constants.icon_button10 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_blank_button.name());
                                        Constants.icon_pressed_button10 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_blank_button.name());
                                        btn10_appname.setVisibility(View.VISIBLE);
                                        btn10_appname.setText(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn10_title", ""));
                                    }
                                } else {
                                    Constants.icon_button10 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button10.name());
                                    Constants.icon_pressed_button10 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button10.name());
                                }
                            } else {
                                Drawable appBanner = getPackageManager().getApplicationIcon(Constants.APPID_BUTTON10);
                                if (appBanner == null)
                                    appBanner = getPackageManager().getApplicationBanner(Constants.APPID_BUTTON10);
                                Constants.icon_button10 = HelperUtils.getBitmapFromDrawable(appBanner, false);
                                Constants.icon_pressed_button10 = HelperUtils.getBitmapFromDrawable(appBanner, true);
                            }
                            onFocusChange(ivButton10, false);
                        } else if (packageName.equalsIgnoreCase(Constants.APPID_BUTTON11)) {
                            if (Constants.APPID_BUTTON11.equalsIgnoreCase(Constants.DEF_APPID_BUTTON11)) {
                                if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn11_type", "").equals("apk")) {
                                    if (HelperUtils.isAppInstalled(HomePageActivity.this, packageName)) {
                                        Constants.icon_button11 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button11.name());
                                        Constants.icon_pressed_button11 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button11.name());
                                        btn11_appname.setVisibility(View.GONE);
                                    } else {
                                        Constants.icon_button11 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_blank_button.name());
                                        Constants.icon_pressed_button11 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_blank_button.name());
                                        btn11_appname.setVisibility(View.VISIBLE);
                                        btn11_appname.setText(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn11_title", ""));
                                    }
                                } else {
                                    Constants.icon_button11 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button11.name());
                                    Constants.icon_pressed_button11 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button11.name());
                                }
                            } else {
                                Drawable appBanner = getPackageManager().getApplicationIcon(Constants.APPID_BUTTON11);
                                if (appBanner == null)
                                    appBanner = getPackageManager().getApplicationBanner(Constants.APPID_BUTTON11);
                                Constants.icon_button11 = HelperUtils.getBitmapFromDrawable(appBanner, false);
                                Constants.icon_pressed_button11 = HelperUtils.getBitmapFromDrawable(appBanner, true);
                            }
                            onFocusChange(ivButton11, false);
                        } else if (packageName.equalsIgnoreCase(Constants.APPID_DOCK)) {
                            if (Constants.APPID_DOCK.equalsIgnoreCase(Constants.DEF_APPID_DOCK)) {
                                Constants.icon_dock_button = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_dock_button.name());
                                Constants.icon_pressed_dock_button = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_dock_button.name());
                            } else {
                                Drawable appBanner = getPackageManager().getApplicationIcon(Constants.APPID_DOCK);
                                if (appBanner == null)
                                    appBanner = getPackageManager().getApplicationBanner(Constants.APPID_DOCK);
                                Constants.icon_dock_button = HelperUtils.getBitmapFromDrawable(appBanner, false);
                                Constants.icon_pressed_dock_button = HelperUtils.getBitmapFromDrawable(appBanner, true);
                            }
                            ivAddRemoveBtn.setAlpha(1f);
                            onFocusChange(ivAddRemoveBtn, false);
                        } else if (packageName.equalsIgnoreCase(Constants.APPID_PWR_BUTTON)) {
                            if (Constants.APPID_PWR_BUTTON.equalsIgnoreCase(Constants.DEF_APPID_PWR_BUTTON)) {
                                Constants.icon_pressed_power_button = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_pwr_button.name());
                                Constants.icon_power_button = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pwr_button.name());

                            } else {
                                Drawable appBanner = getPackageManager().getApplicationIcon(Constants.APPID_PWR_BUTTON);
                                if (appBanner == null)
                                    appBanner = getPackageManager().getApplicationBanner(Constants.APPID_PWR_BUTTON);
                                Constants.icon_power_button = HelperUtils.getBitmapFromDrawable(appBanner, false);
                                Constants.icon_pressed_power_button = HelperUtils.getBitmapFromDrawable(appBanner, true);
                            }
                            ivPoweBtn.setAlpha(1f);
                            onFocusChange(ivPoweBtn, false);
                        } else if (packageName.equalsIgnoreCase(Constants.APPID_VPN_BUTTON)) {
                            if (Constants.APPID_VPN_BUTTON.equalsIgnoreCase(Constants.DEF_APPID_VPN_BUTTON)) {
                                Constants.icon_pressed_vpn_button = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_vpn_button.name());
                                Constants.icon_vpn_button = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_vpn_button.name());

                            } else {
                                Drawable appBanner = getPackageManager().getApplicationIcon(Constants.APPID_VPN_BUTTON);
                                if (appBanner == null)
                                    appBanner = getPackageManager().getApplicationBanner(Constants.APPID_VPN_BUTTON);
                                Constants.icon_vpn_button = HelperUtils.getBitmapFromDrawable(appBanner, false);
                                Constants.icon_pressed_vpn_button = HelperUtils.getBitmapFromDrawable(appBanner, true);
                            }
                            ivVpnBtn.setAlpha(1f);
                            onFocusChange(ivVpnBtn, false);
                        } else if (packageName.equalsIgnoreCase(Constants.DEF_APPID_HELP_BUTTON)) {
                            Constants.icon_pressed_help_button = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_help_button.name());
                            Constants.icon_help_button = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_help_button.name());
                            ivHelpBtn.setAlpha(1f);
                            onFocusChange(ivHelpBtn, false);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    Runnable runnable;
    int delay = 10000;

    @Override
    protected void onResume() {


        if (!isNetworkConnected()) {

            Log.i(TAG, "init:  resume yes");
            ivButtonTop.setVisibility(View.INVISIBLE);
            no_internet.setVisibility(View.VISIBLE);
            no_internet.bringToFront();
            no_internet.setBackgroundResource(R.drawable.internet_offline);
        } else {
            no_internet.setVisibility(View.GONE);
            ivButtonTop.setVisibility(View.VISIBLE);
            Log.i(TAG, "init: resume no");
        }
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);

                setMiniAndAdIcons();
            }
        }, delay);
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); //stop handler when activity not visible super.onPause();
    }

    private void setNetworkIcon() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                try {

                    if (HelperUtils.checkNetworkStatus(HomePageActivity.this, 1)) {
//                        if (!SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_pressed_minibutton1", "").equals("")) {
//                            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_pressed_minibutton1", "")).into(ivWifiState);
//                        } else {
//                            ivWifiState.setImageResource(R.drawable.wifi_on);
//                        }
                        ivWifiState.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_minibutton1.name())));



                        no_internet.setVisibility(View.GONE);
                        ivButtonTop.setVisibility(View.VISIBLE);

                    } else {
                        ivButtonTop.setVisibility(View.INVISIBLE);
                        no_internet.setVisibility(View.VISIBLE);
                        no_internet.bringToFront();
                        no_internet.setBackgroundResource(R.drawable.internet_offline);

//                        if (HelperUtils.checkNetworkStatus(HomePageActivity.this, 2)) {
//                            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton1", "")).into(ivWifiState);
//                        } else {
//                            ivWifiState.setImageResource(R.drawable.wifi_off);
//                        }
                        ivWifiState.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton1.name())));

//                        if (!SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton1", "").equals("")) {
//                            Toast.makeText(HomePageActivity.this, "wifi button available", Toast.LENGTH_SHORT).show();
//                            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton1", "")).into(ivWifiState);
//                        }else{
//                            Log.i(TAG, "run: wifi off");
//                            Toast.makeText(HomePageActivity.this, "wifi button not available", Toast.LENGTH_SHORT).show();
//                            ivWifiState.setImageResource(R.drawable.wifi_off);
//                        }


                    }
                    if (HelperUtils.checkNetworkStatus(HomePageActivity.this, 2)) {

//                        if (!SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_pressed_minibutton2", "").equals("")) {
//                            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_pressed_minibutton2", "")).into(ivLanState);
//                        } else {
//                            ivLanState.setImageResource(R.drawable.ether_on);
//                        }
                        ivLanState.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_minibutton2.name())));
                        no_internet.setVisibility(View.GONE);
                        ivButtonTop.setVisibility(View.VISIBLE);
                    } else {
                        Log.i(TAG, "init: back press yes");
                        if (!isNetworkConnected()) {
                            ivButtonTop.setVisibility(View.INVISIBLE);
                            no_internet.setVisibility(View.VISIBLE);
                            no_internet.bringToFront();
                            no_internet.setBackgroundResource(R.drawable.internet_offline);

                        } else {
                            no_internet.setVisibility(View.GONE);
                            ivButtonTop.setVisibility(View.VISIBLE);
                        }
//                        ivButtonTop.setVisibility(View.INVISIBLE);
//                        no_internet.setVisibility(View.VISIBLE);
//                        no_internet.bringToFront();
//                        no_internet.setBackgroundResource(R.drawable.internet_offline);

//                        if (!SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton2", "").equals("")) {
//                            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton2", "")).into(ivLanState);
//                        } else {
//                            ivLanState.setImageResource(R.drawable.ether_off);
//                        }
                        ivLanState.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton2.name())));

                    }
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter == null) {
                        Log.i(TAG, "run: Bluetooth  not supported");
//                        if (!SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton4", "").equals("")) {
//                            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton4", "")).into(ivBlueState);
//                        } else {
//                            ivBlueState.setImageResource(R.drawable.bluetooth_off);
//                        }
                        ivBlueState.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton4.name())));
                        // Device does not support Bluetooth
                    } else if (!mBluetoothAdapter.isEnabled()) {
                        // Bluetooth is not enabled :)
                        Log.i(TAG, "run: Bluetooth  not enabled");
//                        if (!SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton4", "").equals("")) {
//                            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton4", "")).into(ivBlueState);
//                        } else {
//                            ivBlueState.setImageResource(R.drawable.bluetooth_off);
//                        }
                        ivBlueState.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton4.name())));
                    } else {
                        Log.i(TAG, "run: Bluetooth  enabled");
                        // Bluetooth is enabled
//                        if (!SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_pressed_minibutton4", "").equals("")) {
//                            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_pressed_minibutton4", "")).into(ivBlueState);
//                        } else {
//                            ivBlueState.setImageResource(R.drawable.bluetooth_on);
//                        }
                        ivBlueState.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_minibutton4.name())));
                    }
//                    if (HelperUtils.checkNetworkStatus(HomePageActivity.this, 3)) {
//                        Log.i(TAG, "run: Bluetooth  on");
//                        if (!SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_pressed_minibutton4", "").equals("")) {
//                            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_pressed_minibutton4", "")).into(ivBlueState);
//                        }else{
//                            ivBlueState.setImageResource(R.drawable.bluetooth_on);
//                        }
//
//                    } else {
//                        Log.i(TAG, "run: Bluetooth  off");
//                        if (!SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton4", "").equals("")) {
//                            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton4", "")).into(ivBlueState);
//                        }else{
//                            ivBlueState.setImageResource(R.drawable.bluetooth_off);
//                        }
//                    }
                    if (HelperUtils.isAppInstalled(HomePageActivity.this, Constants.APPID_MiniButton3)) {

                    } else {

                    }
                    if (HelperUtils.haveNetworkConnection(HomePageActivity.this) == 2) {
                        ivMiniButton3.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_minibutton3.name())));


                    } else {
                        ivMiniButton3.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton3.name())));

                    }
                    p1.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 10000);
    }

    private String deletedAppName = null;
    private String helpInfoUrl = "";
    private String appInfoUrl = null;


    @SuppressLint("NonConstantResourceId")
    public void showLongPressDialog(final View view) {
        try {

            SharedPreferences.Editor editor = getSharedPreferences("urls", MODE_PRIVATE).edit();
            deletedAppName = null;
//            final String[] helpInfoUrl={null};
//           final String[] appInfoUrl={null};
            final String[] lockStatusKey = {null};
            final String[] appPackageName = {null};
            switch (view.getId()) {
                case R.id.btn_settings:
                    doShowUpdateSettingsFileDialog();
                    break;
//                case R.id.ivButtonTop:
//                    showUpdateLauncherDialog();
//                    break;
//                        if (lockStatusKey[0] == null)
//                            lockStatusKey[0] = Constants.Storage.LOCK_STATUS_BUTTON0.name();
//
//                        if (appPackageName[0] == null)
//                            appPackageName[0] = Constants.APPID_BUTTON_TOP;
//
//                        if (deletedAppName == null)
//                            deletedAppName = "0";
                case R.id.ivButton1:

//                    SharedPreferenceStorage.setStringValue(HomePageActivity.this, "btn_helpinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn1_helpinfo_url", ""));
//                    SharedPreferenceStorage.setStringValue(HomePageActivity.this, "appinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn1_appinfo_url", ""));

                    editor.putString("btn_helpinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn1_helpinfo_url", ""));
                    editor.putString("appinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn1_appinfo_url", ""));
                    editor.apply();

                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_BUTTON1.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_BUTTON1;

                    if (deletedAppName == null)
                        deletedAppName = "1";
                case R.id.ivTopBtnNew:

                    SharedPreferenceStorage.setStringValue(HomePageActivity.this, "btn_helpinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn_top_new_helpinfo_url", ""));
                    SharedPreferenceStorage.setStringValue(HomePageActivity.this, "appinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn_top_new_appinfo_url", ""));


                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_BUTTON_TOP_NEW.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_BUTTON_TOP_NEW;

                    if (deletedAppName == null)
                        deletedAppName = "23";
                case R.id.ivButton2:
//                    SharedPreferenceStorage.setStringValue(HomePageActivity.this, "btn_helpinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn2_helpinfo_url", ""));
//                    SharedPreferenceStorage.setStringValue(HomePageActivity.this, "appinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn2_appinfo_url", ""));

                    editor.putString("btn_helpinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn2_helpinfo_url", ""));
                    editor.putString("appinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn2_appinfo_url", ""));
                    editor.apply();

                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_BUTTON2.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_BUTTON2;

                    if (deletedAppName == null)
                        deletedAppName = "2";
                case R.id.ivButton3:
                    editor.putString("btn_helpinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn3_helpinfo_url", ""));
                    editor.putString("appinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn3_appinfo_url", ""));
                    editor.apply();
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_BUTTON3.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_BUTTON3;

                    if (deletedAppName == null)
                        deletedAppName = "3";
                case R.id.ivButton4:
                    editor.putString("btn_helpinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn4_helpinfo_url", ""));
                    editor.putString("appinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn4_appinfo_url", ""));
                    editor.apply();
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_BUTTON4.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_BUTTON4;

                    if (deletedAppName == null)
                        deletedAppName = "4";
                case R.id.ivButton5:
                    editor.putString("btn_helpinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn5_helpinfo_url", ""));
                    editor.putString("appinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn5_appinfo_url", ""));
                    editor.apply();
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_BUTTON5.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_BUTTON5;

                    if (deletedAppName == null)
                        deletedAppName = "5";
                case R.id.ivButton6:
                    editor.putString("btn_helpinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn6_helpinfo_url", ""));
                    editor.putString("appinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn6_appinfo_url", ""));
                    editor.apply();
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_BUTTON6.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_BUTTON6;

                    if (deletedAppName == null)
                        deletedAppName = "6";
                case R.id.ivButton7:
                    editor.putString("btn_helpinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn7_helpinfo_url", ""));
                    editor.putString("appinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn7_appinfo_url", ""));
                    editor.apply();
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_BUTTON7.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_BUTTON7;

                    if (deletedAppName == null)
                        deletedAppName = "7";
                case R.id.ivButton8:
                    editor.putString("btn_helpinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn8_helpinfo_url", ""));
                    editor.putString("appinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn8_appinfo_url", ""));
                    editor.apply();
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_BUTTON8.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_BUTTON8;

                    if (deletedAppName == null)
                        deletedAppName = "8";
                case R.id.ivButton9:
                    editor.putString("btn_helpinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn9_helpinfo_url", ""));
                    editor.putString("appinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn9_appinfo_url", ""));
                    editor.apply();
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_BUTTON9.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_BUTTON9;

                    if (deletedAppName == null)
                        deletedAppName = "9";
                case R.id.ivButton10:
                    editor.putString("btn_helpinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn10_helpinfo_url", ""));
                    editor.putString("appinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn10_appinfo_url", ""));
                    editor.apply();
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_BUTTON10.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_BUTTON10;

                    if (deletedAppName == null)
                        deletedAppName = "10";
                case R.id.ivButton11:
                    editor.putString("btn_helpinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn11_helpinfo_url", ""));
                    editor.putString("appinfo_url", SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn11_appinfo_url", ""));
                    editor.apply();
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_BUTTON11.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_BUTTON11;

                    if (deletedAppName == null)
                        deletedAppName = "11";
                case R.id.ivBottomAddRemoveBtn:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_ADD_REMOVE.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_DOCK;

                    if (deletedAppName == null)
                        deletedAppName = "12";
                case R.id.ivPoweBtn:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_POWER_BTN.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_PWR_BUTTON;

                    if (deletedAppName == null)
                        deletedAppName = "13";
                case R.id.ivMiniButton1:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_MiniButton1.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_MiniButton1;

                    if (deletedAppName == null)
                        deletedAppName = "23";
                case R.id.ivMiniButton2:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_MiniButton2.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_MiniButton2;

                    if (deletedAppName == null)
                        deletedAppName = "24";
                case R.id.ivMiniButton3:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_MiniButton3.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_MiniButton3;

                    if (deletedAppName == null)
                        deletedAppName = "25";
                case R.id.ivMiniButton4:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_MiniButton4.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_MiniButton4;

                    if (deletedAppName == null)
                        deletedAppName = "26";
                case R.id.ivMiniButton5:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_MiniButton5.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_MiniButton5;

                    if (deletedAppName == null)
                        deletedAppName = "27";
                case R.id.ivMiniButton6:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_MiniButton6.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_MiniButton6;

                    if (deletedAppName == null)
                        deletedAppName = "28";
                case R.id.ivMiniButton7:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_MiniButton7.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_MiniButton7;

                    if (deletedAppName == null)
                        deletedAppName = "29";
                case R.id.ivMiniButton8:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_MiniButton8.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_MiniButton8;

                    if (deletedAppName == null)
                        deletedAppName = "30";
                case R.id.ivMiniButton9:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_MiniButton9.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_MiniButton9;

                    if (deletedAppName == null)
                        deletedAppName = "31";
                case R.id.ivMiniButton10:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_MiniButton10.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_MiniButton10;

                    if (deletedAppName == null)
                        deletedAppName = "32";
                case R.id.ivMiniButton11:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_MiniButton11.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_MiniButton11;

                    if (deletedAppName == null)
                        deletedAppName = "33";
                case R.id.ivMiniButton12:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_MiniButton12.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_MiniButton12;

                    if (deletedAppName == null)
                        deletedAppName = "34";
                case R.id.ivVpnBtn:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_VPN_BTN.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_VPN_BUTTON;

                    if (deletedAppName == null)
                        deletedAppName = "14";
                case R.id.ivWifiState:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_WIFI_BTN.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.WIFi_STATE_BUTTON;

                    if (deletedAppName == null)
                        deletedAppName = "19";
                case R.id.ivBluetoothState:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_BLUETOOTH_BTN.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.BLUETOOTH_STATE_BUTTON;

                    if (deletedAppName == null)
                        deletedAppName = "20";
                case R.id.ivAppLockBtn:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_PARENTAL_BTN.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.PARENTAL_STATE_BUTTON;

                    if (deletedAppName == null)
                        deletedAppName = "21";
                case R.id.ivHelpBtn:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_HELP_BTN.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.HELP_STATE_BUTTON;

                    if (deletedAppName == null)
                        deletedAppName = "22";


                case R.id.btn_left_top:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_VPN_BTN.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_VPN_BUTTON;

                    if (deletedAppName == null)
                        deletedAppName = "15";
                case R.id.btn_left_top_right:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_VPN_BTN.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_VPN_BUTTON;

                    if (deletedAppName == null)
                        deletedAppName = "35";
                case R.id.btn_left1:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_VPN_BTN.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_VPN_BUTTON;

                    if (deletedAppName == null)
                        deletedAppName = "16";
                case R.id.btn_left2:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_VPN_BTN.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_VPN_BUTTON;

                    if (deletedAppName == null)
                        deletedAppName = "17";
                case R.id.btn_left3:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_VPN_BTN.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_VPN_BUTTON;

                    if (deletedAppName == null)
                        deletedAppName = "18";
                case R.id.btn_right1:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_VPN_BTN.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_VPN_BUTTON;

                    if (deletedAppName == null)
                        deletedAppName = "36";
                case R.id.btn_right2:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_VPN_BTN.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_VPN_BUTTON;

                    if (deletedAppName == null)
                        deletedAppName = "37";
                case R.id.btn_right3:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_VPN_BTN.name();

                    if (appPackageName[0] == null)
                        appPackageName[0] = Constants.APPID_VPN_BUTTON;

                    if (deletedAppName == null)
                        deletedAppName = "38";

                    if (mLongPressOptionDialog == null)
                        mLongPressOptionDialog = new LongPressOptionDialog(this);
                    mLongPressOptionDialog.showDialog(deletedAppName, helpInfoUrl, new LongPressOptionDialog.OnItemClickListener() {


                        @Override
                        public void onClicked(int item) {
                            switch (item) {
                                case 0: // update to new app
                                    SharedPreferenceStorage.setBooleanValue(HomePageActivity.this, deletedAppName + Constants.Storage._isDeleted.name(), false);
                                    if (HelperUtils.haveNetworkConnection(HomePageActivity.this) > 0) {

                                        if (Build.VERSION.SDK_INT >= 23) {
                                            if (checkPermission()) {
                                                // Your Permission granted already .Do next code
                                                doDownloadAndInstallToNewVersion(view, 0, 0);
                                            } else {
                                                requestPermission(view); // Code for permission
                                            }
                                        } else {
                                            doDownloadAndInstallToNewVersion(view, 0, 0);
                                            //long clicked
                                            // Do next code
                                        }
                                    }
                                    break;
                                case 1: // change app icon
                                    SharedPreferenceStorage.setBooleanValue(HomePageActivity.this, deletedAppName + Constants.Storage._isDeleted.name(), false);
//                                    showChangeDefaultIconDialog(view);
                                    goToAppSelectionActivity(view);
                                    break;
                                case 2: //delete app
                                    if (HelperUtils.isAppInstalled(HomePageActivity.this, appPackageName[0])) {
                                        if (APKManager.Uninstall(appPackageName[0])) {
                                            setOrRefreshAppIcons(appPackageName[0]);
                                            onActionAppDelete();
                                        } else {
                                            Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + appPackageName[0]));
                                            myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                                            myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivityForResult(myAppSettings, Constants.RECOGNIZER_APP_DELETE);
                                            Toast.makeText(HomePageActivity.this, "Click on \"Uninstall\" button to delete the app.", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(HomePageActivity.this, getResources().getString(R.string.app_is_not_installed), Toast.LENGTH_LONG).show();
                                    }
                                    break;

                                case 3: // delete app data
                                    try {
                                        if (HelperUtils.isAppInstalled(HomePageActivity.this, appPackageName[0])) {
                                            if (APKManager.ClearAppData(appPackageName[0])) {
                                                Toast.makeText(HomePageActivity.this, "App data cleared successfully.", Toast.LENGTH_LONG).show();
                                            } else {
                                                HelperUtils.goAppDetailsDeviceSettingsScreen(HomePageActivity.this, appPackageName[0]);
                                                Toast.makeText(HomePageActivity.this, "Click on \"Clear data\" button to delete app data.", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(HomePageActivity.this, getResources().getString(R.string.app_is_not_installed), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;

                                case 4: // unlock app
                                    HelperUtils.onActionAppOpening(HomePageActivity.this, new HelperUtils.OnAppOpeningPasswordValidateListener() {
                                        @Override
                                        public void OnAppLockStatus(boolean allowOpening) {
                                            if (allowOpening)
                                                doLockUnlockSelectedApp(lockStatusKey[0], true);
                                            else
                                                HelperUtils.showMessageInfo(HomePageActivity.this, "Wrong password entered. Please try again!");
                                        }
                                    });
                                    break;

                                case 5: //lock app
                                    HelperUtils.onActionAppOpening(HomePageActivity.this, new HelperUtils.OnAppOpeningPasswordValidateListener() {
                                        @Override
                                        public void OnAppLockStatus(boolean allowOpening) {
                                            if (allowOpening)
                                                doLockUnlockSelectedApp(lockStatusKey[0], false);
                                            else
                                                HelperUtils.showMessageInfo(HomePageActivity.this, "Wrong password entered. Please try again!");
                                        }
                                    });
                                    break;

                                case 6: //hide app/UnHide app
                                    onActionHideOrUnHideApp(view);
                                    break;

                                case 7: // delete app cache
                                    try {
                                        if (HelperUtils.isAppInstalled(HomePageActivity.this, appPackageName[0])) {
                                            if (APKManager.ClearAppCache(appPackageName[0])) {
                                                Toast.makeText(HomePageActivity.this, "App cache cleared successfully.", Toast.LENGTH_LONG).show();
                                            } else {
                                                HelperUtils.goAppDetailsDeviceSettingsScreen(HomePageActivity.this, appPackageName[0]);
                                                Toast.makeText(HomePageActivity.this, "Click on \"Clear Cache\" button to delete app cache.", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(HomePageActivity.this, getResources().getString(R.string.app_is_not_installed), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case 8:  //helpinfo url
                                    try {
                                        String url = null;

//                                        SharedPreferences prefs = getSharedPreferences("urls", MODE_PRIVATE);
//                                         prefs.getString("btn_helpinfo_url", "");
                                        if (HelperUtils.isAppInstalled(HomePageActivity.this, appPackageName[0])) {
                                            if (deletedAppName == "1") {
                                                url = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn1_helpinfo_url", "");


                                            } else if (deletedAppName == "2") {
                                                url = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn2_helpinfo_url", "");

                                            } else if (deletedAppName == "3") {
                                                url = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn3_helpinfo_url", "");

                                            } else if (deletedAppName == "4") {
                                                url = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn4_helpinfo_url", "");

                                            } else if (deletedAppName == "5") {
                                                url = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn5_helpinfo_url", "");

                                            } else if (deletedAppName == "6") {
                                                url = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn6_helpinfo_url", "");

                                            } else if (deletedAppName == "7") {
                                                url = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn7_helpinfo_url", "");

                                            } else if (deletedAppName == "8") {
                                                url = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn8_helpinfo_url", "");

                                            } else if (deletedAppName == "9") {
                                                url = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn9_helpinfo_url", "");

                                            } else if (deletedAppName == "10") {
                                                url = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn10_helpinfo_url", "");

                                            } else if (deletedAppName == "11") {
                                                url = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn11_helpinfo_url", "");

                                            }
                                            Log.i(TAG, "onClicked: helpinfobtn" + url);
                                            openFullWebView(url);

                                        } else {
                                            Toast.makeText(HomePageActivity.this, getResources().getString(R.string.app_is_not_installed), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;

                                case 9: //app info url
                                    try {

                                        String url = null;
                                        String url_type = null;

                                        if (HelperUtils.isAppInstalled(HomePageActivity.this, appPackageName[0])) {

                                            if (deletedAppName == "1") {
                                                url = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn1_appinfo_url", "");
                                                url_type = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn1_appinfo_url_type", "");


                                            } else if (deletedAppName == "2") {
                                                url = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn2_appinfo_url", "");
                                                url_type = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn2_appinfo_url_type", "");

                                            } else if (deletedAppName == "3") {
                                                url = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn3_appinfo_url", "");
                                                url_type = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn3_appinfo_url_type", "");

                                            } else if (deletedAppName == "4") {
                                                url = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn4_appinfo_url", "");
                                                url_type = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn4_appinfo_url_type", "");

                                            } else if (deletedAppName == "5") {
                                                url = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn5_appinfo_url", "");
                                                url_type = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn5_appinfo_url_type", "");

                                            } else if (deletedAppName == "6") {
                                                url = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn6_appinfo_url", "");
                                                url_type = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn6_appinfo_url_type", "");

                                            } else if (deletedAppName == "7") {
                                                url = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn7_appinfo_url", "");
                                                url_type = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn7_appinfo_url_type", "");

                                            } else if (deletedAppName == "8") {
                                                url = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn8_appinfo_url", "");
                                                url_type = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn8_appinfo_url_type", "");

                                            } else if (deletedAppName == "9") {
                                                url = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn9_appinfo_url", "");
                                                url_type = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn9_appinfo_url_type", "");

                                            } else if (deletedAppName == "10") {
                                                url = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn10_appinfo_url", "");
                                                url_type = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn10_appinfo_url_type", "");

                                            } else if (deletedAppName == "11") {
                                                url = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn11_appinfo_url", "");
                                                url_type = SharedPreferenceStorage.getStringValue(HomePageActivity.this, "btn11_appinfo_url_type", "");

                                            }
                                            Log.i(TAG, "onClicked: " + url + url_type);
                                            doShowCustomAppInfoDialog(url_type, url);
//                                            openFullWebView(url);
                                        } else {
                                            Toast.makeText(HomePageActivity.this, getResources().getString(R.string.app_is_not_installed), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }

                            Animation anim = AnimationUtils.loadAnimation(HomePageActivity.this, R.anim.zoom_in_edit);
                            view.startAnimation(anim);
                            anim.setFillAfter(true);
                        }
                    });

                    mLongPressOptionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            Animation anim = AnimationUtils.loadAnimation(HomePageActivity.this, R.anim.zoom_in_edit);
                            view.startAnimation(anim);
                            anim.setFillAfter(true);
                        }
                    });

                    /*AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogTheme);
                    final Vector<String> items_H = new Vector<String>();
                    items_H.addElement("UPDATE TO NEW VERSION");
                    items_H.addElement("REASSIGN TO ANOTHER APP");
                    items_H.addElement("DELETE APP");
                    items_H.addElement("DELETE APP DATA");
                    items_H.addElement("UNLOCK APP");
                    items_H.addElement("LOCK APP");
                    CharSequence[] charSequenceItems = items_H.toArray(new CharSequence[items_H.size()]);
                    builder.setItems(charSequenceItems, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            switch (item) {
                                case 0:
                                    if (HelperUtils.haveNetworkConnection(HomePageActivity.this)) {

                                        if (Build.VERSION.SDK_INT >= 23) {
                                            if (checkPermission()) {
                                                // Your Permission granted already .Do next code
                                                doDownloadAndInstallToNewVersion(view);
                                            } else {
                                                requestPermission(view); // Code for permission
                                            }
                                        } else {
                                            doDownloadAndInstallToNewVersion(view);
                                            // Do next code
                                        }
                                    }
                                    break;
                                case 1:
                                    showChangeDefaultIconDialog(view);
                                    break;
                                case 2: //delete app
                                    if (HelperUtils.isAppInstalled(HomePageActivity.this, appPackageName[0])) {
                                        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + appPackageName[0]));
                                        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                                        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivityForResult(myAppSettings, Constants.RECOGNIZER_APP_DELETE);
                                        Toast.makeText(HomePageActivity.this, "Click on \"Uninstall\" button to delete the app.", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(HomePageActivity.this, getResources().getString(R.string.app_is_not_installed), Toast.LENGTH_LONG).show();
                                    }

                                case 3: // delete app data
                                    try {
                                        if (HelperUtils.isAppInstalled(HomePageActivity.this, appPackageName[0])) {
                                            Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + appPackageName[0]));
                                            myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                                            myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivityForResult(myAppSettings, Constants.RECOGNIZER_APP_DATA_DELETE);
                                            Toast.makeText(HomePageActivity.this, "Click on \"Clear data\" button to delete app data.", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(HomePageActivity.this, getResources().getString(R.string.app_is_not_installed), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case 4: // unlock app
                                    HelperUtils.onActionAppOpening(HomePageActivity.this, new HelperUtils.OnAppOpeningPasswordValidateListener() {
                                        @Override
                                        public void OnAppLockStatus(boolean allowOpening) {
                                            if (allowOpening)
                                                doLockUnlockSelectedApp(lockStatusKey[0], true);
                                            else
                                                HelperUtils.showMessageInfo(HomePageActivity.this, "Wrong password entered. Please try again!");
                                        }
                                    });
                                    break;
                                case 5: //lock app
                                    HelperUtils.onActionAppOpening(HomePageActivity.this, new HelperUtils.OnAppOpeningPasswordValidateListener() {
                                        @Override
                                        public void OnAppLockStatus(boolean allowOpening) {
                                            if (allowOpening)
                                                doLockUnlockSelectedApp(lockStatusKey[0], false);
                                            else
                                                HelperUtils.showMessageInfo(HomePageActivity.this, "Wrong password entered. Please try again!");
                                        }
                                    });
                                    break;
                            }
                            Animation anim = AnimationUtils.loadAnimation(HomePageActivity.this, R.anim.zoom_in_edit);
                            view.startAnimation(anim);
                            anim.setFillAfter(true);
                        }
                    });
                    AlertDialog mMenu = builder.create();
                    mMenu.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        public void onDismiss(DialogInterface dialog) {
                            if (items_H != null) {
                                items_H.removeAllElements();
                                Animation anim = AnimationUtils.loadAnimation(HomePageActivity.this, R.anim.zoom_in_edit);
                                view.startAnimation(anim);
                                anim.setFillAfter(true);
                            }

                        }
                    });
                    mMenu.show();*/
                    break;

                    /*case R.id.ivButton11:
                    if (lockStatusKey[0] == null)
                        lockStatusKey[0] = Constants.Storage.LOCK_STATUS_BUTTON11.name();

                    builder = new AlertDialog.Builder(this, R.style.MyAlertDialogTheme);
                    final Vector<String>  items_H1 = new Vector<String>();
                    items_H1.addElement("UPDATE TO NEW VERSION");
                    if (SharedPreferenceStorage.getBooleanValue(this, Constants.Storage.APPS_LOCK_ENABLED.name(),false)){
                        isSelectedAppLocked[0] = SharedPreferenceStorage.getBooleanValue(this, lockStatusKey[0], false);
                        items_H1.addElement(isSelectedAppLocked[0] ? "UNLOCK APP" : "LOCK APP");
                    }
                    charSequenceItems = items_H1.toArray(new CharSequence[items_H1.size()]);
                    builder.setItems(charSequenceItems, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            switch (item) {
                                case 0:
                                    if (HelperUtils.haveNetworkConnection(HomePageActivity.this)) {

                                        if (Build.VERSION.SDK_INT >= 23) {
                                            if (checkPermission()) {
                                                // Your Permission granted already .Do next code
                                                doDownloadAndInstallToNewVersion(view);
                                            } else {
                                                requestPermission(view); // Code for permission
                                            }
                                        } else {
                                            doDownloadAndInstallToNewVersion(view);
                                            // Do next code
                                        }
                                    }
                                    break;
                                case 1:
                                    HelperUtils.onActionAppOpening(HomePageActivity.this, new HelperUtils.OnAppOpeningPasswordValidateListener() {
                                        @Override
                                        public void OnAppLockStatus(boolean allowOpening) {
                                            if (allowOpening)
                                                doLockUnlockSelectedApp(lockStatusKey[0], isSelectedAppLocked[0]);
                                            else
                                                HelperUtils.showMessageInfo(HomePageActivity.this, "Wrong password entered. Please try again!");
                                        }
                                    });
                                    break;
                            }
                            Animation anim = AnimationUtils.loadAnimation(HomePageActivity.this, R.anim.zoom_in_edit);
                            view.startAnimation(anim);
                            anim.setFillAfter(true);
                        }
                    });
                    mMenu = builder.create();
                    mMenu.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        public void onDismiss(DialogInterface dialog) {
                            if (items_H1 != null) {
                                items_H1.removeAllElements();
                                Animation anim = AnimationUtils.loadAnimation(HomePageActivity.this, R.anim.zoom_in_edit);
                                view.startAnimation(anim);
                                anim.setFillAfter(true);
                            }

                        }
                    });
                    mMenu.show();
                    break;*/

                default:
                    showChangeDefaultIconDialog(view);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doDownloadAndInstallToNewVersion(final View view, int installedPkgs, int totalUninstalledPkgs) {
        try {
            switch (view.getId()) {
                case R.id.ivButtonTop:
                    if (Constants.URL_UPDATE_BUTTON0 != null)
                        new DownloadNewUpdate(this, AppName.zLauncher.name(), 0, 0, Constants.FILE_TYPE.apk, new DownloadNewUpdate.OnDownloadStatus() {
                            @Override
                            public void OnComplete() {
                                setOrRefreshAppIcons(Constants.APPID_BUTTON_TOP);
                            }
                        }).execute(Constants.URL_UPDATE_BUTTON0);
                    break;
                case R.id.ivWifiState:
                    if (Constants.URL_UPDATE_BUTTON_WIFI != null)
                        new DownloadNewUpdate(this, AppName.zLauncher.name(), 0, 0, Constants.FILE_TYPE.apk, new DownloadNewUpdate.OnDownloadStatus() {
                            @Override
                            public void OnComplete() {
//                                setOrRefreshAppIcons(Constants.APPID_BUTTON_TOP);
                            }
                        }).execute(Constants.URL_UPDATE_BUTTON_WIFI);
                    break;
                case R.id.ivBluetoothState:
                    if (Constants.URL_UPDATE_BUTTON_BLUETOOTH != null)
                        new DownloadNewUpdate(this, AppName.zLauncher.name(), 0, 0, Constants.FILE_TYPE.apk, new DownloadNewUpdate.OnDownloadStatus() {
                            @Override
                            public void OnComplete() {
//                                setOrRefreshAppIcons(Constants.APPID_BUTTON_TOP);
                            }
                        }).execute(Constants.URL_UPDATE_BUTTON_BLUETOOTH);
                    break;
                case R.id.ivAppLockBtn:
                    if (Constants.URL_UPDATE_BUTTON_PARENTAL != null)
                        new DownloadNewUpdate(this, AppName.zLauncher.name(), 0, 0, Constants.FILE_TYPE.apk, new DownloadNewUpdate.OnDownloadStatus() {
                            @Override
                            public void OnComplete() {
//                                setOrRefreshAppIcons(Constants.APPID_BUTTON_TOP);
                            }
                        }).execute(Constants.URL_UPDATE_BUTTON_PARENTAL);
                    break;
                case R.id.ivHelpBtn:
                    if (Constants.URL_UPDATE_BUTTON_HELP != null)
                        new DownloadNewUpdate(this, AppName.zLauncher.name(), 0, 0, Constants.FILE_TYPE.apk, new DownloadNewUpdate.OnDownloadStatus() {
                            @Override
                            public void OnComplete() {
//                                setOrRefreshAppIcons(Constants.APPID_BUTTON_TOP);
                            }
                        }).execute(Constants.URL_UPDATE_BUTTON_HELP);
                    break;
                case R.id.ivButton1:
                    if (Constants.URL_UPDATE_BUTTON1 != null)
                        new DownloadNewUpdate(this, AppName.MediaCenter.name(), installedPkgs, totalUninstalledPkgs, Constants.FILE_TYPE.apk, new DownloadNewUpdate.OnDownloadStatus() {
                            @Override
                            public void OnComplete() {
                                setOrRefreshAppIcons(Constants.APPID_BUTTON1);
                            }
                        }).execute(Constants.URL_UPDATE_BUTTON1);
                    break;
                case R.id.ivTopBtnNew:
                    if (Constants.URL_UPDATE_BUTTON_TOP_NEW != null)
                        new DownloadNewUpdate(this, AppName.MediaCenter.name(), 0, 0, Constants.FILE_TYPE.apk, new DownloadNewUpdate.OnDownloadStatus() {
                            @Override
                            public void OnComplete() {
                                setOrRefreshAppIcons(Constants.APPID_BUTTON_TOP_NEW);
                            }
                        }).execute(Constants.URL_UPDATE_BUTTON_TOP_NEW);
                    break;

                case R.id.ivButton2:
                    if (Constants.URL_UPDATE_BUTTON2 != null)
                        new DownloadNewUpdate(this, AppName.freevod.name(), installedPkgs, totalUninstalledPkgs, Constants.FILE_TYPE.apk, new DownloadNewUpdate.OnDownloadStatus() {
                            @Override
                            public void OnComplete() {
                                setOrRefreshAppIcons(Constants.APPID_BUTTON2);
                            }
                        }).execute(Constants.URL_UPDATE_BUTTON2);
                    break;

                case R.id.ivButton3:
                    if (Constants.URL_UPDATE_BUTTON3 != null)
                        new DownloadNewUpdate(this, AppName.RewindVOD.name(), installedPkgs, totalUninstalledPkgs, Constants.FILE_TYPE.apk, new DownloadNewUpdate.OnDownloadStatus() {
                            @Override
                            public void OnComplete() {
                                setOrRefreshAppIcons(Constants.APPID_BUTTON3);
                            }
                        }).execute(Constants.URL_UPDATE_BUTTON3);
                    break;

                case R.id.ivButton4:
                    if (Constants.URL_UPDATE_BUTTON4 != null)
                        new DownloadNewUpdate(this, AppName.FuseLive.name(), installedPkgs, totalUninstalledPkgs, Constants.FILE_TYPE.apk, new DownloadNewUpdate.OnDownloadStatus() {
                            @Override
                            public void OnComplete() {
                                setOrRefreshAppIcons(Constants.APPID_BUTTON4);
                            }
                        }).execute(Constants.URL_UPDATE_BUTTON4);
                    break;

                case R.id.ivButton5:
                    if (Constants.URL_UPDATE_BUTTON5 != null)
                        new DownloadNewUpdate(this, AppName.blaze.name(), installedPkgs, totalUninstalledPkgs, Constants.FILE_TYPE.apk, new DownloadNewUpdate.OnDownloadStatus() {
                            @Override
                            public void OnComplete() {
                                setOrRefreshAppIcons(Constants.APPID_BUTTON5);
                            }
                        }).execute(Constants.URL_UPDATE_BUTTON5);
                    break;

                case R.id.ivButton6:
                    if (Constants.URL_UPDATE_BUTTON6 != null)
                        new DownloadNewUpdate(this, AppName.blaze.name(), installedPkgs, totalUninstalledPkgs, Constants.FILE_TYPE.apk, new DownloadNewUpdate.OnDownloadStatus() {
                            @Override
                            public void OnComplete() {
                                setOrRefreshAppIcons(Constants.APPID_BUTTON6);
                            }
                        }).execute(Constants.URL_UPDATE_BUTTON6);
                    break;

                case R.id.ivButton7:
                    if (Constants.URL_UPDATE_BUTTON7 != null)
                        new DownloadNewUpdate(this, AppName.blaze.name(), installedPkgs, totalUninstalledPkgs, Constants.FILE_TYPE.apk, new DownloadNewUpdate.OnDownloadStatus() {
                            @Override
                            public void OnComplete() {
                                setOrRefreshAppIcons(Constants.APPID_BUTTON7);
                            }
                        }).execute(Constants.URL_UPDATE_BUTTON7);
                    break;

                case R.id.ivButton8:
                    if (Constants.URL_UPDATE_BUTTON8 != null)
                        new DownloadNewUpdate(this, AppName.blaze.name(), installedPkgs, totalUninstalledPkgs, Constants.FILE_TYPE.apk, new DownloadNewUpdate.OnDownloadStatus() {
                            @Override
                            public void OnComplete() {
                                setOrRefreshAppIcons(Constants.APPID_BUTTON8);
                            }
                        }).execute(Constants.URL_UPDATE_BUTTON8);
                    break;

                case R.id.ivButton9:
                    if (Constants.URL_UPDATE_BUTTON9 != null)
                        new DownloadNewUpdate(this, AppName.blaze.name(), installedPkgs, totalUninstalledPkgs, Constants.FILE_TYPE.apk, new DownloadNewUpdate.OnDownloadStatus() {
                            @Override
                            public void OnComplete() {
                                setOrRefreshAppIcons(Constants.APPID_BUTTON9);
                            }
                        }).execute(Constants.URL_UPDATE_BUTTON9);
                    break;

                case R.id.ivButton10:
                    if (Constants.URL_UPDATE_BUTTON10 != null)
                        new DownloadNewUpdate(this, AppName.toolsApp.name(), installedPkgs, totalUninstalledPkgs, Constants.FILE_TYPE.apk, new DownloadNewUpdate.OnDownloadStatus() {
                            @Override
                            public void OnComplete() {
                                setOrRefreshAppIcons(Constants.APPID_BUTTON10);
                            }
                        }).execute(Constants.URL_UPDATE_BUTTON10);
                    break;
                case R.id.btn_left_top:
                    if (Constants.URL_UPDATE_BUTTON_left_top != null)
                        new DownloadNewUpdate(this, AppName.toolsApp.name(), 0, 0, Constants.FILE_TYPE.apk, new DownloadNewUpdate.OnDownloadStatus() {
                            @Override
                            public void OnComplete() {
//                                setOrRefreshAppIcons(Constants.APPID_BUTTON_left1);
                            }
                        }).execute(Constants.URL_UPDATE_BUTTON_left_top);
                    break;
                case R.id.btn_left_top_right:
                    if (Constants.URL_UPDATE_BUTTON_left_top_right != null)
                        new DownloadNewUpdate(this, AppName.toolsApp.name(), 0, 0, Constants.FILE_TYPE.apk, new DownloadNewUpdate.OnDownloadStatus() {
                            @Override
                            public void OnComplete() {
//                                setOrRefreshAppIcons(Constants.APPID_BUTTON_left1);
                            }
                        }).execute(Constants.URL_UPDATE_BUTTON_left_top_right);
                    break;
                case R.id.btn_left1:
                    if (Constants.URL_UPDATE_BUTTON_left1 != null)
                        new DownloadNewUpdate(this, AppName.toolsApp.name(), 0, 0, Constants.FILE_TYPE.apk, new DownloadNewUpdate.OnDownloadStatus() {
                            @Override
                            public void OnComplete() {
//                                setOrRefreshAppIcons(Constants.APPID_BUTTON_left1);
                            }
                        }).execute(Constants.URL_UPDATE_BUTTON_left1);
                    break;
                case R.id.btn_left2:
                    if (Constants.URL_UPDATE_BUTTON_left2 != null)
                        new DownloadNewUpdate(this, AppName.toolsApp.name(), 0, 0, Constants.FILE_TYPE.apk, new DownloadNewUpdate.OnDownloadStatus() {
                            @Override
                            public void OnComplete() {
//                                setOrRefreshAppIcons(Constants.APPID_BUTTON_left2);

                            }
                        }).execute(Constants.URL_UPDATE_BUTTON_left2);
                    break;
                case R.id.btn_left3:
                    if (Constants.URL_UPDATE_BUTTON_left3 != null)
                        new DownloadNewUpdate(this, AppName.toolsApp.name(), 0, 0, Constants.FILE_TYPE.apk, new DownloadNewUpdate.OnDownloadStatus() {
                            @Override
                            public void OnComplete() {
//                                setOrRefreshAppIcons(Constants.APPID_BUTTON_left3);

                            }
                        }).execute(Constants.URL_UPDATE_BUTTON_left3);
                    break;
                case R.id.btn_right1:
                    if (Constants.URL_UPDATE_BUTTON_right1 != null)
                        new DownloadNewUpdate(this, AppName.toolsApp.name(), 0, 0, Constants.FILE_TYPE.apk, new DownloadNewUpdate.OnDownloadStatus() {
                            @Override
                            public void OnComplete() {
//                                setOrRefreshAppIcons(Constants.APPID_BUTTON_left3);

                            }
                        }).execute(Constants.URL_UPDATE_BUTTON_right1);
                    break;
                case R.id.btn_right2:
                    if (Constants.URL_UPDATE_BUTTON_right2 != null)
                        new DownloadNewUpdate(this, AppName.toolsApp.name(), 0, 0, Constants.FILE_TYPE.apk, new DownloadNewUpdate.OnDownloadStatus() {
                            @Override
                            public void OnComplete() {
//                                setOrRefreshAppIcons(Constants.APPID_BUTTON_left3);

                            }
                        }).execute(Constants.URL_UPDATE_BUTTON_right2);
                    break;
                case R.id.btn_right3:
                    if (Constants.URL_UPDATE_BUTTON_right3 != null)
                        new DownloadNewUpdate(this, AppName.toolsApp.name(), 0, 0, Constants.FILE_TYPE.apk, new DownloadNewUpdate.OnDownloadStatus() {
                            @Override
                            public void OnComplete() {
//                                setOrRefreshAppIcons(Constants.APPID_BUTTON_left3);

                            }
                        }).execute(Constants.URL_UPDATE_BUTTON_right3);
                    break;

                case R.id.ivPoweBtn:
                    if (Constants.URL_UPDATE_POWER_BTN != null)
                        new DownloadNewUpdate(this, AppName.powerBtn.name(), 0, 0, Constants.FILE_TYPE.apk, null).execute(Constants.URL_UPDATE_POWER_BTN);
                    break;

                case R.id.ivVpnBtn:
                    if (Constants.URL_UPDATE_VPN_BTN != null)
                        new DownloadNewUpdate(this, AppName.vpnBtn.name(), 0, 0, Constants.FILE_TYPE.apk, null).execute(Constants.URL_UPDATE_VPN_BTN);
                    break;
                case R.id.ivMiniButton1:
                    if (Constants.URL_UPDATE_MiniButton1 != null)
                        new DownloadNewUpdate(this, AppName.miniButton1.name(), 0, 0, Constants.FILE_TYPE.apk, null).execute(Constants.URL_UPDATE_MiniButton1);
                    break;

                case R.id.ivMiniButton2:
                    if (Constants.URL_UPDATE_MiniButton2 != null)
                        new DownloadNewUpdate(this, AppName.miniButton2.name(), 0, 0, Constants.FILE_TYPE.apk, null).execute(Constants.URL_UPDATE_MiniButton2);
                    break;

                case R.id.ivMiniButton3:
                    if (Constants.URL_UPDATE_MiniButton3 != null)
                        new DownloadNewUpdate(this, AppName.miniButton3.name(), 0, 0, Constants.FILE_TYPE.apk, null).execute(Constants.URL_UPDATE_MiniButton3);
                    break;

                case R.id.ivMiniButton4:
                    if (Constants.URL_UPDATE_MiniButton4 != null)
                        new DownloadNewUpdate(this, AppName.miniButton4.name(), 0, 0, Constants.FILE_TYPE.apk, null).execute(Constants.URL_UPDATE_MiniButton4);
                    break;

                case R.id.ivMiniButton5:
                    if (Constants.URL_UPDATE_MiniButton5 != null)
                        new DownloadNewUpdate(this, AppName.miniButton5.name(), 0, 0, Constants.FILE_TYPE.apk, null).execute(Constants.URL_UPDATE_MiniButton5);
                    break;

                case R.id.ivMiniButton6:
                    if (Constants.URL_UPDATE_MiniButton6 != null)
                        new DownloadNewUpdate(this, AppName.miniButton6.name(), 0, 0, Constants.FILE_TYPE.apk, null).execute(Constants.URL_UPDATE_MiniButton6);
                    break;

                case R.id.ivMiniButton7:
                    if (Constants.URL_UPDATE_MiniButton7 != null)
                        new DownloadNewUpdate(this, AppName.miniButton7.name(), 0, 0, Constants.FILE_TYPE.apk, null).execute(Constants.URL_UPDATE_MiniButton7);
                    break;

                case R.id.ivMiniButton8:
                    if (Constants.URL_UPDATE_MiniButton8 != null)
                        new DownloadNewUpdate(this, AppName.miniButton8.name(), 0, 0, Constants.FILE_TYPE.apk, null).execute(Constants.URL_UPDATE_MiniButton8);
                    break;

                case R.id.ivMiniButton9:
                    if (Constants.URL_UPDATE_MiniButton9 != null)
                        new DownloadNewUpdate(this, AppName.miniButton9.name(), 0, 0, Constants.FILE_TYPE.apk, null).execute(Constants.URL_UPDATE_MiniButton9);
                    break;

                case R.id.ivMiniButton10:
                    if (Constants.URL_UPDATE_MiniButton10 != null)
                        new DownloadNewUpdate(this, AppName.miniButton10.name(), 0, 0, Constants.FILE_TYPE.apk, null).execute(Constants.URL_UPDATE_MiniButton10);
                    break;

                case R.id.ivMiniButton11:
                    if (Constants.URL_UPDATE_MiniButton11 != null)
                        new DownloadNewUpdate(this, AppName.miniButton11.name(), 0, 0, Constants.FILE_TYPE.apk, null).execute(Constants.URL_UPDATE_MiniButton11);
                    break;

                case R.id.ivMiniButton12:
                    if (Constants.URL_UPDATE_MiniButton12 != null)
                        new DownloadNewUpdate(this, AppName.miniButton12.name(), 0, 0, Constants.FILE_TYPE.apk, null).execute(Constants.URL_UPDATE_MiniButton12);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private enum AppName {
        zLauncher,
        MediaCenter,
        RewindVOD,
        FuseLive,
        blaze,
        freevod,
        toolsApp,
        powerBtn,
        vpnBtn,

        miniButton1,
        miniButton2,
        miniButton3,
        miniButton4,
        miniButton5,
        miniButton6,
        miniButton7,
        miniButton8,
        miniButton9,
        miniButton10,
        miniButton11,
        miniButton12
    }

    private void showUpdateLauncherDialog() {
        try {
            changeAppDialog = new AlertDialog.Builder(this);
            changeAppDialog.setMessage("Do you want to download the newest version?")
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (HelperUtils.haveNetworkConnection(HomePageActivity.this) > 0) {
                                if (Build.VERSION.SDK_INT >= 23) {
                                    if (checkPermission()) {
                                        // Your Permission granted already .Do next code
                                        new DownloadNewUpdate(HomePageActivity.this, AppName.zLauncher.name(), 0, 0, Constants.FILE_TYPE.apk, null).execute(Constants.UPDATE_ZLAUNCHER_URL);
                                    } else {
                                        requestPermission(ivButtonTop); // Code for permission
                                    }
                                } else {
                                    new DownloadNewUpdate(HomePageActivity.this, AppName.zLauncher.name(), 0, 0, Constants.FILE_TYPE.apk, null).execute(Constants.UPDATE_ZLAUNCHER_URL);
                                }

                            }
                            dialog.cancel();
                            changeAppDialog = null;
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            changeAppDialog = null;
                        }
                    });

            AlertDialog alertDialog = changeAppDialog.create();
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

    private void doFetchAndUpdateAppIdAndIcons() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HelperUtils.firstTimeInstallAskForActivationCode(HomePageActivity.this, new HelperUtils.OnActivatedListener() {
                        @Override
                        public void onResult() {
                            Constants.IS_FIRST_LAUNCH_SETTINGS_FETCH_DONE = false;
                            //showLoading("LOADING...");
                            DownloadImage.doFetchUpdateDetails(false, HomePageActivity.this, new DownloadImage.OnJSONLoadListener() {
                                @Override
                                public void OnComplete(final boolean isNeedToDownloadIcon, final boolean isInstantAppUpdateRequire) {
                                    Handler handler = new Handler(Looper.getMainLooper());
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            compareAppVersion();
                                        }
                                    });
                                    doLoadHtmlRssFeed();
                                    if (isNeedToDownloadIcon) {
                                        showLoading("Updating Home Screen App 1...");
                                        System.out.println("HomePageActivity.OnComplete");
                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_BUTTON1, Constants.UpdatableAppNameKey.icon_button1.name(), new DownloadImage.OnSaveComplete() {
                                            @Override
                                            public void LoadImage(boolean isSuccess) {
                                                Constants.icon_button1 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button1.name());
                                                System.out.println("HomePageActivity.LoadImage1");
                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_BUTTON2, Constants.UpdatableAppNameKey.icon_button2.name(), new DownloadImage.OnSaveComplete() {
                                                    @Override
                                                    public void LoadImage(boolean isSuccess) {

                                                        Constants.icon_button2 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button2.name());
                                                        System.out.println("HomePageActivity.LoadImage2");
                                                        showLoading("Updating Home Screen App 2...");
                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_BUTTON3, Constants.UpdatableAppNameKey.icon_button3.name(), new DownloadImage.OnSaveComplete() {
                                                            @Override
                                                            public void LoadImage(boolean isSuccess) {

                                                                Constants.icon_button3 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button3.name());
                                                                System.out.println("HomePageActivity.LoadImage3");
                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_BUTTON4, Constants.UpdatableAppNameKey.icon_button4.name(), new DownloadImage.OnSaveComplete() {
                                                                    @Override
                                                                    public void LoadImage(boolean isSuccess) {

                                                                        Constants.icon_button4 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button4.name());
                                                                        System.out.println("HomePageActivity.LoadImage4");
                                                                        showLoading("Updating Home Screen App 3...");
                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_BUTTON5, Constants.UpdatableAppNameKey.icon_button5.name(), new DownloadImage.OnSaveComplete() {
                                                                            @Override
                                                                            public void LoadImage(boolean isSuccess) {

                                                                                Constants.icon_button5 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button5.name());
                                                                                System.out.println("HomePageActivity.LoadImage5");
                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_BUTTON6, Constants.UpdatableAppNameKey.icon_button6.name(), new DownloadImage.OnSaveComplete() {
                                                                                    @Override
                                                                                    public void LoadImage(boolean isSuccess) {

                                                                                        Constants.icon_button6 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button6.name());
                                                                                        System.out.println("HomePageActivity.LoadImage6");
                                                                                        showLoading("Updating Home Screen App 4...");
                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_BUTTON7, Constants.UpdatableAppNameKey.icon_button7.name(), new DownloadImage.OnSaveComplete() {
                                                                                            @Override
                                                                                            public void LoadImage(boolean isSuccess) {

                                                                                                Constants.icon_button7 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button7.name());
                                                                                                System.out.println("HomePageActivity.LoadImage7");
                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_BUTTON8, Constants.UpdatableAppNameKey.icon_button8.name(), new DownloadImage.OnSaveComplete() {
                                                                                                    @Override
                                                                                                    public void LoadImage(boolean isSuccess) {

                                                                                                        Constants.icon_button8 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button8.name());
                                                                                                        System.out.println("HomePageActivity.LoadImage8");
                                                                                                        showLoading("Updating Home Screen App 5...");
                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_BUTTON9, Constants.UpdatableAppNameKey.icon_button9.name(), new DownloadImage.OnSaveComplete() {
                                                                                                            @Override
                                                                                                            public void LoadImage(boolean isSuccess) {

                                                                                                                Constants.icon_button9 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button9.name());
                                                                                                                System.out.println("HomePageActivity.LoadImage9");
                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_BUTTON10, Constants.UpdatableAppNameKey.icon_button10.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                    @Override
                                                                                                                    public void LoadImage(boolean isSuccess) {

                                                                                                                        Constants.icon_button10 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button10.name());
                                                                                                                        System.out.println("HomePageActivity.LoadImage10");
                                                                                                                        showLoading("Updating Home Screen App 6...");
                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_BUTTON11, Constants.UpdatableAppNameKey.icon_button11.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                            @Override
                                                                                                                            public void LoadImage(boolean isSuccess) {

                                                                                                                                Constants.icon_button11 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button11.name());
                                                                                                                                System.out.println("HomePageActivity.LoadImage11");


                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_BUTTON1, Constants.UpdatableAppNameKey.icon_pressed_button1.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                    @Override
                                                                                                                                    public void LoadImage(boolean isSuccess) {
                                                                                                                                        System.out.println("HomePageActivity.LoadImage1+");
                                                                                                                                        showLoading("Updating Home Screen App 7...");
                                                                                                                                        Constants.icon_pressed_button1 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button1.name());
                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_BUTTON2, Constants.UpdatableAppNameKey.icon_pressed_button2.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                            @Override
                                                                                                                                            public void LoadImage(boolean isSuccess) {
                                                                                                                                                System.out.println("HomePageActivity.LoadImage2+");
                                                                                                                                                Constants.icon_pressed_button2 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button2.name());
                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_BUTTON3, Constants.UpdatableAppNameKey.icon_pressed_button3.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                    @Override
                                                                                                                                                    public void LoadImage(boolean isSuccess) {
                                                                                                                                                        System.out.println("HomePageActivity.LoadImage3+");
                                                                                                                                                        showLoading("Updating Home Screen App 8...");
                                                                                                                                                        Constants.icon_pressed_button3 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button3.name());
                                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_BUTTON4, Constants.UpdatableAppNameKey.icon_pressed_button4.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                            @Override
                                                                                                                                                            public void LoadImage(boolean isSuccess) {
                                                                                                                                                                System.out.println("HomePageActivity.LoadImage4+");
                                                                                                                                                                Constants.icon_pressed_button4 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button4.name());
                                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_BUTTON5, Constants.UpdatableAppNameKey.icon_pressed_button5.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                    @Override
                                                                                                                                                                    public void LoadImage(boolean isSuccess) {
                                                                                                                                                                        System.out.println("HomePageActivity.LoadImage5+");
                                                                                                                                                                        showLoading("Updating Home Screen App 9...");
                                                                                                                                                                        Constants.icon_pressed_button5 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button5.name());

                                                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_BUTTON6, Constants.UpdatableAppNameKey.icon_pressed_button6.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                            @Override
                                                                                                                                                                            public void LoadImage(boolean isSuccess) {
                                                                                                                                                                                System.out.println("HomePageActivity.LoadImage6+");
                                                                                                                                                                                Constants.icon_pressed_button6 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button6.name());
                                                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_BUTTON7, Constants.UpdatableAppNameKey.icon_pressed_button7.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                    @Override
                                                                                                                                                                                    public void LoadImage(boolean isSuccess) {
                                                                                                                                                                                        System.out.println("HomePageActivity.LoadImage7+");
                                                                                                                                                                                        showLoading("Updating Home Screen App 10...");
                                                                                                                                                                                        Constants.icon_pressed_button7 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button7.name());
                                                                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_BUTTON8, Constants.UpdatableAppNameKey.icon_pressed_button8.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                            @Override
                                                                                                                                                                                            public void LoadImage(boolean isSuccess) {
                                                                                                                                                                                                System.out.println("HomePageActivity.LoadImage8+");
                                                                                                                                                                                                Constants.icon_pressed_button8 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button8.name());
                                                                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_BUTTON9, Constants.UpdatableAppNameKey.icon_pressed_button9.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                    @Override
                                                                                                                                                                                                    public void LoadImage(boolean isSuccess) {
                                                                                                                                                                                                        System.out.println("HomePageActivity.LoadImage9+");
                                                                                                                                                                                                        showLoading("Updating Home Screen App 11...");
                                                                                                                                                                                                        Constants.icon_pressed_button9 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button9.name());
                                                                                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_BUTTON10, Constants.UpdatableAppNameKey.icon_pressed_button10.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                            @Override
                                                                                                                                                                                                            public void LoadImage(boolean isSuccess) {
                                                                                                                                                                                                                System.out.println("HomePageActivity.LoadImage10+");
                                                                                                                                                                                                                Constants.icon_pressed_button10 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button10.name());
                                                                                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_BUTTON11, Constants.UpdatableAppNameKey.icon_pressed_button11.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                    @Override
                                                                                                                                                                                                                    public void LoadImage(boolean isSuccess) {
                                                                                                                                                                                                                        System.out.println("HomePageActivity.LoadImage11+");
                                                                                                                                                                                                                        showLoading("Updating Home Screen App 12...");
                                                                                                                                                                                                                        Constants.icon_pressed_button11 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button11.name());
                                                                                                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_DOCK_BUTTON, Constants.UpdatableAppNameKey.icon_dock_button.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                            @Override
                                                                                                                                                                                                                            public void LoadImage(boolean isSuccess) {
                                                                                                                                                                                                                                System.out.println("HomePageActivity.LoadImage12");
                                                                                                                                                                                                                                Constants.icon_dock_button = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_dock_button.name());

                                                                                                                                                                                                                                showLoading("Updating Home Screen Mini Button 1...");
                                                                                                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_MINIBUTTON1, Constants.UpdatableAppNameKey.icon_minibutton1.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                    @Override
                                                                                                                                                                                                                                    public void LoadImage(boolean isSuccess) {
                                                                                                                                                                                                                                        System.out.println("HomePageActivity.LoadImage10+");
                                                                                                                                                                                                                                        Constants.icon_minibutton1 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton1.name());
                                                                                                                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_MINIBUTTON1, Constants.UpdatableAppNameKey.icon_pressed_minibutton1.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                            @Override
                                                                                                                                                                                                                                            public void LoadImage(boolean isSuccess) {
                                                                                                                                                                                                                                                System.out.println("HomePageActivity.LoadImage10+");
                                                                                                                                                                                                                                                Constants.icon_pressed_minibutton1 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_minibutton1.name());

                                                                                                                                                                                                                                                showLoading("Updating Home Screen Mini Button 2...");
                                                                                                                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_MINIBUTTON2, Constants.UpdatableAppNameKey.icon_minibutton2.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                    @Override
                                                                                                                                                                                                                                                    public void LoadImage(boolean isSuccess) {

                                                                                                                                                                                                                                                        Constants.icon_minibutton2 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton2.name());
                                                                                                                                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_MINIBUTTON2, Constants.UpdatableAppNameKey.icon_pressed_minibutton2.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                            @Override
                                                                                                                                                                                                                                                            public void LoadImage(boolean isSuccess) {

                                                                                                                                                                                                                                                                Constants.icon_pressed_minibutton2 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_minibutton2.name());
                                                                                                                                                                                                                                                                showLoading("Updating Home Screen Mini Button 3...");
                                                                                                                                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_MINIBUTTON3, Constants.UpdatableAppNameKey.icon_minibutton3.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                    @Override
                                                                                                                                                                                                                                                                    public void LoadImage(boolean isSuccess) {

                                                                                                                                                                                                                                                                        Constants.icon_minibutton3 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton3.name());
                                                                                                                                                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_MINIBUTTON3, Constants.UpdatableAppNameKey.icon_pressed_minibutton3.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                            @Override
                                                                                                                                                                                                                                                                            public void LoadImage(boolean isSuccess) {

                                                                                                                                                                                                                                                                                Constants.icon_pressed_minibutton3 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_minibutton3.name());

                                                                                                                                                                                                                                                                                showLoading("Updating Home Screen Mini Button 4...");
                                                                                                                                                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_MINIBUTTON4, Constants.UpdatableAppNameKey.icon_minibutton4.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                    @Override
                                                                                                                                                                                                                                                                                    public void LoadImage(boolean isSuccess) {

                                                                                                                                                                                                                                                                                        Constants.icon_minibutton4 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton4.name());
                                                                                                                                                                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_MINIBUTTON4, Constants.UpdatableAppNameKey.icon_pressed_minibutton4.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                            @Override
                                                                                                                                                                                                                                                                                            public void LoadImage(boolean isSuccess) {

                                                                                                                                                                                                                                                                                                Constants.icon_pressed_minibutton4 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_minibutton4.name());

                                                                                                                                                                                                                                                                                                showLoading("Updating Home Screen Mini Button 5...");
                                                                                                                                                                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_MINIBUTTON5, Constants.UpdatableAppNameKey.icon_minibutton5.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                    @Override
                                                                                                                                                                                                                                                                                                    public void LoadImage(boolean isSuccess) {

                                                                                                                                                                                                                                                                                                        Constants.icon_minibutton5 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton5.name());
                                                                                                                                                                                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_MINIBUTTON5, Constants.UpdatableAppNameKey.icon_pressed_minibutton5.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                            @Override
                                                                                                                                                                                                                                                                                                            public void LoadImage(boolean isSuccess) {

                                                                                                                                                                                                                                                                                                                Constants.icon_pressed_minibutton5 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_minibutton5.name());
                                                                                                                                                                                                                                                                                                                showLoading("Updating Home Screen Mini Button 6...");
                                                                                                                                                                                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_MINIBUTTON6, Constants.UpdatableAppNameKey.icon_minibutton6.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                    @Override
                                                                                                                                                                                                                                                                                                                    public void LoadImage(boolean isSuccess) {

                                                                                                                                                                                                                                                                                                                        Constants.icon_minibutton6 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton6.name());
                                                                                                                                                                                                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_MINIBUTTON6, Constants.UpdatableAppNameKey.icon_pressed_minibutton6.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                            @Override
                                                                                                                                                                                                                                                                                                                            public void LoadImage(boolean isSuccess) {

                                                                                                                                                                                                                                                                                                                                Constants.icon_pressed_minibutton6 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_minibutton6.name());
                                                                                                                                                                                                                                                                                                                                showLoading("Updating Home Screen Mini Button 7...");
                                                                                                                                                                                                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_MINIBUTTON7, Constants.UpdatableAppNameKey.icon_minibutton7.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                    @Override
                                                                                                                                                                                                                                                                                                                                    public void LoadImage(boolean isSuccess) {

                                                                                                                                                                                                                                                                                                                                        Constants.icon_minibutton7 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton7.name());
                                                                                                                                                                                                                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_MINIBUTTON7, Constants.UpdatableAppNameKey.icon_pressed_minibutton7.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                            @Override
                                                                                                                                                                                                                                                                                                                                            public void LoadImage(boolean isSuccess) {

                                                                                                                                                                                                                                                                                                                                                Constants.icon_pressed_minibutton7 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_minibutton7.name());
                                                                                                                                                                                                                                                                                                                                                showLoading("Updating Home Screen Mini Button 8...");
                                                                                                                                                                                                                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_MINIBUTTON8, Constants.UpdatableAppNameKey.icon_minibutton8.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                    @Override
                                                                                                                                                                                                                                                                                                                                                    public void LoadImage(boolean isSuccess) {

                                                                                                                                                                                                                                                                                                                                                        Constants.icon_minibutton8 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton8.name());
                                                                                                                                                                                                                                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_MINIBUTTON8, Constants.UpdatableAppNameKey.icon_pressed_minibutton8.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                            @Override
                                                                                                                                                                                                                                                                                                                                                            public void LoadImage(boolean isSuccess) {

                                                                                                                                                                                                                                                                                                                                                                Constants.icon_pressed_minibutton8 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_minibutton8.name());
                                                                                                                                                                                                                                                                                                                                                                showLoading("Updating Home Screen Mini Button 9...");
                                                                                                                                                                                                                                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_MINIBUTTON9, Constants.UpdatableAppNameKey.icon_minibutton9.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                                    @Override
                                                                                                                                                                                                                                                                                                                                                                    public void LoadImage(boolean isSuccess) {

                                                                                                                                                                                                                                                                                                                                                                        Constants.icon_minibutton9 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton9.name());
                                                                                                                                                                                                                                                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_MINIBUTTON9, Constants.UpdatableAppNameKey.icon_pressed_minibutton9.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                                            @Override
                                                                                                                                                                                                                                                                                                                                                                            public void LoadImage(boolean isSuccess) {

                                                                                                                                                                                                                                                                                                                                                                                Constants.icon_pressed_minibutton9 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_minibutton9.name());
                                                                                                                                                                                                                                                                                                                                                                                showLoading("Updating Home Screen Mini Button 10...");
                                                                                                                                                                                                                                                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_MINIBUTTON10, Constants.UpdatableAppNameKey.icon_minibutton10.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                                                    @Override
                                                                                                                                                                                                                                                                                                                                                                                    public void LoadImage(boolean isSuccess) {

                                                                                                                                                                                                                                                                                                                                                                                        Constants.icon_minibutton10 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton10.name());
                                                                                                                                                                                                                                                                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_MINIBUTTON10, Constants.UpdatableAppNameKey.icon_pressed_minibutton10.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                                                            @Override
                                                                                                                                                                                                                                                                                                                                                                                            public void LoadImage(boolean isSuccess) {

                                                                                                                                                                                                                                                                                                                                                                                                Constants.icon_pressed_minibutton10 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_minibutton10.name());
                                                                                                                                                                                                                                                                                                                                                                                                showLoading("Updating Home Screen Mini Button 11...");
                                                                                                                                                                                                                                                                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_MINIBUTTON11, Constants.UpdatableAppNameKey.icon_minibutton11.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                                                                    @Override
                                                                                                                                                                                                                                                                                                                                                                                                    public void LoadImage(boolean isSuccess) {

                                                                                                                                                                                                                                                                                                                                                                                                        Constants.icon_minibutton11 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton11.name());
                                                                                                                                                                                                                                                                                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_MINIBUTTON11, Constants.UpdatableAppNameKey.icon_pressed_minibutton11.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                                                                            @Override
                                                                                                                                                                                                                                                                                                                                                                                                            public void LoadImage(boolean isSuccess) {

                                                                                                                                                                                                                                                                                                                                                                                                                Constants.icon_pressed_minibutton11 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_minibutton11.name());
                                                                                                                                                                                                                                                                                                                                                                                                                showLoading("Updating Home Screen Mini Button 12...");
                                                                                                                                                                                                                                                                                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_MINIBUTTON12, Constants.UpdatableAppNameKey.icon_minibutton12.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                                                                                    @Override
                                                                                                                                                                                                                                                                                                                                                                                                                    public void LoadImage(boolean isSuccess) {

                                                                                                                                                                                                                                                                                                                                                                                                                        Constants.icon_minibutton12 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton12.name());
                                                                                                                                                                                                                                                                                                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_MINIBUTTON12, Constants.UpdatableAppNameKey.icon_pressed_minibutton12.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                                                                                            @Override
                                                                                                                                                                                                                                                                                                                                                                                                                            public void LoadImage(boolean isSuccess) {

                                                                                                                                                                                                                                                                                                                                                                                                                                Constants.icon_pressed_minibutton12 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_minibutton12.name());
                                                                                                                                                                                                                                                                                                                                                                                                                                showLoading("Updating Home Screen Mini Button 13...");
                                                                                                                                                                                                                                                                                                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_MINIBUTTON13, Constants.UpdatableAppNameKey.icon_minibutton13.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                                                                                                    @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                    public void LoadImage(boolean isSuccess) {

                                                                                                                                                                                                                                                                                                                                                                                                                                        Constants.icon_minibutton13 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton13.name());
                                                                                                                                                                                                                                                                                                                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_MINIBUTTON13, Constants.UpdatableAppNameKey.icon_pressed_minibutton13.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                                                                                                            @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                            public void LoadImage(boolean isSuccess) {

                                                                                                                                                                                                                                                                                                                                                                                                                                                Constants.icon_pressed_minibutton13 = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_minibutton13.name());

                                                                                                                                                                                                                                                                                                                                                                                                                                                Handler handler = new Handler(Looper.getMainLooper());
                                                                                                                                                                                                                                                                                                                                                                                                                                                handler.post(new Runnable() {
                                                                                                                                                                                                                                                                                                                                                                                                                                                    @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                    public void run() {
//                                                                                                                                                                                                                                                                                                                                                                                                                                                        setNetworkIcon();
                                                                                                                                                                                                                                                                                                                                                                                                                                                        setMiniAndAdIcons();

                                                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                });
                                                                                                                                                                                                                                                                                                                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_DOCK_BUTTON, Constants.UpdatableAppNameKey.icon_pressed_dock_button.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                                                                                                                    @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                    public void LoadImage(boolean isSuccess) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                        System.out.println("HomePageActivity.LoadImage12+");
                                                                                                                                                                                                                                                                                                                                                                                                                                                        showLoading("Finalizing...");
                                                                                                                                                                                                                                                                                                                                                                                                                                                        Constants.icon_pressed_dock_button = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_dock_button.name());
                                                                                                                                                                                                                                                                                                                                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_PWR_BUTTON, Constants.UpdatableAppNameKey.icon_pwr_button.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                                                                                                                            @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                            public void LoadImage(boolean isSuccess) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                System.out.println("HomePageActivity.LoadImage13");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                Constants.icon_power_button = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pwr_button.name());
                                                                                                                                                                                                                                                                                                                                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_PWR_BUTTON, Constants.UpdatableAppNameKey.icon_pressed_pwr_button.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    public void LoadImage(boolean isSuccess) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        System.out.println("HomePageActivity.LoadImage13+");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        Constants.icon_pressed_power_button = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_pwr_button.name());
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_HELP_BUTTON, Constants.UpdatableAppNameKey.icon_pressed_help_button.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            public void LoadImage(boolean isSuccess) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                System.out.println("HomePageActivity.LoadImage14+");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Constants.icon_pressed_help_button = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_help_button.name());
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_HELP_BUTTON, Constants.UpdatableAppNameKey.icon_help_button.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    public void LoadImage(boolean isSuccess) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        System.out.println("HomePageActivity.LoadImage14");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        Constants.icon_help_button = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_help_button.name());
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_BLANK_BUTTON, Constants.UpdatableAppNameKey.icon_blank_button.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            public void LoadImage(boolean isSuccess) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                System.out.println("HomePageActivity.LoadImage15");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Constants.icon_blank_button = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_blank_button.name());
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_BLANK_BUTTON, Constants.UpdatableAppNameKey.icon_pressed_blank_button.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    public void LoadImage(boolean isSuccess) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        System.out.println("HomePageActivity.LoadImage15+");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        Constants.icon_pressed_blank_button = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_blank_button.name());
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_DOUBLEBLANK_BUTTON, Constants.UpdatableAppNameKey.icon_doubleblank_button.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            public void LoadImage(boolean isSuccess) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                System.out.println("HomePageActivity.LoadImage16");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Constants.icon_big_blank_button = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_doubleblank_button.name());
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_DOUBLEBLANK_BUTTON, Constants.UpdatableAppNameKey.icon_pressed_doubleblank_button.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    public void LoadImage(boolean isSuccess) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        System.out.println("HomePageActivity.LoadImage16+");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        Constants.icon_pressed_big_blank_button = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_doubleblank_button.name());
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_BUTTON0, Constants.UpdatableAppNameKey.icon_button0.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            public void LoadImage(boolean isSuccess) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                System.out.println("HomePageActivity.LoadImage16+");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Constants.icon_button_top = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_button0.name());
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_BUTTON0, Constants.UpdatableAppNameKey.icon_pressed_button0.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    public void LoadImage(boolean isSuccess) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        System.out.println("HomePageActivity.LoadImage16+");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        Constants.icon_pressed_button_top = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_button0.name());
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_URL_BUTTON_VPN, Constants.UpdatableAppNameKey.icon_vpn_button.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            public void LoadImage(boolean isSuccess) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                System.out.println("HomePageActivity.LoadImage17+");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Constants.icon_pressed_vpn_button = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_vpn_button.name());
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                DownloadImage.doDownloadImages(HomePageActivity.this, Constants.ICON_PRESSED_URL_BUTTON_VPN, Constants.UpdatableAppNameKey.icon_pressed_vpn_button.name(), new DownloadImage.OnSaveComplete() {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    public void LoadImage(boolean isSuccess) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        System.out.println("HomePageActivity.LoadImage17");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        Constants.icon_vpn_button = ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_vpn_button.name());
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        doUpdateBackGroundImageAndMainApps(isNeedToDownloadIcon, isInstantAppUpdateRequire);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                });
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        });
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                });
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        });
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                });
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        });
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                });
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        });
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                });
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        });
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                });
                                                                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                                                                        });
                                                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                });
                                                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                                                        });
                                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                                });
                                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                                        });
                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                });

                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                        });
                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                });
                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                        });
                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                });
                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                        });
                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                });
                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                        });
                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                });
                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                        });
                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                });
                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                        });
                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                });


                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                        });
                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                });
                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                        });
                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                });
                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                        });
                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                });
                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                        });
                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                });
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                        });
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                });
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                        });
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                });
                                                                                                                                                                                                            }
                                                                                                                                                                                                        });
                                                                                                                                                                                                    }
                                                                                                                                                                                                });
                                                                                                                                                                                            }
                                                                                                                                                                                        });
                                                                                                                                                                                    }
                                                                                                                                                                                });
                                                                                                                                                                            }
                                                                                                                                                                        });
                                                                                                                                                                    }
                                                                                                                                                                });
                                                                                                                                                            }
                                                                                                                                                        });
                                                                                                                                                    }
                                                                                                                                                });
                                                                                                                                            }
                                                                                                                                        });
                                                                                                                                    }
                                                                                                                                });
                                                                                                                            }
                                                                                                                        });
                                                                                                                    }
                                                                                                                });
                                                                                                            }
                                                                                                        });
                                                                                                    }
                                                                                                });
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });

                                    } else {
                                        removeLoading();
                                        doUpdateBackGroundImageAndMainApps(isNeedToDownloadIcon, isInstantAppUpdateRequire);
                                    }
                                }
                            });
                        }
                    });
                } catch
                (Exception
                                e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void deleteAppByStartup(JSONArray mJsonAppIdArray) {
        try {
            if (mJsonAppIdArray != null && mJsonAppIdArray.length() > 0) {
                for (int i = 0; i < mJsonAppIdArray.length(); i++) {
                    String appPackageName = mJsonAppIdArray.optString(i);
                    if (HelperUtils.isAppInstalled(HomePageActivity.this, appPackageName)) {
                        if (APKManager.Uninstall(appPackageName)) {
                            setOrRefreshAppIcons(appPackageName);
                        }
                    }
                }
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    private void doUpdateBackGroundImageAndMainApps(boolean isNeedToUpdateButtons, final boolean isInstantAppUpdateRequire) {
        deleteAppByStartup(app.getDeleteAppsIdListByServer());
        if (app.getHiddenAppsIdListByServer() != null && app.getHiddenAppsIdListByServer().length() > 0) {
            for (int i = 0; i < app.getHiddenAppsIdListByServer().length(); i++) {
                String btnId = app.getHiddenAppsIdListByServer().optString(i);
                boolean isShowBtn = btnId.split(",")[1].equalsIgnoreCase("1"); // 1 means show
                if (btnId.split(",")[0] != null) {
                    SharedPreferenceStorage.setBooleanValue(this, btnId.split(",")[0] + Constants.Storage._isAppHiddenByServer.name(), !isShowBtn);
                    doHideShowAllButton(btnId.split(",")[0], isShowBtn);
                }
            }
        }

        if (isNeedToUpdateButtons) {
            getHomePageMainApps();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (ivButton4 != null) {
                        ivButton4.requestFocus();
                    }
                } catch (
                        Exception e) {
                    e.printStackTrace();
                } finally {
                    if (ivHelpBtn != null)
                        ivHelpBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        if (HelperUtils.haveNetworkConnection(this) > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        DownloadImage.doDownloadImages(HomePageActivity.this, Constants.IMAGE_LOAD_URL, Constants.IMAGE_NAME, new DownloadImage.OnSaveComplete() {
                            @Override
                            public void LoadImage(final boolean isSuccess) {
                                if (isSuccess) {
                                    showAndUpdateBackgroundImage();
                                }
                            }
                        });
                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            if (isInstantAppUpdateRequire) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        // Your Permission granted already .Do next code
                        checkAndUpdateIfInstantAppUpdateRequire();
                    } else {
                        requestPermission(null); // for auto Update view = null
                    }
                } else {
                    checkAndUpdateIfInstantAppUpdateRequire();
                    // Do next code
                }
            } else {
                RootTask.doChangeDisplaySize(HomePageActivity.this);
                SharedPreferenceStorage.setBooleanValue(HomePageActivity.this, Constants.Storage.IS_NEED_TO_CHANGE_DISPLAY_TEXT_SIZE.name(), false);
            }
        }
        checkAndLaunchStartUpApps();
        checkAndShowStartUpMessage();
    }

    private void checkAndLaunchStartUpApps() {
        try {
            if (Constants.APPID_STARTUP != null && Constants.APPID_STARTUP.length() > 1 && Constants.APPID_STARTUP.contains(".")) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.APPID_STARTUP);
                startActivity(launchIntent);
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    private void checkAndShowStartUpMessage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (app != null && app.getStartUpMessageDetails() != null) {
                        if (SharedPreferenceStorage.getIntegerValue(HomePageActivity.this, Constants.Storage.STARTUP_POPUP_SHOWN_UPDATER.name(), 0) < app.getStartUpMessageDetails().getMsg_update())
                            SharedPreferenceStorage.setIntegerValue(HomePageActivity.this, Constants.Storage.STARTUP_POPUP_SHOWN_COUNT.name(), 0);
                        if (app.getStartUpMessageDetails().getHelpFileUrl() != null
                                && app.getStartUpMessageDetails().getHelpFileUrl().length() > 0
                                && app.getStartUpMessageDetails().getTutorialType() != null
                                && SharedPreferenceStorage.getIntegerValue(HomePageActivity.this, Constants.Storage.STARTUP_POPUP_SHOWN_COUNT.name(), 0) <= app.getStartUpMessageDetails().getMsg_frequency()) {
                            switch (app.getStartUpMessageDetails().getTutorialType()) {
                                case html:
                                    openFullWebView(app.getStartUpMessageDetails().getHelpFileUrl());
                                    break;
                                case png:
                                    new DownloadNewUpdate(HomePageActivity.this, "start_up_message", 0, 0, Constants.FILE_TYPE.png, null).openImage(app.getStartUpMessageDetails().getHelpFileUrl());
                                    break;
                            }
                            SharedPreferenceStorage.setIntegerValue(HomePageActivity.this, Constants.Storage.STARTUP_POPUP_SHOWN_COUNT.name(), SharedPreferenceStorage.getIntegerValue(HomePageActivity.this, Constants.Storage.STARTUP_POPUP_SHOWN_COUNT.name(), 0) + 1);
                            SharedPreferenceStorage.setIntegerValue(HomePageActivity.this, Constants.Storage.STARTUP_POPUP_SHOWN_UPDATER.name(), app.getStartUpMessageDetails().getMsg_update());
                        }
                    }
                } catch (
                        Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getHomePageMainApps() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    updateAppLockIcon();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (sfDefIconContainer != null && (sfDefIconContainer.isShimmerVisible() || sfDefIconContainer.isShimmerStarted())) {
                                sfDefIconContainer.stopShimmer();
                                sfDefIconContainer.hideShimmer();
                            }
                        }
                    });

                    if (Constants.DEF_APPID_BUTTON_TOP == null)
                        Constants.DEF_APPID_BUTTON_TOP = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_button0.name(), Constants.DEF_APPID_BUTTON_TOP);
                    if (Constants.DEF_APPID_BUTTON1 == null)
                        Constants.DEF_APPID_BUTTON1 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_button1.name(), Constants.DEF_APPID_BUTTON1);
                    if (Constants.DEF_APPID_BUTTON2 == null)
                        Constants.DEF_APPID_BUTTON2 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_button2.name(), Constants.DEF_APPID_BUTTON2);
                    if (Constants.DEF_APPID_BUTTON3 == null)
                        Constants.DEF_APPID_BUTTON3 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_button3.name(), Constants.DEF_APPID_BUTTON3);
                    if (Constants.DEF_APPID_BUTTON4 == null)
                        Constants.DEF_APPID_BUTTON4 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_button4.name(), Constants.DEF_APPID_BUTTON4);
                    if (Constants.DEF_APPID_BUTTON5 == null)
                        Constants.DEF_APPID_BUTTON5 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_button5.name(), Constants.DEF_APPID_BUTTON5);
                    if (Constants.DEF_APPID_BUTTON6 == null)
                        Constants.DEF_APPID_BUTTON6 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_button6.name(), Constants.DEF_APPID_BUTTON6);
                    if (Constants.DEF_APPID_BUTTON7 == null)
                        Constants.DEF_APPID_BUTTON7 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_button7.name(), Constants.DEF_APPID_BUTTON7);
                    if (Constants.DEF_APPID_BUTTON8 == null)
                        Constants.DEF_APPID_BUTTON8 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_button9.name(), Constants.DEF_APPID_BUTTON8);
                    if (Constants.DEF_APPID_BUTTON9 == null)
                        Constants.DEF_APPID_BUTTON9 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_button8.name(), Constants.DEF_APPID_BUTTON9);
                    if (Constants.DEF_APPID_BUTTON10 == null)
                        Constants.DEF_APPID_BUTTON10 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_button10.name(), Constants.DEF_APPID_BUTTON10);
                    if (Constants.DEF_APPID_BUTTON11 == null)
                        Constants.DEF_APPID_BUTTON11 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_button11.name(), Constants.DEF_APPID_BUTTON11);
                    if (Constants.DEF_APPID_PWR_BUTTON == null)
                        Constants.DEF_APPID_PWR_BUTTON = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_pwr_button.name(), Constants.DEF_APPID_PWR_BUTTON);
                    if (Constants.DEF_APPID_MiniBUTTON1 == null)
                        Constants.DEF_APPID_MiniBUTTON1 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_minibutton1.name(), Constants.DEF_APPID_MiniBUTTON1);
                    if (Constants.DEF_APPID_MiniBUTTON2 == null)
                        Constants.DEF_APPID_MiniBUTTON2 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_minibutton2.name(), Constants.DEF_APPID_MiniBUTTON2);
                    if (Constants.DEF_APPID_MiniBUTTON3 == null)
                        Constants.DEF_APPID_MiniBUTTON3 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_minibutton3.name(), Constants.DEF_APPID_MiniBUTTON3);
                    if (Constants.DEF_APPID_MiniBUTTON4 == null)
                        Constants.DEF_APPID_MiniBUTTON4 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_minibutton4.name(), Constants.DEF_APPID_MiniBUTTON4);
                    if (Constants.DEF_APPID_MiniBUTTON5 == null)
                        Constants.DEF_APPID_MiniBUTTON5 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_minibutton5.name(), Constants.DEF_APPID_MiniBUTTON5);
                    if (Constants.DEF_APPID_MiniBUTTON6 == null)
                        Constants.DEF_APPID_MiniBUTTON6 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_minibutton6.name(), Constants.DEF_APPID_MiniBUTTON6);
                    if (Constants.DEF_APPID_MiniBUTTON7 == null)
                        Constants.DEF_APPID_MiniBUTTON7 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_minibutton7.name(), Constants.DEF_APPID_MiniBUTTON7);
                    if (Constants.DEF_APPID_MiniBUTTON8 == null)
                        Constants.DEF_APPID_MiniBUTTON8 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_minibutton8.name(), Constants.DEF_APPID_MiniBUTTON8);
                    if (Constants.DEF_APPID_MiniBUTTON9 == null)
                        Constants.DEF_APPID_MiniBUTTON9 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_minibutton9.name(), Constants.DEF_APPID_MiniBUTTON9);
                    if (Constants.DEF_APPID_MiniBUTTON10 == null)
                        Constants.DEF_APPID_MiniBUTTON10 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_minibutton10.name(), Constants.DEF_APPID_MiniBUTTON10);
                    if (Constants.DEF_APPID_MiniBUTTON11 == null)
                        Constants.DEF_APPID_MiniBUTTON11 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_minibutton11.name(), Constants.DEF_APPID_MiniBUTTON11);
                    if (Constants.DEF_APPID_MiniBUTTON12 == null)
                        Constants.DEF_APPID_MiniBUTTON12 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_minibutton12.name(), Constants.DEF_APPID_MiniBUTTON12);
                    if (Constants.DEF_APPID_VPN_BUTTON == null)
                        Constants.DEF_APPID_VPN_BUTTON = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_vpn_button.name(), Constants.DEF_APPID_VPN_BUTTON);
                    if (Constants.DEF_APPID_DOCK == null)
                        Constants.DEF_APPID_DOCK = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_dock_btn.name(), Constants.DEF_APPID_DOCK);
                    if (Constants.DEF_APPID_HELP_BUTTON == null)
                        Constants.DEF_APPID_HELP_BUTTON = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_help_btn.name(), Constants.DEF_APPID_HELP_BUTTON);
                    if (Constants.DEF_APPID_wifi_BUTTON == null)
                        Constants.DEF_APPID_wifi_BUTTON = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_wifi_btn.name(), Constants.DEF_APPID_wifi_BUTTON);
                    if (Constants.DEF_APPID_BLUETOOTH_BUTTON == null)
                        Constants.DEF_APPID_BLUETOOTH_BUTTON = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_bluetooth_btn.name(), Constants.DEF_APPID_BLUETOOTH_BUTTON);
                    if (Constants.DEF_APPID_PARENTAL_BUTTON == null)
                        Constants.DEF_APPID_PARENTAL_BUTTON = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.UpdatableAppNameKey.appid_parental_btn.name(), Constants.DEF_APPID_PARENTAL_BUTTON);

                    Constants.APPID_BUTTON_TOP = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_BUTTON_TOP, Constants.DEF_APPID_BUTTON_TOP);
                    Constants.APPID_BUTTON1 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_BUTTON1, Constants.DEF_APPID_BUTTON1);
                    Constants.APPID_BUTTON2 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_BUTTON2, Constants.DEF_APPID_BUTTON2);
                    Constants.APPID_BUTTON3 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_BUTTON3, Constants.DEF_APPID_BUTTON3);
                    Constants.APPID_BUTTON4 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_BUTTON4, Constants.DEF_APPID_BUTTON4);
                    Constants.APPID_BUTTON5 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_BUTTON5, Constants.DEF_APPID_BUTTON5);
                    Constants.APPID_BUTTON6 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_BUTTON6, Constants.DEF_APPID_BUTTON6);
                    Constants.APPID_BUTTON7 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_BUTTON7, Constants.DEF_APPID_BUTTON7);
                    Constants.APPID_BUTTON9 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_BUTTON9, Constants.DEF_APPID_BUTTON9);
                    Constants.APPID_BUTTON8 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_BUTTON8, Constants.DEF_APPID_BUTTON8);
                    Constants.APPID_BUTTON10 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_BUTTON10, Constants.DEF_APPID_BUTTON10);
                    Constants.APPID_BUTTON11 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_BUTTON11, Constants.DEF_APPID_BUTTON11);
                    Constants.APPID_DOCK = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_DOCK, Constants.DEF_APPID_DOCK);

                    Constants.APPID_PWR_BUTTON = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_PWR_BUTTON, Constants.DEF_APPID_PWR_BUTTON);
                    Constants.APPID_MiniButton1 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_MiniBUTTON1, Constants.DEF_APPID_MiniBUTTON1);
                    Constants.APPID_MiniButton2 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_MiniBUTTON2, Constants.DEF_APPID_MiniBUTTON2);
                    Constants.APPID_MiniButton3 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_MiniBUTTON3, Constants.DEF_APPID_MiniBUTTON3);
                    Constants.APPID_MiniButton4 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_MiniBUTTON4, Constants.DEF_APPID_MiniBUTTON4);
                    Constants.APPID_MiniButton5 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_MiniBUTTON5, Constants.DEF_APPID_MiniBUTTON5);
                    Constants.APPID_MiniButton6 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_MiniBUTTON6, Constants.DEF_APPID_MiniBUTTON6);
                    Constants.APPID_MiniButton7 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_MiniBUTTON7, Constants.DEF_APPID_MiniBUTTON7);
                    Constants.APPID_MiniButton8 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_MiniBUTTON8, Constants.DEF_APPID_MiniBUTTON8);
                    Constants.APPID_MiniButton9 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_MiniBUTTON9, Constants.DEF_APPID_MiniBUTTON9);
                    Constants.APPID_MiniButton10 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_MiniBUTTON10, Constants.DEF_APPID_MiniBUTTON10);
                    Constants.APPID_MiniButton11 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_MiniBUTTON11, Constants.DEF_APPID_MiniBUTTON11);
                    Constants.APPID_MiniButton12 = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_MiniBUTTON12, Constants.DEF_APPID_MiniBUTTON12);

                    Constants.APPID_VPN_BUTTON = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_VPN_BUTTON, Constants.DEF_APPID_VPN_BUTTON);
                    Constants.APPID_WIFI_BUTTON = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_wifi_BUTTON, Constants.DEF_APPID_wifi_BUTTON);
                    Constants.APPID_BLUETOOTH_BUTTON = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_BLUETOOTH_BUTTON, Constants.DEF_APPID_BLUETOOTH_BUTTON);
                    Constants.APPID_PARENTAL_BUTTON = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_PARENTAL_BUTTON, Constants.DEF_APPID_PARENTAL_BUTTON);
                    Constants.APPID_HELP_BUTTON = SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.DEF_APPID_HELP_BUTTON, Constants.DEF_APPID_HELP_BUTTON);

                    doSetOrRefreshAllAppIcons();
//                    doFetchAndUpdateAppIdAndIcons();
                    removeLoading();
                } catch (
                        Exception e) {
                    e.printStackTrace();
//                    doFetchAndUpdateAppIdAndIcons();
                }
            }
        }).start();
    }


    @Override
    public void onUserInteraction() {
        // TODO Auto-generated method stub
        super.onUserInteraction();
        stopHandler();//stop first and then start
        startHandler();
    }

    public void stopHandler() {
        handler.removeCallbacks(r);
    }

    public void startHandler() {
        handler.postDelayed(r, time_for_refresh_time * 60 * 1000); //for 5 minutes
    }

    private void doSetOrRefreshAllAppIcons() {
        try {
            setOrRefreshAppIcons(Constants.APPID_PWR_BUTTON);
            setOrRefreshAppIcons(Constants.APPID_VPN_BUTTON);
            setOrRefreshAppIcons(Constants.APPID_DOCK);
            setOrRefreshAppIcons(Constants.DEF_APPID_HELP_BUTTON);
            setOrRefreshAppIcons(Constants.APPID_BUTTON1);
            setOrRefreshAppIcons(Constants.APPID_BUTTON2);
            setOrRefreshAppIcons(Constants.APPID_BUTTON3);
            setOrRefreshAppIcons(Constants.APPID_BUTTON5);
            setOrRefreshAppIcons(Constants.APPID_BUTTON6);
            setOrRefreshAppIcons(Constants.APPID_BUTTON7);
            setOrRefreshAppIcons(Constants.APPID_BUTTON8);
            setOrRefreshAppIcons(Constants.APPID_BUTTON9);
            setOrRefreshAppIcons(Constants.APPID_BUTTON10);
            setOrRefreshAppIcons(Constants.APPID_BUTTON11);
            setOrRefreshAppIcons(Constants.APPID_BUTTON_TOP);
            setOrRefreshAppIcons(Constants.APPID_BUTTON4);
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    private void showAppLockSettingsDialog() {
        try {
            if (mParentalControlDialog == null)
                mParentalControlDialog = new ParentalControlDialog(this);
            mParentalControlDialog.showDialog(new LongPressOptionDialog.OnItemClickListener() {
                @Override
                public void onClicked(int item) {
                    switch (item) {
                        case 0:
                            if (SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, Constants.Storage.APPS_LOCK_ENABLED.name(), false)) {
                                // do deactivate
                                //first validate password then deactivate
                                HelperUtils.onActionAppOpening(HomePageActivity.this, new HelperUtils.OnAppOpeningPasswordValidateListener() {
                                    @Override
                                    public void OnAppLockStatus(boolean allowOpening) {
                                        if (allowOpening) {
                                            HelperUtils.showAppLockActivateDeActivateDialog(HomePageActivity.this, false, new HelperUtils.OnAppLockListener() {
                                                @Override
                                                public void getAppLockStatus(boolean isAppLockEnable) {
                                                    updateAppLockIcon();
                                                }
                                            });
                                        } else {
                                            HelperUtils.showMessageInfo(HomePageActivity.this, "Wrong password entered. Please try again!");
                                        }
                                    }
                                });
                            } else {
                                //do activate
                                //first create password again and then activate
                                HelperUtils.doShowPasswordCreatingDialog(HomePageActivity.this, false, new HelperUtils.OnPasswordCreateUpdateListener() {
                                    @Override
                                    public void onPasswordCreateOrUpdated() {
                                        HelperUtils.showAppLockActivateDeActivateDialog(HomePageActivity.this, true, new HelperUtils.OnAppLockListener() {
                                            @Override
                                            public void getAppLockStatus(boolean isAppLockEnable) {
                                                updateAppLockIcon();
                                            }
                                        });
                                    }
                                });
                            }
                            break;
                        case 1:
                            if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, Constants.Storage.APP_LOCK_PASSWORD.name(), Constants.APP_LOCK_BLANK_PASSWORD).equalsIgnoreCase(Constants.APP_LOCK_BLANK_PASSWORD)) {
                                // do update password
                                HelperUtils.doShowPasswordCreatingDialog(HomePageActivity.this, false, null);
                            } else {
                                //do create password
                                HelperUtils.doShowPasswordCreatingDialog(HomePageActivity.this, true, null);
                            }
                            break;
                        case 2:
                            doHideUnHideAllApps(false);
                            break;
                        case 3:
                            doHideUnHideAllApps(true);
                            break;
                    }
                }
            });
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAppLockIcon() {
        final boolean isLockActive = SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, Constants.Storage.APPS_LOCK_ENABLED.name(), false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (ivAppLockBtn != null) {
                        ivAppLockBtn.setBackground(null);

                        if (isLockActive) {
                            ivAppLockBtn.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_minibutton5.name())));
//                            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_pressed_minibutton5", "")).into(ivAppLockBtn);
                        } else {
                            ivAppLockBtn.setImageDrawable(new BitmapDrawable(getResources(), ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton5.name())));
//                            Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_minibutton5", "")).into(ivAppLockBtn);
                        }



//                        ivAppLockBtn.setImageDrawable(getResources().getDrawable(isLockActive ? ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_pressed_minibutton5.name()) : ImageManager.getImgFromFiles(HomePageActivity.this, Constants.UpdatableAppNameKey.icon_minibutton5.name())));
                    }
//                    if (news_btn != null) {
//                        news_btn.setBackground(null);
//                        news_btn.setBackground(getResources().getDrawable(isLockActive ? R.drawable.news_button_selector : R.drawable.news_btn_off_selector));
//                    }
                } catch (
                        Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void doLockUnlockSelectedApp(final String lockingAppsStorageKey,
                                         final boolean isAppLocked) {
        try {
            changeAppDialog = new AlertDialog.Builder(this);
            changeAppDialog.setTitle(isAppLocked ? "Do you want to unlock this app?" : "Do you want to lock this app?")
                    .setMessage(isAppLocked ? "No longer password will be required to open this app." : "Every time you need to enter the app lock password to open this app.")
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferenceStorage.setBooleanValue(HomePageActivity.this, lockingAppsStorageKey, !isAppLocked);
                            dialog.cancel();
                            HelperUtils.showMessageInfo(HomePageActivity.this, isAppLocked ? "App has been unlocked Successfully!" : "App has been locked Successfully!");
                            changeAppDialog = null;
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            changeAppDialog = null;
                        }
                    });

            AlertDialog alertDialog = changeAppDialog.create();
            if (!isFinishing())
                alertDialog.show();

            Button negativeBtn = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            if (negativeBtn != null) {
                negativeBtn.setBackgroundResource(R.drawable.border_for_focus);
            }

            Button positiveBtn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            if (positiveBtn != null) {
                positiveBtn.setBackgroundResource(R.drawable.border_for_focus);
            }

        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onRestart() {
        isRefresh = false;
        getHomePageMainApps();
        doFetchAndUpdateAppIdAndIcons();
        super.onRestart();
    }


    @Override
    public void handleKeys(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                is_show = false;
                if (!isNetworkConnected()) {

                    Log.i(TAG, "init: back press yes");
                    ivButtonTop.setVisibility(View.INVISIBLE);
                    no_internet.setVisibility(View.VISIBLE);
                    no_internet.bringToFront();
                    no_internet.setBackgroundResource(R.drawable.internet_offline);
//                    setNetworkIcon();
                } else {
                    setMiniAndAdIcons();
//                    setNetworkIcon();
                    no_internet.setVisibility(View.GONE);
                    ivButtonTop.setVisibility(View.VISIBLE);
                    Log.i(TAG, "init: back press no");
                    if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left_top", "").equals("")) {

                    } else {

                        Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left_top", "")).into(btn_left_top);
                    }
                    if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left_top_right", "").equals("")) {

                    } else {

                        Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left_top_right", "")).into(btn_left_top_right);
                    }
                    if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left1", "").equals("")) {

                    } else {

                        Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left1", "")).into(btn_left1);
                    }
                    if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left2", "").equals("")) {

                    } else {

                        Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left2", "")).into(btn_left2);
                    }
                    if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left3", "").equals("")) {

                    } else {

                        Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_left3", "")).into(btn_left3);
                    }
                    if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_right1", "").equals("")) {

                    } else {

                        Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_right1", "")).into(btn_right1);
                    }
                    if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_right2", "").equals("")) {

                    } else {

                        Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_right2", "")).into(btn_right2);
                    }
                    if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_right3", "").equals("")) {

                    } else {

                        Picasso.get().load(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "icon_button_right3", "")).into(btn_right3);
                    }
                }
                pd.setMessage("Updating Main Menu…");
                pd.setCancelable(false);
                pd.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pd.dismiss();//dismiss dialog
                        is_show = true;
                    }
                });
                if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "refreshtimedisplay", "").equals("")) {
                    time = 5;
                } else {
                    time = Integer.parseInt(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "refreshtimedisplay", "5"));
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (is_show) {

                        } else {
                            pd.show();
                        }

                    }
                }, time * 1000);

                pd.show();

                isRefresh = false;
                getHomePageMainApps();
                doFetchAndUpdateAppIdAndIcons();
                break;
        }
    }

    private boolean isNetworkConnected() {
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void refreshapps() {
        pd.setMessage("Updating Main Menu…");
        pd.setCancelable(false);
        pd.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd.dismiss();//dismiss dialog
                is_show = true;
            }
        });
        if (SharedPreferenceStorage.getStringValue(HomePageActivity.this, "refreshtimedisplay", "").equals("")) {
            time = 5;
        } else {
            time = Integer.parseInt(SharedPreferenceStorage.getStringValue(HomePageActivity.this, "refreshtimedisplay", "5"));
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (is_show) {

                } else {
                    pd.show();
                }

            }
        }, time * 1000);
        isRefresh = false;
        getHomePageMainApps();
        doFetchAndUpdateAppIdAndIcons();
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission(View currentUpdatingAppView) {
        this.__updatingAppViewDuringPermissionRequest = currentUpdatingAppView;
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                    if (__updatingAppViewDuringPermissionRequest == null) {
                        checkAndUpdateIfInstantAppUpdateRequire();
                    } else {
                        doDownloadAndInstallToNewVersion(__updatingAppViewDuringPermissionRequest, 0, 0);
                    }
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RECOGNIZER_APP_UPDATED) {
            //Toast.makeText(this, "App has been updated successfully!", Toast.LENGTH_SHORT).show();
        } else if (requestCode == Constants.RECOGNIZER_APP_DELETE) {
            onActionAppDelete();
        }
    }

    private void onActionAppDelete() {
        try {
            if (deletedAppName != null) {
                SharedPreferenceStorage.setBooleanValue(this, deletedAppName + Constants.Storage._isDeleted.name(), true);
                switch (deletedAppName) {
                    case "0":
                        setOrRefreshAppIcons(Constants.APPID_BUTTON_TOP);
                        ivButtonTop.setClickable(false);
                        break;
                    case "1":
                        //ivButton1.setFocusable(false);
                        setOrRefreshAppIcons(Constants.APPID_BUTTON1);
                        ivButton1.setClickable(false);
                        break;
                    case "23":
                        //ivButton1.setFocusable(false);
                        setOrRefreshAppIcons(Constants.APPID_BUTTON_TOP_NEW);
                        ivButtonTopNew.setClickable(false);
                        break;
                    case "2":
                        //ivButton2.setFocusable(false);
                        setOrRefreshAppIcons(Constants.APPID_BUTTON2);
                        ivButton2.setClickable(false);
                        break;
                    case "3":
                        //ivButton3.setFocusable(false);
                        setOrRefreshAppIcons(Constants.APPID_BUTTON3);
                        ivButton3.setClickable(false);
                        break;
                    case "4":
                        //ivButton4.setFocusable(false);
                        setOrRefreshAppIcons(Constants.APPID_BUTTON4);
                        ivButton4.setClickable(false);
                        break;
                    case "5":
                        //ivButton5.setFocusable(false);
                        setOrRefreshAppIcons(Constants.APPID_BUTTON5);
                        ivButton5.setClickable(false);
                        break;
                    case "6":
                        //ivButton6.setFocusable(false);
                        ivButton6.setClickable(false);
                        break;
                    case "7":
                        //ivButton7.setFocusable(false);
                        ivButton7.setClickable(false);
                        break;
                    case "8":
                        //ivButton8.setFocusable(false);
                        ivButton8.setClickable(false);
                        break;
                    case "9":
                        //ivButton9.setFocusable(false);
                        ivButton9.setClickable(false);
                        break;
                    case "10":
                        //ivButton10.setFocusable(false);
                        ivButton10.setClickable(false);
                        break;
                    case "11":
                        //ivButton11.setFocusable(false);
                        ivButton11.setClickable(false);
                        break;
                }
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    private void onActionHideOrUnHideApp(final View view) {
        try {
            System.out.println("view = " + view);
            System.out.println("deletedAppName = " + deletedAppName);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (deletedAppName != null) {
                            SharedPreferenceStorage.setBooleanValue(HomePageActivity.this, deletedAppName + Constants.Storage._isAppHidden.name()
                                    , !SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, deletedAppName + Constants.Storage._isAppHidden.name(), false));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        onFocusChange(view, false);
                                        boolean isHidden = SharedPreferenceStorage.getBooleanValue(HomePageActivity.this, deletedAppName + Constants.Storage._isAppHidden.name(), false);
                                        view.setFocusable(!isHidden);
                                        if (view.getId() == R.id.ivBottomAddRemoveBtn)
                                            rvDockItemRecyclerVew.setVisibility(isHidden ? View.INVISIBLE : View.VISIBLE);
                                    } catch (
                                            Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    private void doHideUnHideAllApps(final boolean isShowBtn) {
        try {
            if (SharedPreferenceStorage.getBooleanValue(this, Constants.Storage.APPS_LOCK_ENABLED.name(), false)) {
                HelperUtils.onActionAppOpening(this, new HelperUtils.OnAppOpeningPasswordValidateListener() {
                    @Override
                    public void OnAppLockStatus(boolean allowOpening) {
                        if (allowOpening) {
                            doHideUnHideAllOperation(isShowBtn);
                        } else {
                            HelperUtils.showMessageInfo(HomePageActivity.this, "Wrong password entered. Please try again!");
                        }
                    }
                });
            } else {
                doHideUnHideAllOperation(isShowBtn);
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    private void doHideUnHideAllOperation(final boolean isShowBtnAll) {
        try {
            if (isShowBtnAll) {
                for (int i = 1; i < 13; i++) {
                    boolean isBtnNotHiddenByServer = true;
                    for (int x = 0; x < app.getHiddenAppsIdListByServer().length(); x++) {
                        String[] dataSet = app.getHiddenAppsIdListByServer().getString(x).split(",");
                        String btnId = dataSet[0];
                        boolean isShowBtn = dataSet[1].equalsIgnoreCase("1");
                        if (btnId.equalsIgnoreCase(i + "") && !isShowBtn) {
                            isBtnNotHiddenByServer = false;
                            break;
                        }
                    }
                    if (isBtnNotHiddenByServer) {
                        SharedPreferenceStorage.setBooleanValue(this, i + Constants.Storage._isAppHidden.name(), false);
                        doHideShowAllButton(i + "", true);
                    }
                }
            } else {
                for (int i = 1; i < 13; i++) {
                    SharedPreferenceStorage.setBooleanValue(this, i + Constants.Storage._isAppHidden.name(), true);
                    doHideShowAllButton(i + "", false);
                }
            }

            HelperUtils.showMessageInfo(HomePageActivity.this, isShowBtnAll ? "All Apps Are shown!" : "All Apps Are Hidden!");
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    private void doHideShowAllButton(final String btnId, final boolean isShowBtn) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    switch (btnId) {
                        case "0":
                            ivButtonTop.setFocusable(isShowBtn);
                            ivButtonTop.setClickable(isShowBtn);
                            ivButtonTop.setLongClickable(isShowBtn);
                            onFocusChange(ivButtonTop, false);
                            break;
                        case "1":
                            ivButton1.setFocusable(isShowBtn);
                            ivButton1.setClickable(isShowBtn);
                            ivButton1.setLongClickable(isShowBtn);
                            onFocusChange(ivButton1, false);
                            break;
                        case "23":
                            ivButtonTopNew.setFocusable(isShowBtn);
                            ivButtonTopNew.setClickable(isShowBtn);
                            ivButtonTopNew.setLongClickable(isShowBtn);
                            onFocusChange(ivButtonTopNew, false);
                            break;
                        case "2":
                            ivButton2.setFocusable(isShowBtn);
                            ivButton2.setClickable(isShowBtn);
                            ivButton2.setLongClickable(isShowBtn);
                            onFocusChange(ivButton2, false);
                            break;
                        case "3":
                            ivButton3.setFocusable(isShowBtn);
                            ivButton3.setClickable(isShowBtn);
                            ivButton3.setLongClickable(isShowBtn);
                            onFocusChange(ivButton3, false);
                            break;
                        case "4":
                            ivButton4.setFocusable(isShowBtn);
                            ivButton4.setClickable(isShowBtn);
                            ivButton4.setLongClickable(isShowBtn);
                            onFocusChange(ivButton4, false);
                            break;
                        case "5":
                            ivButton5.setFocusable(isShowBtn);
                            ivButton5.setClickable(isShowBtn);
                            ivButton5.setLongClickable(isShowBtn);
                            onFocusChange(ivButton5, false);
                            break;
                        case "6":
                            ivButton6.setFocusable(isShowBtn);
                            ivButton6.setClickable(isShowBtn);
                            ivButton6.setLongClickable(isShowBtn);
                            onFocusChange(ivButton6, false);
                            break;
                        case "7":
                            ivButton7.setFocusable(isShowBtn);
                            ivButton7.setClickable(isShowBtn);
                            ivButton7.setLongClickable(isShowBtn);
                            onFocusChange(ivButton7, false);
                            break;
                        case "8":
                            ivButton8.setFocusable(isShowBtn);
                            ivButton8.setClickable(isShowBtn);
                            ivButton8.setLongClickable(isShowBtn);
                            onFocusChange(ivButton8, false);
                            break;
                        case "9":
                            ivButton9.setFocusable(isShowBtn);
                            ivButton9.setClickable(isShowBtn);
                            ivButton9.setLongClickable(isShowBtn);
                            onFocusChange(ivButton9, false);
                            break;
                        case "10":
                            ivButton10.setFocusable(isShowBtn);
                            ivButton10.setClickable(isShowBtn);
                            ivButton10.setLongClickable(isShowBtn);
                            onFocusChange(ivButton10, false);
                            break;
                        case "11":
                            ivButton11.setFocusable(isShowBtn);
                            ivButton11.setClickable(isShowBtn);
                            ivButton11.setLongClickable(isShowBtn);
                            onFocusChange(ivButton11, false);
                            break;
                        case "12":
                            ivAddRemoveBtn.setFocusable(isShowBtn);
                            ivAddRemoveBtn.setClickable(isShowBtn);
                            ivAddRemoveBtn.setLongClickable(isShowBtn);
                            onFocusChange(ivAddRemoveBtn, false);
                            break;
                        case "13":
                            ivPoweBtn.setFocusable(isShowBtn);
                            ivPoweBtn.setClickable(isShowBtn);
                            ivPoweBtn.setLongClickable(isShowBtn);
                            onFocusChange(ivPoweBtn, false);
                            break;
                        case "14":
                            ivVpnBtn.setFocusable(isShowBtn);
                            ivVpnBtn.setClickable(isShowBtn);
                            ivVpnBtn.setLongClickable(isShowBtn);
                            onFocusChange(ivVpnBtn, false);
                            break;
                    }
                } catch (
                        Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void doLoadHtmlRssFeed() {
        try {
            if (app.getHtmlRssFeedInfo() != null && app.getHtmlRssFeedInfo().getShort_rss_link() != null) {
                DownloadImage.downloadRssFeed(app.getHtmlRssFeedInfo().getShort_rss_link(), new DownloadImage.OnRssFeedLoad() {
                    @Override
                    public void OnComplete(final String rssMessage) {
                        if (rssMessage != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        mWebViewHtmlRssFeedShort.loadData(rssMessage, "text/html; charset=utf-8", "UTF-8");
                                    } catch (
                                            Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    private void doShowCustomDynamicAds(String id) {
        try {
            if (HelperUtils.haveNetworkConnection(this) > 0 && app.getAdsButtonInfoInfoHashMap() != null && app.getAdsButtonInfoInfoHashMap().size() > 0) {
                HelpInfoOrAdsButtonInfo mAdsButtonInfo = app.getAdsButtonInfoInfoHashMap().get(id);
                if (mAdsButtonInfo != null) {
                    switch (mAdsButtonInfo.getTutorialType()) {
                        case png:
                            new DownloadNewUpdate(this, mAdsButtonInfo.getBtnId().concat(Constants.UpdatableAppNameKey._tutorial.name()), 0, 0, Constants.FILE_TYPE.png, null).openImage(mAdsButtonInfo.getHelpFileUrl());
                            break;
                        case html:
                            openFullWebView(mAdsButtonInfo.getHelpFileUrl());
                            break;
                        case mov:
                        case mp4:
                            HelperUtils.doPlayVideo(this, mAdsButtonInfo);
                            break;
                        case hyperlink:
                            try {
                                Uri webpage = Uri.parse(mAdsButtonInfo.getHelpFileUrl());
                                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(intent);
                                }
                            } catch (
                                    Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case apk:
                            try {
                                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(mAdsButtonInfo.getHelpFileUrl());
                                startActivity(launchIntent);
                            } catch (
                                    Exception e) {
                                Toast.makeText(this, "App is not Installed", Toast.LENGTH_SHORT).show();
                            }

                            break;
                        default:
                            new DownloadNewUpdate(this, mAdsButtonInfo.getBtnId().concat(Constants.UpdatableAppNameKey._tutorial.name()), 0, 0, mAdsButtonInfo.getTutorialType(), null).execute(mAdsButtonInfo.getHelpFileUrl());
                            break;
                    }
                }
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    private void doShowCustomAppInfoDialog(String type, String url) {
        try {


            if (type != null) {
                switch (type) {
                    case "png":
                        Log.i(TAG, "doShowCustomAppInfoDialog: " + url);
                        new DownloadNewUpdate(this, "zluancher", 0, 0, Constants.FILE_TYPE.png, null).openImage(url);
                        break;
                    case "html":
                        openFullWebView(url);
                        break;
                    case "mov":
                    case "mp4":
                        Uri uri = Uri.parse(url); //your file URI
                        Intent vlcIntent = new Intent(Intent.ACTION_VIEW);
                        vlcIntent.setPackage("org.videolan.vlc");
                        vlcIntent.setDataAndTypeAndNormalize(uri, "video/*");
                        vlcIntent.putExtra("title", "AppInfo Url");
                        vlcIntent.putExtra("from_start", true);
                        startActivity(vlcIntent);
                        break;
                    case "hyperlink":
                        try {
                            Uri webpage = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivity(intent);
                            }
                        } catch (
                                Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    default:

                        break;
                }

            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    public void openFullWebView(final String url) {
        System.out.println("----url = " + url);
        try {
            final AlertDialog[] alertDialog = {null};
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.image_viewer, null);
            dialogBuilder.setView(dialogView);
            final ImageView imageViewer = dialogView.findViewById(R.id.ivImageViewer);
            imageViewer.setVisibility(View.GONE);
            final WebView webviewRssFull = dialogView.findViewById(R.id.webviewRssFull);
            webviewRssFull.setVisibility(View.VISIBLE);
            webviewRssFull.setBackgroundColor(Color.TRANSPARENT);
            final TextView tvLoading = dialogView.findViewById(R.id.tvLoading);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
//                        webviewRssFull.loadUrl(url);
                        DownloadImage.downloadRssFeed(url, new DownloadImage.OnRssFeedLoad() {
                            @Override
                            public void OnComplete(final String rssMessage) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        webviewRssFull.loadData(rssMessage, "text/html; charset=utf-8", "UTF-8");
                                        tvLoading.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                        });
                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            alertDialog[0] = dialogBuilder.create();
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(alertDialog[0].getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            alertDialog[0].show();
            alertDialog[0].getWindow().setAttributes(lp);
            alertDialog[0].setCancelable(true);
            alertDialog[0].show();
            webviewRssFull.requestFocus();
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    private void checkAndUpdateIfInstantAppUpdateRequire() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    deleteAppByStartup(app.getDeleteAppsIdListByServerDuringAutoUpdate());
                    if (app.getNewInstantAppUpdateLinkList() != null && app.getNewInstantAppUpdateLinkList().size() > 0)
                        new DownloadNewUpdate(HomePageActivity.this, 0).execute(app.getNewInstantAppUpdateLinkList().get(0));
                } catch (
                        Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void doShowUpdateSettingsFileDialog() {
        try {
            changeAppDialog = new AlertDialog.Builder(this);
            changeAppDialog.setMessage("Do you want to change settings configuration?")
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Animation anim = AnimationUtils.loadAnimation(HomePageActivity.this, R.anim.zoom_in_edit);
                            dialog.cancel();
                            changeAppDialog = null;

                            HelperUtils.showSettingsFileLinkAddingDialog(HomePageActivity.this, new HelperUtils.OnSettingsLikAddListener() {
                                @Override
                                public void onLinkAddStatus(boolean isLoaded) {
                                    if (isLoaded) {
                                        SharedPreferenceStorage.setIntegerValue(HomePageActivity.this, Constants.Storage.LAST_APP_INSTALL_UPDATE_VERSION.name(), 0);
                                        SharedPreferenceStorage.setIntegerValue(HomePageActivity.this, Constants.Storage.LAST_UPDATE_VERSION.name(), 0);
                                        doFetchAndUpdateAppIdAndIcons();
                                    } else {
                                        HelperUtils.showMessageInfo(HomePageActivity.this, "Invalid url entered!");
                                    }
                                }
                            });
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            changeAppDialog = null;
                        }
                    });

            AlertDialog alertDialog = changeAppDialog.create();
            if (!isFinishing())
                alertDialog.show();

            Button negativeBtn = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            if (negativeBtn != null) {
                negativeBtn.setBackgroundResource(R.drawable.border_for_focus);
            }

            Button positiveBtn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            if (positiveBtn != null) {
                positiveBtn.setBackgroundResource(R.drawable.border_for_focus);
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    private void launchBluetoothSettings() {
        try {
            final Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            final ComponentName cn = new ComponentName("com.android.tv.settings", "com.android.settings.bluetooth.BluetoothSettings");
            intent.setComponent(cn);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (
                Exception e) {
            Intent intentOpenBluetoothSettings = new Intent();
            intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
            startActivity(intentOpenBluetoothSettings);
        }
    }

    private void launchDateSettings() {
        try {
            final Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            final ComponentName cn = new ComponentName("com.android.tv.settings", "android.settings.DATE_SETTINGS");
            intent.setComponent(cn);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (
                Exception e) {
            Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
            startActivity(intent);
        }
    }

}
