package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    private EditText text;
    private Button button;
    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private Adapter adapter;
    private ArrayList<Data_class> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUI();
        fetch_data();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Please enter a string", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    add_data();
                }
            }
        });
    }

    private void fetch_data() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User complaints");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Data_class data_class = snapshot.getValue(Data_class.class);
                    list.add(data_class);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void add_data() {
        Data_class data_class = new Data_class(text.getText().toString());
        reference.push().setValue(data_class).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete())
                {
                    Toast.makeText(MainActivity.this, "Data added successfully", Toast.LENGTH_SHORT).show();
                    text.setText("");
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Data adding failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUI() {
        text = (EditText) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        reference = FirebaseDatabase.getInstance().getReference("User complaints");

        list = new ArrayList<>();
        adapter = new Adapter(list,this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


}