package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.PhonePeGreen
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.PhonePePurpleContainer
import com.example.ui.theme.PhonePeTextPrimary
import com.example.ui.theme.PhonePeTextSecondary

@Composable
fun WalletScreen(
  userProfile: UserProfile,
  onBack: () -> Unit,
  onTopUpWallet: (Double) -> Unit,
  onTopUpUpiLite: (Double) -> Unit,
  modifier: Modifier = Modifier
) {
  var topUpAmountText by remember { mutableStateOf("1000") }
  var autoTopUpEnabled by remember { mutableStateOf(true) }

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
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = onBack,
            modifier = Modifier.testTag("wallet_back_button")
          ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
          Text(
            text = "PhonePe Wallet & UPI Lite",
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
        // Wallet Balance Card
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .size(44.dp)
                      .clip(CircleShape)
                      .background(PhonePePurpleContainer),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = PhonePePurple)
                  }
                  Spacer(modifier = Modifier.width(12.dp))
                  Column {
                    Text(
                      text = "PhonePe Wallet Balance",
                      fontSize = 13.sp,
                      color = PhonePeTextSecondary
                    )
                    Text(
                      text = "₹${"%.2f".format(userProfile.walletBalance)}",
                      fontSize = 26.sp,
                      fontWeight = FontWeight.ExtraBold,
                      color = PhonePeTextPrimary
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(16.dp))

              Text("Topup Amount", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PhonePeTextSecondary)
              Spacer(modifier = Modifier.height(6.dp))
              OutlinedTextField(
                value = topUpAmountText,
                onValueChange = { if (it.all { char -> char.isDigit() }) topUpAmountText = it },
                prefix = { Text("₹", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("topup_amount_input"),
                shape = RoundedCornerShape(12.dp)
              )

              Spacer(modifier = Modifier.height(10.dp))

              // Quick Chips
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                listOf(500, 1000, 2000, 5000).forEach { amt ->
                  Box(
                    modifier = Modifier
                      .weight(1f)
                      .clip(RoundedCornerShape(8.dp))
                      .background(Color(0xFFF0F1F5))
                      .clickable { topUpAmountText = amt.toString() }
                      .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                  ) {
                    Text("+₹$amt", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PhonePePurple)
                  }
                }
              }

              Spacer(modifier = Modifier.height(16.dp))

              Button(
                onClick = {
                  val amt = topUpAmountText.toDoubleOrNull() ?: 0.0
                  if (amt > 0) {
                    onTopUpWallet(amt)
                  }
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("topup_wallet_button"),
                colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
                shape = RoundedCornerShape(12.dp)
              ) {
                Text("Topup Wallet", fontWeight = FontWeight.Bold)
              }
            }
          }
        }

        // Auto Topup Setting
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
              Column(modifier = Modifier.weight(1f)) {
                Text("Wallet Auto Top-up", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PhonePeTextPrimary)
                Text(
                  "Automatically add ₹1,000 when wallet drops below ₹200",
                  fontSize = 11.sp,
                  color = PhonePeTextSecondary
                )
              }
              Switch(
                checked = autoTopUpEnabled,
                onCheckedChange = { autoTopUpEnabled = it },
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = PhonePePurple)
              )
            }
          }
        }

        // UPI Lite Section
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .size(40.dp)
                      .clip(CircleShape)
                      .background(Color(0xFFE8F5E9)),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(Icons.Default.FlashOn, contentDescription = null, tint = PhonePeGreen)
                  }
                  Spacer(modifier = Modifier.width(12.dp))
                  Column {
                    Text("UPI Lite Balance", fontSize = 13.sp, color = PhonePeTextSecondary)
                    Text("₹${"%.2f".format(userProfile.upiLiteBalance)}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = PhonePeTextPrimary)
                  }
                }
                Button(
                  onClick = { onTopUpUpiLite(500.0) },
                  colors = ButtonDefaults.buttonColors(containerColor = PhonePeGreen),
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text("+ Add ₹500", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = PhonePeGreen, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Zero PIN needed for small merchant payments up to ₹500", fontSize = 11.sp, color = PhonePeTextSecondary)
              }
            }
          }
        }
      }
    }
  }
}
