package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _275_BlackWingedSpies extends Quest implements ScriptFile
{
	private static int Tantus;
	private static int Darkwing_Bat;
	private static int Varangkas_Tracker;
	private static short Darkwing_Bat_Fang;
	private static short Varangkas_Parasite;
	private static int Varangkas_Parasite_Chance;

	public _275_BlackWingedSpies()
	{
		super(false);
		this.addStartNpc(_275_BlackWingedSpies.Tantus);
		this.addKillId(new int[] { _275_BlackWingedSpies.Darkwing_Bat });
		this.addKillId(new int[] { _275_BlackWingedSpies.Varangkas_Tracker });
		addQuestItem(new int[] { _275_BlackWingedSpies.Darkwing_Bat_Fang });
		addQuestItem(new int[] { _275_BlackWingedSpies.Varangkas_Parasite });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("neruga_chief_tantus_q0275_03.htm") && st.getState() == 1)
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		if(npc.getNpcId() != _275_BlackWingedSpies.Tantus)
			return "noquest";
		final int _state = st.getState();
		if(_state == 1)
		{
			if(st.getPlayer().getRace() != Race.orc)
			{
				st.exitCurrentQuest(true);
				return "neruga_chief_tantus_q0275_00.htm";
			}
			if(st.getPlayer().getLevel() < 11)
			{
				st.exitCurrentQuest(true);
				return "neruga_chief_tantus_q0275_01.htm";
			}
			st.set("cond", "0");
			return "neruga_chief_tantus_q0275_02.htm";
		}
		else
		{
			if(_state != 2)
				return "noquest";
			final int cond = st.getInt("cond");
			if(st.getQuestItemsCount(_275_BlackWingedSpies.Darkwing_Bat_Fang) < 70L)
			{
				if(cond != 1)
					st.set("cond", "1");
				return "neruga_chief_tantus_q0275_04.htm";
			}
			if(cond == 2)
			{
				st.giveItems(57, 4550L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
				return "neruga_chief_tantus_q0275_05.htm";
			}
			return "noquest";
		}
	}

	private static void spawn_Varangkas_Tracker(final QuestState st)
	{
		if(st.getQuestItemsCount(_275_BlackWingedSpies.Varangkas_Parasite) > 0L)
			st.takeItems(_275_BlackWingedSpies.Varangkas_Parasite, -1L);
		st.giveItems(_275_BlackWingedSpies.Varangkas_Parasite, 1L);
		st.addSpawn(_275_BlackWingedSpies.Varangkas_Tracker);
	}

	public static void give_Darkwing_Bat_Fang(final QuestState st, long _count)
	{
		final long max_inc = 70L - st.getQuestItemsCount(_275_BlackWingedSpies.Darkwing_Bat_Fang);
		if(max_inc < 1L)
			return;
		if(_count > max_inc)
			_count = max_inc;
		st.giveItems(_275_BlackWingedSpies.Darkwing_Bat_Fang, _count);
		st.playSound(_count == max_inc ? Quest.SOUND_MIDDLE : Quest.SOUND_ITEMGET);
		if(_count == max_inc)
			st.set("cond", "2");
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState qs)
	{
		if(qs.getState() != 2)
			return null;
		final int npcId = npc.getNpcId();
		final long Darkwing_Bat_Fang_count = qs.getQuestItemsCount(_275_BlackWingedSpies.Darkwing_Bat_Fang);
		if(npcId == _275_BlackWingedSpies.Darkwing_Bat && Darkwing_Bat_Fang_count < 70L)
		{
			if(Darkwing_Bat_Fang_count > 10L && Darkwing_Bat_Fang_count < 65L && Rnd.chance(_275_BlackWingedSpies.Varangkas_Parasite_Chance))
			{
				spawn_Varangkas_Tracker(qs);
				return null;
			}
			give_Darkwing_Bat_Fang(qs, 1L);
		}
		else if(npcId == _275_BlackWingedSpies.Varangkas_Tracker && Darkwing_Bat_Fang_count < 70L && qs.getQuestItemsCount(_275_BlackWingedSpies.Varangkas_Parasite) > 0L)
		{
			qs.takeItems(_275_BlackWingedSpies.Varangkas_Parasite, -1L);
			give_Darkwing_Bat_Fang(qs, 5L);
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
		_275_BlackWingedSpies.Tantus = 30567;
		_275_BlackWingedSpies.Darkwing_Bat = 20316;
		_275_BlackWingedSpies.Varangkas_Tracker = 27043;
		_275_BlackWingedSpies.Darkwing_Bat_Fang = 1478;
		_275_BlackWingedSpies.Varangkas_Parasite = 1479;
		_275_BlackWingedSpies.Varangkas_Parasite_Chance = 10;
	}
}
