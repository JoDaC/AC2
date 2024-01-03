package eu.krzdabrowski.starter.basicfeature.presentation.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import eu.krzdabrowski.starter.basicfeature.presentation.RocketsEvent
import eu.krzdabrowski.starter.basicfeature.presentation.RocketsViewModel
import eu.krzdabrowski.starter.core.utils.LocalNavController
import eu.krzdabrowski.starter.core.utils.collectWithLifecycle
import kotlinx.coroutines.flow.Flow
import java.net.URLDecoder

@Composable
fun WebBrowserRoute(
    viewModel: RocketsViewModel = hiltViewModel(),
) {
    HandleEvents(viewModel.event)
    val navController = LocalNavController.current

    // Retrieve the rocket ID from arguments
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rocketUrl =
        navBackStackEntry?.arguments?.getString("encodedRocketUrl")
            ?.let { URLDecoder.decode(it, "UTF-8") }

    if (rocketUrl != null) {
        WebBrowserScreen(rocketUrl = rocketUrl)
    }
}

@Composable
internal fun WebBrowserScreen(
    rocketUrl: String,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            // you need to get rid of all the code that changes the list order
            WebViewWithPlaceholder(rocket = rocketUrl)
        }
    }
}

@Composable
private fun HandleEvents(events: Flow<RocketsEvent>) {
    val uriHandler = LocalUriHandler.current

    events.collectWithLifecycle {
        when (it) {
            is RocketsEvent.OpenWebBrowserWithDetails -> {
                uriHandler.openUri(it.uri)
            }
        }
    }
}