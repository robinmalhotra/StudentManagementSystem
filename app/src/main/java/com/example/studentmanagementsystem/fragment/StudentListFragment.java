package com.example.studentmanagementsystem.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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
import com.example.studentmanagementsystem.communicator.Communicator;
import com.example.studentmanagementsystem.database.StudentHelperDatabase;
import com.example.studentmanagementsystem.model.StudentTemplate;
import com.example.studentmanagementsystem.util.Constants;
import com.example.studentmanagementsystem.util.SortByName;
import com.example.studentmanagementsystem.util.SortByRoll;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

import static android.app.Activity.RESULT_OK;

public class StudentListFragment extends Fragment {

    private LayoutInflater inflater;
    private ViewGroup container;
    private Bundle savedInstanceState;
    private ArrayList<StudentTemplate> mStudentList = new ArrayList<StudentTemplate>();
    private String[] mDialogItems;
    private RecyclerView rvStudentList;
    private StudentAdapter adapter;
    private int positionStudent;
    private StudentHelperDatabase studentHelperDatabase;
    private Button addButton;
    private Context mContext;
    private Communicator mCommunicator;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_student_list,container,false);

        studentHelperDatabase = new StudentHelperDatabase(getContext());
        studentHelperDatabase.getWritableDatabase();
        init(view);
        if(mStudentList.size()<1) {
            mStudentList.addAll(studentHelperDatabase.refreshStudentListfromDb());
        }

        //Adapter has a listener interface implemented.
        handleDialog();
        setHasOptionsMenu(true);
        return view;
    }

    private void init(View view) {

        adapter = new StudentAdapter(mStudentList);
        mDialogItems = getResources().getStringArray(R.array.Dialog_Operations);
        rvStudentList = view.findViewById(R.id.rlRecycler_list);
        rvStudentList.setHasFixedSize(true);
        rvStudentList.setLayoutManager(new LinearLayoutManager(mContext));

        //puts divider in view.
        rvStudentList.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        //shows effects when item is clicked.
        rvStudentList.setItemAnimator(new DefaultItemAnimator());
        rvStudentList.setAdapter(adapter);
        addButton = view.findViewById(R.id.btnAdd_student);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString(Constants.CODE_TO_ADD_STUDENT,Constants.ADD_IT);
                bundle.putParcelableArrayList(Constants.STUDENT_LIST_FROM_MAIN,mStudentList);
                mCommunicator.communicateUpdate(bundle);
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
        try {
            mCommunicator=(Communicator)mContext;
        }catch (ClassCastException e) {
            throw new ClassCastException(getString(R.string.error));
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCommunicator = null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    /** Shows Alert Dialog when the Student in the list is clicked and gives option to View, Edit
     * or Delete the student.
     */
    protected void handleDialog() {
        adapter.setOnItemClickListener(new StudentAdapter.onItemClickListener() {
            @Override
            public void onItemClick(final int position) {//interface to implement onClick.

                //Set the position based on the Viewholder selected by the listener.
                setPositionStudent(position);

                final String[] items = {"View", "Edit", "Delete"};
                final int viewStudent = 0, editStudent = 1, deleteStudent = 2;

                //Alert Dialog that has context of this activity.
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Choose from below");
                final StudentTemplate whichStudent = mStudentList.get(position);


                //Sets the items of the Dialog.
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        switch (which) {
                            case viewStudent:
                               viewDetails(whichStudent);
                                break;

                            case editStudent:
                                Bundle bundle = new Bundle();
                                bundle.putParcelable(Constants.THISSTUDENT,whichStudent);
                                mCommunicator.communicateUpdate(bundle);

                                break;
                            //Delete the Student.
                            case deleteStudent:
                                studentDelete(position);
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
        positionStudent = position;
    }

    protected int getPositionStudent() {
        return this.positionStudent;
    }



    public void addStudent(Bundle bundle) {
        if(bundle.getString(Constants.CODE_TO_ADD_STUDENT).equals(Constants.ADD_IT)) {
            StudentTemplate student = new StudentTemplate(bundle.getString(Constants.NAME),
                    bundle.getString(Constants.ROLL_NO),bundle.getString(Constants.STANDARD),
                    bundle.getString(Constants.AGE));
            mStudentList.add(student);
            adapter.notifyDataSetChanged();
        }else if(bundle.getString(Constants.CODE_TO_ADD_STUDENT).equals(Constants.UPDATE_IT)){
            StudentTemplate student=mStudentList.get(getPositionStudent());
            student.setStudentTemplateRoll(bundle.getString(Constants.ROLL_NO));
            student.setStudentTemplateName(bundle.getString(Constants.NAME));
            student.setStudentTemplateStandard(bundle.getString(Constants.STANDARD));
            student.setStudentTemplateAge(bundle.getString(Constants.AGE));
            adapter.notifyDataSetChanged();
        }

    }
    private void viewDetails(StudentTemplate student){

        Intent forView = new Intent(mContext,
                CreateStudentActivity.class);

//        final Bundle bundleToSend = new Bundle();

        //bundleToSend.putParcelable(Constants.STUDENT_LIST_FROM_MAIN,student);
//        bundleToSend.putString(Constants.VIEW_NAME,student.getStudentTemplateName());
//
//        bundleToSend.putString(Constants.VIEW_STANDARD,student.getStudentTemplateStandard());
//
//        bundleToSend.putString(Constants.VIEW_ROLL,student.getStudentTemplateRoll());
//
//        bundleToSend.putString(Constants.VIEW_AGE,student.getStudentTemplateAge());

        forView.putExtra(Constants.CODE_TO_ADD_STUDENT,Constants.VIEW);
        forView.putExtra(Constants.THISSTUDENT,student);
        mContext.startActivity(forView);
    }

    private void studentDelete(final int position) {
        final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(mContext);
        deleteDialog.setTitle(R.string.deletetitle);
        deleteDialog.setMessage(R.string.want_to_delete_or_not);
        deleteDialog.setPositiveButton(R.string.deleteconfirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                generateDialog(mStudentList.get(position),Constants.DELETE_IT);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
                        forService.putExtra(Constants.STUDENT_FOR_DB, studentToHandle);
                        forService.putExtra(Constants.OPERATION,operationOnStudent);
                        mContext.startService(forService);
                        break;

                    //Send the intent if the User chooses the EDIT option.
                    case useIntentService:

                        Intent forIntentService = new Intent(getActivity(),
                                BackgroundIntentService.class);
                        forIntentService.putExtra(Constants.STUDENT_FOR_DB, studentToHandle);
                        forIntentService.putExtra(Constants.OPERATION,operationOnStudent);
                        mContext.startService(forIntentService);

                        break;
                    //Delete the Student.
                    case useAsyncTasks:
                        BackgroundAsyncTasks backgroundAsyncTasks = new BackgroundAsyncTasks(mContext);
                        backgroundAsyncTasks.execute(studentToHandle,operationOnStudent);

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

    //Create the options for the Menu.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

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
                Toast.makeText(mContext, getString(R.string.sortbyname), Toast.LENGTH_LONG).show();
                return true;
            }
        });

        //Set your sortByRoll item.
        sortByRoll.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(mStudentList, (new SortByRoll()));
                adapter.notifyDataSetChanged();
                Toast.makeText(mContext, getString(R.string.sortbyroll), Toast.LENGTH_LONG).show();
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
                    rvStudentList.setLayoutManager(new GridLayoutManager(mContext, 2));
                } else {
                    rvStudentList.setLayoutManager(new LinearLayoutManager(mContext));
                }
            }
        });

        super.onCreateOptionsMenu(menu,inflater);
    }


}
