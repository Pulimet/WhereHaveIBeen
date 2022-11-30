package net.alexandroid.where.ui.tutorial

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import net.alexandroid.where.R
import net.alexandroid.where.databinding.SlideTutorialBinding
import net.alexandroid.where.ui.binding.FragmentBinding
import net.alexandroid.where.utils.getDrawableResourceByName
import net.alexandroid.where.utils.getStringResourceByName

private const val ARG_POSITION = "ARG_POSITION"

class ScreenSlidePageFragment : Fragment(R.layout.slide_tutorial) {

    companion object {
        @JvmStatic
        fun newInstance(position: Int) = ScreenSlidePageFragment().apply {
            arguments = Bundle().apply { putInt(ARG_POSITION, position) }
        }
    }

    private val binding by FragmentBinding(SlideTutorialBinding::bind)
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = arguments?.getInt(ARG_POSITION, 0) ?: 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvStep.text = getString(R.string.step, position + 1)
        binding.tvInstructions.text = requireContext().getStringResourceByName("step${position + 1}")
        val drawable = requireContext().getDrawableResourceByName("step${position + 1}")
        drawable?.let { binding.ivIcon.setImageDrawable(it) }
    }
}