package com.deinega95.clientMqtt.view.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.model.Message
import com.deinega95.clientMqtt.utils.Utils
import kotlinx.android.synthetic.main.item_image_viewer.view.*


class GalleryAdapter : PagerAdapter() {

    private var data = listOf<Message>()

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return data.size
    }

    fun setData(photos: List<Message>) {
        this.data = photos
        notifyDataSetChanged()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.item_image_viewer, container, false)
        view.viewerImage.showImage(Uri.parse(Utils.getDataURL(data[position].image!!)))
        view.viewerImage.setTapToRetry(true)
        container.addView(view)
        return view
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}