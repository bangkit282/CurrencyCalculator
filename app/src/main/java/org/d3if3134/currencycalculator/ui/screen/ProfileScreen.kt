package org.d3if3134.currencycalculator.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.d3if3134.currencycalculator.R


@Composable
fun ProfileScreen(navController: NavHostController) {
    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            ProfileContent(navController = navController, modifier = Modifier.padding(it))
        }
    }
}

@Composable
fun ProfileContent(navController: NavHostController, modifier: Modifier = Modifier) {
    val auth = Firebase.auth
    val currentUser = auth.currentUser
    val imageUrl = remember { currentUser?.photoUrl?.toString() }
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.loading_img),
                error = painterResource(id = R.drawable.broken_img),
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
            Text(
                text = currentUser?.displayName ?: "Unknown",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = currentUser?.email ?: "Unknown",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Button(onClick = { navController.navigate("mainScreen") }) {
                Text("Go to Currency Calculator")
            }
            Button(onClick = {
                Firebase.auth.signOut()
                Toast.makeText(context, "Logged out success", Toast.LENGTH_SHORT).show()
                navController.navigate("loginScreen") {
                    popUpTo("loginScreen") { inclusive = true }
                }
            }) {
                Text("Logout")
            }
        }
    }


}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}