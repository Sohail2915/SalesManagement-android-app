package com.kk.kamranqadeer.salemanager;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;
import com.kk.kamranqadeer.salemanager.SlideAdapters.All_BILL;
import com.kk.kamranqadeer.salemanager.SlideAdapters.All_PDF;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DbHelper db_helper;
    LinearLayout s,purch,produ,Raw,record;
    private DrawerLayout drawable;
    BarChart barChart;
    private ActionBarDrawerToggle togal;
    AlertDialog alertDialog,alertDialog_pdf;
    ArrayList<String> pdf=new ArrayList<String>();
    ArrayList<String> pic=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        drawable=findViewById(R.id.drawer);
        togal=new ActionBarDrawerToggle(this,drawable,R.string.open,R.string.close);
        drawable.addDrawerListener(togal);
        togal.syncState();
        db_helper=new DbHelper(this);
        NavigationView navigationView=findViewById(R.id.navigation);
        //menu item action
        setMenuItemAction(navigationView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // this.deleteDatabase("SALE_MANAGER1_0.1");
        Sale();
        Purchasing();
        Production();
        Record();
        Raw();
        making_folder();
        barChart=findViewById(R.id.barchart);
        ArrayList<BarEntry> barEntries=new ArrayList<>();
        barEntries.add(new BarEntry(44f,0));
        barEntries.add(new BarEntry(36f,1));
        barEntries.add(new BarEntry(30f,2));
        barEntries.add(new BarEntry(20f,3));
        barEntries.add(new BarEntry(34f,4));
        BarDataSet barDataSet=new BarDataSet(barEntries,"MONTH");
        BarData barData=new BarData(barDataSet);
        barData.setBarWidth(1.9f);
        barData.setValueTextColor(Color.WHITE);
        barData.setValueTextSize(8f);
        barChart.setData(barData);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_action_bar,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void setMenuItemAction(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dash:
                    {
                        drawable.closeDrawers();
                        break;
                    }
                    case R.id.data_backup:
                    {
                        try {
                            Backup();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        drawable.closeDrawers();
                        break;

                    }
                    case R.id.all_bill:
                    {
                       BillPic();
                       drawable.closeDrawers();
                       break;
                    }
                    case R.id.all_file:
                    {
                        Reports();
                        drawable.closeDrawers();
                        break;
                    }
                    default:
                        About();
                        drawable.closeDrawers();

                }
                return false;
            }
        });

        drawable.closeDrawers();
    }
    //open play store
    public void openfolderInexplorer(String path) {
        Intent intent = this.getPackageManager().getLaunchIntentForPackage("com.estrongs.android.pop");
        if (intent != null) {
            // If the application is avilable
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.parse(path);
            intent.setDataAndType(uri, "resource/folder");
            this.startActivity(intent);
        } else {
            // Play store to install app
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" +
                    "com.estrongs.android.pop"));
            this.startActivity(intent);
        }
    }
    public void Backup()throws IOException {

    }
    public void Reports() {
        showpdfList();
        AlertDialog.Builder mbuilder=new AlertDialog.Builder(MainActivity.this);
        View mview=getLayoutInflater().inflate(R.layout.all_reports,null);
        Button button=mview.findViewById(R.id.button);
        RecyclerView recyclerView=(RecyclerView)mview.findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        All_PDF pdf_adapter=new All_PDF(MainActivity.this,pdf);
        recyclerView.setAdapter(pdf_adapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog_pdf.dismiss();
            }
        });
        mbuilder.setView(mview);
        alertDialog_pdf=mbuilder.create();
        alertDialog_pdf.show();
        alertDialog_pdf.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }
    public void BillPic() {
        showbillList();
        AlertDialog.Builder mbuilder=new AlertDialog.Builder(MainActivity.this);
        View mview=getLayoutInflater().inflate(R.layout.all_pictures,null);
        Button button=mview.findViewById(R.id.button);
        RecyclerView recyclerView=(RecyclerView)mview.findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        All_BILL pdf_adapter=new All_BILL(MainActivity.this,pic);
        recyclerView.setAdapter(pdf_adapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog_pdf.dismiss();
            }
        });
        mbuilder.setView(mview);
        alertDialog_pdf=mbuilder.create();
        alertDialog_pdf.show();
        alertDialog_pdf.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }
    public void About() {
        AlertDialog.Builder mbuilder=new AlertDialog.Builder(MainActivity.this);
        View mview=getLayoutInflater().inflate(R.layout.about,null);
        mbuilder.setView(mview);
        alertDialog_pdf=mbuilder.create();
        alertDialog_pdf.show();
        alertDialog_pdf.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }
    public void Help() {
        AlertDialog.Builder mbuilder=new AlertDialog.Builder(MainActivity.this);
        View mview=getLayoutInflater().inflate(R.layout.help,null);
        mbuilder.setView(mview);
        alertDialog_pdf=mbuilder.create();
        alertDialog_pdf.show();
        alertDialog_pdf.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_help:
                Help();
                return true;
            case android.R.id.home:
                if(togal.onOptionsItemSelected(item)){
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void Sale()
    {
        s = (LinearLayout) findViewById(R.id.sale);
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MainActivity.this,Sale.class);
                startActivity(in);

            }
        });
    }
    public void Purchasing()
    {
        purch = (LinearLayout) findViewById(R.id.purchashing);
        purch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MainActivity.this,Purchasing.class);
                startActivity(in);
            }
        });
    }
    public void Production()
    {
        produ = (LinearLayout) findViewById(R.id.production);
        produ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MainActivity.this,Production.class);
                startActivity(in);
            }
        });
    }
    public void Record()
    {
        record = (LinearLayout) findViewById(R.id.record);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MainActivity.this,Record.class);
                startActivity(in);
                AddRecordDate();
            }
        });
    }
    public void Raw()
    {
        Raw = (LinearLayout) findViewById(R.id.Raw_Material);
        Raw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MainActivity.this,All_Raw_Materials.class);
                startActivity(in);
            }
        });
    }
    public void making_folder(){
        File folder = new File(Environment.getExternalStorageDirectory() + "/SaleManager");
        File folder1 = new File(Environment.getExternalStorageDirectory() + "/SaleManager/Reports");
        File folder3 = new File(Environment.getExternalStorageDirectory() + "/SaleManager/BackUp");

        if (!folder.exists()) {
            Toast.makeText(MainActivity.this, "Directory Does Not Exist, Create It", Toast.LENGTH_SHORT).show();
            folder.mkdir();
            folder3.mkdir();
            folder1.mkdir();
        }

    }
    public String SetDate()
    {
        Date date=new Date();
        SimpleDateFormat mdyFormat = new SimpleDateFormat("MM-dd-yyyy");
        String Date = mdyFormat.format(date);
        String split[]=Date.split("-");
        String D=split[0]+"-"+split[2];
        return D;
    }
    public void AddRecordDate(){

        if(check_Record_Date(SetDate())!=1){
             String d=SetDate();
            //Adding date
            SQLiteDatabase mDb=db_helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Contracts.Record_Date.COLUMN_1,d);
            values.put(Contracts.Record_Date.COLUMN_2,1);
            mDb.insert(Contracts.Record_Date.TABLE_NAME, null, values);
        }
    }
    private Cursor getAllRecord_Date() {
        SQLiteDatabase mDb=db_helper.getReadableDatabase();
        Cursor cr=mDb.rawQuery("select * from "+ Contracts.Record_Date.TABLE_NAME,null);
        //  cr = db.query(Contract.TableDetail.TABLE_NAME, null, null, null, null,null, null);
        return cr;

    }
    public int check_Record_Date (String D){
        List<String> list;
        list = new ArrayList<String>();
        ArrayList<Integer> ID = new ArrayList<Integer>();
        int check=0;
        Cursor c=getAllRecord_Date();
        if(c.getCount()!=0) {
            while (c.moveToNext()) {
                String date = c.getString(c.getColumnIndex(Contracts.Record_Date.COLUMN_1));
                int id = c.getInt(c.getColumnIndex(Contracts.Record_Date._ID));
                list.add(date);
                ID.add(id);
            }
            for (int i = 0; i < list.size(); i++) {
                if (D.equals(list.get(i))) {
                    check = 1;
                    return check;
                }
            }
        }
        return check;
    }

    private void showpdfList() {
        File file = new File(android.os.Environment.getExternalStorageDirectory() + "/SaleManager/Reports/");
        if (file.exists()) {
            String[] fileListpdf = file.list();
            Collections.addAll(pdf, fileListpdf);
        }
    }
    private void showbillList() {
        File file = new File(android.os.Environment.getExternalStorageDirectory() + "/SaleManager/Pictures/");
        if (file.exists()) {
            String[] fileListpdf = file.list();
            Collections.addAll(pic, fileListpdf);
        }
    }
}

