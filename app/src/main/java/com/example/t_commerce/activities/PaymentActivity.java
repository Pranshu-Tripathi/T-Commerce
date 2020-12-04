package com.example.t_commerce.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.t_commerce.R;
import com.example.t_commerce.models.PaymentHistory;
import com.example.t_commerce.models.StudentDetails;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.tomer.fadingtextview.FadingTextView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Objects;

public class PaymentActivity extends AppCompatActivity {


    private ArrayList<StudentDetails> details;
    private StudentDetails selectedStudent;
    private AVLoadingIndicatorView loadingIndicatorView;
    private FadingTextView fadingTextView;
    private TextInputLayout textInputLayout;
    private TextView dateText,timeText,displayMode;
    private ImageButton datePickerButton,timePickerButton;
    private Button makePaymentRequestButton;
    private CardView cardView;
    private SwitchMaterial switchMaterial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        final Animation bounce = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
//        final Animation bounceFab = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bouncefab);
        Toolbar mTool = (Toolbar) findViewById(R.id.toolbarPayment);
        setSupportActionBar(mTool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent idReceived = getIntent();
        String id = idReceived.getStringExtra("selectedStudent");
        if(id == null)
        {
            Toast.makeText(this, "Some Issue Occurred", Toast.LENGTH_SHORT).show();
            finish();
        }
        details = MainActivity.Students;
        selectedStudent = getSelectedStudent(id);
        if(selectedStudent == null)
        {
            Toast.makeText(this, "No Such Id Exist", Toast.LENGTH_SHORT).show();
            finish();
        }
        getSupportActionBar().setTitle(selectedStudent.getName());

        loadingIndicatorView = findViewById(R.id.aviRequestPayment);
        fadingTextView = findViewById(R.id.fadingTextViewPayment);
        cardView = findViewById(R.id.cardViewDetails);
        textInputLayout = findViewById(R.id.amountTextInputLayout);
        dateText = findViewById(R.id.displayDate);
        timeText = findViewById(R.id.displayTime);
        datePickerButton = findViewById(R.id.datePickerBtn);
        timePickerButton = findViewById(R.id.timePickerBtn);
        displayMode = findViewById(R.id.displayMode);
        switchMaterial = findViewById(R.id.modeToggle);
        makePaymentRequestButton = findViewById(R.id.makePaymentRequestBtn);
        cardView.setVisibility(View.INVISIBLE);
        makePaymentRequestButton.setVisibility(View.INVISIBLE);
        fadingTextView.stop();
        fadingTextView.setVisibility(View.INVISIBLE);

        animationDisplay();

        makePaymentRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePaymentRequestButton.startAnimation(bounce);
                makePaymentRequest();
            }
        });

        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    displayMode.setText("Online");
                }
                else
                {
                    displayMode.setText("Offline");
                }
            }
        });

    }

    public StudentDetails getSelectedStudent(String id)
    {
        StudentDetails ss = null;
        for(int i = 0 ; i < details.size() ; i++)
        {
            if(details.get(i).getId().equals(id))
            {
                ss = details.get(i);
                break;
            }
        }
        return ss;
    }

    public void animationDisplay()
    {
        final Animation fade_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        cardView.setVisibility(View.VISIBLE);
        makePaymentRequestButton.setVisibility(View.VISIBLE);
        cardView.startAnimation(fade_in);
        makePaymentRequestButton.startAnimation(fade_in);
    }

    boolean validateAmount()
    {
        if(Objects.requireNonNull(textInputLayout.getEditText()).getText().length() > 4 || textInputLayout.getEditText().getText().length() == 0)
        {
            Toast.makeText(this, "Amount To Big/Small", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void makePaymentRequest()
    {
        if(validateAmount() && validateDate() && validateTime())
        {
            loadingIndicatorView.setVisibility(View.VISIBLE);
            loadingIndicatorView.show();
            fadingTextView.setVisibility(View.VISIBLE);
            fadingTextView.restart();
            enableStatus(false);

            ArrayList<PaymentHistory> paymentDetails = selectedStudent.getPayments();

            //TODO : Make Payment Process Here.
        }
        else
        {
            enableStatus(true);
        }
    }

    private boolean validateDate()
    {
        return true;
    }

    private boolean validateTime()
    {
        return true;
    }


    private void enableStatus(boolean status)
    {
        makePaymentRequestButton.setEnabled(status);
        textInputLayout.setEnabled(status);
        datePickerButton.setEnabled(status);
        timePickerButton.setEnabled(status);
        switchMaterial.setEnabled(status);

    }


}