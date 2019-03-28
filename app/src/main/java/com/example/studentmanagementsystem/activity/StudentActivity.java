package com.example.studentmanagementsystem.activity;

import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.adapter.StudentFragmentAdapter;
import com.example.studentmanagementsystem.communicator.Communicator;
import com.example.studentmanagementsystem.fragment.StudentAddFragment;
import com.example.studentmanagementsystem.fragment.StudentListFragment;
import com.example.studentmanagementsystem.util.SortByName;
import com.example.studentmanagementsystem.util.SortByRoll;

import java.util.Collections;


public class StudentActivity extends AppCompatActivity implements Communicator {


    private ViewPager viewPager;
    private StudentFragmentAdapter fragmentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        fragmentAdapter = new StudentFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);

        //to add tabLayout in view pager
        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);


//  }
//    private void clearNextFragment(){
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//                String tag =getString(R.string.tag)+R.id.view_pager+":"+1;
//                StudentAddFragment addStudentFragment = (StudentAddFragment) getSupportFragmentManager().findFragmentByTag(tag);
//              if(addStudentFragment!=null){
//                          addStudentFragment.clearDetails();
//                      }
//
//            }
//        });
//    }
    }

    public void changeTab(){
        if(viewPager.getCurrentItem()==0){
            viewPager.setCurrentItem(1);
        }else if(viewPager.getCurrentItem()==1){
            viewPager.setCurrentItem(0);
        }
    }

    @Override
    public void communicateAdd(Bundle bundle) {

        String tag =getString(R.string.tag)+R.id.view_pager+":"+0;
        StudentListFragment studentListFragment = (StudentListFragment) getSupportFragmentManager().findFragmentByTag(tag);
        studentListFragment.addStudent(bundle);
        changeTab();
    }

    @Override
    public void communicateUpdate(Bundle bundle) {
        String tag =getString(R.string.tag)+R.id.view_pager+":"+1;
        StudentAddFragment addStudentFragment = (StudentAddFragment) getSupportFragmentManager().findFragmentByTag(tag);
        addStudentFragment.updateStudent(bundle);
        changeTab();
    }



    @Override
    public void onBackPressed() {

        if(viewPager.getCurrentItem()>=1){
            viewPager.setCurrentItem(viewPager.getCurrentItem()-1);

        }
        else {
            super.onBackPressed();
            Toast.makeText(this,"Press Back Again To Exit",Toast.LENGTH_SHORT).show();
        }

    }
}
