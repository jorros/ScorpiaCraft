package de.scorpiacraft.obj;

import org.getspout.spoutapi.gui.ListWidgetItem;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.material.MaterialData;

import de.scorpiacraft.ScorpiaCraft.Rank;

public class SItem extends ListWidgetItem
{
	private String name;
	private int amount;
	private int price;
	private SShop shop;
	
	public SItem(String name, int amount, int price)
	{
		super(name + " (" + amount + " verfügbar)", "Preis: " + price + "c");
		this.name = name;
		this.amount = amount;
		this.price = price;
		
		if(amount == -1)
			this.setTitle(name + " (unendlich verfügbar)");
	}
	
	public SItem()
	{
		super("", "");
	}
	
	public SItem setShop(SShop shop)
	{
		this.shop = shop;
		return this;
	}
	
	public SShop getShop()
	{
		return this.shop;
	}
	
	public SItem setName(String name)
	{
		this.name = name;
		if(amount == -1)
			this.setTitle(name + " (unendlich verfügbar)");
		else
			this.setTitle(name + " (" + amount + " verfügbar)");
		return this;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public SItem setAmount(int amount)
	{
		this.amount = amount;
		this.setTitle(name + " (" + amount + " verfügbar)");
		return this;
	}
	
	public SItem setAmount()
	{
		this.amount = -1;
		this.setTitle(name + " (unendlich verfügbar)");
		return this;
	}
	
	public int getAmount()
	{
		return this.amount;
	}
	
	public SItem setPrice(int price)
	{
		this.price = price;
		this.setText("Preis: " + price + "c");
		return this;
	}
	
	public int getPrice()
	{
		return getPrice(null);
	}
	
	public int getPrice(SPlayer player)
	{
		double discount = 1;
		if(player != null && player.getRank().getLevel() <= Rank.Moderator.getLevel())
			discount = 0.5;
		return (int)(this.price * discount);
	}
	
	public SpoutItemStack getItemStack()
	{
		return new SpoutItemStack(MaterialData.getMaterial(this.name), 1);
	}
}
