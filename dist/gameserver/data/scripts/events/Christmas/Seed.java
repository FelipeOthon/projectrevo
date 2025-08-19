package events.Christmas;

import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Spawn;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.templates.npc.NpcTemplate;

public class Seed implements IItemHandler, ScriptFile
{
	private static int[] _itemIds = new int[] { 5560, 5561 };
	private static int[] _npcIds = new int[] { 13006, 13007 };

	private static final int DESPAWN_TIME = 600000;

	@Override
	public boolean useItem(final Playable playable, final ItemInstance item, final Boolean ctrl)
	{
		final Player activeChar = (Player) playable;
		NpcTemplate template = null;
		final int itemId = item.getItemId();
		for(int i = 0; i < Seed._itemIds.length; ++i)
			if(Seed._itemIds[i] == itemId)
			{
				template = NpcTable.getTemplate(Seed._npcIds[i]);
				break;
			}
		if(template == null)
			return false;
		try
		{
			final Spawn spawn = new Spawn(template);
			spawn.setLoc(activeChar.getLoc());
			final NpcInstance npc = spawn.doSpawn(false);
			npc.setTitle(activeChar.getName());
			spawn.respawnNpc(npc);
			if(itemId == 5561)
			{
				npc.setAI(new ctreeAI(npc));
				npc.getAI().startAITask();
			}
			ThreadPoolManager.getInstance().schedule(new DeSpawnScheduleTimerTask(spawn), 600000L);
			activeChar.getInventory().destroyItem(item.getObjectId(), 1L, false);
		}
		catch(Exception e)
		{
			activeChar.sendPacket(new SystemMessage(50));
		}
		return true;
	}

	@Override
	public int[] getItemIds()
	{
		return _itemIds;
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

	public class DeSpawnScheduleTimerTask implements Runnable
	{
		Spawn spawnedTree;

		public DeSpawnScheduleTimerTask(final Spawn spawn)
		{
			spawnedTree = null;
			spawnedTree = spawn;
		}

		@Override
		public void run()
		{
			try
			{
				spawnedTree.getLastSpawn().decayMe();
				spawnedTree.getLastSpawn().deleteMe();
			}
			catch(Throwable t)
			{}
		}
	}
}
