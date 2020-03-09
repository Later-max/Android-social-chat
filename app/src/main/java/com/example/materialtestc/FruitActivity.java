package com.example.materialtestc;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.bumptech.glide.Glide;
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
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;


public class FruitActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String FRUIT_NAME="fruit_name";
    public static final String FRUIT_IMAGE_ID="fruit_image_id";
    public static final String FRUIT_NAME2 = "fruit_name2";
    private String fruitName;
    private String fruitName2;

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
    Name Name;
    private String homename;

    private ImageView fruitImageView;
    private int fruitImageId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //bmob云数据库的初始化
        Bmob.initialize(this, "b0bcbd75ee75b178ed742c5f4e335d75");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit);
        Intent intent = getIntent();
        //获取图片名
        fruitName=intent.getStringExtra(FRUIT_NAME);
        fruitName2 = intent.getStringExtra(FRUIT_NAME2);
        //获取图片
        fruitImageId =intent.getIntExtra(FRUIT_IMAGE_ID,0);
        Toolbar toolbar = findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        fruitImageView = findViewById(R.id.fruit_image_view);
        //点击获取相册
        fruitImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
            }
        });
        TextView fruitContentText = findViewById(R.id.fruit_content_text);
        setSupportActionBar(toolbar);//注意导入的包 若出错此处爆红
        ActionBar actionBar = getSupportActionBar();
        //// 给左上角图标的左边加上一个返回的图标 。对应ActionBar.DISPLAY_HOME_AS_UP
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(fruitName);
        Glide.with(this).load(fruitImageId).into(fruitImageView);
        //设置正文内容
        initQuery();
        String fruitContent = generateFruitContent(fruitName);
        fruitContentText.setText(fruitContent);

        //初始化评论列表
        initView();
    }

    //点击页面后，返回的图片
    //对获取  相册   的数据返回
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //相册
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径I
                Uri uri = data.getData();
                fruitImageView.setImageURI(uri);
               // Glide.with(this).load(fruitImageId).into(fruitImageView);
            }
        }
    }


    private void initQuery() {

        //进入页面就查询数据库
        final BmobQuery<Comment> commentBmobQuery = new BmobQuery<>();
        commentBmobQuery.addWhereEqualTo("soal",fruitName);
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
//                    String name = comment.getName();
//                    String cont = comment.getContent();
//                    comment.add(name,cont);
                    }
                }
            }
        });
    }

    //正文内容
    private String generateFruitContent(String fruitName){
        StringBuilder fruitContent = new StringBuilder();
        //循环500次名字作为内容
        for (int i = 0 ; i < 100; i++){
            fruitContent.append(fruitName);
        }
        return fruitContent.toString();
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
        comment_list = (ListView)findViewById(R.id.comment_list);
        //初始化数据
        data = new ArrayList<>();
        //初始化适配器
        adapterComment = new CommentAdapter(getApplicationContext(),data);
        //为评论列表设置适配器
        comment_list.setAdapter(adapterComment);

        comment = (ImageView)findViewById(R.id.comment);
        hide_down = (TextView)findViewById(R.id.hide_dowm);
        comment_content = (EditText)findViewById(R.id.comment_content);
        comment_send = (Button)findViewById(R.id.comment_send);

        rl_enroll = (LinearLayout)findViewById(R.id.rl_enroll);
        rl_comment = (RelativeLayout)findViewById(R.id.rl_comment);

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
        switch (v.getId()){
            case R.id.comment:
                //弹出输入法
                InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
                //显示评论框
                rl_enroll.setVisibility(View.GONE);
                rl_comment.setVisibility(View.VISIBLE);
                break;
            case R.id.hide_dowm:
                //隐藏评论框
                rl_comment.setVisibility(View.GONE);
                rl_enroll.setVisibility(View.VISIBLE);
                //隐藏输入法
                InputMethodManager im = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(comment_content.getWindowToken(),0);
                break;
            case R.id.comment_send:
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
            final Comment comment = new Comment();
            comment.setContent(comment_content.getText().toString());
            comment.setName(fruitName2+":");
            comment.setSoal(fruitName);
            comment.setTime(simpleDateFormat.format(date));
            adapterComment.addComment(comment);

            //存入云数据库
            comment.save(new SaveListener<String>() {
                @Override
                public void done(String objectId, BmobException e) {
                    if(e==null){
                        Log.d("1111111111111111111111111111","添加数据成功，返回objectdId为："+fruitName);
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
