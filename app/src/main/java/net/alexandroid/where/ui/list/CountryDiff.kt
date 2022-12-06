package net.alexandroid.where.ui.list

import androidx.recyclerview.widget.DiffUtil

class CountryDiff : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
}