# BattleShips

Simple command-line battleships game app based on TCP/IP.

## Startup parameters
```
The application requires the following parameters to start:
> -mode [server|client] - defines whether the app starts as a client or a server.
> -port N  - defines the port on wchich the apps are supposed to communicate.
> -map map-file - the path to the map used by the player during this game session.
```
Example input:
```
Java Battleships server 8080 [map-pathfile]
Java Battleships client 8080 [map2-pathfile]
```
### Map
```
  The map-file is a .txt file made of the following symbols:
  > . - empty space on the map
  > # - boat tile
  After one of the players makes the winning move both client and the server app
  print out the enemy's map as well as its own, with new symbols providing more information about the game's result:
  > @ - Successfull hit by the enemy player
  > ~ - Shot missed by the enemy player
  > ? - Field that were not revealed over the course of the game(when receiving the enemy's map after a loss)
```
#### Commands
```
  The game begins when both players are connected and the client-app sends out a start;[field] command.
  From then on players echange [field] commands, receiving instant feedback on wheter or not their shot
  was successful.
  The [field] command format consists of two symbols - A capital letter from the range <A-J> and a number in the range of <0-9>
