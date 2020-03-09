package com.example.materialtestc;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.R;
import com.example.map.CustomerMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {


    //适配器加载有关
    RecyclerView recyclerView;
    GridLayoutManager layoutManager;

    //侧滑栏
    private DrawerLayout mDrawerLayout;
    //侧滑栏顶部
    View headView;
    CircleImageView aicon_image;
    TextView txtname;
    TextView mail;

    //下拉刷新有关
    private SwipeRefreshLayout swipeRefreshLayout;

    //首页页面
    private List<Fruit> fruitList = new ArrayList<>();
    FruitAdapter adapter;
    //论坛页面
    private New[] news = {new New("孤树","他说最美丽的传说"),new New("宿舍楼","灵魂依赖的寄托"),new New("操场","挥洒汗水的天堂"),new New("溪湖","闲暇的风景")};
    private List<New> newList = new ArrayList<>();
    newAdapter adaptertwo;

    //弹出框
    private View alertDialogView;
    private EditText userNameEdit;
    private EditText userNameEdit2;
    private EditText userNameEdit3;
    private EditText userTitleEdit;
    private EditText userContEdit;
    private EditText userNumEdit;
    private EditText userHomeEdit;
    //弹出框javabean
    Name Name;
    String home0name;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //bmob云数据库的初始化
        Bmob.initialize(this, "b0bcbd75ee75b178ed742c5f4e335d75");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //获取DrawerLayout的实例
        mDrawerLayout = findViewById(R.id.drawer_layout);

        //滑动菜单的实例
        NavigationView navigationView = findViewById(R.id.nav_view);



        headView = navigationView.getHeaderView(0);
        //学号
        mail = headView.findViewById(R.id.mail);
        //用户名
        txtname = headView.findViewById(R.id.username);

//        mail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialogView = getLayoutInflater ().inflate (R.layout.alertdialog_layout0, null, false);//如果再方法体外，将出现第二次点击闪退
//                AlertDialog.Builder loginAlertDialog = new AlertDialog.Builder (MainActivity.this);
//                loginAlertDialog.setView (alertDialogView);
//                loginAlertDialog.setPositiveButton ("OK", new DialogInterface.OnClickListener () {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        userNameEdit = alertDialogView.findViewById(R.id.city_text);
//                        userNumEdit = alertDialogView.findViewById(R.id.number);
//                        if (userNameEdit.getText().length() != 0 || userNumEdit.getText().length() != 0) {//解决往数据库存入空用户名的情况
//                            txtname.setText(userNameEdit.getText().toString());
//                            mail.setText(userNumEdit.getText().toString());
//                            Name = new Name();
//                            Name.setName(userNameEdit.getText().toString());
//                            Name.setNumber(userNumEdit.getText().toString());
//                            numberhome = userNumEdit.getText().toString();
//                            //存入云数据库
//                            Name.save(new SaveListener<String>() {
//                                @Override
//                                public void done(String objectId,BmobException e) {
//                                    if(e==null){
//                                        Log.d("1111111111111111111111111111","添加数据成功，返回objectdId为：");
//                                    }else{
//                                        Log.d("22222222222222222222222222","创建数据失败：" + e.getMessage());
//                                    }
//                                }
//                            });
//
//                        }
//                    }
//                });
//                loginAlertDialog.show ();
//            }
//        });
//        //一进入页面就执行
        alertDialogView = getLayoutInflater ().inflate (R.layout.alertdialog_layout0, null, false);//如果再方法体外，将出现第二次点击闪退
        AlertDialog.Builder loginAlertDialog = new AlertDialog.Builder (MainActivity.this);
        loginAlertDialog.setView (alertDialogView);
        loginAlertDialog.setPositiveButton ("OK", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                userNameEdit = alertDialogView.findViewById(R.id.city_text);
                userNumEdit = alertDialogView.findViewById(R.id.number);
                userHomeEdit = alertDialogView.findViewById(R.id.home);
                if (userNameEdit.getText().length() != 0 || userNumEdit.getText().length() != 0) {//解决往数据库存入空用户名的情况
                    txtname.setText(userNameEdit.getText().toString());
                    mail.setText(userNumEdit.getText().toString());
                    Name = new Name();
                    Name.setName(userNameEdit.getText().toString());
                    Name.setNumber(userNumEdit.getText().toString());

                    Fruit home00 = new Fruit(userNameEdit.getText().toString(),R.drawable.me,userNameEdit.getText().toString());
                    fruitList.add(home00);
                    Fruit home01 = new Fruit(userHomeEdit.getText().toString(),R.drawable.home,userNameEdit.getText().toString());
                    fruitList.add(home01);
                    //numberhome = userNameEdit.getText().toString();
                    //存入云数据库
//                    Name.save(new SaveListener<String>() {
//                        @Override
//                        public void done(String objectId,BmobException e) {
//                            if(e==null){
//                                Log.d("1111111111111111111111111111","添加数据成功，返回objectdId为：");
//                            }else{
//                                Log.d("22222222222222222222222222","创建数据失败：" + e.getMessage());
//                            }
//                        }
//                    });
                }
         }
         });
        loginAlertDialog.show ();
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(MainActivity.this,1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FruitAdapter(fruitList);
        recyclerView.setAdapter(adapter);

        //初始化用户名，上次更改的  从数据库取出
        //initQuery();
        //头像换取
        aicon_image = headView.findViewById(R.id.icon_image);
        aicon_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调取手机相册
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
            }
        });




        //通过getSupportActionBar()方法获取ActionBar的实例
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            //调用方法，让导航按钮显示出现
            actionBar.setDisplayHomeAsUpEnabled(true);
            //设置导航按钮图片
            actionBar.setHomeAsUpIndicator(R.mipmap.list);
        }
        //初始选择call第一栏
        navigationView.setCheckedItem(R.id.nav_my);
        //菜单选项就监听器
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            //可添加方法，此处只有点击后关闭的菜单选项
            public boolean onNavigationItemSelected(MenuItem item){
                switch (item.getItemId()){
                    //我的
                    case R.id.nav_my:
                        fruitList.clear();
                        Fruit home00 = new Fruit(userNameEdit.getText().toString(),R.drawable.me,userNameEdit.getText().toString());
                        fruitList.add(home00);
                        Fruit home01 = new Fruit(userHomeEdit.getText().toString(),R.drawable.home,userNameEdit.getText().toString());
                        fruitList.add(home01);
                        recyclerView = findViewById(R.id.recycler_view);
                        layoutManager = new GridLayoutManager(MainActivity.this,1);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new FruitAdapter(fruitList);
                        recyclerView.setAdapter(adapter);
                        break;
                    //教学楼
                    case R.id.nav_first:
                        //适配器加载主页面内容
                        initFruits();
                        recyclerView = findViewById(R.id.recycler_view);
                        layoutManager = new GridLayoutManager(MainActivity.this,2);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new FruitAdapter(fruitList);
                        recyclerView.setAdapter(adapter);
                        break;
                    //宿舍楼
                    case R.id.nav_location:
                        //适配器加载主页面内容
                        initFruits2();
                        recyclerView = findViewById(R.id.recycler_view);
                        layoutManager = new GridLayoutManager(MainActivity.this,3);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new FruitAdapter(fruitList);
                        recyclerView.setAdapter(adapter);
                        break;
                    //寝室号
                    case R.id.nav_mail:
                        fruitList.clear();
                        alertDialogView = getLayoutInflater ().inflate (R.layout.alertdialog_layout2, null, false);//如果再方法体外，将出现第二次点击闪退
                        AlertDialog.Builder loginAlertDialog = new AlertDialog.Builder (MainActivity.this);
                        loginAlertDialog.setView (alertDialogView);
                        loginAlertDialog.setPositiveButton ("OK", new DialogInterface.OnClickListener () {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                userNameEdit2 = alertDialogView.findViewById (R.id.city_text);
                                home0name = userNameEdit2.getText().toString();
                                Fruit home0 = new Fruit(home0name,R.drawable.five,userNameEdit.getText().toString());
                                fruitList.add(home0);
                            }
                        });
                        loginAlertDialog.show ();
                        recyclerView = findViewById(R.id.recycler_view);
                        layoutManager = new GridLayoutManager(MainActivity.this,1);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new FruitAdapter(fruitList);
                        recyclerView.setAdapter(adapter);
                        break;

                        //食堂
                    case R.id.nav_task:
                        initfood();
                        recyclerView = findViewById(R.id.recycler_view);
                        layoutManager = new GridLayoutManager(MainActivity.this,1);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new FruitAdapter(fruitList);
                        recyclerView.setAdapter(adapter);
                        break;
                        //用户名查找
                    case R.id.nav_name:
                        fruitList.clear();
                        alertDialogView = getLayoutInflater ().inflate (R.layout.alertdialog_layout4, null, false);//如果再方法体外，将出现第二次点击闪退
                        AlertDialog.Builder loginAlertDialog2 = new AlertDialog.Builder (MainActivity.this);
                        loginAlertDialog2.setView (alertDialogView);
                        loginAlertDialog2.setPositiveButton ("OK", new DialogInterface.OnClickListener () {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                userNameEdit3 = alertDialogView.findViewById (R.id.city_text);
                                home0name = userNameEdit3.getText().toString();
                                Fruit home0 = new Fruit(home0name,R.drawable.five,userNameEdit.getText().toString());
                                fruitList.add(home0);
                            }
                        });
                        loginAlertDialog2.show ();
                        recyclerView = findViewById(R.id.recycler_view);
                        layoutManager = new GridLayoutManager(MainActivity.this,1);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new FruitAdapter(fruitList);
                        recyclerView.setAdapter(adapter);
                        break;
                    //论坛
                    case R.id.nav_second:
                        initNews2();
                        recyclerView = findViewById(R.id.recycler_view);
                        layoutManager = new GridLayoutManager(MainActivity.this,1);
                        recyclerView.setLayoutManager(layoutManager);
                        adaptertwo = new newAdapter(newList);
                        recyclerView.setAdapter(adaptertwo);
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });


        //悬浮按钮
        FloatingActionButton fab = findViewById(R.id.fab);
        //悬浮按钮点击事件
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast用法
                // Toast.makeText(MainActivity.this,"FAB clicked",Toast.LENGTH_SHORT).show();
                //Snackbar用法
                Snackbar.make(v,"打开导航",Snackbar.LENGTH_SHORT)
                        .setAction("确定",new  View.OnClickListener(){
                            @Override
                            public void onClick(View view){
                                Intent intent = new Intent(MainActivity.this, CustomerMenu.class);
                                startActivity(intent);
                            }
                        }).show();
            }
        });

        //下拉刷新
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                refreshFruits();
            }
        });
    }



    //对获取  相册   的数据返回
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //相册
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径I
                Uri uri = data.getData();
                aicon_image.setImageURI(uri);
