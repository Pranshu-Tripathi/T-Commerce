package com.example.t_commerce.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.t_commerce.R;
import com.example.t_commerce.adaptor.StandardListAdaptor;
import com.example.t_commerce.adaptor.StudentListAdaptor;
import com.example.t_commerce.models.PaymentHistory;
import com.example.t_commerce.models.StandardListModel;
import com.example.t_commerce.models.StudentDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class StudentDiaryActivity extends AppCompatActivity {

    private CardView cardViewStandardGroup;
    private CardView cardViewStudentListGroup;
    private RecyclerView recyclerViewStandard;
    private RecyclerView recyclerViewStudents;
    private AVLoadingIndicatorView swipeLoader;
    private ArrayList<StandardListModel> Standards;
    private ArrayList<StudentDetails> details;
    private SwitchMaterial cbseSwitch, icseSwitch, sscSwitch;
    private SwipeRefreshLayout swipeRefreshLayout;
    private StandardListAdaptor adaptor;
    private StudentListAdaptor studentListAdaptor;

    FirebaseFirestore db;

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

    public static int previousSelected = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_diary);

        Toolbar mTool = (Toolbar) findViewById(R.id.toolbarStudentDiary);
        setSupportActionBar(mTool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Student Diary");

        db = FirebaseFirestore.getInstance();
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeLoader = findViewById(R.id.aviStudentList);
        swipeLoader.hide();
        cardViewStandardGroup = findViewById(R.id.groupStandardList);
        cardViewStandardGroup.setVisibility(View.INVISIBLE);
        cardViewStudentListGroup = findViewById(R.id.groupStudentList);
        cardViewStudentListGroup.setVisibility(View.INVISIBLE);
        recyclerViewStandard = findViewById(R.id.standardRecyclerView);
        recyclerViewStudents = findViewById(R.id.listStudentRecyclerView);
        cbseSwitch = findViewById(R.id.cbseFilterSwitch);
        icseSwitch = findViewById(R.id.icseFilterSwitch);
        sscSwitch = findViewById(R.id.sscFilterSwitch);

        details = MainActivity.Students;
        loadStandardData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewStandard.setLayoutManager(linearLayoutManager);
        adaptor = new StandardListAdaptor(this,Standards);
        recyclerViewStandard.setAdapter(adaptor);
        adaptor.notifyDataSetChanged();

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewStudents.setLayoutManager(linearLayoutManager1);

        studentListCreation(details);

        loadAnimations();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RenderScreen();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        cbseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    icseSwitch.setChecked(false);
                    sscSwitch.setChecked(false);

                }
            }
        });


        icseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    cbseSwitch.setChecked(false);
                    sscSwitch.setChecked(false);
                }
            }
        });


        sscSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    cbseSwitch.setChecked(false);
                    icseSwitch.setChecked(false);
                }
            }
        });

        studentListAdaptor.onClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = recyclerViewStudents.getChildAdapterPosition(v);
                Toast.makeText(StudentDiaryActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void loadAnimations()
    {
        cardViewStandardGroup.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        cardViewStandardGroup.startAnimation(animation);
        cardViewStudentListGroup.setVisibility(View.VISIBLE);
        cardViewStudentListGroup.startAnimation(animation);
    }

    public void loadStandardData()
    {
        Standards = new ArrayList<>();

        for(int i = -1 ; i <= 10 ; i++)
        {
            StandardListModel s;
            if(i == -1)
                s = new StandardListModel(i,true);
            else
                s = new StandardListModel(i,false);
            Standards.add(s);
        }

    }

    public void studentListCreation(ArrayList<StudentDetails> detail)
    {
        studentListAdaptor = new StudentListAdaptor(this,detail);
        recyclerViewStudents.setAdapter(studentListAdaptor);
        studentListAdaptor.notifyDataSetChanged();
    }


    public void RenderScreen()
    {
        details.clear();
        cardViewStudentListGroup.setVisibility(View.INVISIBLE);
        swipeLoader.setVisibility(View.VISIBLE);
        swipeLoader.show();
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
                                StudentDetails det = new StudentDetails(id,name,parentname,contact,whatsapp,amountDue,fees,
                                        Class,school,board,histories);
                                details.add(det);

                            }

                            Log.i("LLLLLLLLLLLLLLLLLL",String.valueOf(details.size()));

                            filteredList();
                        }
                    }
                });
    }

    public void filteredList()
    {
        swipeLoader.hide();
        ArrayList<StudentDetails> filteredList = new ArrayList<>();
        ArrayList<StudentDetails> NoBoardFilterList = new ArrayList<>();
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        cardViewStudentListGroup.setVisibility(View.VISIBLE);
        cardViewStudentListGroup.startAnimation(animation);

        int x = getBoardFilter();
        Long stdSelected = adaptor.getStandardSelected();


        if(stdSelected == -1)
        {
            NoBoardFilterList.addAll(details);


            Log.i("Seleceted","true");
            if(x == 0)
                filteredList.addAll(NoBoardFilterList);
            else if(x == 1)
            {
                for(int i = 0 ; i < NoBoardFilterList.size() ; i++)
                {
                    if(NoBoardFilterList.get(i).getBoard().toLowerCase().equals("cbse"))
                        filteredList.add(NoBoardFilterList.get(i));
                }
            }
            else if(x == 2)
            {
                for(int i = 0 ; i < NoBoardFilterList.size() ; i++)
                {
                    if(NoBoardFilterList.get(i).getBoard().toLowerCase().equals("icsc"))
                        filteredList.add(NoBoardFilterList.get(i));
                }
            }
            else if(x == 3)
            {
                for(int i = 0 ; i < NoBoardFilterList.size() ; i++)
                {
                    if(NoBoardFilterList.get(i).getBoard().toLowerCase().equals("ssc"))  filteredList.add(NoBoardFilterList.get(i));
                }
            }
        }
        else
        {
            for(int i = 0; i < details.size() ; i++)
            {
                if(details.get(i).getSClass().equals(stdSelected))
                {
                    NoBoardFilterList.add(details.get(i));
                }
            }

            if(x == 0)
                filteredList.addAll(NoBoardFilterList);
            else if(x == 1)
            {
                for(int i = 0 ; i < NoBoardFilterList.size() ; i++)
                {
                    if(NoBoardFilterList.get(i).getBoard().toLowerCase().equals("cbse"))
                        filteredList.add(NoBoardFilterList.get(i));
                }
            }
            else if(x == 2)
            {
                for(int i = 0 ; i < NoBoardFilterList.size() ; i++)
                {
                    if(NoBoardFilterList.get(i).getBoard().toLowerCase().equals("icsc"))
                        filteredList.add(NoBoardFilterList.get(i));
                }
            }
            else if(x == 3)
            {
                for(int i = 0 ; i < NoBoardFilterList.size() ; i++)
                {
                    if(NoBoardFilterList.get(i).getBoard().toLowerCase().equals("ssc"))
                        filteredList.add(NoBoardFilterList.get(i));
                }
            }

        }

        studentListCreation(filteredList);

    }

    public int getBoardFilter()
    {
        int x = 0;
        if(cbseSwitch.isChecked())  x+=1;
        else if(icseSwitch.isChecked()) x+=2;
        else if(sscSwitch.isChecked()) x+=3;
        return x;
    }
}