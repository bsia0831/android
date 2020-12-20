package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private static String DATABASE_NAME = null;
    private static String TABLE_NAME1 = "USER";
    private static int DATABASE_VERSION = 1;
    private RegisterActivity.DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private EditText join_id;
    private EditText join_pw;
    private EditText join_email;
    private RadioGroup gender;
    private RadioButton join_gender;
    private EditText join_age;

    private static String UID, UPW, UEMAIL, UGENDER, UAGE;
    private static int btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        join_id = (EditText)findViewById(R.id.idText);
        join_pw = (EditText)findViewById(R.id.passwordText);
        join_email = (EditText)findViewById(R.id.emailText);
        gender = (RadioGroup) findViewById(R.id.genderGroup);
        join_gender = (RadioButton) findViewById(gender.getCheckedRadioButtonId());
        join_age = (EditText)findViewById(R.id.ageText);

        DATABASE_NAME = "exercise";
        boolean isOpen = openDatabase();
        if (isOpen) {
            executeRawQuery();
        }

    }

    private void executeRawQueryParam2() {

        try {
            db.execSQL("insert into " + TABLE_NAME1 + " (USER_ID,USER_PWD,USER_EMAIL,USER_GENDER,USER_AGE) VALUES"
                    + "(" + "'" + UID + "'" + "," + "'" + UPW + "'" + "," + "'" + UEMAIL + "'" + "," + "'" + UGENDER + "'" + "," + "'" + UAGE + "'" + ");");

        } catch (Exception ex) {
            Log.e(TAG, "Exception in insert SQL", ex);
        }

    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
        }


        public void onOpen(SQLiteDatabase db) {

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ".");

        }
    }

    private boolean openDatabase() {
        dbHelper = new RegisterActivity.DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        return true;
    }

    private void executeRawQuery() {
        Cursor c1 = db.rawQuery("select count(*) as Total from " + TABLE_NAME1, null);
        c1.moveToNext();
        c1.close();
    }

    public void overlap(View v) {
        String SQL05 = "select USER_ID "
                + " from " + TABLE_NAME1
                + " where USER_ID =?";

        String[] args05 = {join_id.getText().toString()};
        Cursor c05 = db.rawQuery(SQL05, args05);

        if (c05.getCount() == 1) {
            //아이디가 틀렸습니다.
            Toast.makeText(RegisterActivity.this, "이미 존재하는 아이디입니다.", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(RegisterActivity.this, "사용가능한 아이디입니다.", Toast.LENGTH_LONG).show();
            btn = 1;
        }
    }

    public void Login2(View v) {
        if (btn == 1) {
            UID = join_id.getText().toString();
            UPW = join_pw.getText().toString();
            UEMAIL = join_email.getText().toString();
            UGENDER = join_gender.getText().toString();
            UAGE = join_age.getText().toString();

            executeRawQueryParam2();

            Intent intent_join = new Intent(this, LoginActivity.class);
            startActivity(intent_join);
        }
        else {
            Toast.makeText(RegisterActivity.this, "아이디 중복확인을 눌러주세요", Toast.LENGTH_LONG).show();
        }
    }

}
