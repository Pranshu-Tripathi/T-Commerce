package com.example.t_commerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import adaptors.ClassAdaptor;

public class MainActivity extends AppCompatActivity {

    private RecyclerView StandardList;
    private ClassAdaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mTool = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mTool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("T-Commerce");

        StandardList = findViewById(R.id.ListClasses);

        StandardList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        StandardList.setLayoutManager(linearLayoutManager);

        ArrayList<String> Numbers = new ArrayList<>();
        updateClassOptionsList(Numbers);

        adaptor = new ClassAdaptor(Numbers,MainActivity.this);
        StandardList.setAdapter(adaptor);


        final ExpandingList expandingList = (ExpandingList) findViewById(R.id.ExpandableListView);

        ExpandingItem item = expandingList.createNewItem(R.layout.expanding_layout);
        ((TextView) item.findViewById(R.id.title)).setText("Dummy1");

        item.createSubItems(1);

        View subItemZero = item.getSubItemView(0);
        ((TextView) subItemZero.findViewById(R.id.sub_title)).setText("Cool");


        item.setIndicatorColorRes(R.color.bgSplashScreen);
        item.setIndicatorIconRes(R.drawable.ic_baseline_person_24);

        ExpandingItem item1 = expandingList.createNewItem(R.layout.expanding_layout);
        ((TextView) item1.findViewById(R.id.title)).setText("Dummy2");

        item1.createSubItems(1);

        View subItemZero1 = item.getSubItemView(0);
        ((TextView) subItemZero1.findViewById(R.id.sub_title)).setText("Cool");


        item1.setIndicatorColorRes(R.color.bgSplashScreen);
        item1.setIndicatorIconRes(R.drawable.ic_baseline_person_24);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchmenu,menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        return super.onCreateOptionsMenu(menu);
    }

    public void updateClassOptionsList(ArrayList<String> n)
    {
        n.add("1");
        n.add("2");
        n.add("3");
        n.add("4");
        n.add("5");
        n.add("6");
        n.add("7");
        n.add("8");
        n.add("9");
        n.add("10");

    }
}