package com.example.studentmanagementsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity  {

    private static final int REQUEST_CODE_MAKE_STUDENT=0;

    //Create an ArrayList to save the data.
    public static ArrayList<StudentTemplate> mStudentList = new ArrayList<StudentTemplate>();
    //Have a Recycler View of own to save the Recycler View from activity_main.xml
    private RecyclerView recyclerView;
    static MyAdapter adapter;
    public LinearLayout messageLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_list);
        //All the elements will have fixed size.
        //recyclerView.setHasFixedSize(true);


        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this);


        //Set the Layout of Recyclerview to be linear. Default: Vertical.

        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new MyAdapter(mStudentList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MyAdapter.onItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                final String[] items = {"View", "Edit", "Delete"};

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose from below");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        switch (which) {
                            case 0:

                                    Intent forView = new Intent(MainActivity.this,
                                            CreateStudent.class);
                                    forView.putExtra("viewStudentPosition",position);
                                    forView.putExtra("thisIsView", 101);
                                    startActivity(forView);



                                break;

                            case 1:
                                    Intent forEdit = new Intent(MainActivity.this,
                                            CreateStudent.class);
                                    forEdit.putExtra("viewStudentPosition",position);
                                    forEdit.putExtra("thisIsEdit", 102);

                                    startActivity(forEdit);

                                break;

                            case 2:
                                final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(MainActivity.this);
                                deleteDialog.setTitle("Delete Student");
                                deleteDialog.setMessage("Do you really want to delete this Student?");
                                deleteDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mStudentList.remove(position);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                                deleteDialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                deleteDialog.show();

                                break;

                        }
                    }
                });
                AlertDialog mAlert = builder.create();
                mAlert.show();
            }
        });


    }
    public void addStudentButton (View view) {

        Intent i = new Intent(this, CreateStudent.class);
        i.putExtra("originalList",mStudentList);
        //startActivityForResult(i,REQUEST_CODE_MAKE_STUDENT);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =  getMenuInflater();

        inflater.inflate(R.menu.action_menu,menu);

        MenuItem item = menu.findItem(R.id.switchitem);

        MenuItem sortByName = menu.findItem(R.id.sortbyname);
        MenuItem sortByRoll = menu.findItem(R.id.sortbyroll);


        //Set your sortByName item.
        sortByName.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(mStudentList,new SortByName());
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this,"Sorted By Name", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        //Set your sortByRoll item.
        sortByRoll.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(mStudentList,(new SortByRoll()));
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this,"Sorted By Roll No.", Toast.LENGTH_LONG).show();
                return true;

            }
        });
        //Switch item is set.
        item.setActionView(R.layout.switch_layout);
        Switch switcherLayout = menu.findItem(R.id.switchitem).getActionView().
                findViewById(R.id.switcher);
        switcherLayout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
                }
                else {
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    //Inner Class that sorts the Student List based on Student's Names.
    public class SortByName implements Comparator<StudentTemplate> {

        @Override
        public int compare(StudentTemplate o1, StudentTemplate o2) {

            return (o1.getStudentTemplateName().compareTo(o2.getStudentTemplateName()));
        }
    }
    //Inner Class that sorts the Student List
    public class SortByRoll implements  Comparator<StudentTemplate> {


        @Override
        public int compare(StudentTemplate o1, StudentTemplate o2) {
            return (o1.getStudentTemplateRoll().compareTo(o2.getStudentTemplateRoll()));
        }
    }
/*  @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        messageLayout=(LinearLayout)findViewById(R.id.messageLayout);
        if(requestCode == REQUEST_CODE_MAKE_STUDENT) {
            if(resultCode==RESULT_OK) {
                //Update the Original List with the Updated values.
                mStudentList=(ArrayList<StudentTemplate>) data.getExtras().getSerializable("update");
                adapter.not(mStudentList);
                adapter.notifyDataSetChanged();

            }
                if(mStudentList.size()!=0){

                messageLayout.setVisibility(View.INVISIBLE);
            }
        }

    }*/
}
