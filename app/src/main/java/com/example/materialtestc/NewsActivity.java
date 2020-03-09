package com.example.materialtestc;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class NewsActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String NEWS_NAME="news_name";
    public static final String NEWS_CONT="news_cont";
    private String newsName;
    private String newsCont;

    //    评论区
    private ImageView comment;
    private TextView hide_down;
    private EditText comment_content;
    private Button comment_send;

    private LinearLayout rl_enroll;
    private RelativeLayout rl_comment;

    private ListView comment_list;
    private CommentAdapter adapterComment;
    private List<Comment> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //bmob云数据库的初始化
        Bmob.initialize(this, "b0bcbd75ee75b178ed742c5f4e335d75");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Intent intent = getIntent();
        //获取新闻名与新闻内容
        newsName=intent.getStringExtra(NEWS_NAME);
        newsCont=intent.getStringExtra(NEWS_CONT);
        Toolbar toolbar = findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        TextView newsContentText = findViewById(R.id.news_content_text);
        setSupportActionBar(toolbar);//注意导入的包 若出错此处爆红
        ActionBar actionBar = getSupportActionBar();
        //// 给左上角图标的左边加上一个返回的图标 。对应ActionBar.DISPLAY_HOME_AS_UP
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //引入数据库
        initQuery();
        collapsingToolbar.setTitle(newsName);
        newsContentText.setText(newsCont);

        //初始化评论列表
        initView();
    }

    private void initQuery() {

        //进入页面就查询数据库
        final BmobQuery<Comment> commentBmobQuery = new BmobQuery<>();
        commentBmobQuery.addWhereEqualTo("soal",newsName);
        commentBmobQuery.findObjects(new FindListener<Comment>() {
            //List <Comment> list =new ArrayList<>();
            @Override
            public void done(List<Comment> list, BmobException e) {
                if (list != null) {  //此处的作用：：：：对于有的为空的数据库内容下，不会出现闪退的情况
                    for (int i = 0; i < list.size(); i++) {
                        Comment comment = list.get(i);
                        comment.setName(comment.getName());
                        comment.setContent(comment.getContent());
                        comment.setTime(comment.getCreatedAt());
                        adapterComment.addComment(comment);
                    }
                }
            }
        });
    }

    //返回到上一个活动按钮
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                //关闭当前活动，从而返回到上一个活动
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //初始化评论控件
    private void initView(){

        //初始化评论列表
        comment_list = (ListView)findViewById(R.id.comment_list_news);
        //初始化数据
        data = new ArrayList<>();
        //初始化适配器
        adapterComment = new CommentAdapter(getApplicationContext(),data);
        //为评论列表设置适配器
        comment_list.setAdapter(adapterComment);

        comment = (ImageView)findViewById(R.id.comment_news);
        hide_down = (TextView)findViewById(R.id.hide_dowm_news);
        comment_content = (EditText)findViewById(R.id.comment_content_news);
        comment_send = (Button)findViewById(R.id.comment_send_news);

        rl_enroll = (LinearLayout)findViewById(R.id.rl_enroll_news);
        rl_comment = (RelativeLayout)findViewById(R.id.rl_comment_news);

        //设置监听事件
        setListener();
    }

    /**
     * 设置监听
     */
    public void setListener(){
        comment.setOnClickListener(this);
        hide_down.setOnClickListener(this);
        comment_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment_news:
                //弹出输入法
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                //显示评论框
                rl_enroll.setVisibility(View.GONE);
                rl_comment.setVisibility(View.VISIBLE);
                break;
            case R.id.hide_dowm_news:
                //隐藏评论框
                rl_comment.setVisibility(View.GONE);
                rl_enroll.setVisibility(View.VISIBLE);
                //隐藏输入法
                InputMethodManager im = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(comment_content.getWindowToken(), 0);
                break;
            case R.id.comment_send_news:
                sendComment();
                break;
            default:
                break;
        }
    }

    /**
     *发送评论
     */
    public void sendComment(){
        if (comment_content.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"评论不能为空",Toast.LENGTH_SHORT).show();
        }else {
            //获取时间方法
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());

            //生成评论数据
            Comment comment = new Comment();
            comment.setContent(comment_content.getText().toString());
            comment.setName("何东: ");
            comment.setSoal(newsName);
            comment.setTime(simpleDateFormat.format(date));
            adapterComment.addComment(comment);

            //存入云数据库
            comment.save(new SaveListener<String>() {
                @Override
                public void done(String objectId, BmobException e) {
                    if(e==null){
                        Log.d("1111111111111111111111111111","添加数据成功，返回objectdId为："+newsName);
                    }else{
                        Log.d("22222222222222222222222222","创建数据失败：" + e.getMessage());
                    }
                }
            });
            //发送完，清空输入框
            comment_content.setText("");
        }
    }
}
