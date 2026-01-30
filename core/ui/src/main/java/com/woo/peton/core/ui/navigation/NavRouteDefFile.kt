package com.woo.peton.core.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface ChattingNavigationRoute : PetOnNavigation {
    @Serializable
    data object ChattingScreen : ChattingNavigationRoute
}

@Serializable
sealed interface HomeNavigationRoute : PetOnNavigation {
    @Serializable
    data object HomeScreen : HomeNavigationRoute
}

@Serializable
sealed interface MissingNavigationRoute : PetOnNavigation {
    @Serializable
    data class MissingScreen(
        val filterType: String? = null
    ) : MissingNavigationRoute
    @Serializable
    data class DetailScreen(val petId: String) : MissingNavigationRoute
    @Serializable
    data object PostingScreen : MissingNavigationRoute
    @Serializable
    data object LocationSelectScreen : MissingNavigationRoute
}

@Serializable
sealed interface MyPageNavigationRoute : PetOnNavigation {
    @Serializable
    data object MyPageScreen : MyPageNavigationRoute
    @Serializable
    data class MyPetDetailScreen(val petId: String) : MyPageNavigationRoute
    @Serializable
    data object UserDetailScreen : MyPageNavigationRoute
}

@Serializable
data object AuthGraph : PetOnNavigation
@Serializable
sealed interface AuthNavigationRoute : PetOnNavigation {
    @Serializable
    data object AuthScreen : AuthNavigationRoute
    @Serializable
    data object SignUpScreen : AuthNavigationRoute
}

