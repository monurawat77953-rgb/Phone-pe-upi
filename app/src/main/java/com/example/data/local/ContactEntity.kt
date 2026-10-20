package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactEntity(
  @PrimaryKey
  val id: String,
  val name: String,
  val phone: String,
  val upiId: String,
  val avatarColorHex: String = "#5F259F",
  val isFavorite: Boolean = false,
  val lastPaidTimestamp: Long = 0L
)
