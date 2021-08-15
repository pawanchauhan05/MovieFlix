package com.app.movieflix.ui.movieDetails

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.app.movieflix.R
import com.app.movieflix.imageLoader.ImageLoader
import com.app.movieflix.pojo.Movie
import com.app.movieflix.utilities.Config
import com.app.movieflix.utilities.Utility
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.popular_movie_list_item_layout.view.*

class MovieDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.setDisplayShowHomeEnabled(true);
        supportActionBar!!.title = getString(R.string.movie_details)

        loadDataFromIntent()
    }

    private fun loadDataFromIntent() {
        intent?.let {
            val data = it.getParcelableExtra<Movie>("MOVIE")
            textViewMovieTitle.text = data.title
            textViewReleaseDate.text = getString(R.string.release_date, data.release_date)
            textViewDescription.text = data.overview
            Utility.loadImage(Utility.getCachingType(), imageViewPoster, "${Config.BACKDROP_IMAGE_PATH}${data.backdrop_path}")
            ratingBar.rating = Utility.getRating(data.vote_average, 10f)
        }
    }

    companion object {
        fun getInstance(context: Context, movie: Movie) : Intent {
            return Intent(context, MovieDetailsActivity::class.java).apply {
                putExtra("MOVIE", movie)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}