package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.Mystic;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.utils.Location;

public class TriolsBeliever extends Mystic
{
	private boolean _tele;
	public static final Location[] locs;

	public TriolsBeliever(final NpcInstance actor)
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
				if(player.getParty().getMemberCount() < 5 || !_tele)
					continue;
				_tele = false;
				player.teleToLocation((Location) Rnd.get((Object[]) TriolsBeliever.locs));
			}
		return true;
	}

	@Override
	protected void onEvtDead(final Creature killer)
	{
		_tele = true;
		super.onEvtDead(killer);
	}

	static
	{
		locs = new Location[] { new Location(-16128, -35888, -10726), new Location(-16397, -44970, -10724), new Location(-15729, -42001, -10724) };
	}
}
