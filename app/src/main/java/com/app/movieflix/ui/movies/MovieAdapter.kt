package com.app.movieflix.ui.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.movieflix.R
import com.app.movieflix.imageLoader.ImageLoader
import com.app.movieflix.pojo.Movie
import com.app.movieflix.utilities.Config
import com.app.movieflix.utilities.Utility
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.popular_movie_list_item_layout.view.*
import kotlinx.android.synthetic.main.popular_movie_list_item_layout.view.imageViewMovie
import kotlinx.android.synthetic.main.unpopular_movie_list_item_layout.view.*

class MovieAdapter(var list: MutableList<Movie>, private val onItemClicked: (Movie) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val POPULAR_MOVIE = 1
    private val UN_POPULAR_MOVIE = 2

    inner class PopularViewHolder(private val containerView: View, onItemClicked: (Int) -> Unit) :
        RecyclerView.ViewHolder(containerView) {

        init {
            itemView.setOnClickListener {
                onItemClicked(adapterPosition)
            }
        }

        fun bind(movie: Movie) {
            Utility.loadImage(Utility.getCachingType(), itemView.imageViewMovie, "${Config.BACKDROP_IMAGE_PATH}${movie.backdrop_path}")
            itemView.setOnClickListener { onItemClicked(movie) }
        }
    }

    inner class UnPopularViewHolder(private val containerView: View, onItemClicked: (Int) -> Unit) :
        RecyclerView.ViewHolder(containerView) {

        init {
            itemView.setOnClickListener {
                onItemClicked(adapterPosition)
            }
        }

        fun bind(movie: Movie) {
            Utility.loadImage(Utility.getCachingType(), itemView.imageViewMovie, "${Config.POSTER_IMAGE_PATH}${movie.poster_path}")
            itemView.textViewTitle.text = movie.title
            itemView.textViewOverview.text = movie.overview
            itemView.setOnClickListener { onItemClicked(movie) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null

        when (viewType) {
            UN_POPULAR_MOVIE -> {
                viewHolder = UnPopularViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.unpopular_movie_list_item_layout, parent, false)
                ) { onItemClicked }
            }

            POPULAR_MOVIE -> {
                viewHolder = PopularViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.popular_movie_list_item_layout, parent, false)
                ) { onItemClicked }
            }
        }

        return viewHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PopularViewHolder) {
            holder.bind(list[position])
        } else if (holder is UnPopularViewHolder) {
            holder.bind(list[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (list[position].vote_average < 7) {
            return UN_POPULAR_MOVIE
        } else {
            return POPULAR_MOVIE
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(dataList: MutableList<Movie>) {
        list.clear()
        list.addAll(dataList)
        notifyDataSetChanged()
    }

    fun deleteMovieItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }
}