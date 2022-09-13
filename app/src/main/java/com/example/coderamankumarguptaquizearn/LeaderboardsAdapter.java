package com.example.coderamankumarguptaquizearn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coderamankumarguptaquizearn.databinding.RowLeaderboardsBinding;

import java.util.ArrayList;

public class LeaderboardsAdapter extends RecyclerView.Adapter<LeaderboardsAdapter.LeaderboardViewHolder>{

    Context context;
    ArrayList<UserDatabase>users;
    public LeaderboardsAdapter(Context context, ArrayList<UserDatabase>users){
        this.context = context;
        this.users = users;
    }
    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_leaderboards,parent,false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {

        UserDatabase user = users.get(position);

        holder.binding.name.setText(user.getName());
        holder.binding.coinsShow.setText(String.valueOf(user.getCoins()));
        holder.binding.index.setText(String.format("#%d",position+1));

        //image loading in leaderboard
        Glide.with(context)
                .load(user.getProfile())
                .into(holder.binding.profileImage);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class LeaderboardViewHolder extends RecyclerView.ViewHolder{

        RowLeaderboardsBinding binding;
        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = RowLeaderboardsBinding.bind(itemView);
        }
    }

}
