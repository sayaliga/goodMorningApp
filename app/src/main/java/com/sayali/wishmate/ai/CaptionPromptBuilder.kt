package com.sayali.wishmate.ai

object CaptionPromptBuilder {
    fun build(
        occasion: String,
        topic: String,
        toneInstruction: String,
        language: String,
        additionalInstructions: String = ""
    ): String {
        val normalizedTopic = topic.trim().ifBlank { "general" }
        val normalizedInstructions = additionalInstructions.trim()
        val additionalInstructionsBlock = if (normalizedInstructions.isNotEmpty()) {
            "- Additional user instructions: $normalizedInstructions\n"
        } else {
            ""
        }

        return """
            You write short greeting messages for social sharing.

            Context:
            - Occasion: $occasion
            - Topic/keywords: $normalizedTopic
            - Tone: $toneInstruction
            - Language: $language
            - Audience: Indian users sharing on WhatsApp and social apps
            $additionalInstructionsBlock

            Writing goals:
            - Sound warm, natural, and human
            - Match the requested occasion and tone
            - Keep the wording easy to read and easy to forward
            - If the topic is generic, keep the message versatile and broadly usable

            Hard rules:
            - Return exactly one final message only
            - Maximum 3 short lines
            - Use 1 or 2 relevant emojis at most
            - Do not use hashtags, @mentions, links, phone numbers, or signatures
            - Do not mention AI, prompts, generation, or being an assistant
            - Avoid offensive, political, religiously divisive, or adult content
            - Avoid heavy slang, internet shorthand, and ALL CAPS
            - No quotes, labels, bullet points, explanations, or alternatives

            Quality bar:
            - Prefer clear, heartfelt wording over generic filler
            - Avoid repetition and awkward punctuation
            - Make the message feel ready to copy and share
        """.trimIndent()
    }
}
