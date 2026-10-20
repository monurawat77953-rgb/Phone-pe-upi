package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.BankAccountEntity
import com.example.data.local.ContactEntity
import com.example.data.model.PaymentRequest
import com.example.data.model.PaymentSourceType
import com.example.data.model.UserProfile
import com.example.ui.theme.PhonePeGreen
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.PhonePePurpleContainer
import com.example.ui.theme.PhonePeTextPrimary
import com.example.ui.theme.PhonePeTextSecondary

@Composable
fun SendMoneyScreen(
  contacts: List<ContactEntity>,
  bankAccounts: List<BankAccountEntity>,
  userProfile: UserProfile,
  initialRecipient: ContactEntity?,
  initialUpiOrPhone: String,
  onBack: () -> Unit,
  onProceedPayment: (PaymentRequest) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedContact by remember { mutableStateOf(initialRecipient) }
  var searchQuery by remember { mutableStateOf(initialUpiOrPhone) }
  var amountText by remember { mutableStateOf("") }
  var noteText by remember { mutableStateOf("") }
  var selectedSourceType by remember { mutableStateOf(PaymentSourceType.BANK_ACCOUNT) }
  var selectedBankId by remember {
    mutableStateOf(bankAccounts.firstOrNull { it.isPrimary }?.id ?: bankAccounts.firstOrNull()?.id ?: "")
  }

  val isRecipientSelected = selectedContact != null || searchQuery.isNotBlank()
  val parsedAmount = amountText.toDoubleOrNull() ?: 0.0

  val filteredContacts = remember(searchQuery, contacts) {
    if (searchQuery.isBlank()) contacts
    else contacts.filter {
      it.name.contains(searchQuery, ignoreCase = true) ||
          it.phone.contains(searchQuery) ||
          it.upiId.contains(searchQuery, ignoreCase = true)
    }
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
      // Top AppBar
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(PhonePePurple)
          .padding(horizontal = 10.dp, vertical = 8.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = onBack,
            modifier = Modifier.testTag("send_money_back_button")
          ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
          Text(
            text = "To Mobile Number / UPI ID",
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      if (selectedContact == null) {
        // Step 1: Select Contact or Enter Mobile/UPI
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Enter mobile number, name, or UPI ID") },
            leadingIcon = {
              Icon(Icons.Default.Search, contentDescription = null, tint = PhonePePurple)
            },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("search_recipient_input"),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
              focusedContainerColor = Color.White,
              unfocusedContainerColor = Color.White,
              focusedIndicatorColor = PhonePePurple
            )
          )

          if (searchQuery.isNotBlank() && filteredContacts.none { it.phone == searchQuery || it.upiId.equals(searchQuery, ignoreCase = true) }) {
            Spacer(modifier = Modifier.height(10.dp))
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  selectedContact = ContactEntity(
                    id = "custom",
                    name = if (searchQuery.contains("@")) searchQuery.substringBefore("@") else "User ($searchQuery)",
                    phone = if (searchQuery.contains("@")) "" else searchQuery,
                    upiId = if (searchQuery.contains("@")) searchQuery else "$searchQuery@ybl",
                    avatarColorHex = "#5F259F"
                  )
                },
              colors = CardDefaults.cardColors(containerColor = Color.White),
              shape = RoundedCornerShape(12.dp)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(PhonePePurpleContainer),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.Person, contentDescription = null, tint = PhonePePurple)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Text(
                    text = "Pay to \"$searchQuery\"",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PhonePePurple
                  )
                  Text(
                    text = if (searchQuery.contains("@")) "UPI ID" else "New Mobile Transfer",
                    fontSize = 11.sp,
                    color = PhonePeTextSecondary
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = "RECENT CONTACTS & UPI",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = PhonePeTextSecondary,
            letterSpacing = 1.sp
          )
        }

        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(horizontal = 16.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(filteredContacts, key = { it.id }) { contact ->
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { selectedContact = contact }
                .testTag("contact_item_${contact.id}"),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              shape = RoundedCornerShape(12.dp)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                val avatarColor = try {
                  Color(android.graphics.Color.parseColor(contact.avatarColorHex))
                } catch (_: Exception) {
                  PhonePePurple
                }
                Box(
                  modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(avatarColor),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = contact.name.take(1).uppercase(),
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = contact.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = PhonePeTextPrimary
                  )
                  Text(
                    text = contact.upiId.ifBlank { contact.phone },
                    fontSize = 12.sp,
                    color = PhonePeTextSecondary
                  )
                }
                Text(
                  text = "PAY",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = PhonePePurple
                )
              }
            }
          }
        }
      } else {
        // Step 2: Recipient is selected -> Enter Amount & Choose Account
        val recipient = selectedContact!!
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          // Recipient Card
          item {
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(PhonePePurple),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = recipient.name.take(1).uppercase(),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = recipient.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PhonePeTextPrimary
                  )
                  Text(
                    text = recipient.upiId.ifBlank { recipient.phone },
                    fontSize = 12.sp,
                    color = PhonePeTextSecondary
                  )
                }
                Text(
                  text = "Change",
                  color = PhonePePurple,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.SemiBold,
                  modifier = Modifier.clickable { selectedContact = null }
                )
              }
            }
          }

          // Amount Input Field
          item {
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(16.dp)
              ) {
                Text(
                  text = "Enter Amount",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = PhonePeTextSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                  value = amountText,
                  onValueChange = { input ->
                    if (input.all { it.isDigit() || it == '.' }) {
                      amountText = input
                    }
                  },
                  prefix = {
                    Text("₹", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = PhonePeTextPrimary)
                  },
                  placeholder = { Text("0", fontSize = 28.sp) },
                  keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                  singleLine = true,
                  textStyle = androidx.compose.ui.text.TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold),
                  modifier = Modifier
                    .fillMaxWidth()
                    .testTag("amount_input_field"),
                  shape = RoundedCornerShape(12.dp),
                  colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF9F9FC),
                    unfocusedContainerColor = Color(0xFFF9F9FC),
                    focusedIndicatorColor = PhonePePurple
                  )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Quick Amount Chips
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  listOf(100, 500, 1000, 2000).forEach { quickAmount ->
                    Box(
                      modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF0F1F5))
                        .clickable { amountText = quickAmount.toString() }
                        .padding(vertical = 8.dp)
                        .testTag("quick_amount_$quickAmount"),
                      contentAlignment = Alignment.Center
                    ) {
                      Text(
                        text = "+₹$quickAmount",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PhonePeTextPrimary
                      )
                    }
                  }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Optional Message / Note
                OutlinedTextField(
                  value = noteText,
                  onValueChange = { noteText = it },
                  placeholder = { Text("Add a note (e.g. Dinner, Rent)") },
                  singleLine = true,
                  modifier = Modifier
                    .fillMaxWidth()
                    .testTag("payment_note_input"),
                  shape = RoundedCornerShape(10.dp),
                  colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF9F9FC),
                    unfocusedContainerColor = Color(0xFFF9F9FC),
                    focusedIndicatorColor = PhonePePurple
                  )
                )
              }
            }
          }

          // Debited Account Selector
          item {
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(16.dp)
              ) {
                Text(
                  text = "Debit From",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = PhonePeTextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Bank Accounts List
                bankAccounts.forEach { account ->
                  val isSelected = selectedSourceType == PaymentSourceType.BANK_ACCOUNT && selectedBankId == account.id
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .clip(RoundedCornerShape(10.dp))
                      .background(if (isSelected) Color(0xFFF3E8FC) else Color.Transparent)
                      .clickable {
                        selectedSourceType = PaymentSourceType.BANK_ACCOUNT
                        selectedBankId = account.id
                      }
                      .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(
                      Icons.Default.AccountBalance,
                      contentDescription = null,
                      tint = if (isSelected) PhonePePurple else Color.Gray,
                      modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                      Text(
                        text = "${account.bankName} ••${account.accountNumberLast4}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PhonePeTextPrimary
                      )
                      Text(
                        text = "A/c Balance: ₹${"%.2f".format(account.balance)}",
                        fontSize = 11.sp,
                        color = PhonePeTextSecondary
                      )
                    }
                    if (isSelected) {
                      Icon(Icons.Default.CheckCircle, contentDescription = "Selected", tint = PhonePePurple)
                    }
                  }
                }

                Divider(color = Color(0xFFEEEEEE), modifier = Modifier.padding(vertical = 8.dp))

                // UPI Lite Option
                val isUpiLiteSelected = selectedSourceType == PaymentSourceType.UPI_LITE
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isUpiLiteSelected) Color(0xFFF3E8FC) else Color.Transparent)
                    .clickable {
                      selectedSourceType = PaymentSourceType.UPI_LITE
                    }
                    .padding(10.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    Icons.Default.FlashOn,
                    contentDescription = null,
                    tint = PhonePeGreen,
                    modifier = Modifier.size(24.dp)
                  )
                  Spacer(modifier = Modifier.width(12.dp))
                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = "UPI Lite (Pin-less under ₹500)",
                      fontSize = 14.sp,
                      fontWeight = FontWeight.SemiBold,
                      color = PhonePeTextPrimary
                    )
                    Text(
                      text = "Balance: ₹${"%.2f".format(userProfile.upiLiteBalance)}",
                      fontSize = 11.sp,
                      color = PhonePeTextSecondary
                    )
                  }
                  if (isUpiLiteSelected) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Selected", tint = PhonePePurple)
                  }
                }

                // PhonePe Wallet Option
                val isWalletSelected = selectedSourceType == PaymentSourceType.WALLET
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isWalletSelected) Color(0xFFF3E8FC) else Color.Transparent)
                    .clickable {
                      selectedSourceType = PaymentSourceType.WALLET
                    }
                    .padding(10.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    tint = PhonePePurple,
                    modifier = Modifier.size(24.dp)
                  )
                  Spacer(modifier = Modifier.width(12.dp))
                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = "PhonePe Wallet",
                      fontSize = 14.sp,
                      fontWeight = FontWeight.SemiBold,
                      color = PhonePeTextPrimary
                    )
                    Text(
                      text = "Balance: ₹${"%.2f".format(userProfile.walletBalance)}",
                      fontSize = 11.sp,
                      color = PhonePeTextSecondary
                    )
                  }
                  if (isWalletSelected) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Selected", tint = PhonePePurple)
                  }
                }
              }
            }
          }
        }

        // Bottom Proceed / Pay Button
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
        ) {
          Button(
            onClick = {
              if (parsedAmount > 0) {
                onProceedPayment(
                  PaymentRequest(
                    recipientName = recipient.name,
                    recipientUpiOrPhone = recipient.upiId.ifBlank { recipient.phone },
                    amount = parsedAmount,
                    note = noteText,
                    category = "TRANSFER",
                    source = selectedSourceType,
                    selectedBankAccountId = selectedBankId
                  )
                )
              }
            },
            enabled = parsedAmount > 0,
            modifier = Modifier
              .fillMaxWidth()
              .height(52.dp)
              .testTag("proceed_payment_button"),
            colors = ButtonDefaults.buttonColors(
              containerColor = PhonePePurple,
              disabledContainerColor = Color(0xFFD1C4E9)
            ),
            shape = RoundedCornerShape(14.dp)
          ) {
            Text(
              text = if (parsedAmount > 0) "PAY ₹${"%.2f".format(parsedAmount)}" else "ENTER AMOUNT",
              color = Color.White,
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}
