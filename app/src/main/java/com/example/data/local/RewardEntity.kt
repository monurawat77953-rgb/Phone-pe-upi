package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rewards")
data class RewardEntity(
  @PrimaryKey
  val id: String,
  val title: String,
  val subtitle: String,
  val cashbackAmount: Double = 0.0,
  val couponCode: String = "",
  val isScratched: Boolean = false,
  val brandName: String = "PhonePe",
  val expiryText: String = "Expires in 7 days"
)
