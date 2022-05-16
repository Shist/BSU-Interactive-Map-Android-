package com.example.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.domain.BuildingItemImage
import com.example.ui.databinding.ModernDepartDetailsBinding
import com.example.ui.databinding.ImgPagerItemBinding
import com.squareup.picasso.Picasso

class ModernImagesPagerAdapter(modernItemBinding: ModernDepartDetailsBinding, _context: Context) : PagerAdapter() {

    private var context : Context = _context
    private val layoutInflater : LayoutInflater = LayoutInflater.from(context)
    private val pager : ViewPager
    private var pages : List<ImageView> = emptyList()

    // View of one of several images needed for pager
    private var _imgPagerItemBinding: ImgPagerItemBinding? = null
    private val imgPagerItemBinding get() = _imgPagerItemBinding!!

    private var currentImageObject: BuildingItemImage? = null

    init {
        modernItemBinding.imgPager.also { pager = it }
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as ConstraintLayout
    }

    fun setNewImageWithDescription(imageObject: BuildingItemImage?) {
        currentImageObject = imageObject
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        _imgPagerItemBinding = ImgPagerItemBinding.inflate(layoutInflater, container, false)
        val imageView = imgPagerItemBinding.pagerImg
        Picasso.get().load("http://map.bsu.by/buildings_images/modern_buildings/" +
                currentImageObject?.imagePath).into(imageView)
        val textView = imgPagerItemBinding.info
        textView.text = currentImageObject?.description
        container.addView(imgPagerItemBinding.pagerView)
        return imgPagerItemBinding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }

}