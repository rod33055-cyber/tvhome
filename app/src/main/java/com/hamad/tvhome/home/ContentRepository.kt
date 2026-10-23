package com.hamad.tvhome.home

import android.util.Log
import com.hamad.tvhome.tmdb.TmdbClient
import com.hamad.tvhome.tmdb.TmdbItem
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/** A streaming service we can both query on TMDB and open on the box. */
data class Provider(val tmdbId: Int, val name: String, val packageName: String)

object Providers {
    val all = listOf(
        Provider(TmdbClient.PROVIDER_NETFLIX, "Netflix", "com.netflix.ninja"),
        Provider(TmdbClient.PROVIDER_PRIME, "Prime Video", "com.amazon.amazonvideo.livingroom"),
        Provider(TmdbClient.PROVIDER_DISNEY, "Disney+", "com.disney.disneyplus"),
        Provider(TmdbClient.PROVIDER_APPLE_TV, "Apple TV+", "com.apple.atve.androidtv.appletv"),
    )

    fun packageFor(tmdbProviderId: Int): String? = null // Bypassed
    const val YOUTUBE_PACKAGE = "com.google.android.youtube.tv"
}

class ContentRepository(private val tmdb: TmdbClient) {

    // ==========================================
    // [تم التعديل هنا] القسم الأول: الصور الكبيرة
    // ==========================================
    suspend fun hero(tab: Tab, region: String, installed: Set<String>): List<HeroItem> = safe {
        if (tab == Tab.Apps) return@safe emptyList()
        
        val img = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?q=80&w=1920&auto=format&fit=crop"
        
        listOf(
            HeroItem(
                title = Title(id = "m1", tmdbId = 1L, mediaType = "movie", name = "ريال مدريد × برشلونة", meta = "الكلاسيكو", overview = "أهم مباراة", heroUrl = img, cardUrl = img, posterUrl = null),
                attribution = "مباراة مهمة",
                logoUrl = null,
                providerPackage = null
            ),
            HeroItem(
                title = Title(id = "y1", tmdbId = 2L, mediaType = "movie", name = "مقاطع يوتيوب", meta = "يوتيوب", overview = "فيديوهات مقترحة", heroUrl = img, cardUrl = img, posterUrl = null),
                attribution = "مقترح لك",
                logoUrl = null,
                providerPackage = Providers.YOUTUBE_PACKAGE
            )
        )
    }

    // ==========================================
    // [تم التعديل هنا] القسم الثاني: الصفوف السفلية
    // ==========================================
    suspend fun rows(tab: Tab, region: String, installed: Set<String>): List<ContentRow> = safe {
        if (tab == Tab.Apps) return@safe emptyList()
        
        val img = "https://images.unsplash.com/photo-1579952363873-27f3bade9f55?q=80&w=780&auto=format&fit=crop"
        val items = (1..5).map { i ->
            Title(id = "r$i", tmdbId = 100L + i, mediaType = "movie", name = "مباراة اليوم $i", meta = "كرة القدم", overview = "وصف", heroUrl = null, cardUrl = img, posterUrl = img)
        }
        
        listOf(
            ContentRow(id = "m_row", heading = "أهم المباريات اليوم", style = CardStyle.Landscape, items = items),
            ContentRow(id = "y_row", heading = "مقترحات يوتيوب", style = CardStyle.Landscape, items = items)
        )
    }

    // ==========================================
    // ما يلي هو الكود الأصلي للمطور (لا تلمسه)
    // ==========================================
    
    private suspend fun <T> safe(block: suspend () -> List<T>): List<T> = try {
        block()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Log.w("HomeContent", "Content load failed", e)
        emptyList()
    }

    private data class Genre(val id: Int, val name: String, val style: CardStyle = CardStyle.Landscape)
}
