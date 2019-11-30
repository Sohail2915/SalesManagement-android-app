package com.kk.kamranqadeer.salemanager.SlideAdapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.kamranqadeer.salemanager.PDF_WORK.PDF_Templet;
import com.kk.kamranqadeer.salemanager.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by kamran qadeer on 7/31/2018.
 */

public class All_PDF extends RecyclerView.Adapter<All_PDF.MyViewHolder> {
    ArrayList<String> pdf;
    Context context;
    public All_PDF(Context context,ArrayList<String> pdf) {
       this.pdf=pdf;
       this.context=context;
    }

    @Override
    public All_PDF.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_card,parent,false);
        MyViewHolder holder=new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // Move the mCursor to the position of the item to be displayed
        holder.textView1.setText(pdf.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,pdf.get(position),Toast.LENGTH_SHORT).show();
                //open pdf file
                File file = new File("/sdcard/SaleManager/Reports/"+pdf.get(position).trim().toString());
                if (file.exists()) {
                    Uri path = Uri.fromFile(file);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    try {
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context,
                                "No Application Available to View PDF",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return pdf.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.tv_1);
        }
    }
}
