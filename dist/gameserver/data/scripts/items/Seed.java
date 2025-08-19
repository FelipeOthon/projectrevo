package items;

import l2s.gameserver.cache.Msg;
import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.instancemanager.TownManager;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Manor;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.entity.residence.Castle;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.SkillTable;

public class Seed implements IItemHandler, ScriptFile
{
	private static int[] _itemIds;

	@Override
	public boolean useItem(final Playable playable, final ItemInstance item, final Boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;
		final Player player = (Player) playable;
		final GameObject targ = player.getTarget();
		if(targ == null)
		{
			player.sendActionFailed();
			return false;
		}
		if(!targ.isMonster() || targ.isRaid() || ((MonsterInstance) targ).isRaidFighter() || targ.isChest() || targ.isBox() || ((MonsterInstance) targ).getChampion() > 0 && !item.isAltSeed())
		{
			player.sendPacket(Msg.THE_TARGET_IS_UNAVAILABLE_FOR_SEEDING);
			return false;
		}
		final MonsterInstance target = (MonsterInstance) targ;
		if(target == null)
		{
			player.sendPacket(Msg.INCORRECT_TARGET);
			return false;
		}
		if(target.isDead())
		{
			player.sendPacket(Msg.INCORRECT_TARGET);
			return false;
		}
		if(target.isSeeded())
		{
			player.sendPacket(Msg.THE_SEED_HAS_BEEN_SOWN);
			return false;
		}
		final int seedId = item.getItemId();
		if(seedId == 0 || player.getInventory().findItemByItemId(item.getItemId()) == null)
		{
			player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
			return false;
		}
		int castleId = TownManager.getInstance().getClosestTown(player).getCastleIndex();
		if(castleId < 0)
			castleId = 1;
		else
		{
			final Castle castle = ResidenceHolder.getInstance().getResidence(Castle.class, castleId);
			if(castle != null)
				castleId = castle.getId();
		}
		if(Manor.getInstance().getCastleIdForSeed(seedId) != castleId)
		{
			player.sendPacket(Msg.THIS_SEED_MAY_NOT_BE_SOWN_HERE);
			return false;
		}
		final Skill skill = SkillTable.getInstance().getInfo(2097, 1);
		if(skill == null)
		{
			player.sendActionFailed();
			return false;
		}
		if(skill.checkCondition(player, target, false, false, true))
		{
			player.setUseSeed(seedId);
			player.getAI().Cast(skill, target);
		}
		return true;
	}

	@Override
	public final int[] getItemIds()
	{
		return Seed._itemIds;
	}

	@Override
	public void onLoad()
	{
		Seed._itemIds = new int[Manor.getInstance().getAllSeeds().size()];
		int id = 0;
		for(final Integer s : Manor.getInstance().getAllSeeds().keySet())
			Seed._itemIds[id++] = s;
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
		Seed._itemIds = new int[0];
	}
}
