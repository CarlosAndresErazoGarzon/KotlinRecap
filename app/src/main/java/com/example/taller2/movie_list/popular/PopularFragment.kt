package com.example.taller2.movie_list.popular

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taller2.movie_list.movieAdapter
import com.example.taller2.R
import com.example.taller2.movie_list.favorite.FavoriteListener


class PopularFragment : Fragment(), FavoriteListener {

    private lateinit var customAdapter: movieAdapter
    private var recyclerView: RecyclerView? = null
    private val viewModel: SetListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_populares, container, false)
        recyclerView = view.findViewById(R.id.recyFragPopular)

        customAdapter = movieAdapter(favoriteListener = this, mContext = requireContext())
        val layoutManager = LinearLayoutManager(this.context)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = customAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setListState.observe(viewLifecycleOwner){
            when(it){
                is SetState.Error -> Log.d("Error", it.message)
                is SetState.Loading -> Log.d("POPULAR", "LOADING")
                is SetState.SuccessWithMatchMovies -> {
                    loadMovies(it.popularMovies)
                }
            }
        }
    }

    private fun loadMovies(movies: List<PopularMovie>) {
        customAdapter.updatePopularMovies(movies)
    }

    override fun onAddedFavorite(movie: PopularMovie) {
        viewModel.onFavClicked(movie)
    }

    override fun onRemoveFavorite(movie: PopularMovie) {
        viewModel.onDelClicked(movie)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PopularFragment()
    }
}