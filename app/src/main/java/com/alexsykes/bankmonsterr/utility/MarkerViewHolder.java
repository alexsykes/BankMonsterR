package com.alexsykes.bankmonsterr.utility;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alexsykes.bankmonsterr.R;
import com.alexsykes.bankmonsterr.data.Marker;

public class MarkerViewHolder extends RecyclerView.ViewHolder {
    private final TextView typeTextView, codeTextView, latTextView, lngTextView;

    private MarkerViewHolder(View view) {
        super(view);
        typeTextView = view.findViewById(R.id.typeTextView);
        codeTextView = view.findViewById(R.id.codeTextView);
        latTextView = view.findViewById(R.id.latTextView);
        lngTextView = view.findViewById(R.id.lngTextView);
    }

    static MarkerViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.marker_list_item, parent, false);
        return new MarkerViewHolder(view);

    }

    public void bind(Marker current) {
        int marker_id = current.getMarker_id();
        codeTextView.setText(current.getCode());
        typeTextView.setText(current.getType());
        lngTextView.setText(String.valueOf(current.getLng()));
        latTextView.setText(String.valueOf(current.getLng()));

    }
}
