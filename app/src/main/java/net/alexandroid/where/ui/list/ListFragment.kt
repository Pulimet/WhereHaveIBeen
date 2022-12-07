package net.alexandroid.where.ui.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import net.alexandroid.where.R
import net.alexandroid.where.databinding.FragmentListBinding
import net.alexandroid.where.ui.binding.FragmentBinding
import net.alexandroid.where.utils.collectIt

class ListFragment : Fragment(R.layout.fragment_list), OnCountryClickListener {

    private val binding by FragmentBinding(FragmentListBinding::bind)
    private val viewModel: ListViewModel by viewModel()
    private var countryAdapter: CountryAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        viewModel.apply {
            getLocations().collectIt(viewLifecycleOwner) { list ->
                countryAdapter?.submitList(list)
            }
            navigateToDetails.collectIt(viewLifecycleOwner) {
                findNavController().navigate(ListFragmentDirections.actionListFragmentToCountryFragment(it))
            }
        }
    }

    private fun setRecyclerView() {
        binding.homeRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CountryAdapter(this@ListFragment)
                .apply { countryAdapter = this }
        }
    }

    // OnCountryClickListener
    override fun onClick(country: String) {
        viewModel.onCountryClick(country)
    }
}