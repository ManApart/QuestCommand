import conversation.dsl.Dialogue
import conversation.dsl.DialogueResource
import core.ai.AIBase
import core.ai.action.AIAction
import core.ai.action.dsl.AIActionResource
import core.ai.behavior.Behavior
import core.ai.behavior.BehaviorResource
import core.ai.dsl.AIResource
import core.commands.Command
import core.conditional.ConditionalString
import core.events.EventListener
import magic.spellCommands.SpellCommand
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import quests.StoryEvent
import quests.StoryEventResource
import traveling.location.location.LocationDescriptionResource
import traveling.location.weather.WeatherStringResource
import java.io.File
import java.lang.reflect.Modifier
import kotlin.reflect.KClass

//This has access to JVM stuff, but common main doesn't
object ReflectionToolsTest {
    private val topLevelPackages = getPrefixes()
    private val reflections = Reflections(topLevelPackages, SubTypesScanner(false))

    private fun getPrefixes(): List<String> {
        return File("./src/main/kotlin").listFiles()!!.filter { it.isDirectory }.map { it.name }.sorted()
    }

    fun generateFiles() {
        generateCollectionsFile(Command::class)
        generateCollectionsFile(SpellCommand::class)
        generateCollectionsFile(EventListener::class)

        generateResourcesFile(AIResource::class, AIBase::class)
        generateResourcesFile(AIActionResource::class, AIAction::class)
        generateResourcesFile(BehaviorResource::class, Behavior::class)
        generateResourcesFile(DialogueResource::class, Dialogue::class)
        generateResourcesFile(LocationDescriptionResource::class, ConditionalString::class)
        generateResourcesFile(StoryEventResource::class, StoryEvent::class)
        generateResourcesFile(WeatherStringResource::class, ConditionalString::class)
    }

    fun getClasses(superClass: KClass<*>): List<Class<*>> {
        return reflections.getSubTypesOf(superClass).filter { !Modifier.isAbstract(it.modifiers) }.sortedBy { it.name }
    }

    /**
     * Find all classes that extend the collected class interface (Command) and dump them into a list in the generated class (CommandsGenerated)
     */
    private fun generateCollectionsFile(collectedClass: Class<*>) {
        val allClasses = getClasses(collectedClass)
        println("Saving ${allClasses.size} classes for ${collectedClass.name}")
        val isTyped = collectedClass.typeParameters.isNotEmpty()
        val typeSuffix = if (isTyped) {
            "<*>"
        } else {
            ""
        }

        val classes = allClasses.joinToString(", ") { "${it.name}()".replace("$", ".") }
        val newClassName = collectedClass.simpleName

        writeInterfaceFile(collectedClass, collectedClass, typeSuffix, newClassName)
        writeGeneratedFile(collectedClass, typeSuffix, classes)
        writeMockedFile(collectedClass, collectedClass, typeSuffix, newClassName)
    }

    /**
     * Takes Two KClasses
     * 1) The Resource interface to look for
     * 2) The implementation or collected class
     * Find all classes that extend the resource interface (WeatherStringResource) and combines their values into a list in the generated class (WeatherStringsGenerated)
     */
    private fun generateResourcesFile(resourceInterface: Class<*>, collectedClass: Class<*>) {
        val allClasses = reflections.getSubTypesOf(resourceInterface).filter { !Modifier.isAbstract(it.modifiers) }
            .sortedBy { it.name }
        println("Saving ${allClasses.size} classes for ${resourceInterface.name}")
        val isTyped = collectedClass.typeParameters.isNotEmpty()
        val typeSuffix = if (isTyped) {
            "<*>"
        } else {
            ""
        }
        val classes = allClasses.joinToString(", ") { "${it.name}()".replace("$", ".") }
        val newClassName = resourceInterface.simpleName.replace("Resource", "")

        writeInterfaceFile(resourceInterface, collectedClass, typeSuffix, newClassName)
        writeGeneratedResourceFile(resourceInterface, classes, newClassName)
        writeMockedFile(resourceInterface, collectedClass, typeSuffix, newClassName)
    }

    private fun writeInterfaceFile(
        resourceInterface: KClass<*>,
        collectedClass: KClass<*>,
        typeSuffix: String,
        newClassName: String
    ) {
        val packageName = resourceInterface.packageName.replace(".", "/")
        File("./src/main/kotlin/$packageName/${newClassName}sCollection.kt").printWriter().use {
            it.print(
                """
                package ${resourceInterface.packageName}
                import ${collectedClass.name}

                interface ${newClassName}sCollection {
                    val values: List<${collectedClass.simpleName}$typeSuffix>
                }
            """.trimIndent()
            )
        }
    }

    private fun writeGeneratedFile(collectedClass: KClass<*>, typeSuffix: String, classes: String) {
        val packageName = collectedClass.packageName.replace(".", "/")
        File("./src/main/kotlin/$packageName/${collectedClass.simpleName}sGenerated.kt").printWriter().use {
            it.print(
                """
                package ${collectedClass.packageName}

                class ${collectedClass.simpleName}sGenerated : ${collectedClass.simpleName}sCollection {
                    override val values: List<${collectedClass.name}$typeSuffix> = listOf($classes)
                }
            """.trimIndent()
            )
        }
    }

    private fun writeGeneratedResourceFile(resourceInterface: KClass<*>, classes: String, newClassName: String) {
        val packageName = resourceInterface.packageName.replace(".", "/")

        File("./src/main/kotlin/$packageName/${newClassName}sGenerated.kt").printWriter().use {
            it.print(
                """
                package ${resourceInterface.packageName}

                class ${newClassName}sGenerated : ${newClassName}sCollection {
                    override val values = listOf<${resourceInterface.simpleName}>($classes).flatMap { it.values }
                }
            """.trimIndent()
            )
        }
    }

    private fun writeMockedFile(
        resourceInterface: KClass<*>,
        collectedClass: KClass<*>,
        typeSuffix: String,
        newClassName: String
    ) {
        val packageName = resourceInterface.packageName.replace(".", "/")
        val file = File("./src/test/kotlin/$packageName/${newClassName}sMock.kt")
        file.parentFile.mkdirs()
        file.printWriter().use {
            it.print(
                """
                package ${resourceInterface.packageName}
                import ${collectedClass.name}

                class ${newClassName}sMock(override val values: List<${collectedClass.simpleName}$typeSuffix> = listOf()) : ${newClassName}sCollection
            """.trimIndent()
            )
        }
    }


}