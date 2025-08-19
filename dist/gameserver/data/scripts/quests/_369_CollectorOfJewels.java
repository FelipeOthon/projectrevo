package quests;

import java.util.HashMap;
import java.util.Map;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _369_CollectorOfJewels extends Quest implements ScriptFile
{
	private static int NELL;
	private static int Roxide;
	private static int Rowin_Undine;
	private static int Lakin_Undine;
	private static int Salamander_Rowin;
	private static int Lakin_Salamander;
	private static int Death_Fire;
	private static int FLARE_SHARD;
	private static int FREEZING_SHARD;
	private final Map<Integer, int[]> DROPLIST;

	public _369_CollectorOfJewels()
	{
		super(false);
		DROPLIST = new HashMap<Integer, int[]>();
		this.addStartNpc(_369_CollectorOfJewels.NELL);
		this.addKillId(new int[] { _369_CollectorOfJewels.Roxide });
		this.addKillId(new int[] { _369_CollectorOfJewels.Rowin_Undine });
		this.addKillId(new int[] { _369_CollectorOfJewels.Lakin_Undine });
		this.addKillId(new int[] { _369_CollectorOfJewels.Salamander_Rowin });
		this.addKillId(new int[] { _369_CollectorOfJewels.Lakin_Salamander });
		this.addKillId(new int[] { _369_CollectorOfJewels.Death_Fire });
		addQuestItem(new int[] { _369_CollectorOfJewels.FLARE_SHARD });
		addQuestItem(new int[] { _369_CollectorOfJewels.FREEZING_SHARD });
		DROPLIST.put(_369_CollectorOfJewels.Roxide, new int[] { _369_CollectorOfJewels.FREEZING_SHARD, 85 });
		DROPLIST.put(_369_CollectorOfJewels.Rowin_Undine, new int[] { _369_CollectorOfJewels.FREEZING_SHARD, 73 });
		DROPLIST.put(_369_CollectorOfJewels.Lakin_Undine, new int[] { _369_CollectorOfJewels.FREEZING_SHARD, 60 });
		DROPLIST.put(_369_CollectorOfJewels.Salamander_Rowin, new int[] { _369_CollectorOfJewels.FLARE_SHARD, 77 });
		DROPLIST.put(_369_CollectorOfJewels.Lakin_Salamander, new int[] { _369_CollectorOfJewels.FLARE_SHARD, 77 });
		DROPLIST.put(_369_CollectorOfJewels.Death_Fire, new int[] { _369_CollectorOfJewels.FLARE_SHARD, 85 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("30376-03.htm") && st.getState() == 1)
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30376-08.htm") && st.getState() == 2)
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
		if(npc.getNpcId() != _369_CollectorOfJewels.NELL)
			return htmltext;
		final int _state = st.getState();
		if(_state == 1)
		{
			if(st.getPlayer().getLevel() >= 25)
			{
				st.set("cond", "0");
				return "30376-02.htm";
			}
			st.exitCurrentQuest(true);
			return "30376-01.htm";
		}
		else
		{
			if(_state != 2)
				return htmltext;
			final int cond = st.getInt("cond");
			if(cond == 1)
				htmltext = "30376-04.htm";
			else if(cond == 3)
				htmltext = "30376-09.htm";
			else if(cond == 2 || cond == 4)
			{
				final int max_count = cond == 2 ? 50 : 200;
				final long FLARE_SHARD_COUNT = st.getQuestItemsCount(_369_CollectorOfJewels.FLARE_SHARD);
				final long FREEZING_SHARD_COUNT = st.getQuestItemsCount(_369_CollectorOfJewels.FREEZING_SHARD);
				if(FLARE_SHARD_COUNT != max_count || FREEZING_SHARD_COUNT != max_count)
				{
					st.set("cond", String.valueOf(cond - 1));
					return onTalk(npc, st);
				}
				st.takeItems(_369_CollectorOfJewels.FLARE_SHARD, -1L);
				st.takeItems(_369_CollectorOfJewels.FREEZING_SHARD, -1L);
				if(cond == 2)
				{
					htmltext = "30376-05.htm";
					st.giveItems(57, 12500L);
					st.playSound(Quest.SOUND_MIDDLE);
					st.set("cond", "3");
				}
				else
				{
					htmltext = "30376-10.htm";
					st.giveItems(57, 63500L);
					st.playSound(Quest.SOUND_FINISH);
					st.exitCurrentQuest(true);
				}
			}
			return htmltext;
		}
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState qs)
	{
		final int cond = qs.getCond();
		if(cond != 1 && cond != 3)
			return null;
		final int[] drop = DROPLIST.get(npc.getNpcId());
		if(drop == null)
			return null;
		final int max_count = cond == 1 ? 50 : 200;
		if(qs.getQuestItemsCount(drop[0]) < max_count && qs.rollAndGive(drop[0], 1, 1, max_count, drop[1]) && qs.getQuestItemsCount(_369_CollectorOfJewels.FLARE_SHARD) >= max_count && qs.getQuestItemsCount(_369_CollectorOfJewels.FREEZING_SHARD) >= max_count)
			qs.setCond(cond == 1 ? 2 : 4);
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
		_369_CollectorOfJewels.NELL = 30376;
		_369_CollectorOfJewels.Roxide = 20747;
		_369_CollectorOfJewels.Rowin_Undine = 20619;
		_369_CollectorOfJewels.Lakin_Undine = 20616;
		_369_CollectorOfJewels.Salamander_Rowin = 20612;
		_369_CollectorOfJewels.Lakin_Salamander = 20609;
		_369_CollectorOfJewels.Death_Fire = 20749;
		_369_CollectorOfJewels.FLARE_SHARD = 5882;
		_369_CollectorOfJewels.FREEZING_SHARD = 5883;
	}
}
