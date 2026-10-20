package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PaymentResult
import com.example.ui.theme.PhonePeAmber
import com.example.ui.theme.PhonePeGreen
import com.example.ui.theme.PhonePeGreenLight
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.PhonePePurpleContainer
import com.example.ui.theme.PhonePeTextPrimary
import com.example.ui.theme.PhonePeTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PaymentSuccessScreen(
  result: PaymentResult,
  onDone: () -> Unit,
  onOpenScratchCard: () -> Unit,
  onPlaySoundbox: (Double) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val scaleAnim = remember { Animatable(0f) }

  LaunchedEffect(Unit) {
    scaleAnim.animateTo(
      targetValue = 1f,
      animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
      )
    )
  }

  val formattedDate = remember(result.timestamp) {
    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    sdf.format(Date(result.timestamp))
  }

  Surface(
    modifier = modifier.fillMaxSize(),
    color = Color.White
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding()
        .padding(horizontal = 20.dp)
        .verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(36.dp))

      // Animated Green PhonePe Success Badge
      Box(
        modifier = Modifier
          .size(90.dp)
          .scale(scaleAnim.value)
          .clip(CircleShape)
          .background(PhonePeGreen),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Check,
          contentDescription = "Success",
          tint = Color.White,
          modifier = Modifier.size(54.dp)
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      Text(
        text = "Payment Successful",
        color = PhonePeGreen,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "₹${"%.2f".format(result.amount)}",
        color = PhonePeTextPrimary,
        fontSize = 36.sp,
        fontWeight = FontWeight.ExtraBold
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "Paid to ${result.recipientName}",
        color = PhonePeTextSecondary,
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium
      )

      Text(
        text = formattedDate,
        color = PhonePeTextSecondary.copy(alpha = 0.8f),
        fontSize = 12.sp,
        modifier = Modifier.padding(top = 2.dp)
      )

      Spacer(modifier = Modifier.height(24.dp))

      // Transaction Summary Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FC))
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Debited from", color = PhonePeTextSecondary, fontSize = 13.sp)
            Text(result.debitedFrom, color = PhonePeTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("UPI Transaction ID", color = PhonePeTextSecondary, fontSize = 13.sp)
            Text(result.transactionId, color = PhonePeTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("UTR No.", color = PhonePeTextSecondary, fontSize = 13.sp)
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.clickable {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("UTR", result.utrNumber))
                Toast.makeText(context, "UTR copied to clipboard", Toast.LENGTH_SHORT).show()
              }
            ) {
              Text(result.utrNumber, color = PhonePePurple, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
              Spacer(modifier = Modifier.width(4.dp))
              Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "Copy UTR",
                tint = PhonePePurple,
                modifier = Modifier.size(14.dp)
              )
            }
          }
        }
      }

      // Scratch Card Reward Banner
      if (result.wonScratchCard) {
        Spacer(modifier = Modifier.height(16.dp))
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
              Brush.horizontalGradient(
                listOf(Color(0xFF5F259F), Color(0xFF8E24AA))
              )
            )
            .clickable(onClick = onOpenScratchCard)
            .padding(16.dp)
            .testTag("won_scratch_card_card")
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(PhonePeAmber),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Redeem,
                contentDescription = "Reward",
                tint = Color.White,
                modifier = Modifier.size(26.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "You won a Scratch Card!",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Tap to scratch & earn real cashback in wallet",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 12.sp
              )
            }
            Text(
              text = "SCRATCH",
              color = PhonePeAmber,
              fontSize = 12.sp,
              fontWeight = FontWeight.ExtraBold,
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 6.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Action row: Share receipt & Soundbox Voice Alert test
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedButton(
          onClick = {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
              type = "text/plain"
              putExtra(
                Intent.EXTRA_TEXT,
                "PhonePe UPI Payment of ₹${result.amount} to ${result.recipientName} Successful. UTR: ${result.utrNumber}."
              )
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share Payment Receipt"))
          },
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .testTag("share_receipt_button"),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Share",
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text("Share", fontSize = 13.sp)
        }

        OutlinedButton(
          onClick = { onPlaySoundbox(result.amount) },
          modifier = Modifier
            .weight(1.3f)
            .height(48.dp)
            .testTag("soundbox_voice_button"),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(
            imageVector = Icons.Default.VolumeUp,
            contentDescription = "Voice",
            tint = PhonePePurple,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text("Voice Alert", color = PhonePePurple, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Done Button
      Button(
        onClick = onDone,
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("payment_done_button"),
        colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
        shape = RoundedCornerShape(14.dp)
      ) {
        Text(
          text = "Done",
          color = Color.White,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
