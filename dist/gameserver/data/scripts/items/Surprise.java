package items;

import l2s.commons.util.Rnd;
import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.ItemList;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;

public class Surprise implements IItemHandler, ScriptFile
{
	private static final int[] _itemIds;
	private static final int[] accessories;
	private static final int[] ls;
	private static final int[] diff;
	private static final int[] diff2;
	private static final int[] enchants;

	@Override
	public boolean useItem(final Playable playable, final ItemInstance item, final Boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;
		final Player player = (Player) playable;
		if(player.isActionsDisabled() || player.isSitting())
		{
			player.sendActionFailed();
			return false;
		}
		int itemId = 57;
		int count = 1;
		if(Rnd.chance(0.02))
			itemId = 6660;
		else if(Rnd.chance(0.08))
			itemId = Rnd.get(6661, 6662);
		else if(Rnd.chance(0.1))
			itemId = 6721;
		else if(Rnd.chance(0.5))
			itemId = 8210;
		else if(Rnd.chance(0.8))
			itemId = 4422;
		else if(Rnd.chance(0.5))
			itemId = Rnd.get(Surprise.accessories);
		else if(Rnd.chance(1))
			itemId = Rnd.get(Surprise.enchants);
		else if(Rnd.chance(2))
			itemId = 4295;
		else if(Rnd.chance(10))
			itemId = Rnd.get(Surprise.diff);
		else if(Rnd.chance(25))
			itemId = Rnd.get(Surprise.ls);
		else if(Rnd.chance(45))
			itemId = Rnd.get(Surprise.diff2);
		if(itemId == 5592)
			count = Rnd.get(200, 400);
		else if(itemId == 8627)
			count = Rnd.get(1, 3);
		else if(itemId == 6673)
			count = Rnd.get(10, 15);
		player.getInventory().destroyItem(item, 1L, false);
		player.getInventory().addItem(itemId, count);
		player.sendPacket(new ItemList(player, false));
		player.sendPacket(SystemMessage.obtainItems(itemId, count, 0));
		return true;
	}

	@Override
	public final int[] getItemIds()
	{
		return Surprise._itemIds;
	}

	@Override
	public void onLoad()
	{
		ItemHandler.getInstance().registerItemHandler(this);
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	static
	{
		_itemIds = new int[] { 9170 };
		accessories = new int[] {
				5808,
				6394,
				6843,
				7680,
				7683,
				6846,
				6844,
				7695,
				7696,
				8921,
				7059,
				7060,
				6845,
				7681,
				7682,
				8552,
				7837,
				7839,
				8177,
				8178,
				8179,
				8180,
				8184,
				8185,
				8186,
				8187,
				8188,
				8189,
				8557,
				8558,
				8559,
				8560,
				8561,
				8562,
				8563,
				8564,
				8565,
				8566,
				8567,
				8568,
				7836,
				8936,
				8569,
				8910,
				8913,
				8914,
				8915,
				8920,
				8917,
				8918,
				8919,
				8922,
				8916,
				8923 };
		ls = new int[] { 8742, 8742, 8752, 8762 };
		diff = new int[] { 6622, 6622, 1538 };
		diff2 = new int[] { 5592, 8627, 5592, 6673 };
		enchants = new int[] { 731, 732, 961, 962 };
	}
}
