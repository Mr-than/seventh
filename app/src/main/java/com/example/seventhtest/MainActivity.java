package com.example.seventhtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    List<Msg> list;
    private Handler mHandler;
    HttpURLConnection httpURLConnection;
    String[] data;
    Button button,button1,button2;
    TextView textView;
    RecyclerView recyclerView;
    MyAdapter adapter;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.button1);
        button1=findViewById(R.id.button2);
        button2=findViewById(R.id.button3);
        textView=findViewById(R.id.text_view);
        list=new ArrayList<>();
        recyclerView=findViewById(R.id.recycler_view);
        intent=new Intent(MainActivity.this,Activity2.class);


        mHandler=new MyHandler();
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        getSomething("https://www.wanandroid.com//hotkey/json");




      button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              getSomething("https://www.wanandroid.com//hotkey/json");
              recyclerView.setLayoutManager(layoutManager);
              adapter=new MyAdapter(list);
              recyclerView.setAdapter(adapter);
              Toast.makeText(MainActivity.this, "请求数据已刷新", Toast.LENGTH_SHORT).show();
          }
      });

      button1.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              startActivity(intent);
          }
      });




      button2.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent=new Intent(MainActivity.this,Activity3.class);
            startActivity(intent);
          }
      });

    }

    public void getSomething(String Url){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url =new URL(Url);
                    httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(8000);
                    httpURLConnection.setReadTimeout(8000);

                    InputStream in =httpURLConnection.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder msg=new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        msg.append(line).append("\n");
                    }

                    Message message=new Message();
                    message.obj=msg;
                    mHandler.sendMessage(message);

                }catch(Exception e){
                    e.printStackTrace();
                }finally {
                    httpURLConnection.disconnect();
                }


            }
        }).start();

    }


    public class MyHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

           String temp=msg.obj.toString();
           t(temp);
        }
    }

    public void t(String temp){

        try{

            JSONObject jsonObject=new JSONObject(temp);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            data=new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject j=jsonArray.getJSONObject(i);
                data[i]=j.getString("name");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        for (int i = 0; i < data.length; i++) {
            list.add(new Msg(data[i]));
        }

    }
}