package com.example.the_strox.meroshareremade;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class ListedListViewAdapter extends BaseAdapter implements Filterable {


    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    ArrayList<HashMap<String, String>> mStringFilterList;
    ValueFilter valueFilter;

    HashMap<String, String> resultp = new HashMap<String, String>();

    public ListedListViewAdapter(Context context,
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
        // Declare Variables
        TextView symbol;
        TextView name;
        TextView sector;
        TextView last_price;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.item_listed, parent, false);
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml
        symbol = (TextView) itemView.findViewById(R.id.symbol);
        name = (TextView) itemView.findViewById(R.id.name);
        sector = (TextView) itemView.findViewById(R.id.sector);
        last_price= (TextView) itemView.findViewById(R.id.lastprice);

        symbol.setText(resultp.get(ListedActivity.Symbol));
        name.setText(resultp.get(ListedActivity.Name));
        sector.setText(resultp.get(ListedActivity.Sector));
        last_price.setText(resultp.get(ListedActivity.Last_price));
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
                    if ((mStringFilterList.get(i).get("name").toUpperCase())
                            .contains(constraint.toString().toUpperCase())||
                            mStringFilterList.get(i).get("symbol").toUpperCase()
                            .contains(constraint.toString().toUpperCase()))
                    {
                        HashMap<String, String> resultpp = new HashMap<String, String>();
                        resultpp.put("symbol", mStringFilterList.get(i).get("symbol"));
                        resultpp.put("name", mStringFilterList.get(i).get("name"));
                        resultpp.put("sector", mStringFilterList.get(i).get("sector"));
                        resultpp.put("last_price", mStringFilterList.get(i).get("last_price"));
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