# Quest Command

An open world rpg with intense levels of interaction, experienced through the command prompt.

See also the [main readme](./readme.md). For in-development features see [the notes](./notes.md).

## Design

### Goals

Create a world more interactive than Skyrim by trading presentation layer for higher levels of interaction.

### Design Pillars

- Least effort possible for content creation
  - Content should be as concise as possible without losing clarity
  - Everything should have a reasonable default
  - Content creators should only need to specify what makes an item etc unique
- Command/Event/Listener model
  - Commands simply parse / understand user input and then create events
  - Commands do not handle or change state
  - Commands should be unknown to game state, events, and logic
  - All intents and actions are created through events
  - Listeners subscribe to individual events, update gamestate and print to console.

### General Design Notes

Any time an activator adds a new triggered event, it needs to be added to the triggered event when statement

Location positions are always relative to their parent. The parent is always (0,0,0). If a location is compared with a location outside the parent, the parent locations are compared.

An event, command and listener should share a package. Event should end in Event, command in Command, and listener without a suffix, with the same main name. If a listener is player only etc, prefix it with player
Ex:
- LookCommand
- LookEvent
- Look
- PlayerLook

Design note
- If a keyword is parsed without args, always show response request
- If an alias is parsed without args, use a reasonable default if it exists, otherwise response request
- A user that knows what he is doing will want the default to be used and will use an alias. A user that doen’t know what he wants will probably use the full default keyword, and will want to know his options



### Research
- manic mansion SCUM
- ducktype
- context free gramer, tokenizers, lexers, (yacc, lex)

## Game Systems Explanation

Below are explanations of how the game works today. See `notes.md` for thoughts and ideas on systems that have not been implemented yet.


### Crafting

#### Recipes

Recipes take in a set of skills, properties for the required tool, and a set of ingredients. They can be triggered by explicitly crafting a recipe or by using an ingredient on a tool etc. Sliced Apple can be created by `craft sliced apple`, by `use dagger on apple` or by `slash apple` if the ingredients, tools, and skills are all present.

In the case of an Apple Pie, an Apple, Pie Tin and Dough are required. The Apple must have the tag `Sliced`.
```
"name": "Apple Pie",
"ingredients": [
  {
    "name": "Apple",
    "tags": "Sliced"
  },
  "Pie Tin",
  "Dough"
]
```


In the case of baked fruit, we don't know exactly what fruit we'll be given, because the ingredient doesn't have a name, but we do know it must have the `Fruit` and `Raw` tags.
```
"name": "Baked Fruit",
"ingredients": [
  {
    "tags": [
      "Fruit",
      "Raw"
    ]
  }
],
```

Recipes generally yield a single result: `"results": ["Apple Pie"]`, but they can also yield multiple items, like in the case of Dough: `"results": ["Dough","Bucket"]`.

They can also add or remove tags to a passed in ingredient. In the case of baked fruit, the first passed in ingredient (referenced by `"id": 0`) is given the baked tag and loses the raw tag:
```
  "results": [
   {
     "id": 0,
     "tagsAdded": [
       "Baked"
     ],
     "tagsRemoved": [
       "Raw"
     ]
   }
 ]
 ```
 A new item can be created (using the `name` property instead of the `id` property) and have tags added / removed as well.


A couple things to note:
- All conditions for recipes are `AND`; all conditions must be met for a recipe to be used.
- In the above Baked Fruit example you could not re-bake the fruit because `Fruit` must be `Raw` and that tag is removed from the result.
- A recipe without any tool properties can be made without a specific tool.


### Effects

#### Chemistry Engine

Whenever a new effect is applied, onFirstApply(newEffect, targetSoul) is called. For elements that have a relationship, the strengths are compared and the weaker effect is cleared / the element logic is applied.

Replace burn-health stat with burn-strength. On scene load, chemestry engine should give targets with certain tags the flamable effect. Wood gets a flammable effect with a strength of 10. Kindling gets a flammable effect with a strength of 1, etc. 
Based on tags, chemistry engine should give other effects (wet, earth etc). To light a tree (with flammable strength 10) on fire, the player needs to use an attack / element with a strength greater than 10. This is a change from burn health that can be drained.


#### Elements

Name | Clears | Notes
--- | --- | ---
Fire | Water, Ice
Water  | Fire
Earth | Lightning, Fire
Stone |
Air | Water
Air | Earth | Must be twice the strength
Air | Fire |
Air | Fire | If same or lower strength, will buff fire
Lightning | Water | Duration is doubled and water is cleared
Ice | Fire 
Ice | Water | Duration is doubled and water is cleared


#### Technical Structure

Hard code effect bases as interface / classes. Effect instances are generated by spells / quest events etc. They’re given a name to find the base, and then amount/duration. 

Effect Bases have
- name
- description
- statAffectType
- element
- amountType: percent or flat number
- damageType
- target: stat this effect targets

Effect Instances have
- amount
- duration (number of turns applied)
- elementStrength
- bodypart

**DamageType**

