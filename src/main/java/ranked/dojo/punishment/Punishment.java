package ranked.dojo.punishment;

import lombok.Getter;
import lombok.Setter;
import ranked.dojo.punishment.enums.EnumPunishmentType;


@Getter
@Setter
public class Punishment {
    private EnumPunishmentType punishmentType;

    private String punisher;
    private String punishReason;
    private String pardoner;
    private String pardonReason;

    private long duration;
    private long expiration;
    private long addedAt;
    private long removedAt;

    private boolean active;
    private boolean permanent;
}