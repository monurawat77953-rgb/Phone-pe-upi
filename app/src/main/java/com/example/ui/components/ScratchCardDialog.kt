package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.RewardEntity
import com.example.ui.theme.PhonePeAmber
import com.example.ui.theme.PhonePeGreen
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.PhonePePurpleDark
import com.example.ui.theme.PhonePeTextPrimary
import com.example.ui.theme.PhonePeTextSecondary
import kotlinx.coroutines.launch

@Composable
fun ScratchCardDialog(
  reward: RewardEntity,
  onDismiss: () -> Unit,
  onScratched: () -> Unit,
  modifier: Modifier = Modifier
) {
  var scratchedProgress by remember { mutableFloatStateOf(if (reward.isScratched) 1f else 0f) }
  val isRevealed = scratchedProgress >= 0.35f || reward.isScratched
  val coroutineScope = rememberCoroutineScope()

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = modifier
        .fillMaxWidth(0.88f)
        .clip(RoundedCornerShape(24.dp)),
      color = Color(0xFF1E172B)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Redeem, contentDescription = null, tint = PhonePeAmber, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "PhonePe Rewards",
              color = Color.White,
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            )
          }
          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("scratch_close_button")
          ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Scratch Card Area
        Box(
          modifier = Modifier
            .size(width = 280.dp, height = 280.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(2.dp, PhonePeAmber.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
        ) {
          // Inner Revealed Content (Underneath)
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(Color.White)
              .padding(16.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center
            ) {
              if (reward.cashbackAmount > 0) {
                Box(
                  modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(PhonePeAmber),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                  text = "₹${reward.cashbackAmount.toInt()}",
                  color = PhonePePurple,
                  fontSize = 38.sp,
                  fontWeight = FontWeight.Black
                )
                Text(
                  text = "Cashback Won!",
                  color = PhonePeGreen,
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "Credited directly into your PhonePe Wallet",
                  color = PhonePeTextSecondary,
                  fontSize = 11.sp,
                  textAlign = TextAlign.Center,
                  modifier = Modifier.padding(top = 4.dp)
                )
              } else {
                Box(
                  modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(PhonePePurple),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = reward.brandName.take(2).uppercase(),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                  text = reward.title,
                  color = PhonePeTextPrimary,
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Bold,
                  textAlign = TextAlign.Center
                )
                Text(
                  text = reward.subtitle,
                  color = PhonePeTextSecondary,
                  fontSize = 12.sp,
                  textAlign = TextAlign.Center,
                  modifier = Modifier.padding(vertical = 4.dp)
                )
                if (reward.couponCode.isNotBlank()) {
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(6.dp))
                      .background(Color(0xFFF3E8FC))
                      .padding(horizontal = 10.dp, vertical = 4.dp)
                  ) {
                    Text(
                      text = "CODE: ${reward.couponCode}",
                      color = PhonePePurple,
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
              }
            }
          }

          // Top Scratchable Silver/Purple Layer
          if (!isRevealed) {
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(
                  Brush.linearGradient(
                    listOf(Color(0xFF8A38E0), Color(0xFF5F259F), Color(0xFF431379))
                  )
                )
                .clickable {
                  // Direct tap to scratch
                  scratchedProgress = 1f
                  onScratched()
                }
                .pointerInput(Unit) {
                  detectDragGestures { _, dragAmount ->
                    scratchedProgress = (scratchedProgress + (dragAmount.getDistance() / 800f)).coerceIn(0f, 1f)
                    if (scratchedProgress >= 0.35f) {
                      onScratched()
                    }
                  }
                }
                .testTag("scratch_interactive_layer"),
              contentAlignment = Alignment.Center
            ) {
              Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
              ) {
                Box(
                  modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Redeem,
                    contentDescription = null,
                    tint = PhonePeAmber,
                    modifier = Modifier.size(36.dp)
                  )
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                  text = "SCRATCH HERE",
                  color = Color.White,
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Black,
                  letterSpacing = 2.sp
                )
                Text(
                  text = "Swipe to reveal your reward",
                  color = Color.White.copy(alpha = 0.8f),
                  fontSize = 12.sp,
                  modifier = Modifier.padding(top = 4.dp)
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Bottom status / Claim button
        if (isRevealed) {
          Button(
            onClick = onDismiss,
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("claim_scratch_button"),
            colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text("Claim & Done", color = Color.White, fontWeight = FontWeight.Bold)
          }
        } else {
          Button(
            onClick = {
              scratchedProgress = 1f
              onScratched()
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("scratch_now_button"),
            colors = ButtonDefaults.buttonColors(containerColor = PhonePeAmber),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text("Tap to Scratch Instantly", color = Color.Black, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
