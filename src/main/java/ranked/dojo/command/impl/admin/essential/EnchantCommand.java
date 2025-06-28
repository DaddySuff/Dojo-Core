package ranked.dojo.command.impl.admin.essential;

import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ranked.dojo.util.CC;
import java.util.HashMap;
import java.util.Map;

public class EnchantCommand extends BaseCommand {

    private static final Map<String, Enchantment> ENCHANTMENT_MAP = new HashMap<>();

    static {
        ENCHANTMENT_MAP.put("protection", Enchantment.PROTECTION_ENVIRONMENTAL);
        ENCHANTMENT_MAP.put("fire_protection", Enchantment.PROTECTION_FIRE);
        ENCHANTMENT_MAP.put("feather_falling", Enchantment.PROTECTION_FALL);
        ENCHANTMENT_MAP.put("blast_protection", Enchantment.PROTECTION_EXPLOSIONS);
        ENCHANTMENT_MAP.put("projectile_protection", Enchantment.PROTECTION_PROJECTILE);
        ENCHANTMENT_MAP.put("respiration", Enchantment.OXYGEN);
        ENCHANTMENT_MAP.put("aqua_affinity", Enchantment.WATER_WORKER);
        ENCHANTMENT_MAP.put("thorns", Enchantment.THORNS);
        ENCHANTMENT_MAP.put("depth_strider", Enchantment.DEPTH_STRIDER);
        ENCHANTMENT_MAP.put("sharpness", Enchantment.DAMAGE_ALL);
        ENCHANTMENT_MAP.put("smite", Enchantment.DAMAGE_UNDEAD);
        ENCHANTMENT_MAP.put("bane_of_arthropods", Enchantment.DAMAGE_ARTHROPODS);
        ENCHANTMENT_MAP.put("knockback", Enchantment.KNOCKBACK);
        ENCHANTMENT_MAP.put("fire_aspect", Enchantment.FIRE_ASPECT);
        ENCHANTMENT_MAP.put("looting", Enchantment.LOOT_BONUS_MOBS);
        ENCHANTMENT_MAP.put("efficiency", Enchantment.DIG_SPEED);
        ENCHANTMENT_MAP.put("silk_touch", Enchantment.SILK_TOUCH);
        ENCHANTMENT_MAP.put("unbreaking", Enchantment.DURABILITY);
        ENCHANTMENT_MAP.put("fortune", Enchantment.LOOT_BONUS_BLOCKS);
        ENCHANTMENT_MAP.put("power", Enchantment.ARROW_DAMAGE);
        ENCHANTMENT_MAP.put("punch", Enchantment.ARROW_KNOCKBACK);
        ENCHANTMENT_MAP.put("flame", Enchantment.ARROW_FIRE);
        ENCHANTMENT_MAP.put("infinity", Enchantment.ARROW_INFINITE);
        ENCHANTMENT_MAP.put("luck", Enchantment.LUCK);
        ENCHANTMENT_MAP.put("lure", Enchantment.LURE);
    }

    @Command(
            name = "enchant",
            permission = "dojo.command.enchant",
            description = "Enchant the item in your hand.",
            usage = "/enchant <enchantment> [level]"
    )
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /enchant <enchantment> [level]"));
            return;
        }

        ItemStack itemInHand = player.getInventory().getItemInHand();
        if (itemInHand == null || itemInHand.getType() == Material.AIR) {
            player.sendMessage(CC.translate("&cYou are not holding any item."));
            return;
        }

        String enchantmentName = args[0].toLowerCase();
        Enchantment enchantment = ENCHANTMENT_MAP.get(enchantmentName);

        if (enchantment == null) {
            player.sendMessage(CC.translate("&cInvalid enchantment: " + args[0]));
            return;
        }

        int level = 1;
        if (args.length >= 2) {
            try {
                level = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(CC.translate("&cInvalid level: " + args[1]));
                return;
            }
        }

        itemInHand.addUnsafeEnchantment(enchantment, level);
        player.sendMessage(CC.translate("&aYou have enchanted the item with " + enchantmentName + " (Level " + level + ")."));
        int maxLevel = enchantment.getMaxLevel();
        if (level > maxLevel) {
            player.sendMessage(CC.translate("&eWARNING: This level exceeds the maximum vanilla cap."));
        }
    }
}