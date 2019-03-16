package com.example.studentmanagementsystem.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.model.StudentTemplate;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    //variables of the adpater class.
    private ArrayList<StudentTemplate> studentList;
    private onItemClickListener mListener;

    public StudentAdapter(ArrayList<StudentTemplate> studentList) {
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

        studentViewHolder.tvName.setText(studentList.get(i).getStudentTemplateName());
        studentViewHolder.tvStandard.setText(studentList.get(i).getStudentTemplateStandard());
        studentViewHolder.tvRoll.setText(studentList.get(i).getStudentTemplateRoll());
        studentViewHolder.tvAge.setText(studentList.get(i).getStudentTemplateAge());

    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    //Inner class that handles the View of the recycler view.
    public class StudentViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvStandard, tvRoll, tvAge;
        //Constructor
        public StudentViewHolder(@NonNull View itemView, final onItemClickListener listener) {

            super(itemView);

            tvName =  itemView.findViewById(R.id.tvNameEmpty);
            tvStandard = itemView.findViewById(R.id.tvStandardEmpty);
            tvRoll = itemView.findViewById(R.id.tvRollEmpty);
            tvAge = itemView.findViewById(R.id.tvAgeEmpty);
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
