package core.events

class EventListenersGenerated : EventListenersCollection {
    override val values: List<core.events.EventListener<*>> = listOf(combat.attack.Attack(), combat.block.Block(), combat.takeDamage.TakeDamage(), core.MessageHandler(), core.TurnListener(), core.ai.AIGameTickListener(), core.ai.AITurnDirector(), core.ai.DelayedEventListener(), core.commands.commandEvent.CommandEventListener(), core.events.multiEvent.MultiEventListener(), core.history.SessionListener(), core.properties.SetProperties(), core.properties.propValChanged.PropertyStatChanged(), core.properties.propValChanged.PropertyStatMinned(), core.target.item.ItemSpawner(), crafting.DiscoverRecipe(), crafting.checkRecipe.CheckRecipes(), crafting.craft.Craft(), explore.examine.Examine(), explore.look.Look(), explore.map.ReadMap(), inventory.ListInventory(), inventory.dropItem.ItemDropped(), inventory.dropItem.PlaceItem(), inventory.equipItem.EquipItem(), inventory.equipItem.ItemEquipped(), inventory.pickupItem.ItemPickedUp(), inventory.pickupItem.TakeItem(), inventory.putItem.TransferItem(), inventory.unEquipItem.ItemUnEquipped(), inventory.unEquipItem.UnEquipItem(), magic.ViewWordHelp(), magic.castSpell.CastSpell(), quests.CompleteQuest(), quests.QuestListener(), quests.journal.ViewQuestJournal(), quests.journal.ViewQuestList(), status.ExpGained(), status.LevelUp(), status.conditions.AddCondition(), status.conditions.ConditionRemover(), status.conditions.RemoveCondition(), status.effects.ApplyEffects(), status.rest.Rest(), status.statChanged.CreatureDied(), status.statChanged.PlayerStatMaxed(), status.statChanged.PlayerStatMinned(), status.statChanged.StatBoosted(), status.statChanged.StatChanged(), status.statChanged.StatMinned(), status.status.Status(), system.alias.CreateAlias(), system.alias.DeleteAlias(), system.alias.ListAlias(), system.debug.DebugListListener(), system.debug.DebugStatListener(), system.debug.DebugTagListener(), system.debug.DebugToggleListener(), system.debug.DebugWeatherListener(), system.help.ViewHelp(), system.history.ViewChatHistory(), system.message.DisplayMessage(), system.persistance.changePlayer.ListCharacters(), system.persistance.changePlayer.PlayAs(), system.persistance.loading.ListSaves(), system.persistance.loading.Load(), system.persistance.newGame.CreateNewGame(), system.persistance.saving.Save(), time.ViewTime(), time.gameTick.GameTick(), time.gameTick.TimeListener(), traveling.RestrictLocation(), traveling.arrive.ArrivalHandler(), traveling.arrive.Arrive(), traveling.climb.AttemptClimb(), traveling.climb.ClimbComplete(), traveling.jump.PlayerFall(), traveling.jump.PlayerJump(), traveling.location.weather.WeatherListener(), traveling.move.Move(), traveling.routes.FindRoute(), traveling.routes.ViewRoute(), traveling.scope.remove.RemoveItem(), traveling.scope.remove.RemoveScope(), traveling.scope.spawn.ActivatorSpawner(), traveling.scope.spawn.SpawnItem(), traveling.travel.TravelStart(), use.actions.ChopWood(), use.actions.DamageCreature(), use.actions.NoUseFound(), use.actions.ScratchSurface(), use.actions.StartFire(), use.actions.UseFoodItem(), use.actions.UseIngredientOnActivatorRecipe(), use.actions.UseItemOnIngredientRecipe(), use.actions.UseOnFire(), use.eat.EatFood(), use.interaction.Interact(), use.interaction.NoInteractionFound(), use.interaction.nothing.DoNothing())
}