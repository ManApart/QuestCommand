# Quest Command

[![Build Status](https://travis-ci.org/ManApart/QuestCommand.svg?branch=master)](https://travis-ci.org/ManApart/QuestCommand)

An open world rpg with intense levels of interact, experienced through the command prompt

## Building / Running

### Building

Run AppBuilder's main method. I use the ide. This generates files so they don't need to be done at runtime. This only needs to be re-run when you change certain files (like adding new commands, events, event listeners etc)

Run `gradlew build jar` to build a jar. This should be found in `QuestCommand/build/libs` (`quest-command-1.0-SNAPSHOT`)


### Running
Navigate to `QuestCommand/build/libs` and run `java -jar quest-command-1.0-SNAPSHOT.jar`

## What can I do?

At this point there is very little solid functionality, but there are a few basic things you can do.

Start by typing `help`, which should get you started and give you a list of commands.

Try using commands like `look` and `map` to explore the world.

You can travel by traveling to a location `travel tree`, or you can just say a direction (`west` or `w`).

Try finding the apple tree and picking up some apples, climbing it, or lighting it on fire with your tinder box. You could also chop it down if you wanted to.

There is very basic inventory functionality and you can equip weapons to different hands.

You can find and kill a rat, and then use its meat on the range in the nearby farm house in order to cook it.

You can also pipe commands together using `&&`.


Here are some example commands you can run:

`w && use tinder on tree && use apple on tree`
`w && use tinder on tree && bag && pickup apple && bag && use apple on tree`
`travel tree && climb tree`
`travel an open field barren patch && equip hatchet && chop rat && slash rat`
`travel farmer's hut interior && cook Raw Poor Quality Meat on range`