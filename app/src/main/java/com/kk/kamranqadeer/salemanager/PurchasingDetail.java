package com.kk.kamranqadeer.salemanager;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.kamranqadeer.salemanager.AllAdapters.Purchasing_Adapter1;
import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.function.DoubleBinaryOperator;

public class PurchasingDetail extends AppCompatActivity {
    Long id = null;
    DbHelper db_helper;
    RecyclerView recyclerView;
    Purchasing_Adapter1 adapter;
    AlertDialog alertDialog;
    String s4,amount,a="FINAL AMOUNT",amo="",scale="";
    ArrayList<String> list,Amount,Scale;
    String Quntity="0";
    LinearLayout linearLayout;
    ImageView back,search,ok;
    AutoCompleteTextView editText;
    TextView textView;
    String date="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchasing_detail);
        db_helper = new DbHelper(this);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View view =getSupportActionBar().getCustomView();
        recyclerView = (RecyclerView) findViewById(R.id.PurcDeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        if (intent.hasExtra("Purch_Date_Time")) {
            date = intent.getStringExtra("Purch_Date_Time");
        }
        if (intent.hasExtra("_ID")) {
            String a = intent.getStringExtra("_ID");
            id = Long.parseLong(a);
            // TextView textView4=findViewById(R.id.non);
            //textView4.setText(a);
        }
        //set action bar value
        setActionBar(view);
        //set suggetion
        editextSuggetion();
       // editextSuggetion();
        Cursor cursor = getAllpurch();
        adapter = new Purchasing_Adapter1(this, cursor);
        recyclerView.setAdapter(adapter);
        addAmount();
        getAllAmount();
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            // COMPLETED (4) Override onMove and simply return false inside
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //do nothing, we only care about swiping
                return false;
            }

