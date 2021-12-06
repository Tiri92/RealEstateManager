package thierry.realestatemanager.model

data class GeocodingResponse(
	val results: List<ResultsItem?>? = null,
	val status: String? = null
)

data class Bounds(
	val southwest: Southwest? = null,
	val northeast: Northeast? = null
)

data class Northeast(
	val lng: Double? = null,
	val lat: Double? = null
)

data class Southwest(
	val lng: Double? = null,
	val lat: Double? = null
)

data class ResultsItem(
	val formattedAddress: String? = null,
	val types: List<String?>? = null,
	val geometry: Geometry? = null,
	val addressComponents: List<AddressComponentsItem?>? = null,
	val placeId: String? = null
)

data class Geometry(
	val viewport: Viewport? = null,
	val bounds: Bounds? = null,
	val location: Location? = null,
	val locationType: String? = null
)

data class Viewport(
	val southwest: Southwest? = null,
	val northeast: Northeast? = null
)

data class AddressComponentsItem(
	val types: List<String?>? = null,
	val shortName: String? = null,
	val longName: String? = null
)

data class Location(
	val lng: Double? = null,
	val lat: Double? = null
)

