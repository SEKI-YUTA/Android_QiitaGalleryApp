package com.example.qiitagalleryapp.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.qiitagalleryapp.R;

import java.io.File;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context context;
    private List<String> pathList;

    public ImageAdapter(Context context, List<String> pathList) {
        this.context = context;
        this.pathList = pathList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String path = pathList.get(holder.getAdapterPosition());
        Glide.with(context).load(new File(path)).into(holder.img_thumbnail);
//        holder.img_thumbnail.setImageURI(Uri.parse(path));
    }

    @Override
    public int getItemCount() {
        return pathList.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_thumbnail;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            img_thumbnail = itemView.findViewById(R.id.img_thumbnail);
        }
    }
}
