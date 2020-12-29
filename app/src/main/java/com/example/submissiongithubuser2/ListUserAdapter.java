package com.example.submissiongithubuser2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ListUserAdapter extends RecyclerView.Adapter<ListUserAdapter.ListViewHolder> {
    private final ArrayList<User> listUser = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public void setData(ArrayList<User> items) {
        listUser.clear();
        listUser.addAll(items);
        notifyDataSetChanged();
    }

    public void clearData() {
        listUser.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListUserAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users, parent, false);
        return new ListViewHolder(view);
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @Override
    public void onBindViewHolder(@NonNull ListUserAdapter.ListViewHolder holder, int position) {
        holder.bind(listUser.get(position));

        User user = listUser.get(position);
        Glide.with(holder.itemView.getContext())
                .load(user.getPhoto())
                .apply(new RequestOptions().override(64, 64))
                .into(holder.imgPhoto);
        holder.tvUsername.setText(user.getUsername());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listUser.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(User data);
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvUsername;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
            tvUsername = itemView.findViewById(R.id.tv_item_username);
        }

        void bind(User user) {
            Glide.with(itemView.getContext())
                    .load(user.getPhoto())
                    .apply(new RequestOptions().override(64, 64))
                    .into(imgPhoto);
            tvUsername.setText(user.getName());
        }
    }
}
