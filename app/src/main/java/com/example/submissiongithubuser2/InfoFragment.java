package com.example.submissiongithubuser2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InfoFragment extends Fragment {
    public static final String EXTRA_USER = "extra_user";
    private User user;

    private View divCompany, divBlog, divLocation;
    private LinearLayout layoutBio, layoutCompany, layoutBlog, layoutLocation, layoutEmail;
    private TextView tvBio, tvRepo, tvFollowers, tvFollowing, tvCompany, tvBlog, tvLocation, tvEmail;

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        divCompany = view.findViewById(R.id.divider_info_company);
        divBlog = view.findViewById(R.id.divider_info_blog);
        divLocation = view.findViewById(R.id.divider_info_location);

        layoutBio = view.findViewById(R.id.linear_info_bio);
        layoutCompany = view.findViewById(R.id.linear_info_company);
        layoutBlog = view.findViewById(R.id.linear_info_blog);
        layoutLocation = view.findViewById(R.id.linear_info_location);
        layoutEmail = view.findViewById(R.id.linear_info_email);

        tvBio = view.findViewById(R.id.tv_info_bio);
        tvRepo = view.findViewById(R.id.tv_info_repos);
        tvFollowers = view.findViewById(R.id.tv_info_followers);
        tvFollowing = view.findViewById(R.id.tv_info_following);
        tvCompany = view.findViewById(R.id.tv_info_company);
        tvBlog = view.findViewById(R.id.tv_info_blog);
        tvLocation = view.findViewById(R.id.tv_info_location);
        tvEmail = view.findViewById(R.id.tv_info_email);

        tvRepo.setText(user.getRepos());
        tvFollowers.setText(user.getFollowers());
        tvFollowing.setText(user.getFollowing());

        if (user.getDescription().equalsIgnoreCase("null") || user.getDescription().isEmpty()) {
            layoutBio.setVisibility(View.GONE);
        } else {
            layoutBio.setVisibility(View.VISIBLE);
            tvBio.setText(user.getDescription());
        }

        if (user.getCompany().equalsIgnoreCase("null") || user.getCompany().isEmpty()) {
            layoutCompany.setVisibility(View.GONE);
            divCompany.setVisibility(View.GONE);
        } else {
            layoutCompany.setVisibility(View.VISIBLE);
            divCompany.setVisibility(View.VISIBLE);
            tvCompany.setText(user.getCompany());
        }

        if (user.getBlog().equalsIgnoreCase("null") || user.getBlog().isEmpty()) {
            layoutBlog.setVisibility(View.GONE);
            divBlog.setVisibility(View.GONE);
        } else {
            layoutBlog.setVisibility(View.VISIBLE);
            divBlog.setVisibility(View.VISIBLE);
            tvBlog.setText(user.getBlog());
        }

        if (user.getLocation().equalsIgnoreCase("null") || user.getLocation().isEmpty()) {
            layoutLocation.setVisibility(View.GONE);
            divLocation.setVisibility(View.GONE);
        } else {
            layoutLocation.setVisibility(View.VISIBLE);
            divLocation.setVisibility(View.VISIBLE);
            tvLocation.setText(user.getLocation());
        }

        if (user.getEmail().equalsIgnoreCase("null") || user.getEmail().isEmpty()) {
            layoutEmail.setVisibility(View.GONE);
        } else {
            layoutEmail.setVisibility(View.VISIBLE);
            tvEmail.setText(user.getEmail());
        }
    }
}