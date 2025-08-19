package items;

import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.scripts.ScriptFile;

public class CrystalCarol implements IItemHandler, ScriptFile
{
	private static final int[] _itemIds;

	@Override
	public boolean useItem(final Playable playable, final ItemInstance item, final Boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;
		final Player player = (Player) playable;
		final int itemId = item.getItemId();
		int skillId = 0;
		switch(itemId)
		{
			default:
			{
				skillId = 2140;
				break;
			}
			case 5563:
			{
				skillId = 2141;
				break;
			}
			case 5564:
			{
				skillId = 2142;
				break;
			}
			case 5565:
			{
				skillId = 2143;
				break;
			}
			case 5566:
			{
				skillId = 2144;
				break;
			}
			case 5583:
			{
				skillId = 2145;
				break;
			}
			case 5584:
			{
				skillId = 2146;
				break;
			}
			case 5585:
			{
				skillId = 2147;
				break;
			}
			case 5586:
			{
				skillId = 2148;
				break;
			}
			case 5587:
			{
				skillId = 2149;
				break;
			}
			case 4411:
			{
				skillId = 2069;
				break;
			}
			case 4412:
			{
				skillId = 2068;
				break;
			}
			case 4413:
			{
				skillId = 2070;
				break;
			}
			case 4414:
			{
				skillId = 2072;
				break;
			}
			case 4415:
			{
				skillId = 2071;
				break;
			}
			case 4416:
			{
				skillId = 2073;
				break;
			}
			case 4417:
			{
				skillId = 2067;
				break;
			}
			case 5010:
			{
				skillId = 2066;
				break;
			}
			case 7061:
			{
				skillId = 2073;
				break;
			}
			case 7062:
			{
				skillId = 2230;
				break;
			}
			case 8555:
			{
				skillId = 2272;
				break;
			}
			case 6903:
			{
				skillId = 2187;
				break;
			}
		}
		player.getInventory().destroyItem(item, 1L, true);
		player.broadcastPacket(new L2GameServerPacket[] { new MagicSkillUse(player, player, skillId, 1, 1, 0L) });
		return true;
	}

	@Override
	public final int[] getItemIds()
	{
		return CrystalCarol._itemIds;
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
		_itemIds = new int[] {
				5562,
				5563,
				5564,
				5565,
				5566,
				5583,
				5584,
				5585,
				5586,
				5587,
				4411,
				4412,
				4413,
				4414,
				4415,
				4416,
				4417,
				5010,
				7061,
				7062,
				6903,
				8555 };
	}
}
