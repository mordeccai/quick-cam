package com.mordeccai.quickcam.adapters

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mordeccai.quickcam.BR

abstract class BaseRecyclerAdapter() : RecyclerView.Adapter<RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            RecyclerViewHolder(DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), viewType, parent, false))

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        getData(position)
                ?.let {
                    val bindingSuccess = holder.binding.setVariable(BR.viewModel, it)
                    if (!bindingSuccess) {
                        throw IllegalStateException("Binding ${holder.binding} variable name should be 'viewModel'")
                    }
                }
    }

    override fun getItemViewType(position: Int) = getLayoutIdForPosition(position)

    abstract fun getLayoutIdForPosition(position: Int): Int

    abstract fun getData(position:  Int): Any?

}

class RecyclerViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
