package com.kk.kamranqadeer.salemanager;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.kamranqadeer.salemanager.AllAdapters.Purchasing_material_adapter;
import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class All_Raw_Materials extends AppCompatActivity {
    private Purchasing_material_adapter adapter1;
    DbHelper db;
    RecyclerView recyclerView1;
    AlertDialog alertDialog;
    Long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__raw__materials);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ALL MATERIALS");
        db=new DbHelper(this);
        Cursor cursor1 =getAllPurch1();
        recyclerView1=(RecyclerView)findViewById(R.id.Purchising_material_RecyclerView);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        adapter1=new Purchasing_material_adapter(this,cursor1);
        recyclerView1.setAdapter(adapter1);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView1, RecyclerView.ViewHolder viewHolder1, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder1, int direction) {
                // COMPLETED (8) Inside, get the viewHolder's itemView's tag and store in a long variable id
                //get the id of the item being swiped
                Notify_deletion1(viewHolder1);
                // COMPLETED (10) call swapCursor on mAdapter passing in getAllGuests() as the argument
                //update the list
                adapter1.swapCursor(getAllPurch1());
            }
        }).attachToRecyclerView(recyclerView1);
        getAllPurch1();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }
    private boolean removeGuest(long id) {
        SQLiteDatabase  mDb=db.getReadableDatabase();
        // COMPLETED (2) Inside, call mDb.delete to pass in the TABLE_NAME and the condition that WaitlistEntry._ID equals id
        return mDb.delete(Contracts.Sale_Table.TABLE_NAME, Contracts.Sale_Table._ID + "=" + id, null) > 0;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            case R.id.action_home: {
                Intent intent=new Intent(this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private Cursor getAllPurch1() {
        SQLiteDatabase mDb=db.getReadableDatabase();
        Cursor cr=mDb.rawQuery("select * from "+ Contracts.Sale_Table.TABLE_NAME,null);
        //  cr = db.query(Contract.TableDetail.TABLE_NAME, null, null, null, null,null, null);
        return cr;
    }
    public boolean add1(String Name,String Scale,String Amount)
    {
        SQLiteDatabase mDb=db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contracts.Sale_Table.COLUMN_1,Name);
        values.put(Contracts.Sale_Table.COLUMN_2,Scale);
        values.put(Contracts.Sale_Table.COLUMN_3,Amount);
        long result= mDb.insert(Contracts.Sale_Table.TABLE_NAME, null, values);
        if (result==-1)
        {
            return  false;
        }
        else {

            return true;
        }
    }
    public void Notify_deletion1(final RecyclerView.ViewHolder viewHolder) {
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
                adapter1.swapCursor(getAllPurch1());
                dialog.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter1.swapCursor(getAllPurch1());
                dialog.dismiss();
            }
        });
    }
    public void ADD_Raw_Material(View view) {
        AlertDialog.Builder mbuilder=new AlertDialog.Builder(All_Raw_Materials.this);
        View mview=getLayoutInflater().inflate(R.layout.all_material_dialog,null);
        final EditText editText1 = mview.findViewById(R.id.Materilas);
        final EditText editText2 = mview.findViewById(R.id.amount);
        final Spinner spinner=mview.findViewById(R.id.spinner);
        final TextView textView=mview.findViewById(R.id.scale);
        Button button1 = (Button) mview.findViewById(R.id.save);
        Button button2 = (Button) mview.findViewById(R.id.exit);
        editText2.setClickable(false);
        editText2.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                textView.setText(spinner.getSelectedItem().toString());
                return false;
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1 = editText1.getText().toString();
                String s2 = editText2.getText().toString();
                String s3 = spinner.getSelectedItem().toString();

                if(check_Material_AllReady(editText1.getText().toString())==1){
                    Toast.makeText(All_Raw_Materials.this, "THIS MATERIL ALL RADY EXIST", Toast.LENGTH_SHORT).show();
                    editText1.getText().clear();}
                    else {
                    if (s1.trim().equals("") || s2.trim().equals("")) {
                        Toast.makeText(All_Raw_Materials.this, "ENTER ALL DETAILS", Toast.LENGTH_SHORT).show();
                    } else {
                        if (add1(s1, s2, s3) == true) {
                            adapter1.swapCursor(getAllPurch1());
                            Toast.makeText(All_Raw_Materials.this, "Details add Successfully", Toast.LENGTH_SHORT).show();
                            //  editText.getText().clear();
                            editText1.getText().clear();
                            editText2.getText().clear();
                            Dialog_Dismis();
                        } else {
                            Toast.makeText(All_Raw_Materials.this, " ERROR", Toast.LENGTH_SHORT).show();

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
        alertDialog=mbuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }
    public void Dialog_Dismis() {
        alertDialog.dismiss();
    }
    public int check_Material_AllReady (String s){
        List<String> list;
        list = new ArrayList<String>();
        int check=0;
        Cursor c=getAllPurch1();
        while (c.moveToNext()){
            String Purch_Name = c.getString(c.getColumnIndex(Contracts.Sale_Table.COLUMN_1));
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
    public void addMaterials(String Date,String Name,String Purchasing,String Used,String Left,Long Idfk)
    {
        SQLiteDatabase mDb=db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contracts.Material_record.COLUMN_1,Date);
        values.put(Contracts.Material_record.COLUMN_2,Name);
        values.put(Contracts.Material_record.COLUMN_3,Purchasing);
        values.put(Contracts.Material_record.COLUMN_4,Used);
        values.put(Contracts.Material_record.COLUMN_5,Left);
        values.put(Contracts.Material_record.COLUMN_6,Idfk);
        mDb.insert(Contracts.Material_record.TABLE_NAME, null, values);
    }
}
