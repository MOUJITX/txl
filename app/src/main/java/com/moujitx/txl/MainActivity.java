package com.moujitx.txl;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {
    SQLites sqLites;
    private EditText et_info;
    private Button btn_add,btn_query;

    public class BookInfo {
        public Integer _id;
        public String name;
        public String phone;
        public String group;
    }
    RecyclerView mRecyclerView;
    MyAdapter mMyAdapter ;
    List<BookInfo> bookInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sqLites = new SQLites(this);
        init();
        setListData("");
    }

    private void setListData(String info) {

        bookInfos.clear();

        SQLiteDatabase db;
        db = sqLites.getReadableDatabase();

        String sql = "select * from information where name LIKE '%" + info + "%' or phone LIKE '%" + info + "%' or rgroup LIKE '%" + info + "%'";
        Cursor cursor=db.rawQuery(sql,null);

        if (cursor.getCount() == 0) {
            AlertDialog dataNull = new AlertDialog.Builder(this)
                    .setTitle("查询失败")//标题
                    .setMessage("根据关键字查询结果为空")//内容
                    .setIcon(R.mipmap.ic_launcher_round)//图标
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })
                    .create();
            dataNull.show();
            return;
        } else {
            cursor.moveToFirst();

            BookInfo bookInfo = new BookInfo();
            bookInfo._id = Integer.valueOf(cursor.getString(0));
            bookInfo.name = cursor.getString(1);
            bookInfo.phone = cursor.getString(2);
            bookInfo.group = cursor.getString(3);
            bookInfos.add(bookInfo);
        }
        while (cursor.moveToNext()) {
            BookInfo bookInfo = new BookInfo();
            bookInfo._id = Integer.valueOf(cursor.getString(0));
            bookInfo.name = cursor.getString(1);
            bookInfo.phone = cursor.getString(2);
            bookInfo.group = cursor.getString(3);
            bookInfos.add(bookInfo);
        }
        cursor.close();
        db.close();

        mMyAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mMyAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHoder> {

        @NonNull
        @Override
        public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(MainActivity.this, R.layout.item_list, null);
            MyViewHoder myViewHoder = new MyViewHoder(view);
            return myViewHoder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHoder holder, @SuppressLint("RecyclerView") int position) {
            BookInfo bookInfo = bookInfos.get(position);
            holder.tv_showName.setText(bookInfo.name);
            holder.tv_showPhone.setText(bookInfo.phone);
            holder.tv_showGroup.setText(bookInfo.group);

            holder.listItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoChange(bookInfo._id);
                }
            });
        }

        @Override
        public int getItemCount() {
            return bookInfos.size();
        }
    }

    private void gotoChange(Integer id) {
        Intent intentInfo = new Intent(this,Manage.class);
        intentInfo.putExtra("type","change");
        intentInfo.putExtra("id",String.valueOf(id));
        startActivity(intentInfo);
    }

    class MyViewHoder extends RecyclerView.ViewHolder {
        TextView tv_showName,tv_showPhone,tv_showGroup;
        ConstraintLayout listItem;

        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            tv_showName = itemView.findViewById(R.id.tv_showName);
            tv_showPhone = itemView.findViewById(R.id.tv_showPhone);
            tv_showGroup = itemView.findViewById(R.id.tv_showGroup);
            listItem = itemView.findViewById(R.id.listItem);
        }
    }

    private void init() {
        et_info = findViewById(R.id.et_info);
        btn_add = findViewById(R.id.btn_add);
        btn_query = findViewById(R.id.btn_query);
        btn_add.setOnClickListener(this);
        btn_query.setOnClickListener(this);

        mRecyclerView = findViewById(R.id.rv_list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                Intent intent = new Intent(this,Manage.class);
                intent.putExtra("type","add");
                startActivity(intent);
                break;
            case R.id.btn_query:
                String info = et_info.getText().toString();
                setListData(info);
                break;
        }
    }
}
