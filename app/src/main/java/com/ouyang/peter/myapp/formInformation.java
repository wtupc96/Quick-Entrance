package com.ouyang.peter.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by wtupc96 on 2017/1/3.
 */
public class formInformation extends AppCompatActivity {

    private static final int RES_CODE_0 = 0;

    private static final int UNI_VERIFIED = 2131492957;
    private static final int LIBRARY = 2131492958;
    private static final int FLOW = 2131492959;
    private static final String USERNAMEFORUNI = "usernameForUni";
    private static final String PASSWORDFORUNI = "passwordForUni";
    private static final String USERNAMEFORLIBRARY = "usernameForLibrary";
    private static final String PASSWORDFORLIBRARY = "passwordForLibrary";
    private static final String USERNAMEFORFLOW = "usernameForFlow";
    private static final String PASSWORDFORFLOW = "passwordForFlow";

    private static RadioGroup radioGroup;
    private static EditText username;
    private static EditText password;
    private static Button btn_forget;
    private static Button btn_submit;

    private static String usernameString;
    private static String passwordString;
    private static int type = UNI_VERIFIED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_layout);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        btn_forget = (Button) findViewById(R.id.forgetInform);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        btn_submit = (Button) findViewById(R.id.submitInform);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (type = checkedId) {
                    case UNI_VERIFIED:
                        usernameString = USERNAMEFORUNI;
                        passwordString = PASSWORDFORUNI;
                        return;
                    case LIBRARY:
                        usernameString = USERNAMEFORLIBRARY;
                        passwordString = PASSWORDFORLIBRARY;
                        return;
                    case FLOW:
                        usernameString = USERNAMEFORFLOW;
                        passwordString = PASSWORDFORFLOW;
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("") || password.getText().toString().equals("")) {
                    Toast.makeText(formInformation.this, "请输入用户名和密码~", Toast.LENGTH_SHORT).show();
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putString(usernameString, username.getText().toString());
                bundle.putString(passwordString, password.getText().toString());

                Log.e("tttt", String.valueOf(type));

                bundle.putInt("type", type);

                Intent intent = new Intent();
                intent.putExtra("userInform", bundle);

                setResult(RES_CODE_0, intent);

                Toast.makeText(formInformation.this, "用户名和密码已保存~", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        btn_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("yyyy", String.valueOf(type));

                Bundle bundle = new Bundle();
                bundle.putString(usernameString, "");
                bundle.putString(passwordString, "");
                bundle.putInt("type", type);

                Intent intent = new Intent();
                intent.putExtra("userInform", bundle);

                setResult(RES_CODE_0, intent);

                Toast.makeText(formInformation.this, "用户名和密码已清空~", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
            finish();
        return super.onKeyDown(keyCode, event);
    }
}
