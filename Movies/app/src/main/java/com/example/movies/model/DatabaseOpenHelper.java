package com.example.movies.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by tuliomoura on 10/20/15.
 */
public class DatabaseOpenHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = "DatabaseOpenHelper";
    private static final String DATABASE_NAME = "Movies";
    private static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
        } catch (SQLException e) {
            Log.e(TAG, "Error while creating database");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    //region MovieDao
    private Dao<MovieEntity, Long> movieDao = null;

    public Dao<MovieEntity, Long> getMovieDao() {
        if (movieDao == null) {
            try {
                movieDao = getDao(MovieEntity.class);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return movieDao;
    }
    //endregion
}
