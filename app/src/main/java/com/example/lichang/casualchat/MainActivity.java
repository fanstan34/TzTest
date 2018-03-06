package com.example.lichang.casualchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lichang.casualchat.db.CCUser;
import com.example.lichang.casualchat.db.InviteMessage;
//import com.example.lichang.casualchat.db.InviteMessgeDao;
import com.example.lichang.casualchat.db.UserDao;
import com.hjm.bottomtabbar.BottomTabBar;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

 //   private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;
    private BottomTabBar bottomTabBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomTabBar = (BottomTabBar)findViewById(R.id.bottomtab);
        bottomTabBar.init(getSupportFragmentManager())
                .setImgSize(50,50)
                .setFontSize(8)
                .setTabPadding(4,6,10)
                .setChangeColor(Color.DKGRAY,Color.RED)
                .addTabItem("消息",R.mipmap.ic_launcher,NewsFragment.class)
                .addTabItem("联系人",R.mipmap.ic_launcher,ContactFragment.class)
                .addTabItem("我",R.mipmap.ic_launcher,MyFragment.class)
                .setTabBarBackgroundColor(Color.BLACK);

     //   inviteMessgeDao = new InviteMessgeDao(MainActivity.this);
        userDao = new UserDao(MainActivity.this);
        //注册联系人变动监听
     //   EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
    }

    /***
     * 好友变化listener
     *
     */
    /*
    public class MyContactListener implements EMContactListener {

        @Override
        public void onContactAdded(final String username) {
            // 保存增加的联系人
            Map<String, CCUser> localUsers = CCApplication.getInstance().getContactList();
            Map<String, CCUser> toAddUsers = new HashMap<String, CCUser>();
            CCUser user = new CCUser(username);
            // 添加好友时可能会回调added方法两次
            if (!localUsers.containsKey(username)) {
                userDao.saveContact(user);
            }
            toAddUsers.put(username, user);
            localUsers.putAll(toAddUsers);
            runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "增加联系人：+"+username, Toast.LENGTH_SHORT).show();
                }


            });


        }

        @Override
        public void onContactDeleted(final String username) {
            // 被删除
            Map<String, CCUser> localUsers = CCApplication.getInstance().getContactList();
            localUsers.remove(username);
            userDao.deleteContact(username);
          //  inviteMessgeDao.deleteMessage(username);

            runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "删除联系人：+"+username, Toast.LENGTH_SHORT).show();
                }


            });

        }

        @Override
        public void onContactInvited(final String username, String reason) {
            // 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
         //   List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();

       //     for (InviteMessage inviteMessage : msgs) {
        //        if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
        //            inviteMessgeDao.deleteMessage(username);
                }
            }
            // 自己封装的javabean
            InviteMessage msg = new InviteMessage();
       //     msg.setFrom(username);
       //     msg.setTime(System.currentTimeMillis());
       //     msg.setReason(reason);

            // 设置相应status
      //      msg.setStatus(InviteMessage.InviteMesageStatus.BEINVITEED);
      //      notifyNewIviteMessage(msg);
      //      runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "收到好友申请：+"+username, Toast.LENGTH_SHORT).show();
                }


            });

        }

        @Override
        public void onFriendRequestAccepted(String s) {

        }

        @Override
        public void onFriendRequestDeclined(String s) {

        }


        // @Override
        public void onContactAgreed(final String username) {
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getFrom().equals(username)) {
                    return;
                }
            }
            // 自己封装的javabean
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());

            msg.setStatus(InviteMessage.InviteMesageStatus.BEAGREED);
            notifyNewIviteMessage(msg);
            runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "好友申请同意：+"+username, Toast.LENGTH_SHORT).show();
                }


            });

        }


    }
    /**
     * 保存并提示消息的邀请消息
     * @param msg
     */
    /*
    private void notifyNewIviteMessage(InviteMessage msg){
        if(inviteMessgeDao == null){
            inviteMessgeDao = new InviteMessgeDao(MainActivity.this);
        }
        inviteMessgeDao.saveMessage(msg);
        //保存未读数，这里没有精确计算
        inviteMessgeDao.saveUnreadMessageCount(1);
        // 提示有新消息
        //响铃或其他操作
    }
*/

}
