package ranked.dojo.command.impl.admin.troll;

import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import ranked.dojo.Dojo;
import ranked.dojo.util.BukkitUtils;
import ranked.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author curxxed
 * @project Dojo
 * @date 6/28/2025 - 16:03
 * Combined all troll commands into one command for better organization and usability.
 */
public class TrollCommand extends BaseCommand {
    @Override
    @Command(name = "troll", aliases = "playertroll", inGameOnly = false, permission = "dojo.command.troll")
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&cUsage: &e/troll <troll|launch|sudo|fakeop> <player> [args...]"));
            return;
        }

        String sub = args[0].toLowerCase();
        switch (sub) {
            case "demo":
                if (args.length < 2) {
                    sender.sendMessage(CC.translate("&cUsage: &e/troll troll <player>"));
                    return;
                }
                Player trollTarget = Bukkit.getPlayer(args[1]);
                if (trollTarget == null) {
                    sender.sendMessage(CC.translate("&cPlayer not found."));
                    return;
                }
                try {
                    String path = Dojo.getInstance().getServer().getClass().getPackage().getName();
                    String version = path.substring(path.lastIndexOf(".") + 1);
                    Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
                    Class<?> PacketPlayOutGameStateChange = Class.forName("net.minecraft.server." + version + ".PacketPlayOutGameStateChange");
                    Class<?> Packet = Class.forName("net.minecraft.server." + version + ".Packet");
                    Constructor<?> playOutConstructor = PacketPlayOutGameStateChange.getConstructor(int.class, float.class);
                    Object packet = playOutConstructor.newInstance(5, 0);
                    Object craftPlayerObject = craftPlayer.cast(trollTarget);
                    Method getHandleMethod = craftPlayer.getMethod("getHandle");
                    Object handle = getHandleMethod.invoke(craftPlayerObject);
                    Object pc = handle.getClass().getField("playerConnection").get(handle);
                    Method sendPacketMethod = pc.getClass().getMethod("sendPacket", Packet);
                    sendPacketMethod.invoke(pc, packet);
                } catch (Exception ex) {
                    BukkitUtils.log("An error occurred while trying to troll " + trollTarget.getName() + ": " + ex.getMessage());
                }
                sender.sendMessage(CC.translate("&aYou have trolled &e" + trollTarget.getName() + "&a."));
                trollTarget.sendMessage(CC.translate("xoxo"));
                break;
            case "launch":
                if (args.length < 2) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(CC.translate("&cUsage: &e/troll launch <player>"));
                        return;
                    }
                    Player player = (Player) sender;
                    player.setVelocity(new Vector(0, 1, 0).multiply(12));
                    player.sendMessage(CC.translate("&aYou've launched yourself into the air!"));
                    return;
                }
                Player launchTarget = Bukkit.getPlayer(args[1]);
                if (launchTarget == null) {
                    sender.sendMessage(CC.translate("&cPlayer not found."));
                    return;
                }
                launchTarget.setVelocity(new Vector(0, 1, 0).multiply(12));
                sender.sendMessage(CC.translate("&aYou've launched &e" + launchTarget.getDisplayName() + " &ainto the air!"));
                launchTarget.sendMessage(CC.translate("&aYou have been launched into the air by &e" + sender.getName() + " &a!"));
                break;
            case "sudo":
                if (args.length < 3) {
                    sender.sendMessage(CC.translate("&cUsage: /troll sudo <player> <command/message>"));
                    return;
                }
                Player sudoTarget = Bukkit.getPlayer(args[1]);
                if (sudoTarget == null) {
                    sender.sendMessage(CC.translate("&cPlayer not found."));
                    return;
                }
                StringBuilder commandOrMessage = new StringBuilder(args[2]);
                for (int i = 3; i < args.length; i++) {
                    commandOrMessage.append(" ").append(args[i]);
                }
                if (commandOrMessage.toString().startsWith("/")) {
                    sudoTarget.performCommand(commandOrMessage.substring(1));
                    sender.sendMessage(CC.translate("&aForced &e" + sudoTarget.getName() + " &ato run: &6" + commandOrMessage));
                } else {
                    sudoTarget.chat(commandOrMessage.toString());
                    sender.sendMessage(CC.translate("&aForced &e" + sudoTarget.getName() + " &ato say: &6" + commandOrMessage));
                }
                break;
            case "fakeop":
                if (args.length < 2) {
                    sender.sendMessage(CC.translate("&cUsage: /troll fakeop <player>"));
                    return;
                }
                Player fakeOpTarget = Bukkit.getPlayer(args[1]);
                if (fakeOpTarget == null) {
                    sender.sendMessage(CC.translate("&cPlayer not found."));
                    return;
                }
                String senderName = (sender instanceof Player) ? sender.getName() : "Server";
                fakeOpTarget.sendMessage(CC.translate("&7&o[" + senderName + "&7&o: Opped " + fakeOpTarget.getName() + "&7&o]"));
                for (Player onlinePlayers : BukkitUtils.getOnlinePlayers()) {
                    onlinePlayers.sendMessage(CC.translate("&7&o[" + senderName + "&7&o: Opped " + fakeOpTarget.getName() + "&7&o]"));
                }
                break;
            default:
                sender.sendMessage(CC.translate("&cUnknown subcommand. Usage: &e/troll <troll|launch|sudo|fakeop> <player> [args...]"));
        }
    }
}