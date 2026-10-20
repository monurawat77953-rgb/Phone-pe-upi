package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TransactionEntity
import com.example.ui.theme.PhonePeGreen
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.PhonePePurpleContainer
import com.example.ui.theme.PhonePeTextPrimary
import com.example.ui.theme.PhonePeTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
  transactions: List<TransactionEntity>,
  onSelectTransaction: (TransactionEntity) -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedFilter by remember { mutableStateOf("All") }

  val filterOptions = listOf("All", "Paid", "Received", "Recharges & Bills", "Cashbacks")

  val filteredTransactions = remember(transactions, searchQuery, selectedFilter) {
    transactions.filter { txn ->
      val matchesSearch = searchQuery.isBlank() ||
          txn.title.contains(searchQuery, ignoreCase = true) ||
          txn.recipientUpiId.contains(searchQuery, ignoreCase = true) ||
          txn.utrNumber.contains(searchQuery) ||
          txn.debitedFrom.contains(searchQuery, ignoreCase = true)

      val matchesCategory = when (selectedFilter) {
        "Paid" -> txn.type == "DEBIT"
        "Received" -> txn.type == "CREDIT"
        "Recharges & Bills" -> txn.category in listOf("RECHARGE", "BILL")
        "Cashbacks" -> txn.category == "CASHBACK"
        else -> true
      }

      matchesSearch && matchesCategory
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF7F8FA))
  ) {
    // Header & Search
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(14.dp)
    ) {
      Text(
        text = "Transaction History",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = PhonePeTextPrimary
      )

      Spacer(modifier = Modifier.height(10.dp))

      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Search by name, UPI ID or UTR...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = PhonePePurple) },
        singleLine = true,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("history_search_input"),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
          focusedContainerColor = Color(0xFFF5F6F8),
          unfocusedContainerColor = Color(0xFFF5F6F8),
          focusedIndicatorColor = PhonePePurple
        )
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Filter Chips
      LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(filterOptions) { filter ->
          val isSelected = selectedFilter == filter
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(18.dp))
              .background(if (isSelected) PhonePePurple else Color(0xFFF0F1F5))
              .clickable { selectedFilter = filter }
              .padding(horizontal = 14.dp, vertical = 6.dp)
              .testTag("history_filter_$filter")
          ) {
            Text(
              text = filter,
              color = if (isSelected) Color.White else PhonePeTextPrimary,
              fontSize = 12.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
          }
        }
      }
    }

    // Transactions Feed
    if (filteredTransactions.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(Icons.Default.History, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(54.dp))
          Spacer(modifier = Modifier.height(12.dp))
          Text("No transactions found", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PhonePeTextPrimary)
          Text("Try changing search term or filter", fontSize = 12.sp, color = PhonePeTextSecondary)
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(filteredTransactions, key = { it.id }) { txn ->
          val isDebit = txn.type == "DEBIT"
          val formattedDate = remember(txn.timestamp) {
            SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(txn.timestamp))
          }

          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onSelectTransaction(txn) }
              .testTag("history_item_${txn.id}"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (isDebit) PhonePePurpleContainer else Color(0xFFE8F5E9)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = if (isDebit) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                    contentDescription = null,
                    tint = if (isDebit) PhonePePurple else PhonePeGreen,
                    modifier = Modifier.size(22.dp)
                  )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                  Text(
                    text = txn.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = PhonePeTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                  Text(
                    text = formattedDate,
                    fontSize = 11.sp,
                    color = PhonePeTextSecondary
                  )
                  Text(
                    text = txn.debitedFrom,
                    fontSize = 11.sp,
                    color = PhonePeTextSecondary.copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                }
              }

              Column(horizontalAlignment = Alignment.End) {
                Text(
                  text = "${if (isDebit) "-" else "+"}₹${"%.2f".format(txn.amount)}",
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Black,
                  color = if (isDebit) PhonePeTextPrimary else PhonePeGreen
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.CheckCircle, contentDescription = null, tint = PhonePeGreen, modifier = Modifier.size(12.dp))
                  Spacer(modifier = Modifier.width(3.dp))
                  Text(
                    text = "Successful",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PhonePeGreen
                  )
                }
              }
            }
          }
        }

        item {
          Spacer(modifier = Modifier.height(80.dp))
        }
      }
    }
  }
}
