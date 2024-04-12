package com.example.section_recycler_view.viewHolder;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.section_recycler_view.R;

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public HeaderViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.section);
    }
}
