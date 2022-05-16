package com.example.ui.fragments

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.domain.BuildingItem
import com.example.domain.BuildingItemImage
import com.example.ui.adapters.HistImagesPagerAdapter
import com.example.ui.databinding.HistBuildDetailsBinding
import org.koin.core.component.KoinComponent

// This is fragment inflates when user click button "Details" on historical icon dialog window
class HistBuildingDetailsFragment : Fragment(), KoinComponent {

    companion object {
        const val buildingID = "buildingID"
        fun newInstance(building: BuildingItem) = HistBuildingDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(buildingID, building)
            }
        }
    }

    private var _binding: HistBuildDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HistBuildDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val building = arguments?.getParcelable<BuildingItem>(buildingID)

        val pageTitle: TextView = binding.title
        val pageImgPager: ViewPager = binding.imgPager
        val pageText: TextView = binding.info
        pageTitle.text = building?.name
        val adapter = HistImagesPagerAdapter(binding, requireContext())
        pageImgPager.adapter = adapter
        if (building?.imagesList != null) {
            for ((i, imageObject: BuildingItemImage?) in building.imagesList!!.withIndex()) {
                adapter.setNewImageWithDescription(imageObject)
                adapter.instantiateItem(binding.imgPager, i)
            }
        }
        pageText.text = Html.fromHtml(building?.address?.description, Html.FROM_HTML_MODE_LEGACY).toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}