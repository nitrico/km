package dev.miguelmoreno.km.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.miguelmoreno.km.android.screen.MainScreen
import dev.miguelmoreno.km.data.source.api.StravaApi
import dev.miguelmoreno.km.domain.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppActivity : ComponentActivity(), KoinComponent {

    private val store: Store by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Turn off the decor fitting system windows, which means we need to through handling insets
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {

            AppTheme {
                val scope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }
                //val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
                var selectedItem by remember { mutableStateOf(0) }
                val items = listOf("Songs", "Artists", "Playlists")

                val systemUiController = rememberSystemUiController()
                val useDarkIcons = !isSystemInDarkTheme()

                SideEffect {
                    // Update all of the system bar colors to be transparent, and use dark icons if we're in light theme
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = useDarkIcons
                    )
                }

                Surface(color = MaterialTheme.colorScheme.background) {
                    @OptIn(ExperimentalMaterial3Api::class)
                    Scaffold(
                        topBar = {
                            Card(
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                                shape = Shapes.None
                            ) {
                                SmallTopAppBar(
                                    title = { Text(text = stringResource(id = R.string.app_name)) },
                                    actions = {
                                        IconButton(onClick = { /* doSomething() */ }) {
                                            Icon(
                                                imageVector = Icons.Filled.AccountCircle,
                                                contentDescription = "Localized description"
                                            )
                                        }
                                    },
                                    modifier = Modifier
                                        //.background(Color.White)
                                        .statusBarsPadding()
                                        //.bottomElevation()
                                        .background(Color.Red),
                                    //scrollBehavior = scrollBehavior
                                )
                            }
                        },
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        floatingActionButton = {
                            ExtendedFloatingActionButton(
                                onClick = {
                                    // show snackbar as a suspend function
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Snackbar is shown!"
                                        )
                                    }
                                }
                            ) { Text("Show snackbar") }
                        }
                    ) {
                        MainScreen(store, ::onSignInButtonClicked, ::onRefresh, Modifier.padding(it))
                    }
                }
            }
        }

        if (savedInstanceState == null) {
            onRefresh()
        }
    }

    private fun Modifier.bottomElevation(): Modifier = this.then(Modifier.drawWithContent {
        val paddingPx = 8.dp.toPx()
        clipRect(
            left = 0f,
            top = 0f,
            right = size.width,
            bottom = size.height + paddingPx
        ) {
            this@drawWithContent.drawContent()
        }
    })

    private fun onRefresh() {
        store.dispatch(LoadUser)
        store.dispatch(LoadRuns)
    }

    private fun onSignInButtonClicked() {
        val uri = Uri.parse(StravaApi.URL_AUTHORIZE)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val code = intent?.data?.getQueryParameter("code")
        if (code != null) {
            store.dispatch(ConnectToStrava(code))
        }
    }
}
