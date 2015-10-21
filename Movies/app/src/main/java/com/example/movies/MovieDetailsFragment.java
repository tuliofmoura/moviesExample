package com.example.movies;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movies.model.DatabaseOpenHelper;
import com.example.movies.model.MovieEntity;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MovieDetailsFragment extends Fragment {

    public static final String TAG = "MovieDetailsFragment";

    public static final String ARG_MOVIE_ID = "argMovieId";
    public static final String ARG_SHOW_BTN = "showBtn";

    private MovieEntity mMovie;

    private ImageView mPoster;
    private TextView mTitle;
    private TextView mYear;
    private TextView mReleased;
    private TextView mRuntime;
    private TextView mGenre;
    private TextView mDirector;
    private TextView mActors;
    private TextView mPlot;
    private TextView mAwards;
    private TextView mRating;
    private TextView mEmpty;
    private View mContent;
    private View mBtn;

    private DatabaseOpenHelper mDbHelper;
    private MovieEntity.MovieDbHelper mMovieDbHelper;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    public static MovieDetailsFragment newInstance(boolean showBtn) {
        return newInstance(-1, showBtn);
    }

    public static MovieDetailsFragment newInstance(long movieId, boolean showBtn) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_MOVIE_ID, movieId);
        args.putBoolean(ARG_SHOW_BTN, showBtn);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDbHelper();
        getMovie();
    }

    private void getMovie() {
        Bundle args = getArguments();
        if (args != null) {
            long movieId = args.getLong(ARG_MOVIE_ID);
            if (movieId > 0)
                mMovie = mMovieDbHelper.getByRowId(movieId);
        }
    }

    private boolean showBtn() {
        Bundle args = getArguments();
        if (args != null)
            return args.getBoolean(ARG_SHOW_BTN, false);
        return false;
    }

    @Override
    public void onDestroy() {
        releaseDbHelper();
        super.onDestroy();
    }

    private void createDbHelper() {
        mDbHelper = OpenHelperManager.getHelper(getActivity(), DatabaseOpenHelper.class);
        mMovieDbHelper = new MovieEntity.MovieDbHelper(mDbHelper.getMovieDao());
    }

    private void releaseDbHelper() {
        if (mDbHelper != null)
            OpenHelperManager.releaseHelper();
        mDbHelper = null;
        mMovieDbHelper = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        initViews(rootView);
        if (mMovie != null)
            populateScreen();
        else
            onNothingToShow();
        return rootView;
    }

    private void initViews(View rootView) {
        mPoster = (ImageView) rootView.findViewById(R.id.detail_poster);
        mTitle = (TextView) rootView.findViewById(R.id.detail_title);
        mYear = (TextView) rootView.findViewById(R.id.detail_year);
        mReleased = (TextView) rootView.findViewById(R.id.detail_released);
        mRuntime = (TextView) rootView.findViewById(R.id.detail_runtime);
        mGenre = (TextView) rootView.findViewById(R.id.detail_genre);
        mDirector = (TextView) rootView.findViewById(R.id.detail_director);
        mActors = (TextView) rootView.findViewById(R.id.detail_actors);
        mPlot = (TextView) rootView.findViewById(R.id.detail_plot);
        mAwards = (TextView) rootView.findViewById(R.id.detail_awards);
        mRating = (TextView) rootView.findViewById(R.id.detail_metascore);
        mPlot = (TextView) rootView.findViewById(R.id.detail_plot);
        mEmpty = (TextView) rootView.findViewById(R.id.detail_empty);
        mContent = rootView.findViewById(R.id.detail_content);
        mBtn = rootView.findViewById(R.id.detail_button);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMovieDbHelper.createIfNotExists(mMovie);
                Toast.makeText(getActivity(), R.string.add_movie_success, Toast.LENGTH_LONG).show();
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });
    }

    /**
     * Podem ser chamados externamente para trocar o filme
     *
     * @param movie
     */
    public void setMovie(MovieEntity movie) {
        mMovie = movie;
        populateScreen();
    }

    /**
     * Pode ser chamado externamente quando não encontrar resultado para a busca
     */
    public void onNothingToShow() {
        mContent.setVisibility(View.GONE);
        mBtn.setVisibility(View.GONE);
        mEmpty.setVisibility(View.VISIBLE);
    }

    private void populateScreen() {
        mEmpty.setVisibility(View.GONE);
        mContent.setVisibility(View.VISIBLE);
        if (showBtn())
            mBtn.setVisibility(View.VISIBLE);
        else
            mBtn.setVisibility(View.GONE);
        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(mMovie.getPoster(), mPoster);
        mTitle.setText(mMovie.getTitle());
        mActors.setText(mMovie.getActors());
        mAwards.setText(mMovie.getAwards());
        mDirector.setText(mMovie.getDirector());
        mGenre.setText(mMovie.getGenre());
        mRating.setText(mMovie.getImdbRating());
        mPlot.setText(mMovie.getPlot());
        mReleased.setText(mMovie.getReleased());
        mRuntime.setText(mMovie.getRuntime());
        mYear.setText(mMovie.getYear());
    }

}