If the stat to affect is heatlh, this property should explain what type of damage is done to the health (so that it can be defended against

Energy is another type of damage (slash, crush etc). This is used for fire, water, air, lightning, etc.

**Element** 

Used for the chemestry engine. Fire, water, lightning, none, etc.

**ElementStrength**

How strong the element is for this effect. Usually similiar to cost formula. Used for the chemestry engine. (immutable)

**StatAffectTypes**

Type | Description
--- | ---
Drain | Each turn reduce current stat by this amount
Deplete | Only reduce the stat once (when first applied). When the effect is removed, recover the stat by the same amount
Boost| Only increase the stat once (when first applied. Reduce the stat by the same amount when the effect is removed
Recover| Each turn increase current stat by this amount
None | 

**Duration**

Will be commuted to 0 if an effect is cleared due to element strength. If 0/blank, there is no duration and the effect is permanent until cured.

If an effect is applied to a target that already has that effect’s clear-by effect, Both effects are canceled For instance if a target is wet and is given the burning tag, both effects are removed. Another example: a target is burning and then given the encased effect; both effects are canceled.

#### Example Effects

Name | Stat | Type | Element | Damage Type | Description
--- | --- | --- | --- | --- | ---
Burning | Health | Drain | Fire | Energy | Damage over time 
Wet | | | Water | | Slightly reduce agility 
Encased  | | | Earth | | Greatly reduce agility. Prevent movement. Greatly increases slash and stab defense. 
Air Blasted | | | Air| | Ends instantly but may clear other statuses
Shocked | | | Lightning | | Deplete 100 action points
Frozen | | | Ice | | Drain 100 action points
Stunned | | | None | | Deplete 50 action points. Immediately clears



#### Misc

Should effects be an effect group with an element and then child effects?

trigger off effect/condition instead of tag


### Triggered Events

See the Triggered Event class to see what events can be used, and what params they can accept

### Story Events

Quests are made up of Story Events. Each event has a stage. When the event runs the quest is automatically updated to be that stage. The first valid stage found will be executed.

Properties of note:

- Completed - Event has been completed
- Repeatable - Can be hit even if completed before
- AvailableAfter - After (`>=`) this stage this event can be hit if the conditions are met, even if that means skipping other stages. If blank available only when this is the next highest stage after current stage
- AvailableUntil - This event can be hit up until this stage (`<=`). If blank available until is just the next highest quest stage after this

Available after and available before default to the story event's stage, meaning the story event cannot be hit unless the event is the next stage in the quest.


### Trigger Conditions

Trigger Conditions evaluate when a looked for calling event is posted. It uses a combination of event params (values of the event variables) and queries to evaluate true or false.

```
"condition": {
  "callingEvent": "UseEvent",
  "eventParams": {
    "used": "$used"
  }
},
```
Trigger conditions get the current values of the event properties and can use them for evaluation. For example, the used event has values `source`, `used`, and `target`. The above condition will trigger on any `UseEvent` because it's checking that the `used` property of the event matches its own value (`$used`).


#### Querying Game State

A Query object is used to evaluate a game value against a given value to see if the event should be executed. It contains the following fields

- Property to query on
- Params passed to that query
- The operator to evaluate `>, <, >=, <=, =, !=`
- The value to compare against

See the `GameStateQuery` class to see a list of queryable values and their parameters.

### Locations

#### Networks

A network is an isolated collection of locations. Networks provide routes and allow you to travel from one place to another. Bodies also have a network that describes the spacial relationships of body parts.

Network names must be globally unique.

#### Location Nodes

Location Nodes contain the spacial relationship data for a location, as opposed to a `Location` that contains the contents of a location. They contain a parent network, the name of their content location, and links to other locations.

Location Node names must be unique within their parent network.


```
  {
    "name": "Kanbara Wall",
    "locationName": "Kanbara Wall",
    "parent": "Kanbara",
    "locations": [
      {
        "name": "Guard Post",
        "position": {
          "x": -1
        }
      }
    ]
  },
```

#### Locations
Locations describe an area and contain

- A unique name (Required)
- A description
- A list of items
- A list of activators
- A list of creatures
- Possibly a body part, if this is a location within a body

Location names must be globally unique.

When specifying a list of targets (items, activators, creatures), you can give just their name, or their name and location within the location. This is just a string of flavor text used for the look command:
```
{
    "name": "Apple",
    "location": "at the base of the tree"
}
```

#### Location Paths
Location Paths describe the relationship between locations. They contain

- A unique name (Required)
- A unique name of the location it is adjacent to(Required)
  - For each attached location, optionally a position relative to the current location
  - When parsed, the attached location will get a reference back to the current location with the proper position so that they can be referenced either way
  - The attached location will not get a link back if it already has an explicit link or the link is marked with the `oneWay` keyword.
- A parent name - used only in descriptions (ex: Blue Cave is part of Kanbara Wilds)
- Restricted - locations are visible but can't be traveled to (for things like climbing trees or opening doors)

## Testing

If tests are running slow, make sure that json parsing and reflection are not happing as part of unit tests. An easy check is to place a debug point in `getImplementation` to catch any time a default implementation is being used in a unit test.