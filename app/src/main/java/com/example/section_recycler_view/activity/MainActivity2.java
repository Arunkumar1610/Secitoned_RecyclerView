package com.example.section_recycler_view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;


import com.example.section_recycler_view.R;
import com.example.section_recycler_view.adapters.RecyclerViewAdapter;
import com.example.section_recycler_view.dataSet.RecyclerData;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity2 extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<RecyclerData> recyclerDataArrayList;
    RecyclerViewAdapter recyclerViewAdapter;
    SwipeRefreshLayout swipeRefreshLayout;


    private Paint mClearPaint;
    private ColorDrawable mBackground;
    private int backgroundColor;
    private Drawable deleteDrawable;
    private int intrinsicWidth;
    private int intrinsicHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // initializing our variables.
        recyclerView = findViewById(R.id.recycler_View);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);


        //For Color
        mBackground = new ColorDrawable();
        backgroundColor = Color.parseColor("#7D0A0A");
        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        deleteDrawable = ContextCompat.getDrawable(this, R.drawable.baseline_delete_24);
        intrinsicWidth = deleteDrawable.getIntrinsicWidth();
        intrinsicHeight = deleteDrawable.getIntrinsicHeight();

        // creating new array list.
        recyclerDataArrayList = new ArrayList<>();

        // in below line we are adding data to our array list.
        recyclerDataArrayList.add(new RecyclerData("Item 1", "Item 1 decription"));
        recyclerDataArrayList.add(new RecyclerData("Item 2", "Item 2 decription"));
        recyclerDataArrayList.add(new RecyclerData("Item 3", "Item 3 decription"));
        recyclerDataArrayList.add(new RecyclerData("Item 4", "Item 4 decription"));
        recyclerDataArrayList.add(new RecyclerData("Item 5", "Item 5 decription"));
        recyclerDataArrayList.add(new RecyclerData("Item 6", "Item 6 decription"));
        recyclerDataArrayList.add(new RecyclerData("Item 7", "Item 7 decription"));
        recyclerDataArrayList.add(new RecyclerData("Item 8", "Item 8 decription"));
        recyclerDataArrayList.add(new RecyclerData("Item 9", "Item 1 decription"));
        recyclerDataArrayList.add(new RecyclerData("Item 10", "Item 2 decription"));
        recyclerDataArrayList.add(new RecyclerData("Item 11", "Item 3 decription"));
        recyclerDataArrayList.add(new RecyclerData("Item 12", "Item 4 decription"));
        recyclerDataArrayList.add(new RecyclerData("Item 13", "Item 5 decription"));
        recyclerDataArrayList.add(new RecyclerData("Item 14", "Item 6 decription"));
        recyclerDataArrayList.add(new RecyclerData("Item 15", "Item 7 decription"));
        recyclerDataArrayList.add(new RecyclerData("Item 16", "Item 8 decription"));
        recyclerDataArrayList.add(new RecyclerData("Item 17", "Item 1 decription"));
        recyclerDataArrayList.add(new RecyclerData("Item 18", "Item 2 decription"));
        recyclerDataArrayList.add(new RecyclerData("Item 19", "Item 3 decription"));
        recyclerDataArrayList.add(new RecyclerData("Item 20", "Item 4 decription"));
        recyclerDataArrayList.add(new RecyclerData("Item 21", "Item 5 decription"));
        recyclerDataArrayList.add(new RecyclerData("Item 22", "Item 6 decription"));
        recyclerDataArrayList.add(new RecyclerData("Item 23", "Item 7 decription"));
        recyclerDataArrayList.add(new RecyclerData("Item 24", "Item 8 decription"));


        recyclerViewAdapter = new RecyclerViewAdapter(recyclerDataArrayList, this);


        LinearLayoutManager manager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(recyclerViewAdapter);


        swipeToDelete();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                Collections.shuffle(recyclerDataArrayList, new Random(System.currentTimeMillis()));
                recyclerViewAdapter = new RecyclerViewAdapter(recyclerDataArrayList, MainActivity2.this);
                recyclerView.setAdapter(recyclerViewAdapter);
            }
        });

    }

    void swipeToDelete() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return 0.7f;
            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, ItemTouchHelper.LEFT);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;
                int itemHeight = itemView.getHeight();

                boolean isCancelled = dX == 0 && !isCurrentlyActive;

                if (isCancelled) {
                    clearCanvas(c, itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    return;
                }

                mBackground.setColor(backgroundColor);
                mBackground.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                mBackground.draw(c);

                int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int deleteIconMargin = (itemHeight - intrinsicHeight) / 2;
                int deleteIconLeft = itemView.getRight() - deleteIconMargin - intrinsicWidth;
                int deleteIconRight = itemView.getRight() - deleteIconMargin;
                int deleteIconBottom = deleteIconTop + intrinsicHeight;


                deleteDrawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
                deleteDrawable.draw(c);
            }

            private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
                c.drawRect(left, top, right, bottom, mClearPaint);

            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                RecyclerData deletedData = recyclerDataArrayList.get(viewHolder.getAdapterPosition());
                int position = viewHolder.getAdapterPosition();

                alertDialog(viewHolder, deletedData, position);

            }

        }).attachToRecyclerView(recyclerView);
    }


    void alertDialog(RecyclerView.ViewHolder viewHolder, RecyclerData deletedData, int position) {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Alert!");
        alertDialogBuilder.setMessage("Are you sure! \nYou want to delete?");

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recyclerDataArrayList.remove(viewHolder.getAdapterPosition());
                recyclerViewAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                Snackbar snackbar = Snackbar
                        .make(swipeRefreshLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        recyclerDataArrayList.add(position, deletedData);
                        recyclerViewAdapter.notifyItemInserted(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recyclerDataArrayList.remove(position);
                recyclerViewAdapter.notifyItemRemoved(position);
                recyclerDataArrayList.add(position, deletedData);
                recyclerViewAdapter.notifyItemInserted(position);
            }
        });
        alertDialogBuilder.setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

}