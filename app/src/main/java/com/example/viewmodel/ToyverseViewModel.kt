package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ToyverseRepository
import com.example.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppNavTab(val label: String, val iconEmoji: String) {
    UNIVERSE("Universe", "🌌"),
    DIRECTORY("Catalog", "🧸"),
    ORBIT_AI("ORBIT AI", "✨"),
    PLAY_LEARN("Play & Learn", "🎨"),
    FAMILY_HUB("Family Hub", "🛡️")
}

data class ToyverseUiState(
    val currentRole: UserRole = UserRole.PARENT,
    val selectedTab: AppNavTab = AppNavTab.UNIVERSE,
    val searchQuery: String = "",
    val selectedCategory: ToyCategory? = null,
    val selectedAgeFilter: String = "All",
    val maxPriceFilter: Double = 160.0,
    val sortBy: String = "Featured",
    val selectedToyForDetail: Toy? = null,
    val isCompareDialogOpen: Boolean = false,
    val isCartSheetOpen: Boolean = false,
    val isCheckoutDialogOpen: Boolean = false,
    val isAddPrelovedOpen: Boolean = false,
    val isAddCollectorOpen: Boolean = false,
    val isKidPlaygroundActive: Boolean = false,
    val activeChildProfile: ChildProfile? = null,
    val orbitInputText: String = "",
    val isOrbitThinking: Boolean = false,
    val lastCompletedOrder: Order? = null,
    val promoCodeApplied: String? = null,
    val promoDiscountPercent: Double = 0.0,
    val checkoutAddress: String = "742 Evergreen Terrace, Springfield",
    val checkoutGiftMessage: String = ""
)

