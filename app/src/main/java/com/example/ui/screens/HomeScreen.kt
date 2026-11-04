package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Toy
import com.example.model.ToyCategory
import com.example.ui.components.ToyCard
import com.example.ui.theme.*
import com.example.viewmodel.AppNavTab
import com.example.viewmodel.ToyverseViewModel

@Composable
fun HomeScreen(
    viewModel: ToyverseViewModel,
    onNavigateTab: (AppNavTab) -> Unit,
    onOpenSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val allToys by viewModel.toys.collectAsState()
    val wishlist by viewModel.wishlist.collectAsState()
    val comparedToyIds by viewModel.comparedToyIds.collectAsState()

    val trendingToys = remember(allToys) { allToys.take(4) }
    val stemToys = remember(allToys) { allToys.filter { it.category == ToyCategory.STEM || it.category == ToyCategory.BUILDING } }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavy),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. GRAND HERO SECTION
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                DeepCosmicViolet,
                                NebulaSurface,
                                DeepCosmicNavy
                            )
                        )
                    )
                    .border(1.dp, GlassBorder, RoundedCornerShape(24.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Tagline badge
                    Surface(
                        color = MidnightNavy.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.border(1.dp, ChampagneGold.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("✨", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "EVERY TOY. EVERY IMAGINATION. ONE UNIVERSE.",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ChampagneGold,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Welcome to a Universe Built for Play.",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = WarmIvory,
                        lineHeight = 32.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Discover extraordinary toys, unlock creativity, and find the perfect safe play experience for every age, interest, and developmental stage.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = StarlightMuted,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Intelligent Search Field
                    Surface(
                        onClick = onOpenSearch,
                        shape = RoundedCornerShape(16.dp),
                        color = MidnightNavy.copy(alpha = 0.9f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                tint = ChampagneGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Search any toy, brand, age, STEM, or interest...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = StarlightMuted,
                                fontSize = 13.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = NebulaSurface
                            ) {
                                Text(
                                    text = "ORBIT AI",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricCyan,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quick Action Shortcut Chips
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        HeroActionChip("✨ Ask ORBIT AI") { onNavigateTab(AppNavTab.ORBIT_AI) }
                        HeroActionChip("🧸 Browse Catalog") { onNavigateTab(AppNavTab.DIRECTORY) }
                        HeroActionChip("🎨 Kid Playground") {
                            viewModel.setKidPlaygroundActive(true)
                            onNavigateTab(AppNavTab.PLAY_LEARN)
                        }
                        HeroActionChip("♻️ Pre-Loved & Rentals") { onNavigateTab(AppNavTab.DIRECTORY) }
                    }
                }
            }
        }

        // 2. EXPLORE BY CATEGORY HORIZONTAL ROW
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                SectionHeader(
                    title = "Explore Toy Galaxies",
                    subtitle = "Find curated collections crafted for every play style",
                    actionLabel = "View All"
                ) {
                    onNavigateTab(AppNavTab.DIRECTORY)
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(ToyCategory.values()) { cat ->
                        CategoryTile(
                            category = cat,
                            isSelected = uiState.selectedCategory == cat,
                            onClick = {
                                viewModel.setCategoryFilter(if (uiState.selectedCategory == cat) null else cat)
                                onNavigateTab(AppNavTab.DIRECTORY)
                            }
                        )
                    }
                }
            }
        }

        // 3. AGE & DEVELOPMENTAL STAGE RAIL
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                SectionHeader(
                    title = "Shop by Developmental Stage",
                    subtitle = "Age-appropriate safety and cognitive development matched",
                    actionLabel = null
                ) {}

                Spacer(modifier = Modifier.height(10.dp))

                val ageStages = listOf(
                    "0-2 yrs" to "Sensory & First Steps",
                    "3-5 yrs" to "Pre-K & Imagination",
                    "6-8 yrs" to "Early STEM & Social",
                    "9-12 yrs" to "Advanced Building & Logic",
                    "13+ yrs" to "Collectors & Complex Sets"
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(ageStages) { (age, desc) ->
                        Surface(
                            onClick = {
                                viewModel.setAgeFilter(age)
                                onNavigateTab(AppNavTab.DIRECTORY)
                            },
                            shape = RoundedCornerShape(16.dp),
                            color = NebulaSurface,
                            modifier = Modifier
                                .width(160.dp)
                                .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = age,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ChampagneGold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = desc,
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

        // 4. TRENDING TOYS & DISCOVERIES
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                SectionHeader(
                    title = "Trending Universe Discoveries",
                    subtitle = "Parent-vetted favorites with stellar safety & engagement ratings",
                    actionLabel = "See All (${allToys.size})"
                ) {
                    onNavigateTab(AppNavTab.DIRECTORY)
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        items(trendingToys) { toy ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                ToyCard(
                    toy = toy,
                    isWishlisted = wishlist.contains(toy.id),
                    isCompared = comparedToyIds.contains(toy.id),
                    onToyClick = { viewModel.openToyDetail(toy) },
                    onWishlistToggle = { viewModel.toggleWishlist(toy.id) },
                    onCompareToggle = { viewModel.toggleCompare(toy.id) },
                    onAddToCart = { viewModel.addToCart(toy) }
                )
            }
        }

        // 5. CIRCULAR PLAY & SUSTAINABILITY BANNER
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DeepCosmicViolet),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(1.dp, GlassBorder, RoundedCornerShape(20.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(EmeraldGreen.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("♻️", fontSize = 28.sp)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Circular Play & Pre-Loved Hub",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = WarmIvory
                        )
                        Text(
                            text = "Buy verified pre-loved toys, rent play kits, or donate outgrown toys to local children's libraries.",
                            style = MaterialTheme.typography.bodySmall,
                            color = StarlightMuted,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }

        // 6. ROBOTICS & STEM DISCOVERY CAROUSEL
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                SectionHeader(
                    title = "STEM, Robotics & Coding",
                    subtitle = "Hands-on engineering that builds real-world spatial & algorithmic intuition",
                    actionLabel = "Explore"
                ) {
                    viewModel.setCategoryFilter(ToyCategory.STEM)
                    onNavigateTab(AppNavTab.DIRECTORY)
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        items(stemToys) { toy ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                ToyCard(
                    toy = toy,
                    isWishlisted = wishlist.contains(toy.id),
                    isCompared = comparedToyIds.contains(toy.id),
                    onToyClick = { viewModel.openToyDetail(toy) },
                    onWishlistToggle = { viewModel.toggleWishlist(toy.id) },
                    onCompareToggle = { viewModel.toggleCompare(toy.id) },
                    onAddToCart = { viewModel.addToCart(toy) }
                )
            }
        }

        // 7. TOY SAFETY & RECALL INTELLIGENCE PROMISE
        item {
            Surface(
                color = DeepCosmicNavy,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(1.dp, GlassBorder, RoundedCornerShape(20.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🛡️", fontSize = 32.sp)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "100% CPSC & EU Safety Audit Verified",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreen
                        )
                        Text(
                            text = "Every listed toy is cross-referenced with global recall databases, Reese's Law battery standards, and non-toxic material certifications.",
                            style = MaterialTheme.typography.bodySmall,
                            color = StarlightMuted,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HeroActionChip(
    label: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = NebulaSurface,
        modifier = Modifier.border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = WarmIvory,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String,
    actionLabel: String?,
    onActionClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = WarmIvory
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = StarlightMuted,
                fontSize = 12.sp
            )
        }
        if (actionLabel != null) {
            TextButton(onClick = onActionClick) {
                Text(
                    text = actionLabel,
                    color = ChampagneGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun CategoryTile(
    category: ToyCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) RoyalGoldAmber else NebulaSurface,
        modifier = Modifier
            .width(120.dp)
            .border(
                1.dp,
                if (isSelected) ChampagneGold else GlassBorder,
                RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(category.emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = category.title,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected) ChampagneGold else WarmIvory,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}
