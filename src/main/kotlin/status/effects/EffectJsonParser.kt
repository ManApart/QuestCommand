package status.effects

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.utility.JsonDirectoryParser
import core.utility.NameSearchableList

class EffectJsonParser : EffectParser {
    private fun parseFile(path: String): List<EffectBase> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    override fun loadEffects(): NameSearchableList<EffectBase> {
        return NameSearchableList(JsonDirectoryParser.parseDirectory("/data/generated/content/effects", ::parseFile))
    }
}