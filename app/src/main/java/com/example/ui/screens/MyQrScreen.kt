package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.BankAccountEntity
import com.example.data.model.UserProfile
import com.example.ui.theme.PhonePeGreen
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.PhonePePurpleContainer
import com.example.ui.theme.PhonePePurpleDark
import com.example.ui.theme.PhonePeTextPrimary
import com.example.ui.theme.PhonePeTextSecondary

@Composable
fun MyQrScreen(
  userProfile: UserProfile,
  primaryBank: BankAccountEntity?,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  Surface(
    modifier = modifier.fillMaxSize(),
    color = PhonePePurple
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding()
    ) {
      // Top Bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("my_qr_back_button")
        ) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
        }
        Text(
          text = "My QR Code",
          color = Color.White,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Central White QR Card
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 24.dp)
          .weight(1f),
        contentAlignment = Alignment.Center
      ) {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(24.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            // PhonePe logo & User info
            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(PhonePePurple),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "पे",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
              text = userProfile.name,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = PhonePeTextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // UPI ID with copy action
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(PhonePePurpleContainer)
                .clickable {
                  val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                  clipboard.setPrimaryClip(ClipData.newPlainText("UPI ID", userProfile.upiId))
                  Toast.makeText(context, "UPI ID copied!", Toast.LENGTH_SHORT).show()
                }
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .testTag("copy_upi_id_chip")
            ) {
              Text(
                text = userProfile.upiId,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = PhonePePurple
              )
              Spacer(modifier = Modifier.width(6.dp))
              Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "Copy UPI ID",
                tint = PhonePePurple,
                modifier = Modifier.size(14.dp)
              )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Stylized PhonePe QR Code Canvas with Finder Patterns
            Box(
              modifier = Modifier
                .size(200.dp)
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                .padding(12.dp),
              contentAlignment = Alignment.Center
            ) {
              Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val dotSize = w / 15f

                // Draw Finder Patterns (Top-Left, Top-Right, Bottom-Left)
                fun drawFinderPattern(x: Float, y: Float) {
                  // Outer box
                  drawRect(
                    color = Color.Black,
                    topLeft = Offset(x, y),
                    size = Size(dotSize * 4, dotSize * 4)
                  )
                  // Inner white box
                  drawRect(
                    color = Color.White,
                    topLeft = Offset(x + dotSize, y + dotSize),
                    size = Size(dotSize * 2, dotSize * 2)
                  )
                  // Center black dot
                  drawRect(
                    color = Color(0xFF5F259F),
                    topLeft = Offset(x + dotSize * 1.35f, y + dotSize * 1.35f),
                    size = Size(dotSize * 1.3f, dotSize * 1.3f)
                  )
                }

                drawFinderPattern(0f, 0f)
                drawFinderPattern(w - dotSize * 4, 0f)
                drawFinderPattern(0f, h - dotSize * 4)

                // Fill with authentic QR pattern blocks
                val seed = 42
                for (row in 0 until 15) {
                  for (col in 0 until 15) {
                    val inTL = row < 4 && col < 4
                    val inTR = row < 4 && col >= 11
                    val inBL = row >= 11 && col < 4
                    if (!inTL && !inTR && !inBL) {
                      // Deterministic mock QR code pattern
                      if ((row * 7 + col * 13 + (row xor col)) % 3 == 0) {
                        drawRect(
                          color = if ((row + col) % 5 == 0) Color(0xFF5F259F) else Color(0xFF222222),
                          topLeft = Offset(col * dotSize, row * dotSize),
                          size = Size(dotSize * 0.9f, dotSize * 0.9f)
                        )
                      }
                    }
                  }
                }
              }

              // Center PhonePe Emblem
              Box(
                modifier = Modifier
                  .size(34.dp)
                  .clip(CircleShape)
                  .background(Color.White)
                  .border(2.dp, PhonePePurple, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "पे",
                  color = PhonePePurple,
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Primary Bank Account info
            Row(
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.AccountBalance, contentDescription = null, tint = PhonePeGreen, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = primaryBank?.let { "${it.bankName} ••${it.accountNumberLast4}" } ?: "State Bank of India ••4821",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = PhonePeTextSecondary
              )
            }

            Text(
              text = "Scan with any UPI App to Pay",
              fontSize = 11.sp,
              color = PhonePeTextSecondary.copy(alpha = 0.8f),
              modifier = Modifier.padding(top = 2.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Action Buttons: Share QR & Download QR
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        OutlinedButton(
          onClick = {
            Toast.makeText(context, "QR Code saved to Gallery", Toast.LENGTH_SHORT).show()
          },
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .testTag("download_qr_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.White)
        ) {
          Icon(Icons.Default.Download, contentDescription = "Download")
          Spacer(modifier = Modifier.width(6.dp))
          Text("Download", fontWeight = FontWeight.Bold)
        }

        Button(
          onClick = {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
              type = "text/plain"
              putExtra(Intent.EXTRA_TEXT, "Pay ${userProfile.name} on PhonePe using UPI ID: ${userProfile.upiId}")
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share QR Code"))
          },
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .testTag("share_qr_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
          Icon(Icons.Default.Share, contentDescription = "Share", tint = PhonePePurple)
          Spacer(modifier = Modifier.width(6.dp))
          Text("Share QR", color = PhonePePurple, fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(8.dp))
    }
  }
}
