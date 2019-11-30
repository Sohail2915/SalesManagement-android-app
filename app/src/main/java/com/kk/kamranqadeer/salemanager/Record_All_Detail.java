package com.kk.kamranqadeer.salemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;
import com.kk.kamranqadeer.salemanager.PDF_WORK.PDF_Templet;
import com.kk.kamranqadeer.salemanager.Tab.Tab1;
import com.kk.kamranqadeer.salemanager.Tab.Tab2;
import com.kk.kamranqadeer.salemanager.Tab.Tab3;
import com.kk.kamranqadeer.salemanager.Tab.Tab4;

public class Record_All_Detail extends AppCompatActivity {
    DbHelper db_helper;
    long id;
    String  s;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record__all__detail);
        db_helper = new DbHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        if (intent.hasExtra("DATE")) {
            s = intent.getStringExtra("DATE");

        }
        if (intent.hasExtra("_ID")) {
            String a = intent.getStringExtra("_ID");
            id = Long.parseLong(a);
        }
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call PDF temp
                String ID=Long.toString(id);
                Intent intent=new Intent(getBaseContext(),PDF_Templet.class);
                intent.putExtra("_ID",ID);
                intent.putExtra("DATE",s);
                startActivity(intent);
            }
        });
    }


    public String SetDateString(String s) {
        String string = "";
        String[] items1 = s.split("-");
        String month = items1[0];
        String year = items1[1];
        if (month.equals("01") || month.equals("1")) {
            string = "January";
        } else if (month.equals("02") || month.equals("2")) {
            string = "February";
        } else if (month.equals("03") || month.equals("3")) {
            string = "March";
        } else if (month.equals("04") || month.equals("4")) {
            string = "April";
        } else if (month.equals("05") || month.equals("5")) {
            string = "May";
        } else if (month.equals("06") || month.equals("6")) {
            string = "June";
        } else if (month.equals("07") || month.equals("7")) {
            string = "July";
        } else if (month.equals("08") || month.equals("8")) {
            string = "Agust";
        } else if (month.equals("09") || month.equals("9")) {
            string = "September";
        } else if (month.equals("10")) {
            string = "October";
        } else if (month.equals("11")) {
            string = "November";
        } else if (month.equals("12")) {
            string = "December";
        } else {
            string = "INVALIDE MONTH";
        }

        String date = "";
        date = string + " " + year;
        return date;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(SetDateString(s));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
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
    /**
     * A placeholder fragment containing a simple view is deleted.
     */
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    Tab1 tab1=new Tab1(id,s);
                    return tab1;
                case 1:
                    Tab2 tab2=new Tab2(id);
                    return tab2;
                case 2:
                    Tab3 tab3=new Tab3(id);
                    return tab3;
                case 3:
                    Tab4 tab4=new Tab4(id,s);
                    return tab4;
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

    }
}
