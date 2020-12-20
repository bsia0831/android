package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private ArrayAdapter adapter;
    private Spinner spinner;
    private static String DATABASE_NAME = null;
    private static String TABLE_NAME1 = "USER";
    private static String TABLE_NAME2 = "EXERCISE";
    private static String TABLE_NAME3 = "PLAN";
    private static String TABLE_NAME4 = "RECORD";
    private static int DATABASE_VERSION = 1;
    private LoginActivity.DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    public static final String TAG = "MainActivity";

    EditText IDT, PWT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button)findViewById(R.id.loginButton);

        TextView registerButton = (TextView) findViewById(R.id.registerButton);

        IDT = (EditText)findViewById(R.id.idText);
        PWT = (EditText)findViewById(R.id.passwordText);

        registerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);

            }
        });

        //loginButton.setOnClickListener(new View.OnClickListener(){

        //    @Override
        //    public void onClick(View view) {
        //        Intent MainIntent = new Intent(LoginActivity.this, MainActivity.class);
        //        LoginActivity.this.startActivity(MainIntent);
        //    }
        //});

        DATABASE_NAME = "exercise";
        boolean isOpen = openDatabase();
        if (isOpen) {
            executeRawQuery();
        }

    }

    private boolean openDatabase() {
        dbHelper = new LoginActivity.DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        return true;
    }

    private void executeRawQuery() {
        Cursor c1 = db.rawQuery("select count(*) as Total from " + TABLE_NAME1, null);
        c1.moveToNext();
        c1.close();
    }

    public void Login(View v) {

        String SQL05 = "select USER_ID "
                + " from " + TABLE_NAME1
                + " where USER_ID =?";

        String[] args05 = {IDT.getText().toString()};
        Cursor c05 = db.rawQuery(SQL05, args05);


        if (c05.getCount() != 1) {
            //아이디가 틀린 경우
            Toast.makeText(LoginActivity.this, "존재하지 않는 아이디입니다.", Toast.LENGTH_LONG).show();
        } else {

            c05.close();
            String SQL06 = "select USER_PWD "
                    + " from " + TABLE_NAME1
                    + " where USER_ID = ?";

            String[] args06 = {IDT.getText().toString()};
            Cursor c06 = db.rawQuery(SQL06, args06);
            c06.moveToNext();

            if (PWT.getText().toString().equals(c06.getString(0))) {


                Toast.makeText(LoginActivity.this, "로그인성공", Toast.LENGTH_SHORT).show();
                //인텐트 생성 및 호출

                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);

                //비밀번호가 틀렸습니다.

                // 자동 로그인 유지
                SaveSharedPreference.setUserName(LoginActivity.this, IDT.getText().toString());
                SaveSharedPreference.setUserPwd(LoginActivity.this, PWT.getText().toString());
                //Toast.makeText(LoginActivity.this, SaveSharedPreference.getUserName(this), Toast.LENGTH_SHORT).show();

            } else {
                //로그인성공
                Toast.makeText(LoginActivity.this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                //Toast.makeText(LoginActivity.this,)

            }
            c06.close();
        }

    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME , null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                String DROP_SQL = "drop table if exists " + TABLE_NAME1;
                db.execSQL(DROP_SQL);
            } catch (Exception ex) {
                Log.e(TAG, "Exception in DROP_SQL", ex);
            }

            String CREATE_SQL = "create table " + TABLE_NAME1 + "("
                    + "USER_ID text PRIMARY KEY, "
                    + "USER_PWD text, "
                    + "USER_EMAIL text, "
                    + "USER_GENDER text, "
                    + "USER_AGE text)";

            String CREATE_SQL2 = "create table " + TABLE_NAME2 + "("
                    + "EXERCISE_ID integer PRIMARY KEY, "
                    + "EXERCISE_NAME text, "
                    + "EXERCISE_LEVEL text, "
                    + "EXERCISE_EXPLAIN text, "
                    + "EXERCISE_IMAGE integer, "
                    + "EXERCISE_PART text, "
                    + "EXERCISE_TIME integer)";

            String CREATE_SQL3 = "create table " + TABLE_NAME3 + "("
                    + "PLAN_ID text PRIMARY KEY, "
                    + "PLAN_TIME text, "
                    + "USER_ID text, "
                    + "EXERCISE_ID text,"
                    + "constraint fk_user1_id foreign key(user_id) references user(user_id), "
                    + "constraint fk_exercise1_id foreign key(exercise_id) references exercise(exercise_id))";

            String CREATE_SQL4 = "create table " + TABLE_NAME4 + "("
                    + "RECORD_ID text PRIMARY KEY, "
                    + "RECORD_TIME text, "
                    + "USER_ID text, "
                    + "EXERCISE_ID text,"
                    + "constraint fk_user2_id foreign key(user_id) references user(user_id), "
                    + "constraint fk_exercise2_id foreign key(exercise_id) references exercise(exercise_id))";

            try {

                db.execSQL(CREATE_SQL);
                db.execSQL(CREATE_SQL2);
                db.execSQL(CREATE_SQL3);
                db.execSQL(CREATE_SQL4);
            } catch (Exception ex) {
                Log.e(TAG, "Exception in CREATE_SQL", ex);
            }

            try {
                db.execSQL("insert into " + TABLE_NAME2 + "(EXERCISE_ID,EXERCISE_NAME,EXERCISE_LEVEL,EXERCISE_EXPLAN,EXERCISE_PART,EXERCISE_TIME)values(1, '덩키킥', '입문', '1. 손목이랑 어깨랑 일직선상에 오도록 잡은 후, 무릎으로 가운데 중심을 잡는다.\n" +
                        "2. 발 뒤꿈치가 천장을 향해서 올라가는 느낌으로 한다.\n" +
                        "3. (포인트)코어에 힘을 잡고 엉덩이 근육이 이완하고 수축하는 것에 집중한다.\n" +
                        "4. (주의) 허리가 재껴지거나 구부러지지 않도록 한다.\n" +
                        "횟수 - 오른쪽 12회 / 왼쪽 12회 * 3세트\n', '하체', 5 )");
                db.execSQL("insert into " + TABLE_NAME2 + "(EXERCISE_ID,EXERCISE_NAME,EXERCISE_LEVEL,EXERCISE_EXPLAN,EXERCISE_PART,EXERCISE_TIME)values(2, '트라이익스텐션', '입문', '1. 양손을 모아서 이완했다가 수축을 반복한다. \n" +
                        "2. (주의) 팔꿈치가 너무 벌어지지 않도록 귀하고 일직선상에 올 수 있도록 배치해준다.\n" +
                        "3. 팔꿈치를 편하게 구부렸다가 삼두 힘으로 쭉 끌어올려준다.\n" +
                        "횟수 - 12회 * 3세트\n', '상체', 5 )");
                db.execSQL("insert into " + TABLE_NAME2 + "(EXERCISE_ID,EXERCISE_NAME,EXERCISE_LEVEL,EXERCISE_EXPLAN,EXERCISE_PART,EXERCISE_TIME)values(3, '스쿼트', '입문', '1. 골반 너비보다 한 발자국 바깥쪽으로 벌려서 선 후 앉았다 일어날 때 편안한 자세로 발을 벌려도 좋다.\n" +
                        "2. (주의) 이 상태로 앉았다 일어났다를 진행하는데 무릎이 앞으로 나가면 관절을 사용하게 되고 앞 허벅지 근육을 사용하게 된다.\n" +
                        "3. (포인트) 중심을 발바닥 가운데에 잡게 하고 엉덩이를 너무 뒤로 뺀다는 느낌이 들도록 앉는다. 무릎이 발가락 앞으로 나가지 않게 한다.\n" +
                        "4. 가볍게 손 앞에로 나란히 하거나 잡은 후 내려가서 엉덩이 근육이 쫙 늘어나는 느낌을 받고 이 상태에서 발뒤꿈치로 일어나면서 업한다. 마지막까지 괄약근을 조여준다.\n" +
                        "횟수 - 12회 * 3세트\n', '하체', 10 )");

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
}
