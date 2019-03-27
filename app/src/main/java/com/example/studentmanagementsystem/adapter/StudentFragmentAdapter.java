package com.example.studentmanagementsystem.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.example.studentmanagementsystem.fragment.StudentAddFragment;
import com.example.studentmanagementsystem.fragment.StudentListFragment;

public class StudentFragmentAdapter extends FragmentPagerAdapter {

    private String title[] = {"Student List","Add Student Details"};
    FragmentManager ffm;

    public StudentFragmentAdapter(FragmentManager fm){
        super(fm);
        ffm=fm;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:


                FragmentTransaction ft=ffm.beginTransaction();
                ft.addToBackStack(null);
                StudentListFragment st = new StudentListFragment();
                ft.commit();

                return st;

            case 1:
                return new StudentAddFragment();

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
