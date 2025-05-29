package com.example.gh_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class GrowthDataAdapter extends RecyclerView.Adapter<GrowthDataAdapter.GrowthViewHolder> {

    private List<GrowthData> growthDataList;

    public GrowthDataAdapter(List<GrowthData> growthDataList) {
        this.growthDataList = growthDataList;
    }

    @NonNull
    @Override
    public GrowthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_growth_data, parent, false);
        return new GrowthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrowthViewHolder holder, int position) {
        GrowthData growthData = growthDataList.get(position);
        holder.timestampTextView.setText(growthData.getTimestamp());

        // Show all bean lengths in one text view
        StringBuilder lengthsText = new StringBuilder("Lengths: ");
        for (Double length : growthData.getBeanLengths()) {
            lengthsText.append(length).append(" cm, ");
        }
        holder.lengthsTextView.setText(lengthsText.toString());

        // Load image using Glide
        Glide.with(holder.imageView.getContext())
                .load(growthData.getImageUrl())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return growthDataList.size();
    }

    static class GrowthViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView timestampTextView, lengthsTextView;

        GrowthViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
            lengthsTextView = itemView.findViewById(R.id.lengthsTextView);
        }
    }
}
