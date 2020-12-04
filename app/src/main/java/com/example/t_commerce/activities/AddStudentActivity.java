package com.example.t_commerce.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.t_commerce.R;
import com.example.t_commerce.models.PaymentHistory;
import com.example.t_commerce.models.StudentDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wang.avi.AVLoadingIndicatorView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class AddStudentActivity extends AppCompatActivity {

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


    final String[] BOARD_OPTION= {"cbse", "icsc", "ssc"};
    final   String[] STANDARD_OPTION = {"P","1","2","3","4","5","6","7","8","9","10"};

    private ImageButton incrementStandard,incrementBoard;
    private ImageButton decrementStandard,decrementBoard;
    private TextView StandardTextView,BoardTextView;
    private TextInputLayout name, school, parent, whatsApp, contact, fees;
    private FloatingActionButton floatingActionButton;
    private LinearLayout parentL,topGrpL;
    private AVLoadingIndicatorView avl;

    private int xBoard = -1;
    private int xStandard = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        Toolbar mTool = (Toolbar) findViewById(R.id.toolbarAddStudent);
        setSupportActionBar(mTool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Student");


        final Animation bounce = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bouncefab);

        incrementBoard = findViewById(R.id.incrementBoard);
        incrementStandard = findViewById(R.id.incrementStandard);
        decrementBoard = findViewById(R.id.decrementBoard);
        decrementStandard = findViewById(R.id.decrementStandard);
        StandardTextView = findViewById(R.id.StandardTextView);
        BoardTextView = findViewById(R.id.BoardTextView);
        floatingActionButton = findViewById(R.id.addStudentRequestFab);
        name = findViewById(R.id.textInputLayoutNameStudent);
        school = findViewById(R.id.textInputLayoutSchoolStudent);
        parent = findViewById(R.id.textInputLayoutParentStudent);
        whatsApp = findViewById(R.id.textInputLayoutWhatsApp);
        contact = findViewById(R.id.textInputLayoutContactStudent);
        fees = findViewById(R.id.textInputLayoutFeesStudent);
        parentL = findViewById(R.id.parentLinearLayout);
        topGrpL = findViewById(R.id.topGroup);
        avl = findViewById(R.id.aviRequestAddition);

        disappearedStart();

        animationStart();

        incrementBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementBoard.startAnimation(bounce);
                xBoard = (xBoard + 1) % 3;
                BoardTextView.setText(BOARD_OPTION[xBoard].toUpperCase());
            }
        });

        decrementBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementBoard.startAnimation(bounce);
                if(xBoard == 0 || xBoard == -1)
                {
                    xBoard = 2;
                }
                else
                {
                    xBoard = xBoard - 1;
                }
                BoardTextView.setText(BOARD_OPTION[xBoard].toUpperCase());
            }
        });

        incrementStandard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementStandard.startAnimation(bounce);
                xStandard = ( xStandard + 1 ) % 11;
                StandardTextView.setText("STD : " + STANDARD_OPTION[xStandard]);
            }
        });

        decrementStandard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(xStandard == 0 || xStandard == -1)
                {
                    xStandard = 10;
                }
                else
                {
                    xStandard--;
                }
                StandardTextView.setText("Std : " + STANDARD_OPTION[xStandard]);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionButton.startAnimation(bounce);
                avl.setVisibility(View.VISIBLE);
                avl.show();
                addStudentProcessRequest();
            }
        });

    }

    public void disappearedStart()
    {
        parentL.setVisibility(View.INVISIBLE);
    }

    public void animationStart()
    {
        parentL.setVisibility(View.VISIBLE);
        Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        Animation translate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_x);
        parentL.startAnimation(fadeIn);
        name.startAnimation(translate);
        school.startAnimation(translate);
        parent.startAnimation(translate);
        whatsApp.startAnimation(translate);
        contact.startAnimation(translate);
        fees.startAnimation(translate);
        topGrpL.startAnimation(translate);
    }

    public void addStudentProcessRequest(){

        enableScreen(false);
        if(!isNetworkAvailable())
        {
            Toast.makeText(this, "Make Sure To Have Internet Connection", Toast.LENGTH_SHORT).show();
        }


        if(validationSuccessful())
        {
            Toast.makeText(this, "Details Valid ! Processing Ahead", Toast.LENGTH_SHORT).show();
            StudentDetails newStudent;
            ArrayList<PaymentHistory> payment = new ArrayList<>();
            String newName = Objects.requireNonNull(name.getEditText()).getText().toString();
            String newSchool =Objects.requireNonNull(school.getEditText()).getText().toString();
            String newParent = Objects.requireNonNull(parent.getEditText()).getText().toString();
            String newWhatsApp = Objects.requireNonNull(whatsApp.getEditText()).getText().toString();
            String newContact = Objects.requireNonNull(contact.getEditText()).getText().toString();
            Long newFees = Long.valueOf(Objects.requireNonNull(fees.getEditText()).getText().toString());
            String newBoard = BOARD_OPTION[xBoard];
            Long newClass = retrieveStandard();
            String id = generateId(newName,newWhatsApp);

            newStudent = new StudentDetails(id,newName,newParent,newContact,newWhatsApp,(long)0,newFees,
                    newClass,newSchool,newBoard,payment);

            if(newStudent.getPayments() == null)
                Log.i("SKFKSF","yes");

            FirebaseFirestore database = FirebaseFirestore.getInstance();

            HashMap<String,Object> map = new HashMap<>();
            map.put(KEY_ID,id);
            map.put(KEY_NAME,newName);
            map.put(KEY_PARENT,newParent);
            map.put(KEY_CONTACT,newContact);
            map.put(KEY_WTSAPP,newWhatsApp);
            map.put(KEY_AMTDUE,(long) 0);
            map.put(KEY_FEES,newFees);
            map.put(KEY_CLASS,newClass);
            map.put(KEY_SCHOOL,newSchool);
            map.put(KEY_BOARD,newBoard);
            map.put(KEY_PAYMENT,payment);

            database.collection("Students").document(id)
                    .set(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            avl.hide();
                            Toast.makeText(AddStudentActivity.this, "Successful Addition", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddStudentActivity.this, "Error Uploading", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "Details InValid", Toast.LENGTH_SHORT).show();
            enableScreen(true);
        }
        avl.hide();

    }

    public boolean validationSuccessful()
    {
        return validateName() && validateSchool() && validateParent() && contactValidation() &&
                whatsAppValidation() && standardBoardValidation();
    }

    private boolean validateName()
    {
        if(Objects.requireNonNull(name.getEditText()).getText().length() == 0)
        {
            name.setError("Name Field Is Compulsory");
            return false;
        }
        else if(name.getEditText().getText().length() > 20)
        {
            name.setError("Limit Name To 20 Characters");
            return false;
        }

        return true;

    }

    private boolean validateSchool()
    {
        if (Objects.requireNonNull(school.getEditText()).getText().length()==0)
        {
            school.setError("School Field Is Compulsory");
            return false;
        }
        else if(school.getEditText().getText().length() > 18)
        {
            school.setError("Limit School To 18 Characters");
            return false;
        }

        return true;
    }

    private boolean validateParent()
    {
        if(Objects.requireNonNull(parent.getEditText()).getText().length() == 0)
        {
            parent.setError("Parent Field Is Compulsory");
            return false;
        }
        else if(parent.getEditText().getText().length() > 20)
        {
            parent.setError("Limit Parent To 20 Characters");
            return false;
        }

        return true;

    }

    private boolean whatsAppValidation()
    {
        if(Objects.requireNonNull(whatsApp.getEditText()).length() != 10)
        {
            whatsApp.setError("Enter A Valid Number");
            return false;
        }
        return true;
    }

    private boolean contactValidation()
    {
        if(Objects.requireNonNull(contact.getEditText()).length() != 10)
        {
            contact.setError("Enter A Valid Number");
            return false;
        }
        return true;
    }

    private boolean standardBoardValidation()
    {
        if(xBoard == -1 || xStandard == -1)
        {
            Toast.makeText(this, "Board/Standard not selected", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private String generateId(String name, String whatsApp)
    {
        return name+whatsApp;
    }
    private void enableScreen(boolean enable_status)
    {
        floatingActionButton.setEnabled(enable_status);
        name.setEnabled(enable_status);
        school.setEnabled(enable_status);
        parent.setEnabled(enable_status);
        whatsApp.setEnabled(enable_status);
        contact.setEnabled(enable_status);
        fees.setEnabled(enable_status);
        incrementStandard.setEnabled(enable_status);
        decrementStandard.setEnabled(enable_status);
        incrementBoard.setEnabled(enable_status);
        decrementBoard.setEnabled(enable_status);
    }

    private Long retrieveStandard()
    {
        if(STANDARD_OPTION[xStandard].equals("P"))
            return (long) 0;

        return Long.valueOf(STANDARD_OPTION[xStandard]);
    }

    @Override
    public void onBackPressed() {
        //remove call to the super class
        //super.onBackPressed();
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are You Sure ?")
                .setMessage("This command is permanent and irreversible")
                .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", null).show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}