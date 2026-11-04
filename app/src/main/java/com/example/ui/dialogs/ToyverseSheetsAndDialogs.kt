package com.example.ui.dialogs

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.model.Toy
import com.example.model.ToyCategory
import com.example.ui.theme.*
import com.example.viewmodel.ToyverseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailBottomSheet(
    toy: Toy,
    isWishlisted: Boolean,
    onDismiss: () -> Unit,
    onWishlistToggle: () -> Unit,
    onAddToCart: (Boolean) -> Unit,
    onCompareToggle: () -> Unit,
    isCompared: Boolean
) {
    var giftWrapSelected by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DeepCosmicNavy,
        dragHandle = { BottomSheetDefaults.DragHandle(color = GlassBorder) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .navigationBarsPadding()
        ) {
            // Hero Visual Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(toy.accentColorHex).copy(alpha = 0.35f),
                                NebulaSurface
                            )
                        )
                    )
                    .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(toy.iconEmoji, fontSize = 72.sp)

                // Top right actions
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                ) {
                    IconButton(
                        onClick = onWishlistToggle,
                        modifier = Modifier
                            .size(38.dp)
                            .background(MidnightNavy.copy(alpha = 0.8f), CircleShape)
                    ) {
                        Icon(
                            imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Wishlist",
                            tint = if (isWishlisted) CoralPink else StarlightMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Brand & Title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = toy.brand,
                    style = MaterialTheme.typography.labelMedium,
                    color = StarlightMuted,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    Icons.Default.Verified,
                    contentDescription = "Verified Brand",
                    tint = ElectricCyan,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Surface(
                    color = RoyalGoldAmber,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "AGE: ${toy.ageRange}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = ChampagneGold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = toy.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = WarmIvory
            )

            // Price & Stock
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$${"%.2f".format(toy.price)}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = ChampagneGold
                    )
                    if (toy.originalPrice != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$${"%.2f".format(toy.originalPrice)}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = StarlightMuted,
                            textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                        )
                    }
                }

                Surface(
                    color = EmeraldGreen.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "✓ In Stock (${toy.stockCount} available)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGreen,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Description
            Text(
                text = toy.description,
                style = MaterialTheme.typography.bodyMedium,
                color = WarmIvory,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Safety Certifications Card
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NebulaSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GlassBorder, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🛡️", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Verified Safety & Material Standards",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreen
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Certifications: ${toy.safetyCertifications.joinToString(" • ")}",
                        fontSize = 11.sp,
                        color = WarmIvory
                    )
                    Text(
                        text = "Materials: ${toy.materials}",
                        fontSize = 11.sp,
                        color = StarlightMuted
                    )
                    Text(
                        text = "Power: ${toy.batteryInfo}",
                        fontSize = 11.sp,
                        color = StarlightMuted
                    )
                    Text(
                        text = "Supervision: ${toy.supervisionLevel}",
                        fontSize = 11.sp,
                        color = ChampagneGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Learning Goals
            Text("Developmental & Learning Objectives:", fontWeight = FontWeight.Bold, color = WarmIvory, fontSize = 13.sp)
            toy.learningObjectives.forEach { goal ->
                Text("• $goal", color = StarlightMuted, fontSize = 12.sp, modifier = Modifier.padding(vertical = 1.dp))
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Gift Wrap Option
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { giftWrapSelected = !giftWrapSelected }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = giftWrapSelected,
                    onCheckedChange = { giftWrapSelected = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = ChampagneGold,
                        checkmarkColor = MidnightNavy
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Add Luxury Toyverse Gift Box & Message", fontWeight = FontWeight.Bold, color = WarmIvory, fontSize = 13.sp)
                    Text("Includes recycled gift wrapping and printed personalized note (+$3.50)", color = StarlightMuted, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onCompareToggle,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isCompared) ChampagneGold else GlassBorder
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (isCompared) "In Compare" else "Compare",
                        color = if (isCompared) ChampagneGold else WarmIvory,
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = {
                        onAddToCart(giftWrapSelected)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ChampagneGold, contentColor = MidnightNavy),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(2f)
                ) {
                    Icon(Icons.Default.AddShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add to Cart • $${"%.2f".format(toy.price)}", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ComparisonDialog(
    comparedToys: List<Toy>,
    onDismiss: () -> Unit,
    onRemoveToy: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DeepCosmicNavy,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⚖️", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Side-by-Side Toy Comparison",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = WarmIvory
                )
            }
        },
        text = {
            if (comparedToys.isEmpty()) {
                Text("No toys selected for comparison.", color = StarlightMuted)
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    comparedToys.forEach { toy ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = NebulaSurface),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(toy.iconEmoji, fontSize = 24.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(toy.name, fontWeight = FontWeight.Bold, color = WarmIvory, fontSize = 13.sp, maxLines = 1)
                                            Text(toy.brand, color = StarlightMuted, fontSize = 11.sp)
                                        }
                                    }
                                    IconButton(
                                        onClick = { onRemoveToy(toy.id) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "Remove", tint = CoralPink, modifier = Modifier.size(16.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Price: $${"%.2f".format(toy.price)}", color = ChampagneGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text("Age: ${toy.ageRange}", color = WarmIvory, fontSize = 12.sp)
                                }

                                Text("Materials: ${toy.materials}", color = StarlightMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 2.dp))
                                Text("Safety: ${toy.safetyCertifications.joinToString(", ")}", color = EmeraldGreen, fontSize = 11.sp, modifier = Modifier.padding(top = 2.dp))
                                Text("Power: ${toy.batteryInfo}", color = StarlightMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 2.dp))
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = ChampagneGold, contentColor = MidnightNavy)
            ) {
                Text("Close Comparison", fontWeight = FontWeight.Bold)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartAndCheckoutSheet(
    viewModel: ToyverseViewModel,
    onDismiss: () -> Unit
) {
    val cart by viewModel.cart.collectAsState()
    val total by viewModel.cartTotal.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    var promoInput by remember { mutableStateOf("") }
    var promoError by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DeepCosmicNavy,
        dragHandle = { BottomSheetDefaults.DragHandle(color = GlassBorder) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Family Shopping Cart",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = WarmIvory
                )
                Text(
                    text = "${cart.size} Items",
                    color = StarlightMuted,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (cart.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🛒", fontSize = 42.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Your Toyverse Cart is Empty", fontWeight = FontWeight.Bold, color = WarmIvory)
                        Text("Add verified toys to your family wishlist or cart.", color = StarlightMuted, fontSize = 12.sp)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .heightIn(max = 280.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(cart) { item ->
                        Surface(
                            color = NebulaSurface,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(item.toy.iconEmoji, fontSize = 28.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(item.toy.name, fontWeight = FontWeight.Bold, color = WarmIvory, fontSize = 13.sp, maxLines = 1)
                                    Text("$${"%.2f".format(item.toy.price)} each", color = ChampagneGold, fontSize = 11.sp)
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { viewModel.updateCartQuantity(item.toy.id, -1) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Text("-", color = WarmIvory, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Text("${item.quantity}", color = WarmIvory, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    IconButton(
                                        onClick = { viewModel.updateCartQuantity(item.toy.id, 1) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Text("+", color = WarmIvory, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Promo Code Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = promoInput,
                        onValueChange = {
                            promoInput = it
                            promoError = false
                        },
                        placeholder = { Text("Code: TOYVERSE10", color = StarlightMuted, fontSize = 12.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ChampagneGold,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = WarmIvory,
                            unfocusedTextColor = WarmIvory
                        ),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val success = viewModel.applyPromoCode(promoInput)
                            if (!success) promoError = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NebulaSurface, contentColor = ChampagneGold),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Apply", fontSize = 12.sp)
                    }
                }

                if (uiState.promoCodeApplied != null) {
                    Text(
                        text = "✓ Applied: ${uiState.promoCodeApplied}",
                        color = EmeraldGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                } else if (promoError) {
                    Text("Invalid code. Try TOYVERSE10 or PLAY15", color = CoralPink, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Summary Total & Proceed
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Order Estimated Total:", color = WarmIvory, fontWeight = FontWeight.Medium)
                    Text(
                        text = "$${"%.2f".format(total)}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = ChampagneGold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.setCheckoutDialogOpen(true)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ChampagneGold, contentColor = MidnightNavy),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Proceed to Secure Adult Checkout →", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CheckoutConfirmationDialog(
    viewModel: ToyverseViewModel,
    onDismiss: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val total by viewModel.cartTotal.collectAsState()

    var addressInput by remember { mutableStateOf(uiState.checkoutAddress) }
    var giftMessageInput by remember { mutableStateOf(uiState.checkoutGiftMessage) }
    var orderSubmitted by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DeepCosmicNavy,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(if (orderSubmitted) "🎉" else "🔒", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (orderSubmitted) "Order Confirmed!" else "Secure Demo Checkout",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = WarmIvory
                )
            }
        },
        text = {
            if (!orderSubmitted) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Total to Authorize: $${"%.2f".format(total)}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Black,
                        color = ChampagneGold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Shipping Address:", color = WarmIvory, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = addressInput,
                        onValueChange = {
                            addressInput = it
                            viewModel.updateCheckoutAddress(it)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ChampagneGold,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = WarmIvory,
                            unfocusedTextColor = WarmIvory
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Gift Note (Optional):", color = WarmIvory, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = giftMessageInput,
                        onValueChange = {
                            giftMessageInput = it
                            viewModel.updateCheckoutGiftMessage(it)
                        },
                        placeholder = { Text("e.g. Happy 8th Birthday!", color = StarlightMuted, fontSize = 11.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ChampagneGold,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = WarmIvory,
                            unfocusedTextColor = WarmIvory
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = NebulaSurface,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "ℹ️ Sandbox Demo Mode: No real funds are charged. Generates live tracking number.",
                            fontSize = 11.sp,
                            color = StarlightMuted,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            } else {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Thank you! Your order has been placed in Toyverse Elite.",
                        color = WarmIvory,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tracking ID: ${uiState.lastCompletedOrder?.trackingNumber}",
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGreen,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Status: ${uiState.lastCompletedOrder?.status}",
                        color = ChampagneGold,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "You can monitor live progress and safety inspection anytime in your Family Command Center.",
                        color = StarlightMuted,
                        fontSize = 11.sp
                    )
                }
            }
        },
        confirmButton = {
            if (!orderSubmitted) {
                Button(
                    onClick = {
                        viewModel.submitCheckoutOrder()
                        orderSubmitted = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ChampagneGold, contentColor = MidnightNavy)
                ) {
                    Text("Confirm & Authorize Order", fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = ChampagneGold, contentColor = MidnightNavy)
                ) {
                    Text("Done", fontWeight = FontWeight.Bold)
                }
            }
        }
    )
}

@Composable
fun AddPrelovedDialog(
    viewModel: ToyverseViewModel,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf("Gently Used") }
    var price by remember { mutableStateOf("25.00") }
    var retail by remember { mutableStateOf("50.00") }
    var city by remember { mutableStateOf("Seattle, WA") }
    var desc by remember { mutableStateOf("Well maintained, sanitized, complete pieces.") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DeepCosmicNavy,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("♻️", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Text("List a Pre-Loved Toy", fontWeight = FontWeight.Bold, color = WarmIvory)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Toy Name / Set Title") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ChampagneGold,
                        unfocusedBorderColor = GlassBorder,
                        focusedTextColor = WarmIvory,
                        unfocusedTextColor = WarmIvory
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = brand,
                    onValueChange = { brand = it },
                    label = { Text("Manufacturer / Brand") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ChampagneGold,
                        unfocusedBorderColor = GlassBorder,
                        focusedTextColor = WarmIvory,
                        unfocusedTextColor = WarmIvory
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Selling Price ($)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ChampagneGold,
                        unfocusedBorderColor = GlassBorder,
                        focusedTextColor = WarmIvory,
                        unfocusedTextColor = WarmIvory
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Condition & Parts Description") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ChampagneGold,
                        unfocusedBorderColor = GlassBorder,
                        focusedTextColor = WarmIvory,
                        unfocusedTextColor = WarmIvory
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        viewModel.submitPrelovedListing(
                            title = title,
                            brand = if (brand.isBlank()) "Generic" else brand,
                            category = ToyCategory.BUILDING,
                            condition = condition,
                            price = price.toDoubleOrNull() ?: 20.0,
                            retail = retail.toDoubleOrNull() ?: 40.0,
                            city = city,
                            desc = desc
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ChampagneGold, contentColor = MidnightNavy)
            ) {
                Text("Publish Listing", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = StarlightMuted)
            }
        }
    )
}

@Composable
fun AddCollectorDialog(
    viewModel: ToyverseViewModel,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var mfr by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("2022") }
    var edition by remember { mutableStateOf("Limited Run #88 of 250") }
    var grade by remember { mutableStateOf("A+ Mint in Sealed Box") }
    var value by remember { mutableStateOf("220.00") }
    var cost by remember { mutableStateOf("150.00") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DeepCosmicNavy,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🏆", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Text("Add to Collector's Vault", fontWeight = FontWeight.Bold, color = WarmIvory)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Collectible Model / Specimen Title") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ChampagneGold,
                        unfocusedBorderColor = GlassBorder,
                        focusedTextColor = WarmIvory,
                        unfocusedTextColor = WarmIvory
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = mfr,
                    onValueChange = { mfr = it },
                    label = { Text("Maker / Studio") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ChampagneGold,
                        unfocusedBorderColor = GlassBorder,
                        focusedTextColor = WarmIvory,
                        unfocusedTextColor = WarmIvory
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = { Text("Estimated Market Valuation ($)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ChampagneGold,
                        unfocusedBorderColor = GlassBorder,
                        focusedTextColor = WarmIvory,
                        unfocusedTextColor = WarmIvory
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        viewModel.submitCollectorItem(
                            title = title,
                            mfr = if (mfr.isBlank()) "Heritage Studio" else mfr,
                            year = year.toIntOrNull() ?: 2023,
                            edition = edition,
                            conditionGrade = grade,
                            value = value.toDoubleOrNull() ?: 180.0,
                            cost = cost.toDoubleOrNull() ?: 120.0
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ChampagneGold, contentColor = MidnightNavy)
            ) {
                Text("Secure to Vault", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = StarlightMuted)
            }
        }
    )
}
