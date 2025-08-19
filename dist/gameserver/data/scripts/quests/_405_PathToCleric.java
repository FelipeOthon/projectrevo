package quests;

import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _405_PathToCleric extends Quest implements ScriptFile
{
	public final int GALLINT = 30017;
	public final int ZIGAUNT = 30022;
	public final int VIVYAN = 30030;
	public final int SIMPLON = 30253;
	public final int PRAGA = 30333;
	public final int LIONEL = 30408;
	public final int RUIN_ZOMBIE = 20026;
	public final int RUIN_ZOMBIE_LEADER = 20029;
	public final int LETTER_OF_ORDER1 = 1191;
	public final int LETTER_OF_ORDER2 = 1192;
	public final int BOOK_OF_LEMONIELL = 1193;
	public final int BOOK_OF_VIVI = 1194;
	public final int BOOK_OF_SIMLON = 1195;
	public final int BOOK_OF_PRAGA = 1196;
	public final int CERTIFICATE_OF_GALLINT = 1197;
	public final int PENDANT_OF_MOTHER = 1198;
	public final int NECKLACE_OF_MOTHER = 1199;
	public final int LEMONIELLS_COVENANT = 1200;
	public final int MARK_OF_FAITH = 1201;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _405_PathToCleric()
	{
		super(false);
		this.addStartNpc(30022);
		this.addTalkId(new int[] { 30017 });
		this.addTalkId(new int[] { 30030 });
		this.addTalkId(new int[] { 30253 });
		this.addTalkId(new int[] { 30333 });
		this.addTalkId(new int[] { 30408 });
		this.addKillId(new int[] { 20026 });
		this.addKillId(new int[] { 20029 });
		addQuestItem(new int[] { 1200, 1192, 1196, 1194, 1195, 1191, 1199, 1198, 1197, 1193 });
	}

	public void checkBooks(final QuestState st)
	{
		if(st.getQuestItemsCount(1196) + st.getQuestItemsCount(1194) + st.getQuestItemsCount(1195) >= 5L)
			st.set("cond", "2");
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("1"))
			if(st.getPlayer().getLevel() >= 18 && st.getPlayer().getClassId().getId() == 10 && st.getQuestItemsCount(1201) < 1L)
			{
				st.set("cond", "1");
				st.setState(2);
				st.playSound(Quest.SOUND_ACCEPT);
				st.giveItems(1191, 1L);
				htmltext = "gigon_q0405_05.htm";
			}
			else if(st.getPlayer().getClassId().getId() != 10)
			{
				if(st.getPlayer().getClassId().getId() == 15)
					htmltext = "gigon_q0405_02a.htm";
				else
					htmltext = "gigon_q0405_02.htm";
			}
			else if(st.getPlayer().getLevel() < 18 && st.getPlayer().getClassId().getId() == 10)
				htmltext = "gigon_q0405_03.htm";
			else if(st.getPlayer().getLevel() >= 18 && st.getPlayer().getClassId().getId() == 10 && st.getQuestItemsCount(1201) > 0L)
				htmltext = "gigon_q0405_04.htm";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 30022)
		{
			if(st.getQuestItemsCount(1201) > 0L)
			{
				htmltext = "gigon_q0405_04.htm";
				st.exitCurrentQuest(true);
			}
			if(cond < 1 && st.getQuestItemsCount(1201) < 1L)
				htmltext = "gigon_q0405_01.htm";
			else if(cond == 1 | cond == 2 && st.getQuestItemsCount(1191) > 0L)
			{
				if(st.getQuestItemsCount(1194) > 0L && st.getQuestItemsCount(1195) > 2L && st.getQuestItemsCount(1196) > 0L)
				{
					htmltext = "gigon_q0405_08.htm";
					st.takeItems(1196, -1L);
					st.takeItems(1194, -1L);
					st.takeItems(1195, -1L);
					st.takeItems(1191, -1L);
					st.giveItems(1192, 1L);
					st.set("cond", "3");
				}
				else
					htmltext = "gigon_q0405_06.htm";
			}
			else if(cond < 6 && st.getQuestItemsCount(1192) > 0L)
				htmltext = "gigon_q0405_07.htm";
			else if(cond == 6 && st.getQuestItemsCount(1192) > 0L && st.getQuestItemsCount(1200) > 0L)
			{
				htmltext = "gigon_q0405_09.htm";
				st.takeItems(1200, -1L);
				st.takeItems(1192, -1L);
				if(!st.getPlayer().getVarBoolean("q405"))
					st.getPlayer().setVar("q405", "1");
				st.exitCurrentQuest(true);
				if(st.getPlayer().getClassId().getLevel() == 1)
				{
					st.giveItems(1201, 1L);
					if(!st.getPlayer().getVarBoolean("prof1"))
					{
						st.getPlayer().setVar("prof1", "1");
						st.addExpAndSp(295862L, 17664L, true);
						st.giveItems(57, 81900L, Config.RATE_QUESTS_OCCUPATION_CHANGE);
					}
				}
				st.playSound(Quest.SOUND_FINISH);
			}
		}
		else if(npcId == 30253 && cond == 1 && st.getQuestItemsCount(1191) > 0L)
		{
			if(st.getQuestItemsCount(1195) < 1L)
			{
				htmltext = "trader_simplon_q0405_01.htm";
				st.giveItems(1195, 3L);
				checkBooks(st);
			}
			else if(st.getQuestItemsCount(1195) > 2L)
				htmltext = "trader_simplon_q0405_02.htm";
		}
		else if(npcId == 30030 && cond == 1 && st.getQuestItemsCount(1191) > 0L)
		{
			if(st.getQuestItemsCount(1194) < 1L)
			{
				htmltext = "vivi_q0405_01.htm";
				st.giveItems(1194, 1L);
				checkBooks(st);
			}
			else if(st.getQuestItemsCount(1194) > 0L)
				htmltext = "vivi_q0405_02.htm";
		}
		else if(npcId == 30333 && cond == 1 && st.getQuestItemsCount(1191) > 0L)
		{
			if(st.getQuestItemsCount(1196) < 1L && st.getQuestItemsCount(1199) < 1L)
			{
				htmltext = "guard_praga_q0405_01.htm";
				st.giveItems(1199, 1L);
			}
			else if(st.getQuestItemsCount(1196) < 1L && st.getQuestItemsCount(1199) > 0L && st.getQuestItemsCount(1198) < 1L)
				htmltext = "guard_praga_q0405_02.htm";
			else if(st.getQuestItemsCount(1196) < 1L && st.getQuestItemsCount(1199) > 0L && st.getQuestItemsCount(1198) > 0L)
			{
				htmltext = "guard_praga_q0405_03.htm";
				st.takeItems(1199, -1L);
				st.takeItems(1198, -1L);
				st.giveItems(1196, 1L);
				checkBooks(st);
			}
			else if(st.getQuestItemsCount(1196) > 0L)
				htmltext = "guard_praga_q0405_04.htm";
		}
		else if(npcId == 30408)
		{
			if(st.getQuestItemsCount(1192) < 1L)
				htmltext = "lemoniell_q0405_02.htm";
			else if(cond == 3 && st.getQuestItemsCount(1192) == 1L && st.getQuestItemsCount(1193) < 1L && st.getQuestItemsCount(1200) < 1L && st.getQuestItemsCount(1197) < 1L)
			{
				htmltext = "lemoniell_q0405_01.htm";
				st.giveItems(1193, 1L);
				st.set("cond", "4");
			}
			else if(cond == 4 && st.getQuestItemsCount(1192) == 1L && st.getQuestItemsCount(1193) > 0L && st.getQuestItemsCount(1200) < 1L && st.getQuestItemsCount(1197) < 1L)
				htmltext = "lemoniell_q0405_03.htm";
			else if(st.getQuestItemsCount(1192) == 1L && st.getQuestItemsCount(1193) < 1L && st.getQuestItemsCount(1200) < 1L && st.getQuestItemsCount(1197) > 0L)
			{
				htmltext = "lemoniell_q0405_04.htm";
				st.takeItems(1197, -1L);
				st.giveItems(1200, 1L);
				st.set("cond", "6");
			}
			else if(st.getQuestItemsCount(1192) == 1L && st.getQuestItemsCount(1193) < 1L && st.getQuestItemsCount(1200) > 0L && st.getQuestItemsCount(1197) < 1L)
				htmltext = "lemoniell_q0405_05.htm";
		}
		else if(npcId == 30017 && st.getQuestItemsCount(1192) > 0L)
			if(cond == 4 && st.getQuestItemsCount(1193) > 0L && st.getQuestItemsCount(1197) < 1L)
			{
				htmltext = "gallin_q0405_01.htm";
				st.takeItems(1193, -1L);
				st.giveItems(1197, 1L);
				st.set("cond", "5");
			}
			else if(cond == 5 && st.getQuestItemsCount(1193) < 1L && st.getQuestItemsCount(1197) > 0L)
				htmltext = "gallin_q0405_02.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		if(npcId == 20026 | npcId == 20029 && st.getInt("cond") == 1 && st.getQuestItemsCount(1198) < 1L)
		{
			st.giveItems(1198, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		return null;
	}
}
