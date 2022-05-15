package com.example.ui.adapter

import android.graphics.Color
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

        constructor(oddBinding: ModernDepartInfoBinding, activity: MainActivity): super(oddBinding.root) {
            departmentPreviewTitle = oddBinding.title
            dpBtnSeeDetails = oddBinding.btnSeeDetails
            dpBtnCreateRoute = oddBinding.btnCreateRoute
            dpBtnSee3DModel = oddBinding.btnSee3dModel
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
        holder.dpBtnSeeDetails.setBackgroundColor(Color.parseColor("#21386D"))
        holder.dpBtnCreateRoute.setBackgroundColor(Color.parseColor("#21386D"))
        holder.dpBtnSee3DModel.setBackgroundColor(Color.parseColor("#21386D"))
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