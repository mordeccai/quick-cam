package com.mordeccai.quickcam.data.models

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String = "",
    var front: String = "",
    var back: String = "",
    var sideA: String = "",
    var sideB: String = "",
    var status: Int = 0,
    var createdAt: String = ""
)

@Dao
interface ProductDao {
    @Insert
    fun insert(product: Product)

    @Update
    fun update(product: Product)

    @Delete
    fun delete(product: Product)

    @Query("DELETE FROM products")
    fun deleteAll()

    @Query("SELECT * FROM products")
    fun findAll(): List<Product>

    @Query("SELECT * FROM products WHERE id =:id")
    fun findProductById(id: Int): List<Product>

    @Query("SELECT * FROM products WHERE name =:name")
    fun findProductByName(name: String): List<Product>

    @Insert
    fun insertAll(vararg product: Product)
}