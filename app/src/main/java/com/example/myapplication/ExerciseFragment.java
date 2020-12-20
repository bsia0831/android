package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExerciseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExerciseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExerciseFragment newInstance(String param1, String param2) {
        ExerciseFragment fragment = new ExerciseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private static String DATABASE_NAME = null;
    private static String TABLE_NAME1 = "USER";
    private static String TABLE_NAME2 = "EXERCISE";
    private static int DATABASE_VERSION = 1;
    private ExerciseFragment.DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    public static final String TAG = "MainActivity";
    private static String U_ID;
    private static String E_ID;

    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        DATABASE_NAME = "exercise";

        boolean isOpen = openDatabase();
        if (isOpen) {
            executeRawQuery();
            //executeRawQueryParam();
        }


    }

    private boolean openDatabase() {
        dbHelper = new ExerciseFragment.DatabaseHelper(getActivity());
        db = dbHelper.getWritableDatabase();
        return true;
    }


    private void executeRawQuery() {
        Cursor c1 = db.rawQuery("select count(*) as Total from " + TABLE_NAME1, null);
        c1.moveToNext();
        c1.close();
    }

    private void executeRawQueryParam() {

    }


    private ArrayAdapter areaAdapter;
    private Spinner areaSpinner;
    private ArrayAdapter difficultyAdapter;
    private Spinner difficultySpinner;
    private ArrayAdapter timeAdapter;
    private Spinner timeSpinner;

    private String excerciseArea = "";
    private String excerciseDifficulty = "";
    private String excerciseTime = "";

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);

        areaSpinner = (Spinner) getView().findViewById(R.id.areaSpinner);
        difficultySpinner = (Spinner) getView().findViewById(R.id.difficultySpinner);
        timeSpinner = (Spinner) getView().findViewById(R.id.timeSpinner);

        listView = (ListView)getView().findViewById(R.id.listView);

        ArrayList<String> items = new ArrayList<>();
        items.add("트라이익스텐션");
        items.add("덩키킥");
        items.add("스쿼트");
        items.add("암 푸쉬 다운");
        items.add("리버스 크런치");
        items.add("프론트 런지");
        items.add("크로스 잭");

        CustomAdapter adapter = new CustomAdapter(getActivity(), 0, items);
        listView.setAdapter(adapter);

        areaAdapter = areaAdapter.createFromResource(getActivity(), R.array.area, android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(areaAdapter);
        difficultyAdapter = difficultyAdapter.createFromResource(getActivity(), R.array.difficulty, android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(difficultyAdapter);
        timeAdapter = timeAdapter.createFromResource(getActivity(), R.array.time, android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);



    }

    private class CustomAdapter extends ArrayAdapter<String> {
        private ArrayList<String> items;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.listview_item, null);
            }

            final ImageView imageView = (ImageView) v.findViewById(R.id.imageview);

            if ("덩키킥".equals(items.get(position)))
                imageView.setImageResource(R.drawable.a_dung);
            else if ("트라이익스텐션".equals(items.get(position)))
                imageView.setImageResource(R.drawable.tryex1);
            else if ("스쿼트".equals(items.get(position)))
                imageView.setImageResource(R.drawable.sq1);
            else if ("암 푸쉬 다운".equals(items.get(position)))
                imageView.setImageResource(R.drawable.ampush);
            else if ("리버스 크런치".equals(items.get(position)))
                imageView.setImageResource(R.drawable.reverse);
            else if ("프론트 런지".equals(items.get(position)))
                imageView.setImageResource(R.drawable.frontlung);
            else if ("크로스 잭".equals(items.get(position)))
                imageView.setImageResource(R.drawable.crossjack);

            final TextView textView = (TextView) v.findViewById(R.id.textView);
            textView.setText(items.get(position));

            final String text = items.get(position);

            final Button button = (Button) v.findViewById(R.id.button);
            //button.setText(items.get(position));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ExplanActivity.class);
                    intent.putExtra("name", button.getText().toString());
                    startActivityForResult(intent, 1);

                }
            });
            return v;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise, container, false);
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
}