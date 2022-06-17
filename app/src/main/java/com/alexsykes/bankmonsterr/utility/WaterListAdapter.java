package com.alexsykes.bankmonsterr.utility;

import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.alexsykes.bankmonsterr.data.Water;
import com.alexsykes.bankmonsterr.data.WaterAndParent;
import com.alexsykes.bankmonsterr.data.WaterAndParents;

public class WaterListAdapter extends ListAdapter<WaterAndParents, WaterViewHolder> {

    AdapterView.OnItemClickListener listener;

    public WaterListAdapter(@NonNull DiffUtil.ItemCallback<WaterAndParents> diffCallback) {
        super(diffCallback);
    }


    @Override
    public WaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return WaterViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull WaterViewHolder holder, int position) {
        WaterAndParents current = getItem(position);
        holder.bind(current, new OnItemClickListener() {
            @Override
            public void onItemClick(Water water) {

            }
        });
    }
    public interface OnItemClickListener {
        void onItemClick(Water water);
    }

    public static class WaterDiff extends DiffUtil.ItemCallback<WaterAndParents> {

        @Override
        public boolean areItemsTheSame(@NonNull WaterAndParents oldItem, @NonNull WaterAndParents newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull WaterAndParents oldItem, @NonNull WaterAndParents newItem) {
            return oldItem.getWater().equals(newItem.getWater());
        }
    }
}
