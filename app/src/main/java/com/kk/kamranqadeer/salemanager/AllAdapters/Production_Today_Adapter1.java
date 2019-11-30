package com.kk.kamranqadeer.salemanager.AllAdapters;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;
import com.kk.kamranqadeer.salemanager.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by kamran qadeer on 8/11/2018.
 */

public class Production_Today_Adapter1 extends RecyclerView.Adapter <Production_Today_Adapter1.MyViewHolder> {
    Context context;
    Cursor cursor;
    DbHelper db_helper;
    int i=1;
    long pos;
    AlertDialog alertDialog;
    View mview;
    public Production_Today_Adapter1(Context context, Cursor cursor) {
        this.context = context;
        this.cursor=cursor;
        db_helper = new DbHelper(context);
    }
    @Override
    public Production_Today_Adapter1.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.production_today_card1, parent, false);
        return new Production_Today_Adapter1.MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final Production_Today_Adapter1.MyViewHolder holder, final int position) {
        // Move the mCursor to the position of the item to be displayed
        if (!cursor.moveToPosition(position)) {
            return; // bail if returned null
        }
        String Name = cursor.getString(cursor.getColumnIndex(Contracts.Production_Meterials.COLUMN_1));
        long id = cursor.getLong(cursor.getColumnIndex(Contracts.Production_Meterials._ID));
        holder.textView3.setText(Name);
        holder.itemView.setTag(id);
        String check=Integer.toString(i);
        holder.textView2.setText(check);
        i++;
        holder.textView3.setText(Name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //alert dialog using bulder
                final AlertDialog.Builder mbuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
                mview=inflater.inflate(R.layout.pro_ma_deta_dialog,null);
                Cursor c=cursor;
                c.moveToPosition(position);
                pos = cursor.getLong(cursor.getColumnIndex(Contracts.Purch_Detail_Table._ID));
                String s1=null,s2=null,s3=null,s4="null",s5="null";
                db_helper = new DbHelper(context);
                SQLiteDatabase mDb1 = db_helper.getReadableDatabase();
                String s = "SELECT * from " + Contracts.Production_Meterials.TABLE_NAME + " WHERE " + Contracts.Production_Meterials._ID + " = " +pos;
                final Cursor cur = mDb1.rawQuery(s, new String[]{});
                try {
                    while (cur.moveToNext()) {
                        s1 = cur.getString(cur.getColumnIndex(Contracts.Production_Meterials.COLUMN_1));
                        s2 = cur.getString(cur.getColumnIndex(Contracts.Production_Meterials.COLUMN_2));
                        s3 = cur.getString(cur.getColumnIndex(Contracts.Production_Meterials.COLUMN_3));
                        s4 = cur.getString(cur.getColumnIndex(Contracts.Production_Meterials.COLUMN_4));
                        s5 = cur.getString(cur.getColumnIndex(Contracts.Production_Meterials.COLUMN_5));
                    }

                } catch (Exception e) {
                }
                TextView textView1=mview.findViewById(R.id.name);
                TextView textView2=mview.findViewById(R.id.used);
                TextView textView3=mview.findViewById(R.id.Scale);
                TextView textView4=mview.findViewById(R.id.totalcost);
                TextView textView5=mview.findViewById(R.id.labourwork);
                Button button1=mview.findViewById(R.id.check_ok);
                textView1.setText(s1);
                textView2.setText(s2);
                textView3.setText(s3);
                textView4.setText(s4);
                textView5.setText(s5);
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                mbuilder.setView(mview);
                alertDialog=mbuilder.create();
                alertDialog.show();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
        TextView textView1,textView2,textView3;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.check1);
            textView2 = (TextView) itemView.findViewById(R.id.check2);
            textView3 = (TextView) itemView.findViewById(R.id.Production_Material_N);
        }
    }

}