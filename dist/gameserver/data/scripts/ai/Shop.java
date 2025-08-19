package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.CharacterAI;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.skills.Env;
import l2s.gameserver.skills.effects.EffectTemplate;
import l2s.gameserver.tables.SkillTable;

public class Shop extends CharacterAI
{
	public Shop(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{
		final NpcInstance actor = (NpcInstance) getActor();
		if(actor != null && attacker != null && Rnd.chance(25))
		{
			final Skill sk = SkillTable.getInstance().getInfo(1170, 1);
			for(final EffectTemplate et : sk.getEffectTemplates())
			{
				final Env env = new Env(attacker, attacker, sk);
				final Abnormal effect = et.getEffect(env);
				effect.setPeriod(60000L);
				attacker.getAbnormalList().add(effect);
			}
			Functions.npcSay(actor, "\u0414\u0440\u0443\u0433, \u044f \u043d\u0430 \u0442\u0432\u043e\u0435\u0439 \u0441\u0442\u043e\u0440\u043e\u043d\u0435!");
		}
		super.onEvtAttacked(attacker, skill, damage);
	}
}
