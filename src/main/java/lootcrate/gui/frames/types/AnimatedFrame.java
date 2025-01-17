package lootcrate.gui.frames.types;

import lootcrate.LootCrate;
import lootcrate.gui.items.GUIItem;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class AnimatedFrame extends BasicFrame implements Listener {

    public AnimatedFrame(LootCrate plugin, Player p, String title, GUIItem[] contents) {
        super(plugin, p, title, contents, 45);
    }

    public AnimatedFrame(LootCrate plugin, Player p, String title) {
        super(plugin, p, title, 45);
    }

    public AnimatedFrame getAnimatedFrame() {
        return this;
    }

    public abstract void showAnimation();



}
