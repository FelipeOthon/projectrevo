package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _035_FindGlitteringJewelry extends Quest implements ScriptFile
{
	int ROUGH_JEWEL;
	int ORIHARUKON;
	int SILVER_NUGGET;
	int THONS;
	int JEWEL_BOX;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _035_FindGlitteringJewelry()
	{
		super(false);
		ROUGH_JEWEL = 7162;
		ORIHARUKON = 1893;
		SILVER_NUGGET = 1873;
		THONS = 4044;
		JEWEL_BOX = 7077;
		this.addStartNpc(30091);
		this.addTalkId(new int[] { 30091 });
		this.addTalkId(new int[] { 30879 });
		this.addKillId(new int[] { 20135 });
		addQuestItem(new int[] { ROUGH_JEWEL });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		final int cond = st.getInt("cond");
		if(event.equals("30091-1.htm") && cond == 0)
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("30879-1.htm") && cond == 1)
			st.set("cond", "2");
		else if(event.equals("30091-3.htm") && cond == 3)
		{
			if(st.getQuestItemsCount(ROUGH_JEWEL) == 10L)
			{
				st.takeItems(ROUGH_JEWEL, -1L);
				st.set("cond", "4");
			}
			else
				htmltext = "30091-hvnore.htm";
		}
		else if(event.equals("30091-5.htm") && cond == 4)
			if(st.getQuestItemsCount(ORIHARUKON) >= 5L && st.getQuestItemsCount(SILVER_NUGGET) >= 500L && st.getQuestItemsCount(THONS) >= 150L)
			{
				st.takeItems(ORIHARUKON, 5L);
				st.takeItems(SILVER_NUGGET, 500L);
				st.takeItems(THONS, 150L);
				st.giveItems(JEWEL_BOX, 1L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
			else
				htmltext = "30091-hvnmat-bug.htm";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 30091)
		{
			if(cond == 0 && st.getQuestItemsCount(JEWEL_BOX) == 0L)
			{
				if(st.getPlayer().getLevel() >= 60)
				{
					final QuestState fwear = st.getPlayer().getQuestState(37);
					if(fwear != null && fwear.getCond() == 6)
						htmltext = "30091-0.htm";
					else
						st.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "30091-6.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "30091-1r.htm";
			else if(cond == 2)
				htmltext = "30091-1r2.htm";
			else if(cond == 3 && st.getQuestItemsCount(ROUGH_JEWEL) == 10L)
				htmltext = "30091-2.htm";
			else if(cond == 4 && (st.getQuestItemsCount(ORIHARUKON) < 5L || st.getQuestItemsCount(SILVER_NUGGET) < 500L || st.getQuestItemsCount(THONS) < 150L))
				htmltext = "30091-hvnmat.htm";
			else if(cond == 4 && st.getQuestItemsCount(ORIHARUKON) >= 5L && st.getQuestItemsCount(SILVER_NUGGET) >= 500L && st.getQuestItemsCount(THONS) >= 150L)
				htmltext = "30091-4.htm";
		}
		else if(npcId == 30879)
			if(cond == 1)
				htmltext = "30879-0.htm";
			else if(cond == 2)
				htmltext = "30879-1r.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final long count = st.getQuestItemsCount(ROUGH_JEWEL);
		if(count < 10L)
		{
			st.giveItems(ROUGH_JEWEL, 1L);
			if(st.getQuestItemsCount(ROUGH_JEWEL) == 10L)
			{
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "3");
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}
}
