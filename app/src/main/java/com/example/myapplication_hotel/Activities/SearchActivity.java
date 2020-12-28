package com.example.myapplication_hotel.Activities;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication_hotel.R;
import com.example.myapplication_hotel.Utils.Endpoints;
import com.example.myapplication_hotel.Utils.VolleySingleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final EditText et_price, et_city;
        et_price = findViewById(R.id.price);
        et_city = findViewById(R.id.et_city);
        Button submit_button = findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String price = et_price.getText().toString();
                String city = et_city.getText().toString();
                if(isValid(price, city)){
                    get_search_results(price, city);
                }
            }
        });
    }


    private void get_search_results(final String price, final String city) {
        StringRequest stringRequest = new StringRequest(
                Method.POST, Endpoints.search, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                //json response
                Intent intent = new Intent(SearchActivity.this, SearchResults.class);
                intent.putExtra("city", city);
                intent.putExtra("price", price);
                intent.putExtra("json", response);
                startActivity(intent);
            }
        }, new ErrorListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchActivity.this, "Something went wrong:(", Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY", Objects.requireNonNull(error.getMessage()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("city", city);
                params.put("price", price);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    private boolean isValid(String price, String city){
        if(price.isEmpty()){
            showMsg("Enter a price");
            return false;
        }else if(city.isEmpty()){
            showMsg("Enter city");
            return false;
        }
        return true;
    }


    private void showMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}