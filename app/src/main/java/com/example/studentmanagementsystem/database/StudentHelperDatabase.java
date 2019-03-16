package com.example.studentmanagementsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.studentmanagementsystem.model.StudentTemplate;

import java.util.ArrayList;


public class StudentHelperDatabase extends SQLiteOpenHelper {

    public static final String STUDENT_DB = "student.db";
    public static final String STUDENT_TABLE = "student_table";
    private static final int DATABASE_VERSION = 1;

    /* Declare Columns of the Table. */
    public static final String COL_1_STUDENT_ROLL = "_roll";
    public static final String COL_2_STUDENT_NAME = "name";
    public static final String COL_3_STUDENT_STANDARD = "standard";
    public static final String COL_4_STUDENT_AGE = "age";

    /* Constructor to setup a new Database. */
    public StudentHelperDatabase(Context context) {

        super(context, STUDENT_DB, null, DATABASE_VERSION);
        Log.d("yyyyyy", "StudentHelperDatabase: constructor");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("yyyyyy", "onCreate: of db");

        /**
        *Write "AUTOINCREAMENT" after PRIMARY KEY to automatically assign roll ids.
        *Remember to parseInt the String RollIds from the Student Model Class when
        *assigning to the COL_1.
        */
        String queryCreateDb = "CREATE TABLE IF NOT EXISTS " + STUDENT_TABLE +
                "(" + COL_1_STUDENT_ROLL + " BLOB PRIMARY KEY, "
                + COL_2_STUDENT_NAME + " BLOB , "
                + COL_3_STUDENT_STANDARD + " BLOB ,"
                + COL_4_STUDENT_AGE + " BLOB " +
                ");";

        db.execSQL(queryCreateDb);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE);
        onCreate(db);

    }

    public void addStudentinDb (StudentTemplate student) throws SQLiteException {

        Log.d("yyyyyy", "addStudentinDb: added in db");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_2_STUDENT_NAME, student.getStudentTemplateName());
        //Since the Roll Id of the student is String to typecast it to Integer.
        values.put(COL_1_STUDENT_ROLL, student.getStudentTemplateRoll());
        values.put(COL_3_STUDENT_STANDARD, student.getStudentTemplateStandard());
        values.put(COL_4_STUDENT_AGE, student.getStudentTemplateAge());

        Log.d("yyyyyy", "addStudentinDb: ended" + student.getStudentTemplateName());

        db.insert(STUDENT_TABLE, null, values);
        db.close();
    }


    public void deleteStudentInDb (StudentTemplate studentTemplate) throws SQLiteException {

        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(STUDENT_TABLE, COL_1_STUDENT_ROLL + "=?", new String[]{studentTemplate.getStudentTemplateRoll()});
            db.close();

    }



//        SQLiteDatabase db = this.getWritableDatabase();
//
//            //db.delete(STUDENT_TABLE, COL_1_STUDENT_ROLL + "=?", new String[]{String.valueOf(studentRollId)});
//            db.execSQL("DELETE FROM " + STUDENT_TABLE + " WHERE " + COL_1_STUDENT_ROLL + "=\"" + studentRollId + "\";");
//            db.close();


    public boolean updateStudentInDb (StudentTemplate studentToUpdate) throws SQLiteException {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2_STUDENT_NAME,studentToUpdate.getStudentTemplateName());
        //Since the Roll Id of the student is String to typecast it to Integer.
        values.put(COL_1_STUDENT_ROLL,studentToUpdate.getStudentTemplateRoll());
        values.put(COL_3_STUDENT_STANDARD,studentToUpdate   .getStudentTemplateStandard());
        values.put(COL_4_STUDENT_AGE,studentToUpdate.getStudentTemplateAge());


        try {

            String[] args = new String[]{studentToUpdate.getStudentTemplateRoll()};
            db.update(STUDENT_TABLE, values, COL_1_STUDENT_ROLL + "=?", args);

            db.close();


            return true;
        } catch (SQLiteException e) {
            db.close();

            return false;
        }


    }


    public ArrayList<StudentTemplate> refreshStudentListfromDb() {
        ArrayList<StudentTemplate> listToInflate= new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + STUDENT_TABLE+ ";";

        Cursor cursor = db.rawQuery(query,null);

        String studentName = new String();
        String studentRoll= new String();
        String studentStandard = new String();
        String studentAge = new String();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){

                Log.d("yyyyyy", "getStudentListFromDb: before while");
                studentRoll = cursor.getString(cursor.getColumnIndex(COL_1_STUDENT_ROLL));
                studentName = cursor.getString(cursor.getColumnIndex(COL_2_STUDENT_NAME));
                studentStandard = cursor.getString(  cursor.getColumnIndex(COL_3_STUDENT_STANDARD));
                studentAge = cursor.getString(cursor.getColumnIndex(COL_4_STUDENT_AGE));
            Log.d("yyyyyy", "getStudentListFromDb: in while:  " + studentName);
                StudentTemplate studentToShow = new StudentTemplate(
                        studentName,
                        studentRoll,
                        studentStandard,
                        studentAge);

                      listToInflate.add(studentToShow);
        }
        cursor.close();
        db.close();
        Log.d("yyyyyy", "getStudentFromDb: studentlistreturned");
        return listToInflate;

    }
    public StudentTemplate StudentAvailable(String thisRollId) throws SQLiteException {
        Log.d("yyyyyy", "StudentAvailable: "+ thisRollId);
        SQLiteDatabase db = this.getReadableDatabase();
        StudentTemplate studentToAdd = new StudentTemplate();
        String query = "SELECT * FROM " + STUDENT_TABLE + " WHERE "
                + COL_1_STUDENT_ROLL + " = '" + String.valueOf(thisRollId) + "'";


            Cursor cursor = db.rawQuery(query, null);

            while(cursor.moveToNext()){
                studentToAdd.setStudentTemplateName(cursor.getString(cursor.getColumnIndex(COL_2_STUDENT_NAME)));
                studentToAdd.setStudentTemplateRoll(cursor.getString(cursor.getColumnIndex(COL_1_STUDENT_ROLL)));
                studentToAdd.setStudentTemplateStandard(cursor.getString(cursor.getColumnIndex(COL_3_STUDENT_STANDARD)));
                studentToAdd.setStudentTemplateAge(cursor.getString(cursor.getColumnIndex(COL_4_STUDENT_AGE)));
            }

        return studentToAdd;
    }
}




