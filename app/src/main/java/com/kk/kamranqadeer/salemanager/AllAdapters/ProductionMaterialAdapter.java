package com.kk.kamranqadeer.salemanager.AllAdapters;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;
import com.kk.kamranqadeer.salemanager.MainActivity;
import com.kk.kamranqadeer.salemanager.Production_Material;
import com.kk.kamranqadeer.salemanager.R;
import com.kk.kamranqadeer.salemanager.Sale;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by kamran qadeer on 8/10/2018.
 */

public class ProductionMaterialAdapter extends RecyclerView.Adapter <ProductionMaterialAdapter.MyViewHolder> {
    Context context;
    Long IDFK;
    Cursor cursor;
    DbHelper db_helper;
    Long pos;
    String Am = "", Sc = "";
    Double kg, am;
    ArrayList<String> list, Amount, Scale,list1;
    ArrayAdapter<String> adapt, adapt1;
    AlertDialog alertDialog;
    View mview;
    public ProductionMaterialAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        db_helper = new DbHelper(context);

    }

    @Override
    public ProductionMaterialAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.production_m_card, parent, false);
        return new ProductionMaterialAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductionMaterialAdapter.MyViewHolder holder, final int position) {
        // Move the mCursor to the position of the item to be displayed
        if (!cursor.moveToPosition(position)) {
            return; // bail if returned null
        }
        final String Name = cursor.getString(cursor.getColumnIndex(Contracts.Production_Meterials.COLUMN_1));
        String Used = cursor.getString(cursor.getColumnIndex(Contracts.Production_Meterials.COLUMN_2));
        String SCALE = cursor.getString(cursor.getColumnIndex(Contracts.Production_Meterials.COLUMN_3));
        String COST = cursor.getString(cursor.getColumnIndex(Contracts.Production_Meterials.COLUMN_4));
        IDFK = cursor.getLong(cursor.getColumnIndex(Contracts.Production_Meterials.COLUMN_6));
        final String Doz = cursor.getString(cursor.getColumnIndex(Contracts.Production_Meterials.COLUMN_7));
        long id = cursor.getLong(cursor.getColumnIndex(Contracts.Production_Meterials._ID));
        holder.textView3.setText(Name);
        holder.textView5.setText(Used + SCALE);
        holder.textView7.setText(COST + "RS");
        holder.textView2.setText("THIS MATERIAL IS USED FOR " + Doz + " DOZEN");
        holder.itemView.setTag(id);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set_Purchasing_name();
                //alert dialog using bulder
                final AlertDialog.Builder mbuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
                mview=inflater.inflate(R.layout.produ_mater_dialog,null);
                Cursor c = cursor;
                c.moveToPosition(position);
                pos = cursor.getLong(cursor.getColumnIndex(Contracts.Purch_Detail_Table._ID));
                //first set spinner ALL purchasing products;
                String s1 = null, s2 = null, s3 = null;
                db_helper = new DbHelper(context);
                SQLiteDatabase mDb1 = db_helper.getReadableDatabase();
                String s = "SELECT * from " + Contracts.Production_Meterials.TABLE_NAME + " WHERE " + Contracts.Production_Meterials._ID + " = " + pos;
                final Cursor cur = mDb1.rawQuery(s, new String[]{});
                try {
                    while (cur.moveToNext()) {
                        s1 = cur.getString(cur.getColumnIndex(Contracts.Production_Meterials.COLUMN_1));
                        s2 = cur.getString(cur.getColumnIndex(Contracts.Production_Meterials.COLUMN_2));
                        s3 = cur.getString(cur.getColumnIndex(Contracts.Production_Meterials.COLUMN_4));
                    }

                } catch (Exception e) {
                }
                //SET AMOUNT AND SCALE
                for (int i = 0; i < list.size(); i++) {
                    if (s1.equals(list.get(i))) {
                        Am = Amount.get(i);
                        Sc = Scale.get(i);
                        break;
                    }
                }
                //SET SPINNER
                ArrayList<String> list;
                list = new ArrayList<String>();
                list.add(s1);
                list1=new ArrayList<String>();
                final Spinner Scale = mview.findViewById(R.id.Production_Material_spinerML);
                setSpinnerSelect(Scale);
                final Spinner All_Name = mview.findViewById(R.id.All_Name);
                adapt = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, list);
                adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                All_Name.setAdapter(adapt);
                //spinner value is set
                final EditText editText2 = mview.findViewById(R.id.Production_Material_qun);
                final EditText editText3 = mview.findViewById(R.id.COST);
                final TextView textView = mview.findViewById(R.id.detail);
                Button button1 = mview.findViewById(R.id.Production_Material_SAVE);
                Button button2 = mview.findViewById(R.id.Production_Material_EXIT);
                final TextView textView1=mview.findViewById(R.id.tv_1);
                textView1.setText("UPDATE");
                button1.setText("UPDATE");
                button2.setText("CANCLE");
                textView.setText(Am + " RS ONE " + Sc);
                editText2.setText(s2);
                editText3.setText(s3);
                editText3.setEnabled(false);
                //SPINNER LISTNER
                Scale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (Scale.getSelectedItem().toString().equals("gm")) {
                            kg = Double.parseDouble(editText2.getText().toString()) / 1000;
                            am = Double.parseDouble(Am) * kg;
                            editText3.setText(Double.toString(am));
                        }
                        else if (Scale.getSelectedItem().toString().equals("ml")) {
                            kg = Double.parseDouble(editText2.getText().toString()) / 1000;
                            am = Double.parseDouble(Am) * kg;
                            editText3.setText(Double.toString(am));
                        }
                        else {
                            am = Double.parseDouble(editText2.getText().toString()) * Double.parseDouble(Am);
                            editText3.setText(Double.toString(am));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                //EDITTEXT LISTNER
                editText2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String no = s.toString();
                        if (editText2.equals("QUNTITY")) {
                        } else {
                            if (no.equals("")) {
                                editText3.setText(Am);
                            } else {
                                if (Scale.getSelectedItem().toString().equals("gm")) {
                                    kg = Double.parseDouble(no) / 1000;
                                    am = Double.parseDouble(Am) * kg;
                                    editText3.setText(Double.toString(am));

                                } else {
                                    am = Double.parseDouble(no) * Double.parseDouble(Am);
                                    editText3.setText(Double.toString(am));
                                }
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Spinner All_Name = mview.findViewById(R.id.All_Name);
                        EditText editText2 = mview.findViewById(R.id.Production_Material_qun);
                        Spinner spinner = mview.findViewById(R.id.Production_Material_spinerML);
                        String s1 = editText2.getText().toString();
                        String s2 = Scale.getSelectedItem().toString();
                        String s3 = editText3.getText().toString();
                        if (s1.equals("") || s2.equals("Select")||s3.equals("")) {
                            Toast.makeText(context, "ENTER ALL DETAIL", Toast.LENGTH_SHORT).show();
                        } else {
                            SQLiteDatabase mDb = db_helper.getWritableDatabase();
                            ContentValues args = new ContentValues();
                            args.put(Contracts.Production_Meterials.COLUMN_2, s1);
                            args.put(Contracts.Production_Meterials.COLUMN_3, s2);
                            args.put(Contracts.Production_Meterials.COLUMN_4, s3);
                            int result = mDb.update(Contracts.Production_Meterials.TABLE_NAME, args, Contracts.Production_Meterials._ID + " = " + pos, null);
                            if (result == -1) {
                            } else {
                                swapCursor(getAllpurch());
                                Toast.makeText(context,"DETAIL IS UPDATE",Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
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
        });
    }

    @Override
    public int getItemCount() {

        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (cursor != null) cursor.close();
        cursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView2, textView3, textView4, textView5, textView6, textView7;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView2 = (TextView) itemView.findViewById(R.id.detail);
            textView3 = (TextView) itemView.findViewById(R.id.name_1);
            textView4 = (TextView) itemView.findViewById(R.id.t_m_1);
            textView5 = (TextView) itemView.findViewById(R.id.t_1_1);
            textView6 = (TextView) itemView.findViewById(R.id.t_m_2);
            textView7 = (TextView) itemView.findViewById(R.id.t_m_2_2);
        }
    }

    public void Set_Purchasing_name() {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String s = "SELECT * from " + Contracts.Sale_Table.TABLE_NAME;
        String uname1;
        String amount;
        String scale;
        Cursor cr = mDb.rawQuery(s, new String[]{});
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
    public void setSpinnerSelect(Spinner dozen){
        //SET SCALE SPINNER
        list1.add("Select");
        list1 = new ArrayList<String>();
        if (Sc.equals("Liter")) {
            list1.add("ml");
            list1.add("Liter");
        } else if (Sc.equals("kg")) {
            list1.add("gm");
            list1.add("kg");
        } else if (Sc.equals("dozen")) {
            list1.add("dozen");
        } else if (Sc.equals("can")) {
            list1.add("can");
        } else if (Sc.equals("pice")) {
            list1.add("pice");
        }
        adapt1 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,list1);
        adapt1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dozen.setAdapter(adapt1);
        //NOW SCAL SPINNER IS SET
    }
    public Cursor getAllpurch() {

        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Production_Meterials._ID, Contracts.Production_Meterials.COLUMN_1, Contracts.Production_Meterials.COLUMN_2, Contracts.Production_Meterials.COLUMN_3, Contracts.Production_Meterials.COLUMN_4, Contracts.Production_Meterials.COLUMN_5,Contracts.Production_Meterials.COLUMN_6,Contracts.Production_Meterials.COLUMN_7};
        Cursor cr = mDb.query(Contracts.Production_Meterials.TABLE_NAME, columns, Contracts.Production_Meterials.COLUMN_6 + " = '" + IDFK + "'", null, null, null, null);
        return cr;


    }
}