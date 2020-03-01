package traveling.location.location

import core.body.BodyPart
import core.properties.Properties
import core.utility.Named
import dialogue.DialogueOptions

val NOWHERE = Location("Nowhere")

const val HEAT = "Heat"
const val LIGHT = "Light"

class Location(
        override val name: String,
        private val description: DialogueOptions = DialogueOptions(""),
        val activators: List<LocationTarget> = listOf(),
        val creatures: List<LocationTarget> = listOf(),
        val items: List<LocationTarget> = listOf(),
        var bodyPart: BodyPart? = null,
        val weatherChangeFrequency: Int = 5,
        private val weather: DialogueOptions = DialogueOptions("Still"),
        val properties: Properties = Properties()
) : Named {
    constructor(base: Location) : this(
            base.name,
            base.description,
            base.activators.toList(),
            base.creatures.toList(),
            base.items.toList(),
            if (base.bodyPart != null) {
                BodyPart(base.bodyPart!!)
            } else {
                null
            },
            base.weatherChangeFrequency,
            base.weather,
            Properties(base.properties)
    )

    override fun toString(): String {
        return name
    }

    fun getWeatherName() : String {
        return weather.getOption() ?: ""
    }

    fun getDescription(): String {
        return description.getOption() ?: ""
    }

}