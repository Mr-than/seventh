package com.example.seventhtest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class Activity2 extends AppCompatActivity {

    EditText editText1, editText2;
    Button button1, button2, button3;
    String use, pwd;
    MyHandler myHandler;
    SharedPreferences sf;
    SharedPreferences.Editor se;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);


        //sp初始化
        sf = getSharedPreferences("cookies", MODE_PRIVATE);
        se = getSharedPreferences("cookies", MODE_PRIVATE).edit();


        editText1 = findViewById(R.id.edit_text1);
        editText2 = findViewById(R.id.edit_text2);
        button1 = findViewById(R.id.signIn);
        button2 = findViewById(R.id.signUp);
        button3 = findViewById(R.id.button999);
        myHandler = new MyHandler();

        //登录
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText1.getText().toString().length() > 0 && editText2.getText().toString().length() > 0) {

                    putSomething("https://www.wanandroid.com/user/login");
                } else {
                    Toast.makeText(Activity2.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //注册
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity2.this, ActivitySignUp.class);
                startActivityForResult(intent, 1);
            }
        });

        //返回个人信息
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                putSomething("https://wanandroid.com//user/lg/userinfo/json");


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("22222", "233");

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String[] s = data.getStringExtra("data233").split("`");
                    String a = s[0];
                    String b = s[1];

                    editText1.setText(a);
                    editText2.setText(b);
                }
                break;
            default:
                break;

        }
    }


    private void putSomething(String url) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL u = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) u.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    String temp = sf.getString("cookie", "").toString();
                    connection.setRequestProperty("Cookie", temp);


                    StringBuilder username = new StringBuilder();
                    StringBuilder password = new StringBuilder();

                    use = editText1.getText().toString();
                    pwd = editText2.getText().toString();

                    username.append("username").append("=").append(use).append("&");
                    password.append("password").append("=").append(pwd);

                    StringBuilder sum = new StringBuilder();
                    sum.append(username).append(password);

                    connection.connect();
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.write(sum.toString().getBytes(StandardCharsets.UTF_8));


                    InputStream in = connection.getInputStream();
                    String back = makeString(in);


                    Message message = new Message();
                    message.obj = back;

                    myHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    public String makeString(InputStream in) {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));
        try {

            while ((line = bf.readLine()) != null) {
                sb.append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return sb.toString();
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            outMsg(msg.obj.toString());
        }
    }

    public void outMsg(String msg) {
        if (msg.length() == 0) {
            Toast.makeText(this, "登录好像成功了！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

}