package com.kk.kamranqadeer.salemanager.AllAdapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;
import com.kk.kamranqadeer.salemanager.Production_Material;
import com.kk.kamranqadeer.salemanager.Production_Today;
import com.kk.kamranqadeer.salemanager.R;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by kamran qadeer on 8/9/2018.
 */

public class ProductionAdapter extends RecyclerView.Adapter<ProductionAdapter.MyViewHolder> {
    Context context;
    Cursor cursor;
    DbHelper db_helper;
    long id;
    int check;
    AlertDialog alertDialog;
    View mview;
    ArrayList<Integer> array1= new ArrayList<Integer>();
    public ProductionAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public ProductionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.productioncard, parent, false);
        return new ProductionAdapter.MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(ProductionAdapter.MyViewHolder holder, final int position) {
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
        array1.add(i);
        final String ss = i + "";
        holder.textView1.setText(Production_Name+" "+Production_Scale);
        holder.textView3.setText(SetDateString(Production_date_time));
        holder.textView4.setText(Production_Grade+" code_"+Production_code);
        // COMPLETED (7) Set the tag of the itemview in the holder to the id
        holder.itemView.setTag(id);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //alert dialog using bulder
                final AlertDialog.Builder mbuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
                mview=inflater.inflate(R.layout.production_dialog_select,null);
                   Cursor c=cursor;
                   c.moveToPosition(position);
                   Long i = cursor.getLong(cursor.getColumnIndex(Contracts.Production._ID));
                   db_helper = new DbHelper(context);
                   check(i);
                mbuilder.setView(mview);
                alertDialog=mbuilder.create();
                   if(check==0) {
                       Button button1 = mview.findViewById(R.id.Yes);
                       Button button2 = mview.findViewById(R.id.Late);
                       button1.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               Intent intent = new Intent(context, Production_Material.class);
                               intent.putExtra("NAME", Production_Name+"  "+Production_Scale);
                               intent.putExtra("_ID", ss);
                               context.startActivity(intent);
                               alertDialog.dismiss();
                           }
                       });
                       button2.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               Intent intent = new Intent(context, Production_Today.class);
                               intent.putExtra("NAME", Production_Name+"  "+Production_Scale);
                               intent.putExtra("_ID", ss);
                               context.startActivity(intent);
                               alertDialog.dismiss();
                           }
                       });
                       alertDialog.show();
                       alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                   }
                   else{
                       Intent intent = new Intent(context, Production_Today.class);
                       intent.putExtra("NAME", Production_Name+"  "+Production_Scale);
                       intent.putExtra("_ID", ss);
                       context.startActivity(intent);
                       alertDialog.dismiss();
                   }



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
    public void check(Long position) {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        ArrayList<String> arrayList=new ArrayList<String>();
        String s = "SELECT "+Contracts.Production_Meterials.COLUMN_1+" from " + Contracts.Production_Meterials.TABLE_NAME + " WHERE " + Contracts.Production_Meterials.COLUMN_6 + " = " + position;
        Cursor cursor = mDb.rawQuery(s, null);
        int count = cursor.getCount();
        check=count;
        cursor.close();
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



