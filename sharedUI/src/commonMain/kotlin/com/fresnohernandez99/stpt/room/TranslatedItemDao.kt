package com.fresnohernandez99.stpt.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.paging.PagingSource
import com.fresnohernandez99.stpt.data.local.TranslatedItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslatedItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: TranslatedItem)

    @Update
    suspend fun update(item: TranslatedItem)

    @Delete
    suspend fun delete(item: TranslatedItem)

    @Query("SELECT * FROM translated_item ORDER BY updateAt DESC")
    fun getAll(): PagingSource<Int, TranslatedItem>

    @Query("SELECT * FROM translated_item ORDER BY updateAt DESC LIMIT 3")
    suspend fun getLast3(): List<TranslatedItem>

    @Query("SELECT * FROM translated_item WHERE id = :id")
    suspend fun getById(id: Long): TranslatedItem?
}
