# HttpInfoServer
Minecraft fabric mod that collects and sends in-game information to **Minecraft HTTP API server** (another my repo). The project consists of `Minecraft fabric mod (agent)` and `HTTP API server`. The code of the Minecraft fabric mod is located in this repo and the HTTP API server code is located by this link: [\*link\*](https://github.com/dadencukillia/httpInfoServer). Read the full manual to get everything set up correctly.

# Manual
The full manual you can read here: [read](https://gist.github.com/dadencukillia/006473a91295191963596ab21f9d3b8b)

# Build
I recommend you use IDEAs like `IntelliJ IDEA` or `Eclipse`. But If you would like to generate a `.jar` file without any IDEA you can use the command `gradle build` in the project root (be sure Java version 17 and higher is installed).

# Installing
Be sure that you already installed the Fabric of a needed version of Minecraft. Follow the link with releases [\*link\*](https://github.com/dadencukillia/httpInfoServer-mod/tags). Choose and download the **.jar** file needed version of Minecraft (also check the patch version). Put the downloaded file to the `.mods` folder (it is located by the `%AppData%/.minecraft/mods` path in most cases). Run Minecraft using fabric installation in your launcher.

# Contribution
I will be glad if you can help in the development of this project. What you can do:
- Spelling correction. My English is not good, so you can correct the comments in the code or the text in the files (like [README.md](https://github.com/dadencukillia/httpInfoServer-mod/blob/main/README.md)).
- Code cleanup
- Add new features or improve old ones

## Code structure:
The code files are located in the folder that follows the path:  `src/main/java/com.crocoby`. There are some files in this folder:
- `HttpInfoServer.java` — have an entrance method that is called `onInitialize` and starts when Minecraft loads. We create and run WebSocket instances in the `onInitialize` method.
- `WebSocketDoor.java` — The project uses the [TooTallNate/Java-WebSocket](https://github.com/TooTallNate/Java-WebSocket) library to make a WebSocket client. The WebSocket class describes the behavior of the WebSocket client that connects to the WebSocket server (another repo). When the server sends a message with the text "collectData" the WebSocket client uses `InfoCollector` class (this behavior is described in the `onMessage` method).
- `InfoCollector.java` — Have the `genJson` function that collects data and turns it into JSON string format. This method needs to generate a response for the WebSocket server.
- `mixin/WorldJoinMixin.java` — Have the `init` method that invokes whenever a player joining to any world (server works too). It sends a message in the in-game chat when the WebSocket client is not connected to the WebSocket server.
