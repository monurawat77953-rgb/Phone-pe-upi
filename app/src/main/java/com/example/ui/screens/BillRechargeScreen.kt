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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TimeToLeave
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PaymentRequest
import com.example.data.model.RechargePlan
import com.example.ui.theme.PhonePeAmber
import com.example.ui.theme.PhonePeGreen
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.PhonePePurpleContainer
import com.example.ui.theme.PhonePeTextPrimary
import com.example.ui.theme.PhonePeTextSecondary

data class BillTabItem(
  val id: String,
  val title: String,
  val icon: ImageVector
)

@Composable
fun BillRechargeScreen(
  initialCategory: String,
  onBack: () -> Unit,
  onPayBill: (PaymentRequest) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableStateOf(initialCategory) }

  val tabs = listOf(
    BillTabItem("MOBILE", "Mobile", Icons.Default.PhoneAndroid),
    BillTabItem("ELECTRICITY", "Electricity", Icons.Default.ElectricBolt),
    BillTabItem("DTH", "DTH", Icons.Default.LiveTv),
    BillTabItem("FASTAG", "FASTag", Icons.Default.TimeToLeave),
    BillTabItem("CREDIT_CARD", "Credit Card", Icons.Default.CreditCard)
  )

  var mobileNumber by remember { mutableStateOf("9876543210") }
  var selectedOperator by remember { mutableStateOf("Jio Prepaid") }
  var consumerId by remember { mutableStateOf("CA-100482910") }
  var electricityBoard by remember { mutableStateOf("BESCOM - Bengaluru") }

  val mobilePlans = listOf(
    RechargePlan("p1", 299.0, "28 Days", "1.5 GB/day", "Unlimited Calls + 100 SMS/day", "TRENDING"),
    RechargePlan("p2", 349.0, "28 Days", "2.0 GB/day", "Unlimited Calls + Hero Unlimited 5G", "BEST VALUE"),
    RechargePlan("p3", 666.0, "84 Days", "1.5 GB/day", "Unlimited Calls + Disney+ Hotstar Mobile", "POPULAR"),
    RechargePlan("p4", 2999.0, "365 Days", "2.5 GB/day", "Unlimited Calls + Prime Video 1 Year", "ANNUAL")
  )

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
      // Top Bar
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(PhonePePurple)
          .padding(horizontal = 10.dp, vertical = 8.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = onBack,
            modifier = Modifier.testTag("bill_recharge_back_button")
          ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
          Text(
            text = "Recharge & Pay Bills",
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      // Category Horizontal Tabs
      LazyRow(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color.White)
          .padding(vertical = 10.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(tabs) { tab ->
          val isSelected = selectedTab == tab.id
          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(if (isSelected) PhonePePurple else Color(0xFFF0F1F5))
              .clickable { selectedTab = tab.id }
              .padding(horizontal = 14.dp, vertical = 8.dp)
              .testTag("bill_tab_${tab.id}"),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = tab.icon,
              contentDescription = null,
              tint = if (isSelected) Color.White else PhonePeTextPrimary,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = tab.title,
              color = if (isSelected) Color.White else PhonePeTextPrimary,
              fontSize = 13.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
          }
        }
      }

      // Content based on selected Tab
      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        when (selectedTab) {
          "MOBILE" -> {
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
                  Text("Enter Mobile Number", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PhonePeTextSecondary)
                  Spacer(modifier = Modifier.height(8.dp))
                  OutlinedTextField(
                    value = mobileNumber,
                    onValueChange = { mobileNumber = it },
                    prefix = { Text("+91 ", fontWeight = FontWeight.Bold) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                  )

                  Spacer(modifier = Modifier.height(12.dp))

                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                  ) {
                    listOf("Jio Prepaid", "Airtel Prepaid", "Vi Prepaid", "BSNL").forEach { op ->
                      Box(
                        modifier = Modifier
                          .weight(1f)
                          .clip(RoundedCornerShape(8.dp))
                          .background(if (selectedOperator == op) Color(0xFFF3E8FC) else Color(0xFFF5F6F8))
                          .border(
                            1.dp,
                            if (selectedOperator == op) PhonePePurple else Color(0xFFE0E0E0),
                            RoundedCornerShape(8.dp)
                          )
                          .clickable { selectedOperator = op }
                          .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                      ) {
                        Text(
                          text = op.substringBefore(" "),
                          fontSize = 11.sp,
                          fontWeight = FontWeight.Bold,
                          color = if (selectedOperator == op) PhonePePurple else PhonePeTextPrimary
                        )
                      }
                    }
                  }
                }
              }
            }

            item {
              Text(
                text = "POPULAR RECHARGE PLANS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = PhonePeTextSecondary,
                letterSpacing = 1.sp
              )
            }

            items(mobilePlans, key = { it.id }) { plan ->
              Card(
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable {
                    onPayBill(
                      PaymentRequest(
                        recipientName = "$selectedOperator Recharge ($mobileNumber)",
                        recipientUpiOrPhone = mobileNumber,
                        amount = plan.price,
                        note = "${plan.validity} • ${plan.data}",
                        category = "RECHARGE"
                      )
                    )
                  }
                  .testTag("plan_card_${plan.id}"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
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
                      text = "₹${plan.price.toInt()}",
                      fontSize = 22.sp,
                      fontWeight = FontWeight.Black,
                      color = PhonePeTextPrimary
                    )
                    if (plan.tag.isNotBlank()) {
                      Box(
                        modifier = Modifier
                          .clip(RoundedCornerShape(6.dp))
                          .background(Color(0xFFE8F5E9))
                          .padding(horizontal = 8.dp, vertical = 4.dp)
                      ) {
                        Text(plan.tag, color = PhonePeGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                      }
                    }
                  }

                  Spacer(modifier = Modifier.height(8.dp))

                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                  ) {
                    Text("Validity: ${plan.validity}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = PhonePePurple)
                    Text("Data: ${plan.data}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = PhonePeTextPrimary)
                  }

                  Text(
                    text = plan.voice,
                    fontSize = 11.sp,
                    color = PhonePeTextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                  )

                  Spacer(modifier = Modifier.height(12.dp))

                  Button(
                    onClick = {
                      onPayBill(
                        PaymentRequest(
                          recipientName = "$selectedOperator Recharge ($mobileNumber)",
                          recipientUpiOrPhone = mobileNumber,
                          amount = plan.price,
                          note = "${plan.validity} • ${plan.data}",
                          category = "RECHARGE"
                        )
                      )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
                    shape = RoundedCornerShape(10.dp)
                  ) {
                    Text("Recharge ₹${plan.price.toInt()}", fontWeight = FontWeight.Bold)
                  }
                }
              }
            }
          }

          "ELECTRICITY" -> {
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
                  Text("Electricity Board", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PhonePeTextSecondary)
                  Spacer(modifier = Modifier.height(6.dp))
                  Text(electricityBoard, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PhonePePurple)

                  Spacer(modifier = Modifier.height(14.dp))

                  Text("Consumer Account ID", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PhonePeTextSecondary)
                  Spacer(modifier = Modifier.height(6.dp))
                  OutlinedTextField(
                    value = consumerId,
                    onValueChange = { consumerId = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                  )

                  Spacer(modifier = Modifier.height(18.dp))

                  // Bill preview
                  Box(
                    modifier = Modifier
                      .fillMaxWidth()
                      .clip(RoundedCornerShape(12.dp))
                      .background(Color(0xFFF9F9FC))
                      .padding(14.dp)
                  ) {
                    Column {
                      Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                      ) {
                        Text("Bill Due Amount", fontSize = 13.sp, color = PhonePeTextSecondary)
                        Text("₹1,840.00", fontSize = 20.sp, fontWeight = FontWeight.Black, color = PhonePeTextPrimary)
                      }
                      Text("Due Date: 12 Oct 2026", fontSize = 11.sp, color = Color(0xFFD32F2F), modifier = Modifier.padding(top = 4.dp))
                    }
                  }

                  Spacer(modifier = Modifier.height(18.dp))

                  Button(
                    onClick = {
                      onPayBill(
                        PaymentRequest(
                          recipientName = "$electricityBoard Electricity Bill",
                          recipientUpiOrPhone = consumerId,
                          amount = 1840.00,
                          note = "Consumer No: $consumerId",
                          category = "BILL"
                        )
                      )
                    },
                    modifier = Modifier
                      .fillMaxWidth()
                      .height(50.dp)
                      .testTag("pay_electricity_bill_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
                    shape = RoundedCornerShape(12.dp)
                  ) {
                    Text("Pay Bill ₹1,840.00", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                  }
                }
              }
            }
          }

          else -> {
            // Other billers (DTH, FASTag, Credit Card)
            item {
              Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
              ) {
                Column(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                  horizontalAlignment = Alignment.CenterHorizontally
                ) {
                  Icon(
                    imageVector = when (selectedTab) {
                      "DTH" -> Icons.Default.LiveTv
                      "FASTAG" -> Icons.Default.TimeToLeave
                      else -> Icons.Default.CreditCard
                    },
                    contentDescription = null,
                    tint = PhonePePurple,
                    modifier = Modifier.size(48.dp)
                  )

                  Spacer(modifier = Modifier.height(12.dp))

                  Text(
                    text = when (selectedTab) {
                      "DTH" -> "DTH Recharge"
                      "FASTAG" -> "FASTag Recharge"
                      else -> "Credit Card Bill Payment"
                    },
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = PhonePeTextPrimary
                  )

                  Text(
                    text = "Instant BBPS confirmed payment with zero convenience fees",
                    fontSize = 12.sp,
                    color = PhonePeTextSecondary,
                    modifier = Modifier.padding(vertical = 6.dp)
                  )

                  Spacer(modifier = Modifier.height(14.dp))

                  Button(
                    onClick = {
                      val billAmount = if (selectedTab == "FASTAG") 500.0 else 999.0
                      val billTitle = when (selectedTab) {
                        "DTH" -> "Tata Play DTH Recharge"
                        "FASTAG" -> "Paytm FASTag Recharge (KA-01-MJ-2024)"
                        else -> "HDFC Credit Card Bill Payment"
                      }
                      onPayBill(
                        PaymentRequest(
                          recipientName = billTitle,
                          recipientUpiOrPhone = "BBPS-PAYMENT",
                          amount = billAmount,
                          note = "BBPS Bill Clearance",
                          category = "BILL"
                        )
                      )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
                    shape = RoundedCornerShape(12.dp)
                  ) {
                    Text(
                      text = if (selectedTab == "FASTAG") "Recharge ₹500" else "Pay ₹999",
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
