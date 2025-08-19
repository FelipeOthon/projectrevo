package items;

import l2s.gameserver.Config;
import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Servitor;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;

public class BeastShot implements IItemHandler, ScriptFile
{
	private static final int[] _itemIds;
	static final SystemMessage PETS_AND_SERVITORS_ARE_NOT_AVAILABLE_AT_THIS_TIME;
	static final SystemMessage WHEN_PET_OR_SERVITOR_IS_DEAD_SOULSHOTS_OR_SPIRITSHOTS_FOR_PET_OR_SERVITOR_ARE_NOT_AVAILABLE;
	static final SystemMessage YOU_DONT_HAVE_ENOUGH_SOULSHOTS_NEEDED_FOR_A_PET_SERVITOR;
	static final SystemMessage YOU_DONT_HAVE_ENOUGH_SPIRITSHOTS_NEEDED_FOR_A_PET_SERVITOR;
	static final SystemMessage THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT;

	@Override
	public boolean useItem(final Playable playable, final ItemInstance item, final Boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return true;
		final Player player = (Player) playable;
		if(player.isInOlympiadMode() && item.getItemId() == 6647)
		{
			player.sendPacket(BeastShot.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
			return false;
		}
		final Servitor pet = player.getServitor();
		if(pet == null)
		{
			player.sendPacket(BeastShot.PETS_AND_SERVITORS_ARE_NOT_AVAILABLE_AT_THIS_TIME);
			return false;
		}
		if(pet.isDead())
		{
			player.sendPacket(BeastShot.WHEN_PET_OR_SERVITOR_IS_DEAD_SOULSHOTS_OR_SPIRITSHOTS_FOR_PET_OR_SERVITOR_ARE_NOT_AVAILABLE);
			return true;
		}
		int consumption = 0;
		int skillid = 0;
		switch(item.getItemId())
		{
			case 6645:
			{
				if(pet.getChargedSoulShot())
					return true;
				consumption = Config.INFINITY_BEAST_SS ? 1 : pet.getSoulshotConsumeCount();
				if(item.getIntegerLimitedCount() < consumption)
				{
					player.sendPacket(BeastShot.YOU_DONT_HAVE_ENOUGH_SOULSHOTS_NEEDED_FOR_A_PET_SERVITOR);
					return false;
				}
				pet.chargeSoulShot();
				skillid = 2033;
				break;
			}
			case 6646:
			{
				if(pet.getChargedSpiritShot() > 0)
					return true;
				consumption = Config.INFINITY_BEAST_SS ? 1 : pet.getSpiritshotConsumeCount();
				if(item.getIntegerLimitedCount() < consumption)
				{
					player.sendPacket(BeastShot.YOU_DONT_HAVE_ENOUGH_SPIRITSHOTS_NEEDED_FOR_A_PET_SERVITOR);
					return false;
				}
				pet.chargeSpiritShot(1);
				skillid = 2008;
				break;
			}
			case 6647:
			{
				if(pet.getChargedSpiritShot() > 1)
					return true;
				consumption = Config.INFINITY_BEAST_SS ? 1 : pet.getSpiritshotConsumeCount();
				if(item.getIntegerLimitedCount() < consumption)
				{
					player.sendPacket(BeastShot.YOU_DONT_HAVE_ENOUGH_SPIRITSHOTS_NEEDED_FOR_A_PET_SERVITOR);
					return false;
				}
				pet.chargeSpiritShot(2);
				skillid = 2009;
				break;
			}
		}
		pet.broadcastPacket(new L2GameServerPacket[] { new MagicSkillUse(pet, pet, skillid, 1, 0, 0L) });
		if(!Config.INFINITY_BEAST_SS)
			player.getInventory().destroyItem(item, consumption, false);
		return true;
	}

	@Override
	public final int[] getItemIds()
	{
		return BeastShot._itemIds;
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
		_itemIds = new int[] { 6645, 6646, 6647 };
		PETS_AND_SERVITORS_ARE_NOT_AVAILABLE_AT_THIS_TIME = new SystemMessage(574);
		WHEN_PET_OR_SERVITOR_IS_DEAD_SOULSHOTS_OR_SPIRITSHOTS_FOR_PET_OR_SERVITOR_ARE_NOT_AVAILABLE = new SystemMessage(1598);
		YOU_DONT_HAVE_ENOUGH_SOULSHOTS_NEEDED_FOR_A_PET_SERVITOR = new SystemMessage(1701);
		YOU_DONT_HAVE_ENOUGH_SPIRITSHOTS_NEEDED_FOR_A_PET_SERVITOR = new SystemMessage(1700);
		THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT = new SystemMessage(1508);
	}
}
