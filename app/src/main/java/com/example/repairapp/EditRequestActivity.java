package com.example.repairapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditRequestActivity extends AppCompatActivity {

    private EditText editName, editPhone, editModel, editDesc;
    private com.google.android.material.textfield.MaterialAutoCompleteTextView spinnerType;
    private Button btnDate, btnTime, btnSave;
    private DatabaseHelper dbHelper;
    private int requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_create_request);

        Intent intent = getIntent();
        requestId = intent.getIntExtra("request_id", -1);
        if (requestId == -1) {
            Toast.makeText(this, "Ошибка: заявка не найдена", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(this);
        initViews();
        loadRequestData();
        setupClickListeners();
    }

    private void initViews() {
        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        editModel = findViewById(R.id.editModel);
        editDesc = findViewById(R.id.editDesc);
        spinnerType = findViewById(R.id.spinnerType);
        btnDate = findViewById(R.id.btnDate);
        btnTime = findViewById(R.id.btnTime);
        btnSave = findViewById(R.id.btnSave);

        String[] types = getResources().getStringArray(R.array.request_types);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, types);
        spinnerType.setAdapter(adapter);
    }

    private void loadRequestData() {
        for (Request r : dbHelper.getAllRequests()) {
            if (r.getId() == requestId) {
                editName.setText(r.getOwnerName());
                editPhone.setText(r.getPhone());
                editModel.setText(r.getModel());
                editDesc.setText(r.getDescription());

                String displayType = "";
                if ("appliance".equals(r.getType())) displayType = "Ремонт бытовой техники";
                else if ("computer".equals(r.getType())) displayType = "Ремонт компьютеров";
                else if ("master".equals(r.getType())) displayType = "Вызов мастера на дом";

                spinnerType.setText(displayType, false);
                btnDate.setText(r.getDateCreated());
                btnTime.setText(r.getTimeCreated());
                return;
            }
        }
        Toast.makeText(this, "Заявка не найдена", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setupClickListeners() {
        btnDate.setOnClickListener(v -> showDatePicker());
        btnTime.setOnClickListener(v -> showTimePicker());
        btnSave.setText("Сохранить изменения");
        btnSave.setOnClickListener(v -> saveChanges());
    }

    private void showDatePicker() {
        String dateStr = btnDate.getText().toString();
        int day = 1, month = 0, year = 2025;
        try {
            String[] parts = dateStr.split("\\.");
            day = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1]) - 1;
            year = Integer.parseInt(parts[2]);
        } catch (Exception ignored) {}

        new DatePickerDialog(this, (view, y, m, d) -> {
            String newDate = String.format(Locale.getDefault(), "%02d.%02d.%d", d, m + 1, y);
            btnDate.setText(newDate);
        }, year, month, day).show();
    }

    private void showTimePicker() {
        String timeStr = btnTime.getText().toString();
        int hour = 12, minute = 0;
        try {
            String[] parts = timeStr.split(":");
            hour = Integer.parseInt(parts[0]);
            minute = Integer.parseInt(parts[1]);
        } catch (Exception ignored) {}

        new TimePickerDialog(this, (view, h, m) -> {
            String newTime = String.format(Locale.getDefault(), "%02d:%02d", h, m);
            btnTime.setText(newTime);
        }, hour, minute, true).show();
    }

    private void saveChanges() {
        String name = editName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String model = editModel.getText().toString().trim();
        String desc = editDesc.getText().toString().trim();
        String newDate = btnDate.getText().toString();
        String newTime = btnTime.getText().toString();

        if (name.isEmpty() || phone.isEmpty() || model.isEmpty() || newDate.isEmpty() || newTime.isEmpty()) {
            Toast.makeText(this, "Заполните все обязательные поля", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedType = spinnerType.getText().toString();
        String typeCode;
        if ("Ремонт бытовой техники".equals(selectedType)) typeCode = "appliance";
        else if ("Ремонт компьютеров".equals(selectedType)) typeCode = "computer";
        else typeCode = "master";

        dbHelper.updateRequestFull(requestId, name, phone, model, desc, newDate, newTime, "новая", typeCode);
        Toast.makeText(this, "Заявка обновлена!", Toast.LENGTH_SHORT).show();
        finish();
    }
}