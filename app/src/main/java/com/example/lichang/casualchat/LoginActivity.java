package com.example.lichang.casualchat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lichang.casualchat.db.CCUser;
//import com.example.lichang.casualchat.db.DBManager;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends Activity {

    private EditText userName;
    private EditText userPass;

    private boolean autoLogin = false;
    private boolean progressShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        // 如果登录成功过，直接进入主页面
        if(EMClient.getInstance().isLoggedInBefore()){
            autoLogin = true;
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            return;
        }

        setContentView(R.layout.activity_login);

        userName = (EditText) findViewById(R.id.userName);
        userPass = (EditText) findViewById(R.id.userPass);

        //登录按钮点击事件
        findViewById(R.id.bt_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        //注册点击事件
        findViewById(R.id.l_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });

    }

    /*
   * 登录方法
   * */
    private void login(){
        final String name = userName.getText().toString().trim();
        final String password =  userPass.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this,"密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        progressShow = true;
      //  final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
      //  pd.setCanceledOnTouchOutside(false);
      /*  pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
       //         Log.d("LoginActivity","EMClient.getInstance().onCancel");
                progressShow = false;
            }
        });
      //  pd.setMessage(getString(R.string.Is_landing));
      //  pd.show();*/
//        DBManager.getInstance().closeDB();
    //    CCApplication.getInstance().setCurrentUserName(name);

        EMClient.getInstance().login(name,password, new EMCallBack() {
            @Override
            public void onSuccess() {
                // Toast.makeText(this,"登录成功" , Toast.LENGTH_SHORT).show();

              /*  if (!LoginActivity.this.isFinishing() && pd.isShowing()){
                    pd.dismiss();
                }*/
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
              //  getFriends();

                //进入主页面
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
                }

                @Override
                public void onError(int i, String s) {
//                    Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();

                    Log.e("---","登录失败," + i + "," + s);
                    if (!progressShow){
                        return;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          //  pd.dismiss();

                        }
                    });
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });


    }
/*
    private void getFriends(){
        try {
            List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
            Map<String,CCUser> users = new HashMap<String, CCUser>();
            for (String username:usernames){
                CCUser user = new CCUser(username);
                users.put(username,user);
            }

            CCApplication.getInstance().setContactList(users);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (autoLogin){
            return;
        }
    }
    */
}
