package com.sayali.wishmate

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.res.stringResource
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.sayali.wishmate.settings.LanguageViewModel
import com.sayali.wishmate.theme.MorningYellow
import com.sayali.wishmate.theme.TextBrown
import java.util.concurrent.TimeUnit
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.sayali.wishmate.theme.NightBlue
import com.posthog.PostHog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, languageViewModel: LanguageViewModel) {

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    val labelMobile = stringResource(R.string.label_mobile_number)
    val labelEnterOtp = stringResource(R.string.label_enter_otp)
    val btnSendOtp = stringResource(R.string.btn_send_otp)
    val btnVerifyOtp = stringResource(R.string.btn_verify_otp)
    val toastInvalidNumber = stringResource(R.string.toast_invalid_number)
    val toastInvalidOtp = stringResource(R.string.toast_invalid_otp)

    val session = remember { SessionManager(context) }
    var isSendingOtp by remember { mutableStateOf(false) }

    val currentLang by languageViewModel.currentLanguage.collectAsState()

    var phoneNumber by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf<String?>(null) }
    var otp by remember { mutableStateOf("") }
    var isOtpSent by remember { mutableStateOf(false) }

    fun identifyUserAfterLogin() {
        val user = FirebaseAuth.getInstance().currentUser ?: return

        PostHog.identify(
            distinctId = user.uid,
            userProperties = mapOf(
                "language" to (currentLang?.displayName ?: "English"),
                "login_method" to "otp"
                // ⚠️ Avoid sending full phone number in production
            )
        )


        PostHog.capture("login_success")
    }

    fun navigateAfterLogin() {
        session.setLoggedIn(phoneNumber)
        identifyUserAfterLogin()

//        if (currentLang == null) {
//            navController.navigate("language_selector") {
//                popUpTo("login") { inclusive = true }
//            }
//        } else {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        //}
    }

    // Detect light/dark theme
    val isDark = isSystemInDarkTheme()

    // Card background based on theme
    val cardBg = if (isDark) Color(0xFFFDF7F7) else Color.White

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NightBlue)  // ⭐ background as requested
            .padding(0.dp)
    ) {
        // ⭐ Illustration at the top
        Image(
            painter = painterResource(R.drawable.login_illustration_vertical),
            contentDescription = "Welcome Image",
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp)
                .height(500.dp), // adjust as needed
            contentScale = ContentScale.Fit
        )

        // ⭐ Login card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .align(Alignment.BottomCenter).
                padding(bottom = 40.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "WishMate App Icon",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(10.dp)
                )

                if (!isOtpSent) {
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text(labelMobile, color = TextBrown) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (phoneNumber.length == 10) {
                                isSendingOtp = true
                                val options = PhoneAuthOptions.newBuilder(auth)
                                    .setPhoneNumber("+91$phoneNumber")
                                    .setTimeout(60L, TimeUnit.SECONDS)
                                    .setActivity(context as Activity)
                                    .setCallbacks(object :
                                        PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                            isSendingOtp = false
                                            auth.signInWithCredential(credential)
                                                .addOnSuccessListener { navigateAfterLogin() }
                                                .addOnFailureListener {
                                                    Toast.makeText(context, "Auto verification failed", Toast.LENGTH_SHORT).show()
                                                }
                                        }

                                        override fun onVerificationFailed(e: FirebaseException) {
                                            isSendingOtp = false
                                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }

                                        override fun onCodeSent(
                                            verId: String,
                                            token: PhoneAuthProvider.ForceResendingToken
                                        ) {
                                            isSendingOtp = false
                                            verificationId = verId
                                            isOtpSent = true
                                        }
                                    })
                                    .build()
                                PhoneAuthProvider.verifyPhoneNumber(options)
                            } else {
                                Toast.makeText(context, toastInvalidNumber, Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(0.9f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NightBlue,
                            contentColor = Color.White
                        ),
                        enabled = !isSendingOtp
                    ) {
                        Text(btnSendOtp)
                    }

                } else {

                    OutlinedTextField(
                        value = otp,
                        onValueChange = { otp = it },
                        label = { Text(labelEnterOtp, color = TextBrown) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = {
                            val id = verificationId
                            if (id == null) {
                                Toast.makeText(context, "Verification ID missing", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            val credential = PhoneAuthProvider.getCredential(id, otp)
                            auth.signInWithCredential(credential)
                                .addOnSuccessListener { navigateAfterLogin() }
                                .addOnFailureListener {
                                    Toast.makeText(context, toastInvalidOtp, Toast.LENGTH_SHORT).show()
                                }
                        },
                        modifier = Modifier.fillMaxWidth(0.9f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NightBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(btnVerifyOtp)
                    }
                }
            }
        }
    }
}
