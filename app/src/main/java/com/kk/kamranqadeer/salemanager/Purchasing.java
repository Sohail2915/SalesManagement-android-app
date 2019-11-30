package com.kk.kamranqadeer.salemanager;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.kamranqadeer.salemanager.AllAdapters.Purchasing_Adapter;
import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Purchasing extends AppCompatActivity {
    DbHelper db;
    RecyclerView recyclerView;
    String Date,check;
    Long id;
    LinearLayout linearLayout;
    ImageView back,search,ok;
    AutoCompleteTextView editText;
    TextView textView;
    private Purchasing_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchasing);
        db=new DbHelper(this);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View view =getSupportActionBar().getCustomView();
        //set action bar values
        setActionBar(view);
        //edittext suggestion
        editextSuggetion();
        //set date
        SetDate();
        Intent intent=getIntent();
        if(intent.hasExtra("_ID"))
        {
            String a=intent.getStringExtra("_ID");
            id=Long.parseLong(a);
        }
        recyclerView=(RecyclerView)findViewById(R.id.PurchisingRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Cursor cursor =getAllPurch();
        // Toast.makeText(this, ""+cursor.getCount(), Toast.LENGTH_SHORT).show();
        adapter=new Purchasing_Adapter(this,cursor);
        recyclerView.setAdapter(adapter);
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
                // COMPLETED (8) Inside, get the viewHolder's itemView's tag and store in a long variable id
                //get the id of the item being swiped
                Notify_deletion(viewHolder);
                // COMPLETED (10) call swapCursor on mAdapter passing in getAllGuests() as the argument
                //update the list
                adapter.swapCursor(getAllPurch());
            }

            //COMPLETED (11) attach the ItemTouchHelper to the waitlistRecyclerView
        }).attachToRecyclerView(recyclerView);
        getAllPurch();
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
        textView.setText("PURCHASING");
        editText.setHint("Search/Date");
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
    }
    public void editextSuggetion(){
        ArrayList<String> Date=new ArrayList<String>();
        Cursor c1=getAllPurch();
        while (c1.moveToNext()){
            String date=c1.getString(c1.getColumnIndex(Contracts.Purch_Table.COLUMN_3));
            Date.add(SetDateString(date));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.suggestion_textview, Date);
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
    private void filter(String d) {
        Cursor c=getAllPurch();
        String s="";
        try{
        s=SetDate(d);}
        catch (Exception e){
            e.printStackTrace();
        }
        long id=0;
        while (c.moveToNext()){
            String date=c.getString(c.getColumnIndex(Contracts.Purch_Table.COLUMN_3));
            if(date.equals(s)){
               id=c.getInt(c.getColumnIndex(Contracts.Purch_Table._ID));
            }
        }
        adapter.swapCursor(getAllpurch1(id));
        if(editText.getText().toString().equals("")){
            adapter.swapCursor(getAllPurch());
        }
    }
    public boolean add(String Name,String Amount,String DateAndTime)
    {
        SQLiteDatabase mDb=db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contracts.Purch_Table.COLUMN_1,Name);
        values.put(Contracts.Purch_Table.COLUMN_2,Amount);
        values.put(Contracts.Purch_Table.COLUMN_3,DateAndTime);
        long result= mDb.insert(Contracts.Purch_Table.TABLE_NAME, null, values);
        if (result==-1)
        {
            return  false;
        }
        else {

            return true;
        }
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
    public String SetDate(String s){
            String string = "";
            String[] items1 = s.split(" ");
            String month = items1[0];
            String date1 = items1[1];
            String year = items1[2];
            if (month.equals("January")) {
                string = "01";
            } else if (month.equals("February")) {
                string = "02";
            } else if (month.equals("March")) {
                string = "03";
            } else if (month.equals("April")) {
                string = "04";
            } else if (month.equals("May")) {
                string = "05";
            } else if (month.equals("June")) {
                string = "06";
            } else if (month.equals("July")) {
                string = "07";
            } else if (month.equals("Agust")) {
                string = "08";
            } else if (month.equals("September")) {
                string = "09";
            } else if (month.equals("October")) {
                string = "10";
            } else if (month.equals("November")) {
                string = "11";
            } else if (month.equals("December")) {
                string = "12";
            } else {
                string = "INVALIDE MONTH";
            }

            String date = "";
            date = string + "-" + date1 + "-" + year;
            return date;
    }
    private Cursor getAllPurch() {
        SQLiteDatabase  mDb=db.getReadableDatabase();
        Cursor cr=mDb.rawQuery("select * from "+ Contracts.Purch_Table.TABLE_NAME,null);
        return cr;
    }
    public Cursor getAllpurch1(long id) {
        SQLiteDatabase mDb = db.getReadableDatabase();
        String[] columns = new String[]{Contracts.Purch_Table._ID, Contracts.Purch_Table.COLUMN_1, Contracts.Purch_Table.COLUMN_2, Contracts.Purch_Table.COLUMN_3};
        Cursor cr = mDb.query(Contracts.Purch_Table.TABLE_NAME, columns, Contracts.Purch_Table._ID + " = '" + id + "'", null, null, null, null);
        return cr;

    }
    private boolean removeGuest(long id) {
        SQLiteDatabase  mDb=db.getReadableDatabase();
        // COMPLETED (2) Inside, call mDb.delete to pass in the TABLE_NAME and the condition that WaitlistEntry._ID equals id
        return mDb.delete(Contracts.Purch_Table.TABLE_NAME, Contracts.Purch_Table._ID + "=" + id, null) > 0;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            case R.id.action_home: {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ADDPURCH(View view) {
                String s1="",s2="FINAL AMOUNT",s3=Date;
                getProfilesCount();
                s1=check;
                    if(add(s1,s2,s3)==true)
                    {

                        adapter.swapCursor(getAllPurch());
                        Toast.makeText(Purchasing.this, "SALL add Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(Purchasing.this, "ERROR", Toast.LENGTH_SHORT).show();

                    }

            }
    public void SetDate()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(c.getTime());
        String spilt[]=date.split("-");
        String y=spilt[0],m=spilt[1],d=spilt[2];
        Date=m+"-"+d+"-"+y;
    }
    public void getProfilesCount() {
        String countQuery = "SELECT * FROM " + Contracts.Purch_Table.TABLE_NAME;
        SQLiteDatabase  mDb=db.getReadableDatabase();
        Cursor cursor = mDb.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        check=Integer.toString(count);
    }
    public boolean All_amount_Delet(Long i)
    {
        SQLiteDatabase mDb = db.getReadableDatabase();
        return mDb.delete(Contracts.Purch_Amount_Table.TABLE_NAME, Contracts.Purch_Amount_Table.COLUMN_5 + "=" + i, null) > 0;
    }
    public boolean All_puchDetail_Delete(Long i)
    {
        SQLiteDatabase mDb = db.getReadableDatabase();
        return mDb.delete(Contracts.Purch_Detail_Table.TABLE_NAME, Contracts.Purch_Detail_Table.COLUMN_5 + "=" + i, null) > 0;
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
                adapter.swapCursor(getAllPurch());
                getProfilesCount();
                All_amount_Delet(id);
                All_puchDetail_Delete(id);
                dialog.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.swapCursor(getAllPurch());
                dialog.dismiss();
            }
        });
    }

}
