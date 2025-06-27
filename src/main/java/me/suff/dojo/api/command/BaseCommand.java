package me.suff.dojo.api.command;

import me.suff.dojo.Dojo;

/**
 * @author minnymin3
 */
public abstract class BaseCommand {

    public Dojo main = Dojo.getInstance();

    public BaseCommand() {
        main.getCommandFramework().registerCommands(this);
    }

    public abstract void onCommand(CommandArgs command);

}
