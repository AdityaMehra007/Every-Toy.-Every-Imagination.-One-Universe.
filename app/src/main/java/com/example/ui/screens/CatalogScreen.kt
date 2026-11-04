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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ToyCategory
import com.example.ui.components.ToyCard
import com.example.ui.theme.*
import com.example.viewmodel.ToyverseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    viewModel: ToyverseViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val filteredToys by viewModel.filteredToys.collectAsState()
    val wishlist by viewModel.wishlist.collectAsState()
    val comparedToyIds by viewModel.comparedToyIds.collectAsState()

    var showFilterSheet by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavy)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Search & Filter Header
            Surface(
                color = DeepCosmicNavy,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GlassBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Search Bar
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        placeholder = {
                            Text(
                                "Search toys, brands, STEM, materials...",
                                color = StarlightMuted,
                                fontSize = 13.sp
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = ChampagneGold)
                        },
                        trailingIcon = {
                            if (uiState.searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear", tint = StarlightMuted)
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ChampagneGold,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = WarmIvory,
                            unfocusedTextColor = WarmIvory,
                            cursorColor = ChampagneGold
                        ),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Categories Horizontal Filter Chips
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = uiState.selectedCategory == null,
                            onClick = { viewModel.setCategoryFilter(null) },
                            label = { Text("All Categories", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = RoyalGoldAmber,
                                selectedLabelColor = ChampagneGold,
                                containerColor = NebulaSurface,
                                labelColor = StarlightMuted
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = if (uiState.selectedCategory == null) ChampagneGold else GlassBorder,
                                enabled = true,
                                selected = uiState.selectedCategory == null
                            )
                        )

                        ToyCategory.values().forEach { cat ->
                            val isSelected = uiState.selectedCategory == cat
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    viewModel.setCategoryFilter(if (isSelected) null else cat)
                                },
                                label = { Text("${cat.emoji} ${cat.title}", fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = RoyalGoldAmber,
                                    selectedLabelColor = ChampagneGold,
                                    containerColor = NebulaSurface,
                                    labelColor = StarlightMuted
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    borderColor = if (isSelected) ChampagneGold else GlassBorder,
                                    enabled = true,
                                    selected = isSelected
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Secondary Age & Sort row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Quick age chips
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("All", "0-2 yrs", "3-5 yrs", "6-8 yrs", "9-12 yrs", "13+ yrs").forEach { age ->
                                val isSelected = uiState.selectedAgeFilter == age
                                Surface(
                                    onClick = { viewModel.setAgeFilter(age) },
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) NebulaSurface else Color.Transparent,
                                    modifier = Modifier.border(
                                        1.dp,
                                        if (isSelected) ChampagneGold else GlassBorder,
                                        RoundedCornerShape(8.dp)
                                    )
                                ) {
                                    Text(
                                        text = age,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) ChampagneGold else StarlightMuted,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        // Filter settings button
                        IconButton(
                            onClick = { showFilterSheet = true },
                            modifier = Modifier
                                .size(36.dp)
                                .background(NebulaSurface, RoundedCornerShape(8.dp))
                                .border(1.dp, GlassBorder, RoundedCornerShape(8.dp))
                        ) {
                            Icon(
                                Icons.Default.Tune,
                                contentDescription = "Advanced Filters",
                                tint = ChampagneGold,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // Results count & active sort indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredToys.size} Verified Toys Found",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = WarmIvory
                )

                Text(
                    text = "Sorted by: ${uiState.sortBy}",
                    style = MaterialTheme.typography.bodySmall,
                    color = StarlightMuted
                )
            }

            // Toys List
            if (filteredToys.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🔍", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No Toys Match Your Universe Criteria",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = WarmIvory
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Try adjusting your price ceiling, age range, or search terms to uncover more verified toys.",
                            style = MaterialTheme.typography.bodySmall,
                            color = StarlightMuted,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                viewModel.setSearchQuery("")
                                viewModel.setCategoryFilter(null)
                                viewModel.setAgeFilter("All")
                                viewModel.setMaxPrice(160.0)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ChampagneGold, contentColor = MidnightNavy)
                        ) {
                            Text("Reset All Filters", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 120.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredToys) { toy ->
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
            }
        }

        // Floating Compare Bar if >= 1 toy selected
        if (comparedToyIds.isNotEmpty()) {
            Surface(
                color = DeepCosmicViolet,
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 10.dp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .border(1.dp, ChampagneGold, RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⚖️", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "${comparedToyIds.size}/3 Toys in Comparison",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = WarmIvory
                            )
                            Text(
                                text = "Compare specs, safety, and price side-by-side",
                                style = MaterialTheme.typography.bodySmall,
                                color = StarlightMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Row {
                        TextButton(onClick = { viewModel.clearCompare() }) {
                            Text("Clear", color = CoralPink, fontSize = 12.sp)
                        }
                        Button(
                            onClick = { viewModel.setCompareDialogOpen(true) },
                            colors = ButtonDefaults.buttonColors(containerColor = ChampagneGold, contentColor = MidnightNavy),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Compare", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }

    // Advanced Filter Sheet
    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
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
                    text = "Catalog Discovery Filters",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = WarmIvory
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Price Ceiling Slider
                Text(
                    text = "Maximum Price: $${"%.0f".format(uiState.maxPriceFilter)}",
                    style = MaterialTheme.typography.titleSmall,
                    color = ChampagneGold,
                    fontWeight = FontWeight.Bold
                )
                Slider(
                    value = uiState.maxPriceFilter.toFloat(),
                    onValueChange = { viewModel.setMaxPrice(it.toDouble()) },
                    valueRange = 20f..160f,
                    colors = SliderDefaults.colors(
                        thumbColor = ChampagneGold,
                        activeTrackColor = ChampagneGold,
                        inactiveTrackColor = NebulaSurface
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Sort Order
                Text(
                    text = "Sort Order",
                    style = MaterialTheme.typography.titleSmall,
                    color = WarmIvory,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                listOf("Featured", "Rating", "Price: Low to High", "Price: High to Low").forEach { sort ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.setSortBy(sort) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = uiState.sortBy == sort,
                            onClick = { viewModel.setSortBy(sort) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = ChampagneGold,
                                unselectedColor = StarlightMuted
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = sort,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (uiState.sortBy == sort) ChampagneGold else WarmIvory
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showFilterSheet = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ChampagneGold, contentColor = MidnightNavy),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Apply Filters (${filteredToys.size} Matches)", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
