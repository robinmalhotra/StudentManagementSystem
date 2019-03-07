package com.example.studentmanagementsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class IndividualProperties extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_properties);

        TextView nameOfS = (TextView)findViewById(R.id.nameDialog);
        TextView standardOfS = (TextView)findViewById(R.id.standardDialog);
        TextView rollOfS = (TextView)findViewById(R.id.rollDialog);
        TextView ageOfS = (TextView)findViewById(R.id.ageDialog);
        int positionOfList;
        Intent createDialog = getIntent();
        positionOfList = (int) createDialog.getExtras().getSerializable("pos");

        nameOfS.setText(MainActivity.mStudentList.get(positionOfList).getStudentTemplateName());
        standardOfS.setText(MainActivity.mStudentList.get(positionOfList).getStudentTemplateStandard());
        rollOfS.setText(MainActivity.mStudentList.get(positionOfList).getStudentTemplateRoll());
        ageOfS.setText(MainActivity.mStudentList.get(positionOfList).getStudentTemplateAge());
    }
}
