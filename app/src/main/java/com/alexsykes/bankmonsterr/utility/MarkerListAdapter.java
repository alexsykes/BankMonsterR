package com.alexsykes.bankmonsterr.utility;

import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.alexsykes.bankmonsterr.data.Marker;

public class MarkerListAdapter extends ListAdapter<Marker, MarkerViewHolder> {

    AdapterView.OnItemClickListener listener;

    public MarkerListAdapter(@NonNull DiffUtil.ItemCallback<Marker> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public MarkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MarkerViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull MarkerViewHolder holder, int position) {
        Marker current = getItem(position);
        holder.bind(current);
    }

    public interface onItemClickListener {
        void onItemClick(Marker marker);
    }

    public static class MarkerDiff extends DiffUtil.ItemCallback<Marker> {

        @Override
        public boolean areItemsTheSame(@NonNull Marker oldItem, @NonNull Marker newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Marker oldItem, @NonNull Marker newItem) {
            return false;
        }
    }
}
