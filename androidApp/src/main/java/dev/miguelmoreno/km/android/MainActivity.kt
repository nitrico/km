package dev.miguelmoreno.km.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import dev.miguelmoreno.km.Store
import dev.miguelmoreno.km.android.databinding.ActivityMainBinding
import dev.miguelmoreno.km.data.source.api.StravaApi
import dev.miguelmoreno.km.domain.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainActivity : AppCompatActivity(), KoinComponent {

    private lateinit var binding: ActivityMainBinding

    private val store: Store<State, Action> by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        store.dispatch(LoadUser)
        store.dispatch(LoadRuns)

        binding.signIn.setOnClickListener {
            startConnection()
        }

        binding.logOut.setOnClickListener {
            store.dispatch(DisconnectFromStrava)
        }

        lifecycleScope.launch {
            store.state.flowWithLifecycle(lifecycle, STARTED).collect {
                binding.bindState(it)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val code = intent?.data?.getQueryParameter("code")
        if (code != null) {
            store.dispatch(ConnectToStrava(code))
        }
    }

    private fun startConnection() {
        val uri = Uri.parse(StravaApi.URL_AUTHORIZE)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun ActivityMainBinding.bindState(state: State) {
        userId.isVisible = state.user != null
        userId.text = state.user?.id

        username.isVisible = state.user != null
        username.text = state.user?.username

        firstName.isVisible = state.user != null
        firstName.text = state.user?.firstName

        lastName.isVisible = state.user != null
        lastName.text = state.user?.lastName

        userProfilePicture.load(state.user?.profilePicture) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }

        dayOfYear.isVisible = state.user != null
        remainingDays.isVisible = state.user != null
        remainingWeeks.isVisible = state.user != null
        distance.isVisible = state.user != null
        remainingDistance.isVisible = state.user != null
        remainingDistancePerDay.isVisible = state.user != null
        remainingDistancePerYear.isVisible = state.user != null
        progressBar.isVisible = state.isLoading

        signIn.isVisible = state.user == null
        logOut.isVisible = state.user != null

        distance.text = state.distance.toString()

        /*
        dayOfYear.text = state?.dayOfYear.toString()
        remainingDays.text = state?.remainingDays.toString()
        remainingWeeks.text = state?.remainingWeeks.toString()
        remainingDistance.text = state?.remainingDistance.toString()
        remainingDistancePerDay.text = state?.let { String.format("%.2f", state.remainingDistancePerDay) } ?: ""
        remainingDistancePerYear.text = state?.let { String.format("%.2f", state.remainingDistancePerWeek) } ?: ""
        */
    }
}
