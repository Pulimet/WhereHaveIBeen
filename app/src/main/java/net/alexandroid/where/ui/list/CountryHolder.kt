package net.alexandroid.where.ui.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import net.alexandroid.where.databinding.ItemCountryBinding

class CountryHolder(v: View, private val listener: OnCountryClickListener) :
    RecyclerView.ViewHolder(v), View.OnClickListener {
    private var country = ""

    init {
        itemView.setOnClickListener(this)
    }

    private val binding = ItemCountryBinding.bind(v)

    fun onBindViewHolder(country: String) {
        this.country = country
        binding.tvListItem.text = country
    }

    // View.OnClickListener
    override fun onClick(v: View?) {
        if (adapterPosition != RecyclerView.NO_POSITION) {
            listener.onClick(country)
        }
    }
}