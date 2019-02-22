package com.mordeccai.quickcam.activities.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.View
import com.mordeccai.quickcam.R
import com.mordeccai.quickcam.activities.BasePresenter
import com.mordeccai.quickcam.adapters.ProductsAdapter
import com.mordeccai.quickcam.activities.product.ProductActivity
import com.mordeccai.quickcam.viewmodels.ProductViewModel
import org.joda.time.DateTime
import java.io.*






class HomePresenter(private val ctx: HomeActivity): BasePresenter(ctx){
    val NEW_PRODUCT = 1
    var products: MutableList<ProductViewModel> = mutableListOf()
    val productsAdapter = ProductsAdapter(ctx, true, products)

    fun deleteAllRecords(){
        confirm("Delete all?", "This will delete all records of your images. You cannot reverse this process", checkBoxText = "Delete images",
                hasCheckBox = false,
                onOk= {it ->
                    onConfirmDeleteAll(it)
        })
    }

    fun onConfirmDeleteAll(deleteImages: Boolean){
            Thread{
                db.productDao().deleteAll()
                products.clear()
                notifyChange()
            }.start()
    }

    fun hasProducts(): Boolean{
        return products.size > 0
    }

    fun openAppAbout(){
        alert(ctx.getString(R.string.app_name), "v${ctx.resources.getString(R.string.app_version)} \nDate: ${ctx.resources.getString(R.string.app_release_date)} \nDeveloper: ${ctx.resources.getString(R.string.app_developer)}")
    }

    fun downloadDataAsCsv(){
        if(products.size > 0){
            var csvString = "Name,Front,Back,Side A,Side B"
            for (i in 0 until products.size) {
                csvString += "\r\n" + "${products[i].product.name},${ctx.getImageName(products[i].product.front)},${ctx.getImageName(products[i].product.back)},${ctx.getImageName(products[i].product.sideA)},${ctx.getImageName(products[i].product.sideB)}"
            }
            writeToCsvFile(csvString, "qc_products-${DateTime.now().millis}")
        } else {
            alert("No products", "You cannot export an empty set of data as csv")
        }
    }

    fun writeToCsvFile( data: String, fileName: String) {
        try {
            val sdcard: File = Environment.getExternalStorageDirectory()
            val dir: File = File(sdcard.getAbsolutePath() + "/QuickCam")
            dir.mkdir()
            val file: File= File(dir, "$fileName.csv")
            val os = FileOutputStream(file)
            os.write(data.toByteArray())
            os.close()
            alert("Download successful","The products data was downloaded and saved successfuly as $fileName.csv")
        }
        catch( e: IOException){
            alert("Download unsuccessful", "We need permissions to save your file. Please provide required permissions to save the file.")
            Log.e("Exception", "File write failed: " + e.toString())
        }
    }

    fun addProduct(){
        ctx.saveTargetStatus()
        val intent= Intent(ctx, ProductActivity::class.java)
        ctx.startActivityForResult(intent, NEW_PRODUCT)
    }

    fun loadData(){
        products.clear()
        db.productDao().findAll().forEach { product ->
            products.add(ProductViewModel(product, ctx))
        }
        notifyChange()
    }

    override fun onInit(){
        loadData()
    }
}
