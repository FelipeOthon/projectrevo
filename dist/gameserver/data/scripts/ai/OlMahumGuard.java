package ai;

import java.util.concurrent.ScheduledFuture;

import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.CtrlEvent;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.geodata.GeoEngine;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;

public class OlMahumGuard extends Fighter
{
	private Creature _attacker;
	private ScheduledFuture<?> _endFearTask;

	public OlMahumGuard(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{
		final NpcInstance actor = getActor();
		if(attacker != null && !actor.isAfraid() && actor.getCurrentHp() < actor.getMaxHp() / 2)
		{
			_attacker = attacker;
			if(Rnd.chance(50))
				Functions.npcShout(actor, "I'll be back", 2000);
			else
				Functions.npcShout(actor, "You are stronger than expected", 2000);
			clearTasks();
			actor.abortAttack(true, false);
			actor.abortCast(true, false);
			actor.stopMove();
			actor.startFear();
			int posX = actor.getX();
			int posY = actor.getY();
			int posZ = actor.getZ();
			int signx = -1;
			int signy = -1;
			if(posX > attacker.getX())
				signx = 1;
			if(posX > attacker.getY())
				signy = 1;
			final int range = 1000;
			posX += Math.round(signx * range);
			posY += Math.round(signy * range);
			posZ = GeoEngine.getLowerHeight(posX, posY, posZ, actor.getGeoIndex());
			actor.setRunning();
			actor.moveToLocation(posX, posY, posZ, 0, true);
			startEndFearTask();
		}
		else
			super.onEvtAttacked(attacker, skill, damage);
	}

	@Override
	protected void onEvtDead(final Creature killer)
	{
		final NpcInstance actor = getActor();
		_attacker = null;
		if(actor != null)
			actor.stopFear();
		if(_endFearTask != null)
			_endFearTask.cancel(true);
		_endFearTask = null;
		super.onEvtDead(killer);
	}

	public void startEndFearTask()
	{
		if(_endFearTask != null)
			_endFearTask.cancel(true);
		_endFearTask = ThreadPoolManager.getInstance().schedule(new EndFearTask(), 10000L);
	}

	@Override
	public boolean checkAggression(final Creature target)
	{
		return false;
	}

	@Override
	protected void onEvtAggression(final Creature target, final int aggro)
	{}

	public class EndFearTask implements Runnable
	{
		@Override
		public void run()
		{
			final NpcInstance actor = getActor();
			if(actor != null)
				actor.stopFear();
			_endFearTask = null;
			if(_attacker != null)
				OlMahumGuard.this.notifyEvent(CtrlEvent.EVT_AGGRESSION, _attacker, 100);
		}
	}
}
