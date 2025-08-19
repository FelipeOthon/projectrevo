package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _298_LizardmensConspiracy extends Quest implements ScriptFile
{
	public final int PRAGA = 30333;
	public final int ROHMER = 30344;
	public final int MAILLE_LIZARDMAN_WARRIOR = 20922;
	public final int MAILLE_LIZARDMAN_SHAMAN = 20923;
	public final int MAILLE_LIZARDMAN_MATRIARCH = 20924;
	public final int POISON_ARANEID = 20926;
	public final int KING_OF_THE_ARANEID = 20927;
	public final int REPORT = 7182;
	public final int SHINING_GEM = 7183;
	public final int SHINING_RED_GEM = 7184;
	public final int[][] MobsTable;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _298_LizardmensConspiracy()
	{
		super(false);
		MobsTable = new int[][] { { 20922, 7183 }, { 20923, 7183 }, { 20924, 7183 }, { 20926, 7184 }, { 20927, 7184 } };
		this.addStartNpc(30333);
		this.addTalkId(new int[] { 30333 });
		this.addTalkId(new int[] { 30344 });
		for(final int[] element : MobsTable)
			this.addKillId(new int[] { element[0] });
		addQuestItem(new int[] { 7182, 7183, 7184 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("guard_praga_q0298_0104.htm"))
		{
			st.setState(2);
			st.set("cond", "1");
			st.giveItems(7182, 1L);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("magister_rohmer_q0298_0201.htm"))
		{
			st.takeItems(7182, -1L);
			st.set("cond", "2");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("magister_rohmer_q0298_0301.htm") && st.getQuestItemsCount(7183) + st.getQuestItemsCount(7184) > 99L)
		{
			st.takeItems(7183, -1L);
			st.takeItems(7184, -1L);
			st.addExpAndSp(0L, 42000L);
			st.exitCurrentQuest(true);
			st.playSound(Quest.SOUND_FINISH);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 30333)
		{
			if(cond < 1)
				if(st.getPlayer().getLevel() < 25)
				{
					htmltext = "guard_praga_q0298_0102.htm";
					st.exitCurrentQuest(true);
				}
				else
					htmltext = "guard_praga_q0298_0101.htm";
			if(cond == 1)
				htmltext = "guard_praga_q0298_0105.htm";
		}
		else if(npcId == 30344)
			if(cond < 1)
				htmltext = "magister_rohmer_q0298_0202.htm";
			else if(cond == 1)
				htmltext = "magister_rohmer_q0298_0101.htm";
			else if(cond == 2 | st.getQuestItemsCount(7183) + st.getQuestItemsCount(7184) < 100L)
			{
				htmltext = "magister_rohmer_q0298_0204.htm";
				st.set("cond", "2");
			}
			else if(cond == 3 && st.getQuestItemsCount(7183) + st.getQuestItemsCount(7184) > 99L)
				htmltext = "magister_rohmer_q0298_0203.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int rand = Rnd.get(10);
		if(st.getInt("cond") == 2)
			for(final int[] element : MobsTable)
				if(npcId == element[0] && rand < 6 && st.getQuestItemsCount(element[1]) < 50L)
				{
					if(rand < 2 && element[1] == 7183)
						st.giveItems(element[1], 2L);
					else
						st.giveItems(element[1], 1L);
					if(st.getQuestItemsCount(7183) + st.getQuestItemsCount(7184) > 99L)
					{
						st.set("cond", "3");
						st.playSound(Quest.SOUND_MIDDLE);
					}
					else
						st.playSound(Quest.SOUND_ITEMGET);
				}
		return null;
	}
}
