package com.example.section_recycler_view.baseadapter;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GenericRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    SparseArray<Integer> mViewTypeMap;

    List<RecyclableViewItem> mDataStore;
    protected onItemClickListener mItemClickListener;
    public GenericRecyclerAdapter(){

    }
    public GenericRecyclerAdapter(List<RecyclableViewItem> data){
        super();
        mDataStore = data;
        generateViewTypeMapping();
    }


    public GenericRecyclerAdapter(List<RecyclableViewItem> data, onItemClickListener itemClickListener)
    {
        this(data);
        mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int position = mViewTypeMap.get(viewType);
        if(position >= 0)
        {
            return mDataStore.get(position).onCreateViewHolder(parent, mItemClickListener);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        mDataStore.get(position).onBindViewHolder(this, holder,position);
    }

    @Override
    public int getItemCount() {
        if(mDataStore != null)
        {
            return mDataStore.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(mDataStore != null && position < mDataStore.size())
        {
            return  mDataStore.get(position).getViewType();
        }
        return super.getItemViewType(position);
    }

    private void generateViewTypeMapping(){
        mViewTypeMap = new SparseArray<Integer>();
        if(mDataStore != null && !mDataStore.isEmpty())
        {
            int position = 0;

            for(RecyclableViewItem item : mDataStore)
            {
                addMappingForItem(item, position);

                position++;
            }
        }
    }

    private void addMappingForItem(RecyclableViewItem item, int position)
    {
        if(mViewTypeMap.indexOfKey(item.getViewType()) < 0)
        {
            mViewTypeMap.put(item.getViewType(), position);
        }
    }

    public void appendItem(RecyclableViewItem item){
        if(mDataStore == null)
        {
            mDataStore = new ArrayList<RecyclableViewItem>();
        }

        int index = mDataStore.size();
        mDataStore.add(item);

        addMappingForItem(item, index);
        notifyItemInserted(index);
    }

    public void addItemAtPosition(RecyclableViewItem item, int position)
    {
        if(mDataStore == null)
        {
            mDataStore = new ArrayList<RecyclableViewItem>();
        }

        mDataStore.add(position, item);
        generateViewTypeMapping();
        notifyItemInserted(position);
    }

    public RecyclableViewItem removeItemAtPosition(int position)
    {
        RecyclableViewItem item = null;

        if(mDataStore != null && mDataStore.size() > position)
        {
            item = mDataStore.remove(position);
            if(mViewTypeMap.get(item.getViewType()) == position)
            {
                generateViewTypeMapping();
            }
            notifyItemRemoved(position);
        }
        return item;
    }

    public void removeItem(RecyclableViewItem item){
        if(mDataStore != null && !mDataStore.isEmpty()){
            int position = mDataStore.indexOf(item);
            if(mDataStore.remove(item))
            {
                if(mViewTypeMap.get(item.getViewType()) == position)
                {
                    generateViewTypeMapping();
                }
                notifyItemRemoved(position);
            }
        }
    }

    public void setItemAtPosition(int position, RecyclableViewItem item){
        if(mDataStore != null && !mDataStore.isEmpty())
        {
            int viewType = mDataStore.get(position).getViewType();
            mDataStore.set(position, item);
            if(mViewTypeMap.get(viewType) == position)
            {
                generateViewTypeMapping();
            }
            else {
                addMappingForItem(item, position);
            }
            notifyItemChanged(position);
        }
    }

    public RecyclableViewItem getItemAtPosition(int position)
    {
        if(mDataStore != null && !mDataStore.isEmpty() && position >=0 && position < mDataStore.size())
        {
            return mDataStore.get(position);
        }

        return null;
    }

    public void moveItem(int fromPos, int toPos)
    {
        if(mDataStore != null && !mDataStore.isEmpty())
        {
            RecyclableViewItem item = mDataStore.remove(fromPos);
            mDataStore.add(toPos, item);
            if(mViewTypeMap.get(item.getViewType()) == fromPos)
            {
                mViewTypeMap.put(item.getViewType(), toPos);
            }

            notifyItemMoved(fromPos, toPos);
        }
    }

    protected int viewTypePosition(int viewType){
        return mViewTypeMap.get(viewType);
    }

    public interface onItemClickListener{
        void onItemClick(int position, View clickedView);
    }
}
