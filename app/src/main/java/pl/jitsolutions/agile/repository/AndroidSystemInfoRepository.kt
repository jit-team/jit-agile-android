package pl.jitsolutions.agile.repository

import pl.jitsolutions.agile.BuildConfig
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.response

class AndroidSystemInfoRepository : SystemInfoRepository {
    override suspend fun getApplicationVersion(): Response<String> {
        return response(BuildConfig.VERSION_NAME)
    }
}