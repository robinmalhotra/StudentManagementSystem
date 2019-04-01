package com.example.studentmanagementsystem.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.listener.RecyclerViewOnClickListener;
import com.example.studentmanagementsystem.model.StudentTemplate;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    //variables of the adpater class.
    private ArrayList<StudentTemplate> studentList;
    private RecyclerViewOnClickListener mListener;

    public StudentAdapter(ArrayList<StudentTemplate> studentList, Context context) {
        this.studentList = studentList;
         mListener = (RecyclerViewOnClickListener) context;
    }


    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.student_container_layout,viewGroup,false);

        StudentViewHolder holder = new StudentViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final StudentViewHolder studentViewHolder, int position) {

        studentViewHolder.tvName.setText(studentList.get(position).getStudentTemplateName());
        studentViewHolder.tvStandard.setText(studentList.get(position).getStudentTemplateStandard());
        studentViewHolder.tvRoll.setText(studentList.get(position).getStudentTemplateRoll());
        studentViewHolder.tvAge.setText(studentList.get(position).getStudentTemplateAge());
        studentViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(studentViewHolder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    //Inner class that handles the View of the recycler view.
    public class StudentViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvStandard, tvRoll, tvAge;
        //Constructor
        public StudentViewHolder(@NonNull View itemView) {

            super(itemView);

            tvName =  itemView.findViewById(R.id.tvNameEmpty);
            tvStandard = itemView.findViewById(R.id.tvStandardEmpty);
            tvRoll = itemView.findViewById(R.id.tvRollEmpty);
            tvAge = itemView.findViewById(R.id.tvAgeEmpty);

        }

    }
}

