package com.example.t_commerce.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.t_commerce.R;
import com.example.t_commerce.models.PaymentHistory;
import com.example.t_commerce.models.StudentDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.tomer.fadingtextview.FadingTextView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    String KEY_NAME = "name";
    String KEY_ID = "id";
    String KEY_PARENT = "parent";
    String KEY_CLASS = "Class";
    String KEY_PAYMENT = "PaymentHistory";
    String KEY_SCHOOL = "School";
    String KEY_AMTDUE = "amountDue";
    String KEY_FEES = "fees";
    String KEY_CONTACT = "mobileContact";
    String KEY_WTSAPP = "WhatsAppNumber";
    String KEY_BOARD = "board";

    private AVLoadingIndicatorView avl;
    private FadingTextView fadingTextView;
    private CardView cardView;
    private FloatingActionButton fab;
    private Button studentDiary, timeTable;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String[] texts = {"Gathering Resources",
            "Checking All the Dates",
            "Marking the Calendar",
            "Generating Buttons",
            "Entering Cheat Codes",
            "Downloading Profiles",
            "Leaking Nuclear Codes"};

    private FirebaseFirestore db;

    public static ArrayList<StudentDetails> Students = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mTool = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mTool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("T-Commerce");

        avl = findViewById(R.id.avi);
        avl.show();
        avl.setVisibility(View.VISIBLE);

        fadingTextView = findViewById(R.id.fadingTextView);
        fadingTextView.setVisibility(View.INVISIBLE);
        fadingTextView.setTexts(texts);
        fadingTextView.restart();
        fadingTextView.setVisibility(View.VISIBLE);
        fab = findViewById(R.id.addStudentsFab);
        fab.setVisibility(View.INVISIBLE);
        cardView = findViewById(R.id.cardView);
        cardView.setVisibility(View.INVISIBLE);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshMain);

        studentDiary = findViewById(R.id.studentDiary);
        timeTable = findViewById(R.id.TimeTable);

        studentDiary.setVisibility(View.INVISIBLE);
        timeTable.setVisibility(View.INVISIBLE);


        LoadStudents();

        studentDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentButton();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                    Students.clear();
                    studentDiary.setVisibility(View.INVISIBLE);
                    timeTable.setVisibility(View.INVISIBLE);
                    fab.setVisibility(View.INVISIBLE);
                    avl.show();
                    avl.setVisibility(View.VISIBLE);
                    cardView.setVisibility(View.INVISIBLE);
                    LoadStudents();
                    swipeRefreshLayout.setRefreshing(false);

            }
        });

        timeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               timeTableButton();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabButton();
            }
        });
    }

    public void LoadStudents()
    {
        db = FirebaseFirestore.getInstance();
        db.collection("Students")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null)
                        {
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                            {
                                String name = documentSnapshot.getString(KEY_NAME);
                                String id = documentSnapshot.getString(KEY_ID);
                                String parentname = documentSnapshot.getString(KEY_PARENT);
                                String school = documentSnapshot.getString(KEY_SCHOOL);
                                String contact = documentSnapshot.getString(KEY_CONTACT);
                                String whatsapp = documentSnapshot.getString(KEY_WTSAPP);
                                ArrayList<PaymentHistory> histories = new ArrayList<>();
                                ArrayList<HashMap<String,Object>> k = (ArrayList<HashMap<String, Object>>) documentSnapshot.get(KEY_PAYMENT);
                                for(HashMap<String,Object> element : k)
                                {
                                    Long Amount = (Long) element.get("Amount");
                                    Timestamp timestamp = (Timestamp) element.get("Date");
                                    assert timestamp != null;
                                    Date date = timestamp.toDate();
                                    String Mode = (String) element.get("Mode");
                                    PaymentHistory p = new PaymentHistory(date,Amount,Mode);
                                    histories.add(p);
                                }

                                Long Class = (Long) documentSnapshot.get(KEY_CLASS);
                                Long amountDue = (Long) documentSnapshot.get(KEY_AMTDUE);
                                Long fees = (Long) documentSnapshot.get(KEY_FEES);
                                String board = (String) documentSnapshot.get(KEY_BOARD);
                                if(board == null)
                                {
                                    Log.i("NULL", "BOARD");
                                }
                                else
                                {
                                    Log.i("NOT NULL", "BOARD");
                                }
                                StudentDetails details = new StudentDetails(id,name,parentname,contact,whatsapp,amountDue,fees,
                                        Class,school,board,histories);
                                Students.add(details);

                                avl.hide();
                                fadingTextView.setVisibility(View.INVISIBLE);
                                fadingTextView.stop();

                                showLaterDesign();
                            }
                        }
                    }
                });
    }

    public void showLaterDesign()
    {
        fab.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.VISIBLE);
        studentDiary.setVisibility(View.VISIBLE);
        timeTable.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        fab.startAnimation(animation);
        cardView.startAnimation(animation);
        studentDiary.startAnimation(animation);
        timeTable.startAnimation(animation);

        TextView textView = findViewById(R.id.totalAmountDue);
        TextView textView1 = findViewById(R.id.totalStudentsCount);
        TextView textView2 = findViewById(R.id.cbseStudents);
        TextView textView3 = findViewById(R.id.sscStudents);
        TextView textView4 = findViewById(R.id.icseStudents);
        TextView textView5 = findViewById(R.id.totalBatches);

        textView5.setText("4");
        int cbseCount = 0,sscCount = 0, icseCount = 0;
        textView1.setText(String.valueOf(Students.size()));

        int totalAmountDue = 0;
        for(int i = 0 ; i < Students.size(); i++)
        {
            Log.i("STUDENTS",Students.get(i).getBoard());
            if(Students.get(i).getBoard().toLowerCase().equals("cbse")) cbseCount++;
            else if(Students.get(i).getBoard().toLowerCase().equals("ssc")) sscCount++;
            else  if (Students.get(i).getBoard().toLowerCase().equals("icsc")) icseCount++;

            for(int j = 0; j < Students.get(i).getPayments().size(); j++)
            {
                totalAmountDue += Students.get(i).getPayments().get(j).getAmount();
            }
        }

        textView.setText(String.valueOf(totalAmountDue));
        textView2.setText(String.valueOf(cbseCount));
        textView3.setText(String.valueOf(sscCount));
        textView4.setText(String.valueOf(icseCount));
    }

    public void studentButton()
    {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
        studentDiary.startAnimation(animation);
        Log.i("Student button","Pressed");

        Intent intent = new Intent(MainActivity.this,StudentDiaryActivity.class);

        startActivity(intent);
    }

    public void timeTableButton()
    {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
        timeTable.startAnimation(animation);
        Toast.makeText(this, "TimeTable", Toast.LENGTH_SHORT).show();
    }

    public void fabButton()
    {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bouncefab);
        fab.startAnimation(animation);
        Intent intent = new Intent(this,AddStudentActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchmenu,menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        return super.onCreateOptionsMenu(menu);
    }
}