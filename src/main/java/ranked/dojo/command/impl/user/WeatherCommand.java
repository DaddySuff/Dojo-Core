package ranked.dojo.command.impl.user;

import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.WeatherType;
import ranked.dojo.util.CC;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WeatherCommand extends BaseCommand {
    private static final Map<UUID, WeatherType> playerWeather = new HashMap<>();

    @Override
    @Command(name = "weather")
    public void onCommand(CommandArgs commandArgs) {
        if (!(commandArgs.getSender() instanceof Player)) {
            commandArgs.getSender().sendMessage(CC.translate(CC.PREFIX + "&cThis command can only be used by players."));
            return;
        }
        Player player = (Player) commandArgs.getSender();
        UUID uuid = player.getUniqueId();

        if (commandArgs.getArgs().length == 0) {
            player.sendMessage(CC.translate(CC.PREFIX + "&cUsage: /weather <clear|rain>"));
            return;
        }

        String type = commandArgs.getArgs()[0].toLowerCase();
        switch (type) {
            case "clear":
                player.setPlayerWeather(WeatherType.CLEAR);
                playerWeather.put(uuid, WeatherType.CLEAR);
                player.sendMessage(CC.translate(CC.PREFIX + "&aWeather set to clear."));
                break;
            case "rain":
                player.setPlayerWeather(WeatherType.DOWNFALL);
                playerWeather.put(uuid, WeatherType.DOWNFALL);
                player.sendMessage(CC.translate(CC.PREFIX + "&bWeather set to rain."));
                break;
            default:
                player.sendMessage(CC.translate(CC.PREFIX + "&cInvalid weather type. Use: clear or rain."));
                break;
        }
    }
}
