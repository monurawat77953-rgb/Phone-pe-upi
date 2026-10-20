package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.BankAccountEntity
import com.example.data.local.ContactEntity
import com.example.data.local.PhonePeDatabase
import com.example.data.local.RewardEntity
import com.example.data.local.TransactionEntity
import com.example.data.model.PaymentRequest
import com.example.data.model.PaymentResult
import com.example.data.model.PaymentSourceType
import com.example.data.model.UserProfile
import com.example.data.repository.PhonePeRepository
import com.example.util.SoundboxVoiceUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class MainTab {
  HOME,
  STORES,
  INSURANCE,
  WEALTH,
  HISTORY
}

sealed interface ActiveModal {
  data object None : ActiveModal
  data class SendMoney(val prefilledRecipient: ContactEntity? = null, val prefilledPhoneOrUpi: String = "") : ActiveModal
  data object ToBankUpi : ActiveModal
  data object ToSelf : ActiveModal
  data object CheckBalance : ActiveModal
  data object QrScanner : ActiveModal
  data object MyQrCode : ActiveModal
  data class BillRecharge(val initialCategory: String = "MOBILE") : ActiveModal
  data object Wallet : ActiveModal
  data object Rewards : ActiveModal
  data object Soundbox : ActiveModal
}

data class UpiPinPrompt(
  val request: PaymentRequest,
  val bankName: String,
  val enteredPin: String = "",
  val isError: Boolean = false,
  val isProcessing: Boolean = false
)

class PhonePeViewModel(application: Application) : AndroidViewModel(application) {

  private val repository: PhonePeRepository
  private val soundboxUtil: SoundboxVoiceUtil = SoundboxVoiceUtil(application)

  val userProfile: StateFlow<UserProfile>
  val transactions: StateFlow<List<TransactionEntity>>
  val bankAccounts: StateFlow<List<BankAccountEntity>>
  val contacts: StateFlow<List<ContactEntity>>
  val rewards: StateFlow<List<RewardEntity>>

  private val _currentTab = MutableStateFlow(MainTab.HOME)
  val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

  private val _activeModal = MutableStateFlow<ActiveModal>(ActiveModal.None)
  val activeModal: StateFlow<ActiveModal> = _activeModal.asStateFlow()

  // UPI PIN Prompt State
  private val _upiPinPrompt = MutableStateFlow<UpiPinPrompt?>(null)
  val upiPinPrompt: StateFlow<UpiPinPrompt?> = _upiPinPrompt.asStateFlow()

  // Payment Success Dialog State
  private val _lastPaymentResult = MutableStateFlow<PaymentResult?>(null)
  val lastPaymentResult: StateFlow<PaymentResult?> = _lastPaymentResult.asStateFlow()

  // Selected Transaction for Detailed Receipt
  private val _selectedTransaction = MutableStateFlow<TransactionEntity?>(null)
  val selectedTransaction: StateFlow<TransactionEntity?> = _selectedTransaction.asStateFlow()

  // Active Scratch Card being viewed/scratched
  private val _activeScratchCard = MutableStateFlow<RewardEntity?>(null)
  val activeScratchCard: StateFlow<RewardEntity?> = _activeScratchCard.asStateFlow()

  // Balance checking dialog result state
  private val _balanceCheckedAccount = MutableStateFlow<BankAccountEntity?>(null)
  val balanceCheckedAccount: StateFlow<BankAccountEntity?> = _balanceCheckedAccount.asStateFlow()

