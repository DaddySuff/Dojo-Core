package me.suff.dojo.tag.utility;

import lombok.experimental.UtilityClass;
import me.suff.dojo.Dojo;
import me.suff.dojo.tag.Tag;
import me.suff.dojo.tag.TagService;
import me.suff.dojo.util.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Material;

/**
 * @author Emmy
 * @project Artex
 * @date 23/10/2024 - 12:11
 */
@UtilityClass
public class TagUtility {
    /**
     * Create the default tag
     */
    public void createDefaultTags() {
        TagService tagService = Dojo.getInstance().getTagService();

        if (tagService == null) {
            Logger.logError("Tag service is null.");
        }
        
        Tag Heart = new Tag("Heart", "❤", Material.NAME_TAG, ChatColor.RED, 0, false, false);
        Tag BlackHeart = new Tag("BlackHeart", "❤", Material.NAME_TAG, ChatColor.BLACK, 0, true, false);
        Tag Diamond = new Tag("Diamond", "&7[&b&l♦&7]", Material.NAME_TAG, ChatColor.AQUA, 0, false, false);
        Tag Star = new Tag("Star", "★", Material.NAME_TAG, ChatColor.YELLOW, 0, true, false);
        Tag BestWW = new Tag("BestWW", "BestWW", Material.NAME_TAG, ChatColor.DARK_RED, 0, true, false);
        Tag Crown = new Tag("Crown", "&7[&6&l♛&7]", Material.NAME_TAG, ChatColor.GOLD, 0, false, false);
        Tag King = new Tag("King", "King ♚", Material.NAME_TAG, ChatColor.RED, 0, true, false);
        Tag Queen = new Tag("Queen", "Queen ♛", Material.NAME_TAG, ChatColor.LIGHT_PURPLE, 0, true, false);
        Tag Tick = new Tag("Tick", "✔", Material.NAME_TAG, ChatColor.GREEN, 0, false, false);
        Tag Flower = new Tag("Flower", "&7[&d&l❖&7]", Material.NAME_TAG, ChatColor.LIGHT_PURPLE, 0, false, false);
        Tag Cross = new Tag("Cross", "✖", Material.NAME_TAG, ChatColor.RED, 0, true, false);
        Tag Blood = new Tag("Blood", "BLOOD", Material.NAME_TAG, ChatColor.RED, 0, true, false);
        Tag Goat = new Tag("Goat", "GOAT", Material.NAME_TAG, ChatColor.AQUA, 0, true, false);
        Tag Banana = new Tag("Banana", "Banana", Material.NAME_TAG, ChatColor.YELLOW, 0, true, true);
        Tag Love = new Tag("Love", "Love", Material.NAME_TAG, ChatColor.RED, 0, true, false);
        Tag Yurrrrrrr = new Tag("Yurrrrrrr", "yurrrrrrr", Material.NAME_TAG, ChatColor.GREEN, 0, false, false);
        Tag Legend = new Tag("Legend", "&r&k|&r&9Legend&r&k|&r", Material.NAME_TAG, ChatColor.GOLD, 0, false, false);
        Tag First = new Tag("#1", "#1", Material.NAME_TAG, ChatColor.RED, 0, true, false);
        Tag Godly = new Tag("Godly", "Godly", Material.NAME_TAG, ChatColor.DARK_RED, 0, true, false);

        tagService.getTags().add(Heart);
        tagService.getTags().add(BlackHeart);
        tagService.getTags().add(Diamond);
        tagService.getTags().add(Star);
        tagService.getTags().add(BestWW);
        tagService.getTags().add(Crown);
        tagService.getTags().add(King);
        tagService.getTags().add(Queen);
        tagService.getTags().add(Tick);
        tagService.getTags().add(Flower);
        tagService.getTags().add(Cross);
        tagService.getTags().add(Blood);
        tagService.getTags().add(Goat);
        tagService.getTags().add(Banana);
        tagService.getTags().add(Love);
        tagService.getTags().add(Yurrrrrrr);
        tagService.getTags().add(Legend);
        tagService.getTags().add(First);
        tagService.getTags().add(Godly);

        tagService.saveTags();
    }
}