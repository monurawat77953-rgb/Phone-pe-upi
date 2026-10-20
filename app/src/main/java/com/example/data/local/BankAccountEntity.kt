package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bank_accounts")
data class BankAccountEntity(
  @PrimaryKey
  val id: String,
  val bankName: String,
  val accountNumberLast4: String,
  val ifsc: String,
  val accountType: String = "Savings Account",
  val balance: Double,
  val isPrimary: Boolean = false,
  val upiPin: String = "1234",
  val upiId: String = ""
)
