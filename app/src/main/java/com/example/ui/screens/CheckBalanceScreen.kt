package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.BankAccountEntity
import com.example.data.model.UserProfile
import com.example.ui.theme.PhonePeGreen
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.PhonePePurpleContainer
import com.example.ui.theme.PhonePeTextPrimary
import com.example.ui.theme.PhonePeTextSecondary

@Composable
fun CheckBalanceScreen(
  bankAccounts: List<BankAccountEntity>,
  userProfile: UserProfile,
  onBack: () -> Unit,
  onCheckBankPin: (BankAccountEntity) -> Unit,
  onAddNewBank: (String, String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  // Revealed balance map for user session
  val revealedBalances = remember { mutableStateMapOf<String, Boolean>() }
  var showAddBankDialog by remember { mutableStateOf(false) }

  Surface(
    modifier = modifier.fillMaxSize(),
    color = Color(0xFFF7F8FA)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding()
    ) {
      // Top Bar
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(PhonePePurple)
          .padding(horizontal = 10.dp, vertical = 8.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = onBack,
            modifier = Modifier.testTag("check_balance_back_button")
          ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
          Text(
            text = "Check Bank Balance",
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // Section: Prepaid & Wallets
        item {
          Text(
            text = "PREPAID BALANCES",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = PhonePeTextSecondary,
            letterSpacing = 1.sp
          )
        }

        // PhonePe Wallet Card
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(PhonePePurpleContainer),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = PhonePePurple)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Text(
                    text = "PhonePe Wallet",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = PhonePeTextPrimary
                  )
                  Text(
                    text = "Low Balance Auto Top-up Available",
                    fontSize = 11.sp,
                    color = PhonePeTextSecondary
                  )
                }
              }

              Text(
                text = "₹${"%.2f".format(userProfile.walletBalance)}",
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PhonePeTextPrimary
              )
            }
          }
        }

        // UPI Lite Card
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F5E9)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.FlashOn, contentDescription = null, tint = PhonePeGreen)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Text(
                    text = "UPI Lite",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = PhonePeTextPrimary
                  )
                  Text(
                    text = "Zero PIN instant payments",
                    fontSize = 11.sp,
                    color = PhonePeTextSecondary
                  )
                }
              }

              Text(
                text = "₹${"%.2f".format(userProfile.upiLiteBalance)}",
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PhonePeTextPrimary
              )
            }
          }
        }

        // Section: Linked Bank Accounts
        item {
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "LINKED BANK ACCOUNTS",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = PhonePeTextSecondary,
            letterSpacing = 1.sp
          )
        }

        items(bankAccounts, key = { it.id }) { bank ->
          val isRevealed = revealedBalances[bank.id] == true

          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEDE7F6)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.AccountBalance, contentDescription = null, tint = PhonePePurple)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = bank.bankName,
                      fontSize = 15.sp,
                      fontWeight = FontWeight.Bold,
                      color = PhonePeTextPrimary
                    )
                    if (bank.isPrimary) {
                      Spacer(modifier = Modifier.width(6.dp))
                      Box(
                        modifier = Modifier
                          .clip(RoundedCornerShape(4.dp))
                          .background(Color(0xFFE8F5E9))
                          .padding(horizontal = 6.dp, vertical = 2.dp)
                      ) {
                        Text("PRIMARY", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = PhonePeGreen)
                      }
                    }
                  }
                  Text(
                    text = "A/c No: •••• ${bank.accountNumberLast4}",
                    fontSize = 12.sp,
                    color = PhonePeTextSecondary
                  )
                }
              }

              if (isRevealed) {
                Column(horizontalAlignment = Alignment.End) {
                  Text(
                    text = "₹${"%.2f".format(bank.balance)}",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PhonePeGreen
                  )
                  Text("Available", fontSize = 10.sp, color = PhonePeTextSecondary)
                }
              } else {
                Button(
                  onClick = {
                    revealedBalances[bank.id] = true
                    onCheckBankPin(bank)
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier.testTag("check_balance_${bank.id}")
                ) {
                  Text("Check Balance", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }

        // Add New Bank Account Card
        item {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { showAddBankDialog = true }
              .testTag("add_new_bank_card"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(42.dp)
                  .clip(CircleShape)
                  .background(PhonePePurpleContainer),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = PhonePePurple)
              }
              Spacer(modifier = Modifier.width(14.dp))
              Column {
                Text(
                  text = "Add Another Bank Account",
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = PhonePePurple
                )
                Text(
                  text = "Link accounts from Axis, Kotak, PNB & more",
                  fontSize = 11.sp,
                  color = PhonePeTextSecondary
                )
              }
            }
          }
        }
      }
    }
  }

  // Add Bank Account Dialog
  if (showAddBankDialog) {
    androidx.compose.ui.window.Dialog(onDismissRequest = { showAddBankDialog = false }) {
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ) {
          Text("Select Bank to Link", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = PhonePeTextPrimary)
          Spacer(modifier = Modifier.height(14.dp))

          val popularBanks = listOf(
            "Axis Bank" to "UTIB0000123",
            "Kotak Mahindra Bank" to "KKBK0000456",
            "Punjab National Bank" to "PUNB0000789",
            "Bank of Baroda" to "BARB0000101"
          )

          popularBanks.forEach { (name, ifsc) ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                  onAddNewBank(name, (1000..9999).random().toString(), ifsc)
                  showAddBankDialog = false
                }
                .padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.AccountBalance, contentDescription = null, tint = PhonePePurple)
              Spacer(modifier = Modifier.width(12.dp))
              Text(name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = PhonePeTextPrimary)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Button(
            onClick = { showAddBankDialog = false },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF0F1F5))
          ) {
            Text("Cancel", color = PhonePeTextPrimary)
          }
        }
      }
    }
  }
}
