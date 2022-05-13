package com.example.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.StructuralObjectItem
import com.example.ui.databinding.DepartmentPreviewInfoBinding

class DepartmentsListAdapter(context: Context) :
    ListAdapter<StructuralObjectItem, DepartmentsListAdapter.ItemViewHolder>(DepartmentsDiffCallback()) {

    private val context = context

    class ItemViewHolder: RecyclerView.ViewHolder {

        val departmentPreviewTitle: TextView
        val dpBtnSeeDetails: Button
        val dpBtnCreateRoute: Button
        val dpBtnSee3DModel: Button

        constructor(oddBinding: DepartmentPreviewInfoBinding, context: Context): super(oddBinding.root) {
            departmentPreviewTitle = oddBinding.departmentPreviewTitle
            dpBtnSeeDetails = oddBinding.modernIconBtnSeeDetails
            dpBtnCreateRoute = oddBinding.modernIconBtnCreateRoute
            dpBtnSee3DModel = oddBinding.modernIconBtnSee3dModel
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val departmentObject = getItem(position)
        holder.departmentPreviewTitle.text = departmentObject.subdivision
        holder.dpBtnSeeDetails.setOnClickListener {
            // TODO вызвать onClick() из MainActivity
        }
        holder.dpBtnCreateRoute.setOnClickListener {
            // TODO вызвать onClick() из MainActivity
        }
        holder.dpBtnSee3DModel.setOnClickListener {
            // TODO вызвать onClick() из MainActivity
        }
        holder.dpBtnSeeDetails.setBackgroundColor(Color.parseColor("#21386D"))
        holder.dpBtnCreateRoute.setBackgroundColor(Color.parseColor("#21386D"))
        holder.dpBtnSee3DModel.setBackgroundColor(Color.parseColor("#21386D"))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(DepartmentPreviewInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false), context)
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