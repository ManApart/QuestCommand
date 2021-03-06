package resources.storyEvents

import core.GameState
import inventory.pickupItem.ItemPickedUpEvent
import inventory.pickupItem.TakeItemEvent
import inventory.putItem.TransferItemEvent
import quests.StoryEvent
import quests.StoryEventResource
import quests.ConditionalEvents
import system.message.MessageEvent
import traveling.arrive.ArriveEvent
import use.interaction.InteractEvent

class ASimplePie : StoryEventResource {
    override val values: List<StoryEvent> = listOf(
            StoryEvent("A Simple Pie", 10, "I should pick up an Apple, which I could get by traveling to the Apple Tree.",
                    ConditionalEvents(InteractEvent::class.java,
                            { event, _ -> event.source == GameState.player && event.target.name == "Apple Pie Recipe" },
                            { _, _ -> listOf(MessageEvent("This pie sounds pretty simple to make. I should start by getting an Apple.")) }
                    )
            ),

            StoryEvent("A Simple Pie", 20, "I should slice the apple by using my dagger or something sharp on it.",
                    ConditionalEvents(TakeItemEvent::class.java,
                            { event, _ -> event.taker == GameState.player && event.item.name == "Apple" },
                            { _, _ -> listOf(MessageEvent("I should slice the apple by using my dagger or something sharp on it.")) }
                    )
            ),

            StoryEvent("A Simple Pie", 30, "I should use my dagger on the wheat in the Open Field to get some wheat.",
                    ConditionalEvents(ItemPickedUpEvent::class.java,
                            { event, _ -> event.source == GameState.player && event.item.name == "Apple" && event.item.properties.tags.has("Sliced") },
                            { _, _ -> listOf(MessageEvent("Next I should use my dagger on the wheat in the Open Field to get some wheat.")) }
                    )
            ),

            StoryEvent("A Simple Pie", 40, "I should place this wheat in the chute at the top of the windmill.",
                    ConditionalEvents(TakeItemEvent::class.java,
                            { event, _ -> event.taker == GameState.player && event.item.name == "Wheat Bundle" },
                            { _, _ -> listOf(MessageEvent("I should place this wheat in the chute at the top of the windmill.")) }
                    )
            ),

            StoryEvent("A Simple Pie", 50, "I can now pickup flour from the grain bin on the first floor.",
                    ConditionalEvents(TransferItemEvent::class.java,
                            { event, _ -> event.source == GameState.player && event.item.name == "Wheat Bundle" && event.destination.name == "Grain Chute" },
                            { _, _ -> listOf(MessageEvent("I can now pickup flour from the grain bin on the first floor.")) }
                    )
            ),

            StoryEvent("A Simple Pie", 60, "I should travel to farmer's hut and use a bucket on the well to get water.",
                    ConditionalEvents(TakeItemEvent::class.java,
                            { event, _ -> event.taker == GameState.player && event.item.name == "Wheat Flour" },
                            { _, _ -> listOf(MessageEvent("To make dough I'll need some water, which I can get from a well.")) }
                    )
            ),

            StoryEvent("A Simple Pie", 70, "I should use water on flour to make dough.",
                    ConditionalEvents(TakeItemEvent::class.java,
                            { event, _ -> event.taker == GameState.player && event.item.name == "Bucket of Water" },
                            { _, _ -> listOf(MessageEvent("Now that I have water, I should use it on some flour.")) }
                    )
            ),

            StoryEvent("A Simple Pie", 80, "I should combine the sliced apple, dough, and a pie tin on the range in the Farmer's Hut Interior.",
                    ConditionalEvents(TakeItemEvent::class.java,
                            { event, _ -> event.taker == GameState.player && event.item.name == "Dough" },
                            { _, _ -> listOf(MessageEvent("Now that I have dough, I should combine the ingredients on the range in the Farmer's Hut Interior.")) }
                    )
            ),

            StoryEvent("A Simple Pie", 90, "I should take the pie tin, light the range, and then type 'craft apple pie`.",
                    ConditionalEvents(ArriveEvent::class.java,
                            { event, _ -> event.destination.location.name == "Farmer's Hut Interior" },
                            { _, _ -> listOf(MessageEvent("I should take the pie tin, light the range, and then type 'craft apple pie`.")) }
                    )
            ),

            StoryEvent("A Simple Pie", 100, "I've baked an an Apple Pie! That was easier than expected!",
                    ConditionalEvents(TakeItemEvent::class.java,
                            { event, _ -> event.taker == GameState.player && event.item.name == "Apple Pie" },
                            { _, _ ->
                                listOf(MessageEvent("I've baked an an Apple Pie! That was easier than expected!"))
                            }
                    ), availableAfter = 10, completesQuest = true
            ),
    )

}