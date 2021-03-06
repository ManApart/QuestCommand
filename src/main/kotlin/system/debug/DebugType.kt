package system.debug

enum class DebugType(val propertyName: String) {
    DEBUG_GROUP("Debug Group"),
    LEVEL_REQ("Ignore Level Requirements"),
    STAT_CHANGES("Ignore Stat Changes"),
    RANDOM_SUCCEED("Ignore Random"),
    RANDOM_FAIL("Random Always Fails"),
    RANDOM_RESPONSE("Random Returns Number"),
    DISPLAY_UPDATES("Display Updates")
}