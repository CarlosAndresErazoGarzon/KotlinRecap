package com.example.taller2.movie_list.favorite

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taller2.model.Movie
import com.example.taller2.R
import com.example.taller2.movie_list.popular.PopularMovie

class FavoriteFragment : Fragment(), FavoriteListener {
    private val viewModelFav: FavListViewModel by activityViewModels()
    private lateinit var customAdapter: FavoriteMovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favoritos, container, false)
        var recyclerView = view.findViewById<RecyclerView>(R.id.recyFragFavorite)

        customAdapter = FavoriteMovieAdapter(this, requireContext())
        val layoutManager = LinearLayoutManager(this.context)
        customAdapter.notifyDataSetChanged()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = customAdapter

        viewModelFav.favMoviesState.observe(viewLifecycleOwner){
            when(it){
                is FavoriteState.Error -> Log.d("FAVORITEFRAG", "ERROR")
                is FavoriteState.Loading -> Log.d("FAVORITEFRAG", "LOADING")
                is FavoriteState.Success -> loadMovies(it.movies)
            }
        }

        return view
    }

    private fun loadMovies(favList: List<PopularMovie>) {
        customAdapter.updateFavoriteMovies(favList)
    }

    override fun onAddedFavorite(movie: PopularMovie) {
      //No operation
    }

    override fun onRemoveFavorite(movie: PopularMovie) {
        viewModelFav.onDelClicked(movie)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoriteFragment()
    }
}