package com.example.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.BuildingItemImage
import com.example.domain.StructuralObjectItem
import com.example.ui.MainActivity
import com.example.ui.databinding.ModernDepartInfoBinding

// This class is needed to place list of structural object (of modern building) to RecycleView
class DepartmentsListAdapter(imagesList: List<BuildingItemImage?>?,
                             private val activity: MainActivity
) :
    ListAdapter<StructuralObjectItem, DepartmentsListAdapter.ItemViewHolder>(DepartmentsDiffCallback()) {

    private val imageList: List<BuildingItemImage?>? = imagesList

    class ItemViewHolder(itemBinding: ModernDepartInfoBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        val departmentPreviewTitle: TextView
        val dpBtnSeeDetails: Button
        val dpBtnCreateRoute: Button
        val dpBtnSee3DModel: Button

        init {
            departmentPreviewTitle = itemBinding.title
            dpBtnSeeDetails = itemBinding.btnSeeDetails
            dpBtnCreateRoute = itemBinding.btnCreateRoute
            dpBtnSee3DModel = itemBinding.btnSee3dModel
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val departmentObject = getItem(position)
        holder.departmentPreviewTitle.text = departmentObject.subdivision
        holder.dpBtnSeeDetails.setOnClickListener {
            activity.onModernBuildingClick(departmentObject, imageList)
        }
        holder.dpBtnCreateRoute.setOnClickListener {
            // TODO вызвать onClick() из MainActivity для маршрута
        }
        holder.dpBtnSee3DModel.setOnClickListener {
            // TODO вызвать onClick() из MainActivity для 3D модели
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ModernDepartInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

}

class DepartmentsDiffCallback : DiffUtil.ItemCallback<StructuralObjectItem>() {
    override fun areItemsTheSame(oldItem: StructuralObjectItem, newItem: StructuralObjectItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: StructuralObjectItem, newItem: StructuralObjectItem): Boolean {
        return oldItem == newItem
    }
}