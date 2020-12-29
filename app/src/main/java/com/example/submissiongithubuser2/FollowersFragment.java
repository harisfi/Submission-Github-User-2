package com.example.submissiongithubuser2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.submissiongithubuser2.databinding.FragmentFollowersBinding;
import com.google.android.material.snackbar.Snackbar;

public class FollowersFragment extends Fragment {
    public static final String EXTRA_URI = "extra_url";
    public static final String EXTRA_FOLLOWING = "extra_followers";
    private String url, followers;

    private FollowersViewModel followersViewModel;
    private ListUserAdapter listUserAdapter;
    private FragmentFollowersBinding binding;

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
        binding = FragmentFollowersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.rvFollowers.setLayoutManager(new LinearLayoutManager(view.getContext()));
        listUserAdapter = new ListUserAdapter();
        listUserAdapter.notifyDataSetChanged();
        binding.rvFollowers.setAdapter(listUserAdapter);

        listUserAdapter.setOnItemClickCallback(data -> {
            Snackbar snackbar = Snackbar.make(requireView(), data.getName(), Snackbar.LENGTH_SHORT);
            snackbar.show();
        });

        followersViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.NewInstanceFactory()).get(FollowersViewModel.class);

        if (!followers.equalsIgnoreCase("0")) {
            setStatus(0, null);
            followersViewModel.getListUsers(url);
            listUserAdapter.notifyDataSetChanged();
        } else setStatus(2, getResources().getString(R.string.no_followers));

        followersViewModel.getUsers().observe(requireActivity(), users -> {
            if (users != null) {
                listUserAdapter.setData(users);
                setStatus(1, null);
            }
        });
    }

    private void setStatus(int status, String message) {
        switch (status) {
            case 0: // loading
                binding.progressbarFollowers.setVisibility(View.VISIBLE);
                binding.rvFollowers.setVisibility(View.INVISIBLE);
                binding.tvFollowersStatus.setVisibility(View.INVISIBLE);
                break;
            case 1: // normal
                binding.progressbarFollowers.setVisibility(View.INVISIBLE);
                binding.rvFollowers.setVisibility(View.VISIBLE);
                binding.tvFollowersStatus.setVisibility(View.INVISIBLE);
                break;
            case 2: // message
                binding.progressbarFollowers.setVisibility(View.INVISIBLE);
                binding.rvFollowers.setVisibility(View.INVISIBLE);
                binding.tvFollowersStatus.setVisibility(View.VISIBLE);
                binding.tvFollowersStatus.setText(message);
                break;
        }
    }
}