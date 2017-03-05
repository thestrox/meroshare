package com.example.the_strox.meroshareremade;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public  class Common {
    private Context ctx;
    public static int user =0;

    public Common(Context context){
        ctx = context;
    }
    public boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

    public String getHtml(String url) {
        String html = "";
        if (isOnline()) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(url);

                HttpResponse response = client.execute(request);
                InputStream in = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder str = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
                in.close();
                html = str.toString();
            } catch (IOException e) {
            }
            return html;
        }
        else
            return null;

    }

    public void  downloaddata(){
        if (isOnline()) {
            Log.d(ctx.toString(), "Enteded downloaddata: ");
            DBHelper myDb;
            myDb = new DBHelper(ctx);
            String nepseindex, pointchange;
            String url = "http://www.nepalstock.com.np/company/index/1/stock-symbol/asc/YTo0OntzOjEwOiJzdG9jay1uYW1lIjtzOjA6IiI7czoxMjoic3RvY2stc3ltYm9sIjtzOjA6IiI7czo5OiJzZWN0b3ItaWQiO3M6MDoiIjtzOjY6Il9saW1pdCI7czozOiI1MDAiO30?stock-name=&stock-symbol=&sector-id=&_limit=500";
            String url1 = "http://www.nepalstock.com.np";
            String symbol, name, sector, last_price, open_price, diff, id;
            if(getHtml(url1)!=null){
                Log.d(ctx.toString(), "URl1 !=null: ");
                String html1 = getHtml(url1);
                Cursor result = myDb.getData();
                if (result.getCount() == 0) {
                    if (getHtml(url) != null) {
                        Log.d(ctx.toString(), "URl !=null: ");
                        String html = getHtml(url);
                        Document doc = Jsoup.parse(html);
                        for (Element table : doc.select("table[class=my-table table]")) {
                            for (Element row : table.select("tr:gt(1):lt(241)")) {
                                Elements tds = row.select("td");
                                symbol = tds.get(3).text();
                                name = tds.get(2).text();
                                sector = tds.get(4).text();
                                myDb.insertData(symbol, name, sector);
                            }
                        }
                    }
                }
                    Document doc1 = Jsoup.parse(html1);
                    Element index = doc1.select("div.current-index").first();
                    Element change = doc1.select("div.point-change").first();
                    nepseindex = index.getElementsByTag("div").text();
                    pointchange = change.getElementsByTag("div").text();
                    id = Character.toString('1');
                    myDb.insertindexData(id, nepseindex, pointchange);

                    for (Element content : doc1.select("div[class=col-xs-10 col-md-10 col-sm-12]")) {
                        Elements b = content.getElementsByTag("b");
                        String data = "";

                        data = b.text();

                        Float findopen;
                        String[] split = data.split("\\s+");
                        int len = split.length / 8;

                        int c = 0;
                        for (int i = 0; i < len; i++) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            symbol = split[0 + c];
                            last_price = split[1 + c].replace(",", "");
                            findopen = Float.parseFloat(split[1 + c].replace(",", "")) - Float.parseFloat(split[6 + c].replace(",", ""));
                            open_price = Float.toString(findopen);
                            diff = split[6 + c].replace(",", "");

                            myDb.insertData1(symbol, last_price, open_price, diff, id);
                            Log.d(ctx.toString(), "Inserted DATA: ");
                            c += 8;
                        }
                    }
                }
            }
    }


    public void showMessage(String title,String Message,int icon_id){
        AlertDialog.Builder builder=new AlertDialog.Builder(ctx);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setIcon(icon_id);
        builder.setMessage(Message);
        builder.show();
    }
    public void showMessage(String title,String Message){
        AlertDialog.Builder builder=new AlertDialog.Builder(ctx);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
