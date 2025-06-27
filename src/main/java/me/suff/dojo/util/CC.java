package me.suff.dojo.util;

import lombok.experimental.UtilityClass;
import me.suff.dojo.Dojo;
import me.suff.dojo.locale.Locale;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project Artex
 * @date 15/08/2024 - 21:25
 */
@UtilityClass
public class CC {
    public final String PREFIX = "§8[§6Dojo§8] §7";

    /**
     * Translate a string with color codes
     *
     * @param string the string to translate
     * @return the translated string
     */
    public String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Send an enable message
     */
    public void sendEnableMessage() {
        if (Dojo.getInstance().getDatabaseService().isMongo()) {
            List<String> message = Arrays.asList(
                    "",
                    " &e<---------- &6&l" + ProjectInfo.NAME + " Core &e---------->",
                    "",
                    "   &ePlugin Info:",
                    "   &7| &e&lAuthor: &e" + ProjectInfo.AUTHORS,
                    "   &7| &e&lVersion: &e" + ProjectInfo.VERSION,
                    "   &7| &e&lDescription: &e" + ProjectInfo.DESCRIPTION,
                    "",
                    "   &eInstance:",
                    "   &7| &fServer Name: &e" + Locale.SERVER_NAME.getString(),
                    "   &7| &fServer Region: &e" + Locale.SERVER_REGION.getString(),
                    "   &7| &fServer Version: &e" + ProjectInfo.BUKKIT_VERSION_EXACT,
                    "   &7| &fSpigot: &e" + ProjectInfo.SERVER_TYPE,
                    "",
                    "   &eStorage:",
                    "   &7| &fRanks: &e" + Dojo.getInstance().getRankService().getRanks().size(),
                    "   &7| &fTags: &e" + Dojo.getInstance().getTagService().getTags().size(),
                    "",
                    " &e<-------------------------------->",
                    ""
            );

            for (String line : message) {
                Bukkit.getConsoleSender().sendMessage(translate(line));
            }
        } else if (Dojo.getInstance().getDatabaseService().isFlatFile()) {
            List<String> message = Arrays.asList(
                    "",
                    " &e<---------- &6&l" + ProjectInfo.NAME + " Core &e---------->",
                    "",
                    "   &ePlugin Info:",
                    "   &7| &eAuthor: &f" + ProjectInfo.AUTHORS,
                    "   &7| &eVersion: &f" + ProjectInfo.VERSION,
                    "   &7| &e&lDescription: &f" + ProjectInfo.DESCRIPTION,
                    "",
                    "   &aDatabase Info: FlatFile",
                    "   &7| &7&oProfiles are stored in 'profiles.yml'.",
                    "",
                    "   &eInstance:",
                    "   &7| &eServer Name: &f" + Locale.SERVER_NAME.getString(),
                    "   &7| &eServer Region: &f" + Locale.SERVER_REGION.getString(),
                    "   &7| &eServer Version: &f" + ProjectInfo.BUKKIT_VERSION_EXACT,
                    "   &7| &eSpigot: &f" + ProjectInfo.SERVER_TYPE,
                    "",
                    "   &eStorage:",
                    "   &7| &eRanks: &f" + Dojo.getInstance().getRankService().getRanks().size(),
                    "   &7| &eTags: &f" + Dojo.getInstance().getTagService().getTags().size(),
                    "",
                    " &e<-------------------------------->",
                    ""
            );

            for (String line : message) {
                Bukkit.getConsoleSender().sendMessage(translate(line));
            }
        }
    }

    /**
     * Send a disable message
     */
    public void sendDisableMessage() {
        Bukkit.getConsoleSender().sendMessage(translate(PREFIX + "&cPlugin has been disabled!"));
    }
}