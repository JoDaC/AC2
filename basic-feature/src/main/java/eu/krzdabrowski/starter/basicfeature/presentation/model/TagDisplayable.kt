package eu.krzdabrowski.starter.basicfeature.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TagDisplayable (
    val id: String,
    val name: String,
    val color: Int
) : Parcelable