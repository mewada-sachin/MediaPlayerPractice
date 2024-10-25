package com.example.mediaplayerpractice.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.mediaplayerpractice.R
import com.example.mediaplayerpractice.data.MovieDetails
import com.example.mediaplayerpractice.databinding.ActivityMainBinding
import com.example.mediaplayerpractice.recyclerview.MovieClickListener
import com.example.mediaplayerpractice.recyclerview.MoviesCategoryAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), MovieClickListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObserver()
    }

    private fun initObserver() {
        viewModel.liveData.observe(this) {
            binding.moviesCategory.layoutManager = LinearLayoutManager(this)
            binding.moviesCategory.adapter = MoviesCategoryAdapter(it, this)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getMoviesList("bollywood_movies")
    }

    override fun onMoviePosterClick(movieDetails: MovieDetails) {
        binding.apply {
            movieName.text = movieDetails.title
            description.text = movieDetails.description
            val layoutParams = posterImageView.layoutParams
            posterImageView.layoutParams = layoutParams
            director.text = Html.fromHtml(getString(R.string.director, movieDetails.director))
            duration.text = "Duration: ${movieDetails.duration}"
            stars.text = "Stars: ${movieDetails.cast}"
            Glide.with(this@MainActivity)
                .load(movieDetails.poster_url)
                .into(posterImageView)

            Glide.with(this@MainActivity)
                .load(movieDetails.poster_url)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        resource.alpha = 30
                        binding.root.background = resource
                        binding.root.foreground = ColorDrawable(Color.parseColor("#00000000"))
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        binding.root.background = placeholder
                    }
                })

            movieName.visibility = View.VISIBLE
            description.visibility = View.VISIBLE
            posterImageView.visibility = View.VISIBLE
        }
    }

}