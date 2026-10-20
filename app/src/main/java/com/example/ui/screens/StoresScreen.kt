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
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ContactEntity
import com.example.data.model.StoreMerchant
import com.example.ui.theme.PhonePeAmber
import com.example.ui.theme.PhonePeGreen
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.PhonePePurpleContainer
import com.example.ui.theme.PhonePeTextPrimary
import com.example.ui.theme.PhonePeTextSecondary

@Composable
fun StoresScreen(
  onPayMerchant: (ContactEntity) -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("All") }

  val categories = listOf("All", "Groceries", "Food & Cafes", "Pharmacy", "Electronics", "Fashion")

  val merchants = listOf(
    StoreMerchant("s1", "Sharma Grocery Mart", "Groceries", "350 m away", "sharmagrocery@ybl", "Flat ₹25 cashback on ₹200+", 4.8),
    StoreMerchant("s2", "Chai Point & Snacks", "Food & Cafes", "500 m away", "chaipoint@paytm", "Pay with UPI Lite", 4.7),
    StoreMerchant("s3", "Apollo Pharmacy & Healthcare", "Pharmacy", "800 m away", "apollopharmacy@okhdfcbank", "Flat 10% OFF with PhonePe", 4.9),
    StoreMerchant("s4", "Reliance Fresh Supermarket", "Groceries", "1.1 km away", "reliancefresh@axis", "Earn 2X Scratch Cards", 4.6),
    StoreMerchant("s5", "Karnataka Sweets & Bakery", "Food & Cafes", "1.4 km away", "karnatakasweets@ybl", "Zero fees on UPI", 4.8),
    StoreMerchant("s6", "Croma Electronics Store", "Electronics", "2.0 km away", "croma.store@icici", "No cost EMI on credit card", 4.7)
  )

  val filteredMerchants = remember(searchQuery, selectedCategory) {
    merchants.filter { merchant ->
      (selectedCategory == "All" || merchant.category == selectedCategory) &&
          (searchQuery.isBlank() || merchant.name.contains(searchQuery, ignoreCase = true) || merchant.category.contains(searchQuery, ignoreCase = true))
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
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.LocationOn, contentDescription = null, tint = PhonePePurple, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "Stores in HSR Layout, Bengaluru",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = PhonePeTextPrimary
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Search stores, groceries, cafes...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = PhonePePurple) },
        singleLine = true,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("stores_search_input"),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
          focusedContainerColor = Color(0xFFF5F6F8),
          unfocusedContainerColor = Color(0xFFF5F6F8),
          focusedIndicatorColor = PhonePePurple
        )
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Category chips
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(categories) { cat ->
          val isSelected = selectedCategory == cat
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(18.dp))
              .background(if (isSelected) PhonePePurple else Color(0xFFF0F1F5))
              .clickable { selectedCategory = cat }
              .padding(horizontal = 14.dp, vertical = 6.dp)
          ) {
            Text(
              text = cat,
              color = if (isSelected) Color.White else PhonePeTextPrimary,
              fontSize = 12.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
          }
        }
      }
    }

    // Stores List
    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      items(filteredMerchants, key = { it.id }) { merchant ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
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
              verticalAlignment = Alignment.Top
            ) {
              Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(PhonePePurpleContainer),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.Storefront, contentDescription = null, tint = PhonePePurple, modifier = Modifier.size(26.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Text(
                    text = merchant.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = PhonePeTextPrimary
                  )
                  Text(
                    text = "${merchant.category} • ${merchant.distance}",
                    fontSize = 12.sp,
                    color = PhonePeTextSecondary
                  )
                }
              }

              // Rating pill
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(PhonePeGreen)
                  .padding(horizontal = 6.dp, vertical = 3.dp)
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = "${merchant.rating}",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                  )
                  Spacer(modifier = Modifier.width(2.dp))
                  Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
                }
              }
            }

            if (merchant.offer.isNotBlank()) {
              Spacer(modifier = Modifier.height(10.dp))
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(Color(0xFFFFF8E1))
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Icon(Icons.Default.LocalOffer, contentDescription = null, tint = PhonePeAmber, modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(merchant.offer, color = Color(0xFF7A4F01), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Pay Now Action
            Button(
              onClick = {
                onPayMerchant(
                  ContactEntity(
                    id = merchant.id,
                    name = merchant.name,
                    phone = "",
                    upiId = merchant.upiId,
                    avatarColorHex = "#5F259F"
                  )
                )
              },
              modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("pay_store_${merchant.id}"),
              colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
              shape = RoundedCornerShape(10.dp)
            ) {
              Text("Pay Store via UPI", fontWeight = FontWeight.Bold)
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
