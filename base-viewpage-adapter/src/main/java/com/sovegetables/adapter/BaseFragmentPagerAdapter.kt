package com.sovegetables.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

open class BaseFragmentPagerAdapter<T:Fragment>(fm: FragmentManager, behavior: Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) : FragmentStatePagerAdapter(fm, behavior) {

    protected var fragments : List<T>? = null

    open fun setData(fragments: List<T>?){
        this.fragments = fragments
        notifyDataSetChanged()
    }

    open fun getData(): List<T>?{
        return fragments
    }

    override fun getItem(position: Int): T {
        return fragments?.get(position)!!
    }

    override fun getCount(): Int {
        return fragments?.size ?: 0
    }
}