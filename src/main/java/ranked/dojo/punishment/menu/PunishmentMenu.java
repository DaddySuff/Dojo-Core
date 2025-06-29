package ranked.dojo.punishment.menu;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ranked.dojo.api.menu.Button;
import ranked.dojo.api.menu.pagination.PaginatedMenu;
import ranked.dojo.punishment.Punishment;
import ranked.dojo.punishment.PunishmentHandler;
import ranked.dojo.punishment.enums.EnumPunishmentType;
import ranked.dojo.util.CC;
import ranked.dojo.util.ItemBuilder;

import java.text.SimpleDateFormat;
import java.util.*;

public class PunishmentMenu extends PaginatedMenu {
    private final OfflinePlayer target;
    private boolean showActive = true;
    private EnumPunishmentType filterType = null;

    public PunishmentMenu(OfflinePlayer target, boolean showActive) {
        this.target = target;
        this.showActive = showActive;
        this.filterType = null;
    }
    public PunishmentMenu(OfflinePlayer target, boolean showActive, EnumPunishmentType filterType) {
        this.target = target;
        this.showActive = showActive;
        this.filterType = filterType;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        String type = filterType == null ? "All" : filterType.name();
        return "Punishments: " + target.getName() + " (" + type + ") " + (showActive ? "(Active)" : "(Inactive)");
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        List<Punishment> punishments = showActive ?
                PunishmentHandler.getActivePunishments(target.getUniqueId()) :
                PunishmentHandler.getInactivePunishments(target.getUniqueId());
        if (filterType != null) {
            punishments.removeIf(p -> p.getPunishmentType() != filterType);
        }
        punishments.sort(Comparator.comparingLong(Punishment::getAddedAt).reversed());
        Map<Integer, Button> buttons = new HashMap<>();
        int i = 0;
        for (Punishment punishment : punishments) {
            buttons.put(i++, new PunishmentButton(punishment));
        }
        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(4, new ToggleActiveButton());
        this.addGlassHeader(buttons, 15);
        return buttons;
    }

    private class ToggleActiveButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(showActive ? Material.EMERALD : Material.REDSTONE)
                    .name(showActive ? "&aShowing Active" : "&cShowing Inactive")
                    .lore(Collections.singletonList("&7Click to toggle."))
                    .build();
        }
        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;
            new PunishmentMenu(target, !showActive, filterType).openMenu(player);
        }
    }

    @AllArgsConstructor
    private class PunishmentButton extends Button {
        private final Punishment punishment;
        @Override
        public ItemStack getButtonItem(Player player) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            List<String> lore = new ArrayList<>();
            lore.add("&7&m--------------------------------------");
            lore.add("&6Type: &e" + punishment.getPunishmentType());
            lore.add("&6Reason: &f" + punishment.getPunishReason());
            lore.add("&6By: &f" + punishment.getPunisher());
            lore.add("&6At: &f" + sdf.format(new Date(punishment.getAddedAt())));
            if (punishment.isPermanent()) {
                lore.add("&6Duration: &fPermanent");
            } else {
                lore.add("&6Duration: &f" + formatDurationFancy(punishment.getDuration()));
            }
            if (punishment.isActive()) {
                if (!punishment.isPermanent()) {
                    lore.add("&6Expires: &f" + sdf.format(new Date(punishment.getExpiration())));
                }
            }
            if (!punishment.isActive()) {
                lore.add("&cRemoved at: &f" + (punishment.getRemovedAt() > 0 ? sdf.format(new Date(punishment.getRemovedAt())) : "N/A"));
                lore.add("&cPardoned by: &f" + (punishment.getPardoner() != null ? punishment.getPardoner() : "N/A"));
                lore.add("&cPardon reason: &f" + (punishment.getPardonReason() != null ? punishment.getPardonReason() : "N/A"));
                lore.add("&cThis punishment is inactive.");
            } else {
                lore.add("");
                lore.add("&aClick to remove this punishment.");
            }
            lore.add("&7&m--------------------------------------");
            return new ItemBuilder(Material.PAPER)
                    .name(punishment.isActive() ? "&6&lActive Punishment" : "&c&lInactive Punishment")
                    .lore(lore)
                    .build();
        }
        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;
            if (!punishment.isActive()) {
                player.sendMessage(CC.translate("&cThis punishment is already inactive."));
                return;
            }
            punishment.setActive(false);
            punishment.setRemovedAt(System.currentTimeMillis());
            punishment.setPardoner(player.getName());
            punishment.setPardonReason("Removed by " + player.getName());
            // Actually unban/unmute etc if needed
            if (punishment.getPunishmentType() == EnumPunishmentType.BAN) {
                PunishmentHandler.unbanPlayer(target.getUniqueId());
            }
            // TODO: Add similar logic for other types if needed (e.g., unmute)
            player.sendMessage(CC.translate("&aYou have successfully removed the punishment for " + target.getName() + "."));
            new PunishmentMenu(target, showActive, filterType).openMenu(player);
        }
    }

    private String formatDurationFancy(long millis) {
        if (millis == -1L) return "Permanent";
        long seconds = millis / 1000;
        long days = seconds / 86400;
        seconds %= 86400;
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        seconds %= 60;
        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append(" Day").append(days > 1 ? "s " : " ");
        if (hours > 0) sb.append(hours).append(" Hour").append(hours > 1 ? "s " : " ");
        if (minutes > 0) sb.append(minutes).append(" Minute").append(minutes > 1 ? "s " : " ");
        if (seconds > 0 && days == 0 && hours == 0) sb.append(seconds).append(" Second").append(seconds > 1 ? "s" : "");
        return sb.toString().trim();
    }
}
