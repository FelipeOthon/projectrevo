package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _659_IdRatherBeCollectingFairyBreath extends Quest implements ScriptFile
{
	public final int GALATEA = 30634;
	public final int[] MOBS;
	public final int FAIRY_BREATH = 8286;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _659_IdRatherBeCollectingFairyBreath()
	{
		super(false);
		MOBS = new int[] { 20078, 21026, 21025, 21024, 21023 };
		this.addStartNpc(30634);
		this.addTalkId(new int[] { 30634 });
		this.addTalkId(new int[] { 30634 });
		this.addTalkId(new int[] { 30634 });
		for(final int i : MOBS)
			this.addKillId(new int[] { i });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("high_summoner_galatea_q0659_0103.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("high_summoner_galatea_q0659_0203.htm"))
		{
			final long count = st.getQuestItemsCount(8286);
			if(count > 0L)
			{
				long reward = 0L;
				if(count < 10L)
					reward = count * 50L;
				else
					reward = count * 50L + 5365L;
				st.takeItems(8286, -1L);
				st.giveItems(57, reward);
			}
		}
		else if(event.equalsIgnoreCase("high_summoner_galatea_q0659_0204.htm"))
			st.exitCurrentQuest(true);
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int id = st.getState();
		int cond = 0;
		if(id != 1)
			cond = st.getInt("cond");
		if(npcId == 30634)
			if(st.getPlayer().getLevel() < 26)
			{
				htmltext = "high_summoner_galatea_q0659_0102.htm";
				st.exitCurrentQuest(true);
			}
			else if(cond == 0)
				htmltext = "high_summoner_galatea_q0659_0101.htm";
			else if(cond == 1)
				if(st.getQuestItemsCount(8286) == 0L)
					htmltext = "high_summoner_galatea_q0659_0105.htm";
				else
					htmltext = "high_summoner_galatea_q0659_0105.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(cond == 1)
			for(final int i : MOBS)
				if(npcId == i && Rnd.chance(30))
				{
					st.giveItems(8286, 1L);
					st.playSound(Quest.SOUND_ITEMGET);
				}
		return null;
	}
}
