package com.example.t_commerce.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.t_commerce.R;
import com.example.t_commerce.models.PaymentHistory;
import com.example.t_commerce.models.StudentDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tomer.fadingtextview.FadingTextView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class PaymentActivity extends AppCompatActivity {


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

    private FirebaseFirestore db;
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
    private Calendar mCurrentDateTime;
    private Date selectedDate;
    private int cur_date,cur_month,cur_year,hour,min;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mCurrentDateTime = Calendar.getInstance();
        cur_date = mCurrentDateTime.get(Calendar.DAY_OF_MONTH);
        cur_month = mCurrentDateTime.get(Calendar.MONTH);
        cur_year = mCurrentDateTime.get(Calendar.YEAR);
        hour = mCurrentDateTime.get(Calendar.HOUR_OF_DAY);
        min = mCurrentDateTime.get(Calendar.MINUTE);
        selectedDate = new Date();

        Log.i("Date",String.valueOf(cur_date));
        Log.i("Month",String.valueOf(cur_month));
        Log.i("Year",String.valueOf(cur_year));

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

        Objects.requireNonNull(textInputLayout.getEditText()).setText(String.valueOf(selectedStudent.getFees()));

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

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialogShow();
            }
        });

        timePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeDialogShow();
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
            textInputLayout.setError("Fill with caution");
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
            long Amount =   Long.parseLong(String.valueOf(Objects.requireNonNull(textInputLayout.getEditText()).getText()));
            String Mode = displayMode.getText().toString();
            PaymentHistory newPayment = null;

            newPayment = new PaymentHistory(selectedDate,Amount,Mode);
            paymentDetails.add(newPayment);
            selectedStudent.setPayments(paymentDetails);

            Long amtDue = selectedStudent.getAmountDue();
            amtDue -= Amount;
            selectedStudent.setAmountDue(amtDue);
            HashMap<String,Object> detail = new HashMap<>();
            detail.put(KEY_NAME,selectedStudent.getName());
            detail.put(KEY_ID,selectedStudent.getId());
            detail.put(KEY_AMTDUE,selectedStudent.getAmountDue());
            detail.put(KEY_PARENT,selectedStudent.getParent());
            detail.put(KEY_PAYMENT,selectedStudent.getPayments());
            detail.put(KEY_SCHOOL,selectedStudent.getSchool());
            detail.put(KEY_CLASS,selectedStudent.getSClass());
            detail.put(KEY_WTSAPP,selectedStudent.getWhatsAppNumber());
            detail.put(KEY_CONTACT,selectedStudent.getMobileContact());
            detail.put(KEY_FEES,selectedStudent.getFees());
            detail.put(KEY_BOARD,selectedStudent.getBoard());

            updateDataBase(detail);

        }
        else
        {
            enableStatus(true);
        }
    }

    private boolean validateDate()
    {
        if(!dateText.getText().equals("Date"))
        {
            return true;
        }
        Toast.makeText(this, "Date Not Selected", Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean validateTime()
    {
        if(!timeText.getText().equals("Time"))
        {
            return true;
        }
        Toast.makeText(this, "Time Not Selected", Toast.LENGTH_SHORT).show();
        return false;
    }


    private void enableStatus(boolean status)
    {
        makePaymentRequestButton.setEnabled(status);
        textInputLayout.setEnabled(status);
        datePickerButton.setEnabled(status);
        timePickerButton.setEnabled(status);
        switchMaterial.setEnabled(status);

    }

    private void dateDialogShow()
    {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                selectedDate.setDate(dayOfMonth);
                selectedDate.setMonth(month);
                selectedDate.setYear(year);
                selectedDate.setYear(2020);

                String dateString = "";
                String monthString = "";

                if(selectedDate.getDate() < 9)
                    dateString+= "0"+selectedDate.getDate();
                else
                    dateString+= selectedDate.getDate();

                if(selectedDate.getMonth() + 1< 9)
                    monthString+= "0"+String.valueOf(selectedDate.getMonth()+1);
                else
                    monthString+= String.valueOf(selectedDate.getMonth()+1);

                String s = dateString + " / " + monthString+ " / " + String.valueOf(selectedDate.getYear());
                dateText.setText(s);

            }
        },cur_year,cur_month,cur_date);
        dialog.show();
    }

    private void timeDialogShow()
    {
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                selectedDate.setHours(hourOfDay);
                selectedDate.setMinutes(minute);
                String hourString = "";
                String minString = "";

                Log.i("SELECTED DATE",selectedDate.toString());

                if(selectedDate.getHours() < 9)
                    hourString += "0"+selectedDate.getHours();
                else
                    hourString += selectedDate.getHours();
                if(minute < 9)
                    minString += "0"+selectedDate.getMinutes();
                else
                    minString += selectedDate.getMinutes();

                String s = hourString + " : " + minString;
                timeText.setText(s);
            }
        },hour,min,false);
        dialog.show();
    }

    private void updateDataBase(HashMap<String,Object> hashMap)
    {
        db = FirebaseFirestore.getInstance();
        db.collection("Students").document(selectedStudent.getId())
                .set(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PaymentActivity.this, "Payment Updated", Toast.LENGTH_SHORT).show();
                        loadingIndicatorView.hide();
                        fadingTextView.stop();
                        fadingTextView.setVisibility(View.INVISIBLE);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PaymentActivity.this, "Payment Denied!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}