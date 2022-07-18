package com.example.taller2.movie_list.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.taller2.model.Movie
import com.example.taller2.R
import com.example.taller2.movie_list.popular.PopularMovie

class FavoriteMovieAdapter (
    private var favoriteListener: FavoriteListener,
    private var mContext: Context,
    private var dataSet: MutableList<PopularMovie> = mutableListOf())
    : RecyclerView.Adapter<FavoriteMovieAdapter.ViewHolder>(){

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){

        val name: TextView
        val id: TextView
        val popularity: TextView
        val unselect_favorite: Button
        val image: ImageView
        init {
            name = view.findViewById(R.id.adapt_fav_name)
            id = view.findViewById(R.id.adapt_fav_id)
            popularity = view.findViewById(R.id.adapt_fav_popularity)
            unselect_favorite = view.findViewById(R.id.adapt_fav_border)
            image = view.findViewById(R.id.adapt_fav_photo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fav_movie_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataSet.get(position)

        holder.name.text = item.title
        holder.id.text = item.id
        holder.popularity.text = item.popularity.toString()

        Glide.with(mContext)
            .load("https://image.tmdb.org/t/p/w500${item.backdrop_path}")
            .transform(CenterCrop())
            .into(holder.image)

        holder.unselect_favorite.setOnClickListener(){
            favoriteListener.onRemoveFavorite(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount() = dataSet.size

    fun updateFavoriteMovies(favList: List<PopularMovie>) {
        dataSet.clear()
        dataSet.addAll(favList)
        notifyDataSetChanged()
    }
}
