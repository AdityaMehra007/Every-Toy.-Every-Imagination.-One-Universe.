package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.RoleSelectorBottomSheet
import com.example.ui.components.ToyverseBottomBar
import com.example.ui.components.ToyverseHeader
import com.example.ui.dialogs.*
import com.example.ui.screens.*
import com.example.ui.theme.MidnightNavy
import com.example.ui.theme.ToyverseEliteTheme
import com.example.viewmodel.AppNavTab
import com.example.viewmodel.ToyverseViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToyverseEliteTheme {
                ToyverseApp()
            }
        }
    }
}

@Composable
fun ToyverseApp(
    viewModel: ToyverseViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val cartCount by viewModel.cartCount.collectAsState()
    val comparedToyIds by viewModel.comparedToyIds.collectAsState()
    val allToys by viewModel.toys.collectAsState()

    var isRoleSelectorOpen by remember { mutableStateOf(false) }

    // Predictive back / BackHandler handling
    BackHandler(enabled = uiState.selectedToyForDetail != null ||
            uiState.isCompareDialogOpen ||
            uiState.isCartSheetOpen ||
            uiState.isCheckoutDialogOpen ||
            uiState.isAddPrelovedOpen ||
            uiState.isAddCollectorOpen ||
            uiState.selectedTab != AppNavTab.UNIVERSE
    ) {
        when {
            uiState.selectedToyForDetail != null -> viewModel.openToyDetail(null)
            uiState.isCompareDialogOpen -> viewModel.setCompareDialogOpen(false)
            uiState.isCartSheetOpen -> viewModel.setCartSheetOpen(false)
            uiState.isCheckoutDialogOpen -> viewModel.setCheckoutDialogOpen(false)
            uiState.isAddPrelovedOpen -> viewModel.setAddPrelovedOpen(false)
            uiState.isAddCollectorOpen -> viewModel.setAddCollectorOpen(false)
            uiState.selectedTab != AppNavTab.UNIVERSE -> viewModel.selectTab(AppNavTab.UNIVERSE)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightNavy),
        topBar = {
            ToyverseHeader(
                currentRole = uiState.currentRole,
                cartCount = cartCount,
                compareCount = comparedToyIds.size,
                onRoleClick = { isRoleSelectorOpen = true },
                onCartClick = { viewModel.setCartSheetOpen(true) },
                onCompareClick = { viewModel.setCompareDialogOpen(true) }
            )
        },
        bottomBar = {
            ToyverseBottomBar(
                selectedTab = uiState.selectedTab,
                onTabSelected = { viewModel.selectTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(
                targetState = uiState.selectedTab,
                label = "ToyverseScreenTransition"
            ) { tab ->
                when (tab) {
                    AppNavTab.UNIVERSE -> {
                        HomeScreen(
                            viewModel = viewModel,
                            onNavigateTab = { viewModel.selectTab(it) },
                            onOpenSearch = { viewModel.selectTab(AppNavTab.DIRECTORY) }
                        )
                    }
                    AppNavTab.DIRECTORY -> {
                        CatalogScreen(
                            viewModel = viewModel
                        )
                    }
                    AppNavTab.ORBIT_AI -> {
                        OrbitAiScreen(
                            viewModel = viewModel
                        )
                    }
                    AppNavTab.PLAY_LEARN -> {
                        PlayLearnScreen(
                            viewModel = viewModel
                        )
                    }
                    AppNavTab.FAMILY_HUB -> {
                        FamilyScreen(
                            viewModel = viewModel,
                            onOpenRoleSwitcher = { isRoleSelectorOpen = true }
                        )
                    }
                }
            }
        }
    }

    // Modal Overlays
    uiState.selectedToyForDetail?.let { toy ->
        val wishlist by viewModel.wishlist.collectAsState()
        ProductDetailBottomSheet(
            toy = toy,
            isWishlisted = wishlist.contains(toy.id),
            isCompared = comparedToyIds.contains(toy.id),
            onDismiss = { viewModel.openToyDetail(null) },
            onWishlistToggle = { viewModel.toggleWishlist(toy.id) },
            onAddToCart = { giftWrap -> viewModel.addToCart(toy, giftWrap) },
            onCompareToggle = { viewModel.toggleCompare(toy.id) }
        )
    }

    if (uiState.isCompareDialogOpen) {
        val comparedToys = remember(comparedToyIds, allToys) {
            allToys.filter { comparedToyIds.contains(it.id) }
        }
        ComparisonDialog(
            comparedToys = comparedToys,
            onDismiss = { viewModel.setCompareDialogOpen(false) },
            onRemoveToy = { viewModel.toggleCompare(it) }
        )
    }

    if (uiState.isCartSheetOpen) {
        CartAndCheckoutSheet(
            viewModel = viewModel,
            onDismiss = { viewModel.setCartSheetOpen(false) }
        )
    }

    if (uiState.isCheckoutDialogOpen) {
        CheckoutConfirmationDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.setCheckoutDialogOpen(false) }
        )
    }

    if (uiState.isAddPrelovedOpen) {
        AddPrelovedDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.setAddPrelovedOpen(false) }
        )
    }

    if (uiState.isAddCollectorOpen) {
        AddCollectorDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.setAddCollectorOpen(false) }
        )
    }

    if (isRoleSelectorOpen) {
        RoleSelectorBottomSheet(
            currentRole = uiState.currentRole,
            onRoleSelected = { viewModel.setRole(it) },
            onDismiss = { isRoleSelectorOpen = false }
        )
    }
}
