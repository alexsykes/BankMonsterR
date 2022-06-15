package com.alexsykes.bankmonsterr.utility;

import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.alexsykes.bankmonsterr.data.Water;

public class WaterListAdapter extends ListAdapter<Water, WaterViewHolder> {

    AdapterView.OnItemClickListener listener;

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
        holder.bind(current, new OnItemClickListener() {
            @Override
            public void onItemClick(Water water) {

            }
        });
    }
    public interface OnItemClickListener {
        void onItemClick(Water water);
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
