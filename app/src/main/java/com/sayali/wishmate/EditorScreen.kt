package com.sayali.wishmate

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.sayali.wishmate.ai.CaptionPromptBuilder
import com.sayali.wishmate.ai.TextGenerator
import com.sayali.wishmate.data.CaptionTemplates
import com.sayali.wishmate.share.ShareUtils
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.sayali.wishmate.theme.BgGradientEnd
import com.sayali.wishmate.theme.BgGradientStart
import com.sayali.wishmate.theme.MorningYellow
import com.sayali.wishmate.theme.NightBlue
import com.sayali.wishmate.theme.TextBrown
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.posthog.PostHog
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    id: String,
    navController: NavHostController,
    searchTerm: String = ""
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val session = remember { SessionManager(ctx) }
    var selectedToneId by remember { mutableStateOf("fun") }
    var showSheet by remember { mutableStateOf(false) }
    var additionalInstructions by remember { mutableStateOf("") }

    val tones = listOf(
        "fun" to stringResource(R.string.tone_fun),
        "minimal" to stringResource(R.string.tone_minimal),
        "formal" to stringResource(R.string.tone_formal),
        "emotional" to stringResource(R.string.tone_emotional),
        "poetic" to stringResource(R.string.tone_poetic),
        "short_sweet" to stringResource(R.string.tone_short_sweet)
    )
    val toneInstruction = when (selectedToneId) {
        "fun" -> "in a fun, playful tone"
        "minimal" -> "in a short, minimal and clean tone"
        "formal" -> "in a warm yet formal tone"
        "emotional" -> "in a heartfelt, emotional tone"
        "poetic" -> "in a poetic, lyrical tone"
        "short_sweet" -> "in a short and sweet, minimal tone"
        else -> "in a simple, friendly tone"
    }

    val generateGreetingText = stringResource(R.string.btn_generate_greeting)
    val shareGreetingText = stringResource(R.string.btn_share_greeting)
    val limitReachedErrorText = stringResource(R.string.limit_reached_error)
