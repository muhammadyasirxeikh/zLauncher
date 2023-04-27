package com.zvision.zlaunchertwo.support;

import android.app.Application;

import com.zvision.zlaunchertwo.part.HelpInfoOrAdsButtonInfo;
import com.zvision.zlaunchertwo.part.RssFeedInfo;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class ApplicationStorage extends Application {
    private boolean inittask = false;
    private boolean finishApp = false;
    private ArrayList<String> homePageDockAppPackageList = new ArrayList<>();
    private ArrayList<HelpInfoOrAdsButtonInfo> mHelpInfoInfoArrayList = new ArrayList<>();
    private HashMap<String, HelpInfoOrAdsButtonInfo> mAdsButtonInfoInfoHashMap = new HashMap<>();
    private RssFeedInfo mHtmlRssFeedInfo = new RssFeedInfo();
    private ArrayList<String> newInstantAppUpdateLinkList = new ArrayList<>();
    private JSONArray hiddenAppsIdListByServer = new JSONArray();
    private JSONArray deleteAppsIdListByServer = new JSONArray();
    private JSONArray deleteAppsIdListByServerDuringAutoUpdate = new JSONArray();
    private HelpInfoOrAdsButtonInfo mStartUpMessageDetails = new HelpInfoOrAdsButtonInfo();

    private static ApplicationStorage mApplicationStorage;

    public static ApplicationStorage getInstance() {
        if (mApplicationStorage == null)
            mApplicationStorage = new ApplicationStorage();
        return mApplicationStorage;
    }

    public void onCreate() {
        super.onCreate();
        mApplicationStorage = this;
    }

    public void onTerminate() {
        super.onTerminate();
        inittask = false;
        finishApp = false;
    }

    public boolean isinittask() {
        return inittask;
    }

    public void setinittask(boolean isf) {
        inittask = isf;
    }

    public boolean isFinishApp() {
        return finishApp;
    }

    public void setFinishApp(boolean isf) {
        finishApp = isf;
    }

    public ArrayList<String> getHomePageDockAppList() {
        return homePageDockAppPackageList;
    }

    public ArrayList<HelpInfoOrAdsButtonInfo> getHelpInfoInfoArrayList() {
        return mHelpInfoInfoArrayList;
    }

    public HashMap<String, HelpInfoOrAdsButtonInfo> getAdsButtonInfoInfoHashMap() {
        return mAdsButtonInfoInfoHashMap;
    }

    public RssFeedInfo getHtmlRssFeedInfo() {
        return mHtmlRssFeedInfo;
    }

    public void setHtmlRssFeedInfo(RssFeedInfo htmlRssFeedInfo) {
        this.mHtmlRssFeedInfo = htmlRssFeedInfo;
    }

    public ArrayList<String> getNewInstantAppUpdateLinkList() {
        return newInstantAppUpdateLinkList;
    }

    public void setNewInstantAppUpdateLinkList(ArrayList<String> newInstantAppUpdateLinkList) {
        this.newInstantAppUpdateLinkList = newInstantAppUpdateLinkList;
    }

    public JSONArray getHiddenAppsIdListByServer() {
        return hiddenAppsIdListByServer;
    }

    public void setHiddenAppsIdListByServer(JSONArray hiddenAppsIdListByServer) {
        this.hiddenAppsIdListByServer = hiddenAppsIdListByServer;
    }

    public HelpInfoOrAdsButtonInfo getStartUpMessageDetails() {
        return mStartUpMessageDetails;
    }

    public void setStartUpMessageDetails(HelpInfoOrAdsButtonInfo startUpMessageDetails) {
        this.mStartUpMessageDetails = startUpMessageDetails;
    }

    public JSONArray getDeleteAppsIdListByServer() {
        return deleteAppsIdListByServer;
    }

    public void setDeleteAppsIdListByServer(JSONArray deleteAppsIdListByServer) {
        this.deleteAppsIdListByServer = deleteAppsIdListByServer;
    }

    public JSONArray getDeleteAppsIdListByServerDuringAutoUpdate() {
        return deleteAppsIdListByServerDuringAutoUpdate;
    }

    public void setDeleteAppsIdListByServerDuringAutoUpdate(JSONArray deleteAppsIdListByServerDuringAutoUpdate) {
        this.deleteAppsIdListByServerDuringAutoUpdate = deleteAppsIdListByServerDuringAutoUpdate;
    }
}
