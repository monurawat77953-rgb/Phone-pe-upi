package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.filled.TimeToLeave
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.BankAccountEntity
import com.example.data.local.ContactEntity
import com.example.data.local.TransactionEntity
import com.example.data.model.UserProfile
import com.example.ui.theme.PhonePeAmber
import com.example.ui.theme.PhonePeGreen
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.PhonePePurpleContainer
import com.example.ui.theme.PhonePePurpleDark
import com.example.ui.theme.PhonePeTextPrimary
import com.example.ui.theme.PhonePeTextSecondary
import com.example.ui.viewmodel.ActiveModal
import com.example.ui.viewmodel.MainTab
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
  userProfile: UserProfile,
  recentTransactions: List<TransactionEntity>,
  primaryBank: BankAccountEntity?,
  onOpenModal: (ActiveModal) -> Unit,
  onNavigateTab: (MainTab) -> Unit,
  onSelectTransaction: (TransactionEntity) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF7F8FA))
      .padding(horizontal = 14.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. UPI ID & My QR Banner Strip
    item {
      Spacer(modifier = Modifier.height(2.dp))
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
              val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
              clipboard.setPrimaryClip(ClipData.newPlainText("UPI ID", userProfile.upiId))
              Toast.makeText(context, "UPI ID copied: ${userProfile.upiId}", Toast.LENGTH_SHORT).show()
            }
          ) {
            Text(
              text = "UPI ID: ",
              fontSize = 12.sp,
              color = PhonePeTextSecondary
            )
            Text(
              text = userProfile.upiId,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = PhonePeTextPrimary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              imageVector = Icons.Default.ContentCopy,
              contentDescription = "Copy UPI ID",
              tint = PhonePePurple,
              modifier = Modifier.size(14.dp)
            )
          }

          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clickable { onOpenModal(ActiveModal.MyQrCode) }
              .testTag("home_my_qr_chip")
          ) {
            Icon(
              imageVector = Icons.Default.QrCode,
              contentDescription = "My QR",
              tint = PhonePePurple,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "My QR",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = PhonePePurple
            )
          }
        }
      }
    }

    // 2. TRANSFER MONEY - 4 Iconic PhonePe Actions
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
            .padding(16.dp)
        ) {
          Text(
            text = "TRANSFER MONEY",
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            color = PhonePeTextPrimary,
            letterSpacing = 0.5.sp
          )

          Spacer(modifier = Modifier.height(16.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            // 1. To Mobile Number
            TransferActionItem(
              title = "To Mobile\nNumber",
              icon = Icons.Default.Person,
              onClick = { onOpenModal(ActiveModal.SendMoney()) },
              testTag = "action_to_mobile"
            )

            // 2. To Bank / UPI ID
            TransferActionItem(
              title = "To Bank /\nUPI ID",
              icon = Icons.Default.AccountBalance,
              onClick = { onOpenModal(ActiveModal.SendMoney()) },
              testTag = "action_to_bank"
            )

            // 3. To Self Account
            TransferActionItem(
              title = "To Self\nAccount",
              icon = Icons.Default.SyncAlt,
              onClick = { onOpenModal(ActiveModal.CheckBalance) },
              testTag = "action_to_self"
            )

            // 4. Check Balance
            TransferActionItem(
              title = "Check\nBalance",
              icon = Icons.Default.AccountBalanceWallet,
              onClick = { onOpenModal(ActiveModal.CheckBalance) },
              testTag = "action_check_balance"
            )
          }
        }
      }
    }

    // 3. Quick Shortcuts Strip (Wallet, Rewards, Refer & Get ₹100, UPI Lite)
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // PhonePe Wallet
        QuickShortcutCard(
          modifier = Modifier.weight(1f),
          title = "PhonePe Wallet",
          subtitle = "₹${"%.2f".format(userProfile.walletBalance)}",
          icon = Icons.Default.AccountBalanceWallet,
          iconTint = PhonePePurple,
          onClick = { onOpenModal(ActiveModal.Wallet) },
          testTag = "shortcut_wallet"
        )

        // Rewards
        QuickShortcutCard(
          modifier = Modifier.weight(1f),
          title = "Rewards",
          subtitle = "Scratch Cards",
          icon = Icons.Default.CardGiftcard,
          iconTint = PhonePeAmber,
          onClick = { onOpenModal(ActiveModal.Rewards) },
          testTag = "shortcut_rewards"
        )

        // UPI Lite
        QuickShortcutCard(
          modifier = Modifier.weight(1f),
          title = "UPI Lite",
          subtitle = "₹${"%.2f".format(userProfile.upiLiteBalance)}",
          icon = Icons.Default.FlashOn,
          iconTint = PhonePeGreen,
          onClick = { onOpenModal(ActiveModal.Wallet) },
          testTag = "shortcut_upi_lite"
        )
      }
    }

    // 4. Promotional Banner (Generated Artwork)
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onOpenModal(ActiveModal.Rewards) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Image(
          painter = painterResource(id = R.drawable.phonepe_banner_1790923164617),
          contentDescription = "PhonePe Cashback Offers Banner",
          modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f),
          contentScale = ContentScale.Crop
        )
      }
    }

    // 5. RECHARGE & PAY BILLS Section
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
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "RECHARGE & PAY BILLS",
              fontSize = 12.sp,
              fontWeight = FontWeight.ExtraBold,
              color = PhonePeTextPrimary,
              letterSpacing = 0.5.sp
            )
            Text(
              text = "View All",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = PhonePePurple,
              modifier = Modifier.clickable { onOpenModal(ActiveModal.BillRecharge("MOBILE")) }
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          // 4x2 Grid of Bill Payments
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            BillGridItem("Mobile\nRecharge", Icons.Default.PhoneAndroid) {
              onOpenModal(ActiveModal.BillRecharge("MOBILE"))
            }
            BillGridItem("DTH", Icons.Default.LiveTv) {
              onOpenModal(ActiveModal.BillRecharge("DTH"))
            }
            BillGridItem("Electricity", Icons.Default.ElectricBolt) {
              onOpenModal(ActiveModal.BillRecharge("ELECTRICITY"))
            }
            BillGridItem("Credit Card\nPayment", Icons.Default.CreditCard) {
              onOpenModal(ActiveModal.BillRecharge("CREDIT_CARD"))
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            BillGridItem("FASTag\nRecharge", Icons.Default.TimeToLeave) {
              onOpenModal(ActiveModal.BillRecharge("FASTAG"))
            }
            BillGridItem("Book a\nCylinder", Icons.Default.LocalGasStation) {
              onOpenModal(ActiveModal.BillRecharge("ELECTRICITY"))
            }
            BillGridItem("Buy 24K\nGold", Icons.Default.MonetizationOn) {
              onNavigateTab(MainTab.WEALTH)
            }
            BillGridItem("See All\nBills >", Icons.AutoMirrored.Filled.ArrowForward) {
              onOpenModal(ActiveModal.BillRecharge("MOBILE"))
            }
          }
        }
      }
    }

    // 6. INSURANCE & WEALTH CARDS
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
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "INSURANCE & INVESTMENTS",
              fontSize = 12.sp,
              fontWeight = FontWeight.ExtraBold,
              color = PhonePeTextPrimary,
              letterSpacing = 0.5.sp
            )
            Text(
              text = "Explore",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = PhonePePurple,
              modifier = Modifier.clickable { onNavigateTab(MainTab.INSURANCE) }
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            InsuranceGridItem("Bike\nInsurance", Icons.Default.TwoWheeler, "From ₹499/yr") {
              onNavigateTab(MainTab.INSURANCE)
            }
            InsuranceGridItem("Car\nInsurance", Icons.Default.DirectionsCar, "From ₹2094") {
              onNavigateTab(MainTab.INSURANCE)
            }
            InsuranceGridItem("Health\nInsurance", Icons.Default.HealthAndSafety, "1 Cr Cover") {
              onNavigateTab(MainTab.INSURANCE)
            }
            InsuranceGridItem("24K Gold\nVault", Icons.Default.MonetizationOn, "₹7,480/gm") {
              onNavigateTab(MainTab.WEALTH)
            }
          }
        }
      }
    }

    // 7. RECENT TRANSACTIONS PREVIEW
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
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "RECENT TRANSACTIONS",
              fontSize = 12.sp,
              fontWeight = FontWeight.ExtraBold,
              color = PhonePeTextPrimary,
              letterSpacing = 0.5.sp
            )
            Text(
              text = "View History",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = PhonePePurple,
              modifier = Modifier
                .clickable { onNavigateTab(MainTab.HISTORY) }
                .testTag("home_view_history_button")
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          if (recentTransactions.isEmpty()) {
            Text(
              text = "No recent transactions found.",
              fontSize = 13.sp,
              color = PhonePeTextSecondary,
              modifier = Modifier.padding(vertical = 12.dp)
            )
          } else {
            recentTransactions.take(3).forEach { txn ->
              RecentTxnRow(txn = txn, onClick = { onSelectTransaction(txn) })
              Divider(color = Color(0xFFF0F1F5), modifier = Modifier.padding(vertical = 8.dp))
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(80.dp)) // Padding for bottom nav bar
    }
  }
}

