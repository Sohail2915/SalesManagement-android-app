package com.kk.kamranqadeer.salemanager.AllAdapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;
import com.kk.kamranqadeer.salemanager.PurchasingDetail;
import com.kk.kamranqadeer.salemanager.R;
import com.kk.kamranqadeer.salemanager.SaleList;

/**
 * Created by kamran qadeer on 8/2/2018.
 */

public class Purchasing_Adapter extends RecyclerView.Adapter<Purchasing_Adapter.MyViewHolder> {
    Context context;
    Cursor cursor;
    public Purchasing_Adapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public Purchasing_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.puchasing_card, parent, false);
        return new Purchasing_Adapter.MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(Purchasing_Adapter.MyViewHolder holder, final int position) {
        // Move the mCursor to the position of the item to be displayed
        if (!cursor.moveToPosition(position)) {
            return; // bail if returned null
        }
        final String Purch_Name = cursor.getString(cursor.getColumnIndex(Contracts.Purch_Table.COLUMN_1));
        final String Purch_total_Amount = cursor.getString(cursor.getColumnIndex(Contracts.Purch_Table.COLUMN_2));
        final String Purch_Date = cursor.getString(cursor.getColumnIndex(Contracts.Purch_Table.COLUMN_3));
        long id = cursor.getLong(cursor.getColumnIndex(Contracts.Purch_Table._ID));
        int i = (int) id;
        final String ss = i + "";
        holder.textView2.setText(Purch_total_Amount+" RS");
        holder.textView3.setText(SetDateString(Purch_Date));
        // COMPLETED (7) Set the tag of the itemview in the holder to the id
        holder.itemView.setTag(id);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,PurchasingDetail.class);
                intent.putExtra("Purch_Date_Time", Purch_Date);
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

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView2,textView3,textView4;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView2 = (TextView) itemView.findViewById(R.id.purch_Amount);
            textView3 = (TextView) itemView.findViewById(R.id.purch_Date_Time);
            textView4 = (TextView) itemView.findViewById(R.id.purchashing);
        }
    }
    public String SetDateString(String s){
        String string="";
        String[] items1 = s.split("-");
        String month=items1[0];
        String date1=items1[1];
        String year=items1[2];
        if(month.equals("01")||month.equals("1")){
            string="January";
        }
        else if(month.equals("02")||month.equals("2")){
            string="February";
        }
        else if(month.equals("03")||month.equals("3")){
            string="March";
        }
        else if(month.equals("04")||month.equals("4")){
            string="April";
        }
        else if(month.equals("05")||month.equals("5")){
            string="May";
        }
        else if(month.equals("06")||month.equals("6")){
            string="June";
        }
        else if(month.equals("07")||month.equals("7")){
            string="July";
        }
        else if(month.equals("08")||month.equals("8")){
            string="Agust";
        }
        else if(month.equals("09")||month.equals("9")){
            string="September";
        }
        else if(month.equals("10")){
            string="October";
        }
        else if(month.equals("11")){
            string="November";
        }
        else if(month.equals("12")){
            string="December";
        }
        else{
            string="INVALIDE MONTH";
        }

        String date="";
        date=string+" "+date1+" "+year;
        return date;
    }
}
