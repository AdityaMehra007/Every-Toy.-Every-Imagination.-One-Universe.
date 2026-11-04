package com.example.model

enum class UserRole(val label: String, val subtitle: String, val badge: String) {
    PARENT("Parent / Guardian", "Full marketplace, family controls & budgets", "Family Lead"),
    CHILD("Toyverse Kid (Safe Mode)", "Supervised playground, games & challenges", "Junior Explorer"),
    COLLECTOR("Elite Collector", "Vault inventory, grading & valuations", "Collector VIP"),
    CREATOR("Creator & Brand Studio", "Product showcase, safety filings & analytics", "Verified Maker"),
    EDUCATOR("Educator / STEM Hub", "Curriculum challenges & group lending", "Play Mentor")
}

enum class ToyCategory(val id: String, val title: String, val emoji: String) {
    BUILDING("building", "Building & Construction", "🧱"),
    STEM("stem", "Robotics & STEM", "🔬"),
    DOLLS("dolls", "Dolls & Role-Play", "🧸"),
    VEHICLES("vehicles", "Vehicles & RC", "🚀"),
    BOARD_GAMES("games", "Board Games & Puzzles", "🎲"),
    ARTS("arts", "Arts, Crafts & Music", "🎨"),
    WOODEN("wooden", "Montessori & Wooden", "🌲"),
    OUTDOOR("outdoor", "Outdoor & Active", "⚽"),
    COLLECTIBLE("collectible", "Elite Collectibles", "🏆")
}

data class Toy(
    val id: String,
    val name: String,
    val brand: String,
    val category: ToyCategory,
    val ageRange: String,
    val minAgeYears: Int,
    val price: Double,
    val originalPrice: Double? = null,
    val rating: Float,
    val reviewCount: Int,
    val iconEmoji: String,
    val accentColorHex: Long,
    val summary: String,
    val description: String,
    val safetyCertifications: List<String>,
    val safetyWarnings: List<String>,
    val materials: String,
    val batteryInfo: String,
    val dimensions: String,
    val supervisionLevel: String, // e.g. "Independent", "Mild Adult Supervision", "Guided Play"
    val learningObjectives: List<String>,
    val isVerifiedSeller: Boolean = true,
    val sellerName: String = "Toyverse Official",
    val inStock: Boolean = true,
    val stockCount: Int = 24,
    val isPrelovedAvailable: Boolean = false,
    val isRentalAvailable: Boolean = false,
    val rentalWeeklyPrice: Double? = null,
    val isCollectorGrade: Boolean = false,
    val conditionGrade: String? = null
)

data class CartItem(
    val toy: Toy,
    var quantity: Int = 1,
    val giftWrap: Boolean = false,
    val giftMessage: String = ""
)

data class ChildProfile(
    val id: String,
    val nickname: String,
    val ageYears: Int,
    val favoriteInterests: List<String>,
    val avatarEmoji: String,
    val badgesEarned: List<String> = emptyList(),
    val completedChallengesCount: Int = 0
)

data class LearningActivity(
    val id: String,
    val title: String,
    val category: String,
    val ageRange: String,
    val durationMinutes: Int,
    val difficulty: String, // "Easy", "Medium", "Master"
    val supervisionRequired: String,
    val materialsNeeded: List<String>,
    val stepByStepInstructions: List<String>,
    val safetyPrecautions: String,
    val learningGoals: String,
    val rewardBadge: String
)

data class PrelovedListing(
    val id: String,
    val toyTitle: String,
    val brand: String,
    val category: ToyCategory,
    val condition: String, // "Like New - Boxed", "Gently Used", "Well Loved", "Vintage"
    val price: Double,
    val originalRetail: Double,
    val sellerCity: String,
    val verifiedSafe: Boolean,
    val description: String,
    val missingPartsNotes: String = "Complete set, no missing pieces."
)

data class CollectorItem(
    val id: String,
    val title: String,
    val manufacturer: String,
    val releaseYear: Int,
    val edition: String, // e.g., "1st Edition - 1 of 500", "Limited Anniversary"
    val conditionGrade: String, // e.g. "A+ Mint in Sealed Box", "Near Mint"
    val estimatedValue: Double,
    val purchasePrice: Double,
    val hasCertificateOfAuthenticity: Boolean = true,
    val isPublicShowcase: Boolean = true
)

data class SafetyRecallNotice(
    val id: String,
    val productTitle: String,
    val brand: String,
    val noticeType: String, // "CLEAN_RECORD", "ADVISORY", "OFFICIAL_RECALL"
    val issuingBody: String, // "CPSC (US)", "EU Safety Gate", "BIS (India)", "ISO Toy Standards"
    val dateUpdated: String,
    val details: String,
    val actionRequired: String
)

data class OrbitMessage(
    val id: String,
    val isUser: Boolean,
    val text: String,
    val timestamp: String,
    val suggestedToyIds: List<String> = emptyList(),
    val safetyTips: String? = null
)

data class Order(
    val orderId: String,
    val dateFormatted: String,
    val items: List<CartItem>,
    val totalAmount: Double,
    val status: String, // "Confirmed", "Safety Inspection", "Shipped", "Delivered"
    val trackingNumber: String,
    val shippingAddress: String
)
