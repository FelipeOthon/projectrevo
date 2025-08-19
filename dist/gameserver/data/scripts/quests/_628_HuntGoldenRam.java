package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _628_HuntGoldenRam extends Quest implements ScriptFile
{
	private static int KAHMAN;
	private static int CHITIN;
	private static int CHITIN2;
	private static int RECRUIT;
	private static int SOLDIER;
	private static int CHANCE;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _628_HuntGoldenRam()
	{
		super(true);
		this.addStartNpc(_628_HuntGoldenRam.KAHMAN);
		for(int npcId = 21508; npcId <= 21518; ++npcId)
			this.addKillId(new int[] { npcId });
		addQuestItem(new int[] { _628_HuntGoldenRam.CHITIN });
		addQuestItem(new int[] { _628_HuntGoldenRam.CHITIN2 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("31554-03a.htm"))
		{
			if(st.getQuestItemsCount(_628_HuntGoldenRam.CHITIN) >= 100L && st.getInt("cond") == 1)
			{
				st.set("cond", "2");
				st.takeItems(_628_HuntGoldenRam.CHITIN, 100L);
				st.giveItems(_628_HuntGoldenRam.RECRUIT, 1L);
				st.getPlayer().updateRam();
				htmltext = "31554-04.htm";
			}
		}
		else if(event.equalsIgnoreCase("31554-07.htm"))
		{
			st.playSound(Quest.SOUND_GIVEUP);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		final long chitin1 = st.getQuestItemsCount(_628_HuntGoldenRam.CHITIN);
		final long chitin2 = st.getQuestItemsCount(_628_HuntGoldenRam.CHITIN2);
		if(st.isCompleted())
			htmltext = "31554-05a.htm";
		else if(cond == 0)
		{
			if(st.getPlayer().getLevel() >= 66)
			{
				htmltext = "31554-02.htm";
				st.set("cond", "1");
				st.setState(2);
				st.playSound(Quest.SOUND_ACCEPT);
			}
			else
			{
				htmltext = "31554-01.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(cond == 1)
		{
			if(chitin1 >= 100L)
				htmltext = "31554-03.htm";
			else
				htmltext = "31554-03a.htm";
		}
		else if(cond == 2)
		{
			if(chitin1 >= 100L && chitin2 >= 100L)
			{
				htmltext = "31554-05.htm";
				st.takeItems(_628_HuntGoldenRam.CHITIN, -1L);
				st.takeItems(_628_HuntGoldenRam.CHITIN2, -1L);
				st.takeItems(_628_HuntGoldenRam.RECRUIT, -1L);
				st.giveItems(_628_HuntGoldenRam.SOLDIER, 1L);
				st.getPlayer().updateRam();
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
			if(chitin1 == 0L && chitin2 == 0L)
				htmltext = "31554-04b.htm";
			else
				htmltext = "31554-04a.htm";
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getState() != 2)
			return null;
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(cond >= 1 && 21507 < npcId && npcId < 21513)
			st.dropItems(_628_HuntGoldenRam.CHITIN, 1, 1, 100L, _628_HuntGoldenRam.CHANCE + (npcId - 21506) * 2, true);
		else if(cond == 2 && 21513 <= npcId && npcId <= 21518)
			st.dropItems(_628_HuntGoldenRam.CHITIN2, 1, 1, 100L, _628_HuntGoldenRam.CHANCE + (npcId - 21512) * 3, true);
		return null;
	}

	static
	{
		_628_HuntGoldenRam.KAHMAN = 31554;
		_628_HuntGoldenRam.CHITIN = 7248;
		_628_HuntGoldenRam.CHITIN2 = 7249;
		_628_HuntGoldenRam.RECRUIT = 7246;
		_628_HuntGoldenRam.SOLDIER = 7247;
		_628_HuntGoldenRam.CHANCE = 49;
	}
}
