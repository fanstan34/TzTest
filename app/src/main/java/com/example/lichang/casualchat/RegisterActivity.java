package com.example.lichang.casualchat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class RegisterActivity extends Activity {

    private EditText userName;
    private EditText userPass;
    private EditText Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = (EditText)findViewById(R.id.r_userName);
        userPass = (EditText)findViewById(R.id.r_userPass);
        Password = (EditText)findViewById(R.id.r_Password);

        //注册按钮点击事件
       findViewById(R.id.bt_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        //返回点击事件
        findViewById(R.id.return_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }

        });


    }
   public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            intent = new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);
            this.finish();
        }
        return super.onKeyDown(keyCode,event);
    }

     /*
    * 注册方法
    * */
    private void register(){

        final String name = userName.getText().toString().trim();
        final String password = userPass.getText().toString().trim();
        String confirm_pwd = Password.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            userName.requestFocus();
            return;
        }else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this,"密码不能为空", Toast.LENGTH_SHORT).show();
            userPass.requestFocus();
            return;
        } else if (TextUtils.isEmpty(confirm_pwd)) {
            Toast.makeText(this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
            Password.requestFocus();
            return;
        } else if (!password.equals(confirm_pwd)) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)){
        //    final ProgressDialog pd = new ProgressDialog(this);
        //    pd.setMessage(getResources().getString(R.string.Is_the_registered));
        //    pd.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //调用SDK注册方法
                        EMClient.getInstance().createAccount(name,password);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            //    if (!RegisterActivity.this.isFinishing())
                               //     pd.dismiss();
                                //保存用户名
//                                CCApplication.getInstance().setCurrentUserName(name);
                              //  Toast.makeText(getApplicationContext(),getResources().getString(R.string.Registered_successfully),0).show();
                                Log.e("---","注册成功");
                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                finish();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        Log.e("---","注册失败"+e.getErrorCode() + "," + e.getMessage());
                        Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }).start();
        }

    }

}
