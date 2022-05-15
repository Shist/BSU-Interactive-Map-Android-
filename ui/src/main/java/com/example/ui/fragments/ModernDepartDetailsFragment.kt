package com.example.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.domain.StructuralObjectItem
import com.example.ui.R
import com.example.ui.databinding.ModernDepartDetailsBinding
import org.koin.core.component.KoinComponent

// This is fragment inflates when user click button "Details" on one of departments of modern icon dialog window
class ModernDepartDetailsFragment : Fragment(), KoinComponent {

    companion object {
        const val keyItemID = "itemID"
        fun newInstance(department: StructuralObjectItem) = HistBuildingDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(keyItemID, department)
            }
        }
    }

    private var _binding: ModernDepartDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ModernDepartDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val department = arguments?.getParcelable<StructuralObjectItem>(keyItemID)

        val pageLogotypeButtonLink: Button = binding.btnLogotypeWithWebLink
        val pageTitle: TextView = binding.title
        val pageImgSpinner: Spinner = binding.imgSpinner
        val pageTextBlock1: TextView = binding.infoBlock1
        val pageTitleHistReference: TextView = binding.histInfoTitle
        val pageTextBlock2: TextView = binding.infoBlock2

        // TODO Add logotype as fg of button and link to website
        //pageLogotypeButtonLink
        pageTitle.text = department?.subdivision
        // TODO Add images (with Picasso) of building to spinner
        // pageImgSpinner
        pageTextBlock1.text = department?.description?.substringBefore("ИСТОРИЧЕСКАЯ СПАРВКА")
        pageTitleHistReference.text = requireContext().resources.getString(R.string.historical_information)
        pageTextBlock2.text = department?.description?.substringAfter("ИСТОРИЧЕСКАЯ СПРАВКА")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}