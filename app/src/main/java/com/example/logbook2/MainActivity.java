package com.example.logbook2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> taskList;
    ImageView empty_image_view;
    TextView no_data;
    private FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialise UI components
        recyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.add_btn);
        taskList = new ArrayList<>();
        empty_image_view = findViewById(R.id.empty_image_view);
        no_data = findViewById(R.id.no_data);

        //Set up the RecyclerView with the TaskAdapter
        taskAdapter = new TaskAdapter(taskList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);

        //Set up the click listener for the add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MainActivity", "onActivityResult called with requestCode: " + requestCode + ", resultCode: " + resultCode);

        //Handle result from AddActivity
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Task newTask = (Task) data.getSerializableExtra("task");
            if (newTask != null) {
                //Add the task to the list and refresh RecyclerView
                taskList.add(newTask);
                taskAdapter.notifyDataSetChanged();
                Log.d("MainActivity", "New Task Added: " + newTask.getName());
            } else {
                Log.d("MainActivity", "New Task is null.");
            }
        } //Handle result from UpdateActivity
        else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            Task updatedTask = (Task) data.getSerializableExtra("updatedTask");
            int position = data.getIntExtra("position", -1);

            if (updatedTask != null && position != -1) {
                //Update the task in the list and notify the adapter
                taskList.set(position, updatedTask);
                taskAdapter.notifyItemChanged(position);
            }
        }

        //Handle the delete task function from the UpdateActivity
        int deletePosition = data.getIntExtra("deletePosition", -1);
        if (deletePosition != -1) {
            //Remove the task from the list and notify the adapter
            taskList.remove(deletePosition);
            taskAdapter.notifyItemRemoved(deletePosition);
        }
        updateUI();
    }




    // Update UI based on task list
    private void updateUI() {
        if (taskList.isEmpty()) {
            empty_image_view.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        } else {
            empty_image_view.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
        taskAdapter.notifyDataSetChanged();
    }

    //Create menu to delete all tasks
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Handle item selections in the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_all) {
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    //Confirm dialog for delete all tasks
    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All Tasks?");
        builder.setMessage("Are you sure want to delete all the tasks?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                taskList.clear();
                updateUI();
                Toast.makeText(MainActivity.this, "All Task Deleted!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }

}