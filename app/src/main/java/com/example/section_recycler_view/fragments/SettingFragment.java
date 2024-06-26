package com.example.section_recycler_view.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.section_recycler_view.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    ImageView accDrop,beneDrop,infoDrop;
    CardView accCard,beneCard,infoCard;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_setting, container, false);

        accDrop=view.findViewById(R.id.acc_drop);
        beneDrop=view.findViewById(R.id.bene_drop);
        infoDrop=view.findViewById(R.id.info_drop);


        accCard=view.findViewById(R.id.acc_card);
        beneCard=view.findViewById(R.id.bene_card);
        infoCard=view.findViewById(R.id.info_card);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        openCard();
    }

    void openCard()
    {
        accDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (accCard.getVisibility()==View.VISIBLE)
                {
                    accDrop.setImageResource(R.drawable.drop_down);
                    accCard.setVisibility(View.GONE);
                }else {
                    accDrop.setImageResource(R.drawable.drop_up);
                    accCard.setVisibility(View.VISIBLE);
                }
            }
        });

        beneDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (beneCard.getVisibility()==View.VISIBLE)
                {
                    beneDrop.setImageResource(R.drawable.drop_down);
                    beneCard.setVisibility(View.GONE);
                }else {
                    beneDrop.setImageResource(R.drawable.drop_up);
                    beneCard.setVisibility(View.VISIBLE);
                }
            }
        });

        infoDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (infoCard.getVisibility()==View.VISIBLE)
                {
                    infoDrop.setImageResource(R.drawable.drop_down);
                    infoCard.setVisibility(View.GONE);
                }else {
                    infoDrop.setImageResource(R.drawable.drop_up);
                    infoCard.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}