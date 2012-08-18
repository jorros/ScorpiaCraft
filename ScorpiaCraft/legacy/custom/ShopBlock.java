package de.scorpiacraft.custom;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.block.design.GenericCubeBlockDesign;
import org.getspout.spoutapi.block.design.Texture;
import org.getspout.spoutapi.gui.GenericListWidget;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.material.block.GenericCubeCustomBlock;
import org.getspout.spoutapi.player.SpoutPlayer;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.features.Dynmap;
import de.scorpiacraft.features.Economy;
import de.scorpiacraft.obj.SButton;
import de.scorpiacraft.obj.SItem;
import de.scorpiacraft.obj.SPlayer;
import de.scorpiacraft.obj.SShop;
import de.scorpiacraft.util.Disk;

public class ShopBlock extends GenericCubeCustomBlock
{
	public ShopBlock()
	{
		super(ScorpiaCraft.p, "Shop", 58, new GenericCubeBlockDesign(ScorpiaCraft.p, new Texture(ScorpiaCraft.p, Disk.getTexture("Nations", "Shop"), 512, 128, 128), new int[] { 0, 1, 1, 1, 1, 2 }));
	}
	
	@Override
	public boolean onBlockInteract(World world, int x, int y, int z, SpoutPlayer player)
	{
		SShop shop = Economy.getShop(new Location(world, x, y, z));
		
		if(shop == null)
			return false;
		
		if(!ScorpiaCraft.getPlayer(player).checkSpoutCraft())
			return true;
		
		// Shop gehört dem Besitzer
		if(shop.getOwner().equals(player.getName()))
		{
			SPlayer sp = ScorpiaCraft.getPlayer(player);
			if(sp.getSelectedShop() == null || !sp.getSelectedShop().equals(new Location(world, x, y, z)))
			{
				sp.setSelectedShop(x + "," + y + "," + z);
				sp.sendMessage("Shop " + shop.getName() + " ausgewählt");
				return true;
			}
		}
		
		// Wenn bereits ausgewählt oder nicht Besitzer
		GenericPopup popup = new GenericPopup();
		
		GenericListWidget itemlist = new GenericListWidget();
		itemlist.setAnchor(WidgetAnchor.TOP_LEFT).shiftYPos(5).shiftXPos(5);
		itemlist.setHeight(200).setWidth(400);
		
		for(SItem item : shop.getItems())
		{
			itemlist.addItem(item);
		}
		
		SButton buy = new SButton("Kaufen", "buy");
		buy.setAnchor(WidgetAnchor.TOP_LEFT).shiftYPos(210).shiftXPos(5);
		buy.setHeight(20).setWidth(100);
		
		SButton stackbuy = new SButton("Stack kaufen", "buystack");
		stackbuy.setAnchor(WidgetAnchor.TOP_LEFT).shiftYPos(210).shiftXPos(110);
		stackbuy.setHeight(20).setWidth(100);
		
		popup.attachWidget(this.getPlugin(), itemlist);
		popup.attachWidget(this.getPlugin(), buy);
		popup.attachWidget(this.getPlugin(), stackbuy);
		player.getMainScreen().attachPopupScreen(popup);
		return true;
	}
	
	@Override
	public void onBlockPlace(World world, int x, int y, int z, LivingEntity living)
	{
		if(living instanceof Player)
		{
			SPlayer sp = ScorpiaCraft.getPlayer(((Player)living).getName());
			
			Economy.createShop(sp.getName(), new Location(world, x, y, z), "");
			
			Dynmap.setMarker("shop", "Shop", world.getBlockAt(x, y, z).getLocation());
		}
	}
	
	@Override
	public void onBlockDestroyed(World world, int x, int y, int z) 
	{
		Economy.removeShop(new Location(world, x, y, z));
		Dynmap.removeMarker("shop", world.getBlockAt(x, y, z).getLocation());
	}
}
