package com.hamad.tvhome.home

import android.util.Log
import com.hamad.tvhome.tmdb.TmdbClient
import com.hamad.tvhome.tmdb.TmdbItem
import kotlinx.coroutines.CancellationException

/** A streaming service we can both query on TMDB and open on the box. */
data class Provider(val tmdbId: Int, val name: String, val packageName: String)

object Providers {
    val all = listOf(
        Provider(TmdbClient.PROVIDER_NETFLIX, "Netflix", "com.netflix.ninja"),
        Provider(TmdbClient.PROVIDER_PRIME, "Prime Video", "com.amazon.amazonvideo.livingroom"),
        Provider(TmdbClient.PROVIDER_DISNEY, "Disney+", "com.disney.disneyplus"),
        Provider(TmdbClient.PROVIDER_APPLE_TV, "Apple TV+", "com.apple.atve.androidtv.appletv"),
    )

    fun packageFor(tmdbProviderId: Int): String? = null
    const val YOUTUBE_PACKAGE = "com.google.android.youtube.tv"
}

class ContentRepository(private val tmdb: TmdbClient) {

    suspend fun hero(tab: Tab, region: String, installed: Set<String>): List<HeroItem> = safe {
        if (tab == Tab.Apps) {
            emptyList()
        } else {
            val img = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?q=80&w=1920&auto=format&fit=crop"
            listOf(
                HeroItem(
                    title = Title("m1", 1L, "movie", "ريال مدريد × برشلونة", "الكلاسيكو", "أهم مباراة", img, img, null),
                    attribution = "مباراة مهمة",
                    logoUrl = null,
                    providerPackage = null
                ),
                HeroItem(
                    title = Title("y1", 2L, "movie", "مقاطع يوتيوب", "يوتيوب", "فيديوهات مقترحة", img, img, null),
                    attribution = "مقترح لك",
                    logoUrl = null,
                    providerPackage = Providers.YOUTUBE_PACKAGE
                )
            )
        }
    }

    suspend fun rows(tab: Tab, region: String, installed: Set<String>): List<ContentRow> = safe {
        if (tab == Tab.Apps) {
            emptyList()
        } else {
            val img = "https://images.unsplash.com/photo-1579952363873-27f3bade9f55?q=80&w=780&auto=format&fit=crop"
            val items = listOf(
                Title("r1", 1L, "movie", "مباراة اليوم 1", "كرة القدم", "وصف", null, img, img),
                Title("r2", 2L, "movie", "مباراة اليوم 2", "كرة القدم", "وصف", null, img, img),
                Title("r3", 3L, "movie", "مباراة اليوم 3", "كرة القدم", "وصف", null, img, img)
            )
            listOf(
                ContentRow("m_row", "أهم المباريات اليوم", CardStyle.Landscape, items),
                ContentRow("y_row", "مقترحات يوتيوب", CardStyle.Landscape, items)
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
