package quests;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.Spawn;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.SpawnTable;

public class _653_WildMaiden extends Quest implements ScriptFile
{
	public final int SUKI = 32013;
	public final int GALIBREDO = 30181;
	public final int SOE = 736;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _653_WildMaiden()
	{
		super(false);
		this.addStartNpc(32013);
		this.addTalkId(new int[] { 32013 });
		this.addTalkId(new int[] { 30181 });
	}

	private NpcInstance findNpc(final int npcId, final Player player)
	{
		NpcInstance instance = null;
		final List<NpcInstance> npclist = new ArrayList<NpcInstance>();
		for(final Spawn spawn : SpawnTable.getInstance().getSpawnTable())
			if(spawn.getNpcId() == npcId)
			{
				instance = spawn.getLastSpawn();
				npclist.add(instance);
			}
		for(final NpcInstance npc : npclist)
			if(player.isInRange(npc, 1600L))
				return npc;
		return instance;
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		final Player player = st.getPlayer();
		if(event.equalsIgnoreCase("spring_girl_sooki_q0653_03.htm"))
		{
			if(st.getQuestItemsCount(736) > 0L)
			{
				st.set("cond", "1");
				st.setState(2);
				st.playSound(Quest.SOUND_ACCEPT);
				st.takeItems(736, 1L);
				htmltext = "spring_girl_sooki_q0653_04a.htm";
				final NpcInstance n = findNpc(32013, player);
				n.broadcastPacket(new L2GameServerPacket[] { new MagicSkillUse(n, n, 2013, 1, 20000, 0L) });
				st.startQuestTimer("suki_timer", 20000L);
			}
		}
		else if(event.equalsIgnoreCase("spring_girl_sooki_q0653_03.htm"))
		{
			st.exitCurrentQuest(false);
			st.playSound(Quest.SOUND_GIVEUP);
		}
		else if(event.equalsIgnoreCase("suki_timer"))
		{
			final NpcInstance n = findNpc(32013, player);
			n.deleteMe();
			htmltext = null;
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int id = st.getState();
		if(npcId == 32013 && id == 1)
		{
			if(st.getPlayer().getLevel() >= 36)
				htmltext = "spring_girl_sooki_q0653_01.htm";
			else
			{
				htmltext = "spring_girl_sooki_q0653_01a.htm";
				st.exitCurrentQuest(false);
			}
		}
		else if(npcId == 30181 && st.getInt("cond") == 1)
		{
			htmltext = "galicbredo_q0653_01.htm";
			st.giveItems(57, 2883L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return htmltext;
	}
}
