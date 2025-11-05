package com.example.repairapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView textStats;
    private RecyclerView rvActive, rvCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_statistics);

        dbHelper = new DatabaseHelper(this);
        textStats = findViewById(R.id.textStats);
        rvActive = findViewById(R.id.recyclerViewActive);
        rvCompleted = findViewById(R.id.recyclerViewCompleted);

        rvActive.setNestedScrollingEnabled(false);
        rvCompleted.setNestedScrollingEnabled(false);
        rvActive.setLayoutManager(new LinearLayoutManager(this));
        rvCompleted.setLayoutManager(new LinearLayoutManager(this));

        loadStatistics();
    }

    private void loadStatistics() {
        List<Request> allRequests = dbHelper.getAllRequests();
        List<Request> activeList = new ArrayList<>();
        List<Request> completedList = new ArrayList<>();

        int total = allRequests.size();
        int appliance = 0, computer = 0, master = 0;
        int activeCount = 0, completedCount = 0;

        for (Request r : allRequests) {
            String type = r.getType();
            if ("appliance".equals(type)) appliance++;
            else if ("computer".equals(type)) computer++;
            else if ("master".equals(type)) master++;

            if ("выполнено".equals(r.getStatus())) {
                completedList.add(r);
                completedCount++;
            } else {
                activeList.add(r);
                activeCount++;
            }
        }

        String stats = String.format(
                "Всего заявок: %d\n" +
                        "• Бытовая техника: %d\n" +
                        "• Компьютеры: %d\n" +
                        "• Вызов мастера: %d\n\n" +
                        "В работе: %d\n" +
                        "Выполнено: %d",
                total, appliance, computer, master, activeCount, completedCount
        );
        textStats.setText(stats);

        rvActive.setAdapter(new RequestAdapter(dbHelper, activeList, true));
        rvCompleted.setAdapter(new RequestAdapter(dbHelper, completedList, true));
    }
}