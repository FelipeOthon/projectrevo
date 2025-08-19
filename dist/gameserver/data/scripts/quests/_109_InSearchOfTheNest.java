package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _109_InSearchOfTheNest extends Quest implements ScriptFile
{
	private static final int PIERCE = 31553;
	private static final int CORPSE = 32015;
	private static final int KAHMAN = 31554;
	private static final int MEMO = 8083;
	private static final int GOLDEN_BADGE_RECRUIT = 7246;
	private static final int GOLDEN_BADGE_SOLDIER = 7247;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _109_InSearchOfTheNest()
	{
		super(false);
		this.addStartNpc(31553);
		this.addTalkId(new int[] { 32015 });
		this.addTalkId(new int[] { 31554 });
		addQuestItem(new int[] { 8083 });
		addQuestItem(new int[] { 7246 });
		addQuestItem(new int[] { 7247 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		final int cond = st.getInt("cond");
		if(event.equalsIgnoreCase("Memo") && cond == 1)
		{
			st.giveItems(8083, 1L);
			st.set("cond", "2");
			st.playSound(Quest.SOUND_ITEMGET);
			htmltext = "You've find something...";
		}
		else if(event.equalsIgnoreCase("31553-02.htm") && cond == 2)
		{
			st.takeItems(8083, -1L);
			st.set("cond", "3");
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int id = st.getState();
		if(id == 3)
			return "completed";
		final int cond = st.getInt("cond");
		String htmltext = "noquest";
		if(id == 1)
		{
			if(st.getPlayer().getLevel() >= 66 && npcId == 31553 && (st.getQuestItemsCount(7246) > 0L || st.getQuestItemsCount(7247) > 0L))
			{
				st.setState(2);
				st.playSound(Quest.SOUND_ACCEPT);
				st.set("cond", "1");
				htmltext = "31553-03.htm";
			}
			else
			{
				htmltext = "31553-00.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(id == 2)
			if(npcId == 32015)
			{
				if(cond == 1)
					htmltext = "32015-01.htm";
				else if(cond == 2)
					htmltext = "32015-02.htm";
			}
			else if(npcId == 31553)
			{
				if(cond == 1)
					htmltext = "31553-04.htm";
				else if(cond == 2)
					htmltext = "31553-01.htm";
				else if(cond == 3)
					htmltext = "31553-05.htm";
			}
			else if(npcId == 31554 && cond == 3)
			{
				htmltext = "31554-01.htm";
				st.addExpAndSp(146113L, 13723L);
				st.giveItems(57, 25461L);
				st.exitCurrentQuest(false);
				st.playSound(Quest.SOUND_FINISH);
			}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		return null;
	}
}
