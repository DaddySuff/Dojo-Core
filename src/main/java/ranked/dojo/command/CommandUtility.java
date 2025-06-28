package ranked.dojo.command;

import lombok.experimental.UtilityClass;
import net.curxxed.dev.CommandAPI.CommandManager;
import ranked.dojo.Dojo;
import ranked.dojo.chat.command.MuteChatCommand;
import ranked.dojo.chat.command.UnMuteChatCommand;
import ranked.dojo.command.impl.DojoCommand;
import ranked.dojo.command.impl.admin.ReloadCommand;
import ranked.dojo.command.impl.admin.essential.*;
import ranked.dojo.command.impl.admin.gamemode.AdventureCommand;
import ranked.dojo.command.impl.admin.gamemode.CreativeCommand;
import ranked.dojo.command.impl.admin.gamemode.SpectatorCommand;
import ranked.dojo.command.impl.admin.gamemode.SurvivalCommand;
import ranked.dojo.command.impl.admin.server.AlertCommand;
import ranked.dojo.command.impl.admin.server.BroadcastCommand;
import ranked.dojo.command.impl.admin.troll.TrollCommand;
import ranked.dojo.command.impl.donator.MediaBroadcastCommand;
import ranked.dojo.command.impl.user.JoinCommand;
import ranked.dojo.command.impl.user.ListCommand;
import ranked.dojo.conversation.command.MessageCommand;
import ranked.dojo.conversation.command.ReplyCommand;
import ranked.dojo.godmode.command.GodModeCommand;
import ranked.dojo.grant.command.GrantCommand;
import ranked.dojo.grant.command.GrantsCommand;
import ranked.dojo.grant.command.RemoveRankCommand;
import ranked.dojo.instance.command.InstanceCommand;
import ranked.dojo.namecolor.command.NameColorCommand;
import ranked.dojo.rank.command.RankCommand;
import ranked.dojo.spawn.command.SetJoinLocationCommand;
import ranked.dojo.spawn.command.TeleportToSpawnCommand;
import ranked.dojo.tag.command.TagCommand;

@UtilityClass
public class CommandUtility {


    /**
     * Register all commands based on their category.
     */
    public void registerCommands() {
       new CommandManager(Dojo.getInstance());
        registerChatCommands();
        registerEssentialCommands();
        registerGrantCommands();
        registerRankCommands();
        registerDonatorCommands();
        new TagCommand();
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
    }

    public void registerRankCommands() {
        new RankCommand();
    }
    private void registerDonatorCommands() {
        new MediaBroadcastCommand();
    }
}
