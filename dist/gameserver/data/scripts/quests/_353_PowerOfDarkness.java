package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _353_PowerOfDarkness extends Quest implements ScriptFile
{
	private static int GALMAN;
	private static int Malruk_Succubus;
	private static int Malruk_Succubus_Turen;
	private static int Malruk_Succubus2;
	private static int Malruk_Succubus_Turen2;
	private static int STONE;
	private static int STONE_CHANCE;

	public _353_PowerOfDarkness()
	{
		super(false);
		this.addStartNpc(_353_PowerOfDarkness.GALMAN);
		this.addKillId(new int[] { _353_PowerOfDarkness.Malruk_Succubus });
		this.addKillId(new int[] { _353_PowerOfDarkness.Malruk_Succubus_Turen });
		this.addKillId(new int[] { _353_PowerOfDarkness.Malruk_Succubus2 });
		this.addKillId(new int[] { _353_PowerOfDarkness.Malruk_Succubus_Turen2 });
		addQuestItem(new int[] { _353_PowerOfDarkness.STONE });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int _state = st.getState();
		if(event.equalsIgnoreCase("31044-04.htm") && _state == 1)
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("31044-08.htm") && _state == 2)
		{
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		if(npc.getNpcId() != _353_PowerOfDarkness.GALMAN)
			return htmltext;
		if(st.getState() == 1)
		{
			if(st.getPlayer().getLevel() >= 55)
			{
				htmltext = "31044-02.htm";
				st.set("cond", "0");
			}
			else
			{
				htmltext = "31044-01.htm";
				st.exitCurrentQuest(true);
			}
		}
		else
		{
			final long stone_count = st.getQuestItemsCount(_353_PowerOfDarkness.STONE);
			if(stone_count > 0L)
			{
				htmltext = "31044-06.htm";
				st.takeItems(_353_PowerOfDarkness.STONE, -1L);
				st.giveItems(57, 2500L + 230L * stone_count);
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else
				htmltext = "31044-05.htm";
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState qs)
	{
		if(qs.getState() != 2)
			return null;
		if(Rnd.chance(_353_PowerOfDarkness.STONE_CHANCE))
		{
			qs.giveItems(_353_PowerOfDarkness.STONE, 1L);
			qs.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	static
	{
		_353_PowerOfDarkness.GALMAN = 31044;
		_353_PowerOfDarkness.Malruk_Succubus = 20283;
		_353_PowerOfDarkness.Malruk_Succubus_Turen = 20284;
		_353_PowerOfDarkness.Malruk_Succubus2 = 20244;
		_353_PowerOfDarkness.Malruk_Succubus_Turen2 = 20245;
		_353_PowerOfDarkness.STONE = 5862;
		_353_PowerOfDarkness.STONE_CHANCE = 50;
	}
}
