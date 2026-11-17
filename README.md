# 🎬 CinematicHardcore

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.x-green.svg)
![Platform](https://img.shields.io/badge/Platform-Paper-orange.svg)
![Java](https://img.shields.io/badge/Java-21-red.svg)

**CinematicHardcore** is a Paper 1.21.x plugin that delivers an epic, fully **automated Cinematic Introduction** for players when they first join the server. It is seamlessly integrated with a true *Hardcore* system (permanent death) and stunning *Cinematic Fantasy* visuals.

---

## ✨ Key Features

- 🤖 **Auto-Play Cinematic:** **NO COMMANDS REQUIRED.** When a new player logs in, the plugin automatically detects them, freezes their movement, and plays the 8-scene Cinematic sequence.
- 🎥 **Cinematic Engine:** 8 distinct cinematic scenes (Opening, Welcome, Guide, Hardcore Warning, Enter World, Countdown, Deployment) with smooth transitions.
- 🗿 **3D Floating Totem:** A beautifully animated Totem of Undying floating and rotating in 3D using *ItemDisplay Entities*.
- 📸 **Camera Shake & Transitions:** Immersive camera shake effects (particles) and smooth screen transitions (Blindness/Darkness).
- ⏱️ **BossBar Countdown:** An epic red *BossBar* counting down before the player is deployed into the world.
- 🛡️ **Anti-Crash & Anti-Dupe:** Safely backs up the player's inventory before the cinematic. If the server crashes, inventories are preserved, and the cinematic is safely bypassed.
- 💀 **Real Hardcore System:** Permanently saves death status using UUIDs. Dead players cannot bypass this status just by reconnecting.
- 🌍 **Safe Deployment:** Searches for a random, safe spawn location (avoiding lava, ravines, or oceans) using block validation.
- 🌐 **Multi-Language GUI:** Supports English & Indonesian. Players can switch languages via the `/changelang` GUI.
- ⚙️ **Fully Configurable:** All text, durations, sounds, and particles can be customized via `config.yml`.

---

## ⚙️ How It Works (Automated)

1. **Player Joins:** A new player logs into the server.
2. **Automatic Check:** The plugin checks the database (by UUID). Has this player completed the intro before?
   * If **No**, the plugin delays for 1 second (to allow the client to load the chunks) and then **automatically starts the Cinematic** (locking the player's controls).
   * If **Yes**, the player joins normally without any interruption.
3. **Deployment:** After the cinematic ends, the player is automatically teleported to a safe location in the Overworld, and their controls are restored.

*Note: The `/intro` command is strictly for Admins for debugging purposes (force start, skip, or reset). Normal players never need to use it.*

---

## 📋 Requirements

- **Server:** Paper 1.21.x (or forks like Purpur)
- **Java:** JDK 21
- **Optional Dependency:** [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) (For placeholder support)

---

## 🚀 Installation

1. Download the `CinematicHardcore.jar` file from the **Releases** page.
2. Place the `.jar` file into your server's `plugins/` folder.
3. Start the server to generate the default configuration files.
4. (Optional) Install **PlaceholderAPI** if you wish to use placeholders.
5. Restart the server. Done! New players will automatically experience the Cinematic on their first join.

---

## 🎮 Commands & Permissions (Admin Only)

| Command | Description | Permission |
|---------|-------------|------------|
| `/guide` | Opens the server guide | `cinematichardcore.guide` (Default: All) |
| `/changelang` | Opens the language selection GUI | `cinematichardcore.guide` (Default: All) |
| `/intro start <player>` | Forces the cinematic to play for a player | `cinematichardcore.intro` (Default: OP) |
| `/intro skip <player>` | Skips the cinematic for a player | `cinematichardcore.intro` (Default: OP) |
| `/intro reset <player>` | Resets intro status (useful for testing) | `cinematichardcore.intro` (Default: OP) |
| `/intro reload` | Reloads config & language files | `cinematichardcore.intro` (Default: OP) |
| `/intro status <player>` | Checks a player's cinematic status | `cinematichardcore.intro` (Default: OP) |
| `/intro editor` | Opens the Config Editor GUI (Admin) | `cinematichardcore.intro` (Default: OP) |

**Bypass Permission:**
- `cinematichardcore.bypass` : Skips the auto-cinematic (highly recommended for Admin/Staff accounts) and grants immunity to the Hardcore death system.

---

## ⚙️ Important Configuration

All settings can be found in `plugins/CinematicHardcore/config.yml`.

```yaml
settings:
  # IF TRUE: Cinematic ONLY plays automatically for first-time players.
  # IF FALSE: Cinematic plays automatically EVERY TIME a player logs in.
  first-join-only: true 

cinematic:
  enabled: true
  # ... (Duration, title, and sound settings per scene)
  
hardcore:
  enabled: true
  death-message: "<red>You have died in Hardcore mode.</red>"
