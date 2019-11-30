package com.kk.kamranqadeer.salemanager.Tab;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;
import com.kk.kamranqadeer.salemanager.R;

import java.text.DecimalFormat;

/**
 * Created by kamran qadeer on 8/30/2018.
 */

@SuppressLint("ValidFragment")
public class Tab4 extends Fragment {
    long id;
    String Date;
    DbHelper db_helper;
    TextView textView1,textView2,textView3,textView4;
    @SuppressLint("ValidFragment")
    public Tab4(long id,String Date) {
        this.id=id;
        this.Date=Date;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        db_helper = new DbHelper(getContext());
        view=inflater.inflate(R.layout.record_account,container,false);
        UpdateAccount();
        textView1=view.findViewById(R.id.sale_amount);
        textView2=view.findViewById(R.id.production_cost);
        textView3=view.findViewById(R.id.profit);
        textView4=view.findViewById(R.id.loss);
        setTextView();

       return view;
    }
    public void UpdateAccount(){
        Cursor c1=getAllSale(),c2=getAllProduction_Amount();
        String Sale_amount="0",Production_Amount="0",Profit="0";
        //get all sale amoount
        while (c1.moveToNext()){
            String D=c1.getString(c1.getColumnIndex(Contracts.Sale_ProduDeteil_Table.COLUMN_4));
            String spilt1[]=D.split("-");
            String M=spilt1[0],Y=spilt1[2],d=M+"-"+Y;
            if(d.equals(Date)){
                String sale=c1.getString(c1.getColumnIndex(Contracts.Sale_ProduDeteil_Table.COLUMN_2));
                Sale_amount=Double.toString(Double.parseDouble(sale)+Double.parseDouble(Sale_amount));
            }
        }
        //get all production amount
        while (c2.moveToNext()){
            String D=c2.getString(c2.getColumnIndex(Contracts.Production_Today.COLUMN_5));
            String spilt1[]=D.split("-");
            String M=spilt1[0],Y=spilt1[2],d=M+"-"+Y;
            if(d.equals(Date)){
                String sale=c2.getString(c2.getColumnIndex(Contracts.Production_Today.COLUMN_4));
                Production_Amount=Double.toString(Double.parseDouble(sale)+Double.parseDouble(Production_Amount));
            }
        }
        DecimalFormat dec=new DecimalFormat("#0.00");
        Profit=dec.format(Double.parseDouble(Sale_amount)-Double.parseDouble(Production_Amount));
        SQLiteDatabase mDb = db_helper.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(Contracts.Account_Record.COLUMN_2, Sale_amount);
        args.put(Contracts.Account_Record.COLUMN_3, Production_Amount);
        if(Double.parseDouble(Profit)<0){
            args.put(Contracts.Account_Record.COLUMN_4, "0");
            args.put(Contracts.Account_Record.COLUMN_5, Profit);
        }
        else {
            args.put(Contracts.Account_Record.COLUMN_4, Profit);
            args.put(Contracts.Account_Record.COLUMN_5, "0");
        }
        int result=mDb.update(Contracts.Account_Record.TABLE_NAME, args, Contracts.Account_Record.COLUMN_6 + " = " + id, null);
       if(result==-1){
           System.out.println("ERROR");
       }
       else{
           System.out.println("Update");
       }
    }
    public void setTextView(){
        Cursor c=getAllAccount_Record();
        while (c.moveToNext()){
            String Sale_Amount=c.getString(c.getColumnIndex(Contracts.Account_Record.COLUMN_2));
            String Production_Amount=c.getString(c.getColumnIndex(Contracts.Account_Record.COLUMN_3));
            String Profit_Amount=c.getString(c.getColumnIndex(Contracts.Account_Record.COLUMN_4));
            String Loss_Amount=c.getString(c.getColumnIndex(Contracts.Account_Record.COLUMN_5));
            textView1.setText(Sale_Amount+" RS");
            textView2.setText(Production_Amount+" RS");
            textView3.setText(Profit_Amount+" RS");
            textView4.setText(Loss_Amount+" RS");
        }
        if(textView3.getText().toString().equals("0 RS")){
            textView4.setTextColor(Color.RED);
        }
        else {
            textView3.setTextColor(Color.GREEN);
        }
    }
    private Cursor getAllSale() {
        SQLiteDatabase mDb=db_helper.getReadableDatabase();
        String[] columns=new String[]{Contracts.Sale_ProduDeteil_Table._ID,Contracts.Sale_ProduDeteil_Table.COLUMN_1,Contracts.Sale_ProduDeteil_Table.COLUMN_2,Contracts.Sale_ProduDeteil_Table.COLUMN_3,Contracts.Sale_ProduDeteil_Table.COLUMN_4,Contracts.Sale_ProduDeteil_Table.COLUMN_5,Contracts.Sale_ProduDeteil_Table.COLUMN_6};
        Cursor cr=mDb.query(Contracts.Sale_ProduDeteil_Table.TABLE_NAME,columns,null,null,null,null,null);
        return cr;

    }
    public Cursor getAllAccount_Record() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Account_Record._ID, Contracts.Account_Record.COLUMN_1, Contracts.Account_Record.COLUMN_2,Contracts.Account_Record.COLUMN_3,Contracts.Account_Record.COLUMN_4,Contracts.Account_Record.COLUMN_5,Contracts.Account_Record.COLUMN_6};
        Cursor cr = mDb.query(Contracts.Account_Record.TABLE_NAME, columns, Contracts.Account_Record.COLUMN_6+" = "+id, null, null, null, null);
        return cr;
    }
    public Cursor getAllProduction_Amount() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Production_Today._ID, Contracts.Production_Today.COLUMN_1, Contracts.Production_Today.COLUMN_2, Contracts.Production_Today.COLUMN_3, Contracts.Production_Today.COLUMN_4, Contracts.Production_Today.COLUMN_5, Contracts.Production_Today.COLUMN_6, Contracts.Production_Today.COLUMN_7, Contracts.Production_Today.COLUMN_8};
        Cursor cr = mDb.query(Contracts.Production_Today.TABLE_NAME, columns, null, null, null, null, null);
        return cr;
    }
}
