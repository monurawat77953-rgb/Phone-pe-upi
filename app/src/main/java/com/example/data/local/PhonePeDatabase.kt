package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
  entities = [
    TransactionEntity::class,
    BankAccountEntity::class,
    ContactEntity::class,
    RewardEntity::class
  ],
  version = 1,
  exportSchema = false
)
abstract class PhonePeDatabase : RoomDatabase() {

  abstract fun phonePeDao(): PhonePeDao

  companion object {
    @Volatile
    private var INSTANCE: PhonePeDatabase? = null

    fun getDatabase(context: Context, scope: CoroutineScope): PhonePeDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          PhonePeDatabase::class.java,
          "phonepe_database"
        )
          .addCallback(PhonePeDatabaseCallback(scope))
          .fallbackToDestructiveMigration()
          .build()
        INSTANCE = instance
        instance
      }
    }
  }

  private class PhonePeDatabaseCallback(
    private val scope: CoroutineScope
  ) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
      super.onCreate(db)
      INSTANCE?.let { database ->
        scope.launch(Dispatchers.IO) {
          populateInitialData(database.phonePeDao())
        }
      }
    }

    suspend fun populateInitialData(dao: PhonePeDao) {
      // 1. Initial Bank Accounts
      val bankAccounts = listOf(
        BankAccountEntity(
          id = "bank_sbi",
          bankName = "State Bank of India",
          accountNumberLast4 = "4821",
          ifsc = "SBIN0001234",
          accountType = "Savings Account",
          balance = 34580.50,
          isPrimary = true,
          upiPin = "1234",
          upiId = "rahul.sharma@ybl"
        ),
        BankAccountEntity(
          id = "bank_hdfc",
          bankName = "HDFC Bank",
          accountNumberLast4 = "9012",
          ifsc = "HDFC0000456",
          accountType = "Savings Account",
          balance = 12450.00,
          isPrimary = false,
          upiPin = "5678",
          upiId = "rahul.sharma@ibl"
        ),
        BankAccountEntity(
          id = "bank_icici",
          bankName = "ICICI Bank",
          accountNumberLast4 = "3341",
          ifsc = "ICIC0000789",
          accountType = "Salary Account",
          balance = 82190.25,
          isPrimary = false,
          upiPin = "4321",
          upiId = "rahul.sharma@axl"
        )
      )
      dao.insertBankAccounts(bankAccounts)

      // 2. Initial Contacts
      val contacts = listOf(
        ContactEntity(
          id = "c1",
          name = "Priya Patel",
          phone = "+91 98201 44521",
          upiId = "priya.patel@okaxis",
          avatarColorHex = "#9C27B0",
          isFavorite = true,
          lastPaidTimestamp = System.currentTimeMillis() - 3600000 * 5
        ),
        ContactEntity(
          id = "c2",
          name = "Amit Kumar Verma",
          phone = "+91 99341 87219",
          upiId = "amit.verma@ybl",
          avatarColorHex = "#2196F3",
          isFavorite = true,
          lastPaidTimestamp = System.currentTimeMillis() - 3600000 * 24
        ),
        ContactEntity(
          id = "c3",
          name = "Ananya Iyer",
          phone = "+91 97412 39011",
          upiId = "ananya.iyer@okhdfcbank",
          avatarColorHex = "#E91E63",
          isFavorite = true,
          lastPaidTimestamp = System.currentTimeMillis() - 3600000 * 48
        ),
        ContactEntity(
          id = "c4",
          name = "Vikram Singh",
          phone = "+91 94140 22381",
          upiId = "vikram.singh@icici",
          avatarColorHex = "#FF9800",
          isFavorite = false,
          lastPaidTimestamp = System.currentTimeMillis() - 3600000 * 72
        ),
        ContactEntity(
          id = "c5",
          name = "Neha Deshmukh",
          phone = "+91 98220 18472",
          upiId = "neha.d@paytm",
          avatarColorHex = "#009688",
          isFavorite = false,
          lastPaidTimestamp = System.currentTimeMillis() - 3600000 * 120
        ),
        ContactEntity(
          id = "c6",
          name = "Sharma Grocery Mart",
          phone = "+91 98450 67123",
          upiId = "sharmagrocery@ybl",
          avatarColorHex = "#4CAF50",
          isFavorite = false,
          lastPaidTimestamp = System.currentTimeMillis() - 3600000 * 2
        )
      )
      dao.insertContacts(contacts)

      // 3. Initial Transactions
      val now = System.currentTimeMillis()
      val sampleTxns = listOf(
        TransactionEntity(
          id = "TXN" + (1000000000..9999999999).random(),
          title = "Sharma Grocery Mart",
          recipientUpiId = "sharmagrocery@ybl",
          recipientPhone = "+91 98450 67123",
          amount = 450.0,
          type = "DEBIT",
          status = "SUCCESS",
          category = "MERCHANT",
          timestamp = now - (1000 * 60 * 45), // 45 mins ago
          utrNumber = "428937102948",
          note = "Weekly groceries & milk",
          debitedFrom = "State Bank of India ••4821"
        ),
        TransactionEntity(
          id = "TXN" + (1000000000..9999999999).random(),
          title = "Priya Patel",
          recipientUpiId = "priya.patel@okaxis",
          recipientPhone = "+91 98201 44521",
          amount = 1200.0,
          type = "DEBIT",
          status = "SUCCESS",
          category = "TRANSFER",
          timestamp = now - (1000 * 60 * 60 * 5),
          utrNumber = "428919283741",
          note = "Dinner split at Mainland China",
          debitedFrom = "State Bank of India ••4821"
        ),
        TransactionEntity(
          id = "TXN" + (1000000000..9999999999).random(),
          title = "Received from Amit Kumar",
          recipientUpiId = "amit.verma@ybl",
          recipientPhone = "+91 99341 87219",
          amount = 2500.0,
          type = "CREDIT",
          status = "SUCCESS",
          category = "TRANSFER",
          timestamp = now - (1000 * 60 * 60 * 24),
          utrNumber = "428847291039",
          note = "Cab fare and concert passes",
          debitedFrom = "Credited to SBI ••4821"
        ),
        TransactionEntity(
          id = "TXN" + (1000000000..9999999999).random(),
          title = "Jio Prepaid Recharge",
          recipientUpiId = "jio.prepaid@billdesk",
          recipientPhone = "+91 98765 43210",
          amount = 299.0,
          type = "DEBIT",
          status = "SUCCESS",
          category = "RECHARGE",
          timestamp = now - (1000 * 60 * 60 * 48),
          utrNumber = "428738192039",
          note = "28 Days 1.5GB/day Unlimited 5G",
          debitedFrom = "State Bank of India ••4821"
        ),
        TransactionEntity(
          id = "TXN" + (1000000000..9999999999).random(),
          title = "BESCOM Electricity Bill",
          recipientUpiId = "bescom.blr@bbps",
          recipientPhone = "CA-8891024",
          amount = 1840.0,
          type = "DEBIT",
          status = "SUCCESS",
          category = "BILL",
          timestamp = now - (1000 * 60 * 60 * 72),
          utrNumber = "428619283011",
          note = "Bill ID: BES-AUG-2026",
          debitedFrom = "HDFC Bank ••9012"
        ),
        TransactionEntity(
          id = "TXN" + (1000000000..9999999999).random(),
          title = "PhonePe Cashback",
          recipientUpiId = "cashback@phonepe",
          recipientPhone = "",
          amount = 50.0,
          type = "CREDIT",
          status = "SUCCESS",
          category = "CASHBACK",
          timestamp = now - (1000 * 60 * 60 * 96),
          utrNumber = "428519284019",
          note = "Won on Bill Payment",
          debitedFrom = "Credited to PhonePe Wallet"
        )
      )
      sampleTxns.forEach { dao.insertTransaction(it) }

      // 4. Initial Rewards
      val rewards = listOf(
        RewardEntity(
          id = "rew_1",
          title = "Flat ₹25 Cashback",
          subtitle = "On your next Mobile Recharge",
          cashbackAmount = 25.0,
          couponCode = "RECHARGE25",
          isScratched = false,
          brandName = "PhonePe Recharge",
          expiryText = "Valid for 6 days"
        ),
        RewardEntity(
          id = "rew_2",
          title = "Win Up to ₹100 Cashback",
          subtitle = "Pay at any merchant using UPI QR",
          cashbackAmount = 45.0,
          couponCode = "MERCHANT45",
          isScratched = false,
          brandName = "PhonePe Merchant",
          expiryText = "Valid for 4 days"
        ),
        RewardEntity(
          id = "rew_3",
          title = "Swiggy ₹75 OFF",
          subtitle = "On orders above ₹299",
          cashbackAmount = 0.0,
          couponCode = "SWIGGY75PP",
          isScratched = true,
          brandName = "Swiggy",
          expiryText = "Expiring tomorrow"
        ),
        RewardEntity(
          id = "rew_4",
          title = "Myntra ₹250 OFF",
          subtitle = "On fashion styles above ₹999",
          cashbackAmount = 0.0,
          couponCode = "MYNTRAPP250",
          isScratched = true,
          brandName = "Myntra",
          expiryText = "Valid for 15 days"
        )
      )
      dao.insertRewards(rewards)
    }
  }
}
