package com.example.section_recycler_view;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ChildViewHolder extends RecyclerView.ViewHolder {

    TextView name;

    public ChildViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.child);
    }
}
