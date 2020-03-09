package com.example.materialtestc;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.R;

import java.util.List;

public class newAdapter extends RecyclerView.Adapter<newAdapter.ViewHolder> {

    private Context mcontext;
    private List<New> mNewList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView newname;
        TextView newcont;

        public ViewHolder(@NonNull View view) {
            super(view);
            newname = view.findViewById(R.id.new_name);
            newcont = view.findViewById(R.id.new_cont);
        }
    }

    public newAdapter(List<New> newList){
        mNewList = newList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mcontext == null) {
            mcontext = parent.getContext();
        }
        View view = LayoutInflater.from(mcontext).inflate(R.layout.activity_newone,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                New news = mNewList.get(position);
                Intent intent = new Intent(mcontext,NewsActivity.class);
                intent.putExtra(NewsActivity.NEWS_NAME,news.getName());
                intent.putExtra(NewsActivity.NEWS_CONT,news.getcont());
                mcontext.startActivity(intent);
            }
        });
        return holder;
        //return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        New aNew = mNewList.get(position);
        holder.newname.setText("标题："+aNew.getName());
        holder.newcont.setText("内容："+aNew.getcont());
       // Glide.with(mcontext).load(New.getcont()).into(holder.newcont);
    }

    @Override
    public int getItemCount() {
        return mNewList.size();
    }
}
