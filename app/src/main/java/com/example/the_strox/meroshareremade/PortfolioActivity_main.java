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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class PortfolioActivity_main extends BaseActivity {
    Common common=new Common(this);
    DBHelper myDb;
    int totalshare=0,totalinvestment=0,totalworth=0,pl,tpl=0,profit;
    ListView listview;
    PortfolioListViewAdapter adapter;
    ProgressDialog mProgressDialog;
    ArrayList<HashMap<String, String>> arraylist;
    FloatingActionButton fab_add;
    static String Id="id";
    static String Symbol = "symbol";
    static String Lastprice = "lastprice";
    static String Buyprice = "buyprice";
    static String Shares = "shares";
    static String Profit = "profit";
    private CircularProgressView progressView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int last_price,open_price,share,price,id;

        String symbol;
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_porfolio_main, contentFrameLayout);
        getSupportActionBar().setTitle("Your Porfolio");
        navigationView.getMenu().getItem(2).setChecked(true);
        myDb = new DBHelper(this);



        arraylist = new ArrayList<HashMap<String, String>>();

        TextView total_investment = (TextView) findViewById(R.id.textView_investment);
        TextView total_worth  = (TextView) findViewById(R.id.textView_worth);
        TextView p_l = (TextView) findViewById(R.id.textView_pl);
        TextView t_p_l = (TextView) findViewById(R.id.textView_tpl);

        fab_add=(FloatingActionButton) findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PortfolioActivity_main.this, PortfolioActivity_add.class);
                startActivity(intent);
            }
        });

        mSwipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        progressView =(CircularProgressView)findViewById(R.id.progress_view);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent=new Intent(getApplicationContext(),PortfolioActivity_main.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        if (common.isOnline()) {
            progressView.startAnimation();

            new Livemain().execute();
        }
        if(!common.isOnline()) {
            Toast.makeText(getApplicationContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
        }
            Cursor res =myDb.getportfolioData();
            res.moveToFirst();

            if (res.getCount() != 0) {

                for(int i=0;i<res.getCount();i++){
                    HashMap<String, String> map = new HashMap<String, String>();
                    symbol=res.getString(0);
                    last_price=res.getInt(1);
                    open_price=res.getInt(2);
                    share=res.getInt(3);
                    price=res.getInt(4);
                    id=res.getInt(5);
                    totalshare=totalshare+share;
                    totalinvestment=totalinvestment+share*price;
                    totalworth=totalworth+share*last_price;
                    tpl=tpl+((share*last_price)-(share*open_price));
                    profit=((share*last_price)-(share*price));
                    map.put("symbol",symbol);
                    map.put("lastprice", Integer.toString(last_price));
                    map.put("buyprice", Integer.toString(price));
                    map.put("shares", Integer.toString(share));
                    map.put("profit", Integer.toString(profit));
                    map.put("id", Integer.toString(id));
                    arraylist.add(map);
                    res.moveToNext();
                }
                pl=totalworth-totalinvestment;
                DecimalFormat formatter = new DecimalFormat("#,##,###");
                total_investment.setText("Rs. "+ String.valueOf(formatter.format(totalinvestment) ));
                total_worth.setText("Rs. "+ String.valueOf(formatter.format(totalworth)));

                String profit_loss = String.valueOf(pl);
                if(profit_loss.charAt(0)=='-'){
                    p_l.setTextColor(Color.parseColor("#D32F2F"));}
                else if(profit_loss.charAt(0)=='0'){
                    p_l.setTextColor(Color.parseColor("#F5F5F5"));}
                else{
                    p_l.setTextColor(Color.parseColor("#388E3C"));}

                p_l.setText("Rs. " + String.valueOf(formatter.format(pl)));

                String todays_profit_loss= String.valueOf(tpl);
                if(todays_profit_loss.charAt(0)=='-'){
                    t_p_l.setTextColor(Color.parseColor("#D32F2F"));}
                else if(todays_profit_loss.charAt(0)=='0'){
                    t_p_l.setTextColor(Color.parseColor("#F5F5F5"));}
                else{
                    t_p_l.setTextColor(Color.parseColor("#388E3C"));}
                t_p_l.setText("Rs. "+ String.valueOf(formatter.format(tpl) ));

                Cursor res_index=myDb.getindexData();
                res_index.moveToFirst();

                TextView txtdate = (TextView)appBarLayout. findViewById(R.id.toolbar_date);
                txtdate.setVisibility(View.VISIBLE);
                txtdate.setText("As of "+res_index.getString(3));

                listview = (ListView) findViewById(R.id.listView_portofolio);
                // Pass the results into ListViewAdapter.java
                adapter = new PortfolioListViewAdapter(PortfolioActivity_main.this, arraylist);
                // Set the adapter to the ListView
                listview.setEmptyView(findViewById(R.id.empty_view));
                listview.setAdapter(adapter);
            }


    }

    public class Livemain extends AsyncTask<Void, Void, Void> {
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
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }
}