@Composable
private fun TransferActionItem(
  title: String,
  icon: ImageVector,
  onClick: () -> Unit,
  testTag: String
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clickable(onClick = onClick)
      .testTag(testTag)
  ) {
    Box(
      modifier = Modifier
        .size(52.dp)
        .clip(CircleShape)
        .background(PhonePePurple),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = title,
        tint = Color.White,
        modifier = Modifier.size(26.dp)
      )
    }
    Spacer(modifier = Modifier.height(6.dp))
    Text(
      text = title,
      fontSize = 11.sp,
      fontWeight = FontWeight.SemiBold,
      color = PhonePeTextPrimary,
      textAlign = TextAlign.Center,
      lineHeight = 14.sp
    )
  }
}

@Composable
private fun QuickShortcutCard(
  title: String,
  subtitle: String,
  icon: ImageVector,
  iconTint: Color,
  onClick: () -> Unit,
  testTag: String,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .clickable(onClick = onClick)
      .testTag(testTag),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = iconTint,
        modifier = Modifier.size(22.dp)
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = title,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = PhonePeTextPrimary,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
      Text(
        text = subtitle,
        fontSize = 10.sp,
        color = PhonePeTextSecondary,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    }
  }
}

@Composable
private fun BillGridItem(
  title: String,
  icon: ImageVector,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .width(72.dp)
      .clickable(onClick = onClick)
  ) {
    Box(
      modifier = Modifier
        .size(46.dp)
        .clip(CircleShape)
        .background(Color(0xFFF3E8FC)),
      contentAlignment = Alignment.Center
    ) {
      Icon(imageVector = icon, contentDescription = null, tint = PhonePePurple, modifier = Modifier.size(24.dp))
    }
    Spacer(modifier = Modifier.height(6.dp))
    Text(
      text = title,
      fontSize = 10.sp,
      fontWeight = FontWeight.Medium,
      color = PhonePeTextPrimary,
      textAlign = TextAlign.Center,
      lineHeight = 13.sp
    )
  }
}

