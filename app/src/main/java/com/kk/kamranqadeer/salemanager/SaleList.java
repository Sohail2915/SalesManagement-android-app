package com.kk.kamranqadeer.salemanager;

import android.Manifest;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.kamranqadeer.salemanager.AllAdapters.SaleAdapter_2;
import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SaleList extends AppCompatActivity {

    DbHelper db;
    RecyclerView recyclerView;
    String strDate,pic_id="0";
    Long id;
    AlertDialog alertDialog;
    private SaleAdapter_2 adapter;
    String Product_Name;
    AutoCompleteTextView editText;
    LinearLayout linearLayout;
    ImageView back,search,ok;
    TextView textView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;
    ArrayList<String> PicList=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_list);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View view =getSupportActionBar().getCustomView();
        textView=view.findViewById(R.id.text);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
        Intent intent = getIntent();
        if (intent.hasExtra("Product_Name")) {
            Product_Name = intent.getStringExtra("Product_Name");
            textView.setText(Product_Name);
        }
        if (intent.hasExtra("_ID")) {
            String a = intent.getStringExtra("_ID");
            id = Long.parseLong(a);
        }
        SetDate();
        db = new DbHelper(this);
        getIdAndProductName();
        back=view.findViewById(R.id.back);
        search=view.findViewById(R.id.search);
        editText=view.findViewById(R.id.edittext);
        linearLayout=view.findViewById(R.id.check1);
        ok=view.findViewById(R.id.ok);
        linearLayout.setVisibility(View.GONE);
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
                  editextSuggetion();
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
        ArrayList<String> Name=new ArrayList<String>();
        Cursor c=getAllSale();
        while (c.moveToNext()){
            String name=c.getString(c.getColumnIndex(Contracts.Sale_ProduDeteil_Table.COLUMN_5));
            String date=c.getString(c.getColumnIndex(Contracts.Sale_ProduDeteil_Table.COLUMN_4));
            Name.add(name+"("+date+")");
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
    public void getIdAndProductName() {
        recyclerView = (RecyclerView) findViewById(R.id.Sale_List_RecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Cursor cursor = getAllSale();
        // Toast.makeText(this, ""+cursor.getCount(), Toast.LENGTH_SHORT).show();
        adapter = new SaleAdapter_2(this, cursor);
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
                adapter.swapCursor(getAllSale());
            }

            //COMPLETED (11) attach the ItemTouchHelper to the waitlistRecyclerView
        }).attachToRecyclerView(recyclerView);
        SetStockValue();
    }
    public void SetDate() {
        Date date = new Date();
        SimpleDateFormat mdyFormat = new SimpleDateFormat("MM-dd-yyyy");
        strDate = mdyFormat.format(date);
    }

    private Cursor getAllSale() {
        SQLiteDatabase mDb = db.getReadableDatabase();
        String[] columns = new String[]{Contracts.Sale_ProduDeteil_Table._ID, Contracts.Sale_ProduDeteil_Table.COLUMN_1, Contracts.Sale_ProduDeteil_Table.COLUMN_2, Contracts.Sale_ProduDeteil_Table.COLUMN_3, Contracts.Sale_ProduDeteil_Table.COLUMN_4, Contracts.Sale_ProduDeteil_Table.COLUMN_5, Contracts.Sale_ProduDeteil_Table.COLUMN_6};
        Cursor cr = mDb.query(Contracts.Sale_ProduDeteil_Table.TABLE_NAME, columns, Contracts.Sale_ProduDeteil_Table.COLUMN_6 + " = '" + id + "'", null, null, null, null);
        return cr;

    }
    private Cursor getAllSale1(long id) {
        SQLiteDatabase mDb = db.getReadableDatabase();
        String[] columns = new String[]{Contracts.Sale_ProduDeteil_Table._ID, Contracts.Sale_ProduDeteil_Table.COLUMN_1, Contracts.Sale_ProduDeteil_Table.COLUMN_2, Contracts.Sale_ProduDeteil_Table.COLUMN_3, Contracts.Sale_ProduDeteil_Table.COLUMN_4, Contracts.Sale_ProduDeteil_Table.COLUMN_5, Contracts.Sale_ProduDeteil_Table.COLUMN_6};
        Cursor cr = mDb.query(Contracts.Sale_ProduDeteil_Table.TABLE_NAME, columns, Contracts.Sale_ProduDeteil_Table._ID + " = '" + id + "'", null, null, null, null);
        return cr;

    }

    private boolean removeSale(long id) {
        SQLiteDatabase mDb = db.getReadableDatabase();
        // COMPLETED (2) Inside, call mDb.delete to pass in the TABLE_NAME and the condition that WaitlistEntry._ID equals id
        return mDb.delete(Contracts.Sale_ProduDeteil_Table.TABLE_NAME, Contracts.Sale_ProduDeteil_Table._ID + "=" + id, null) > 0;
    }

    public void Add_Sale(View view) {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(SaleList.this);
        View mview = getLayoutInflater().inflate(R.layout.sale_list_dialog, null);
        final EditText editText1 = (EditText) mview.findViewById(R.id.Dozen_Amount);
        final EditText editText2 = (EditText) mview.findViewById(R.id.Sope_Name);
        final EditText editText3 = (EditText) mview.findViewById(R.id.Production_Today_Date);
        editText3.setText(strDate);
        final Spinner spinner1 = (Spinner) mview.findViewById(R.id.Sale);
        SelectDoz(spinner1);
        Button button1 = (Button) mview.findViewById(R.id.save_sale);
        Button button2 = (Button) mview.findViewById(R.id.exit_sale);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1 = spinner1.getSelectedItem().toString();
                String s2 = editText1.getText().toString();
                String s3 = Product_Name;
                String s4 = editText3.getText().toString();
                String s5 = editText2.getText().toString();
                if (s1.trim().equals("0") || s2.trim().equals("")|| s4.trim().equals("") || s5.trim().equals("")) {
                    Toast.makeText(SaleList.this, "Please Enter All Details", Toast.LENGTH_SHORT).show();
                } else {
                    if (addN(s1, s2, s3, s4, s5, id) == true) {
                        Toast.makeText(SaleList.this, "TAKE BILL PICTURE", Toast.LENGTH_LONG).show();
                        adapter.swapCursor(getAllSale());
                        SetStockValue();
                        Dialog_Dismis();
                        getID();
                        //TakePic();
                        takepic_1();

                    } else {
                        Toast.makeText(SaleList.this, "Detail is not add", Toast.LENGTH_SHORT).show();

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

    public boolean addN(String Sale_Dozen, String Total_Doz_Amount, String Day, String Date, String ShopName, Long id) {
        SQLiteDatabase mDb = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contracts.Sale_ProduDeteil_Table.COLUMN_1, Sale_Dozen);
        values.put(Contracts.Sale_ProduDeteil_Table.COLUMN_2, Total_Doz_Amount);
        values.put(Contracts.Sale_ProduDeteil_Table.COLUMN_3, Day);
        values.put(Contracts.Sale_ProduDeteil_Table.COLUMN_4, Date);
        values.put(Contracts.Sale_ProduDeteil_Table.COLUMN_5, ShopName);
        values.put(Contracts.Sale_ProduDeteil_Table.COLUMN_6, id);
        long result = mDb.insert(Contracts.Sale_ProduDeteil_Table.TABLE_NAME, null, values);
        if (result == -1) {
            return false;
        } else {

            return true;
        }
    }

    public void Dialog_Dismis() {
        alertDialog.dismiss();
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
                delete_pic(id);
                PicList.clear();
                removeSale(id);
                adapter.swapCursor(getAllSale());
                SetStockValue();
                dialog.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.swapCursor(getAllSale());
                dialog.dismiss();
            }
        });
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

    public void SetStockValue() {
        Cursor cr = getAllProductionRecord();
        Cursor cr1 = getAllSale();
        String Stock = "0", Left = "0", Sale = "0", AddTotalSale = "0";
        while (cr.moveToNext()) {
            String production = cr.getString(cr.getColumnIndex(Contracts.Production_Record.COLUMN_3));
            String ProductName = cr.getString(cr.getColumnIndex(Contracts.Production_Record.COLUMN_2));
            if (ProductName.equals(Product_Name)) {
                Stock = Double.toString(Double.parseDouble(production) + Double.parseDouble(Stock));
            }
        }

        if (cr1.getCount() != 0) {
            while (cr1.moveToNext()) {
                Sale = cr1.getString(cr1.getColumnIndex(Contracts.Sale_ProduDeteil_Table.COLUMN_1));
                AddTotalSale = Double.toString(Double.parseDouble(AddTotalSale) + Double.parseDouble(Sale));
            }
            //SET TEXTVIEW VALUE
            TextView textView2 = findViewById(R.id.text_1_2);
            Left = Double.toString(Double.parseDouble(Stock) - Double.parseDouble(AddTotalSale));
            if (Double.parseDouble(Left) < 0) {
                textView2.setTextColor(Color.RED);
            } else {
                textView2.setTextColor(Color.WHITE);
            }
            textView2.setText(Left + " DOZEN");
        }
    }

    public Cursor getAllProductionRecord() {
        SQLiteDatabase mDb = db.getReadableDatabase();
        String[] columns = new String[]{Contracts.Production_Record._ID, Contracts.Production_Record.COLUMN_1, Contracts.Production_Record.COLUMN_2, Contracts.Production_Record.COLUMN_3, Contracts.Production_Record.COLUMN_4, Contracts.Production_Record.COLUMN_5, Contracts.Production_Record.COLUMN_6};
        Cursor cr = mDb.query(Contracts.Production_Record.TABLE_NAME, columns, null, null, null, null, null);
        return cr;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            galleryAddPic();
        }
    }
    //deleting picture from gallery
    public void delete_pic(long id){
        showImageList();
        for (int i=0;i<PicList.size();i++) {
            String picname = PicList.get(i);
            String split[] = picname.split("-");
            String ID=split[1];
            if (ID.equals(Long.toString(id)+".jpg")) {
                File fdelete = new File("/sdcard/SaleManager/Pictures/"+picname.trim().toString());
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        Toast.makeText(this, "PIC DELETED", Toast.LENGTH_SHORT).show();
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File("/sdcard/SaleManager/Pictures/"+picname.trim().toString()))));
                    } else {
                        Toast.makeText(this, "PIC not DELETED", Toast.LENGTH_SHORT).show();
                    }
                  }
                }
            }
    }
    private void filter(String d) {
        Cursor c=getAllSale();
        long id=0;
        while (c.moveToNext()){
            String name=c.getString(c.getColumnIndex(Contracts.Sale_ProduDeteil_Table.COLUMN_5));
            String date=c.getString(c.getColumnIndex(Contracts.Sale_ProduDeteil_Table.COLUMN_4));
            String s=name+"("+date+")";
            if(s.equals(d)){
                id=c.getInt(c.getColumnIndex(Contracts.Sale_ProduDeteil_Table._ID));
            }
        }
        adapter.swapCursor(getAllSale1(id));
        if(editText.getText().toString().equals("")){
            adapter.swapCursor(getAllSale());
        }
    }

// next take pic code
    public  void takepic_1(){
     dispatchTakePictureIntent();
    }
    private File createImageFile() throws IOException {
        String root = Environment.getExternalStorageDirectory().toString();
        File storageDir = new File(root + "/SaleManager/Pictures");
        File image = File.createTempFile("S_M","-"+pic_id.trim().toString()+".jpg",storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    public void getID(){
        Cursor c=getAllSale();
        while (c.moveToNext()){
                pic_id=Long.toString(c.getInt(c.getColumnIndex(Contracts.Sale_ProduDeteil_Table._ID)));
            }
        Toast.makeText(SaleList.this, pic_id, Toast.LENGTH_SHORT).show();
        }
    private void showImageList() {
        File file = new File(android.os.Environment.getExternalStorageDirectory() + "/SaleManager/Pictures/");
        if (file.exists()) {
            String[] fileListVideo = file.list();
            Collections.addAll(PicList, fileListVideo);
        }
    }
}
