package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Toy
import com.example.model.UserRole
import com.example.ui.theme.*
import com.example.viewmodel.AppNavTab

@Composable
fun ToyverseHeader(
    currentRole: UserRole,
    cartCount: Int,
    compareCount: Int,
    onRoleClick: () -> Unit,
    onCartClick: () -> Unit,
    onCompareClick: () -> Unit
) {
    Surface(
        color = DeepCosmicNavy,
        tonalElevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = GlassBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Mark
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onRoleClick() }
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(listOf(ChampagneGold, RoyalCosmicPurple))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🚀", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "TOYVERSE",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = WarmIvory,
                            letterSpacing = 1.2.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "ELITE",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = ChampagneGold,
                            modifier = Modifier
                                .background(RoyalGoldAmber, RoundedCornerShape(4.dp))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = currentRole.badge,
                        style = MaterialTheme.typography.bodySmall,
                        color = StarlightMuted,
                        fontSize = 11.sp
                    )
                }
            }

            // Right Action Controls
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Role badge pill
                AssistChip(
                    onClick = onRoleClick,
                    label = { Text(currentRole.name.take(6), fontSize = 11.sp) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = "Switch Role",
                            modifier = Modifier.size(16.dp),
                            tint = ChampagneGold
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = NebulaSurface,
                        labelColor = WarmIvory
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
                    modifier = Modifier.padding(end = 6.dp)
                )

                // Comparison badge if items selected
                if (compareCount > 0) {
                    IconButton(
                        onClick = onCompareClick,
                        modifier = Modifier
                            .size(40.dp)
                            .background(NebulaSurface, CircleShape)
                            .border(1.dp, ChampagneGold, CircleShape)
                    ) {
                        BadgedBox(
                            badge = {
                                Badge(containerColor = ChampagneGold, contentColor = MidnightNavy) {
                                    Text("$compareCount")
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.CompareArrows,
                                contentDescription = "Compare Toys",
                                tint = ChampagneGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                }

                // Cart badge
                IconButton(
                    onClick = onCartClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(NebulaSurface, CircleShape)
                        .border(1.dp, GlassBorder, CircleShape)
                ) {
                    BadgedBox(
                        badge = {
                            if (cartCount > 0) {
                                Badge(containerColor = CoralPink, contentColor = Color.White) {
                                    Text("$cartCount")
                                }
                            }
                        }
                    ) {
                        Icon(
                            Icons.Outlined.ShoppingCart,
                            contentDescription = "Shopping Cart",
                            tint = WarmIvory,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ToyCard(
    toy: Toy,
    isWishlisted: Boolean,
    isCompared: Boolean,
    onToyClick: () -> Unit,
    onWishlistToggle: () -> Unit,
    onCompareToggle: () -> Unit,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onToyClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DeepCosmicNavy),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isCompared) ChampagneGold else GlassBorder,
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Visual Showcase Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(toy.accentColorHex).copy(alpha = 0.25f),
                                NebulaSurface
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Top Tag Badges
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Category / Age pill
                    Surface(
                        color = MidnightNavy.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "${toy.category.emoji} ${toy.ageRange}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ChampagneGold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // Wishlist Button
                    IconButton(
                        onClick = onWishlistToggle,
                        modifier = Modifier
                            .size(32.dp)
                            .background(MidnightNavy.copy(alpha = 0.7f), CircleShape)
                    ) {
                        Icon(
                            imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isWishlisted) CoralPink else StarlightMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Toy Main Emoji Showcase
                Text(
                    text = toy.iconEmoji,
                    fontSize = 58.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )

                // Safety tag pill bottom
                Surface(
                    color = MidnightNavy.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("🛡️", fontSize = 10.sp)
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = toy.safetyCertifications.firstOrNull() ?: "Verified",
                            fontSize = 10.sp,
                            color = EmeraldGreen,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Brand & Verified Badge
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = toy.brand,
                    style = MaterialTheme.typography.labelSmall,
                    color = StarlightMuted,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    Icons.Default.Verified,
                    contentDescription = "Verified Brand",
                    tint = ElectricCyan,
                    modifier = Modifier.size(12.dp)
                )
            }

            // Toy Title
            Text(
                text = toy.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = WarmIvory,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Rating & Reviews
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text("★", color = ChampagneGold, fontSize = 13.sp)
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "${toy.rating} (${toy.reviewCount})",
                    fontSize = 12.sp,
                    color = WarmIvory,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "• ${toy.supervisionLevel.take(12)}",
                    fontSize = 11.sp,
                    color = StarlightMuted
                )
            }

            // Price & Action Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "$${"%.2f".format(toy.price)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = ChampagneGold
                        )
                        if (toy.originalPrice != null && toy.originalPrice > toy.price) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "$${"%.2f".format(toy.originalPrice)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = StarlightMuted,
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                            )
                        }
                    }
                }

                Row {
                    // Compare Toggle
                    IconButton(
                        onClick = onCompareToggle,
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                if (isCompared) RoyalGoldAmber else NebulaSurface,
                                CircleShape
                            )
                            .border(
                                1.dp,
                                if (isCompared) ChampagneGold else GlassBorder,
                                CircleShape
                            )
                    ) {
                        Icon(
                            Icons.Default.CompareArrows,
                            contentDescription = "Compare",
                            tint = if (isCompared) ChampagneGold else StarlightMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Add to Cart button
                    IconButton(
                        onClick = onAddToCart,
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                Brush.linearGradient(listOf(ChampagneGold, RoyalGoldAmber)),
                                CircleShape
                            )
                    ) {
                        Icon(
                            Icons.Default.AddShoppingCart,
                            contentDescription = "Add to Cart",
                            tint = MidnightNavy,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ToyverseBottomBar(
    selectedTab: AppNavTab,
    onTabSelected: (AppNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = DeepCosmicNavy,
        tonalElevation = 8.dp,
        windowInsets = WindowInsets.navigationBars,
        modifier = modifier.border(width = 1.dp, color = GlassBorder)
    ) {
        AppNavTab.values().forEach { tab ->
            val isSelected = selectedTab == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Text(
                        text = tab.iconEmoji,
                        fontSize = if (isSelected) 22.sp else 19.sp
                    )
                },
                label = {
                    Text(
                        text = tab.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) ChampagneGold else StarlightMuted
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = RoyalGoldAmber,
                    selectedIconColor = ChampagneGold,
                    unselectedIconColor = StarlightMuted
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleSelectorBottomSheet(
    currentRole: UserRole,
    onRoleSelected: (UserRole) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DeepCosmicNavy,
        dragHandle = { BottomSheetDefaults.DragHandle(color = GlassBorder) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .navigationBarsPadding()
        ) {
            Text(
                text = "Switch Toyverse Ecosystem Persona",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = WarmIvory
            )
            Text(
                text = "Experience tailored interfaces built for every member of the toy world.",
                style = MaterialTheme.typography.bodyMedium,
                color = StarlightMuted,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            UserRole.values().forEach { role ->
                val isSelected = role == currentRole
                Card(
                    onClick = {
                        onRoleSelected(role)
                        onDismiss()
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) NebulaSurface else MidnightNavy
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) ChampagneGold else GlassBorder,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = {
                                onRoleSelected(role)
                                onDismiss()
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = ChampagneGold,
                                unselectedColor = StarlightMuted
                            )
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = role.label,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) ChampagneGold else WarmIvory
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = role.badge,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color = CoralPink,
                                    modifier = Modifier
                                        .background(CoralPink.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = role.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = StarlightMuted
                            )
                        }
                    }
                }
            }
        }
    }
}
