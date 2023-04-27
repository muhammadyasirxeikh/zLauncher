package com.zvision.zlaunchertwo.part;

import com.zvision.zlaunchertwo.base.Constants;

public class HelpInfoOrAdsButtonInfo {
    private String btnId = null, title = null, description = null, helpFileUrl = null;
    private int msg_update = 0, msg_frequency = 0;
    private Constants.FILE_TYPE mTutorialType = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHelpFileUrl() {
        return helpFileUrl;
    }

    public void setHelpFileUrl(String helpFileUrl) {
        this.helpFileUrl = helpFileUrl;
    }

    public String getBtnId() {
        return btnId;
    }

    public void setBtnId(String btnId) {
        this.btnId = btnId;
    }

    public Constants.FILE_TYPE getTutorialType() {
        return mTutorialType;
    }

    public void setTutorialType(Constants.FILE_TYPE mTutorialType) {
        this.mTutorialType = mTutorialType;
    }

    public int getMsg_update() {
        return msg_update;
    }

    public void setMsg_update(int msg_update) {
        this.msg_update = msg_update;
    }

    public int getMsg_frequency() {
        return msg_frequency;
    }

    public void setMsg_frequency(int msg_frequency) {
        this.msg_frequency = msg_frequency;
    }
}
