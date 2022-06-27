package com.alexsykes.bankmonsterr.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alexsykes.bankmonsterr.R;
import com.alexsykes.bankmonsterr.activities.MainActivity;
import com.alexsykes.bankmonsterr.data.WaterAndParents;

public class WaterViewHolder extends RecyclerView.ViewHolder {
    private final TextView waterViewItem;

    private WaterViewHolder(View itemView) {
        super(itemView);
        waterViewItem = itemView.findViewById(R.id.waterTextView);
      //  parentViewItem = itemView.findViewById(R.id.parentTextView);
    }

    public void bind(WaterAndParents current, final WaterListAdapter.OnItemClickListener listener) {
        String water_name = current.getWater();
        waterViewItem.setText(water_name);
     //   parentViewItem.setText(current.getParent());
        int water_id = current.getWater_id();
        // Log.i("Info", "bind: " + water_id);
        itemView.setOnClickListener(v -> {
            Context context = itemView.getContext();
            ((MainActivity) context).onWaterListItemClicked(water_id, water_name);
        });
    }

    static WaterViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.waterview_item, parent, false);
        return new WaterViewHolder(view);
    }
}
