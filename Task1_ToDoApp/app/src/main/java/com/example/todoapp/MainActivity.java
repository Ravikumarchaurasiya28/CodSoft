package com.example.todoapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Adapter.ToDoAdapter;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {
    private RecyclerView recyclerView;
    private ToDoAdapter toDoAdapter;
    private List<ToDoModel> taskList;
    private DatabaseHandler db;
    FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        button = findViewById(R.id.button);
        db = new DatabaseHandler(this);
        db.openDatabase();

        taskList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toDoAdapter = new ToDoAdapter(db, this);
        recyclerView.setAdapter(toDoAdapter);
        
//        for (int i = 1; i <= 5; i++) {
//            ToDoModel task = new ToDoModel();
//            task.setTask("Task " + i);
//            task.setStatus(0);
//            task.setId(i);
//            taskList.add(task);
//        }

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(toDoAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        toDoAdapter.setTasks(taskList);
        
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
    }
    
    @Override
    public void handleDialogClose(DialogInterface dialogInterface) {
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        toDoAdapter.setTasks(taskList);
        toDoAdapter.notifyDataSetChanged();
    }
}
