package com.kk.kamranqadeer.salemanager.AllAdapters;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;
import com.kk.kamranqadeer.salemanager.Production_Today;
import com.kk.kamranqadeer.salemanager.R;
import com.kk.kamranqadeer.salemanager.Tab.Tab1;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by kamran qadeer on 8/12/2018.
 */

public class Production_Today_Adapter2 extends RecyclerView.Adapter<Production_Today_Adapter2.MyViewHolder> {
    Context context;
    Cursor cursor;
    DbHelper db_helper;
    Long pos;
    int IDFK;
    String LabourWork="",One_Doz="";
    String ProductName="";
    AlertDialog alertDialog;
    View mview;
    public Production_Today_Adapter2(Context context, Cursor cursor,String Name) {
        this.context = context;
        this.cursor = cursor;
        this.ProductName=Name;
        db_helper = new DbHelper(context);
    }

    @Override
    public Production_Today_Adapter2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.production_today_card2, parent, false);
        return new Production_Today_Adapter2.MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(Production_Today_Adapter2.MyViewHolder holder, final int position) {
        // Move the mCursor to the position of the item to be displayed
        if (!cursor.moveToPosition(position)) {
            return; // bail if returned null
        }
        final String PRODUCTION = cursor.getString(cursor.getColumnIndex(Contracts.Production_Today.COLUMN_1));
        final String TODAY_COST = cursor.getString(cursor.getColumnIndex(Contracts.Production_Today.COLUMN_2));
        final String TODAY_LABOR_WORK = cursor.getString(cursor.getColumnIndex(Contracts.Production_Today.COLUMN_3));
        final String TOTAL_AMOUNT = cursor.getString(cursor.getColumnIndex(Contracts.Production_Today.COLUMN_4));
        final String DATE = cursor.getString(cursor.getColumnIndex(Contracts.Production_Today.COLUMN_5));
        final String DAY = cursor.getString(cursor.getColumnIndex(Contracts.Production_Today.COLUMN_6));
        IDFK = cursor.getInt(cursor.getColumnIndex(Contracts.Production_Today.COLUMN_7));
        final String LABOUR_NAME = cursor.getString(cursor.getColumnIndex(Contracts.Production_Today.COLUMN_8));
        long id = cursor.getLong(cursor.getColumnIndex(Contracts.Production_Today._ID));
        int i = (int) id;
        final String ss = i + "";
        holder.textView1.setText(SetDateString(DATE));
        holder.textView3.setText(PRODUCTION+"-DOZEN");
        holder.textView5.setText(TOTAL_AMOUNT+" RS");
        holder.textView6.setText(DAY);
        holder.textView8.setText(TODAY_COST+" RS");
        holder.textView10.setText(TODAY_LABOR_WORK+" RS");
        holder.textView12.setText(LABOUR_NAME);
        // COMPLETED (7) Set the tag of the itemview in the holder to the id
        holder.itemView.setTag(id);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //THIS IS UPDATING DIALOG
                //alert dialog using bulder
                final AlertDialog.Builder mbuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
                mview=inflater.inflate(R.layout.production_today_dialog,null);
                Cursor c=cursor;
                c.moveToPosition(position);
                pos = cursor.getLong(cursor.getColumnIndex(Contracts.Purch_Detail_Table._ID));
                db_helper = new DbHelper(context);
                String s1=null,s2=null,s3=null;
                SQLiteDatabase mDb1 = db_helper.getReadableDatabase();
                final String s = "SELECT * from " + Contracts.Production_Today.TABLE_NAME + " WHERE " + Contracts.Production_Today._ID + " = " +pos;
                final Cursor cur = mDb1.rawQuery(s, new String[]{});
                ArrayList<String> Sugession;
                Sugession=new ArrayList<String>();
                try {
                    while (cur.moveToNext()) {
                        s1 = cur.getString(cur.getColumnIndex(Contracts.Production_Today.COLUMN_1));
                        s2 = cur.getString(cur.getColumnIndex(Contracts.Production_Today.COLUMN_5));
                        s3 = cur.getString(cur.getColumnIndex(Contracts.Production_Today.COLUMN_8));
                        Sugession.add(s3);
                    }

                } catch (Exception e) {
                }

                EditText editText1=mview.findViewById(R.id.Production_Today_Dozen);
                EditText editText2_year = mview.findViewById(R.id.Year);
                EditText editText2_month = mview.findViewById(R.id.Month);
                EditText editText2_date = mview.findViewById(R.id.Date);
                EditText editText3=mview.findViewById(R.id.Labour_Name);
                Button button1=mview.findViewById(R.id.Production_Today_SAVE);
                Button button2=mview.findViewById(R.id.Production_Today_EXIT);
                final TextView textView1=mview.findViewById(R.id.tv_1);
                textView1.setText("UPDATE");
                button1.setText("UPDATE");
                button2.setText("CANCLE");
                editText1.setText(s1);
                String[] items1 = s2.split("-");
                String month=items1[0];
                String date1=items1[1];
                String year=items1[2];
                editText2_year.setText(year);
                editText2_year.setEnabled(false);
                editText2_month.setText(month);
                editText2_month.setEnabled(false);
                editText2_date.setText(date1);
                editText3.setText(s3);
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Set_All_Detail();
                        EditText editText1=mview.findViewById(R.id.Production_Today_Dozen);
                        EditText editText2_year = mview.findViewById(R.id.Year);
                        EditText editText2_month = mview.findViewById(R.id.Month);
                        EditText editText2_date = mview.findViewById(R.id.Date);
                        EditText editText3=mview.findViewById(R.id.Labour_Name);
                        Spinner spinner=mview.findViewById(R.id.Production_Today_spiner);
                        String s1=editText1.getText().toString();
                        String s2=editText2_month.getText().toString()+"-"+editText2_date.getText().toString()+"-"+editText2_year.getText().toString();
                        String s3=spinner.getSelectedItem().toString();
                        String s4=editText3.getText().toString();
                        String TotalProductionCost=Float.toString(Float.parseFloat(s1)*Float.parseFloat(One_Doz)*12);
                        String La_Work=Float.toString(Float.parseFloat(s1)*Float.parseFloat(LabourWork));
                        if(editText1.equals("")||editText2_year.equals("")||editText2_month.equals("")||editText2_date.equals("")||editText3.equals("")||spinner.getSelectedItem().toString().equals("DAY")){
                            Toast.makeText(context,"ENTER ALL DETAIL",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            SQLiteDatabase mDb = db_helper.getWritableDatabase();
                            ContentValues args = new ContentValues();
                            args.put(Contracts.Production_Today.COLUMN_1, s1);
                            args.put(Contracts.Production_Today.COLUMN_2,TotalProductionCost );
                            args.put(Contracts.Production_Today.COLUMN_3, La_Work);
                            args.put(Contracts.Production_Today.COLUMN_4, Float.toString(Float.parseFloat(TotalProductionCost)+Float.parseFloat(La_Work)));
                            args.put(Contracts.Production_Today.COLUMN_5, s2);
                            args.put(Contracts.Production_Today.COLUMN_6, s3);
                            args.put(Contracts.Production_Today.COLUMN_8, s4);
                            int result = mDb.update(Contracts.Production_Today.TABLE_NAME, args, Contracts.Production_Today._ID + " = " + pos, null);
                            if (result == -1) {
                            } else {
                                Toast.makeText(context,"DETAIL IS UPDATE",Toast.LENGTH_SHORT).show();
                                swapCursor(getAllpurch2());
                                SetStock();
                                Today_Changing_Production(pos,PRODUCTION);
                                LabourRecordChang(PRODUCTION,LABOUR_NAME,DATE,s1,s4,s2);
                                ProductionUpdate(s2,PRODUCTION,s1);
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
        TextView textView1,textView2,textView3,textView4,textView5,textView6,textView7,textView8,textView9,textView10,textView11,textView12;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.Production_date);
            textView2 = (TextView) itemView.findViewById(R.id.Production_textm1);
            textView3 = (TextView) itemView.findViewById(R.id.Production_textm1_1);
            textView4 = (TextView) itemView.findViewById(R.id.Production_textm2);
            textView5 = (TextView) itemView.findViewById(R.id.Production_textm2_2);
            textView6 = (TextView) itemView.findViewById(R.id.Day);
            textView7 = (TextView) itemView.findViewById(R.id.Production_textm3);
            textView8 = (TextView) itemView.findViewById(R.id.Production_textm3_3);
            textView9 = (TextView) itemView.findViewById(R.id.Production_textm4);
            textView10 = (TextView) itemView.findViewById(R.id.Production_textm4_4);
            textView11 = (TextView) itemView.findViewById(R.id.Production_textm5);
            textView12 = (TextView) itemView.findViewById(R.id.Production_textm5_1);
        }
    }
    public void Today_Changing_Production(long id,String Previous_Production){
        Cursor c=getAllTodayProduction(id),c1=getAllpurch();
        String Production="0",Date="";
        while (c.moveToNext()){
            Production=c.getString(c.getColumnIndex(Contracts.Production_Today.COLUMN_1));
            Date=c.getString(c.getColumnIndex(Contracts.Production_Today.COLUMN_5));
        }
        while (c1.moveToNext()) {
            String Name = c1.getString(c1.getColumnIndex(Contracts.Production_Meterials.COLUMN_1));
                UpdateMaterials(Date,Previous_Production,Production,Name);
        }
        c.close();
    }
    public void UpdateMaterials(String Date,String Pre_Pro,String New_Pro,String M_Name) {
        long id = 0;
        int i=0;
        String Used="";
        if(Integer.parseInt(Pre_Pro)>Integer.parseInt(New_Pro)){
            int pro=Integer.parseInt(Pre_Pro)- Integer.parseInt(New_Pro);
            Used= MaterialUsed(M_Name,Integer.toString(pro));
            i=1;
        }
        else {
            int pro= Integer.parseInt(New_Pro)-Integer.parseInt(Pre_Pro);
            Used= MaterialUsed(M_Name,Integer.toString(pro));
        }
       // MaterialUsed(Name,Production)
        String split[] = Date.split("-");
        String m = split[0], y = split[2], date = m + "-" + y;
        String split_Scale[] = Used.split("-");
        String U = split_Scale[0], Scale = split_Scale[1];
        System.out.println(U+" Scale "+Scale );
        Cursor c1 = getAllMaterial_Record();
        String PreviousUsed="0";
        DecimalFormat dec = new DecimalFormat("#0.00");
        //get material_record id
        while (c1.moveToNext()) {
            String D = c1.getString(c1.getColumnIndex(Contracts.Material_record.COLUMN_1));
            String N = c1.getString(c1.getColumnIndex(Contracts.Material_record.COLUMN_2));
            if (D.equals(date) && N.equals(M_Name)) {
                id = c1.getInt(c1.getColumnIndex(Contracts.Material_record._ID));
                PreviousUsed = c1.getString(c1.getColumnIndex(Contracts.Material_record.COLUMN_4));
                if(PreviousUsed.equals("")){
                    PreviousUsed="0";
                }
            }
        }
        String Remove_Scale[] = PreviousUsed.split(" ");
        PreviousUsed = Remove_Scale[0];
        //now adding material
        if(i==1){
            Used = Double.toString( Double.parseDouble(PreviousUsed)-Double.parseDouble(U));
        }
        else{
            Used = Double.toString( Double.parseDouble(PreviousUsed)+Double.parseDouble(U));
        }

        SQLiteDatabase mDb = db_helper.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(Contracts.Material_record.COLUMN_4, Used+" "+Scale);
        mDb.update(Contracts.Material_record.TABLE_NAME, args, Contracts.Material_record._ID + " = " + id, null);
    }
    public String MaterialUsed(String M_Name,String Production){
        Cursor c=getAllpurch(),c2=getAllMaterial_Record();
        String Used="0",Scale="0",How_Many_Dozen="0",Material_Used="0";
        if(c2.getCount()!=0) {
            while (c.moveToNext()) {
                String Material_Name = c.getString(c.getColumnIndex(Contracts.Production_Meterials.COLUMN_1));
                if (Material_Name.equals(M_Name)) {
                    Used = c.getString(c.getColumnIndex(Contracts.Production_Meterials.COLUMN_2));
                    Scale = c.getString(c.getColumnIndex(Contracts.Production_Meterials.COLUMN_3));
                    How_Many_Dozen = c.getString(c.getColumnIndex(Contracts.Production_Meterials.COLUMN_7));
                }
            }
            c.close();
            if (Scale.equals("gm") || Scale.equals("ml")) {
                Used = Double.toString((Double.parseDouble(Used) / 1000) / Double.parseDouble(How_Many_Dozen));
            } else {
                Used = Double.toString(Double.parseDouble(Used) / Double.parseDouble(How_Many_Dozen));
            }
            DecimalFormat dec = new DecimalFormat("#0.00");
            Material_Used = dec.format(Double.parseDouble(Production) * Double.parseDouble(Used));
        }
        return Material_Used+"-"+CheckScale(Scale);
    }
    public String CheckScale(String scale){
        switch (scale){
            case "gm":
                return "kg";
            case "kg":
                return "kg";
            case "ml":
                return "kg";
            case "pice":
                return "pice";
            case "can":
                return "can";
            default:
                return "SCALE";
        }
    }
    public Cursor getAllMaterial_Record() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Material_record._ID, Contracts.Material_record.COLUMN_1,Contracts.Material_record.COLUMN_2,Contracts.Material_record.COLUMN_4};
        Cursor cr = mDb.query(Contracts.Material_record.TABLE_NAME, columns, null, null, null, null, null);
        return cr;

    }
    public Cursor getAllpurch2() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Production_Today._ID, Contracts.Production_Today.COLUMN_1, Contracts.Production_Today.COLUMN_2, Contracts.Production_Today.COLUMN_3, Contracts.Production_Today.COLUMN_4, Contracts.Production_Today.COLUMN_5, Contracts.Production_Today.COLUMN_6,Contracts.Production_Today.COLUMN_7,Contracts.Production_Today.COLUMN_8};
        Cursor cr = mDb.query(Contracts.Production_Today.TABLE_NAME, columns, Contracts.Production_Today.COLUMN_7 + " = '" + IDFK + "'", null, null, null, null);
        return cr;
    }
    public Cursor getAllTodayProduction(long id) {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Production_Today._ID, Contracts.Production_Today.COLUMN_1, Contracts.Production_Today.COLUMN_5};
        Cursor cr = mDb.query(Contracts.Production_Today.TABLE_NAME, columns, Contracts.Production_Today._ID + " = '" + id + "'", null, null, null, null);
        return cr;

    }
    public Cursor getAllpurch() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Production_Meterials._ID, Contracts.Production_Meterials.COLUMN_1, Contracts.Production_Meterials.COLUMN_2, Contracts.Production_Meterials.COLUMN_3, Contracts.Production_Meterials.COLUMN_7};
        Cursor cr = mDb.query(Contracts.Production_Meterials.TABLE_NAME, columns, Contracts.Production_Meterials.COLUMN_6 + " = '" + IDFK + "'", null, null, null, null);
        return cr;

    }

    public void Set_All_Detail() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String s = "SELECT * from " + Contracts.Production_Meterials.TABLE_NAME +" WHERE "+Contracts.Production_Meterials.COLUMN_6+" = "+IDFK;
        Cursor cr = mDb.rawQuery(s, new String[]{});
        while (cr.moveToNext()) {
            LabourWork = cr.getString(cr.getColumnIndex(Contracts.Production_Meterials.COLUMN_5));
            One_Doz = cr.getString(cr.getColumnIndex(Contracts.Production_Meterials.COLUMN_8));
        }
        cr.close();
    }
    public void SetStock(){
        long AddDoz=0;
        String dozen="";
        Cursor cr=getAllpurch2();
            while (cr.moveToNext()){
                dozen = cr.getString(cr.getColumnIndex(Contracts.Production_Today.COLUMN_1));
                AddDoz=AddDoz+Integer.parseInt(dozen);}
                dozen=Long.toString(AddDoz);
        SQLiteDatabase mDb = db_helper.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(Contracts.Stock.COLUMN_2, dozen);
        mDb.update(Contracts.Stock.TABLE_NAME, args, Contracts.Stock.COLUMN_6+ " = " +IDFK, null);
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
            string="July";
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
    public Cursor getAllLabour_Record() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Labour_Record._ID, Contracts.Labour_Record.COLUMN_1,Contracts.Labour_Record.COLUMN_2,Contracts.Labour_Record.COLUMN_3,Contracts.Labour_Record.COLUMN_4};
        Cursor cr = mDb.query(Contracts.Labour_Record.TABLE_NAME, columns, null, null, null, null, null);
        return cr;
    }
    public Cursor getAllLabour_Recycle() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Labour_Record_Recycle._ID, Contracts.Labour_Record_Recycle.COLUMN_1,Contracts.Labour_Record_Recycle.COLUMN_2,Contracts.Labour_Record_Recycle.COLUMN_3,Contracts.Labour_Record_Recycle.COLUMN_4,Contracts.Labour_Record_Recycle.COLUMN_5};
        Cursor cr = mDb.query(Contracts.Labour_Record_Recycle.TABLE_NAME, columns, null, null, null, null, null);
        return cr;

    }
    public void LabourRecordChang(String Work,String L_Name,String Date,String NewWork,String NewName,String NewDate){
        long ID=0,ID1=0;
        String split[]=Date.split("-");
        String m=split[0],y=split[2],date=m+"-"+y;
        Cursor c1=getAllLabour_Recycle(),c2=getAllLabour_Record();
        while (c2.moveToNext()) {
            String D = c2.getString(c2.getColumnIndex(Contracts.Labour_Record.COLUMN_1));
            String N = c2.getString(c2.getColumnIndex(Contracts.Labour_Record.COLUMN_2));
            if (D.equals(date) || N.equals(L_Name)) {
                ID1 = c2.getInt(c2.getColumnIndex(Contracts.Labour_Record._ID));
            }
        }
            SQLiteDatabase mDb1 = db_helper.getWritableDatabase();
            ContentValues args1 = new ContentValues();
            args1.put(Contracts.Labour_Record.COLUMN_1, NewDate);
            args1.put(Contracts.Labour_Record.COLUMN_2, NewName);
            int result1 = mDb1.update(Contracts.Labour_Record.TABLE_NAME, args1, Contracts.Labour_Record._ID + " = " + ID1, null);
            if (result1 == -1)

                System.out.println("Labour  Error");

            else
                System.out.println("Labour  update");
        while (c1.moveToNext()) {
            String D = c1.getString(c1.getColumnIndex(Contracts.Labour_Record_Recycle.COLUMN_1));
            String N = c1.getString(c1.getColumnIndex(Contracts.Labour_Record_Recycle.COLUMN_2));
            String PN = c1.getString(c1.getColumnIndex(Contracts.Labour_Record_Recycle.COLUMN_3));
            String W = c1.getString(c1.getColumnIndex(Contracts.Labour_Record_Recycle.COLUMN_4));
            if (D.equals(Date) || N.equals(L_Name) || Work.equals(W) || PN.equals(ProductName)) {
                ID = c1.getInt(c1.getColumnIndex(Contracts.Labour_Record_Recycle._ID));
            }
        }
            SQLiteDatabase mDb = db_helper.getWritableDatabase();
            ContentValues args = new ContentValues();
            args.put(Contracts.Labour_Record_Recycle.COLUMN_1, NewDate);
            args.put(Contracts.Labour_Record_Recycle.COLUMN_2, NewName);
            args.put(Contracts.Labour_Record_Recycle.COLUMN_4, NewWork);
            int result = mDb.update(Contracts.Labour_Record_Recycle.TABLE_NAME, args, Contracts.Labour_Record_Recycle._ID + " = " + ID, null);
            if (result == -1)

                System.out.println("Labour Recycle Error");

            else
                System.out.println("Labour Recycle update");

    }
    public void ProductionUpdate(String Date,String Production,String NewProduction ){
        String spilt[]=Date.split("-");
        String m=spilt[0],day=spilt[1],y=spilt[2],date=m+"-"+y,OldProduction="",NewProd="";
        int ID=check_Production_allredy(date,ProductName);
        Cursor c=getAllProduction_Record_For_Update(ID);
        while (c.moveToNext()){
            OldProduction=c.getString(c.getColumnIndex(Contracts.Production_Record.COLUMN_3));
        }
        if(Double.parseDouble(NewProduction)>Double.parseDouble(Production)){
            NewProd=Double.toString(Double.parseDouble(NewProduction)-Double.parseDouble(Production));
            NewProd=Double.toString(Double.parseDouble(NewProd)+Double.parseDouble(OldProduction));
        }
        else{
            NewProd=Double.toString(Double.parseDouble(Production)-Double.parseDouble(NewProduction));
            NewProd=Double.toString(Double.parseDouble(OldProduction)-Double.parseDouble(NewProd));
        }
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        ContentValues args = new ContentValues();
        args.put(Contracts.Production_Record.COLUMN_3, NewProd);
        mDb.update(Contracts.Production_Record.TABLE_NAME, args, Contracts.Production_Record._ID + "=" + ID, null);
    }
    public int check_Production_allredy (String D,String P_N){
        List<String> list,name;
        list = new ArrayList<String>();
        name = new ArrayList<String>();
        ArrayList<Integer> ID = new ArrayList<Integer>();
        int check=0;
        Cursor c=getAllProduction_Record();
        if(c.getCount()!=0) {
            while (c.moveToNext()) {
                String date = c.getString(c.getColumnIndex(Contracts.Production_Record.COLUMN_1));
                String Name = c.getString(c.getColumnIndex(Contracts.Production_Record.COLUMN_2));
                int id = c.getInt(c.getColumnIndex(Contracts.Production_Record._ID));
                list.add(date);
                name.add(Name);
                ID.add(id);
            }
            for (int i = 0; i < list.size(); i++) {
                if (D.equals(list.get(i))&&P_N.equals(name.get(i))) {
                    check = (int)(long)ID.get(i);
                    return check;
                }
            }
        }
        return check;
    }
    public Cursor getAllProduction_Record_For_Update(long id) {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Production_Record._ID,Contracts.Production_Record.COLUMN_3};
        Cursor cr = mDb.query(Contracts.Production_Record.TABLE_NAME, columns, Contracts.Production_Record._ID+" = "+id, null, null, null, null);
        return cr;

    }
    public Cursor getAllProduction_Record() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Production_Record._ID, Contracts.Production_Record.COLUMN_1,Contracts.Production_Record.COLUMN_2,Contracts.Production_Record.COLUMN_3,Contracts.Production_Record.COLUMN_4,Contracts.Production_Record.COLUMN_5};
        Cursor cr = mDb.query(Contracts.Production_Record.TABLE_NAME, columns, null, null, null, null, null);
        return cr;

    }
}
