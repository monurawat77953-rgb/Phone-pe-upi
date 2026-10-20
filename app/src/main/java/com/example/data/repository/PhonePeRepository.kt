package com.example.data.repository

import com.example.data.local.BankAccountEntity
import com.example.data.local.ContactEntity
import com.example.data.local.PhonePeDao
import com.example.data.local.RewardEntity
import com.example.data.local.TransactionEntity
import com.example.data.model.PaymentRequest
import com.example.data.model.PaymentResult
import com.example.data.model.PaymentSourceType
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class PhonePeRepository(
  private val dao: PhonePeDao
) {

  private val _userProfile = MutableStateFlow(UserProfile())
  val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

  val transactions: Flow<List<TransactionEntity>> = dao.getAllTransactions()
  val bankAccounts: Flow<List<BankAccountEntity>> = dao.getAllBankAccounts()
  val contacts: Flow<List<ContactEntity>> = dao.getAllContacts()
  val rewards: Flow<List<RewardEntity>> = dao.getAllRewards()

  suspend fun processPayment(request: PaymentRequest): PaymentResult {
    val txnId = "T" + System.currentTimeMillis().toString().takeLast(8) + Random.nextInt(1000, 9999)
    val utr = "428" + Random.nextLong(100000000L, 999999999L)
    val now = System.currentTimeMillis()

    var debitedFromDesc = "State Bank of India ••4821"

    when (request.source) {
      PaymentSourceType.BANK_ACCOUNT -> {
        val accounts = dao.getAllBankAccounts().firstOrNull() ?: emptyList()
        val account = accounts.find { it.id == request.selectedBankAccountId }
          ?: accounts.firstOrNull { it.isPrimary }
          ?: accounts.firstOrNull()

        if (account != null) {
          debitedFromDesc = "${account.bankName} ••${account.accountNumberLast4}"
          val updatedBalance = (account.balance - request.amount).coerceAtLeast(0.0)
          dao.updateBalance(account.id, updatedBalance)
        }
      }
      PaymentSourceType.WALLET -> {
        debitedFromDesc = "PhonePe Wallet"
        _userProfile.update {
          it.copy(walletBalance = (it.walletBalance - request.amount).coerceAtLeast(0.0))
        }
      }
      PaymentSourceType.UPI_LITE -> {
        debitedFromDesc = "UPI Lite"
        _userProfile.update {
          it.copy(upiLiteBalance = (it.upiLiteBalance - request.amount).coerceAtLeast(0.0))
        }
      }
    }

    val transaction = TransactionEntity(
      id = txnId,
      title = request.recipientName,
      recipientUpiId = request.recipientUpiOrPhone,
      recipientPhone = if (request.recipientUpiOrPhone.startsWith("+") || request.recipientUpiOrPhone.all { it.isDigit() || it == ' ' }) request.recipientUpiOrPhone else "",
      amount = request.amount,
      type = "DEBIT",
      status = "SUCCESS",
      category = request.category,
      timestamp = now,
      utrNumber = utr,
      note = request.note.ifBlank { "Payment to ${request.recipientName}" },
      debitedFrom = debitedFromDesc
    )

    dao.insertTransaction(transaction)

    // Check if this recipient is in contacts, update lastPaidTimestamp
    val currentContacts = dao.getAllContacts().firstOrNull() ?: emptyList()
    val matchedContact = currentContacts.find {
      it.name.equals(request.recipientName, ignoreCase = true) ||
          it.upiId.equals(request.recipientUpiOrPhone, ignoreCase = true)
    }

    if (matchedContact != null) {
      dao.updateContact(matchedContact.copy(lastPaidTimestamp = now))
    } else if (request.category == "TRANSFER") {
      // Add newly paid person to contacts list
      dao.insertContact(
        ContactEntity(
          id = "c_${System.currentTimeMillis()}",
          name = request.recipientName,
          phone = if (request.recipientUpiOrPhone.contains("@")) "" else request.recipientUpiOrPhone,
          upiId = if (request.recipientUpiOrPhone.contains("@")) request.recipientUpiOrPhone else "${request.recipientName.lowercase().replace(" ", "")}@ybl",
          avatarColorHex = listOf("#5F259F", "#1976D2", "#00A86B", "#E91E63", "#FF9800").random(),
          lastPaidTimestamp = now
        )
      )
    }

    // Award a scratch card reward for transactions >= ₹50
    val wonReward = request.amount >= 50.0
    if (wonReward) {
      val cashbackWon = listOf(10.0, 15.0, 25.0, 30.0, 50.0).random()
      val newReward = RewardEntity(
        id = "rew_${System.currentTimeMillis()}",
        title = "Cashback Won!",
        subtitle = "On transfer to ${request.recipientName}",
        cashbackAmount = cashbackWon,
        couponCode = "CASH" + Random.nextInt(100, 999),
        isScratched = false,
        brandName = "PhonePe Rewards",
        expiryText = "Valid for 7 days"
      )
      dao.insertReward(newReward)
    }

    return PaymentResult(
      success = true,
      transactionId = txnId,
      utrNumber = utr,
      amount = request.amount,
      recipientName = request.recipientName,
      debitedFrom = debitedFromDesc,
      timestamp = now,
      wonScratchCard = wonReward
    )
  }

  suspend fun scratchReward(rewardId: String, cashbackAmount: Double) {
    dao.markRewardScratched(rewardId)
    if (cashbackAmount > 0.0) {
      // Add cashback to wallet
      _userProfile.update {
        it.copy(walletBalance = it.walletBalance + cashbackAmount)
      }
      // Record transaction
      val cashbackTxn = TransactionEntity(
        id = "CB" + System.currentTimeMillis().toString().takeLast(8),
        title = "PhonePe Cashback Won",
        recipientUpiId = "rewards@phonepe",
        amount = cashbackAmount,
        type = "CREDIT",
        status = "SUCCESS",
        category = "CASHBACK",
        timestamp = System.currentTimeMillis(),
        utrNumber = "428" + Random.nextLong(100000000L, 999999999L),
        note = "Cashback credited to PhonePe Wallet",
        debitedFrom = "Credited to PhonePe Wallet"
      )
      dao.insertTransaction(cashbackTxn)
    }
  }

  suspend fun topUpWallet(amount: Double) {
    _userProfile.update {
      it.copy(walletBalance = it.walletBalance + amount)
    }
    val txn = TransactionEntity(
      id = "WL" + System.currentTimeMillis().toString().takeLast(8),
      title = "Wallet Top-up",
      recipientUpiId = "wallet@phonepe",
      amount = amount,
      type = "DEBIT",
      status = "SUCCESS",
      category = "WALLET_TOPUP",
      timestamp = System.currentTimeMillis(),
      utrNumber = "428" + Random.nextLong(100000000L, 999999999L),
      note = "Money added to PhonePe Wallet",
      debitedFrom = "State Bank of India ••4821"
    )
    dao.insertTransaction(txn)
  }

  suspend fun topUpUpiLite(amount: Double) {
    _userProfile.update {
      it.copy(upiLiteBalance = it.upiLiteBalance + amount)
    }
    val txn = TransactionEntity(
      id = "UL" + System.currentTimeMillis().toString().takeLast(8),
      title = "UPI Lite Top-up",
      recipientUpiId = "upilite@phonepe",
      amount = amount,
      type = "DEBIT",
      status = "SUCCESS",
      category = "WALLET_TOPUP",
      timestamp = System.currentTimeMillis(),
      utrNumber = "428" + Random.nextLong(100000000L, 999999999L),
      note = "Funds added to UPI Lite balance",
      debitedFrom = "State Bank of India ••4821"
    )
    dao.insertTransaction(txn)
  }

  suspend fun buyDigitalGold(amount: Double, grams: Double) {
    _userProfile.update {
      it.copy(goldGrams = it.goldGrams + grams)
    }
    val txn = TransactionEntity(
      id = "GD" + System.currentTimeMillis().toString().takeLast(8),
      title = "24K Digital Gold Purchase",
      recipientUpiId = "safegold@phonepe",
      amount = amount,
      type = "DEBIT",
      status = "SUCCESS",
      category = "GOLD",
      timestamp = System.currentTimeMillis(),
      utrNumber = "428" + Random.nextLong(100000000L, 999999999L),
      note = "Bought %.4f gm 24K 99.9%% Pure Gold".format(grams),
      debitedFrom = "State Bank of India ••4821"
    )
    dao.insertTransaction(txn)
  }

  suspend fun addNewBankAccount(bankName: String, accountNum: String, ifsc: String) {
    val last4 = accountNum.takeLast(4).ifBlank { "0000" }
    val newAccount = BankAccountEntity(
      id = "bank_" + System.currentTimeMillis(),
      bankName = bankName,
      accountNumberLast4 = last4,
      ifsc = ifsc.ifBlank { "BANK0001234" },
      accountType = "Savings Account",
      balance = Random.nextDouble(5000.0, 50000.0),
      isPrimary = false,
      upiPin = "1234",
      upiId = "rahul.sharma@${bankName.lowercase().take(3)}"
    )
    dao.insertBankAccount(newAccount)
  }

  fun updateSoundboxLanguage(lang: String) {
    _userProfile.update { it.copy(soundboxLanguage = lang) }
  }

  fun toggleSoundboxAlerts(enabled: Boolean) {
    _userProfile.update { it.copy(soundboxAlertsEnabled = enabled) }
  }
}
