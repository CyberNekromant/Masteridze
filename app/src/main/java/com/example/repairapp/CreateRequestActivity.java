package com.example.repairapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateRequestActivity extends AppCompatActivity {

    private EditText editName, editPhone, editModel, editDesc;
    private com.google.android.material.textfield.MaterialAutoCompleteTextView spinnerType;
    private Button btnDate, btnTime, btnSave;
    private String selectedDate = "", selectedTime = "";
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_create_request);

        dbHelper = new DatabaseHelper(this);
        initViews();
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

        btnDate.setOnClickListener(v -> showDatePicker());
        btnTime.setOnClickListener(v -> showTimePicker());
        btnSave.setOnClickListener(v -> saveRequest());
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            selectedDate = String.format(Locale.getDefault(), "%02d.%02d.%d", day, month + 1, year);
            btnDate.setText(selectedDate);
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(this, (view, hour, minute) -> {
            selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
            btnTime.setText(selectedTime);
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    private void saveRequest() {
        String name = editName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String model = editModel.getText().toString().trim();
        String desc = editDesc.getText().toString().trim();
        String typeStr = spinnerType.getText().toString();

        if (name.isEmpty() || phone.isEmpty() || model.isEmpty() || selectedDate.isEmpty() || selectedTime.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        String type;
        if ("Ремонт бытовой техники".equals(typeStr)) type = "appliance";
        else if ("Ремонт компьютеров".equals(typeStr)) type = "computer";
        else type = "master";

        Request request = new Request(0, name, phone, model, desc, selectedDate, selectedTime, "новая", type);
        dbHelper.insertRequest(request);
        Toast.makeText(this, "Заявка создана!", Toast.LENGTH_SHORT).show();
        finish();
    }
}