package quests;

import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _047_IntoTheDarkForest extends Quest implements ScriptFile
{
	private static final int GALLADUCCIS_ORDER_DOCUMENT_ID_1 = 7563;
	private static final int GALLADUCCIS_ORDER_DOCUMENT_ID_2 = 7564;
	private static final int GALLADUCCIS_ORDER_DOCUMENT_ID_3 = 7565;
	private static final int MAGIC_SWORD_HILT_ID = 7568;
	private static final int GEMSTONE_POWDER_ID = 7567;
	private static final int PURIFIED_MAGIC_NECKLACE_ID = 7566;
	private static final int MARK_OF_TRAVELER_ID = 7570;
	private static final int SCROLL_OF_ESCAPE_DARK_ELF_VILLAGE = 7119;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _047_IntoTheDarkForest()
	{
		super(false);
		this.addStartNpc(30097);
		this.addTalkId(new int[] { 30097 });
		this.addTalkId(new int[] { 30097 });
		this.addTalkId(new int[] { 30097 });
		this.addTalkId(new int[] { 30094 });
		this.addTalkId(new int[] { 30090 });
		this.addTalkId(new int[] { 30116 });
		addQuestItem(new int[] { 7563, 7564, 7565, 7568, 7567, 7566 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("1"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
			st.giveItems(7563, 1L);
			htmltext = "galladuchi_q0047_0104.htm";
		}
		else if(event.equals("2"))
		{
			st.set("cond", "2");
			st.takeItems(7563, 1L);
			st.giveItems(7568, 1L);
			htmltext = "gentler_q0047_0201.htm";
		}
		else if(event.equals("3"))
		{
			st.set("cond", "3");
			st.takeItems(7568, 1L);
			st.giveItems(7564, 1L);
			htmltext = "galladuchi_q0047_0301.htm";
		}
		else if(event.equals("4"))
		{
			st.set("cond", "4");
			st.takeItems(7564, 1L);
			st.giveItems(7567, 1L);
			htmltext = "sandra_q0047_0401.htm";
		}
		else if(event.equals("5"))
		{
			st.set("cond", "5");
			st.takeItems(7567, 1L);
			st.giveItems(7565, 1L);
			htmltext = "galladuchi_q0047_0501.htm";
		}
		else if(event.equals("6"))
		{
			st.set("cond", "6");
			st.takeItems(7565, 1L);
			st.giveItems(7566, 1L);
			htmltext = "dustin_q0047_0601.htm";
		}
		else if(event.equals("7"))
		{
			st.giveItems(7119, 1L);
			st.takeItems(7566, 1L);
			htmltext = "galladuchi_q0047_0701.htm";
			st.set("cond", "0");
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(false);
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
			if(st.getPlayer().getRace() != Race.darkelf || st.getQuestItemsCount(7570) == 0L)
			{
				htmltext = "galladuchi_q0047_0102.htm";
				st.exitCurrentQuest(true);
			}
			else if(st.getPlayer().getLevel() < 3)
			{
				htmltext = "galladuchi_q0047_0103.htm";
				st.exitCurrentQuest(true);
			}
			else
				htmltext = "galladuchi_q0047_0101.htm";
		}
		else if(npcId == 30097 && st.getInt("cond") == 1)
			htmltext = "galladuchi_q0047_0105.htm";
		else if(npcId == 30097 && st.getInt("cond") == 2)
			htmltext = "galladuchi_q0047_0201.htm";
		else if(npcId == 30097 && st.getInt("cond") == 3)
			htmltext = "galladuchi_q0047_0303.htm";
		else if(npcId == 30097 && st.getInt("cond") == 4)
			htmltext = "galladuchi_q0047_0401.htm";
		else if(npcId == 30097 && st.getInt("cond") == 5)
			htmltext = "galladuchi_q0047_0503.htm";
		else if(npcId == 30097 && st.getInt("cond") == 6)
			htmltext = "galladuchi_q0047_0601.htm";
		else if(npcId == 30094 && st.getInt("cond") == 1)
			htmltext = "gentler_q0047_0101.htm";
		else if(npcId == 30094 && st.getInt("cond") == 2)
			htmltext = "gentler_q0047_0203.htm";
		else if(npcId == 30090 && st.getInt("cond") == 3)
			htmltext = "sandra_q0047_0301.htm";
		else if(npcId == 30090 && st.getInt("cond") == 4)
			htmltext = "sandra_q0047_0403.htm";
		else if(npcId == 30116 && st.getInt("cond") == 5)
			htmltext = "dustin_q0047_0501.htm";
		else if(npcId == 30116 && st.getInt("cond") == 6)
			htmltext = "dustin_q0047_0603.htm";
		return htmltext;
	}
}
