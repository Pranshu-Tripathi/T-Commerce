package adaptors;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.t_commerce.R;

import java.util.ArrayList;

public class ClassAdaptor extends RecyclerView.Adapter <ClassAdaptor.MyViewHolder> {

    ArrayList<String> Numbers;
    Context context;
    public ClassAdaptor(ArrayList<String> n,Context c)
    {
        this.Numbers = n;
        this.context = c;
    }


    @NonNull
    @Override
    public ClassAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.standard_layout,parent,false);
        return new MyViewHolder((view));
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.ClassNumber.setText(Numbers.get(position));

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,holder.ClassNumber.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView ClassString,ClassNumber;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.ClassList);
            ClassString = itemView.findViewById(R.id.ClassString);
            ClassNumber = itemView.findViewById(R.id.ClassNumber);
        }
    }
}
