package com.example.mediccompanion.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbAdapter {
    static final String DATABASENAME = "medication";
    static final String TABLE = "medicTable";
    static final String ROWID = "rowId";
    static final String DRUGNAME = "name";
    static final String DRUGTYPE = "type";
    static final String ROWIDTIME = "rowId";
    static final String WAKETIME = "Wake";
    static final String SLEEPTIME = "sleep";
    static final String TIMETABLE = "timeTable";
    static final String PROGTABLE = "progress";
    static final String ROWIDDATE = "rowId";
    static final String DAYtABLE = "day";
    static final String ROWIDPROGRESS = "rowId";
    static final String VALUE = "value";
    static final String DRUGAMOUNT = "quantity";
    static final String AMOUNTPERDOSE = "amount";
    static final String NOOFDOSAGES = "number";
    static final String DATE = "date";
    static final int DATABASEVERSION = 2;
    static final String CREATE = "CREATE TABLE "+TABLE+"(_id integer primary key autoincrement not null, name text not null, type text not null, quantity int not null, amount int not null, number int not null);";
    static final String CREATEP = "CREATE TABLE "+PROGTABLE+"(_id integer primary key autoincrement not null, value int not null);";
    static final String CREATETIMETABLE = "CREATE TABLE "+TIMETABLE+"(_id integer primary key autoincrement not null, wakeTime text not null, sleepTime text not null);";
    static final String  CREATEDAY = "CREATE TABLE " + DAYtABLE + "(_id integer primary key autoincrement not null, date int not null);";

    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    final Context context;

    public DbAdapter(Context ctx){
        this.context = ctx;
        dbHelper = new DatabaseHelper(context, DATABASENAME, null, DATABASEVERSION);
    }
    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                db.execSQL(CREATE);
                db.execSQL(CREATEP);
                db.execSQL(CREATEDAY);
                db.execSQL(CREATETIMETABLE);
                ContentValues contentValues = new ContentValues();
                contentValues.put(VALUE, 0);
                db.insert(PROGTABLE, null, contentValues);
                ContentValues date = new ContentValues();
                date.put(DATE, 0);
                db.insert(DAYtABLE, null, date);
                ContentValues content = new ContentValues();
                content.put(WAKETIME, "6:00");
                content.put(SLEEPTIME, "10:00");
                db.insert(TIMETABLE, null, content);
                ContentValues values = new ContentValues();
                values.put(NOOFDOSAGES, 0);
                db.insert(TABLE, null, values);
            }catch (Exception E){
                E.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists "+TABLE);
            db.execSQL("drop table if exists "+ PROGTABLE);
            db.execSQL("drop table if exists " + DAYtABLE);
            db.execSQL("drop table if EXISTS " + TIMETABLE);
            onCreate(db);
        }
    }
    public DbAdapter open() throws SQLException {
        db= dbHelper.getWritableDatabase();
        return this;
    }
    public void close(){
        dbHelper.close();
    }
    public long insertData(String name,String type, int quantity, int amount, int number){
        ContentValues values = new ContentValues();
        values.put(DRUGNAME, name);
        values.put(DRUGTYPE, type);
        values.put(DRUGAMOUNT, quantity);
        values.put(AMOUNTPERDOSE, amount);
        values.put(NOOFDOSAGES, number);

        return db.insert(TABLE, null, values);
    }
    public Cursor getAllMedications(){
        return db.query(TABLE, new String[]{
                ROWID, DRUGNAME,DRUGTYPE, DRUGAMOUNT, AMOUNTPERDOSE, NOOFDOSAGES
        }, null, null, null, null, null);
    }
    public Cursor getData(long rowId) throws SQLException{
        Cursor mCursor = db.query(true, TABLE, new String[]{
                ROWID, DRUGNAME,DRUGTYPE, DRUGAMOUNT, AMOUNTPERDOSE, NOOFDOSAGES
        }, ROWID + "=" +rowId, null, null, null, null, null);
        return mCursor;
    }
    public boolean update(long rowId, String name, String type, int amount, int quantity, int dosages){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DRUGNAME, name);
        contentValues.put(DRUGTYPE, type);
        contentValues.put(DRUGAMOUNT, amount);
        contentValues.put(AMOUNTPERDOSE, quantity);
        contentValues.put(NOOFDOSAGES, dosages);


        return db.update(TABLE, contentValues, ROWID + "="+ rowId, null)>0;
    }
    public boolean updateAmount(long rowId, int amount){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DRUGAMOUNT, amount);

        return db.update(TABLE, contentValues, ROWID + "="+ rowId, null)>0;
    }

    //PROGRESS TABLE
    public long insertProgress(int value){
        ContentValues values = new ContentValues();
        values.put(VALUE, value);

        return db.insert(PROGTABLE, null, values);
    }
    public Cursor getProgress(long rowId) throws SQLException{
        Cursor mCursor = db.query(true, PROGTABLE, new String[]{
                ROWIDPROGRESS, VALUE
        }, ROWIDPROGRESS + "=" +rowId, null, null, null, null, null);
        return mCursor;
    }
    public boolean updateProgress(long rowId, int value){
        ContentValues contentValues = new ContentValues();
        contentValues.put(VALUE, value);

        return db.update(PROGTABLE, contentValues, ROWIDPROGRESS + "="+ rowId, null)>0;
    }

    //date table
    public long insertDate(int date){
        ContentValues values = new ContentValues();
        values.put(DATE, date);

        return db.insert(DAYtABLE, null, values);
    }
    public Cursor getDate(long rowId) throws SQLException{
        Cursor mCursor = db.query(true, DAYtABLE, new String[]{
                ROWIDDATE, DATE
        }, ROWIDDATE + "=" +rowId, null, null, null, null, null);
        return mCursor;
    }
    public boolean updateDate(long rowId, int date){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE, date);

        return db.update(DAYtABLE, contentValues, ROWIDDATE + "="+ rowId, null)>0;
    }

    // TIME TABLE
    public Cursor getTime(long rowId) throws SQLException{
        Cursor mCursor = db.query(true, TIMETABLE, new String[]{
                ROWIDTIME, WAKETIME, SLEEPTIME
        }, ROWIDTIME + "=" +rowId, null, null, null, null, null);
        return mCursor;
    }
    public boolean updateTime(long rowId, String wakeTime, String sleepTime){
        ContentValues contentValues = new ContentValues();
        contentValues.put(WAKETIME, wakeTime);
        contentValues.put(SLEEPTIME, sleepTime);

        return db.update(TIMETABLE, contentValues, ROWIDTIME + "="+ rowId, null)>0;
    }
}
