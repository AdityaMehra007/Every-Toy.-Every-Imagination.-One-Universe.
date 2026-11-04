package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class ToyverseRepository {

    private val _toys = MutableStateFlow<List<Toy>>(sampleToys())
    val toys: StateFlow<List<Toy>> = _toys.asStateFlow()

    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    private val _wishlist = MutableStateFlow<Set<String>>(setOf("toy_1", "toy_4"))
    val wishlist: StateFlow<Set<String>> = _wishlist.asStateFlow()

    private val _comparedToyIds = MutableStateFlow<Set<String>>(emptySet())
    val comparedToyIds: StateFlow<Set<String>> = _comparedToyIds.asStateFlow()

    private val _childProfiles = MutableStateFlow<List<ChildProfile>>(sampleChildProfiles())
    val childProfiles: StateFlow<List<ChildProfile>> = _childProfiles.asStateFlow()

    private val _learningActivities = MutableStateFlow<List<LearningActivity>>(sampleActivities())
    val learningActivities: StateFlow<List<LearningActivity>> = _learningActivities.asStateFlow()

    private val _prelovedListings = MutableStateFlow<List<PrelovedListing>>(samplePrelovedListings())
    val prelovedListings: StateFlow<List<PrelovedListing>> = _prelovedListings.asStateFlow()

    private val _collectorVault = MutableStateFlow<List<CollectorItem>>(sampleCollectorVault())
    val collectorVault: StateFlow<List<CollectorItem>> = _collectorVault.asStateFlow()

    private val _safetyNotices = MutableStateFlow<List<SafetyRecallNotice>>(sampleSafetyNotices())
    val safetyNotices: StateFlow<List<SafetyRecallNotice>> = _safetyNotices.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(sampleOrders())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _orbitChat = MutableStateFlow<List<OrbitMessage>>(sampleOrbitChat())
    val orbitChat: StateFlow<List<OrbitMessage>> = _orbitChat.asStateFlow()

    // Cart operations
    fun addToCart(toy: Toy, giftWrap: Boolean = false) {
        val current = _cart.value.toMutableList()
        val existingIndex = current.indexOfFirst { it.toy.id == toy.id }
        if (existingIndex >= 0) {
            val item = current[existingIndex]
            current[existingIndex] = item.copy(quantity = item.quantity + 1)
        } else {
            current.add(CartItem(toy = toy, quantity = 1, giftWrap = giftWrap))
        }
        _cart.value = current
    }

    fun updateCartQuantity(toyId: String, delta: Int) {
        val current = _cart.value.toMutableList()
        val index = current.indexOfFirst { it.toy.id == toyId }
        if (index >= 0) {
            val newQty = current[index].quantity + delta
            if (newQty <= 0) {
                current.removeAt(index)
            } else {
                current[index] = current[index].copy(quantity = newQty)
            }
            _cart.value = current
        }
    }

    fun clearCart() {
        _cart.value = emptyList()
    }

    // Wishlist
    fun toggleWishlist(toyId: String) {
        val set = _wishlist.value.toMutableSet()
        if (set.contains(toyId)) set.remove(toyId) else set.add(toyId)
        _wishlist.value = set
    }

    // Comparison
    fun toggleCompare(toyId: String) {
        val set = _comparedToyIds.value.toMutableSet()
        if (set.contains(toyId)) {
            set.remove(toyId)
        } else {
            if (set.size < 3) {
                set.add(toyId)
            }
        }
        _comparedToyIds.value = set
    }

    fun clearCompare() {
        _comparedToyIds.value = emptySet()
    }

    // Pre-loved submission
    fun addPrelovedListing(listing: PrelovedListing) {
        _prelovedListings.value = listOf(listing) + _prelovedListings.value
    }

    // Collector submission
    fun addCollectorItem(item: CollectorItem) {
        _collectorVault.value = listOf(item) + _collectorVault.value
    }

    // Checkout / Order placement
    fun placeOrder(shippingAddress: String, giftWrapMessage: String): Order {
        val cartItems = _cart.value
        val total = cartItems.sumOf { it.toy.price * it.quantity }
        val newOrder = Order(
            orderId = "TV-" + (1000..9999).random(),
            dateFormatted = "Just now",
            items = cartItems.map { it.copy(giftMessage = giftWrapMessage) },
            totalAmount = total,
            status = "Confirmed - Quality & Safety Inspection",
            trackingNumber = "TRK-TV-" + (10000..99999).random(),
            shippingAddress = shippingAddress
        )
        _orders.value = listOf(newOrder) + _orders.value
        clearCart()
        return newOrder
    }

    // ORBIT Discovery AI Interaction
    fun sendOrbitPrompt(userQuery: String) {
        val currentMessages = _orbitChat.value.toMutableList()
        val userMsg = OrbitMessage(
            id = UUID.randomUUID().toString(),
            isUser = true,
            text = userQuery,
            timestamp = "Now"
        )
        currentMessages.add(userMsg)

        // Generate intelligent contextual response
        val response = generateOrbitResponse(userQuery)
        currentMessages.add(response)
        _orbitChat.value = currentMessages
    }

    private fun generateOrbitResponse(query: String): OrbitMessage {
        val lower = query.lowercase()
        return when {
            lower.contains("stem") || lower.contains("robot") || lower.contains("code") -> {
                OrbitMessage(
                    id = UUID.randomUUID().toString(),
                    isUser = false,
                    text = "For budding inventors, I strongly recommend high-engagement robotics with hands-on mechanics. Here are 2 verified safe STEM picks that teach spatial reasoning and logic without screen fatigue:",
                    timestamp = "Now",
                    suggestedToyIds = listOf("toy_2", "toy_5"),
                    safetyTips = "Notice: RoboPaws AI and Celestial Lab both adhere strictly to ASTM F963 safety standards with enclosed lithium battery compartments."
                )
            }
            lower.contains("under") || lower.contains("budget") || lower.contains("cheap") || lower.contains("price") -> {
                OrbitMessage(
                    id = UUID.randomUUID().toString(),
                    isUser = false,
                    text = "Here are top parent-approved discoveries with exceptional play value under $40. High replayability guaranteed!",
                    timestamp = "Now",
                    suggestedToyIds = listOf("toy_3", "toy_6"),
                    safetyTips = "Certified non-toxic water-based paints and smooth chamfered wooden edges for toddler safety."
                )
            }
            lower.contains("travel") || lower.contains("car") || lower.contains("journey") -> {
                OrbitMessage(
                    id = UUID.randomUUID().toString(),
                    isUser = false,
                    text = "For travel and road trips, portable toys with contained magnetic parts and compact carrying cases prevent lost pieces while keeping children engaged for hours:",
                    timestamp = "Now",
                    suggestedToyIds = listOf("toy_6", "toy_1"),
                    safetyTips = "Travel safety: Magnetic pieces are encased in sonic-welded ABS plastic preventing detachment."
                )
            }
            lower.contains("rainy") || lower.contains("activity") || lower.contains("indoor") -> {
                OrbitMessage(
                    id = UUID.randomUUID().toString(),
                    isUser = false,
                    text = "Rainy Day Play Suggestion: Try the 'Cosmic Lunar Base Challenge' using building blocks paired with scrap cardboard and paper tubes! It fosters 90+ minutes of cooperative problem solving.",
                    timestamp = "Now",
                    suggestedToyIds = listOf("toy_1", "toy_7"),
                    safetyTips = "Always supervise young builders when using household safety scissors or paper tape."
                )
            }
            lower.contains("safety") || lower.contains("recall") -> {
                OrbitMessage(
                    id = UUID.randomUUID().toString(),
                    isUser = false,
                    text = "All catalog items on Toyverse Elite undergo multi-point safety verification with CPSC and EU Safety Gate cross-checks. Currently 100% of our featured building and electronic sets have clean safety records.",
                    timestamp = "Now",
                    suggestedToyIds = listOf("toy_1", "toy_2", "toy_4"),
                    safetyTips = "Inspect toys periodically for loose wear and ensure battery compartments remain securely screwed shut."
                )
            }
            else -> {
                OrbitMessage(
                    id = UUID.randomUUID().toString(),
                    isUser = false,
                    text = "I've matched your request across Toyverse Elite's 9,000+ verified catalog attributes. Here are top-ranked toys matching age suitability, developmental value, and verified safety:",
                    timestamp = "Now",
                    suggestedToyIds = listOf("toy_1", "toy_4", "toy_5"),
                    safetyTips = "Recommended: Confirm child age meets the minimum rating to avoid potential small-part hazards."
                )
            }
        }
    }

    private fun sampleToys(): List<Toy> = listOf(
        Toy(
            id = "toy_1",
            name = "AstroCraft Lunar Odyssey Explorer",
            brand = "CosmoBricks",
            category = ToyCategory.BUILDING,
            ageRange = "8-14 yrs",
            minAgeYears = 8,
            price = 64.99,
            originalPrice = 79.99,
            rating = 4.9f,
            reviewCount = 342,
            iconEmoji = "🚀",
            accentColorHex = 0xFF7B4DFF,
            summary = "1,120-piece modular space rover with solar panels, lunar laboratory, and astronaut crew.",
            description = "Embark on interplanetary missions! Features functional suspension, opening airlock doors, illuminated interior research bay, and realistic deployable lunar drone.",
            safetyCertifications = listOf("ASTM F963-23", "CE Compliant", "BPA Free", "Non-Toxic ABS"),
            safetyWarnings = listOf("Small parts choking hazard for children under 3 years", "Adult supervision suggested during lighting module assembly"),
            materials = "High-impact food-grade ABS resin, zinc-plated axle pins",
            batteryInfo = "2x AAA batteries (included in secure screw-closed bay)",
            dimensions = "38 cm x 24 cm x 18 cm (Weight: 1.2 kg)",
            supervisionLevel = "Independent (Guided initially)",
            learningObjectives = listOf("Spatial Reasoning", "Structural Engineering", "Fine Motor Dexterity", "Patience & Blueprint Reading"),
            inStock = true,
            stockCount = 18,
            isPrelovedAvailable = true,
            isRentalAvailable = true,
            rentalWeeklyPrice = 9.99,
            isCollectorGrade = true,
            conditionGrade = "Mint Factory Sealed"
        ),
        Toy(
            id = "toy_2",
            name = "RoboPaws AI Interactive Puppy",
            brand = "CyberPlay Labs",
            category = ToyCategory.STEM,
            ageRange = "5-10 yrs",
            minAgeYears = 5,
            price = 89.99,
            originalPrice = 119.99,
            rating = 4.8f,
            reviewCount = 512,
            iconEmoji = "🐶",
            accentColorHex = 0xFF00D2FF,
            summary = "Voice-activated smart robotic companion that learns tricks, avoids obstacles, and responds to touch.",
            description = "RoboPaws listens, barks melodies, dances, and teaches introductory coding blocks through a companion app or on-device touch programming.",
            safetyCertifications = listOf("EN 71 Certified", "FCC Class B", "UL Battery Safe", "RoHS Compliant"),
            safetyWarnings = listOf("Contains enclosed rechargeable lithium polymer battery", "Do not immerse in water"),
            materials = "Child-safe polycarbonate body with soft hypoallergenic silicone ears and paws",
            batteryInfo = "Internal USB-C rechargeable 1200mAh battery (overcharge protected)",
            dimensions = "22 cm x 15 cm x 20 cm (Weight: 650g)",
            supervisionLevel = "Independent",
            learningObjectives = listOf("Introductory Algorithmic Thinking", "Emotional Empathy", "Cause-and-Effect Logic"),
            inStock = true,
            stockCount = 12,
            isRentalAvailable = true,
            rentalWeeklyPrice = 14.50
        ),
        Toy(
            id = "toy_3",
            name = "Nordic Forest Montessori Wooden Play Set",
            brand = "Silvano Nature",
            category = ToyCategory.WOODEN,
            ageRange = "1-4 yrs",
            minAgeYears = 1,
            price = 38.50,
            originalPrice = 45.00,
            rating = 4.95f,
            reviewCount = 680,
            iconEmoji = "🌲",
            accentColorHex = 0xFF00D084,
            summary = "Hand-carved organic beechwood balancing stones, stacking rainbow arches, and sensory sorting rings.",
            description = "Crafted from sustainably harvested European beechwood. Finished with cold-pressed botanical oils and organic vegetable dyes. Smooth to the touch, splinter-free, and safe for teething toddlers.",
            safetyCertifications = listOf("FSC 100% Certified Wood", "EN71-3 Safe", "Lead-Free", "Saliva-Resistant"),
            safetyWarnings = listOf("Suitable for toddlers 12 months and older", "Inspect wood periodically for natural wear"),
            materials = "Solid FSC-certified Beech and Linden wood, organic vegetable pigments",
            batteryInfo = "No batteries required (100% natural unplugged play)",
            dimensions = "Box: 30 cm x 22 cm x 8 cm (Weight: 890g)",
            supervisionLevel = "Independent",
            learningObjectives = listOf("Tactile Sensory Integration", "Balance & Gravitational Equilibrium", "Color Discrimination", "Open-Ended Imagination"),
            inStock = true,
            stockCount = 45,
            isPrelovedAvailable = true
        ),
        Toy(
            id = "toy_4",
            name = "Vortex Drone 4K Sky Scout",
            brand = "AeroMotion Tech",
            category = ToyCategory.VEHICLES,
            ageRange = "10+ yrs",
            minAgeYears = 10,
            price = 74.99,
            originalPrice = 95.00,
            rating = 4.7f,
            reviewCount = 289,
            iconEmoji = "🚁",
            accentColorHex = 0xFFFF5376,
            summary = "Quad-rotor drone with altitude hold, 360-degree obstacle guards, and one-touch flip acrobatics.",
            description = "Designed specifically for novice pilots. Full cage propeller guard prevents finger contact. Includes headless flight mode and emergency auto-land sensor.",
            safetyCertifications = listOf("ASTM F963", "CE", "Propeller Shield Protection", "Fail-Safe Auto Stop"),
            safetyWarnings = listOf("Fly outdoors in open areas or high-ceiling rooms away from overhead fixtures", "Not for children under 8"),
            materials = "Impact-absorbing EPP foam chassis and flexible nylon rotors",
            batteryInfo = "2x Modular quick-swap 3.7V batteries with USB dual charger",
            dimensions = "18 cm x 18 cm x 5 cm (Weight: 85g ultralight)",
            supervisionLevel = "Mild Adult Supervision Recommended",
            learningObjectives = listOf("Hand-Eye Coordination", "Aerodynamics Understanding", "Three-Dimensional Spatial Control"),
            inStock = true,
            stockCount = 22
        ),
        Toy(
            id = "toy_5",
            name = "Celestial Lab Micro-Optics Station",
            brand = "Discovery Spark",
            category = ToyCategory.STEM,
            ageRange = "7-13 yrs",
            minAgeYears = 7,
            price = 52.00,
            originalPrice = 62.00,
            rating = 4.85f,
            reviewCount = 410,
            iconEmoji = "🔬",
            accentColorHex = 0xFFF5C542,
            summary = "Compound optical microscope with 100x-1200x magnification, prepared biological specimen slides, and smartphone viewer.",
            description = "Uncover the hidden microscopic universe! Comes with 24 prepared slides of butterfly wings, plant stomata, and crystals, plus clean blank slides and pipettes.",
            safetyCertifications = listOf("ASTM F963 Toy Safety", "Safe Acrylic Optics (Shatterproof)", "CE Marked"),
            safetyWarnings = listOf("Slides feature smooth beveled acrylic safety edges", "Pipettes and specimen vials contain no active chemicals"),
            materials = "Reinforced ABS casing with shatter-resistant optical acrylic lenses",
            batteryInfo = "2x AA batteries for dual LED illuminator top/bottom",
            dimensions = "25 cm x 14 cm x 10 cm (Weight: 540g)",
            supervisionLevel = "Mild Adult Supervision for slide preparation",
            learningObjectives = listOf("Biological Scientific Inquiry", "Observation & Hypothesis Testing", "Precision Hand Skills"),
            inStock = true,
            stockCount = 30,
            isRentalAvailable = true,
            rentalWeeklyPrice = 8.00
        ),
        Toy(
            id = "toy_6",
            name = "Chronicles of Catan: Junior Kingdoms",
            brand = "Nexus Tabletop",
            category = ToyCategory.BOARD_GAMES,
            ageRange = "6-12 yrs (2-4 Players)",
            minAgeYears = 6,
            price = 32.99,
            originalPrice = 39.99,
            rating = 4.9f,
            reviewCount = 890,
            iconEmoji = "🎲",
            accentColorHex = 0xFFFF9F1C,
            summary = "Cooperative island exploration board game of resource trading, harbor building, and friendly pirate encounters.",
            description = "Fast-paced, high-laughter family tabletop game designed by master game designers. Teaches negotiation, resource management, and strategic thinking.",
            safetyCertifications = listOf("EN71", "ASTM", "Soy-based Inks", "Recycled Greyboard"),
            safetyWarnings = listOf("Small resource tokens, keep away from infants under 3"),
            materials = "FSC-certified heavy recycled cardboard with linen finish, solid wood pirate tokens",
            batteryInfo = "No batteries required",
            dimensions = "30 cm x 30 cm x 7 cm (Weight: 1.1 kg)",
            supervisionLevel = "Family Play (Adults & Children together)",
            learningObjectives = listOf("Strategic Planning", "Fair Negotiation & Sharing", "Probability Intuition", "Social Sportsmanship"),
            inStock = true,
            stockCount = 35
        ),
        Toy(
            id = "toy_7",
            name = "Little Master Chef Artisan Wooden Kitchen",
            brand = "PretendVille",
            category = ToyCategory.DOLLS,
            ageRange = "3-7 yrs",
            minAgeYears = 3,
            price = 115.00,
            originalPrice = 139.99,
            rating = 4.88f,
            reviewCount = 275,
            iconEmoji = "🍳",
            accentColorHex = 0xFFFF85A1,
            summary = "Deluxe wooden pretend kitchen with clicking burner dials, sink, oven, and stainless steel cookware.",
            description = "Fosters hours of roleplay and family bonding. Built with solid pine legs, magnetic door catches, chalkboard grocery list panel, and 12-piece accessory set.",
            safetyCertifications = listOf("ASTM F963-23", "Lead-Free", "Formaldehyde-Free Wood Composite", "Anti-Tip Wall Anchor Included"),
            safetyWarnings = listOf("Adult assembly required", "Always install provided anti-tip strap onto wall stud"),
            materials = "FSC certified birch plywood and solid pine, food-safe finishes",
            batteryInfo = "No batteries required (mechanical clicking dials)",
            dimensions = "72 cm x 34 cm x 94 cm (Weight: 11 kg)",
            supervisionLevel = "Independent once assembled",
            learningObjectives = listOf("Social Empathy & Caretaking", "Narrative Storytelling", "Nutrition Awareness", "Household Cooperation"),
            inStock = true,
            stockCount = 8,
            isRentalAvailable = true,
            rentalWeeklyPrice = 19.00
        ),
        Toy(
            id = "toy_8",
            name = "Starlight Nebula Glow Bear",
            brand = "DreamHaven Plush",
            category = ToyCategory.DOLLS,
            ageRange = "0+ All Ages",
            minAgeYears = 0,
            price = 28.00,
            originalPrice = 34.00,
            rating = 4.96f,
            reviewCount = 940,
            iconEmoji = "🧸",
            accentColorHex = 0xFF7B4DFF,
            summary = "Ultra-soft plush bear with soothing heartbeat pulse, gentle starlight glow, and organic lavender sachet.",
            description = "Beloved comfort companion for bedtime and soothing sensory calm. Embroidered facial features with zero plastic buttons to guarantee infant safety.",
            safetyCertifications = listOf("OEKO-TEX Standard 100", "Hypoallergenic", "Baby-Safe Choke-Proof Embroidery"),
            safetyWarnings = listOf("Machine washable (remove battery unit prior to washing)"),
            materials = "100% recycled organic polyester velvet fleece with plush cornfiber filling",
            batteryInfo = "3x AAA batteries in velcro-sealed internal pouch with safety screw lock",
            dimensions = "32 cm height (Weight: 320g)",
            supervisionLevel = "Independent",
            learningObjectives = listOf("Emotional Self-Regulation", "Bedtime Sleep Routine Confidence", "Tactile Calming"),
            inStock = true,
            stockCount = 50,
            isPrelovedAvailable = true
        ),
        Toy(
            id = "toy_9",
            name = "1968 Monza GT Die-Cast Speedster (1:18)",
            brand = "Heritage Apex",
            category = ToyCategory.COLLECTIBLE,
            ageRange = "14+ Adult Collector",
            minAgeYears = 14,
            price = 145.00,
            originalPrice = 160.00,
            rating = 4.98f,
            reviewCount = 144,
            iconEmoji = "🏎️",
            accentColorHex = 0xFFF5C542,
            summary = "Precision die-cast museum scale replica with opening gullwing doors, steerable front wheels, and wire-spoke wheels.",
            description = "Hand-assembled from over 180 individual metal and rubber components. Individually numbered chassis with official certificate of authenticity.",
            safetyCertifications = listOf("Collector Grade Specimen", "Lead-Free Electrostatic Paint"),
            safetyWarnings = listOf("Adult display collectible, not intended as a toy for children under 14"),
            materials = "Zamak zinc alloy die-cast body, genuine rubber tires, polished photo-etched metal parts",
            batteryInfo = "No batteries required",
            dimensions = "26 cm x 11 cm x 7 cm (Weight: 1.4 kg)",
            supervisionLevel = "Adult Collector",
            learningObjectives = listOf("Automotive Design History", "Mechanical Appreciation", "Archival Curation"),
            inStock = true,
            stockCount = 6,
            isCollectorGrade = true,
            conditionGrade = "A+ Mint in Original Packaging"
        )
    )

    private fun sampleChildProfiles(): List<ChildProfile> = listOf(
        ChildProfile(
            id = "child_1",
            nickname = "Leo",
            ageYears = 6,
            favoriteInterests = listOf("Space", "Robots", "Drawing", "Dinosaurs"),
            avatarEmoji = "🚀",
            badgesEarned = listOf("Star Navigator", "Junior Builder", "Curious Scientist"),
            completedChallengesCount = 8
        ),
        ChildProfile(
            id = "child_2",
            nickname = "Maya",
            ageYears = 9,
            favoriteInterests = listOf("STEM", "Architecture", "Board Games", "Nature"),
            avatarEmoji = "🎨",
            badgesEarned = listOf("Master Strategist", "Eco Guardian", "Story Crafter", "Logic Wizard"),
            completedChallengesCount = 15
        )
    )

    private fun sampleActivities(): List<LearningActivity> = listOf(
        LearningActivity(
            id = "act_1",
            title = "Cosmic Lunar Base Challenge",
            category = "Building & Spatial",
            ageRange = "6-12 yrs",
            durationMinutes = 45,
            difficulty = "Medium",
            supervisionRequired = "Mild Supervision",
            materialsNeeded = listOf("Any building blocks / bricks", "Cardboard tube", "Aluminum foil", "Paper tape"),
            stepByStepInstructions = listOf(
                "Step 1: Choose a flat surface and construct an airtight dome base with interlocking blocks.",
                "Step 2: Attach a cardboard tube to create an astronaut airlock decompression chamber.",
                "Step 3: Wrap cardboard scraps in foil to craft reflective solar energy panels.",
                "Step 4: Test your rover access ramp to make sure small toy vehicles can roll into the research bay!"
            ),
            safetyPrecautions = "Use child-safe blunt-tip scissors. Ensure foil edges are folded neatly.",
            learningGoals = "Teaches geometric foundation stability, balance, and spatial volume estimation.",
            rewardBadge = "Lunar Architect"
        ),
        LearningActivity(
            id = "act_2",
            title = "Paper Circuit Firefly Bug",
            category = "Science & Electronics",
            ageRange = "8-14 yrs",
            durationMinutes = 35,
            difficulty = "Easy",
            supervisionRequired = "Adult Present",
            materialsNeeded = listOf("Heavy construction paper", "Copper foil tape or graphite pencil", "3V Coin cell battery", "5mm LED diode"),
            stepByStepInstructions = listOf(
                "Step 1: Draw a big whimsical beetle or firefly on cardstock with folded wings.",
                "Step 2: Trace two parallel lines from the abdomen to the corner edge using graphite or copper tape.",
                "Step 3: Place the long leg of the LED on the positive trace and the short leg on the negative trace.",
                "Step 4: Fold the paper corner over the coin cell battery—watch the firefly glow warmly!"
            ),
            safetyPrecautions = "Adult must supervise coin cell battery handling. Keep away from small children and pets.",
            learningGoals = "Demonstrates closed electrical loops, positive/negative polarity, and conductivity.",
            rewardBadge = "Circuit Master"
        ),
        LearningActivity(
            id = "act_3",
            title = "The Great Detective Mystery Quest",
            category = "Logic & Storytelling",
            ageRange = "5-10 yrs",
            durationMinutes = 30,
            difficulty = "Easy",
            supervisionRequired = "Independent / Collaborative",
            materialsNeeded = listOf("3 household toy figurines", "Index cards with clues", "Magnifying glass"),
            stepByStepInstructions = listOf(
                "Step 1: Place a 'missing treasure' (a colorful block or stone) in a safe secret spot.",
                "Step 2: Write 3 rhyming riddle clues that lead from one toy to the next.",
                "Step 3: Hand the magnifying glass to your family detective and start the timer!",
                "Step 4: Have the child narrate how their detective team solved the mystery."
            ),
            safetyPrecautions = "Keep clues at eye level; avoid climbing or precarious furniture.",
            learningGoals = "Strengthens deductive reasoning, reading comprehension, and oral storytelling.",
            rewardBadge = "Master Sleuth"
        )
    )

    private fun samplePrelovedListings(): List<PrelovedListing> = listOf(
        PrelovedListing(
            id = "pre_1",
            toyTitle = "Technic Heavy Excavator Crane",
            brand = "CosmoBricks",
            category = ToyCategory.BUILDING,
            condition = "Like New - Boxed",
            price = 39.00,
            originalRetail = 79.99,
            sellerCity = "San Francisco, CA",
            verifiedSafe = true,
            description = "Assembled once for a school project and cleanly disassembled into numbered bags. Includes full manual and box in pristine shape.",
            missingPartsNotes = "100% complete, verified with parts checklist."
        ),
        PrelovedListing(
            id = "pre_2",
            toyTitle = "Solid Birch Train Track Expansion Pack (64 Pcs)",
            brand = "Silvano Nature",
            category = ToyCategory.WOODEN,
            condition = "Gently Used",
            price = 22.00,
            originalRetail = 55.00,
            sellerCity = "Austin, TX",
            verifiedSafe = true,
            description = "Beautiful natural wooden train tracks compatible with all standard wooden railway systems. Thoroughly sanitized with organic baby-safe cleaner.",
            missingPartsNotes = "Complete 64 pieces, minor cosmetic wood grain marks."
        )
    )

    private fun sampleCollectorVault(): List<CollectorItem> = listOf(
        CollectorItem(
            id = "col_1",
            title = "1968 Monza GT Die-Cast Speedster (1:18)",
            manufacturer = "Heritage Apex",
            releaseYear = 2021,
            edition = "Limited Studio Run #142 of 500",
            conditionGrade = "A+ Mint in Sealed Box",
            estimatedValue = 185.00,
            purchasePrice = 145.00,
            hasCertificateOfAuthenticity = true,
            isPublicShowcase = true
        ),
        CollectorItem(
            id = "col_2",
            title = "AstroCraft Apollo First Launch Prototype",
            manufacturer = "CosmoBricks",
            releaseYear = 2019,
            edition = "Convention Exclusive Foil Packaging",
            conditionGrade = "Near Mint (Grade 9.4)",
            estimatedValue = 310.00,
            purchasePrice = 110.00,
            hasCertificateOfAuthenticity = true,
            isPublicShowcase = true
        )
    )

    private fun sampleSafetyNotices(): List<SafetyRecallNotice> = listOf(
        SafetyRecallNotice(
            id = "sec_1",
            productTitle = "AstroCraft Modular Line (All Batches 2024-2026)",
            brand = "CosmoBricks",
            noticeType = "CLEAN_RECORD",
            issuingBody = "CPSC (US) & EU Safety Gate",
            dateUpdated = "Updated 2 days ago",
            details = "Full compliance audit passed. Zero choking hazard alerts, zero heavy metals, zero phthalates detected.",
            actionRequired = "No action required. Safe for recommended age groups."
        ),
        SafetyRecallNotice(
            id = "sec_2",
            productTitle = "General Advisory: High-Powered Magnetic Desk Toys",
            brand = "Cross-Brand Industry Advisory",
            noticeType = "ADVISORY",
            issuingBody = "Consumer Product Safety Commission (CPSC)",
            dateUpdated = "Active Global Notice",
            details = "Rare-earth neodymium high-flux magnets must not be given to children under 14. Toyverse Elite bans loose magnetic balls platform-wide.",
            actionRequired = "Always check that magnetic toys utilize sonic-welded, fully encased safety chambers."
        ),
        SafetyRecallNotice(
            id = "sec_3",
            productTitle = "Universal Button Cell & Coin Battery Standards (Reese's Law)",
            brand = "All Electronic Toys",
            noticeType = "CLEAN_RECORD",
            issuingBody = "ANSI / UL 4200A-2023",
            dateUpdated = "Strict Standard Enforcement",
            details = "Every battery compartment on Toyverse Elite electronic toys must require a tool or double-finger squeeze to open.",
            actionRequired = "Ensure replacement batteries are kept in locked storage away from infants."
        )
    )

    private fun sampleOrders(): List<Order> = listOf(
        Order(
            orderId = "TV-8492",
            dateFormatted = "Yesterday",
            items = listOf(
                CartItem(toy = sampleToys()[0], quantity = 1, giftWrap = true, giftMessage = "Happy 8th Birthday Leo! Reach for the stars!"),
                CartItem(toy = sampleToys()[2], quantity = 1)
            ),
            totalAmount = 103.49,
            status = "Out for Delivery (Arriving Today)",
            trackingNumber = "TRK-TV-77382",
            shippingAddress = "742 Evergreen Terrace, Springfield"
        )
    )

    private fun sampleOrbitChat(): List<OrbitMessage> = listOf(
        OrbitMessage(
            id = "init_1",
            isUser = false,
            text = "Welcome to Toyverse Elite! I am ORBIT, your discovery intelligence. Ask me anything about finding the perfect toy, age safety, budget comparisons, or learning activities.",
            timestamp = "Just now",
            suggestedToyIds = listOf("toy_1", "toy_2", "toy_3"),
            safetyTips = "Tip: You can ask me to check safety ratings, compare two products, or find creative activities for rainy days."
        )
    )
}
