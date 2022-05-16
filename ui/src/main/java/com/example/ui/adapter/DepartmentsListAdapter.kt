package com.example.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.StructuralObjectItem
import com.example.ui.MainActivity
import com.example.ui.databinding.ModernDepartInfoBinding

// This class is needed to place list of structural object (of modern building) to RecycleView
class DepartmentsListAdapter(activity: MainActivity) :
    ListAdapter<StructuralObjectItem, DepartmentsListAdapter.ItemViewHolder>(DepartmentsDiffCallback()) {

    private val activity = activity

    class ItemViewHolder: RecyclerView.ViewHolder {

        val departmentPreviewTitle: TextView
        val dpBtnSeeDetails: Button
        val dpBtnCreateRoute: Button
        val dpBtnSee3DModel: Button
        private val activity: MainActivity

        constructor(itemBinding: ModernDepartInfoBinding, activity: MainActivity): super(itemBinding.root) {
            departmentPreviewTitle = itemBinding.title
            dpBtnSeeDetails = itemBinding.btnSeeDetails
            dpBtnCreateRoute = itemBinding.btnCreateRoute
            dpBtnSee3DModel = itemBinding.btnSee3dModel
            this.activity = activity
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val departmentObject = getItem(position)
        holder.departmentPreviewTitle.text = departmentObject.subdivision
        holder.dpBtnSeeDetails.setOnClickListener {
            activity.onModernBuildingClick(departmentObject)
        }
        holder.dpBtnCreateRoute.setOnClickListener {
            // TODO вызвать onClick() из MainActivity для маршрута
        }
        holder.dpBtnSee3DModel.setOnClickListener {
            // TODO вызвать onClick() из MainActivity для 3D модели
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ModernDepartInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false), activity)
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