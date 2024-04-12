package com.example.section_recycler_view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.section_recycler_view.dataSet.Child;
import com.example.section_recycler_view.viewHolder.ChildViewHolder;
import com.example.section_recycler_view.viewHolder.HeaderViewHolder;
import com.example.section_recycler_view.R;
import com.example.section_recycler_view.dataSet.SectionHeader;
import com.example.section_recycler_view.library.SectionRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdapterSectionRecycler extends SectionRecyclerViewAdapter<SectionHeader, Child, HeaderViewHolder, ChildViewHolder>{

    Context context;
    List<SectionHeader> sectionHeaderItemList;
    ImageView imageView;


    ArrayList<String> fileTypeList;
    ArrayList<String> rangeList;


    ImageView download;
    Spinner range_s, file_s;
    ImageView close;
    Button download_2;

    public AdapterSectionRecycler(Context context, List<SectionHeader> sectionHeaderItemList, ImageView imageView, ArrayList<String> rangeList,ArrayList<String> fileTypeList) {
        super(context, sectionHeaderItemList);
        this.context = context;
        this.sectionHeaderItemList = sectionHeaderItemList;
        this.imageView=imageView;
    }



    @Override
    public HeaderViewHolder onCreateSectionViewHolder(ViewGroup sectionViewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.header_section, sectionViewGroup, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.child_section, childViewGroup, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindSectionViewHolder(HeaderViewHolder sectionViewHolder, int sectionPosition, SectionHeader sectionHeader) {
        sectionViewHolder.name.setText(sectionHeader.getSectionText());
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindChildViewHolder(ChildViewHolder childViewHolder, int sectionPosition, int childPosition, Child child) {
        childViewHolder.date.setText(child.getDate());
        childViewHolder.amount.setText("₹ " + child.getAmount());
        childViewHolder.txn_id.setText(child.getTxn_id());

        if (childPosition%2!=0)
        {
            childViewHolder.status.setTextColor(Color.parseColor("#B71C1C"));
        }else {
            childViewHolder.status.setTextColor(Color.parseColor("#388E3C"));
        }

            if (sectionHeaderItemList.get(sectionPosition).childList.size()-1==childPosition)
            {
                childViewHolder.divider.setVisibility(View.GONE);
            }
    }

}
