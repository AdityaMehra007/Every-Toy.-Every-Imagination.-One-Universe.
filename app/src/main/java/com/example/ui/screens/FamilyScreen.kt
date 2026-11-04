package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChildProfile
import com.example.model.Order
import com.example.model.SafetyRecallNotice
import com.example.model.UserRole
import com.example.ui.theme.*
import com.example.viewmodel.ToyverseViewModel

@Composable
fun FamilyScreen(
    viewModel: ToyverseViewModel,
    onOpenRoleSwitcher: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val childProfiles by viewModel.childProfiles.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val safetyNotices by viewModel.safetyNotices.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavy),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. PARENT COMMAND CENTER HEADER
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DeepCosmicNavy),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GlassBorder, RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(RoyalGoldAmber),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🛡️", fontSize = 24.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Family Command Center",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = WarmIvory
                                )
                                Text(
                                    text = "Role: ${uiState.currentRole.label}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ChampagneGold
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = onOpenRoleSwitcher,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ChampagneGold),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ChampagneGold)
                        ) {
                            Text("Switch", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Zero child behavioral profiling. Safe parental controls and full gift list coordination.",
                        style = MaterialTheme.typography.bodySmall,
                        color = StarlightMuted,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // 2. CHILD PROFILES SECTION
        item {
            Column {
                Text(
                    text = "Protected Child Profiles",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = WarmIvory
                )
                Text(
                    text = "Configured with age suitability filters and privacy shielding",
                    style = MaterialTheme.typography.bodySmall,
                    color = StarlightMuted,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    childProfiles.forEach { child ->
                        val isSelected = uiState.activeChildProfile?.id == child.id
                        Surface(
                            onClick = { viewModel.setActiveChildProfile(child) },
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) NebulaSurface else DeepCosmicNavy,
                            modifier = Modifier
                                .weight(1f)
                                .border(
                                    1.dp,
                                    if (isSelected) ChampagneGold else GlassBorder,
                                    RoundedCornerShape(16.dp)
                                )
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(child.avatarEmoji, fontSize = 28.sp)
                                    if (isSelected) {
                                        Text("ACTIVE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ChampagneGold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = child.nickname,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = WarmIvory
                                )
                                Text(
                                    text = "Age: ${child.ageYears} yrs",
                                    fontSize = 11.sp,
                                    color = ChampagneGold
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = child.favoriteInterests.joinToString(", "),
                                    fontSize = 10.sp,
                                    color = StarlightMuted,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. TOY BUDGET & SPENDING TRACKER
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DeepCosmicNavy),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GlassBorder, RoundedCornerShape(18.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Monthly Play Budget Tracker",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = WarmIvory
                        )
                        Text(
                            text = "$124.98 of $200.00 Spent",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ChampagneGold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { 0.62f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = ChampagneGold,
                        trackColor = NebulaSurface,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Healthy balance: $75.02 remaining for birthdays or rewards.",
                        style = MaterialTheme.typography.bodySmall,
                        color = StarlightMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // 4. ORDER HISTORY & SHIPMENT TRACKING
        item {
            Column {
                Text(
                    text = "Family Order History & Tracking",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = WarmIvory
                )
                Text(
                    text = "Real-time dispatch, packing inspection, and courier updates",
                    style = MaterialTheme.typography.bodySmall,
                    color = StarlightMuted,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }
        }

        items(orders) { order ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DeepCosmicNavy),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Order #${order.orderId}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = WarmIvory
                        )
                        Text(
                            text = order.dateFormatted,
                            style = MaterialTheme.typography.bodySmall,
                            color = StarlightMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    order.items.forEach { cartItem ->
                        Text(
                            text = "• ${cartItem.quantity}x ${cartItem.toy.name}",
                            fontSize = 12.sp,
                            color = WarmIvory
                        )
                        if (cartItem.giftMessage.isNotBlank()) {
                            Text(
                                text = "  🎁 Gift note: \"${cartItem.giftMessage}\"",
                                fontSize = 11.sp,
                                color = ChampagneGold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = EmeraldGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "🚚 ${order.status}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGreen,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Text(
                            text = "Total: $${"%.2f".format(order.totalAmount)}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Black,
                            color = ChampagneGold
                        )
                    }
                }
            }
        }

        // 5. TOY SAFETY & OFFICIAL RECALL FEED
        item {
            Column {
                Text(
                    text = "Toy Safety & Official Recall Feed",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = WarmIvory
                )
                Text(
                    text = "Live compliance cross-checks with CPSC, EU Safety Gate, and BIS standards",
                    style = MaterialTheme.typography.bodySmall,
                    color = StarlightMuted,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }
        }

        items(safetyNotices) { notice ->
            Surface(
                color = NebulaSurface,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (notice.noticeType == "CLEAN_RECORD") EmeraldGreen.copy(alpha = 0.4f) else CoralPink.copy(alpha = 0.4f),
                        RoundedCornerShape(14.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = notice.issuingBody,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ChampagneGold
                        )
                        Text(
                            text = notice.dateUpdated,
                            fontSize = 10.sp,
                            color = StarlightMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = notice.productTitle,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = WarmIvory
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = notice.details,
                        style = MaterialTheme.typography.bodySmall,
                        color = StarlightMuted,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Recommendation: ${notice.actionRequired}",
                        style = MaterialTheme.typography.bodySmall,
                        color = EmeraldGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
