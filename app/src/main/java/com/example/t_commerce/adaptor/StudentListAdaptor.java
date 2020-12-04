package com.example.t_commerce.adaptor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.t_commerce.R;
import com.example.t_commerce.models.StudentDetails;

import java.util.ArrayList;

public class StudentListAdaptor extends RecyclerView.Adapter<StudentListAdaptor.MyViewHolder> implements View.OnClickListener {

    Context context;
    ArrayList<StudentDetails> studentDetails;
    View.OnClickListener onClickListener;

    public StudentListAdaptor(Context c, ArrayList<StudentDetails> s)
    {
        this.context = c;
        this.studentDetails = s;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =  LayoutInflater.from(parent.getContext());
        View studentItem = layoutInflater.inflate(R.layout.student_listitem_layout,parent,false);
        studentItem.setOnClickListener(this);
        return new MyViewHolder(studentItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        Long standard = studentDetails.get(position).getSClass();
        String name = studentDetails.get(position).getName();
        String school = studentDetails.get(position).getSchool();

        final String whatsAppNumber = studentDetails.get(position).getWhatsAppNumber();
        final String contactNumber = studentDetails.get(position).getMobileContact();

        holder.standardText.setText(String.valueOf(standard));
        holder.nameText.setText(name);
        holder.schoolText.setText(school);

        if(school.length() > 18)
        {
            String schoolConcatenated = school.substring(0,14);
            holder.schoolText.setText(schoolConcatenated + " ...");
        }

        holder.whatsAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation a = AnimationUtils.loadAnimation(context,R.anim.bouncefab);
                holder.whatsAppBtn.startAnimation(a);


                String url = "https://api.whatsapp.com/send?phone=91"+whatsAppNumber.trim();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });

        holder.contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation a = AnimationUtils.loadAnimation(context,R.anim.bouncefab);
                holder.contactBtn.startAnimation(a);

                String uri = "tel:" + contactNumber.trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentDetails.size();
    }

    public void onClickListener(View.OnClickListener clickListener)
    {
        this.onClickListener = clickListener;
    }


    public StudentDetails getItemAt(int position)
    {
        return studentDetails.get(position);
    }

    public void updateList(ArrayList<StudentDetails> filteredList)
    {
        this.studentDetails = filteredList;
        notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        onClickListener.onClick(v);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView standardText, nameText, schoolText;
        ImageButton whatsAppBtn, contactBtn;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            standardText = itemView.findViewById(R.id.standardStudent);
            nameText = itemView.findViewById(R.id.studentName);
            schoolText = itemView.findViewById(R.id.schoolName);
            whatsAppBtn = itemView.findViewById(R.id.studentWhatsApp);
            contactBtn = itemView.findViewById(R.id.studentContact);
            linearLayout = itemView.findViewById(R.id.grpList);
        }
    }

}
