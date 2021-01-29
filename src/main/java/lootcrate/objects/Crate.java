package lootcrate.objects;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import lootcrate.other.CrateOptionType;
import lootcrate.utils.ObjUtils;

public class Crate implements ConfigurationSerializable
{
    private int id;
    private String name;
    private CrateKey key;
    private List<CrateItem> items;
    private Map<CrateOptionType, Object> options;
    
    public Crate(String name, CrateKey key, List<CrateItem> items, Map<CrateOptionType, Object> options)
    {
	this.setId(ObjUtils.randomID(3));
	this.setName(name);
	this.setKey(key);
	if(items != null)
	    this.setItems(items);
	else
	    this.setItems(new ArrayList<CrateItem>());
	if(options != null)
	    this.setOptions(options);
	else
	    this.setOptions(new LinkedHashMap<CrateOptionType, Object>());
	    
    }
    
    public Crate(String name)
    {
	this.setId(ObjUtils.randomID(3));
	this.setName(name);
	this.setKey(null);
	this.setItems(new ArrayList<CrateItem>());
	this.setOptions(new LinkedHashMap<CrateOptionType, Object>());
    }
    
    public Crate(Map<String,Object> data)
    {
	this.id = (int) data.get("Id");
	this.name = (String) data.get("Name");
	this.key = new CrateKey((Map<String, Object>) data.get("Key"));
	this.items = (List<CrateItem>) data.get("Items");
	this.options = (Map<CrateOptionType, Object>) getDeseralizedOptions((MemorySection) data.get("Options"));
    }
    
    
    private int calculateChances()
    {
	int chance = 0;
	for(CrateItem item : getItems())
	{
	    chance+=item.getChance();
	}
	return chance;
    }

    public int getId()
    {
	return id;
    }

    public void setId(int id)
    {
	this.id = id;
    }

    public String getName()
    {
	return name;
    }

    public void setName(String name)
    {
	this.name = name;
    }

    public CrateKey getKey()
    {
	return key;
    }

    public void setKey(CrateKey key)
    {
	this.key = key;
    }

    public List<CrateItem> getItems()
    {
	return items;
    }
    
    public void addItem(CrateItem item)
    {
	getItems().add(item);
    }
    
    public CrateItem getItem(int id)
    {
	for(CrateItem item : getItems())
	{
	    if(item.getId() == id) return item;
	}
	return null;
    }
    
    public void removeItem(CrateItem item)
    {
	if(getItems().contains(item))
	    getItems().remove(item);
    }
    
    public void replaceItem(CrateItem item)
    {
	for(CrateItem item2 : getItems())
	{
	    if(item2.getId() == item.getId()) item2 = item;
	}
    }
   

    public void setItems(List<CrateItem> items)
    {
	this.items = items;
    }

    public int getChanceCount()
    {
	return calculateChances();
    }

    public Map<CrateOptionType, Object> getOptions()
    {
	return options;
    }

    public void setOptions(Map<CrateOptionType, Object> options)
    {
	this.options = options;
    }
    
    public void addOption(CrateOptionType key, Object value)
    {
	getOptions().put(key, value);
    }
    
    public CrateOption getOption(CrateOptionType type)
    {
	if(getOptions().containsKey(type))
	    return new CrateOption(type, getOptions().get(type));
	return null;
    }
    
    public void setOption(CrateOption option)
    {
	if(getOptions().containsKey(option.getKey()))
	    getOptions().remove(option.getKey());
	getOptions().put(option.getKey(), option.getValue());
    }
    
    
    @Override
    public Map<String, Object> serialize()
    {
	Map<String, Object> map = new LinkedHashMap<String, Object>();
	map.put("Id", getId());
	map.put("Name", getName());
	map.put("Options", getSeralizedOptions());
	map.put("Key", getKey() != null ? getKey().serialize() : null);
	map.put("Items", getItems());
	return map;
    }
    
    public Map<String, Object> getSeralizedOptions()
    {
	Map<String, Object> map = new LinkedHashMap<String, Object>();
	for(CrateOptionType type : getOptions().keySet())
	{
	    map.put(type.getKey(), getOptions().get(type));
	}
	return map;
    }
    
    public Map<CrateOptionType, Object> getDeseralizedOptions(MemorySection section)
    {
	Map<CrateOptionType, Object> item = new LinkedHashMap<CrateOptionType, Object>();
	for (String s : section.getKeys(false))
	{
	    CrateOptionType type = CrateOptionType.fromKey(s);
	    Object value = section.get(s);
	    item.put(type, value);
	}
	return item;
    }

}
