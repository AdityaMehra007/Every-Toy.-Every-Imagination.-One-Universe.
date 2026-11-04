package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import com.example.model.LearningActivity
import com.example.model.PrelovedListing
import com.example.model.CollectorItem
import com.example.ui.theme.*
import com.example.viewmodel.ToyverseViewModel

enum class PlayLearnSubTab(val label: String, val iconEmoji: String) {
    ACTIVITIES("STEM Activities", "🔬"),
    KID_ZONE("Kid Playground", "🎠"),
    CIRCULAR("Pre-Loved & Reuse", "♻️"),
    COLLECTOR("Collector's Vault", "🏆")
}

@Composable
fun PlayLearnScreen(
    viewModel: ToyverseViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val activities by viewModel.learningActivities.collectAsState()
    val prelovedListings by viewModel.prelovedListings.collectAsState()
    val collectorVault by viewModel.collectorVault.collectAsState()
    val childProfiles by viewModel.childProfiles.collectAsState()

    var activeSubTab by remember {
        mutableStateOf(if (uiState.isKidPlaygroundActive) PlayLearnSubTab.KID_ZONE else PlayLearnSubTab.ACTIVITIES)
    }

    var selectedActivityDetail by remember { mutableStateOf<LearningActivity?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavy)
    ) {
        // Sub-Tab Switcher
        Surface(
            color = DeepCosmicNavy,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PlayLearnSubTab.values().forEach { tab ->
                    val isSelected = activeSubTab == tab
                    Surface(
                        onClick = {
                            activeSubTab = tab
                            if (tab == PlayLearnSubTab.KID_ZONE) {
                                viewModel.setKidPlaygroundActive(true)
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) RoyalGoldAmber else NebulaSurface,
                        modifier = Modifier.border(
                            1.dp,
                            if (isSelected) ChampagneGold else GlassBorder,
                            RoundedCornerShape(12.dp)
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(tab.iconEmoji, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = tab.label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) ChampagneGold else WarmIvory
                            )
                        }
                    }
                }
            }
        }

        // Sub-Tab Contents
        when (activeSubTab) {
            PlayLearnSubTab.ACTIVITIES -> {
                ActivitiesContent(
                    activities = activities,
                    onSelectActivity = { selectedActivityDetail = it }
                )
            }
            PlayLearnSubTab.KID_ZONE -> {
                KidPlaygroundContent(
                    childProfile = uiState.activeChildProfile ?: childProfiles.firstOrNull(),
                    onExitKidMode = {
                        viewModel.setKidPlaygroundActive(false)
                        activeSubTab = PlayLearnSubTab.ACTIVITIES
                    }
                )
            }
            PlayLearnSubTab.CIRCULAR -> {
                CircularPlayContent(
                    listings = prelovedListings,
                    onAddNewListing = { viewModel.setAddPrelovedOpen(true) }
                )
            }
            PlayLearnSubTab.COLLECTOR -> {
                CollectorVaultContent(
                    items = collectorVault,
                    onAddNewItem = { viewModel.setAddCollectorOpen(true) }
                )
            }
        }
    }

    // Activity Detail Dialog
    selectedActivityDetail?.let { act ->
        AlertDialog(
            onDismissRequest = { selectedActivityDetail = null },
            containerColor = DeepCosmicNavy,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🚀", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = act.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = WarmIvory
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Age: ${act.ageRange}", color = ChampagneGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("⏱️ ${act.durationMinutes} mins", color = StarlightMuted, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Materials Needed:", fontWeight = FontWeight.Bold, color = WarmIvory, fontSize = 13.sp)
                    act.materialsNeeded.forEach { mat ->
                        Text("• $mat", color = StarlightMuted, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Step-by-Step Instructions:", fontWeight = FontWeight.Bold, color = WarmIvory, fontSize = 13.sp)
                    act.stepByStepInstructions.forEach { step ->
                        Text(step, color = WarmIvory, fontSize = 12.sp, modifier = Modifier.padding(vertical = 2.dp))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = MidnightNavy,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, CoralPink.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                    ) {
                        Text(
                            text = "⚠️ Safety: ${act.safetyPrecautions}",
                            fontSize = 11.sp,
                            color = CoralPink,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedActivityDetail = null },
                    colors = ButtonDefaults.buttonColors(containerColor = ChampagneGold, contentColor = MidnightNavy)
                ) {
                    Text("Got It! Start Playing", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
fun ActivitiesContent(
    activities: List<LearningActivity>,
    onSelectActivity: (LearningActivity) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Learn Through Play Studio",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = WarmIvory
                )
                Text(
                    text = "Screen-free challenges and science activities that transform curiosity into invention.",
                    style = MaterialTheme.typography.bodySmall,
                    color = StarlightMuted
                )
            }
        }

        items(activities) { act ->
            Card(
                onClick = { onSelectActivity(act) },
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
                        Surface(
                            color = NebulaSurface,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = act.category,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ChampagneGold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            color = MidnightNavy,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Badge: ${act.rewardBadge}",
                                fontSize = 11.sp,
                                color = EmeraldGreen,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = act.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = WarmIvory
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = act.learningGoals,
                        style = MaterialTheme.typography.bodySmall,
                        color = StarlightMuted,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⏱️ ${act.durationMinutes} min • ${act.ageRange}",
                            fontSize = 11.sp,
                            color = WarmIvory
                        )

                        Text(
                            text = "View Step-by-Step Instructions →",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ChampagneGold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun KidPlaygroundContent(
    childProfile: com.example.model.ChildProfile?,
    onExitKidMode: () -> Unit
) {
    var storyPromptIndex by remember { mutableStateOf(0) }
    val stories = listOf(
        "A friendly robotic puppy builds a treehouse on Mars with magnetic bricks...",
        "A dinosaur detective uses a magnifying glass to find the lost golden puzzle piece...",
        "A fleet of toy submarines explores an ocean trench made of glowing blue blocks..."
    )

    var triviaAnswered by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Kid Safety Top Banner
        item {
            Surface(
                color = DeepCosmicViolet,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, RoyalCosmicPurple, RoundedCornerShape(18.dp))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(childProfile?.avatarEmoji ?: "🚀", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Welcome, Explorer ${childProfile?.nickname ?: "Kid"}!",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = WarmIvory
                            )
                            Text(
                                text = "🛡️ Supervised Child-Safe Playground Mode Active",
                                style = MaterialTheme.typography.bodySmall,
                                color = EmeraldGreen,
                                fontSize = 11.sp
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = onExitKidMode,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ChampagneGold),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ChampagneGold)
                    ) {
                        Text("Parent Exit", fontSize = 11.sp)
                    }
                }
            }
        }

        // Daily Kid Toy Trivia
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DeepCosmicNavy),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GlassBorder, RoundedCornerShape(18.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🧠", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Daily Toyverse Space Trivia",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = ChampagneGold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Question: In what year was the modern interlocking plastic toy brick patented?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmIvory
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    if (!triviaAnswered) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("1932", "1958", "1984").forEach { year ->
                                Button(
                                    onClick = { triviaAnswered = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = NebulaSurface, contentColor = WarmIvory),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(year, fontSize = 12.sp)
                                }
                            }
                        }
                    } else {
                        Surface(
                            color = EmeraldGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🎉 Correct! The iconic brick patent was filed on January 28, 1958! You earned 10 Explorer XP!",
                                color = EmeraldGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }

        // Creative Story Starter Generator
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DeepCosmicNavy),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GlassBorder, RoundedCornerShape(18.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📖", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Imaginative Story Starter",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "\"${stories[storyPromptIndex % stories.size]}\"",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = PaleGold,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { storyPromptIndex++ },
                        colors = ButtonDefaults.buttonColors(containerColor = NebulaSurface, contentColor = ChampagneGold),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("🎲 Roll New Story Idea", fontSize = 12.sp)
                    }
                }
            }
        }

        // Explorer Badges Showcase
        item {
            Text(
                text = "Explorer Badges Earned",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = WarmIvory
            )
        }

        items(childProfile?.badgesEarned ?: listOf("Star Navigator", "Junior Builder")) { badge ->
            Surface(
                color = NebulaSurface,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GlassBorder, RoundedCornerShape(14.dp))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(RoyalGoldAmber),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🏅", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = badge,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = WarmIvory
                        )
                        Text(
                            text = "Awarded for creative building and collaborative curiosity",
                            style = MaterialTheme.typography.bodySmall,
                            color = StarlightMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CircularPlayContent(
    listings: List<PrelovedListing>,
    onAddNewListing: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Pre-Loved & Circular Play",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = WarmIvory
                    )
                    Text(
                        text = "Give high-quality toys a second life with verified safety inspection.",
                        style = MaterialTheme.typography.bodySmall,
                        color = StarlightMuted
                    )
                }

                Button(
                    onClick = onAddNewListing,
                    colors = ButtonDefaults.buttonColors(containerColor = ChampagneGold, contentColor = MidnightNavy),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("+ List a Toy", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        items(listings) { listing ->
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
                        Surface(
                            color = EmeraldGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "🛡️ Verified Safe Resale",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGreen,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Text(
                            text = listing.condition,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ChampagneGold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = listing.toyTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = WarmIvory
                    )

                    Text(
                        text = "Brand: ${listing.brand} • Located in: ${listing.sellerCity}",
                        style = MaterialTheme.typography.bodySmall,
                        color = StarlightMuted,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = listing.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = WarmIvory,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "$${"%.2f".format(listing.price)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = ChampagneGold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "orig $${"%.2f".format(listing.originalRetail)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = StarlightMuted,
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                            )
                        }

                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = NebulaSurface, contentColor = WarmIvory),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Contact Seller", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CollectorVaultContent(
    items: List<CollectorItem>,
    onAddNewItem: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Collectors' Elite Vault",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = WarmIvory
                    )
                    Text(
                        text = "Catalog rare editions, verified condition grading, and historical valuations.",
                        style = MaterialTheme.typography.bodySmall,
                        color = StarlightMuted
                    )
                }

                Button(
                    onClick = onAddNewItem,
                    colors = ButtonDefaults.buttonColors(containerColor = ChampagneGold, contentColor = MidnightNavy),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("+ Vault Item", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        items(items) { item ->
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
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Year ${item.releaseYear} • ${item.edition}",
                            fontSize = 11.sp,
                            color = StarlightMuted
                        )
                        Surface(
                            color = RoyalGoldAmber,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = item.conditionGrade,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ChampagneGold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = WarmIvory
                    )
                    Text(
                        text = "Manufacturer: ${item.manufacturer}",
                        style = MaterialTheme.typography.bodySmall,
                        color = StarlightMuted
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Estimated Market Value", fontSize = 10.sp, color = StarlightMuted)
                            Text(
                                text = "$${"%.2f".format(item.estimatedValue)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = ChampagneGold
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("📜", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "COA Authenticated",
                                fontSize = 11.sp,
                                color = EmeraldGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
