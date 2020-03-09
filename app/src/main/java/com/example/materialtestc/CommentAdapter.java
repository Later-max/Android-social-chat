package com.example.materialtestc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CommentAdapter extends BaseAdapter {
    Context context;
    List<Comment> data;

    /**
     * *
     * 添加一条评论，刷新列表
     * @param comment
     */
    public void addComment(Comment comment){
        data.add(comment);
        notifyDataSetChanged();
    }

    public CommentAdapter(Context c, List<Comment> data){
        this.context = c;
        this.data = data;
    }

    public static class ViewHolder{
        TextView comment_name;
        TextView comment_content;
        TextView comment_time;
    }

    @Override
    public int getCount() {
        //返回列表子项数目
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        //当在活动中设置列表监听时，可以获取具体的点击的列表子项
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        //获取被点击的子项的id
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        //重用view
        if (view == null){
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_comment,null);
            holder.comment_name = (TextView)view.findViewById(R.id.comment_name);
            holder.comment_content = (TextView)view.findViewById(R.id.comment_content);
            holder.comment_time = view.findViewById(R.id.time);
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }
        //适配数据
        holder.comment_name.setText(data.get(i).getName());
        holder.comment_content.setText(data.get(i).getContent());
        holder.comment_time.setText(data.get(i).getCreatedAt());

        return view;
    }

}

