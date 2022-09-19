package com.example.coderamankumarguptaquizearn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coderamankumarguptaquizearn.databinding.WithdrawHistoryDesignBinding;

import java.sql.Date;
import java.util.ArrayList;

public class WithdrawHistoryAdapter extends RecyclerView.Adapter<WithdrawHistoryAdapter.WithdrawHistoryViewHolder> {

    Context context;
    ArrayList<WithdrawRequest>requests;

    public WithdrawHistoryAdapter(Context context,ArrayList<WithdrawRequest>requests) {
        this.context = context;
        this.requests = requests;
    }


    @NonNull
    @Override
    public WithdrawHistoryAdapter.WithdrawHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
       View view = inflater.inflate(R.layout.withdraw_history_design,parent,false);
       return new WithdrawHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WithdrawHistoryAdapter.WithdrawHistoryViewHolder holder, int position) {
        WithdrawRequest request = requests.get(position);

        holder.binding.wIndex.setText(String.format("%d",position+1));
        holder.binding.wName.setText(request.getRequestedBy());
        holder.binding.wPaytm.setText(request.getPaytmNo());
        holder.binding.wCoins.setText (String.valueOf(request.getNoOfCoins()));
        holder.binding.wSendTime.setText((String.valueOf(request.getCreateAt())));
        holder.binding.wPaymentStatus.setText(request.getStatus());
        holder.binding.wRupees.setText("Rs: "+String.valueOf(request.getRupees()));


    }

    @Override
    public int getItemCount() {
        return requests.size();
    }
    public class WithdrawHistoryViewHolder extends RecyclerView.ViewHolder{

        WithdrawHistoryDesignBinding binding;
        public WithdrawHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = WithdrawHistoryDesignBinding.bind(itemView);
        }
    }
}
