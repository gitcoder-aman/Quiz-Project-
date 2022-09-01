package com.example.quizearn;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.example.quizearn.SpinWheel.model.LuckyItem;
import com.example.quizearn.databinding.ActivitySpinnerBinding;

import java.util.ArrayList;
import java.util.List;

public class SpinnerActivity extends AppCompatActivity {

    ActivitySpinnerBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivitySpinnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        List<LuckyItem> data = new ArrayList<>();

        LuckyItem item1 = new LuckyItem();
        item1.topText = "10";
        item1.secondaryText = "Coins";
        item1.color = Color.parseColor("#FFFFFFFF");
        item1.textColor = Color.parseColor("#FF000000");
        data.add(item1);

        LuckyItem item2 = new LuckyItem();
        item2.topText = "200";
        item2.secondaryText = "Coins";
        item2.color = Color.parseColor("#4CAF50");
        item2.textColor = Color.parseColor("#ffffff");
        data.add(item2);

        LuckyItem item3 = new LuckyItem();
        item3.topText = "50";
        item3.secondaryText = "COINS";
        item3.color = Color.parseColor("#FFFFFFFF");
        item3.textColor = Color.parseColor("#FF000000");
        data.add(item3);

        LuckyItem item4 = new LuckyItem();
        item4.topText = "100";
        item4.secondaryText = "COINS";
        item4.color = Color.parseColor("#7f00d9");
        item4.textColor = Color.parseColor("#eceff1");
        data.add(item4);

        LuckyItem item5 = new LuckyItem();
        item5.topText = "150";
        item5.secondaryText = "COINS";
        item5.color = Color.parseColor("#FFFFFFFF");
        item5.textColor = Color.parseColor("#FF000000");
        data.add(item5);

        LuckyItem item6 = new LuckyItem();
        item6.topText = "25";
        item6.secondaryText = "COINS";
        item6.color = Color.parseColor("#dc0000");
        item6.textColor = Color.parseColor("#eceff1");
        data.add(item6);

        LuckyItem item7 = new LuckyItem();
        item7.topText = "500";
        item7.secondaryText = "COINS";
        item7.color = Color.parseColor("#FFFFFFFF");
        item7.textColor = Color.parseColor("#FF000000");
        data.add(item7);

        LuckyItem item8 = new LuckyItem();
        item8.topText = "0";
        item8.secondaryText = "COINS";
        item8.color = Color.parseColor("#008bff");
        item8.textColor = Color.parseColor("#eceff1");
        data.add(item8);

        binding.wheelview.setData(data);
        binding.wheelview.setRound(5);
    }
}