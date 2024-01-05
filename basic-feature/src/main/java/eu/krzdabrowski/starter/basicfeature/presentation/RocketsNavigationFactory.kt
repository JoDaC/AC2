package eu.krzdabrowski.starter.basicfeature.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.krzdabrowski.starter.basicfeature.presentation.composable.ArchiveRoute
import eu.krzdabrowski.starter.basicfeature.presentation.composable.InsightsRoute
import eu.krzdabrowski.starter.basicfeature.presentation.composable.RocketsRoute
import eu.krzdabrowski.starter.basicfeature.presentation.composable.WebBrowserRoute
import eu.krzdabrowski.starter.core.navigation.NavigationDestination.Archive
import eu.krzdabrowski.starter.core.navigation.NavigationDestination.Insights
import eu.krzdabrowski.starter.core.navigation.NavigationDestination.Rockets
import eu.krzdabrowski.starter.core.navigation.NavigationDestination.WebBrowser
import eu.krzdabrowski.starter.core.navigation.NavigationFactory
import javax.inject.Inject

class RocketsNavigationFactory @Inject constructor() : NavigationFactory {

    override fun create(builder: NavGraphBuilder) {
        builder.composable(Rockets.route) {
            RocketsRoute()
        }

//        builder.composable(
//            route = Rockets.route + "/{hideBottomNavigation}",
//            arguments = listOf(navArgument("hideBottomNavigation") { type = NavType.BoolType })
//        ) {
////            RocketsRoute()
//        }


        builder.composable(Archive.route) {
            ArchiveRoute()
        }

        builder.composable(Insights.route) {
            InsightsRoute()
        }

        builder.composable(
            route = WebBrowser.route + "/{encodedRocketUrl}",
            arguments = listOf(navArgument("encodedRocketUrl") { type = NavType.StringType })
        ) {
            WebBrowserRoute()
        }
    }
}
