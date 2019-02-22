package com.mordeccai.quickcam.adapters

import android.content.Context
import android.view.View
import android.widget.Toast
import com.mordeccai.quickcam.BR
import com.mordeccai.quickcam.R
import com.mordeccai.quickcam.data.models.Product
import com.mordeccai.quickcam.viewmodels.ProductViewModel

class ProductsAdapter(
        private val context: Context,
        private val isAlternate: Boolean,
        private val data: MutableList<ProductViewModel>
) : BaseRecyclerAdapter() {
    override fun getLayoutIdForPosition(position: Int) = R.layout.product_view

    override fun getData(position: Int) = data[position]

    override fun getItemCount() = data.size
}
