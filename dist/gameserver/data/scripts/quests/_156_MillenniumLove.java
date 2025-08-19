package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _156_MillenniumLove extends Quest implements ScriptFile
{
	int LILITHS_LETTER;
	int THEONS_DIARY;
	int GR_COMP_PACKAGE_SS;
	int GR_COMP_PACKAGE_SPS;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _156_MillenniumLove()
	{
		super(false);
		LILITHS_LETTER = 1022;
		THEONS_DIARY = 1023;
		GR_COMP_PACKAGE_SS = 5250;
		GR_COMP_PACKAGE_SPS = 5256;
		this.addStartNpc(30368);
		this.addTalkId(new int[] { 30368 });
		this.addTalkId(new int[] { 30368 });
		this.addTalkId(new int[] { 30368 });
		this.addTalkId(new int[] { 30369 });
		addQuestItem(new int[] { LILITHS_LETTER, THEONS_DIARY });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("quest_accept"))
		{
			htmltext = "rylith_q0156_06.htm";
			st.giveItems(LILITHS_LETTER, 1L);
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("156_1"))
		{
			st.takeItems(LILITHS_LETTER, -1L);
			if(st.getQuestItemsCount(THEONS_DIARY) == 0L)
			{
				st.giveItems(THEONS_DIARY, 1L);
				st.set("cond", "2");
			}
			htmltext = "master_baenedes_q0156_03.htm";
		}
		else if(event.equals("156_2"))
		{
			st.takeItems(LILITHS_LETTER, -1L);
			st.playSound(Quest.SOUND_FINISH);
			htmltext = "master_baenedes_q0156_04.htm";
			st.exitCurrentQuest(false);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(npcId == 30368)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 15)
					htmltext = "rylith_q0156_02.htm";
				else
				{
					htmltext = "rylith_q0156_05.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1 && st.getQuestItemsCount(LILITHS_LETTER) == 1L)
				htmltext = "rylith_q0156_07.htm";
			else if(cond == 2 && st.getQuestItemsCount(THEONS_DIARY) == 1L)
			{
				st.takeItems(THEONS_DIARY, -1L);
				if(st.getPlayer().isMageClass())
					st.giveItems(GR_COMP_PACKAGE_SPS, 1L);
				else
					st.giveItems(GR_COMP_PACKAGE_SS, 1L);
				st.addExpAndSp(3000L, 0L);
				st.playSound(Quest.SOUND_FINISH);
				htmltext = "rylith_q0156_08.htm";
				st.exitCurrentQuest(false);
			}
		}
		else if(npcId == 30369)
			if(cond == 1 && st.getQuestItemsCount(LILITHS_LETTER) == 1L)
				htmltext = "master_baenedes_q0156_02.htm";
			else if(cond == 2 && st.getQuestItemsCount(THEONS_DIARY) == 1L)
				htmltext = "master_baenedes_q0156_05.htm";
		return htmltext;
	}
}
