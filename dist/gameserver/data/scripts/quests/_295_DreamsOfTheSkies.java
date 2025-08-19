package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _295_DreamsOfTheSkies extends Quest implements ScriptFile
{
	public static int FLOATING_STONE;
	public static int RING_OF_FIREFLY;
	public static int Arin;
	public static int MagicalWeaver;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _295_DreamsOfTheSkies()
	{
		super(false);
		this.addStartNpc(_295_DreamsOfTheSkies.Arin);
		this.addTalkId(new int[] { _295_DreamsOfTheSkies.Arin });
		this.addKillId(new int[] { _295_DreamsOfTheSkies.MagicalWeaver });
		addQuestItem(new int[] { _295_DreamsOfTheSkies.FLOATING_STONE });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("elder_arin_q0295_03.htm"))
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
		if(id == 1)
			st.set("cond", "0");
		final int cond = st.getInt("cond");
		if(cond == 0)
		{
			if(st.getPlayer().getLevel() >= 11)
			{
				htmltext = "elder_arin_q0295_02.htm";
				return htmltext;
			}
			htmltext = "elder_arin_q0295_01.htm";
			st.exitCurrentQuest(true);
		}
		else if(cond == 1 || st.getQuestItemsCount(_295_DreamsOfTheSkies.FLOATING_STONE) < 50L)
			htmltext = "elder_arin_q0295_04.htm";
		else if(cond == 2 && st.getQuestItemsCount(_295_DreamsOfTheSkies.FLOATING_STONE) == 50L)
		{
			st.addExpAndSp(0L, 500L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
			if(st.getQuestItemsCount(_295_DreamsOfTheSkies.RING_OF_FIREFLY) < 1L)
			{
				htmltext = "elder_arin_q0295_05.htm";
				st.giveItems(_295_DreamsOfTheSkies.RING_OF_FIREFLY, 1L);
			}
			else
			{
				htmltext = "elder_arin_q0295_06.htm";
				st.giveItems(57, 2400L);
			}
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getInt("cond") == 1 && st.getQuestItemsCount(_295_DreamsOfTheSkies.FLOATING_STONE) < 50L)
			if(Rnd.chance(25))
			{
				st.giveItems(_295_DreamsOfTheSkies.FLOATING_STONE, 1L);
				if(st.getQuestItemsCount(_295_DreamsOfTheSkies.FLOATING_STONE) == 50L)
				{
					st.playSound(Quest.SOUND_MIDDLE);
					st.set("cond", "2");
				}
				else
					st.playSound(Quest.SOUND_ITEMGET);
			}
			else if(st.getQuestItemsCount(_295_DreamsOfTheSkies.FLOATING_STONE) >= 48L)
			{
				st.giveItems(_295_DreamsOfTheSkies.FLOATING_STONE, 50L - st.getQuestItemsCount(_295_DreamsOfTheSkies.FLOATING_STONE));
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "2");
			}
			else
			{
				st.giveItems(_295_DreamsOfTheSkies.FLOATING_STONE, 2L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
		return null;
	}

	static
	{
		_295_DreamsOfTheSkies.FLOATING_STONE = 1492;
		_295_DreamsOfTheSkies.RING_OF_FIREFLY = 1509;
		_295_DreamsOfTheSkies.Arin = 30536;
		_295_DreamsOfTheSkies.MagicalWeaver = 20153;
	}
}
