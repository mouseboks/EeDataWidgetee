package com.definedoutcomes.eedatawidget;

/**
 * Created by jamesm on 2017/11/29.
 */

public class EeUsageData {

    public String getPhoneData() {
        return phoneData;
    }

    public String getMifiData() {
        return mifiData;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    String phoneData = "??.?";
    String mifiData = "??.?";
    String updateTime = "??";

    public EeUsageData(String phoneData, String mifiData, String updateTime) {
        this.phoneData = phoneData;
        this.mifiData = mifiData;
        this.updateTime = updateTime;

    }
}
