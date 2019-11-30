package com.kk.kamranqadeer.salemanager.AllAdapters;

import android.content.Context;
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

/**
 * Created by kamran qadeer on 8/28/2018.
 */

public class Recor_Adapter_3 extends RecyclerView.Adapter<Recor_Adapter_3.MyViewHolder> {
    Context context;
    Cursor cursor;
    long id;
    DbHelper db_helper;

    public Recor_Adapter_3(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        db_helper=new DbHelper(context);
    }

    @Override
    public Recor_Adapter_3.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.record_production_card, parent, false);
        return new Recor_Adapter_3.MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(Recor_Adapter_3.MyViewHolder holder, final int position) {
        // Move the mCursor to the position of the item to be displayed
            if (!cursor.moveToPosition(position)) {
                return; // bail if returned null
            }
            final String Name = cursor.getString(cursor.getColumnIndex(Contracts.Production_Record.COLUMN_2));
            final String production = cursor.getString(cursor.getColumnIndex(Contracts.Production_Record.COLUMN_3));
            final String sale = cursor.getString(cursor.getColumnIndex(Contracts.Production_Record.COLUMN_4));
            final String left = cursor.getString(cursor.getColumnIndex(Contracts.Production_Record.COLUMN_5));
            id = cursor.getLong(cursor.getColumnIndex(Contracts.Production_Record._ID));
            int i = (int) id;
            final String ss = i + "";
            holder.textView1.setText(Name);
            holder.textView2.setText(production+" Dozen");
            holder.textView3.setText(sale+" Dozen");
            if(Double.parseDouble(left)<0){
                holder.textView4.setTextColor(Color.RED);
            }
            holder.textView4.setText(left+" Dozen");
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
            textView2 = (TextView) itemView.findViewById(R.id.production);
            textView3 = (TextView) itemView.findViewById(R.id.sale);
            textView4 = (TextView) itemView.findViewById(R.id.left);
        }
    }
}
