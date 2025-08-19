package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _274_SkirmishWithTheWerewolves extends Quest implements ScriptFile
{
	private static final int MARAKU_WEREWOLF_HEAD = 1477;
	private static final int NECKLACE_OF_VALOR = 1507;
	private static final int NECKLACE_OF_COURAGE = 1506;
	private static final int MARAKU_WOLFMEN_TOTEM = 1501;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _274_SkirmishWithTheWerewolves()
	{
		super(false);
		this.addStartNpc(30569);
		this.addKillId(new int[] { 20363 });
		this.addKillId(new int[] { 20364 });
		addQuestItem(new int[] { 1477 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equals("prefect_brukurse_q0274_03.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int id = st.getState();
		final int cond = st.getInt("cond");
		if(id == 1)
		{
			if(st.getPlayer().getRace() != Race.orc)
			{
				htmltext = "prefect_brukurse_q0274_00.htm";
				st.exitCurrentQuest(true);
			}
			else if(st.getPlayer().getLevel() < 9)
			{
				htmltext = "prefect_brukurse_q0274_01.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				if(st.getQuestItemsCount(1507) > 0L || st.getQuestItemsCount(1506) > 0L)
				{
					htmltext = "prefect_brukurse_q0274_02.htm";
					return htmltext;
				}
				htmltext = "prefect_brukurse_q0274_07.htm";
			}
		}
		else if(cond == 1)
			htmltext = "prefect_brukurse_q0274_04.htm";
		else if(cond == 2)
			if(st.getQuestItemsCount(1477) < 40L)
				htmltext = "prefect_brukurse_q0274_04.htm";
			else
			{
				st.takeItems(1477, -1L);
				st.giveItems(57, 3500L, true);
				if(st.getQuestItemsCount(1501) >= 1L)
				{
					st.giveItems(57, st.getQuestItemsCount(1501) * 600L, true);
					st.takeItems(1501, -1L);
				}
				htmltext = "prefect_brukurse_q0274_05.htm";
				st.exitCurrentQuest(true);
				st.playSound(Quest.SOUND_FINISH);
			}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getInt("cond") == 1 && st.getQuestItemsCount(1477) < 40L)
		{
			if(st.getQuestItemsCount(1477) < 39L)
				st.playSound(Quest.SOUND_ITEMGET);
			else
			{
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "2");
			}
			st.giveItems(1477, 1L);
		}
		if(Rnd.chance(5))
			st.giveItems(1501, 1L);
		return null;
	}
}