            // COMPLETED (5) Override onSwiped
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Notify_deletion(viewHolder);
                adapter.swapCursor(getAllpurch());
            }

        }).attachToRecyclerView(recyclerView);
        showfinalamount();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void setActionBar(View view){
        back=view.findViewById(R.id.back);
        search=view.findViewById(R.id.search);
        editText=view.findViewById(R.id.edittext);
        linearLayout=view.findViewById(R.id.check1);
        linearLayout.setVisibility(View.GONE);
        textView=view.findViewById(R.id.text);
        linearLayout.setVisibility(View.GONE);
        ok=view.findViewById(R.id.ok);
        textView.setText(SetDateString(date));
        //back action
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //search action
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setVisibility(View.GONE);
                search.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setVisibility(View.VISIBLE);
                search.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                editText.getText().clear();
            }
        });
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editextSuggetion();
                return false;
            }
        });
    }
    public void editextSuggetion(){
        ArrayList<String> Name=new ArrayList<String>();
        Cursor c1=getAllpurch();
        while (c1.moveToNext()){
            String name=c1.getString(c1.getColumnIndex(Contracts.Purch_Detail_Table.COLUMN_1));
            Name.add(name);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.suggestion_textview, Name);
        editText.setAdapter(adapter);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });
    }
    private void filter(String n) {
        Cursor c=getAllpurch();
        long id=0;
        while (c.moveToNext()){
            String name=c.getString(c.getColumnIndex(Contracts.Purch_Detail_Table.COLUMN_1));
            if(n.equals(name)){
                id=c.getInt(c.getColumnIndex(Contracts.Purch_Detail_Table._ID));
            }
        }
        adapter.swapCursor(getAllpurch_Search(id));
        if(editText.getText().toString().equals("")){
            adapter.swapCursor(getAllpurch());
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_home: {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            }
        }

        return PurchasingDetail.super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    public void AddMaterial(View view) {
        get_Raw_Material_List();
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(PurchasingDetail.this);
        View mview = getLayoutInflater().inflate(R.layout.purch_dialog, null);
        ArrayAdapter<String> adapt;

        final EditText editText1 = mview.findViewById(R.id.m_quntity);
        final EditText editText2 = mview.findViewById(R.id.amount);
        final TextView textView1=mview.findViewById(R.id.SCALE_1);
        final TextView textView2=mview.findViewById(R.id.SCALE_2);
        final TextView textView3=mview.findViewById(R.id.SCALE_3);
        final Spinner spinner=mview.findViewById(R.id.spinner);
        final Spinner spinner1=mview.findViewById(R.id.spinner1);
        adapt = new ArrayAdapter<String>(getApplicationContext(),
        android.R.layout.simple_spinner_dropdown_item, list);
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapt);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner.getSelectedItem().toString().equals("Select")) {
                    textView1.setText("Scale");
                    textView2.setText("ONE");
                    textView3.setText("FINAL");
                    editText2.getText().clear();
                    editText1.getText().clear();
                    editText2.setEnabled(true);
                }
                else {
                    for (int i = 0; i < list.size(); i++) {
                        if (spinner.getSelectedItem().toString().equals(list.get(i))) {
                            amo = Amount.get(i);
                            scale = Scale.get(i);
                            break;
                        }
                    }
                    textView1.setText(scale);
                    textView2.setText("ONE "+scale);
                    editText2.setText(amo);
                    editText2.setEnabled(false);
                    ArrayList<String> s=new ArrayList<String>();
                    if(textView1.getText().toString().equals("kg")){
                        s.clear();
                        s.add("kg");
                        s.add("gm");
                    }
                    else if(textView1.getText().toString().equals("Liter")){
                        s.clear();
                        s.add("Liter");
                        s.add("ml");
                    }
                    else{
                        s.clear();
                        s.add(scale);
                    }
                    ArrayAdapter<String> adapt1;
                    adapt1 = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, s);
                    spinner1.setAdapter(adapt1);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (editText1.getText().toString().equals("")) {}
                    else{
                        if(spinner1.getSelectedItem().toString().equals("gm")||spinner1.getSelectedItem().toString().equals("ml")){
                            Double i = Double.parseDouble(editText1.getText().toString()), j = Double.parseDouble(editText2.getText().toString()), k = (i/1000) * j;
                            s4 = Double.toString(k);
                            Quntity=Double.toString(Double.parseDouble(editText1.getText().toString())/1000);
                            textView3.setText(s4);
                        }
                        else{
                            Double i = Double.parseDouble(editText1.getText().toString()), j = Double.parseDouble(editText2.getText().toString()), k = i * j;
                            s4 = Double.toString(k);
                            Quntity=editText1.getText().toString();
                            textView3.setText(s4);
                        }
                    }

                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText2.getText().toString()!="Amount") {
                    if(s.toString().equals("")){
                        textView3.setText(editText2.getText().toString());
                    }
                    else {
                        if(spinner1.getSelectedItem().toString().equals("gm")||spinner1.getSelectedItem().toString().equals("ml")){
                            Double i = Double.parseDouble(s.toString()), j = Double.parseDouble(editText2.getText().toString()), k = (i/1000) * j;
                            s4 = Double.toString(k);
                            Quntity=Double.toString(Double.parseDouble(editText1.getText().toString())/1000);
                            textView3.setText(s4);
                        }
                        else{
                            Double i = Double.parseDouble(s.toString()), j = Double.parseDouble(editText2.getText().toString()), k = i * j;
                            s4 = Double.toString(k);
                            Quntity=editText1.getText().toString();
                            textView3.setText(s4);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        Button button1 = (Button) mview.findViewById(R.id.save);
        Button button2 = (Button) mview.findViewById(R.id.exit);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1 = spinner.getSelectedItem().toString();
                String s2 = editText2.getText().toString();
                String s3 = Quntity;
                String s5 = Quntity;
                String s6 = scale;
                if(check_Material_AllReady(spinner.getSelectedItem().toString())==1){
                    Toast.makeText(PurchasingDetail.this, "THIS MATERIL ALL RADY EXIST", Toast.LENGTH_SHORT).show();
                    editText1.getText().clear();
                    editText2.getText().clear();
                }
                else {
                    if (s2.trim().equals("") || s3.trim().equals("")||s1.trim().equals("Select")) {
                        Toast.makeText(PurchasingDetail.this, "ENTER ALL DETAILS", Toast.LENGTH_SHORT).show();
                    } else {

                        if (addN(s1, s2, s3, s4, s5, s6, id) == true) {
                            adapter.swapCursor(getAllpurch());
                            Toast.makeText(PurchasingDetail.this, "Details add Successfully", Toast.LENGTH_SHORT).show();
                            //  editText.getText().clear();
                            Dialog_Dismis();
                            addAmount();
                            getAllAmount();
                        } else {
                            Toast.makeText(PurchasingDetail.this, " ERROR", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog_Dismis();
            }
        });
        mbuilder.setView(mview);
        alertDialog = mbuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


    }

    public void Dialog_Dismis() {
        alertDialog.dismiss();
    }

    public boolean addN(String n, String onepice, String quntity, String totalamount,String Weight,String Scale, Long id) {
        SQLiteDatabase mDb = db_helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contracts.Purch_Detail_Table.COLUMN_1, n);
        values.put(Contracts.Purch_Detail_Table.COLUMN_2, onepice);
        values.put(Contracts.Purch_Detail_Table.COLUMN_3, quntity);
        values.put(Contracts.Purch_Detail_Table.COLUMN_4, totalamount);
        values.put(Contracts.Purch_Detail_Table.COLUMN_6, Weight);
        values.put(Contracts.Purch_Detail_Table.COLUMN_7, Scale);
        values.put(Contracts.Purch_Detail_Table.COLUMN_5, id);
        long result = mDb.insert(Contracts.Purch_Detail_Table.TABLE_NAME, null, values);
        if (result == -1) {
            return false;
        } else {

            return true;
        }
    }

    public Cursor getAllpurch() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Purch_Detail_Table._ID, Contracts.Purch_Detail_Table.COLUMN_1, Contracts.Purch_Detail_Table.COLUMN_2, Contracts.Purch_Detail_Table.COLUMN_3,Contracts.Purch_Detail_Table.COLUMN_4,Contracts.Purch_Detail_Table.COLUMN_6,Contracts.Purch_Detail_Table.COLUMN_7};
        Cursor cr = mDb.query(Contracts.Purch_Detail_Table.TABLE_NAME, columns, Contracts.Purch_Detail_Table.COLUMN_5 + " = '" + id + "'", null, null, null, null);
        return cr;

    }
    public void addAmount() {
        final EditText editText = findViewById(R.id.Ext_Amount);
        Button button1 = (Button) findViewById(R.id.SAVE_amount);
        final String s2=getProfilesCount();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1 = editText.getText().toString();
                if (s1.equals("")||getAllpurch().getCount()==0) {
                    Toast.makeText(PurchasingDetail.this, "ENTER ALL DETAILS", Toast.LENGTH_SHORT).show();
                } else {
                    String total;
                    if (check()==0) {
                        Double i = Double.parseDouble(amount), j = Double.parseDouble(s1), k = i + j;
                         total = Double.toString(k);
                        a=Double.toString(k);
                        if (addN2(amount, s2, s1, total, id) == true) {
                            Toast.makeText(PurchasingDetail.this, "Details add Successfully", Toast.LENGTH_SHORT).show();
                            showfinalamount();
                            editText.getText().clear();
                        } else {
                            Toast.makeText(PurchasingDetail.this, " ERROR", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else{
                        Double i = Double.parseDouble(amount), j = Double.parseDouble(s1), k = i + j;
                        total = Double.toString(k);
                        a=Double.toString(k);
                        if (update(amount, s2, s1, total, id) == true) {
                            Toast.makeText(PurchasingDetail.this, "Details update Successfully", Toast.LENGTH_SHORT).show();
                            showfinalamount();
                            editText.getText().clear();
                        } else {
                            Toast.makeText(PurchasingDetail.this, " ERROR", Toast.LENGTH_SHORT).show();

                        }
                    }

                }
                }

        }
        );
    }
    private void getAllAmount() {
        SQLiteDatabase  mDb=db_helper.getReadableDatabase();
        String s="SELECT " +Contracts.Purch_Detail_Table.COLUMN_4+ " from " + Contracts.Purch_Detail_Table.TABLE_NAME+" WHERE "+Contracts.Purch_Detail_Table.COLUMN_5+" = "+id;
        Cursor cur = mDb.rawQuery(s, new String[] {});
        ArrayList<Double> array = new ArrayList<Double>();
        Double j=null;
        try {
            while (cur.moveToNext()) {
                String uname = cur.getString(cur.getColumnIndex(Contracts.Purch_Detail_Table.COLUMN_4));
                j = Double.parseDouble(uname);
                array.add(j);
                j=j-j;
            }
            for (int i = 0; i < array.size(); i++) {
                j+=array.get(i);
            }
            if (j!= null) {
                amount = Double.toString(j);
                final TextView textView = findViewById(R.id.total_amount);
                textView.setText(amount + " RS");
                cur.close();
            } else {
                final TextView textView = findViewById(R.id.total_amount);
                textView.setText("0 RS");
                cur.close();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(PurchasingDetail.this, " ERROR", Toast.LENGTH_SHORT).show();

        }


    }
        public String getProfilesCount() {
        String countQuery = "SELECT * FROM " + Contracts.Purch_Detail_Table.TABLE_NAME+" WHERE "+Contracts.Purch_Detail_Table.COLUMN_5+" = "+id;
        SQLiteDatabase  mDb=db_helper.getReadableDatabase();
        Cursor cursor = mDb.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        String i;
        i=Integer.toString(count);
        final TextView textView = findViewById(R.id.item_quntity);
        textView.setText(i);
        if(count==0)
        {
            if(deleterow()==true){
                Toast.makeText(this,"TOTAL IS 0 RS",Toast.LENGTH_SHORT).show();
                final TextView textView1 = findViewById(R.id.final_amount);
                textView1.setText("0 RS");
            }
        }
        return i;
    }
    public boolean addN2(String totalcost,String items,String Extra,String Amount,Long id)
    {
        SQLiteDatabase mDb=db_helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contracts.Purch_Amount_Table.COLUMN_1,totalcost);
        values.put(Contracts.Purch_Amount_Table.COLUMN_2,items);
        values.put(Contracts.Purch_Amount_Table.COLUMN_3,Extra);
        values.put(Contracts.Purch_Amount_Table.COLUMN_4,Amount);
        values.put(Contracts.Purch_Amount_Table.COLUMN_5,id);
        purchasingupdate(Amount);
        long result= mDb.insert(Contracts.Purch_Amount_Table.TABLE_NAME, null, values);
        if (result==-1)
        {
            return  false;
        }
        else {

            return true;
        }
    }
    public boolean update(String totalcost,String items,String Extra,String Amount,Long id) {
        SQLiteDatabase mDb = db_helper.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(Contracts.Purch_Amount_Table.COLUMN_1, totalcost);
        args.put(Contracts.Purch_Amount_Table.COLUMN_2, items);
        args.put(Contracts.Purch_Amount_Table.COLUMN_3, Extra);
        args.put(Contracts.Purch_Amount_Table.COLUMN_4, Amount);
        args.put(Contracts.Purch_Amount_Table.COLUMN_5, id);
        purchasingupdate(Amount);
        long result=mDb.update(Contracts.Purch_Amount_Table.TABLE_NAME, args, Contracts.Purch_Amount_Table.COLUMN_5 + "=" + id, null);
        if (result==-1)
        {
            return  false;
        }
        else {

            return true;
        }
    }
    public void showfinalamount() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String s = "SELECT " + Contracts.Purch_Amount_Table.COLUMN_4+","+Contracts.Purch_Amount_Table.COLUMN_3 + " from " + Contracts.Purch_Amount_Table.TABLE_NAME + " WHERE " + Contracts.Purch_Amount_Table.COLUMN_5 + " = " +id;
        Cursor cur = mDb.rawQuery(s, new String[]{});
        final TextView textView1 = findViewById(R.id.final_amount);
        final EditText editText=findViewById(R.id.Ext_Amount);
        try {
            while (cur.moveToNext()) {
                String amou = cur.getString(cur.getColumnIndex(Contracts.Purch_Amount_Table.COLUMN_4));
                String extra = cur.getString(cur.getColumnIndex(Contracts.Purch_Amount_Table.COLUMN_3));
                textView1.setText(amou);
                editText.setText(extra);
            }
        }
        catch (Exception e){}

    }
    public boolean deleterow()
    {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        return mDb.delete(Contracts.Purch_Amount_Table.TABLE_NAME, Contracts.Purch_Amount_Table.COLUMN_5 + "=" + id, null) > 0;
    }
    public int check()
    {
        String countQuery = "SELECT * FROM " + Contracts.Purch_Amount_Table.TABLE_NAME+" WHERE "+Contracts.Purch_Amount_Table.COLUMN_5+" = "+id;
        SQLiteDatabase  mDb=db_helper.getReadableDatabase();
        Cursor cursor = mDb.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    public void purchasingupdate(String totalcost) {
        SQLiteDatabase mDb = db_helper.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(Contracts.Purch_Table.COLUMN_2, totalcost);
        mDb.update(Contracts.Purch_Table.TABLE_NAME, args, Contracts.Purch_Table._ID+" = " +id, null);
    }
    private boolean removeGuest(long id) {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        // COMPLETED (2) Inside, call mDb.delete to pass in the TABLE_NAME and the condition that WaitlistEntry._ID equals id
        return mDb.delete(Contracts.Purch_Detail_Table.TABLE_NAME, Contracts.Purch_Detail_Table._ID + "=" + id, null) > 0;
    }
    public void Notify_deletion(final RecyclerView.ViewHolder viewHolder) {
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.delete_dialog);
        Button button1 = (Button) dialog.findViewById(R.id.ok);
        Button button2 = (Button) dialog.findViewById(R.id.no);
        dialog.show();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = (long) viewHolder.itemView.getTag();
                // COMPLETED (9) call removeGuest and pass through that id
                //remove from DB
                removeGuest(id);
                adapter.swapCursor(getAllpurch());
                addAmount();
                getAllAmount();
                purchasingupdate(a);
                showfinalamount();
                dialog.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.swapCursor(getAllpurch());
                dialog.dismiss();
            }
        });
    }
    public void get_Raw_Material_List(){
        list=new ArrayList<String>();
        Amount=new ArrayList<String>();
        Scale=new ArrayList<String>();
        list.add("Select");
        Amount.add("kk");
        Scale.add("kk");
        String countQuery = "SELECT * FROM " + Contracts.Sale_Table.TABLE_NAME;
        SQLiteDatabase  mDb=db_helper.getReadableDatabase();
        Cursor cursor = mDb.rawQuery(countQuery, null);
        while (cursor.moveToNext()){
            String Name = cursor.getString(cursor.getColumnIndex(Contracts.Sale_Table.COLUMN_1));
            String amount = cursor.getString(cursor.getColumnIndex(Contracts.Sale_Table.COLUMN_2));
            String scale = cursor.getString(cursor.getColumnIndex(Contracts.Sale_Table.COLUMN_3));
            list.add(Name);
            Amount.add(amount);
            Scale.add(scale);
        }
    }
    public int check_Material_AllReady (String s){
        List<String> list;
        list = new ArrayList<String>();
        int check=0;
        Cursor c=getAllpurch();
        while (c.moveToNext()){
            String Purch_Name = c.getString(c.getColumnIndex(Contracts.Purch_Detail_Table.COLUMN_1));
            list.add(Purch_Name);
        }
        for(int i=0;i<list.size();i++){
            if(s.equals(list.get(i))){
                check=1;
                return check;
            }
        }
        return check;
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
            string="Agust";
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
    public Cursor getAllpurch_Search(long id) {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Purch_Detail_Table._ID, Contracts.Purch_Detail_Table.COLUMN_1, Contracts.Purch_Detail_Table.COLUMN_2, Contracts.Purch_Detail_Table.COLUMN_3,Contracts.Purch_Detail_Table.COLUMN_4,Contracts.Purch_Detail_Table.COLUMN_6,Contracts.Purch_Detail_Table.COLUMN_7};
        Cursor cr = mDb.query(Contracts.Purch_Detail_Table.TABLE_NAME, columns, Contracts.Purch_Detail_Table._ID + " = '" + id + "'", null, null, null, null);
        return cr;

    }

}
