package lootcrate.commands.subs;

import com.google.common.collect.ImmutableMap;
import lootcrate.LootCrate;
import lootcrate.commands.SubCommand;
import lootcrate.enums.Message;
import lootcrate.enums.Permission;
import lootcrate.enums.Placeholder;
import lootcrate.managers.KeyCacheManager;
import lootcrate.objects.Crate;
import lootcrate.utils.CommandUtils;
import lootcrate.utils.ObjUtils;
import lootcrate.utils.TabUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class SubCommandLootCrateGive extends SubCommand {
    private final String[] args;
    private final CommandSender sender;
    private final LootCrate plugin;
    private final KeyCacheManager keyCacheManager;

    /**
     * Default constructor for any {@link lootcrate.commands.SubCommand}
     *
     * @param plugin an instance of {@link lootcrate.LootCrate}
     * @param sender the {@link org.bukkit.command.CommandSender} which is executing this command
     * @param args the following arguments in the command string
     *
     */
    public SubCommandLootCrateGive(LootCrate plugin, CommandSender sender, String[] args) {
        super(plugin, sender, args, Permission.COMMAND_LOOTCRATE_GIVE, Permission.COMMAND_LOOTCRATE_ADMIN);
        this.plugin = plugin;
        this.sender = sender;
        this.args = args;
        this.keyCacheManager = plugin.getKeyCacheManager();
    }

    @Override
    public void runSubCommand(boolean playerRequired) {
        if (this.testPlayer(playerRequired))
            return;
        if (!this.testPermissions())
            return;

        if (args.length < 3) {
            plugin.getMessageManager().sendMessage(sender, Message.LOOTCRATE_COMMAND_GET_USAGE, null);
            return;
        }
        Player player = null;
        if (Bukkit.getPlayer(args[1]) == null) {
            if(!args[1].equalsIgnoreCase("all")) {
                plugin.getMessageManager().sendMessage(sender, Message.PLAYER_NOT_FOUND,
                        ImmutableMap.of(Placeholder.PLAYER_NAME, args[1]));
                return;
            }
        } else
            player = Bukkit.getPlayer(args[1]);

        if (CommandUtils.tryParse(args[2]) == null) {
            plugin.getMessageManager().sendMessage(sender, Message.LOOTCRATE_COMMAND_GET_USAGE, null);
            return;
        }

        Crate crate = plugin.getCacheManager().getCrateById(CommandUtils.tryParse(args[2]));
        if (crate == null) {
            plugin.getMessageManager().sendMessage(sender, Message.LOOTCRATE_NOT_FOUND,
                    ImmutableMap.of(Placeholder.CRATE_ID, "" + CommandUtils.tryParse(args[2])));
            return;
        }

        if (crate.getKey().getItem() == null) {
            plugin.getMessageManager().sendMessage(sender, Message.KEY_NOT_FOUND, ImmutableMap.of(Placeholder.CRATE_ID,
                    "" + CommandUtils.tryParse(args[2]), Placeholder.CRATE_NAME, "" + crate.getName()));
            return;
        }

        if (args.length == 4) {
            if (CommandUtils.tryParse(args[3]) == null) {
                plugin.getMessageManager().sendMessage(sender, Message.LOOTCRATE_COMMAND_GET_USAGE, null);
                return;
            }
            for (int i = 0; i < CommandUtils.tryParse(args[3]); i++) {
                addItem(player, crate);
            }

        } else
            addItem(player, crate);

    }

    @Override
    public List<String> runTabComplete() {
        List<String> list = new LinkedList<String>();

        if (!sender.hasPermission(Permission.COMMAND_LOOTCRATE_GIVE.getKey())
                && !sender.hasPermission(Permission.COMMAND_LOOTCRATE_ADMIN.getKey()))
            return list;

        if (args.length == 2) {
            for (Player pl : Bukkit.getOnlinePlayers())
                list.add(pl.getName());
            list.add("all");
        }
        if (args.length == 3) {
            list.add("[CrateID]");
            TabUtils.addCratesToList(list, plugin.getCacheManager());
        }
        if (args.length == 4)
            list.add("(Amount)");
        return list;
    }

    private void addItem(Player p, Crate crate) {
        if(p == null)
            for(Player pl : Bukkit.getOnlinePlayers()) {
                keyCacheManager.update(pl.getUniqueId(), crate);
                plugin.getMessageManager().sendMessage(sender, Message.LOOTCRATE_COMMAND_GIVE_SUCCESS_SENDER,
                        ImmutableMap.of(Placeholder.CRATE_ID, crate.getId() + "", Placeholder.CRATE_NAME, crate.getName(),
                                Placeholder.PLAYER_NAME, p == null ? "everyone" : p.getName()));
                plugin.getMessageManager().sendMessage(pl, Message.LOOTCRATE_COMMAND_GIVE_SUCCESS_RECEIVER,
                        ImmutableMap.of(Placeholder.CRATE_ID, crate.getId() + "", Placeholder.CRATE_NAME, crate.getName(),
                                Placeholder.SENDER_NAME, sender.getName()));
            }
        else {
            keyCacheManager.update(p.getUniqueId(), crate);
            plugin.getMessageManager().sendMessage(sender, Message.LOOTCRATE_COMMAND_GIVE_SUCCESS_SENDER,
                    ImmutableMap.of(Placeholder.CRATE_ID, crate.getId() + "", Placeholder.CRATE_NAME, crate.getName(),
                            Placeholder.PLAYER_NAME, p == null ? "everyone" : p.getName()));
            plugin.getMessageManager().sendMessage(p, Message.LOOTCRATE_COMMAND_GIVE_SUCCESS_RECEIVER,
                    ImmutableMap.of(Placeholder.CRATE_ID, crate.getId() + "", Placeholder.CRATE_NAME, crate.getName(),
                            Placeholder.SENDER_NAME, sender.getName()));
        }
    }

}
