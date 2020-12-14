package com.apseducation.studentinformation.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apseducation.studentinformation.R;
import com.apseducation.studentinformation.bean.Registration_Bean;
import com.apseducation.studentinformation.bean.Survey_bean;

import java.util.ArrayList;


public class ShowDataAdapter extends RecyclerView.Adapter<ShowDataAdapter.MyViewHolder> {
    Survey_bean survey_bean;
    ArrayList list;
    Registration_Bean registration_bean;

    public ShowDataAdapter(Survey_bean survey_bean) {
        this.survey_bean = survey_bean;
        list = new ArrayList();
        list.add(survey_bean);
    }

    public ShowDataAdapter(Registration_Bean registration_bean) {
        this.registration_bean = registration_bean;
        list = new ArrayList();
        list.add(registration_bean);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_data_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder myViewHolder = new MyViewHolder(view); // pass the view to View
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.e("list get",list.get(position)+"");
        if (list.get(position)!=null){
            holder.textView.setText(list.get(position).toString()+"");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RelativeLayout relativeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.show_data);
            relativeLayout = itemView.findViewById(R.id.relative_show);

        }
    }
}
