package lootcrate.enums;

public enum Permission {
    COMMAND_META("lootcrate.command.meta"),
    COMMAND_LOOTCRATE_CREATE("lootcrate.command.lootcrate.create"),
    COMMAND_LOOTCRATE_DELETE("lootcrate.command.lootcrate.delete"),
    COMMAND_LOOTCRATE_KEY("lootcrate.command.lootcrate.key"),
    COMMAND_LOOTCRATE_ADD("lootcrate.command.lootcrate.add"),
    COMMAND_LOOTCRATE_REMOVE("lootcrate.command.lootcrate.remove"),
    COMMAND_LOOTCRATE_ITEMS("lootcrate.command.lootcrate.items"),
    COMMAND_LOOTCRATE_SET("lootcrate.command.lootcrate.set"),
    COMMAND_LOOTCRATE_GIVE("lootcrate.command.lootcrate.give"),
    COMMAND_LOOTCRATE_COMMAND("lootcrate.command.lootcrate.command"),
    COMMAND_LOOTCRATE_RELOAD("lootcrate.command.lootcrate.reload"),
    COMMAND_LOOTCRATE_LIST("lootcrate.command.lootcrate.list"),
    COMMAND_LOOTCRATE_DISPLAYCHANCES("lootcrate.command.lootcrate.displaychances"),
    COMMAND_LOOTCRATE_VERSION("lootcrate.command.lootcrate.version"),
    COMMAND_LOOTCRATE_GUI("lootcrate.command.lootcrate.gui"),
    COMMAND_LOOTCRATE_PREVIEW("lootcate.command.lootcrate.preview"),
    COMMAND_LOOTCRATE_CLAIM("lootcrate.command.lootcrate.claim"),
    COMMAND_LOOTCRATE_ADMIN("lootcrate.command.*"),
    LOOTCRATE_INTERACT("lootcrate.interact."),
    LOOTCRATE_INTERACT_ADMIN("lootcrate.interact.*"),
    LOOTCRATE_UPDATE_NOTIFICATION("lootcrate.notification");

    String key;

    Permission(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
