package com.sovegetables.base

interface BottomTab {
    fun navigateVideoTab(positionVideo: Int)
    fun navigateHomeTab()
    fun getCurrentTab() : Int
    fun navigateTab(tab: Int)
    fun navigateTab(tab: Int, withoutOneKeyLogin: Boolean)
}