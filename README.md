<div align="center">

# Splitscreen Support

**Local multiplayer for Minecraft on a single screen.**

Fabric mod for Minecraft 26.3

[![Minecraft](https://img.shields.io/badge/Minecraft-26.3-green)](https://www.minecraft.net/)
[![Fabric](https://img.shields.io/badge/Fabric-0.20.1%2B-purple)](https://fabricmc.net/)
[![License](https://img.shields.io/badge/License-MIT-blue)](LICENSE)

</div>

---

Splitscreen Support turns multiple instances of Minecraft running on one machine into a coordinated split-screen setup. Each instance automatically positions itself in its own region of the screen, joins the same world, and coordinates audio so players can share a desk without overlapping sounds.

No configuration, no IP addresses, no command line. Launch the game twice and play.

## Features

### Automatic Window Positioning

Each game instance detects the others on startup and claims a region of the screen:

- **Horizontal split** — two players side by side
- **Vertical split** — top and bottom
- **Quad split** — up to four players
- **Triple split** — automatic layout for three players

Windows are borderless and snapped together with a configurable gap. When an instance closes, the remaining windows automatically reposition to fill the freed space.

### Layout Picker (F11)

Press F11 in-game to open a visual layout picker with live previews. Select a layout and every connected instance repositions instantly. Layouts can also be rotated, so each player can claim their preferred quadrant.

### Instant World Joining

A **Join** button appears on the title screen whenever another instance is in a world:

- One player creates or opens a world, singleplayer or LAN
- Every other instance sees the world and joins with one click
- Works with LAN worlds and remote servers
- No addresses to copy, no manual setup

### Proximity Audio

When two players' characters are within 16 blocks of each other, the mod mutes one instance's game audio so the same sounds do not play twice. Music, UI sounds and master volume are preserved. The mute is released automatically when the players separate.

### Instance Awareness

Each window displays the player's username in the corner, so you always know which screen belongs to whom.

## Requirements

| Component | Version |
|---|---|
| Minecraft | 26.3 |
| Fabric Loader | 0.20.1 or later |
| Fabric API | 0.165.0+26.3 or later |
| Java | 25 |

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 26.3
2. Download [Fabric API](https://modrinth.com/mod/fabric-api) and place it in your `mods` folder
3. Download the latest release of Splitscreen Support and place it in your `mods` folder
4. Launch the game once per player — on separate instances of the launcher

### Setting Up Multiple Instances

Use any launcher that supports isolated instances, such as Prism Launcher or Modrinth App:

1. Create one instance with the mod installed
2. Duplicate the instance once per additional player
3. Launch all instances
4. Press F11 to pick a layout, or let the automatic layout apply

## Recommended Mods

These mods significantly improve the split-screen experience:

- [**Sodium**](https://modrinth.com/mod/sodium) — rendering optimizations; split-screen multiplies GPU load
- [**C2ME**](https://modrinth.com/mod/c2me) — parallel chunk loading for smoother world loading
- [**Controllify**](https://modrinth.com/mod/controllify) — full controller support, one controller per player

## How It Works

Instances communicate over a loopback UDP bridge on `localhost:25570` — no external network traffic and no files shared between instances. The first instance acts as coordinator: it assigns each instance a number, relays layout changes, and announces the shared world address. Player positions are exchanged so proximity audio can react.

## Building From Source

```bash
git clone https://github.com/Guroun/splitscreen.git
cd splitscreen
./gradlew :fabric:build
```

The jar is produced in `fabric/build/libs/`. Building requires JDK 25.

## Credits

- **Guroun** — 26.3 port, bug fixes and audio coordination improvements
- **[pcal43](https://github.com/pcal43)** — original [mc-splitscreen](https://github.com/pcal43/mc-splitscreen) mod, which this project is forked from

## License

This project is licensed under the [MIT License](LICENSE).
