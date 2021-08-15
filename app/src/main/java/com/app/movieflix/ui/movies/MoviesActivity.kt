package com.app.movieflix.ui.movies

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.movieflix.R
import com.app.movieflix.data.source.IDataRepository
import com.app.movieflix.pojo.Movie
import com.app.movieflix.pojo.NetworkMode
import com.app.movieflix.pojo.Result
import com.app.movieflix.ui.movieDetails.MovieDetailsActivity
import com.app.movieflix.utilities.visibility
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.coroutines.CoroutineDispatcher
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG = "MoviesActivity"

@AndroidEntryPoint
class MoviesActivity : AppCompatActivity() {

    private val messageEventSubject = PublishSubject.create<String>()
    private val bag = CompositeDisposable()

    @Inject
    lateinit var dataRepository: IDataRepository

    @Inject
    lateinit var dispatcher: CoroutineDispatcher

    private lateinit var movieAdapter: MovieAdapter

    private val moviesViewModel by viewModels<MoviesViewModel> {
        MoviesViewModelFactory(dataRepository, dispatcher)
    }

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this@MoviesActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

        movieAdapter = MovieAdapter(mutableListOf()) {
            startActivity(MovieDetailsActivity.getInstance(this, it))
        }

        recyclerView.apply {
            layoutManager = linearLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = movieAdapter
            setHasFixedSize(true)
            setItemViewCacheSize(20)
        }

        initObservers()

        messageEventSubject
            .debounce(500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter { it.length >= 3 }
            .subscribe {
                moviesViewModel.filterMovie("%$it%")
            }.run { bag.add(this) }

        moviesViewModel.getMovies(NetworkMode.ONLINE)

        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.DOWN or ItemTouchHelper.UP
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                Toast.makeText(this@MoviesActivity, getString(R.string.movie_has_beed_removed), Toast.LENGTH_SHORT).show()
                val movie = movieAdapter.list[viewHolder.adapterPosition]
                movieAdapter.deleteMovieItem(viewHolder.adapterPosition)
                moviesViewModel.deleteMovieById(movie.id)
            }
        }
        ItemTouchHelper(simpleItemTouchCallback).run {
            attachToRecyclerView(recyclerView)
        }
    }

    private fun initObservers() {
        moviesViewModel.result.observe(this, Observer {
            when (it) {
                is Result.Success -> {
                    movieAdapter.updateList(it.mutableList)
                    if (movieAdapter.list.isEmpty()) {
                        textViewError.apply {
                            text = context.getString(R.string.no_movies_found)
                            visibility(true)
                        }
                        recyclerView.visibility(false)
                    } else {
                        recyclerView.visibility(true)
                        textViewError.visibility(false)
                    }
                }
                is Result.Failure -> {
                    var errorMessage: String = when (it.ex) {
                        is SocketTimeoutException -> {
                            getString(R.string.timeout_error)
                        }
                        is UnknownHostException -> {
                            getString(R.string.no_internet)
                        }
                        else -> {
                            it.ex.message ?: getString(R.string.something_went_wrong)
                        }
                    }
                    if (movieAdapter.list.isEmpty()) {
                        textViewError.apply {
                            text = errorMessage
                            visibility(true)
                        }
                        recyclerView.visibility(false)
                    } else {
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        recyclerView.visibility(true)
                    }
                }
            }
        })

        moviesViewModel.dataLoading.observe(this, Observer {
            progressBar.visibility(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val myActionMenuItem = menu!!.findItem(R.id.action_search)
        val searchView = myActionMenuItem.actionView as SearchView
        val searchTextView =
            searchView.findViewById<View>(androidx.appcompat.R.id.search_src_text) as AutoCompleteTextView
        try {
            val mCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")
            mCursorDrawableRes.isAccessible = true
            mCursorDrawableRes[searchTextView] =
                R.drawable.cursor //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (e: Exception) {
            e.printStackTrace()
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (!searchView.isIconified) {
                    searchView.isIconified = true
                }
                myActionMenuItem.collapseActionView()
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (query.isNullOrEmpty()) {
                    moviesViewModel.getMovies(NetworkMode.OFFLINE)
                } else {
                    messageEventSubject.onNext(query)
                }
                return false
            }
        })

        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        bag.clear()
    }
}