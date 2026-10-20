package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.UserProfile
import com.example.ui.theme.PhonePeAmber
import com.example.ui.theme.PhonePeGreen
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.PhonePePurpleDark
import com.example.ui.theme.PhonePeTextPrimary
import com.example.ui.theme.PhonePeTextSecondary

@Composable
fun SoundboxDialog(
  userProfile: UserProfile,
  onDismiss: () -> Unit,
  onTestSound: (Double) -> Unit,
  onLanguageChange: (String) -> Unit,
  onToggleSoundbox: (Boolean) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedAmount by remember { mutableDoubleStateOf(500.0) }

  val infiniteTransition = rememberInfiniteTransition(label = "soundwave")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.95f,
    targetValue = 1.05f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse"
  )

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = modifier
        .fillMaxWidth(0.92f)
        .clip(RoundedCornerShape(24.dp)),
      color = Color.White
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Top Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(PhonePePurple),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "PhonePe Smart Soundbox",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PhonePeTextPrimary
              )
              Text(
                text = "Instant audio payment alerts",
                fontSize = 11.sp,
                color = PhonePeTextSecondary
              )
            }
          }
          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("soundbox_close_button")
          ) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Visual Representation of the PhonePe Soundbox Device
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
              Brush.verticalGradient(
                listOf(PhonePePurple, PhonePePurpleDark)
              )
            )
            .padding(20.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            // Speaker Grille with LED
            Box(
              modifier = Modifier
                .size(90.dp)
                .scale(pulseScale)
                .clip(CircleShape)
                .background(Color(0xFF26004D))
                .border(3.dp, Color(0xFF7E3FB8), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Glowing LED
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (userProfile.soundboxAlertsEnabled) PhonePeGreen else Color.Gray)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Icon(
                  imageVector = Icons.Default.GraphicEq,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(32.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
              text = "PhonePe",
              color = Color.White,
              fontSize = 18.sp,
              fontWeight = FontWeight.Black,
              letterSpacing = 1.sp
            )

            Text(
              text = if (userProfile.soundboxLanguage == "HINDI")
                "\"फ़ोनपे पर ₹${selectedAmount.toInt()} प्राप्त हुए\""
              else
                "\"Payment of ₹${selectedAmount.toInt()} received on PhonePe\"",
              color = Color(0xFFFFD54F),
              fontSize = 13.sp,
              fontWeight = FontWeight.Medium,
              textAlign = TextAlign.Center,
              modifier = Modifier.padding(top = 6.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Soundbox Alert Toggle
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FC)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Voice Announcements",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = PhonePeTextPrimary
              )
              Text(
                text = "Announce every incoming payment",
                fontSize = 11.sp,
                color = PhonePeTextSecondary
              )
            }
            Switch(
              checked = userProfile.soundboxAlertsEnabled,
              onCheckedChange = onToggleSoundbox,
              colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PhonePePurple
              ),
              modifier = Modifier.testTag("soundbox_toggle_switch")
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Language Selector: Hindi vs English
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(10.dp))
              .background(if (userProfile.soundboxLanguage == "HINDI") PhonePePurple else Color(0xFFF0F1F5))
              .clickable { onLanguageChange("HINDI") }
              .padding(vertical = 10.dp)
              .testTag("soundbox_lang_hindi"),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "🇮🇳 हिन्दी (Hindi)",
              color = if (userProfile.soundboxLanguage == "HINDI") Color.White else PhonePeTextPrimary,
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold
            )
          }

          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(10.dp))
              .background(if (userProfile.soundboxLanguage == "ENGLISH") PhonePePurple else Color(0xFFF0F1F5))
              .clickable { onLanguageChange("ENGLISH") }
              .padding(vertical = 10.dp)
              .testTag("soundbox_lang_english"),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "English",
              color = if (userProfile.soundboxLanguage == "ENGLISH") Color.White else PhonePeTextPrimary,
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Test Amounts chips
        Text(
          text = "Select Test Amount:",
          fontSize = 12.sp,
          color = PhonePeTextSecondary,
          modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf(50.0, 100.0, 500.0, 1200.0).forEach { amount ->
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (selectedAmount == amount) Color(0xFFF3E8FC) else Color(0xFFF5F6F8))
                .border(
                  width = 1.dp,
                  color = if (selectedAmount == amount) PhonePePurple else Color(0xFFE0E0E0),
                  shape = RoundedCornerShape(8.dp)
                )
                .clickable { selectedAmount = amount }
                .padding(vertical = 8.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "₹${amount.toInt()}",
                color = if (selectedAmount == amount) PhonePePurple else PhonePeTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Play Soundbox Button
        Button(
          onClick = { onTestSound(selectedAmount) },
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("play_soundbox_test_button"),
          colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Play Soundbox Announcement",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}
