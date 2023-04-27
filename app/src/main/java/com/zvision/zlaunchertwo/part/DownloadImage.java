package com.zvision.zlaunchertwo.part;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.zvision.zlaunchertwo.base.BaseActivity;
import com.zvision.zlaunchertwo.base.Constants;
import com.zvision.zlaunchertwo.shared_storage.SharedPreferenceStorage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class DownloadImage {

    public interface OnSaveComplete {
        void LoadImage(boolean isSuccess);
    }

    public static void doDownloadImages(final BaseActivity mBase, final String mImageLoadUrl, final String mImageName, final OnSaveComplete mOnSaveCompleteListener) {
        Bitmap mBitmap = null;
        try {
            mBitmap = BitmapFactory.decodeStream((InputStream) new URL(mImageLoadUrl).getContent());
            ImageManager.saveImageBitmapToFiles(mBase, mBitmap, mImageName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mOnSaveCompleteListener != null) {
                mOnSaveCompleteListener.LoadImage(mBitmap != null);
            }
        }
    }

    public interface OnRssFeedLoad {
        void OnComplete(String rssMessage);
    }

    public static void downloadRssFeed(String url, OnRssFeedLoad mOnRssFeedLoadListener) {
        try {
            URLConnection conn = new URL(url).openConnection();

            InputStream in = conn.getInputStream();
            String contents = convertStreamToString(in);
            if (mOnRssFeedLoadListener != null)
                mOnRssFeedLoadListener.OnComplete(contents);
            mOnRssFeedLoadListener = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnJSONLoadListener {
        void OnComplete(boolean isNeedToDownloadIcon, boolean isInstantAppUpdateRequire);
    }

    public static void doFetchUpdateDetails(final boolean isPasswordFetch, final BaseActivity mBase, final OnJSONLoadListener mOnJSONLoadListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(convertStreamToString(new URL(isPasswordFetch ? Constants.VERSION_PASSWORD_URL : SharedPreferenceStorage.getStringValue(mBase, Constants.Storage.UPDATE_DETAILS_SETTINGS_URL.name(), null)).openConnection().getInputStream()));
                     //jsonObject = getTestData(mBase);
                    System.out.println("!--!@jsonObject = " + jsonObject);

                    boolean isDownloadRequired = false, isInstantAppUpdateRequire = false;
                    if (jsonObject != null && jsonObject.length() > 0) {
                        Constants.IS_FIRST_LAUNCH_SETTINGS_FETCH_DONE = true;
                        if (isPasswordFetch) {
                            JSONObject jPassword = jsonObject.optJSONObject(Constants.UpdatableAppNameKey.version_password.name());
                            if (jPassword != null) {
                                Constants.VERSION.Box1.setVerVal(jPassword.optString(Constants.UpdatableAppNameKey.Box1.name(), Constants.VERSION.Box1.getVerVal()));
                                Constants.VERSION.Box2.setVerVal(jPassword.optString(Constants.UpdatableAppNameKey.Box2.name(), Constants.VERSION.Box2.getVerVal()));
                                Constants.VERSION.Box3.setVerVal(jPassword.optString(Constants.UpdatableAppNameKey.Box3.name(), Constants.VERSION.Box3.getVerVal()));
                                Constants.VERSION.Box4.setVerVal(jPassword.optString(Constants.UpdatableAppNameKey.Box4.name(), Constants.VERSION.Box4.getVerVal()));
                                Constants.VERSION.Box5.setVerVal(jPassword.optString(Constants.UpdatableAppNameKey.Box5.name(), Constants.VERSION.Box5.getVerVal()));
                                Constants.VERSION.Box6.setVerVal(jPassword.optString(Constants.UpdatableAppNameKey.Box6.name(), Constants.VERSION.Box6.getVerVal()));
                            }
                            JSONObject jSettingsUrl = jsonObject.optJSONObject(Constants.UpdatableAppNameKey.settings_url.name());
                            if (jSettingsUrl != null && jSettingsUrl.length() > 0) {
                                Constants.SETTINGS_URL_BOX1 = jSettingsUrl.optString(Constants.UpdatableAppNameKey.Box1.name());
                                Constants.SETTINGS_URL_BOX2 = jSettingsUrl.optString(Constants.UpdatableAppNameKey.Box2.name());
                                Constants.SETTINGS_URL_BOX3 = jSettingsUrl.optString(Constants.UpdatableAppNameKey.Box3.name());
                                Constants.SETTINGS_URL_BOX4 = jSettingsUrl.optString(Constants.UpdatableAppNameKey.Box4.name());
                                Constants.SETTINGS_URL_BOX5 = jSettingsUrl.optString(Constants.UpdatableAppNameKey.Box5.name());
                                Constants.SETTINGS_URL_BOX6 = jSettingsUrl.optString(Constants.UpdatableAppNameKey.Box6.name());
                            }
                        } else {
                            Log.i("TAG", "run: jsonobject "+jsonObject.toString());
                            Constants.APPID_STARTUP = jsonObject.optString(Constants.UpdatableAppNameKey.startupalaunchapp.name());
                            Constants.UPDATE_ZLAUNCHER_URL = jsonObject.optString(Constants.UpdatableAppNameKey.zlauncher.name());
                            Constants.URL_UPDATE_BUTTON0 = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButton0.name());
                            Constants.URL_UPDATE_BUTTON_TOP_NEW = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButtonTOP_NEW.name());
                            Constants.URL_UPDATE_BUTTON1 = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButton1.name());
                            Constants.URL_UPDATE_BUTTON3 = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButton3.name());
                            Constants.URL_UPDATE_BUTTON4 = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButton4.name());
                            Constants.URL_UPDATE_BUTTON5 = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButton5.name());
                            Constants.URL_UPDATE_BUTTON2 = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButton2.name());
                            Constants.URL_UPDATE_BUTTON10 = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButton10.name());
                            Constants.URL_UPDATE_BUTTON6 = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButton6.name());
                            Constants.URL_UPDATE_BUTTON7 = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButton7.name());
                            Constants.URL_UPDATE_BUTTON8 = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButton8.name());
                            Constants.URL_UPDATE_BUTTON9 = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButton9.name());
                            Constants.URL_UPDATE_BUTTON11 = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButton11.name());


                            Constants.URL_UPDATE_MiniButton1 = jsonObject.optString(Constants.UpdatableAppNameKey.update_minibutton_1.name());
                            Constants.URL_UPDATE_MiniButton2 = jsonObject.optString(Constants.UpdatableAppNameKey.update_minibutton_2.name());
                            Constants.URL_UPDATE_MiniButton3 = jsonObject.optString(Constants.UpdatableAppNameKey.update_minibutton_3.name());
                            Constants.URL_UPDATE_MiniButton4 = jsonObject.optString(Constants.UpdatableAppNameKey.update_minibutton_4.name());
                            Constants.URL_UPDATE_MiniButton5 = jsonObject.optString(Constants.UpdatableAppNameKey.update_minibutton_5.name());
                            Constants.URL_UPDATE_MiniButton6 = jsonObject.optString(Constants.UpdatableAppNameKey.update_minibutton_6.name());
                            Constants.URL_UPDATE_MiniButton7 = jsonObject.optString(Constants.UpdatableAppNameKey.update_minibutton_7.name());
                            Constants.URL_UPDATE_MiniButton8 = jsonObject.optString(Constants.UpdatableAppNameKey.update_minibutton_8.name());
                            Constants.URL_UPDATE_MiniButton9 = jsonObject.optString(Constants.UpdatableAppNameKey.update_minibutton_9.name());
                            Constants.URL_UPDATE_MiniButton10 = jsonObject.optString(Constants.UpdatableAppNameKey.update_minibutton_10.name());
                            Constants.URL_UPDATE_MiniButton11 = jsonObject.optString(Constants.UpdatableAppNameKey.update_minibutton_11.name());
                            Constants.URL_UPDATE_MiniButton12 = jsonObject.optString(Constants.UpdatableAppNameKey.update_minibutton_12.name());

                            Constants.URL_UPDATE_BUTTON_left_top = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButtonLeftTop.name());
                            Constants.URL_UPDATE_BUTTON_left_top_right = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButtonLeftTopRight.name());
                            Constants.URL_UPDATE_BUTTON_left1 = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButtonLeft1.name());
                            Constants.URL_UPDATE_BUTTON_left2 = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButtonLeft2.name());
                            Constants.URL_UPDATE_BUTTON_left3 = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButtonLeft3.name());
                            Constants.URL_UPDATE_BUTTON_right1 = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButtonRight1.name());
                            Constants.URL_UPDATE_BUTTON_right2 = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButtonRight2.name());
                            Constants.URL_UPDATE_BUTTON_right3 = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButtonRight3.name());
                            Constants.URL_UPDATE_BUTTON_WIFI = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButtonWifi.name());
                            Constants.URL_UPDATE_BUTTON_BLUETOOTH = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButtonBluetooth.name());
                            Constants.URL_UPDATE_BUTTON_PARENTAL = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButtonParental.name());
                            Constants.URL_UPDATE_BUTTON_HELP = jsonObject.optString(Constants.UpdatableAppNameKey.UpdateIvButtonHelp.name());
                            Constants.URL_UPDATE_POWER_BTN = jsonObject.optString(Constants.UpdatableAppNameKey.pwr_button.name());
                            Constants.URL_UPDATE_VPN_BTN = jsonObject.optString(Constants.UpdatableAppNameKey.vpn_button.name());

                            Constants.IMAGE_LOAD_URL = jsonObject.optString(Constants.UpdatableAppNameKey.background_image.name());
                            Constants.HELP_BACKGROUND_IMAGE_URL = jsonObject.optString(Constants.UpdatableAppNameKey.help_background_image.name());
                            Constants.LONG_CLICK_DELAY = Long.parseLong(jsonObject.optString(Constants.UpdatableAppNameKey.long_press_delay.name(), "3000"));
                            Constants.DYNAMIC_SYSTEM_DISPLAY_DENSITY = jsonObject.optString(Constants.UpdatableAppNameKey.display_size.name(), Constants.DYNAMIC_SYSTEM_DISPLAY_DENSITY);
                            if (jsonObject.has(Constants.UpdatableAppNameKey.display_size_version.name()) && (jsonObject.optInt(Constants.UpdatableAppNameKey.display_size_version.name(), 1) > SharedPreferenceStorage.getIntegerValue(mBase, Constants.Storage.DISPLAY_SIZE_LAST_UPDATE_VERSION.name(), 0))) {
                                SharedPreferenceStorage.setIntegerValue(mBase, Constants.Storage.DISPLAY_SIZE_LAST_UPDATE_VERSION.name(), jsonObject.optInt(Constants.UpdatableAppNameKey.display_size_version.name(), 1));
                                SharedPreferenceStorage.setBooleanValue(mBase, Constants.Storage.IS_NEED_TO_CHANGE_DISPLAY_TEXT_SIZE.name(), true);
                            }else {
                                SharedPreferenceStorage.setBooleanValue(mBase, Constants.Storage.IS_NEED_TO_CHANGE_DISPLAY_TEXT_SIZE.name(), false);
                            }

                            JSONArray jbutton_tutorial = jsonObject.optJSONArray(Constants.UpdatableAppNameKey.button_tutorial.name());
                            if (jbutton_tutorial != null && jbutton_tutorial.length() > 0) {
                                mBase.app.getHelpInfoInfoArrayList().clear();
                                for (int i = 0; i < jbutton_tutorial.length(); i++) {
                                    JSONObject jButtonTutotialItem = jbutton_tutorial.optJSONObject(i);
                                    if (jButtonTutotialItem != null) {
                                        HelpInfoOrAdsButtonInfo mHelpInfoOrAdsButtonInfo = new HelpInfoOrAdsButtonInfo();
                                        mHelpInfoOrAdsButtonInfo.setBtnId(jButtonTutotialItem.optString(Constants.UpdatableAppNameKey.btn_id.name()));
                                        mHelpInfoOrAdsButtonInfo.setTitle(jButtonTutotialItem.optString(Constants.UpdatableAppNameKey.title.name()));
                                        mHelpInfoOrAdsButtonInfo.setDescription(jButtonTutotialItem.optString(Constants.UpdatableAppNameKey.description.name()));
                                        mHelpInfoOrAdsButtonInfo.setHelpFileUrl(jButtonTutotialItem.optString(Constants.UpdatableAppNameKey.help_url.name()));
                                        String tutorialType = jButtonTutotialItem.optString(Constants.UpdatableAppNameKey.tutorial_type.name());
                                        if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.mov.name())) {
                                            mHelpInfoOrAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.mov);
                                        } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.mp4.name())) {
                                            mHelpInfoOrAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.mp4);
                                        } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.pdf.name())) {
                                            mHelpInfoOrAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.pdf);
                                        } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.png.name())) {
                                            mHelpInfoOrAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.png);
                                        }
                                        mBase.app.getHelpInfoInfoArrayList().add(mHelpInfoOrAdsButtonInfo);
                                    }
                                }
                            }

                            JSONArray hideAppsArray = jsonObject.optJSONArray(Constants.UpdatableAppNameKey.hide_apps.name());
                            mBase.app.setHiddenAppsIdListByServer(hideAppsArray);

                            JSONArray deleteAppsArray = jsonObject.optJSONArray(Constants.UpdatableAppNameKey.delete_app_startup.name());
                            mBase.app.setDeleteAppsIdListByServer(deleteAppsArray);

                            JSONObject jAds_button = jsonObject.optJSONObject(Constants.UpdatableAppNameKey.ads_button.name());
                            if (jAds_button != null && jAds_button.length() > 0) {
                                mBase.app.getAdsButtonInfoInfoHashMap().clear();
                                JSONObject btn_left_top = jAds_button.optJSONObject(Constants.UpdatableAppNameKey.btn_left_top.name());
                                if (btn_left_top != null) {
                                    HelpInfoOrAdsButtonInfo mAdsButtonInfo = new HelpInfoOrAdsButtonInfo();
                                    mAdsButtonInfo.setBtnId(Constants.UpdatableAppNameKey.btn_left_top.name());
                                    mAdsButtonInfo.setDescription(btn_left_top.optString(Constants.UpdatableAppNameKey.description.name()));
                                    mAdsButtonInfo.setTitle(btn_left_top.optString(Constants.UpdatableAppNameKey.title.name()));
                                    mAdsButtonInfo.setTitle(btn_left_top.optString(Constants.UpdatableAppNameKey.title.name()));
                                    mAdsButtonInfo.setHelpFileUrl(btn_left_top.optString(Constants.UpdatableAppNameKey.help_url.name()));
                                    String tutorialType = btn_left_top.optString(Constants.UpdatableAppNameKey.tutorial_type.name());
                                    if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.mov.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.mov);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.mp4.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.mp4);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.pdf.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.pdf);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.png.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.png);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.html.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.html);
                                    }else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.apk.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.apk);
                                    }
                                    mBase.app.getAdsButtonInfoInfoHashMap().put(mAdsButtonInfo.getBtnId(), mAdsButtonInfo);
                                }
                                JSONObject btn_left_top_right = jAds_button.optJSONObject(Constants.UpdatableAppNameKey.btn_left_top_right.name());
                                if (btn_left_top_right != null) {
                                    HelpInfoOrAdsButtonInfo mAdsButtonInfo = new HelpInfoOrAdsButtonInfo();
                                    mAdsButtonInfo.setBtnId(Constants.UpdatableAppNameKey.btn_left_top_right.name());
                                    mAdsButtonInfo.setDescription(btn_left_top_right.optString(Constants.UpdatableAppNameKey.description.name()));
                                    mAdsButtonInfo.setTitle(btn_left_top_right.optString(Constants.UpdatableAppNameKey.title.name()));
                                    mAdsButtonInfo.setTitle(btn_left_top_right.optString(Constants.UpdatableAppNameKey.title.name()));
                                    mAdsButtonInfo.setHelpFileUrl(btn_left_top_right.optString(Constants.UpdatableAppNameKey.help_url.name()));
                                    String tutorialType = btn_left_top_right.optString(Constants.UpdatableAppNameKey.tutorial_type.name());
                                    if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.mov.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.mov);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.mp4.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.mp4);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.pdf.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.pdf);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.png.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.png);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.html.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.html);
                                    }else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.apk.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.apk);
                                    }
                                    mBase.app.getAdsButtonInfoInfoHashMap().put(mAdsButtonInfo.getBtnId(), mAdsButtonInfo);
                                }
                                JSONObject btn_left1 = jAds_button.optJSONObject(Constants.UpdatableAppNameKey.btn_left1.name());
                                if (btn_left1 != null) {
                                    HelpInfoOrAdsButtonInfo mAdsButtonInfo = new HelpInfoOrAdsButtonInfo();
                                    mAdsButtonInfo.setBtnId(Constants.UpdatableAppNameKey.btn_left1.name());
                                    mAdsButtonInfo.setDescription(btn_left1.optString(Constants.UpdatableAppNameKey.description.name()));
                                    mAdsButtonInfo.setTitle(btn_left1.optString(Constants.UpdatableAppNameKey.title.name()));
                                    mAdsButtonInfo.setTitle(btn_left1.optString(Constants.UpdatableAppNameKey.title.name()));
                                    mAdsButtonInfo.setHelpFileUrl(btn_left1.optString(Constants.UpdatableAppNameKey.help_url.name()));
                                    String tutorialType = btn_left1.optString(Constants.UpdatableAppNameKey.tutorial_type.name());
                                    if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.mov.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.mov);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.mp4.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.mp4);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.pdf.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.pdf);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.png.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.png);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.html.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.html);
                                    }else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.apk.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.apk);
                                    }
                                    mBase.app.getAdsButtonInfoInfoHashMap().put(mAdsButtonInfo.getBtnId(), mAdsButtonInfo);
                                }
                                JSONObject btn_left2 = jAds_button.optJSONObject(Constants.UpdatableAppNameKey.btn_left2.name());
                                if (btn_left2 != null) {
                                    HelpInfoOrAdsButtonInfo mAdsButtonInfo = new HelpInfoOrAdsButtonInfo();
                                    mAdsButtonInfo.setBtnId(Constants.UpdatableAppNameKey.btn_left2.name());
                                    mAdsButtonInfo.setDescription(btn_left2.optString(Constants.UpdatableAppNameKey.description.name()));
                                    mAdsButtonInfo.setTitle(btn_left2.optString(Constants.UpdatableAppNameKey.title.name()));
                                    mAdsButtonInfo.setTitle(btn_left2.optString(Constants.UpdatableAppNameKey.title.name()));
                                    mAdsButtonInfo.setHelpFileUrl(btn_left2.optString(Constants.UpdatableAppNameKey.help_url.name()));
                                    String tutorialType = btn_left2.optString(Constants.UpdatableAppNameKey.tutorial_type.name());
                                    if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.mov.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.mov);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.mp4.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.mp4);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.pdf.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.pdf);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.png.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.png);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.html.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.html);
                                    }else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.apk.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.apk);
                                    }
                                    mBase.app.getAdsButtonInfoInfoHashMap().put(mAdsButtonInfo.getBtnId(), mAdsButtonInfo);
                                }
                                JSONObject btn_left3 = jAds_button.optJSONObject(Constants.UpdatableAppNameKey.btn_left3.name());
                                if (btn_left3 != null) {
                                    HelpInfoOrAdsButtonInfo mAdsButtonInfo = new HelpInfoOrAdsButtonInfo();
                                    mAdsButtonInfo.setBtnId(Constants.UpdatableAppNameKey.btn_left3.name());
                                    mAdsButtonInfo.setDescription(btn_left3.optString(Constants.UpdatableAppNameKey.description.name()));
                                    mAdsButtonInfo.setTitle(btn_left3.optString(Constants.UpdatableAppNameKey.title.name()));
                                    mAdsButtonInfo.setTitle(btn_left3.optString(Constants.UpdatableAppNameKey.title.name()));
                                    mAdsButtonInfo.setHelpFileUrl(btn_left3.optString(Constants.UpdatableAppNameKey.help_url.name()));
                                    String tutorialType = btn_left3.optString(Constants.UpdatableAppNameKey.tutorial_type.name());
                                    if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.mov.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.mov);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.mp4.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.mp4);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.pdf.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.pdf);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.png.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.png);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.html.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.html);
                                    }else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.apk.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.apk);
                                    }
                                    mBase.app.getAdsButtonInfoInfoHashMap().put(mAdsButtonInfo.getBtnId(), mAdsButtonInfo);
                                }
                                JSONObject btn_right1 = jAds_button.optJSONObject(Constants.UpdatableAppNameKey.btn_right1.name());
                                if (btn_right1 != null) {
                                    HelpInfoOrAdsButtonInfo mAdsButtonInfo = new HelpInfoOrAdsButtonInfo();
                                    mAdsButtonInfo.setBtnId(Constants.UpdatableAppNameKey.btn_right1.name());
                                    mAdsButtonInfo.setDescription(btn_right1.optString(Constants.UpdatableAppNameKey.description.name()));
                                    mAdsButtonInfo.setTitle(btn_right1.optString(Constants.UpdatableAppNameKey.title.name()));
                                    mAdsButtonInfo.setTitle(btn_right1.optString(Constants.UpdatableAppNameKey.title.name()));
                                    mAdsButtonInfo.setHelpFileUrl(btn_right1.optString(Constants.UpdatableAppNameKey.help_url.name()));
                                    String tutorialType = btn_right1.optString(Constants.UpdatableAppNameKey.tutorial_type.name());
                                    if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.mov.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.mov);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.mp4.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.mp4);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.pdf.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.pdf);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.png.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.png);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.html.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.html);
                                    }
                                    mBase.app.getAdsButtonInfoInfoHashMap().put(mAdsButtonInfo.getBtnId(), mAdsButtonInfo);
                                }
                                JSONObject btn_right2 = jAds_button.optJSONObject(Constants.UpdatableAppNameKey.btn_right2.name());
                                if (btn_right2 != null) {
                                    HelpInfoOrAdsButtonInfo mAdsButtonInfo = new HelpInfoOrAdsButtonInfo();
                                    mAdsButtonInfo.setBtnId(Constants.UpdatableAppNameKey.btn_right2.name());
                                    mAdsButtonInfo.setDescription(btn_right2.optString(Constants.UpdatableAppNameKey.description.name()));
                                    mAdsButtonInfo.setTitle(btn_right2.optString(Constants.UpdatableAppNameKey.title.name()));
                                    mAdsButtonInfo.setTitle(btn_right2.optString(Constants.UpdatableAppNameKey.title.name()));
                                    mAdsButtonInfo.setHelpFileUrl(btn_right2.optString(Constants.UpdatableAppNameKey.help_url.name()));
                                    String tutorialType = btn_right2.optString(Constants.UpdatableAppNameKey.tutorial_type.name());
                                    if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.mov.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.mov);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.mp4.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.mp4);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.pdf.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.pdf);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.png.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.png);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.html.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.html);
                                    }
                                    mBase.app.getAdsButtonInfoInfoHashMap().put(mAdsButtonInfo.getBtnId(), mAdsButtonInfo);
                                }
                                JSONObject btn_right3 = jAds_button.optJSONObject(Constants.UpdatableAppNameKey.btn_right3.name());
                                if (btn_right3 != null) {
                                    HelpInfoOrAdsButtonInfo mAdsButtonInfo = new HelpInfoOrAdsButtonInfo();
                                    mAdsButtonInfo.setBtnId(Constants.UpdatableAppNameKey.btn_right3.name());
                                    mAdsButtonInfo.setDescription(btn_right3.optString(Constants.UpdatableAppNameKey.description.name()));
                                    mAdsButtonInfo.setTitle(btn_right3.optString(Constants.UpdatableAppNameKey.title.name()));
                                    mAdsButtonInfo.setTitle(btn_right3.optString(Constants.UpdatableAppNameKey.title.name()));
                                    mAdsButtonInfo.setHelpFileUrl(btn_right3.optString(Constants.UpdatableAppNameKey.help_url.name()));
                                    String tutorialType = btn_right3.optString(Constants.UpdatableAppNameKey.tutorial_type.name());
                                    if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.mov.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.mov);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.mp4.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.mp4);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.pdf.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.pdf);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.png.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.png);
                                    } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.html.name())) {
                                        mAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.html);
                                    }
                                    mBase.app.getAdsButtonInfoInfoHashMap().put(mAdsButtonInfo.getBtnId(), mAdsButtonInfo);
                                }
                            }

                            JSONObject rss_html = jsonObject.optJSONObject(Constants.UpdatableAppNameKey.rss_html.name());
                            if (rss_html != null) {
                                RssFeedInfo mRssFeedInfo = new RssFeedInfo();
                                mRssFeedInfo.setRss_status(rss_html.optString(Constants.UpdatableAppNameKey.rss_status.name()));
                                mRssFeedInfo.setShort_rss_link(rss_html.optString(Constants.UpdatableAppNameKey.short_rss_link.name()));
                                mRssFeedInfo.setFull_rss_link(rss_html.optString(Constants.UpdatableAppNameKey.full_rss_link.name()));
                                mBase.app.setHtmlRssFeedInfo(mRssFeedInfo);
                            }

                            JSONObject start_up_popup = jsonObject.optJSONObject(Constants.UpdatableAppNameKey.start_up_popup.name());
                            if (start_up_popup != null) {
                                HelpInfoOrAdsButtonInfo helpInfoOrAdsButtonInfo = new HelpInfoOrAdsButtonInfo();
                                helpInfoOrAdsButtonInfo.setBtnId("");
                                helpInfoOrAdsButtonInfo.setDescription("");
                                helpInfoOrAdsButtonInfo.setTitle("");
                                helpInfoOrAdsButtonInfo.setHelpFileUrl(start_up_popup.optString(Constants.UpdatableAppNameKey.msg_url.name(), ""));
                                String tutorialType = start_up_popup.optString(Constants.UpdatableAppNameKey.msg_type.name());
                                if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.html.name())) {
                                    helpInfoOrAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.html);
                                } else if (tutorialType.equalsIgnoreCase(Constants.FILE_TYPE.png.name())) {
                                    helpInfoOrAdsButtonInfo.setTutorialType(Constants.FILE_TYPE.png);
                                }
                                helpInfoOrAdsButtonInfo.setMsg_frequency(start_up_popup.optInt(Constants.UpdatableAppNameKey.msg_frequency.name()));
                                helpInfoOrAdsButtonInfo.setMsg_update(start_up_popup.optInt(Constants.UpdatableAppNameKey.msg_update.name()));
                                mBase.app.setStartUpMessageDetails(helpInfoOrAdsButtonInfo);
                            }

                            JSONObject app_auto_update = jsonObject.optJSONObject(Constants.UpdatableAppNameKey.app_auto_update.name());
                            if (app_auto_update != null) {
                                if (app_auto_update.has(Constants.UpdatableAppNameKey.update_no.name()) && (app_auto_update.optInt(Constants.UpdatableAppNameKey.update_no.name(), 1) > SharedPreferenceStorage.getIntegerValue(mBase, Constants.Storage.LAST_APP_INSTALL_UPDATE_VERSION.name(), 0))) {
                                    isInstantAppUpdateRequire = true;
                                    JSONArray updateAppLinksArray = app_auto_update.optJSONArray(Constants.UpdatableAppNameKey.update_app_link.name());
                                    JSONArray deleteAppDuringUpdateArray = app_auto_update.optJSONArray(Constants.UpdatableAppNameKey.update_del.name());
                                    if (deleteAppDuringUpdateArray != null && deleteAppDuringUpdateArray.length() > 0) {
                                        mBase.app.setDeleteAppsIdListByServerDuringAutoUpdate(deleteAppDuringUpdateArray);
                                    }

                                    if (mBase.app.getNewInstantAppUpdateLinkList() == null)
                                        mBase.app.setNewInstantAppUpdateLinkList(new ArrayList<String>());
                                    mBase.app.getNewInstantAppUpdateLinkList().clear();
                                    if (updateAppLinksArray != null && updateAppLinksArray.length() > 0) {
                                        for (int i = 0; i < updateAppLinksArray.length(); i++) {
                                            String linkIs = updateAppLinksArray.optString(i);
                                            if (linkIs != null && linkIs.length() > 0)
                                                mBase.app.getNewInstantAppUpdateLinkList().add(linkIs);
                                        }
                                    }
                                    Constants.INSTANT_UPDATE_VERSION = app_auto_update.optInt(Constants.UpdatableAppNameKey.update_no.name(), 1);
                                }
                            }

                            JSONObject jsonButtonImages = jsonObject.optJSONObject(Constants.UpdatableAppNameKey.buttons_images.name());
                            if (jsonButtonImages != null && jsonButtonImages.length() > 0) {
                                Constants.ICON_URL_BUTTON_VPN = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_vpn_button.name());
                                Constants.ICON_URL_BUTTON_TOP_NEW = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_button_top_new.name());
                                Constants.ICON_URL_BUTTON0 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_button0.name());
                                SharedPreferenceStorage.setStringValue(mBase, "icon_button0", Constants.ICON_URL_BUTTON0);
                                Constants.ICON_URL_BUTTON1 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_button1.name());
                                Constants.ICON_URL_BUTTON2 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_button2.name());
                                Constants.ICON_URL_BUTTON3 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_button3.name());
                                Constants.ICON_URL_BUTTON4 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_button4.name());
                                Constants.ICON_URL_BUTTON5 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_button5.name());
                                Constants.ICON_URL_BUTTON6 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_button6.name());
                                Constants.ICON_URL_BUTTON7 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_button7.name());
                                Constants.ICON_URL_BUTTON8 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_button8.name());
                                Constants.ICON_URL_BUTTON9 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_button9.name());
                                Constants.ICON_URL_BUTTON10 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_button10.name());
                                Constants.ICON_URL_BUTTON11 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_button11.name());

                                Constants.ICON_URL_MINIBUTTON1 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_minibutton1.name());
                                Constants.ICON_URL_MINIBUTTON2 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_minibutton2.name());
                                Constants.ICON_URL_MINIBUTTON3 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_minibutton3.name());
                                Constants.ICON_URL_MINIBUTTON4 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_minibutton4.name());
                                Constants.ICON_URL_MINIBUTTON5 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_minibutton5.name());
                                Constants.ICON_URL_MINIBUTTON6 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_minibutton6.name());
                                Constants.ICON_URL_MINIBUTTON7 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_minibutton7.name());
                                Constants.ICON_URL_MINIBUTTON8 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_minibutton8.name());
                                Constants.ICON_URL_MINIBUTTON9 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_minibutton9.name());
                                Constants.ICON_URL_MINIBUTTON10 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_minibutton10.name());
                                Constants.ICON_URL_MINIBUTTON11 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_minibutton11.name());
                                Constants.ICON_URL_MINIBUTTON12 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_minibutton12.name());
                                Constants.ICON_URL_MINIBUTTON13 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_minibutton13.name());


                                Constants.ICON_PRESSED_URL_MINIBUTTON1 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_minibutton1.name());
                                Constants.ICON_PRESSED_URL_MINIBUTTON2 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_minibutton2.name());
                                Constants.ICON_PRESSED_URL_MINIBUTTON3 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_minibutton3.name());
                                Constants.ICON_PRESSED_URL_MINIBUTTON4 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_minibutton4.name());
                                Constants.ICON_PRESSED_URL_MINIBUTTON5 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_minibutton5.name());
                                Constants.ICON_PRESSED_URL_MINIBUTTON6 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_minibutton6.name());
                                Constants.ICON_PRESSED_URL_MINIBUTTON7 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_minibutton7.name());
                                Constants.ICON_PRESSED_URL_MINIBUTTON8 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_minibutton8.name());
                                Constants.ICON_PRESSED_URL_MINIBUTTON9 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_minibutton9.name());
                                Constants.ICON_PRESSED_URL_MINIBUTTON10 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_minibutton10.name());
                                Constants.ICON_PRESSED_URL_MINIBUTTON11 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_minibutton11.name());
                                Constants.ICON_PRESSED_URL_MINIBUTTON12 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_minibutton12.name());
                                Constants.ICON_PRESSED_URL_MINIBUTTON13 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_minibutton13.name());

                                Constants.ICON_URL_DOCK_BUTTON = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_dock_button.name());
                                Constants.ICON_URL_PWR_BUTTON = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pwr_button.name());
                                Constants.ICON_URL_HELP_BUTTON = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_help_button.name());
                                Constants.ICON_URL_BLANK_BUTTON = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_blank_button.name());
                                Constants.ICON_URL_DOUBLEBLANK_BUTTON = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_doubleblank_button.name());
                                Constants.ICON_URL_UPDATE_AVAILABLE = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_updateavailable_button.name());

                                SharedPreferenceStorage.setStringValue(mBase, "icon_update_button", jsonButtonImages.optString("icon_update_button"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_pressed_update_button", jsonButtonImages.optString("icon_pressed_update_button"));

                                SharedPreferenceStorage.setStringValue(mBase, "icon_button_left_top", jsonButtonImages.optString("icon_button_left_top"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_button_left_top_right", jsonButtonImages.optString("icon_button_left_top_right"));
                                Log.i("TAG", "json icon: "+ jsonButtonImages.optString("icon_button_left_top"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_button_left1", jsonButtonImages.optString("icon_button_left1"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_button_left2", jsonButtonImages.optString("icon_button_left2"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_button_left3", jsonButtonImages.optString("icon_button_left3"));
                                
                                SharedPreferenceStorage.setStringValue(mBase, "icon_button_right1", jsonButtonImages.optString("icon_button_right1"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_button_right2", jsonButtonImages.optString("icon_button_right2"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_button_right3", jsonButtonImages.optString("icon_button_right3"));

                                Constants.ICON_PRESSED_URL_BUTTON_VPN = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_vpn_button.name());
                                Constants.ICON_PRESSED_URL_BUTTON0 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_button0.name());
                                Constants.ICON_PRESSED_URL_BUTTON0 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_button_top_new.name());
                                Constants.ICON_PRESSED_URL_BUTTON1 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_button1.name());
                                Constants.ICON_PRESSED_URL_BUTTON2 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_button2.name());
                                Constants.ICON_PRESSED_URL_BUTTON3 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_button3.name());
                                Constants.ICON_PRESSED_URL_BUTTON4 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_button4.name());
                                Constants.ICON_PRESSED_URL_BUTTON5 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_button5.name());
                                Constants.ICON_PRESSED_URL_BUTTON6 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_button6.name());
                                Constants.ICON_PRESSED_URL_BUTTON7 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_button7.name());
                                Constants.ICON_PRESSED_URL_BUTTON8 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_button8.name());
                                Constants.ICON_PRESSED_URL_BUTTON9 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_button9.name());
                                Constants.ICON_PRESSED_URL_BUTTON10 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_button10.name());
                                Constants.ICON_PRESSED_URL_BUTTON11 = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_button11.name());
                                Constants.ICON_PRESSED_URL_DOCK_BUTTON = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_dock_button.name());
                                Constants.ICON_PRESSED_URL_PWR_BUTTON = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_pwr_button.name());
                                Constants.ICON_PRESSED_URL_HELP_BUTTON = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_help_button.name());
                                Constants.ICON_PRESSED_URL_BLANK_BUTTON = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_blank_button.name());
                                Constants.ICON_PRESSED_URL_DOUBLEBLANK_BUTTON = jsonButtonImages.optString(Constants.UpdatableAppNameKey.icon_pressed_doubleblank_button.name());

                                SharedPreferenceStorage.setStringValue(mBase, "icon_minibutton1", jsonButtonImages.optString("icon_minibutton1"));

                                SharedPreferenceStorage.setStringValue(mBase, "icon_minibutton2", jsonButtonImages.optString("icon_minibutton2"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_minibutton3", jsonButtonImages.optString("icon_minibutton3"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_minibutton4", jsonButtonImages.optString("icon_minibutton4"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_minibutton5", jsonButtonImages.optString("icon_minibutton5"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_minibutton6", jsonButtonImages.optString("icon_minibutton6"));
                                Log.i("TAG", "run:  miniicon "  + jsonButtonImages.optString("icon_minibutton6"));

                                SharedPreferenceStorage.setStringValue(mBase, "icon_minibutton7", jsonButtonImages.optString("icon_minibutton7"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_minibutton8", jsonButtonImages.optString("icon_minibutton8"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_minibutton9", jsonButtonImages.optString("icon_minibutton9"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_minibutton10", jsonButtonImages.optString("icon_minibutton10"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_minibutton11", jsonButtonImages.optString("icon_minibutton11"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_minibutton12", jsonButtonImages.optString("icon_minibutton12"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_minibutton13", jsonButtonImages.optString("icon_minibutton13"));

                                SharedPreferenceStorage.setStringValue(mBase, "icon_pressed_minibutton1", jsonButtonImages.optString("icon_pressed_minibutton1"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_pressed_minibutton2", jsonButtonImages.optString("icon_pressed_minibutton2"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_pressed_minibutton3", jsonButtonImages.optString("icon_pressed_minibutton3"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_pressed_minibutton4", jsonButtonImages.optString("icon_pressed_minibutton4"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_pressed_minibutton5", jsonButtonImages.optString("icon_pressed_minibutton5"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_pressed_minibutton6", jsonButtonImages.optString("icon_pressed_minibutton6"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_pressed_minibutton7", jsonButtonImages.optString("icon_pressed_minibutton7"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_pressed_minibutton8", jsonButtonImages.optString("icon_pressed_minibutton8"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_pressed_minibutton9", jsonButtonImages.optString("icon_pressed_minibutton9"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_pressed_minibutton10", jsonButtonImages.optString("icon_pressed_minibutton10"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_pressed_minibutton11", jsonButtonImages.optString("icon_pressed_minibutton11"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_pressed_minibutton12", jsonButtonImages.optString("icon_pressed_minibutton12"));
                                SharedPreferenceStorage.setStringValue(mBase, "icon_pressed_minibutton13", jsonButtonImages.optString("icon_pressed_minibutton13"));
                            }
                            JSONObject jsonButtonAppIds = jsonObject.optJSONObject(Constants.UpdatableAppNameKey.button_appids.name());
                            if (jsonButtonAppIds != null && jsonButtonAppIds.length() > 0) {
                                Constants.DEF_APPID_BUTTON_TOP = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_button0.name());
                                Log.i("TAG", "run: toppackage "+Constants.DEF_APPID_BUTTON_TOP);
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_button0.name(), Constants.DEF_APPID_BUTTON_TOP);

                                Constants.DEF_APPID_BUTTON1 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_button1.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_button1.name(), Constants.DEF_APPID_BUTTON1);

                                Constants.DEF_APPID_BUTTON_TOP_NEW = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_buttontop_new.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_buttontop_new.name(), Constants.DEF_APPID_BUTTON_TOP_NEW);

                                Constants.DEF_APPID_BUTTON2 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_button2.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_button2.name(), Constants.DEF_APPID_BUTTON2);
                                Constants.DEF_APPID_BUTTON3 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_button3.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_button3.name(), Constants.DEF_APPID_BUTTON3);
                                Constants.DEF_APPID_BUTTON4 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_button4.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_button4.name(), Constants.DEF_APPID_BUTTON4);
                                Constants.DEF_APPID_BUTTON5 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_button5.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_button5.name(), Constants.DEF_APPID_BUTTON5);
                                Constants.DEF_APPID_BUTTON6 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_button6.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_button6.name(), Constants.DEF_APPID_BUTTON6);
                                Constants.DEF_APPID_BUTTON7 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_button7.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_button7.name(), Constants.DEF_APPID_BUTTON7);
                                Constants.DEF_APPID_BUTTON8 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_button8.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_button8.name(), Constants.DEF_APPID_BUTTON8);
                                Constants.DEF_APPID_BUTTON9 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_button9.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_button9.name(), Constants.DEF_APPID_BUTTON9);
                                Constants.DEF_APPID_BUTTON10 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_button10.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_button10.name(), Constants.DEF_APPID_BUTTON10);
                                Constants.DEF_APPID_BUTTON11 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_button11.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_button11.name(), Constants.DEF_APPID_BUTTON11);

                                //mini buttons
                                Constants.DEF_APPID_MiniBUTTON1 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton1.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_minibutton1.name(), Constants.DEF_APPID_MiniBUTTON1);
                                Constants.DEF_APPID_MiniBUTTON2 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton2.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_minibutton2.name(), Constants.DEF_APPID_MiniBUTTON2);
                                Constants.DEF_APPID_MiniBUTTON3 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton3.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_minibutton3.name(), Constants.DEF_APPID_MiniBUTTON3);
                                Constants.DEF_APPID_MiniBUTTON4 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton4.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_minibutton4.name(), Constants.DEF_APPID_MiniBUTTON4);
                                Constants.DEF_APPID_MiniBUTTON5 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton5.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_minibutton5.name(), Constants.DEF_APPID_MiniBUTTON5);
                                Constants.DEF_APPID_MiniBUTTON6 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton6.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_minibutton6.name(), Constants.DEF_APPID_MiniBUTTON6);
                                Constants.DEF_APPID_MiniBUTTON7 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton7.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_minibutton7.name(), Constants.DEF_APPID_MiniBUTTON7);
                                Constants.DEF_APPID_MiniBUTTON8 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton8.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_minibutton8.name(), Constants.DEF_APPID_MiniBUTTON8);
                                Constants.DEF_APPID_MiniBUTTON9 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton9.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_minibutton9.name(), Constants.DEF_APPID_MiniBUTTON9);
                                Constants.DEF_APPID_MiniBUTTON10 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton10.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_minibutton10.name(), Constants.DEF_APPID_MiniBUTTON10);
                                Constants.DEF_APPID_MiniBUTTON11 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton11.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_minibutton11.name(), Constants.DEF_APPID_MiniBUTTON11);
                                Constants.DEF_APPID_MiniBUTTON12 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton12.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_minibutton12.name(), Constants.DEF_APPID_MiniBUTTON12);
                                Constants.DEF_APPID_MiniBUTTON13 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton13.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_minibutton13.name(), Constants.DEF_APPID_MiniBUTTON13);



                                Constants.DEF_APPID_PWR_BUTTON = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_pwr_button.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_pwr_button.name(), Constants.DEF_APPID_PWR_BUTTON);
                                Constants.DEF_APPID_VPN_BUTTON = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_vpn_button.name());
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_vpn_button.name(), Constants.DEF_APPID_VPN_BUTTON);
                                Constants.DEF_APPID_DOCK = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_dock_btn.name(), Constants.DEF_APPID_DOCK);
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_dock_btn.name(), Constants.DEF_APPID_DOCK);
                                Constants.DEF_APPID_HELP_BUTTON = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_help_btn.name(), Constants.DEF_APPID_HELP_BUTTON);
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.appid_help_btn.name(), Constants.DEF_APPID_HELP_BUTTON);

                                Constants.APPID_BUTTON_TOP = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_button0.name());
                                Constants.APPID_BUTTON_TOP_NEW = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_buttontop_new.name());
                                Constants.APPID_BUTTON1 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_button1.name());
                                Constants.APPID_BUTTON2 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_button2.name());
                                Constants.APPID_BUTTON3 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_button3.name());
                                Constants.APPID_BUTTON4 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_button4.name());
                                Constants.APPID_BUTTON5 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_button5.name());
                                Constants.APPID_BUTTON6 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_button6.name());
                                Constants.APPID_BUTTON7 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_button7.name());
                                Constants.APPID_BUTTON8 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_button8.name());
                                Constants.APPID_BUTTON9 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_button9.name());
                                Constants.APPID_BUTTON10 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_button10.name());
                                Constants.APPID_BUTTON11 = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_button11.name());
                                Constants.APPID_DOCK = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_dock_btn.name(), Constants.APPID_DOCK);
                                Constants.APPID_PWR_BUTTON = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_pwr_button.name(), Constants.APPID_PWR_BUTTON);

                                Constants.APPID_MiniButton1=jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton1.name(), Constants.APPID_MiniButton1);
                                Constants.APPID_MiniButton2=jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton2.name(), Constants.APPID_MiniButton2);
                                Constants.APPID_MiniButton3=jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton3.name(), Constants.APPID_MiniButton3);
                                Constants.APPID_MiniButton4=jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton4.name(), Constants.APPID_MiniButton4);
                                Constants.APPID_MiniButton5=jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton5.name(), Constants.APPID_MiniButton5);
                                Constants.APPID_MiniButton6=jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton6.name(), Constants.APPID_MiniButton6);
                                Constants.APPID_MiniButton7=jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton7.name(), Constants.APPID_MiniButton7);
                                Constants.APPID_MiniButton8=jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton8.name(), Constants.APPID_MiniButton8);
                                Constants.APPID_MiniButton9=jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton9.name(), Constants.APPID_MiniButton9);
                                Constants.APPID_MiniButton10=jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton10.name(), Constants.APPID_MiniButton10);
                                Constants.APPID_MiniButton11=jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton11.name(), Constants.APPID_MiniButton11);
                                Constants.APPID_MiniButton12=jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton12.name(), Constants.APPID_MiniButton12);
                                Constants.APPID_MiniButton13=jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_minibutton13.name(), Constants.APPID_MiniButton13);

                                Constants.APPID_VPN_BUTTON = jsonButtonAppIds.optString(Constants.UpdatableAppNameKey.appid_vpn_button.name(), Constants.APPID_VPN_BUTTON);
                            }

                            JSONObject jsonDeleteAppIds = jsonObject.optJSONObject(Constants.UpdatableAppNameKey.delete_appids.name());
                            Log.i("TAG", "run: delete package: "+jsonDeleteAppIds.toString());
                            if (jsonDeleteAppIds != null && jsonDeleteAppIds.length() > 0){
                                SharedPreferenceStorage.setStringValue(mBase, "delete_pkg_length", jsonDeleteAppIds.length()+"");
                                for (int i=0; i<jsonDeleteAppIds.length(); i++){
                                    String deleteAppId = jsonDeleteAppIds.optString(String.valueOf(i));
                                    Log.i("TAG", "run: delete package: "+deleteAppId);
                                    if (!TextUtils.isEmpty(deleteAppId)){
                                        SharedPreferenceStorage.setStringValue(mBase, i+"", deleteAppId);
                                    }
                                }

                            }
                            SharedPreferenceStorage.setStringValue(mBase, "startupvideo_status", jsonObject.optString("startupvideo_status"));
                            SharedPreferenceStorage.setStringValue(mBase, "isshow", jsonObject.optString("isshow"));
                            SharedPreferenceStorage.setStringValue(mBase, "link", jsonObject.optString("link"));


                            SharedPreferenceStorage.setStringValue(mBase, "refreshtimedisplay", jsonObject.optString("refreshtimedisplay"));
                            SharedPreferenceStorage.setStringValue(mBase, "idlerefreshtime", jsonObject.optString("idlerefreshtime"));

                            SharedPreferenceStorage.setStringValue(mBase, "news_button_url", jsonObject.optString("news_button_url"));

                            SharedPreferenceStorage.setStringValue(mBase, "push_launcher_update", jsonObject.optString("push_launcher_update"));
                            Log.i("TAG", "run:+push_launcher_update "+jsonObject.optString("push_launcher_update"));




                            JSONObject jsonAppButton0 = jsonObject.optJSONObject(Constants.UpdatableAppNameKey.button0.name());
                            if (jsonAppButton0 != null && jsonAppButton0.length() > 0) {

                                Log.i("TAG", "TopButton package: " + jsonAppButton0.getString("button_type"));
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.button0.name(), jsonAppButton0.getString("app_id"));
                                SharedPreferenceStorage.setStringValue(mBase, "btn0_type", jsonAppButton0.getString("button_type"));
                                SharedPreferenceStorage.setStringValue(mBase, "btn0_help_url", jsonAppButton0.getString("function_url"));
                                SharedPreferenceStorage.setStringValue(mBase, "btn0_version", jsonAppButton0.getString("version"));
                                SharedPreferenceStorage.setStringValue(mBase, "btn0_title", jsonAppButton0.getString("title"));
                                SharedPreferenceStorage.setStringValue(mBase, "btn0_helpinfo_url", jsonAppButton0.getString("btn0_helpinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase, "btn0_appinfo_url", jsonAppButton0.getString("btn0_appinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase, "btn0_appinfo_url_type", jsonAppButton0.getString("appinfo_type"));

                            }
                            JSONObject jsonAppButtonTopNew = jsonObject.optJSONObject(Constants.UpdatableAppNameKey.buttontop_new.name());
                            if (jsonAppButton0 != null && jsonAppButton0.length() > 0) {

                                Log.i("TAG", "TopButton package: "+jsonAppButtonTopNew.getString("button_type"));
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.buttontop_new.name(), jsonAppButtonTopNew.getString("app_id"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn_top_new_type", jsonAppButtonTopNew.getString("button_type"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn_top_new_help_url", jsonAppButtonTopNew.getString("function_url"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn_top_new_version",jsonAppButtonTopNew.getString("version"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn_top_new_title", jsonAppButtonTopNew.getString("title"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn_top_new_helpinfo_url", jsonAppButtonTopNew.getString("btn_top_new_helpinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn_top_new_appinfo_url", jsonAppButtonTopNew.getString("btn_top_new_appinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase, "btn_top_new_appinfo_url_type", jsonAppButton0.getString("appinfo_type"));
//

                            }
                            JSONObject jsonAppButton1 = jsonObject.optJSONObject(Constants.UpdatableAppNameKey.button1.name());
                            if (jsonAppButton1 != null && jsonAppButton1.length() > 0) {

                                Log.i("TAG", "TopButton package: "+jsonAppButton1.getString("button_type"));
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.button1.name(), jsonAppButton1.getString("app_id"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn1_type", jsonAppButton1.getString("button_type"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn1_help_url", jsonAppButton1.getString("function_url"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn1_version",jsonAppButton1.getString("version"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn1_title", jsonAppButton1.getString("title"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn1_helpinfo_url", jsonAppButton1.getString("btn1_helpinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn1_appinfo_url", jsonAppButton1.getString("btn1_appinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase, "btn1_appinfo_url_type", jsonAppButton1.getString("appinfo_type"));

                                Log.i("TAG", "run:  btn1_helpinfo_url "  + jsonAppButton1.getString("btn1_helpinfo_url"));
//

                            }
                            JSONObject jsonAppButton2 = jsonObject.optJSONObject(Constants.UpdatableAppNameKey.button2.name());
                            if (jsonAppButton2 != null && jsonAppButton2.length() > 0) {
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.button2.name(), jsonAppButton2.getString("app_id"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn2_type", jsonAppButton2.getString("button_type"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn2_help_url", jsonAppButton2.getString("function_url"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn2_version",jsonAppButton2.getString("version"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn2_title", jsonAppButton2.getString("title"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn2_helpinfo_url", jsonAppButton2.getString("btn2_helpinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn2_appinfo_url", jsonAppButton2.getString("btn2_appinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase, "btn2_appinfo_url_type", jsonAppButton2.getString("appinfo_type"));
                            }
                            JSONObject jsonAppButton3 = jsonObject.optJSONObject(Constants.UpdatableAppNameKey.button3.name());
                            if (jsonAppButton3 != null && jsonAppButton3.length() > 0) {
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.button3.name(), jsonAppButton3.getString("app_id"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn3_type", jsonAppButton3.getString("button_type"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn3_help_url", jsonAppButton3.getString("function_url"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn3_version",jsonAppButton3.getString("version"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn3_title", jsonAppButton3.getString("title"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn3_helpinfo_url", jsonAppButton3.getString("btn3_helpinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn3_appinfo_url", jsonAppButton3.getString("btn3_appinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase, "btn3_appinfo_url_type", jsonAppButton3.getString("appinfo_type"));
                            }
                            JSONObject jsonAppButton4 = jsonObject.optJSONObject(Constants.UpdatableAppNameKey.button4.name());
                            if (jsonAppButton4 != null && jsonAppButton4.length() > 0) {
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.button4.name(), jsonAppButton4.getString("app_id"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn4_type", jsonAppButton4.getString("button_type"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn4_help_url", jsonAppButton4.getString("function_url"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn4_version",jsonAppButton4.getString("version"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn4_title", jsonAppButton4.getString("title"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn4_helpinfo_url", jsonAppButton4.getString("btn4_helpinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn4_appinfo_url", jsonAppButton4.getString("btn4_appinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase, "btn4_appinfo_url_type", jsonAppButton4.getString("appinfo_type"));
                            }
                            JSONObject jsonAppButton5 = jsonObject.optJSONObject(Constants.UpdatableAppNameKey.button5.name());
                            if (jsonAppButton5 != null && jsonAppButton5.length() > 0) {
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.button5.name(), jsonAppButton5.getString("app_id"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn5_type", jsonAppButton5.getString("button_type"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn5_help_url", jsonAppButton5.getString("function_url"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn5_version",jsonAppButton5.getString("version"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn5_title", jsonAppButton5.getString("title"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn5_helpinfo_url", jsonAppButton5.getString("btn5_helpinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn5_appinfo_url", jsonAppButton5.getString("btn5_appinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase, "btn5_appinfo_url_type", jsonAppButton5.getString("appinfo_type"));
                            }
                            JSONObject jsonAppButton6 = jsonObject.optJSONObject(Constants.UpdatableAppNameKey.button6.name());
                            if (jsonAppButton6 != null && jsonAppButton6.length() > 0) {

                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.button6.name(), jsonAppButton6.getString("app_id"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn6_type", jsonAppButton6.getString("button_type"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn6_help_url", jsonAppButton6.getString("function_url"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn6_version",jsonAppButton6.getString("version"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn6_title", jsonAppButton6.getString("title"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn6_helpinfo_url", jsonAppButton6.getString("btn6_helpinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn6_appinfo_url", jsonAppButton6.getString("btn6_appinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase, "btn6_appinfo_url_type", jsonAppButton6.getString("appinfo_type"));
                            }
                            JSONObject jsonAppButton7 = jsonObject.optJSONObject(Constants.UpdatableAppNameKey.button7.name());
                            if (jsonAppButton7 != null && jsonAppButton7.length() > 0) {
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.button7.name(), jsonAppButton7.getString("app_id"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn7_type", jsonAppButton7.getString("button_type"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn7_help_url", jsonAppButton7.getString("function_url"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn7_version",jsonAppButton7.getString("version"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn7_title", jsonAppButton7.getString("title"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn7_helpinfo_url", jsonAppButton7.getString("btn7_helpinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn7_appinfo_url", jsonAppButton7.getString("btn7_appinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase, "btn7_appinfo_url_type", jsonAppButton7.getString("appinfo_type"));
                            }
                            JSONObject jsonAppButton8 = jsonObject.optJSONObject(Constants.UpdatableAppNameKey.button8.name());
                            if (jsonAppButton8 != null && jsonAppButton8.length() > 0) {
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.button8.name(), jsonAppButton8.getString("app_id"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn8_type", jsonAppButton8.getString("button_type"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn8_help_url", jsonAppButton8.getString("function_url"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn8_version",jsonAppButton8.getString("version"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn8_title", jsonAppButton8.getString("title"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn8_helpinfo_url", jsonAppButton8.getString("btn8_helpinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn8_appinfo_url", jsonAppButton8.getString("btn8_appinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase, "btn8_appinfo_url_type", jsonAppButton8.getString("appinfo_type"));
                            }
                            JSONObject jsonAppButton9 = jsonObject.optJSONObject(Constants.UpdatableAppNameKey.button9.name());
                            if (jsonAppButton9 != null && jsonAppButton9.length() > 0) {
                                Log.i("TAG", "run: button9 "+jsonAppButton9);
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.button9.name(), jsonAppButton9.getString("app_id"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn9_type", jsonAppButton9.getString("button_type"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn9_help_url", jsonAppButton9.getString("function_url"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn9_version",jsonAppButton9.getString("version"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn9_title", jsonAppButton9.getString("title"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn9_helpinfo_url", jsonAppButton9.getString("btn9_helpinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn9_appinfo_url", jsonAppButton9.getString("btn9_appinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase, "btn9_appinfo_url_type", jsonAppButton9.getString("appinfo_type"));
                            }
                            JSONObject jsonAppButton10 = jsonObject.optJSONObject(Constants.UpdatableAppNameKey.button10.name());
                            if (jsonAppButton10 != null && jsonAppButton10.length() > 0) {
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.button10.name(), jsonAppButton10.getString("app_id"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn10_type", jsonAppButton10.getString("button_type"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn10_help_url", jsonAppButton10.getString("function_url"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn10_version",jsonAppButton10.getString("version"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn10_title", jsonAppButton10.getString("title"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn10_helpinfo_url", jsonAppButton10.getString("btn10_helpinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn10_appinfo_url", jsonAppButton10.getString("btn10_appinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase, "btn10_appinfo_url_type", jsonAppButton10.getString("appinfo_type"));
                            }
                            JSONObject jsonAppButton11 = jsonObject.optJSONObject(Constants.UpdatableAppNameKey.button11.name());
                            if (jsonAppButton11 != null && jsonAppButton11.length() > 0) {
                                SharedPreferenceStorage.setStringValue(mBase, Constants.UpdatableAppNameKey.button11.name(), jsonAppButton11.getString("app_id"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn11_type", jsonAppButton11.getString("button_type"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn11_help_url", jsonAppButton11.getString("function_url"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn11_version",jsonAppButton11.getString("version"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn11_title", jsonAppButton11.getString("title"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn11_helpinfo_url", jsonAppButton11.getString("btn11_helpinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase,"btn11_appinfo_url", jsonAppButton11.getString("btn11_appinfo_url"));
                                SharedPreferenceStorage.setStringValue(mBase, "btn11_appinfo_url_type", jsonAppButton11.getString("appinfo_type"));
                            }



                            if (jsonObject.has(Constants.UpdatableAppNameKey.update_status.name()) && (jsonObject.optInt(Constants.UpdatableAppNameKey.update_status.name(), 1) > SharedPreferenceStorage.getIntegerValue(mBase, Constants.Storage.LAST_UPDATE_VERSION.name(), 0))) {
                                SharedPreferenceStorage.setIntegerValue(mBase, Constants.Storage.LAST_UPDATE_VERSION.name(), jsonObject.optInt(Constants.UpdatableAppNameKey.update_status.name(), 1));
                                isDownloadRequired = true;
                            }
                        }

                    }
                    if (mOnJSONLoadListener != null)
                        mOnJSONLoadListener.OnComplete(isPasswordFetch ? false : (isDownloadRequired || !isAllImageExistInStorage(mBase)), isInstantAppUpdateRequire);
                } catch (Exception e) {
                    mBase.removeLoading();
                    e.printStackTrace();
                    Constants.IS_FIRST_LAUNCH_SETTINGS_FETCH_DONE = false;
                    /*mBase.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mBase, "ERROR OCCURRED IN JSON DATA FETCH! PLEASE CONTACT SUPPORT.", Toast.LENGTH_SHORT).show();
                        }
                    });*/
                }
            }
        }).start();
    }

    public static boolean isAllImageExistInStorage(final BaseActivity mBase) {
        boolean isExists = false;
        try {
            isExists = ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_button0.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_button1.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_button2.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_button3.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_button4.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_button5.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_button6.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_button7.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_button8.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_button9.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_button10.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_button11.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_dock_button.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_pressed_button0.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_pressed_button1.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_pressed_button2.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_pressed_button3.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_pressed_button4.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_pressed_button5.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_pressed_button6.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_pressed_button7.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_pressed_button8.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_pressed_button9.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_pressed_button10.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_pressed_dock_button.name())
                    && ImageManager.isImageExists(mBase, Constants.UpdatableAppNameKey.icon_pressed_button11.name());
        } catch (Exception e) {

        }
        return isExists;
    }

    public static String convertStreamToString(InputStream is) throws UnsupportedEncodingException {

        BufferedReader reader = new BufferedReader(new
                InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private static JSONObject getTestData(BaseActivity mBase) {
        try {
            InputStream is = mBase.getAssets().open("gita.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new JSONObject(new String(buffer, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

