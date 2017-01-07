package com.ouyang.peter.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by wtupc96 on 2017/1/3.
 */
public class formInformation extends AppCompatActivity {

    // 返回给MainActivity的标识号
    private static final int RES_CODE_0 = 0;

    // 三个RadioButton的id
    private static final int UNI_VERIFIED = 2131492957;
    private static final int LIBRARY = 2131492958;
    private static final int FLOW = 2131492959;
    // 选择RadioButton的标识
    private static final String USERNAME_FOR_UNI = "usernameForUni";
    private static final String PASSWORD_FOR_UNI = "passwordForUni";
    private static final String USERNAME_FOR_LIBRARY = "usernameForLibrary";
    private static final String PASSWORD_FOR_LIBRARY = "passwordForLibrary";
    private static final String USERNAME_FOR_FLOW = "usernameForFlow";
    private static final String PASSWORD_FOR_FLOW = "passwordForFlow";

    // 获取界面控件对象
    private static RadioGroup radioGroup;
    private static EditText username;
    private static EditText password;
    private static Button btn_forget;
    private static Button btn_submit;

    // 保存RadioButton的标识值
    private static String usernameString;
    private static String passwordString;
    private static int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化
        type = UNI_VERIFIED;
        usernameString = USERNAME_FOR_UNI;
        passwordString = PASSWORD_FOR_UNI;

        setContentView(R.layout.form_layout);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        btn_forget = (Button) findViewById(R.id.forgetInform);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        btn_submit = (Button) findViewById(R.id.submitInform);

        // RadioGroup设置状态改变监听器，根据所选的RadioButton来设置返回标识值
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (type = checkedId) {
                    case UNI_VERIFIED:
                        usernameString = USERNAME_FOR_UNI;
                        passwordString = PASSWORD_FOR_UNI;
                        return;
                    case LIBRARY:
                        usernameString = USERNAME_FOR_LIBRARY;
                        passwordString = PASSWORD_FOR_LIBRARY;
                        return;
                    case FLOW:
                        usernameString = USERNAME_FOR_FLOW;
                        passwordString = PASSWORD_FOR_FLOW;
                }
            }
        });

        // submit按钮监听器
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 确保用户名和密码都被填写
                if (username.getText().toString().equals("") || password.getText().toString().equals("")) {
                    Toast.makeText(formInformation.this, "请输入用户名和密码~", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 准备回传给MainActivity的数据包
                Bundle bundle = new Bundle();
                bundle.putString(usernameString, username.getText().toString());
                bundle.putString(passwordString, password.getText().toString());

                // 表明传回的值是针对哪个类型页面的
                bundle.putInt("type", type);

                Intent intent = new Intent();
                intent.putExtra("userInform", bundle);

                setResult(RES_CODE_0, intent);

                // 用户提示信息
                Toast.makeText(formInformation.this, "用户名和密码已保存~", Toast.LENGTH_SHORT).show();

                // 结束当前Activity
                finish();
            }
        });

        // 忘记密码按钮的监听器
        btn_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将传回Activity的数据置为“”即可达到重置效果
                Bundle bundle = new Bundle();
                bundle.putString(usernameString, "");
                bundle.putString(passwordString, "");
                bundle.putInt("type", type);

                Intent intent = new Intent();
                intent.putExtra("userInform", bundle);

                setResult(RES_CODE_0, intent);

                // 用户提示信息
                Toast.makeText(formInformation.this, "用户名和密码已清空~", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 按下返回键时结束当前Activity
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
            finish();
        return super.onKeyDown(keyCode, event);
    }
}
