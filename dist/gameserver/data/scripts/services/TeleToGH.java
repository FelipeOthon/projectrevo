package services;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

import l2s.gameserver.Config;
import l2s.gameserver.instancemanager.ZoneManager;
import l2s.gameserver.listener.ZoneEnterLeaveListener;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Spawn;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.utils.Util;

public class TeleToGH extends Functions implements ScriptFile
{
	public class ZoneListener extends ZoneEnterLeaveListener
	{
		@Override
		public void objectEntered(final Zone zone, final GameObject object)
		{
			//
		}

		@Override
		public void objectLeaved(final Zone zone, final GameObject object)
		{
			if(Config.SERVICES_GIRAN_HARBOR_ENABLED)
			{
				final Player player = object.getPlayer();
				if(player != null && player.isVisible())
				{
					final Playable playable = (Playable) object;
					final double angle = Util.convertHeadingToDegree(playable.getHeading());
					final double radian = Math.toRadians(angle - 90.0);
					playable.teleToLocation((int) (playable.getX() + 50.0 * Math.sin(radian)), (int) (playable.getY() - 50.0 * Math.cos(radian)), playable.getZ());
				}
			}
		}
	}

	private static ArrayList<Spawn> _spawns = new ArrayList<Spawn>();
	private static int[] npcIds = new int[] { 30059, 30080, 30177, 30233, 30256, 30320, 30848, 30899, 31210, 31275, 31320, 31964 };

	private Zone _zone;
	private ZoneListener _zoneListener;

	@Override
	public void onLoad()
	{
		if(Config.SERVICES_GIRAN_HARBOR_ENABLED)
		{
			try
			{
				final Spawn sp1 = new Spawn(NpcTable.getTemplate(30086));
				sp1.setLocx(48059);
				sp1.setLocy(186791);
				sp1.setLocz(-3512);
				sp1.setAmount(1);
				sp1.setHeading(42000);
				sp1.setRespawnDelay(5);
				sp1.init();
				TeleToGH._spawns.add(sp1);
				final Spawn sp2 = new Spawn(NpcTable.getTemplate(30081));
				sp2.setLocx(48146);
				sp2.setLocy(186753);
				sp2.setLocz(-3512);
				sp2.setAmount(1);
				sp2.setHeading(42000);
				sp2.setRespawnDelay(5);
				sp2.init();
				TeleToGH._spawns.add(sp2);
				final Spawn sp3 = new Spawn(NpcTable.getTemplate(31860));
				sp3.setLocx(48129);
				sp3.setLocy(186828);
				sp3.setLocz(-3512);
				sp3.setAmount(1);
				sp3.setHeading(45452);
				sp3.setRespawnDelay(5);
				sp3.init();
				TeleToGH._spawns.add(sp3);
				final Spawn sp4 = new Spawn(NpcTable.getTemplate(30300));
				sp4.setLocx(48102);
				sp4.setLocy(186772);
				sp4.setLocz(-3512);
				sp4.setAmount(1);
				sp4.setHeading(42000);
				sp4.setRespawnDelay(5);
				sp4.init();
				TeleToGH._spawns.add(sp4);
			}
			catch(SecurityException e)
			{
				e.printStackTrace();
			}
			catch(ClassNotFoundException e2)
			{
				e2.printStackTrace();
			}
			_zoneListener = new ZoneListener();
			_zone = ZoneManager.getInstance().getZoneById(Zone.ZoneType.offshore, 500014, false);
			_zone.getListenerEngine().addMethodInvokedListener(_zoneListener);
			ZoneManager.getInstance().getZoneById(Zone.ZoneType.offshore, 500014, false).setActive(true);
			ZoneManager.getInstance().getZoneById(Zone.ZoneType.peace_zone, 500023, false).setActive(true);
			ZoneManager.getInstance().getZoneById(Zone.ZoneType.dummy, 500024, false).setActive(true);
			ScriptFile._log.info("Loaded Service: Teleport to Giran Harbor");
		}
	}

	@Override
	public void onReload()
	{
		_zone.getListenerEngine().removeMethodInvokedListener(_zoneListener);
		for(final Spawn spawn : TeleToGH._spawns)
			spawn.despawnAll();
		TeleToGH._spawns.clear();
	}

	@Override
	public void onShutdown()
	{
		//
	}

	public void toGH()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final NpcInstance npc = getNpc();
		if(!NpcInstance.canBypassCheck(player, npc) || !ArrayUtils.contains(TeleToGH.npcIds, npc.getNpcId()))
			return;
		player.setVar("backCoords", player.getX() + " " + player.getY() + " " + player.getZ());
		player.teleToLocation(47416, 186568, -3480);
	}

	public void fromGH()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final NpcInstance npc = getNpc();
		if(!NpcInstance.canBypassCheck(player, npc) || npc.getNpcId() != 30878)
			return;
		final String var = player.getVar("backCoords");
		if(var == null || var.equals(""))
		{
			teleOut(player);
			return;
		}
		final String[] coords = var.split(" ");
		if(coords.length < 3)
		{
			teleOut(player);
			return;
		}
		player.teleToLocation(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));
	}

	private static void teleOut(final Player player)
	{
		player.teleToLocation(46776, 185784, -3528);
		if(!player.isLangRus())
			show("I don't know from where you came here, but I can teleport you the another border side.", player);
		else
			show("Я не знаю, как Вы попали сюда, но я могу Вас отправить за ограждение.", player);
	}
}
