package com.alexsykes.bankmonsterr.utility;

import android.service.autofill.TextValueSanitizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alexsykes.bankmonsterr.R;

public class WaterViewHolder extends RecyclerView.ViewHolder {
    private final TextView waterViewItem;

    private WaterViewHolder(View itemView) {
        super(itemView);
        waterViewItem = itemView.findViewById(R.id.textView);
    }

    public void bind(String text) {
        waterViewItem.setText(text);
    }

    static WaterViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new WaterViewHolder(view);
    }
}
