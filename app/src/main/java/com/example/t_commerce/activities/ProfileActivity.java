package com.example.t_commerce.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.t_commerce.R;
import com.example.t_commerce.models.StudentDetails;

public class ProfileActivity extends AppCompatActivity {

    private StudentDetails selectedStudent;
    private TextView profileName, profileSchool, profileFees, profileStandard, profileAmtDue, profileParent, profileBoard;
    private LinearLayout profileWhatsApp, profileContact;
    private Button downloadBtn;
    private RecyclerView paymentHistoryRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        final Animation bounce = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
        Toolbar mTool = (Toolbar) findViewById(R.id.toolbarProfile);
        setSupportActionBar(mTool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent idReceived = getIntent();
        String id = idReceived.getStringExtra("selectedStudent");
        selectedStudent = getSelectedStudent(id);
        getSupportActionBar().setTitle(selectedStudent.getName());
        initViews();

        profileWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.whatsapp.com/send?phone=91"+selectedStudent.getWhatsAppNumber().trim();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        profileContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + selectedStudent.getMobileContact().trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadBtn.startAnimation(bounce);
            }
        });
    }

    private StudentDetails getSelectedStudent(String id)
    {
        StudentDetails finding = null;
        for(StudentDetails element : MainActivity.Students)
            if(element.getId().equals(id))
                finding = element;
        return  finding;
    }

    private void initViews()
    {
        profileName = findViewById(R.id.profileName);
        profileStandard = findViewById(R.id.profileClass);
        profileSchool = findViewById(R.id.profileSchool);
        profileFees = findViewById(R.id.profileFees);
        profileAmtDue = findViewById(R.id.profileAmtDue);
        profileParent = findViewById(R.id.profileParent);
        profileBoard = findViewById(R.id.profileBoard);
        profileWhatsApp = findViewById(R.id.profileWhatsApp);
        profileContact = findViewById(R.id.profileContact);
        downloadBtn = findViewById(R.id.downloadPdf);
        paymentHistoryRecyclerView = findViewById(R.id.recyclerViewPaymentHistory);
        profileName.setText(selectedStudent.getName());
        profileFees.setText(String.valueOf(selectedStudent.getFees()));
        profileAmtDue.setText(String.valueOf(selectedStudent.getAmountDue()));
        profileStandard.setText(String.valueOf(selectedStudent.getSClass()));
        profileParent.setText(selectedStudent.getParent());
        profileBoard.setText(selectedStudent.getBoard());
    }


}