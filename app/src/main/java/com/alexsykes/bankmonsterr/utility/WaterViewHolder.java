package com.alexsykes.bankmonsterr.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alexsykes.bankmonsterr.R;
import com.alexsykes.bankmonsterr.activities.MainActivity;
import com.alexsykes.bankmonsterr.data.Water;

public class WaterViewHolder extends RecyclerView.ViewHolder {
    private final TextView waterViewItem, parentViewItem;

    private WaterViewHolder(View itemView) {
        super(itemView);
        waterViewItem = itemView.findViewById(R.id.waterTextView);
        parentViewItem = itemView.findViewById(R.id.parentTextView);
    }

    public void bind(Water current, final WaterListAdapter.OnItemClickListener listener) {
        waterViewItem.setText(current.getName());
        parentViewItem.setText(String.valueOf(current.getParent_id()));
        itemView.setOnClickListener(v -> {
            int id = current.getWater_id();
            Context context = itemView.getContext();
            ((MainActivity) context).onClickCalled(id);
        });
    }

    static WaterViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.waterview_item, parent, false);
        return new WaterViewHolder(view);
    }
}
