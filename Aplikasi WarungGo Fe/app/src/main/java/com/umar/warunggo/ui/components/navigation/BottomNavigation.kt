package com.umar.warunggo.ui.components.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umar.warunggo.ui.theme.WarungGoTheme

/**
 * Bottom Navigation Item Data Class
 */
data class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

/**
 * WarungGo Bottom Navigation Bar Component
 * 5 menu utama: Dashboard, Produk, Transaksi, Laporan, Profil
 * 
 * Features:
 * - Sticky bottom navigation
 * - Active page highlight
 * - Smooth animations
 * - Icon + label text
 * 
 * @param selectedRoute Route yang sedang aktif
 * @param onNavigate Callback untuk navigasi
 */
@Composable
fun WarungGoBottomNavigation(
    selectedRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val navItems = remember {
        listOf(
            BottomNavItem(
                route = "dashboard",
                label = "Dashboard",
                selectedIcon = Icons.Filled.Dashboard,
                unselectedIcon = Icons.Outlined.Dashboard
            ),
            BottomNavItem(
                route = "product",
                label = "Produk",
                selectedIcon = Icons.Filled.Inventory,
                unselectedIcon = Icons.Outlined.Inventory2
            ),
            BottomNavItem(
                route = "transaction",
                label = "Transaksi",
                selectedIcon = Icons.Filled.Receipt,
                unselectedIcon = Icons.Outlined.ReceiptLong
            ),
            BottomNavItem(
                route = "report",
                label = "Laporan",
                selectedIcon = Icons.Filled.Assessment,
                unselectedIcon = Icons.Outlined.Assessment
            ),
            BottomNavItem(
                route = "profile",
                label = "Profil",
                selectedIcon = Icons.Filled.AccountCircle,
                unselectedIcon = Icons.Outlined.AccountCircle
            )
        )
    }
    
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        navItems.forEach { item ->
            val isSelected = selectedRoute == item.route
            
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = {
                    // Animated icon transition
                    AnimatedContent(
                        targetState = isSelected,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(300)) togetherWith
                            fadeOut(animationSpec = tween(300))
                        },
                        label = "icon_animation"
                    ) { selected ->
                        Icon(
                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = {
                    // Animated text
                    AnimatedContent(
                        targetState = isSelected,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(300)) togetherWith
                            fadeOut(animationSpec = tween(300))
                        },
                        label = "label_animation"
                    ) { selected ->
                        Text(
                            text = item.label,
                            fontSize = 12.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1
                        )
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

/**
 * Compact version - untuk landscape atau tablet mode
 */
@Composable
fun CompactWarungGoBottomNavigation(
    selectedRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val navItems = remember {
        listOf(
            BottomNavItem(
                route = "dashboard",
                label = "Dashboard",
                selectedIcon = Icons.Filled.Dashboard,
                unselectedIcon = Icons.Outlined.Dashboard
            ),
            BottomNavItem(
                route = "product",
                label = "Produk",
                selectedIcon = Icons.Filled.Inventory,
                unselectedIcon = Icons.Outlined.Inventory2
            ),
            BottomNavItem(
                route = "transaction",
                label = "Transaksi",
                selectedIcon = Icons.Filled.Receipt,
                unselectedIcon = Icons.Outlined.ReceiptLong
            ),
            BottomNavItem(
                route = "report",
                label = "Laporan",
                selectedIcon = Icons.Filled.Assessment,
                unselectedIcon = Icons.Outlined.Assessment
            ),
            BottomNavItem(
                route = "profile",
                label = "Profil",
                selectedIcon = Icons.Filled.AccountCircle,
                unselectedIcon = Icons.Outlined.AccountCircle
            )
        )
    }
    
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        navItems.forEach { item ->
            val isSelected = selectedRoute == item.route
            
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        modifier = Modifier.size(20.dp)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                ),
                alwaysShowLabel = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WarungGoBottomNavigationPreview() {
    WarungGoTheme {
        var selectedRoute by remember { mutableStateOf("dashboard") }
        WarungGoBottomNavigation(
            selectedRoute = selectedRoute,
            onNavigate = { selectedRoute = it }
        )
    }
}

@Preview(showBackground = true, widthDp = 720)
@Composable
private fun CompactWarungGoBottomNavigationPreview() {
    WarungGoTheme {
        var selectedRoute by remember { mutableStateOf("transaction") }
        CompactWarungGoBottomNavigation(
            selectedRoute = selectedRoute,
            onNavigate = { selectedRoute = it }
        )
    }
}
