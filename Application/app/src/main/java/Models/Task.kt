package Models

data class Task (
    var id_t: Int,
    var title : String,
    var description: String,
    var category: String,
    var state: State,
    var estimatedTime : String,
    var realTime : String,
    var id_u : Int)
