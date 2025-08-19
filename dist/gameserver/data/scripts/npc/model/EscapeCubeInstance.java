package npc.model;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.s2c.NpcHtmlMessage;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.Location;

public class EscapeCubeInstance extends NpcInstance
{
	private static final Location[] baiumTeleOut;

	public EscapeCubeInstance(final int objectId, final NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(final Player player, final String command)
	{
		if(!canBypassCheck(player, this))
			return;
		if(player.getZ() > 9990)
		{
			final Location loc = EscapeCubeInstance.baiumTeleOut[Rnd.get(EscapeCubeInstance.baiumTeleOut.length)];
			player.teleToLocation(loc.x + Rnd.get(-100, 100), loc.y + Rnd.get(-100, 100), loc.z);
		}
		else
			player.teleToLocation(150037 + Rnd.get(-250, 250), -57720 + Rnd.get(-250, 250), -2976);
	}

	@Override
	public void showChatWindow(final Player player, final int val, final Object... replace)
	{
		final boolean toi = player.getZ() > 9990;
		final String text = "<html><body>" + (toi ? "Teleportation Cubic" : "Teleport Cube") + ":<br><br><a action=\"bypass -h npc_" + getObjectId() + "_exit\">" + (toi ? "Go above ground" : "Leave Valakas's Nest") + ".</a></body></html>";
		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setHtml(text);
		player.sendPacket(html);
	}

	@Override
	public Clan getClan()
	{
		return null;
	}

	static
	{
		baiumTeleOut = new Location[] { new Location(108784, 16000, -4928), new Location(113824, 10448, -5164), new Location(115488, 22096, -5168) };
	}
}
