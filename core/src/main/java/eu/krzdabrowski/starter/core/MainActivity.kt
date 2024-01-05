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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
//        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AndroidStarterTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
//                val navHide = navBackStackEntry?.arguments?.getBoolean("hideBottomNavigation")
//                val navHide =
//                    navBackStackEntry?.savedStateHandle?.get<Boolean>("hideBottomNavigation")
                val navHide = remember { mutableStateOf(false) }

                LaunchedEffect(navController) {
                    navBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("hideBottomNavigation")
                        ?.observe(this@MainActivity) {
                            navHide.value = it
                        }
                }
                CompositionLocalProvider(LocalNavController provides navController) {
                    Scaffold(
                        bottomBar = {
                            val shouldShowBottomNavigation = when (currentRoute) {
                                NavigationDestination.Rockets.route,
                                NavigationDestination.Archive.route,
                                NavigationDestination.Insights.route -> navHide.value != true

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
                                                }
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

@Composable
fun CustomNavigationBarItem(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        // evnly spaced
        verticalArrangement = Arrangement.SpaceEvenly,
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.Center
    ) {
        icon()
        if (selected) {
            Spacer(modifier = Modifier.width(8.dp))
            label()
        }
    }
}