package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.scripts.ScriptFile;

public class _169_OffspringOfNightmares extends Quest implements ScriptFile
{
	private static final int Vlasty = 30145;
	private static final int CrackedSkull = 1030;
	private static final int PerfectSkull = 1031;
	private static final int BoneGaiters = 31;
	private static final int DarkHorror = 20105;
	private static final int LesserDarkHorror = 20025;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _169_OffspringOfNightmares()
	{
		super(false);
		this.addStartNpc(30145);
		this.addTalkId(new int[] { 30145 });
		this.addKillId(new int[] { 20105 });
		this.addKillId(new int[] { 20025 });
		addQuestItem(new int[] { 1030, 1031 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("30145-04.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30145-08.htm"))
		{
			st.takeItems(1030, -1L);
			st.takeItems(1031, -1L);
			st.giveItems(31, 1L);
			st.giveItems(57, 17050L, true);
			st.getPlayer().addExpAndSp(17475L, 818L, false, false);
			if(st.getPlayer().getClassId().getLevel() == 1 && !st.getPlayer().getVarBoolean("p1q4"))
			{
				st.getPlayer().setVar("p1q4", "1");
				st.getPlayer().sendPacket(new ExShowScreenMessage("Now go find the Newbie Guide.", 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true));
			}
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(npcId == 30145)
			if(cond == 0)
			{
				if(st.getPlayer().getRace() != Race.darkelf)
				{
					htmltext = "30145-00.htm";
					st.exitCurrentQuest(true);
				}
				else if(st.getPlayer().getLevel() >= 15)
					htmltext = "30145-03.htm";
				else
				{
					htmltext = "30145-02.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
			{
				if(st.getQuestItemsCount(1030) == 0L)
					htmltext = "30145-05.htm";
				else
					htmltext = "30145-06.htm";
			}
			else if(cond == 2)
				htmltext = "30145-07.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int cond = st.getInt("cond");
		if(cond == 1)
		{
			if(Rnd.chance(20) && st.getQuestItemsCount(1031) == 0L)
			{
				st.giveItems(1031, 1L);
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "2");
				st.setState(2);
			}
			if(Rnd.chance(70))
			{
				st.giveItems(1030, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
		}
		return null;
	}
}
