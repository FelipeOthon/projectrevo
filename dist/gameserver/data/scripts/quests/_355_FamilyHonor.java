package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _355_FamilyHonor extends Quest implements ScriptFile
{
	private static final int GALIBREDO = 30181;
	private static final int PATRIN = 30929;
	private static final int CHANCE_FOR_GALFREDOS_BUST = 80;
	private static final int CHANCE_FOR_GODDESS_BUST = 30;
	private static final int GALFREDOS_BUST = 4252;
	private static final int BUST_OF_ANCIENT_GODDESS = 4349;
	private static final int WORK_OF_BERONA = 4350;
	private static final int STATUE_PROTOTYPE = 4351;
	private static final int STATUE_ORIGINAL = 4352;
	private static final int STATUE_REPLICA = 4353;
	private static final int STATUE_FORGERY = 4354;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _355_FamilyHonor()
	{
		super(true);
		this.addStartNpc(30181);
		this.addTalkId(new int[] { 30929 });
		this.addKillId(new int[] { 20767 });
		this.addKillId(new int[] { 20768 });
		this.addKillId(new int[] { 20769 });
		this.addKillId(new int[] { 20770 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("galicbredo_q0355_03.htm"))
			return htmltext;
		if(event.equals("galicbredo_q0355_04.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("galicbredo_q0355_07.htm"))
		{
			final long count = st.getQuestItemsCount(4349);
			st.takeItems(4349, count);
			st.giveItems(4350, count);
		}
		else
		{
			if(event.equals("patrin_q0355_01.htm"))
				return htmltext;
			if(event.equals("patrin_q0355_01a.htm"))
				return htmltext;
			if(event.equals("appraise"))
			{
				final int appraising = Rnd.get(100);
				if(appraising < 20)
				{
					htmltext = "patrin_q0355_07.htm";
					st.takeItems(4350, 1L);
				}
				else if(appraising < 40)
				{
					htmltext = "patrin_q0355_05.htm";
					st.takeItems(4350, 1L);
					st.giveItems(4353, 1L);
				}
				else if(appraising < 60)
				{
					htmltext = "patrin_q0355_04.htm";
					st.takeItems(4350, 1L);
					st.giveItems(4352, 1L);
				}
				else if(appraising < 80)
				{
					htmltext = "galicbredo_q0355_10.htm";
					st.takeItems(4350, 1L);
					st.giveItems(4354, 1L);
				}
				else if(appraising < 100)
				{
					htmltext = "galicbredo_q0355_11.htm";
					st.takeItems(4350, 1L);
					st.giveItems(4351, 1L);
				}
			}
			else if(event.equals("galicbredo_q0355_09.htm"))
			{
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		final long count = st.getQuestItemsCount(4252);
		if(npcId == 30181)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 36)
					htmltext = "galicbredo_q0355_02.htm";
				else
				{
					htmltext = "galicbredo_q0355_01.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				if(count > 0L)
				{
					long reward = count * 232L;
					if(count >= 100L)
						reward += 5000L;
					st.takeItems(4252, count);
					st.giveItems(57, reward);
					htmltext = "galicbredo_q0355_07a.htm";
				}
				else
					htmltext = "galicbredo_q0355_08.htm";
		}
		else if(npcId == 30929)
			if(st.getQuestItemsCount(4350) > 0L)
				htmltext = "patrin_q0355_01.htm";
			else
				htmltext = "<html><head><body>You have nothing to appraise.</body></html>";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getInt("cond") == 1)
		{
			if(Rnd.chance(80))
			{
				st.giveItems(4252, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
			if(Rnd.chance(30))
				st.giveItems(4349, 1L);
		}
		return null;
	}
}
