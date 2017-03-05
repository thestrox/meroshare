package com.example.the_strox.meroshareremade;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class PortfolioActivity_changepassword extends AppCompatActivity {
    Common common=new Common(this);
    DBHelper myDb;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_change_password);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Password Change");
        myDb=new DBHelper(this);
        final EditText Oldpass =(EditText) findViewById(R.id.editText_old);
        final EditText Newpass =(EditText) findViewById(R.id.editText_new);
        Button Changepass = (Button) findViewById(R.id.button_changepass);
        Changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldpass=Oldpass.getText()+"";
                String newpass=Newpass.getText()+"";
                Cursor res=myDb.getpassword();
                res.moveToFirst();
                String correctpass=res.getString(1);
                Boolean validate= true;
                if (TextUtils.isEmpty(Oldpass.getText())) {
                    Oldpass.setError("Required");
                    validate=false;
                }

                // Body is required
                if (TextUtils.isEmpty(Newpass.getText())) {
                    Newpass.setError("Required");
                    validate=false;
                }

                if (validate==false) return;
                 if (oldpass.equals(correctpass)){
                    if(newpass.length()<4){
                        Newpass.setError("Password must have at least 4 characters");
                    }
                    else{
                        myDb.changepassword(newpass);
                        Intent intent = new Intent(PortfolioActivity_changepassword.this, PortfolioActivity_login.class);
                        Toast.makeText(getApplicationContext(), "Password Changed", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    }
                }
                else if (!oldpass.equals(correctpass))
                Oldpass.setError("Incorrect Old password");

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), PortfolioActivity_login.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
