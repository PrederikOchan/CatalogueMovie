package com.rozedfrozzy.cataloguemovie.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.rozedfrozzy.cataloguemovie.db.DatabaseContract.FavouriteColumns.TABLE_FAVOURITE;

public class DatabaseContract {

    public static final class FavouriteColumns implements BaseColumns{

        public static String TABLE_FAVOURITE = "favourite";

        public static String MOVIE_ID = "_id";
        public static String TITLE = "title";
        public static String OVERVIEW = "overview";
        public static String RELEASE_DATE = "release_date";
        public static String BACKDROP = "backdrop";
        public static String POSTER = "poster";
    }

    public static final String AUTHORITY = "com.rozedfrozzy.cataloguemovie";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_FAVOURITE)
            .build();
    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }
    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt( cursor.getColumnIndex(columnName) );
    }
    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong( cursor.getColumnIndex(columnName) );
    }
}
