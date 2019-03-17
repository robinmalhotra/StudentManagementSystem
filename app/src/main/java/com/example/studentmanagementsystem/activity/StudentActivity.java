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
import com.example.studentmanagementsystem.backgrounddbhandle.BackgroundAsyncTasks;
import com.example.studentmanagementsystem.backgrounddbhandle.BackgroundIntentService;
import com.example.studentmanagementsystem.backgrounddbhandle.BackgroundService;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        studentHelperDatabase = new StudentHelperDatabase(this);
        studentHelperDatabase.getWritableDatabase();

        rvStudentList = findViewById(R.id.rlRecycler_list);
        rvStudentList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this);

        //Set the Layout of Recyclerview to be linear. Default: Vertical.
        rvStudentList.setLayoutManager(linearLayoutManager);
        adapter = new StudentAdapter(this.mStudentList);
        rvStudentList.setAdapter(adapter);

        //Adapter has a listener interface implemented.
        handleDialog(adapter);

        if(mStudentList.size()<1) {
            mStudentList.addAll(studentHelperDatabase.refreshStudentListfromDb());
            Log.d("yyyyyy", "onCreate: " + mStudentList);

        }

    }

    protected void handleDialog(final StudentAdapter adapter) {
        adapter.setOnItemClickListener(new StudentAdapter.onItemClickListener() {
            @Override
            public void onItemClick(final int position) {//interface to implement onClick.

                //Set the position based on the Viewholder selected by the listener.
                setPositionStudent(position);

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
                                        Log.d("yyyyyy", "onClick: " + position);

                                        Log.d("yyyyyy", "onClick: deleted student: " + mStudentList.get(position).getStudentTemplateRoll());
                                        String operationOnStudent = "deleteIt";
                                        generateDialog(mStudentList.get(position),operationOnStudent);
                                        //studentHelperDatabase.deleteStudentInDb(mStudentList.get(position));
                                        mStudentList.remove(position);

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


    //Setter Method for postion of student thats clicked on the recyclerview.
    protected void setPositionStudent(int position) {
        POSITION_STUDENT = position;
    }

    protected int getPositionStudent() {
        return this.POSITION_STUDENT;
    }

    //Creates an intent that requests for Student Object from the CreateStudentActivity Activity.
    public void addStudentButton(View view) {

        ArrayList<Integer> currentRollList;
        Intent i = new Intent(this, CreateStudentActivity.class);
        //If there is any student in the list then send the rolls Id list.
        if(mStudentList.size()>0) {
            currentRollList=makeRollIdsList(mStudentList);
            i.putExtra("rollsList", currentRollList);
        }
        startActivityForResult(i, Constants.CODE_TO_ADD_STUDENT);
    }


    // Gets the intent that can have both the added student and the updated student. We will have
    //a check inside to see which is which.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK) {
            StudentTemplate studentManipulate;

            //Check to see if the Student returned is to be updated.
            if(requestCode==Constants.CODE_TO_VIEW_STUDENT||requestCode==Constants.CODE_TO_EDIT_STUDENT) {

                int pos=getPositionStudent();
                assert data != null;
                studentManipulate=data.getParcelableExtra("updatedStudent");
                mStudentList.get(pos).setStudentTemplateName(studentManipulate.getStudentTemplateName());
                mStudentList.get(pos).setStudentTemplateStandard(studentManipulate.getStudentTemplateStandard());
                mStudentList.get(pos).setStudentTemplateRoll(studentManipulate.getStudentTemplateRoll());
                mStudentList.get(pos).setStudentTemplateAge(studentManipulate.getStudentTemplateAge());
                adapter.notifyDataSetChanged();

            }

            //Check to see if the Student got is to be added as new.
            if(requestCode==Constants.CODE_TO_ADD_STUDENT) {

                assert data != null;
                studentManipulate = data.getParcelableExtra("addedStudent");
                mStudentList.add(studentManipulate);
                adapter.notifyDataSetChanged();

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
    //Creates a Roll Ids list out from the student list to make the Parcelable object light.
    //@returns: String Array containing the List of Roll Numbers
    //@params: ArrayList of Students;
    public ArrayList<Integer> makeRollIdsList(ArrayList<StudentTemplate> listForRolls) {

        int listSize= listForRolls.size();
        ArrayList<Integer> rollsList=new ArrayList<Integer>();
        for(int thisStudentRoll=0;thisStudentRoll<listSize;thisStudentRoll++) {
            rollsList.add(Integer.parseInt(listForRolls.
                    get(thisStudentRoll).getStudentTemplateRoll()));
        }
        return rollsList;
    }

    private void generateDialog(final StudentTemplate studentToHandle, final String operationOnStudent) {



        final String[] items = {getString(R.string.alert_service),
                getString(R.string.alert_intentservice),
                getString(R.string.alert_async)};
        final int useService = 0, useIntentService = 1, useAsyncTasks = 2;

        //Alert Dialog that has context of this activity.
        AlertDialog.Builder builder = new AlertDialog.Builder(StudentActivity.this);
        builder.setTitle(R.string.userchoice_dbhandle_alert);
        //Sets the items of the Dialog.
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                switch (which) {
                    case useService:
                        //Send the intent if the User chooses the VIEW option.
                        Intent forService = new Intent(StudentActivity.this,
                                BackgroundService.class);
                        forService.putExtra("studentForDb", studentToHandle);
                        forService.putExtra("operation",operationOnStudent);
                        startService(forService);
                        Log.d("yyyyyy", "generateDialog: "+ studentToHandle.getStudentTemplateName());

                        break;

                    //Send the intent if the User choses the EDIT option.
                    case useIntentService:
                        Intent forIntentService = new Intent(StudentActivity.this,
                                BackgroundIntentService.class);
                        forIntentService.putExtra("studentForDb", studentToHandle);
                        forIntentService.putExtra("operation",operationOnStudent);
                        startService(forIntentService);


                        break;
                    //Delete the Student.
                    case useAsyncTasks:
                        BackgroundAsyncTasks backgroundAsyncTasks = new BackgroundAsyncTasks(StudentActivity.this);

                        backgroundAsyncTasks.execute(studentToHandle,operationOnStudent);

                        //startService(forIntentService);
                        break;
                }
                adapter.notifyDataSetChanged();

            }
        });
        AlertDialog mAlert = builder.create();
        mAlert.show();

    }

}
