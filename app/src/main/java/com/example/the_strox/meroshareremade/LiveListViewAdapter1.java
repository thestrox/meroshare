package com.example.the_strox.meroshareremade;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class LiveListViewAdapter1 extends BaseAdapter implements Filterable{

    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    ArrayList<HashMap<String, String>> mStringFilterList;
    ValueFilter valueFilter;
    HashMap<String, String> resultp = new HashMap<String, String>();
    DBHelper myDb;

    public LiveListViewAdapter1(Context context,
                                ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
        mStringFilterList = arraylist;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.indexOf(getItem(position));
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
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

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }
    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                final ArrayList<HashMap<String, String>> filterList = new ArrayList<HashMap<String, String>>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if (mStringFilterList.get(i).get("company").toUpperCase()
                                    .contains(constraint.toString().toUpperCase()))
                    {
                        HashMap<String, String> resultpp = new HashMap<String, String>();
                        resultpp.put("company", mStringFilterList.get(i).get("company"));
                        resultpp.put("lastprice", mStringFilterList.get(i).get("lastprice"));
                        resultpp.put("open", mStringFilterList.get(i).get("open"));
                        resultpp.put("difference", mStringFilterList.get(i).get("difference"));
                        filterList.add(resultpp);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;

            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }

            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            data = (ArrayList<HashMap<String, String>>) results.values;
            notifyDataSetChanged();
        }
    }
}