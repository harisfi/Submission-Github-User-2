package com.example.submissiongithubuser2;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.submissiongithubuser2.databinding.ActivityDetailBinding;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_USER = "extra_user";
    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        User user = getIntent().getParcelableExtra(EXTRA_USER);
        String url = user.getUrl();

        AsyncHttpClient client = new AsyncHttpClient();

        showLoading(true);
        client.addHeader("Authorization", "token " + BuildConfig.GITHUB_TOKEN);
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Jika koneksi berhasil

                String result = new String(responseBody);
                try {
                    JSONObject item = new JSONObject(result);
                    User userItem = new User();

                    userItem.setUsername(item.getString("login"));
                    userItem.setPhoto(item.getString("avatar_url"));
                    userItem.setFollowers_url(item.getString("followers_url"));
                    userItem.setFollowing_url(item.getString("url") + "/following");
                    userItem.setName(item.getString("name"));
                    userItem.setCompany(item.getString("company"));
                    userItem.setBlog(item.getString("blog"));
                    userItem.setLocation(item.getString("location"));
                    userItem.setEmail(item.getString("email"));
                    userItem.setDescription(item.getString("bio"));
                    userItem.setRepos(item.getString("public_repos"));
                    userItem.setFollowers(item.getString("followers"));
                    userItem.setFollowing(item.getString("following"));

                    Glide.with(DetailActivity.this)
                            .load(userItem.getPhoto())
                            .apply(new RequestOptions().override(86, 86))
                            .into(binding.imgDetailPhoto);
                    binding.tvDetailName.setText(userItem.getName()
                            .compareToIgnoreCase("null") == 0 ?
                            userItem.getUsername() : userItem.getName());
                    binding.tvDetailUsername.setText(userItem.getUsername());
                    setupTab(userItem);
                    DetailActivity.this.getSupportActionBar().setTitle(userItem.getUsername());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                showLoading(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Jika koneksi gagal
                String errorMessage;
                switch (statusCode) {
                    case 401:
                        errorMessage = statusCode + " : Bad Request";
                        break;
                    case 403:
                        errorMessage = statusCode + " : Forbidden";
                        break;
                    case 404:
                        errorMessage = statusCode + " : Not Found";
                        break;
                    default:
                        errorMessage = statusCode + " : " + error.getMessage();
                        break;
                }
                Log.e("get user data", errorMessage);
                showLoading(false);
            }
        });
    }

    public void setupTab(User user) {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        sectionsPagerAdapter.infoUser = user;
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tab_detail);
        tabs.setupWithViewPager(viewPager);
        Objects.requireNonNull(tabs.getTabAt(1)).getOrCreateBadge().setNumber(Integer.parseInt(user.getFollowers()));
        Objects.requireNonNull(tabs.getTabAt(2)).getOrCreateBadge().setNumber(Integer.parseInt(user.getFollowing()));
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return true;
    }

    private void showLoading(boolean state) {
        binding.detailProgressbar.setVisibility(state ? View.VISIBLE : View.INVISIBLE);
        binding.imgDetailPhoto.setVisibility(state ? View.INVISIBLE : View.VISIBLE);
        binding.tvDetailName.setVisibility(state ? View.INVISIBLE : View.VISIBLE);
        binding.tvDetailUsername.setVisibility(state ? View.INVISIBLE : View.VISIBLE);
        binding.tabDetail.setVisibility(state ? View.INVISIBLE : View.VISIBLE);
        binding.viewLine.setVisibility(state ? View.INVISIBLE : View.VISIBLE);
        binding.viewPager.setVisibility(state ? View.INVISIBLE : View.VISIBLE);
    }
}