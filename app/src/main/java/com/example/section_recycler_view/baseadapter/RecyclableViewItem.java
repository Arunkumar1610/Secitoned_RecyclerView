package com.example.section_recycler_view.baseadapter;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public interface RecyclableViewItem<T extends RecyclerView.ViewHolder> {

    Class<T> getViewHolderClassType();
    int getViewType();
    T onCreateViewHolder(ViewGroup parent, GenericRecyclerAdapter.onItemClickListener itemClickListener);
    void onBindViewHolder(RecyclerView.Adapter adapter, T viewHolder,int position);


}
