package npc.model;

import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.templates.npc.NpcTemplate;

public final class FrintezzaInstance extends NpcInstance
{
	public FrintezzaInstance(final int objectId, final NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void showChatWindow(final Player player, final int val, final Object... arg)
	{}

	@Override
	public void showChatWindow(final Player player, final String filename, final Object... replace)
	{}

	@Override
	public void reduceCurrentHp(final double i, final Creature attacker, final Skill skill, final int poleHitCount, final boolean crit, final boolean awake, final boolean standUp, final boolean directHp, final boolean canReflect, final boolean transferDamage, final boolean isDot, final boolean sendMessage)
	{}

	@Override
	public boolean isInvul()
	{
		return false;
	}

	@Override
	public boolean isFearImmune()
	{
		return true;
	}

	@Override
	public boolean isParalyzeImmune()
	{
		return true;
	}

	@Override
	public boolean isLethalImmune()
	{
		return true;
	}

	@Override
	public Clan getClan()
	{
		return null;
	}
}
