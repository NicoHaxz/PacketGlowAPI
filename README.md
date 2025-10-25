# 🌈 PacketGlowAPI

![Maven Package](https://img.shields.io/badge/Maven-GitHub%20Packages-blue?logo=apache-maven)
![Version](https://img.shields.io/badge/version-1.0.0-green)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.4+-yellow?logo=minecraft)

**PacketGlowAPI** es una API ligera para **Spigot/Paper 1.21.4** que permite aplicar efectos de **Glow (brillo)** en colores personalizados **sin usar equipos visibles**.  
Funciona mediante **ProtocolLib**, enviando paquetes directamente al cliente para mostrar brillos globales o individuales por jugador.

---

## ✨ Características

- 🎨 Soporte para los **16 colores vanilla** de Minecraft.  
- 👁️ Mostrar el brillo solo a jugadores específicos o a todos.  
- 🧠 Sin uso de teams ni scoreboard visibles (todo es *client-side*).  
- ⚡ API simple y estática, fácil de integrar.  
- 🧩 Compatible con **Spigot**, **Paper**, **Purpur** y forks que soporten **ProtocolLib**.  

---

## 🛠️ Instalación / Dependencia

Agrega esto en tu `pom.xml`:

```xml
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
```
# 📦 Agregar PacketGlowAPI con Gradle

Puedes usar **PacketGlowAPI** tanto con **Maven** como con **Gradle**.  
Aquí te explico cómo hacerlo para Gradle, usando los dos formatos más comunes: **Groovy DSL** (`build.gradle`) y **Kotlin DSL** (`build.gradle.kts`).

---

## 🧩 Usando Gradle (Groovy DSL)

```gradle
repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/NicoHaxz/PacketGlowAPI")
    }
}

dependencies {
    implementation("nico.dev:PacketGlowAPI:1.0.5")
}
```

---

## 🧠 Usando Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/NicoHaxz/PacketGlowAPI")
    }
}

dependencies {
    implementation("nico.dev:PacketGlowAPI:1.0.5")
}
```

---

## 🚀 Inicialización

En tu plugin principal:

```java
@Override
public void onEnable() {
    GlowAPI.initialize(); // Inicializar la API
}

@Override
public void onDisable() {
    GlowAPI.shutdown(); // Apagar la API
}
```

---

## 💡 Ejemplos de uso

### 1️⃣ Hacer que una entidad brille en color verde para todos
```java
GlowAPI.setGlowingForAll(entity, GlowColor.GREEN, true);
```

### 2️⃣ Hacer que solo un jugador vea a otro con brillo rojo
```java
GlowAPI.setGlowing(targetEntity, viewerPlayer, GlowColor.RED, true);
```

### 3️⃣ Mostrar brillo azul por 5 segundos
```java
GlowAPI.setGlowingForAll(entity, GlowColor.BLUE, true);

Bukkit.getScheduler().runTaskLater(plugin, () -> {
    GlowAPI.setGlowingForAll(entity, GlowColor.BLUE, false);
}, 20L * 5); // 5 segundos
```

### 4️⃣ Al entrar al servidor, aplicar glow verde por 30 segundos
```java
@EventHandler
public void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    GlowAPI.setGlowingForAll(player, GlowColor.GREEN, true);

    Bukkit.getScheduler().runTaskLater(plugin, () -> {
        GlowAPI.setGlowingForAll(player, GlowColor.GREEN, false);
    }, 20L * 30);
}
```

---

## 📦 Requisitos

- **Spigot / Paper / Purpur 1.21.4+**  
- **ProtocolLib 5.4.0+**

---

## 🧑‍💻 Autor

Desarrollado por **NicoHaxz**  
🌐 [github.com/NicoHaxz](https://github.com/NicoHaxz)

---

# 🌍 English Version

![Maven Package](https://img.shields.io/badge/Maven-GitHub%20Packages-blue?logo=apache-maven)
![Version](https://img.shields.io/badge/version-1.0.0-green)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.4+-yellow?logo=minecraft)

**PacketGlowAPI** is a lightweight API for **Spigot/Paper 1.21.4** that lets you apply **colored glow effects** to entities **without using visible teams**.  
It works through **ProtocolLib**, sending packets directly to the client to show glowing effects per-player or globally.

---

## ✨ Features

- 🎨 Supports all **16 vanilla glow colors**.  
- 👁️ Show glow to specific players or globally.  
- 🧠 No visible teams or scoreboard modifications (fully client-side).  
- ⚡ Easy to use with static calls.  
- 🧩 Compatible with **Spigot**, **Paper**, **Purpur**, and ProtocolLib-supported forks.

---

## 🛠️ Installation / Dependency

Add this to your `pom.xml`:

```xml
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
```
## 🧩 Gradle Dependency

If you use **Gradle** instead of Maven, you can also import **PacketGlowAPI** directly from **GitHub Packages**, since it’s a Maven-compatible repository.

Add this to your `build.gradle`:

```groovy
repositories {
    maven { url "https://maven.pkg.github.com/NicoHaxz/PacketGlowAPI" }
}

dependencies {
    implementation "nico.dev:PacketGlowAPI:1.0.5"
}
```

Or if you’re using the **Kotlin DSL (`build.gradle.kts`)**:

```kotlin
repositories {
    maven("https://maven.pkg.github.com/NicoHaxz/PacketGlowAPI")
}

dependencies {
    implementation("nico.dev:PacketGlowAPI:1.0.5")
}
```

---

## 🚀 Initialization

In your main plugin class:

```java
@Override
public void onEnable() {
    GlowAPI.initialize();
}

@Override
public void onDisable() {
    GlowAPI.shutdown();
}
```

---

## 💡 Usage Examples

### 1️⃣ Make an entity glow green for everyone
```java
GlowAPI.setGlowingForAll(entity, GlowColor.GREEN, true);
```

### 2️⃣ Make only one player see another with red glow
```java
GlowAPI.setGlowing(targetEntity, viewerPlayer, GlowColor.RED, true);
```

### 3️⃣ Make an entity glow blue for 5 seconds
```java
GlowAPI.setGlowingForAll(entity, GlowColor.BLUE, true);

Bukkit.getScheduler().runTaskLater(plugin, () -> {
    GlowAPI.setGlowingForAll(entity, GlowColor.BLUE, false);
}, 20L * 5);
```

### 4️⃣ Give players green glow for 30 seconds on join
```java
@EventHandler
public void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    GlowAPI.setGlowingForAll(player, GlowColor.GREEN, true);

    Bukkit.getScheduler().runTaskLater(plugin, () -> {
        GlowAPI.setGlowingForAll(player, GlowColor.GREEN, false);
    }, 20L * 30);
}
```

---

## 📦 Requirements

- **Spigot / Paper / Purpur 1.21.4+**  
- **ProtocolLib 5.4.0+**

---

## 🧑‍💻 Author

Developed by **NicoHaxz**  
🌐 [github.com/NicoHaxz](https://github.com/NicoHaxz)
