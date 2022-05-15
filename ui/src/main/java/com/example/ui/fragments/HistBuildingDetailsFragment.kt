package com.example.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.domain.BuildingItem
import com.example.ui.databinding.HistBuildDetailsBinding
import org.koin.core.component.KoinComponent

// This is fragment inflates when user click button "Details" on historical icon dialog window
class HistBuildingDetailsFragment : Fragment(), KoinComponent {

    companion object {
        const val keyItemID = "itemID"
        fun newInstance(building: BuildingItem) = HistBuildingDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(keyItemID, building)
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

        val building = arguments?.getParcelable<BuildingItem>(keyItemID)

        val pageTitle: TextView = binding.title
        val pageImgSpinner: Spinner = binding.imgSpinner
        val pageText: TextView = binding.info

        pageTitle.text = building?.name
        // TODO Add images (with Picasso) of building to spinner
        // pageImgSpinner
        pageText.text = building?.address?.description
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}