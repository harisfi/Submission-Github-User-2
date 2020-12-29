package com.example.submissiongithubuser2;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private final Context mContext;
    @StringRes
    private final int[] TAB_TITLES = new int[]{R.string.info, R.string.followers, R.string.following};
    public User infoUser = new User();

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = InfoFragment.newInstance(infoUser);
                break;
            case 1:
                fragment = FollowersFragment.newInstance(infoUser.getFollowers_url(), infoUser.getFollowers());
                break;
            case 2:
                fragment = FollowingFragment.newInstance(infoUser.getFollowing_url(), infoUser.getFollowing());
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
