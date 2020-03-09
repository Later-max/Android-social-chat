package com.example.materialtestc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.InputStream;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class text extends AppCompatActivity {

    ImageView txt;
    TextView tet;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bmob.initialize(this, "b0bcbd75ee75b178ed742c5f4e335d75");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        txt = findViewById(R.id.txt);
        tet = findViewById(R.id.tet);
        //verifyStoragePermissions(this);
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int permission = ActivityCompat.checkSelfPermission(text.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    text.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        Button btn1 = findViewById(R.id.Btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//查找Person表里面id为6b6c11c537的数据
                BmobQuery<javaBean> bmobQuery = new BmobQuery<javaBean>();
                bmobQuery.getObject("616b106760", new QueryListener<javaBean>() {
                    @Override
                    public void done(javaBean object,BmobException e) {
                        if(e==null){
                            Log.d("1111111111111111","查询成功");

                            tet.setText(object.getName());

                            Bitmap bitmap = BitmapFactory.decodeFile(object.getPhoto());
                            txt.setImageBitmap(bitmap);
                            //txt.setImageURI(Uri.parse(object.getPhoto()));
                            //Uri uri = Uri.parse((String) object.getPhoto());
                            //txt.setImageURI(uri);
                        }else{
                            Log.d("222222222222222222222","查询失败：" + e.getMessage());
                        }
                    }
                });
            }
        });

        Button btn2 = findViewById(R.id.Btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //调取手机相册
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
            }
        });
    }



    //对获取  相册    的数据返回
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //返回到界面显示图片和图片名-----没有理解？？？并未对fruit_item.xml实例化，为什么呢能够实例化它的控件


        //相册
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                txt.setImageURI(uri);

                Object path = getImagePath(uri, null);
                javaBean javabean = new javaBean();
                javabean.setName("何东");
                javabean.setPhoto(path.toString());
                //存入云数据库
                javabean.save(new SaveListener<String>() {
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
        }
    } private String getImagePath(Uri uri, String seletion) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, seletion, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            }
            cursor.close();

        }
        return path;

    }

 }
//                /**
//                 * 查询多条数据
//                 */
//                    BmobQuery<javaBean> bmobQuery = new BmobQuery<>();
//                    bmobQuery.findObjects(new FindListener<javaBean>() {
//                        @Override
//                        public void done(List<javaBean> categories, BmobException e) {
//                            if (e == null) {
//                                Log.d("55555555555555","查询成功");
//                                txt.setText((CharSequence) categories);
//                                //Snackbar.make(mBtnQuery, "查询成功：" + categories.size(), Snackbar.LENGTH_LONG).show();
//                            } else {
//                                Log.e("BMOB", e.toString());
//                                //Snackbar.make(mBtnQuery, e.getMessage(), Snackbar.LENGTH_LONG).show();
//                            }
//                        }
//                    });

