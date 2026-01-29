package com.sayali.wishmate.data

object CaptionTemplates {

    data class SearchTag(
        val label: String,  // shown in UI, localized (Marathi/Hindi/English)
        val query: String   // always English, sent to Unsplash
    )

    data class Prompt(
        val id: String,
        val display: String,
        val prompt: String,
        val caption: String,
        val category: String,   // "Morning", "Night", "Birthday", "Anniversary", "Festival"
        val language: String,   // "Marathi", "Hindi", "English"
        val searchTags: List<SearchTag> = emptyList()
    )

    // 🌞 Morning tags (queries always English)
    val morningTagsMr = listOf(
        SearchTag(label = "सूर्योदय", query = "sunrise"),
        SearchTag(label = "सकाळ", query = "morning"),
        SearchTag(label = "प्रकाश", query = "sunlight"),
        SearchTag(label = "चहा", query = "tea"),
        SearchTag(label = "नाश्ता", query = "breakfast"),
        SearchTag(label = "फुले", query = "flowers")
    )

    val morningTagsHi = listOf(
        SearchTag(label = "सूर्योदय", query = "sunrise"),
        SearchTag(label = "सुबह", query = "morning"),
        SearchTag(label = "धूप", query = "sunlight"),
        SearchTag(label = "चाय", query = "tea"),
        SearchTag(label = "नाश्ता", query = "breakfast"),
        SearchTag(label = "फूल", query = "flowers")
    )

    val morningTagsEn = listOf(
        SearchTag(label = "Sunrise", query = "sunrise"),
        SearchTag(label = "Morning", query = "morning"),
        SearchTag(label = "Sunlight", query = "sunlight"),
        SearchTag(label = "Coffee", query = "coffee"),
        SearchTag(label = "Breakfast", query = "breakfast"),
        SearchTag(label = "Flowers", query = "flowers")
    )

    // 🌙 Night tags
    val nightTagsMr = listOf(
        SearchTag(label = "तारे", query = "stars"),
        SearchTag(label = "रात्रीचे आकाश", query = "night-sky"),
        SearchTag(label = "शांत", query = "peaceful"),
        SearchTag(label = "समुद्रावरील सूर्यास्त", query = "sunset-sea"),
        SearchTag(label = "सूर्यास्त", query = "sunset"),
        )

    val nightTagsHi = listOf(
        SearchTag(label = "तारे", query = "stars"),
        SearchTag(label = "रात का आसमान", query = "night sky"),
        SearchTag(label = "सुकून", query = "peaceful"),
        SearchTag(label = "समुद्र पर सूर्यास्त", query = "sunset-sea"),
        SearchTag(label = "सूर्यास्त", query = "sunset"),
    )

    val nightTagsEn = listOf(
        SearchTag(label = "Stars", query = "stars"),
        SearchTag(label = "Night sky", query = "night-sky"),
        SearchTag(label = "Peaceful", query = "peaceful"),
        SearchTag(label = "Sunset sea", query = "sunset-sea"),
        SearchTag(label = "Sunset", query = "sunset"),
    )

    // 🎂 Birthday tags
    val birthdayTagsMr = listOf(
        SearchTag(label = "केक", query = "birthday cake"),
        SearchTag(label = "फुगे", query = "balloons"),
        SearchTag(label = "पार्टी", query = "party"),
        SearchTag(label = "मेणबत्त्या", query = "candles"),
        SearchTag(label = "साजरा", query = "celebration")
    )

    val birthdayTagsHi = listOf(
        SearchTag(label = "बर्थडे केक", query = "birthday cake"),
        SearchTag(label = "गुब्बारे", query = "balloons"),
        SearchTag(label = "पार्टी", query = "party"),
        SearchTag(label = "मोमबत्तियाँ", query = "candles"),
        SearchTag(label = "जश्न", query = "celebration")
    )

    val birthdayTagsEn = listOf(
        SearchTag(label = "Birthday cake", query = "birthday cake"),
        SearchTag(label = "Balloons", query = "balloons"),
        SearchTag(label = "Party", query = "party"),
        SearchTag(label = "Candles", query = "candles"),
        SearchTag(label = "Celebration", query = "celebration")
    )

    // 💍 Anniversary tags
    val anniversaryTagsMr = listOf(
        SearchTag(label = "जोडपे", query = "couple"),
        SearchTag(label = "प्रेम", query = "love"),
        SearchTag(label = "गुलाब", query = "roses"),
        SearchTag(label = "अंगठ्या", query = "rings"),
        SearchTag(label = "रोमँटिक डिनर", query = "romantic dinner")
    )

