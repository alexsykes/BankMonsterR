package com.alexsykes.bankmonsterr.utility;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.alexsykes.bankmonsterr.data.Water;

public class WaterListAdapter extends ListAdapter<Water, WaterViewHolder> {

    public WaterListAdapter(@NonNull DiffUtil.ItemCallback<Water> diffCallback) {
        super(diffCallback);
    }


    @Override
    public WaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return WaterViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull WaterViewHolder holder, int position) {
        Water current = getItem(position);
        holder.bind(current.getName());
    }

    public static class WaterDiff extends DiffUtil.ItemCallback<Water> {

        @Override
        public boolean areItemsTheSame(@NonNull Water oldItem, @NonNull Water newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Water oldItem, @NonNull Water newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }
}
