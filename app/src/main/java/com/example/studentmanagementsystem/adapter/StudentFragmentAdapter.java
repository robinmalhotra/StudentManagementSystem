package com.example.studentmanagementsystem.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.studentmanagementsystem.fragment.StudentAddFragment;
import com.example.studentmanagementsystem.fragment.StudentListFragment;

public class StudentFragmentAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;

    private String title[] = {"Student List","Add Student Details"};

    public StudentFragmentAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return new StudentListFragment();

            case 1:
                return new StudentAddFragment();

            default:
                return null;
        }



    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
