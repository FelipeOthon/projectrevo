package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _363_SorrowfulSoundofFlute extends Quest implements ScriptFile
{
	public final int NANARIN = 30956;
	public final int BARBADO = 30959;
	public final int POITAN = 30458;
	public final int HOLVAS = 30058;
	public final int MUSICAL_SCORE = 4420;
	public final int EVENT_CLOTHES = 4318;
	public final int NANARINS_FLUTE = 4319;
	public final int SABRINS_BLACK_BEER = 4320;
	public final int Musical_Score = 4420;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _363_SorrowfulSoundofFlute()
	{
		super(false);
		this.addStartNpc(30956);
		this.addTalkId(new int[] { 30956 });
		this.addTalkId(new int[] { 30458 });
		this.addTalkId(new int[] { 30058 });
		this.addTalkId(new int[] { 30959 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("30956_2.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
			st.takeItems(4318, -1L);
			st.takeItems(4319, -1L);
			st.takeItems(4320, -1L);
		}
		else if(event.equalsIgnoreCase("30956_4.htm"))
		{
			st.giveItems(4319, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
			st.set("cond", "3");
		}
		else if(event.equalsIgnoreCase("answer1"))
		{
			st.giveItems(4318, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
			st.set("cond", "3");
			htmltext = "30956_6.htm";
		}
		else if(event.equalsIgnoreCase("answer2"))
		{
			st.giveItems(4320, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
			st.set("cond", "3");
			htmltext = "30956_6.htm";
		}
		else if(event.equalsIgnoreCase("30956_7.htm"))
		{
			st.giveItems(4420, 1L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(npcId == 30956)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() < 15)
				{
					htmltext = "30956-00.htm";
					st.exitCurrentQuest(true);
				}
				else
					htmltext = "30956_1.htm";
			}
			else if(cond == 1)
				htmltext = "30956_8.htm";
			else if(cond == 2)
				htmltext = "30956_3.htm";
			else if(cond == 3)
				htmltext = "30956_6.htm";
			else if(cond == 4)
				htmltext = "30956_5.htm";
		}
		else if(npcId == 30959)
		{
			if(cond == 3)
			{
				if(st.getQuestItemsCount(4318) > 0L)
				{
					st.takeItems(4318, -1L);
					htmltext = "30959_2.htm";
					st.exitCurrentQuest(true);
				}
				else if(st.getQuestItemsCount(4320) > 0L)
				{
					st.takeItems(4320, -1L);
					htmltext = "30959_2.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					st.takeItems(4319, -1L);
					st.set("cond", "4");
					st.playSound(Quest.SOUND_MIDDLE);
					htmltext = "30959_1.htm";
				}
			}
			else if(cond == 4)
				htmltext = "30959_3.htm";
		}
		else if(npcId == 30058 && (cond == 1 || cond == 2))
		{
			st.set("cond", "2");
			if(Rnd.chance(60))
				htmltext = "30058_2.htm";
			else
				htmltext = "30058_1.htm";
		}
		else if(npcId == 30458 && (cond == 1 || cond == 2))
		{
			st.set("cond", "2");
			if(Rnd.chance(60))
				htmltext = "30458_2.htm";
			else
				htmltext = "30458_1.htm";
		}
		return htmltext;
	}
}
