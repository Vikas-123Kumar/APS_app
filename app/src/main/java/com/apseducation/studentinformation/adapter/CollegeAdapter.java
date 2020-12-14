package com.apseducation.studentinformation.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apseducation.studentinformation.R;
import com.apseducation.studentinformation.bean.Aps_Bean;
import com.apseducation.studentinformation.selectpreference.SelectSecondPreference;

import java.util.ArrayList;

public class CollegeAdapter extends RecyclerView.Adapter<CollegeAdapter.MyViewHolder> {
    Context context;
    ArrayList<Aps_Bean> aps_list;
    Aps_Bean bean, Bean;

    public CollegeAdapter(Context context, ArrayList college) {
        this.context = context;
        this.aps_list = college;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder myViewHolder = new MyViewHolder(view); // pass the view to View
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        bean = aps_list.get(position);
        Log.e("bean", bean + "");
        String text = holder.radioButton.getText().toString();
        Log.e("log", text);
        holder.radioButton.setText(bean.getCollegeName());
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (holder.radioButton.isChecked()) {
                    Intent intent = new Intent(context, SelectSecondPreference.class);
                    intent.putExtra("college name", holder.radioButton.getText().toString());
                    context.startActivity(intent);
                    holder.radioGroup.clearCheck();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return aps_list.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        RadioGroup radioGroup;
        RelativeLayout relativeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            radioButton = (RadioButton) itemView.findViewById(R.id.college_radio);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            relativeLayout = itemView.findViewById(R.id.relative_layout);

        }
    }
}
