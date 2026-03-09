package com.example.goodmorning.content

enum class GenerationTone(
    val label: String,
    val instruction: String
) {
    PLAYFUL_LIGHT(
        label = "Playful light",
        instruction = "Write one sentence that feels playful and warm, with a light flirtiness, while avoiding childish jokes, slang overload, or anything that sounds forced."
    ),
    CLEAN_WARM(
        label = "Clean warm",
        instruction = "Write one sentence that feels concise, polished, and emotionally warm, using simple language without sounding cold, robotic, or flat."
    ),
    ROMANTIC_GROUNDED(
        label = "Romantic grounded",
        instruction = "Write one sentence that feels romantic and sincere, with real emotional weight, while avoiding cheesy metaphors, exaggeration, or overly dramatic phrasing."
    ),
    CONFIDENT_SOFT(
        label = "Confident soft",
        instruction = "Write one sentence that feels confident, calm, and attractive, with gentle emotional clarity and no aggressive, pushy, or overly poetic language."
    )
}

data class PromptTemplate(
    val title: String,
    val useCase: String,
    val tone: GenerationTone
) {
    fun buildPrompt(): String = buildString {
        appendLine("You are generating a short Wishmate text.")
        appendLine("Goal: $useCase")
        appendLine("Tone instruction: ${tone.instruction}")
        append("Output exactly one sentence.")
    }
}

val samplePromptTemplates = listOf(
    PromptTemplate(
        title = "Morning spark",
        useCase = "Send a sweet good-morning text that feels fresh and easy to reply to.",
        tone = GenerationTone.PLAYFUL_LIGHT
    ),
    PromptTemplate(
        title = "Soft reassurance",
        useCase = "Send a calming text that makes the other person feel seen and cared for.",
        tone = GenerationTone.CLEAN_WARM
    ),
    PromptTemplate(
        title = "Heartfelt check-in",
        useCase = "Send a romantic message that feels intimate, genuine, and emotionally present.",
        tone = GenerationTone.ROMANTIC_GROUNDED
    ),
    PromptTemplate(
        title = "Smooth follow-up",
        useCase = "Send a confident message that keeps momentum without sounding needy or performative.",
        tone = GenerationTone.CONFIDENT_SOFT
    )
)
