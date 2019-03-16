package com.example.studentmanagementsystem.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.studentmanagementsystem.adapter.StudentAdapter;
import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.database.StudentHelperDatabase;
import com.example.studentmanagementsystem.model.StudentTemplate;
import com.example.studentmanagementsystem.util.Constants;
import com.example.studentmanagementsystem.util.SortByName;
import com.example.studentmanagementsystem.util.SortByRoll;

import java.util.ArrayList;
import java.util.Collections;


public class StudentActivity extends AppCompatActivity {

    //Create an ArrayList to save the data.
    private ArrayList<StudentTemplate> mStudentList = new ArrayList<StudentTemplate>();

    private RecyclerView rvStudentList;
    private StudentAdapter adapter;
    private int POSITION_STUDENT;
    private static final int DATABASE_VERSION = 1;
    private StudentHelperDatabase studentHelperDatabase;

    public ArrayList<StudentTemplate> getmStudentList() {
        return mStudentList;
    }

    public void setmStudentList(ArrayList<StudentTemplate> mStudentList) {
        this.mStudentList = mStudentList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int[] numberOfStudentDeleted = new int[1];
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        studentHelperDatabase = new StudentHelperDatabase(this);



        rvStudentList = findViewById(R.id.rlRecycler_list);
        rvStudentList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this);

        //Set the Layout of Recyclerview to be linear. Default: Vertical.
        rvStudentList.setLayoutManager(linearLayoutManager);
        adapter = new StudentAdapter(this.mStudentList);
        rvStudentList.setAdapter(adapter);

        //Adapter has a listener interface implemented.
        adapter.setOnItemClickListener(new StudentAdapter.onItemClickListener() {
            @Override
            public void onItemClick(final int position) {//interface to implement onClick.

                //Set the position based on the Viewholder selected by the listener.
                //setPositionStudent(position);

                final String[] items = {"View", "Edit", "Delete"};
                final int viewStudent = 0, editStudent = 1, deleteStudent = 2;

                //Alert Dialog that has context of this activity.
                AlertDialog.Builder builder = new AlertDialog.Builder(StudentActivity.this);
                builder.setTitle("Choose from below");
                final StudentTemplate whichStudent = mStudentList.get(position);
                //Sets the items of the Dialog.
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        switch (which) {
                            case viewStudent:
                                //Send the intent if the User chooses the VIEW option.
                                Intent forView = new Intent(StudentActivity.this,
                                        CreateStudentActivity.class);
                                forView.putExtra("thisStudent", whichStudent);
                                forView.putExtra("thisIsView", 101);
                                startActivityForResult(forView, Constants.CODE_TO_VIEW_STUDENT);

                                break;

                            //Send the intent if the User choses the EDIT option.
                            case editStudent:
                                Intent forEdit = new Intent(StudentActivity.this,
                                        CreateStudentActivity.class);
                                forEdit.putExtra("thisStudent", whichStudent);
                                forEdit.putExtra("thisIsEdit", 102);

                                startActivityForResult(forEdit, Constants.CODE_TO_EDIT_STUDENT);

                                break;
                            //Delete the Student.
                            case deleteStudent:
                                final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(StudentActivity.this);
                                deleteDialog.setTitle("Delete Student");
                                deleteDialog.setMessage("Do you really want to delete this Student?");
                                deleteDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mStudentList.remove(position);
                                        studentHelperDatabase.deleteStudentinDb(mStudentList.get(position).getStudentTemplateRoll());
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
                            default:
                                Toast.makeText(StudentActivity.this,
                                        "Nothing Selected",
                                        Toast.LENGTH_LONG).show();

                        }
                    }
                });
                AlertDialog mAlert = builder.create();
                mAlert.show();
            }
        });


        mStudentList=studentHelperDatabase.refreshStudentListfromDb(mStudentList);

        //Log.d("yyyyyy", "onCreate: " + studentHelperDatabase.databaseToString());

    }


    //Setter Method for postion of student thats clicked on the recyclerview.
//    protected void setPositionStudent(int position) {
//        POSITION_STUDENT = position;
//    }

//    protected int getPositionStudent() {
//        return this.POSITION_STUDENT;
//    }

    //Creates an intent that requests for Student Object from the CreateStudentActivity Activity.
    public void addStudentButton(View view) {

        //ArrayList<Integer> currentRollList;
        Intent i = new Intent(this, CreateStudentActivity.class);
        //If there is any student in the list then send the rolls Id list.
//        if(mStudentList.size()>0) {
//            currentRollList=makeRollIdsList(mStudentList);
//            i.putExtra("rollsList", currentRollList);
//        }
//        startActivityForResult(i, Constants.CODE_TO_ADD_STUDENT);
        startActivityForResult(i,Constants.CODE_TO_ADD_STUDENT);
    }


    // Gets the intent that can have both the added student and the updated student. We will have
    //a check inside to see which is which.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            StudentTemplate studentManipulate;
            StudentHelperDatabase studentHelperDatabase = new StudentHelperDatabase(this);
//
//            //Check to see if the Student returned is to be updated.
//            if (requestCode == Constants.CODE_TO_VIEW_STUDENT || requestCode == Constants.CODE_TO_EDIT_STUDENT) {
//
//                int pos = getPositionStudent();
//                assert data != null;
//                studentManipulate = data.getParcelableExtra("updatedStudent");
//                mStudentList.get(pos).setStudentTemplateName(studentManipulate.getStudentTemplateName());
//                mStudentList.get(pos).setStudentTemplateStandard(studentManipulate.getStudentTemplateStandard());
//                mStudentList.get(pos).setStudentTemplateRoll(studentManipulate.getStudentTemplateRoll());
//                mStudentList.get(pos).setStudentTemplateAge(studentManipulate.getStudentTemplateAge());
//                adapter.notifyDataSetChanged();
//
//            }
//
//            //Check to see if the Student got is to be added as new.
           if (requestCode == Constants.CODE_TO_ADD_STUDENT) {
//
                assert data != null;
                String checkRollinDb = data.getStringExtra("studentRoll");
               StudentTemplate studentToAddInList = studentHelperDatabase.StudentAvailable(checkRollinDb);
               mStudentList.add(studentToAddInList);
               adapter.notifyDataSetChanged();
               Log.d("yyyyyy", "onActivityResult: " + checkRollinDb);
                //studentManipulate = studentHelperDatabase.getStudentFromDb(checkRollinDb);
                //studentManipulate = data.getParcelableExtra("addedStudent");



//
            }
        }
    }


    //Create the options for the Menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        MenuItem item = menu.findItem(R.id.switchitem);
        MenuItem sortByName = menu.findItem(R.id.sortbyname);
        MenuItem sortByRoll = menu.findItem(R.id.sortbyroll);

        //Set your sortByName item.
        sortByName.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(mStudentList, new SortByName());
                adapter.notifyDataSetChanged();
                Toast.makeText(StudentActivity.this, "Sorted By Name", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        //Set your sortByRoll item.
        sortByRoll.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(mStudentList, (new SortByRoll()));
                adapter.notifyDataSetChanged();
                Toast.makeText(StudentActivity.this, "Sorted By Roll No.", Toast.LENGTH_LONG).show();
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
                    rvStudentList.setLayoutManager(new GridLayoutManager(StudentActivity.this, 2));
                } else {
                    rvStudentList.setLayoutManager(new LinearLayoutManager(StudentActivity.this));
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

}
