package com.example.submissiongithubuser2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.submissiongithubuser2.databinding.FragmentInfoBinding;

public class InfoFragment extends Fragment {
    public static final String EXTRA_USER = "extra_user";
    private FragmentInfoBinding binding;
    private User user;

    public InfoFragment() {
    }

    public static InfoFragment newInstance(User user) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(EXTRA_USER);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvInfoRepos.setText(user.getRepos());
        binding.tvInfoFollowers.setText(user.getFollowers());
        binding.tvInfoFollowing.setText(user.getFollowing());

        if (user.getDescription().equalsIgnoreCase("null") || user.getDescription().isEmpty()) {
            binding.linearInfoBio.setVisibility(View.GONE);
        } else {
            binding.linearInfoBio.setVisibility(View.VISIBLE);
            binding.tvInfoBio.setText(user.getDescription());
        }

        if (user.getCompany().equalsIgnoreCase("null") || user.getCompany().isEmpty()) {
            binding.linearInfoCompany.setVisibility(View.GONE);
            binding.dividerInfoCompany.setVisibility(View.GONE);
        } else {
            binding.linearInfoCompany.setVisibility(View.VISIBLE);
            binding.dividerInfoCompany.setVisibility(View.VISIBLE);
            binding.tvInfoCompany.setText(user.getCompany());
        }

        if (user.getBlog().equalsIgnoreCase("null") || user.getBlog().isEmpty()) {
            binding.linearInfoBlog.setVisibility(View.GONE);
            binding.dividerInfoBlog.setVisibility(View.GONE);
        } else {
            binding.linearInfoBlog.setVisibility(View.VISIBLE);
            binding.dividerInfoBlog.setVisibility(View.VISIBLE);
            binding.tvInfoBlog.setText(user.getBlog());
        }

        if (user.getLocation().equalsIgnoreCase("null") || user.getLocation().isEmpty()) {
            binding.linearInfoLocation.setVisibility(View.GONE);
            binding.dividerInfoLocation.setVisibility(View.GONE);
        } else {
            binding.linearInfoLocation.setVisibility(View.VISIBLE);
            binding.dividerInfoLocation.setVisibility(View.VISIBLE);
            binding.tvInfoLocation.setText(user.getLocation());
        }

        if (user.getEmail().equalsIgnoreCase("null") || user.getEmail().isEmpty()) {
            binding.linearInfoEmail.setVisibility(View.GONE);
        } else {
            binding.linearInfoEmail.setVisibility(View.VISIBLE);
            binding.tvInfoEmail.setText(user.getEmail());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}