@Composable
private fun InsuranceGridItem(
  title: String,
  icon: ImageVector,
  badgeText: String,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .width(72.dp)
      .clickable(onClick = onClick)
  ) {
    Box(
      modifier = Modifier
        .size(46.dp)
        .clip(CircleShape)
        .background(Color(0xFFE8F1FC)),
      contentAlignment = Alignment.Center
    ) {
      Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF1976D2), modifier = Modifier.size(24.dp))
    }
    Spacer(modifier = Modifier.height(6.dp))
    Text(
      text = title,
      fontSize = 10.sp,
      fontWeight = FontWeight.Medium,
      color = PhonePeTextPrimary,
      textAlign = TextAlign.Center,
      lineHeight = 13.sp
    )
    Text(
      text = badgeText,
      fontSize = 9.sp,
      fontWeight = FontWeight.Bold,
      color = PhonePeGreen,
      textAlign = TextAlign.Center
    )
  }
}

@Composable
private fun RecentTxnRow(
  txn: TransactionEntity,
  onClick: () -> Unit
) {
  val isDebit = txn.type == "DEBIT"
  val dateFormatted = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(txn.timestamp))

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .padding(vertical = 4.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(
      modifier = Modifier.weight(1f),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(if (isDebit) PhonePePurpleContainer else Color(0xFFE8F5E9)),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = txn.title.take(1).uppercase(),
          color = if (isDebit) PhonePePurple else PhonePeGreen,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(
          text = txn.title,
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold,
          color = PhonePeTextPrimary,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          text = dateFormatted,
          fontSize = 11.sp,
          color = PhonePeTextSecondary
        )
      }
    }

    Column(horizontalAlignment = Alignment.End) {
      Text(
        text = "${if (isDebit) "-" else "+"}₹${"%.2f".format(txn.amount)}",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = if (isDebit) PhonePeTextPrimary else PhonePeGreen
      )
      Text(
        text = "Successful",
        fontSize = 10.sp,
        color = PhonePeGreen,
        fontWeight = FontWeight.Medium
      )
    }
  }
}
