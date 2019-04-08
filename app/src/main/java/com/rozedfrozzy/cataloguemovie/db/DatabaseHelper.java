package com.rozedfrozzy.cataloguemovie.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "dbcataloguemovie";

    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE_FAVOURITE = "create table " + DatabaseContract.FavouriteColumns.TABLE_FAVOURITE + " (" +
                DatabaseContract.FavouriteColumns.MOVIE_ID + " integer primary key autoincrement, " +
                DatabaseContract.FavouriteColumns.TITLE + " text not null, " +
                DatabaseContract.FavouriteColumns.BACKDROP + " text not null, " +
                DatabaseContract.FavouriteColumns.POSTER + " text not null, " +
                DatabaseContract.FavouriteColumns.RELEASE_DATE + " text not null, " +
                DatabaseContract.FavouriteColumns.OVERVIEW + " text not null " +
                ");";


        db.execSQL(SQL_CREATE_TABLE_FAVOURITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.FavouriteColumns.TABLE_FAVOURITE);
        onCreate(db);
    }
}
