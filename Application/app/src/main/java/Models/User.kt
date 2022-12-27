package Models

@kotlinx.serialization.Serializable
data class User (
    val id_u: Int,
    val name : String,
    val surname: String,
    val login : String,
    val password: String,
    val token : Int
){
    override fun toString(): String {
        return name + " " + surname
    }
}