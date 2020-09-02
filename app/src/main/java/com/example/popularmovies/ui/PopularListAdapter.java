package com.example.popularmovies.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.data.models.Movie;
import com.example.popularmovies.databinding.MovieListItemBinding;
import com.example.popularmovies.utils.Constants;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PopularListAdapter extends RecyclerView.Adapter<PopularListAdapter.PopularListViewHolder> {

    private static final String TAG = "PopularListAdapter";
    @NonNull
    private List<Movie> movieList;
    @NonNull
    private Context context;
    private MovieListItemBinding binding;
    private OnClickCallback onClickListener;
   private MutableLiveData<Integer> onMovieClick = new MutableLiveData<>();

    public MutableLiveData<Integer> getOnMovieClick() {
        return onMovieClick;
    }

    public PopularListAdapter(@NotNull List<Movie> movies, @NonNull Context context, OnClickCallback listener ) {

        this.movieList = movies;
        this.context = context;
        this.onClickListener = listener;



    }
    public void updateRecyclerViewData(List<Movie> newList){

        movieList.addAll(newList);
        notifyItemRangeInserted((movieList.size()) , (newList.size()));
    }

    public void initRecyclerViewWithData(List<Movie> newList){
        movieList.clear();
        movieList.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PopularListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = MovieListItemBinding.inflate(LayoutInflater.from(context)
                ,parent,false);

        return new PopularListViewHolder(binding.getRoot().getRootView(),binding, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularListViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder: called");
        Log.d(TAG, "onBindViewHolder: moviesList :: Pos = " + position + " , moviesName = " + movieList.get(position).getTitle()
        + " , ID = "+ movieList.get(position).getId());

        holder.bind(movieList.get(position));
    }


    @Override
    public int getItemCount() {
        return movieList.size();
    }

    static class PopularListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private static final String TAG = "PopularListViewHolder";
        private MovieListItemBinding binding;
        private OnClickCallback onClickListener;
        private Movie movie;
        public PopularListViewHolder(@NonNull View itemView , MovieListItemBinding binding, OnClickCallback listener) {
            super(itemView);
            this.binding = binding;
            this.onClickListener = listener;
            Log.d(TAG, "PopularListViewHolder: called");

        }

        public void bind(Movie movie){

            this.movie = movie;
            binding.movieListReleaseId.setText( movie.getTitle() + " (" +movie.getReleaseDate().substring(0,4) + ")" );
            binding.movieListReleaseId.bringToFront();
           // binding.movieListBriefId.setText(movie.getOverview());
            Picasso.get().load(Constants.image_url + "w1280" + movie.getBackdropPath())
                    .into(binding.movieListIconId);

            binding.movieListHeadLayoutId.setOnClickListener(this);
            binding.movieListBriefLayoutId.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
         onClickListener.onMovieClick(movie.getId());
        }
    }


}
