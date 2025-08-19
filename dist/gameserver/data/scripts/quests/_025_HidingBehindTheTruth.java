package quests;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _025_HidingBehindTheTruth extends Quest implements ScriptFile
{
	private final int AGRIPEL = 31348;
	private final int BENEDICT = 31349;
	private final int BROKEN_BOOK_SHELF = 31534;
	private final int COFFIN = 31536;
	private final int MAID_OF_LIDIA = 31532;
	private final int MYSTERIOUS_WIZARD = 31522;
	private final int TOMBSTONE = 31531;
	private final int CONTRACT = 7066;
	private final int EARRING_OF_BLESSING = 874;
	private final int GEMSTONE_KEY = 7157;
	private final int LIDIAS_DRESS = 7155;
	private final int MAP_FOREST_OF_DEADMAN = 7063;
	private final int NECKLACE_OF_BLESSING = 936;
	private final int RING_OF_BLESSING = 905;
	private final int SUSPICIOUS_TOTEM_DOLL_1 = 7151;
	private final int SUSPICIOUS_TOTEM_DOLL_2 = 7156;
	private final int SUSPICIOUS_TOTEM_DOLL_3 = 7158;
	private final int TRIOLS_PAWN = 27218;
	private NpcInstance COFFIN_SPAWN;

	public _025_HidingBehindTheTruth()
	{
		super(false);
		COFFIN_SPAWN = null;
		this.addStartNpc(31349);
		this.addTalkId(new int[] { 31348 });
		this.addTalkId(new int[] { 31534 });
		this.addTalkId(new int[] { 31536 });
		this.addTalkId(new int[] { 31532 });
		this.addTalkId(new int[] { 31522 });
		this.addTalkId(new int[] { 31531 });
		this.addKillId(new int[] { 27218 });
		addQuestItem(new int[] { 7158 });
	}

	@Override
	public String onEvent(final String event, final QuestState qs, final NpcInstance npc)
	{
		if(!event.equalsIgnoreCase("StartQuest"))
		{
			if(event.equalsIgnoreCase("31349-10.htm"))
				qs.set("cond", "4");
			else if(event.equalsIgnoreCase("31348-08.htm"))
			{
				if(qs.getInt("cond") == 4)
				{
					qs.set("cond", "5");
					qs.takeItems(7151, -1L);
					qs.takeItems(7156, -1L);
					if(qs.getQuestItemsCount(7157) == 0L)
						qs.giveItems(7157, 1L);
				}
				else if(qs.getInt("cond") == 5)
					return "31348-08a.htm";
			}
			else if(event.equalsIgnoreCase("31522-04.htm"))
			{
				qs.set("cond", "6");
				if(qs.getQuestItemsCount(7063) == 0L)
					qs.giveItems(7063, 1L);
			}
			else if(event.equalsIgnoreCase("31534-07.htm"))
			{
				final Player player = qs.getPlayer();
				qs.addSpawn(27218, player.getX() + 50, player.getY() + 50, player.getZ());
				qs.set("cond", "7");
			}
			else if(event.equalsIgnoreCase("31534-11.htm"))
			{
				qs.set("id", "8");
				qs.giveItems(7066, 1L);
			}
			else if(event.equalsIgnoreCase("31532-07.htm"))
				qs.set("cond", "11");
			else if(event.equalsIgnoreCase("31531-02.htm"))
			{
				qs.set("cond", "12");
				final Player player = qs.getPlayer();
				if(COFFIN_SPAWN != null)
					COFFIN_SPAWN.deleteMe();
				COFFIN_SPAWN = qs.addSpawn(31536, player.getX() + 50, player.getY() + 50, player.getZ());
				qs.startQuestTimer("Coffin_Despawn", 120000L);
			}
			else
			{
				if(event.equalsIgnoreCase("Coffin_Despawn"))
				{
					if(COFFIN_SPAWN != null)
						COFFIN_SPAWN.deleteMe();
					if(qs.getInt("cond") == 12)
						qs.set("cond", "11");
					return null;
				}
				if(event.equalsIgnoreCase("Lidia_wait"))
				{
					qs.set("id", "14");
					return null;
				}
				if(event.equalsIgnoreCase("31532-21.htm"))
					qs.set("cond", "15");
				else if(event.equalsIgnoreCase("31522-13.htm"))
					qs.set("cond", "16");
				else if(event.equalsIgnoreCase("31348-16.htm"))
					qs.set("cond", "17");
				else if(event.equalsIgnoreCase("31348-17.htm"))
					qs.set("cond", "18");
				else if(event.equalsIgnoreCase("31348-14.htm"))
					qs.set("id", "16");
				else if(event.equalsIgnoreCase("End1"))
				{
					if(qs.getInt("cond") != 17)
						return "31532-24.htm";
					qs.giveItems(905, 2L);
					qs.giveItems(874, 1L);
					qs.addExpAndSp(572277L, 53750L);
					qs.exitCurrentQuest(false);
					return "31532-25.htm";
				}
				else if(event.equalsIgnoreCase("End2"))
				{
					if(qs.getInt("cond") != 18)
						return "31522-15a.htm";
					qs.giveItems(936, 1L);
					qs.giveItems(874, 1L);
					qs.addExpAndSp(572277L, 53750L);
					qs.exitCurrentQuest(false);
					return "31522-16.htm";
				}
			}
			return event;
		}
		if(qs.getInt("cond") == 0)
			qs.setState(2);
		final QuestState qs_24 = qs.getPlayer().getQuestState(24);
		if(qs_24 == null || !qs_24.isCompleted())
		{
			qs.set("cond", "1");
			return "31349-02.htm";
		}
		qs.playSound(Quest.SOUND_ACCEPT);
		if(qs.getQuestItemsCount(7151) == 0L)
		{
			qs.set("cond", "2");
			return "31349-03a.htm";
		}
		return "31349-03.htm";
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		final int IntId = st.getInt("id");
		switch(npcId)
		{
			case 31349:
			{
				if(cond == 0 || cond == 1)
					return "31349-01.htm";
				if(cond == 2)
					return st.getQuestItemsCount(7151) == 0L ? "31349-03a.htm" : "31349-03.htm";
				if(cond == 3)
					return "31349-03.htm";
				if(cond == 4)
					return "31349-11.htm";
				break;
			}
			case 31522:
			{
				if(cond == 2)
				{
					st.set("cond", "3");
					st.giveItems(7156, 1L);
					return "31522-01.htm";
				}
				if(cond == 3)
					return "31522-02.htm";
				if(cond == 5)
					return "31522-03.htm";
				if(cond == 6)
					return "31522-05.htm";
				if(cond == 8)
				{
					if(IntId != 8)
						return "31522-05.htm";
					st.set("cond", "9");
					return "31522-06.htm";
				}
				else
				{
					if(cond == 15)
						return "31522-06a.htm";
					if(cond == 16)
						return "31522-12.htm";
					if(cond == 17)
						return "31522-15a.htm";
					if(cond == 18)
					{
						st.set("id", "18");
						return "31522-15.htm";
					}
					break;
				}
			}
			case 31348:
			{
				if(cond == 4)
					return "31348-01.htm";
				if(cond == 5)
					return "31348-03.htm";
				if(cond == 16)
					return IntId == 16 ? "31348-15.htm" : "31348-09.htm";
				if(cond == 17 || cond == 18)
					return "31348-15.htm";
				break;
			}
			case 31534:
			{
				if(cond == 6)
					return "31534-01.htm";
				if(cond == 7)
					return "31534-08.htm";
				if(cond == 8)
					return IntId == 8 ? "31534-06.htm" : "31534-10.htm";
				break;
			}
			case 31532:
			{
				if(cond == 9)
					return st.getQuestItemsCount(7066) > 0L ? "31532-01.htm" : "You have no Contract...";
				if(cond == 11 || cond == 12)
					return "31532-08.htm";
				if(cond == 13)
				{
					if(st.getQuestItemsCount(7155) == 0L)
						return "31532-08.htm";
					st.set("cond", "14");
					st.startQuestTimer("Lidia_wait", 60000L);
					st.takeItems(7155, 1L);
					return "31532-09.htm";
				}
				else
				{
					if(cond == 14)
						return IntId == 14 ? "31532-10.htm" : "31532-09.htm";
					if(cond == 17)
					{
						st.set("id", "17");
						return "31532-23.htm";
					}
					if(cond == 18)
						return "31532-24.htm";
					break;
				}
			}
			case 31531:
			{
				if(cond == 11)
					return "31531-01.htm";
				if(cond == 12)
					return "31531-02.htm";
				if(cond == 13)
					return "31531-03.htm";
				break;
			}
			case 31536:
			{
				if(cond == 12)
				{
					st.set("cond", "13");
					st.giveItems(7155, 1L);
					return "31536-01.htm";
				}
				if(cond == 13)
					return "31531-03.htm";
				break;
			}
		}
		return "noquest";
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState qs)
	{
		if(qs == null)
			return null;
		if(qs.getState() != 2)
			return null;
		final int npcId = npc.getNpcId();
		final int cond = qs.getInt("cond");
		if(npcId == 27218 && cond == 7)
		{
			qs.giveItems(7158, 1L);
			qs.playSound(Quest.SOUND_MIDDLE);
			qs.set("cond", "8");
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
}
