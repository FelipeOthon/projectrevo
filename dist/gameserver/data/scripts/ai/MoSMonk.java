package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.CtrlIntention;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;

public class MoSMonk extends Fighter
{
	public MoSMonk(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onIntentionAttack(final Creature target)
	{
		final NpcInstance actor = getActor();
		if(actor == null)
			return;
		if(target != null && target.isPlayer() && target.getActiveWeaponInstance() != null && getIntention() == CtrlIntention.AI_INTENTION_ACTIVE && Rnd.chance(20))
			Functions.npcSayCustomMessage(actor, "scripts.ai.MoSMonk.onIntentionAttack", new Object[0]);
		super.onIntentionAttack(target);
	}

	@Override
	public boolean checkAggression(final Creature target)
	{
		return target.getActiveWeaponInstance() != null && super.checkAggression(target);
	}
}
