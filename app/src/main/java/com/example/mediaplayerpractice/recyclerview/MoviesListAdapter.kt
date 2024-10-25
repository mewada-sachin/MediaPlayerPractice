package com.example.mediaplayerpractice.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mediaplayerpractice.data.MovieDetails
import com.example.mediaplayerpractice.databinding.MoviesListItemBinding

class MoviesListAdapter(
    private val list: List<MovieDetails>,
    private val movieClickListener: MovieClickListener
) : RecyclerView.Adapter<MoviesListAdapter.MoviesListViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    inner class MoviesListViewHolder(private val binding: MoviesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieDetails) {
            Glide.with(binding.root.context)
                .load(movie.poster_url)
                .into(binding.movieImage)

            val layoutParams = binding.movieImage.layoutParams
            if (movie.isSelected) {
                layoutParams.width = dpToPx(100, binding.root.context)
                layoutParams.height = dpToPx(200, binding.root.context)


            } else {
                layoutParams.width = dpToPx(200, binding.root.context)
                layoutParams.height = dpToPx(300, binding.root.context)
            }
            binding.movieImage.layoutParams = layoutParams
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesListViewHolder {
        val view = MoviesListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MoviesListViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoviesListViewHolder, position: Int) {
        val movie = list[position]
        holder.bind(movie)
        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0) {
                list[selectedPosition].isSelected = false
                notifyItemChanged(selectedPosition)
            }
            selectedPosition = holder.adapterPosition
            movie.isSelected = true
            movieClickListener.onMoviePosterClick(movie)
            notifyItemChanged(selectedPosition)
        }
    }

    override fun getItemCount() = list.size

    fun dpToPx(dp: Int, context: Context): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }

}
