package ai;

import java.util.List;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.ai.Mystic;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.tables.SkillTable;

public class HotSpringsMob extends Mystic
{
	public HotSpringsMob(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{
		if(Rnd.chance(10))
			debuff(attacker, getActor());
		super.onEvtAttacked(attacker, skill, damage);
	}

	@Override
	protected void onEvtSeeSpell(final Skill skill, final Creature caster)
	{
		if(skill.isOffensive() || skill.isHandler())
			return;
		if(Rnd.chance(10))
			debuff(caster, getActor());
	}

	private void debuff(final Creature attacker, final NpcInstance actor)
	{
		if(actor != null && attacker != null && !actor.isDead() && !attacker.isAlikeDead() && !attacker.isInvul() && getAttackTarget() == attacker && attacker.isInRange(actor, 600L))
		{
			final int[] DeBuffs = new int[4];
			final int id = actor.getNpcId();
			int n = 0;
			for(int i = 0; i < Config.HS_DISEASE.length; i += 2)
				if(id == Config.HS_DISEASE[i])
				{
					DeBuffs[n] = Config.HS_DISEASE[i + 1];
					++n;
				}
			final int DeBuff = DeBuffs[Rnd.get(n)];
			final List<Abnormal> effect = attacker.getAbnormalList().getEffectsBySkillId(DeBuff);
			if(effect != null)
			{
				final int level = effect.get(0).getSkill().getLevel();
				if(level < 10)
				{
					effect.get(0).exit();
					final Skill skill = SkillTable.getInstance().getInfo(DeBuff, level + 1);
					skill.getEffects(actor, attacker, false, false);
				}
			}
			else
			{
				final Skill skill2 = SkillTable.getInstance().getInfo(DeBuff, 1);
				skill2.getEffects(actor, attacker, false, false);
			}
		}
	}

	@Override
	public int getRateDAM()
	{
		return 5;
	}

	@Override
	public int getRateSTUN()
	{
		return 3;
	}
}
