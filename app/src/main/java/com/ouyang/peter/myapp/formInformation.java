package com.ouyang.peter.myapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private static EditText username;
    private static EditText password;
    private static Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_layout);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        btn_submit = (Button) findViewById(R.id.submitInform);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("") || password.getText().toString().equals("")) {
                    Toast.makeText(formInformation.this, "请输入用户名和密码~", Toast.LENGTH_SHORT).show();
                    return;
                }

//                try (FileWriter fileWriter = new FileWriter(new File("Information.peter"))) {
//                    fileWriter.write(username.getText().toString().toCharArray());
//                    fileWriter.write('\n');
//                    fileWriter.write(password.getText().toString().toCharArray());
//                    fileWriter.flush();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                try {
                    FileOutputStream fileOutputStream = openFileOutput("textFile.txt", MODE_PRIVATE);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

                    outputStreamWriter.write(username.getText().toString());
                    outputStreamWriter.write("\n");
                    outputStreamWriter.write(password.getText().toString());

                    outputStreamWriter.flush();
                    outputStreamWriter.close();

                    Toast.makeText(formInformation.this, "saved", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    Log.e("exception", "File not Found occurs~~~~~~~~~");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("exception", "IOException occurs~~~~~~~~~");
                    e.printStackTrace();
                }
            }
        });
    }

    private void informTest() {

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("Information.peter")))) {
            String dataLine;
            while ((dataLine = bufferedReader.readLine()) != null)
                Log.e("msg", dataLine);
        } catch (FileNotFoundException e) {
            Log.e("exception", "File not Found occurs~~~~~~~~~");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("exception", "IOException occurs~~~~~~~~~");
            e.printStackTrace();
        }
    }
}
