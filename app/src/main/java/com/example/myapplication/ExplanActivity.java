package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ExplanActivity extends AppCompatActivity {

    private static String DATABASE_NAME = null;
    private static String TABLE_NAME1 = "USER";
    private static String TABLE_NAME2 = "EXERCISE";
    private static int DATABASE_VERSION = 1;
    private ExplanActivity.DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    public static final String TAG = "MainActivity";

    Button btn_close;
    TextView txt_title;
    TextView txt_explan;
    private static String name;
    private int ex_id;
    private String ex_name;
    private String ex_level;
    private String ex_explan;
    private int ex_img = 0;
    private static String ex_part;
    private int ex_time = 0;
    private int AllExNo = 3;   //DB에 들어간 운동 수
    int[] AllExplan = new int[AllExNo];
    private int exnum = 2;
    private int check = 0;

    ImageView img1;
    ImageView img2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explan);

        //원래 액티비티로 돌아가기
        btn_close = (Button)findViewById(R.id.btn_close);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        txt_title = (TextView)findViewById(R.id.popup_title);
        txt_explan = (TextView)findViewById(R.id.txt_explan);
        txt_title.setText(name);

        img1 = (ImageView)findViewById(R.id.img1);
        img2 = (ImageView)findViewById(R.id.img2);

        img2.setVisibility(View.INVISIBLE);

        /*
        if (txt_title.getText().toString() == "덩키킥") {
            img1.setImageResource(R.drawable.a_dung);
            img2.setImageResource(R.drawable.a_dung2);
            img2.setVisibility(View.VISIBLE);
        } else if (txt_title.getText().toString() == "트라이익스텐션") {
            img1.setImageResource(R.drawable.tryex1);
            img2.setImageResource(R.drawable.tryex2);
            img2.setVisibility(View.VISIBLE);
        } else if (txt_title.getText().toString() == "스쿼트") {
            img1.setImageResource(R.drawable.sq1);
            img2.setImageResource(R.drawable.sq2);
            img2.setVisibility(View.VISIBLE);
        }
        */


        //for (int i=0; i<AllExNo; i++){
        //    AllExplan[i] = i+1;
        //}

        DATABASE_NAME = "exercise";
        boolean isOpen = openDatabase();
        if (isOpen) {
            executeRawQuery();
            executeRawQueryParam();
        }

    }

    private boolean openDatabase() {
        dbHelper = new ExplanActivity.DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        return true;
    }


    private void executeRawQuery() {
        Cursor c1 = db.rawQuery("select count(*) as Total from " + TABLE_NAME2, null);
        c1.moveToNext();
        c1.close();
    }

    private void executeRawQueryParam() {
        System.out.println("******name="+txt_title.getText().toString()+"*******");
        String SQL01 = "select EXERCISE_ID,EXERCISE_NAME,EXERCISE_EXPLAIN "
                + " from " + TABLE_NAME2
                + " where EXERCISE_NAME =?";
        String[] args = {txt_title.getText().toString()};

        Cursor c01 = db.rawQuery(SQL01,args);
        int recordCount = c01.getCount();
        System.out.println("******recordCount="+recordCount+"*******");

        for (int i=0; i <recordCount; i++) {
            c01.moveToNext();
            ex_id = c01.getInt(0);
            ex_name = c01.getString(1);
            ex_explan = c01.getString(2);

            System.out.println("Record #" + i + " : " + ex_id + "," + ex_name + "," + ex_explan);
        }
        c01.close();
    }

    private void println(String msg) {
        Log.d(TAG, msg);
        txt_explan.setText( msg);
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        public void onCreate(SQLiteDatabase db) {
            try {

            } catch (Exception ex) {
                Log.e(TAG, "Exception in DROP_SQL", ex);
            }


            try {

            } catch (Exception ex) {
                Log.e(TAG, "Exception in CREATE_SQL", ex);
            }

            try {


            } catch (Exception ex) {
                Log.e(TAG, "Exception in insert SQL", ex);
            }
        }


        public void onOpen(SQLiteDatabase db) {

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ".");

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
}