package com.jiesong.myapplicationlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
    private Button btn_app;
    private Button btn_list;
    private Button btn_usb;
    HashMap<String , String> map;
    HashMap<String , String> reflectMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        SystemManager.execRootCmdSilent("chmod 777 " + pkgCodePath);
//        Runtime.getRuntime().exec(new String[]{"/system/bin/su","-c", cmd});
        map = new HashMap<>();
        reflectMap = new HashMap<>();
        reflectMap.put("管家金服投资理财", "管家金服");
        reflectMap.put("微米在线" , "微米");
        reflectMap.put("亲宝宝" , "亲亲宝育儿");
        reflectMap.put("至尊炸金花" , "超级诈金花");
        reflectMap.put("TOPLIFE" , "toplife");
        reflectMap.put("幸运中彩票" , "幸运彩票");
        reflectMap.put("酷狗直播" , "繁星直播");
        reflectMap.put("长龙捕鱼" , "辰龙捕鱼");
        reflectMap.put("Hello语音交友" , "hello语音交友");
        reflectMap.put("超级WiFi" , "超级wifi");
        reflectMap.put("爱贷网理财" , "爱贷网");
        reflectMap.put("有个表情" , "表情me");
        reflectMap.put("双开小助手" , "微信双开助手");
        reflectMap.put("超级拼拼乐" , "超级诈金花");
        reflectMap.put("赚客-手机赚钱" , "赚客");
        reflectMap.put("德国自驾购物" , "德国自驾游-次日签到价格更高");

        btn_app = findViewById(R.id.btn_app);
        btn_list = findViewById(R.id.btn_list);
        btn_usb = findViewById(R.id.btn_usb);
        btn_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAppList();
                writeToFile(new Gson().toJson(map));
                for(int i = 0;i < appList.size();i++){
                    Log.e("songjie" , "appName = " + appList.get(i).appName + "              packageName = " + appList.get(i).pkgName);
                }
            }
        });
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , AppListActivity.class);
                startActivity(intent);
            }
        });
        btn_usb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean enableAdb = (Settings.Secure.getInt(getContentResolver(), Settings.Secure.ADB_ENABLED, 0) > 0);
                if(enableAdb){
                    Toast.makeText(MainActivity.this, "usb enable", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(MainActivity.this, "not enable", Toast.LENGTH_LONG).show();
                }
            }
        });
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
//                info.appIcon = packageInfo.applicationInfo.loadIcon(pm);
//                // 获取该应用安装包的Intent，用于启动该应用
//                info.appIntent = pm.getLaunchIntentForPackage(packageInfo.packageName);
                map.put(info.appName.trim() , info.pkgName);
                if(reflectMap.get(info.appName) != null) {
                    map.put(reflectMap.get(info.appName) , info.pkgName);
                }
                appList.add(info);
            } else {
                // 系统应用　　　　　　　　
            }
        }
        map.put("360手机安全卫士" , "com.qihoo360.mobilesafe");
        map.put("Hello语音交友1" , "com.yy.huanju");
        map.put("园园live" , "sg.bigo.xhalo");
        map.put("搜狐新闻资讯版" , "com.sohu.newsclient");
        map.put("东方航空1" , "com.rytong.ceair");
        map.put("2345阅读王" , "com.book2345.reader");
        map.put("荷包理财" , "com.hebao.app");
        map.put("京东商城" , "com.jingdong.app.mall");
        map.put("七猫精品小说" ,"com.book2345.reader");
        map.put("B612" , "com.campmobile.snowcamera");
        com.campmobile.snowcamera
        map.put("予财缘" , "com.gsoc.yucaiyuan");
    }

    /**
     * 获取渠道名
     * @param ctx 此处习惯性的设置为activity，实际上context就可以
     * @return 如果没有获取成功，那么返回值为空
     */
    public String getChannelName(Activity ctx , String packageName) {
        if (ctx == null) {
            return null;
        }
        String channelName = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = applicationInfo.metaData.getString("UMENG_CHANNEL");
                        if(channelName == null){
                        }
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelName;
    }

    /**
     * 获取application中指定的meta-data
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }
    private void writeToFile(String str){
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/appInfo.txt");
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.println(str);// 往文件里写入字符串
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
