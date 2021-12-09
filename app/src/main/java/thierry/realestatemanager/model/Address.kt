package thierry.realestatemanager.model

data class Address(
    val street: String?,
    val city: String,
    val postcode: Int,
    val country: String,
    var propertyLatitude: Double? = null,
    var propertyLongitude: Double? = null,

    )