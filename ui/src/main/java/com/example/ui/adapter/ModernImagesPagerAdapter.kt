package com.example.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.ui.databinding.ImgPagerItemBinding
import com.example.ui.databinding.ModernDepartDetailsBinding

class ModernImagesPagerAdapter(modernItemBinding: ModernDepartDetailsBinding, _context: Context) :
    PagerAdapter() {

    private var context : Context = _context
    private val layoutInflater : LayoutInflater = LayoutInflater.from(context)
    private val pager : ViewPager
    private var pages : List<ImageView> = emptyList()

    // View of one of several images needed for pager
    private var _imgPagerItemBinding: ImgPagerItemBinding? = null
    private val imgPagerItemBinding get() = _imgPagerItemBinding!!

    init {
        pager = modernItemBinding.imgPager
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        ImgPagerItemBinding.inflate(layoutInflater, container, false)
        val imageView = imgPagerItemBinding.pagerImg
        container.addView(imageView)
        return super.instantiateItem(container, position)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }

}