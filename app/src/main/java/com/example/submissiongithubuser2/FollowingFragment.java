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

public class FollowingFragment extends Fragment {
    public static final String EXTRA_URI = "extra_url";
    public static final String EXTRA_FOLLOWING = "extra_following";
    private String url, following;

    private RecyclerView rvFollowing;
    private ListUserAdapter listUserAdapter;

    private FollowingViewModel followingViewModel;

    private ProgressBar progressBar;
    private TextView tvStatus;

    public FollowingFragment() {
    }

    public static FollowingFragment newInstance(String url, String following) {
        FollowingFragment fragment = new FollowingFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_URI, url);
        args.putString(EXTRA_FOLLOWING, following);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(EXTRA_URI);
            following = getArguments().getString(EXTRA_FOLLOWING);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressbar_following);
        tvStatus = view.findViewById(R.id.tv_following_status);

        rvFollowing = view.findViewById(R.id.rv_following);
        rvFollowing.setLayoutManager(new LinearLayoutManager(view.getContext()));
        listUserAdapter = new ListUserAdapter();
        listUserAdapter.notifyDataSetChanged();
        rvFollowing.setAdapter(listUserAdapter);

        listUserAdapter.setOnItemClickCallback(new ListUserAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(User data) {
                Snackbar snackbar = Snackbar.make(getView(), data.getName(), Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });

        followingViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel.class);

        if (!following.equalsIgnoreCase("0")) {
            setStatus(0, null);
            followingViewModel.getListUsers(url);
            listUserAdapter.notifyDataSetChanged();
        } else setStatus(2, getResources().getString(R.string.no_following));

        followingViewModel.getUsers().observe(getActivity(), users -> {
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
                rvFollowing.setVisibility(View.INVISIBLE);
                tvStatus.setVisibility(View.INVISIBLE);
                break;
            case 1: // normal
                progressBar.setVisibility(View.INVISIBLE);
                rvFollowing.setVisibility(View.VISIBLE);
                tvStatus.setVisibility(View.INVISIBLE);
                break;
            case 2: // message
                progressBar.setVisibility(View.INVISIBLE);
                rvFollowing.setVisibility(View.INVISIBLE);
                tvStatus.setVisibility(View.VISIBLE);
                tvStatus.setText(message);
                break;
        }
    }
}