package com.example.the_strox.meroshareremade;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;
import java.util.HashMap;


public class HomeActivity extends BaseActivity {
    ListView listview;
    LiveListViewAdapter adapter;
    ProgressDialog mProgressDialog;
    ArrayList<HashMap<String, String>> arraylist1;
    static String Company = "company";
    static String Lastprice = "lastprice";
    static String Open = "open";
    static String Difference = "difference";
    Common common=new Common(HomeActivity.this);
    private CircularProgressView progressView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout linearLayout;

    DBHelper myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_home.xml
        getLayoutInflater().inflate(R.layout.live1, contentFrameLayout);
        getSupportActionBar().setTitle("Watch List");
        navigationView.getMenu().getItem(0).setChecked(true);
        myDb=new DBHelper(this);

        mSwipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        progressView =(CircularProgressView)findViewById(R.id.progress_view);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Intent intent=new Intent(getApplicationContext(),HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        if (common.isOnline()) {

            progressView.startAnimation();
            new insertdata().execute();

        }
        if(!common.isOnline()) {
            Toast.makeText(getApplicationContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
        }

        String comp, last, openn, diff;
        arraylist1 = new ArrayList<HashMap<String, String>>();


        Cursor res=myDb.gethomeData();
        Cursor res2=myDb.getindexData();
        res.moveToFirst();
        res2.moveToFirst();

        for(int i=0;i<res.getCount();i++){
            HashMap<String, String> mapp = new HashMap<String, String>();
            comp = res.getString(0);
            last = res.getString(1);
            openn = res.getString(2);
            diff = res.getString(3);

            mapp.put("company", comp);
            mapp.put("lastprice", last);
            mapp.put("open", openn);
            mapp.put("difference", diff);
            arraylist1.add(mapp);
            res.moveToNext();
        }
        Cursor res_index=myDb.getindexData();
        res_index.moveToFirst();

        listview = (ListView) findViewById(R.id.LiveListView);
        // Pass the results into ListViewAdapter.java
        adapter = new LiveListViewAdapter(HomeActivity.this, arraylist1);
        // Set the adapter to the ListView
        listview.setEmptyView(findViewById(android.R.id.empty));
        listview.setAdapter(adapter);
        TextView txtindex = (TextView) findViewById(R.id.textView_indexlabel);
        txtindex.setText(res2.getString(1));
        TextView txtchange = (TextView) findViewById(R.id.textView_diff);
        txtchange.setText(res2.getString(2));
        TextView txtdate = (TextView)appBarLayout. findViewById(R.id.toolbar_date);
        txtdate.setVisibility(View.VISIBLE);
        txtdate.setText("As of "+res_index.getString(3));
        TextView txtopen=(TextView) findViewById(R.id.textView_openlabel);
        float x= Float.parseFloat(res2.getString(1).replace(",",""))-Float.parseFloat(res2.getString(2).replace(",",""));
        txtopen.setText(Float.toString(x));


        linearLayout = (LinearLayout) findViewById(R.id.nepse);

        if (txtchange.getText().toString().charAt(0) == '-') {
            linearLayout.setBackgroundColor(Color.parseColor("#FF0000"));
        } else if (txtchange.getText().toString().charAt(0) == '0') {
            linearLayout.setBackgroundColor(Color.parseColor("#0000FF"));
        } else {
            linearLayout.setBackgroundColor(Color.parseColor("#00AF00"));
        }


    }

    public class insertdata extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... params) {

            common.downloaddata();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressView.stopAnimation();
            progressView.setVisibility(View.GONE);
        }


        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }



}


