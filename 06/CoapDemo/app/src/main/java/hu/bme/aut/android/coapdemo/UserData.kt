package hu.bme.aut.android.coapdemo

data class UserData(
    var id: Long? = null,
    var userId: String? = null,
    var timestamp: String? = null,
    var accX: Float? = null,
    var accY: Float? = null,
    var accZ: Float? = null,
    var light: Float? = null,
    var temp: Float? = null
)