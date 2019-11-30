package com.kk.kamranqadeer.salemanager;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kk.kamranqadeer.salemanager.AllAdapters.Recor_Adapter;
import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.widget.LinearLayout.HORIZONTAL;

public class Record extends AppCompatActivity {
    DbHelper db_helper;
    RecyclerView recyclerView;
    private Recor_Adapter adapter;
    LinearLayout linearLayout;
    ImageView back,search,ok;
    AutoCompleteTextView editText;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        db_helper=new DbHelper(this);
        //custom action bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View view =getSupportActionBar().getCustomView();
        //set action bar values
        setActionBar(view);
        //edittext suggestion
        editextSuggetion();
        //set recycleview
        recyclerView=(RecyclerView)findViewById(R.id.RecordRecycleView);
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, HORIZONTAL);
        recyclerView.addItemDecoration(itemDecor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Cursor cursor =getAllRecord_Date();
        adapter=new Recor_Adapter(this,cursor);
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
                Notify_deletion(viewHolder);
                adapter.swapCursor(getAllRecord_Date());
            }

            //COMPLETED (11) attach the ItemTouchHelper to the waitlistRecyclerView
        }).attachToRecyclerView(recyclerView);
        getDate();
        giveMaterialID();
        giveLabourRecordId();
        giveLabourRecycleId();
        giveProduction_RecordID();
        giveAccount_Record_ID();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
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
    public void setActionBar(View view){
        back=view.findViewById(R.id.back);
        search=view.findViewById(R.id.search);
        editText=view.findViewById(R.id.edittext);
        linearLayout=view.findViewById(R.id.check1);
        linearLayout.setVisibility(View.GONE);
        textView=view.findViewById(R.id.text);
        linearLayout.setVisibility(View.GONE);
        ok=view.findViewById(R.id.ok);
        textView.setText("ALL RECORD DATE");
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
        Cursor c1=getAllRecord_Date();
        while (c1.moveToNext()){
            String date=c1.getString(c1.getColumnIndex(Contracts.Record_Date.COLUMN_1));
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
        Cursor c=getAllRecord_Date();
        String s="";
        try{
            s=SetDate(d);}
        catch (Exception e){
            e.printStackTrace();
        }
        long id=0;
        while (c.moveToNext()){
            String date=c.getString(c.getColumnIndex(Contracts.Record_Date.COLUMN_1));
            if(date.equals(s)){
                id=c.getInt(c.getColumnIndex(Contracts.Record_Date._ID));
            }
        }
        adapter.swapCursor(getAllRecord_DateSearch(id));
        if(editText.getText().toString().equals("")){
            adapter.swapCursor(getAllRecord_Date());
        }
    }
    public String SetDateString(String s){
        String string="";
        String[] items1 = s.split("-");
        String month=items1[0];
       // String date1=items1[1];
        String year=items1[1];
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
        date=string+" "+year;
        return date;
    }
    public String SetDate(String s){
        String string = "";
        String[] items1 = s.split(" ");
        String month = items1[0];
        //String date1 = items1[1];
        String year = items1[1];
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
        date = string + "-" + year;
        return date;
    }
    private Cursor getAllRecord_Date() {
        SQLiteDatabase mDb=db_helper.getReadableDatabase();
        Cursor cr=mDb.rawQuery("select * from "+ Contracts.Record_Date.TABLE_NAME,null);
        return cr;

    }
    private Cursor getAllRecord_DateSearch(long id) {
        SQLiteDatabase mDb=db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Record_Date._ID, Contracts.Record_Date.COLUMN_1};
        Cursor cr = mDb.query(Contracts.Record_Date.TABLE_NAME, columns, Contracts.Record_Date._ID+" = "+id, null, null, null, null);
        return cr;

    }
    public void Notify_deletion(final RecyclerView.ViewHolder viewHolder) {
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.delete_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button button1 = (Button) dialog.findViewById(R.id.ok);
        Button button2 = (Button) dialog.findViewById(R.id.no);
        dialog.show();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = (long) viewHolder.itemView.getTag();
                // COMPLETED (9) call removeGuest and pass through that id
                //remove from DB
                //get LAbour Record id
                Cursor c=getAllLabour_Record_id(id);
                long ID=0;
                while (c.moveToNext()){
                    ID=c.getInt(c.getColumnIndex(Contracts.Labour_Record._ID));
                }
                removeLabour_Recycle(ID);
                removeLabour_Record(id);
                removeMaterial_Record(id);
                removeAccount_Record(id);
                removeProduction_Record(id);
                removeRecord(id);
                adapter.swapCursor(getAllRecord_Date());
                dialog.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.swapCursor(getAllRecord_Date());
                dialog.dismiss();
            }
        });
    }
    private boolean removeRecord(long id) {
        SQLiteDatabase  mDb=db_helper.getReadableDatabase();
        // COMPLETED (2) Inside, call mDb.delete to pass in the TABLE_NAME and the condition that WaitlistEntry._ID equals id
        return mDb.delete(Contracts.Record_Date.TABLE_NAME, Contracts.Record_Date._ID + "=" + id, null) > 0;
    }
    private boolean removeLabour_Record(long id) {
        SQLiteDatabase  mDb=db_helper.getReadableDatabase();
        // COMPLETED (2) Inside, call mDb.delete to pass in the TABLE_NAME and the condition that WaitlistEntry._ID equals id
        return mDb.delete(Contracts.Labour_Record.TABLE_NAME, Contracts.Labour_Record.COLUMN_6 + "=" + id, null) > 0;
    }
    private boolean removeAccount_Record(long id) {
        SQLiteDatabase  mDb=db_helper.getReadableDatabase();
        // COMPLETED (2) Inside, call mDb.delete to pass in the TABLE_NAME and the condition that WaitlistEntry._ID equals id
        return mDb.delete(Contracts.Account_Record.TABLE_NAME, Contracts.Account_Record.COLUMN_6 + "=" + id, null) > 0;
    }
    private boolean removeLabour_Recycle(long id) {
        SQLiteDatabase  mDb=db_helper.getReadableDatabase();
        // COMPLETED (2) Inside, call mDb.delete to pass in the TABLE_NAME and the condition that WaitlistEntry._ID equals id
        return mDb.delete(Contracts.Labour_Record_Recycle.TABLE_NAME, Contracts.Labour_Record_Recycle.COLUMN_6 + "=" + id, null) > 0;
    }
    private boolean removeMaterial_Record(long id) {
        SQLiteDatabase  mDb=db_helper.getReadableDatabase();
        // COMPLETED (2) Inside, call mDb.delete to pass in the TABLE_NAME and the condition that WaitlistEntry._ID equals id
        return mDb.delete(Contracts.Material_record.TABLE_NAME, Contracts.Material_record.COLUMN_6 + "=" + id, null) > 0;
    }
    private boolean removeProduction_Record(long id) {
        SQLiteDatabase  mDb=db_helper.getReadableDatabase();
        // COMPLETED (2) Inside, call mDb.delete to pass in the TABLE_NAME and the condition that WaitlistEntry._ID equals id
        return mDb.delete(Contracts.Production_Record.TABLE_NAME, Contracts.Production_Record.COLUMN_6 + "=" + id, null) > 0;
    }
    public void getDate(){
        ArrayList<String> Month_Year1;
        ArrayList<Long> id_list=new ArrayList<Long>();
        Set<String> hs = new HashSet<>();
        Month_Year1=new ArrayList<String>();
      Cursor c=getAllpurch2();
        while (c.moveToNext()){
           String DATE=c.getString(c.getColumnIndex(Contracts.Production_Today.COLUMN_5));
           long id=c.getInt(c.getColumnIndex(Contracts.Production_Today._ID));
           String list[]=DATE.split("-");
           String M=list[0],Y=list[2],date=M+"-"+Y;
            hs.add(date);
            id_list.add(id);
        }
         hs.addAll(Month_Year1);
        Month_Year1.clear();
        Month_Year1.addAll(hs);
        for(int i=0;i<Month_Year1.size();i++){
            if(check_Date_AllReady(Month_Year1.get(i))==0){
                addN(Month_Year1.get(i),id_list.get(i));
            }
        }
    }
    public boolean addN(String DATE,long id)
    {
        SQLiteDatabase mDb=db_helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contracts.Record_Date.COLUMN_1,DATE);
        values.put(Contracts.Record_Date.COLUMN_2,id);
        long result= mDb.insert(Contracts.Record_Date.TABLE_NAME, null, values);
        if (result==-1)
        {
            return  false;
        }
        else {

            return true;
        }
    }
    public Cursor getAllpurch2() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Production_Today._ID, Contracts.Production_Today.COLUMN_1, Contracts.Production_Today.COLUMN_2, Contracts.Production_Today.COLUMN_3, Contracts.Production_Today.COLUMN_4, Contracts.Production_Today.COLUMN_5, Contracts.Production_Today.COLUMN_6, Contracts.Production_Today.COLUMN_7, Contracts.Production_Today.COLUMN_8};
        Cursor cr = mDb.query(Contracts.Production_Today.TABLE_NAME, columns, null, null, null, null, null);
        return cr;

    }
    public int check_Date_AllReady (String s){
        List<String> list;
        list = new ArrayList<String>();
        int check=0;
        Cursor c=getAllRecord_Date();
        while (c.moveToNext()){
            String Purch_Name = c.getString(c.getColumnIndex(Contracts.Record_Date.COLUMN_1));
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
    public Cursor getAllMaterial() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Material_record._ID, Contracts.Material_record.COLUMN_1,Contracts.Material_record.COLUMN_2};
        Cursor cr = mDb.query(Contracts.Material_record.TABLE_NAME, columns, null, null, null, null, null);
        return cr;

    }
    public void giveMaterialID()
    {
        Cursor c1=getAllRecord_Date(),c2=getAllMaterial();
        ArrayList<String> list=new ArrayList<String>();
        ArrayList<Integer> ID=new ArrayList<Integer>();

        while (c2.moveToNext()){
            String date=c2.getString(c2.getColumnIndex(Contracts.Material_record.COLUMN_1));
            int id=c2.getInt(c2.getColumnIndex(Contracts.Material_record._ID));
            list.add(date);
            ID.add(id);
        }
        while (c1.moveToNext()){
            String date=c1.getString(c1.getColumnIndex(Contracts.Record_Date.COLUMN_1));
            for (int i=0;i<list.size();i++){
                if(date.equals(list.get(i))) {
                        int id = c1.getInt(c1.getColumnIndex(Contracts.Record_Date._ID));
                        SQLiteDatabase mDb = db_helper.getWritableDatabase();
                        ContentValues args = new ContentValues();
                        args.put(Contracts.Material_record.COLUMN_6, id);
                        mDb.update(Contracts.Material_record.TABLE_NAME, args, Contracts.Material_record._ID + " = " + ID.get(i), null);
                }
            }
        }

    }
    public void giveProduction_RecordID()
    {
        Cursor c1=getAllRecord_Date(),c2=getAllProductionRecord();
        ArrayList<String> list=new ArrayList<String>();
        ArrayList<Integer> ID=new ArrayList<Integer>();
        while (c2.moveToNext()){
            String date=c2.getString(c2.getColumnIndex(Contracts.Production_Record.COLUMN_1));
            int id=c2.getInt(c2.getColumnIndex(Contracts.Production_Record._ID));
            list.add(date);
            ID.add(id);
        }
        while (c1.moveToNext()){
            String date=c1.getString(c1.getColumnIndex(Contracts.Record_Date.COLUMN_1));
            for (int i=0;i<list.size();i++){
                if(date.equals(list.get(i))) {
                    int id = c1.getInt(c1.getColumnIndex(Contracts.Record_Date._ID));
                    SQLiteDatabase mDb = db_helper.getWritableDatabase();
                    ContentValues args = new ContentValues();
                   // args.put(Contracts.Production_Record.COLUMN_4, Sale);
                   // args.put(Contracts.Production_Record.COLUMN_5, id);
                    args.put(Contracts.Production_Record.COLUMN_6, id);
                    mDb.update(Contracts.Production_Record.TABLE_NAME, args, Contracts.Production_Record._ID + " = " + ID.get(i), null);
                }
            }
        }

    }
    public Cursor getAllLabour_Record() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Labour_Record._ID, Contracts.Labour_Record.COLUMN_1,Contracts.Labour_Record.COLUMN_2,Contracts.Labour_Record.COLUMN_3,Contracts.Labour_Record.COLUMN_4};
        Cursor cr = mDb.query(Contracts.Labour_Record.TABLE_NAME, columns, null, null, null, null, null);
        return cr;

    }
    public Cursor getAllLabour_Record_id(long id) {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Labour_Record._ID};
        Cursor cr = mDb.query(Contracts.Labour_Record.TABLE_NAME, columns, Contracts.Labour_Record.COLUMN_6+" = "+id, null, null, null, null);
        return cr;

    }
    public Cursor getAllLabour_Recycle() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Labour_Record_Recycle._ID, Contracts.Labour_Record_Recycle.COLUMN_1, Contracts.Labour_Record_Recycle.COLUMN_2,Contracts.Labour_Record_Recycle.COLUMN_3,Contracts.Labour_Record_Recycle.COLUMN_4,Contracts.Labour_Record_Recycle.COLUMN_5,Contracts.Labour_Record_Recycle.COLUMN_6};
        Cursor cr = mDb.query(Contracts.Labour_Record_Recycle.TABLE_NAME, columns, null, null, null, null, null);
        return cr;
    }
    public Cursor getAllProductionRecord() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Production_Record._ID, Contracts.Production_Record.COLUMN_1, Contracts.Production_Record.COLUMN_2,Contracts.Production_Record.COLUMN_3,Contracts.Production_Record.COLUMN_4,Contracts.Production_Record.COLUMN_5,Contracts.Production_Record.COLUMN_6};
        Cursor cr = mDb.query(Contracts.Production_Record.TABLE_NAME, columns, null, null, null, null, null);
        return cr;
    }
    public Cursor getAllAccount_Record() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Account_Record._ID, Contracts.Account_Record.COLUMN_1, Contracts.Account_Record.COLUMN_2,Contracts.Account_Record.COLUMN_3,Contracts.Account_Record.COLUMN_4,Contracts.Account_Record.COLUMN_5,Contracts.Account_Record.COLUMN_6};
        Cursor cr = mDb.query(Contracts.Account_Record.TABLE_NAME, columns, null, null, null, null, null);
        return cr;
    }
    public void giveLabourRecordId(){
        Cursor c1=getAllRecord_Date(),c2=getAllLabour_Record();
        ArrayList<String> list=new ArrayList<String>();
        ArrayList<Integer> ID=new ArrayList<Integer>();
        String Date="",Name="";
        while (c2.moveToNext()){
            Date=c2.getString(c2.getColumnIndex(Contracts.Labour_Record.COLUMN_1));
            int id=c2.getInt(c2.getColumnIndex(Contracts.Labour_Record._ID));
            list.add(Date);
            ID.add(id);
        }
        while (c1.moveToNext()){
            String date=c1.getString(c1.getColumnIndex(Contracts.Record_Date.COLUMN_1));
            for (int i=0;i<list.size();i++){
                if(date.equals(list.get(i))) {
                    int id = c1.getInt(c1.getColumnIndex(Contracts.Record_Date._ID));
                    SQLiteDatabase mDb = db_helper.getWritableDatabase();
                    ContentValues args = new ContentValues();
                    args.put(Contracts.Labour_Record.COLUMN_6, id);
                    mDb.update(Contracts.Labour_Record.TABLE_NAME, args, Contracts.Labour_Record._ID + " = " + ID.get(i), null);
                }
            }
        }
    }
    public void giveLabourRecycleId(){
        Cursor c1=getAllLabour_Recycle(),c2=getAllLabour_Record();
        ArrayList<String> list=new ArrayList<String>();
        ArrayList<Integer> ID=new ArrayList<Integer>();
        ArrayList<String> N=new ArrayList<String>();
        String Date="",Name="";
        while (c1.moveToNext()){
            Date=c1.getString(c1.getColumnIndex(Contracts.Labour_Record_Recycle.COLUMN_1));
            Name=c1.getString(c1.getColumnIndex(Contracts.Labour_Record_Recycle.COLUMN_2));
            int id=c1.getInt(c1.getColumnIndex(Contracts.Labour_Record_Recycle._ID));
            String spilt[]=Date.split("-");
            String m=spilt[0],day=spilt[1],y=spilt[2],date=m+"-"+y;
            list.add(date);
            ID.add(id);
            N.add(Name);
        }
        while (c2.moveToNext()){
            String date=c2.getString(c2.getColumnIndex(Contracts.Labour_Record.COLUMN_1));
            String name=c2.getString(c2.getColumnIndex(Contracts.Labour_Record.COLUMN_2));
            for (int i=0;i<list.size();i++){
                if(date.equals(list.get(i))&&name.equals(N.get(i))) {
                    int id = c2.getInt(c2.getColumnIndex(Contracts.Labour_Record._ID));
                    SQLiteDatabase mDb = db_helper.getWritableDatabase();
                    ContentValues args = new ContentValues();
                    args.put(Contracts.Labour_Record_Recycle.COLUMN_6, id);
                    mDb.update(Contracts.Labour_Record_Recycle.TABLE_NAME, args, Contracts.Labour_Record_Recycle._ID + " = " + ID.get(i), null);
                }
            }
        }
    }
    public int check_Account_Allredy (String s){
        List<String> list;
        list = new ArrayList<String>();
        int check=0;
        Cursor c=getAllAccount_Record();
        if(c.getCount()!=0) {
            while (c.moveToNext()) {
                String date = c.getString(c.getColumnIndex(Contracts.Account_Record.COLUMN_1));
                list.add(date);
            }
            for (int i = 0; i < list.size(); i++) {
                if (s.equals(list.get(i))) {
                    check = 1;
                    return check;
                }
            }
        }
        return check;
    }
    public void giveAccount_Record_ID() {
        Cursor c=getAllRecord_Date();
        while (c.moveToNext()) {
            String Date=c.getString(c.getColumnIndex(Contracts.Record_Date.COLUMN_1));
            long id=c.getInt(c.getColumnIndex(Contracts.Record_Date._ID));
            if (check_Account_Allredy(Date) == 0) {
                SQLiteDatabase mDb = db_helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(Contracts.Account_Record.COLUMN_1, Date);
                values.put(Contracts.Account_Record.COLUMN_2, "0");
                values.put(Contracts.Account_Record.COLUMN_3, "0");
                values.put(Contracts.Account_Record.COLUMN_4, "0");
                values.put(Contracts.Account_Record.COLUMN_5, "0");
                values.put(Contracts.Account_Record.COLUMN_6, id);
                mDb.insert(Contracts.Account_Record.TABLE_NAME, null, values);
            }
        }
    }

}
