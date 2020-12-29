package com.example.submissiongithubuser2;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<User>> listUser = new MutableLiveData<>();

    void getListUsers(final String url) {
        final ArrayList<User> listItems = new ArrayList<>();

        AsyncHttpClient client = new AsyncHttpClient();

        client.addHeader("Authorization", "token " + BuildConfig.GITHUB_TOKEN);
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray items = responseObject.getJSONArray("items");

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        User user = new User();
                        user.setUrl(item.getString("url"));
                        user.setName(item.getString("login"));
                        user.setUsername(item.getString("login"));
                        user.setDescription(item.getString("login"));
                        user.setLocation(item.getString("login"));
                        user.setPhoto(item.getString("avatar_url"));
                        listItems.add(user);
                    }
                    listUser.postValue(listItems);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                Log.d("onFailure", errorMessage);
            }
        });
    }

    LiveData<ArrayList<User>> getUsers() {
        return listUser;
    }
}
