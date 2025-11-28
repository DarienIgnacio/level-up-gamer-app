package com.example.level_up_gamer_app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.level_up_gamer_app.Model.Producto

@Dao
interface ProductoDao {

    @Query("SELECT * FROM productos")
    suspend fun getAll(): List<Producto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(productos: List<Producto>)
}