    val anniversaryTagsHi = listOf(
        SearchTag(label = "जोड़ा", query = "couple"),
        SearchTag(label = "प्यार", query = "love"),
        SearchTag(label = "गुलाब", query = "roses"),
        SearchTag(label = "अंगूठियाँ", query = "rings"),
        SearchTag(label = "रोमांटिक डिनर", query = "romantic dinner")
    )

    val anniversaryTagsEn = listOf(
        SearchTag(label = "Couple", query = "couple"),
        SearchTag(label = "Love", query = "love"),
        SearchTag(label = "Roses", query = "roses"),
        SearchTag(label = "Rings", query = "rings"),
        SearchTag(label = "Romantic dinner", query = "romantic dinner")
    )

    // 🪔 Festival tags
    val festivalTagsMr = listOf(
        SearchTag(label = "उत्सव", query = "festival"),
        SearchTag(label = "सण", query = "festivals"),
        SearchTag(label = "दिवे", query = "diyas"),
        SearchTag(label = "रांगोळी", query = "rangoli")
    )

    val festivalTagsHi = listOf(
        SearchTag(label = "त्योहार", query = "festival"),
        SearchTag(label = "त्योहारों का माहौल", query = "festivals"),
        SearchTag(label = "दीये", query = "diyas"),
        SearchTag(label = "रंगोली", query = "rangoli")
    )

    val festivalTagsEn = listOf(
        SearchTag(label = "Festival", query = "festival"),
        SearchTag(label = "Festivals", query = "festivals"),
        SearchTag(label = "Diyas", query = "diyas"),
        SearchTag(label = "Rangoli", query = "rangoli")
    )

