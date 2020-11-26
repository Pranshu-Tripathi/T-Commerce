package com.example.t_commerce.adaptor;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.t_commerce.R;
import com.example.t_commerce.activities.MainActivity;
import com.example.t_commerce.activities.StudentDiaryActivity;
import com.example.t_commerce.models.StandardListModel;

import java.util.ArrayList;

public class StandardListAdaptor extends RecyclerView.Adapter<StandardListAdaptor.MyViewHolder>  {
    Context context;
    ArrayList<StandardListModel> standards;

    public StandardListAdaptor(Context c, ArrayList<StandardListModel> s)
    {
        this.context = c;
        this.standards = s;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.standard_listitem_layout,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.StandardText.setText(String.valueOf(standards.get(position).getStandard()));

        if(standards.get(position).getStandard() == -1)
            holder.StandardText.setText("A");
        if(standards.get(position).getStandard() == 0)
            holder.StandardText.setText("P");

        if(standards.get(position).isSelected())
            holder.Background.setBackgroundColor(Color.BLACK);
        else
            holder.Background.setBackgroundColor(context.getResources().getColor(R.color.charcoal));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i <= 11 ; i++)
                {
                    if(i != position)
                    {
                        StandardListModel s = new StandardListModel(i-1,false);
                        standards.set(i,s);
                    }
                    else
                    {
                        StandardListModel k = new StandardListModel(i-1,true);
                        standards.set(i,k);
                    }
                }

                updateList(standards);
            }
        });

    }

    @Override
    public int getItemCount() {
        return standards.size();
    }



    public void updateList(ArrayList<StandardListModel> standards)
    {
        this.standards = standards;
        notifyDataSetChanged();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView StandardText;
        LinearLayout Background;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            StandardText = itemView.findViewById(R.id.standardText);
            Background = itemView.findViewById(R.id.backColorLinearLayout);
        }
    }
}
