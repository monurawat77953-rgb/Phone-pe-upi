package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.TransactionEntity
import com.example.ui.theme.PhonePeGreen
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.PhonePePurpleContainer
import com.example.ui.theme.PhonePeRed
import com.example.ui.theme.PhonePeTextPrimary
import com.example.ui.theme.PhonePeTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReceiptDetailDialog(
  transaction: TransactionEntity,
  onDismiss: () -> Unit,
  onRepeatPayment: (TransactionEntity) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val isDebit = transaction.type == "DEBIT"

  val formattedDate = remember(transaction.timestamp) {
    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    sdf.format(Date(transaction.timestamp))
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = modifier
        .fillMaxWidth(0.92f)
        .clip(RoundedCornerShape(20.dp)),
      color = Color.White
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        // Header with Title and Close
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(PhonePePurple),
              contentAlignment = Alignment.Center
            ) {
              Text("पे", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Transaction Details",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = PhonePeTextPrimary
            )
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("close_receipt_button")
          ) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Status Banner
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = if (isDebit) Color(0xFFF9F9FB) else Color(0xFFE8F5E9)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = PhonePeGreen,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = if (isDebit) "Paid Successfully" else "Received Successfully",
                color = PhonePeGreen,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = formattedDate,
                color = PhonePeTextSecondary,
                fontSize = 12.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Recipient / Sender Name and Amount
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = if (isDebit) "To" else "From",
              color = PhonePeTextSecondary,
              fontSize = 12.sp
            )
            Text(
              text = transaction.title,
              color = PhonePeTextPrimary,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold
            )
            if (transaction.recipientUpiId.isNotBlank()) {
              Text(
                text = transaction.recipientUpiId,
                color = PhonePeTextSecondary,
                fontSize = 13.sp
              )
            }
          }

          Text(
            text = "${if (isDebit) "-" else "+"}₹${"%.2f".format(transaction.amount)}",
            color = if (isDebit) PhonePeTextPrimary else PhonePeGreen,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold
          )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = Color(0xFFEEEEEE))
        Spacer(modifier = Modifier.height(16.dp))

        // Details Grid
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          // Account / Debited from
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Debited from", color = PhonePeTextSecondary, fontSize = 13.sp)
            Text(transaction.debitedFrom, color = PhonePeTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
          }

          // Transaction ID
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Transaction ID", color = PhonePeTextSecondary, fontSize = 13.sp)
            Text(transaction.id, color = PhonePeTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
          }

          // UTR Number with copy
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("UTR Number", color = PhonePeTextSecondary, fontSize = 13.sp)
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.clickable {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("UTR", transaction.utrNumber))
                Toast.makeText(context, "UTR copied", Toast.LENGTH_SHORT).show()
              }
            ) {
              Text(transaction.utrNumber, color = PhonePePurple, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
              Spacer(modifier = Modifier.width(4.dp))
              Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "Copy UTR",
                tint = PhonePePurple,
                modifier = Modifier.size(14.dp)
              )
            }
          }

          // Note if present
          if (transaction.note.isNotBlank()) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Note", color = PhonePeTextSecondary, fontSize = 13.sp)
              Text(transaction.note, color = PhonePeTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Action Buttons: Repeat Payment & Share
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          if (isDebit) {
            Button(
              onClick = {
                onDismiss()
                onRepeatPayment(transaction)
              },
              modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .testTag("repeat_payment_button"),
              colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
              shape = RoundedCornerShape(12.dp)
            ) {
              Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Pay Again", fontSize = 13.sp)
            }
          }

          OutlinedButton(
            onClick = {
              val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                  Intent.EXTRA_TEXT,
                  "PhonePe Payment Receipt: ₹${transaction.amount} paid to ${transaction.title}. UTR: ${transaction.utrNumber}."
                )
              }
              context.startActivity(Intent.createChooser(shareIntent, "Share Receipt"))
            },
            modifier = Modifier
              .weight(1f)
              .height(48.dp)
              .testTag("share_receipt_action"),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Share", fontSize = 13.sp)
          }
        }
      }
    }
  }
}
