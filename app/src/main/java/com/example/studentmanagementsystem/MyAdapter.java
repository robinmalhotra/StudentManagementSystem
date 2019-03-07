package com.example.studentmanagementsystem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.StudentViewHolder> {

    //variables of the adpater class.
    ArrayList<StudentTemplate> studentList;
    private onItemClickListener mListener;

    //interface to implement onClick.
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

    public class StudentViewHolder extends RecyclerView.ViewHolder {

        TextView name,standard,roll,age;
        //Constructor
        public StudentViewHolder(@NonNull View itemView, final onItemClickListener listener) {

            super(itemView);

            name = (TextView) itemView.findViewById(R.id.nameEmpty);
            standard = (TextView)itemView.findViewById(R.id.standardEmpty);
            roll = (TextView)itemView.findViewById(R.id.rollEmpty);
            age = (TextView)itemView.findViewById(R.id.ageEmpty);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null) {
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

    public void setOnItemClickListener(onItemClickListener listener) {
        mListener = listener;
    }
}
