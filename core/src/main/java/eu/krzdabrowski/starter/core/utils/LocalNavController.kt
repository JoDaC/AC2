package eu.krzdabrowski.starter.core.utils

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController

val LocalNavController = compositionLocalOf<NavController> {
    error("No NavController found!")
}