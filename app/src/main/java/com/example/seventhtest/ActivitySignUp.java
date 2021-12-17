package com.example.seventhtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.DialogPreference;
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

public class ActivitySignUp extends AppCompatActivity {

    Button button;
    EditText editText1,editText2,editText3;
    NewHandler newHandler;
    SharedPreferences sf;
    SharedPreferences.Editor se;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        //按钮
        button=findViewById(R.id.button666);

        //输入框
        editText1=findViewById(R.id.edit_text7);
        editText2=findViewById(R.id.edit_text8);
        editText3=findViewById(R.id.edit_text9);
        //线程信息交互

        //sp初始化
        sf=getSharedPreferences("cookies",MODE_PRIVATE);
        se=getSharedPreferences("cookies",MODE_PRIVATE).edit();



        newHandler=new NewHandler();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(editText1.getText().toString().length()>0&&editText2.getText().toString().length()>0
                        &&editText3.getText().toString().length()>0
                        &&(editText2.getText().toString().equals(editText3.getText().toString()))) {
                    putSomething("https://www.wanandroid.com/user/register");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            finish();
                        }
                    }).start();
                }else{
                    Toast.makeText(ActivitySignUp.this, "输入格式有问题！", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void putSomething(String url){

        new Thread(new Runnable() {
            @Override
            public void run() {


        try{

            URL u=new URL(url);
            HttpURLConnection connection=(HttpURLConnection) u.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setDoOutput(true);
            connection.setDoInput(true);


            //处理一下要post的信息
            StringBuilder use=new StringBuilder();
            StringBuilder pwd=new StringBuilder();
            StringBuilder rePwd=new StringBuilder();
            StringBuilder sum=new StringBuilder();

            use.append("username").append("=").append(editText1.getText().toString()).append("&");
            pwd.append("password").append("=").append(editText2.getText().toString()).append("&");
            rePwd.append("repassword").append("=").append(editText3.getText().toString());
            sum.append(use).append(pwd).append(rePwd);

            DataOutputStream dataOutputStream=new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write(sum.toString().getBytes(StandardCharsets.UTF_8));

            //下面得到cookie并将cookie存起来
            Map<String,List<String>> cookies = connection.getHeaderFields();
            List<String> setCookies = cookies.get("Set-Cookie");


            StringBuilder s=new StringBuilder();
            for (String key:setCookies) {
                s.append(key).append("; ");
            }

            String ss=s.toString().substring(0,s.length()-1);
            se.putString("cookie",ss);
            se.apply();
            //下面获取登录后的反馈信息
            InputStream in=connection.getInputStream();

            String back=ts(in);

            JSONObject jsonObject=new JSONObject(back);
            String temp=jsonObject.getString("errorMsg");

            Message message=new Message();
            message.obj=temp;

            newHandler.sendMessage(message);


        }catch (Exception e){
            e.printStackTrace();
        }

            }
        }).start();

    }

    public String ts(InputStream in){
        StringBuilder te=new StringBuilder();
        try {

            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(in));

            String line;

            while((line=bufferedReader.readLine())!=null){
                te.append(line);
            }

        }catch (Exception e){
         e.printStackTrace();
        }

        return te.toString();
    }

    class NewHandler extends Handler{

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            set(msg.obj.toString());
        }
    }

    public void set(String msg) {

        if (msg.length() == 0) {

            String user=editText1.getText().toString();
            String pwd=editText2.getText().toString();
            String sum=user+"`"+pwd;

            Intent intent=new Intent();
            intent.putExtra("data233",sum);
            setResult(RESULT_OK,intent);

         Toast.makeText(ActivitySignUp.this, "好像注册成功了呢！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }
}