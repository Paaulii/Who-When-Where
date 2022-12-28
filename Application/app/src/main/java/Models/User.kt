package Models

@kotlinx.serialization.Serializable
data class User (
    val id_u: Int,
    val name : String,
    val surname: String,
    val login : String
){
    override fun toString(): String {
        return name + " " + surname
    }
}