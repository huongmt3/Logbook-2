package com.example.logbook2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    private EditText taskName;
    private TextView showDate, showTime, datePicker, timePicker;
    private Button addButton;
    private String selectedDate, selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        taskName = findViewById(R.id.task_name);
        showDate = findViewById(R.id.show_date);
        showTime = findViewById(R.id.show_time);
        datePicker = findViewById(R.id.date_picker);
        timePicker = findViewById(R.id.time_picker);
        addButton = findViewById(R.id.add_btn);

        // Click listener for date and time picker
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePicker();
            }
        });

        addButton.setOnClickListener(v -> {
            String name = taskName.getText().toString();
            if (name.isEmpty() || selectedDate == null || selectedTime == null) {
                Toast.makeText(AddActivity.this, "Please enter all information!", Toast.LENGTH_SHORT).show();
            } else {
                Task newTask = new Task(name, selectedDate, selectedTime);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("task", newTask);
                setResult(RESULT_OK, resultIntent);
                Toast.makeText(AddActivity.this, "Task created!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void openDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
            showDate.setText(selectedDate); //Update showDate to show the chosen date
        }, year, month, day);
        datePickerDialog.show();
    }

    private void openTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, selectedHour, selectedMinute) -> {
            selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
            showTime.setText(selectedTime); //Update showDate to show the chosen time
        }, hour, minute, true);
        timePickerDialog.show();
    }
}
