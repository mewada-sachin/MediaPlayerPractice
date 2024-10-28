package com.example.mediaplayerpractice.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayerpractice.data.model.MoviesCategory
import com.example.mediaplayerpractice.databinding.MoviesCategoryItemBinding

class MoviesCategoryAdapter(
    private val list: List<MoviesCategory>,
    private val movieClickListener: MovieClickListener
) :
    RecyclerView.Adapter<MoviesCategoryAdapter.MoviesCategoryViewHolder>() {

    inner class MoviesCategoryViewHolder(private val binding: MoviesCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(moviesCategory: MoviesCategory) {
            val linearLayoutManager = LinearLayoutManager(binding.root.context)
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            binding.title.text = moviesCategory.title
            binding.moviesList.layoutManager = linearLayoutManager
            binding.moviesList.adapter =
                MoviesListAdapter(moviesCategory.movies_list, movieClickListener)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MoviesCategoryViewHolder {
        val view = MoviesCategoryItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MoviesCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoviesCategoryViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}