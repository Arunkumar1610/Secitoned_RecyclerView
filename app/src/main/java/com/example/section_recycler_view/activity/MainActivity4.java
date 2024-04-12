package com.example.section_recycler_view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.section_recycler_view.R;
import com.example.section_recycler_view.baseadapter.DataModel;
import com.example.section_recycler_view.baseadapter.GenericRecyclerAdapter;
import com.example.section_recycler_view.baseadapter.MyAdapter;
import com.example.section_recycler_view.baseadapter.RecyclableViewItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity4 extends AppCompatActivity implements ActivityMethodCallBack{
    private RecyclerView recyclerView;
    List<RecyclableViewItem> dataList;

    Button del ,add;
    public static GenericRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        add=findViewById(R.id.addBtn);

        dataList = new ArrayList<>();
        dataList.add(new MyAdapter(new DataModel(1, "Item 1")));
        dataList.add(new MyAdapter(new DataModel(2, "Item 2")));
        dataList.add(new MyAdapter(new DataModel(3, "Item 3")));

        adapter = new GenericRecyclerAdapter(dataList);

        recyclerView = findViewById(R.id.b_recycler_View);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
RecyclableViewItem item=new MyAdapter((new DataModel(4, "Item 4")));
                adapter.appendItem(item);
            }
        });


    }

   public static void deleteItemByPosition(int pos)
    {
        adapter.removeItemAtPosition(pos);
    }


    @Override
    public void methodCallBack(int pos) {
        deleteItemByPosition(pos);
    }
}