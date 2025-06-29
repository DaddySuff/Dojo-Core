package ranked.dojo.permissible;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.*;
import org.bukkit.plugin.Plugin;
import ranked.dojo.Dojo;

import java.lang.reflect.Field;
import java.util.*;

public class DojoPermissible extends PermissibleBase {
    private final Dojo plugin;
    private final Player player;
    private final Map<String, Boolean> rawPermissions = new HashMap<>();
    private ServerOperator opable;

    private static final Field ATTACHMENTS_FIELD;

    static {
        try {
            ATTACHMENTS_FIELD = PermissibleBase.class.getDeclaredField("attachments");
            ATTACHMENTS_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private final Map<String, PermissionAttachmentInfo> permissions = new HashMap<>();
    private final List<PermissionAttachment> attachments = new ArrayList<>();

    public DojoPermissible(Dojo plugin, Player player) {
        super((ServerOperator)player);
        this.plugin = plugin;
        this.player = player;
        this.opable = (ServerOperator)player;
        try {
            ATTACHMENTS_FIELD.set(this, attachments);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to set attachments field", e);
        }
        recalculatePermissions();
    }

    public boolean isOp() {
        return this.opable.isOp();
    }

    public void setOp(boolean value) {
        this.opable.setOp(value);
    }

    public void addRawPermission(String node, boolean value) {
        String lower = node.toLowerCase(Locale.ENGLISH);
        rawPermissions.put(lower, value);
    }

    public void clearRawPermissions() {
        rawPermissions.clear();
    }

    @Override
    public boolean isPermissionSet(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Permission name cannot be null");
        }
        String lower = name.toLowerCase(Locale.ENGLISH);
        if (rawPermissions.containsKey("-" + lower)) return true;
        if (lower.contains(".")) {
            String[] parts = lower.split("\\.");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < parts.length - 1; i++) {
                if (i > 0) sb.append(".");
                sb.append(parts[i]);
                String negWildcard = "-"+sb+".*";
                if (rawPermissions.containsKey(negWildcard)) return true;
            }
        }

        if (rawPermissions.containsKey("*")) return true;

        if (lower.contains(".")) {
            String[] parts = lower.split("\\.");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < parts.length - 1; i++) {
                if (i > 0) sb.append(".");
                sb.append(parts[i]);
                String wildcard = sb + ".*";
                if (rawPermissions.containsKey(wildcard)) return true;
            }
        }

        if (rawPermissions.containsKey(lower)) return true;

        Permission perm = Bukkit.getServer().getPluginManager().getPermission(name);
        return perm != null;
    }

    @Override
    public boolean hasPermission(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Permission name cannot be null");
        }
        String lower = name.toLowerCase(Locale.ENGLISH);

        if (rawPermissions.containsKey("-" + lower)) return false;

        if (lower.contains(".")) {
            String[] parts = lower.split("\\.");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < parts.length - 1; i++) {
                if (i > 0) sb.append(".");
                sb.append(parts[i]);
                String negWildcard = "-"+sb+".*";
                if (rawPermissions.containsKey(negWildcard)) return false;
            }
        }

        if (rawPermissions.containsKey("*")) return true;

        if (lower.contains(".")) {
            String[] parts = lower.split("\\.");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < parts.length - 1; i++) {
                if (i > 0) sb.append(".");
                sb.append(parts[i]);
                String wildcard = sb + ".*";
                if (rawPermissions.containsKey(wildcard) && rawPermissions.get(wildcard)) return true;
            }
        }

        if (rawPermissions.containsKey(lower)) return rawPermissions.get(lower);

        Permission perm = Bukkit.getServer().getPluginManager().getPermission(name);
        if (perm != null) return perm.getDefault().getValue(isOp());
        return Permission.DEFAULT_PERMISSION.getValue(isOp());
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        if (perm == null) throw new IllegalArgumentException("Permission cannot be null");
        return isPermissionSet(perm.getName());
    }

    @Override
    public boolean hasPermission(Permission perm) {
        if (perm == null) throw new IllegalArgumentException("Permission cannot be null");
        return hasPermission(perm.getName());
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        if (name == null) {
            throw new IllegalArgumentException("Permission name cannot be null");
        }
        if (plugin == null) throw new IllegalArgumentException("Plugin cannot be null");
        if (!plugin.isEnabled()) throw new IllegalArgumentException("Plugin " + plugin.getDescription().getFullName() + " is disabled");
        PermissionAttachment result = addAttachment(plugin);
        result.setPermission(name, value);
        recalculatePermissions();
        return result;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        if (plugin == null) throw new IllegalArgumentException("Plugin cannot be null");
        if (!plugin.isEnabled()) throw new IllegalArgumentException("Plugin " + plugin.getDescription().getFullName() + " is disabled");
        PermissionAttachment result = new PermissionAttachment(plugin, this.player);
        attachments.add(result);
        recalculatePermissions();
        return result;
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        if (attachment == null) throw new IllegalArgumentException("Attachment cannot be null");
        if (attachments.contains(attachment)) {
            attachments.remove(attachment);
            PermissionRemovedExecutor ex = attachment.getRemovalCallback();
            if (ex != null) ex.attachmentRemoved(attachment);
            recalculatePermissions();
            return;
        }
        throw new IllegalArgumentException("Given attachment is not part of Permissible object " + this.player);
    }

    @Override
    public void recalculatePermissions() {
        // Prevent NPE during construction
        if (player == null || permissions == null || rawPermissions == null || attachments == null) return;

        clearPermissions();

        for (Map.Entry<String, Boolean> entry : rawPermissions.entrySet()) {
            permissions.put(entry.getKey(), new PermissionAttachmentInfo(player, entry.getKey(), null, entry.getValue()));
        }

        Set<Permission> defaults = Bukkit.getServer().getPluginManager().getDefaultPermissions(isOp());
        Bukkit.getServer().getPluginManager().subscribeToDefaultPerms(isOp(), this.player);
        for (Permission perm : defaults) {
            String name = perm.getName().toLowerCase();
            permissions.putIfAbsent(name, new PermissionAttachmentInfo(player, name, null, true));
            Bukkit.getServer().getPluginManager().subscribeToPermission(name, this.player);
            calculateChildPermissions(perm.getChildren(), false, null);
        }

        for (PermissionAttachment attachment : attachments) {
            calculateChildPermissions(attachment.getPermissions(), false, attachment);
        }
    }

    @Override
    public synchronized void clearPermissions() {
        if (player == null) return;
        Set<String> perms = new HashSet<>(permissions.keySet());
        for (String name : perms) {
            Bukkit.getServer().getPluginManager().unsubscribeFromPermission(name, this.player);
        }
        Bukkit.getServer().getPluginManager().unsubscribeFromDefaultPerms(false, this.player);
        Bukkit.getServer().getPluginManager().unsubscribeFromDefaultPerms(true, this.player);
        permissions.clear();
    }

    private void calculateChildPermissions(Map<String, Boolean> children, boolean invert, PermissionAttachment attachment) {
        for (Map.Entry<String, Boolean> entry : children.entrySet()) {
            String node = entry.getKey().toLowerCase();
            boolean value = entry.getValue() ^ invert;
            permissions.put(node, new PermissionAttachmentInfo(player, node, attachment, value));
            Permission perm = Bukkit.getServer().getPluginManager().getPermission(node);
            if (perm != null) {
                calculateChildPermissions(perm.getChildren(), !value, attachment);
            }
        }
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return new HashSet<>(permissions.values());
    }


}

