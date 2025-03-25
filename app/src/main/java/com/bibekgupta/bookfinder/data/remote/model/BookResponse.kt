package com.bibekgupta.bookfinder.data.remote.model

import com.bibekgupta.bookfinder.domain.remote.model.Book
import com.google.gson.annotations.SerializedName

data class BookResponse(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("totalItems")
    val totalItems: Int,
    @SerializedName("items")
    val items: List<Item>
)

data class Item(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("etag")
    val etag: String,
    @SerializedName("selfLink")
    val selfLink: String,
    @SerializedName("volumeInfo")
    val volumeInfo: VolumeInfo,
    @SerializedName("saleInfo")
    val saleInfo: SaleInfo,
    @SerializedName("accessInfo")
    val accessInfo: AccessInfo
)

data class VolumeInfo(
    @SerializedName("title")
    val title: String,
    @SerializedName("subtitle")
    val subtitle: String?,
    @SerializedName("authors")
    val authors: List<String>?,
    @SerializedName("publisher")
    val publisher: String?,
    @SerializedName("publishedDate")
    val publishedDate: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("industryIdentifiers")
    val industryIdentifiers: List<IndustryIdentifier>?,
    @SerializedName("readingModes")
    val readingModes: ReadingModes,
    @SerializedName("pageCount")
    val pageCount: Int?,
    @SerializedName("printType")
    val printType: String,
    @SerializedName("categories")
    val categories: List<String>?,
    @SerializedName("averageRating")
    val averageRating: Double?,
    @SerializedName("ratingsCount")
    val ratingsCount: Int?,
    @SerializedName("maturityRating")
    val maturityRating: String?,
    @SerializedName("allowAnonLogging")
    val allowAnonLogging: Boolean,
    @SerializedName("contentVersion")
    val contentVersion: String?,
    @SerializedName("panelizationSummary")
    val panelizationSummary: PanelizationSummary?,
    @SerializedName("imageLinks")
    val imageLinks: ImageLinks?,
    @SerializedName("language")
    val language: String,
    @SerializedName("previewLink")
    val previewLink: String,
    @SerializedName("infoLink")
    val infoLink: String,
    @SerializedName("canonicalVolumeLink")
    val canonicalVolumeLink: String
)

data class IndustryIdentifier(
    @SerializedName("type")
    val type: String,
    @SerializedName("identifier")
    val identifier: String
)

data class ReadingModes(
    @SerializedName("text")
    val text: Boolean,
    @SerializedName("image")
    val image: Boolean
)

data class PanelizationSummary(
    @SerializedName("containsEpubBubbles")
    val containsEpubBubbles: Boolean,
    @SerializedName("containsImageBubbles")
    val containsImageBubbles: Boolean
)

data class ImageLinks(
    @SerializedName("smallThumbnail")
    val smallThumbnail: String,
    @SerializedName("thumbnail")
    val thumbnail: String
)

data class SaleInfo(
    @SerializedName("country")
    val country: String,
    @SerializedName("saleability")
    val saleability: String,
    @SerializedName("isEbook")
    val isEbook: Boolean
)

data class AccessInfo(
    @SerializedName("country")
    val country: String,
    @SerializedName("viewability")
    val viewability: String,
    @SerializedName("embeddable")
    val embeddable: Boolean,
    @SerializedName("publicDomain")
    val publicDomain: Boolean,
    @SerializedName("textToSpeechPermission")
    val textToSpeechPermission: String,
    @SerializedName("epub")
    val epub: Epub,
    @SerializedName("pdf")
    val pdf: Pdf,
    @SerializedName("webReaderLink")
    val webReaderLink: String,
    @SerializedName("accessViewStatus")
    val accessViewStatus: String,
    @SerializedName("quoteSharingAllowed")
    val quoteSharingAllowed: Boolean
)

data class Epub(
    @SerializedName("isAvailable")
    val isAvailable: Boolean
)

data class Pdf(
    @SerializedName("isAvailable")
    val isAvailable: Boolean,
    @SerializedName("acsTokenLink")
    val acsTokenLink: String?
)

fun Item.toDomain(): Book {
    return Book(
        id = id,
        title = this.volumeInfo.title,
        authors = this.volumeInfo.authors ?: emptyList(),
        publishedDate = this.volumeInfo.publishedDate.toString(),
        coverImageUrl = this.volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://") ?: "",
        description = this.volumeInfo.description
    )
}
