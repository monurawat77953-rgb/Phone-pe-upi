package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.PhonePeAmber
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.PhonePePurpleDark

@Composable
fun PhonePeTopBar(
  userProfile: UserProfile,
  onProfileClick: () -> Unit,
  onQrScanClick: () -> Unit,
  onSoundboxClick: () -> Unit,
  onNotificationsClick: () -> Unit,
  onHelpClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(PhonePePurple)
      .statusBarsPadding()
      .padding(horizontal = 14.dp, vertical = 10.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Profile Avatar with initials and QR badge
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(PhonePePurpleDark)
          .border(1.5.dp, Color.White.copy(alpha = 0.8f), CircleShape)
          .clickable(onClick = onProfileClick)
          .testTag("profile_avatar_button"),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "RS",
          color = Color.White,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      // Location & Address Selector
      Column(
        modifier = Modifier
          .weight(1f)
          .clickable(onClick = onProfileClick)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "Home",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
          Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "Change Location",
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
        }
        Text(
          text = "HSR Layout, Bengaluru",
          color = Color.White.copy(alpha = 0.8f),
          fontSize = 11.sp,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }

      // Actions: Soundbox, QR Scan, Notifications, Help
      // Soundbox Icon Button
      IconButton(
        onClick = onSoundboxClick,
        modifier = Modifier
          .size(38.dp)
          .testTag("soundbox_button")
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
            imageVector = Icons.Default.VolumeUp,
            contentDescription = "PhonePe Soundbox",
            tint = Color.White,
            modifier = Modifier.size(22.dp)
          )
          Box(
            modifier = Modifier
              .size(6.dp)
              .clip(CircleShape)
              .background(PhonePeAmber)
              .align(Alignment.TopEnd)
          )
        }
      }

      // QR Scanner Icon Button
      IconButton(
        onClick = onQrScanClick,
        modifier = Modifier
          .size(38.dp)
          .testTag("qr_scanner_button")
      ) {
        Icon(
          imageVector = Icons.Default.QrCodeScanner,
          contentDescription = "Scan Any QR",
          tint = Color.White,
          modifier = Modifier.size(24.dp)
        )
      }

      // Notifications Icon Button
      IconButton(
        onClick = onNotificationsClick,
        modifier = Modifier
          .size(38.dp)
          .testTag("notifications_button")
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notifications",
            tint = Color.White,
            modifier = Modifier.size(22.dp)
          )
          Box(
            modifier = Modifier
              .size(7.dp)
              .clip(CircleShape)
              .background(Color(0xFFFF3B30))
              .align(Alignment.TopEnd)
          )
        }
      }

      // Help Icon Button
      IconButton(
        onClick = onHelpClick,
        modifier = Modifier
          .size(38.dp)
          .testTag("help_button")
      ) {
        Icon(
          imageVector = Icons.Default.HelpOutline,
          contentDescription = "Help & Support",
          tint = Color.White,
          modifier = Modifier.size(22.dp)
        )
      }
    }
  }
}
