package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
  @PrimaryKey
  val id: String,
  val title: String,
  val recipientUpiId: String = "",
  val recipientPhone: String = "",
  val amount: Double,
  val type: String, // "DEBIT" or "CREDIT"
  val status: String = "SUCCESS", // "SUCCESS", "PENDING", "FAILED"
  val category: String = "TRANSFER", // "TRANSFER", "RECHARGE", "BILL", "CASHBACK", "GOLD", "WALLET_TOPUP", "MERCHANT"
  val timestamp: Long = System.currentTimeMillis(),
  val utrNumber: String = "",
  val note: String = "",
  val debitedFrom: String = "State Bank of India ••4821"
)
