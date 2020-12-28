package com.example.myapplication_hotel.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {
    EditText numerr,passerr;
    Button submit_button;
    TextView signUpText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        numerr=findViewById(R.id.username);
        passerr=findViewById(R.id.password);
        submit_button=findViewById(R.id.submit_button);
        signUpText=findViewById(R.id.signUpText);

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numerr.setError(null);
                passerr.setError(null);
                String number=numerr.getText().toString();
                String password=passerr.getText().toString();

                if (isValid(number,password))
                {

                    login(number,password);

                }


            }
        });

    }

    private void login(final String number, final String password) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Endpoints.login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(!response.equals("Invalid Credentials"))
                {
                    showMessage("Login Successful");
                    Toast.makeText(LoginActivity.this,response,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("number",number).apply();
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("city",response).apply();
                    LoginActivity.this.finish();
                }
                else{
                    Toast.makeText(LoginActivity.this,response,Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY",error.getMessage());

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("number",number);
                params.put("password",password);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private boolean isValid(String number,String password)
    {
        if(number.isEmpty())
        {
            showMessage("Enter Mobile Number");
            numerr.setError("Enter Mobile Number");
            return false;
        }
        else if(password.isEmpty())
        {
            showMessage("Enter Password");
            passerr.setError("Enter Password");
            return false;
        }
        return true;
    }

    private void showMessage(String s)
    {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
}
