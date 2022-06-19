package com.alexsykes.bankmonsterr.utility;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.alexsykes.bankmonsterr.R;
import com.alexsykes.bankmonsterr.data.Marker;

import java.text.DecimalFormat;
import java.util.List;

public class MarkerListAdapter extends RecyclerView.Adapter<MarkerListAdapter.MarkerViewHolder> {
    List<Marker> markerList;
    Marker current;

    public MarkerListAdapter(List<Marker> markerList) {
        this.markerList = markerList;
    }


    @Override
    public void onBindViewHolder(@NonNull MarkerViewHolder holder, int position) {
        Marker marker = markerList.get(position);
        holder.bind(marker);
    }

    @NonNull
    @Override
    public MarkerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.marker_list_item, viewGroup, false);
        MarkerViewHolder holder = new MarkerViewHolder(v);
        return holder;
    }

    @Override
    public int getItemCount() {
        return markerList.size();
    }


     static class MarkerViewHolder extends RecyclerView.ViewHolder {
        private final TextView typeTextView, codeTextView, latTextView, lngTextView;

        public MarkerViewHolder(@NonNull  View view) {
            super(view);
            typeTextView = view.findViewById(R.id.typeTextView);
            codeTextView = view.findViewById(R.id.codeTextView);
            latTextView = view.findViewById(R.id.latTextView);
            lngTextView = view.findViewById(R.id.lngTextView);
        }

        public void bind(Marker current) {
            int marker_id = current.getMarker_id();

            DecimalFormat myFormatter = new DecimalFormat("###.00000");
            String latStr, lngStr;
            lngStr = myFormatter.format(current.getLng());
            latStr = myFormatter.format(current.getLat());

            codeTextView.setText(current.getCode());
            typeTextView.setText(current.getType());
            lngTextView.setText(lngStr);
            latTextView.setText(latStr);

        }
    }
}
