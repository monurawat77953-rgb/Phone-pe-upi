package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.PhonePeAmber
import com.example.ui.theme.PhonePeGreen
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.PhonePePurpleContainer
import com.example.ui.theme.PhonePePurpleDark
import com.example.ui.theme.PhonePeTextPrimary
import com.example.ui.theme.PhonePeTextSecondary

data class MutualFundItem(
  val id: String,
  val name: String,
  val category: String,
  val return3Y: String,
  val minSip: String
)

@Composable
fun WealthScreen(
  userProfile: UserProfile,
  onBuyGold: (Double, Double) -> Unit,
  modifier: Modifier = Modifier
) {
  var goldBuyAmountText by remember { mutableStateOf("1000") }
  val goldPrice = userProfile.currentGoldPricePerGram
  val parsedAmount = goldBuyAmountText.toDoubleOrNull() ?: 0.0
  val calculatedGrams = if (goldPrice > 0) parsedAmount / goldPrice else 0.0

  val totalLockerValue = userProfile.goldGrams * goldPrice

  val mutualFunds = listOf(
    MutualFundItem("mf1", "Nippon India Growth Fund", "Mid Cap • Very High Risk", "34.2% p.a.", "₹100"),
    MutualFundItem("mf2", "SBI Small Cap Direct Fund", "Small Cap • Very High Risk", "29.4% p.a.", "₹500"),
    MutualFundItem("mf3", "Parag Parikh Flexi Cap Fund", "Flexi Cap • High Risk", "24.8% p.a.", "₹1,000"),
    MutualFundItem("mf4", "Mirae Asset Large & Midcap", "Large & Mid Cap • High Risk", "22.6% p.a.", "₹500")
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF7F8FA))
  ) {
    // Header Banner
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.verticalGradient(listOf(Color(0xFF2E1052), PhonePePurple))
        )
        .padding(18.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = PhonePeAmber, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "PhonePe Wealth",
              color = Color.White,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold
            )
          }
          Text(
            text = "24K Digital Gold & High Return Mutual Funds",
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 11.sp,
            modifier = Modifier.padding(top = 4.dp)
          )
        }

        // Live Gold Price Pill
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF3B1768))
            .border(1.dp, PhonePeAmber.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Column(horizontalAlignment = Alignment.End) {
            Text("LIVE 24K GOLD", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = PhonePeAmber)
            Text("₹${goldPrice.toInt()}/gm", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.White)
          }
        }
      }
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // 1. Digital Gold Vault Card
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
              Column {
                Text("YOUR 24K GOLD LOCKER", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PhonePeTextSecondary, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("₹${"%.2f".format(totalLockerValue)}", fontSize = 24.sp, fontWeight = FontWeight.Black, color = PhonePeTextPrimary)
                Text("${"%.4f".format(userProfile.goldGrams)} grams • 99.9% Pure Gold", fontSize = 12.sp, color = PhonePeGreen, fontWeight = FontWeight.Bold)
              }

              Box(
                modifier = Modifier
                  .size(54.dp)
                  .clip(CircleShape)
                  .background(Color(0xFFFFF8E1)),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = PhonePeAmber, modifier = Modifier.size(34.dp))
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Buy Gold
            Text("Buy 24K Gold Instantly", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PhonePeTextPrimary)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
              value = goldBuyAmountText,
              onValueChange = { if (it.all { char -> char.isDigit() }) goldBuyAmountText = it },
              prefix = { Text("₹", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
              suffix = {
                Text(
                  text = "≈ ${"%.4f".format(calculatedGrams)} gm",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = PhonePePurple
                )
              },
              singleLine = true,
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("buy_gold_amount_input"),
              shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

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
                    .clickable { goldBuyAmountText = amt.toString() }
                    .padding(vertical = 8.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text("+₹$amt", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PhonePeTextPrimary)
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
              onClick = {
                if (parsedAmount > 0) {
                  onBuyGold(parsedAmount, calculatedGrams)
                }
              },
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("buy_gold_button"),
              colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
              shape = RoundedCornerShape(12.dp)
            ) {
              Text("Buy 24K Gold for ₹${parsedAmount.toInt()}", fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      // 2. Mutual Funds & SIPs Section
      item {
        Text(
          text = "TOP PERFORMING MUTUAL FUNDS",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = PhonePeTextSecondary,
          letterSpacing = 1.sp
        )
      }

      items(mutualFunds, key = { it.id }) { fund ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.Top
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(fund.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = PhonePeTextPrimary)
                Text(fund.category, fontSize = 11.sp, color = PhonePeTextSecondary, modifier = Modifier.padding(top = 2.dp))
              }

              Column(horizontalAlignment = Alignment.End) {
                Text(fund.return3Y, fontSize = 16.sp, fontWeight = FontWeight.Black, color = PhonePeGreen)
                Text("3Y Return", fontSize = 10.sp, color = PhonePeTextSecondary)
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Min SIP: ${fund.minSip}/month",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = PhonePeTextPrimary
              )

              Button(
                onClick = {
                  val sipAmount = fund.minSip.replace("₹", "").replace(",", "").toDoubleOrNull() ?: 500.0
                  onBuyGold(sipAmount, 0.0) // Simulates investment payment
                },
                colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("invest_${fund.id}")
              ) {
                Text("Start SIP", fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(80.dp))
      }
    }
  }
}
