package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _165_ShilensHunt extends Quest implements ScriptFile
{
	private static final int DARK_BEZOAR = 1160;
	private static final int LESSER_HEALING_POTION = 1060;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _165_ShilensHunt()
	{
		super(false);
		this.addStartNpc(30348);
		this.addTalkId(new int[] { 30348 });
		this.addKillId(new int[] { 20456 });
		this.addKillId(new int[] { 20529 });
		this.addKillId(new int[] { 20532 });
		this.addKillId(new int[] { 20536 });
		addQuestItem(new int[] { 1160 });
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
			htmltext = "30348-03.htm";
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(cond == 0)
		{
			if(st.getPlayer().getRace() != Race.darkelf)
				htmltext = "30348-00.htm";
			else
			{
				if(st.getPlayer().getLevel() >= 3)
				{
					htmltext = "30348-02.htm";
					return htmltext;
				}
				htmltext = "30348-01.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(cond == 1 || st.getQuestItemsCount(1160) < 13L)
			htmltext = "30348-04.htm";
		else if(cond == 2)
		{
			htmltext = "30348-05.htm";
			st.takeItems(1160, -1L);
			st.giveItems(1060, 5L);
			st.addExpAndSp(1000L, 0L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int cond = st.getInt("cond");
		if(cond == 1 && st.getQuestItemsCount(1160) < 13L && Rnd.chance(90))
		{
			st.giveItems(1160, 1L);
			if(st.getQuestItemsCount(1160) == 13L)
			{
				st.set("cond", "2");
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}
}
