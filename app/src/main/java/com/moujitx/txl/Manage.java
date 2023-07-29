package com.moujitx.txl;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Manage extends AppCompatActivity implements
        View.OnClickListener {
    SQLites sqLites;
    private EditText et_name,et_phone,et_group;
    private Button btn_add,btn_clean,btn_update,btn_delete,btn_return;
    Integer id = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        sqLites = new SQLites(this);
        init();

    }
    private void init() {
        et_name = findViewById(R.id.et_name);
        et_phone = findViewById(R.id.et_phone);
        et_group = findViewById(R.id.et_group);
        btn_add = findViewById(R.id.btn_add);
        btn_clean = findViewById(R.id.btn_clean);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        btn_return = findViewById(R.id.btn_return);
        btn_add.setOnClickListener(this);
        btn_clean.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_return.setOnClickListener(this);
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        if (type.equals("add")){
            btn_update.setVisibility(View.INVISIBLE);
            btn_delete.setVisibility(View.INVISIBLE);
        }
        else if (type.equals("change")){
            btn_add.setVisibility(View.INVISIBLE);
            btn_clean.setVisibility(View.INVISIBLE);
            id = Integer.valueOf(intent.getStringExtra("id"));

            SQLiteDatabase db;
            db = sqLites.getReadableDatabase();
            String sql = "select * from information where _id=" + id;
            Cursor cursor=db.rawQuery(sql,null);
            cursor.moveToFirst();
            et_name.setText(cursor.getString(1));
            et_phone.setText(cursor.getString(2));
            et_group.setText(cursor.getString(3));
            cursor.close();
            db.close();
        }
    }
    @Override
    public void onClick(View v) {
        String name, phone, rgroup;
        SQLiteDatabase db;
        ContentValues values;
        switch (v.getId()) {
            case R.id.btn_add: //添加
                name = et_name.getText().toString();
                phone = et_phone.getText().toString();
                rgroup = et_group.getText().toString();
                db = sqLites.getWritableDatabase();
                values = new ContentValues();
                values.put("name", name);
                values.put("phone", phone);
                values.put("rgroup", rgroup);
                db.insert("information", null, values);
                db.close();
                Toast.makeText(this, "信息添加成功！", Toast.LENGTH_SHORT).show();
                AlertDialog addConfirm = new AlertDialog.Builder(this)
                        .setTitle("添加成功")
                        .setMessage("信息添加成功，是否继续添加")
                        .setIcon(R.mipmap.ic_launcher_round)
                        .setPositiveButton("继续添加", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                et_name.setText("");
                                et_phone.setText("");
                                et_group.setText("");
                            }
                        })
                        .setNegativeButton("添加完毕", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                goback();
                            }
                        })
                        .create();
                addConfirm.show();
                break;
            case R.id.btn_clean: //重置
                et_name.setText("");
                et_phone.setText("");
                et_group.setText("");
                break;
            case R.id.btn_update: //修改
                name = et_name.getText().toString();
                phone = et_phone.getText().toString();
                rgroup = et_group.getText().toString();
                db = sqLites.getWritableDatabase();
                values = new ContentValues();
                values.put("name", name);
                values.put("phone", phone);
                values.put("rgroup", rgroup);
                db.update("information", values, "_id=?",
                        new String[]{String.valueOf(id)});
                Toast.makeText(this, "信息修改成功", Toast.LENGTH_SHORT).show();
                db.close();
                break;
            case R.id.btn_delete: //删除
                AlertDialog delConfirm = new AlertDialog.Builder(this)
                        .setTitle("确认删除")//标题
                        .setMessage("确认要删除这条信息吗？")//内容
                        .setIcon(R.mipmap.ic_launcher_round)//图标
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delInfo();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create();
                delConfirm.show();
                break;
            case R.id.btn_return: //返回
                goback();
                break;
        }
    }

    private void goback() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void delInfo() {
        SQLiteDatabase db;
        db = sqLites.getWritableDatabase();
        db.delete("information", "_id = ?", new String[]{String.valueOf(id)});
        Toast.makeText(this, "信息删除成功", Toast.LENGTH_SHORT).show();
        db.close();

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}