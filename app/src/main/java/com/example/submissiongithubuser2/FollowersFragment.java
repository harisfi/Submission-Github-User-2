package com.example.submissiongithubuser2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

public class FollowersFragment extends Fragment {
    public static final String EXTRA_URI = "extra_url";
    public static final String EXTRA_FOLLOWING = "extra_followers";
    private String url, followers;

    private RecyclerView rvFollowers;
    private ListUserAdapter listUserAdapter;

    private FollowersViewModel followersViewModel;

    private ProgressBar progressBar;
    private TextView tvStatus;

    public FollowersFragment() {
    }

    public static FollowersFragment newInstance(String url, String followers) {
        FollowersFragment fragment = new FollowersFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_URI, url);
        args.putString(EXTRA_FOLLOWING, followers);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(EXTRA_URI);
            followers = getArguments().getString(EXTRA_FOLLOWING);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressbar_followers);
        tvStatus = view.findViewById(R.id.tv_followers_status);

        rvFollowers = view.findViewById(R.id.rv_followers);
        rvFollowers.setLayoutManager(new LinearLayoutManager(view.getContext()));
        listUserAdapter = new ListUserAdapter();
        listUserAdapter.notifyDataSetChanged();
        rvFollowers.setAdapter(listUserAdapter);

        listUserAdapter.setOnItemClickCallback(new ListUserAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(User data) {
                Snackbar snackbar = Snackbar.make(getView(), data.getName(), Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });

        followersViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(FollowersViewModel.class);

        if (!followers.equalsIgnoreCase("0")) {
            setStatus(0, null);
            followersViewModel.getListUsers(url);
            listUserAdapter.notifyDataSetChanged();
        } else setStatus(2, getResources().getString(R.string.no_followers));

        followersViewModel.getUsers().observe(getActivity(), users -> {
            if (users != null) {
                listUserAdapter.setData(users);
                setStatus(1, null);
            }
        });
    }

    private void setStatus(int status, String message) {
        switch (status) {
            case 0: // loading
                progressBar.setVisibility(View.VISIBLE);
                rvFollowers.setVisibility(View.INVISIBLE);
                tvStatus.setVisibility(View.INVISIBLE);
                break;
            case 1: // normal
                progressBar.setVisibility(View.INVISIBLE);
                rvFollowers.setVisibility(View.VISIBLE);
                tvStatus.setVisibility(View.INVISIBLE);
                break;
            case 2: // message
                progressBar.setVisibility(View.INVISIBLE);
                rvFollowers.setVisibility(View.INVISIBLE);
                tvStatus.setVisibility(View.VISIBLE);
                tvStatus.setText(message);
                break;
        }
    }
}