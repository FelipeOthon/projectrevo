package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import npc.model.DrChaosInstance;

public class ChaosGolem extends Fighter
{
	public ChaosGolem(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtSpawn()
	{
		if(DrChaosInstance.first)
		{
			final NpcInstance actor = getActor();
			if(actor != null)
			{
				actor.block();
				actor.decayMe();
				DrChaosInstance.GOLEM = actor.getObjectId();
				DrChaosInstance.reset();
			}
		}
		DrChaosInstance.first = true;
		super.onEvtSpawn();
	}

	@Override
	public boolean isGlobalAI()
	{
		return true;
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{
		final NpcInstance actor = getActor();
		if(actor != null)
		{
			final int chance = Rnd.get(300);
			if(chance < 3)
			{
				String message = "";
				switch(chance)
				{
					case 0:
					{
						message = "Bwah-ha-ha! Your doom is at hand! Behold the Ultra Secret Super Weapon!";
						break;
					}
					case 1:
					{
						message = "Foolish, insignificant creatures! How dare you challenge me!";
						break;
					}
					default:
					{
						message = "I see that none will challenge me now!";
						break;
					}
				}
				Functions.npcSay(actor, message);
			}
		}
		super.onEvtAttacked(attacker, skill, damage);
	}

	@Override
	protected void onEvtDead(final Creature killer)
	{
		DrChaosInstance.stopDespawn();
		DrChaosInstance.status = 2;
		final NpcInstance actor = getActor();
		if(actor != null)
			Functions.npcSay(actor, "Urggh! You will pay dearly for this insult.");
		super.onEvtDead(killer);
	}
}
