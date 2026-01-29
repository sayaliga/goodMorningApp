package com.sayali.wishmate

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.posthog.PostHog
import com.sayali.wishmate.data.CaptionTemplates
import com.sayali.wishmate.imagegen.ImageRepository
import com.sayali.wishmate.imagegen.PexelsImageRepository
import com.sayali.wishmate.theme.BgGradientEnd
import com.sayali.wishmate.theme.BgGradientStart
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerScreen(
    id: String,
    navController: NavHostController,
    searchTerm: String = ""
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val gridState = rememberLazyGridState()

    var images by remember { mutableStateOf<List<String>>(emptyList()) }
    var page by remember { mutableStateOf(1) }
    var canLoadMore by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(true) }
    var isLoadingMore by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val prompt = remember(id) {
        CaptionTemplates.prompts.firstOrNull { it.id == id }
            ?: CaptionTemplates.prompts.first()
    }

    val promptStyle = remember { PromptStyles.forPrompt(prompt) }

    fun buildQueries(): List<String> {
        return if (searchTerm.isNotBlank()) {
            listOf(searchTerm)
        } else {
            prompt.searchTags.map { it.query }
        }
    }

    suspend fun loadFirstPage() {
        isLoading = true
        error = null
        images = emptyList()
        page = 1
        canLoadMore = true

        try {
            val res = ImageRepository.fetchImageUrlsPaged(
                queries = buildQueries(),
                page = 1,
                perPage = 30
            )
            images = res.urls
            page = 2
            canLoadMore = res.hasNext
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            error = e.message ?: "Something went wrong"
            canLoadMore = false
        } finally {
            isLoading = false
        }
    }

    suspend fun loadMore() {
        if (!canLoadMore || isLoadingMore || isLoading) return
        isLoadingMore = true
        error = null

        try {
            Log.d("PAGING", "Unsplash Loading page=$page currentCount=${images.size}")
            val res = ImageRepository.fetchImageUrlsPaged(
                queries = buildQueries(),
                page = page,
                perPage = 30
            )
            images = images + res.urls
            page += 1
            canLoadMore = res.hasNext
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            error = e.message ?: "Something went wrong"
            canLoadMore = false
        } finally {
            isLoadingMore = false
        }
    }


//    suspend fun loadMore() {
//        if (!canLoadMore || isLoadingMore || isLoading) return
//        isLoadingMore = true
//        error = null
//
//        try {
//            Log.d("PAGING", "Loading page=$page currentCount=${images.size}")
//            val res = PexelsImageRepository.fetchImageUrlsPaged(
//                queries = buildQueries(),
//                page = page,
//                perPage = 30
//            )
//            images = images + res.urls
//            page += 1
//            canLoadMore = res.hasNext
//        } catch (e: CancellationException) {
//            throw e
//        } catch (e: Exception) {
//            error = e.message ?: "Something went wrong"
//            canLoadMore = false
//        } finally {
//            isLoadingMore = false
//        }
//    }

    // Reload whenever id or searchTerm changes
    LaunchedEffect(id, searchTerm) {
        loadFirstPage()
        gridState.scrollToItem(0)
    }

    /**
     * ✅ Stable infinite scroll watcher:
     * - Uses collect (NOT collectLatest) so scrolling doesn't cancel ongoing work.
     * - Launches loadMore in a separate coroutine, guarded by isLoadingMore.
     */
    LaunchedEffect(Unit) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .filterNotNull()
            .distinctUntilChanged()
            .collect { lastVisibleIndex ->
                val triggerIndex = images.size - 6
                if (triggerIndex >= 0 &&
                    lastVisibleIndex >= triggerIndex &&
                    canLoadMore &&
                    !isLoading &&
                    !isLoadingMore
                ) {
                    scope.launch { loadMore() }
                }
            }
    }

    ScreenScaffold(
        title = "Choose a photo",
        showTopBar = true,
        onBack = { navController.popBackStack() }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(BgGradientStart, BgGradientEnd)))
                .padding(padding)
        ) {
            when {
                isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = promptStyle.color)
                    }
                }

                error != null && images.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = error ?: "Something went wrong",
                                color = Color.White
                            )
                            Spacer(Modifier.height(12.dp))
                            Button(onClick = { scope.launch { loadFirstPage() } }) {
                                Text("Retry")
                            }
                        }
                    }
                }

                else -> {
                    LazyVerticalGrid(
                        state = gridState,
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        itemsIndexed(images) { _, url ->
                            Card(
                                modifier = Modifier
                                    .aspectRatio(3f / 4f)
                                    .clickable {
                                        navController.previousBackStackEntry
                                            ?.savedStateHandle
                                            ?.set("selectedImageUrl", url)
                                        navController.popBackStack()
                                        PostHog.capture(
                                            event = "image_selected",
                                            properties = mapOf(
                                                "source" to "unsplash"
                                            )
                                        )

                                    },
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(url),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        if (isLoadingMore) {
                            item(span = { GridItemSpan(2) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = promptStyle.color)
                                }
                            }
                        }

                        if (error != null && images.isNotEmpty()) {
                            item(span = { GridItemSpan(2) }) {
                                Text(
                                    text = error ?: "",
                                    color = Color.White.copy(alpha = 0.9f),
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
