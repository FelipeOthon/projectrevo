package ai;

import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.tables.SkillTable;

public class Quest421FairyTree extends Fighter
{
	public Quest421FairyTree(final NpcInstance actor)
	{
		super(actor);
		actor.setImmobilized(true);
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{
		final NpcInstance actor = getActor();
		if(attacker == null || actor == null || actor.isDead())
			return;
		if(attacker.isPet())
		{
			final Player player = attacker.getPlayer();
			if(player != null)
			{
				final QuestState qs = player.getQuestState(421);
				if(qs != null)
					qs.getQuest().notifyAttack(actor, qs);
			}
		}
		else if(attacker.isPlayer())
		{
			final Skill sk = SkillTable.getInstance().getInfo(4243, 1);
			sk.getEffects(getActor(), attacker, false, false);
		}
	}

	@Override
	protected void onEvtAggression(final Creature attacker, final int aggro)
	{
		if(attacker != null && attacker.isPlayer())
		{
			final Skill sk = SkillTable.getInstance().getInfo(4243, 1);
			sk.getEffects(getActor(), attacker, false, false);
		}
	}

	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		return actor == null || actor.isActionsDisabled() || actor.isBlocked() || _randomAnimationEnd > System.currentTimeMillis() || randomAnimation();
	}

	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}
