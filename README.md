# 🎬 CinematicHardcore

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.x-green.svg)
![Platform](https://img.shields.io/badge/Platform-Paper-orange.svg)
![Java](https://img.shields.io/badge/Java-21-red.svg)

**CinematicHardcore** adalah plugin Paper 1.21.x yang menghadirkan pengalaman *Cinematic Introduction* yang epik dan mulus untuk pemain baru, dipadukan dengan sistem *Hardcore* yang sesungguhnya (kematian permanen).

Plugin ini terinspirasi dari video referensi Minecraft dengan visual *Cinematic Fantasy Hardcore*, dilengkapi dengan animasi Totem 3D, partikel, transisi layar, dan sistem deployment spawn yang aman.

---

## ✨ Fitur Utama

- 🎥 **Cinematic Engine:** 8 Scene sinematik (Opening, Welcome, Guide, Hardcore Warning, Enter World, Countdown, Deployment).
- 🗿 **3D Floating Totem:** Animasi Totem of Undying yang melayang dan berputar secara 3D menggunakan *ItemDisplay Entity*.
- 📸 **Camera Shake & Transitions:** Efek getar kamera (partikel) dan transisi layar (Blindness/Darkness) yang halus.
- ⏱️ **BossBar Countdown:** Hitungan mundur sebelum terjun ke dunia menggunakan *BossBar* merah yang epik.
- 🛡️ **Anti-Crash & Anti-Dupe:** Menyimpan inventory pemain secara aman sebelum cinematic. Jika server crash, inventory tidak akan hilang.
- 💀 **Real Hardcore System:** Menyimpan status kematian permanen menggunakan UUID. Pemain yang mati tidak bisa lolos hanya dengan *reconnect*.
- 🌍 **Safe Deployment:** Mencari lokasi spawn acak yang aman (bebas lava, jurang, atau laut) dengan validasi blok.
- 🌐 **Multi-Language GUI:** Mendukung Bahasa Indonesia & Inggris. Pemain dapat mengganti bahasa via GUI `/changelang`.
- 📢 **Discord Webhook:** Kirim notifikasi otomatis ke Discord saat ada pemain yang mati Hardcore.
- ⚙️ **Fully Configurable:** Semua teks, durasi, suara, dan partikel dapat diubah melalui `config.yml`.
- 🔗 **PlaceholderAPI Support:** Mendukung placeholder dari plugin lain (misal: Rank LuckPerms).

---

## 📋 Persyaratan (Requirements)

- **Server:** Paper 1.21.x (atau fork seperti Purpur)
- **Java:** JDK 21
- **Dependensi Opsional:** [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) (Untuk fitur Placeholder)

---

## 🚀 Instalasi (Installation)

1. Download file `CinematicHardcore.jar` dari halaman **Releases**.
2. Masukkan file `.jar` ke dalam folder `plugins/` di server Anda.
3. Nyalakan server untuk menghasilkan file konfigurasi default.
4. (Opsional) Install plugin **PlaceholderAPI** jika ingin menggunakan fitur placeholder.
5. Restart server. Selesai!

---

## 🎮 Perintah & Permission (Commands & Permissions)

| Command | Deskripsi | Permission |
|---------|-----------|------------|
| `/guide` | Membuka panduan server | `cinematichardcore.guide` (Default: All) |
| `/changelang` | Membuka GUI pemilih bahasa | `cinematichardcore.guide` (Default: All) |
| `/intro start <player>` | Memulai cinematic secara paksa | `cinematichardcore.intro` (Default: OP) |
| `/intro skip <player>` | Melewati cinematic pemain | `cinematichardcore.intro` (Default: OP) |
| `/intro reset <player>` | Mereset status intro pemain | `cinematichardcore.intro` (Default: OP) |
| `/intro reload` | Reload config & bahasa | `cinematichardcore.intro` (Default: OP) |
| `/intro status <player>` | Melihat status cinematic pemain | `cinematichardcore.intro` (Default: OP) |
| `/intro editor` | Membuka GUI Editor Config (Admin) | `cinematichardcore.intro` (Default: OP) |

**Permission Lainnya:**
- `cinematichardcore.bypass` : Melewati cinematic dan sistem kematian Hardcore (Default: OP).

---

## ⚙️ Konfigurasi (Configuration)

Semua pengaturan dapat ditemukan di `plugins/CinematicHardcore/config.yml`.

```yaml
settings:
  first-join-only: true # Jika true, cinematic hanya untuk pemain baru

cinematic:
  enabled: true
  opening:
    duration: 9
    title: "<gradient:#00ff88:#00ccff><bold>GILIRANMU TIBA!</bold></gradient>"
    # ...
  deployment:
    world: world
    min-distance-from-spawn: 500
    max-distance-from-spawn: 3000
    max-attempts: 50

hardcore:
  enabled: true
  death-message: "<red>Kamu telah mati dalam mode Hardcore.</red>"

discord:
  enabled: false
  webhook-url: "URL_DISCORD_ANDA_DISINI"