package com.example.lichang.casualchat;

import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.lichang.casualchat.db.CCUser;
import com.example.lichang.casualchat.db.Myinfo;
import com.example.lichang.casualchat.db.UserDao;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lichang on 2017/10/26.
 */

public class CCApplication extends Application{

    private Map<String, CCUser> contactList = null;
    private UserDao userDao;
    private String username = "";
    private static CCApplication instance;
    public void onCreate(){
        super.onCreate();
        initCasual();
    }

    public static CCApplication getInstance() {
        return instance;
    }

    private void initCasual(){
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null ||!processAppName.equalsIgnoreCase(this.getPackageName())) {
            Log.e("---", "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }


        options.setAutoLogin(false);  //自动登录
        //初始化
        EMClient.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);

    }

    public String getCurrentUserName() {
        if (TextUtils.isEmpty(username)) {
            username = Myinfo.getInstance(instance).getUserInfo(Constant.KEY_USERNAME);

        }
        return username;

    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }


    public Map<String, CCUser> getContactList() {

        if (contactList == null) {

        //    contactList = userDao.getContactList();

        }
        return contactList;

    }

    public void setContactList(Map<String, CCUser> contactList) {

        this.contactList = contactList;

        userDao.saveContactList(new ArrayList<CCUser>(contactList.values()));

    }

    public void setCurrentUserName(String username) {
        this.username = username;
        Myinfo.getInstance(instance).setUserInfo(Constant.KEY_USERNAME, username);
    }

    /**
     * 退出登录
     *
     * @param unbindDeviceToken
     *            是否解绑设备token(使用GCM才有)
     * @param callback
     *            callback
     */
    public void logout(boolean unbindDeviceToken, final EMCallBack callback) {

        EMClient.getInstance().logout(unbindDeviceToken, new EMCallBack() {

            @Override
            public void onSuccess() {

                if (callback != null) {
                    callback.onSuccess();
                }

            }

            @Override
            public void onProgress(int progress, String status) {
                if (callback != null) {
                    callback.onProgress(progress, status);
                }
            }

            @Override
            public void onError(int code, String error) {

                if (callback != null) {
                    callback.onError(code, error);
                }
            }
        });
    }

}
