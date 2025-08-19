package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _370_AnElderSowsSeeds extends Quest implements ScriptFile
{
	private static int CASIAN;
	private static int[] MOBS;
	private static int SPB_PAGE;
	private static int[] CHAPTERS;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _370_AnElderSowsSeeds()
	{
		super(false);
		this.addStartNpc(_370_AnElderSowsSeeds.CASIAN);
		for(final int npcId : _370_AnElderSowsSeeds.MOBS)
			this.addKillId(new int[] { npcId });
		addQuestItem(new int[] { _370_AnElderSowsSeeds.SPB_PAGE });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("30612-1.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30612-6.htm"))
		{
			if(st.getQuestItemsCount(_370_AnElderSowsSeeds.CHAPTERS[0]) > 0L && st.getQuestItemsCount(_370_AnElderSowsSeeds.CHAPTERS[1]) > 0L && st.getQuestItemsCount(_370_AnElderSowsSeeds.CHAPTERS[2]) > 0L && st.getQuestItemsCount(_370_AnElderSowsSeeds.CHAPTERS[3]) > 0L)
			{
				long mincount = st.getQuestItemsCount(_370_AnElderSowsSeeds.CHAPTERS[0]);
				for(final int itemId : _370_AnElderSowsSeeds.CHAPTERS)
					mincount = Math.min(mincount, st.getQuestItemsCount(itemId));
				for(final int itemId : _370_AnElderSowsSeeds.CHAPTERS)
					st.takeItems(itemId, mincount);
				st.giveItems(57, 3600L * mincount);
				htmltext = "30612-8.htm";
			}
			else
				htmltext = "30612-4.htm";
		}
		else if(event.equalsIgnoreCase("30612-9.htm"))
		{
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(st.getState() == 1)
		{
			if(st.getPlayer().getLevel() < 28)
			{
				htmltext = "30612-0a.htm";
				st.exitCurrentQuest(true);
			}
			else
				htmltext = "30612-0.htm";
		}
		else if(cond == 1)
			htmltext = "30612-4.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getState() != 2)
			return null;
		if(Rnd.chance(Math.min((int) (15.0f * st.getRateQuestsReward()), 100)))
		{
			st.giveItems(_370_AnElderSowsSeeds.SPB_PAGE, 1L);
			st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}

	static
	{
		_370_AnElderSowsSeeds.CASIAN = 30612;
		_370_AnElderSowsSeeds.MOBS = new int[] { 20082, 20084, 20086, 20089, 20090 };
		_370_AnElderSowsSeeds.SPB_PAGE = 5916;
		_370_AnElderSowsSeeds.CHAPTERS = new int[] { 5917, 5918, 5919, 5920 };
	}
}
