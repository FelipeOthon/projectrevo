package npc.model;

import java.util.concurrent.Future;

import l2s.commons.lang.reference.HardReference;
import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.NpcHtmlMessage;
import l2s.gameserver.network.l2.s2c.PlaySound;
import l2s.gameserver.network.l2.s2c.SocialAction;
import l2s.gameserver.network.l2.s2c.SpecialCamera;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.Location;

public class DrChaosInstance extends NpcInstance
{
	public static int status = 2;
	private static long _lastAttackVsGolem = 0L;
	private static int _pissedOffTimer;
	public static boolean first = true;
	private static boolean canspawn = false;
	public static int GOLEM = 0;
	private static int sd = 0;
	private static Future<?> _activity;
	private static Future<?> _despawn;

	public DrChaosInstance(final int objectId, final NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void spawnMe()
	{
		super.spawnMe();
		sd = getObjectId();
		if(!canspawn)
			decayMe();
	}

	@Override
	public void showChatWindow(final Player player, final int val, final Object... replace)
	{
		String htmltext = null;
		if(status == 0)
		{
			_pissedOffTimer -= 1 + Rnd.get(5);
			if(_pissedOffTimer > 20 && _pissedOffTimer <= 30)
				htmltext = "<html><body>Doctor Chaos:<br>What?! Who are you? How did you come here?<br>You really look suspicious... Aren't those filthy members of Black Anvil guild send you? No? Mhhhhh... I don't trust you!</body></html>";
			else if(_pissedOffTimer > 10 && _pissedOffTimer <= 20)
				htmltext = "<html><body>Doctor Chaos:<br>Why are you standing here? Don't you see it's a private propertie? Don't look at him with those eyes... Did you smile?! Don't make fun of me! He will ... destroy ... you ... if you continue!</body></html>";
			else if(_pissedOffTimer > 0 && _pissedOffTimer <= 10)
				htmltext = "<html><body>Doctor Chaos:<br>I know why you are here, traitor! He discovered your plans! You are assassin ... sent by the Black Anvil guild! But you won't kill the Emperor of Evil!</body></html>";
			else if(_pissedOffTimer <= 0)
				crazyMidgetBecomesAngry(this);
		}
		if(htmltext != null)
			player.sendPacket(new NpcHtmlMessage(player, this, htmltext, val));
		else
			player.sendActionFailed();
	}

	private static void crazyMidgetBecomesAngry(final NpcInstance npc)
	{
		if(status == 0)
		{
			status = 1;
			if(_activity != null)
			{
				_activity.cancel(false);
				_activity = null;
			}
			npc.moveToLocation(new Location(96323, -110914, -3328, 0), 0, false);
			Functions.npcSay(npc, "Fools! Why haven't you fled yet? Prepare to learn a lesson!");
			startTimer("1", 2000, npc);
			startTimer("2", 4000, npc);
			startTimer("3", 6500, npc);
			startTimer("4", 12500, npc);
			startTimer("5", 17000, npc);
		}
	}

	public static void reset()
	{
		status = 0;
		canspawn = true;
		final NpcInstance npc = GameObjectsStorage.getNpc(sd);
		if(npc == null)
			return;
		_pissedOffTimer = 30;
		npc.spawnMe(new Location(96320, -110912, -3328, 8191));
		startTimer("paranoia_activity", 1000, npc);
	}

	private static void startTimer(final String event, final int time, final NpcInstance npc)
	{
		if(event.equals("golem_despawn"))
		{
			if(_despawn != null)
				_despawn.cancel(false);
			_despawn = ThreadPoolManager.getInstance().scheduleAtFixedRate(new TimerTask(event, npc), time, time);
		}
		else if(event.equals("paranoia_activity"))
		{
			if(_activity != null)
				_activity.cancel(false);
			_activity = ThreadPoolManager.getInstance().scheduleAtFixedRate(new TimerTask(event, npc), time, time);
		}
		else
			ThreadPoolManager.getInstance().schedule(new TimerTask(event, npc), time);
	}

	private static void doTask(final String event, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("golem_despawn"))
		{
			if(npc.getNpcId() == 25512 && !npc.isDead() && _lastAttackVsGolem + 1800000L < System.currentTimeMillis())
			{
				npc.block();
				npc.decayMe();
				reset();
				stopDespawn();
			}
		}
		else if(event.equalsIgnoreCase("1"))
		{
			npc.broadcastPacket(new L2GameServerPacket[] { new SocialAction(npc.getObjectId(), 2) });
			npc.broadcastPacket(new L2GameServerPacket[] { new SpecialCamera(npc.getObjectId(), 1, -200, 15, 5500, 13500, 0, 0, 1, 0) });
		}
		else if(event.equalsIgnoreCase("2"))
			npc.broadcastPacket(new L2GameServerPacket[] { new SocialAction(npc.getObjectId(), 3) });
		else if(event.equalsIgnoreCase("3"))
			npc.broadcastPacket(new L2GameServerPacket[] { new SocialAction(npc.getObjectId(), 1) });
		else if(event.equalsIgnoreCase("4"))
		{
			npc.broadcastPacket(new L2GameServerPacket[] { new SpecialCamera(npc.getObjectId(), 1, -150, 10, 3500, 5000, 0, 0, 1, 0) });
			npc.moveToLocation(new Location(95928, -110671, -3340, 0), 0, false);
		}
		else if(event.equalsIgnoreCase("5"))
		{
			npc.decayMe();
			final NpcInstance golem = GameObjectsStorage.getNpc(GOLEM);
			if(golem == null)
				return;
			first = false;
			golem.setCurrentHpMp(golem.getMaxHp(), golem.getMaxMp(), true);
			golem.unblock();
			golem.spawnMe(new Location(96080, -110822, -3343, 0));
			golem.broadcastPacket(new L2GameServerPacket[] { new SpecialCamera(golem.getObjectId(), 30, 200, 20, 6000, 8000, 0, 0, 1, 0) });
			golem.broadcastPacket(new L2GameServerPacket[] { new SocialAction(golem.getObjectId(), 1) });
			golem.broadcastPacket(new L2GameServerPacket[] { new PlaySound(1, "Rm03_A", 0, 0, new Location(0, 0, 0)) });
			_lastAttackVsGolem = System.currentTimeMillis();
			startTimer("golem_despawn", 60000, golem);
		}
		else if(event.equalsIgnoreCase("paranoia_activity") && status == 0)
			for(final Player player : World.getAroundPlayers(npc, 500, 400))
				if(player != null)
				{
					if(player.isDead())
						continue;
					--_pissedOffTimer;
					if(_pissedOffTimer == 15)
						Functions.npcSay(npc, "How dare you trespass into my territory! Have you no fear?");
					if(_pissedOffTimer > 0)
						continue;
					crazyMidgetBecomesAngry(npc);
				}
	}

	public static void stopDespawn()
	{
		if(_despawn == null)
			return;
		_despawn.cancel(false);
		_despawn = null;
	}

	@Override
	public Clan getClan()
	{
		return null;
	}

	private static class TimerTask implements Runnable
	{
		private final String _event;
		private final HardReference<NpcInstance> _npcRef;

		public TimerTask(final String event, final NpcInstance npc)
		{
			_event = event;
			_npcRef = npc.getRef();
		}

		@Override
		public void run()
		{
			final NpcInstance npc = _npcRef.get();
			if(npc == null)
				return;

			doTask(_event, npc);
		}
	}
}
