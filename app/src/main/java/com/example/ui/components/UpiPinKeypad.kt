package com.example.ui.components

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
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PhonePeGreen
import com.example.ui.theme.PhonePeRed
import com.example.ui.viewmodel.UpiPinPrompt

@Composable
fun UpiPinKeypadScreen(
  prompt: UpiPinPrompt,
  onDigitClick: (Char) -> Unit,
  onBackspaceClick: () -> Unit,
  onSubmitClick: () -> Unit,
  onCancelClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxSize(),
    color = Color(0xFF1E212D) // NPCI / UPI style authentic dark security slate
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding()
    ) {
      // Top header with Back and Bank Name
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onCancelClick,
          modifier = Modifier.testTag("pin_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Cancel UPI PIN",
            tint = Color.White
          )
        }
        Text(
          text = prompt.bankName,
          color = Color.White,
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.weight(1f)
        )
        // NPCI UPI branding badge
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF2C3246))
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(
            text = "UPI",
            color = Color(0xFF00C853),
            fontSize = 12.sp,
            fontWeight = FontWeight.Black
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Recipient and Amount card
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 24.dp)
          .clip(RoundedCornerShape(14.dp))
          .background(Color(0xFF282D3E))
          .padding(18.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Paying to",
              color = Color(0xFF9EA3B2),
              fontSize = 12.sp
            )
            Text(
              text = prompt.request.recipientName,
              color = Color.White,
              fontSize = 17.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
          Text(
            text = "₹${"%.2f".format(prompt.request.amount)}",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(32.dp))

      // "ENTER 4-DIGIT UPI PIN"
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "ENTER 4-DIGIT UPI PIN",
          color = Color(0xFFD1D5DB),
          fontSize = 13.sp,
          fontWeight = FontWeight.Medium,
          letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // PIN Dots
        Row(
          horizontalArrangement = Arrangement.spacedBy(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          repeat(4) { index ->
            val isFilled = index < prompt.enteredPin.length
            Box(
              modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(
                  when {
                    prompt.isError -> PhonePeRed
                    isFilled -> Color.White
                    else -> Color.Transparent
                  }
                )
                .border(
                  width = 2.dp,
                  color = if (prompt.isError) PhonePeRed else Color(0xFF6B7280),
                  shape = CircleShape
                )
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Security hint / Processing state
        Box(
          modifier = Modifier.height(30.dp),
          contentAlignment = Alignment.Center
        ) {
          if (prompt.isProcessing) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = PhonePeGreen,
                strokeWidth = 2.dp
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Verifying UPI PIN...",
                color = Color.White,
                fontSize = 13.sp
              )
            }
          } else if (prompt.isError) {
            Text(
              text = "Incorrect UPI PIN. Please try again.",
              color = PhonePeRed,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
          } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = Color(0xFF9EA3B2),
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "UPI PIN is never stored or shared with PhonePe",
                color = Color(0xFF9EA3B2),
                fontSize = 11.sp
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.weight(1f))

      // Custom Authentic UPI Numeric Keypad
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
          .background(Color(0xFF141720))
          .padding(top = 16.dp, bottom = 12.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // Row 1: 1, 2, 3
          KeypadRow(listOf("1", "2", "3"), onDigitClick)

          // Row 2: 4, 5, 6
          KeypadRow(listOf("4", "5", "6"), onDigitClick)

          // Row 3: 7, 8, 9
          KeypadRow(listOf("7", "8", "9"), onDigitClick)

          // Row 4: Backspace, 0, Submit
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            // Backspace Key
            Box(
              modifier = Modifier
                .width(90.dp)
                .height(54.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onBackspaceClick)
                .testTag("pin_key_backspace"),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.Backspace,
                contentDescription = "Backspace",
                tint = Color(0xFFE5E7EB),
                modifier = Modifier.size(26.dp)
              )
            }

            // '0' Key
            Box(
              modifier = Modifier
                .width(90.dp)
                .height(54.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = { onDigitClick('0') })
                .testTag("pin_key_0"),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "0",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Medium
              )
            }

            // Submit Key
            Box(
              modifier = Modifier
                .width(90.dp)
                .height(54.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(PhonePeGreen)
                .clickable(onClick = onSubmitClick)
                .testTag("pin_key_submit"),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Submit UPI PIN",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun KeypadRow(
  digits: List<String>,
  onDigitClick: (Char) -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    digits.forEach { digit ->
      Box(
        modifier = Modifier
          .width(90.dp)
          .height(54.dp)
          .clip(RoundedCornerShape(12.dp))
          .clickable(onClick = { onDigitClick(digit.first()) })
          .testTag("pin_key_$digit"),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = digit,
          color = Color.White,
          fontSize = 26.sp,
          fontWeight = FontWeight.Medium
        )
      }
    }
  }
}
