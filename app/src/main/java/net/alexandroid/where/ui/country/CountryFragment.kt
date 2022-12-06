package net.alexandroid.where.ui.country

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import net.alexandroid.where.R
import net.alexandroid.where.databinding.FragmentCountryBinding
import net.alexandroid.where.ui.binding.FragmentBinding

class CountryFragment : Fragment(R.layout.fragment_country) {
    private val binding by FragmentBinding(FragmentCountryBinding::bind)
    private val args: CountryFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvCountry.text = args.country
    }
}