package npc.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import bosses.FourSepulchersManager;
import bosses.FourSepulchersSpawn;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.s2c.NpcHtmlMessage;
import l2s.gameserver.network.l2.s2c.NpcSay;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.templates.npc.NpcTemplate;

public class SepulcherNpcInstance extends NpcInstance
{
	protected static Map<Integer, Integer> _hallGateKeepers;
	protected Future<?> _closeTask;
	protected Future<?> _spawnMonsterTask;
	private static final String HTML_FILE_PATH = "SepulcherNpc/";
	private static final int CHAPEL_KEY = 7260;

	public SepulcherNpcInstance(final int objectID, final NpcTemplate template)
	{
		super(objectID, template);
		_closeTask = null;
		_spawnMonsterTask = null;
		if(_closeTask != null)
			_closeTask.cancel(false);
		if(_spawnMonsterTask != null)
			_spawnMonsterTask.cancel(false);
		_closeTask = null;
		_spawnMonsterTask = null;
	}

	@Override
	public void deleteMe()
	{
		if(_closeTask != null)
		{
			_closeTask.cancel(false);
			_closeTask = null;
		}
		if(_spawnMonsterTask != null)
		{
			_spawnMonsterTask.cancel(false);
			_spawnMonsterTask = null;
		}
		super.deleteMe();
	}

	@Override
	public void showChatWindow(Player player, final int val, final Object... arg)
	{
		if(isDead())
		{
			player.sendActionFailed();
			return;
		}
		switch(getNpcId())
		{
			case 31468:
			case 31469:
			case 31470:
			case 31471:
			case 31472:
			case 31473:
			case 31474:
			case 31475:
			case 31476:
			case 31477:
			case 31478:
			case 31479:
			case 31480:
			case 31481:
			case 31482:
			case 31483:
			case 31484:
			case 31485:
			case 31486:
			case 31487:
			{
				doDie(player);
				if(_spawnMonsterTask != null)
					_spawnMonsterTask.cancel(false);
				_spawnMonsterTask = ThreadPoolManager.getInstance().schedule(new SpawnMonster(getNpcId()), 3500L);
				break;
			}
			case 31455:
			case 31456:
			case 31457:
			case 31458:
			case 31459:
			case 31460:
			case 31461:
			case 31462:
			case 31463:
			case 31464:
			case 31465:
			case 31466:
			case 31467:
			{
				doDie(player);
				if(player.getParty() != null && !player.getParty().isLeader(player))
					player = player.getParty().getPartyLeader();
				Functions.addItem(player, 7260, 1L);
				break;
			}
			default:
			{
				super.showChatWindow(player, val, arg);
				break;
			}
		}
	}

	@Override
	public String getHtmlPath(final int npcId, final int val, final Player player)
	{
		String pom = "";
		if(val == 0)
			pom = "" + npcId;
		else
			pom = npcId + "-" + val;
		return "SepulcherNpc/" + pom + ".htm";
	}

	@Override
	public void onBypassFeedback(final Player player, final String command)
	{
		if(command.startsWith("open_gate"))
		{
			ItemInstance hallsKey = player.getInventory().getItemByItemId(7260);
			if(hallsKey == null)
				showHtmlFile(player, "Gatekeeper-no.htm");
			else if(FourSepulchersManager.isAttackTime())
			{
				if(player.getParty() != null)
					for(final Player mem : player.getParty().getPartyMembers())
					{
						hallsKey = mem.getInventory().getItemByItemId(7260);
						if(hallsKey != null)
							Functions.removeItem(mem, 7260, hallsKey.getCount());
					}
				else
					Functions.removeItem(player, 7260, hallsKey.getCount());
				final Zone zone = FourSepulchersManager.getZone(player);
				if(zone != null)
					for(final GameObject obj : zone.getInsideObjectsIncludeZ())
						if(obj.isItem() && ((ItemInstance) obj).getItemId() == 7260)
							obj.deleteMe();
				switch(getNpcId())
				{
					case 31929:
					case 31934:
					case 31939:
					case 31944:
					{
						if(FourSepulchersSpawn.isShadowAlive(getNpcId()) || FourSepulchersSpawn._shadows.contains(getNpcId()))
						{
							player.sendMessage("Already in farm.");
							player.sendActionFailed();
							return;
						}
						FourSepulchersSpawn._shadows.add(getNpcId());
						if(Config.SHADOW_SPAWN_DELAY > 0)
						{
							final int npcId = getNpcId();
							ThreadPoolManager.getInstance().schedule(new Runnable(){
								@Override
								public void run()
								{
									if(!FourSepulchersSpawn.isShadowAlive(npcId))
										FourSepulchersSpawn.spawnShadow(npcId);
								}
							}, Config.SHADOW_SPAWN_DELAY * 1000L);
							break;
						}
						FourSepulchersSpawn.spawnShadow(getNpcId());
						break;
					}
				}
				openNextDoor(getNpcId());
			}
		}
		else
			super.onBypassFeedback(player, command);
	}

	public void openNextDoor(final int npcId)
	{
		final FourSepulchersSpawn.GateKeeper gk = FourSepulchersManager.getHallGateKeeper(npcId);
		if(gk.door.isOpen())
			return;
		gk.door.openMe();
		if(_closeTask != null)
			_closeTask.cancel(false);
		_closeTask = ThreadPoolManager.getInstance().schedule(new CloseNextDoor(gk), 10000L);
	}

	public void sayInShout(final String msg, final String i)
	{
		if(msg == null || msg.isEmpty())
			return;
		for(final Player player : GameObjectsStorage.getPlayers())
			if(player != null && this.isInRange(player, 5000L))
				player.sendPacket(new NpcSay(this, 1, new CustomMessage(msg).addString(i.equals("90") ? "" : i).toString(player)));
	}

	public void showHtmlFile(final Player player, final String file)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		html.setFile("SepulcherNpc/" + file);
		html.replace("%npcname%", getName());
		player.sendPacket(html);
	}

	@Override
	public Clan getClan()
	{
		return null;
	}

	static
	{
		SepulcherNpcInstance._hallGateKeepers = new HashMap<Integer, Integer>();
	}

	private class CloseNextDoor implements Runnable
	{
		private final FourSepulchersSpawn.GateKeeper _gk;
		private int state;

		public CloseNextDoor(final FourSepulchersSpawn.GateKeeper gk)
		{
			state = 0;
			_gk = gk;
		}

		@Override
		public void run()
		{
			if(state == 0)
			{
				try
				{
					_gk.door.closeMe();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				++state;
				_closeTask = ThreadPoolManager.getInstance().schedule(this, 10000L);
			}
			else if(state == 1)
			{
				FourSepulchersSpawn.spawnMysteriousBox(_gk.template.npcId);
				_closeTask = null;
			}
		}
	}

	private class SpawnMonster implements Runnable
	{
		private final int _NpcId;

		public SpawnMonster(final int npcId)
		{
			_NpcId = npcId;
		}

		@Override
		public void run()
		{
			FourSepulchersSpawn.spawnMonster(_NpcId, "showChatWindow");
		}
	}
}
