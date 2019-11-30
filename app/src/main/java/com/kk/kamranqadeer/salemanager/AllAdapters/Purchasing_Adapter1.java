package com.kk.kamranqadeer.salemanager.AllAdapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.params.BlackLevelPattern;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;
import com.kk.kamranqadeer.salemanager.Purchasing;
import com.kk.kamranqadeer.salemanager.PurchasingDetail;
import com.kk.kamranqadeer.salemanager.R;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by kamran qadeer on 8/2/2018.
 */

public class Purchasing_Adapter1  extends RecyclerView.Adapter <Purchasing_Adapter1.MyViewHolder> {
    Context context;
    Cursor cursor;
    DbHelper db_helper;
    String total_amount;
    long id;Long pos;
    String s4=null;
    AlertDialog alertDialog;
    View mview;
    String Quntity="0";
    public Purchasing_Adapter1(Context context, Cursor cursor) {
        this.context = context;
        this.cursor=cursor;
        db_helper = new DbHelper(context);
    }
    @Override
    public Purchasing_Adapter1.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.purch_detail_card, parent, false);
        return new Purchasing_Adapter1.MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final Purchasing_Adapter1.MyViewHolder holder, final int position) {
        // Move the mCursor to the position of the item to be displayed
        if (!cursor.moveToPosition(position)) {
            return; // bail if returned null
        }
         String Name = cursor.getString(cursor.getColumnIndex(Contracts.Purch_Detail_Table.COLUMN_1));
         String on_pice_Amount = cursor.getString(cursor.getColumnIndex(Contracts.Purch_Detail_Table.COLUMN_2));
         final String Total_quntity = cursor.getString(cursor.getColumnIndex(Contracts.Purch_Detail_Table.COLUMN_3));
         total_amount = cursor.getString(cursor.getColumnIndex(Contracts.Purch_Detail_Table.COLUMN_4));
         String quntity = cursor.getString(cursor.getColumnIndex(Contracts.Purch_Detail_Table.COLUMN_6));
         String scale = cursor.getString(cursor.getColumnIndex(Contracts.Purch_Detail_Table.COLUMN_7));
         id = cursor.getLong(cursor.getColumnIndex(Contracts.Purch_Detail_Table._ID));
        holder.textView1.setText(Name);
        holder.textView3.setText(total_amount+" RS");
        holder.textView5.setText(quntity+" "+scale);
        holder.textView7.setText("ONE "+scale+" "+on_pice_Amount+" RS");
        holder.textView8.setText("QUN#"+Total_quntity);
        holder.itemView.setTag(id);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //THIS IS UPDATING DIALOG
                //alert dialog using bulder
                final AlertDialog.Builder mbuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
                mview=inflater.inflate(R.layout.purch_dialog,null);                Cursor c=cursor;
                c.moveToPosition(position);
                pos = cursor.getLong(cursor.getColumnIndex(Contracts.Purch_Detail_Table._ID));
                String s1="",s2="",s3="",s5="";

                SQLiteDatabase mDb1 = db_helper.getReadableDatabase();
                String s = "SELECT * from " + Contracts.Purch_Detail_Table.TABLE_NAME + " WHERE " + Contracts.Purch_Detail_Table._ID + " = " +pos;
                final Cursor cur = mDb1.rawQuery(s, new String[]{});
                try {
                    while (cur.moveToNext()) {
                        s1 = cur.getString(cur.getColumnIndex(Contracts.Purch_Detail_Table.COLUMN_1));
                        s2 = cur.getString(cur.getColumnIndex(Contracts.Purch_Detail_Table.COLUMN_2));
                        s3 = cur.getString(cur.getColumnIndex(Contracts.Purch_Detail_Table.COLUMN_3));
                        s5 = cur.getString(cur.getColumnIndex(Contracts.Purch_Detail_Table.COLUMN_7));
                    }

                } catch (Exception e) {
                }
                final Spinner spinner=mview.findViewById(R.id.spinner);
                final Spinner spinner1=mview.findViewById(R.id.spinner1);
                final EditText editText1 = mview.findViewById(R.id.m_quntity);
                final EditText editText2 = mview.findViewById(R.id.amount);
                final TextView textView1=mview.findViewById(R.id.SCALE_1);
                final TextView textView2=mview.findViewById(R.id.SCALE_2);
                final TextView textView3=mview.findViewById(R.id.SCALE_3);
                Button button1=mview.findViewById(R.id.save);
                Button button2=mview.findViewById(R.id.exit);
                final TextView textView=mview.findViewById(R.id.tv_1);
                textView.setText("UPDATE");
                button1.setText("UPDATE");
                button2.setText("CANCLE");
                //set spinner value
                ArrayList<String> list;
                list = new ArrayList<String>();
                ArrayAdapter<String> adapt;
                list.add(s1);
                adapt = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item, list);
                adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapt);
                //spinner value is selected
                editText1.setText(s3);
                editText2.setEnabled(false);
                editText2.setText(s2);
                textView1.setText(s5);
                textView2.setText("ONE "+s5);
                Double i = Double.parseDouble(s2), j = Double.parseDouble(s3), k = i * j;
                s4 = Double.toString(k);
                textView3.setText(s4);
                ArrayList<String> spin=new ArrayList<String>();
                if(s5.equals("kg")){
                    spin.add("kg");
                    spin.add("gm");
                }
                else if(s5.equals("Liter")){
                    spin.add("Liter");
                    spin.add("ml");
                }
                else{
                    spin.add(s5);
                }

                ArrayAdapter<String> spinner_adapter;
                spinner_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, spin);
                spinner1.setAdapter(spinner_adapter);
                //edittext1 action listtner to set value
                editText1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String no=s.toString();
                            if(no.equals("")){
                                textView3.setText(editText2.getText().toString());
                            }
                            else {
                                if (spinner1.getSelectedItem().toString().equals("gm") || spinner1.getSelectedItem().toString().equals("ml")) {
                                    Double i = Double.parseDouble(s.toString()), j = Double.parseDouble(editText2.getText().toString()), k = (i / 1000) * j;
                                    s4 = Double.toString(k);
                                    Quntity = Double.toString(Double.parseDouble(editText1.getText().toString()) / 1000);
                                    textView3.setText(s4);
                                } else {
                                    Double i = Double.parseDouble(s.toString()), j = Double.parseDouble(editText2.getText().toString()), k = i * j;
                                    s4 = Double.toString(k);
                                    Quntity = editText1.getText().toString();
                                    textView3.setText(s4);
                                }
                            }
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                //spinner 1 action listner
                spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(editText1.getText().toString().equals("")){}
                        else {
                            if (spinner1.getSelectedItem().toString().equals("gm") || spinner1.getSelectedItem().toString().equals("ml")) {
                                Double i = Double.parseDouble(editText1.getText().toString()), j = Double.parseDouble(editText2.getText().toString()), k = (i / 1000) * j;
                                s4 = Double.toString(k);
                                Quntity = Double.toString(Double.parseDouble(editText1.getText().toString()) / 1000);
                                textView3.setText(s4);
                            } else {
                                Double i = Double.parseDouble(editText1.getText().toString()), j = Double.parseDouble(editText2.getText().toString()), k = i * j;
                                s4 = Double.toString(k);
                                Quntity = editText1.getText().toString();
                                textView3.setText(s4);
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Spinner spinner=mview.findViewById(R.id.spinner);
                        String s1=spinner.getSelectedItem().toString();
                        String s2=editText2.getText().toString();
                        String s3=Quntity;
                        String s5=Quntity;
                        String s6=editText1.getText().toString();
                        SQLiteDatabase mDb = db_helper.getWritableDatabase();
                        ContentValues args = new ContentValues();
                        Double i = Double.parseDouble(s2), j = Double.parseDouble(s3), k = i * j;
                        s4 = Double.toString(k);
                        args.put(Contracts.Purch_Detail_Table.COLUMN_1, s1);
                        args.put(Contracts.Purch_Detail_Table.COLUMN_2, s2);
                        args.put(Contracts.Purch_Detail_Table.COLUMN_3, s3);
                        args.put(Contracts.Purch_Detail_Table.COLUMN_4, s4);
                        args.put(Contracts.Purch_Detail_Table.COLUMN_5, s5);
                        args.put(Contracts.Purch_Detail_Table.COLUMN_6, s6);
                        int result=mDb.update(Contracts.Purch_Detail_Table.TABLE_NAME, args, Contracts.Purch_Detail_Table._ID+" = "+pos, null);
                        if (result==-1){
                            Toast.makeText(context,"UDATE IS COMPLETE",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            swapCursor(getAllpurch());
                            alertDialog.dismiss();
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
                notifyDataSetChanged();
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
        TextView textView1,textView2,textView3,textView4,textView5,textView7,textView8;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.name_1);
            textView2 = (TextView) itemView.findViewById(R.id.t_m_1);
            textView3 = (TextView) itemView.findViewById(R.id.t_1_1);
            textView4 = (TextView) itemView.findViewById(R.id.t_m_2);
            textView5 = (TextView) itemView.findViewById(R.id.t_m_2_2);
            textView7 = (TextView) itemView.findViewById(R.id.t_3_2);
            textView8 = (TextView) itemView.findViewById(R.id.t_3_3);
        }
    }
    public Cursor getAllpurch() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Purch_Detail_Table._ID, Contracts.Purch_Detail_Table.COLUMN_1, Contracts.Purch_Detail_Table.COLUMN_2, Contracts.Purch_Detail_Table.COLUMN_3,Contracts.Purch_Detail_Table.COLUMN_4,Contracts.Purch_Detail_Table.COLUMN_6,Contracts.Purch_Detail_Table.COLUMN_7};
        Cursor cr = mDb.query(Contracts.Purch_Detail_Table.TABLE_NAME, columns, Contracts.Purch_Detail_Table._ID+ " = '" + id + "'", null, null, null, null);
        return cr;
    }
}