package lootcrate.gui.frames;

import org.bukkit.entity.Player;

import lootcrate.LootCrate;
import lootcrate.gui.frames.types.BasicFrame;
import lootcrate.gui.items.GUIItem;
import lootcrate.objects.Crate;
import lootcrate.objects.CrateItem;

public class CrateViewFrame extends BasicFrame
{

    private LootCrate plugin;
    private Crate crate;
    
    public CrateViewFrame(LootCrate plugin, Player p, Crate crate)
    {
	super(plugin, p, crate.getName());
	
	this.plugin = plugin;
	this.crate = crate;
	
	generateFrame();
	registerItems();
	registerFrame();
    }

    @Override
    public void generateFrame()
    {
	int index = 0;
	for(CrateItem item : plugin.invManager.addCrateEffects(crate))
	{
	    if(index < getInventory().getSize())
		this.setItem(index, new GUIItem(item.getItem()));
	    index++;
	}
    }


}
