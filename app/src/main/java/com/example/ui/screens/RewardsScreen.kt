package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.RewardEntity
import com.example.ui.theme.PhonePeAmber
import com.example.ui.theme.PhonePeGreen
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.PhonePePurpleContainer
import com.example.ui.theme.PhonePePurpleDark
import com.example.ui.theme.PhonePeTextPrimary
import com.example.ui.theme.PhonePeTextSecondary

@Composable
fun RewardsScreen(
  rewards: List<RewardEntity>,
  onBack: () -> Unit,
  onOpenScratchCard: (RewardEntity) -> Unit,
  modifier: Modifier = Modifier
) {
  val totalCashbackWon = remember(rewards) {
    rewards.filter { it.isScratched }.sumOf { it.cashbackAmount } + 250.0 // seeded base
  }

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
            modifier = Modifier.testTag("rewards_back_button")
          ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
          Text(
            text = "Rewards & Cashbacks",
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Hero Total Cashback Banner
        item(span = { GridItemSpan(2) }) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(20.dp))
              .background(
                Brush.horizontalGradient(
                  listOf(PhonePePurple, Color(0xFF8E24AA))
                )
              )
              .padding(20.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "TOTAL CASHBACK WON",
                  color = Color.White.copy(alpha = 0.8f),
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "₹${"%.2f".format(totalCashbackWon)}",
                  color = PhonePeAmber,
                  fontSize = 32.sp,
                  fontWeight = FontWeight.Black
                )
                Text(
                  text = "Credited directly to PhonePe Wallet",
                  color = Color.White.copy(alpha = 0.9f),
                  fontSize = 11.sp
                )
              }
              Box(
                modifier = Modifier
                  .size(56.dp)
                  .clip(CircleShape)
                  .background(PhonePeAmber),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  Icons.Default.CardGiftcard,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(32.dp)
                )
              }
            }
          }
        }

        item(span = { GridItemSpan(2) }) {
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "YOUR SCRATCH CARDS",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = PhonePeTextSecondary,
            letterSpacing = 1.sp
          )
        }

        // List of Cards
        items(rewards, key = { it.id }) { reward ->
          val isScratched = reward.isScratched

          Card(
            modifier = Modifier
              .fillMaxWidth()
              .aspectRatio(0.9f)
              .clickable { onOpenScratchCard(reward) }
              .testTag("reward_card_${reward.id}"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
          ) {
            if (!isScratched) {
              // Unscratched Shiny Purple/Gold Card
              Box(
                modifier = Modifier
                  .fillMaxSize()
                  .background(
                    Brush.linearGradient(
                      listOf(Color(0xFF8A38E0), PhonePePurple, Color(0xFF3F116E))
                    )
                  )
                  .padding(14.dp),
                contentAlignment = Alignment.Center
              ) {
                Column(
                  horizontalAlignment = Alignment.CenterHorizontally,
                  verticalArrangement = Arrangement.Center
                ) {
                  Box(
                    modifier = Modifier
                      .size(46.dp)
                      .clip(CircleShape)
                      .background(PhonePeAmber),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(Icons.Default.Redeem, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
                  }
                  Spacer(modifier = Modifier.height(10.dp))
                  Text(
                    text = "TAP TO SCRATCH",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                  )
                  Text(
                    text = "Win up to ₹100",
                    color = PhonePeAmber,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 2.dp)
                  )
                }
              }
            } else {
              // Scratched revealed card
              Column(
                modifier = Modifier
                  .fillMaxSize()
                  .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
              ) {
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (reward.cashbackAmount > 0) Color(0xFFE8F5E9) else PhonePePurpleContainer),
                  contentAlignment = Alignment.Center
                ) {
                  if (reward.cashbackAmount > 0) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = PhonePeGreen, modifier = Modifier.size(20.dp))
                  } else {
                    Text(reward.brandName.take(2).uppercase(), color = PhonePePurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                  }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  if (reward.cashbackAmount > 0) {
                    Text("₹${reward.cashbackAmount.toInt()}", fontSize = 22.sp, fontWeight = FontWeight.Black, color = PhonePePurple)
                    Text("Cashback", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PhonePeGreen)
                  } else {
                    Text(reward.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PhonePeTextPrimary, textAlign = TextAlign.Center)
                    Text(reward.subtitle, fontSize = 10.sp, color = PhonePeTextSecondary, textAlign = TextAlign.Center)
                  }
                }

                Text(
                  text = "Claimed",
                  fontSize = 10.sp,
                  color = PhonePeTextSecondary,
                  fontWeight = FontWeight.Medium
                )
              }
            }
          }
        }
      }
    }
  }
}