    // 🔹 Prompts grouped by categories & languages
    val prompts = listOf(

        // 🌞 Morning
        Prompt(
            id = "mr_morning_1",
            display = "शुभ सकाळ",
            prompt = "Soft sunrise over Indian village, golden light, dew on leaves, peaceful vibe, UHD",
            caption = "शुभ सकाळ ☀️ नवीन दिवसासाठी प्रेम आणि शांतता!",
            category = "Morning",
            language = "Marathi",
            searchTags = morningTagsMr
        ),
        Prompt(
            id = "hi_morning_1",
            display = "सुप्रभात",
            prompt = "Morning rays through curtains, chai steam, marigold flowers, calm tone, UHD",
            caption = "सुप्रभात 🌼 आज का दिन मंगलमय हो!",
            category = "Morning",
            language = "Hindi",
            searchTags = morningTagsHi
        ),
        Prompt(
            id = "en_morning_1",
            display = "Good Morning",
            prompt = "Bright sunrise over nature, cup of coffee, cozy home feel, 4k realism",
            caption = "Good Morning ☀️ Have a beautiful day ahead!",
            category = "Morning",
            language = "English",
            searchTags = morningTagsEn
        ),

        // 🌙 Night
        Prompt(
            id = "mr_night_1",
            display = "शुभ रात्री",
            prompt = "Moonlight over quiet city, soft stars, minimal style",
            caption = "শुभ রাত्री 🌙 गोड स्वप्नांसाठी!",
            category = "Night",
            language = "Marathi",
            searchTags = nightTagsMr
        ),
        Prompt(
            id = "hi_night_1",
            display = "शुभ रात्रि",
            prompt = "Calm moon over lake, glowing diya, dreamy night sky",
            caption = "शुभ रात्रि 🌙 मीठे सपनों के साथ!",
            category = "Night",
            language = "Hindi",
            searchTags = nightTagsHi
        ),
        Prompt(
            id = "en_night_1",
            display = "Good Night",
            prompt = "Peaceful night sky with stars, cozy bedroom lamp glow, soothing tone",
            caption = "Good Night 🌙 Sweet dreams!",
            category = "Night",
            language = "English",
            searchTags = nightTagsEn
        ),

        // 🎂 Birthday
        Prompt(
            id = "mr_bday_1",
            display = "वाढदिवसाच्या शुभेच्छा",
            prompt = "Colorful birthday cake, confetti, Marathi touch decorations, bright vibe",
            caption = "वाढदिवसाच्या हार्दिक शुभेच्छा 🎂 आनंदी राहा!",
            category = "Birthday",
            language = "Marathi",
            searchTags = birthdayTagsMr
        ),
        Prompt(
            id = "hi_bday_1",
            display = "जन्मदिन मुबारक",
            prompt = "Balloon decorations, soft golden lighting, celebration theme",
            caption = "जन्मदिन मुबारक 🎉 खुश रहो हमेशा!",
            category = "Birthday",
            language = "Hindi",
            searchTags = birthdayTagsHi
        ),
        Prompt(
            id = "en_bday_1",
            display = "Happy Birthday",
            prompt = "Cake with candles, balloons, cheerful colors, UHD",
            caption = "Happy Birthday 🎂 Wishing you joy and smiles!",
            category = "Birthday",
            language = "English",
            searchTags = birthdayTagsEn
        ),

        // 💍 Anniversary
        Prompt(
            id = "mr_anniv_1",
            display = "लग्नाच्या वाढदिवसाच्या शुभेच्छा",
            prompt = "Roses, rings, romantic dinner lights, elegant style",
            caption = "लग्नाच्या वाढदिवसाच्या शुभेच्छा 💕 प्रेम असंच फुलत राहो!",
            category = "Anniversary",
            language = "Marathi",
            searchTags = anniversaryTagsMr
        ),
        Prompt(
            id = "hi_anniv_1",
            display = "शादी की सालगिरह मुबारक",
            prompt = "Candlelight dinner, roses, wedding rings close-up",
            caption = "सालगिरह मुबारक 💖 आपका रिश्ता हमेशा यूँ ही खिलता रहे!",
            category = "Anniversary",
            language = "Hindi",
            searchTags = anniversaryTagsHi
        ),
        Prompt(
            id = "en_anniv_1",
            display = "Happy Anniversary",
            prompt = "Soft romantic lighting, couple silhouette, heart-shaped decor, UHD",
            caption = "Happy Anniversary 💕 Wishing you endless love!",
            category = "Anniversary",
            language = "English",
            searchTags = anniversaryTagsEn
        ),
        Prompt(
            id = "en_fest_generic",
            display = "Festival Wishes",
            prompt = "Indian festival celebration, traditional decor, warm lighting, elegant festive mood",
            caption = "Wishing you joy, peace, and happiness on this special occasion ✨",
            category = "Festival",
            language = "English",
            searchTags = festivalTagsEn
        ),
        Prompt(
            id = "hi_fest_generic",
            display = "त्योहार की शुभकामनाएं",
            prompt = "भारतीय त्योहार, पारंपरिक सजावट, दीप, रंगोली, उत्सव का माहौल",
            caption = "इस शुभ अवसर पर आपको और आपके परिवार को हार्दिक शुभकामनाएं ✨",
            category = "Festival",
            language = "Hindi",
            searchTags = festivalTagsHi
        ),
        Prompt(
                id = "mr_fest_generic",
            display = "सणाच्या शुभेच्छा",
            prompt = "भारतीय सण, दिवे, रांगोळी, पारंपरिक सजावट, आनंदी वातावरण",
            caption = "या शुभ प्रसंगी तुम्हाला आणि तुमच्या परिवाराला मनःपूर्वक शुभेच्छा ✨",
            category = "Festival",
            language = "Marathi",
            searchTags = festivalTagsMr
        )


    )

    private fun normalizeCategory(raw: String?): String {
        return when (raw?.trim()?.lowercase()) {
            "morning", "good morning" -> "Morning"
            "night", "good night"     -> "Night"
            "birthday", "bday"        -> "Birthday"
            "anniversary", "anniv"    -> "Anniversary"
            "festival", "festivals", "fest" -> "Festival"
            else -> raw?.trim().orEmpty()
        }
    }

    private fun normalizeLanguage(raw: String?): String {
        return when (raw?.trim()?.lowercase()) {
            "english", "en" -> "English"
            "hindi", "hi"   -> "Hindi"
            "marathi", "mr" -> "Marathi"
            else -> raw?.trim().orEmpty()
        }
    }

    fun getPromptsFor(language: String?, category: String?): List<Prompt> {
        val lang = normalizeLanguage(language)
        val cat  = normalizeCategory(category)

        val result = prompts.filter { p ->
            p.language.trim() == lang && p.category.trim() == cat
        }
        return result
    }

    fun getCategoriesForLanguage(language: String): List<String> {
        return prompts
            .filter { it.language.equals(language, ignoreCase = true) }
            .map { it.category }
            .distinct()
    }
    fun getDefaultFestivalPromptId(language: String): String {
        val lang = normalizeLanguage(language)

        return prompts.firstOrNull {
            it.category == "Festival" &&
                    it.language == lang &&
                    it.id.contains("generic")
        }?.id
            ?: prompts.firstOrNull {
                it.category == "Festival" && it.language == lang
            }?.id
            ?: prompts.firstOrNull { it.language == lang }?.id
            ?: prompts.first().id
    }

}
