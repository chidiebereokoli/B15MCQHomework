package com.example.chidi.b15mcqhomework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.support.annotation.Nullable;
//import android.support.annotation.Nullable;
import com.example.chidi.b15mcqhomework.QuizContract.*;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TheQuiz.db";
    private static final int DATABASE_VERSION =1;

    private SQLiteDatabase db; //This holds a reference to the actual database, so we can easily add some values to it
    public QuizDbHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //This method will be called only the first time that we try to access the database. After this time, it
        //won't be called again unless we delete the app from our phone.
//it is here that we create the initial database
    this.db = db;//We first save the database reference into our db variable
    final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
            QuestionsTable.TABLE_NAME + " ( " +
            QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            QuestionsTable.COLUMN_QUESTION + " TEXT, " +
            QuestionsTable.COLUMN_OPTIONA + " TEXT, " +
            QuestionsTable.COLUMN_OPTIONB + " TEXT, " +
            QuestionsTable.COLUMN_OPTIONC + " TEXT, " +
            QuestionsTable.COLUMN_OPTIOND + " TEXT, " +
            QuestionsTable.COLUMN_ANSWER + " INTEGER" +
            ")";
    db.execSQL(SQL_CREATE_QUESTIONS_TABLE); // this creates a database with the table name SQL_CREATE_QUESTIONS_TABLE
        fillQuestionsTable();//Method to fill our db with questions.
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
    //if we want to edit our database, it is not enough to change the name of the string in the schema, we must also do an update in the onUpgrade method
        // to edit our database, we change the DATABASE_VERSION number (e.g. from 1 to 2) and then define the change to be made in the
        //onUpgrade method
        //e.g. we can simply delete our old table and call on create again to create a new table/execute the new SQL statement with the new schema

        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
        //with the methods defined in the onUpgrade, we can then edit our DB schema, increment our DATABASE_VERSION number and a new
        //DB will be created.
        //If we are testing the app, we can simply just delete the app from our phone, change the schema and then reinstall it, and the
        //oncreate method will again be called.
    }

    private void fillQuestionsTable() {
        Question q1 = new Question( "Which of the following is not an Android Teacher in RJT?",
                "Abdul","Ansari","John","Shiva",3);
        addQuestions(q1);
        Question q2 = new Question( "Which of the following is an Android Teacher in RJT?",
                "Alok","Shiva","Raji","DG",2);
        addQuestions(q2);
        Question q3 = new Question( "Which is the best Software development Training in RJT?",
                "Android","IOS","Java","None of the above",1);
        addQuestions(q3);
        Question q4 = new Question( "Which company do we work for?",
                "Software Technologies","MobileAppsDevelopment","IntelliJ","RJT Compuquest",4);
        addQuestions(q4);
    }

    //We will need to create another method to insert questions into database.
    private void addQuestions (Question question){
        // we will add the questions using the content values class
        //we do not have to insert an ID, into the database, since _ID from basecolumns will automatically increase
        //per database entry.
        ContentValues contentValues = new ContentValues();
        contentValues.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        contentValues.put(QuestionsTable.COLUMN_OPTIONA,question.getOptionA());
        contentValues.put(QuestionsTable.COLUMN_OPTIONB,question.getOptionB());
        contentValues.put(QuestionsTable.COLUMN_OPTIONC,question.getOptionC());
        contentValues.put(QuestionsTable.COLUMN_OPTIOND,question.getOptionD());
        contentValues.put(QuestionsTable.COLUMN_ANSWER,question.getAnswer());
        db.insert(QuestionsTable.TABLE_NAME, null,contentValues);
    }
    //we need a method to help us to retrieve data from our database
    //This method will be called from our quiz activity to request the questions from our database.
    public List<Question> getAllQuestions() {
        //what we have below is an interface, so that we cannot create a new list,
        // instead, we create an array list.
        List<Question>  questionList = new ArrayList<>();
        //We need a reference to our database to get data out of there. The getReadableDatabase() method when
        //called the first time will call our db oncreate method and create the database.
        db = getReadableDatabase();
        //we will need a cursor to query our database
        Cursor cursor = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME,null);

        //cursor.moveToFirst(): this method will move our cursor to the first entry, and return false
        //if the table is empty
        //cursor.moveToNext():same analogy...
        if(cursor.moveToFirst()){



      //We now fill the question object with data from the database row
     //keeping it simple, the cursor gives the y coordinate of a field in a table, while the columnindex/column name
     //specifies the x coordinate
      //the cursor getstring method takes int columnindex as argument and returns the content stored in the column
                //corresponding to the columnindex, at the current cursor position
      //The cursor getcolumnindex mthod returns the table index of a column table name passed in as argument
            do{
                Question question = new Question();
                question.setQuestion(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOptionA(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_OPTIONA)));
                question.setOptionB(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_OPTIONB)));
                question.setOptionC(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_OPTIONC)));
                question.setOptionD(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_OPTIOND)));
                question.setAnswer(cursor.getInt(cursor.getColumnIndex(QuestionsTable.COLUMN_ANSWER)));

                questionList.add(question);
            }while(cursor.moveToNext());

        }

        cursor.close();
        return questionList;
    }
}
