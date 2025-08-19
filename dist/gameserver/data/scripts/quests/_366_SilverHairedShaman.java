package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _366_SilverHairedShaman extends Quest implements ScriptFile
{
	private static final int DIETER = 30111;
	private static final int SAIRON = 20986;
	private static final int SAIRONS_DOLL = 20987;
	private static final int SAIRONS_PUPPET = 20988;
	private static final int ADENA_PER_ONE = 500;
	private static final int START_ADENA = 12070;
	private static final int SAIRONS_SILVER_HAIR = 5874;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _366_SilverHairedShaman()
	{
		super(false);
		this.addStartNpc(30111);
		this.addKillId(new int[] { 20986 });
		this.addKillId(new int[] { 20987 });
		this.addKillId(new int[] { 20988 });
		addQuestItem(new int[] { 5874 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("30111-02.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30111-quit.htm"))
		{
			st.takeItems(5874, -1L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int id = st.getState();
		int cond = st.getInt("cond");
		if(id == 1)
			st.set("cond", "0");
		else
			cond = st.getInt("cond");
		if(npcId == 30111)
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 48)
					htmltext = "30111-01.htm";
				else
				{
					htmltext = "30111-00.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1 && st.getQuestItemsCount(5874) == 0L)
				htmltext = "30111-03.htm";
			else if(cond == 1 && st.getQuestItemsCount(5874) >= 1L)
			{
				st.giveItems(57, st.getQuestItemsCount(5874) * 500L + 12070L);
				st.takeItems(5874, -1L);
				htmltext = "30111-have.htm";
			}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int cond = st.getInt("cond");
		if(cond == 1 && Rnd.chance(66))
		{
			st.giveItems(5874, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		return null;
	}
}
