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

import com.example.submissiongithubuser2.databinding.FragmentFollowingBinding;
import com.google.android.material.snackbar.Snackbar;

public class FollowingFragment extends Fragment {
    public static final String EXTRA_URI = "extra_url";
    public static final String EXTRA_FOLLOWING = "extra_following";
    private String url, following;

    private FollowingViewModel followingViewModel;
    private ListUserAdapter listUserAdapter;
    private FragmentFollowingBinding binding;

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
        binding = FragmentFollowingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.rvFollowing.setLayoutManager(new LinearLayoutManager(view.getContext()));
        listUserAdapter = new ListUserAdapter();
        listUserAdapter.notifyDataSetChanged();
        binding.rvFollowing.setAdapter(listUserAdapter);

        listUserAdapter.setOnItemClickCallback(data -> {
            Snackbar snackbar = Snackbar.make(requireView(), data.getName(), Snackbar.LENGTH_SHORT);
            snackbar.show();
        });

        followingViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel.class);

        if (!following.equalsIgnoreCase("0")) {
            setStatus(0, null);
            followingViewModel.getListUsers(url);
            listUserAdapter.notifyDataSetChanged();
        } else setStatus(2, getResources().getString(R.string.no_following));

        followingViewModel.getUsers().observe(requireActivity(), users -> {
            if (users != null) {
                listUserAdapter.setData(users);
                setStatus(1, null);
            }
        });
    }

    private void setStatus(int status, String message) {
        switch (status) {
            case 0: // loading
                binding.progressbarFollowing.setVisibility(View.VISIBLE);
                binding.rvFollowing.setVisibility(View.INVISIBLE);
                binding.tvFollowingStatus.setVisibility(View.INVISIBLE);
                break;
            case 1: // normal
                binding.progressbarFollowing.setVisibility(View.INVISIBLE);
                binding.rvFollowing.setVisibility(View.VISIBLE);
                binding.tvFollowingStatus.setVisibility(View.INVISIBLE);
                break;
            case 2: // message
                binding.progressbarFollowing.setVisibility(View.INVISIBLE);
                binding.rvFollowing.setVisibility(View.INVISIBLE);
                binding.tvFollowingStatus.setVisibility(View.VISIBLE);
                binding.tvFollowingStatus.setText(message);
                break;
        }
    }
}