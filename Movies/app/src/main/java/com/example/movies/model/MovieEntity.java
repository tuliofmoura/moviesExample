package com.example.movies.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by tuliomoura on 10/20/15.
 */
@DatabaseTable(tableName = MovieEntity.TABLE_NAME)
public class MovieEntity {

    //region CONSTANTS
    public static final String TABLE_NAME = "Movie";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_RELEASED = "released";
    public static final String COLUMN_RUNTIME = "runtime";
    public static final String COLUMN_GENRE = "genre";
    public static final String COLUMN_DIRECTOR = "director";
    public static final String COLUMN_ACTORS = "actors";
    public static final String COLUMN_PLOT = "plot";
    public static final String COLUMN_AWARDS = "awards";
    public static final String COLUMN_METASCORE = "metascore";
    public static final String COLUMN_IMDB_RATING = "imdbRating";
    public static final String COLUMN_POSTER = "poster";
    //endregion

    //region COLUMNS
    @DatabaseField(generatedId = true, columnName = COLUMN_ID)
    private Long id;

    @SerializedName("Title")
    @DatabaseField(columnName = COLUMN_TITLE)
    private String title;

    @SerializedName("Year")
    @DatabaseField(columnName = COLUMN_YEAR)
    private String year;

    @SerializedName("Released")
    @DatabaseField(columnName = COLUMN_RELEASED)
    private String released;

    @SerializedName("Runtime")
    @DatabaseField(columnName = COLUMN_RUNTIME)
    private String runtime;

    @SerializedName("Genre")
    @DatabaseField(columnName = COLUMN_GENRE)
    private String genre;

    @SerializedName("Director")
    @DatabaseField(columnName = COLUMN_DIRECTOR)
    private String director;

    @SerializedName("Actors")
    @DatabaseField(columnName = COLUMN_ACTORS)
    private String actors;

    @SerializedName("Plot")
    @DatabaseField(columnName = COLUMN_PLOT)
    private String plot;

    @SerializedName("Awards")
    @DatabaseField(columnName = COLUMN_AWARDS)
    private String awards;

    @SerializedName("Metascore")
    @DatabaseField(columnName = COLUMN_METASCORE)
    private String metascore;

    @SerializedName("imdbRating")
    @DatabaseField(columnName = COLUMN_IMDB_RATING)
    private String imdbRating;

    @SerializedName("Poster")
    @DatabaseField(columnName = COLUMN_POSTER)
    private String poster;
    //endregion

    //region GETTERS/SETTERS
    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getMetascore() {
        return metascore;
    }

    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
    //endregion

    //region DbHelper

    public static class MovieDbHelper {
        private final String TAG = "MovieDbHelper";

        private Dao<MovieEntity, Long> mMovieDao;

        public MovieDbHelper(Dao<MovieEntity, Long> dao) {
            mMovieDao = dao;
        }

        public MovieEntity createIfNotExists(MovieEntity entity) {
            try {
                return mMovieDao.createIfNotExists(entity);
            } catch (SQLException e) {
                Log.e(TAG, "Error in method createIfNotExists()", e);
                return null;
            }
        }

        public int deleteByRowId(long rowId) {
            int rowsDeleted = 0;
            try {
                rowsDeleted = mMovieDao.deleteById(rowId);
            } catch (SQLException e) {
                Log.e(TAG, "Error in method deleteByRowId", e);
            }
            return rowsDeleted;
        }

        public int update(MovieEntity entity) {
            try {
                int rowsUpdated = mMovieDao.update(entity);
                Log.d(TAG, rowsUpdated + " rows updated");
                return rowsUpdated;
            } catch (SQLException e) {
                Log.e(TAG, "Error in method update()", e);
                return 0;
            }
        }

        public MovieEntity createOrUpdate(MovieEntity entity) {
            MovieEntity savedEntity = getByRowId(entity.getId());
            if (savedEntity == null)
                return createIfNotExists(entity);
            else {
                entity.setId(savedEntity.getId());
                update(entity);
                return entity;
            }
        }

        public MovieEntity getByRowId(long rowId) {
            MovieEntity entity = null;
            try {
                QueryBuilder<MovieEntity, Long> queryBuilder = mMovieDao.queryBuilder();
                queryBuilder.where().eq(COLUMN_ID, rowId);
                PreparedQuery<MovieEntity> preparedQuery = queryBuilder.prepare();
                entity = mMovieDao.queryForFirst(preparedQuery);
            } catch (Exception ex) {
                Log.e(TAG, "Error in method getByRowId", ex);
            }
            return entity;
        }

        public ArrayList<MovieEntity> getAll() {
            ArrayList<MovieEntity> cards = new ArrayList<>();
            try {
                cards = new ArrayList<>(mMovieDao.queryForAll());
            } catch (SQLException e) {
                Log.e(TAG, "Error in method getAll", e);
            }
            return cards;
        }
    }

    //endregion

}
