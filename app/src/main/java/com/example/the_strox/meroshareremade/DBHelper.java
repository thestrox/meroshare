package com.example.the_strox.meroshareremade;



import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

       // import java.sql.Date;
        import java.util.Date;
        import java.text.SimpleDateFormat;

public class DBHelper  extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "mero_share_rename.db";
    public static final int DATABASE_VERSION = 20;

    public static final String TABLE_NAME = "Data";
    public static final String TABLE_NAME_2 = "Index_Data";
    public static final String TABLE_NAME_3 = "Password";
    public static final String TABLE_NAME_4 = "Portfolio";


    public static final String COL_1 = "SYMBOL";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "SECTOR";
    public static final String COL_4 = "LASTPRICE";
    public static final String COL_5 = "OPEN";
    public static final String COL_6 = "DIFFERENCE";
    public static final String COL_11 = "ID";
    public static final String COL_22 = "NEPSE_INDEX";
    public static final String COL_33 = "NEPSE_DIFF";
    public static final String COL_44 = "NEPSE_DATE";
    public static final String COL_id= "PASSWORDID";
    public static final String COL_password= "PASSWORD";
    public static final String COL_p_company = "P_COMPANY";
    public static final String COL_p_share = "P_SHARE";
    public static final String COL_p_price = "P_PRICE";
    public static final String COL_pid = "ID";

    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME_2 + "( ID INTEGER PRIMARY KEY,NEPSE_INDEX TEXT,NEPSE_DIFF TEXT,NEPSE_DATE TEXT)");
        db.execSQL("create table " + TABLE_NAME + "( SYMBOL TEXT PRIMARY KEY,NAME TEXT,SECTOR TEXT,LASTPRICE TEXT DEFAULT '0',OPEN TEXT DEFAULT '0',DIFFERENCE TEXT DEFAULT '0')");
        db.execSQL("create table " + TABLE_NAME_3 + "( PASSWORDID INTEGER PRIMARY KEY,PASSWORD TEXT)");
        db.execSQL("create table " + TABLE_NAME_4 + "( P_COMPANY TEXT,P_SHARE INTEGER,P_PRICE INTEGER,ID INTEGER PRIMARY KEY)");
        db.execSQL("insert into " + TABLE_NAME_3 + "( PASSWORDID,PASSWORD ) values (1,'1234')");
        db.execSQL("insert into " + TABLE_NAME_2 + "( ID,NEPSE_INDEX,NEPSE_DIFF ) values (1,'0','0')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_3);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_4);
        // Create tables again
        onCreate(db);

    }
    public void insertData(String symbol,String name, String sector){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_1, symbol);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, sector);
        db.insert(TABLE_NAME, null, contentValues);

    }
    public void insertData1(String symbol,String lastprice,String open, String difference, String id){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        ContentValues indexdata=new ContentValues();
        contentValues.put(COL_1,symbol);
        contentValues.put(COL_4,lastprice);
        contentValues.put(COL_5, open);
        contentValues.put(COL_6, difference);
        indexdata.put(COL_11, id);
        db.update(TABLE_NAME, contentValues, "SYMBOL = ?", new String[]{symbol});
    }

    public void insertindexData(String id,String nepse_index,String nepse_diff){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues indexdata=new ContentValues();
        indexdata.put(COL_11, id);
        indexdata.put(COL_22, nepse_index);
        indexdata.put(COL_33, nepse_diff);
        indexdata.put(COL_44, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        db.update(TABLE_NAME_2, indexdata, "ID = ?", new String[]{id});

    }

    public void insertportfolioData(String company,int share,int price,int i){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues portfolioData=new ContentValues();
        portfolioData.put(COL_p_company,company);
        portfolioData.put(COL_p_share,share);
        portfolioData.put(COL_p_price,price);
        portfolioData.put(COL_pid,i);

        db.insert(TABLE_NAME_4, null, portfolioData);

    }

    public void changepassword(String password){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues passwordData=new ContentValues();
        passwordData.put(COL_password,password);
        String id=1+"";
        db.update(TABLE_NAME_3, passwordData, "PASSWORDID = ?", new String[]{id});

    }

    public void deleteData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_2, null, null);
    }

    public void deletePortfolio(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_4, "ID = ?", new String[]{id});
    }
    public void deleteAllPortfolio(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_4, null, null);
    }

    public Cursor getName(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res =db.rawQuery("select "+COL_1+","+COL_2+ " from "+ TABLE_NAME,null);
        return res;
    }

    public Cursor getlistedData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getindexData(){
    SQLiteDatabase db=this.getWritableDatabase();
    Cursor res = db.rawQuery("select * from "+TABLE_NAME_2,null);
    return res;
    }


    public Cursor getpassword(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME_3,null);
        return res;
    }
    public Cursor test(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME_4,null);
        return res;
    }

    public Cursor getportfolioData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res = db.rawQuery("select "+ TABLE_NAME+ "."+ COL_1+ ","+ COL_4+ ","+ COL_5+ ","+ COL_p_share+","+ COL_p_price+","+ COL_pid+" from "+ TABLE_NAME+ ","+ TABLE_NAME_4+ " where "+ TABLE_NAME+ "."+ COL_1+ "="+ TABLE_NAME_4+ "."+ COL_p_company,null);
        return res;

    }
    public Cursor gethomeData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res = db.rawQuery("select "+ TABLE_NAME+ "."+ COL_1+ ","+ COL_4+ ","+ COL_5+ ","+ COL_6+ " from "+ TABLE_NAME+ ","+ TABLE_NAME_4+ " where "+ TABLE_NAME+ "."+ COL_1+ "="+ TABLE_NAME_4+ "."+ COL_p_company+" GROUP BY "+COL_1,null);
        return res;

    }

    public void updateportfolioData(String id,int share,int price){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues portfolioData=new ContentValues();
        portfolioData.put(COL_p_share,share);
        portfolioData.put(COL_p_price,price);
        db.update(TABLE_NAME_4, portfolioData, "ID = ?", new String[]{id});
    }
}