//                //将Uri转换成String
               // String str = uri.toString();

//                HeadPhoto headPhoto = new HeadPhoto();
//                headPhoto.setPhoto(str);
//                headPhoto.setName("何东");
//                //存入云数据库
//                headPhoto.save(new SaveListener<String>() {
//                    @Override
//                    public void done(String objectId, BmobException e) {
//                        if(e==null){
//                            Log.d("1111111111111111111111111111","添加数据成功，返回objectdId为：");
//                        }else{
//                            Log.d("22222222222222222222222222","创建数据失败：" + e.getMessage());
//                        }
//                    }
//                });
            }
        }
    }

    //开启页面自动查询头像获取

//    private void initHead(){
//        //查找Person表里面id为6b6c11c537的数据
//        final BmobQuery<HeadPhoto> bmobQuery = new BmobQuery<>();
//        bmobQuery.getObject("9787f01570", new QueryListener<HeadPhoto>() {
//            @Override
//            public void done(HeadPhoto object,BmobException e) {
//                if(e==null){
//                    Log.d("33333333333333","查询成功");
//                    // Uri uri = Uri.parse((String) object.getPhoto());
//                    //txt.setImageURI(uri);
//
//                    Bitmap bitmap ;
//                    //byte[] bitmapArray = Base64.decode(object.getPhoto(), Base64.DEFAULT);
//                    //Log.d("444444444444444444444",object.getPhoto());
//                    bitmap = BitmapFactory.decodeFile(object.getPhoto());
//                    //  Context mcontext = null;
//
//                    // Glide.with(mcontext).load(object.getPhoto()).into(txt);
//                    aicon_image.setImageBitmap(bitmap);
//
//                }else{
//                    Log.d("444444444444444","查询失败：" + e.getMessage());
//                }
//            }
//        });
    //}
