package npc.model;

import java.util.List;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.model.entity.events.impl.ClanHallTeamBattleEvent;
import l2s.gameserver.model.entity.events.objects.CTBTeamObject;
import l2s.gameserver.model.entity.residence.ClanHall;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.Location;

public class MatchMassTeleporterInstance extends NpcInstance
{
	private int _flagId;
	private long _timeout;

	public MatchMassTeleporterInstance(final int objectId, final NpcTemplate template)
	{
		super(objectId, template);
		_flagId = template.getAIParams().getInteger("flag");
	}

	@Override
	public void showChatWindow(final Player player, final int val, final Object... arg)
	{
		final ClanHall clanHall = getClanHall();
		final ClanHallTeamBattleEvent siegeEvent = (ClanHallTeamBattleEvent) clanHall.getSiegeEvent();
		if(siegeEvent == null) {
			// TODO: Message??
			return;
		}
		if(_timeout > System.currentTimeMillis())
		{
			this.showChatWindow(player, "residence2/clanhall/agit_mass_teleporter001.htm", new Object[0]);
			return;
		}
		if(this.isInRange(player, 150L))
		{
			_timeout = System.currentTimeMillis() + 60000L;
			final List<CTBTeamObject> locs = siegeEvent.getObjects("tryout_part");
			final CTBTeamObject object = locs.get(_flagId);
			if(object.getFlag() != null)
				for(final Player $player : World.getAroundPlayers(this, 400, 100))
					if($player != null)
						$player.teleToLocation(Location.findPointToStay(object.getFlag().getLoc(), 100, 125, $player.getGeoIndex()));
		}
	}
}
