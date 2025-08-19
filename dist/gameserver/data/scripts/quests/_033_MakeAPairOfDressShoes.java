package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _033_MakeAPairOfDressShoes extends Quest implements ScriptFile
{
	int LEATHER;
	int THREAD;
	int DRESS_SHOES_BOX;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _033_MakeAPairOfDressShoes()
	{
		super(false);
		LEATHER = 1882;
		THREAD = 1868;
		DRESS_SHOES_BOX = 7113;
		this.addStartNpc(30838);
		this.addTalkId(new int[] { 30838 });
		this.addTalkId(new int[] { 30838 });
		this.addTalkId(new int[] { 30164 });
		this.addTalkId(new int[] { 31520 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("30838-1.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("31520-1.htm"))
			st.set("cond", "2");
		else if(event.equals("30838-3.htm"))
			st.set("cond", "3");
		else if(event.equals("30838-5.htm"))
		{
			if(st.getQuestItemsCount(LEATHER) >= 200L && st.getQuestItemsCount(THREAD) >= 600L && st.getQuestItemsCount(57) >= 200000L)
			{
				st.takeItems(LEATHER, 200L);
				st.takeItems(THREAD, 600L);
				st.takeItems(57, 200000L);
				st.set("cond", "4");
			}
			else
				htmltext = "You don't have enough materials";
		}
		else if(event.equals("30164-1.htm"))
		{
			if(st.getQuestItemsCount(57) >= 300000L)
			{
				st.takeItems(57, 300000L);
				st.set("cond", "5");
			}
			else
				htmltext = "30164-havent.htm";
		}
		else if(event.equals("30838-7.htm"))
		{
			st.giveItems(DRESS_SHOES_BOX, 1L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 30838)
		{
			if(cond == 0 && st.getQuestItemsCount(DRESS_SHOES_BOX) == 0L)
			{
				if(st.getPlayer().getLevel() >= 60)
				{
					final QuestState fwear = st.getPlayer().getQuestState(37);
					if(fwear != null && fwear.getCond() == 7)
						htmltext = "30838-0.htm";
					else
						st.exitCurrentQuest(true);
				}
				else
					htmltext = "30838-00.htm";
			}
			else if(cond == 1)
				htmltext = "30838-1.htm";
			else if(cond == 2)
				htmltext = "30838-2.htm";
			else if(cond == 3 && st.getQuestItemsCount(LEATHER) >= 200L && st.getQuestItemsCount(THREAD) >= 600L && st.getQuestItemsCount(57) >= 200000L)
				htmltext = "30838-4.htm";
			else if(cond == 3 && (st.getQuestItemsCount(LEATHER) < 200L || st.getQuestItemsCount(THREAD) < 600L || st.getQuestItemsCount(57) < 200000L))
				htmltext = "30838-4r.htm";
			else if(cond == 4)
				htmltext = "30838-5r.htm";
			else if(cond == 5)
				htmltext = "30838-6.htm";
		}
		else if(npcId == 31520)
		{
			if(cond == 1)
				htmltext = "31520-0.htm";
			else if(cond == 2)
				htmltext = "31520-1r.htm";
		}
		else if(npcId == 30164)
			if(cond == 4)
				htmltext = "30164-0.htm";
			else if(cond == 5)
				htmltext = "30164-2.htm";
		return htmltext;
	}
}
