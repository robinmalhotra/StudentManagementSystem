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
import com.example.studentmanagementsystem.adapter.StudentAdapter;
import com.example.studentmanagementsystem.backgrounddbhandle.BackgroundAsyncTasks;
import com.example.studentmanagementsystem.backgrounddbhandle.BackgroundIntentService;
import com.example.studentmanagementsystem.backgrounddbhandle.BackgroundService;
import com.example.studentmanagementsystem.communicator.Communicator;
import com.example.studentmanagementsystem.database.StudentHelperDatabase;
import com.example.studentmanagementsystem.dialog.GenerateDialog;
import com.example.studentmanagementsystem.listener.OnItemViewClickListener;
import com.example.studentmanagementsystem.model.StudentTemplate;
import com.example.studentmanagementsystem.util.Constants;
import com.example.studentmanagementsystem.util.SortByName;
import com.example.studentmanagementsystem.util.SortByRoll;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class StudentListFragment extends Fragment implements OnItemViewClickListener {


    private ArrayList<StudentTemplate> mStudentList = new ArrayList<StudentTemplate>();
    private String[] mDialogItems;
    private RecyclerView rvStudentList;
    private StudentAdapter adapter;
    private int positionStudent;
    private StudentHelperDatabase studentHelperDatabase;
    private Button addButton;
    private Context mContext;
    private Communicator mCommunicator;
    private OnItemViewClickListener mListener;
    private GenerateDialog generateDialog;



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
        //Get the student list from database.
        if(mStudentList.size()<1) {
            mStudentList.addAll(studentHelperDatabase.refreshStudentListfromDb());
        }
        setHasOptionsMenu(true);
        return view;
    }

    /**
     * Initialise Components.
     * @param view  fragment view from XML.
     */
    private void init(View view) {


        generateDialog = new GenerateDialog(mContext);
        mListener = this;
        adapter = new StudentAdapter(mStudentList,mListener);
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

    /**
     * When the second fragment makes a student object either by adding new or updating the old one,
     * this function checks how to add it back to the Array List.
     * @param bundle
     */
    public void addOrUpdateStudentInList(Bundle bundle) {

        //Add new student.
        if(Objects.equals(bundle.getString(Constants.CODE_TO_ADD_STUDENT), Constants.ADD_IT)) {
            StudentTemplate student = new StudentTemplate(bundle.getString(Constants.NAME),
                    bundle.getString(Constants.ROLL_NO),bundle.getString(Constants.STANDARD),
                    bundle.getString(Constants.AGE));
            mStudentList.add(student);
            adapter.notifyDataSetChanged();

            //Update old student.
        }else if(Objects.equals(bundle.getString(Constants.CODE_TO_ADD_STUDENT), Constants.UPDATE_IT)){
            StudentTemplate student=mStudentList.get(positionStudent);
            student.setStudentTemplateRoll(bundle.getString(Constants.ROLL_NO));
            student.setStudentTemplateName(bundle.getString(Constants.NAME));
            student.setStudentTemplateStandard(bundle.getString(Constants.STANDARD));
            student.setStudentTemplateAge(bundle.getString(Constants.AGE));
            adapter.notifyDataSetChanged();
        }

    }

    /**
     * To edit the student selected on the clicklistener
     * @param whichStudent student selected.
     */
    private void editStudent(StudentTemplate whichStudent){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.CODE_TO_ADD_STUDENT,Constants.UPDATE_IT);
        bundle.putParcelable(Constants.THISSTUDENT,whichStudent);
        mCommunicator.communicateUpdate(bundle);
    }

    /**
     * When we click the student on the recycler view's list, this funciton sends an intent to
     * open another activity that shows the student.
     * @param student selected on click listener.
     */
    private void viewDetails(StudentTemplate student){

        Intent forView = new Intent(mContext, CreateStudentActivity.class);

        forView.putExtra(Constants.CODE_TO_ADD_STUDENT,Constants.VIEW);
        forView.putExtra(Constants.THISSTUDENT,student);
        mContext.startActivity(forView);
    }

    /**Generates a dialog when a student from the recycler view list is chosen to be deleted.
     *
     * @param position of the student that needs to be deleted.
     */
    private void studentDelete(final int position) {
        final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(mContext);
        deleteDialog.setTitle(R.string.deletetitle);
        deleteDialog.setMessage(R.string.want_to_delete_or_not);
        deleteDialog.setPositiveButton(R.string.deleteconfirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                generateDialog.generateDialogOnTouch(mStudentList.get(position),Constants.DELETE_IT);
                mStudentList.remove(position);
                adapter.notifyDataSetChanged();

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



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.action_menu, menu);
        MenuItem item = menu.findItem(R.id.switchitem);
        MenuItem sortByName = menu.findItem(R.id.sortbyname);
        MenuItem sortByRoll = menu.findItem(R.id.sortbyroll);


        sortByName.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(mStudentList, new SortByName());
                adapter.notifyDataSetChanged();
                Toast.makeText(mContext, getString(R.string.sortbyname), Toast.LENGTH_LONG).show();
                return true;
            }
        });


        sortByRoll.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(mStudentList, (new SortByRoll()));
                adapter.notifyDataSetChanged();
                Toast.makeText(mContext, getString(R.string.sortbyroll), Toast.LENGTH_LONG).show();
                return true;

            }
        });

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

    /**Shows Alert Dialog when the Student in the list is clicked and gives option to View, Edit
     * or Delete the student.
     *
     * @param position is the position of the student selected.
     */
    @Override
    public void onItemClick(int position) {

        //Set the position based on the Viewholder selected by the listener.
        positionStudent = position;

        final String[] items = {Constants.VIEWMESSAGE, Constants.EDITMESSAGE, Constants.DELETEMESSAGE};
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
                        editStudent(whichStudent);

                        break;
                    //Delete the Student.
                    case deleteStudent:
                        studentDelete(positionStudent);
                        break;
                }

            }
        });
        AlertDialog mAlert = builder.create();
        mAlert.show();
    }
}
