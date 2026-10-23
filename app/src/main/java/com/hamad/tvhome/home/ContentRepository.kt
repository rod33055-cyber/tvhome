package com.hamad.tvhome.home

import android.util.Log
import com.hamad.tvhome.tmdb.TmdbClient

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

    suspend fun hero(tab: Tab, region: String, installed: Set<String>): List<HeroItem> = try {
        if (tab == Tab.Apps) {
            emptyList()
        } else {
            val img = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?q=80&w=1920"
            listOf(
                HeroItem(
                    title = Title("m1", 1L, "movie", "ريال مدريد", "الكلاسيكو", "مباراة", img, img, null),
                    attribution = "مهم",
                    logoUrl = null,
                    providerPackage = null
                ),
                HeroItem(
                    title = Title("y1", 2L, "movie", "يوتيوب", "فيديوهات", "مقترحات", img, img, null),
                    attribution = "يوتيوب",
                    logoUrl = null,
                    providerPackage = Providers.YOUTUBE_PACKAGE
                )
            )
        }
    } catch (e: Exception) {
        emptyList()
    }

    suspend fun rows(tab: Tab, region: String, installed: Set<String>): List<ContentRow> = try {
        if (tab == Tab.Apps) {
            emptyList()
        } else {
            val img = "https://images.unsplash.com/photo-1579952363873-27f3bade9f55?q=80&w=780"
            val items = listOf(
                Title("r1", 1L, "movie", "مباراة 1", "كرة القدم", "وصف", null, img, img),
                Title("r2", 2L, "movie", "مباراة 2", "كرة القدم", "وصف", null, img, img)
            )
            listOf(
                ContentRow("m_row", "المباريات", CardStyle.Landscape, items),
                ContentRow("y_row", "يوتيوب", CardStyle.Landscape, items)
            )
        }
    } catch (e: Exception) {
        emptyList()
    }
}
