package com.example.studentmanagementsystem.Student;

import android.content.DialogInterface;
import android.content.Intent;

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

import com.example.studentmanagementsystem.Adapters.MyAdapter;
import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.StudentClass.StudentTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity  {

    //Create an ArrayList to save the data.
    private ArrayList<StudentTemplate> mStudentList = new ArrayList<StudentTemplate>();
    private ArrayList<Integer> currentRollList;


    //Have a Recycler View of own to save the Recycler View from activity_main.xml
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    public static final int CODE_TO_ADD_STUDENT=101;
    public static final int CODE_TO_VIEW_STUDENT=102;
    public static final int CODE_TO_EDIT_STUDENT=103;

    private int POSITION_STUDENT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.rlRecycler_list);
        //All the elements will have fixed size.
        recyclerView.setHasFixedSize(true);


        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this);

        //Set the Layout of Recyclerview to be linear. Default: Vertical.
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new MyAdapter(this.mStudentList);
        recyclerView.setAdapter(adapter);
        //Adapter has a listener interface implemented.
        adapter.setOnItemClickListener(new MyAdapter.onItemClickListener() {
            @Override
            public void onItemClick(final int position) {//interface to implement onClick.

                //Set the position based on the Viewholder selected by the listener.
                setPositionStudent(position);

                final String[] items = {"View", "Edit", "Delete"};
                final int viewStudent = 0, editStudent = 1, deleteStudent = 2;

                //Alert Dialog that has context of this activity.
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose from below");
                final StudentTemplate whichStudent = mStudentList.get(position);
                //Sets the items of the Dialog.
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        switch (which) {
                            case viewStudent:
                                //Send the intent if the User choses the VIEW option.
                                    Intent forView = new Intent(MainActivity.this,
                                            CreateStudent.class);
                                    forView.putExtra("thisStudent",whichStudent);
                                    forView.putExtra("thisIsView", 101);
                                    startActivityForResult(forView,CODE_TO_VIEW_STUDENT);



                                break;
                            //Send the intent if the User choses the EDIT option.
                            case editStudent:
                                    Intent forEdit = new Intent(MainActivity.this,
                                            CreateStudent.class);
                                    forEdit.putExtra("thisStudent",whichStudent);
                                    forEdit.putExtra("thisIsEdit", 102);

                                    startActivityForResult(forEdit,CODE_TO_EDIT_STUDENT);

                                break;
                                //Delete the Student.
                            case deleteStudent:
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
                //Create the alert dialog.
                AlertDialog mAlert = builder.create();
                //Show the alert.
                mAlert.show();
            }
        });

    }
    protected void setPositionStudent(int position){
        POSITION_STUDENT=position;
    }
    protected int getPositionStudent(){
        return this.POSITION_STUDENT;
    }

    //Creates an intent that requests for Student Object from the CreateStudent Activity.
    public void addStudentButton (View view) {

        Log.d("robss", "addStudentButton: started");


        Intent i = new Intent(this, CreateStudent.class);
        if(mStudentList.size()>0) {
            currentRollList=makeRollIdsList(mStudentList);
            i.putExtra("rollsList", currentRollList);
            Log.d("yyyyyy", "addStudentButton: listcaughtin1"+currentRollList.get(0));
        }
        //startActivityForResult(i,REQUEST_CODE_MAKE_STUDENT);
        startActivityForResult(i,CODE_TO_ADD_STUDENT);
        Log.d("robss", "addStudentButton: started2");
    }

    //Gets the intent that can have both the added student and the updated student. We will have
    //a check inside to see which is which.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK) {
            StudentTemplate studentManipulate;
            Log.d("robss", "addStudentButton: started3");


            //Check to see if the Student returned is to be updated.
            if(requestCode==CODE_TO_VIEW_STUDENT||requestCode==CODE_TO_EDIT_STUDENT) {
                int pos=getPositionStudent();
                studentManipulate=data.getParcelableExtra("updatedStudent");
                mStudentList.get(pos).setStudentTemplateName(studentManipulate.getStudentTemplateName());
                mStudentList.get(pos).setStudentTemplateStandard(studentManipulate.getStudentTemplateStandard());
                mStudentList.get(pos).setStudentTemplateRoll(studentManipulate.getStudentTemplateRoll());
                mStudentList.get(pos).setStudentTemplateAge(studentManipulate.getStudentTemplateAge());
                adapter.notifyDataSetChanged();
                Log.d("studddd", "onActivityResult: "+mStudentList.get(pos));
            }
            //Check to see if the Student got is to be added as new.
            if(requestCode==CODE_TO_ADD_STUDENT) {
                Log.d("tttty", "onActivityResult: 11");
                studentManipulate=(StudentTemplate) data.getParcelableExtra("addedStudent");
                mStudentList.add(studentManipulate);
                adapter.notifyDataSetChanged();
            }
        }
    }

    //Create the options for the Menu.
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

}
