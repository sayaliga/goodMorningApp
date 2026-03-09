package com.example.goodmorning.content

enum class GenerationTone(
    val selectorLabel: String,
    val promptInstruction: String
) {
    WARM(
        selectorLabel = "Warm",
        promptInstruction = "Make it emotionally warm, natural, and easy to receive, with clean modern wording and no generic filler, robotic softness, or flat phrasing."
    ),
    PLAYFUL(
        selectorLabel = "Playful",
        promptInstruction = "Make it light, charming, and slightly flirty, with natural energy and no childish humor, meme language, or anything that feels try-hard."
    ),
    ROMANTIC(
        selectorLabel = "Romantic",
        promptInstruction = "Make it intimate, sincere, and emotionally grounded, with genuine affection and no cheesy metaphors, dramatic exaggeration, or predictable clichés."
    ),
    POISED(
        selectorLabel = "Poised",
        promptInstruction = "Make it smooth, confident, and emotionally clear, with a calm attractive tone and no pushiness, arrogance, or overly poetic wording."
    )
}

data class PromptTemplate(
    val title: String,
    val useCase: String,
    val tone: GenerationTone
) {
    fun buildPrompt(): String = buildString {
        appendLine("You are generating a Wishmate message.")
        appendLine("Goal: $useCase")
        appendLine("Tone: ${tone.promptInstruction}")
        appendLine("Quality bar: the line should feel personal, specific in emotion, smooth to read, and immediately sendable.")
        appendLine("Avoid anything cheesy, generic, cringe, stiff, overly formal, or obviously AI-written.")
        append("Output exactly one sentence.")
    }
}

val samplePromptTemplates = listOf(
    PromptTemplate(
        title = "Morning spark",
        useCase = "Send a sweet good-morning text that feels fresh and easy to reply to.",
        tone = GenerationTone.PLAYFUL
    ),
    PromptTemplate(
        title = "Soft reassurance",
        useCase = "Send a calming text that makes the other person feel seen and cared for.",
        tone = GenerationTone.WARM
    ),
    PromptTemplate(
        title = "Heartfelt check-in",
        useCase = "Send a romantic message that feels intimate, genuine, and emotionally present.",
        tone = GenerationTone.ROMANTIC
    ),
    PromptTemplate(
        title = "Smooth follow-up",
        useCase = "Send a confident message that keeps momentum without sounding needy or performative.",
        tone = GenerationTone.POISED
    )
)
