package com.example.t_commerce.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.t_commerce.R;
import com.example.t_commerce.models.PaymentHistory;
import com.example.t_commerce.models.StudentDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
    private String[] texts = {"Gathering Resources",
            "Checking All the Dates",
            "Marking the Calendar",
            "Generating Buttons",
            "Entering Cheat Codes",
            "Downloading Profiles",
            "Leaking Nuclear Codes"};

    private FirebaseFirestore db;

    public static final ArrayList<StudentDetails> Students = new ArrayList<>();



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

        fadingTextView = findViewById(R.id.fading_text_view);
        fadingTextView.setVisibility(View.INVISIBLE);
        fadingTextView.setTexts(texts);
        fadingTextView.restart();
        fadingTextView.setVisibility(View.VISIBLE);

        


        LoadStudents();

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
                                    Date date = (Date) element.get("Date");
                                    String Mode = (String) element.get("Mode");
                                    PaymentHistory p = new PaymentHistory(date,Amount,Mode);
                                    histories.add(p);
                                }

                                Long Class = (Long) documentSnapshot.get(KEY_CLASS);
                                Long amountDue = (Long) documentSnapshot.get(KEY_AMTDUE);
                                Long fees = (Long) documentSnapshot.get(KEY_FEES);
                                String board = (String) documentSnapshot.get(KEY_BOARD);
                                StudentDetails details = new StudentDetails(id,name,parentname,contact,whatsapp,amountDue,fees,
                                        Class,school,board,histories);
                                Students.add(details);

                                avl.hide();
                                fadingTextView.setVisibility(View.INVISIBLE);
                                fadingTextView.stop();
                            }
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchmenu,menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        return super.onCreateOptionsMenu(menu);
    }
}