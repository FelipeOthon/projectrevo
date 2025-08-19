package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.World;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.utils.Location;

public class AndreasCaptainRoyalGuard extends Fighter
{
	private boolean _tele;
	public static final Location[] locs;

	public AndreasCaptainRoyalGuard(final NpcInstance actor)
	{
		super(actor);
		_tele = true;
	}

	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if(actor == null)
			return true;
		for(final Player player : World.getAroundPlayers(actor, 500, 500))
			if(player != null)
			{
				if(!player.isInParty())
					continue;
				if(player.getParty().getMemberCount() < 9 || !_tele)
					continue;
				_tele = false;
				player.teleToLocation((Location) Rnd.get((Object[]) AndreasCaptainRoyalGuard.locs));
			}
		return true;
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{
		final NpcInstance actor = getActor();
		if(actor.getCurrentHpPercents() <= 70.0)
		{
			actor.doCast(SkillTable.getInstance().getInfo(4612, 9), attacker, true);
			actor.doDie(attacker);
		}
		super.onEvtAttacked(attacker, skill, damage);
	}

	@Override
	protected void onEvtDead(final Creature killer)
	{
		_tele = true;
		AndreasVanHalter.action();
		super.onEvtDead(killer);
	}

	static
	{
		locs = new Location[] { new Location(-16128, -35888, -10726), new Location(-17029, -39617, -10724), new Location(-15729, -42001, -10724) };
	}
}
