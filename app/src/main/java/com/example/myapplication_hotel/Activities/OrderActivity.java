package com.example.myapplication_hotel.Activities;

import android.content.Intent;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import com.example.myapplication_hotel.Adapters.OrderAdapter;
import com.example.myapplication_hotel.DataModels.Order;
import com.example.myapplication_hotel.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    List<Order> searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        searchList = new ArrayList<>();
        String json;
        Intent intent = getIntent();
        json = intent.getStringExtra("json");
        TextView heading = findViewById(R.id.heading);
        String str = "Reservations";
        heading.setText(str);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Order>>() {}.getType();
        List<Order> dataModels = gson.fromJson(json, type);
        if (dataModels != null && dataModels.isEmpty()) {
            heading.setText("No results");
        }else if(dataModels!=null){
            searchList.addAll(dataModels);
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        OrderAdapter adapter = new OrderAdapter(searchList, OrderActivity.this);
        recyclerView.setAdapter(adapter);

    }


}

