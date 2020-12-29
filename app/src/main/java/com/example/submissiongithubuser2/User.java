package com.example.submissiongithubuser2;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    private String url;
    private String name;
    private String username;
    private String description;
    private String location;
    private String photo;
    private String following;
    private String followers;
    private String followers_url;
    private String following_url;
    private String repos;
    private String company;
    private String blog;
    private String email;

    User() {
    }

    private User(Parcel in) {
        this.url = in.readString();
        this.name = in.readString();
        this.username = in.readString();
        this.description = in.readString();
        this.location = in.readString();
        this.photo = in.readString();
        this.following = in.readString();
        this.followers = in.readString();
        this.followers_url = in.readString();
        this.following_url = in.readString();
        this.repos = in.readString();
        this.company = in.readString();
        this.blog = in.readString();
        this.email = in.readString();
    }

    public String getRepos() {
        return repos;
    }

    public void setRepos(String repos) {
        this.repos = repos;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFollowing_url() {
        return following_url;
    }

    public void setFollowing_url(String following_url) {
        this.following_url = following_url;
    }

    public String getFollowers_url() {
        return followers_url;
    }

    public void setFollowers_url(String followers_url) {
        this.followers_url = followers_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.name);
        dest.writeString(this.username);
        dest.writeString(this.description);
        dest.writeString(this.location);
        dest.writeString(this.photo);
        dest.writeString(this.following);
        dest.writeString(this.followers);
        dest.writeString(this.followers_url);
        dest.writeString(this.following_url);
        dest.writeString(this.repos);
        dest.writeString(this.company);
        dest.writeString(this.blog);
        dest.writeString(this.email);
    }
}
