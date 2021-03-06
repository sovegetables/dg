package com.sovegetables.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import com.sovegetables.topnavbar.TopBar

abstract class BaseViewStubFragment<VB: ViewBinding> : com.sovegetables.base.BaseFragment() {

    private var mSavedInstanceState: Bundle? = null
    private var hasInflated = false
    private lateinit var container: FrameLayout
    private var layoutResources: Int = -1
    private var _binding: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    companion object{
        private const val KEY_HAS_INFLATED = "KEY.BaseViewStubFragment.INFLATED"
        private const val TAG = "BaseViewStubFragment"
    }


    final override fun onBaseCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_viewstub, container, false)
    }

    @CallSuper
    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        container = view.findViewById(R.id.fragment_sub_container)
        super.onViewCreated(view, savedInstanceState)
        if(!hasInflated){
            showLoading()
            layoutResources = getViewStubLayoutResource()
        }
        mSavedInstanceState = savedInstanceState
        if(!enableLazyLoading()){
            loadRealLayout()
        }
    }

    protected open fun enableLazyLoading(): Boolean {
        return true
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        loadRealLayout()
    }

    private fun loadRealLayout() {
        if(!hasInflated){
            val viewGroup = container as ViewGroup
            viewGroup.removeAllViews()
            val inflatedView: View
            if(bindingInflater != null){
                _binding = bindingInflater.invoke(layoutInflater, viewGroup, false)
                inflatedView = _binding!!.root
            }else{
                inflatedView = LayoutInflater.from(requireContext()).inflate(
                    layoutResources,
                    viewGroup,
                    false
                )
            }
            hasInflated = true
            viewGroup.addView(inflatedView)
            onCreateViewAfterViewStubInflated(inflatedView, mSavedInstanceState)
            afterViewStubInflated(view)
        }

    }

    protected abstract fun onCreateViewAfterViewStubInflated(
        inflatedView: View,
        savedInstanceState: Bundle?
    )

    /**
     * The layout ID associated with this ViewStub
     * @see ViewStub.setLayoutResource
     * @return
     */
    @LayoutRes
    protected abstract fun getViewStubLayoutResource(): Int

    /**
     *
     * @param originalViewContainerWithViewStub
     */
    @CallSuper
    protected fun afterViewStubInflated(originalViewContainerWithViewStub: View?) {
        if (originalViewContainerWithViewStub != null) {
           hideLoading()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_HAS_INFLATED, hasInflated)
    }

    protected fun hasInflated() : Boolean{
        return hasInflated
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: ")
        hasInflated = false
    }

    override fun onDestroy() {
        super.onDestroy()
        hasInflated = false
    }

    open fun onPageFragmentSelected(){
    }

    open fun onPageFragmentReleased(){
    }

    open fun getPageTitle(): CharSequence?{
        return null
    }
    protected fun topBar(title: CharSequence?): TopBar {
        return TopBar.Builder()
            .title(title)
            .build(activity)
    }
}