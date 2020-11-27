package com.example.t_commerce.adaptor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class StudentListAdaptor extends RecyclerView.Adapter<StudentListAdaptor.MyViewHolder> {

    Context context;
    ArrayList<StudentDetails> studentDetails;

    public StudentListAdaptor(Context c, ArrayList<StudentDetails> s)
    {
        this.context = c;
        this.studentDetails = s;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.student_listitem_layout,parent,false));
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

        holder.whatsAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation a = AnimationUtils.loadAnimation(context,R.anim.bouncefab);
                holder.whatsAppBtn.startAnimation(a);

                //TODO: intent to whatsApp

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

                //TODO: intent to contact

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

    public class MyViewHolder extends RecyclerView.ViewHolder{

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
