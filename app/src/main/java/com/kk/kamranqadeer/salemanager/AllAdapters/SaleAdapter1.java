package com.kk.kamranqadeer.salemanager.AllAdapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;
import com.kk.kamranqadeer.salemanager.R;
import com.kk.kamranqadeer.salemanager.SaleList;

/**
 * Created by kamran qadeer on 7/31/2018.
 */

public class SaleAdapter1 extends RecyclerView.Adapter<SaleAdapter1.MyViewHolder> {
    Context context;
    Cursor cursor;
    DbHelper db_helper;
    long id;
    int check;

    public SaleAdapter1(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.productioncard, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // Move the mCursor to the position of the item to be displayed
        if (!cursor.moveToPosition(position)) {
            return; // bail if returned null
        }
        final String Production_Name = cursor.getString(cursor.getColumnIndex(Contracts.Production.COLUMN_1));
        final String Production_Scale = cursor.getString(cursor.getColumnIndex(Contracts.Production.COLUMN_2));
        final String Production_Grade = cursor.getString(cursor.getColumnIndex(Contracts.Production.COLUMN_3));
        final String Production_code = cursor.getString(cursor.getColumnIndex(Contracts.Production.COLUMN_4));
        final String Production_date_time = cursor.getString(cursor.getColumnIndex(Contracts.Production.COLUMN_5));
        id = cursor.getLong(cursor.getColumnIndex(Contracts.Production._ID));
        int i = (int) id;
        final String ss = i + "";
        holder.textView1.setText(Production_Name+" "+Production_Scale);
        holder.textView3.setText(SetDateString(Production_date_time));
        holder.textView4.setText(Production_Grade+" code_"+Production_code);
        // COMPLETED (7) Set the tag of the itemview in the holder to the id
        holder.itemView.setTag(id);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SaleList.class);
                intent.putExtra("Product_Name", Production_Name+"  "+Production_Scale);
                intent.putExtra("_ID", ss);
                context.startActivity(intent);
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
        TextView textView1, textView3, textView4;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.ProductName);
            textView3 = (TextView) itemView.findViewById(R.id.Production_date_time);
            textView4 = (TextView) itemView.findViewById(R.id.grade);
        }
    }
    public String SetDateString(String s) {
        String string = "";
        String[] items1 = s.split("-");
        String month = items1[0];
        String year = items1[2];
        String d=items1[1];
        if (month.equals("01") || month.equals("1")) {
            string = "January";
        } else if (month.equals("02") || month.equals("2")) {
            string = "February";
        } else if (month.equals("03") || month.equals("3")) {
            string = "March";
        } else if (month.equals("04") || month.equals("4")) {
            string = "April";
        } else if (month.equals("05") || month.equals("5")) {
            string = "May";
        } else if (month.equals("06") || month.equals("6")) {
            string = "June";
        } else if (month.equals("07") || month.equals("7")) {
            string = "July";
        } else if (month.equals("08") || month.equals("8")) {
            string = "Agust";
        } else if (month.equals("09") || month.equals("9")) {
            string = "September";
        } else if (month.equals("10")) {
            string = "October";
        } else if (month.equals("11")) {
            string = "November";
        } else if (month.equals("12")) {
            string = "December";
        } else {
            string = "INVALIDE MONTH";
        }

        String date = " ";
        date = string + " "+ d +" "+ year;
        return date;
    }
}
