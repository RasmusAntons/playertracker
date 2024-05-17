# PlayerTracker

A Fabric mod that gives players a player tracking compass.

![2024-05-17_17 16 57](https://github.com/RasmusAntons/playertracker/assets/6364347/44cca5f3-ba09-4c65-8c43-96653e2f1447)


## Features:

The mod gives every player a *player tracker*, a compass that can be used to cycle through all online players and point to their current location.

The player tracker cannot be dropped, traded or put into any non-player inventory.

## Usage:

The mod should be installed on server and client. \
It technically works server side only, but the client side removes the update animation whenever the compass target changes.
Also, currently the actionbar text only works correctly if the mod is installed on the client, I think this can be resolved though.

To make sure every player has a player tracker at all times, enable the game rule `givePlayerTracker`. \
Otherwise, a player tracker can be given with `/give @p minecraft:compass[minecraft:custom_data={playertracker:1b}]`, in this case they will be deleted if dropped by the player.
