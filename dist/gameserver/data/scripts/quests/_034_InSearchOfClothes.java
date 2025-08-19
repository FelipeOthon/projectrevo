package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _034_InSearchOfClothes extends Quest implements ScriptFile
{
	int SPINNERET;
	int SUEDE;
	int THREAD;
	int SPIDERSILK;
	int MYSTERIOUS_CLOTH;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _034_InSearchOfClothes()
	{
		super(false);
		SPINNERET = 7528;
		SUEDE = 1866;
		THREAD = 1868;
		SPIDERSILK = 1493;
		MYSTERIOUS_CLOTH = 7076;
		this.addStartNpc(30088);
		this.addTalkId(new int[] { 30088 });
		this.addTalkId(new int[] { 30165 });
		this.addTalkId(new int[] { 30294 });
		this.addKillId(new int[] { 20560 });
		addQuestItem(new int[] { SPINNERET });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		final int cond = st.getInt("cond");
		if(event.equals("30088-1.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("30294-1.htm") && cond == 1)
			st.set("cond", "2");
		else if(event.equals("30088-3.htm") && cond == 2)
			st.set("cond", "3");
		else if(event.equals("30165-1.htm") && cond == 3)
			st.set("cond", "4");
		else if(event.equals("30165-3.htm") && cond == 5)
		{
			if(st.getQuestItemsCount(SPINNERET) == 10L)
			{
				st.takeItems(SPINNERET, 10L);
				st.giveItems(SPIDERSILK, 1L);
				st.set("cond", "6");
			}
			else
				htmltext = "30165-1r.htm";
		}
		else if(event.equals("30088-5.htm") && cond == 6)
			if(st.getQuestItemsCount(SUEDE) >= 3000L && st.getQuestItemsCount(THREAD) >= 5000L && st.getQuestItemsCount(SPIDERSILK) == 1L)
			{
				st.takeItems(SUEDE, 3000L);
				st.takeItems(THREAD, 5000L);
				st.takeItems(SPIDERSILK, 1L);
				st.giveItems(MYSTERIOUS_CLOTH, 1L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
			else
				htmltext = "30088-havent.htm";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 30088)
		{
			if(cond == 0 && st.getQuestItemsCount(MYSTERIOUS_CLOTH) == 0L)
			{
				if(st.getPlayer().getLevel() >= 60)
				{
					final QuestState fwear = st.getPlayer().getQuestState(37);
					if(fwear != null && fwear.getInt("cond") == 6)
						htmltext = "30088-0.htm";
					else
						st.exitCurrentQuest(true);
				}
				else
					htmltext = "30088-6.htm";
			}
			else if(cond == 1)
				htmltext = "30088-1r.htm";
			else if(cond == 2)
				htmltext = "30088-2.htm";
			else if(cond == 3)
				htmltext = "30088-3r.htm";
			else if(cond == 6 && (st.getQuestItemsCount(SUEDE) < 3000L || st.getQuestItemsCount(THREAD) < 5000L || st.getQuestItemsCount(SPIDERSILK) < 1L))
				htmltext = "30088-havent.htm";
			else if(cond == 6)
				htmltext = "30088-4.htm";
		}
		else if(npcId == 30294)
		{
			if(cond == 1)
				htmltext = "30294-0.htm";
			else if(cond == 2)
				htmltext = "30294-1r.htm";
		}
		else if(npcId == 30165)
			if(cond == 3)
				htmltext = "30165-0.htm";
			else if(cond == 4 && st.getQuestItemsCount(SPINNERET) < 10L)
				htmltext = "30165-1r.htm";
			else if(cond == 5)
				htmltext = "30165-2.htm";
			else if(cond == 6 && (st.getQuestItemsCount(SUEDE) < 3000L || st.getQuestItemsCount(THREAD) < 5000L || st.getQuestItemsCount(SPIDERSILK) < 1L))
				htmltext = "30165-3r.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getQuestItemsCount(SPINNERET) < 10L)
		{
			st.giveItems(SPINNERET, 1L);
			if(st.getQuestItemsCount(SPINNERET) == 10L)
			{
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "5");
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}
}
