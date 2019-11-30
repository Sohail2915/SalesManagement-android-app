package com.kk.kamranqadeer.salemanager;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.kamranqadeer.salemanager.AllAdapters.ProductionAdapter;
import com.kk.kamranqadeer.salemanager.AllAdapters.Purchasing_Adapter;
import com.kk.kamranqadeer.salemanager.AllAdapters.SaleAdapter1;
import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Production extends AppCompatActivity {
    DbHelper db;
    RecyclerView recyclerView;
    AlertDialog alertDialog ;
    String d;
    ImageView back;
    private ProductionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_simple_action_bar);
        View view =getSupportActionBar().getCustomView();
        back=view.findViewById(R.id.back);
        TextView tv=view.findViewById(R.id.text);
        tv.setText("PRODUCTION");
        SetDate();
        db=new DbHelper(this);
        recyclerView=(RecyclerView)findViewById(R.id.ProductionRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Cursor cursor =getAllProduction();
        // Toast.makeText(this, ""+cursor.getCount(), Toast.LENGTH_SHORT).show();
        adapter=new ProductionAdapter(this,cursor);
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
                adapter.swapCursor(getAllProduction());
            }

            //COMPLETED (11) attach the ItemTouchHelper to the waitlistRecyclerView
        }).attachToRecyclerView(recyclerView);
        //back action
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void Add_Production(View view) {
        AlertDialog.Builder mbuilder=new AlertDialog.Builder(Production.this);
        View mview=getLayoutInflater().inflate(R.layout.production_dialog,null);
        final EditText editText1=(EditText) mview.findViewById(R.id.Production_Name);
        final EditText editText2=(EditText) mview.findViewById(R.id.Production_qun);
        final EditText editText4=(EditText) mview.findViewById(R.id.P_Date_Time);
        final Spinner spinner1=(Spinner) mview.findViewById(R.id.Production_spiner_ml);
        final Spinner spinner2=(Spinner) mview.findViewById(R.id.Production_spinner_grade);
        Button button1=(Button) mview.findViewById(R.id.Production_SAVE);
        Button button2=(Button) mview.findViewById(R.id.Production_EXIT);
        editText4.setText(d);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1=editText1.getText().toString();
                String s2=editText2.getText().toString()+spinner1.getSelectedItem().toString();
                String s3=spinner2.getSelectedItem().toString();
                String s4=Long.toString(getAllProduction().getCount()+1);
                String s5=editText4.getText().toString();
                if(check_Product(s2)==1) {
                    Toast.makeText(Production.this, "THIS PRODUCT ALL RADY EXIST", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (s1.trim().equals("") || s3.trim().equals("QUALITY") || s2.trim().equals("Scale")) {
                        Toast.makeText(Production.this, "ENTER ALL DETAILS", Toast.LENGTH_SHORT).show();
                    } else {
                        if (add(s1, s2, s3, s4,s5) == true) {

                            adapter.swapCursor(getAllProduction());
                            Toast.makeText(Production.this, "PRODUCT add Successfully", Toast.LENGTH_SHORT).show();
                            editText1.getText().clear();
                            editText2.getText().clear();
                            editText4.getText().clear();
                            Dialog_Dismis();
                            making_folder();

                        } else {
                            Toast.makeText(Production.this, "ERROR", Toast.LENGTH_SHORT).show();

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
        alertDialog=mbuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
    public void Dialog_Dismis()
    {
        alertDialog.dismiss();
    }
    public boolean add(String Name,String Scale,String Grade,String Code,String date_time)
    {
        SQLiteDatabase mDb=db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contracts.Production.COLUMN_1,Name);
        values.put(Contracts.Production.COLUMN_2,Scale);
        values.put(Contracts.Production.COLUMN_3,Grade);
        values.put(Contracts.Production.COLUMN_4,Code);
        values.put(Contracts.Production.COLUMN_5,date_time);
        long result= mDb.insert(Contracts.Production.TABLE_NAME, null, values);
        if (result==-1)
        {
            return  false;
        }
        else {

            return true;
        }
    }
    private Cursor getAllProduction() {
        SQLiteDatabase  mDb=db.getReadableDatabase();
        Cursor cr=mDb.rawQuery("select * from "+ Contracts.Production.TABLE_NAME,null);
        //  cr = db.query(Contract.TableDetail.TABLE_NAME, null, null, null, null,null, null);
        return cr;
    }
    private boolean removeGuest(long id) {
        SQLiteDatabase  mDb=db.getReadableDatabase();
        // COMPLETED (2) Inside, call mDb.delete to pass in the TABLE_NAME and the condition that WaitlistEntry._ID equals id
        return mDb.delete(Contracts.Production.TABLE_NAME, Contracts.Production._ID + "=" + id, null) > 0;
    }
    private boolean removeSale(long id) {
        SQLiteDatabase  mDb=db.getReadableDatabase();
        // COMPLETED (2) Inside, call mDb.delete to pass in the TABLE_NAME and the condition that WaitlistEntry._ID equals id
        return mDb.delete(Contracts.Sale_ProduDeteil_Table.TABLE_NAME, Contracts.Sale_ProduDeteil_Table.COLUMN_6 + "=" + id, null) > 0;
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
    public boolean All_production_material_Delet(Long i)
    {
        SQLiteDatabase mDb = db.getReadableDatabase();
        return mDb.delete(Contracts.Production_Meterials.TABLE_NAME, Contracts.Production_Meterials.COLUMN_6 + "=" + i, null) > 0;
    }
    public boolean All_production_Delet(Long i)
    {
        SQLiteDatabase mDb = db.getReadableDatabase();
        return mDb.delete(Contracts.Production_Today.TABLE_NAME, Contracts.Production_Today.COLUMN_7 + "=" + i, null) > 0;
    }
    public void SetDate()
    {
        Date date=new Date();
        SimpleDateFormat mdyFormat = new SimpleDateFormat("MM-dd-yyyy");
        d = mdyFormat.format(date);
    }
    public boolean All_today_Delete(Long i)
    {
        SQLiteDatabase mDb = db.getReadableDatabase();
        return mDb.delete(Contracts.Production_Today.TABLE_NAME, Contracts.Production_Today.COLUMN_7 + "=" + i, null) > 0;
    }
    public void Notify_deletion(final RecyclerView.ViewHolder viewHolder) {
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.delete_dialog);
        Button button1 = (Button) dialog.findViewById(R.id.ok);
        Button button2 = (Button) dialog.findViewById(R.id.no);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = (long) viewHolder.itemView.getTag();
                // COMPLETED (9) call removeGuest and pass through that id
                //remove from DB
                removeGuest(id);
                adapter.swapCursor(getAllProduction());
                removeSale(id);
                All_production_material_Delet(id);
                All_today_Delete(id);
                All_production_Delet(id);
                dialog.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.swapCursor(getAllProduction());
                dialog.dismiss();
            }
        });
    }
    public int check_Product (String s){
        List<String> list;
        list = new ArrayList<String>();
        int check=0;
        Cursor c=getAllProduction();
        while (c.moveToNext()){
            String Purch_Name = c.getString(c.getColumnIndex(Contracts.Production.COLUMN_1));
            String Purch_Name1 = c.getString(c.getColumnIndex(Contracts.Production.COLUMN_2));
            list.add(Purch_Name+Purch_Name1);
        }
        for(int i=0;i<list.size();i++){
            if(s.equals(list.get(i))){
                check=1;
                return check;
            }
        }
        return check;
    }
    public void making_folder(){
        File folder = new File(Environment.getExternalStorageDirectory() + "/SaleManager");
        File folder1 = new File(Environment.getExternalStorageDirectory() + "/SaleManager/Reports");
        File folder3 = new File(Environment.getExternalStorageDirectory() + "/SaleManager/BackUp");

        if (!folder.exists()) {
            Toast.makeText(Production.this, "Directory Does Not Exist, Create It", Toast.LENGTH_SHORT).show();
            folder.mkdir();
            folder3.mkdir();
            folder1.mkdir();
        }

    }
}
