package ranked.dojo.locale;

import lombok.Getter;
import ranked.dojo.Dojo;


@Getter
public enum Locale {
    SERVER_NAME("server.name"),
    SERVER_REGION("server.region"),

    DISCORD("socials.discord"),
    YOUTUBE("socials.youtube"),
    TWITTER("socials.twitter"),
    WEBSITE("socials.website"),
    TIKTOK("socials.tiktok"),
    STORE("socials.store"),
    GITHUB("socials.github"),

    TEAMSPEAK("socials.teamspeak"),
    FACEBOOK("socials.facebook"),
    INSTAGRAM("socials.instagram"),

    ;

    private final String string;

    Locale(String string) {
        this.string = Dojo.getInstance().getConfig().getString(string);
    }
}