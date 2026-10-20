package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.ContactEntity
import com.example.data.model.PaymentRequest
import com.example.ui.components.PaymentSuccessScreen
import com.example.ui.components.PhonePeBottomNav
import com.example.ui.components.PhonePeTopBar
import com.example.ui.components.ReceiptDetailDialog
import com.example.ui.components.ScratchCardDialog
import com.example.ui.components.SoundboxDialog
import com.example.ui.components.UpiPinKeypadScreen
import com.example.ui.screens.BillRechargeScreen
import com.example.ui.screens.CheckBalanceScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.InsuranceScreen
import com.example.ui.screens.MyQrScreen
import com.example.ui.screens.QrScannerScreen
import com.example.ui.screens.RewardsScreen
import com.example.ui.screens.SendMoneyScreen
import com.example.ui.screens.StoresScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.screens.WealthScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ActiveModal
import com.example.ui.viewmodel.MainTab
import com.example.ui.viewmodel.PhonePeViewModel

class MainActivity : ComponentActivity() {

  private val viewModel: PhonePeViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        PhonePeAppRoot(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun PhonePeAppRoot(viewModel: PhonePeViewModel) {
  val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
  val activeModal by viewModel.activeModal.collectAsStateWithLifecycle()
  val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
  val transactions by viewModel.transactions.collectAsStateWithLifecycle()
  val bankAccounts by viewModel.bankAccounts.collectAsStateWithLifecycle()
  val contacts by viewModel.contacts.collectAsStateWithLifecycle()
  val rewards by viewModel.rewards.collectAsStateWithLifecycle()

  val upiPinPrompt by viewModel.upiPinPrompt.collectAsStateWithLifecycle()
  val lastPaymentResult by viewModel.lastPaymentResult.collectAsStateWithLifecycle()
  val selectedTransaction by viewModel.selectedTransaction.collectAsStateWithLifecycle()
  val activeScratchCard by viewModel.activeScratchCard.collectAsStateWithLifecycle()

  val primaryBank = bankAccounts.firstOrNull { it.isPrimary } ?: bankAccounts.firstOrNull()

  // Handle hardware Back button: pop modals or return to Home tab
  BackHandler(enabled = activeModal !is ActiveModal.None || currentTab != MainTab.HOME || upiPinPrompt != null) {
    when {
      upiPinPrompt != null -> viewModel.cancelPinPrompt()
      activeModal !is ActiveModal.None -> viewModel.closeModal()
      currentTab != MainTab.HOME -> viewModel.setTab(MainTab.HOME)
    }
  }

  // UPI PIN Prompt Full-Screen Overlay (Authentic NPCI Security Screen)
  if (upiPinPrompt != null) {
    val prompt = upiPinPrompt!!
    UpiPinKeypadScreen(
      prompt = prompt,
      onDigitClick = { digit -> viewModel.onPinDigitEntered(digit) },
      onBackspaceClick = { viewModel.onPinBackspace() },
      onSubmitClick = { viewModel.confirmPin() },
      onCancelClick = { viewModel.cancelPinPrompt() }
    )
    return
  }

  // Payment Success Screen Celebration Overlay
  if (lastPaymentResult != null) {
    val result = lastPaymentResult!!
    PaymentSuccessScreen(
      result = result,
      onDone = { viewModel.dismissPaymentResult() },
      onOpenScratchCard = {
        viewModel.dismissPaymentResult()
        viewModel.openModal(ActiveModal.Rewards)
      },
      onPlaySoundbox = { amt -> viewModel.playSoundboxTest(amt) }
    )
    return
  }

  // Check if a modal/sub-screen is active
  when (val modal = activeModal) {
    is ActiveModal.SendMoney -> {
      SendMoneyScreen(
        contacts = contacts,
        bankAccounts = bankAccounts,
        userProfile = userProfile,
        initialRecipient = modal.prefilledRecipient,
        initialUpiOrPhone = modal.prefilledPhoneOrUpi,
        onBack = { viewModel.closeModal() },
        onProceedPayment = { request -> viewModel.initiatePayment(request) }
      )
    }
    is ActiveModal.QrScanner -> {
      QrScannerScreen(
        onBack = { viewModel.closeModal() },
        onQrScanned = { merchantContact, suggestedAmount ->
          viewModel.openModal(ActiveModal.SendMoney(prefilledRecipient = merchantContact))
        },
        onOpenMyQr = { viewModel.openModal(ActiveModal.MyQrCode) }
      )
    }
    is ActiveModal.MyQrCode -> {
      MyQrScreen(
        userProfile = userProfile,
        primaryBank = primaryBank,
        onBack = { viewModel.closeModal() }
      )
    }
    is ActiveModal.CheckBalance -> {
      CheckBalanceScreen(
        bankAccounts = bankAccounts,
        userProfile = userProfile,
        onBack = { viewModel.closeModal() },
        onCheckBankPin = { bank ->
          viewModel.promptCheckBalance(bank)
        },
        onAddNewBank = { name, acc, ifsc ->
          viewModel.addNewBankAccount(name, acc, ifsc)
        }
      )
    }
    is ActiveModal.BillRecharge -> {
      BillRechargeScreen(
        initialCategory = modal.initialCategory,
        onBack = { viewModel.closeModal() },
        onPayBill = { request -> viewModel.initiatePayment(request) }
      )
    }
    is ActiveModal.Wallet -> {
      WalletScreen(
        userProfile = userProfile,
        onBack = { viewModel.closeModal() },
        onTopUpWallet = { amt -> viewModel.topUpWallet(amt) },
        onTopUpUpiLite = { amt -> viewModel.topUpUpiLite(amt) }
      )
    }
    is ActiveModal.Rewards -> {
      RewardsScreen(
        rewards = rewards,
        onBack = { viewModel.closeModal() },
        onOpenScratchCard = { reward -> viewModel.openScratchCard(reward) }
      )
    }
    is ActiveModal.Soundbox -> {
      SoundboxDialog(
        userProfile = userProfile,
        onDismiss = { viewModel.closeModal() },
        onTestSound = { amt -> viewModel.playSoundboxTest(amt) },
        onLanguageChange = { lang -> viewModel.setSoundboxLanguage(lang) },
        onToggleSoundbox = { enabled -> viewModel.toggleSoundboxAlerts(enabled) }
      )
    }
    else -> {
      // Main App Scaffold with TopBar and BottomNavigationBar
      Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
          PhonePeTopBar(
            userProfile = userProfile,
            onProfileClick = { viewModel.openModal(ActiveModal.MyQrCode) },
            onQrScanClick = { viewModel.openModal(ActiveModal.QrScanner) },
            onSoundboxClick = { viewModel.openModal(ActiveModal.Soundbox) },
            onNotificationsClick = { viewModel.openModal(ActiveModal.Rewards) },
            onHelpClick = { viewModel.openModal(ActiveModal.CheckBalance) }
          )
        },
        bottomBar = {
          PhonePeBottomNav(
            currentTab = currentTab,
            onTabSelected = { tab -> viewModel.setTab(tab) }
          )
        }
      ) { innerPadding ->
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(Color(0xFFF7F8FA))
        ) {
          when (currentTab) {
            MainTab.HOME -> {
              HomeScreen(
                userProfile = userProfile,
                recentTransactions = transactions,
                primaryBank = primaryBank,
                onOpenModal = { targetModal -> viewModel.openModal(targetModal) },
                onNavigateTab = { targetTab -> viewModel.setTab(targetTab) },
                onSelectTransaction = { txn -> viewModel.selectTransaction(txn) }
              )
            }
            MainTab.STORES -> {
              StoresScreen(
                onPayMerchant = { merchantContact ->
                  viewModel.openModal(ActiveModal.SendMoney(prefilledRecipient = merchantContact))
                }
              )
            }
            MainTab.INSURANCE -> {
              InsuranceScreen(
                onBuyInsurance = { req -> viewModel.initiatePayment(req) }
              )
            }
            MainTab.WEALTH -> {
              WealthScreen(
                userProfile = userProfile,
                onBuyGold = { amount, grams -> viewModel.buyDigitalGold(amount, grams) }
              )
            }
            MainTab.HISTORY -> {
              HistoryScreen(
                transactions = transactions,
                onSelectTransaction = { txn -> viewModel.selectTransaction(txn) }
              )
            }
          }
        }
      }
    }
  }

  // Detailed Transaction Receipt Dialog
  if (selectedTransaction != null) {
    ReceiptDetailDialog(
      transaction = selectedTransaction!!,
      onDismiss = { viewModel.selectTransaction(null) },
      onRepeatPayment = { txn ->
        viewModel.openModal(
          ActiveModal.SendMoney(
            prefilledPhoneOrUpi = txn.recipientUpiId.ifBlank { txn.recipientPhone }
          )
        )
      }
    )
  }

  // Active Scratch Card Dialog
  if (activeScratchCard != null) {
    ScratchCardDialog(
      reward = activeScratchCard!!,
      onDismiss = { viewModel.closeScratchCard() },
      onScratched = { viewModel.scratchCurrentReward() }
    )
  }
}
