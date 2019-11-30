package com.kk.kamranqadeer.salemanager.AllAdapters;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;
import com.kk.kamranqadeer.salemanager.R;
import com.kk.kamranqadeer.salemanager.SaleList;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by kamran qadeer on 8/23/2018.
 */

public class SaleAdapter_2  extends RecyclerView.Adapter<SaleAdapter_2.MyViewHolder> {
    Context context;
    Cursor cursor;
    long id,IDFK,pos;
    int POSITION;
    DbHelper db;
    AlertDialog alertDialog;
    View mview;
    ArrayList<String> PicList=new ArrayList<String>();
    public SaleAdapter_2(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        db= new DbHelper(context);
    }

    @Override
    public SaleAdapter_2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.sale_list_card, parent, false);
        return new SaleAdapter_2.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SaleAdapter_2.MyViewHolder holder, final int position) {
        // Move the mCursor to the position of the item to be displayed
        this.POSITION=position;
        if (!cursor.moveToPosition(position)) {
            return; // bail if returned null
        }
        final String Sale = cursor.getString(cursor.getColumnIndex(Contracts.Sale_ProduDeteil_Table.COLUMN_1));
        final String One_Daoz_Cost = cursor.getString(cursor.getColumnIndex(Contracts.Sale_ProduDeteil_Table.COLUMN_2));
        final String Date = cursor.getString(cursor.getColumnIndex(Contracts.Sale_ProduDeteil_Table.COLUMN_4));
        final String Shope_Name = cursor.getString(cursor.getColumnIndex(Contracts.Sale_ProduDeteil_Table.COLUMN_5));
        IDFK = cursor.getInt(cursor.getColumnIndex(Contracts.Sale_ProduDeteil_Table.COLUMN_6));
        id = cursor.getLong(cursor.getColumnIndex(Contracts.Sale_ProduDeteil_Table._ID));
        int i = (int) id;
        final String ss = i + "";
        DecimalFormat dec = new DecimalFormat("#0.00");
        holder.textView1.setText(Sale + " DOZEN SALE");
        holder.textView2.setText(One_Daoz_Cost + " RS");
        holder.textView3.setText(SetDateString(Date));
        holder.textView5.setText(dec.format(Set_One_doze() * Double.parseDouble(Sale))+" RS");
        holder.textView6.setText(Shope_Name);
        // COMPLETED (7) Set the tag of the itemview in the holder to the id
        holder.itemView.setTag(id);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c = cursor;
                String picname="0";
                c.moveToPosition(position);
                pos = cursor.getLong(cursor.getColumnIndex(Contracts.Purch_Detail_Table._ID));
                showImageList();
                //get pic name base on id
                for (int i=0;i<PicList.size();i++){
                    picname=PicList.get(i);
                    String split[]=picname.split("-");
                    if(split[1].equals(Long.toString(pos)+".jpg")){
                        picname=PicList.get(i);
                        break;
                    }
                }
                //open pic
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + "/sdcard/SaleManager/Pictures/"+picname.trim().toString()), "image/*");
                context.startActivity(intent);

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //THIS IS UPDATING DIALOG
                //alert dialog using bulder
                final AlertDialog.Builder mbuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
                mview=inflater.inflate(R.layout.sale_list_dialog,null);
                Cursor c = cursor;
                c.moveToPosition(position);
                pos = cursor.getLong(cursor.getColumnIndex(Contracts.Purch_Detail_Table._ID));
                String s1 = "", s2 = "", s3 = "";
                db = new DbHelper(context);
                SQLiteDatabase mDb1 = db.getReadableDatabase();
                final String s = "SELECT * from " + Contracts.Sale_ProduDeteil_Table.TABLE_NAME + " WHERE " + Contracts.Sale_ProduDeteil_Table._ID + " = " + pos;
                final Cursor cur = mDb1.rawQuery(s, new String[]{});
                try {
                    while (cur.moveToNext()) {
                        s1 = cur.getString(cur.getColumnIndex(Contracts.Sale_ProduDeteil_Table.COLUMN_2));
                        s2 = cur.getString(cur.getColumnIndex(Contracts.Sale_ProduDeteil_Table.COLUMN_4));
                        s3 = cur.getString(cur.getColumnIndex(Contracts.Sale_ProduDeteil_Table.COLUMN_5));
                    }

                } catch (Exception e) {
                }

                EditText editText1 = mview.findViewById(R.id.Dozen_Amount);
                final EditText editText2 = mview.findViewById(R.id.Production_Today_Date);
                final EditText editText3 = mview.findViewById(R.id.Sope_Name);
                Spinner spinner1=mview.findViewById(R.id.Sale);
                SelectDoz(spinner1);
                Button button1 = mview.findViewById(R.id.save_sale);
                Button button2 = mview.findViewById(R.id.exit_sale);
                final TextView textView1=mview.findViewById(R.id.tv_1);
                textView1.setText("UPDATE");
                button1.setText("UPDATE");
                button2.setText("CANCLE");
                editText1.setText(s1);
                editText2.setText(s2);
                editText3.setText(s3);
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText editText1 = mview.findViewById(R.id.Dozen_Amount);
                        EditText editText2 = mview.findViewById(R.id.Production_Today_Date);
                        EditText editText3 = mview.findViewById(R.id.Sope_Name);
                        Spinner spinner1=mview.findViewById(R.id.Sale);
                        String s1 = spinner1.getSelectedItem().toString();
                        String s2 = editText1.getText().toString();
                        String s4=editText2.getText().toString();
                        String s5=editText3.getText().toString();
                        if (s1.equals("0") || s2.equals("")||s4.equals("")||s5.equals("")) {
                            Toast.makeText(context, "ENTER ALL DETAIL", Toast.LENGTH_SHORT).show();
                        } else {
                            SQLiteDatabase mDb = db.getWritableDatabase();
                            ContentValues args = new ContentValues();
                            args.put(Contracts.Sale_ProduDeteil_Table.COLUMN_1, s1);
                            args.put(Contracts.Sale_ProduDeteil_Table.COLUMN_2, s2);
                            args.put(Contracts.Sale_ProduDeteil_Table.COLUMN_4, s4);
                            args.put(Contracts.Sale_ProduDeteil_Table.COLUMN_5, s5);
                            int result = mDb.update(Contracts.Sale_ProduDeteil_Table.TABLE_NAME, args, Contracts.Sale_ProduDeteil_Table._ID + " = " + pos, null);
                            if (result == -1) {
                            } else {
                                swapCursor(getAllSale());
                                Toast.makeText(context,"DETAIL IS UPDATE",Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        }
                    }

                });
                button2.setOnClickListener(new View.OnClickListener() {
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3, textView4, textView5, textView6;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.text_1);
            textView2 = (TextView) itemView.findViewById(R.id.text_1_1);
            textView3 = (TextView) itemView.findViewById(R.id.Date);
            textView4 = (TextView) itemView.findViewById(R.id.Day);
            textView5 = (TextView) itemView.findViewById(R.id.text_2);
            textView6 = (TextView) itemView.findViewById(R.id.text_3);
            imageView= (ImageView) itemView.findViewById(R.id.takepic);
        }
    }

    public Double Set_One_doze() {
        String ONE_DOZEN="0.0";
        SQLiteDatabase mDb = db.getReadableDatabase();
        String s = "SELECT * from " + Contracts.Production_Meterials.TABLE_NAME +" WHERE "+Contracts.Production_Meterials.COLUMN_6+" = "+IDFK;
        Cursor cr = mDb.rawQuery(s, new String[]{});
        while (cr.moveToNext()) {
            ONE_DOZEN = cr.getString(cr.getColumnIndex(Contracts.Production_Meterials.COLUMN_8));
        }
        ONE_DOZEN=Double.toString(Double.parseDouble(ONE_DOZEN)*12);
        return Double.parseDouble(ONE_DOZEN);
    }
    public void SelectDoz(Spinner spinner) {
        ArrayAdapter<String> adapter;
        List<String> list;
        String string = null;
        list = new ArrayList<String>();
        for (int i = 0; i <= 200; i++) {
            string = Integer.toString(i);
            list.add(string);
        }
        adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    private Cursor getAllSale() {
        SQLiteDatabase mDb=db.getReadableDatabase();
        String[] columns=new String[]{Contracts.Sale_ProduDeteil_Table._ID,Contracts.Sale_ProduDeteil_Table.COLUMN_1,Contracts.Sale_ProduDeteil_Table.COLUMN_2,Contracts.Sale_ProduDeteil_Table.COLUMN_3,Contracts.Sale_ProduDeteil_Table.COLUMN_4,Contracts.Sale_ProduDeteil_Table.COLUMN_5,Contracts.Sale_ProduDeteil_Table.COLUMN_6};
        Cursor cr=mDb.query(Contracts.Sale_ProduDeteil_Table.TABLE_NAME,columns,Contracts.Sale_ProduDeteil_Table.COLUMN_6+" = '"+IDFK+"'",null,null,null,null);
        return cr;
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
    private void showImageList() {
        File file = new File(android.os.Environment.getExternalStorageDirectory() + "/SaleManager/Pictures/");
        if (file.exists()) {
            String[] fileListVideo = file.list();
            Collections.addAll(PicList, fileListVideo);
        }
    }
}
