package dev.miguelmoreno.km.android

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import dev.miguelmoreno.km.Store
import dev.miguelmoreno.km.domain.Action
import dev.miguelmoreno.km.domain.DisconnectFromStrava
import dev.miguelmoreno.km.domain.State

@Composable
fun MainScreen(
    store: Store<State, Action>,
    onSignInButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = store.state.collectAsState()

    Column(
        modifier = modifier
    ) {
        state.value.user?.let { user ->
            AsyncImage(
                model = user.profilePicture,
                //placeholder = painterResource(R.drawable.placeholder),
                contentDescription = "",//stringResource(R.string.description),
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape)
            )
            Text(text = user.id)
            Text(text = user.username)
            Text(text = user.firstName)
            Text(text = user.lastName)
            Text(text = state.value.distance.toString())
            Button(onClick = { store.dispatch(DisconnectFromStrava) }) {
                Text(text = "Log out")
            }
        }

        if (state.value.isLoading) {
            CircularProgressIndicator()
        }

        if (state.value.user == null && state.value.isLoading.not()) {
            Button(onClick = onSignInButtonClicked) {
                Text(text = "Sign in")
            }
        }
    }
}
