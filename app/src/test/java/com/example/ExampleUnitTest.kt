package com.example

import com.example.data.ToyverseRepository
import com.example.model.ToyCategory
import com.example.model.UserRole
import com.example.viewmodel.AppNavTab
import com.example.viewmodel.ToyverseViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ExampleUnitTest {

    private lateinit var repository: ToyverseRepository
    private lateinit var viewModel: ToyverseViewModel

    @Before
    fun setUp() {
        repository = ToyverseRepository()
        viewModel = ToyverseViewModel(repository)
    }

    @Test
    fun testInitialCatalogLoaded() {
        val toys = repository.toys.value
        assertTrue("Repository must load verified toys", toys.isNotEmpty())
        assertTrue("Contains Building category toys", toys.any { it.category == ToyCategory.BUILDING })
        assertTrue("Contains STEM category toys", toys.any { it.category == ToyCategory.STEM })
    }

    @Test
    fun testCartOperationsAndTotals() {
        val sampleToy = repository.toys.value.first()
        repository.addToCart(sampleToy, giftWrap = true)

        assertEquals(1, repository.cart.value.size)
        assertEquals(1, repository.cart.value.first().quantity)

        // Increment quantity
        repository.updateCartQuantity(sampleToy.id, 1)
        assertEquals(2, repository.cart.value.first().quantity)

        // Submit checkout
        val order = repository.placeOrder("123 Play Lane", "Happy Birthday!")
        assertNotNull("Order must be generated", order)
        assertTrue("Order ID must have prefix", order.orderId.startsWith("TV-"))
        assertEquals(0, repository.cart.value.size)
        assertEquals(2, repository.orders.value.size) // 1 initial sample + 1 new
    }

    @Test
    fun testWishlistAndCompareToggles() {
        val sampleToyId = "toy_3"
        val initialWishlisted = repository.wishlist.value.contains(sampleToyId)
        repository.toggleWishlist(sampleToyId)
        assertNotEquals(initialWishlisted, repository.wishlist.value.contains(sampleToyId))

        // Compare up to 3 toys
        repository.clearCompare()
        repository.toggleCompare("toy_1")
        repository.toggleCompare("toy_2")
        repository.toggleCompare("toy_3")
        repository.toggleCompare("toy_4") // Exceeds 3, should remain 3
        assertEquals(3, repository.comparedToyIds.value.size)
    }

    @Test
    fun testRoleSwitching() {
        viewModel.setRole(UserRole.CHILD)
        assertEquals(UserRole.CHILD, viewModel.uiState.value.currentRole)
        assertTrue(viewModel.uiState.value.isKidPlaygroundActive)
        assertEquals(AppNavTab.PLAY_LEARN, viewModel.uiState.value.selectedTab)

        viewModel.setRole(UserRole.COLLECTOR)
        assertEquals(UserRole.COLLECTOR, viewModel.uiState.value.currentRole)
        assertFalse(viewModel.uiState.value.isKidPlaygroundActive)
    }

    @Test
    fun testOrbitAiChatResponse() {
        val initialCount = repository.orbitChat.value.size
        repository.sendOrbitPrompt("Find STEM robotics under 50 dollars")
        val updatedChat = repository.orbitChat.value
        assertEquals(initialCount + 2, updatedChat.size) // 1 user + 1 assistant
        val assistantResponse = updatedChat.last()
        assertFalse(assistantResponse.isUser)
        assertTrue(assistantResponse.suggestedToyIds.isNotEmpty())
        assertNotNull(assistantResponse.safetyTips)
    }
}
