package com.example.the_strox.meroshareremade;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class LiveListViewAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    DBHelper myDb;



    public LiveListViewAdapter(Context context,
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
        TextView company;
        TextView lastprice;
        TextView open;
        TextView difference;
        ImageView up;
        ImageView down;
        ImageView same;
        myDb = new DBHelper(context);
        final Common common = new Common(context);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

       View itemView = inflater.inflate(R.layout.item_live, parent, false);
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml
        company = (TextView) itemView.findViewById(R.id.company);
        lastprice = (TextView) itemView.findViewById(R.id.lastprice);
        open = (TextView) itemView.findViewById(R.id.open);
        difference = (TextView) itemView.findViewById(R.id.difference);
        up= (ImageView) itemView.findViewById(R.id.up);
        down= (ImageView) itemView.findViewById(R.id.down);
        same= (ImageView) itemView.findViewById(R.id.same);

        company.setText(resultp.get(LiveActivity.Company));
        lastprice.setText(resultp.get(LiveActivity.Lastprice));
        open.setText(resultp.get(LiveActivity.Open));
        difference.setText(resultp.get(LiveActivity.Difference));
        String test=resultp.get(LiveActivity.Difference);
        if(test.charAt(0)=='-') {
            difference.setTextColor(Color.parseColor("#FF0000"));
            down.setVisibility(View.VISIBLE);
        }
        else if(test.charAt(0)=='0'){
            difference.setTextColor(Color.parseColor("#0000ff"));
            same.setVisibility(View.VISIBLE);
        }
        else {
            difference.setTextColor(Color.parseColor("#00DD00"));
            up.setVisibility(View.VISIBLE);
        }
        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                resultp=data.get(position);
                String name="";
                String symbol=resultp.get(LiveActivity.Company);
                Cursor res = myDb.getName();
                res.moveToFirst();
                for (int i=0;i<res.getCount();i++){
                    if (res.getString(0).equals(symbol)){
                        name=res.getString(1);
                        Toast.makeText(context,name, Toast.LENGTH_SHORT).show();
                        break;}
                    res.moveToNext();
                }
            }
        });

        return itemView;
    }
}