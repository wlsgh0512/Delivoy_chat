package com.example.testcode;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 친구 / 채팅 탭 선택에 따른 Fragment가 보여지도록.
 *
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs; //탭의 갯수
    Frag1 tab1;
    Frag2 tab2;

    public ViewPagerAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        this.mNumOfTabs = numTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                tab1 = new Frag1();
                return tab1;
            case 1:
                tab2 = new Frag2();
                return tab2;
            default:
                return null;
        }
        //return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}