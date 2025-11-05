package com.example.repairapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textDateTime;
    private Handler handler;
    private Runnable runnable;
    private DatabaseHelper dbHelper;
    private RequestAdapter adapterAppliances;
    private RequestAdapter adapterComputers;
    private RequestAdapter adapterMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        textDateTime = findViewById(R.id.textDateTime);
        RecyclerView rvAppliances = findViewById(R.id.recyclerViewAppliances);
        RecyclerView rvComputers = findViewById(R.id.recyclerViewComputers);
        RecyclerView rvMaster = findViewById(R.id.recyclerViewMaster);
        dbHelper = new DatabaseHelper(this);

        rvAppliances.setLayoutManager(new LinearLayoutManager(this));
        rvComputers.setLayoutManager(new LinearLayoutManager(this));
        rvMaster.setLayoutManager(new LinearLayoutManager(this));

        adapterAppliances = new RequestAdapter(dbHelper, "appliance");
        adapterComputers = new RequestAdapter(dbHelper, "computer");
        adapterMaster = new RequestAdapter(dbHelper, "master");

        rvAppliances.setAdapter(adapterAppliances);
        rvComputers.setAdapter(adapterComputers);
        rvMaster.setAdapter(adapterMaster);

        handler = new Handler();
        runnable = () -> {
            updateDateTime();
            handler.postDelayed(this.runnable, 1000);
        };
        handler.post(runnable);

        findViewById(R.id.btnNewRequest).setOnClickListener(v ->
                startActivity(new Intent(this, CreateRequestActivity.class)));
        findViewById(R.id.btnStatistics).setOnClickListener(v ->
                startActivity(new Intent(this, StatisticsActivity.class)));
    }

    private void updateDateTime() {
        String now = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        textDateTime.setText(now);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapterAppliances != null) adapterAppliances.refresh();
        if (adapterComputers != null) adapterComputers.refresh();
        if (adapterMaster != null) adapterMaster.refresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }
}