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
    private final List<Fragment> fragments = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                return fragments.get(0);
            case 1:
                return fragments.get(1);
            default:
                return null;
        }
    }

    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }


        @Override
    public int getCount() {
        return fragments.size();
    }
}