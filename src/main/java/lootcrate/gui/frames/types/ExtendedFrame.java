package lootcrate.gui.frames.types;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import lootcrate.LootCrate;
import lootcrate.gui.events.custom.GUICloseEvent;
import lootcrate.gui.events.custom.GUIItemClickEvent;
import lootcrate.gui.items.GUIItem;
import lootcrate.utils.ObjUtils;

public abstract class ExtendedFrame implements Frame, Listener
{
    private LootCrate plugin;
    private int id;
    private Player player;
    private String title;
    private GUIItem[] contents;
    private Inventory inventory;
    
    public ExtendedFrame(LootCrate plugin, Player p, String title, GUIItem[] contents)
    {
	this.id = ObjUtils.randomID(5);
	this.plugin = plugin;
	this.player = p;
	this.title = title;
	
	if(contents.length != 54)
	    this.contents = new GUIItem[54];
	else
	    this.contents = contents;
	
	this.inventory = createInventory();
    }
    
    public ExtendedFrame(LootCrate plugin, Player p, String title)
    {
	this.id = ObjUtils.randomID(5);
	this.plugin = plugin;
	this.player = p;
	this.title = title;
	this.contents = new GUIItem[54];
	this.inventory = createInventory();
    }
    
    @Override
    public int getId()
    {
	return id;
    }

    @Override
    public Player getViewer()
    {
	return this.player;
    }

    @Override
    public String getTitle()
    {
	return this.title;
    }

    @Override
    public GUIItem[] getContents()
    {
	return this.contents;
    }

    @Override
    public Inventory getInventory()
    {
	return this.inventory;
    }
    
    @Override
    public void open()
    {
	player.closeInventory();
	player.openInventory(getInventory());
    }
    
    @Override
    public void close()
    {
	if(player.getOpenInventory().getTitle().equalsIgnoreCase(getTitle()))
	    player.closeInventory();
    }
    
    @Override
    public void registerItems()
    {
	for(GUIItem item : getContents())
	    if(item != null)
		plugin.getServer().getPluginManager().registerEvents(item, plugin);
    }
    
    @Override
    public void registerFrame()
    {
	plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    public abstract void generateFrame();
    
    public void setItem(int slot, GUIItem item)
    {
	if(slot >= contents.length || slot < 0) return;
	
	contents[slot] = item;
	getInventory().setItem(slot, item.getItemStack());
    }
    
    private Inventory createInventory()
    {
	return Bukkit.createInventory(null, 54, getTitle());
    }
    
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e)
    {
	if (!((Player) e.getWhoClicked()).equals(this.getViewer()))
	    return;
	if (e.getInventory() != this.getInventory())
	    return;
	if (e.getCurrentItem() == null)
	    return;

	e.setCancelled(true);
	
	GUIItemClickEvent event = new GUIItemClickEvent((Player) e.getWhoClicked(), getContents()[e.getSlot()], this);
	Bukkit.getPluginManager().callEvent(event);
    }
    
    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent e)
    {
	if (!Bukkit.getPlayer(e.getPlayer().getUniqueId()).equals(this.getViewer()))
	    return;
	if (e.getInventory() != this.getInventory())
	    return;
	
	GUICloseEvent event = new GUICloseEvent(Bukkit.getPlayer(e.getPlayer().getUniqueId()), this);
	Bukkit.getPluginManager().callEvent(event);
    }
    
    

}