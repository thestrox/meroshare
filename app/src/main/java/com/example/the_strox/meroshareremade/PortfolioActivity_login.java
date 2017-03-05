package com.example.the_strox.meroshareremade;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;


public class PortfolioActivity_login extends AppCompatActivity {
    DBHelper myDb;
    Common common =new Common(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_login);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sign in");
        myDb=new DBHelper(this);

        final EditText Password = (EditText) findViewById(R.id.text_password);
        Button buttonlogin = (Button) findViewById(R.id.button_login);
        Button buttonchangepass=(Button)findViewById(R.id.button_changepassword);
        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password=Password.getText()+"";
                Cursor res=myDb.getpassword();
                res.moveToFirst();
                Boolean validate= true;
                if (TextUtils.isEmpty(Password.getText())) {
                    Password.setError("Required");
                    validate=false;
                }
                if (validate==false) return;

                if(password.equals(res.getString(1))){
                    Common.user=1;
                    Intent intent = new Intent(PortfolioActivity_login.this, PortfolioActivity_main.class);
                    startActivity(intent);
                }
                else{
                    Password.setError("Incorrect password");

                }
            }
        });

        buttonchangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PortfolioActivity_login.this, PortfolioActivity_changepassword.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}

