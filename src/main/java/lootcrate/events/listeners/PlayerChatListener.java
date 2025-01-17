package lootcrate.events.listeners;

import lootcrate.LootCrate;
import lootcrate.enums.ChatState;
import lootcrate.enums.CrateOptionType;
import lootcrate.gui.frames.creation.items.CrateItemCreationCommandsFrame;
import lootcrate.gui.frames.menu.CrateFrame;
import lootcrate.objects.Crate;
import lootcrate.objects.CrateOption;
import lootcrate.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    private final LootCrate plugin;

    public PlayerChatListener(LootCrate plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if (!plugin.getChatManager().hasState(p))
            return;

        ChatState state = plugin.getChatManager().getState(p);
        Crate crate = state.getCrate();
        Crate finalCrate;

        switch (state) {
            case CHANGE_CRATE_NAME:
                crate.setName(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
                break;
            case CHANGE_CRATE_MESSAGE:
                crate.setOption(new CrateOption(CrateOptionType.OPEN_MESSAGE,
                        ChatColor.translateAlternateColorCodes('&', e.getMessage())));
                break;
            case CHANGE_CRATE_SOUND:
                Sound sound = Sound.valueOf(e.getMessage());
                if (sound == null)
                    return;
                crate.setOption(new CrateOption(CrateOptionType.OPEN_SOUND, sound.toString()));
                p.playSound(p.getLocation(), sound, 1, 1);
                break;
            case CREATE_CRATE_NAME:
                crate = new Crate(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
                plugin.getCrateManager().addDefaultOptions(crate);
                plugin.getCacheManager().update(crate);
                finalCrate = crate;
                Bukkit.getScheduler().runTask(plugin, () -> {
                    plugin.getInvManager().openFrame(p, new CrateFrame(plugin, e.getPlayer(), finalCrate));
                });
                break;
            case ADD_ITEM_COMMAND:
                state.getCrateItem().getCommands().add(e.getMessage());
                crate.replaceItem(state.getCrateItem());
                plugin.getCacheManager().update(crate);
                finalCrate = crate;
                Bukkit.getScheduler().runTask(plugin, () -> {
                    plugin.getInvManager().openFrame(p, new CrateItemCreationCommandsFrame(plugin, e.getPlayer(), finalCrate, state.getCrateItem()));
                });
                break;
            default:
                return;
        }

        e.setCancelled(true);
        plugin.getCacheManager().update(crate);
        plugin.getChatManager().removePlayer(p);

    }

}
