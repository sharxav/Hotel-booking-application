package com.example.myapplication_hotel.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication_hotel.Activities.MakeRequestActivity;
import com.example.myapplication_hotel.DataModels.RequestDataModel;
import com.example.myapplication_hotel.R;

import java.util.List;

import static android.Manifest.permission.CALL_PHONE;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private List<RequestDataModel> dataSet;
    private Context context;

    public RequestAdapter(
            List<RequestDataModel> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_item_layout, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,
                                 final int position) {
        String s=dataSet.get(position).getName()+"\n"+dataSet.get(position).getMessage();
        holder.msg.setText(s);
        Glide.with(context).load(dataSet.get(position).getUrl()).into(holder.imageView);
        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
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

        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent=new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,holder.msg.getText().toString()+"\n\nContact:"+dataSet.get(position).getNumber());
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Hey,could you help here");
                context.startActivity(Intent.createChooser(shareIntent,"Share..."));

            }
        });
        holder.bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestAdapter.this.context.startActivity(new Intent(RequestAdapter.this.context,MakeRequestActivity.class));

            }
        });


    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView msg;
        ImageView imageView,callBtn,shareBtn,bookBtn;

        ViewHolder(final View itemView) {
            super(itemView);
            msg=itemView.findViewById(R.id.message);
            imageView=itemView.findViewById(R.id.image);
            callBtn=itemView.findViewById(R.id.call_btn);
            shareBtn=itemView.findViewById(R.id.share_btn);
            bookBtn=itemView.findViewById(R.id.book_btn);
        }

    }

}


