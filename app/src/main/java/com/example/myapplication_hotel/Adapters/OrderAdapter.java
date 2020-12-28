package com.example.myapplication_hotel.Adapters;

import static android.Manifest.permission.CALL_PHONE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.PermissionChecker;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication_hotel.Activities.MainActivity;
import com.example.myapplication_hotel.DataModels.Order;
import com.example.myapplication_hotel.DataModels.RequestDataModel;
import com.example.myapplication_hotel.R;
import com.example.myapplication_hotel.Utils.Endpoints;
import com.example.myapplication_hotel.Utils.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<Order> dataSet;
    private Context context;

    public OrderAdapter(List<Order> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,
                                 final int position) {
        String str = "Name: " + dataSet.get(position).getName();
        str += "\nHotel Name: " + dataSet.get(position).getHotelName();
        str += "\nNumber of Rooms: " + dataSet.get(position).getNumOfRooms();

        holder.message.setText(str);

        holder.callButton.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                if (PermissionChecker.checkSelfPermission(context, CALL_PHONE)
                        == PermissionChecker.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + dataSet.get(position).getNumber()));
                    context.startActivity(intent);
                } else {
                    ((Activity) context).requestPermissions(new String[]{CALL_PHONE}, 401);
                }
            }
        });

        holder.cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=dataSet.get(position).getHotelName();
                deleteOrder(name);

            }
        });

    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView message;
        ImageView imageView, callButton,cancelButton;

        ViewHolder(final View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            imageView = itemView.findViewById(R.id.image);
            callButton = itemView.findViewById(R.id.call_button);
            cancelButton=itemView.findViewById(R.id.cancel_button);
        }

    }

    private void deleteOrder(final String name)
    {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Endpoints.delete_order, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                OrderAdapter.this.context.startActivity(new Intent(OrderAdapter.this.context,MainActivity.class));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderAdapter.this.context,"Something went wrong",Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY",error.getMessage());

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("name",name);

                return params;
            }
        };
        VolleySingleton.getInstance(OrderAdapter.this.context).addToRequestQueue(stringRequest);

    }

}