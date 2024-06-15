package org.d3if3134.currencycalculator.ui.screen

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import org.d3if3134.currencycalculator.navigation.Screen

@Composable
fun LoginScreen(navController: NavHostController) {
    val auth = remember { FirebaseAuth.getInstance() }
    val currentUser = auth.currentUser

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            navController.navigate(Screen.Profile.route) {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Scaffold {
        LoginContent(modifier = Modifier.padding(it), navController)
    }
}

@Composable
fun LoginContent(modifier: Modifier = Modifier, navController: NavHostController) {
    val context = LocalContext.current
    val googleSignInClient = remember { getGoogleSignInClient(context) }
    val auth = remember { FirebaseAuth.getInstance() }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(Exception::class.java)!!
            firebaseAuthWithGoogle(account, auth, context, navController)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            }
        ) {
            Text("Login with Google", fontSize = 18.sp)
        }
    }
}

fun getGoogleSignInClient(context: android.content.Context): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("921398940631-tj1f9db5fqiq349j7d84h1p9jqik0cqt.apps.googleusercontent.com")
        .requestEmail()
        .build()
    return GoogleSignIn.getClient(context, gso)
}

private fun firebaseAuthWithGoogle(
    acct: GoogleSignInAccount,
    auth: FirebaseAuth,
    context: android.content.Context,
    navController: NavHostController
) {
    val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                Log.d("firebaseAuthWithGoogle", "signInWithCredential:success, user: ${user?.displayName}")
                navController.navigate(Screen.Home.route)
                Toast.makeText(context, "Welcome ${user?.displayName}", Toast.LENGTH_SHORT).show()
            } else {
                Log.w("firebaseAuthWithGoogle", "signInWithCredential:failure", task.exception)
                Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController())
}