package net.alexandroid.where.ui.country

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import net.alexandroid.where.R
import net.alexandroid.where.databinding.FragmentCountryBinding
import net.alexandroid.where.ui.binding.FragmentBinding
import net.alexandroid.where.utils.collectIt
import org.koin.androidx.viewmodel.ext.android.viewModel

class CountryFragment : Fragment(R.layout.fragment_country) {
    private val binding by FragmentBinding(FragmentCountryBinding::bind)
    private val args: CountryFragmentArgs by navArgs()
    private val viewModel: CountryViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvCountry.text = args.country
        viewModel.apply {
            onViewCreated(args.country)
            searchResult.collectIt(viewLifecycleOwner) {
                binding.tvCountry.text = it
            }
        }
    }
}