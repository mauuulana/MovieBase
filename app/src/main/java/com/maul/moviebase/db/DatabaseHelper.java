package com.maul.moviebase.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "movie_base";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_MOVIES =
            String.format("CREATE TABLE %s"
                            + "(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL)",
                    DatabaseContract.FavoriteMovie.TABLE_MOVIE,
                    DatabaseContract.FavoriteMovie._ID,
                    DatabaseContract.FavoriteMovie.COLUMN_TITLE,
                    DatabaseContract.FavoriteMovie.COLUMN_POSTER_PATH
            );
    private static final String SQL_CREATE_TABLE_TVS =
            String.format("CREATE TABLE %s" + "(%s INTEGER PRIMARY KEY AUTOINCREMENT," + " %s TEXT NOT NULL," + " %s TEXT NOT NULL)",
                    DatabaseContract.FavoriteTV.TABLE_TV,
                    DatabaseContract.FavoriteTV._ID,
                    DatabaseContract.FavoriteTV.COLUMN_TITLE,
                    DatabaseContract.FavoriteTV.COLUMN_POSTER_PATH
            );

    DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SQL_CREATE_TABLE_MOVIES);
        database.execSQL(SQL_CREATE_TABLE_TVS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {

        database.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.FavoriteMovie.TABLE_MOVIE);
        database.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.FavoriteTV.TABLE_TV);
        onCreate(database);

    }
}
