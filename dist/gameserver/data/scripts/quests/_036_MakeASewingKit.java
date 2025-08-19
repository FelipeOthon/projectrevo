package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _036_MakeASewingKit extends Quest implements ScriptFile
{
	int REINFORCED_STEEL;
	int ARTISANS_FRAME;
	int ORIHARUKON;
	int SEWING_KIT;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _036_MakeASewingKit()
	{
		super(false);
		REINFORCED_STEEL = 7163;
		ARTISANS_FRAME = 1891;
		ORIHARUKON = 1893;
		SEWING_KIT = 7078;
		this.addStartNpc(30847);
		this.addTalkId(new int[] { 30847 });
		this.addTalkId(new int[] { 30847 });
		this.addKillId(new int[] { 20566 });
		addQuestItem(new int[] { REINFORCED_STEEL });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		final int cond = st.getInt("cond");
		if(event.equals("head_blacksmith_ferris_q0036_0104.htm") && cond == 0)
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("head_blacksmith_ferris_q0036_0201.htm") && cond == 2)
		{
			st.takeItems(REINFORCED_STEEL, 5L);
			st.set("cond", "3");
		}
		else if(event.equals("head_blacksmith_ferris_q0036_0301.htm"))
			if(st.getQuestItemsCount(ORIHARUKON) >= 10L && st.getQuestItemsCount(ARTISANS_FRAME) >= 10L)
			{
				st.takeItems(ORIHARUKON, 10L);
				st.takeItems(ARTISANS_FRAME, 10L);
				st.giveItems(SEWING_KIT, 1L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
			else
				htmltext = "head_blacksmith_ferris_q0036_0203.htm";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(cond == 0 && st.getQuestItemsCount(SEWING_KIT) == 0L)
		{
			if(st.getPlayer().getLevel() >= 60)
			{
				final QuestState fwear = st.getPlayer().getQuestState(37);
				if(fwear != null && fwear.getState() == 2)
				{
					if(fwear.getCond() == 6)
						htmltext = "head_blacksmith_ferris_q0036_0101.htm";
					else
						st.exitCurrentQuest(true);
				}
				else
					st.exitCurrentQuest(true);
			}
			else
				htmltext = "head_blacksmith_ferris_q0036_0103.htm";
		}
		else if(cond == 1 && st.getQuestItemsCount(REINFORCED_STEEL) < 5L)
			htmltext = "head_blacksmith_ferris_q0036_0106.htm";
		else if(cond == 2 && st.getQuestItemsCount(REINFORCED_STEEL) == 5L)
			htmltext = "head_blacksmith_ferris_q0036_0105.htm";
		else if(cond == 3 && (st.getQuestItemsCount(ORIHARUKON) < 10L || st.getQuestItemsCount(ARTISANS_FRAME) < 10L))
			htmltext = "head_blacksmith_ferris_q0036_0204.htm";
		else if(cond == 3 && st.getQuestItemsCount(ORIHARUKON) >= 10L && st.getQuestItemsCount(ARTISANS_FRAME) >= 10L)
			htmltext = "head_blacksmith_ferris_q0036_0203.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getQuestItemsCount(REINFORCED_STEEL) < 5L)
		{
			st.giveItems(REINFORCED_STEEL, 1L);
			if(st.getQuestItemsCount(REINFORCED_STEEL) == 5L)
			{
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "2");
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}
}
