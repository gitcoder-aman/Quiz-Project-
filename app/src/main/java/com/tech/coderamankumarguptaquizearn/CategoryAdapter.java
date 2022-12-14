package com.tech.coderamankumarguptaquizearn;
//for item category xml
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coderamankumarguptaquizearn.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    Context context;
    ArrayList<CategoryModel> categoryModels;
    public CategoryAdapter(Context context, ArrayList<CategoryModel> categoryModels){
        this.context = context;
        this.categoryModels = categoryModels;
    }
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.item_category,null);
       return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        CategoryModel model = categoryModels.get(position);

        holder.textView.setText(model.getCategoryName());

        //holder.imageView.setImageResource(); //not work here image id is not int

        //so we can use glide // load image from internet
        Glide.with(context)
                .load(model.getCategoryImage())
                .into(holder.imageView);

        //when we click any category item then next page show you will show questions
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,QuizActivity.class);
                intent.putExtra("catId",model.getCategoryId()); // catId is Unique for all categories
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.category);
        }
    }
}
