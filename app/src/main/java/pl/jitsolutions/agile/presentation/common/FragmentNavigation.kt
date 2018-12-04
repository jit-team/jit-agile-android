package pl.jitsolutions.agile.presentation.common

import pl.jitsolutions.agile.presentation.navigation.Navigator

interface FragmentNavigation {
    var destination: Navigator.Destination?
    fun onBackPressed(): Boolean
}