//package com.example.testcode;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.View;
//
//import androidx.fragment.app.FragmentManager;
//import androidx.viewpager.widget.ViewPager;
//
//import com.google.android.material.tabs.TabLayout;
//
//public class Chat_list_Activity extends Activity {
//
//    ViewPager viewpager;
//    TabLayout tabs;
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.friends_list);
//
//        viewpager = findViewById(R.id.viewpager);
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        viewpager.setAdapter(adapter);
//
//        tabs = (TabLayout) findViewById(R.id.tabs);
//        tabs.setupWithViewPager(viewpager);
//    }
//
//
//}
