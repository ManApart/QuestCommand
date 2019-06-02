package core.gameState.location

import core.gameState.Target
import core.gameState.body.BodyPart

class LocationPoint(val location: LocationNode, val targetName: String? = null, val partName: String? = null) {

    override fun toString(): String {
        return getName()
    }

    fun getName(): String {
        return when {
            targetName != null && partName != null -> "${location.name}: $partName of $targetName"
            targetName != null -> "${location.name}: $targetName"
            else -> location.name
        }
    }

    fun equals(location: LocationNode, target: Target?, part: BodyPart?): Boolean {
        return location == this.location
                && (target == null || target.name == targetName)
                && (part == null || part.name == partName)
    }

    fun hasTargetAndPart() : Boolean {
        return !targetName.isNullOrBlank() && !partName.isNullOrBlank()
    }
}