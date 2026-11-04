package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OrbitMessage
import com.example.model.Toy
import com.example.ui.theme.*
import com.example.viewmodel.ToyverseViewModel
import kotlinx.coroutines.launch

@Composable
fun OrbitAiScreen(
    viewModel: ToyverseViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val orbitChat by viewModel.orbitChat.collectAsState()
    val allToys by viewModel.toys.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(orbitChat.size) {
        if (orbitChat.isNotEmpty()) {
            listState.animateScrollToItem(orbitChat.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavy)
    ) {
        // Assistant Header
        Surface(
            color = DeepCosmicNavy,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(listOf(ChampagneGold, RoyalCosmicPurple))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✨", fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "ORBIT",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = WarmIvory,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = RoyalGoldAmber,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "DISCOVERY AI",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ChampagneGold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "Safety-verified toy reasoning & gift intelligence",
                        style = MaterialTheme.typography.bodySmall,
                        color = StarlightMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Preset Suggested Queries Carousel
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NebulaSurface)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val suggestions = listOf(
                "Find 5 creative STEM toys under $45",
                "Compare robotics kit vs building set for 7yo",
                "Durable travel-friendly toy for long car journey",
                "Rainy day activity with blocks and paper",
                "Check safety & recall audit status"
            )

            suggestions.forEach { suggestion ->
                Surface(
                    onClick = {
                        viewModel.sendOrbitMessage(suggestion)
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = DeepCosmicNavy,
                    modifier = Modifier.border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = suggestion,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = PaleGold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Messages Thread
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(orbitChat) { message ->
                OrbitChatBubble(
                    message = message,
                    allToys = allToys,
                    onToyClick = { viewModel.openToyDetail(it) },
                    onAddToCart = { viewModel.addToCart(it) }
                )
            }

            if (uiState.isOrbitThinking) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        CircularProgressIndicator(
                            color = ChampagneGold,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "ORBIT is consulting the verified toy safety & catalog database...",
                            style = MaterialTheme.typography.bodySmall,
                            color = StarlightMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Bottom Input Bar
        Surface(
            color = DeepCosmicNavy,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder)
                .padding(bottom = 80.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = uiState.orbitInputText,
                    onValueChange = { viewModel.updateOrbitInput(it) },
                    placeholder = {
                        Text(
                            "Ask ORBIT: age, budget, STEM, gifts, travel...",
                            color = StarlightMuted,
                            fontSize = 13.sp
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ChampagneGold,
                        unfocusedBorderColor = GlassBorder,
                        focusedTextColor = WarmIvory,
                        unfocusedTextColor = WarmIvory,
                        cursorColor = ChampagneGold
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.weight(1f),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        viewModel.sendOrbitMessage()
                    },
                    modifier = Modifier
                        .size(46.dp)
                        .background(
                            Brush.linearGradient(listOf(ChampagneGold, RoyalCosmicPurple)),
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Send to ORBIT",
                        tint = MidnightNavy,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun OrbitChatBubble(
    message: OrbitMessage,
    allToys: List<Toy>,
    onToyClick: (Toy) -> Unit,
    onAddToCart: (Toy) -> Unit
) {
    if (message.isUser) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Surface(
                color = DeepCosmicViolet,
                shape = RoundedCornerShape(18.dp).copy(bottomEnd = CornerSize(4.dp)),
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .border(1.dp, RoyalCosmicPurple.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
            ) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarmIvory,
                    modifier = Modifier.padding(14.dp)
                )
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Column(modifier = Modifier.fillMaxWidth(0.92f)) {
                Surface(
                    color = NebulaSurface,
                    shape = RoundedCornerShape(18.dp).copy(bottomStart = CornerSize(4.dp)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, GlassBorder, RoundedCornerShape(18.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("✨", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ORBIT Intelligence",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = ChampagneGold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = message.text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = WarmIvory,
                            lineHeight = 20.sp
                        )

                        // Safety Tips box if present
                        if (message.safetyTips != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                color = MidnightNavy.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, EmeraldGreen.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text("🛡️", fontSize = 13.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = message.safetyTips,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = EmeraldGreen,
                                        fontSize = 11.sp,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Suggested Toy Cards Attached to Response
                if (message.suggestedToyIds.isNotEmpty()) {
                    val matchingToys = allToys.filter { message.suggestedToyIds.contains(it.id) }
                    Column(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        matchingToys.forEach { toy ->
                            Surface(
                                onClick = { onToyClick(toy) },
                                color = DeepCosmicNavy,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, GlassBorder, RoundedCornerShape(14.dp))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(NebulaSurface),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(toy.iconEmoji, fontSize = 24.sp)
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = toy.name,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = WarmIvory,
                                            maxLines = 1
                                        )
                                        Text(
                                            text = "${toy.ageRange} • $${"%.2f".format(toy.price)}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = ChampagneGold,
                                            fontSize = 11.sp
                                        )
                                    }

                                    IconButton(
                                        onClick = { onAddToCart(toy) },
                                        modifier = Modifier
                                            .size(34.dp)
                                            .background(RoyalGoldAmber, CircleShape)
                                    ) {
                                        Icon(
                                            Icons.Default.AddShoppingCart,
                                            contentDescription = "Add to Cart",
                                            tint = ChampagneGold,
                                            modifier = Modifier.size(16.dp)
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
