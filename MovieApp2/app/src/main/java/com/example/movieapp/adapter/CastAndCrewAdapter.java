package com.example.movieapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.databinding.LayoutCastAndCrewItemBinding;
import com.example.movieapp.model.ItemCastAndCrew;

import java.util.ArrayList;

public class CastAndCrewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<ItemCastAndCrew> itemCastAndCrewArrayList;
    private OpenDetailPersonClickListener openDetailPersonClickListener;

    public CastAndCrewAdapter(ArrayList<ItemCastAndCrew> itemCastAndCrewArrayList) {
        this.itemCastAndCrewArrayList = itemCastAndCrewArrayList;
    }

    public void setOpenDetailPersonClickListener(OpenDetailPersonClickListener openDetailPersonClickListener) {
        this.openDetailPersonClickListener = openDetailPersonClickListener;
    }

    public static class CastAndCrewAdapterViewHolder extends RecyclerView.ViewHolder{
        private final LayoutCastAndCrewItemBinding binding;

        public CastAndCrewAdapterViewHolder(LayoutCastAndCrewItemBinding layoutCastAndCrewItemBinding) {
            super(layoutCastAndCrewItemBinding.getRoot());
            this.binding = layoutCastAndCrewItemBinding;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CastAndCrewAdapterViewHolder(LayoutCastAndCrewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemCastAndCrew castAndCrew = itemCastAndCrewArrayList.get(position);
        CastAndCrewAdapterViewHolder movieViewHolder = (CastAndCrewAdapterViewHolder) holder;
        movieViewHolder.binding.tvArtistName.setText(castAndCrew.getName());

        String urlImg = "https://image.tmdb.org/t/p/w500" + castAndCrew.getPath();
        Glide.with(holder.itemView.getContext()).load(urlImg).placeholder(R.drawable.beginner3).dontAnimate().into(movieViewHolder.binding.imgArtistAvatar);
        movieViewHolder.itemView.setOnClickListener(v -> {openDetailPersonClickListener.openDetailPersonClick();});
    }

    @Override
    public int getItemCount() {
        return itemCastAndCrewArrayList.size();
    }

    public interface OpenDetailPersonClickListener{
        void openDetailPersonClick();
    }
}
