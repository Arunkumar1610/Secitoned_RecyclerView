package com.example.section_recycler_view.viewHolder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.section_recycler_view.R;

public class ChildViewHolder extends RecyclerView.ViewHolder {

    public TextView date;
    public TextView amount;
    public TextView txn_id;

   public TextView status;

    public ImageView divider;

    public ChildViewHolder(View itemView) {
        super(itemView);
        date = (TextView) itemView.findViewById(R.id.date);
        amount = (TextView) itemView.findViewById(R.id.amount);
        txn_id = (TextView) itemView.findViewById(R.id.txn_d);
        divider = (ImageView) itemView.findViewById(R.id.divider);
        status = itemView.findViewById(R.id.status);

    }
}
