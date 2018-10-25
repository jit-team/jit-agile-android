package pl.jitsolutions.agile.presentation.projects.details

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.presentation.navigation.Navigator

class ProjectDetailsViewModel(private val navigator: Navigator,
                              mainDispatcher: CoroutineDispatcher
) : CoroutineViewModel(mainDispatcher) {

}