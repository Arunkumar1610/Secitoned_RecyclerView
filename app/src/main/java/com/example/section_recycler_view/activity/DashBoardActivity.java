package com.example.section_recycler_view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.section_recycler_view.R;
import com.example.section_recycler_view.fragments.BeneficiaryFragment;
import com.example.section_recycler_view.fragments.HistoryFragment;
import com.example.section_recycler_view.fragments.HomeFragment;
import com.example.section_recycler_view.fragments.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DashBoardActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {


    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    BeneficiaryFragment beneficiaryFragment = new BeneficiaryFragment();
    HistoryFragment historyFragment = new HistoryFragment();
    SettingFragment settingFragment = new SettingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(DashBoardActivity.this);
        bottomNavigationView.setSelectedItemId(R.id.home_m);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home_m:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, homeFragment)
                        .commit();
                return true;

            case R.id.beneficiary_m:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, beneficiaryFragment)
                        .commit();
                return true;

            case R.id.history_m:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, historyFragment)
                        .commit();
                return true;

            case R.id.settings_m:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, settingFragment)
                        .commit();
                return true;
        }

        return false;
    }
}