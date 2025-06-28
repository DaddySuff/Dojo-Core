package ranked.dojo.godmode.listener;

import ranked.dojo.Dojo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;


public class GodModeListener implements Listener {

    @EventHandler
    private void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (Dojo.getInstance().getGodModeRepository().isGodModeEnabled(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (Dojo.getInstance().getGodModeRepository().isGodModeEnabled(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onEntityDamageByBlock(EntityDamageByBlockEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (Dojo.getInstance().getGodModeRepository().isGodModeEnabled(player)) {
                event.setCancelled(true);
            }
        }
    }
}