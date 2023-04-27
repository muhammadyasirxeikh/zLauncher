package com.zvision.zlaunchertwo;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.zvision.zlaunchertwo.base.BaseActivity;
import com.zvision.zlaunchertwo.base.Constants;
import com.zvision.zlaunchertwo.base.HelperUtils;
import com.zvision.zlaunchertwo.part.AllAppsListAdapter;
import com.zvision.zlaunchertwo.shared_storage.SharedPreferenceStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AllAppsActivity extends BaseActivity implements View.OnClickListener {

    public ArrayList<Item> appArrayList = new ArrayList<>();
    private RecyclerView recyclerViewApp = null;
    private Button btnSaveAddRemoveApp = null;
    public boolean __isAddRemoveAppPage = false;
    public String __customAppPackageName = null, __currentDefaultAppPkg = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_apps);
        __isAddRemoveAppPage = getIntent().getBooleanExtra(Constants.IntentExtraKey.ISADDREMOVEAPP.name(), false);
        __customAppPackageName = getIntent().getStringExtra(Constants.IntentExtraKey.CURRENT_CUSTOM_APP.name());
        __currentDefaultAppPkg = getIntent().getStringExtra(Constants.IntentExtraKey.CURRENT_DEFAULT_APP.name());

        recyclerViewApp = findViewById(R.id.rv_dock_item_recyclerview);
        btnSaveAddRemoveApp = findViewById(R.id.btnAddAppsSave);
        btnSaveAddRemoveApp.setOnClickListener(this);
        if (__isAddRemoveAppPage) {
            if (__customAppPackageName == null) {
                btnSaveAddRemoveApp.setText("SAVE");
            }else{
                btnSaveAddRemoveApp.setText("RESTORE DEFAULTS");
            }
            btnSaveAddRemoveApp.getLayoutParams().width = HelperUtils.getHorizontalRatio(50);
            btnSaveAddRemoveApp.getLayoutParams().height = HelperUtils.getVerticalRatio(50);
            btnSaveAddRemoveApp.setTextSize(HelperUtils.getTextRatio(2));
            btnSaveAddRemoveApp.setVisibility(View.VISIBLE);
            btnSaveAddRemoveApp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        btnSaveAddRemoveApp.setTextColor(Color.WHITE);
                        Animation anim = AnimationUtils.loadAnimation(AllAppsActivity.this, R.anim.scale_in);
                        btnSaveAddRemoveApp.startAnimation(anim);
                        anim.setFillAfter(true);
                    } else {
                        btnSaveAddRemoveApp.setTextColor(Color.RED);
                        Animation anim = AnimationUtils.loadAnimation(AllAppsActivity.this, R.anim.scale_out);
                        btnSaveAddRemoveApp.startAnimation(anim);
                        anim.setFillAfter(true);
                    }
                }
            });
        } else {
            btnSaveAddRemoveApp.setVisibility(View.GONE);
        }
    }

    public void init() {
        getAllInstalledPackges();
        if (appArrayList != null) {
            AllAppsListAdapter adapter = new AllAppsListAdapter(this);
            recyclerViewApp.setLayoutManager(new GridLayoutManager(this, Constants.APP_APPS_COLUMN_SPAN));
            recyclerViewApp.setAdapter(adapter);
        }
    }


    private void getAllInstalledPackges() {
        try {
            appArrayList.clear();
            PackageManager mPackageManager = getPackageManager();
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> avaiableActivities = mPackageManager.queryIntentActivities(intent, 0);
            for (ResolveInfo appIs : avaiableActivities) {
                Drawable banner =null;
                banner = appIs.activityInfo.loadIcon(mPackageManager);
                if (banner == null) {
                    banner = appIs.activityInfo.applicationInfo.loadIcon(mPackageManager);
                }
                if (banner == null)
                banner = appIs.activityInfo.loadBanner(mPackageManager);
                if (banner == null) {
                    banner = appIs.activityInfo.applicationInfo.loadBanner(mPackageManager);
                }

                CharSequence appNameIs = appIs.loadLabel(mPackageManager);
                appArrayList.add(new Item(appIs.activityInfo.packageName, appNameIs, banner));
            }
            Collections.sort(appArrayList, new AppNameComparator());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleKeys(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (__isAddRemoveAppPage && __customAppPackageName == null)
                saveAppSelection();
            else
                finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddAppsSave:
                saveAppSelection();
                break;
        }
    }

    private void saveAppSelection() {
        try {
            if (__customAppPackageName == null) {
                StringBuilder packgeNameSb = new StringBuilder();
                for (int i = 0; i < app.getHomePageDockAppList().size(); i++) {
                    packgeNameSb.append(app.getHomePageDockAppList().get(i));
                    packgeNameSb.append(Constants.STRING_SPLITTER);
                }
                //System.out.println("packgeNameSb = " + packgeNameSb.toString());
                SharedPreferenceStorage.setStringValue(this, Constants.Storage.HOMEPAGE_CUSTOM_APP.name(), packgeNameSb.toString());
                finish();
            } else {
                showChangeToDefaultAppDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private AlertDialog.Builder restoreAppDialog = null;

    private void showChangeToDefaultAppDialog() {
        try {
            restoreAppDialog = new AlertDialog.Builder(this);
            restoreAppDialog.setMessage("Restore to Default App?")
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            restoreDefaultApp();
                            Toast.makeText(AllAppsActivity.this, "DEFAULT APP HAS BEEN RESTORED!", Toast.LENGTH_LONG).show();
                            dialog.cancel();
                            restoreAppDialog = null;
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            restoreAppDialog = null;
                        }
                    });

            AlertDialog alertDialog = restoreAppDialog.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void restoreDefaultApp() {
        try {
            if (__currentDefaultAppPkg != null) {
                if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_BUTTON4)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_BUTTON4);
                } else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_BUTTON3)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_BUTTON3);
                } else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_BUTTON1)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_BUTTON1);
                } else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_BUTTON2)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_BUTTON2);
                } else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_BUTTON5)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_BUTTON5);
                } else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_BUTTON6)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_BUTTON6);
                } else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_BUTTON7)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_BUTTON7);
                } else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_BUTTON9)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_BUTTON9);
                } else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_BUTTON8)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_BUTTON8);
                } else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_BUTTON10)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_BUTTON10);
                } else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_BUTTON11)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_BUTTON11);
                } else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_DOCK)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_DOCK);
                } else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_PWR_BUTTON)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_PWR_BUTTON);
                } else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_MiniBUTTON1)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_MiniBUTTON1);
                }else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_MiniBUTTON2)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_MiniBUTTON2);
                }else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_MiniBUTTON3)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_MiniBUTTON3);
                }else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_MiniBUTTON4)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_MiniBUTTON4);
                }else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_MiniBUTTON5)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_MiniBUTTON5);
                }else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_MiniBUTTON6)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_MiniBUTTON6);
                }else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_MiniBUTTON7)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_MiniBUTTON7);
                }else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_MiniBUTTON8)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_MiniBUTTON8);
                }else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_MiniBUTTON9)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_MiniBUTTON9);
                }else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_MiniBUTTON10)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_MiniBUTTON10);
                }else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_MiniBUTTON11)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_MiniBUTTON11);
                }else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_MiniBUTTON12)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_MiniBUTTON12);
                } else if (__currentDefaultAppPkg.equalsIgnoreCase(Constants.DEF_APPID_BUTTON_TOP)) {
                    SharedPreferenceStorage.setStringValue(AllAppsActivity.this, __currentDefaultAppPkg, Constants.DEF_APPID_BUTTON_TOP);
                }
            }
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "ERROR OCCURED!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private class AppNameComparator implements Comparator<Item> {

        @Override
        public int compare(Item item1, Item item2) {
            return item1.__appName.toString().compareToIgnoreCase(item2.__appName.toString());
        }
    }
}
