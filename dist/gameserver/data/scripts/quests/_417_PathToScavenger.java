package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _417_PathToScavenger extends Quest implements ScriptFile
{
	private static final int RING_OF_RAVEN = 1642;
	private static final int PIPIS_LETTER = 1643;
	private static final int ROUTS_TP_SCROLL = 1644;
	private static final int SUCCUBUS_UNDIES = 1645;
	private static final int MIONS_LETTER = 1646;
	private static final int BRONKS_INGOT = 1647;
	private static final int CHARIS_AXE = 1648;
	private static final int ZIMENFS_POTION = 1649;
	private static final int BRONKS_PAY = 1650;
	private static final int CHALIS_PAY = 1651;
	private static final int ZIMENFS_PAY = 1652;
	private static final int BEAR_PIC = 1653;
	private static final int TARANTULA_PIC = 1654;
	private static final int HONEY_JAR = 1655;
	private static final int BEAD = 1656;
	private static final int BEAD_PARCEL = 1657;
	private static final int Pippi = 30524;
	private static final int Raut = 30316;
	private static final int Shari = 30517;
	private static final int Mion = 30519;
	private static final int Bronk = 30525;
	private static final int Zimenf = 30538;
	private static final int Toma = 30556;
	private static final int Torai = 30557;
	private static final int HunterTarantula = 20403;
	private static final int HoneyBear = 27058;
	private static final int PlunderTarantula = 20508;
	private static final int HunterBear = 20777;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _417_PathToScavenger()
	{
		super(false);
		this.addStartNpc(30524);
		this.addTalkId(new int[] { 30316, 30517, 30519, 30525, 30538, 30556, 30557 });
		this.addKillId(new int[] { 20777 });
		addQuestItem(new int[] { 1651, 1652, 1650, 1643, 1648, 1649, 1647, 1646, 1655, 1653, 1657, 1656, 1654, 1645, 1644 });
		addSkillUseId(27058);
		addSkillUseId(20508);
		addSkillUseId(20403);
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		final int cond = st.getCond();
		final Player player = st.getPlayer();
		if(event.equals("1"))
		{
			st.set("id", "0");
			if(player.getLevel() >= 18 && player.getClassId() == ClassId.DwarvenFighter && st.getQuestItemsCount(1642) == 0L)
			{
				st.setCond(1);
				st.setState(2);
				st.playSound(Quest.SOUND_ACCEPT);
				st.giveItems(1643, 1L);
				htmltext = "collector_pipi_q0417_05.htm";
			}
			else if(player.getClassId() != ClassId.DwarvenFighter)
			{
				if(player.getClassId().getId() == 54)
					htmltext = "collector_pipi_q0417_02a.htm";
				else
					htmltext = "collector_pipi_q0417_08.htm";
			}
			else if(player.getLevel() < 18 && player.getClassId() == ClassId.DwarvenFighter)
				htmltext = "collector_pipi_q0417_02.htm";
			else if(player.getLevel() >= 18 && player.getClassId() == ClassId.DwarvenFighter && st.getQuestItemsCount(1642) == 1L)
				htmltext = "collector_pipi_q0417_04.htm";
		}
		else if(event.equals("30519_1"))
		{
			if(st.getQuestItemsCount(1643) > 0L)
			{
				st.takeItems(1643, 1L);
				st.setCond(2);
				final int n = Rnd.get(3);
				if(n == 0)
				{
					htmltext = "trader_mion_q0417_02.htm";
					st.giveItems(1649, 1L);
				}
				else if(n == 1)
				{
					htmltext = "trader_mion_q0417_03.htm";
					st.giveItems(1648, 1L);
				}
				else if(n == 2)
				{
					htmltext = "trader_mion_q0417_04.htm";
					st.giveItems(1647, 1L);
				}
			}
			else
				htmltext = "noquest";
		}
		else if(event.equals("30519_2"))
			htmltext = "trader_mion_q0417_06.htm";
		else if(event.equals("30519_3"))
		{
			htmltext = "trader_mion_q0417_07.htm";
			st.set("id", st.getInt("id") + 1);
		}
		else if(event.equals("30519_4"))
			htmltext = Rnd.nextBoolean() ? "trader_mion_q0417_06.htm" : "trader_mion_q0417_11.htm";
		else if(event.equals("30519_5"))
		{
			if(st.getQuestItemsCount(new int[] { 1649, 1648, 1647 }) > 0L)
			{
				if(st.getInt("id") / 10 < 2)
				{
					htmltext = "trader_mion_q0417_07.htm";
					st.set("id", st.getInt("id") + 1);
				}
				else if(st.getInt("id") / 10 >= 2 && cond == 0)
				{
					htmltext = "trader_mion_q0417_09.htm";
					if(st.getInt("id") / 10 < 3)
						st.set("id", st.getInt("id") + 1);
				}
				else if(st.getInt("id") / 10 >= 3 && cond > 0)
				{
					htmltext = "trader_mion_q0417_10.htm";
					st.giveItems(1646, 1L);
					st.takeItems(1648, 1L);
					st.takeItems(1649, 1L);
					st.takeItems(1647, 1L);
				}
			}
			else
				htmltext = "noquest";
		}
		else if(event.equals("30519_6"))
		{
			if(st.getQuestItemsCount(1652) > 0L || st.getQuestItemsCount(1651) > 0L || st.getQuestItemsCount(1650) > 0L)
			{
				final int n = Rnd.get(3);
				st.takeItems(1652, 1L);
				st.takeItems(1651, 1L);
				st.takeItems(1650, 1L);
				if(n == 0)
				{
					htmltext = "trader_mion_q0417_02.htm";
					st.giveItems(1649, 1L);
				}
				else if(n == 1)
				{
					htmltext = "trader_mion_q0417_03.htm";
					st.giveItems(1648, 1L);
				}
				else if(n == 2)
				{
					htmltext = "trader_mion_q0417_04.htm";
					st.giveItems(1647, 1L);
				}
			}
			else
				htmltext = "noquest";
		}
		else if(event.equals("30316_1"))
		{
			if(st.getQuestItemsCount(1657) > 0L)
			{
				htmltext = "raut_q0417_02.htm";
				st.takeItems(1657, 1L);
				st.giveItems(1644, 1L);
				st.setCond(10);
			}
			else
				htmltext = "noquest";
		}
		else if(event.equals("30316_2"))
		{
			if(st.getQuestItemsCount(1657) > 0L)
			{
				htmltext = "raut_q0417_03.htm";
				st.takeItems(1657, 1L);
				st.giveItems(1644, 1L);
				st.setCond(10);
			}
			else
				htmltext = "noquest";
		}
		else if(event.equals("30557_1"))
			htmltext = "torai_q0417_02.htm";
		else if(event.equals("30557_2"))
			if(st.getQuestItemsCount(1644) > 0L)
			{
				htmltext = "torai_q0417_03.htm";
				st.takeItems(1644, 1L);
				st.giveItems(1645, 1L);
				st.setCond(11);
			}
			else
				htmltext = "noquest";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int id = st.getState();
		final int cond = st.getCond();
		if(id == 1)
			st.setState(2);
		if(npcId == 30524)
		{
			if(cond == 0)
				htmltext = "collector_pipi_q0417_01.htm";
			else if(st.getQuestItemsCount(1643) > 0L)
				htmltext = "collector_pipi_q0417_06.htm";
			else if(st.getQuestItemsCount(1643) == 0L && id == 2)
				htmltext = "collector_pipi_q0417_01.htm";
			else if(st.getQuestItemsCount(1643) == 0L)
				htmltext = "collector_pipi_q0417_07.htm";
		}
		else
		{
			if(cond == 0)
				return "noquest";
			if(npcId == 30519)
			{
				if(st.getQuestItemsCount(1643) > 0L)
					htmltext = "trader_mion_q0417_01.htm";
				else if(st.getQuestItemsCount(new int[] { 1648, 1647, 1649 }) > 0L && st.getInt("id") / 10 == 0)
					htmltext = "trader_mion_q0417_05.htm";
				else if(st.getQuestItemsCount(new int[] { 1648, 1647, 1649 }) > 0L && st.getInt("id") / 10 > 0)
					htmltext = "trader_mion_q0417_08.htm";
				else if(st.getQuestItemsCount(new int[] { 1651, 1650, 1652 }) > 0L && st.getInt("id") < 50)
					htmltext = "trader_mion_q0417_12.htm";
				else if(st.getQuestItemsCount(new int[] { 1651, 1650, 1652 }) > 0L && st.getInt("id") >= 50)
				{
					htmltext = "trader_mion_q0417_15.htm";
					st.giveItems(1646, 1L);
					st.takeItems(1651, -1L);
					st.takeItems(1652, -1L);
					st.takeItems(1650, -1L);
					st.setCond(4);
				}
				else if(st.getQuestItemsCount(1646) > 0L)
					htmltext = "trader_mion_q0417_13.htm";
				else if(st.getQuestItemsCount(1653) > 0L || st.getQuestItemsCount(1654) > 0L || st.getQuestItemsCount(1657) > 0L || st.getQuestItemsCount(1644) > 0L || st.getQuestItemsCount(1645) > 0L)
					htmltext = "trader_mion_q0417_14.htm";
			}
			else if(npcId == 30517)
			{
				if(st.getQuestItemsCount(1648) > 0L)
				{
					if(st.getInt("id") < 20)
						htmltext = "trader_chali_q0417_01.htm";
					else
						htmltext = "trader_chali_q0417_02.htm";
					st.takeItems(1648, 1L);
					st.giveItems(1651, 1L);
					if(st.getInt("id") >= 50)
						st.setCond(3);
					st.set("id", st.getInt("id") + 10);
				}
				else if(st.getQuestItemsCount(1651) == 1L)
					htmltext = "trader_chali_q0417_03.htm";
			}
			else if(npcId == 30525)
			{
				if(st.getQuestItemsCount(1647) == 1L)
				{
					if(st.getInt("id") < 20)
						htmltext = "head_blacksmith_bronk_q0417_01.htm";
					else
						htmltext = "head_blacksmith_bronk_q0417_02.htm";
					st.takeItems(1647, 1L);
					st.giveItems(1650, 1L);
					if(st.getInt("id") >= 50)
						st.setCond(3);
					st.set("id", st.getInt("id") + 10);
				}
				else if(st.getQuestItemsCount(1650) == 1L)
					htmltext = "head_blacksmith_bronk_q0417_03.htm";
			}
			else if(npcId == 30538)
			{
				if(st.getQuestItemsCount(1649) == 1L)
				{
					if(st.getInt("id") < 20)
						htmltext = "zimenf_priest_of_earth_q0417_01.htm";
					else
						htmltext = "zimenf_priest_of_earth_q0417_02.htm";
					st.takeItems(1649, 1L);
					st.giveItems(1652, 1L);
					if(st.getInt("id") >= 50)
						st.setCond(3);
					st.set("id", st.getInt("id") + 10);
				}
				else if(st.getQuestItemsCount(1652) == 1L)
					htmltext = "zimenf_priest_of_earth_q0417_03.htm";
			}
			else if(npcId == 30556)
			{
				if(st.getQuestItemsCount(1646) == 1L)
				{
					htmltext = "master_toma_q0417_01.htm";
					st.takeItems(1646, 1L);
					st.giveItems(1653, 1L);
					st.setCond(5);
					st.set("id", 0);
				}
				else if(st.getQuestItemsCount(1653) == 1L && st.getQuestItemsCount(1655) < 5L)
					htmltext = "master_toma_q0417_02.htm";
				else if(st.getQuestItemsCount(1653) == 1L && st.getQuestItemsCount(1655) >= 5L)
				{
					htmltext = "master_toma_q0417_03.htm";
					st.takeItems(1655, st.getQuestItemsCount(1655));
					st.takeItems(1653, 1L);
					st.giveItems(1654, 1L);
					st.setCond(7);
				}
				else if(st.getQuestItemsCount(1654) == 1L && st.getQuestItemsCount(1656) < 20L)
					htmltext = "master_toma_q0417_04.htm";
				else if(st.getQuestItemsCount(1654) == 1L && st.getQuestItemsCount(1656) >= 20L)
				{
					htmltext = "master_toma_q0417_05.htm";
					st.takeItems(1656, st.getQuestItemsCount(1656));
					st.takeItems(1654, 1L);
					st.giveItems(1657, 1L);
					st.setCond(9);
				}
				else if(st.getQuestItemsCount(1657) > 0L)
					htmltext = "master_toma_q0417_06.htm";
				else if(st.getQuestItemsCount(1644) > 0L || st.getQuestItemsCount(1645) > 0L)
					htmltext = "master_toma_q0417_07.htm";
			}
			else if(npcId == 30316)
			{
				if(st.getQuestItemsCount(1657) == 1L)
					htmltext = "raut_q0417_01.htm";
				else if(st.getQuestItemsCount(1644) == 1L)
					htmltext = "raut_q0417_04.htm";
				else if(st.getQuestItemsCount(1645) == 1L)
				{
					htmltext = "raut_q0417_05.htm";
					st.takeItems(1645, 1L);
					if(st.getPlayer().getClassId().getLevel() == 1)
					{
						st.giveItems(1642, 1L);
						if(!st.getPlayer().getVarBoolean("prof1"))
						{
							st.getPlayer().setVar("prof1", "1");
							st.addExpAndSp(295862L, 24404L, true);
							st.giveItems(57, 81900L, Config.RATE_QUESTS_OCCUPATION_CHANGE);
						}
					}
					st.playSound(Quest.SOUND_FINISH);
					st.exitCurrentQuest(false);
				}
			}
			else if(npcId == 30557 && st.getQuestItemsCount(1644) == 1L)
				htmltext = "torai_q0417_01.htm";
		}
		return htmltext;
	}

	@Override
	public String onSkillUse(final NpcInstance npc, final Skill skill, final QuestState st)
	{
		if(skill.getId() != 42 && !((MonsterInstance) npc).isSpoiled())
			return null;
		if(npc.getNpcId() == 27058)
		{
			st.giveItems(1655, 1L);
			if(st.getQuestItemsCount(1655) == 5L)
			{
				st.playSound(Quest.SOUND_MIDDLE);
				st.setCond(6);
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		else if(st.getQuestItemsCount(1654) == 1L && Rnd.chance(90))
		{
			st.giveItems(1656, 1L);
			if(st.getQuestItemsCount(1656) == 20L)
			{
				st.playSound(Quest.SOUND_MIDDLE);
				st.setCond(8);
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getState() != 2)
			return null;
		if(npc.getNpcId() == 20777 && st.getQuestItemsCount(1653) == 1L && st.getQuestItemsCount(1655) < 5L)
		{
			final int flag = st.getInt("flag");
			if(Rnd.chance(flag))
				st.addSpawn(27058);
			else
				st.set("flag", flag + 1);
		}
		return null;
	}
}
