package eu.krzdabrowski.starter.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import eu.krzdabrowski.starter.core.design.AndroidStarterTheme
import eu.krzdabrowski.starter.core.navigation.NavigationDestination
import eu.krzdabrowski.starter.core.navigation.NavigationFactory
import eu.krzdabrowski.starter.core.navigation.NavigationHost
import eu.krzdabrowski.starter.core.navigation.NavigationManager
import eu.krzdabrowski.starter.core.utils.LocalNavController
import eu.krzdabrowski.starter.core.utils.collectWithLifecycle
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationFactories: @JvmSuppressWildcards Set<NavigationFactory>

    @Inject
    lateinit var navigationManager: NavigationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidStarterTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                CompositionLocalProvider(LocalNavController provides navController) {
                    Scaffold(
                        bottomBar = {
                            val shouldShowBottomNavigation = when (currentRoute) {
                                NavigationDestination.Rockets.route,
                                NavigationDestination.Archive.route,
                                NavigationDestination.Insights.route,
                                -> true

                                else -> false
                            }
                            if (shouldShowBottomNavigation) {
                                //wrap NavigationBarr in Animated Visibility to animate it
                                AnimatedVisibility(
                                    visible = true,
                                    enter = slideInVertically(
                                        animationSpec = tween(300)
                                    ) + fadeIn(),
                                    exit = slideOutVertically(
                                        animationSpec = tween(300)
                                    ) + fadeOut()
                                ) {
                                    NavigationBar {
                                        bottomNavigationItems.forEach { item ->
                                            NavigationBarItem(
                                                icon = {
                                                    Icon(
                                                        painterResource(item.icon),
                                                        contentDescription = item.title
                                                    )
                                                },
                                                label = { Text(item.title) },
                                                selected = currentRoute == item.route,
                                                onClick = {
                                                    if (item.route != currentRoute) {
                                                        navController.navigate(item.route) {
                                                            popUpTo(navController.graph.startDestinationId) {
                                                            }
                                                            launchSingleTop = true
                                                        }
                                                    }
                                                },
                                            )
                                        }
                                    }

                                }
                            }
                        }
                    ) {
                        NavigationHost(
                            modifier = Modifier
                                .padding(it),
                            navController = navController,
                            factories = navigationFactories,
                        )
                    }

                    navigationManager
                        .navigationEvent
                        .collectWithLifecycle(
                            key = navController,
                        ) {
                            when (it.destination) {
                                NavigationDestination.Back.route -> navController.navigateUp()
                                else -> navController.navigate(it.destination, it.configuration)
                            }
                        }
                }
            }
        }
    }
}

val bottomNavigationItems = listOf(
    BottomNavigationItem(
        "Recent",
        R.drawable.baseline_article_24,
        NavigationDestination.Rockets.route
    ),
    BottomNavigationItem(
        "Archived",
        R.drawable.baseline_archive_24,
        NavigationDestination.Archive.route
    ),
    BottomNavigationItem(
        "Insights",
        R.drawable.outline_insights_24,
        NavigationDestination.Insights.route
    )
)

data class BottomNavigationItem(val title: String, val icon: Int, val route: String)