//    val shareToOthersText = stringResource(R.string.share_other_apps)
    val copyToClipBoardText = stringResource(R.string.copy_message)
    val additionalInstructionsLabel = stringResource(R.string.label_additional_instructions)
    val additionalInstructionsHint = stringResource(R.string.hint_additional_instructions)
    val additionalInstructionsLimitText = stringResource(R.string.additional_instructions_limit)

    val clipboardManager = LocalClipboardManager.current

    val prompt = remember(id) {
        CaptionTemplates.prompts.firstOrNull { it.id == id }
            ?: CaptionTemplates.prompts.first()
    }

    val promptStyle = remember(prompt.id) { PromptStyles.forPrompt(prompt) }

    var caption by remember { mutableStateOf(prompt.caption) }
    var selectedImage by remember { mutableStateOf<String?>(null) }
    var isGenerating by remember { mutableStateOf(false) }
    var showPhotoSourceDialog by remember { mutableStateOf(false) }
    var cameraPhotoUri by remember { mutableStateOf<Uri?>(null) }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && cameraPhotoUri != null) {
            selectedImage = cameraPhotoUri.toString()
            PostHog.capture(
                event = "image_selected",
                properties = mapOf("source" to "camera")
            )
        }
    }

    // Gallery picker launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImage = uri.toString()
            PostHog.capture(
                event = "image_selected",
                properties = mapOf("source" to "gallery")
            )
        }
    }

    // Function to create temp file and launch camera
    fun launchCamera() {
        val photoDir = File(ctx.cacheDir, "camera_photos")
        photoDir.mkdirs()
        val photoFile = File(photoDir, "${System.currentTimeMillis()}.jpg")
        val uri = FileProvider.getUriForFile(ctx, "${ctx.packageName}.fileprovider", photoFile)
        cameraPhotoUri = uri
        cameraLauncher.launch(uri)
    }

    // Camera permission launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            launchCamera()
        } else {
            Toast.makeText(ctx, "Camera permission required", Toast.LENGTH_SHORT).show()
        }
    }

    fun handleTakePhoto() {
        when {
            ContextCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED -> {
                launchCamera()
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    // 🔄 Listen for image selected from ImagePickerScreen via SavedStateHandle
    val currentBackStackEntry = navController.currentBackStackEntry
    val savedStateHandle = currentBackStackEntry?.savedStateHandle
    val pickedImageUrl: String? = savedStateHandle?.get<String>("selectedImageUrl")

    LaunchedEffect(pickedImageUrl) {
        if (pickedImageUrl != null) {
            selectedImage = pickedImageUrl
            // clear it so it doesn't retrigger
            savedStateHandle?.remove<String>("selectedImageUrl")
        }
    }

    if (showSheet) {
        ModalBottomSheet(onDismissRequest = { showSheet = false }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp), // Add some padding
                verticalArrangement = Arrangement.spacedBy(12.dp), // Space between buttons
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Share to WhatsApp
//                Button(
//                    modifier = Modifier.fillMaxWidth(),
//                    enabled = selectedImage != null,
//                    onClick = {
//                        scope.launch {
//                            selectedImage?.let { url ->
//                                ShareUtils.shareToWhatsApp(ctx, url, caption)
//                            }
//                            showSheet = false
//                        }
//                    }
//                ) {
//                    Text(shareToWhatsAppText)
//                }

                // 2. Share to Other Apps
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        scope.launch {
                            selectedImage?.let { url ->
                                // Note: Generic handler since whatsapp share does not share caption
                                ShareUtils.shareToOtherApps(ctx, url, caption)
                            }
                            showSheet = false
                            PostHog.capture(
                                event = "greeting_shared",
                                properties = mapOf(
                                    "channel" to "whatsapp",
                                    "has_image" to true,
                                    "has_text" to true
                                )
                            )
                        }
                    }
                ) {
                    Text(shareGreetingText)
                }

                // 3. Copy Caption Button (New)
                OutlinedButton( // Use OutlinedButton to distinguish it visually
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        clipboardManager.nativeClipboard.setText(AnnotatedString(caption))
                        Toast.makeText(ctx, "Caption copied to clipboard!", Toast.LENGTH_SHORT).show()
                        showSheet = false
                        PostHog.capture(
                            event = "greeting_copied",
                            properties = mapOf(
                                "has_text" to true
                            )
                        )
                    }
                ) {
                    Text(copyToClipBoardText)
                }

                Spacer(modifier = Modifier.height(24.dp)) // Bottom spacing
            }
        }
    }

    // Photo source selection bottom sheet
    if (showPhotoSourceDialog) {
        ModalBottomSheet(onDismissRequest = { showPhotoSourceDialog = false }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.choose_image_source),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Take Photo button
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        showPhotoSourceDialog = false
                        handleTakePhoto()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NightBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text(stringResource(R.string.take_photo))
                }

                // Choose from Gallery button
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        showPhotoSourceDialog = false
                        galleryLauncher.launch("image/*")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NightBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text(stringResource(R.string.choose_from_gallery))
                }

                // Browse Stock Images button
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        showPhotoSourceDialog = false
                        val tagParam = if (searchTerm.isNotEmpty()) searchTerm else ""
                        navController.navigate("image_picker/${prompt.id}?tag=$tagParam")
                    }
                ) {
                    Text(stringResource(R.string.browse_stock_images))
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    ScreenScaffold(
        title = prompt.display,
        showTopBar = true,
        onBack = { navController.popBackStack() },
        actions = {
            TextButton(
                onClick = {
                    session.logout()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            ) {
                Text("Logout")
            }
        }
    ) { innerPadding ->

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
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // 🔵 TOP: Selected Image (50% height)
                Surface(
                    modifier = Modifier
                        .weight(0.30f)   // 50% height
                        .fillMaxWidth(),
                    tonalElevation = 2.dp,
                    shape = MaterialTheme.shapes.medium,
                    color = promptStyle.color.copy(alpha = 0.15f)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedImage != null) {
                            Image(
                                painter = rememberAsyncImagePainter(selectedImage),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .padding(16.dp)
                            )
                        } else {
                            Text(
                                text = "No image selected",
                                color = promptStyle.textColor,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                // 🟡 BUTTON: Choose Photo
                Button(
                    onClick = {
                        showPhotoSourceDialog = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NightBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text("Choose a Photo")
                }

                // 🔶 BOTTOM 50%: Caption + Generate + Share
                Surface(
                    modifier = Modifier
                        .weight(0.70f)   // 50% height
                        .fillMaxWidth(),
                    tonalElevation = 4.dp,
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {

                        OutlinedTextField(
                            value = caption,
                            onValueChange = { caption = it },
                            label = { Text("Caption") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),   // caption takes remaining space of bottom half
                        )

                        // 🔹 Tone Chips Row
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            tones.forEach { (id, label) ->
                                FilterChip(
                                    selected = selectedToneId == id,
                                    onClick = { selectedToneId = id },
                                    label = {
                                        Text(
                                            label,
                                            fontSize = 12.sp,  // ⭐ smaller text
                                            maxLines = 1
                                        )
                                    }
                                )
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = additionalInstructions,
                            onValueChange = {
                                additionalInstructions = it.take(200)
                            },
                            label = { Text(additionalInstructionsLabel) },
                            placeholder = { Text(additionalInstructionsHint) },
                            supportingText = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(additionalInstructionsLimitText)
                                    Text("${additionalInstructions.length}/200")
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = false,
                            minLines = 2,
                            maxLines = 4
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                            // Generate caption
                            Button(
                                onClick = {
                                    if (isGenerating) return@Button
                                    scope.launch {
                                        if (session.canGenerateCaption()) {
                                            try {
                                                    isGenerating = true

                                                    val aiText = TextGenerator.generateCaption(
                                                        CaptionPromptBuilder.build(
                                                            occasion = prompt.display,
                                                            topic = searchTerm,
                                                            toneInstruction = toneInstruction,
                                                            language = prompt.language,
                                                            additionalInstructions = additionalInstructions
                                                        ),
                                                        prompt.language
                                                    )

                                                    if (!aiText.isNullOrEmpty()) {
                                                        caption = aiText
                                                        session.incrementCaptionCount()
                                                    } else {
                                                        Toast.makeText(
                                                            ctx,
                                                            "Could not generate caption. Please try again.",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                } finally {
                                                    isGenerating = false
                                                }
                                        } else {
                                            Toast.makeText(
                                                ctx,
                                                limitReachedErrorText,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MorningYellow,
                                    contentColor = TextBrown
                                ),
                                enabled = session.canGenerateCaption() && !isGenerating,
                            ) {
                                Text(generateGreetingText)
                            }
                            if (!session.canGenerateCaption()) {
                                Text(
                                    text = stringResource(R.string.limit_reached_error),
                                    color = Color.Red.copy(alpha = 0.7f),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            // Share
                            Button(
                                onClick = {
                                    showSheet = true
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NightBlue,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(shareGreetingText)
                            }
                        }
                    }
                }
            }
        }
    }
}
