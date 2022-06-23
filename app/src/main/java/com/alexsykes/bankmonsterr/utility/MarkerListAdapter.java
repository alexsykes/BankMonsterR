package com.alexsykes.bankmonsterr.utility;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alexsykes.bankmonsterr.R;
import com.alexsykes.bankmonsterr.data.BMarker;

import java.text.DecimalFormat;
import java.util.List;

public class MarkerListAdapter extends RecyclerView.Adapter<MarkerListAdapter.MarkerViewHolder> {
    List<BMarker> BMarkerList;
    BMarker current;

    public MarkerListAdapter(List<BMarker> BMarkerList) {
        this.BMarkerList = BMarkerList;
    }


    @Override
    public void onBindViewHolder(@NonNull MarkerViewHolder holder, int position) {
        BMarker BMarker = BMarkerList.get(position);
        holder.bind(BMarker);
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
        return BMarkerList.size();
    }


     static class MarkerViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView, typeTextView, latTextView, lngTextView;

        public MarkerViewHolder(@NonNull  View view) {
            super(view);
            nameTextView = view.findViewById(R.id.nameTextView);
            typeTextView = view.findViewById(R.id.typeTextView);
            latTextView = view.findViewById(R.id.latTextView);
            lngTextView = view.findViewById(R.id.lngTextView);
        }

         public void bind(BMarker current) {
             int marker_id = current.getMarker_id();

             DecimalFormat myFormatter = new DecimalFormat("###.00000");
             String latStr, lngStr;
             lngStr = myFormatter.format(current.getLng());
             latStr = myFormatter.format(current.getLat());

             nameTextView.setText(current.getName() + " " + current.getCode());
             typeTextView.setText(current.getType());
             lngTextView.setText(lngStr);
            latTextView.setText(latStr);
        }
    }
}
