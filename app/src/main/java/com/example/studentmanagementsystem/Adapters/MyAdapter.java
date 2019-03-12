package com.example.studentmanagementsystem.Adapters;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.StudentClass.StudentTemplate;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.StudentViewHolder> {

    //variables of the adpater class.
    private ArrayList<StudentTemplate> studentList;
    private onItemClickListener mListener;

    public MyAdapter(ArrayList<StudentTemplate> studentList) {
        this.studentList = studentList;
    }


    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.student_container_layout,viewGroup,false);

        StudentViewHolder holder = new StudentViewHolder(view,mListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder studentViewHolder, int i) {

        studentViewHolder.name.setText(studentList.get(i).getStudentTemplateName());
        studentViewHolder.standard.setText(studentList.get(i).getStudentTemplateStandard());
        studentViewHolder.roll.setText(studentList.get(i).getStudentTemplateRoll());
        studentViewHolder.age.setText(studentList.get(i).getStudentTemplateAge());

    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    //Inner class that handles the View of the recycler view.
    public class StudentViewHolder extends RecyclerView.ViewHolder {

        TextView name,standard,roll,age;
        //Constructor
        public StudentViewHolder(@NonNull View itemView, final onItemClickListener listener) {

            super(itemView);

            name =  itemView.findViewById(R.id.tvNameEmpty);
            standard = itemView.findViewById(R.id.tvStandardEmpty);
            roll = itemView.findViewById(R.id.tvRollEmpty);
            age = itemView.findViewById(R.id.tvAgeEmpty);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null) {
                        int i = getAdapterPosition();
                        if(i != RecyclerView.NO_POSITION) {
                            listener.onItemClick(i);
                        }
                    }
                }
            });
        }

    }
    //interface to implement onClick.
    public interface onItemClickListener {
        void onItemClick (int position);
    }
    //Sets the listener to the private fields of the Adapter Class.
    public void setOnItemClickListener(onItemClickListener listener) {
        mListener = listener;
    }
}