class ToyverseViewModel(
    private val repository: ToyverseRepository = ToyverseRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ToyverseUiState())
    val uiState: StateFlow<ToyverseUiState> = _uiState.asStateFlow()

    val toys: StateFlow<List<Toy>> = repository.toys
    val cart: StateFlow<List<CartItem>> = repository.cart
    val wishlist: StateFlow<Set<String>> = repository.wishlist
    val comparedToyIds: StateFlow<Set<String>> = repository.comparedToyIds
    val childProfiles: StateFlow<List<ChildProfile>> = repository.childProfiles
    val learningActivities: StateFlow<List<LearningActivity>> = repository.learningActivities
    val prelovedListings: StateFlow<List<PrelovedListing>> = repository.prelovedListings
    val collectorVault: StateFlow<List<CollectorItem>> = repository.collectorVault
    val safetyNotices: StateFlow<List<SafetyRecallNotice>> = repository.safetyNotices
    val orders: StateFlow<List<Order>> = repository.orders
    val orbitChat: StateFlow<List<OrbitMessage>> = repository.orbitChat

    // Filtered toys derived state
    val filteredToys: StateFlow<List<Toy>> = combine(
        toys,
        _uiState
    ) { allToys, state ->
        allToys.filter { toy ->
            val matchesSearch = state.searchQuery.isBlank() ||
                    toy.name.contains(state.searchQuery, ignoreCase = true) ||
                    toy.brand.contains(state.searchQuery, ignoreCase = true) ||
                    toy.category.title.contains(state.searchQuery, ignoreCase = true) ||
                    toy.summary.contains(state.searchQuery, ignoreCase = true)

            val matchesCategory = state.selectedCategory == null || toy.category == state.selectedCategory

            val matchesPrice = toy.price <= state.maxPriceFilter

            val matchesAge = when (state.selectedAgeFilter) {
                "0-2 yrs" -> toy.minAgeYears <= 2
                "3-5 yrs" -> toy.minAgeYears in 1..5
                "6-8 yrs" -> toy.minAgeYears in 4..8
                "9-12 yrs" -> toy.minAgeYears in 7..12
                "13+ yrs" -> toy.minAgeYears >= 10
                else -> true
            }

            matchesSearch && matchesCategory && matchesPrice && matchesAge
        }.let { list ->
            when (state.sortBy) {
                "Rating" -> list.sortedByDescending { it.rating }
                "Price: Low to High" -> list.sortedBy { it.price }
                "Price: High to Low" -> list.sortedByDescending { it.price }
                else -> list // Featured order
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartTotal: StateFlow<Double> = cart.map { items ->
        val subtotal = items.sumOf { it.toy.price * it.quantity }
        val discount = subtotal * (_uiState.value.promoDiscountPercent / 100.0)
        (subtotal - discount).coerceAtLeast(0.0)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val cartCount: StateFlow<Int> = cart.map { items ->
        items.sumOf { it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    init {
        // Set first child profile as default active
        viewModelScope.launch {
            childProfiles.collect { profiles ->
                if (_uiState.value.activeChildProfile == null && profiles.isNotEmpty()) {
                    _uiState.update { it.copy(activeChildProfile = profiles.first()) }
                }
            }
        }
    }

    // Role switching
    fun setRole(role: UserRole) {
        _uiState.update {
            it.copy(
                currentRole = role,
                isKidPlaygroundActive = (role == UserRole.CHILD)
            )
        }
        if (role == UserRole.CHILD) {
            _uiState.update { it.copy(selectedTab = AppNavTab.PLAY_LEARN) }
        }
    }

    fun selectTab(tab: AppNavTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setCategoryFilter(category: ToyCategory?) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun setAgeFilter(age: String) {
        _uiState.update { it.copy(selectedAgeFilter = age) }
    }

    fun setMaxPrice(price: Double) {
        _uiState.update { it.copy(maxPriceFilter = price) }
    }

    fun setSortBy(sort: String) {
        _uiState.update { it.copy(sortBy = sort) }
    }

    fun openToyDetail(toy: Toy?) {
        _uiState.update { it.copy(selectedToyForDetail = toy) }
    }

    fun toggleWishlist(toyId: String) {
        repository.toggleWishlist(toyId)
    }

    fun toggleCompare(toyId: String) {
        repository.toggleCompare(toyId)
    }

    fun clearCompare() {
        repository.clearCompare()
    }

    fun setCompareDialogOpen(open: Boolean) {
        _uiState.update { it.copy(isCompareDialogOpen = open) }
    }

    fun setCartSheetOpen(open: Boolean) {
        _uiState.update { it.copy(isCartSheetOpen = open) }
    }

    fun setCheckoutDialogOpen(open: Boolean) {
        _uiState.update { it.copy(isCheckoutDialogOpen = open) }
    }

    fun setAddPrelovedOpen(open: Boolean) {
        _uiState.update { it.copy(isAddPrelovedOpen = open) }
    }

    fun setAddCollectorOpen(open: Boolean) {
        _uiState.update { it.copy(isAddCollectorOpen = open) }
    }

    fun setKidPlaygroundActive(active: Boolean) {
        _uiState.update {
            it.copy(
                isKidPlaygroundActive = active,
                currentRole = if (active) UserRole.CHILD else UserRole.PARENT
            )
        }
    }

    fun setActiveChildProfile(profile: ChildProfile) {
        _uiState.update { it.copy(activeChildProfile = profile) }
    }

    // Cart actions
    fun addToCart(toy: Toy, giftWrap: Boolean = false) {
        repository.addToCart(toy, giftWrap)
    }

    fun updateCartQuantity(toyId: String, delta: Int) {
        repository.updateCartQuantity(toyId, delta)
    }

    fun applyPromoCode(code: String): Boolean {
        return if (code.trim().equals("TOYVERSE10", ignoreCase = true)) {
            _uiState.update { it.copy(promoCodeApplied = "TOYVERSE10 (10% OFF)", promoDiscountPercent = 10.0) }
            true
        } else if (code.trim().equals("PLAY15", ignoreCase = true)) {
            _uiState.update { it.copy(promoCodeApplied = "PLAY15 (15% OFF)", promoDiscountPercent = 15.0) }
            true
        } else {
            false
        }
    }

    fun updateCheckoutAddress(address: String) {
        _uiState.update { it.copy(checkoutAddress = address) }
    }

    fun updateCheckoutGiftMessage(msg: String) {
        _uiState.update { it.copy(checkoutGiftMessage = msg) }
    }

    fun submitCheckoutOrder(): Order {
        val state = _uiState.value
        val order = repository.placeOrder(
            shippingAddress = state.checkoutAddress,
            giftWrapMessage = state.checkoutGiftMessage
        )
        _uiState.update {
            it.copy(
                lastCompletedOrder = order,
                isCheckoutDialogOpen = false,
                isCartSheetOpen = false,
                promoCodeApplied = null,
                promoDiscountPercent = 0.0
            )
        }
        return order
    }

    // Pre-loved & Collector submission
    fun submitPrelovedListing(
        title: String,
        brand: String,
        category: ToyCategory,
        condition: String,
        price: Double,
        retail: Double,
        city: String,
        desc: String
    ) {
        val newListing = PrelovedListing(
            id = "pre_" + System.currentTimeMillis(),
            toyTitle = title,
            brand = brand,
            category = category,
            condition = condition,
            price = price,
            originalRetail = retail,
            sellerCity = city,
            verifiedSafe = true,
            description = desc
        )
        repository.addPrelovedListing(newListing)
        _uiState.update { it.copy(isAddPrelovedOpen = false) }
    }

    fun submitCollectorItem(
        title: String,
        mfr: String,
        year: Int,
        edition: String,
        conditionGrade: String,
        value: Double,
        cost: Double
    ) {
        val newItem = CollectorItem(
            id = "col_" + System.currentTimeMillis(),
            title = title,
            manufacturer = mfr,
            releaseYear = year,
            edition = edition,
            conditionGrade = conditionGrade,
            estimatedValue = value,
            purchasePrice = cost,
            hasCertificateOfAuthenticity = true,
            isPublicShowcase = true
        )
        repository.addCollectorItem(newItem)
        _uiState.update { it.copy(isAddCollectorOpen = false) }
    }

    // ORBIT Chat
    fun updateOrbitInput(text: String) {
        _uiState.update { it.copy(orbitInputText = text) }
    }

    fun sendOrbitMessage(query: String? = null) {
        val text = query ?: _uiState.value.orbitInputText
        if (text.isBlank()) return

        _uiState.update { it.copy(orbitInputText = "", isOrbitThinking = true) }
        viewModelScope.launch {
            repository.sendOrbitPrompt(text)
            _uiState.update { it.copy(isOrbitThinking = false) }
        }
    }
}
