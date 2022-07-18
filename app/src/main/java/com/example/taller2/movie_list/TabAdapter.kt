package com.example.taller2.movie_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.taller2.movie_list.favorite.FavoriteFragment
import com.example.taller2.movie_list.popular.PopularFragment

class TabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {


    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {

        if (position == 0){
            var fragment = PopularFragment()
            fragment.arguments = Bundle().apply {
                "populares"
            }
            return fragment
        } else {
            var fragment = FavoriteFragment()
            fragment.arguments = Bundle().apply {
                "favoritos"
            }
            return fragment
        }
    }
}