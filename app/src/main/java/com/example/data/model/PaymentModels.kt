package com.example.data.model

data class UserProfile(
  val name: String = "Rahul Sharma",
  val phone: String = "+91 98765 43210",
  val upiId: String = "rahul.sharma@ybl",
  val qrCodeText: String = "upi://pay?pa=rahul.sharma@ybl&pn=Rahul%20Sharma&cu=INR",
  val walletBalance: Double = 1250.00,
  val upiLiteBalance: Double = 450.00,
  val goldGrams: Double = 2.45,
  val currentGoldPricePerGram: Double = 7480.00,
  val soundboxAlertsEnabled: Boolean = true,
  val soundboxLanguage: String = "HINDI" // HINDI or ENGLISH
)

enum class PaymentSourceType {
  BANK_ACCOUNT,
  WALLET,
  UPI_LITE
}

data class PaymentRequest(
  val recipientName: String,
  val recipientUpiOrPhone: String,
  val amount: Double,
  val note: String,
  val category: String = "TRANSFER",
  val source: PaymentSourceType = PaymentSourceType.BANK_ACCOUNT,
  val selectedBankAccountId: String = "bank_sbi"
)

data class PaymentResult(
  val success: Boolean,
  val transactionId: String,
  val utrNumber: String,
  val amount: Double,
  val recipientName: String,
  val debitedFrom: String,
  val timestamp: Long,
  val wonScratchCard: Boolean = true
)

data class BillCategory(
  val id: String,
  val title: String,
  val iconName: String,
  val tag: String = ""
)

data class RechargePlan(
  val id: String,
  val price: Double,
  val validity: String,
  val data: String,
  val voice: String,
  val tag: String = ""
)

data class StoreMerchant(
  val id: String,
  val name: String,
  val category: String,
  val distance: String,
  val upiId: String,
  val offer: String = "",
  val rating: Double = 4.8
)
