package com.kk.kamranqadeer.salemanager.AllAdapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;
import com.kk.kamranqadeer.salemanager.R;
import com.kk.kamranqadeer.salemanager.Record_All_Detail;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.DuplicateFormatFlagsException;

/**
 * Created by kamran qadeer on 8/28/2018.
 */

public class Recor_Adapter_1 extends RecyclerView.Adapter<Recor_Adapter_1.MyViewHolder> {
    Context context;
    Cursor cursor;
    long id,IDFK;
    String Date,Dozen;
    DbHelper db_helper;

    public Recor_Adapter_1(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        db_helper=new DbHelper(context);
    }

    @Override
    public Recor_Adapter_1.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.record_detail_card, parent, false);
        return new Recor_Adapter_1.MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(Recor_Adapter_1.MyViewHolder holder, final int position) {
        // Move the mCursor to the position of the item to be displayed
            if (!cursor.moveToPosition(position)) {
                return; // bail if returned null
            }
            final String Date = cursor.getString(cursor.getColumnIndex(Contracts.Material_record.COLUMN_1));
            final String Name = cursor.getString(cursor.getColumnIndex(Contracts.Material_record.COLUMN_2));
            String purch = cursor.getString(cursor.getColumnIndex(Contracts.Material_record.COLUMN_3));
            String Used = cursor.getString(cursor.getColumnIndex(Contracts.Material_record.COLUMN_4));
            String Left = cursor.getString(cursor.getColumnIndex(Contracts.Material_record.COLUMN_5));
            IDFK = cursor.getLong(cursor.getColumnIndex(Contracts.Material_record.COLUMN_6));
            id = cursor.getLong(cursor.getColumnIndex(Contracts.Material_record._ID));
            int i = (int) id;
            final String ss = i + "";
            DecimalFormat format=new DecimalFormat("#0.00");
            String split_Used[]=Used.split(" "),split_Purch[]=purch.split(" "),split_Left[]=Left.split(" ");
            holder.textView1.setText(Name);
            holder.textView2.setText(format.format(Double.parseDouble(split_Purch[0]))+" "+split_Purch[1]);
            holder.textView3.setText(format.format(Double.parseDouble(split_Used[0]))+" "+split_Used[1]);
            if(Double.parseDouble(split_Left[0])<0){
                holder.textView4.setTextColor(Color.RED);
            }
            holder.textView4.setText(format.format(Double.parseDouble(split_Left[0]))+" "+split_Left[1]);
            holder.itemView.setTag(id);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Intent intent = new Intent(context, Record_All_Detail.class);
                    //intent.putExtra("DATE", DATE);
                    //intent.putExtra("_ID", ss);
                    //context.startActivity(intent);
                }
            });

    }

    @Override
    public int getItemCount() {

        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (cursor != null) cursor.close();
        cursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView1,textView2,textView3,textView4;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.Name);
            textView2 = (TextView) itemView.findViewById(R.id.purchashing);
            textView3 = (TextView) itemView.findViewById(R.id.used);
            textView4 = (TextView) itemView.findViewById(R.id.left);
        }
    }
}
