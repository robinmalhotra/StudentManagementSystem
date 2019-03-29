package com.example.studentmanagementsystem.activity;

import android.content.DialogInterface;
import android.content.Intent;

import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    private ArrayList<StudentTemplate> mStudentList = new ArrayList<>();
    private RecyclerView rvStudentList;
    private StudentAdapter adapter;
    private int POSITION_STUDENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StudentHelperDatabase studentHelperDatabase = new StudentHelperDatabase(this);
        studentHelperDatabase.getWritableDatabase();

        rvStudentList = findViewById(R.id.rlRecycler_list);
        rvStudentList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this);

        rvStudentList.setLayoutManager(linearLayoutManager);
        adapter = new StudentAdapter(this.mStudentList);
        rvStudentList.setAdapter(adapter);

        //Adapter has a listener interface implemented.
        handleDialog(adapter);

        /** Check if the List is empty, if it is then get the list from the local Database.
         *
        */
        if(mStudentList.size()<1) {

            mStudentList.addAll(studentHelperDatabase.refreshStudentListfromDb());

        }

    }


    /** Shows Alert Dialog when the Student in the list is clicked and gives option to View, Edit
     * or Delete the student.
     * @param adapter
     */
    protected void handleDialog(final StudentAdapter adapter) {
        adapter.setOnItemClickListener(new StudentAdapter.onItemClickListener() {
            @Override
            public void onItemClick(final int position) {//interface to implement onClick.

                //Set the position based on the Viewholder selected by the listener.

                POSITION_STUDENT=position;

                final String[] items = {getString(R.string.viewitem), getString(R.string.edititem),
                        getString(R.string.deleteitem)};
                final int viewStudent = 0, editStudent = 1, deleteStudent = 2;

                //Alert Dialog that has context of this activity.
                AlertDialog.Builder builder = new AlertDialog.Builder(StudentActivity.this);
                builder.setTitle(R.string.alertchoice);
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
                                forView.putExtra(Constants.THISSTUDENT, whichStudent);
                                forView.putExtra(Constants.THISISVIEW, 101);
                                startActivityForResult(forView, Constants.CODE_TO_VIEW_STUDENT);

                                break;

                            //Send the intent if the User choses the EDIT option.
                            case editStudent:
                                Intent forEdit = new Intent(StudentActivity.this,
                                        CreateStudentActivity.class);
                                forEdit.putExtra(Constants.THISSTUDENT, whichStudent);
                                forEdit.putExtra(Constants.THISISEDIT, 102);
                                startActivityForResult(forEdit, Constants.CODE_TO_EDIT_STUDENT);

                                break;
                            //Delete the Student.
                            case deleteStudent:
                                final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(StudentActivity.this);
                                deleteDialog.setTitle(R.string.deletetitle);
                                deleteDialog.setMessage(R.string.want_to_delete_or_not);
                                deleteDialog.setPositiveButton(R.string.deleteconfirm, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        String operationOnStudent = Constants.DELETE_IT;
                                        generateDialog(mStudentList.get(position),operationOnStudent);
                                        mStudentList.remove(position);

                                    }
                                });
                                deleteDialog.setNegativeButton(R.string.deletecancel, new DialogInterface.OnClickListener() {
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
                //SORRY BROOO FOR THIS EVIL
//                AlertDialog mAlert = builder.create();
//                mAlert.show();
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


    /**
     *
     * @param view
     */
    //Creates an intent that requests for Student Object from the CreateStudentActivity Activity.
    public void addStudentButton(View view) {

        ArrayList<Integer> currentRollList;
        Intent i = new Intent(this, CreateStudentActivity.class);

        if(mStudentList.size()>0) {
            currentRollList=makeRollIdsList(mStudentList);
            i.putExtra("rollsList", currentRollList);
        }
        startActivityForResult(i, Constants.CODE_TO_ADD_STUDENT);
    }


    /**
    Gets the intent that can have both the added student and the updated student. We will have
    a check inside to see which is which.
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK) {
            StudentTemplate studentManipulate;

            //Check to see if the Student returned is to be updated.
            if(requestCode==Constants.CODE_TO_VIEW_STUDENT||requestCode==Constants.CODE_TO_EDIT_STUDENT) {

                int pos=getPositionStudent();
                assert data != null;
                studentManipulate=data.getParcelableExtra(Constants.UPDATEDSTUDENT);
                mStudentList.get(pos).setStudentTemplateName(studentManipulate.getStudentTemplateName());
                mStudentList.get(pos).setStudentTemplateStandard(studentManipulate.getStudentTemplateStandard());
                mStudentList.get(pos).setStudentTemplateRoll(studentManipulate.getStudentTemplateRoll());
                mStudentList.get(pos).setStudentTemplateAge(studentManipulate.getStudentTemplateAge());
                adapter.notifyDataSetChanged();

            }

            //Check to see if the Student got is to be added as new.
            if(requestCode==Constants.CODE_TO_ADD_STUDENT) {

                assert data != null;
                studentManipulate = data.getParcelableExtra(Constants.ADDEDSTUDENT);
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
                Toast.makeText(StudentActivity.this, getString(R.string.sortbyname), Toast.LENGTH_LONG).show();
                return true;
            }
        });

        //Set your sortByRoll item.
        sortByRoll.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(mStudentList, (new SortByRoll()));
                adapter.notifyDataSetChanged();
                Toast.makeText(StudentActivity.this, getString(R.string.sortbyroll), Toast.LENGTH_LONG).show();
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
    /**Creates a Roll Ids list out from the student list to make the Parcelable object light.
    *@returns: ArrayList<Integer>: String Array containing the List of Roll Numbers
    *@params: ArrayList<StudentTemplate>: ArrayList of Students;
     * */
    public ArrayList<Integer> makeRollIdsList(ArrayList<StudentTemplate> listForRolls) {

        int listSize= listForRolls.size();
        ArrayList<Integer> rollsList=new ArrayList<>();
        for(int thisStudentRoll=0;thisStudentRoll<listSize;thisStudentRoll++) {
            rollsList.add(Integer.parseInt(listForRolls.
                    get(thisStudentRoll).getStudentTemplateRoll()));
        }
        return rollsList;
    }
    /** Generates Dialog box when we press Delete option in the Dialog already on the screen.
     * Choice 1: via Service
     * Choice 2: via Intent Service
     * Choice 3: via AsyncTasks
     * param@ studentToHandle: Student model object
     * param@ operationOnStudent: To save the delete Operation in the Background db Handlers.
     */
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
                        forService.putExtra(Constants.STUDENT_FOR_DB, studentToHandle);
                        forService.putExtra(Constants.OPERATION,operationOnStudent);
                        startService(forService);
                        break;

                    //Send the intent if the User choses the EDIT option.
                    case useIntentService:

                        Intent forIntentService = new Intent(StudentActivity.this,
                                BackgroundIntentService.class);
                        forIntentService.putExtra(Constants.STUDENT_FOR_DB, studentToHandle);
                        forIntentService.putExtra(Constants.OPERATION,operationOnStudent);
                        startService(forIntentService);

                        break;
                    //Delete the Student.
                    case useAsyncTasks:
                        BackgroundAsyncTasks backgroundAsyncTasks = new BackgroundAsyncTasks(StudentActivity.this);

                        backgroundAsyncTasks.execute(studentToHandle,operationOnStudent,null);

                        //startService(forIntentService);
                        break;
                    default:
                        break;
                }
                adapter.notifyDataSetChanged();

            }
        });
        AlertDialog mAlert = builder.create();
        mAlert.show();

    }

}
