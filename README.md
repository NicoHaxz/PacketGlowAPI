# 🌈 PacketGlowAPI

**PacketGlowAPI** es una API ligera para Spigot/Paper 1.21.4 que permite aplicar efectos de **Glow (brillo de entidad)** en colores personalizados **sin usar Teams**.  
Está basada en **ProtocolLib**, lo que permite enviar paquetes directamente al cliente y mostrar brillos individuales o globales.

### ✨ Características
- Soporta **todos los colores vanilla** (16 disponibles).
- Permite mostrar el glow **solo a jugadores específicos** o a **todos**.
- No usa equipos visibles, todo es client-side (solo el jugador ve el efecto).
- API completamente estática, fácil de integrar con una sola línea.
- Compatible con **Spigot**, **Paper**, **Purpur**, etc.

### 🧱 Ejemplo de uso
```java
import com.nico.glowapi.GlowAPI;
import com.nico.glowapi.GlowColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;

Entity entity = player.getTargetEntity(10);
GlowAPI.setGlowing(entity, player, GlowColor.RED, true); // Solo ese jugador lo ve en rojo
GlowAPI.setGlowing(entity, null, GlowColor.AQUA, true);  // Todos lo ven en aqua
GlowAPI.setGlowing(entity, player, null, false);         // Quitar glow para ese jugador
