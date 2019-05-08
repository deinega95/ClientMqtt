package com.deinega95.clientMqtt.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.model.Message
import com.deinega95.clientMqtt.presenter.ImageViewerPresenter
import com.deinega95.clientMqtt.view.adapters.GalleryAdapter
import com.deinega95.clientMqtt.view.fragments.interfaces.IImageViewerFragment
import kotlinx.android.synthetic.main.fragment_image_viewer.*
import javax.inject.Inject

class ImageViewerFragment : BaseFragment(), IImageViewerFragment {

    @Inject
    lateinit var presenter: ImageViewerPresenter

    private var fragmentView: View? = null
    private lateinit var adapter: GalleryAdapter

    init {
        App.instance.imageViewerComponent?.inject(this)
    }

    companion object {
        enum class ImageViewerTypeEnum {
            PhotoByPeriod, Message
        }

        fun newInstance(type:ImageViewerTypeEnum, photoId: Long): ImageViewerFragment {
            val fr = ImageViewerFragment()
            fr.presenter.setParams(type, photoId)
            return fr
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.fragment_image_viewer, container, false)

        }
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = GalleryAdapter()
        images.adapter = adapter

        presenter.viewReady(this)
    }

    override fun setContent(photos: List<Message>, pos: Int) {
        adapter.setData(photos)
        titleToolbar.text = "1/${photos.size}"
        images.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                titleToolbar.text = "${position + 1}/${photos.size}"
            }
        })

        images.currentItem = pos

        closeBtn.setOnClickListener {
            presenter.onCloseClicked()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        super.onDestroy()
    }
}