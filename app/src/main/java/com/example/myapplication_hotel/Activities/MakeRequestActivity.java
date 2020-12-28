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

public class MakeRequestActivity extends AppCompatActivity {
    private EditText s_name,s_age,s_city,s_address,s_hotel_name,s_adults,s_children,s_duration,s_room_num;
    private Button book_Btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_request);

        s_name=findViewById(R.id.name);
        s_city=findViewById(R.id.city);
        s_age=findViewById(R.id.age);
        s_address=findViewById(R.id.address);
        s_hotel_name=findViewById(R.id.hotel_name);
        s_adults=findViewById(R.id.adults);
        s_children=findViewById(R.id.children);
        s_duration=findViewById(R.id.days);
        s_room_num=findViewById(R.id.room_number);
        book_Btn=findViewById(R.id.book_button);


        book_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,city,age,address,hotelName,adults,children,duration,num_of_rooms;
                name=s_name.getText().toString();
                city=s_city.getText().toString();
                age=s_age.getText().toString();
                address=s_address.getText().toString();
                hotelName=s_hotel_name.getText().toString();
                adults=s_adults.getText().toString();
                children=s_children.getText().toString();
                duration=s_duration.getText().toString();
                num_of_rooms=s_room_num.getText().toString();


                if(isValid(name,age,city,address,hotelName,adults,children,num_of_rooms,duration))
                {
                    register(name,age,city,address,hotelName,adults,children,num_of_rooms,duration);
                }


            }
        });
    }

    private void register(final String name,final String age,final String city,final String address,final String hotelName,final String adults,final String children,final String num_of_rooms,final String duration)
    {
        final String mobile=PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("number","0");
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Endpoints.book_room, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Success"))
                {
                    Toast.makeText(MakeRequestActivity.this,response,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MakeRequestActivity.this,MainActivity.class));
                    MakeRequestActivity.this.finish();
                }
                else{
                    Toast.makeText(MakeRequestActivity.this,response,Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MakeRequestActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY",error.getMessage());

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("name",name);
                params.put("age",age);
                params.put("city",city);
                params.put("number",mobile);
                params.put("address",address);
                params.put("hotelname",hotelName);
                params.put("adults",adults);
                params.put("children",children);
                params.put("room_num",num_of_rooms);
                params.put("no_of_days",duration);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private boolean isValid(String name,String age,String city,String address,String hotelName,String adults,String children,String num_of_rooms,String duration)
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
        else if(age.isEmpty())
        {
            showMessage("Age is empty");
            return false;
        }
        else if(address.isEmpty())
        {
            showMessage("Address is empty");
            return false;
        }
        else if(hotelName.isEmpty())
        {
            showMessage("Please fill a Hotel Name");
            return false;
        }
        else if(adults.isEmpty())
        {
            showMessage("Enter the number of adults");
            return false;
        }
        else if(children.isEmpty())
        {
            showMessage("Enter the number of children");
            return false;
        }
        else if(duration.isEmpty())
        {
            showMessage("Enter the duration of your stay");
            return false;
        }
        else if(num_of_rooms.isEmpty())
        {
            showMessage("Enter the number of rooms");
            return false;
        }
        return true;
    }

    private void showMessage(String s)
    {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
}
