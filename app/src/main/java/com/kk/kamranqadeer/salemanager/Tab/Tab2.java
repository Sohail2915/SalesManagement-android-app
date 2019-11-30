package com.kk.kamranqadeer.salemanager.Tab;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.kk.kamranqadeer.salemanager.AllAdapters.Recor_Adapter_2;
import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;
import com.kk.kamranqadeer.salemanager.R;

import java.util.ArrayList;

import static android.widget.LinearLayout.HORIZONTAL;

/**
 * Created by kamran qadeer on 8/30/2018.
 */

@SuppressLint("ValidFragment")
public class Tab2 extends Fragment {
    DbHelper db_helper;
    RecyclerView recyclerView;
    Recor_Adapter_2 adapter;
    Spinner spinner;
    long id;
    TextView textView1,textView2,textView3,textView4;
    EditText editText1;
    Button button1;
    int initial_Weight=1;
    int OldWeight;
    ArrayList<Long> ID=new ArrayList<Long>();
    @SuppressLint("ValidFragment")
    public Tab2(long id) {
        this.id=id;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        db_helper = new DbHelper(getContext());
        view=inflater.inflate(R.layout.record_labour,container,false);
        spinner=view.findViewById(R.id.spinner);
        textView1=view.findViewById(R.id.total_work);
        textView2=view.findViewById(R.id.total_amount);
        editText1=view.findViewById(R.id.pade_amount);
        textView3=view.findViewById(R.id.left_amount);
        textView4=view.findViewById(R.id.text_check);
        if(getAllpurch2().getCount()!=0) {
            OldWeight = (textView4).findViewById(R.id.text_check).getLayoutParams().width;
            button1 = view.findViewById(R.id.pay);
            initial_Weight = view.getLayoutParams().width;
            SetSpinner(spinner);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new Recor_Adapter_2(getContext(), getAllLabour_Recycle(ID.get(spinner.getSelectedItemPosition())));
            recyclerView.setAdapter(adapter);
            UpdateLabourRecor(ID.get(spinner.getSelectedItemPosition()));
            SpinnerAcction();
        }
        return view;
    }
    public Cursor getAllpurch2() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Production_Today._ID, Contracts.Production_Today.COLUMN_1, Contracts.Production_Today.COLUMN_2, Contracts.Production_Today.COLUMN_3, Contracts.Production_Today.COLUMN_4, Contracts.Production_Today.COLUMN_5, Contracts.Production_Today.COLUMN_6, Contracts.Production_Today.COLUMN_7, Contracts.Production_Today.COLUMN_8};
        Cursor cr = mDb.query(Contracts.Production_Today.TABLE_NAME, columns, null, null, null, null, null);
        return cr;

    }
    public Cursor getAllLabourName() {
        db_helper=new DbHelper(getContext());
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Labour_Record._ID, Contracts.Labour_Record.COLUMN_1, Contracts.Labour_Record.COLUMN_2,Contracts.Labour_Record.COLUMN_3,Contracts.Labour_Record.COLUMN_4,Contracts.Labour_Record.COLUMN_5,Contracts.Labour_Record.COLUMN_6};
        Cursor cr = mDb.query(Contracts.Labour_Record.TABLE_NAME, columns, Contracts.Labour_Record.COLUMN_6 + " = '" + id + "'", null, null, null, null);
        return cr;
    }
    public Cursor getAllLabour_Recycle(long id) {
        db_helper=new DbHelper(getContext());
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Labour_Record_Recycle._ID, Contracts.Labour_Record_Recycle.COLUMN_1, Contracts.Labour_Record_Recycle.COLUMN_2,Contracts.Labour_Record_Recycle.COLUMN_3,Contracts.Labour_Record_Recycle.COLUMN_4,Contracts.Labour_Record_Recycle.COLUMN_5,Contracts.Labour_Record_Recycle.COLUMN_6};
        Cursor cr = mDb.query(Contracts.Labour_Record_Recycle.TABLE_NAME, columns, Contracts.Labour_Record_Recycle.COLUMN_6 + " = '" + id + "'", null, null, null, null);
        return cr;
    }
    public void SetSpinner(Spinner spinner){
        ArrayAdapter<String> adapter;
        ArrayList<String> list=new ArrayList<String>();
        Cursor c=getAllLabourName();
        String Labour_Name;
        while (c.moveToNext()){
            long id=c.getInt(c.getColumnIndex(Contracts.Labour_Record._ID));
            Labour_Name=c.getString(c.getColumnIndex(Contracts.Labour_Record.COLUMN_2));
            list.add(Labour_Name);
            ID.add(id);
        }
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        for (int i=0;i<list.size();i++){
            UpdateLabourRecor(ID.get(i));
        }
    }
    public void SpinnerAcction(){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapter.swapCursor(getAllLabour_Recycle(ID.get(position)));
                UpdateLabourRecor(ID.get(position));
                SetTextValue(ID.get(position));
                PayTextViewAction(ID.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void UpdateLabourRecor(long id){
     Cursor c=getAllLabour_Recycle(id);
     Double CountWork = 0.0,CountWorkAmount=0.0;
     while (c.moveToNext()){
         String Work=c.getString(c.getColumnIndex(Contracts.Labour_Record_Recycle.COLUMN_4));
         String Amount=c.getString(c.getColumnIndex(Contracts.Labour_Record_Recycle.COLUMN_5));
         CountWork=CountWork+Double.parseDouble(Work);
         CountWorkAmount=CountWorkAmount+Double.parseDouble(Amount);
     }
     //updateing labour Record
        SQLiteDatabase mDb = db_helper.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(Contracts.Labour_Record.COLUMN_3,Double.toString(CountWork));
        args.put(Contracts.Labour_Record.COLUMN_4,Double.toString(CountWorkAmount));
        mDb.update(Contracts.Labour_Record.TABLE_NAME, args, Contracts.Labour_Record._ID + " = " + id, null);
    }
    public void SetTextValue(long id) {
        db_helper = new DbHelper(getContext());
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Labour_Record._ID, Contracts.Labour_Record.COLUMN_1, Contracts.Labour_Record.COLUMN_2, Contracts.Labour_Record.COLUMN_3, Contracts.Labour_Record.COLUMN_4, Contracts.Labour_Record.COLUMN_5, Contracts.Labour_Record.COLUMN_6};
        Cursor cr = mDb.query(Contracts.Labour_Record.TABLE_NAME, columns, Contracts.Labour_Record._ID + " = '" + id + "'", null, null, null, null);
        String Work = "", Amount = "", Pay = "";
        while (cr.moveToNext()) {
            Work = cr.getString(cr.getColumnIndex(Contracts.Labour_Record.COLUMN_3));
            Amount = cr.getString(cr.getColumnIndex(Contracts.Labour_Record.COLUMN_4));
            Pay = cr.getString(cr.getColumnIndex(Contracts.Labour_Record.COLUMN_5));
            if (Pay.equals("")) {
                Pay = "0.0";
            }
        }
        textView1.setText(Work);
        textView2.setText(Amount);
        editText1.setText(Pay);
        System.out.println();
        if (textView2.getText().toString().equals(editText1.getText().toString())) {
            textView3.setText("PAID");
            textView3.setTextColor(Color.GREEN);
        }
        else {
            textView3.setText(Double.toString(Double.parseDouble(Amount) - Double.parseDouble(Pay)));
        }
    }
    public void PayTextViewAction(final long ID){
        editText1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editText1.setTextColor(Color.RED);
                editText1.getLayoutParams().width=OldWeight;
                textView4.getLayoutParams().width=0;
                editText1.setText("0.0");
                button1.setVisibility(View.VISIBLE);
                return false;
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String total="";
                if(editText1.getText().toString().equals("")){
                    total=editText1.getText().toString();
                }
                else{
                    if(textView3.getText().toString().equals("PAID"))
                    {
                        textView3.setTextColor(Color.BLACK);
                        textView3.setText("0.0");

                    }
                    System.out.println("tx2"+textView2.getText().toString()+" tx3"+textView3.getText().toString());
                    String PreviousAmount=Double.toString(Double.parseDouble(textView2.getText().toString())-Double.parseDouble(textView3.getText().toString()));
                    total=Double.toString(Double.parseDouble(PreviousAmount)+Double.parseDouble(editText1.getText().toString()));
                }
                SQLiteDatabase mDb = db_helper.getWritableDatabase();
                ContentValues args = new ContentValues();
                args.put(Contracts.Labour_Record.COLUMN_5,total);
                mDb.update(Contracts.Labour_Record.TABLE_NAME, args, Contracts.Labour_Record._ID + " = " + ID, null);
                editText1.setTextColor(Color.BLACK);
                textView4.getLayoutParams().width=OldWeight;
                editText1.getLayoutParams().width=initial_Weight;
                button1.setVisibility(View.GONE);
                SetTextValue(ID);
            }
        });

    }
}
