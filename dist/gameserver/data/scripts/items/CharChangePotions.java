package items;

import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.scripts.ScriptFile;

public class CharChangePotions implements IItemHandler, ScriptFile
{
	private static final int[] _itemIds;

	@Override
	public boolean useItem(final Playable playable, final ItemInstance item, final Boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;
		final Player player = (Player) playable;
		final int itemId = item.getItemId();
		if(player.isActionsDisabled())
		{
			player.sendActionFailed();
			return false;
		}
		player.getInventory().destroyItem(item, 1L, true);
		switch(itemId)
		{
			case 5235:
			{
				player.setFace(0);
				break;
			}
			case 5236:
			{
				player.setFace(1);
				break;
			}
			case 5237:
			{
				player.setFace(2);
				break;
			}
			case 5238:
			{
				player.setHairColor(0);
				break;
			}
			case 5239:
			{
				player.setHairColor(1);
				break;
			}
			case 5240:
			{
				player.setHairColor(2);
				break;
			}
			case 5241:
			{
				player.setHairColor(3);
				break;
			}
			case 5242:
			{
				player.setHairStyle(0);
				break;
			}
			case 5243:
			{
				player.setHairStyle(1);
				break;
			}
			case 5244:
			{
				player.setHairStyle(2);
				break;
			}
			case 5245:
			{
				player.setHairStyle(3);
				break;
			}
			case 5246:
			{
				player.setHairStyle(4);
				break;
			}
			case 5247:
			{
				player.setHairStyle(5);
				break;
			}
			case 5248:
			{
				player.setHairStyle(6);
				break;
			}
		}
		player.broadcastPacket(new L2GameServerPacket[] { new MagicSkillUse(player, player, 2003, 1, 1, 0L) });
		player.broadcastUserInfo(true);
		return true;
	}

	@Override
	public final int[] getItemIds()
	{
		return CharChangePotions._itemIds;
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
		_itemIds = new int[] { 5235, 5236, 5237, 5238, 5239, 5240, 5241, 5242, 5243, 5244, 5245, 5246, 5247, 5248 };
	}
}
