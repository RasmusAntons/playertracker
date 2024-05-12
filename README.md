# PlayerTracker

A Fabric mod that gives players a player tracking compass.

## Features:

The mod gives every player a *player tracker*, a compass that can be used to cycle through all online players and point to their current location.

The player cannot be dropped, traded or put into any non-player inventory.

## Usage:

The mod should be installed on server and client.
It technically works server side only, but the client side removes the update animation whenever the compass target changes.

To make sure every player has a player tracker at all times, enable the game rule `givePlayerTracker`.
