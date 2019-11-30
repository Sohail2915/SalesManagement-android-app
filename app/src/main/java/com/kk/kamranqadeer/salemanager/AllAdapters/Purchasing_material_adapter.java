package com.kk.kamranqadeer.salemanager.AllAdapters;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.kamranqadeer.salemanager.All_Raw_Materials;
import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;
import com.kk.kamranqadeer.salemanager.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by kamran qadeer on 8/19/2018.
 */

public class Purchasing_material_adapter extends RecyclerView.Adapter<Purchasing_material_adapter.MyViewHolder> {
    Context context;
    Cursor cursor;
    DbHelper db_helper;
    AlertDialog alertDialog;
    Long pos;
    String s1="0";
    View mview;
    public Purchasing_material_adapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        db_helper = new DbHelper(context);
    }

    @Override
    public Purchasing_material_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.all_material_card, parent, false);
        return new Purchasing_material_adapter.MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(Purchasing_material_adapter.MyViewHolder holder, final int position) {
        // Move the mCursor to the position of the item to be displayed
        if (!cursor.moveToPosition(position)) {
            return; // bail if returned null
        }
        final String Purch_Name = cursor.getString(cursor.getColumnIndex(Contracts.Sale_Table.COLUMN_1));
        final String Purch_Amount = cursor.getString(cursor.getColumnIndex(Contracts.Sale_Table.COLUMN_2));
        final String Purch_Scale = cursor.getString(cursor.getColumnIndex(Contracts.Sale_Table.COLUMN_3));
        long id = cursor.getLong(cursor.getColumnIndex(Contracts.Sale_Table._ID));
        int i = (int) id;
        final String ss = i + "";
        holder.textView1.setText(Purch_Name);
        holder.textView2.setText(Purch_Amount+" RS PER "+Purch_Scale);
        // COMPLETED (7) Set the tag of the itemview in the holder to the id
        holder.itemView.setTag(id);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //alert dialog using bulder
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
                mview=inflater.inflate(R.layout.all_material_dialog,null);
                //THIS IS UPDATING DIALOG
                Cursor c=cursor;
                c.moveToPosition(position);
                pos = cursor.getLong(cursor.getColumnIndex(Contracts.Purch_Detail_Table._ID));
                db_helper = new DbHelper(context);
                String s2=null;
                SQLiteDatabase mDb1 = db_helper.getReadableDatabase();
                final String s = "SELECT * from " + Contracts.Sale_Table.TABLE_NAME + " WHERE " + Contracts.Sale_Table._ID + " = " +pos;
                final Cursor cur = mDb1.rawQuery(s, new String[]{});
                try {
                    while (cur.moveToNext()) {
                        s1 = cur.getString(cur.getColumnIndex(Contracts.Sale_Table.COLUMN_1));
                        s2 = cur.getString(cur.getColumnIndex(Contracts.Sale_Table.COLUMN_2));
                    }

                } catch (Exception e) {
                }
                EditText editText1=mview.findViewById(R.id.Materilas);
                EditText editText2=mview.findViewById(R.id.amount);
                Button button1=mview.findViewById(R.id.save);
                Button button2=mview.findViewById(R.id.exit);
                final TextView textView=mview.findViewById(R.id.scale);
                final TextView textView1=mview.findViewById(R.id.tv_1);
                textView1.setText("UPDATE");
                button1.setText("UPDATE");
                button2.setText("CANCLE");
                editText1.setText(s1);
                editText1.setEnabled(false);
                editText2.setText(s2);
                editText2.setOnTouchListener(new View.OnTouchListener() {
                    Spinner spinner=mview.findViewById(R.id.spinner);
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        textView.setText(spinner.getSelectedItem().toString());
                        return false;
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText editText2=mview.findViewById(R.id.amount);
                        Spinner spinner=mview.findViewById(R.id.spinner);
                        String s2=editText2.getText().toString();
                        String s3=spinner.getSelectedItem().toString();
                        if(s2.equals("")||s3.equals("Scale")){
                            Toast.makeText(context,"ENTER ALL DETAILS",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            SQLiteDatabase mDb = db_helper.getWritableDatabase();
                            ContentValues args = new ContentValues();
                            args.put(Contracts.Sale_Table.COLUMN_2, s2);
                            args.put(Contracts.Sale_Table.COLUMN_3, s3);
                            int result = mDb.update(Contracts.Sale_Table.TABLE_NAME, args, Contracts.Sale_Table._ID + " = " + pos, null);
                            if (result == -1) {
                            } else {
                                swapCursor(getAllPurch1());
                                UpdateAllMaterial(s1,s2);
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
                notifyDataSetChanged();
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
        TextView textView1,textView2;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.material);
            textView2 = (TextView) itemView.findViewById(R.id.check1);
        }
    }
    private Cursor getAllPurch1() {
        SQLiteDatabase mDb=db_helper.getReadableDatabase();
        Cursor cr=mDb.rawQuery("select * from "+ Contracts.Sale_Table.TABLE_NAME,null);
        //  cr = db.query(Contract.TableDetail.TABLE_NAME, null, null, null, null,null, null);
        return cr;
    }
    public Cursor getAllMaterial() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Production_Meterials._ID, Contracts.Production_Meterials.COLUMN_1, Contracts.Production_Meterials.COLUMN_2, Contracts.Production_Meterials.COLUMN_3, Contracts.Production_Meterials.COLUMN_4, Contracts.Production_Meterials.COLUMN_5,Contracts.Production_Meterials.COLUMN_6,Contracts.Production_Meterials.COLUMN_7,Contracts.Production_Meterials.COLUMN_8,Contracts.Production_Meterials.COLUMN_9};
        Cursor cr = mDb.query(Contracts.Production_Meterials.TABLE_NAME, columns, null, null, null, null, null);
        return cr;
    }
    public  void UpdateAllMaterial(String M_name,String Amount){
        Cursor c1=getAllMaterial();
        DecimalFormat format=new DecimalFormat("#0.0");
        String Material_Amount="0";
        while (c1.moveToNext()){
            String name=c1.getString(c1.getColumnIndex(Contracts.Production_Meterials.COLUMN_1));
            if(name.equals(M_name)){
                String Used=c1.getString(c1.getColumnIndex(Contracts.Production_Meterials.COLUMN_2));
                String Scale=c1.getString(c1.getColumnIndex(Contracts.Production_Meterials.COLUMN_3));
                long id=c1.getInt(c1.getColumnIndex(Contracts.Production_Meterials._ID));
                if(Scale.equals("gm")||Scale.equals("ml")){
                    Material_Amount=format.format((Double.parseDouble(Used)/1000)*Double.parseDouble(Amount));
                }
                else {
                    Material_Amount=format.format(Double.parseDouble(Used)*Double.parseDouble(Amount));
                }
                //now update material amount
                SQLiteDatabase mdb=db_helper.getWritableDatabase();
                ContentValues args=new ContentValues();
                args.put(Contracts.Production_Meterials.COLUMN_4, Material_Amount);
                mdb.update(Contracts.Production_Meterials.TABLE_NAME, args, Contracts.Production._ID + " = " + id, null);

            }
        }
    }
}
