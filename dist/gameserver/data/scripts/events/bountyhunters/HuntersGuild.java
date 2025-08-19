package events.bountyhunters;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.handler.IVoicedCommandHandler;
import l2s.gameserver.handler.VoicedCommandHandler;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.BoxInstance;
import l2s.gameserver.model.instances.ChestInstance;
import l2s.gameserver.model.instances.DeadManInstance;
import l2s.gameserver.model.instances.FestivalMonsterInstance;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.model.instances.RaidBossInstance;
import l2s.gameserver.model.instances.TamedBeastInstance;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.templates.npc.NpcTemplate;
import npc.model.QueenAntLarvaInstance;
import npc.model.SquashInstance;

public class HuntersGuild extends Functions implements ScriptFile, IVoicedCommandHandler
{
	private static final String[] _commandList;

	@Override
	public void onLoad()
	{
		if(!Config.EVENT_BOUNTY_HUNTERS_ENABLED)
			return;
		VoicedCommandHandler.getInstance().registerVoicedCommandHandler(this);
		ScriptFile._log.info("Loaded Event: Bounty Hunters Guild");
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	private static boolean checkTarget(final NpcTemplate npc)
	{
		return npc.isInstanceOf(MonsterInstance.class) && npc.revardExp != 0 && !npc.isInstanceOf(RaidBossInstance.class) && !npc.isInstanceOf(QueenAntLarvaInstance.class) && !npc.isInstanceOf(SquashInstance.class) && !npc.isInstanceOf(FestivalMonsterInstance.class) && !npc.isInstanceOf(TamedBeastInstance.class) && !npc.isInstanceOf(DeadManInstance.class) && !npc.isInstanceOf(BoxInstance.class) && !npc.isInstanceOf(ChestInstance.class) && !npc.title.contains("Quest Monster") && !GameObjectsStorage.getNpcs(false, npc.getId()).isEmpty();
	}

	public void getTask(final Player player, final int id)
	{
		if(!Config.EVENT_BOUNTY_HUNTERS_ENABLED)
			return;
		double mod = 1.0;
		NpcTemplate target;
		if(id == 0)
		{
			final List<NpcTemplate> monsters = NpcTable.getAllOfLevel(player.getLevel());
			if(monsters == null || monsters.isEmpty())
			{
				show(new CustomMessage("scripts.events.bountyhunters.NoTargets"), player);
				return;
			}
			final List<NpcTemplate> targets = new ArrayList<NpcTemplate>();
			for(final NpcTemplate npc : monsters)
				if(checkTarget(npc))
					targets.add(npc);
			if(targets.isEmpty())
			{
				show(new CustomMessage("scripts.events.bountyhunters.NoTargets"), player);
				return;
			}
			target = targets.get(Rnd.get(targets.size()));
		}
		else
		{
			target = NpcTable.getTemplate(id);
			if(target == null || !checkTarget(target))
			{
				show(new CustomMessage("scripts.events.bountyhunters.WrongTarget"), player);
				return;
			}
			if(player.getLevel() - target.level > 5)
			{
				show(new CustomMessage("scripts.events.bountyhunters.TooEasy"), player);
				return;
			}
			mod = 0.5 * (10 + target.level - player.getLevel()) / 10.0;
		}
		final int mobcount = target.level + Rnd.get(25, 50);
		player.setVar("bhMonstersId", String.valueOf(target.getId()));
		player.setVar("bhMonstersNeeded", String.valueOf(mobcount));
		player.setVar("bhMonstersKilled", "0");
		final int fails = player.getVar("bhfails") == null ? 0 : Integer.parseInt(player.getVar("bhfails")) * 5;
		final int success = player.getVar("bhsuccess") == null ? 0 : Integer.parseInt(player.getVar("bhsuccess")) * 5;
		final double reputation = Math.min(Math.max((100 + success - fails) / 100.0, 0.25), 2.0) * mod;
		final long adenarewardvalue = Math.round((target.level * Math.max(Math.log(target.level), 1.0) * 10.0 + Math.max((target.level - 60) * 33, 0) + Math.max((target.level - 65) * 50, 0)) * target.expRate * mobcount * Config.getRateAdena(player) * reputation * 0.15);
		if(Rnd.chance(30))
		{
			player.setVar("bhRewardId", "57");
			player.setVar("bhRewardCount", String.valueOf(adenarewardvalue));
		}
		else
		{
			int crystal = 0;
			if(target.level <= 39)
				crystal = 1458;
			else if(target.level <= 51)
				crystal = 1459;
			else if(target.level <= 60)
				crystal = 1460;
			else if(target.level <= 75)
				crystal = 1461;
			else
				crystal = 1462;
			player.setVar("bhRewardId", String.valueOf(crystal));
			player.setVar("bhRewardCount", String.valueOf(adenarewardvalue / ItemTable.getInstance().getTemplate(crystal).getReferencePrice()));
		}
		show(new CustomMessage("scripts.events.bountyhunters.TaskGiven").addNumber(mobcount).addString(target.name), player);
	}

	public static void OnDie(final Creature cha, final Creature killer)
	{
		if(!Config.EVENT_BOUNTY_HUNTERS_ENABLED)
			return;
		if(cha.isMonster() && !cha.isRaid() && killer != null && killer.getPlayer() != null && killer.getPlayer().getVar("bhMonstersId") != null && Integer.parseInt(killer.getPlayer().getVar("bhMonstersId")) == cha.getNpcId())
		{
			final int count = Integer.parseInt(killer.getPlayer().getVar("bhMonstersKilled")) + 1;
			killer.getPlayer().setVar("bhMonstersKilled", String.valueOf(count));
			final int needed = Integer.parseInt(killer.getPlayer().getVar("bhMonstersNeeded"));
			if(count >= needed)
				doReward(killer.getPlayer());
			else
				sendMessage(new CustomMessage("scripts.events.bountyhunters.NotifyKill").addNumber(needed - count), killer.getPlayer());
		}
	}

	private static void doReward(final Player player)
	{
		if(!Config.EVENT_BOUNTY_HUNTERS_ENABLED)
			return;
		final int rewardid = Integer.parseInt(player.getVar("bhRewardId"));
		final long rewardcount = Long.parseLong(player.getVar("bhRewardCount"));
		player.unsetVar("bhMonstersId");
		player.unsetVar("bhMonstersNeeded");
		player.unsetVar("bhMonstersKilled");
		player.unsetVar("bhRewardId");
		player.unsetVar("bhRewardCount");
		if(player.getVar("bhsuccess") != null)
			player.setVar("bhsuccess", String.valueOf(Integer.parseInt(player.getVar("bhsuccess")) + 1));
		else
			player.setVar("bhsuccess", "1");
		addItem(player, rewardid, rewardcount);
		show(new CustomMessage("scripts.events.bountyhunters.TaskCompleted").addNumber(rewardcount).addItemName(rewardid), player);
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return HuntersGuild._commandList;
	}

	@Override
	public boolean useVoicedCommand(final String command, final Player activeChar, final String target)
	{
		if(activeChar == null || !Config.EVENT_BOUNTY_HUNTERS_ENABLED)
			return false;
		if(activeChar.getLevel() < 20)
		{
			sendMessage(new CustomMessage("scripts.events.bountyhunters.TooLowLevel"), activeChar);
			return true;
		}
		if(command.equalsIgnoreCase("gettask"))
		{
			if(activeChar.getVar("bhMonstersId") != null)
			{
				final int mobid = Integer.parseInt(activeChar.getVar("bhMonstersId"));
				final int mobcount = Integer.parseInt(activeChar.getVar("bhMonstersNeeded")) - Integer.parseInt(activeChar.getVar("bhMonstersKilled"));
				show(new CustomMessage("scripts.events.bountyhunters.TaskGiven").addNumber(mobcount).addString(NpcTable.getTemplate(mobid).name), activeChar);
				return true;
			}
			int id = 0;
			if(target != null && target.trim().matches("[\\d]{1,9}"))
				id = Integer.parseInt(target);
			getTask(activeChar, id);
			return true;
		}
		else
		{
			if(!command.equalsIgnoreCase("declinetask"))
				return false;
			if(activeChar.getVar("bhMonstersId") == null)
			{
				sendMessage(new CustomMessage("scripts.events.bountyhunters.NoTask"), activeChar);
				return true;
			}
			activeChar.unsetVar("bhMonstersId");
			activeChar.unsetVar("bhMonstersNeeded");
			activeChar.unsetVar("bhMonstersKilled");
			activeChar.unsetVar("bhRewardId");
			activeChar.unsetVar("bhRewardCount");
			if(activeChar.getVar("bhfails") != null)
				activeChar.setVar("bhfails", String.valueOf(Integer.parseInt(activeChar.getVar("bhfails")) + 1));
			else
				activeChar.setVar("bhfails", "1");
			show(new CustomMessage("scripts.events.bountyhunters.TaskCanceled"), activeChar);
			return true;
		}
	}

	static
	{
		_commandList = new String[] { "gettask", "declinetask" };
	}
}
