package com.kk.kamranqadeer.salemanager.AllAdapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;
import com.kk.kamranqadeer.salemanager.R;

/**
 * Created by kamran qadeer on 8/28/2018.
 */

public class Recor_Adapter_2 extends RecyclerView.Adapter<Recor_Adapter_2.MyViewHolder> {
    Context context;
    Cursor cursor;
    long id,IDFK;
    String Date,Dozen;
    DbHelper db_helper;

    public Recor_Adapter_2(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        db_helper=new DbHelper(context);
    }

    @Override
    public Recor_Adapter_2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.record_labour_card, parent, false);
        return new Recor_Adapter_2.MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(Recor_Adapter_2.MyViewHolder holder, final int position) {
        // Move the mCursor to the position of the item to be displayed
            if (!cursor.moveToPosition(position)) {
                return; // bail if returned null
            }
            final String Date = cursor.getString(cursor.getColumnIndex(Contracts.Labour_Record_Recycle.COLUMN_1));
            final String Name = cursor.getString(cursor.getColumnIndex(Contracts.Labour_Record_Recycle.COLUMN_2));
            final String ProductName = cursor.getString(cursor.getColumnIndex(Contracts.Labour_Record_Recycle.COLUMN_3));
            final String Work = cursor.getString(cursor.getColumnIndex(Contracts.Labour_Record_Recycle.COLUMN_4));
           final String Amount = cursor.getString(cursor.getColumnIndex(Contracts.Labour_Record_Recycle.COLUMN_5));
            id = cursor.getLong(cursor.getColumnIndex(Contracts.Production_Meterials._ID));
            int i = (int) id;
            final String ss = i + "";
            holder.textView1.setText(Name);
            holder.textView2.setText(ProductName);
            holder.textView3.setText(Work);
            holder.textView4.setText(Amount+" RS");
            holder.textView5.setText(Date);
            // COMPLETED (7) Set the tag of the itemview in the holder to the id
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
        TextView textView1,textView2,textView3,textView4,textView5;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.Name);
            textView2 = (TextView) itemView.findViewById(R.id.Product_name);
            textView3 = (TextView) itemView.findViewById(R.id.dozen);
            textView4 = (TextView) itemView.findViewById(R.id.amount);
            textView5 = (TextView) itemView.findViewById(R.id.Date);
        }
    }
}
