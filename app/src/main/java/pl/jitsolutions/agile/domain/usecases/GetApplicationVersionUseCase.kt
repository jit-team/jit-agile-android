package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.repository.SystemInfoRepository

class GetApplicationVersionUseCase(private val systemInfoRepository: SystemInfoRepository,
                                   private val dispatcher: CoroutineDispatcher
) : UseCase<GetApplicationVersionUseCase.Params, String>(dispatcher) {

    override suspend fun build(params: Params): Response<String> {
        return systemInfoRepository.getApplicationVersion()
    }

    data class Params(val void: Void? = null)
}