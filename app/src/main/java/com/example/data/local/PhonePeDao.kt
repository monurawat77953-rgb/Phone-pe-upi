package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PhonePeDao {

  // Transactions
  @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
  fun getAllTransactions(): Flow<List<TransactionEntity>>

  @Query("SELECT * FROM transactions WHERE category = :category ORDER BY timestamp DESC")
  fun getTransactionsByCategory(category: String): Flow<List<TransactionEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTransaction(transaction: TransactionEntity)

  @Query("DELETE FROM transactions WHERE id = :id")
  suspend fun deleteTransaction(id: String)

  // Bank Accounts
  @Query("SELECT * FROM bank_accounts ORDER BY isPrimary DESC, bankName ASC")
  fun getAllBankAccounts(): Flow<List<BankAccountEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBankAccount(account: BankAccountEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBankAccounts(accounts: List<BankAccountEntity>)

  @Update
  suspend fun updateBankAccount(account: BankAccountEntity)

  @Query("UPDATE bank_accounts SET balance = :newBalance WHERE id = :id")
  suspend fun updateBalance(id: String, newBalance: Double)

  // Contacts
  @Query("SELECT * FROM contacts ORDER BY lastPaidTimestamp DESC, name ASC")
  fun getAllContacts(): Flow<List<ContactEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertContacts(contacts: List<ContactEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertContact(contact: ContactEntity)

  @Update
  suspend fun updateContact(contact: ContactEntity)

  // Rewards
  @Query("SELECT * FROM rewards ORDER BY isScratched ASC, id DESC")
  fun getAllRewards(): Flow<List<RewardEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertRewards(rewards: List<RewardEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertReward(reward: RewardEntity)

  @Query("UPDATE rewards SET isScratched = 1 WHERE id = :id")
  suspend fun markRewardScratched(id: String)
}
