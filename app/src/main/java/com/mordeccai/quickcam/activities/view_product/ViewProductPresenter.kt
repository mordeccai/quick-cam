package com.mordeccai.quickcam.activities.view_product

import com.mordeccai.quickcam.activities.BasePresenter
import com.mordeccai.quickcam.adapters.PhotosAdapter
import com.mordeccai.quickcam.adapters.ProductsAdapter
import com.mordeccai.quickcam.data.models.Photo
import com.mordeccai.quickcam.viewmodels.PhotoViewModel
import com.mordeccai.quickcam.viewmodels.ProductViewModel
import android.content.Intent.getIntent



class ViewProductPresenter(val ctx: ViewProductActivity): BasePresenter(ctx) {
    var photos: MutableList<PhotoViewModel> = mutableListOf()
    val photosAdapter = PhotosAdapter(ctx, true, photos)
    val productId: Int = ctx.getIntent().getIntExtra("productId", 0)

    fun loadPhotos(){
        photos.clear()
        db.productDao().findProductById(productId).forEach {
            if(it.front !=""){
                val photo = Photo(name = "Front", path = it.front)
                val photoModel = PhotoViewModel(photo, ctx)
                photos.add(photoModel)
            }
            if(it.back !=""){
                val photo = Photo(name = "Back", path = it.back)
                val photoModel = PhotoViewModel(photo, ctx)
                photos.add(photoModel)
            }
            if(it.sideA !=""){
                val photo = Photo(name = "Side A", path = it.sideA)
                val photoModel = PhotoViewModel(photo, ctx)
                photos.add(photoModel)
            }

            if(it.sideB !=""){
                val photo = Photo(name = "Side B", path = it.sideB)
                val photoModel = PhotoViewModel(photo, ctx)
                photos.add(photoModel)
            }
        }
        notifyChange()
    }
    override fun onInit() {
        loadPhotos()
    }
}