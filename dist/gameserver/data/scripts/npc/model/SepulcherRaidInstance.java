package npc.model;

import java.util.concurrent.Future;

import bosses.FourSepulchersSpawn;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.RaidBossInstance;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.templates.npc.NpcTemplate;

public class SepulcherRaidInstance extends RaidBossInstance
{
	public int mysteriousBoxId;
	protected Future<?> _onDeadEventTask;

	public SepulcherRaidInstance(final int objectId, final NpcTemplate template)
	{
		super(objectId, template);
		mysteriousBoxId = 0;
		_onDeadEventTask = null;
	}

	@Override
	public void onDeath(final Creature killer)
	{
		super.onDeath(killer);
		final Player player = killer.getPlayer();
		if(player != null)
			giveCup(player);
		if(_onDeadEventTask != null)
			_onDeadEventTask.cancel(true);
		_onDeadEventTask = ThreadPoolManager.getInstance().schedule(new OnDeadEvent(this), 2500L);
	}

	@Override
	public void deleteMe()
	{
		if(_onDeadEventTask != null)
		{
			_onDeadEventTask.cancel(true);
			_onDeadEventTask = null;
		}
		super.deleteMe();
	}

	private void giveCup(final Player player)
	{
		final int questId = 620;
		int cupId = 0;
		final int oldBrooch = 7262;
		switch(getNpcId())
		{
			case 25339:
			{
				cupId = 7256;
				break;
			}
			case 25342:
			{
				cupId = 7257;
				break;
			}
			case 25346:
			{
				cupId = 7258;
				break;
			}
			case 25349:
			{
				cupId = 7259;
				break;
			}
		}
		if(player.getParty() != null)
			for(final Player mem : player.getParty().getPartyMembers())
			{
				final QuestState qs = mem.getQuestState(questId);
				if(qs != null && (qs.isStarted() || qs.isCompleted()) && mem.getInventory().getItemByItemId(oldBrooch) == null)
					Functions.addItem(mem, cupId, 1L);
			}
		else
		{
			final QuestState qs2 = player.getQuestState(questId);
			if(qs2 != null && (qs2.isStarted() || qs2.isCompleted()) && player.getInventory().getItemByItemId(oldBrooch) == null)
				Functions.addItem(player, cupId, 1L);
		}
	}

	private class OnDeadEvent implements Runnable
	{
		SepulcherRaidInstance _activeChar;

		public OnDeadEvent(final SepulcherRaidInstance activeChar)
		{
			_activeChar = activeChar;
		}

		@Override
		public void run()
		{
			FourSepulchersSpawn.spawnEmperorsGraveNpc(_activeChar.mysteriousBoxId);
		}
	}
}
