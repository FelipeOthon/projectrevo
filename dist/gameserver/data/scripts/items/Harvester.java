package items;

import l2s.gameserver.cache.Msg;
import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.SkillTable;

public class Harvester implements IItemHandler, ScriptFile
{
	private static final int[] _itemIds;
	Player player;
	MonsterInstance target;

	@Override
	public boolean useItem(final Playable playable, final ItemInstance _item, final Boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;
		final Player player = (Player) playable;
		if(player.getTarget() == null || !player.getTarget().isMonster())
		{
			player.sendPacket(Msg.TARGET_IS_INCORRECT);
			return false;
		}
		target = (MonsterInstance) player.getTarget();
		if(!target.isDead() || target.isDying())
		{
			player.sendPacket(Msg.TARGET_IS_INCORRECT);
			return false;
		}
		final Skill skill = SkillTable.getInstance().getInfo(2098, 1);
		if(skill != null && skill.checkCondition(player, target, false, false, true))
		{
			player.getAI().Cast(skill, target);
			return true;
		}
		return false;
	}

	@Override
	public final int[] getItemIds()
	{
		return Harvester._itemIds;
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
		_itemIds = new int[] { 5125 };
	}
}