  init {
    val database = PhonePeDatabase.getDatabase(application, viewModelScope)
    repository = PhonePeRepository(database.phonePeDao())

    userProfile = repository.userProfile
    transactions = repository.transactions.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )
    bankAccounts = repository.bankAccounts.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )
    contacts = repository.contacts.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )
    rewards = repository.rewards.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )
  }

  fun setTab(tab: MainTab) {
    _currentTab.value = tab
  }

  fun openModal(modal: ActiveModal) {
    _activeModal.value = modal
  }

  fun closeModal() {
    _activeModal.value = ActiveModal.None
  }

  fun selectTransaction(txn: TransactionEntity?) {
    _selectedTransaction.value = txn
  }

  fun openScratchCard(reward: RewardEntity) {
    _activeScratchCard.value = reward
  }

  fun closeScratchCard() {
    _activeScratchCard.value = null
  }

  fun scratchCurrentReward() {
    val reward = _activeScratchCard.value ?: return
    viewModelScope.launch {
      repository.scratchReward(reward.id, reward.cashbackAmount)
      _activeScratchCard.value = reward.copy(isScratched = true)
      if (reward.cashbackAmount > 0) {
        if (userProfile.value.soundboxAlertsEnabled) {
          soundboxUtil.speakPaymentReceived(reward.cashbackAmount, userProfile.value.soundboxLanguage)
        }
      }
    }
  }

  // Payment Flow initiation
  fun initiatePayment(request: PaymentRequest) {
    // If UPI Lite and amount <= 500, skip PIN
    if (request.source == PaymentSourceType.UPI_LITE && request.amount <= 500.0) {
      executePayment(request)
      return
    }

    val accounts = bankAccounts.value
    val bank = accounts.find { it.id == request.selectedBankAccountId }
      ?: accounts.firstOrNull { it.isPrimary }
      ?: accounts.firstOrNull()

    _upiPinPrompt.value = UpiPinPrompt(
      request = request,
      bankName = bank?.bankName ?: "Linked Bank Account",
      enteredPin = ""
    )
  }

  fun onPinDigitEntered(digit: Char) {
    _upiPinPrompt.update { current ->
      if (current == null || current.enteredPin.length >= 6 || current.isProcessing) return@update current
      val updatedPin = current.enteredPin + digit
      current.copy(enteredPin = updatedPin, isError = false)
    }

    // If 4 digits entered, auto-verify
    val current = _upiPinPrompt.value
    if (current != null && current.enteredPin.length == 4) {
      confirmPin()
    }
  }

  fun onPinBackspace() {
    _upiPinPrompt.update { current ->
      if (current == null || current.enteredPin.isEmpty() || current.isProcessing) return@update current
      current.copy(enteredPin = current.enteredPin.dropLast(1), isError = false)
    }
  }

  fun cancelPinPrompt() {
    _upiPinPrompt.value = null
  }

  fun confirmPin() {
    val prompt = _upiPinPrompt.value ?: return
    if (prompt.enteredPin.length < 4) {
      _upiPinPrompt.update { it?.copy(isError = true) }
      return
    }

    _upiPinPrompt.update { it?.copy(isProcessing = true) }

    viewModelScope.launch {
      delay(750) // Realistic secure UPI processing delay
      _upiPinPrompt.value = null
      executePayment(prompt.request)
    }
  }

  private fun executePayment(request: PaymentRequest) {
    viewModelScope.launch {
      val result = repository.processPayment(request)
      _lastPaymentResult.value = result
      closeModal()

      // Play Soundbox alert if enabled
      if (userProfile.value.soundboxAlertsEnabled) {
        soundboxUtil.speakPaymentReceived(result.amount, userProfile.value.soundboxLanguage)
      }
    }
  }

  fun dismissPaymentResult() {
    _lastPaymentResult.value = null
  }

  // Check Balance with PIN
  fun promptCheckBalance(account: BankAccountEntity) {
    _balanceCheckedAccount.value = account
  }

  fun dismissCheckBalance() {
    _balanceCheckedAccount.value = null
  }

  // Wallet and UPI Lite Actions
  fun topUpWallet(amount: Double) {
    viewModelScope.launch {
      repository.topUpWallet(amount)
    }
  }

  fun topUpUpiLite(amount: Double) {
    viewModelScope.launch {
      repository.topUpUpiLite(amount)
    }
  }

  fun buyDigitalGold(amount: Double, grams: Double) {
    viewModelScope.launch {
      repository.buyDigitalGold(amount, grams)
      closeModal()
      _lastPaymentResult.value = PaymentResult(
        success = true,
        transactionId = "GD" + System.currentTimeMillis().toString().takeLast(8),
        utrNumber = "428" + (100000000L..999999999L).random(),
        amount = amount,
        recipientName = "24K Digital Gold (SafeGold)",
        debitedFrom = "State Bank of India ••4821",
        timestamp = System.currentTimeMillis(),
        wonScratchCard = true
      )
    }
  }

  fun addNewBankAccount(bankName: String, accountNum: String, ifsc: String) {
    viewModelScope.launch {
      repository.addNewBankAccount(bankName, accountNum, ifsc)
    }
  }

  fun playSoundboxTest(amount: Double = 500.0) {
    soundboxUtil.speakPaymentReceived(amount, userProfile.value.soundboxLanguage)
  }

  fun setSoundboxLanguage(lang: String) {
    repository.updateSoundboxLanguage(lang)
  }

  fun toggleSoundboxAlerts(enabled: Boolean) {
    repository.toggleSoundboxAlerts(enabled)
  }

  override fun onCleared() {
    super.onCleared()
    soundboxUtil.shutdown()
  }
}
