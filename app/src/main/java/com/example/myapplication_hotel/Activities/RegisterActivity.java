package com.example.myapplication_hotel.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication_hotel.R;
import com.example.myapplication_hotel.Utils.Endpoints;
import com.example.myapplication_hotel.Utils.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText s_name,s_city,s_email,s_mobile,s_password;
    private Button submit_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        s_name=findViewById(R.id.name);
        s_city=findViewById(R.id.city);
        s_email=findViewById(R.id.email);
        s_mobile=findViewById(R.id.number);
        s_password=findViewById(R.id.password);
        submit_btn=findViewById(R.id.submit_button);


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,city,email,mobile,password;
                name=s_name.getText().toString();
                city=s_city.getText().toString();
                email=s_email.getText().toString();
                mobile=s_mobile.getText().toString();
                password=s_password.getText().toString();

                if(isValid(name,city,email,password,mobile))
                {
                    register(name,city,email,password,mobile);
                }
                showMessage(name+"\n"+city+"\n"+email+"\n"+mobile+"\n"+password);

            }
        });
    }

    private void register(final String name,final String city,final String email,final String password,final String mobile)
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Endpoints.register_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Success"))
                {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("city",city).apply();
                    Toast.makeText(RegisterActivity.this,response,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                    RegisterActivity.this.finish();
                }
                else{
                    Toast.makeText(RegisterActivity.this,response,Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY",error.getMessage());

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("name",name);
                params.put("city",city);
                params.put("email",email);
                params.put("password",password);
                params.put("number",mobile);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private boolean isValid(String name,String city,String email,String password,String mobile)
    {
        if(name.isEmpty())
        {
            showMessage("Name is empty");
            return false;
        }
        else if(city.isEmpty())
        {
            showMessage("City is empty");
            return false;
        }
        else if(password.isEmpty())
        {
            showMessage("Password is empty");
            return false;
        }
        else if(email.isEmpty())
        {
            showMessage("Email is empty");
        }
        else if(mobile.length()!=10)
        {
            showMessage("Please enter a valid mobile number");
        }
        return true;
    }

    private void showMessage(String s)
    {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
}
