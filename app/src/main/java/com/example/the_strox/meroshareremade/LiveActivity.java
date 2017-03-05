package com.example.the_strox.meroshareremade;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import com.example.the_strox.meroshareremade.Common;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

public class LiveActivity extends BaseActivity {
    ListView listview;
    LiveListViewAdapter1 adapter;
    ProgressDialog mProgressDialog;

    ArrayList<HashMap<String, String>> arraylist;
    ArrayList<HashMap<String, String>> arraylist1;
    static String Company = "company";
    static String Lastprice = "lastprice";
    static String Open = "open";
    static String Difference = "difference";
    SearchView searchView;
    Common common=new Common(this);
    DBHelper myDb;
    private CircularProgressView progressView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout linearLayout;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from listview_main.xml
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.live1, contentFrameLayout);
        // get Instance  of Database Adapter
        getSupportActionBar().setTitle("Live Stock");
        navigationView.getMenu().getItem(1).setChecked(true);

        myDb=new DBHelper(this);

        mSwipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        progressView =(CircularProgressView)findViewById(R.id.progress_view);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Intent intent=new Intent(getApplicationContext(),LiveActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        if (common.isOnline()) {
            //myDb.deleteData();
            progressView.startAnimation();
            new LiveListView().execute();
        }
       if(!common.isOnline()) {
            Toast.makeText(getApplicationContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
       }
            String comp, last, openn, diff;
            arraylist1 = new ArrayList<HashMap<String, String>>();
            Cursor res=myDb.getData();
            Cursor res2=myDb.getindexData();
            res.moveToFirst();
            res2.moveToFirst();

            for(int i=0;i<res.getCount();i++){
                HashMap<String, String> mapp = new HashMap<String, String>();
                comp = res.getString(0);
                last = res.getString(3);
                openn = res.getString(4);
                diff = res.getString(5);
                mapp.put("company", comp);
                mapp.put("lastprice", last);
                mapp.put("open", openn);
                mapp.put("difference", diff);
                arraylist1.add(mapp);
                res.moveToNext();
            }
            listview = (ListView) findViewById(R.id.LiveListView);
            adapter = new LiveListViewAdapter1(LiveActivity.this, arraylist1);
            listview.setAdapter(adapter);
            TextView txtindex = (TextView) findViewById(R.id.textView_indexlabel);
            txtindex.setText(res2.getString(1));
            TextView txtchange = (TextView) findViewById(R.id.textView_diff);
            txtchange.setText(res2.getString(2));
            TextView txtdate = (TextView)appBarLayout. findViewById(R.id.toolbar_date);
             txtdate.setVisibility(View.VISIBLE);
            txtdate.setText("As of "+res2.getString(3));
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
    public class LiveListView extends AsyncTask<Void, Void, Void> {

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
            protected void onPostExecute (Void result){
                progressView.stopAnimation();
                progressView.setVisibility(View.GONE);
            }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listed, menu);
        final MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

}