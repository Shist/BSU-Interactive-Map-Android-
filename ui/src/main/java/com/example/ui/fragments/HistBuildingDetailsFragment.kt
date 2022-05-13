package com.example.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.domain.BuildingItem
import com.example.ui.databinding.HistDepartDetailsBinding
import org.koin.core.component.KoinComponent

class HistBuildingDetailsFragment : Fragment(), KoinComponent {

    companion object {
        const val keyItemID = "itemID"
        fun newInstance(building: BuildingItem) = HistBuildingDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(keyItemID, building)
            }
        }
    }

    private var _binding: HistDepartDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HistDepartDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val building = arguments?.getParcelable<BuildingItem>(keyItemID)

        val pageTitle: TextView = binding.histDepartTitle
        val pageImgSpinner: Spinner = binding.histDepartImgSpinner
        val pageText: TextView = binding.histDepartAddressInfo

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