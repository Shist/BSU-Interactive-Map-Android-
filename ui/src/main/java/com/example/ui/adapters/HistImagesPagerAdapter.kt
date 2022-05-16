package com.example.ui.adapters

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.domain.BuildingItemImage
import com.example.ui.databinding.HistBuildDetailsBinding
import com.example.ui.databinding.ImgPagerItemBinding
import com.squareup.picasso.Picasso

class HistImagesPagerAdapter(histItemBinding: HistBuildDetailsBinding, _context: Context) : PagerAdapter() {

    private var context : Context = _context
    private val layoutInflater : LayoutInflater = LayoutInflater.from(context)
    private val pager : ViewPager
    private var pages : List<ImageView> = emptyList()

    // View of one of several images needed for pager
    private var _imgPagerItemBinding: ImgPagerItemBinding? = null
    private val imgPagerItemBinding get() = _imgPagerItemBinding!!

    private var currentImageObject: BuildingItemImage? = null

    init {
        histItemBinding.imgPager.also { pager = it }
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    fun setNewImageWithDescription(imageObject: BuildingItemImage?) {
        currentImageObject = imageObject
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        ImgPagerItemBinding.inflate(layoutInflater, container, false)
        val imageView = imgPagerItemBinding.pagerImg
        Picasso.get().load("http://map.bsu.by/buildings_images/historical_buildings/" +
                currentImageObject?.imagePath).into(imageView)
        val textView = imgPagerItemBinding.info
        textView.text = Html.fromHtml(currentImageObject?.description, Html.FROM_HTML_MODE_LEGACY).toString()
        container.addView(imgPagerItemBinding.pagerView)
        return super.instantiateItem(container, position)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }

}