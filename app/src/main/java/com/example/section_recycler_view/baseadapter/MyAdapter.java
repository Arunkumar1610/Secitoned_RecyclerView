package com.example.section_recycler_view.baseadapter;

import static com.example.section_recycler_view.activity.MainActivity4.deleteItemByPosition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.section_recycler_view.R;
import com.example.section_recycler_view.activity.MainActivity4;

public class MyAdapter implements RecyclableViewItem<MyAdapter.ViewHolder> {
    DataModel data;
    Context context;


    public MyAdapter(DataModel data) {
        this.data = data;

    }

    @Override
    public Class<ViewHolder> getViewHolderClassType() {
        return ViewHolder.class;
    }

    @Override
    public int getViewType() {
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, GenericRecyclerAdapter.onItemClickListener itemClickListener) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.Adapter adapter, ViewHolder viewHolder, int position) {
        viewHolder.textView.setText(data.getName());
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (context instanceof MainActivity4) {
//                    ((MainActivity4) context).methodCallBack(position);
//                }
                deleteItemByPosition(position);
            }
        });

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        Button button;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.itemTextView);
            button = itemView.findViewById(R.id.itemButton);
        }
    }


}
