package com.moringaschool.libraryjava.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

public class LibraryActivity extends AppCompatActivity {
    public static final String TAG = LibraryActivity.class.getSimpleName();
    @BindView(R.id.errorTextView)
    TextView mErrorTextView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String mRecentAddress;

    private ParlorListAdapter mAdapter;

    public List<BeautyParlor> parlors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parlor);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");

        SalonApi client = SalonClient.getClient();

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mRecentAddress = mSharedPreferences.getString(SyncStateContract.Constants.PREFERENCES_LOCATION_KEY, null);

        if (mRecentAddress != null) {
            //client.getBeautyParlor(mRecentAddress, locatio);
        }

        Call<List<BeautyParlor>> call = client.getBeautyParlor( "mRecentAddress");
        call.enqueue(new Callback<List<BeautyParlor>>() {
            @Override
            public void onResponse(Call<List<BeautyParlor>> call, Response<List<BeautyParlor>> response) {
                hideProgressBar();

                if (response.isSuccessful()) {
                    parlors = response.body();
                    mAdapter = new ParlorListAdapter(ParlorActivity.this, parlors);
                    mRecyclerView.setAdapter(mAdapter);
                    RecyclerView.LayoutManager layoutManager =
                            new LinearLayoutManager(ParlorActivity.this);
                    mRecyclerView.setLayoutManager(layoutManager);
                    mRecyclerView.setHasFixedSize(true);
                    showParlors();
                } else {
                    showUnsuccessfulMessage();
                }
            }

            @Override
            public void onFailure(Call<List<BeautyParlor>> call, Throwable t) {
                hideProgressBar();
                showFailureMessage();
            }

        });
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String location) {
                addToSharedPreferences(location);
                SalonApi client = SalonClient.getClient();
                Call<List<BeautyParlor>> call = client.getBeautyParlor(location);

                call.enqueue(new Callback<List<BeautyParlor>>() {
                    @Override
                    public void onResponse(Call<List<BeautyParlor>> call, Response<List<BeautyParlor>> response) {
                        hideProgressBar();

                        if (response.isSuccessful()) {
                            parlors = response.body();
                            mAdapter = new ParlorListAdapter(ParlorActivity.this, parlors);
                            mRecyclerView.setAdapter(mAdapter);
                            RecyclerView.LayoutManager layoutManager =
                                    new LinearLayoutManager(ParlorActivity.this);
                            mRecyclerView.setLayoutManager(layoutManager);
                            mRecyclerView.setHasFixedSize(true);
                            showParlors();
                        } else {
                            showUnsuccessfulMessage();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<BeautyParlor>> call, Throwable t) {
                        hideProgressBar();
                        showFailureMessage();
                    }

                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        return super.onOptionsItemSelected(item);
    }

    private void addToSharedPreferences(String location) {
        mEditor.putString(SyncStateContract.Constants.PREFERENCES_LOCATION_KEY, location).apply();
    }
    private void showFailureMessage() {
        mErrorTextView.setText("Something went wrong. Please check your Internet connection and try again later");
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showUnsuccessfulMessage() {
        mErrorTextView.setText("Something went wrong. Please try again later");
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showParlors() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
}
