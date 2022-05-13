package dev.miguelmoreno.km.android.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.miguelmoreno.km.data.RunUiModel
import dev.miguelmoreno.km.data.toUiModel
import dev.miguelmoreno.km.domain.DisconnectFromStrava
import dev.miguelmoreno.km.domain.Store

@Composable
fun MainScreen(
    store: Store,
    onSignInButtonClicked: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by store.state.collectAsState()

    SwipeRefresh(
        state = rememberSwipeRefreshState(state.isLoading),
        onRefresh = onRefresh,
        modifier = modifier
    ) {
        Column {
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

            if (state.user == null && state.isLoading.not()) {
                Button(onClick = onSignInButtonClicked) {
                    Text("Sign in")
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                val grouped = state.runs.groupBy { it.startDate.month }
                grouped.forEach { (month, runs) ->
                    @OptIn(ExperimentalFoundationApi::class)
                    stickyHeader {
                        Header(month.name)
                    }
                    items(runs) { run ->
                        RunItem(run.toUiModel())
                    }
                }
            }
        }
    }
}

@Composable
fun Header(
    text: String,
) {
    Text(text, modifier = Modifier.background(Color.LightGray))
}

@Composable
fun RunItem(run: RunUiModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
       Text(run.distance)
       Text(run.movingTime)
       Text(run.startDate)
    }
}
