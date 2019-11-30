package com.kk.kamranqadeer.salemanager;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.kk.kamranqadeer.salemanager.AllAdapters.ProductionMaterialAdapter;
import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Production_Material extends AppCompatActivity {
    Long id ;
    Double kg,am,One_Dozen_Amount=0.0;
    DbHelper db_helper;
    RecyclerView recyclerView;
    ProductionMaterialAdapter adapter;
    AlertDialog alertDialog;
    String LabourWork = "null",TotalDoz="0",Am="",Sc="";
    Cursor cursor;
    ArrayList<String> list,Amount,Scale,list1;
    ImageView back,info;
    LinearLayout linearLayout;
    int check=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production__material);
        db_helper = new DbHelper(this);
        getSupportActionBar().setTitle("USED MATERILS");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_production_material_bar);
        View view =getSupportActionBar().getCustomView();
        back=view.findViewById(R.id.back);
        info=view.findViewById(R.id.info);
        TextView tv=view.findViewById(R.id.text);
        tv.setText("USED MATERIALS");
        Intent intent = getIntent();
        TextView textView=findViewById(R.id.Product_id);
        linearLayout=findViewById(R.id.check1);
        linearLayout.setVisibility(View.GONE);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check==0){
                linearLayout.setVisibility(View.VISIBLE);
                check=1;}
                else{
                    linearLayout.setVisibility(View.GONE);
                    check=0;
                }
            }
        });
        if (intent.hasExtra("NAME")) {
            String  s = intent.getStringExtra("NAME");
            textView.setText(s);

        }
        if (intent.hasExtra("_ID")) {
            String a = intent.getStringExtra("_ID");
            id = Long.parseLong(a);
        }
        recyclerView = (RecyclerView) findViewById(R.id.Productuion_Material_Recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Toast.makeText(this, "id "+id, Toast.LENGTH_SHORT).show();
        cursor = getAllpurch();
        // Toast.makeText(this, ""+cursor.getCount(), Toast.LENGTH_SHORT).show();
        adapter = new ProductionMaterialAdapter(this, cursor);
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {

            // COMPLETED (4) Override onMove and simply return false inside
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //do nothing, we only care about swiping
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // COMPLETED (8) Inside, get the viewHolder's itemView's tag and store in a long variable id
                //get the id of the item being swiped
                 //viewHolder=(RecyclerView.ViewHolder)recyclerView.getTag();
                   Notify_deletion(viewHolder);
                    // COMPLETED (10) call swapCursor on mAdapter passing in getAllGuests() as the argument
                    //update the list
                adapter.swapCursor(getAllpurch());

            }
        }).attachToRecyclerView(recyclerView);
        //back action
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        check_Doz_Labourwork();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.production_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void New_Materials(View view) {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(Production_Material.this);
        View mview = getLayoutInflater().inflate(R.layout.produ_mater_dialog, null);
        Set_Purchasing_name();
        final EditText editText3 = findViewById(R.id.Production_Labour_Work);
        final EditText editText4 = findViewById(R.id.ExtraAmount);
        final Spinner dozen =  findViewById(R.id.Production_Doz_Spiner);
        TotalDoz=dozen.getSelectedItem().toString();
        LabourWork=editText3.getText().toString();
        if(TotalDoz.equals("0")||LabourWork.equals("")){
            Toast.makeText(Production_Material.this, "ENTER DOZEN AND ALBOUR WORK", Toast.LENGTH_SHORT).show();
        }
        else {
            //first set spinner ALL purchasing products;
            final Spinner All_Name =mview.findViewById(R.id.All_Name);
            final Spinner Sca =mview.findViewById(R.id.Production_Material_spinerML);
            list1 = new ArrayList<String>();
            //set Scale
            list1.add("ml");
            list1.add("kg");
            list1.add("gm");
            list1.add("pice");
            list1.add("can");
            list1.add("dozen");
            ArrayAdapter<String> adapt,adapt1;
            //NAME SPINNER IS SET
            adapt = new ArrayAdapter<String>(getApplicationContext(),
            android.R.layout.simple_spinner_dropdown_item, list);
            adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            All_Name.setAdapter(adapt);
            //SCALE SPINER IS SET
            adapt1 = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_spinner_dropdown_item, list1);
            adapt1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Sca.setAdapter(adapt1);
            final TextView textView=mview.findViewById(R.id.detail);
            //SPINNER 1 SELECT ITEM LISTNER
            All_Name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String s="";
                    if(All_Name.getSelectedItem().toString()!="Select") {
                        for (int i = 0; i < list.size(); i++) {
                            if (All_Name.getSelectedItem().toString().equals(list.get(i))) {
                                 Am= Amount.get(i);
                                 Sc= Scale.get(i);
                                break;
                            }
                        }
                        //NOW SET SACLE SPINNER BASED ON MATERIAL NAME
                        if(Sc.equals("Liter")){
                            list1.clear();
                            list1.add("ml");
                            list1.add("Liter");
                            setSpinnerScale(Sca);

                        }
                        else if(Sc.equals("kg")){
                            list1.clear();
                            list1.add("gm");
                            list1.add("kg");
                            setSpinnerScale(Sca);
                        }
                        else if(Sc.equals("dozen")){
                            list1.clear();
                            list1.add("dozen");
                            setSpinnerScale(Sca);
                        }
                        else if(Sc.equals("can")){
                            list1.clear();
                            list1.add("can");
                            setSpinnerScale(Sca);
                        }
                        else if(Sc.equals("pice")){
                            list1.clear();
                            list1.add("pice");
                            setSpinnerScale(Sca);
                        }
                    }
                    else {
                            list1.clear();
                        list1.add("kg");
                        list1.add("gm");
                        list1.add("Liter");
                        list1.add("ml");
                        list1.add("pice");
                        list1.add("can");
                        list1.add("dozen");
                        setSpinnerScale(Sca);
                    }
                    textView.setText(Am+" RS ONE "+Sc);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            final EditText editText1 = mview.findViewById(R.id.COST);
            editText1.setEnabled(false);
            final EditText editText2 = mview.findViewById(R.id.Production_Material_qun);
            final Spinner spinner = mview.findViewById(R.id.Production_Material_spinerML);
            Button button1 = (Button) mview.findViewById(R.id.Production_Material_SAVE);
            Button button2 = (Button) mview.findViewById(R.id.Production_Material_EXIT);
            //EDITTEXT LISTNER
            editText2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String no=s.toString();
                    if (editText2.equals("QUNTITY")) {
                    } else {
                        if(no.equals("")){
                            editText1.setText(Am);
                        }
                        else {
                            if (Sca.getSelectedItem().toString().equals("gm")) {
                                kg = Double.parseDouble(no) / 1000;
                                am = Double.parseDouble(Am) * kg;
                                editText1.setText(Double.toString(am));

                            }
                            else if (Sca.getSelectedItem().toString().equals("ml")) {
                                kg = Double.parseDouble(no) / 1000;
                                am = Double.parseDouble(Am) * kg;
                                editText1.setText(Double.toString(am));

                            }else {
                                am = Double.parseDouble(no) * Double.parseDouble(Am);
                                editText1.setText(Double.toString(am));
                            }
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            //SCAL SPINNER ACTION LISTNOR
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (editText2.getText().toString().equals("") || editText2.getText().toString().equals("QUNTITY")) {
                        editText1.setText(Am);
                    } else {

                            if (spinner.getSelectedItem().toString().equals("gm")) {
                                kg = Double.parseDouble(editText2.getText().toString()) / 1000;
                                am = Double.parseDouble(Am) * kg;
                                editText1.setText(Double.toString(am));

                            } else if (spinner.getSelectedItem().toString().equals("ml")) {
                                kg = Double.parseDouble(editText2.getText().toString()) / 1000;
                                am = Double.parseDouble(Am) * kg;
                                editText1.setText(Double.toString(am));

                            } else {
                                kg = Double.parseDouble(editText2.getText().toString());
                                am = Double.parseDouble(Am) * kg;
                                editText1.setText(Double.toString(am));
                            }

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final TextView textView2=findViewById(R.id.detail_2);
                    String s1 = All_Name.getSelectedItem().toString();
                    String s2 = editText2.getText().toString();
                    String s3 = spinner.getSelectedItem().toString();
                    String s4 = editText1.getText().toString();
                    String s5 =editText3.getText().toString();
                    String s6=dozen.getSelectedItem().toString();
                    String s7=Double.toString(One_Dozen_Amount);
                    String s8=editText4.getText().toString();
                    if(check_Material_AllReady(All_Name.getSelectedItem().toString())==1){
                        Toast.makeText(Production_Material.this, "THIS MATERIL ALL RADY EXIST", Toast.LENGTH_SHORT).show();
                        editText1.getText().clear();
                        editText2.getText().clear();
                        editText3.getText().clear();
                        editText4.getText().clear();}
                    else {
                        if (s2.trim().equals("")) {
                            Toast.makeText(Production_Material.this, "ENTER ALL DETAILS", Toast.LENGTH_SHORT).show();
                        } else {
                            if (addN(s1, s2, s3, s4, s5, s6,s7,s8, id) == true) {
                                adapter.swapCursor(getAllpurch());
                                Toast.makeText(Production_Material.this, "Details add Successfully", Toast.LENGTH_SHORT).show();
                                editText1.getText().clear();
                                editText2.getText().clear();
                                editText3.getText().clear();
                                Dialog_Dismis();
                                check_Doz_Labourwork();
                            } else {
                                Toast.makeText(Production_Material.this, " ERROR", Toast.LENGTH_SHORT).show();

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
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    public void Dialog_Dismis() {
        alertDialog.dismiss();
    }

    public boolean addN(String name, String used, String scale, String amount, String labourWork, String Total_Doz,String One_Doz_Amount,String Extra_Amount,Long id) {
        SQLiteDatabase mDb = db_helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contracts.Production_Meterials.COLUMN_1, name);
        values.put(Contracts.Production_Meterials.COLUMN_2, used);
        values.put(Contracts.Production_Meterials.COLUMN_3, scale);
        values.put(Contracts.Production_Meterials.COLUMN_4, amount);
        values.put(Contracts.Production_Meterials.COLUMN_5, labourWork);
        values.put(Contracts.Production_Meterials.COLUMN_6, id);
        values.put(Contracts.Production_Meterials.COLUMN_7, Total_Doz);
        values.put(Contracts.Production_Meterials.COLUMN_8, One_Doz_Amount);
        values.put(Contracts.Production_Meterials.COLUMN_9, Extra_Amount);
        long result = mDb.insert(Contracts.Production_Meterials.TABLE_NAME, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAllpurch() {
        db_helper = new DbHelper(this);
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Production_Meterials._ID, Contracts.Production_Meterials.COLUMN_1, Contracts.Production_Meterials.COLUMN_2, Contracts.Production_Meterials.COLUMN_3, Contracts.Production_Meterials.COLUMN_4, Contracts.Production_Meterials.COLUMN_5,Contracts.Production_Meterials.COLUMN_6,Contracts.Production_Meterials.COLUMN_7,Contracts.Production_Meterials.COLUMN_8,Contracts.Production_Meterials.COLUMN_9};
        Cursor cr = mDb.query(Contracts.Production_Meterials.TABLE_NAME, columns, Contracts.Production_Meterials.COLUMN_6 + " = '" + id + "'", null, null, null, null);
        return cr;
    }

    private boolean removeGuest(long id) {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        // COMPLETED (2) Inside, call mDb.delete to pass in the TABLE_NAME and the condition that WaitlistEntry._ID equals id
        return mDb.delete(Contracts.Production_Meterials.TABLE_NAME, Contracts.Production_Meterials._ID + "=" + id, null) > 0;
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

    public void SelectDoz(Spinner spinner) {
        ArrayAdapter<String> adapter;
        List<String> list;
        String string = null;
        list = new ArrayList<String>();
        for (int i = 0; i <= 200; i++) {
            string = Integer.toString(i);
            list.add(string);
        }
        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                home_action();
                return true;
            case R.id.action_production:
                production_action();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void home_action(){
        Intent intent=new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void production_action(){
        Intent intent=new Intent(this,Production.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
                adapter.swapCursor(getAllpurch());
                check_Doz_Labourwork();
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
    public void check_Doz_Labourwork(){
        SQLiteDatabase mDb1 = db_helper.getReadableDatabase();
        int i=0;
        String s = "SELECT * from " + Contracts.Production_Meterials.TABLE_NAME + " WHERE " + Contracts.Production_Meterials.COLUMN_6 + " = " +id;
        final Cursor cur = mDb1.rawQuery(s, new String[]{});
        String s1="",s2="",Amount,scale="",s3="";
        Double TotalAmount=0.0,LaWork=0.0,Total_Doz=0.0,Add=0.0;
        ArrayList<Double> count;
        count=new ArrayList<Double>();
        final TextView textView1=findViewById(R.id.detail_1);
        final TextView textView2=findViewById(R.id.detail_2);
        final EditText editText = findViewById(R.id.Production_Labour_Work);
        final EditText editText1= findViewById(R.id.ExtraAmount);
        final Spinner spinner =   findViewById(R.id.Production_Doz_Spiner);
            while (cur.moveToNext()) {
                scale = cur.getString(cur.getColumnIndex(Contracts.Production_Meterials.COLUMN_3));
                Amount = cur.getString(cur.getColumnIndex(Contracts.Production_Meterials.COLUMN_4));
                count.add(Double.parseDouble(Amount));
                s1 = cur.getString(cur.getColumnIndex(Contracts.Production_Meterials.COLUMN_5));
                s2 = cur.getString(cur.getColumnIndex(Contracts.Production_Meterials.COLUMN_7));
                s3 = cur.getString(cur.getColumnIndex(Contracts.Production_Meterials.COLUMN_9));
            }
            if(s1.equals("")||s2.equals("0")||s3.equals("")){
                SelectDoz(spinner);
                textView1.setText("ONE DOZE =0.0 RS");
                textView2.setText("ONE PICE =0.0 RS");
                editText.getText().clear();
                editText1.getText().clear();
                editText.setEnabled(true);
                editText1.setEnabled(true);
                spinner.setEnabled(true);
            }
            else{
                editText.setText(s1);
                editText.setEnabled(false);
                editText1.setText(s3);
                editText1.setEnabled(false);
                //NOW SET SPINNER VALUE
                ArrayAdapter<String> adapter;
                List<String> list;
                String string = s2;
                list = new ArrayList<String>();
                list.add(string);
                adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setEnabled(false);
                for (int j=0;j<count.size();j++){
                    TotalAmount+=count.get(j);
                }
                Total_Doz=Double.parseDouble(spinner.getSelectedItem().toString());
                LaWork=Double.parseDouble(s1);
                Add=LaWork*Total_Doz;
                TotalAmount+=Add+(Double.parseDouble(s3)/Double.parseDouble(s2));
               //SET DOUBLE TO DECIMAL  FORMATE
                DecimalFormat dec = new DecimalFormat("#0.00");
                textView1.setText("ONE DOZE ="+ dec.format(TotalAmount/Total_Doz)+" RS");
                textView2.setText("ONE PICE ="+dec.format((TotalAmount/Total_Doz)/12)+" RS");
                One_Dozen_Amount=Double.parseDouble(dec.format((TotalAmount/Total_Doz)/12));
                ContentValues args = new ContentValues();
                SQLiteDatabase mDb = db_helper.getWritableDatabase();
                args.put(Contracts.Production_Meterials.COLUMN_8,dec.format((TotalAmount/Total_Doz)/12));
                long result = mDb.update(Contracts.Production_Meterials.TABLE_NAME, args, Contracts.Production_Meterials.COLUMN_6 + "=" + id, null);
                if (result == -1) {
                } else {
                }

                Edit_Doz_Rs();
            }

    }
   public void Edit_Doz_Rs(){
       LinearLayout linearLayout=findViewById(R.id.Update1);
       linearLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               AlertDialog.Builder mbuilder = new AlertDialog.Builder(Production_Material.this);
               View mview = getLayoutInflater().inflate(R.layout.prod_labour_dozen_dialog, null);
               final EditText editText1 = mview.findViewById(R.id.edittext);
               final Spinner spinner=mview.findViewById(R.id.spinner);
               SelectDoz(spinner);
               final EditText editText3 = mview.findViewById(R.id.EXTRA);
               String s1="",s2="",s3="";
               Cursor cur=getAllpurch();
               while (cur.moveToNext()){
                   s1 = cur.getString(cur.getColumnIndex(Contracts.Production_Meterials.COLUMN_5));
                   s2 = cur.getString(cur.getColumnIndex(Contracts.Production_Meterials.COLUMN_9));
               }
               Button button1=mview.findViewById(R.id.s);
               Button button2=mview.findViewById(R.id.e);
               editText1.setText(s1);
               editText3.setText(s2);
               button1.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       String s1="",s2="",s3="";
                       s1=editText1.getText().toString();
                       s2=spinner.getSelectedItem().toString();
                       s3=editText3.getText().toString();
                       if(s1.equals("")||s2.equals("")||s3.equals("")||spinner.getSelectedItem().toString().equals("0")){
                           Toast.makeText(Production_Material.this, "ADD ALL DETAILS", Toast.LENGTH_SHORT).show();
                       }
                       else {
                           if(update_labour(s1,s2,s3)==true){
                               Toast.makeText(Production_Material.this, "Details UPDATE Successfully", Toast.LENGTH_SHORT).show();
                               check_Doz_Labourwork();
                           }
                           else{
                               Toast.makeText(Production_Material.this, "ERROR", Toast.LENGTH_SHORT).show();
                           }
                       }
                       alertDialog.dismiss();
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
               alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
           }
       });
   }
    public boolean update_labour(String Doz, String Rs,String Extra_Amount) {
        SQLiteDatabase mDb = db_helper.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(Contracts.Production_Meterials.COLUMN_5, Doz);
        args.put(Contracts.Production_Meterials.COLUMN_7, Rs);
        args.put(Contracts.Production_Meterials.COLUMN_9, Extra_Amount);
        long result = mDb.update(Contracts.Production_Meterials.TABLE_NAME, args, Contracts.Production_Meterials.COLUMN_6 + "=" + id, null);
        if (result == -1) {
            return false;
        } else {

            return true;
        }
    }

   public void Set_Purchasing_name(){
       SQLiteDatabase  mDb=db_helper.getReadableDatabase();
       String s="SELECT * from " + Contracts.Sale_Table.TABLE_NAME;
       String uname1;
       String amount;
       String scale;
       Cursor cr = mDb.rawQuery(s, new String[] {});
       list = new ArrayList<String>();
       Amount = new ArrayList<String>();
       Scale = new ArrayList<String>();
       list.add("Select");
       Amount.add("kk");
       Scale.add("kk");
           while (cr.moveToNext()) {
               uname1 = cr.getString(cr.getColumnIndex(Contracts.Sale_Table.COLUMN_1));
               amount = cr.getString(cr.getColumnIndex(Contracts.Sale_Table.COLUMN_2));
               scale = cr.getString(cr.getColumnIndex(Contracts.Sale_Table.COLUMN_3));
               list.add(uname1);
               Amount.add(amount);
               Scale.add(scale);
           }

   }
   public void setSpinnerScale(Spinner Sca){
       //SET SCALE SPINNER
       ArrayAdapter<String> adapt1;
       adapt1 = new ArrayAdapter<String>(getApplicationContext(),
               android.R.layout.simple_spinner_dropdown_item, list1);
       adapt1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       Sca.setAdapter(adapt1);
       //SPINNER 2 is SET
   }
    public int check_Material_AllReady (String s){
        List<String> list;
        list = new ArrayList<String>();
        int check=0;
        Cursor c=getAllpurch();
        while (c.moveToNext()){
            String Purch_Name = c.getString(c.getColumnIndex(Contracts.Production_Meterials.COLUMN_1));
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

}

