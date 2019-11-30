package com.kk.kamranqadeer.salemanager;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.kamranqadeer.salemanager.AllAdapters.ProductionMaterialAdapter;
import com.kk.kamranqadeer.salemanager.AllAdapters.Production_Today_Adapter1;
import com.kk.kamranqadeer.salemanager.AllAdapters.Production_Today_Adapter2;
import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;

public class Production_Today extends AppCompatActivity {
    Long id = null;
    DbHelper db_helper;
    RecyclerView recyclerView1, recyclerView2;
    Production_Today_Adapter1 adapter1;
    Production_Today_Adapter2 adapter2;
    AlertDialog alertDialog;
    LinearLayout linearLayout,slide_ayout;
    String Labour_Work = "", Dozen_Material_Cost = "";
    String s, da = "", month = "", year = "";
    ImageView back,search,ok,slide;
    AutoCompleteTextView editText;
    TextView textView;
    int check=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production__today);
        db_helper = new DbHelper(this);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View view =getSupportActionBar().getCustomView();
        //set action bar value and slide layout
        setActionBar(view);
        //set editetxt suggestion
        editextSuggetion();
        SetDate();
        recyclerView1 = (RecyclerView) findViewById(R.id.Production_Today_Recycle1);
        recyclerView1.addItemDecoration(new DividerItemDecoration(Production_Today.this, LinearLayout.HORIZONTAL));
        recyclerView2 = (RecyclerView) findViewById(R.id.Production_Today_Recycle2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        if (intent.hasExtra("NAME")) {
            s = intent.getStringExtra("NAME");

        }
        if (intent.hasExtra("_ID")) {
            String a = intent.getStringExtra("_ID");
            id = Long.parseLong(a);
        }
        // Toast.makeText(this, "id "+id, Toast.LENGTH_SHORT).show();
        Cursor cursor1 = getAllpurch();
        Cursor cursor2 = getAllpurch2();
        // Toast.makeText(this, ""+cursor.getCount(), Toast.LENGTH_SHORT).show();
        adapter1 = new Production_Today_Adapter1(this, cursor1);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(Production_Today.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(horizontalLayoutManager);
        recyclerView1.setAdapter(adapter1);
        adapter2 = new Production_Today_Adapter2(this, cursor2,s);
        recyclerView2.setAdapter(adapter2);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN) {

            // COMPLETED (4) Override onMove and simply return false inside
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //do nothing, we only care about swiping
                return false;
            }

            // COMPLETED (5) Override onSwiped
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // COMPLETED (8) Inside, get the viewHolder's itemView's tag and store in a long variable id
                //get the id of the item being swiped
                Notify_deletion(viewHolder);
                // COMPLETED (10) call swapCursor on mAdapter passing in getAllGuests() as the argument
                //update the list
                adapter2.swapCursor(getAllpurch2());
            }

        }).attachToRecyclerView(recyclerView2);
        Go_to_material();
    }
    public void setActionBar(View view){
        slide=findViewById(R.id.slide);
        slide_ayout=findViewById(R.id.slide_Layout);
        back=view.findViewById(R.id.back);
        search=view.findViewById(R.id.search);
        editText=view.findViewById(R.id.edittext);
        linearLayout=view.findViewById(R.id.check1);
        linearLayout.setVisibility(View.GONE);
        textView=view.findViewById(R.id.text);
        linearLayout.setVisibility(View.GONE);
        slide_ayout.setVisibility(View.GONE);
        ok=view.findViewById(R.id.ok);
        textView.setText("TODAY PRODUCTION");
        editText.setHint("Search/Labour Name");
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
        slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check==0){
                    slide_ayout.setVisibility(View.VISIBLE);
                    slide.setImageResource(R.drawable.ic_chevron_right_black_24dp);
                    Toast.makeText(Production_Today.this,"Material Detail",Toast.LENGTH_SHORT).show();
                    check++;
                }
                else {
                    slide_ayout.setVisibility(View.GONE);
                    slide.setImageResource(R.drawable.ic_chevron_left_black_24dp);
                    check=0;
                }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_home:
                Intent intent=new Intent(this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void editextSuggetion(){
        ArrayList<String> Name=new ArrayList<String>();
        Cursor c=getAllpurch2();
        while (c.moveToNext()){
            String name=c.getString(c.getColumnIndex(Contracts.Production_Today.COLUMN_8));
            String date=c.getString(c.getColumnIndex(Contracts.Production_Today.COLUMN_5));
            String day=c.getString(c.getColumnIndex(Contracts.Production_Today.COLUMN_6));
            Name.add(name+"("+day+"_"+date+")");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.suggestion_textview, Name);
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
        Cursor c=getAllpurch2();
        long id=0;
        while (c.moveToNext()){
            String name=c.getString(c.getColumnIndex(Contracts.Production_Today.COLUMN_8));
            String date=c.getString(c.getColumnIndex(Contracts.Production_Today.COLUMN_5));
            String day=c.getString(c.getColumnIndex(Contracts.Production_Today.COLUMN_6));
            String s=name+"("+day+"_"+date+")";
            if(s.equals(d)){
                id=c.getInt(c.getColumnIndex(Contracts.Production_Today._ID));
            }
        }
        adapter2.swapCursor(getAllToday(id));
        if(editText.getText().toString().equals("")){
            adapter2.swapCursor(getAllpurch2());
        }
    }
    public void Today_Production(View view) {
        Set_All_Detail();
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(Production_Today.this);
        View mview = getLayoutInflater().inflate(R.layout.production_today_dialog, null);
        final EditText editText1 = mview.findViewById(R.id.Production_Today_Dozen);
        final EditText editText2_year = mview.findViewById(R.id.Year);
        final EditText editText2_month = mview.findViewById(R.id.Month);
        final EditText editText2_date = mview.findViewById(R.id.Date);
        final EditText editText3 = mview.findViewById(R.id.Labour_Name);
        final Spinner spinner = mview.findViewById(R.id.Production_Today_spiner);
        Button button1 = (Button) mview.findViewById(R.id.Production_Today_SAVE);
        Button button2 = (Button) mview.findViewById(R.id.Production_Today_EXIT);
        editText2_month.setText(month);
        editText2_year.setText(year);
        editText2_date.setText(da);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getAllpurch().getCount() > 0) {
                    String s1 = editText1.getText().toString();
                    String s2 = Float.toString(Float.parseFloat(s1) * Float.parseFloat(Dozen_Material_Cost) * 12);
                    String s3 = Double.toString(Double.parseDouble(Labour_Work) * Double.parseDouble(s1));
                    String s4 = Double.toString(Double.parseDouble(s2) + Double.parseDouble(s3));
                    String s5 = editText2_month.getText().toString() + "-" + editText2_date.getText().toString() + "-" + editText2_year.getText().toString();
                    String s6 = spinner.getSelectedItem().toString();
                    String s7 = editText3.getText().toString();
                    if (s1.trim().equals("") || s2.trim().equals("") || spinner.getSelectedItem().toString().equals("DAY")) {
                        Toast.makeText(Production_Today.this, "ENTER ALL DETAILS", Toast.LENGTH_SHORT).show();
                    } else {

                        if (addN(s1, s2, s3, s4, s5, s6, s7, id) == true) {
                            adapter2.swapCursor(getAllpurch2());
                            Toast.makeText(Production_Today.this, "Details add Successfully", Toast.LENGTH_SHORT).show();
                            //  editText.getText().clear();
                            editText1.getText().clear();
                            editText2_month.getText().clear();
                            editText2_year.getText().clear();
                            editText2_date.getText().clear();
                            //UpdateLabour_Work(s5,s1,s7);
                            Add_Materil_Date_Name(s5);
                            Add_Labour_Name_Date(s5,s7);
                            Add_Labour_Record_Recycle(s5,s7,s1,s,s3);
                            Material_Used_Add(s5,s1);
                            Add_Production_Record(s5,s1);
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(Production_Today.this, " ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    String s1 = editText1.getText().toString();
                    String s2 = "INFO_MISSING";
                    String s3 = "INFO_MISSING";
                    String s4 = "INFO_MISSING";
                    String s5 = editText2_month.getText().toString() + "-" + editText2_date.getText().toString() + "-" + editText2_year.getText().toString();
                    String s6 = spinner.getSelectedItem().toString();
                    String s7 = editText3.getText().toString();
                    if (s1.trim().equals("") || s2.trim().equals("") || spinner.getSelectedItem().toString().equals("DAY")) {
                        Toast.makeText(Production_Today.this, "ENTER ALL DETAILS", Toast.LENGTH_SHORT).show();
                    } else {
                        if (addN(s1, s2, s3, s4, s5, s6, s7, id) == true) {
                            adapter2.swapCursor(getAllpurch2());
                            Toast.makeText(Production_Today.this, "Details add Successfully", Toast.LENGTH_SHORT).show();
                            //  editText.getText().clear();
                            editText1.getText().clear();
                            editText2_month.getText().clear();
                            editText2_year.getText().clear();
                            editText2_date.getText().clear();
                            alertDialog.dismiss();
                            Add_Labour_Name_Date(s5,s7);
                        } else {
                            Toast.makeText(Production_Today.this, " ERROR", Toast.LENGTH_SHORT).show();

                        }
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
        alertDialog = mbuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    public boolean addN(String PRODUCTION, String TODAY_COST, String TODAY_LABOR_WORK, String TOTAL_AMOUNT, String DATE, String DAY, String Labour_Name, Long id) {
        SQLiteDatabase mDb = db_helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contracts.Production_Today.COLUMN_1, PRODUCTION);
        values.put(Contracts.Production_Today.COLUMN_2, TODAY_COST);
        values.put(Contracts.Production_Today.COLUMN_3, TODAY_LABOR_WORK);
        values.put(Contracts.Production_Today.COLUMN_4, TOTAL_AMOUNT);
        values.put(Contracts.Production_Today.COLUMN_5, DATE);
        values.put(Contracts.Production_Today.COLUMN_6, DAY);
        values.put(Contracts.Production_Today.COLUMN_7, id);
        values.put(Contracts.Production_Today.COLUMN_8, Labour_Name);
        long result = mDb.insert(Contracts.Production_Today.TABLE_NAME, null, values);
        if (result == -1) {
            return false;
        } else {

            return true;
        }
    }

    public Cursor getAllpurch() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Production_Meterials._ID, Contracts.Production_Meterials.COLUMN_1, Contracts.Production_Meterials.COLUMN_2, Contracts.Production_Meterials.COLUMN_3, Contracts.Production_Meterials.COLUMN_7};
        Cursor cr = mDb.query(Contracts.Production_Meterials.TABLE_NAME, columns, Contracts.Production_Meterials.COLUMN_6 + " = '" + id + "'", null, null, null, null);
        return cr;

    }

    public Cursor getAllpurch2() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Production_Today._ID, Contracts.Production_Today.COLUMN_1, Contracts.Production_Today.COLUMN_2, Contracts.Production_Today.COLUMN_3, Contracts.Production_Today.COLUMN_4, Contracts.Production_Today.COLUMN_5, Contracts.Production_Today.COLUMN_6, Contracts.Production_Today.COLUMN_7, Contracts.Production_Today.COLUMN_8};
        Cursor cr = mDb.query(Contracts.Production_Today.TABLE_NAME, columns, Contracts.Production_Today.COLUMN_7 + " = '" + id + "'", null, null, null, null);
        return cr;

    }
    public Cursor getAllToday(long id) {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Production_Today._ID, Contracts.Production_Today.COLUMN_1, Contracts.Production_Today.COLUMN_2, Contracts.Production_Today.COLUMN_3, Contracts.Production_Today.COLUMN_4, Contracts.Production_Today.COLUMN_5, Contracts.Production_Today.COLUMN_6, Contracts.Production_Today.COLUMN_7, Contracts.Production_Today.COLUMN_8};
        Cursor cr = mDb.query(Contracts.Production_Today.TABLE_NAME, columns, Contracts.Production_Today._ID + " = '" + id + "'", null, null, null, null);
        return cr;

    }
    public Cursor getAllTodayProduction(long id) {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Production_Today._ID, Contracts.Production_Today.COLUMN_1, Contracts.Production_Today.COLUMN_5};
        Cursor cr = mDb.query(Contracts.Production_Today.TABLE_NAME, columns, Contracts.Production_Today._ID + " = '" + id + "'", null, null, null, null);
        return cr;

    }

    public boolean update(String totalcost, String items, String Extra, String Amount, Long id) {
        SQLiteDatabase mDb = db_helper.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(Contracts.Purch_Amount_Table.COLUMN_1, totalcost);
        args.put(Contracts.Purch_Amount_Table.COLUMN_2, items);
        args.put(Contracts.Purch_Amount_Table.COLUMN_3, Extra);
        args.put(Contracts.Purch_Amount_Table.COLUMN_4, Amount);
        args.put(Contracts.Purch_Amount_Table.COLUMN_5, id);
        long result = mDb.update(Contracts.Purch_Amount_Table.TABLE_NAME, args, Contracts.Purch_Amount_Table.COLUMN_5 + "=" + id, null);
        if (result == -1) {
            return false;
        } else {

            return true;
        }
    }

    public void SetDate() {
        Date d = new Date();
        SimpleDateFormat mdyFormat = new SimpleDateFormat("MM-dd-yyyy");
        String date = mdyFormat.format(d);
        String[] items1 = date.split("-");
        month = items1[0];
        da = items1[1];
        year = items1[2];
    }

    public void Go_to_material() {
        ImageView imageView = findViewById(R.id.Go_to_Materilas);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Production_Today.this, Production_Material.class);
                intent.putExtra("NAME", s);
                String i = Long.toString(id);
                intent.putExtra("_ID", i);
                startActivity(intent);
            }
        });

    }
    private boolean removeGuest(long id) {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        // COMPLETED (2) Inside, call mDb.delete to pass in the TABLE_NAME and the condition that WaitlistEntry._ID equals id
        return mDb.delete(Contracts.Production_Today.TABLE_NAME, Contracts.Production_Today._ID + "=" + id, null) > 0;
    }
    private boolean removeLabour_Recycle(long id) {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        // COMPLETED (2) Inside, call mDb.delete to pass in the TABLE_NAME and the condition that WaitlistEntry._ID equals id
        return mDb.delete(Contracts.Labour_Record_Recycle.TABLE_NAME, Contracts.Labour_Record_Recycle._ID + "=" + id, null) > 0;
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
                //get Labour_Recrd_Recycle id
                long ID=0;
                Cursor c=getAllToday(id),c1=getAllLabour_Recycle();
                String Date="",LabName="",Work="";
                while (c.moveToNext()){
                    Work=c.getString(c.getColumnIndex(Contracts.Production_Today.COLUMN_1));
                    Date=c.getString(c.getColumnIndex(Contracts.Production_Today.COLUMN_5));
                    LabName=c.getString(c.getColumnIndex(Contracts.Production_Today.COLUMN_8));
                }
                while (c1.moveToNext()){
                    String D=c1.getString(c1.getColumnIndex(Contracts.Labour_Record_Recycle.COLUMN_1));
                    String N=c1.getString(c1.getColumnIndex(Contracts.Labour_Record_Recycle.COLUMN_2));
                    String PN=c1.getString(c1.getColumnIndex(Contracts.Labour_Record_Recycle.COLUMN_3));
                    String W=c1.getString(c1.getColumnIndex(Contracts.Labour_Record_Recycle.COLUMN_4));
                    if(D.equals(Date)&&N.equals(LabName)&&Work.equals(W)&&PN.equals(s)){
                        ID=c1.getInt(c1.getColumnIndex(Contracts.Labour_Record_Recycle._ID));
                    }
                }
                ProductionUpdate(Date,Work);
                removeLabour_Recycle(ID);
                MaterialDeleteUpdate(id);
                removeGuest(id);
                adapter2.swapCursor(getAllpurch2());
                dialog.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter2.swapCursor(getAllpurch2());
                dialog.dismiss();
            }
        });
    }

    public void MaterialDeleteUpdate(long id){
        Cursor c=getAllTodayProduction(id),c1=getAllpurch();
        String Production="0",Date="";
        while (c.moveToNext()){
            Production=c.getString(c.getColumnIndex(Contracts.Production_Today.COLUMN_1));
            Date=c.getString(c.getColumnIndex(Contracts.Production_Today.COLUMN_5));

        }
        while (c1.moveToNext()) {
            String Name = c1.getString(c1.getColumnIndex(Contracts.Production_Meterials.COLUMN_1));
            UpdateMaterials_Delete(Date,MaterialUsed(Name,Production),Name);

        }
        c.close();

    }
    public void UpdateMaterials_Delete(String Date,String Used,String M_Name) {
        long id = 0;
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
                System.out.println( "Previous record "+PreviousUsed+" and id "+id);
            }
        }
        String Remove_Scale[] = PreviousUsed.split(" ");
        PreviousUsed = Remove_Scale[0];
        //now adding material
        Used = Double.toString( Double.parseDouble(PreviousUsed)-Double.parseDouble(U));
        System.out.println( "After used material "+Used);
        SQLiteDatabase mDb = db_helper.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(Contracts.Material_record.COLUMN_4, Used+" "+Scale);
        long result=mDb.update(Contracts.Material_record.TABLE_NAME, args, Contracts.Material_record._ID + " = " + id, null);
        if(result==-1){
            System.out.println("UPDATEING ERROR");
        }else{
            System.out.println("UPDATEING");
        }
    }

    public void Set_All_Detail() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String s = "SELECT * from " + Contracts.Production_Meterials.TABLE_NAME + " WHERE " + Contracts.Production_Meterials.COLUMN_6 + " = " + id;
        String LabourWork = "", One_Doz = "";
        Cursor cr = mDb.rawQuery(s, new String[]{});
        while (cr.moveToNext()) {
            LabourWork = cr.getString(cr.getColumnIndex(Contracts.Production_Meterials.COLUMN_5));
            One_Doz = cr.getString(cr.getColumnIndex(Contracts.Production_Meterials.COLUMN_8));
        }
        if (getAllpurch().getCount() > 0) {
            Dozen_Material_Cost = One_Doz;
            Labour_Work = LabourWork;
        }
        cr.close();
    }
    public void Add_Materil_Date_Name(String Date) {
        Cursor c = getAllpurch();
        String split[]=Date.split("-");
        String m=split[0],y=split[2],date=m+"-"+y;
        while (c.moveToNext()) {
            String Name = c.getString(c.getColumnIndex(Contracts.Production_Meterials.COLUMN_1));
            if(check_Material_AllReady(date,Name)==0){
                SQLiteDatabase mDb = db_helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(Contracts.Material_record.COLUMN_1, date);
                values.put(Contracts.Material_record.COLUMN_2, Name);
                values.put(Contracts.Material_record.COLUMN_3, "0");
                values.put(Contracts.Material_record.COLUMN_4, "0");
                values.put(Contracts.Material_record.COLUMN_5, "0");
                values.put(Contracts.Material_record.COLUMN_6, "0");
                mDb.insert(Contracts.Material_record.TABLE_NAME, null, values);
            }
        }
        c.close();

    }
    public void Add_Labour_Name_Date(String Date,String Name) {
        String split[]=Date.split("-");
        String m=split[0],y=split[2],date=m+"-"+y;
            if(check_Labour_Allredy(date,Name)==0){
                SQLiteDatabase mDb = db_helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(Contracts.Labour_Record.COLUMN_1, date);
                values.put(Contracts.Labour_Record.COLUMN_2, Name);
                values.put(Contracts.Labour_Record.COLUMN_3, "");
                values.put(Contracts.Labour_Record.COLUMN_4, "0");
                values.put(Contracts.Labour_Record.COLUMN_5, "");
                values.put(Contracts.Labour_Record.COLUMN_6, "");
                mDb.insert(Contracts.Labour_Record.TABLE_NAME, null, values);
            }
    }
    public void Add_Labour_Record_Recycle(String Date,String Name,String Work,String ProductName,String Amount) {
        String spilt[]=Date.split("-");
        String m=spilt[0],day=spilt[1],y=spilt[2],date=m+"-"+y;
        if(check_Labour(date,Name,Work,ProductName)==0){
            SQLiteDatabase mDb = db_helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            String spilt1[]=date.split("-");
            String M=spilt1[0],Y=spilt1[1],D=M+"-"+day+"-"+Y;
            values.put(Contracts.Labour_Record_Recycle.COLUMN_1, D);
            values.put(Contracts.Labour_Record_Recycle.COLUMN_2, Name);
            values.put(Contracts.Labour_Record_Recycle.COLUMN_3, ProductName);
            values.put(Contracts.Labour_Record_Recycle.COLUMN_4, Work);
            values.put(Contracts.Labour_Record_Recycle.COLUMN_5, Amount);
            values.put(Contracts.Labour_Record_Recycle.COLUMN_6, "0");
            mDb.insert(Contracts.Labour_Record_Recycle.TABLE_NAME, null, values);
        }
    }
    public void Add_Production_Record(String Date,String Work) {
        String spilt[]=Date.split("-");
        String m=spilt[0],day=spilt[1],y=spilt[2],date=m+"-"+y,OldWork="";
        int id=check_Production_allredy(date,s);
        System.out.println("kkkkkkkkkkkkkkk"+id);
        if(id==0){
            SQLiteDatabase mDb = db_helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            String spilt1[]=date.split("-");
            String M=spilt1[0],Y=spilt1[1],D=M+"-"+day+"-"+Y;
            values.put(Contracts.Production_Record.COLUMN_1, date);
            values.put(Contracts.Production_Record.COLUMN_2, s);
            values.put(Contracts.Production_Record.COLUMN_3, Work);
            values.put(Contracts.Production_Record.COLUMN_4, "");
            values.put(Contracts.Production_Record.COLUMN_5, "");
            values.put(Contracts.Production_Record.COLUMN_6, "0");
            mDb.insert(Contracts.Production_Record.TABLE_NAME, null, values);
        }
        else{
            SQLiteDatabase mDb = db_helper.getReadableDatabase();
            String[] columns = new String[]{Contracts.Production_Record._ID,Contracts.Production_Record.COLUMN_3};
            Cursor cr = mDb.query(Contracts.Production_Record.TABLE_NAME, columns, Contracts.Production_Record._ID+" = "+id, null, null, null, null);
            while (cr.moveToNext()){
                OldWork=cr.getString(cr.getColumnIndex(Contracts.Production_Record.COLUMN_3));
            }
            OldWork=Double.toString(Double.parseDouble(OldWork)+Double.parseDouble(Work));
            ContentValues args = new ContentValues();
            args.put(Contracts.Production_Record.COLUMN_3, OldWork);
            mDb.update(Contracts.Production_Record.TABLE_NAME, args, Contracts.Production_Record._ID + "=" + id, null);
        }
    }
    public void ProductionUpdate(String Date,String Production){
        String spilt[]=Date.split("-");
        String m=spilt[0],day=spilt[1],y=spilt[2],date=m+"-"+y,OldProduction="";
        int ID=check_Production_allredy(date,s);
        Cursor c=getAllProduction_Record_For_Update(ID);
        while (c.moveToNext()){
           OldProduction=c.getString(c.getColumnIndex(Contracts.Production_Record.COLUMN_3));
        }
        if(OldProduction.equals("")){
            OldProduction="0.0";}
        OldProduction=Double.toString(Double.parseDouble(OldProduction)-Double.parseDouble(Production));
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        ContentValues args = new ContentValues();
        args.put(Contracts.Production_Record.COLUMN_3, OldProduction);
        mDb.update(Contracts.Production_Record.TABLE_NAME, args, Contracts.Production_Record._ID + "=" + ID, null);
    }
    public Cursor getAllProduction_Record_For_Update(long id) {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Production_Record._ID,Contracts.Production_Record.COLUMN_3};
        Cursor cr = mDb.query(Contracts.Production_Record.TABLE_NAME, columns, Contracts.Production_Record._ID+" = "+id, null, null, null, null);
        return cr;

    }
    public void Material_Used_Add(String Date,String Production) {

        Cursor c = getAllpurch();
        while (c.moveToNext()) {
            String Name = c.getString(c.getColumnIndex(Contracts.Production_Meterials.COLUMN_1));
            UpdateMaterials(Date,MaterialUsed(Name,Production),Name);}
        c.close();
    }
    public void UpdateMaterials(String Date,String Used,String M_Name) {
        long id = 0;
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
                    System.out.println( "Previous record "+PreviousUsed+" and id "+id);
                }
            }
            String Remove_Scale[] = PreviousUsed.split(" ");
            PreviousUsed = Remove_Scale[0];
            //now adding material
            Used = Double.toString(Double.parseDouble(U) + Double.parseDouble(PreviousUsed));
            System.out.println( "After used material "+Used);
            SQLiteDatabase mDb = db_helper.getWritableDatabase();
            ContentValues args = new ContentValues();
            args.put(Contracts.Material_record.COLUMN_4, Used+" "+Scale);
            long result=mDb.update(Contracts.Material_record.TABLE_NAME, args, Contracts.Material_record._ID + " = " + id, null);
            if(result==-1){
                System.out.println("UPDATEING ERROR");
            }else{
                System.out.println("UPDATEING");
            }
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
    public Cursor getAllMaterial_Record() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Material_record._ID, Contracts.Material_record.COLUMN_1,Contracts.Material_record.COLUMN_2,Contracts.Material_record.COLUMN_4};
        Cursor cr = mDb.query(Contracts.Material_record.TABLE_NAME, columns, null, null, null, null, null);
        return cr;

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
    public Cursor getAllProduction_Record() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Production_Record._ID, Contracts.Production_Record.COLUMN_1,Contracts.Production_Record.COLUMN_2,Contracts.Production_Record.COLUMN_3,Contracts.Production_Record.COLUMN_4,Contracts.Production_Record.COLUMN_5};
        Cursor cr = mDb.query(Contracts.Production_Record.TABLE_NAME, columns, null, null, null, null, null);
        return cr;

    }

    public int check_Material_AllReady (String Date,String N){
        List<String> list,name;
        list = new ArrayList<String>();
        name = new ArrayList<String>();
        int check=0;
        Cursor c=getAllMaterial_Record();
        if(c.getCount()!=0) {
            while (c.moveToNext()) {
                String date = c.getString(c.getColumnIndex(Contracts.Material_record.COLUMN_1));
                String Name = c.getString(c.getColumnIndex(Contracts.Material_record.COLUMN_2));
                list.add(date);
                name.add(Name);
            }
            for (int i = 0; i < list.size(); i++) {
                if (Date.equals(list.get(i))&&N.equals(name.get(i))) {
                    check = 1;
                    return check;
                }
            }
        }
        c.close();
        return check;
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
    public int check_Labour_Allredy (String s,String n){
        List<String> list,name;
        list = new ArrayList<String>();
        name = new ArrayList<String>();
        int check=0;
        Cursor c=getAllLabour_Record();
        if(c.getCount()!=0) {
            while (c.moveToNext()) {
                String date = c.getString(c.getColumnIndex(Contracts.Labour_Record.COLUMN_1));
                String Name = c.getString(c.getColumnIndex(Contracts.Labour_Record.COLUMN_2));
                list.add(date);
                name.add(Name);
            }
            for (int i = 0; i < list.size(); i++) {
                if (s.equals(list.get(i))&&n.equals(name.get(i))) {
                    check = 1;
                    return check;
                }
            }
        }
        return check;
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
    public int check_Labour (String Date,String Name,String Production,String Product){
        List<String> d,n,pn,w;
        d = new ArrayList<String>();
        n = new ArrayList<String>();
        pn = new ArrayList<String>();
        w = new ArrayList<String>();
        int check=0;
        Cursor c=getAllLabour_Recycle();
        if(c.getCount()!=0) {
            while (c.moveToNext()) {
                String date = c.getString(c.getColumnIndex(Contracts.Labour_Record_Recycle.COLUMN_1));
                String name = c.getString(c.getColumnIndex(Contracts.Labour_Record_Recycle.COLUMN_2));
                String product_name = c.getString(c.getColumnIndex(Contracts.Labour_Record_Recycle.COLUMN_3));
                String work = c.getString(c.getColumnIndex(Contracts.Labour_Record_Recycle.COLUMN_4));
                d.add(date);
                n.add(name);
                pn.add(product_name);
                w.add(work);
            }
            for (int i = 0; i < d.size(); i++) {
                if (Date.equals(d.get(i))&&Name.equals(n.get(i))&&Product.equals(pn.get(i))&&Production.equals(w.get(i))) {
                    check = 1;
                    return check;
                }
            }
        }
        return check;
    }

}

