package com.example.studentmanagementsystem.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.activity.CreateStudentActivity;
import com.example.studentmanagementsystem.activity.StudentActivity;
import com.example.studentmanagementsystem.adapter.StudentAdapter;
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

import static android.app.Activity.RESULT_OK;

public class StudentListFragment extends Fragment {

    private LayoutInflater inflater;
    private ViewGroup container;
    private Bundle savedInstanceState;
    private ArrayList<StudentTemplate> mStudentList = new ArrayList<StudentTemplate>();
    private RecyclerView rvStudentList;
    private StudentAdapter adapter;
    private int POSITION_STUDENT;
    private StudentHelperDatabase studentHelperDatabase;
    private Button addButton;
    private FragmentListListener listListener;

    public interface FragmentListListener {
        void inputListSent(CharSequence input);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_student_list,container,false);
        rvStudentList = view.findViewById(R.id.rlRecycler_list);
        rvStudentList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getContext());

        //Set the Layout of Recyclerview to be linear. Default: Vertical.
        rvStudentList.setLayoutManager(linearLayoutManager);
        adapter = new StudentAdapter(this.mStudentList);
        rvStudentList.setAdapter(adapter);
        addButton = view.findViewById(R.id.btnAdd_student);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ArrayList<Integer> currentRollList;
                Intent i = new Intent(this, CreateStudentActivity.class);
                //If there is any student in the list then send the rolls Id list.
                if(mStudentList.size()>0) {
                    currentRollList=makeRollIdsList(mStudentList);
                    i.putExtra("rollsList", currentRollList);
                }
                startActivityForResult(i, Constants.CODE_TO_ADD_STUDENT);

            }
        });



        return view;
    }

    int position;

    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        StudentListFragment tabFragment = new StudentListFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Adapter has a listener interface implemented.
        handleDialog(adapter);

        /** Check if the List is empty, if it is then get the list from the local Database.
         *
         */
        if(mStudentList.size()<1) {

            mStudentList.addAll(studentHelperDatabase.refreshStudentListfromDb());

        }

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                setPositionStudent(position);

                final String[] items = {"View", "Edit", "Delete"};
                final int viewStudent = 0, editStudent = 1, deleteStudent = 2;

                //Alert Dialog that has context of this activity.
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                                Intent forView = new Intent(getContext(),
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
                                final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(getContext());
                                deleteDialog.setTitle(R.string.deletetitle);
                                deleteDialog.setMessage(R.string.want_to_delete_or_not);
                                deleteDialog.setPositiveButton(R.string.deleteconfirm, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        String operationOnStudent = "deleteIt";
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

//    //Creates an intent that requests for Student Object from the CreateStudentActivity Activity.
//    public void addStudentButton(View view) {
//
//        ArrayList<Integer> currentRollList;
//        Intent i = new Intent(this, CreateStudentActivity.class);
//        //If there is any student in the list then send the rolls Id list.
//        if(mStudentList.size()>0) {
//            currentRollList=makeRollIdsList(mStudentList);
//            i.putExtra("rollsList", currentRollList);
//        }
//        startActivityForResult(i, Constants.CODE_TO_ADD_STUDENT);
//    }


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
                Toast.makeText(getContext(), getString(R.string.sortbyname), Toast.LENGTH_LONG).show();
                return true;
            }
        });

        //Set your sortByRoll item.
        sortByRoll.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(mStudentList, (new SortByRoll()));
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), getString(R.string.sortbyroll), Toast.LENGTH_LONG).show();
                return true;

            }
        });
        //Switch item is set.
        item.setActionView(R.layout.switch_layout);
        Switch switcherLayout = menu.findItem(R.id.switchitem).getActionView().
                findViewById(R.id.switcher);getContext(
        switcherLayout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rvStudentList.setLayoutManager(new GridLayoutManager(getContext(), 2));
                } else {
                    rvStudentList.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                        Intent forService = new Intent(getActivity(),
                                BackgroundService.class);
                        forService.putExtra("studentForDb", studentToHandle);
                        forService.putExtra("operation",operationOnStudent);
                        startService(forService);
                        break;

                    //Send the intent if the User choses the EDIT option.
                    case useIntentService:

                        Intent forIntentService = new Intent(getActivity(),
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
