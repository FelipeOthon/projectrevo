package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _157_RecoverSmuggled extends Quest implements ScriptFile
{
	int ADAMANTITE_ORE_ID;
	int BUCKLER;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _157_RecoverSmuggled()
	{
		super(false);
		ADAMANTITE_ORE_ID = 1024;
		BUCKLER = 20;
		this.addStartNpc(30005);
		this.addTalkId(new int[] { 30005 });
		this.addKillId(new int[] { 20121 });
		addQuestItem(new int[] { ADAMANTITE_ORE_ID });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("quest_accept"))
		{
			st.set("id", "0");
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
			htmltext = "wilph_q0157_05.htm";
		}
		else if(event.equals("157_1"))
		{
			htmltext = "wilph_q0157_04.htm";
			return htmltext;
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int id = st.getState();
		if(id == 1)
		{
			st.set("cond", "0");
			st.set("id", "0");
		}
		if(npcId == 30005 && st.getInt("cond") == 0)
		{
			if(st.getInt("cond") < 15)
			{
				if(st.getPlayer().getLevel() >= 5)
					htmltext = "wilph_q0157_03.htm";
				else
				{
					htmltext = "wilph_q0157_02.htm";
					st.exitCurrentQuest(true);
				}
			}
			else
			{
				htmltext = "wilph_q0157_02.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(npcId == 30005 && st.getInt("cond") != 0 && st.getQuestItemsCount(ADAMANTITE_ORE_ID) < 20L)
			htmltext = "wilph_q0157_06.htm";
		else if(npcId == 30005 && st.getInt("cond") != 0 && st.getQuestItemsCount(ADAMANTITE_ORE_ID) >= 20L)
		{
			st.takeItems(ADAMANTITE_ORE_ID, st.getQuestItemsCount(ADAMANTITE_ORE_ID));
			st.playSound(Quest.SOUND_FINISH);
			st.giveItems(BUCKLER, 1L);
			htmltext = "wilph_q0157_07.htm";
			st.exitCurrentQuest(false);
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		if(npcId == 20121)
		{
			st.set("id", "0");
			if(st.getInt("cond") != 0 && st.getQuestItemsCount(ADAMANTITE_ORE_ID) < 20L && Rnd.chance(14))
			{
				st.giveItems(ADAMANTITE_ORE_ID, 1L);
				if(st.getQuestItemsCount(ADAMANTITE_ORE_ID) == 20L)
					st.playSound(Quest.SOUND_MIDDLE);
				else
					st.playSound(Quest.SOUND_ITEMGET);
			}
		}
		return null;
	}
}
