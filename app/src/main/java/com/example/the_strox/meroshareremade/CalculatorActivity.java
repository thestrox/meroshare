package com.example.the_strox.meroshareremade;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;


public class CalculatorActivity extends BaseActivity {
    Common common =new Common(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_calculator1, contentFrameLayout);

        getSupportActionBar().setTitle("Calculator");
        navigationView.getMenu().getItem(3).setChecked(true);
        final EditText share=(EditText)findViewById(R.id.editText_share);
        final EditText buy=(EditText)findViewById(R.id.editText_buy);
        final EditText sell=(EditText)findViewById(R.id.editText_sell);
        final TextView amt=(TextView)findViewById(R.id.textView_amt);
        final TextView broker=(TextView)findViewById(R.id.textView_broker);
        final TextView name=(TextView)findViewById(R.id.textView_name);
        final TextView sebon=(TextView)findViewById(R.id.textView_sebon);
        final TextView capital=(TextView)findViewById(R.id.textView_capital);
        final TextView totalamt=(TextView)findViewById(R.id.textView_totalamt);
        final TextView pl=(TextView)findViewById(R.id.textView_pl);
        final Button calculate =(Button)findViewById(R.id.button_calculate);
        final RadioGroup radio_group=(RadioGroup)findViewById(R.id.radio_group);
        final RadioButton buying=(RadioButton)findViewById(R.id.radioButton_buy);
        final RadioButton selling=(RadioButton)findViewById(R.id.radioButton_sell);
        final RelativeLayout data=(RelativeLayout) findViewById(R.id.data) ;


        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == buying.getId()) {
                    sell.setVisibility(View.GONE);

                } else if (checkedId == selling.getId()) {
                    sell.setVisibility(View.VISIBLE);

                }
            }
        });
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DecimalFormat formatter = new DecimalFormat("#,##,###.00");
                if (radio_group.getCheckedRadioButtonId() == buying.getId()) {
                    if(share.getText().length()!=0&&buy.getText().length()!=0){
                        int int_amt = Integer.parseInt(share.getText().toString()) * Integer.parseInt(buy.getText().toString());
                        amt.setText("Rs. "+formatter.format(int_amt));
                        double int_broker;
                        if (int_amt <= 50000) {
                            int_broker = int_amt * .01;
                            broker.setText("+ Rs. " +formatter.format(int_broker));
                        } else if (int_amt > 50000 && int_amt <= 500000) {
                            int_broker = int_amt * .009;
                            broker.setText("+ Rs. " + formatter.format(int_broker));
                        } else if (int_amt > 500000 && int_amt <= 1000000) {
                            int_broker = int_amt * .008;
                            broker.setText("+ Rs. " + formatter.format(int_broker));
                        }   else {
                            int_broker = int_amt * .007;
                            broker.setText("+ Rs. " + formatter.format(int_broker));
                        }
                        name.setText("Rs. 0.00");
                        double int_sebon = int_amt * .00015;
                        sebon.setText("+ Rs. " + formatter.format(int_sebon));
                        capital.setText("Rs. 0.00");
                        double int_totalamt = int_amt + int_broker + 5 + int_sebon;
                        totalamt.setText("Rs. "+formatter.format( int_totalamt));
                        pl.setText("N/A");
                        data.setVisibility(View.VISIBLE);
                    }
                    else
                        common.showMessage("Caution","- Please Enter all details.",R.drawable.ic_caution);
                } else if (radio_group.getCheckedRadioButtonId() == selling.getId()) {
                    if(share.getText().length()!=0&&buy.getText().length()!=0&&sell.getText().length()!=0){
                    int int_amt = Integer.parseInt(share.getText().toString()) * Integer.parseInt(sell.getText().toString());
                    amt.setText("Rs."+formatter.format(int_amt));
                    double int_broker;
                    if (int_amt <= 50000) {
                        int_broker = int_amt * .01;
                        broker.setText("- Rs. " + formatter.format(int_broker));
                    } else if (int_amt > 50000 && int_amt <= 500000) {
                        int_broker = int_amt * .009;
                        broker.setText("- Rs. " + formatter.format(int_broker));
                    } else if (int_amt > 500000 && int_amt <= 1000000) {
                        int_broker = int_amt * .008;
                        broker.setText("- Rs. " + formatter.format(int_broker));
                    } else {
                        int_broker = int_amt * .007;
                        broker.setText("- Rs. " + formatter.format(int_broker));
                    }
                    name.setText("Rs. 0.00");
                    double int_sebon = int_amt * .00015;
                    sebon.setText("- Rs. " + formatter.format(int_sebon));
                    double int_beforetotalamt = int_amt - int_broker - int_sebon;
                    int purchase_price = Integer.parseInt(share.getText().toString()) * Integer.parseInt(buy.getText().toString());
                        double int_bbroker;
                        if (purchase_price <= 50000) {
                            int_bbroker = purchase_price * .01;
                        } else if (purchase_price > 50000 && purchase_price <= 500000) {
                            int_bbroker = purchase_price * .009;

                        } else if (purchase_price > 500000 && purchase_price <= 1000000) {
                            int_bbroker = purchase_price * .008;

                        }   else {
                            int_bbroker = purchase_price * .007;

                        }
                        double int_bsebon = purchase_price * .00015;
                        double total_purchase = purchase_price + int_bbroker +5+ int_bsebon;
                        double int_capital;
                    if (int_beforetotalamt < total_purchase) {
                        int_capital = 0;
                        capital.setText("Rs. 0.00");
                    } else {
                        int_capital = (int_beforetotalamt-total_purchase) * .05;
                        capital.setText("- Rs. "+ formatter.format(int_capital));
                    }
                    double int_totalamt = int_beforetotalamt - int_capital;
                    totalamt.setText("Rs. "+formatter.format(int_totalamt));
                    double int_pl = int_totalamt - total_purchase;
                    pl.setText("Rs. "+formatter.format(int_pl));
                        data.setVisibility(View.VISIBLE);
                    }
                    else
                        common.showMessage("Caution", "- Please Enter all details.", R.drawable.ic_caution);
                }
                if (share.getText().length()!=0&&buy.getText().length()!=0&&sell.getText().length()!=0){
                    if(broker.getText().toString().charAt(0)=='-'){
                        broker.setTextColor(Color.parseColor("#D32F2F"));}
                    else if(broker.getText().toString().charAt(0)=='0'){
                        }
                    else{
                     broker.setTextColor(Color.parseColor("#388E3C"));}
                    if(name.getText().toString().charAt(0)=='-'){
                        name.setTextColor(Color.parseColor("#D32F2F"));}
                    else if(name.getText().toString().charAt(0)=='0'){
                        }
                    else{
                        name.setTextColor(Color.parseColor("#388E3C"));}
                    if(sebon.getText().toString().charAt(0)=='-'){
                        sebon.setTextColor(Color.parseColor("#D32F2F"));}
                    else if(sebon.getText().toString().charAt(0)=='0'){
                        }
                    else{
                        sebon.setTextColor(Color.parseColor("#388E3C"));}
                    if(capital.getText().toString().charAt(0)=='-'){
                        capital.setTextColor(Color.parseColor("#D32F2F"));}
                    else if(capital.getText().toString().charAt(0)=='0'){
                        }
                    else{
                        capital.setTextColor(Color.parseColor("#388E3C"));}
                    if(totalamt.getText().toString().charAt(0)=='-'){
                        totalamt.setTextColor(Color.parseColor("#D32F2F"));}
                    else if(totalamt.getText().toString().charAt(0)=='0'){
                    }
                     else{
                        totalamt.setTextColor(Color.parseColor("#388E3C"));}
                    if(pl.getText().toString().charAt(0)=='-'){
                        pl.setTextColor(Color.parseColor("#D32F2F"));}
                    else if(pl.getText().toString().charAt(0)=='0'|| String.valueOf(pl).charAt(0)=='N'){
                     }
                    else{
                        pl.setTextColor(Color.parseColor("#388E3C"));
                    }
                }
            }
        });

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
