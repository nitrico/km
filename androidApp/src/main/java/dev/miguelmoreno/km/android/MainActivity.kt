package dev.miguelmoreno.km.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.miguelmoreno.km.Store
import dev.miguelmoreno.km.data.source.api.StravaApi
import dev.miguelmoreno.km.domain.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainActivity : ComponentActivity(), KoinComponent {

    private val store: Store<State, Action> by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                MainScreen(store, ::onSignInButtonClicked)
            }
        }

        if (savedInstanceState == null) {
            store.dispatch(LoadUser)
            store.dispatch(LoadRuns)
        }
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
