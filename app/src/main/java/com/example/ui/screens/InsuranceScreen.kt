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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PaymentRequest
import com.example.ui.theme.PhonePeAmber
import com.example.ui.theme.PhonePeGreen
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.PhonePePurpleContainer
import com.example.ui.theme.PhonePePurpleDark
import com.example.ui.theme.PhonePeTextPrimary
import com.example.ui.theme.PhonePeTextSecondary

data class InsurancePlan(
  val id: String,
  val title: String,
  val subtitle: String,
  val priceTag: String,
  val amount: Double,
  val icon: androidx.compose.ui.graphics.vector.ImageVector,
  val features: List<String>
)

@Composable
fun InsuranceScreen(
  onBuyInsurance: (PaymentRequest) -> Unit,
  modifier: Modifier = Modifier
) {
  val insurancePlans = listOf(
    InsurancePlan(
      id = "bike_ins",
      title = "Bike Insurance",
      subtitle = "Comprehensive & Third Party Cover",
      priceTag = "Starting at ₹499/year",
      amount = 549.0,
      icon = Icons.Default.TwoWheeler,
      features = listOf("Instant policy in 2 mins", "Zero paperwork required", "Cashless repairs in 5000+ garages")
    ),
    InsurancePlan(
      id = "car_ins",
      title = "Car Insurance",
      subtitle = "Save up to 85% with Zero Dep",
      priceTag = "Starting at ₹2,094/year",
      amount = 2399.0,
      icon = Icons.Default.DirectionsCar,
      features = listOf("No vehicle inspection", "24x7 Roadside assistance", "Instant digital policy on PhonePe")
    ),
    InsurancePlan(
      id = "health_ins",
      title = "1 Crore Health Insurance",
      subtitle = "Complete Medical & Hospital Protection",
      priceTag = "From ₹390/month",
      amount = 390.0,
      icon = Icons.Default.HealthAndSafety,
      features = listOf("Cashless claim in 30 mins", "No room rent capping", "Covers pre & post hospitalization")
    ),
    InsurancePlan(
      id = "term_life",
      title = "Term Life Insurance",
      subtitle = "₹1 Crore Financial Security for Family",
      priceTag = "From ₹450/month",
      amount = 450.0,
      icon = Icons.Default.VolunteerActivism,
      features = listOf("Tax benefit up to ₹46,800", "Critical illness cover", "100% claim settlement guarantee")
    )
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF7F8FA))
  ) {
    // Header Banner
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.verticalGradient(listOf(PhonePePurple, PhonePePurpleDark))
        )
        .padding(20.dp)
    ) {
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Security, contentDescription = null, tint = PhonePeAmber, modifier = Modifier.size(24.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "PhonePe Insurance Broking",
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Trusted by 5+ Crore Indians. 100% IRDAI licensed.",
          color = Color.White.copy(alpha = 0.9f),
          fontSize = 12.sp
        )
      }
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      items(insurancePlans, key = { it.id }) { plan ->
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
                    .background(Color(0xFFE8F1FC)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(plan.icon, contentDescription = null, tint = Color(0xFF1976D2), modifier = Modifier.size(26.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Text(
                    text = plan.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PhonePeTextPrimary
                  )
                  Text(
                    text = plan.subtitle,
                    fontSize = 12.sp,
                    color = PhonePeTextSecondary
                  )
                }
              }

              Text(
                text = plan.priceTag,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = PhonePeGreen
              )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bullet points
            plan.features.forEach { feat ->
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 2.dp)
              ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = PhonePeGreen, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(feat, fontSize = 12.sp, color = PhonePeTextPrimary)
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
              onClick = {
                onBuyInsurance(
                  PaymentRequest(
                    recipientName = "PhonePe Insurance (${plan.title})",
                    recipientUpiOrPhone = "insurance@phonepe",
                    amount = plan.amount,
                    note = "Policy issuance for ${plan.title}",
                    category = "BILL"
                  )
                )
              },
              modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .testTag("buy_insurance_${plan.id}"),
              colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
              shape = RoundedCornerShape(10.dp)
            ) {
              Text("Get Policy Now (₹${plan.amount.toInt()})", fontWeight = FontWeight.Bold)
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
