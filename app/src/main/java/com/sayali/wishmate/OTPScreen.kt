package com.sayali.wishmate

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTPScreen(navController: NavController, verificationId: String) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    var otp by remember { mutableStateOf("") }

    Scaffold(topBar = { TopAppBar(title = { Text("Verify OTP") }) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = otp,
                onValueChange = { otp = it },
                label = { Text("Enter OTP") },
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val credential = PhoneAuthProvider.getCredential(verificationId, otp)
                    auth.signInWithCredential(credential)
                        .addOnSuccessListener {
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show()
                        }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Verify OTP")
            }
        }
    }
}
