package com.jiesong.myapplicationlist;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jie.song on 2018/6/19.
 */

public class AppListActivity extends AppCompatActivity {

    private ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_app);
        listView = findViewById(R.id.appList);
        getAppList();
        AppAdapter adapter = new AppAdapter(appList);
        listView.setAdapter(adapter);
    }

    /**
     * 获取非系统应用信息列表
     */
    private void getAppList() {
        PackageManager pm = this.getPackageManager();
        // Return a List of all packages that are installed on the device.
        List<PackageInfo> packages = pm.getInstalledPackages(0);

        for (PackageInfo packageInfo : packages) {
            // 判断系统/非系统应用
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) // 非系统应用
            {
                AppInfo info = new AppInfo();
                info.appName = packageInfo.applicationInfo.loadLabel(pm)
                        .toString();
                info.pkgName = packageInfo.packageName;
                info.appIcon = packageInfo.applicationInfo.loadIcon(pm);
                appList.add(info);
            } else {
                // 系统应用　　　　　　　　
            }
        }
    }


    class AppAdapter extends BaseAdapter {

        ArrayList<AppInfo> appInfos;

        public AppAdapter(ArrayList<AppInfo> appInfos) {
            this.appInfos = appInfos;
        }


        @Override
        public int getCount() {
            return appInfos.size();
        }

        @Override
        public Object getItem(int i) {
            return appInfos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder mViewHolder;
            final AppInfo appInfo = appInfos.get(i);
            if (view == null) {
                mViewHolder = new ViewHolder();
                view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_app_info, null);
                mViewHolder.iv_app_icon = (ImageView) view.findViewById(R.id.iv_app_icon);
                mViewHolder.tx_app_name = (TextView) view.findViewById(R.id.tv_app_name);
                mViewHolder.tv_package_name = view.findViewById(R.id.tv_package_name);
                mViewHolder.btn_disable = view.findViewById(R.id.btn_disable);
                mViewHolder.btn_enable = view.findViewById(R.id.btn_enable);
                view.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) view.getTag();
            }
            mViewHolder.iv_app_icon.setImageDrawable(appInfo.getAppIcon());
            mViewHolder.tx_app_name.setText(appInfo.getAppName());
            mViewHolder.tv_package_name.setText(appInfo.getPkgName());
            mViewHolder.btn_disable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SystemManager.execRootCmdSilent("pm disable " + appInfo.pkgName);
                }
            });
            mViewHolder.btn_enable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SystemManager.execRootCmdSilent("pm enable " + appInfo.getPkgName());
                }
            });
            return view;
        }

        class ViewHolder {

            ImageView iv_app_icon;
            TextView tx_app_name;
            TextView tv_package_name;
            Button btn_disable;
            Button btn_enable;
        }
    }


}
