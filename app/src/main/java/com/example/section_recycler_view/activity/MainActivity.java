package com.example.section_recycler_view.activity;

import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.fonts.Font;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
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
import com.example.section_recycler_view.adapters.AdapterSectionRecycler;
import com.example.section_recycler_view.dataSet.Child;
import com.example.section_recycler_view.services.CreateCSV_File;
import com.example.section_recycler_view.R;
import com.example.section_recycler_view.dataSet.SectionHeader;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RecyclerView recyclerView;
    Button download_2, webBtn;
    ImageView download;

    Spinner range_s, file_s;
    ImageView close, next;

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

    CreateCSV_File createCSV_file = new CreateCSV_File(MainActivity.this, sectionHeader);
    File file = new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/history55.pdf");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        download = findViewById(R.id.download_img);
        recyclerView = findViewById(R.id.recycler_view);
        textView = findViewById(R.id.heading);
        next = findViewById(R.id.nextPage);
        webBtn = findViewById(R.id.web_btn);

        webBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WebActivity.class);
                startActivity(intent);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });


        rangeList.add("Select Range");
        rangeList.add("All");
        rangeList.add("Last Three Month");
        rangeList.add("Last Six Month");
        rangeList.add("1 Year");

        fileTypeList.add("Select File Type");
        fileTypeList.add("PDF");
        fileTypeList.add("Excel");
        fileTypeList.add("TXT");
        loadData();

        askPermission();
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog download_dialog = new Dialog(MainActivity.this);
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


    }

//    public void askForPermissions() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if (!Environment.isExternalStorageManager()) {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//                startActivity(intent);
//                return;
//            }
//          //  createDir();
//        }

    public void askPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
                return;
            }
            // createDir();
        }

    }

    private void setUIRef() {


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterRecycler = new AdapterSectionRecycler(MainActivity.this, sectionHeader, download, rangeList, fileTypeList);
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
        }, error -> Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show()) {

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(MainActivity.this);
        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }

    void rangeSpinnerRef() {
        range_s.setOnItemSelectedListener(MainActivity.this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rangeList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        range_s.setAdapter(arrayAdapter);
    }

    void fileSpinnerRef() {
        file_s.setOnItemSelectedListener(this);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fileTypeList);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        file_s.setAdapter(arrayAdapter1);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

//    void downloadPDF() {
//        //  Child child1 = transfer.sendData();
//
//
//        PdfDocument pdfDocument = new PdfDocument();
//        Paint paint = new Paint();
//
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
//        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
//        Canvas canvas = page.getCanvas();
//
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(2);
//        canvas.drawRect(20, 780, pageWidth - 20, 860, paint);
//
//        paint.setTextAlign(Paint.Align.LEFT);
//        paint.setStyle(Paint.Style.FILL);
//        canvas.drawText("S.No", 40, 830, paint);
//        canvas.drawText("Date", 50, 830, paint);
//        canvas.drawText("Transaction ID", 150, 830, paint);
//        canvas.drawText("Amount", 250, 830, paint);
//
////        paint.setStyle(Paint.Style.STROKE);
////        paint.setStrokeWidth(2);
////        canvas.drawRect(20, 780, pageWidth - 20, 860, paint);
//
//        paint.setTextAlign(Paint.Align.LEFT);
//      //  paint.setStyle(Paint.Style.FILL);
//        canvas.drawText("1", 140, 830, paint);
//        canvas.drawText("child1.getDate()", 150, 830, paint);
//        canvas.drawText("child1.getTxn_id()", 250, 830, paint);
//        canvas.drawText("child1.getAmount()", 350, 830, paint);
//
//        pdfDocument.finishPage(page);
//
//       // File file = new File(Environment.getExternalStorageDirectory(), "/history.pdf");
//
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                pdfDocument.writeTo((Files.newOutputStream(file.toPath())));
//                Toast.makeText(this, "File Downloaded", Toast.LENGTH_SHORT).show();
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        pdfDocument.close();
//    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int permission3 = ContextCompat.checkSelfPermission(getApplicationContext(), MANAGE_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED && permission3 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, MANAGE_EXTERNAL_STORAGE}, 1);
    }

    private void generatePDF() {
        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        Paint paint = new Paint();
        Paint title = new Paint();

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        // below line is used for setting
        // start page for our PDF file.
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        // creating a variable for canvas
        // from our page of PDF.
        Canvas canvas = myPage.getCanvas();

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.

        // canvas.drawBitmap(scaledbmp, 56, 40, paint);

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.setTextSize(15);

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(this, R.color.purple_200));

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText("A portal for IT professionals.", 209, 100, title);
        canvas.drawText("Geeks for Geeks", 209, 80, title);

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.purple_200));
        title.setTextSize(15);

        // below line is used for setting
        // our text to center of PDF.
        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("This is sample document which we have created.", 396, 560, title);

        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage);

        // below line is used to set the name of
        // our PDF file and its path.
        // File file = new File(Environment.getExternalStorageDirectory(), "GFG.pdf");

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                pdfDocument.writeTo(Files.newOutputStream(file.toPath()));
            }

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(MainActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
    }


    public void createDir() {
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public void highlights() {


        new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(textView, "Button 1", "This is Button 1")
                                .outerCircleColor(R.color.teal_200)
                                .outerCircleAlpha(0.96f)
                                .targetCircleColor(R.color.white)
                                .cancelable(false)
                                .titleTextSize(20)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(10)
                                .descriptionTextColor(R.color.black)
                                .textColor(R.color.black)
                                .textTypeface(Typeface.DEFAULT)
                                .dimColor(R.color.black)
                                .drawShadow(true)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(width),
                        TapTarget.forView(download, "Button 2", "This is Button 2")
                                .outerCircleColor(R.color.teal_200)
                                .outerCircleAlpha(0.96f)
                                .targetCircleColor(R.color.white)
                                .titleTextSize(20)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(10)
                                .descriptionTextColor(R.color.black)
                                .textColor(R.color.black)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.black)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(width),
                        TapTarget.forView(textView, "Button 3", "This is Button 3")
                                .outerCircleColor(R.color.purple_700)
                                .outerCircleAlpha(0.96f)
                                .targetCircleColor(R.color.white)
                                .titleTextSize(20)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(10)
                                .descriptionTextColor(R.color.black)
                                .textColor(R.color.black)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.black)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(60),
                        TapTarget.forView(download, "Button 3", "This is Button 3")
                                .outerCircleColor(R.color.teal_200)
                                .outerCircleAlpha(0.96f)
                                .targetCircleColor(R.color.white)
                                .titleTextSize(20)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(10)
                                .descriptionTextColor(R.color.black)
                                .textColor(R.color.black)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.black)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(60),
                        TapTarget.forView(textView, "Button 3", "This is Button 3")
                                .outerCircleColor(R.color.purple_700)
                                .outerCircleAlpha(0.96f)
                                .targetCircleColor(R.color.white)
                                .titleTextSize(20)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(10)
                                .descriptionTextColor(R.color.black)
                                .textColor(R.color.black)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.black)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(60)).listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {

                        Toast.makeText(MainActivity.this, "Sequence Finished", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                        Toast.makeText(MainActivity.this, "GREAT!", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {

                    }
                }).start();

    }


}

