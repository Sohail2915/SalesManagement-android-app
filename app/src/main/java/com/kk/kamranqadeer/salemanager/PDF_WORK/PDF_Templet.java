package com.kk.kamranqadeer.salemanager.PDF_WORK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;
import com.kk.kamranqadeer.salemanager.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class PDF_Templet extends AppCompatActivity implements View.OnClickListener {
    Button btn_generate;
    TextView tv_link;
    ImageView iv_image;
    LinearLayout ll_pdflayout;
    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    boolean boolean_save;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    private TableLayout tableLayout;
    String Date;
    long id;
    DbHelper db_helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf__templet);
        db_helper=new DbHelper(this);
        Intent intent = getIntent();
        if (intent.hasExtra("DATE")) {
            Date = intent.getStringExtra("DATE");
        }
        if (intent.hasExtra("_ID")) {
            String a = intent.getStringExtra("_ID");
            id = Long.parseLong(a);
            System.out.println(id);
        }
        setTableLayout();
        init();
        fn_permission();
        listener();
    }
    private void init(){
        btn_generate = (Button)findViewById(R.id.btn_generate);
        iv_image = (ImageView) findViewById(R.id.iv_image);
        ll_pdflayout = (LinearLayout) findViewById(R.id.ll_pdflayout);

    }

    private void listener(){
        btn_generate.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_generate:
                if (boolean_save) {
                   //open pdf file
                    File file = new File("/sdcard/SaleManager/Reports/Report("+Date+").pdf");

                    if (file.exists()) {
                        Uri path = Uri.fromFile(file);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(path, "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        try {
                            startActivity(intent);
                        }
                        catch (ActivityNotFoundException e) {
                            Toast.makeText(PDF_Templet.this,
                                    "No Application Available to View PDF",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (boolean_permission) {
                        progressDialog = new ProgressDialog(PDF_Templet.this);
                        progressDialog.setMessage("Please wait");
                        bitmap = loadBitmapFromView(ll_pdflayout, ll_pdflayout.getWidth(), ll_pdflayout.getHeight());
                        createPdf();
//                        saveBitmap(bitmap);
                    } else {

                    }

                    createPdf();
                    break;
                }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPdf(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();


        Paint paint = new Paint();
        canvas.drawPaint(paint);


        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);


        // write the document content
        String targetPdf = "/sdcard/SaleManager/Reports/Report("+Date+").pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            btn_generate.setText("Check PDF");
            boolean_save=true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
    }



    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(PDF_Templet.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(PDF_Templet.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(PDF_Templet.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(PDF_Templet.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;


        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                boolean_permission = true;


            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }
    public void setTableLayout(){
        tableLayout=(TableLayout)findViewById(R.id.tableLayout);
        //MATERIAL RECORD ARRAY
        ArrayList<String> one_m=new ArrayList<String>();
        ArrayList<String> two_m=new ArrayList<String>();
        ArrayList<String> Three_m=new ArrayList<String>();
        ArrayList<String> Four_m=new ArrayList<String>();
        // LABOUR RECORD ARRAY
        ArrayList<String> one_l=new ArrayList<String>();
        ArrayList<String> two_l=new ArrayList<String>();
        ArrayList<String> Three_l=new ArrayList<String>();
        ArrayList<String> Four_l=new ArrayList<String>();
        //PRODUCTION ARRAY
        ArrayList<String> one_p=new ArrayList<String>();
        ArrayList<String> two_p=new ArrayList<String>();
        ArrayList<String> Three_p=new ArrayList<String>();
        ArrayList<String> Four_p=new ArrayList<String>();
        //ACCOUNT RECORD ARRAY
        ArrayList<String> one_a=new ArrayList<String>();
        ArrayList<String> two_a=new ArrayList<String>();
        ArrayList<String> Three_a=new ArrayList<String>();
        ArrayList<String> Four_a=new ArrayList<String>();
        Cursor c1=getAllMaterialName(),c2=getAllLabourName(),c3=getAllProductionRecord(),c4=getAllAccount_Record();
       // Set formate
        DecimalFormat format=new DecimalFormat("#0.0");
        while (c1.moveToNext()){
            String Name=c1.getString(c1.getColumnIndex(Contracts.Material_record.COLUMN_2));
            String Pur=c1.getString(c1.getColumnIndex(Contracts.Material_record.COLUMN_3));
            String Used=c1.getString(c1.getColumnIndex(Contracts.Material_record.COLUMN_4));
            String Left=c1.getString(c1.getColumnIndex(Contracts.Material_record.COLUMN_5));
            String spilt_Used[]=Used.split(" "),split_Purch[]=Pur.split(" "),split_Left[]=Left.split(" ");
            one_m.add(Name);
            two_m.add((format.format(Double.parseDouble(split_Purch[0])))+" "+split_Purch[1]);
            Three_m.add((format.format(Double.parseDouble(spilt_Used[0])))+" "+spilt_Used[1]);
            Four_m.add((format.format(Double.parseDouble(split_Left[0])))+" "+split_Left[1]);
        }
        while (c2.moveToNext()){
            String Name=c2.getString(c2.getColumnIndex(Contracts.Labour_Record.COLUMN_2));
            String Work=c2.getString(c2.getColumnIndex(Contracts.Labour_Record.COLUMN_3));
            String Amount=c2.getString(c2.getColumnIndex(Contracts.Labour_Record.COLUMN_4));
            String Pay=c2.getString(c2.getColumnIndex(Contracts.Labour_Record.COLUMN_5));
            one_l.add(Name);
            two_l.add(Work);
            Three_l.add(Amount);
            Four_l.add(Pay);
        }
        while (c3.moveToNext()){
            String Name=c3.getString(c3.getColumnIndex(Contracts.Production_Record.COLUMN_2));
            String Pro=c3.getString(c3.getColumnIndex(Contracts.Production_Record.COLUMN_3));
            String Sale=c3.getString(c3.getColumnIndex(Contracts.Production_Record.COLUMN_4));
            String Left=c3.getString(c3.getColumnIndex(Contracts.Production_Record.COLUMN_5));
            one_p.add(Name);
            two_p.add(Pro);
            Three_p.add(Sale);
            Four_p.add(Left);
        }
        while (c4.moveToNext()){
            String Sale=c4.getString(c4.getColumnIndex(Contracts.Account_Record.COLUMN_2));
            String Prod=c4.getString(c4.getColumnIndex(Contracts.Account_Record.COLUMN_3));
            String Prof=c4.getString(c4.getColumnIndex(Contracts.Account_Record.COLUMN_4));
            String Loss=c4.getString(c4.getColumnIndex(Contracts.Account_Record.COLUMN_5));
            one_a.add(format.format(Double.parseDouble(Sale)));
            two_a.add(format.format(Double.parseDouble(Prod)));
            Three_a.add(format.format(Double.parseDouble(Prof)));
            Four_a.add(format.format(Double.parseDouble(Loss)));
        }
        for(int check=0;check<=3;check++) {
            if (check == 0) {
                for (int i = 0; i < c1.getCount(); i++) {
                    View tableRow = LayoutInflater.from(this).inflate(R.layout.table_item, null, false);
                    TextView no = (TextView) tableRow.findViewById(R.id.no);
                    TextView one = (TextView) tableRow.findViewById(R.id.one);
                    TextView two = (TextView) tableRow.findViewById(R.id.two);
                    TextView three = (TextView) tableRow.findViewById(R.id.three);
                    TextView four = (TextView) tableRow.findViewById(R.id.four);
                    no.setText("" + (i + 1));
                    one.setText(one_m.get(i));
                    two.setText(two_m.get(i));
                    three.setText(Three_m.get(i));
                    four.setText(Four_m.get(i));
                    tableLayout.addView(tableRow);
                    if (i > 0) {
                        TextView M_Name = (TextView) tableRow.findViewById(R.id.M_Name);
                        M_Name.setVisibility(View.GONE);
                        TableRow tableRow1 = (TableRow) tableRow.findViewById(R.id.table_check);
                        tableRow1.setVisibility(View.GONE);
                    }
                    if (i == c1.getCount() - 1) {
                        check++;
                    }
                }
            }
            if (check == 1) {
                System.out.println(check);
                for (int i = 0; i < c2.getCount(); i++) {
                    View tableRow = LayoutInflater.from(this).inflate(R.layout.table_item, null, false);
                    TextView no = (TextView) tableRow.findViewById(R.id.no);
                    TextView one = (TextView) tableRow.findViewById(R.id.one);
                    TextView two = (TextView) tableRow.findViewById(R.id.two);
                    TextView three = (TextView) tableRow.findViewById(R.id.three);
                    TextView four = (TextView) tableRow.findViewById(R.id.four);
                    no.setText("" + (i + 1));
                    one.setText(one_l.get(i));
                    two.setText(two_l.get(i));
                    three.setText(Three_l.get(i));
                    four.setText(Four_l.get(i));
                    tableLayout.addView(tableRow);
                    if (i == 0) {
                        TextView M_Name = (TextView) tableRow.findViewById(R.id.M_Name);
                        M_Name.setVisibility(View.VISIBLE);
                        TextView textView2 = tableRow.findViewById(R.id.tv_3);
                        TextView textView3 = tableRow.findViewById(R.id.tv_4);
                        TextView textView4 = tableRow.findViewById(R.id.tv_5);
                        textView2.setText("WORK");
                        textView3.setText("AMOUNT");
                        textView4.setText("PAY");
                        M_Name.setText("LABOUR RECORD");
                        TableRow tableRow1 = (TableRow) tableRow.findViewById(R.id.table_check);
                        tableRow1.setVisibility(View.VISIBLE);

                    }
                    if (i > 0) {
                        TextView M_Name = (TextView) tableRow.findViewById(R.id.M_Name);
                        M_Name.setVisibility(View.GONE);
                        TableRow tableRow1 = (TableRow) tableRow.findViewById(R.id.table_check);
                        tableRow1.setVisibility(View.GONE);
                    }
                    if (i == c2.getCount() - 1) {
                        check++;
                    }
                }
            }
            if (check == 2) {
                System.out.println(check);
                for (int i = 0; i < c3.getCount(); i++) {
                    View tableRow = LayoutInflater.from(this).inflate(R.layout.table_item, null, false);
                    TextView no = (TextView) tableRow.findViewById(R.id.no);
                    TextView one = (TextView) tableRow.findViewById(R.id.one);
                    TextView two = (TextView) tableRow.findViewById(R.id.two);
                    TextView three = (TextView) tableRow.findViewById(R.id.three);
                    TextView four = (TextView) tableRow.findViewById(R.id.four);
                    no.setText("" + (i + 1));
                    one.setText(one_p.get(i));
                    two.setText(two_p.get(i));
                    three.setText(Three_p.get(i));
                    four.setText(Four_p.get(i));
                    tableLayout.addView(tableRow);
                    if (i == 0) {
                        TextView M_Name = (TextView) tableRow.findViewById(R.id.M_Name);
                        M_Name.setVisibility(View.VISIBLE);
                        TextView textView2 = tableRow.findViewById(R.id.tv_3);
                        TextView textView3 = tableRow.findViewById(R.id.tv_4);
                        TextView textView4 = tableRow.findViewById(R.id.tv_5);
                        textView2.setText("PROD");
                        textView3.setText("SALE");
                        textView4.setText("LEFT");
                        M_Name.setText("PRODUCTION RECORD");
                        TableRow tableRow1 = (TableRow) tableRow.findViewById(R.id.table_check);
                        tableRow1.setVisibility(View.VISIBLE);

                    }
                    if (i > 0) {
                        TextView M_Name = (TextView) tableRow.findViewById(R.id.M_Name);
                        M_Name.setVisibility(View.GONE);
                        TableRow tableRow1 = (TableRow) tableRow.findViewById(R.id.table_check);
                        tableRow1.setVisibility(View.GONE);
                    }
                }
            }
            if (check == 3) {
                System.out.println(check);
                for (int i = 0; i < c4.getCount(); i++) {
                    View tableRow = LayoutInflater.from(this).inflate(R.layout.table_item, null, false);
                    TextView no = (TextView) tableRow.findViewById(R.id.no);
                    TextView one = (TextView) tableRow.findViewById(R.id.one);
                    TextView two = (TextView) tableRow.findViewById(R.id.two);
                    TextView three = (TextView) tableRow.findViewById(R.id.three);
                    TextView four = (TextView) tableRow.findViewById(R.id.four);
                    no.setText("" + (i + 1));
                    one.setText(one_a.get(i));
                    two.setText(two_a.get(i));
                    three.setText(Three_a.get(i));
                    four.setText(Four_a.get(i));
                    tableLayout.addView(tableRow);
                    if (i == 0) {
                        TextView M_Name = (TextView) tableRow.findViewById(R.id.M_Name);
                        M_Name.setVisibility(View.VISIBLE);
                        TextView textView1 = tableRow.findViewById(R.id.tv_2);
                        TextView textView2 = tableRow.findViewById(R.id.tv_3);
                        TextView textView3 = tableRow.findViewById(R.id.tv_4);
                        TextView textView4 = tableRow.findViewById(R.id.tv_5);
                        textView1.setText("SALE");
                        textView2.setText("PROD");
                        textView3.setText("PROF");
                        textView4.setText("LOSS");
                        M_Name.setText("ACCOUNT RECORD");
                        TableRow tableRow1 = (TableRow) tableRow.findViewById(R.id.table_check);
                        tableRow1.setVisibility(View.VISIBLE);

                    }
                    if (i > 0) {
                        TextView M_Name = (TextView) tableRow.findViewById(R.id.M_Name);
                        M_Name.setVisibility(View.GONE);
                        TableRow tableRow1 = (TableRow) tableRow.findViewById(R.id.table_check);
                        tableRow1.setVisibility(View.GONE);
                    }
                }
            }
        }

    }
    public Cursor getAllMaterialName() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Material_record._ID, Contracts.Material_record.COLUMN_1, Contracts.Material_record.COLUMN_2,Contracts.Material_record.COLUMN_3, Contracts.Material_record.COLUMN_4, Contracts.Material_record.COLUMN_5, Contracts.Material_record.COLUMN_6};
        Cursor cr = mDb.query(Contracts.Material_record.TABLE_NAME, columns, Contracts.Material_record.COLUMN_6 + " = '" + id + "'", null, null, null, null);
        return cr;
    }
    public Cursor getAllLabourName() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Labour_Record._ID, Contracts.Labour_Record.COLUMN_1, Contracts.Labour_Record.COLUMN_2,Contracts.Labour_Record.COLUMN_3,Contracts.Labour_Record.COLUMN_4,Contracts.Labour_Record.COLUMN_5,Contracts.Labour_Record.COLUMN_6};
        Cursor cr = mDb.query(Contracts.Labour_Record.TABLE_NAME, columns, Contracts.Labour_Record.COLUMN_6 + " = '" + id + "'", null, null, null, null);
        return cr;
    }
    public Cursor getAllProductionRecord() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Production_Record._ID, Contracts.Production_Record.COLUMN_1, Contracts.Production_Record.COLUMN_2,Contracts.Production_Record.COLUMN_3,Contracts.Production_Record.COLUMN_4,Contracts.Production_Record.COLUMN_5,Contracts.Production_Record.COLUMN_6};
        Cursor cr = mDb.query(Contracts.Production_Record.TABLE_NAME, columns, Contracts.Production_Record.COLUMN_6 + " = '" + id + "'", null, null, null, null);
        return cr;
    }
    public Cursor getAllAccount_Record() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Account_Record._ID, Contracts.Account_Record.COLUMN_1, Contracts.Account_Record.COLUMN_2,Contracts.Account_Record.COLUMN_3,Contracts.Account_Record.COLUMN_4,Contracts.Account_Record.COLUMN_5,Contracts.Account_Record.COLUMN_6};
        Cursor cr = mDb.query(Contracts.Account_Record.TABLE_NAME, columns, Contracts.Account_Record.COLUMN_6+" = "+id, null, null, null, null);
        return cr;
    }
}

