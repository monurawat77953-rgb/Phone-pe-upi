package com.example.ui.screens

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ContactEntity
import com.example.ui.theme.PhonePeGreen
import com.example.ui.theme.PhonePePurple

@Composable
fun QrScannerScreen(
  onBack: () -> Unit,
  onQrScanned: (ContactEntity, Double) -> Unit,
  onOpenMyQr: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isFlashOn by remember { mutableStateOf(false) }

  val infiniteTransition = rememberInfiniteTransition(label = "laser")
  val laserOffset by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 240f,
    animationSpec = infiniteRepeatable(
      animation = tween(1800, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "laser_y"
  )

  Surface(
    modifier = modifier.fillMaxSize(),
    color = Color.Black
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Top Controls Bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("scanner_back_button")
        ) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
        }

        Text(
          text = "Scan Any QR Code",
          color = Color.White,
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold
        )

        Row {
          IconButton(
            onClick = { isFlashOn = !isFlashOn },
            modifier = Modifier.testTag("torch_toggle_button")
          ) {
            Icon(
              imageVector = if (isFlashOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
              contentDescription = "Torch",
              tint = if (isFlashOn) Color(0xFFFFEB3B) else Color.White
            )
          }

          IconButton(
            onClick = onOpenMyQr,
            modifier = Modifier.testTag("my_qr_shortcut_button")
          ) {
            Icon(Icons.Default.QrCode, contentDescription = "My QR", tint = Color.White)
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      Text(
        text = "Align QR code inside the frame to scan",
        color = Color(0xFFD1D5DB),
        fontSize = 13.sp,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(30.dp))

      // Viewfinder box with laser line
      Box(
        modifier = Modifier
          .size(260.dp)
          .clip(RoundedCornerShape(24.dp))
          .border(3.dp, PhonePeGreen, RoundedCornerShape(24.dp))
          .background(Color(0x33101010)),
        contentAlignment = Alignment.TopCenter
      ) {
        // Laser scanning line
        Box(
          modifier = Modifier
            .offset(y = laserOffset.dp)
            .fillMaxWidth()
            .height(2.5.dp)
            .background(PhonePeGreen)
        )

        // Corner Guides
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
        ) {
          Text(
            text = "BHIM UPI",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.BottomCenter)
          )
        }
      }

      Spacer(modifier = Modifier.height(36.dp))

      // Quick Scan Preset Merchants
      Text(
        text = "OR SCAN POPULAR PRESET QRs",
        color = Color(0xFF9E9E9E),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
      )

      Spacer(modifier = Modifier.height(14.dp))

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Preset 1: Sharma Grocery
        PresetQrItem(
          merchantName = "Sharma Grocery Mart",
          upiId = "sharmagrocery@ybl",
          defaultAmount = 180.0,
          onClick = {
            onQrScanned(
              ContactEntity("p1", "Sharma Grocery Mart", "+91 98450 67123", "sharmagrocery@ybl", "#4CAF50"),
              180.0
            )
          }
        )

        // Preset 2: Chai & Snacks Corner
        PresetQrItem(
          merchantName = "Chai & Snacks Point",
          upiId = "chaipoint@paytm",
          defaultAmount = 40.0,
          onClick = {
            onQrScanned(
              ContactEntity("p2", "Chai & Snacks Point", "+91 97120 44102", "chaipoint@paytm", "#FF9800"),
              40.0
            )
          }
        )

        // Preset 3: Apollo Pharmacy
        PresetQrItem(
          merchantName = "Apollo Pharmacy",
          upiId = "apollopharmacy@okhdfcbank",
          defaultAmount = 520.0,
          onClick = {
            onQrScanned(
              ContactEntity("p3", "Apollo Pharmacy", "+91 99001 88310", "apollopharmacy@okhdfcbank", "#2196F3"),
              520.0
            )
          }
        )
      }

      Spacer(modifier = Modifier.weight(1f))

      // Bottom Bar to open My QR
      Button(
        onClick = onOpenMyQr,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 24.dp, vertical = 12.dp)
          .height(50.dp)
          .testTag("show_my_qr_button"),
        colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
        shape = RoundedCornerShape(12.dp)
      ) {
        Icon(Icons.Default.QrCode, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Show My QR Code", color = Color.White, fontWeight = FontWeight.Bold)
      }
    }
  }
}

@Composable
private fun PresetQrItem(
  merchantName: String,
  upiId: String,
  defaultAmount: Double,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(Color(0xFF1E212D))
      .clickable(onClick = onClick)
      .padding(horizontal = 14.dp, vertical = 10.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Storefront, contentDescription = null, tint = PhonePeGreen, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(text = merchantName, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
          Text(text = upiId, color = Color(0xFF9E9E9E), fontSize = 11.sp)
        }
      }
      Text(
        text = "Pay ₹${defaultAmount.toInt()}",
        color = PhonePeGreen,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold
      )
    }
  }
}
