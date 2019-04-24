package com.deinega95.clientMqtt.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deinega95.clientMqtt.R
import com.deinega95.clientMqtt.di.App
import com.deinega95.clientMqtt.presenter.PhotosByPeriodPresenter
import com.deinega95.clientMqtt.view.adapters.PhotosByPeriodRecyclerViewAdapter
import com.deinega95.clientMqtt.view.fragments.interfaces.IPhotosByPeriodFragment
import kotlinx.android.synthetic.main.fragment_photos_by_period.*
import javax.inject.Inject

class PhotosByPeriodFragment : BaseFragment(), IPhotosByPeriodFragment {
    @Inject
    lateinit var presenter: PhotosByPeriodPresenter


    private lateinit var adapter: PhotosByPeriodRecyclerViewAdapter

    init {
        App.instance.photoByPeriodComponent?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_photos_by_period, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = PhotosByPeriodRecyclerViewAdapter(context!!)
        val layoutManager = GridLayoutManager(context!!, 3, RecyclerView.VERTICAL, false)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    R.layout.item_empty_header -> 3
                    R.layout.item_photo -> 1
                    else -> -1
                }
            }
        }
        list.layoutManager = layoutManager

        list.adapter = adapter



        presenter.viewReady(this)
    }

/*    override fun setContent(photos: List<Message>) {
        adapter.setData(photos)
    }*/


    override fun onDestroy() {
        super.onDestroy()
        presenter.viewDied(this)
    }
}