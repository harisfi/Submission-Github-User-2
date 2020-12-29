package com.example.submissiongithubuser2;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_USER = "extra_user";

    private ProgressBar progressBar;
    private ImageView imgPhoto;
    private TextView tvName, tvUsername;
    private TabLayout tabDetail;
    private View viewLine;
    private ViewPager vpDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setHomeButtonEnabled(true);

        progressBar = findViewById(R.id.detail_progressbar);
        imgPhoto = findViewById(R.id.img_detail_photo);
        tvName = findViewById(R.id.tv_detail_name);
        tvUsername = findViewById(R.id.tv_detail_username);
        tabDetail = findViewById(R.id.tab_detail);
        viewLine = findViewById(R.id.view_line);
        vpDetail = findViewById(R.id.view_pager);

        User user = getIntent().getParcelableExtra(EXTRA_USER);
        String url = user.getUrl();

        AsyncHttpClient client = new AsyncHttpClient();

        showLoading(true);
        client.addHeader("Authorization", "token a048404d19ed76a34cb9ef37f59895f7590aeaff");
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
                            .into(imgPhoto);
                    tvName.setText(userItem.getName()
                            .compareToIgnoreCase("null") == 0 ?
                            userItem.getUsername() : userItem.getName());
                    tvUsername.setText(userItem.getUsername());
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
        tabs.getTabAt(1).getOrCreateBadge().setNumber(Integer.parseInt(user.getFollowers()));
        tabs.getTabAt(2).getOrCreateBadge().setNumber(Integer.parseInt(user.getFollowing()));
        getSupportActionBar().setElevation(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return true;
    }

    private void showLoading(boolean state) {
        progressBar.setVisibility(state ? View.VISIBLE : View.INVISIBLE);
        imgPhoto.setVisibility(state ? View.INVISIBLE : View.VISIBLE);
        tvName.setVisibility(state ? View.INVISIBLE : View.VISIBLE);
        tvUsername.setVisibility(state ? View.INVISIBLE : View.VISIBLE);
        tabDetail.setVisibility(state ? View.INVISIBLE : View.VISIBLE);
        viewLine.setVisibility(state ? View.INVISIBLE : View.VISIBLE);
        vpDetail.setVisibility(state ? View.INVISIBLE : View.VISIBLE);
    }
}