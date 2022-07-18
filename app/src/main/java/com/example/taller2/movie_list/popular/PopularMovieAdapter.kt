package com.example.taller2.movie_list

import android.content.Context
import android.util.Log
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
import com.example.taller2.movie_list.favorite.FavoriteListener
import com.example.taller2.R
import com.example.taller2.movie_list.popular.PopularMovie

class movieAdapter(
    private var favoriteListener: FavoriteListener,
    private var mContext: Context,
    private var popularDataSet: MutableList<PopularMovie> = mutableListOf()) :
    RecyclerView.Adapter<movieAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        val id: TextView
        val popularity: TextView
        val ico_fav_border: Button
        val image: ImageView

        init {
            name = view.findViewById(R.id.adapt_name)
            id = view.findViewById(R.id.adapt_id)
            popularity = view.findViewById(R.id.adapt_popularity)
            ico_fav_border = view.findViewById(R.id.adapt_fav_border)
            image = view.findViewById(R.id.adapt_photo)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.movie_adapter, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val item = popularDataSet.get(position)

        viewHolder.name.text = item.title
        viewHolder.id.text = item.id
        viewHolder.popularity.text = item.popularity.toString()

        Glide.with(mContext)
            .load("https://image.tmdb.org/t/p/w500${item.backdrop_path}")
            .transform(CenterCrop())
            .into(viewHolder.image)

        var state = 0

        if(!item.isFavorite){
            viewHolder.ico_fav_border.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24)
            state = 0
        }else{
            viewHolder.ico_fav_border.setBackgroundResource(R.drawable.ic_baseline_favorite_24)
            state = 1
        }

        viewHolder.ico_fav_border.setOnClickListener(){
            if(state == 0){
                favoriteListener.onAddedFavorite(item)
                viewHolder.ico_fav_border.setBackgroundResource(R.drawable.ic_baseline_favorite_24)
                state = 1
            }
            else{
                favoriteListener.onRemoveFavorite(item)
                viewHolder.ico_fav_border.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24)
                state = 0
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount() = popularDataSet.size

    fun updatePopularMovies(popularMovies: List<PopularMovie>) {
        popularDataSet.clear()
        popularDataSet.addAll(popularMovies)
        notifyDataSetChanged()
    }
}
