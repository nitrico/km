package dev.miguelmoreno.km.android.screen.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import dev.miguelmoreno.km.domain.DisconnectFromStrava
import dev.miguelmoreno.km.domain.Store
import org.koin.androidx.compose.get

//@Destination
@Composable
fun AccountScreen(
    modifier: Modifier = Modifier
) {
    val store = get<Store>()
    val state by store.state.collectAsState()

    Column(
        modifier = modifier
    ) {
        state.user?.let { user ->
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
            Text(text = state.distance.toString())
            Button(onClick = { store.dispatch(DisconnectFromStrava) }) {
                Text(text = "Log out")
            }
        }
    }
}
