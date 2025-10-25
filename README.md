# 🌈 PacketGlowAPI

![Maven Package](https://img.shields.io/badge/Maven-GitHub%20Packages-blue?logo=apache-maven)
![Version](https://img.shields.io/badge/version-1.0.0-green)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.4+-yellow?logo=minecraft)

PacketGlowAPI es una API ligera para **Spigot/Paper 1.21.4** que permite aplicar efectos de **Glow (brillo)** en colores personalizados **sin usar ProtocolLib ni teams visibles**.  
Funciona directamente usando el sistema de **scoreboards internos y team client-side** de Minecraft para mostrar brillos a todos o a jugadores específicos.

---

✨ Características

- Soporte para los 16 colores vanilla de Minecraft.
- Aplicar glow a jugadores y entidades.
- Mostrar glow por tiempo determinado.
- Sin uso de ProtocolLib ni equipos visibles.
- API simple y estática, fácil de integrar.
- Compatible con **Spigot**, **Paper**, **Purpur** y forks modernos.
- Fácil integración con Gradle y Maven.

---

🛠️ Instalación / Dependencia

Maven
Agrega esto en tu pom.xml:

<repositories>
  <repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/NicoHaxz/PacketGlowAPI</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>io.github.nicohaxz</groupId>
    <artifactId>packetglowapi</artifactId>
    <version>1.0.5</version>
  </dependency>
</dependencies>

Gradle (Groovy DSL)
repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.github.com/NicoHaxz/PacketGlowAPI") }
}

dependencies {
    implementation("io.github.nicohaxz:packetglowapi:1.0.5")
}

Gradle (Kotlin DSL)
repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.github.com/NicoHaxz/PacketGlowAPI") }
}

dependencies {
    implementation("io.github.nicohaxz:packetglowapi:1.0.5")
}

---

🚀 Inicialización

En tu plugin principal:

@Override
public void onEnable() {
    GlowAPI.initialize(); // Inicializar la API
}

@Override
public void onDisable() {
    GlowAPI.shutdown(); // Apagar la API
}

---

💡 Ejemplos de uso

Hacer que un jugador brille en rojo por 10 segundos:
GlowAPI.setCustomPlayerGlow(player, org.bukkit.ChatColor.RED, 200);

Hacer que una entidad brille en verde para todos:
GlowAPI.setCustomEntityGlow(entity, org.bukkit.ChatColor.GREEN);

Hacer que una entidad brille azul por 5 segundos:
GlowAPI.setCustomEntityGlow(entity, org.bukkit.ChatColor.BLUE, 100);

Aplicar glow a un jugador al entrar al servidor por 30 segundos:
@EventHandler
public void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    GlowAPI.setCustomPlayerGlow(player, org.bukkit.ChatColor.GREEN, 600);
}

---

📦 Requisitos

- Spigot / Paper / Purpur 1.21.4+
- No requiere ProtocolLib

---

🧑‍💻 Autor / Créditos

Desarrollado por **NicoHaxz** y **xxditoxx27**  
GitHub: https://github.com/NicoHaxz  
GitHub: https://github.com/xxditoxx27

---

⚠️ Notas

- Esta API es para uso de **programadores amateur**, facilitando la implementación de efectos glow sin preocuparse por teams ni scoreboards visibles.
- Todos los métodos son **estáticos**, lo que la hace muy fácil de usar en cualquier parte de tu plugin.

---

# 🌍 PacketGlowAPI - English Version

![Maven Package](https://img.shields.io/badge/Maven-GitHub%20Packages-blue?logo=apache-maven)
![Version](https://img.shields.io/badge/version-1.0.0-green)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.4+-yellow?logo=minecraft)

PacketGlowAPI is a lightweight API for **Spigot/Paper 1.21.4** that allows applying **Glow effects** in custom colors **without ProtocolLib or visible teams**.  
It works directly using Minecraft’s internal scoreboard/team system to show glow to everyone or specific players.

---

✨ Features

- Supports all 16 vanilla Minecraft glow colors.
- Apply glow to players and entities.
- Show glow for a set amount of time.
- No ProtocolLib needed, no visible teams.
- Easy-to-use static API.
- Compatible with **Spigot**, **Paper**, **Purpur**, and modern forks.
- Easy integration with Gradle and Maven.

---

🚀 Initialization

In your main plugin class:

@Override
public void onEnable() {
    GlowAPI.initialize();
}

@Override
public void onDisable() {
    GlowAPI.shutdown();
}

---

💡 Usage Examples

Make a player glow red for 10 seconds:
GlowAPI.setCustomPlayerGlow(player, org.bukkit.ChatColor.RED, 200);

Make an entity glow green for all players:
GlowAPI.setCustomEntityGlow(entity, org.bukkit.ChatColor.GREEN);

Make an entity glow blue for 5 seconds:
GlowAPI.setCustomEntityGlow(entity, org.bukkit.ChatColor.BLUE, 100);

Give players green glow for 30 seconds on join:
@EventHandler
public void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    GlowAPI.setCustomPlayerGlow(player, org.bukkit.ChatColor.GREEN, 600);
}

---

📦 Requirements

- Spigot / Paper / Purpur 1.21.4+
- No ProtocolLib required

---

🧑‍💻 Author / Credits

Developed by **NicoHaxz** and **xxditoxx27**  
GitHub: https://github.com/NicoHaxz  
GitHub: https://github.com/xxditoxx27

---

⚠️ Notes

- This API is intended for **amateur programmers**, simplifying the implementation of glow effects without worrying about teams or visible scoreboards.
- All methods are **static**, making it very easy to use anywhere in your plugin.
