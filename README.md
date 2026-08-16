# Hyplook

A client-side temporary free-look ("perspective") camera for Minecraft 1.8.9 Forge. Hold the keybind and the camera swings out behind your character so you can look around without turning the player - your hitbox, movement direction, aiming and nametag-facing all stay locked to where you were looking when you pressed the key.

## Features

- Hold or toggle mode (default: hold)
- Configurable keybind, including mouse buttons
- Invert camera pitch option
- Nametags always face the camera while the perspective is active
- Chat commands and an in-game config GUI
- All settings persist to `config/hyplook.cfg`

## Usage

Default keybind is **Left Alt**.

| Action | Command |
| --- | --- |
| Show the help menu | `/hyplook` or `/hyplook help` |
| Open the config GUI | `/hyplook gui` |
| Enable / disable | `/hyplook on` / `/hyplook off` |
| Toggle | `/hyplook toggle` |
| Toggle hold mode | `/hyplook hold` |
| Toggle invert pitch | `/hyplook invert` |
| Toggle camera-facing nametags | `/hyplook nametags` |
| Set keybind (`LALT`, `RALT`, `RSHIFT`, `F5`, `mouse3`, `-97`, ...) | `/hyplook key <key>` |

Mouse button key codes are negative: `-100` = left, `-99` = right, `-98` = middle, `-97` = mouse 3, `-96` = mouse 4. Named keys accept common aliases too (`LALT`, `RALT`, `LCTRL`, `RCTRL`, `ESC`, `ENTER`, `PGUP`, `PGDN`, `CAPSLOCK`, ...).

## How it works

No coremod. The camera is driven entirely from Forge events plus one access transformer:

- `RenderTickEvent` (Phase.START) consumes the raw mouse deltas and feeds them into the mod's camera yaw/pitch using the exact vanilla sensitivity formula, so the player's real rotation never changes — the character keeps facing, moving and aiming exactly as if in first person.
- `EntityViewRenderEvent.CameraSetup` overrides the camera yaw/pitch/roll to orbit the camera around the player at the free-look angles.
- The vanilla third-person wall raycast follows the *real* aim (the wrong direction while free-looking), so the mod raycasts along the camera direction and writes the safe distance into `EntityRenderer.thirdPersonDistance`. The one access transformer simply makes that private field public; the transform is a supported Forge feature, not a coremod.
- Nametags (and other camera-facing billboards) are rotated toward the camera from `RenderLivingEvent.Specials.Pre`.
- `thirdPersonView` is held at 1 while active, so F5 and the vanilla inside-a-block camera reset can't fight the mod.

## Building

Requirements: **Java 8** (JDK, not just JRE) and **Gradle 3.1** (the wrapper is pinned to it). ForgeGradle 2.1 is not compatible with modern JDKs.

```
set JAVA_HOME=C:\path\to\jdk-8
gradlew setupDecompWorkspace
gradlew build
```

The compiled mod is `build/libs/hyplook-1.0.0.jar`. Drop it into your `mods/` folder.

## Usage Notice

This mod bypasses Hypixel's AntiFreeLook detection system.

Hypixel's rules do not permit the use of freelook mods. Using this mod on Hypixel is against their Terms of Service and may result in a ban.

Use at your own risk. The developer(s) of this mod are not responsible for any penalties, bans, or other consequences resulting from its use.
