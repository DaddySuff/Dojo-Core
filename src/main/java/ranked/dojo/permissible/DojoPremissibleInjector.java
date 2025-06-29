package ranked.dojo.permissible;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import ranked.dojo.Dojo;
import ranked.dojo.util.BukkitUtils;

import java.lang.reflect.Field;

public class DojoPremissibleInjector {
    public static final Field HUMAN_ENTITY_PERMISSIBLE_FIELD;
    private static final Field PERMISSIBLE_BASE_ATTACHMENTS_FIELD;

    public static void initPlayer(final Player player) throws IllegalAccessException {
        if (HUMAN_ENTITY_PERMISSIBLE_FIELD == null) {
            throw new IllegalStateException("HUMAN_ENTITY_PERMISSIBLE_FIELD is null. Injection failed.");
        }

        final DojoPermissible permissible = new DojoPermissible(Dojo.getInstance(), player);
        HUMAN_ENTITY_PERMISSIBLE_FIELD.set(player, permissible);
    }


    static {
        Field humanEntityPermissibleField = null;
        Field permissibleBaseAttachmentsField = null;
        try {
            String version = BukkitUtils.getServerVersion();
            String className = "org.bukkit.craftbukkit." + version + ".entity.CraftHumanEntity";
            Class<?> clazz = Class.forName(className);
            humanEntityPermissibleField = clazz.getDeclaredField("perm");
            humanEntityPermissibleField.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            permissibleBaseAttachmentsField = PermissibleBase.class.getDeclaredField("attachments");
            permissibleBaseAttachmentsField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new ExceptionInInitializerError(e);
        }
        HUMAN_ENTITY_PERMISSIBLE_FIELD = humanEntityPermissibleField;
        PERMISSIBLE_BASE_ATTACHMENTS_FIELD = permissibleBaseAttachmentsField;
    }
}
