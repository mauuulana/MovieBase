package com.maul.moviebase.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import static com.maul.moviebase.db.DatabaseContract.AUTHORITY;
import static com.maul.moviebase.db.DatabaseContract.CONTENT_URI;
import static com.maul.moviebase.db.DatabaseContract.CONTENT_URI_TV;
import static com.maul.moviebase.db.DatabaseContract.FavoriteMovie.TABLE_MOVIE;
import static com.maul.moviebase.db.DatabaseContract.FavoriteTV.TABLE_TV;

public class Provider extends ContentProvider {

    private static final int MOVIE = 100;
    private static final int MOVIE_ID = 101;

    private static final int TV = 200;
    private static final int TV_ID = 201;


    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        mUriMatcher.addURI(AUTHORITY, TABLE_MOVIE, MOVIE);
        mUriMatcher.addURI(AUTHORITY,
                TABLE_MOVIE + "/#",
                MOVIE_ID);
    }

    static {
        mUriMatcher.addURI(AUTHORITY, TABLE_TV, TV);

        mUriMatcher.addURI(AUTHORITY,
                TABLE_TV + "/#",
                TV_ID);
    }

    private MovieHelper helperMovie;
    private TVShowHelper helperTVS;

    @Override
    public boolean onCreate() {
        helperMovie = new MovieHelper(getContext());
        helperMovie.open();
        helperTVS = new TVShowHelper(getContext());
        helperTVS.open();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor;
        switch (mUriMatcher.match(uri)) {
            case MOVIE:
                cursor = helperMovie.queryProvider();
                break;
            case MOVIE_ID:
                cursor = helperMovie.queryByIdProvider(uri.getLastPathSegment());
                break;
            case TV:
                cursor = helperTVS.queryProvider();
                break;
            case TV_ID:
                cursor = helperTVS.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        if (cursor != null) {
            cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        long added;
        Uri contentUri = null;

        switch (mUriMatcher.match(uri)) {
            case MOVIE:
                added = helperMovie.insertProvider(contentValues);
                if (added > 0) {
                    contentUri = ContentUris.withAppendedId(CONTENT_URI, added);
                }
                break;
            case TV:
                added = helperTVS.insertProvider(contentValues);
                if (added > 0) {
                    contentUri = ContentUris.withAppendedId(CONTENT_URI_TV, added);
                }
                break;
            default:
                added = 0;
                break;
        }

        if (added > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return contentUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int deleted;
        switch (mUriMatcher.match(uri)) {
            case MOVIE_ID:
                deleted = helperMovie.deleteProvider(uri.getLastPathSegment());
                break;
            case TV_ID:
                deleted = helperTVS.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }

        if (deleted > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int updated;
        switch (mUriMatcher.match(uri)) {
            case MOVIE_ID:
                updated = helperMovie.updateProvider(uri.getLastPathSegment(), contentValues);
                break;
            case TV_ID:
                updated = helperTVS.updateProvider(uri.getLastPathSegment(), contentValues);
                break;
            default:
                updated = 0;
                break;
        }

        if (updated > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return updated;
    }
}
