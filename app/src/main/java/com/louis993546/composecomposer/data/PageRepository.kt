package com.louis993546.composecomposer.data

import android.content.Context
import com.louis993546.composecomposer.Database
import com.louis993546.composecomposer.PageInfo
import com.louis993546.composecomposer.data.model.Page
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.IOException

/**
 * TODO maybe it would be better to return some Result type instead of "null when failed"
 *   https://github.com/Kotlin/KEEP/pull/244 (kotlin 1.5)
 */
interface PageRepository {
    suspend fun getPageInfoList(): List<PageInfo>

    suspend fun getPage(pageInfo: PageInfo): Page?

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
    }

    override suspend fun getPage(pageInfo: PageInfo) = withContext(dispatcher) {
        try {
            val json = File(context.filesDir, pageInfo.fileName).readText()
            pageAdapter.fromJson(json)
        } catch (e: IOException) {
            Timber.e(e)
            null
        }
    }

    override suspend fun savePage(page: Page) = withContext(dispatcher) {
        val json = pageAdapter.toJson(page)
        Timber.d("raw json: $json")
        try {
            File(context.filesDir, page.fileName).writeText(json)
            database.pageInfoQueries.savePage(page.id.toLong(), page.name)
            page
        } catch (e: IOException) {
            Timber.e(e)
            null
        }
    }

    override suspend fun deletePage(page: Page) = withContext(dispatcher) {
        try {
            if (File(context.filesDir, page.fileName).delete()) {
                database.pageInfoQueries.deletePage(page.id.toLong())
                page
            } else {
                null
            }
        } catch (e: IOException) {
            Timber.e(e)
            null
        } catch (e: SecurityException) {
            Timber.e(e)
            error("Why am I getting this for just accessing internal storage?")
        }
    }
}

private val PageInfo.fileName
    get() = "$id.json"

private val Page.fileName
    get() = "$id.json"
