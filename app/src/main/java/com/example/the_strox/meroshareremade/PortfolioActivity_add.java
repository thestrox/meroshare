package com.example.the_strox.meroshareremade;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;


public class PortfolioActivity_add extends AppCompatActivity {
    EditText Ecompany;
    String[] allcompany;
    DBHelper myDb;
    ArrayAdapter<String> arrayAdapter;
    Common common= new Common(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_add1);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Portfolio");

        myDb=new DBHelper(this);
        Button add=(Button)findViewById(R.id.button_add);

        Cursor res=myDb.getData();
        res.moveToFirst();
        allcompany = new String[res.getCount()];
        for(int i=0;i<res.getCount();i++) {
            allcompany[i]=res.getString(1)+'('+ res.getString(0) +')';
            res.moveToNext();
        }
        Ecompany=(EditText)findViewById(R.id.editText_company);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, allcompany);
        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Ecompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog builderSingle = new AlertDialog.Builder(
                        PortfolioActivity_add.this).create();

                arrayAdapter = new ArrayAdapter<String>(PortfolioActivity_add.this, R.layout.portfolio_add_listview_item,allcompany);

                final View view = LayoutInflater.from(PortfolioActivity_add.this).inflate(R.layout.add_company_list, null);
                builderSingle.setTitle("Select Company:-");
                builderSingle.setView(view);
                ListView list = (ListView) view.findViewById(R.id.listView_company);
                list.setEmptyView(view.findViewById(android.R.id.empty));
                list.setAdapter(arrayAdapter);

                EditText search_company=(EditText)view.findViewById(R.id.editText_search);
                search_company.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        PortfolioActivity_add.this.arrayAdapter.getFilter().filter(s);

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                builderSingle.show();
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String company_name=arrayAdapter.getItem(position);
                        Ecompany.setText(company_name);
                        Ecompany.clearFocus();
                        builderSingle.dismiss();
                    }
                });
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText share = (EditText)findViewById(R.id.editText_share);
                EditText price = (EditText) findViewById(R.id.editText_price);
                Boolean validate =true;
                if (TextUtils.isEmpty(share.getText())) {
                    share.setError("Required");
                    validate=false;
                }

                if (TextUtils.isEmpty(price.getText())) {
                    price.setError("Required");
                    validate=false;
                }
                if (TextUtils.isEmpty(Ecompany.getText())) {
                    Ecompany.setError("Required");
                    validate=false;

                }
                if (validate==false) return;
                    String company = Ecompany.getText().toString();
                    int share1 = Integer.parseInt(share.getText().toString());
                    int price1 = Integer.parseInt(price.getText().toString());
                    String symbol;
                    symbol = company.split("[\\(\\)]")[1];

                    Cursor res =myDb.test();
                    int i;
                    if(res.getCount()==0){
                        i=0;
                    }
                    else     {
                        res.moveToLast();
                        i=res.getInt(3);
                    }
                    myDb.insertportfolioData(symbol,share1,price1,i+1);
                    Intent intent = new Intent(PortfolioActivity_add.this, PortfolioActivity_main.class);
                    startActivity(intent);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), PortfolioActivity_main.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}