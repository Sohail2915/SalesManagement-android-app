package com.kk.kamranqadeer.salemanager.SlideAdapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.kamranqadeer.salemanager.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by kamran qadeer on 7/31/2018.
 */

public class All_BILL extends RecyclerView.Adapter<All_BILL.MyViewHolder> {
    ArrayList<String> pic;
    Context context;
    public All_BILL(Context context, ArrayList<String> pic) {
       this.pic=pic;
       this.context=context;
    }

    @Override
    public All_BILL.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_card,parent,false);
        MyViewHolder holder=new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // Move the mCursor to the position of the item to be displayed
        holder.textView1.setText(pic.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,pic.get(position),Toast.LENGTH_SHORT).show();
                //open pic
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + "/sdcard/SaleManager/Pictures/"+pic.get(position).trim().toString()), "image/*");
                context.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return pic.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.tv_1);
        }
    }
}
