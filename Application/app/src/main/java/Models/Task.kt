package Models

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Keep
@Serializable
data class Task (
    var id_t: Int,
    var title : String,
    var description: String,
    var category: String,
    var status: String,
    var estimatedTime : Float,
    var realTime : Float,
    var id_u : Int,
    var blockedBy : Int?) : java.io.Serializable
{
    override fun toString(): String {
        return title
    }
}
