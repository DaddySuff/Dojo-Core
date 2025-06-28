package ranked.dojo.command;

import lombok.experimental.UtilityClass;
import net.curxxed.dev.CommandAPI.CommandManager;
import ranked.dojo.Dojo;
import ranked.dojo.chat.command.*;
import ranked.dojo.command.impl.*;
import ranked.dojo.command.impl.admin.*;
import ranked.dojo.command.impl.admin.essential.*;
import ranked.dojo.command.impl.admin.gamemode.*;
import ranked.dojo.command.impl.admin.server.*;
import ranked.dojo.command.impl.admin.troll.*;
import ranked.dojo.command.impl.donator.*;
import ranked.dojo.command.impl.staff.KickCommand;
import ranked.dojo.command.impl.user.*;
import ranked.dojo.conversation.command.*;
import ranked.dojo.godmode.command.*;
import ranked.dojo.grant.command.*;
import ranked.dojo.instance.command.*;
import ranked.dojo.namecolor.command.*;
import ranked.dojo.rank.command.*;
import ranked.dojo.spawn.command.*;
import ranked.dojo.tag.command.*;

@UtilityClass
public class CommandUtility {
    /**
     * Register all commands in one place.
     */
    public void registerCommands() {
        new CommandManager(Dojo.getInstance());
        new MuteChatCommand();
        new UnMuteChatCommand();
        new KickCommand();
        if (Dojo.getInstance().getConfig().getBoolean("conversation.enabled")) {
            new MessageCommand();
            new ReplyCommand();
        }
        new AdventureCommand();
        new CreativeCommand();
        new SpectatorCommand();
        new SurvivalCommand();
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
        new GrantCommand();
        new GrantsCommand();
        new RemoveRankCommand();
        new RankCommand();
        new MediaBroadcastCommand();
        new TagCommand();
    }
}
