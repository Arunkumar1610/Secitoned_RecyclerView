package com.example.section_recycler_view.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.section_recycler_view.R;
import com.example.section_recycler_view.activity.DashBoardActivity;
import com.example.section_recycler_view.activity.MainActivity;
import com.example.section_recycler_view.activity.MainActivity2;
import com.example.section_recycler_view.adapters.AdapterSectionRecycler;
import com.example.section_recycler_view.dataSet.Child;
import com.example.section_recycler_view.dataSet.SectionHeader;
import com.example.section_recycler_view.services.CreateCSV_File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    RecyclerView recyclerView;
    Button download_2;
    ImageView download,close,next;

    Spinner range_s, file_s;

    AdapterSectionRecycler adapterRecycler;
    List<SectionHeader> sectionHeaders;
    List<SectionHeader> sectionHeader = new ArrayList<>();
    List<Child> childList;
    List<Child> childListCopy;

    Child child1 = new Child();

    ArrayList<String> rangeList = new ArrayList<>();
    ArrayList<String> fileTypeList = new ArrayList<>();
    //    Transfer transfer;
    int pageWidth = 1200;

    int pageHeight = 1120;
    int pagewidth = 792;

    TextView textView;
    int width;


    File file = new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/history55.pdf");










    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        download = view.findViewById(R.id.download_img);
        recyclerView = view.findViewById(R.id.recycler_view);
        textView = view.findViewById(R.id.heading);
        next=view.findViewById(R.id.nextPage);




        rangeList.add("Select Range");
        rangeList.add("All");
        rangeList.add("Last Three Month");
        rangeList.add("Last Six Month");
        rangeList.add("1 Year");

        fileTypeList.add("Select File Type");
        fileTypeList.add("PDF");
        fileTypeList.add("Excel");
        fileTypeList.add("TXT");



        askPermission();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), MainActivity2.class);
                startActivity(intent);
            }
        });




        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog download_dialog = new Dialog(getContext());
                download_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                download_dialog.setContentView(R.layout.download_dialog);


                range_s = download_dialog.findViewById(R.id.range_spinner);
                file_s = download_dialog.findViewById(R.id.file_spinner);
                close = download_dialog.findViewById(R.id.close);
                download_2 = download_dialog.findViewById(R.id.download);

                rangeSpinnerRef();
                fileSpinnerRef();
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        download_dialog.dismiss();
                    }
                });
                download_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CreateCSV_File createCSV_file = new CreateCSV_File(getContext(), sectionHeader);
                        createCSV_file.generateCSV();
                        download_dialog.dismiss();
                    }
                });


                download_dialog.setCancelable(false);
                download_dialog.show();
                download_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                download_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                download_dialog.getWindow().setGravity(Gravity.CENTER);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sectionHeader.clear();
        loadData();
    }


    private void setUIRef() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterRecycler = new AdapterSectionRecycler(getContext(), sectionHeader, download, rangeList, fileTypeList);
        recyclerView.setAdapter(adapterRecycler);
    }


    public void loadData() {

        String url = "https://nexsan.one/api/sectioned_recycler";

        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject rootJSONObject = new JSONObject(response);

                    JSONArray typeJSON = rootJSONObject.getJSONArray("type_one");

                    for (int i = 0; i < typeJSON.length(); i++) {

                        JSONObject jsonObject = typeJSON.getJSONObject(i);

                        String month = jsonObject.getString("month");
                        int total = 0;

                        JSONArray innerJSONarr = jsonObject.getJSONArray("data");

                        childList = new ArrayList<>();

                        for (int j = 0; j < innerJSONarr.length(); j++) {


                            JSONObject dataJSONobj = innerJSONarr.getJSONObject(j);
                            //   total+=Integer.parseInt(dataJSONobj.getString("amount"));
                            childList.add(new Child(dataJSONobj.getString("date"), dataJSONobj.getString("amount"), dataJSONobj.getString("txn_id")));
                            childListCopy = childList;
                        }
                        sectionHeader.add(new SectionHeader(childList, month, String.valueOf(total)));

                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                setUIRef();
            }
        }, error -> Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show()) {

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }

    public void askPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
                return;
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    void fileSpinnerRef() {
        file_s.setOnItemSelectedListener(this);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, fileTypeList);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        file_s.setAdapter(arrayAdapter1);
    }

    void rangeSpinnerRef() {
        range_s.setOnItemSelectedListener(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, rangeList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        range_s.setAdapter(arrayAdapter);
    }
}