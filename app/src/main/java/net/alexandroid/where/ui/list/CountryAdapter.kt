package net.alexandroid.where.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import net.alexandroid.where.R

class CountryAdapter(
    private val listener: OnCountryClickListener
) : ListAdapter<String, CountryHolder>(CountryDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        return CountryHolder(v, listener)
    }

    override fun onBindViewHolder(holder: CountryHolder, position: Int) {
        holder.onBindViewHolder(getItem(position))
    }
}