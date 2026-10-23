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
    fun packageFor(tmdbProviderId: Int): String? = null // Bypassed for custom data
    const val YOUTUBE_PACKAGE = "com.google.android.youtube.tv"
}

class ContentRepository(private val tmdb: TmdbClient) {

    // 1. HERO CAROUSEL (الصور الكبيرة في الأعلى)
    suspend fun hero(tab: Tab, region: String, installed: Set<String>): List<HeroItem> = safe {
        if (tab == Tab.Apps) return emptyList()
        
        // صورة عريضة لمباراة كرة قدم
        val matchImage = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?q=80&w=1920&auto=format&fit=crop"
        // صورة عريضة ليوتيوب/جيمنج
        val ytImage = "https://images.unsplash.com/photo-1611162617474-5b21e879e113?q=80&w=1920&auto=format&fit=crop"

        listOf(
            HeroItem(
                title = Title(
                    id = "match_1",
                    tmdbId = 1L,
                    mediaType = "movie",
                    name = "ريال مدريد ضد برشلونة (الكلاسيكو)",
                    meta = "الدوري الإسباني • 22:00",
                    overview = "مباراة قمة كروية تجمع بين الغريمين التقليديين.",
                    heroUrl = matchImage,
                    cardUrl = matchImage,
                    posterUrl = null
                ),
                attribution = "مباراة مهمة",
                logoUrl = null,
                providerPackage = null
            ),
            HeroItem(
                title = Title(
                    id = "yt_1",
                    tmdbId = 2L,
                    mediaType = "movie",
                    name = "أفضل مقاطع يوتيوب (Gaming)",
                    meta = "يوتيوب • مقترح لك",
                    overview = "مجموعة مختارة من أفضل مقاطع الألعاب على يوتيوب.",
                    heroUrl = ytImage,
                    cardUrl = ytImage,
                    posterUrl = null
                ),
                attribution = "مقترح لك",
                logoUrl = null,
                providerPackage = Providers.YOUTUBE_PACKAGE
            )
        )
    }

    // 2. ROWS (الصفوف السفلية)
    suspend fun rows(tab: Tab, region: String, installed: Set<String>): List<ContentRow> = safe {
        if (tab == Tab.Apps) return emptyList()

        // صورة طولية لبطاقات المباريات
        val matchCard = "https://images.unsplash.com/photo-1579952363873-27f3bade9f55?q=80&w=780&auto=format&fit=crop"
        // صورة طولية لبطاقات اليوتيوب
        val ytCard = "https://images.unsplash.com/photo-1522869635100-9f4c5e86aa37?q=80&w=780&auto=format&fit=crop"

        // نصنع قائمة مباريات وهمية لتعبئة الصف
        val fakeMatches = (1..6).map { i ->
            Title(
                id = "match_row_$i",
                tmdbId = 100L + i,
                mediaType = "movie",
                name = "مباراة اليوم $i",
                meta = "كرة القدم",
                overview = "تفاصيل المباراة...",
                heroUrl = null,
                cardUrl = matchCard,
                posterUrl = matchCard
            )
        }

        // نصنع قائمة فيديوهات وهمية
        val fakeVideos = (1..6).map { i ->
            Title(
                id = "yt_row_$i",
                tmdbId = 200L + i,
                mediaType = "movie",
                name = "فيديو يوتيوب مقترح $i",
                meta = "قناة يوتيوب",
                overview = "وصف الفيديو...",
                heroUrl = null,
                cardUrl = ytCard,
                posterUrl = ytCard
            )
        }

        listOf(
            ContentRow(
                id = "row_matches",
                heading = "أهم المباريات اليوم",
                style = CardStyle.Landscape,
                items = fakeMatches
            ),
            ContentRow(
                id = "row_youtube",
                heading = "مقترحات يوتيوب لك",
                style = CardStyle.Landscape,
                items = fakeVideos
            )
        )
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
