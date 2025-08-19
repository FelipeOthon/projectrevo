package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.CtrlEvent;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.World;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.NpcSay;
import npc.model.SepulcherMonsterInstance;

public class Victim extends DefaultAI
{
	public Victim(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{
		final SepulcherMonsterInstance actor = getActor();
		if(actor == null || actor.isDead() || attacker == null)
			return;
		if(Rnd.chance(10))
			actor.broadcastPacket(new L2GameServerPacket[] { new NpcSay(actor, 0, "forgive me!!") });
	}

	@Override
	protected void onEvtThink()
	{
		final SepulcherMonsterInstance actor = getActor();
		if(actor == null || actor.noFollow || actor.isActionsDisabled() || actor.isAfraid() || actor.isDead() || actor.isMovementDisabled() || actor.isFollow)
			return;
		Creature target = null;
		for(final Player player : World.getAroundPlayers(actor, 1000, 400))
			if(player.getParty() != null)
			{
				target = player.getParty().getPartyLeader();
				break;
			}
		if(target != null && actor.getDistance(target) < 4000.0 && !actor.isInRange(target, 120L) && !actor.isFollow)
			actor.followToCharacter(target, 100, false);
	}

	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				final SepulcherMonsterInstance actor = Victim.this.getActor();
				if(actor == null || actor.isDead())
					return;
				for(final Creature cha : World.getAroundCharacters(actor, 1000, 400))
					if(cha.isMonster() && cha.getNpcId() == 18170)
					{
						cha.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, actor, 100);
						break;
					}
			}
		}, 3000L);
	}

	@Override
	protected void thinkAttack()
	{}

	@Override
	protected boolean randomWalk()
	{
		return false;
	}

	@Override
	public SepulcherMonsterInstance getActor()
	{
		return (SepulcherMonsterInstance) super.getActor();
	}
}
