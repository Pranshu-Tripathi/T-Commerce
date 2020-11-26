package com.example.t_commerce.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.t_commerce.R;
import com.example.t_commerce.adaptor.StandardListAdaptor;
import com.example.t_commerce.models.StandardListModel;
import com.example.t_commerce.models.StudentDetails;

import java.util.ArrayList;

public class StudentDiaryActivity extends AppCompatActivity {

    private CardView cardView;
    private RecyclerView recyclerView;
    private ArrayList<StandardListModel> Standards;
    private ArrayList<StudentDetails> details;
    public static int previousSelected = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_diary);

        Toolbar mTool = (Toolbar) findViewById(R.id.toolbarStudentDiary);
        setSupportActionBar(mTool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Student Diary");

        cardView = findViewById(R.id.groupStandardList);
        cardView.setVisibility(View.INVISIBLE);
        recyclerView = findViewById(R.id.standardRecyclerView);
        details = MainActivity.Students;
        Log.i("copied Details",details.get(0).getId());


        loadStandardData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        StandardListAdaptor adaptor = new StandardListAdaptor(this,Standards);

        recyclerView.setAdapter(adaptor);

        adaptor.notifyDataSetChanged();


        loadAnimations();





    }

    public void loadAnimations()
    {
        cardView.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        cardView.startAnimation(animation);
    }

    public void loadStandardData()
    {
        Standards = new ArrayList<>();

        for(int i = -1 ; i <= 10 ; i++)
        {
            StandardListModel s = new StandardListModel( i,false);
            Standards.add(s);
        }

    }

}