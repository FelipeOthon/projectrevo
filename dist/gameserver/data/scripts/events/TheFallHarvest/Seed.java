package events.TheFallHarvest;

import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.GameObjectTasks;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.ExUseSharedGroupItem;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.Location;
import npc.model.SquashInstance;

public class Seed implements IItemHandler, ScriptFile
{
	private static final int REUSE = 180000;
	private static int[] _itemIds;
	private static int[] _npcIds;

	@Override
	public boolean useItem(final Playable playable, final ItemInstance item, final Boolean ctrl)
	{
		final Player activeChar = (Player) playable;
		if(activeChar.isInZone(Zone.ZoneType.RESIDENCE))
		{
			activeChar.sendMessage("\u041d\u0435\u043b\u044c\u0437\u044f \u0432\u0437\u0440\u0430\u0449\u0438\u0432\u0430\u0442\u044c \u0442\u044b\u043a\u0432\u0443 \u0432 \u0440\u0435\u0437\u0438\u0434\u0435\u043d\u0446\u0438\u0438.");
			return false;
		}
		if(activeChar.isInZone(Zone.ZoneType.OlympiadStadia))
		{
			activeChar.sendMessage("\u041d\u0435\u043b\u044c\u0437\u044f \u0432\u0437\u0440\u0430\u0449\u0438\u0432\u0430\u0442\u044c \u0442\u044b\u043a\u0432\u0443 \u043d\u0430 \u0441\u0442\u0430\u0434\u0438\u043e\u043d\u0435.");
			return false;
		}
		final Skill sk = SkillTable.getInstance().getInfo(7045, 1);
		if(activeChar.isSkillDisabled(sk))
		{
			activeChar.sendPacket(new SystemMessage(48).addItemName(item.getItemId()));
			return false;
		}
		NpcTemplate template = null;
		final int itemId = item.getItemId();
		for(int i = 0; i < Seed._itemIds.length; ++i)
			if(Seed._itemIds[i] == itemId)
			{
				template = NpcTable.getTemplate(Seed._npcIds[i]);
				break;
			}
		if(template == null)
		{
			activeChar.sendPacket(Msg.TARGET_CAN_NOT_BE_FOUND);
			return false;
		}
		GameObject target = activeChar.getTarget();
		if(target == null)
			target = activeChar;
		activeChar.getInventory().destroyItem(item.getObjectId(), 1L, false);
		activeChar.sendPacket(new ExUseSharedGroupItem(itemId, 6, 180000, 180000));
		activeChar.disableSkill(sk, 180000L);
		final NpcInstance npc = template.getNewInstance();
		npc.setSpawnedLoc(Location.findAroundPosition(activeChar.getLoc(), 30, 70, activeChar.getGeoIndex()));
		npc.setAI(new SquashAI(npc));
		((SquashInstance) npc).setSpawner(activeChar);
		npc.spawnMe(npc.getSpawnedLoc());
		ThreadPoolManager.getInstance().schedule(new GameObjectTasks.DeleteTask(npc), 180000L);
		return true;
	}

	@Override
	public int[] getItemIds()
	{
		return Seed._itemIds;
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
		Seed._itemIds = new int[] { 6389, 6390 };
		Seed._npcIds = new int[] { 12774, 12777 };
	}
}
