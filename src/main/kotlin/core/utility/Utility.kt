package core.utility

fun <A, B> mapsMatch(map: Map<A, B>, other: Map<A, B>) : Boolean {
    map.keys.forEach {
        if (!other.containsKey(it)) {
            return false
        } else if (map[it] != other[it]){
            return false
        }
    }
    other.keys.forEach {
        if (!map.containsKey(it)) {
            return false
        } else if (map[it] != other[it]){
            return false
        }
    }
    return true
}

fun listsMatch(list: List<String>, other: List<String>) : Boolean {
    if (list.size != other.size) {
        return false
    }
    val listLower = list.map { it.toLowerCase() }
    val otherLower = list.map { it.toLowerCase() }

    listLower.forEach {
        if (!otherLower.contains(it)) {
            return false
        }
    }

    otherLower.forEach {
        if (!listLower.contains(it)) {
            return false
        }
    }

    return true
}