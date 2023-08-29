package com.sunnyweather.android.ui.listener

interface ItemClickListener<T> {

    fun onItemClicked(position: Int, t: T)
}