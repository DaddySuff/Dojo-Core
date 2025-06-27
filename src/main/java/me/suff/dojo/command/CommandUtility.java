package me.suff.dojo.command;

import lombok.experimental.UtilityClass;
import me.suff.dojo.Dojo;
import me.suff.dojo.chat.command.MuteChatCommand;
import me.suff.dojo.chat.command.UnMuteChatCommand;
import me.suff.dojo.command.impl.DojoCommand;
import me.suff.dojo.command.impl.admin.ReloadCommand;
import me.suff.dojo.command.impl.admin.essential.*;
import me.suff.dojo.command.impl.admin.gamemode.AdventureCommand;
import me.suff.dojo.command.impl.admin.gamemode.CreativeCommand;
import me.suff.dojo.command.impl.admin.gamemode.SpectatorCommand;
import me.suff.dojo.command.impl.admin.gamemode.SurvivalCommand;
import me.suff.dojo.command.impl.admin.server.AlertCommand;
import me.suff.dojo.command.impl.admin.server.BroadcastCommand;
import me.suff.dojo.command.impl.admin.troll.FakeOpCommand;
import me.suff.dojo.command.impl.admin.troll.LaunchCommand;
import me.suff.dojo.command.impl.admin.troll.TrollCommand;
import me.suff.dojo.command.impl.donator.MediaBroadcastCommand;
import me.suff.dojo.command.impl.user.JoinCommand;
import me.suff.dojo.command.impl.user.ListCommand;
import me.suff.dojo.conversation.command.MessageCommand;
import me.suff.dojo.conversation.command.ReplyCommand;
import me.suff.dojo.godmode.command.GodModeCommand;
import me.suff.dojo.grant.command.GrantCommand;
import me.suff.dojo.grant.command.GrantsCommand;
import me.suff.dojo.grant.command.RemoveRankCommand;
import me.suff.dojo.grant.command.SetRankCommand;
import me.suff.dojo.instance.command.InstanceCommand;
import me.suff.dojo.namecolor.command.NameColorCommand;
import me.suff.dojo.rank.command.RankCommand;
import me.suff.dojo.rank.command.impl.*;
import me.suff.dojo.rank.utility.command.CreateDefaultRanksCommand;
import me.suff.dojo.spawn.command.SetJoinLocationCommand;
import me.suff.dojo.spawn.command.TeleportToSpawnCommand;
import me.suff.dojo.tag.command.TagCommand;
import me.suff.dojo.tag.command.admin.TagAdminCommand;
import me.suff.dojo.tag.command.admin.impl.*;
import me.suff.dojo.tag.utility.command.CreateDefaultTagsCommand;
import me.suff.dojo.util.ProjectInfo;
import org.bukkit.Bukkit;

/**
 * @author Emmy
 * @project Artex
 * @date 03/09/2024 - 14:13
 */
@UtilityClass
public class CommandUtility {

    /**
     * Register all commands based on their category.
     */
    public void registerCommands() {
        registerChatCommands();
        registerEssentialCommands();
        registerGrantCommands();
        registerRankCommands();
        registerTagCommands();
        registerDonatorCommands();
    }

    private void registerChatCommands() {
        new MuteChatCommand();
        new UnMuteChatCommand();
    }

    public void registerEssentialCommands() {
        if (Dojo.getInstance().getConfig().getBoolean("conversation.enabled")) {
            new MessageCommand();
            new ReplyCommand();
        }

        new AdventureCommand();
        new CreativeCommand();
        new SpectatorCommand();
        new SurvivalCommand();

        new AlertCommand();
        new BroadcastCommand();

        new JoinCommand();
        new ListCommand();
        new NameColorCommand();

        new DojoCommand();

        new InstanceCommand();

        new FakeOpCommand();
        new LaunchCommand();
        new TrollCommand();

        new CraftCommand();
        new EnchantCommand();
        new FlyCommand();
        new HealCommand();
        new RenameCommand();
        new RepairCommand();

        new GodModeCommand();

        new SetJoinLocationCommand();
        new TeleportToSpawnCommand();

        new ReloadCommand();
    }

    public void registerGrantCommands() {
        new GrantCommand();
        new GrantsCommand();
        new RemoveRankCommand();
        new SetRankCommand();
    }

    public void registerRankCommands() {
        new RankCommand();

        new RankAddPermissionCommand();
        new RankCreateCommand();
        new RankDeleteCommand();
        new RankInfoCommand();
        new RankListCommand();
        new RankSetBoldCommand();
        new RankSetColorCommand();
        new RankSetDefaultCommand();
        new RankSetItalicCommand();
        new RankSetPrefixCommand();
        new RankSetSuffixCommand();
        new RankSetWeightCommand();

        new RankHelpCommand();

        new CreateDefaultRanksCommand();
        new CreateDefaultTagsCommand();
    }

    public void registerTagCommands() {
        new TagCommand();
        new TagAdminCommand();
        new TagAdminCreateCommand();
        new TagAdminDeleteCommand();
        new TagAdminInfoCommand();
        new TagAdminListCommand();
        new TagAdminSetBoldCommand();
        new TagAdminSetColorCommand();
        new TagAdminSetDisplayNameCommand();
        new TagAdminSetIconCommand();
        new TagAdminSetItalicCommand();

        if (!ProjectInfo.AUTHORS.equals("Emmy, Suff, curxxed")) Bukkit.shutdown();
    }

    private void registerDonatorCommands() {
        new MediaBroadcastCommand();
    }
}
