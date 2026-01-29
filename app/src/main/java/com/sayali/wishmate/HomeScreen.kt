package com.sayali.wishmate

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sayali.wishmate.data.CaptionTemplates
import com.sayali.wishmate.settings.LanguageViewModel
import com.sayali.wishmate.theme.BgGradientEnd
import com.sayali.wishmate.theme.BgGradientStart
import com.sayali.wishmate.theme.MorningYellow
import com.sayali.wishmate.theme.NightBlue
import com.sayali.wishmate.theme.TextBrown
import com.sayali.wishmate.ui.LanguageDropdown
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.FilterChipDefaults
import com.posthog.PostHog
import com.sayali.wishmate.BuildConfig
import com.sayali.wishmate.calendar.FestivalEventsViewModel
import com.sayali.wishmate.calendar.GoogleFestivalCalendarRepository

@Composable
private fun FestivalSection(
    uiState: com.sayali.wishmate.calendar.FestivalUiState,
    onFestivalClick: (String) -> Unit
) {
    when {
        uiState.isLoading -> Text("Loading festivals…")
        uiState.error != null -> Text("Festival error: ${uiState.error}")
        uiState.events.isEmpty() -> Text("No festivals found for next 7 days.")
        else -> {
            Text("Upcoming festivals", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            uiState.events.forEach { ev ->
                Button(
                    onClick = { onFestivalClick(ev.title) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.88f),
                        contentColor = TextBrown
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = ev.title,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = ev.date,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextBrown.copy(alpha = 0.7f)
                        )
                    }

                    Text(
                        text = "Wish →",
                        style = MaterialTheme.typography.labelLarge,
                        color = NightBlue
                    )
                }

                Spacer(Modifier.height(10.dp))
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(navController: NavController, languageViewModel: LanguageViewModel) {
    val context = LocalContext.current

    val title = stringResource(R.string.title_home)
    val logoutText = stringResource(R.string.btn_logout)
    val generateGreetingText = stringResource(R.string.btn_generate_greeting)

    val currentLang by languageViewModel.currentLanguage.collectAsState()

    // Fallback to English until DataStore emits something
    val languageName = currentLang?.displayName ?: "English"

    val categories = CaptionTemplates.getCategoriesForLanguage(languageName).filterNot { it -> it == "Festival" }
    var selectedPrompt by remember { mutableStateOf<CaptionTemplates.Prompt?>(null) }

    // 🔹 This holds the selected Unsplash query (always English)
    var selectedTagQuery by remember { mutableStateOf<String?>(null) }
    val defaultFestivalPromptId = remember(languageName) {
        CaptionTemplates.getDefaultFestivalPromptId(languageName)
    }

    val session = remember { SessionManager(context) }

    val repo = remember {
        GoogleFestivalCalendarRepository(
            calendarIdRaw = BuildConfig.FESTIVAL_CALENDAR_ID,
            apiKey = BuildConfig.GOOGLE_CALENDAR_API_KEY
        )
    }

    val vm = remember { FestivalEventsViewModel(repo) }
    val uiState by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadNextDays(7)
    }




    ScreenScaffold(
        title = title,
        showTopBar = true,
        onBack = { navController.popBackStack() },
        actions = {
            TextButton(onClick = {
                session.logout()
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }) {
                Text(logoutText, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { padding ->

        // 🔹 Full-screen gradient background under the content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(BgGradientStart, BgGradientEnd)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // language dropdown
                LanguageDropdown(
                    current = languageName,
                    onLanguageSelected = { newLang ->
                        languageViewModel.setLanguage(newLang)
                        selectedPrompt = null
                        selectedTagQuery = null
                    }
                )

                Spacer(Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item {
                        FestivalSection(
                            uiState = uiState,
                            onFestivalClick = { festivalTitle ->

                                val encodedTag = java.net.URLEncoder.encode(festivalTitle, "UTF-8")
                                navController.navigate("editor/$defaultFestivalPromptId?tag=$encodedTag")
                                PostHog.capture(
                                    event = "festival_selected",
                                    properties = mapOf(
                                        "festival_name" to festivalTitle
                                    )
                                )
                            }
                        )
                        Spacer(Modifier.height(12.dp))
                        Divider(color = Color.White.copy(alpha = 0.4f))
                    }

                    items(categories) { category ->
                        val categoryPrompts = CaptionTemplates.getPromptsFor(languageName, category)

                        Column {
                            PromptGrid(
                                prompts = categoryPrompts,
                                onClick = { selected ->
                                    selectedPrompt = selected
                                    selectedTagQuery = null
                                    PostHog.capture(
                                        event = "prompt_selected",
                                        properties = mapOf(
                                            "prompt_id" to selected.id,
                                            "category" to selected.category,
                                            "language" to selected.language
                                        )
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                            if (selectedPrompt != null && categoryPrompts.contains(selectedPrompt)) {
                                val tags = selectedPrompt!!.searchTags
                                if (tags.isNotEmpty()) {
                                    Spacer(Modifier.height(12.dp))

                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        tags.forEach { tag ->
                                            val selected = selectedTagQuery == tag.query

                                            FilterChip(
                                                selected = selected,
                                                onClick = {
                                                    selectedTagQuery = tag.query
                                                },
                                                label = {
                                                    Text(tag.label)
                                                },
                                                colors = FilterChipDefaults.filterChipColors(
                                                    containerColor = Color.White.copy(alpha = 0.85f),
                                                    labelColor = TextBrown,
                                                    selectedContainerColor = NightBlue,
                                                    selectedLabelColor = Color.White
                                                )
                                            )
                                        }
                                    }

                                    Spacer(Modifier.height(16.dp))

                                    Button(
                                        onClick = {
                                            selectedPrompt?.let { prompt ->
                                                selectedTagQuery?.let { query ->
                                                    navController.navigate("editor/${prompt.id}?tag=$query")
                                                }
                                            }
                                        },
                                        enabled = selectedTagQuery != null,
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = NightBlue,
                                            contentColor = Color.White,
                                            disabledContainerColor = NightBlue.copy(alpha = 0.4f),
                                            disabledContentColor = Color.Black.copy(alpha = 0.4f)
                                        )
                                    ) {
                                        Text(generateGreetingText)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
