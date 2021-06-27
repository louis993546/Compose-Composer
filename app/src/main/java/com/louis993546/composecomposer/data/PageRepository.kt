package com.louis993546.composecomposer.data

import android.content.Context
import com.louis993546.composecomposer.Database
import com.louis993546.composecomposer.data.model.Page
import com.louis993546.composecomposer.data.model.PageInfo
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import timber.log.Timber
import java.io.File
import java.io.IOException
import com.louis993546.composecomposer.PageInfo as DaoPageInfo

/**
 * TODO maybe it would be better to return some Result type instead of "null when failed"
 *   https://github.com/Kotlin/KEEP/pull/244 (kotlin 1.5)
 */
interface PageRepository {
    suspend fun getPageInfoList(): List<PageInfo>

    fun getPageInfoListFLow(): Flow<List<PageInfo>>

    suspend fun getPage(pageInfo: PageInfo): Page?

    suspend fun createPage(page: Page): Page?

    suspend fun savePage(page: Page): Page?

    suspend fun deletePage(page: Page): Page?
}

class FilePageRepository(
    moshi: Moshi,
    private val database: Database,
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : PageRepository {
    private val pageAdapter: JsonAdapter<Page> = moshi.adapter(Page::class.java)

    override suspend fun getPageInfoList() = withContext(dispatcher) {
        database.pageInfoQueries
            .selectAll()
            .executeAsList()
            .map { it.toPageInfo() }
    }

    override fun getPageInfoListFLow(): Flow<List<PageInfo>> = database
        .pageInfoQueries
        .selectAll()
        .asFlow()
        .mapToList()
        .map { list -> list.map { dao -> dao.toPageInfo() } }

    /**
     * TODO if necessary, try-catch IOException from File
     */
    override suspend fun getPage(pageInfo: PageInfo) = withContext(dispatcher) {
        val json = File(context.filesDir, pageInfo.fileName).readText()
        pageAdapter.fromJson(json)
    }

    /**
     * TODO if necessary, try-catch IOException from File
     */
    override suspend fun createPage(page: Page): Page? = withContext(dispatcher) {
        val json = pageAdapter.toJson(page)
        File(context.filesDir, page.fileName).writeText(json)
        database.pageInfoQueries.createPage(page.info.name)
        page
    }

    /**
     * TODO if necessary, try-catch IOException from File
     */
    override suspend fun savePage(page: Page) = withContext(dispatcher) {
        val json = pageAdapter.toJson(page)
        Timber.d("raw json: $json")
        File(context.filesDir, page.fileName).writeText(json)
        database.pageInfoQueries.updatePage(
            name = page.info.name,
            id = page.info.id.toLong(),
        )
        page
    }

    /**
     * TODO if necessary, try-catch IOException and SecurityException from File
     */
    override suspend fun deletePage(page: Page) = withContext(dispatcher) {
        if (File(context.filesDir, page.fileName).delete()) {
            database.pageInfoQueries.deletePage(page.info.id.toLong())
            page
        } else {
            null
        }
    }
}

private val PageInfo.fileName
    get() = "$id.json"

private val Page.fileName
    get() = "${info.id}.json"

private fun DaoPageInfo.toPageInfo(): PageInfo {
    return PageInfo(
        id = id.toInt(),
        name = name,
        createdAt = Instant.parse(created_at),
        lastUpdateAt = last_updated_at?.let { Instant.parse(it) }
    )
}
