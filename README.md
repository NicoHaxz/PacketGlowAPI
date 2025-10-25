# 🌈 PacketGlowAPI

PacketGlowAPI es una API ligera para Spigot/Paper 1.21.4 que permite aplicar efectos de **Glow (brillo de entidad)** en colores personalizados **sin usar teams visibles**.  
Funciona mediante ProtocolLib, enviando paquetes directamente al cliente para mostrar brillos globales o personalizados por jugador.

---

## ✨ Características

- Soporte para los **16 colores vanilla** de Minecraft.  
- Mostrar el glow **solo a jugadores específicos** o a **todos**.  
- No usa equipos visibles ni modifica el servidor: todo es client-side.  
- Fácil de usar con llamadas estáticas.  
- Compatible con Spigot, Paper, Purpur y derivados que soporten ProtocolLib.

---

## 🛠️ Instalación / Uso

### Obtener la dependencia (GitHub Packages)

```xml
<repositories>
  <repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/NicoHaxz/PacketGlowAPI</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>nico.dev</groupId>
    <artifactId>PacketGlowAPI</artifactId>
    <version>1.0.0</version>
  </dependency>
</dependencies>
