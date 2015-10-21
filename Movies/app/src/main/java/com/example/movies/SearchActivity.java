package com.example.movies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.movies.application.MoviesApplication;
import com.example.movies.model.MovieEntity;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    private EditText mSearch;
    private FrameLayout mContainer;
    private MovieDetailsFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setUpToolbar();
        setStatusBarColor();
        initViews();
        mFragment = MovieDetailsFragment.newInstance(true);
        getSupportFragmentManager().beginTransaction()
                .add(mContainer.getId(), mFragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        MoviesApplication.getRequestQueue().cancelAll(TAG);
        super.onDestroy();
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.hideOverflowMenu();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            w.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void initViews() {
        mContainer = (FrameLayout) findViewById(R.id.fragment_container);

        mSearch = (EditText) findViewById(R.id.search);

        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH || EditorInfo.IME_ACTION_UNSPECIFIED == actionId) {
                    doSearch();
                    handled = true;
                }
                return handled;
            }
        });

        final Drawable clearBtn = getResources().getDrawable(android.R.drawable.presence_offline);
        clearBtn.setBounds(0, 0, clearBtn.getIntrinsicWidth(), clearBtn.getIntrinsicHeight());
        mSearch.setCompoundDrawables(null, null, mSearch.getText().toString().isEmpty() ? null : clearBtn, null);
        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mSearch.setCompoundDrawables(null, null, mSearch.getText().toString().isEmpty() ? null : clearBtn, null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mSearch.getCompoundDrawables()[2] == null) {
                    return false;
                }
                if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }
                if (motionEvent.getX() > mSearch.getWidth() - mSearch.getPaddingRight() - clearBtn.getIntrinsicWidth()) {
                    mSearch.setText("");
                    mSearch.setCompoundDrawables(null, null, null, null);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_search:
                doSearch();
        }
        return super.onOptionsItemSelected(item);
    }

    private void doSearch() {
        final String query = mSearch.getText().toString().trim();
        if (!query.isEmpty()) {
            String queryEncoded;
            try {
                queryEncoded = URLEncoder.encode(query, "utf-8");
            } catch (UnsupportedEncodingException e) {
                final String msg = getString(R.string.search_encode_error_msg);
                showErrorDialog(msg);
                return;
            }
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(mSearch.getWindowToken(), 0);

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage(getString(R.string.searching));
            progressDialog.show();

            String url = getString(R.string.web_service_url);
            url += queryEncoded;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();
                    try {
                        final boolean success = Boolean.valueOf(response.get("Response").toString());
                        if (success) {
                            MovieEntity movie = new Gson().fromJson(response.toString(), MovieEntity.class);
                            mFragment.setMovie(movie);
                        } else {
                            mFragment.onNothingToShow();
                            final String msg = getString(R.string.search_no_result_msg);
                            showErrorDialog(msg);
                        }
                    } catch (Exception e) {
                        mFragment.onNothingToShow();
                        final String message = getString(R.string.error_dialog_internal_msg);
                        showErrorDialog(message);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    mFragment.onNothingToShow();
                    String message;
                    if (error instanceof NoConnectionError)
                        message = getString(R.string.error_dialog_no_connection_msg);
                    else
                        message = getString(R.string.error_dialog_internal_msg);
                    showErrorDialog(message);
                }
            });
            request.setTag(TAG);
            MoviesApplication.getRequestQueue().add(request);
        }
    }

    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
        builder.setTitle(R.string.error_dialog_title)
                .setMessage(message)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        builder.create().show();
    }

}
