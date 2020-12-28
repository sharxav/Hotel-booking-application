package com.example.myapplication_hotel.Activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication_hotel.Adapters.OrderAdapter;
import com.example.myapplication_hotel.Adapters.RequestAdapter;
import com.example.myapplication_hotel.DataModels.Order;
import com.example.myapplication_hotel.DataModels.RequestDataModel;
import com.example.myapplication_hotel.R;
import com.example.myapplication_hotel.Utils.Endpoints;
import com.example.myapplication_hotel.Utils.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<RequestDataModel> requestDataModels;
    private RequestAdapter requestAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button make_request_button=findViewById(R.id.make_request);
        make_request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrderDetails();

            }
        });
        Button logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                MainActivity.this.finish();
            }
        });
        requestDataModels=new ArrayList<>();

        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.search_button)
                {
                    startActivity(new Intent(MainActivity.this,SearchActivity.class));
                }
                return false;
            }
        });

        recyclerView=findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        requestAdapter=new RequestAdapter(requestDataModels,this);

        recyclerView.setAdapter(requestAdapter);
        populateHomePage();
        TextView pick_location=(TextView)findViewById(R.id.pick_location);
        String location= PreferenceManager.getDefaultSharedPreferences(this).getString("city","no_city_found");
        if(!location.equals("no_city_found"))
        {
            pick_location.setText(location);
        }
    }

    private void populateHomePage()
    {
        final String city=PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("city","no_city");
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Endpoints.get_requests, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson=new Gson();
                Type type=new TypeToken<List<RequestDataModel>>(){}.getType();
                List<RequestDataModel> dataModels = gson.fromJson(response, type);
                requestDataModels.addAll(dataModels);
                requestAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY",error.getMessage());

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("city",city);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void getOrderDetails()
    {
        final String number=PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("number","0");
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Endpoints.order, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                intent.putExtra("json", response);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY",error.getMessage());

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("number",number);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
}
