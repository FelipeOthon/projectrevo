package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _652_AnAgedExAdventurer extends Quest implements ScriptFile
{
	private static final int Tantan = 32012;
	private static final int Sara = 30180;
	private static final int SoulshotCgrade = 1464;
	private static final int ScrollEnchantArmorD = 956;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _652_AnAgedExAdventurer()
	{
		super(false);
		this.addStartNpc(32012);
		this.addTalkId(new int[] { 30180 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext;
		if(event.equalsIgnoreCase("retired_oldman_tantan_q0652_03.htm") && st.getQuestItemsCount(1464) >= 100L)
		{
			st.set("cond", "1");
			st.setState(2);
			st.takeItems(1464, 100L);
			st.playSound(Quest.SOUND_ACCEPT);
			htmltext = "retired_oldman_tantan_q0652_04.htm";
		}
		else
		{
			htmltext = "retired_oldman_tantan_q0652_03.htm";
			st.exitCurrentQuest(true);
			st.playSound(Quest.SOUND_GIVEUP);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(npcId == 32012)
		{
			if(cond == 0)
				if(st.getPlayer().getLevel() < 46)
				{
					htmltext = "retired_oldman_tantan_q0652_01a.htm";
					st.exitCurrentQuest(true);
				}
				else
					htmltext = "retired_oldman_tantan_q0652_01.htm";
		}
		else if(npcId == 30180 && cond == 1)
		{
			htmltext = "sara_q0652_01.htm";
			st.giveItems(57, 10000L, true);
			if(Rnd.chance(50))
				st.giveItems(956, 1L, false);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}
}
