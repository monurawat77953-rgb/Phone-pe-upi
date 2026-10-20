package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.PhonePePurpleContainer
import com.example.ui.theme.PhonePeTextMuted
import com.example.ui.theme.PhonePeTextPrimary
import com.example.ui.viewmodel.MainTab

@Composable
fun PhonePeBottomNav(
  currentTab: MainTab,
  onTabSelected: (MainTab) -> Unit,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    modifier = modifier.testTag("phonepe_bottom_nav"),
    containerColor = Color.White,
    tonalElevation = 6.dp,
    windowInsets = WindowInsets.navigationBars
  ) {
    // 1. Home
    NavigationBarItem(
      selected = currentTab == MainTab.HOME,
      onClick = { onTabSelected(MainTab.HOME) },
      icon = {
        Icon(
          imageVector = if (currentTab == MainTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
          contentDescription = "Home",
          modifier = Modifier.size(24.dp)
        )
      },
      label = {
        Text(
          text = "Home",
          fontSize = 11.sp,
          fontWeight = if (currentTab == MainTab.HOME) FontWeight.Bold else FontWeight.Medium
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = PhonePePurple,
        selectedTextColor = PhonePePurple,
        unselectedIconColor = PhonePeTextMuted,
        unselectedTextColor = PhonePeTextMuted,
        indicatorColor = PhonePePurpleContainer
      ),
      modifier = Modifier.testTag("nav_item_home")
    )

    // 2. Stores
    NavigationBarItem(
      selected = currentTab == MainTab.STORES,
      onClick = { onTabSelected(MainTab.STORES) },
      icon = {
        Icon(
          imageVector = if (currentTab == MainTab.STORES) Icons.Filled.Storefront else Icons.Outlined.Storefront,
          contentDescription = "Stores",
          modifier = Modifier.size(24.dp)
        )
      },
      label = {
        Text(
          text = "Stores",
          fontSize = 11.sp,
          fontWeight = if (currentTab == MainTab.STORES) FontWeight.Bold else FontWeight.Medium
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = PhonePePurple,
        selectedTextColor = PhonePePurple,
        unselectedIconColor = PhonePeTextMuted,
        unselectedTextColor = PhonePeTextMuted,
        indicatorColor = PhonePePurpleContainer
      ),
      modifier = Modifier.testTag("nav_item_stores")
    )

    // 3. Insurance
    NavigationBarItem(
      selected = currentTab == MainTab.INSURANCE,
      onClick = { onTabSelected(MainTab.INSURANCE) },
      icon = {
        Icon(
          imageVector = if (currentTab == MainTab.INSURANCE) Icons.Filled.Security else Icons.Outlined.Security,
          contentDescription = "Insurance",
          modifier = Modifier.size(24.dp)
        )
      },
      label = {
        Text(
          text = "Insurance",
          fontSize = 11.sp,
          fontWeight = if (currentTab == MainTab.INSURANCE) FontWeight.Bold else FontWeight.Medium
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = PhonePePurple,
        selectedTextColor = PhonePePurple,
        unselectedIconColor = PhonePeTextMuted,
        unselectedTextColor = PhonePeTextMuted,
        indicatorColor = PhonePePurpleContainer
      ),
      modifier = Modifier.testTag("nav_item_insurance")
    )

    // 4. Wealth
    NavigationBarItem(
      selected = currentTab == MainTab.WEALTH,
      onClick = { onTabSelected(MainTab.WEALTH) },
      icon = {
        Icon(
          imageVector = if (currentTab == MainTab.WEALTH) Icons.Filled.TrendingUp else Icons.Outlined.TrendingUp,
          contentDescription = "Wealth",
          modifier = Modifier.size(24.dp)
        )
      },
      label = {
        Text(
          text = "Wealth",
          fontSize = 11.sp,
          fontWeight = if (currentTab == MainTab.WEALTH) FontWeight.Bold else FontWeight.Medium
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = PhonePePurple,
        selectedTextColor = PhonePePurple,
        unselectedIconColor = PhonePeTextMuted,
        unselectedTextColor = PhonePeTextMuted,
        indicatorColor = PhonePePurpleContainer
      ),
      modifier = Modifier.testTag("nav_item_wealth")
    )

    // 5. History
    NavigationBarItem(
      selected = currentTab == MainTab.HISTORY,
      onClick = { onTabSelected(MainTab.HISTORY) },
      icon = {
        Icon(
          imageVector = if (currentTab == MainTab.HISTORY) Icons.Filled.History else Icons.Outlined.History,
          contentDescription = "History",
          modifier = Modifier.size(24.dp)
        )
      },
      label = {
        Text(
          text = "History",
          fontSize = 11.sp,
          fontWeight = if (currentTab == MainTab.HISTORY) FontWeight.Bold else FontWeight.Medium
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = PhonePePurple,
        selectedTextColor = PhonePePurple,
        unselectedIconColor = PhonePeTextMuted,
        unselectedTextColor = PhonePeTextMuted,
        indicatorColor = PhonePePurpleContainer
      ),
      modifier = Modifier.testTag("nav_item_history")
    )
  }
}
