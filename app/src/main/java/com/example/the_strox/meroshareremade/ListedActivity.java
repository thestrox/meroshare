package com.example.the_strox.meroshareremade;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;
import java.util.HashMap;

public class ListedActivity extends BaseActivity {
    ListView listview;
    ListedListViewAdapter adapter;
    ProgressDialog mProgressDialog;
    ArrayList<HashMap<String, String>> arraylist;
    static String Symbol = "symbol";
    static String Name = "name";
    static String Sector = "sector";
    static String Last_price = "last_price";
    SearchView searchView;
    private CircularProgressView progressView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    DBHelper myDb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_listed, contentFrameLayout);
        getSupportActionBar().setTitle("Listed Company");
        navigationView.getMenu().getItem(4).setChecked(true);
        myDb = new DBHelper(this);

        mSwipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        progressView =(CircularProgressView)findViewById(R.id.progress_view);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent=new Intent(getApplicationContext(),ListedActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        arraylist = new ArrayList<HashMap<String, String>>();
        String symbol, name, sector, last_price;
        Cursor res = myDb.getlistedData();
        res.moveToFirst();
        for (int i = 0; i < res.getCount(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            symbol = res.getString(0);
            name = res.getString(1);
            sector = res.getString(2);
            last_price = res.getString(3);
            map.put("symbol", symbol);
            map.put("name", name);
            map.put("sector", sector);
            map.put("last_price", last_price);
            arraylist.add(map);
            res.moveToNext();
        }
        listview = (ListView) findViewById(R.id.ListedListView);
        adapter = new ListedListViewAdapter(ListedActivity.this, arraylist);
        listview.setEmptyView(findViewById(android.R.id.empty));
        listview.setAdapter(adapter);

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

        int id = item.getItemId();
        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

}
