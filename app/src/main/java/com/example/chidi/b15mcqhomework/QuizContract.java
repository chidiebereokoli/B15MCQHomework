package com.example.chidi.b15mcqhomework;

import android.provider.BaseColumns;

//The quiz contract class is a container for the different constants that we need for our SQLite applications
//If we want to change the column names, we can do it one place, instead of in everywhere in our code.
//We do this by creating an inner class for each different table in our database
public final class QuizContract {
    //We have an empty constructor for the QuizContract class, and we also make it final, because we do
    //not want to mistakenly create an object of the quiz contract class. This is because we want to
    //use the quiz contract class only as a container for the constants; we do not want to do anything else
    // with it.
    private QuizContract(){}
    //BaseColumns will provide an additional constant called _ID which will automatically increase with
    //each new entry in the quiz table. It provides an ID column for our table
    //the QuestionsTable will contain our questions in this database
    public static class QuestionsTable implements BaseColumns{
        //In here,we will create a constant, which will contain the same information that we have in our
        //Questions Java class, AND the name of the Table.

        //Public: because we want to access the variables outside of the QuestionTable class
        //static: because you want to access the varaiables without needing an instance of QuestionsTable
        //Final: because you do not want to change the varaibles

        public static final String TABLE_NAME = "quiz_questions";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_OPTIONA = "optionA";
        public static final String COLUMN_OPTIONB = "optionB";
        public static final String COLUMN_OPTIONC = "optionC";
        public static final String COLUMN_OPTIOND = "optionD";
        public static final String COLUMN_ANSWER = "answer";
    }

}
