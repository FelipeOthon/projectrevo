package events.Christmas;

import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.tables.SkillTable;

public class ctreeAI extends DefaultAI
{
	public ctreeAI(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected boolean thinkActive()
	{
		final int skillId = 2139;
		final NpcInstance actor = getActor();
		if(actor == null)
			return true;
		for(final Player player : World.getAroundPlayers(actor, 200, 200))
			if(player != null && player.getAbnormalList().getEffectsBySkillId(skillId) == null)
				actor.doCast(SkillTable.getInstance().getInfo(skillId, 1), player, true);
		return false;
	}

	@Override
	protected boolean randomAnimation()
	{
		return false;
	}

	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}
