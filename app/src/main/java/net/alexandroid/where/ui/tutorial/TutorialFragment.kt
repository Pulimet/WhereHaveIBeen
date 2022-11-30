package net.alexandroid.where.ui.tutorial

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import net.alexandroid.where.R
import net.alexandroid.where.databinding.FragmentTutorialBinding
import net.alexandroid.where.ui.binding.FragmentBinding

class TutorialFragment : Fragment(R.layout.fragment_tutorial) {

    private val binding by FragmentBinding(FragmentTutorialBinding::bind)
    private var selectedPosition = 0

    private val viewPagerCallBack = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            selectedPosition = position
            when (selectedPosition) {
                2 -> binding.btnNextStep.setText(R.string.upload_fragment_label)
                else -> binding.btnNextStep.setText(R.string.next)
            }
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            binding.motionLayout.progress = positionOffset
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNextStep.setOnClickListener { onBtnClick() }
        binding.pager.apply {
            adapter = ScreenSlidePagerAdapter(requireActivity())
            setPageTransformer(ZoomOutPageTransformer())
        }
    }

    override fun onResume() {
        super.onResume()
        binding.pager.registerOnPageChangeCallback(viewPagerCallBack)
    }

    override fun onPause() {
        super.onPause()
        binding.pager.registerOnPageChangeCallback(viewPagerCallBack)
    }

    private fun onBtnClick() {
        if (selectedPosition > 1) {
            findNavController().navigate(TutorialFragmentDirections.toUploadFragment())
        } else {
            binding.pager.currentItem = selectedPosition + 1
        }
    }
}