package com.example.seventhtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Activity3 extends AppCompatActivity {


    ViewPager2 viewPager2;
    List<Fragment> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        list=new ArrayList<>();
        list.add(new Fragment001());
        list.add(new Fragment002());
        list.add(new Fragment003());

        viewPager2=findViewById(R.id.view_page);

        ImgAdapter i=new ImgAdapter(this,list);

        viewPager2.setAdapter(i);


    }
}