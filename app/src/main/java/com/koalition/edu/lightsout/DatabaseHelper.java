package com.koalition.edu.lightsout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Kingston on 3/15/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String SCHEMA = "lightsout";
    public static int VERSION = 3;

    public DatabaseHelper(Context context) {
        super(context, SCHEMA, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // called once when SCHEMA has not been created.
        String powerUpTable = "CREATE TABLE " + PowerUp.TABLE_NAME + " ( "
                + PowerUp.COLUMN_ID + " INTEGER PRIMARY KEY, "
                + PowerUp.COLUMN_TITLE + " STRING, "
                + PowerUp.COLUMN_PRICE + " INTEGER, "
                + PowerUp.COLUMN_CATEGORY + " INTEGER );";
        db.execSQL(powerUpTable);
        //insertPowerUp(new PowerUp(1, "Freeze", 200));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method will be called when the version is incremented
    }

    public void insertPowerUp(PowerUp p){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PowerUp.COLUMN_ID, p.getId());
        contentValues.put(PowerUp.COLUMN_TITLE, p.getTitle());
        contentValues.put(PowerUp.COLUMN_PRICE, p.getPrice());
        contentValues.put(PowerUp.COLUMN_CATEGORY, p.getCategory());
        db.insert(PowerUp.TABLE_NAME, null, contentValues);
        db.close();
    }

    public PowerUp queryPowerUp(int id){
        SQLiteDatabase db = getReadableDatabase();
        PowerUp powerUp = new PowerUp();
        Cursor cursor = db.query(PowerUp.TABLE_NAME, null,
                PowerUp.COLUMN_ID + " =? ", new String[]{String.valueOf(id)},
                null, null, null);
        if(cursor.moveToFirst()){
            powerUp.setId(cursor.getInt(cursor.getColumnIndex(PowerUp.COLUMN_ID)));
            powerUp.setTitle(cursor.getString(cursor.getColumnIndex(PowerUp.COLUMN_TITLE)));
            powerUp.setPrice(cursor.getInt(cursor.getColumnIndex(PowerUp.COLUMN_PRICE)));
            powerUp.setCategory(cursor.getInt(cursor.getColumnIndex(PowerUp.COLUMN_CATEGORY)));
        }
        cursor.close();
        db.close();
        return powerUp;
    }

    public ArrayList<PowerUp> queryAllPowerups(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(PowerUp.TABLE_NAME, null,
                PowerUp.COLUMN_CATEGORY + " =? ", new String[]{String.valueOf(0)},
                null, null, null);

        ArrayList<PowerUp> powerupArrayList = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                PowerUp powerUp = new PowerUp();
                powerUp.setId(cursor.getInt(cursor.getColumnIndex(PowerUp.COLUMN_ID)));
                powerUp.setTitle(cursor.getString(cursor.getColumnIndex(PowerUp.COLUMN_TITLE)));
                powerUp.setPrice(cursor.getInt(cursor.getColumnIndex(PowerUp.COLUMN_PRICE)));
                powerupArrayList.add(powerUp);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return powerupArrayList;
    }
    public ArrayList<PowerUp> queryAllDesigns(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(PowerUp.TABLE_NAME, null,
                PowerUp.COLUMN_CATEGORY + " =? ", new String[]{String.valueOf(1)},
                null, null, null);

        ArrayList<PowerUp> powerupArrayList = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                PowerUp powerUp = new PowerUp();
                powerUp.setId(cursor.getInt(cursor.getColumnIndex(PowerUp.COLUMN_ID)));
                powerUp.setTitle(cursor.getString(cursor.getColumnIndex(PowerUp.COLUMN_TITLE)));
                powerUp.setPrice(cursor.getInt(cursor.getColumnIndex(PowerUp.COLUMN_PRICE)));
                powerupArrayList.add(powerUp);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return powerupArrayList;
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(PowerUp.TABLE_NAME, null, null);
    }
}