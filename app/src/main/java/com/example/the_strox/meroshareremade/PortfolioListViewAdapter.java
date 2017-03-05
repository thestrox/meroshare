package com.example.the_strox.meroshareremade;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class PortfolioListViewAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    DBHelper myDb;
    HashMap<String, String> resultp = new HashMap<String, String>();

    public PortfolioListViewAdapter(Context context,
                                    ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // Declare Variables
        TextView symbol;
        TextView today;
        TextView buying;
        TextView shares;
        TextView profit;
        ImageView up;
        ImageView down;
        ImageView same;
        myDb = new DBHelper(context);
        final Common common=new Common(context);

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.item_portfolio, parent, false);
        // Get the position
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml
        symbol = (TextView) itemView.findViewById(R.id.textView_symbol);
        today = (TextView) itemView.findViewById(R.id.textView_lastprice);
        buying = (TextView) itemView.findViewById(R.id.textView_buyprice);
        shares = (TextView) itemView.findViewById(R.id.textView_shares);
        profit = (TextView) itemView.findViewById(R.id.textView_profit);


        DecimalFormat formatter = new DecimalFormat("#,##,###");
        // Capture position and set results to the TextViews
        symbol.setText(resultp.get(PortfolioActivity_main.Symbol));
        today.setText(String.valueOf(formatter.format(Integer.parseInt(resultp.get(PortfolioActivity_main.Lastprice)))));
        buying.setText(String.valueOf(formatter.format(Integer.parseInt(resultp.get(PortfolioActivity_main.Buyprice) )) ));
        shares.setText(String.valueOf(formatter.format(Integer.parseInt(resultp.get(PortfolioActivity_main.Shares))) ));
        profit.setText(String.valueOf(formatter.format(Integer.parseInt(resultp.get(PortfolioActivity_main.Profit))) ));
        String test=resultp.get(PortfolioActivity_main.Profit);
        up= (ImageView) itemView.findViewById(R.id.up);
        down= (ImageView) itemView.findViewById(R.id.down);
        same= (ImageView) itemView.findViewById(R.id.same);

        if(test.charAt(0)=='-') {
            profit.setTextColor(Color.parseColor("#FF0000"));
            down.setVisibility(View.VISIBLE);
        }
        else if(test.charAt(0)=='0') {
            profit.setTextColor(Color.parseColor("#0000ff"));
            same.setVisibility(View.VISIBLE);
        }
        else {
            profit.setTextColor(Color.parseColor("#00DD00"));
            up.setVisibility(View.VISIBLE);
        }

        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                resultp=data.get(position);
                String name="";
                String symbol=resultp.get(PortfolioActivity_main.Symbol);
                //common.showMessage(symbol,name,context);
                Cursor res = myDb.getName();
                res.moveToFirst();
                for (int i=0;i<res.getCount();i++){
                    if (res.getString(0).equals(symbol)){
                        name=res.getString(1);
                        Toast.makeText(context,name, Toast.LENGTH_SHORT).show();
                        break;}
                    res.moveToNext();
                }
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                        context);
                builderSingle.setTitle("Select Option:-");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,
                        android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add("Edit");
                arrayAdapter.add("Delete");
                arrayAdapter.add("Delete All");
                builderSingle.setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builderSingle.setAdapter(arrayAdapter,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strName = arrayAdapter.getItem(which);
                                final AlertDialog.Builder builderInner = new AlertDialog.Builder(
                                        context);
                                if(strName.equals("Edit")){
                                    resultp=data.get(position);
                                    final String symbol=resultp.get(PortfolioActivity_main.Symbol);
                                    Cursor res = myDb.getName();
                                    res.moveToFirst();
                                    for (int i=0;i<res.getCount();i++){
                                        if (res.getString(0).equals(symbol)){
                                            String name=res.getString(1);

                                            final View view = LayoutInflater.from(context).inflate(R.layout.portfolio_edit, null);

                                            builderInner.setTitle(name);
                                            builderInner.setView(view);
                                            builderInner.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int which) {
                                                            EditText edit_share =(EditText)view.findViewById(R.id.editText_share);
                                                             EditText edit_price =(EditText)view.findViewById(R.id.editText_price);
                                                            if(edit_share.getText().length()!=0&&edit_price.getText().length()!=0){
                                                                final int share = Integer.parseInt(edit_share.getText().toString());
                                                                final int price = Integer.parseInt(edit_price.getText().toString());
                                                                resultp=data.get(position);
                                                                String id=resultp.get(PortfolioActivity_main.Id);
                                                                myDb.updateportfolioData(id, share, price);
                                                                Intent intent = new Intent(context, PortfolioActivity_main.class);
                                                                context.startActivity(intent);
                                                            }
                                                            else{
                                                                Toast.makeText(context, "Enter all details", Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                            break;}
                                        res.moveToNext();
                                    }

                                }
                                else if (strName.equals("Delete")){
                                    resultp=data.get(position);
                                    String id=resultp.get(PortfolioActivity_main.Id);
                                    myDb.deletePortfolio(id);
                                    Intent intent = new Intent(context,PortfolioActivity_main.class);
                                    context.startActivity(intent);

                                }
                                else if(strName.equals("Delete All")){
                                    myDb.deleteAllPortfolio();
                                    Intent intent = new Intent(context,PortfolioActivity_main.class);
                                    context.startActivity(intent);
                                }

                                builderInner.show();
                            }
                        });
                builderSingle.show();

            }
        });

        return itemView;
    }

}