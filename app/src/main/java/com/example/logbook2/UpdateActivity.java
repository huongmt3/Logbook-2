package com.example.logbook2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {
    private EditText taskName;
    private TextView datePicker, timePicker, showDate, showTime;
    private Button updateBtn, deleteBtn;
    private Task task;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        taskName = findViewById(R.id.task_name2);

        datePicker = findViewById(R.id.date_picker2);
        showDate = findViewById(R.id.show_date2);
        timePicker = findViewById(R.id.time_picker2);
        showTime = findViewById(R.id.show_time2);
        updateBtn = findViewById(R.id.update_btn);
        deleteBtn = findViewById(R.id.delete_btn);

        task = (Task) getIntent().getSerializableExtra("task");
        position = getIntent().getIntExtra("position", -1);

        if (task != null) {
            taskName.setText(task.getName());
            showDate.setText(task.getDate());
            showTime.setText(task.getTime ());
        }

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

        updateBtn.setOnClickListener(v -> {
            updateTask();
        });


        deleteBtn.setOnClickListener(v -> {
            if (task != null) {
                confirmDialog(task.getName(), position);
            }
        });
    }

    // Open date picker dialog
    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                showDate.setText(day + "/" + (month + 1) + "/" + year);
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();
    }

    // Open time picker dialog
    private void openTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                showTime.setText(String.format("%02d:%02d", hourOfDay, minute));
            }
        }, HOUR, MINUTE, true);

        timePickerDialog.show();
    }

    private void confirmDialog(String name, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + name + "?");
        builder.setMessage("Are you sure you want to delete " + name + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("deletePosition", position);
                setResult(RESULT_OK, resultIntent);
                Toast.makeText(UpdateActivity.this, "Task Deleted!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do nothing
            }
        });
        builder.create().show();
    }

    private void updateTask() {
        String name = taskName.getText().toString();
        String date = showDate.getText().toString();
        String time = showTime.getText().toString();

        if (!name.isEmpty()) {
            Task updatedTask = new Task(name, date, time);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedTask", updatedTask);
            resultIntent.putExtra("position", position);

            setResult(RESULT_OK, resultIntent);
            Toast.makeText(UpdateActivity.this, "Task Updated!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(UpdateActivity.this, "Please enter task name!", Toast.LENGTH_SHORT).show();
        }
    }
}