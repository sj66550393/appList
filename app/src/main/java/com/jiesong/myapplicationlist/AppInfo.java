package com.jiesong.myapplicationlist;

import android.content.Intent;
import android.graphics.drawable.Drawable;

public class AppInfo {
	public String pkgName;
	public String appName;
	public Drawable appIcon;
//	public Intent appIntent;


	public String getPkgName() {
		return pkgName;
	}

	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}
}
