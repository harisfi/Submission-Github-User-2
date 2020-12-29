package com.example.submissiongithubuser2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvUsers;
    private ListUserAdapter listUserAdapter;
    private TextView tvStatus;
    private ProgressBar progressbar;
    private MainViewModel mainViewModel;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = findViewById(R.id.tv_status);
        progressbar = findViewById(R.id.progressbar);

        rvUsers = findViewById(R.id.rv_users);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        listUserAdapter = new ListUserAdapter();
        listUserAdapter.notifyDataSetChanged();
        rvUsers.setAdapter(listUserAdapter);
        rvUsers.setHasFixedSize(true);

        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);

        if (username == null || username.isEmpty())
            setStatus(2, getResources().getString(R.string.start_search));

        mainViewModel.getUsers().observe(this, users -> {
            if (users != null) {
                listUserAdapter.setData(users);
                setStatus(1, null);
            }
        });

        listUserAdapter.setOnItemClickCallback(new ListUserAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(User data) {
                Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
                detailIntent.putExtra(DetailActivity.EXTRA_USER, data);
                startActivity(detailIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    setStatus(0, null);
                    username = query;
                    mainViewModel.getListUsers("https://api.github.com/search/users?q=" + query);
                    listUserAdapter.notifyDataSetChanged();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (newText.isEmpty())
                        setStatus(2, getResources().getString(R.string.start_search));
                    else {
                        setStatus(0, null);
                        username = newText;
                        mainViewModel.getListUsers("https://api.github.com/search/users?q=" + newText);
                        listUserAdapter.notifyDataSetChanged();
                    }
                    return true;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_language) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setStatus(int status, String message) {
        switch (status) {
            case 0: // loading
                progressbar.setVisibility(View.VISIBLE);
                rvUsers.setVisibility(View.INVISIBLE);
                tvStatus.setVisibility(View.INVISIBLE);
                break;
            case 1: // stop loading
                progressbar.setVisibility(View.INVISIBLE);
                rvUsers.setVisibility(View.VISIBLE);
                tvStatus.setVisibility(View.INVISIBLE);
                break;
            case 2: // message
                progressbar.setVisibility(View.INVISIBLE);
                rvUsers.setVisibility(View.INVISIBLE);
                tvStatus.setVisibility(View.VISIBLE);
                tvStatus.setText(message);
                break;
        }
    }
}