//


    private void refreshFruits(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        initNews2();
//                        initFruits();
//                        initFruits2();
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    //适配器加载主页面内容
    private void initNews2(){
        newList.clear();
        BmobQuery<NewTitleCont> commentBmobQuery = new BmobQuery<>();
        //commentBmobQuery.addWhereEqualTo("soal",fruitName);
        commentBmobQuery.findObjects(new FindListener<NewTitleCont>() {
            //List <Comment> list =new ArrayList<>();
            @Override
            public void done(List<NewTitleCont> list, BmobException e) {
                if (list != null) {  //此处的作用：：：：对于有的为空的数据库内容下，不会出现闪退的情况
                    for (int i = 0; i < list.size(); i++) {
                        NewTitleCont newTitleCont = list.get(i);
                        New tree000 = new New(newTitleCont.getTitle(),newTitleCont.getCont());
                        newList.add(tree000);
                    }
                }
            }
        });
    }
    //适配器加载主页面内容
    private void initFruits(){
        for (int i = 0;i<1;i++){
            fruitList.clear();
            Fruit home1 = new Fruit("思学楼",R.drawable.s1,userNameEdit.getText().toString());
            fruitList.add(home1);
            Fruit home2 = new Fruit("博学楼",R.drawable.s2,userNameEdit.getText().toString());
            fruitList.add(home2);
            Fruit home3 = new Fruit("图书馆",R.drawable.s3,userNameEdit.getText().toString());
            fruitList.add(home3);
            Fruit home4 = new Fruit("行政大楼",R.drawable.s4,userNameEdit.getText().toString());
            fruitList.add(home4);
            Fruit home5 = new Fruit("明理楼",R.drawable.s5,userNameEdit.getText().toString());
            fruitList.add(home5);
            Fruit home6 = new Fruit("明德楼",R.drawable.s6,userNameEdit.getText().toString());
            fruitList.add(home6);
            Fruit home7 = new Fruit("明志楼",R.drawable.s7,userNameEdit.getText().toString());
            fruitList.add(home7);
            Fruit home8 = new Fruit("明辨楼",R.drawable.s8,userNameEdit.getText().toString());
            fruitList.add(home8);
        }
    }
    //适配器加载主页面内容
    private void initFruits2(){
        for (int i = 0;i<1;i++){
            fruitList.clear();
            Fruit home9 = new Fruit("一栋",R.drawable.six,userNameEdit.getText().toString());
            fruitList.add(home9);
            Fruit home10 = new Fruit("二栋",R.drawable.six,userNameEdit.getText().toString());
            fruitList.add(home10);
            Fruit home11 = new Fruit("三栋",R.drawable.six,userNameEdit.getText().toString());
            fruitList.add(home11);
            Fruit home12 = new Fruit("四栋",R.drawable.six,userNameEdit.getText().toString());
            fruitList.add(home12);
            Fruit home13 = new Fruit("五栋",R.drawable.six,userNameEdit.getText().toString());
            fruitList.add(home13);
            Fruit home14 = new Fruit("六栋",R.drawable.six,userNameEdit.getText().toString());
            fruitList.add(home14);
            Fruit home15 = new Fruit("七栋",R.drawable.six,userNameEdit.getText().toString());
            fruitList.add(home15);
            Fruit home16 = new Fruit("八栋",R.drawable.six,userNameEdit.getText().toString());
            fruitList.add(home16);
            Fruit home17 = new Fruit("九栋",R.drawable.six,userNameEdit.getText().toString());
            fruitList.add(home17);
            Fruit home18 = new Fruit("十栋",R.drawable.six,userNameEdit.getText().toString());
            fruitList.add(home18);
            Fruit home19 = new Fruit("十一栋",R.drawable.six,userNameEdit.getText().toString());
            fruitList.add(home19);
            Fruit home20 = new Fruit("十二栋",R.drawable.six,userNameEdit.getText().toString());
            fruitList.add(home20);
            Fruit home21 = new Fruit("十三栋",R.drawable.six,userNameEdit.getText().toString());
            fruitList.add(home21);
            Fruit home22 = new Fruit("十四栋",R.drawable.six,userNameEdit.getText().toString());
            fruitList.add(home22);
            Fruit home23 = new Fruit("十五栋",R.drawable.six,userNameEdit.getText().toString());
            fruitList.add(home23);
            Fruit home24 = new Fruit("十六栋",R.drawable.six,userNameEdit.getText().toString());
            fruitList.add(home24);
            Fruit home25 = new Fruit("十七栋",R.drawable.six,userNameEdit.getText().toString());
            fruitList.add(home25);
            Fruit home26 = new Fruit("十八栋",R.drawable.six,userNameEdit.getText().toString());
            fruitList.add(home26);
            Fruit home27 = new Fruit("十九栋",R.drawable.six,userNameEdit.getText().toString());
            fruitList.add(home27);
            Fruit home28 = new Fruit("二十栋",R.drawable.six,userNameEdit.getText().toString());
            fruitList.add(home28);
            Fruit home29 = new Fruit("二十一栋",R.drawable.six,userNameEdit.getText().toString());
            fruitList.add(home29);
            Fruit home30 = new Fruit("二十二栋",R.drawable.six,userNameEdit.getText().toString());
            fruitList.add(home30);
            Fruit home31 = new Fruit("二十三栋",R.drawable.six,userNameEdit.getText().toString());
            fruitList.add(home31);
            Fruit home32 = new Fruit("二十四栋",R.drawable.six,userNameEdit.getText().toString());
            fruitList.add(home32);
        }
    }
    private void initfood() {
        for (int i = 0; i < 1; i++) {
            fruitList.clear();
            Fruit home11 = new Fruit("一食堂", R.drawable.six, userNameEdit.getText().toString());
            fruitList.add(home11);
            Fruit home12 = new Fruit("二食堂", R.drawable.six, userNameEdit.getText().toString());
            fruitList.add(home12);
            Fruit home13 = new Fruit("三食堂", R.drawable.six, userNameEdit.getText().toString());
            fruitList.add(home13);
            Fruit home14 = new Fruit("四食堂", R.drawable.six, userNameEdit.getText().toString());
            fruitList.add(home14);
            Fruit home15 = new Fruit("五食堂", R.drawable.six, userNameEdit.getText().toString());
            fruitList.add(home15);
            Fruit home16 = new Fruit("六食堂", R.drawable.six, userNameEdit.getText().toString());
            fruitList.add(home16);
        }
    }

    //加载toolbar菜单文件
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    //toolbat点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            //Toolbar最左侧的按钮HomeAsUp按钮的id永远都是android.R.id.home
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.backup:

                alertDialogView = getLayoutInflater ().inflate (R.layout.alertdialog_layout3, null, false);//如果再方法体外，将出现第二次点击闪退
                AlertDialog.Builder loginAlertDialog = new AlertDialog.Builder (MainActivity.this);
                loginAlertDialog.setView (alertDialogView);
                loginAlertDialog.setPositiveButton ("OK", new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        userTitleEdit = alertDialogView.findViewById (R.id.Htitle);
                        userContEdit = alertDialogView.findViewById(R.id.Hcont);
                        New tree3 = new New(userTitleEdit.getText().toString(),userContEdit.getText().toString());
                        newList.add(tree3);

                        NewTitleCont newTitleCont = new NewTitleCont();
                        newTitleCont.setTitle(userTitleEdit.getText().toString());
                        newTitleCont.setCont(userContEdit.getText().toString());
                        //存入云数据库
                        newTitleCont.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                if(e==null){
                                    Log.d("1111111111111111111111111111","添加数据成功，返回objectdId为：");
                                }else{
                                    Log.d("22222222222222222222222222","创建数据失败：" + e.getMessage());
                                }
                            }
                        });

                    }
                });
                loginAlertDialog.show ();

                //Toast.makeText(this," You clicked Backup",Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this," You clicked Delete",Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this," You clicked Settings",Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }
}
