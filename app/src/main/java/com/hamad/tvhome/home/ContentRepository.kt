package com.hamad.tvhome.home

import android.util.Log
import com.hamad.tvhome.tmdb.TmdbClient
import com.hamad.tvhome.tmdb.TmdbItem
import kotlinx.coroutines.CancellationException

data class Provider(val tmdbId: Int, val name: String, val packageName: String)

object Providers {
    val all = listOf(
        Provider(TmdbClient.PROVIDER_NETFLIX, "Netflix", "com.netflix.ninja"),
        Provider(TmdbClient.PROVIDER_PRIME, "Prime Video", "com.amazon.amazonvideo.livingroom"),
        Provider(TmdbClient.PROVIDER_DISNEY, "Disney+", "com.disney.disneyplus"),
        Provider(TmdbClient.PROVIDER_APPLE_TV, "Apple TV+", "com.apple.atve.androidtv.appletv")
    )
    fun packageFor(tmdbProviderId: Int): String? = null
    const val YOUTUBE_PACKAGE = "com.google.android.youtube.tv"
}

class ContentRepository(private val tmdb: TmdbClient) {

    suspend fun hero(tab: Tab, region: String, installed: Set<String>): List<HeroItem> = safe {
        if (tab == Tab.Apps) {
            emptyList<HeroItem>()
        } else {
            val img = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?q=80&w=1920&auto=format&fit=crop"
            val title1 = Title(
                id = "m1", tmdbId = 1L, mediaType = "movie", name = "ريال مدريد × برشلونة",
                meta = "الكلاسيكو", overview = "أهم مباراة", heroUrl = img, cardUrl = img, posterUrl = null
            )
            val title2 = Title(
                id = "y1", tmdbId = 2L, mediaType = "movie", name = "مقاطع يوتيوب",
                meta = "يوتيوب", overview = "فيديوهات مقترحة", heroUrl = img, cardUrl = img, posterUrl = null
            )
            listOf(
                HeroItem(title = title1, attribution = "مباراة مهمة", logoUrl = null, providerPackage = null),
                HeroItem(title = title2, attribution = "مقترح لك", logoUrl = null, providerPackage = Providers.YOUTUBE_PACKAGE)
            )
        }
    }

    suspend fun rows(tab: Tab, region: String, installed: Set<String>): List<ContentRow> = safe {
        if (tab == Tab.Apps) {
            emptyList<ContentRow>()
        } else {
            val img = "https://images.unsplash.com/photo-1579952363873-27f3bade9f55?q=80&w=780&auto=format&fit=crop"
            val item1 = Title(
                id = "r1", tmdbId = 1L, mediaType = "movie", name = "مباراة اليوم 1",
                meta = "كرة القدم", overview = "وصف", heroUrl = null, cardUrl = img, posterUrl = img
            )
            val item2 = Title(
                id = "r2", tmdbId = 2L, mediaType = "movie", name = "مباراة اليوم 2",
                meta = "كرة القدم", overview = "وصف", heroUrl = null, cardUrl = img, posterUrl = img
            )
            listOf(
                ContentRow(id = "m_row", heading = "أهم المباريات اليوم", style = CardStyle.Landscape, items = listOf(item1, item2)),
                ContentRow(id = "y_row", heading = "مقترحات يوتيوب", style = CardStyle.Landscape, items = listOf(item1, item2))
            )
        }
    }

    private suspend fun <T> safe(block: suspend () -> List<T>): List<T> = try {
        block()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Log.w("HomeContent", "Content load failed", e)
        emptyList()
    }
}
