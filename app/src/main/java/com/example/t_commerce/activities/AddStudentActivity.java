package com.example.t_commerce.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.t_commerce.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.wang.avi.AVLoadingIndicatorView;


public class AddStudentActivity extends AppCompatActivity {

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
                if(xBoard == 0)
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
                if(xStandard == 0)
                {
                    xStandard = 11;
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

}