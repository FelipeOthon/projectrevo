package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _333_BlackLionHunt extends Quest implements ScriptFile
{
	private int BLACK_LION_MARK;
	private int CARGO_BOX1;
	private int UNDEAD_ASH;
	private int BLOODY_AXE_INSIGNIAS;
	private int DELU_FANG;
	private int STAKATO_TALONS;
	private int SOPHIAS_LETTER1;
	private int SOPHIAS_LETTER2;
	private int SOPHIAS_LETTER3;
	private int SOPHIAS_LETTER4;
	private int LIONS_CLAW;
	private int LIONS_EYE;
	private int GUILD_COIN;
	private int COMPLETE_STATUE;
	private int COMPLETE_TABLET;
	private int ALACRITY_POTION;
	private int SCROLL_ESCAPE;
	private int SOULSHOT_D;
	private int SPIRITSHOT_D;
	private int HEALING_POTION;
	private int OPEN_BOX_PRICE;
	private int GLUDIO_APPLE;
	private int CORN_MEAL;
	private int WOLF_PELTS;
	private int MONNSTONE;
	private int GLUDIO_WEETS_FLOWER;
	private int SPIDERSILK_ROPE;
	private int ALEXANDRIT;
	private int SILVER_TEA;
	private int GOLEM_PART;
	private int FIRE_EMERALD;
	private int SILK_FROCK;
	private int PORCELAN_URN;
	private int IMPERIAL_DIAMOND;
	private int STATUE_SHILIEN_HEAD;
	private int STATUE_SHILIEN_TORSO;
	private int STATUE_SHILIEN_ARM;
	private int STATUE_SHILIEN_LEG;
	private int FRAGMENT_ANCIENT_TABLE1;
	private int FRAGMENT_ANCIENT_TABLE2;
	private int FRAGMENT_ANCIENT_TABLE3;
	private int FRAGMENT_ANCIENT_TABLE4;
	private int Sophya;
	private int Redfoot;
	private int Rupio;
	private int Undrias;
	private int Lockirin;
	private int Morgan;
	int[] statue_list;
	int[] tablet_list;
	int[][] DROPLIST;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _333_BlackLionHunt()
	{
		super(false);
		BLACK_LION_MARK = 1369;
		CARGO_BOX1 = 3440;
		UNDEAD_ASH = 3848;
		BLOODY_AXE_INSIGNIAS = 3849;
		DELU_FANG = 3850;
		STAKATO_TALONS = 3851;
		SOPHIAS_LETTER1 = 3671;
		SOPHIAS_LETTER2 = 3672;
		SOPHIAS_LETTER3 = 3673;
		SOPHIAS_LETTER4 = 3674;
		LIONS_CLAW = 3675;
		LIONS_EYE = 3676;
		GUILD_COIN = 3677;
		COMPLETE_STATUE = 3461;
		COMPLETE_TABLET = 3466;
		ALACRITY_POTION = 735;
		SCROLL_ESCAPE = 736;
		SOULSHOT_D = 1463;
		SPIRITSHOT_D = 2510;
		HEALING_POTION = 1061;
		OPEN_BOX_PRICE = 650;
		GLUDIO_APPLE = 3444;
		CORN_MEAL = 3445;
		WOLF_PELTS = 3446;
		MONNSTONE = 3447;
		GLUDIO_WEETS_FLOWER = 3448;
		SPIDERSILK_ROPE = 3449;
		ALEXANDRIT = 3450;
		SILVER_TEA = 3451;
		GOLEM_PART = 3452;
		FIRE_EMERALD = 3453;
		SILK_FROCK = 3454;
		PORCELAN_URN = 3455;
		IMPERIAL_DIAMOND = 3456;
		STATUE_SHILIEN_HEAD = 3457;
		STATUE_SHILIEN_TORSO = 3458;
		STATUE_SHILIEN_ARM = 3459;
		STATUE_SHILIEN_LEG = 3460;
		FRAGMENT_ANCIENT_TABLE1 = 3462;
		FRAGMENT_ANCIENT_TABLE2 = 3463;
		FRAGMENT_ANCIENT_TABLE3 = 3464;
		FRAGMENT_ANCIENT_TABLE4 = 3465;
		Sophya = 30735;
		Redfoot = 30736;
		Rupio = 30471;
		Undrias = 30130;
		Lockirin = 30531;
		Morgan = 30737;
		statue_list = new int[] { STATUE_SHILIEN_HEAD, STATUE_SHILIEN_TORSO, STATUE_SHILIEN_ARM, STATUE_SHILIEN_LEG };
		tablet_list = new int[] { FRAGMENT_ANCIENT_TABLE1, FRAGMENT_ANCIENT_TABLE2, FRAGMENT_ANCIENT_TABLE3, FRAGMENT_ANCIENT_TABLE4 };
		DROPLIST = new int[][] {
				{ 20160, 1, 1, 67, 29, UNDEAD_ASH },
				{ 20171, 1, 1, 76, 31, UNDEAD_ASH },
				{ 20197, 1, 1, 89, 25, UNDEAD_ASH },
				{ 20200, 1, 1, 60, 28, UNDEAD_ASH },
				{ 20201, 1, 1, 70, 29, UNDEAD_ASH },
				{ 20202, 1, 0, 60, 24, UNDEAD_ASH },
				{ 20198, 1, 1, 60, 35, UNDEAD_ASH },
				{ 20207, 2, 1, 69, 29, BLOODY_AXE_INSIGNIAS },
				{ 20208, 2, 1, 67, 32, BLOODY_AXE_INSIGNIAS },
				{ 20209, 2, 1, 62, 33, BLOODY_AXE_INSIGNIAS },
				{ 20210, 2, 1, 78, 23, BLOODY_AXE_INSIGNIAS },
				{ 20211, 2, 1, 71, 22, BLOODY_AXE_INSIGNIAS },
				{ 20251, 3, 1, 70, 30, DELU_FANG },
				{ 20252, 3, 1, 67, 28, DELU_FANG },
				{ 20253, 3, 1, 65, 26, DELU_FANG },
				{ 27151, 3, 1, 69, 31, DELU_FANG },
				{ 20157, 4, 1, 66, 32, STAKATO_TALONS },
				{ 20230, 4, 1, 68, 26, STAKATO_TALONS },
				{ 20232, 4, 1, 67, 28, STAKATO_TALONS },
				{ 20234, 4, 1, 69, 32, STAKATO_TALONS },
				{ 27152, 4, 1, 69, 32, STAKATO_TALONS } };
		this.addStartNpc(Sophya);
		this.addTalkId(new int[] { Redfoot });
		this.addTalkId(new int[] { Rupio });
		this.addTalkId(new int[] { Undrias });
		this.addTalkId(new int[] { Lockirin });
		this.addTalkId(new int[] { Morgan });
		for(int i = 0; i < DROPLIST.length; ++i)
			this.addKillId(new int[] { DROPLIST[i][0] });
		addQuestItem(new int[] {
				LIONS_CLAW,
				LIONS_EYE,
				GUILD_COIN,
				UNDEAD_ASH,
				BLOODY_AXE_INSIGNIAS,
				DELU_FANG,
				STAKATO_TALONS,
				SOPHIAS_LETTER1,
				SOPHIAS_LETTER2,
				SOPHIAS_LETTER3,
				SOPHIAS_LETTER4 });
	}

	public void giveRewards(final QuestState st, final int item, final long count)
	{
		st.giveItems(57, 35L * count);
		st.takeItems(item, count);
		if(count >= 20L)
			st.giveItems(LIONS_CLAW, count / 20L * (long) st.getRateQuestsReward());
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int part = st.getInt("part");
		if(event.equalsIgnoreCase("start"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
			return "30735-01.htm";
		}
		if(event.equalsIgnoreCase("p1_t"))
		{
			st.set("part", "1");
			st.giveItems(SOPHIAS_LETTER1, 1L);
			return "30735-02.htm";
		}
		if(event.equalsIgnoreCase("p2_t"))
		{
			st.set("part", "2");
			st.giveItems(SOPHIAS_LETTER2, 1L);
			return "30735-03.htm";
		}
		if(event.equalsIgnoreCase("p3_t"))
		{
			st.set("part", "3");
			st.giveItems(SOPHIAS_LETTER3, 1L);
			return "30735-04.htm";
		}
		if(event.equalsIgnoreCase("p4_t"))
		{
			st.set("part", "4");
			st.giveItems(SOPHIAS_LETTER4, 1L);
			return "30735-05.htm";
		}
		if(event.equalsIgnoreCase("exit"))
		{
			st.giveItems(57, 12400L);
			st.takeItems(BLACK_LION_MARK, 1L);
			st.exitCurrentQuest(true);
			return "30735-exit.htm";
		}
		if(event.equalsIgnoreCase("continue"))
		{
			long claw = st.getQuestItemsCount(LIONS_CLAW) / 10L;
			final long check_eye = st.getQuestItemsCount(LIONS_EYE);
			if(claw <= 0L)
				return "30735-start.htm";
			st.giveItems(LIONS_EYE, claw);
			final long eye = st.getQuestItemsCount(LIONS_EYE);
			st.takeItems(LIONS_CLAW, claw * 10L);
			int ala_count = 3;
			int soul_count = 100;
			int soe_count = 20;
			int heal_count = 20;
			int spir_count = 50;
			if(eye > 9L)
			{
				ala_count = 4;
				soul_count = 400;
				soe_count = 30;
				heal_count = 50;
				spir_count = 200;
			}
			else if(eye > 4L)
			{
				spir_count = 100;
				soul_count = 200;
				heal_count = 25;
			}
			while(claw > 0L)
			{
				final int n = Rnd.get(5);
				if(n == 0)
					st.giveItems(ALACRITY_POTION, Math.round(ala_count * st.getRateQuestsReward()));
				else if(n == 1)
					st.giveItems(SOULSHOT_D, Math.round(soul_count * st.getRateQuestsReward()));
				else if(n == 2)
					st.giveItems(SCROLL_ESCAPE, Math.round(soe_count * st.getRateQuestsReward()));
				else if(n == 3)
					st.giveItems(SPIRITSHOT_D, Math.round(spir_count * st.getRateQuestsReward()));
				else if(n == 4)
					st.giveItems(HEALING_POTION, Math.round(heal_count * st.getRateQuestsReward()));
				--claw;
			}
			if(check_eye > 0L)
				return "30735-06.htm";
			return "30735-06.htm";
		}
		else
		{
			if(event.equalsIgnoreCase("leave"))
			{
				int order;
				if(part == 1)
					order = SOPHIAS_LETTER1;
				else if(part == 2)
					order = SOPHIAS_LETTER2;
				else if(part == 3)
					order = SOPHIAS_LETTER3;
				else if(part == 4)
					order = SOPHIAS_LETTER4;
				else
					order = 0;
				st.set("part", "0");
				if(order > 0)
					st.takeItems(order, 1L);
				return "30735-07.htm";
			}
			if(!event.equalsIgnoreCase("f_info"))
			{
				if(event.equalsIgnoreCase("f_give"))
				{
					if(st.getQuestItemsCount(CARGO_BOX1) <= 0L)
						return "red_foor-no_box.htm";
					if(st.getQuestItemsCount(57) < OPEN_BOX_PRICE)
						return "red_foor-no_adena.htm";
					st.takeItems(CARGO_BOX1, 1L);
					st.takeItems(57, OPEN_BOX_PRICE);
					final int rand = Rnd.get(1, 162);
					if(rand < 21)
					{
						st.giveItems(GLUDIO_APPLE, 1L);
						return "red_foor-02.htm";
					}
					if(rand < 41)
					{
						st.giveItems(CORN_MEAL, 1L);
						return "red_foor-03.htm";
					}
					if(rand < 61)
					{
						st.giveItems(WOLF_PELTS, 1L);
						return "red_foor-04.htm";
					}
					if(rand < 74)
					{
						st.giveItems(MONNSTONE, 1L);
						return "red_foor-05.htm";
					}
					if(rand < 86)
					{
						st.giveItems(GLUDIO_WEETS_FLOWER, 1L);
						return "red_foor-06.htm";
					}
					if(rand < 98)
					{
						st.giveItems(SPIDERSILK_ROPE, 1L);
						return "red_foor-07.htm";
					}
					if(rand < 99)
					{
						st.giveItems(ALEXANDRIT, 1L);
						return "red_foor-08.htm";
					}
					if(rand < 109)
					{
						st.giveItems(SILVER_TEA, 1L);
						return "red_foor-09.htm";
					}
					if(rand < 119)
					{
						st.giveItems(GOLEM_PART, 1L);
						return "red_foor-10.htm";
					}
					if(rand < 123)
					{
						st.giveItems(FIRE_EMERALD, 1L);
						return "red_foor-11.htm";
					}
					if(rand < 127)
					{
						st.giveItems(SILK_FROCK, 1L);
						return "red_foor-12.htm";
					}
					if(rand < 131)
					{
						st.giveItems(PORCELAN_URN, 1L);
						return "red_foor-13.htm";
					}
					if(rand < 132)
					{
						st.giveItems(IMPERIAL_DIAMOND, 1L);
						return "red_foor-13.htm";
					}
					if(rand < 147)
					{
						final int random_stat = Rnd.get(4);
						if(random_stat == 3)
						{
							st.giveItems(STATUE_SHILIEN_HEAD, 1L);
							return "red_foor-14.htm";
						}
						if(random_stat == 0)
						{
							st.giveItems(STATUE_SHILIEN_TORSO, 1L);
							return "red_foor-14.htm";
						}
						if(random_stat == 1)
						{
							st.giveItems(STATUE_SHILIEN_ARM, 1L);
							return "red_foor-14.htm";
						}
						if(random_stat == 2)
						{
							st.giveItems(STATUE_SHILIEN_LEG, 1L);
							return "red_foor-14.htm";
						}
					}
					else if(rand <= 162)
					{
						final int random_tab = Rnd.get(4);
						if(random_tab == 0)
						{
							st.giveItems(FRAGMENT_ANCIENT_TABLE1, 1L);
							return "red_foor-15.htm";
						}
						if(random_tab == 1)
						{
							st.giveItems(FRAGMENT_ANCIENT_TABLE2, 1L);
							return "red_foor-15.htm";
						}
						if(random_tab == 2)
						{
							st.giveItems(FRAGMENT_ANCIENT_TABLE3, 1L);
							return "red_foor-15.htm";
						}
						if(random_tab == 3)
						{
							st.giveItems(FRAGMENT_ANCIENT_TABLE4, 1L);
							return "red_foor-15.htm";
						}
					}
				}
				else if(event.equalsIgnoreCase("r_give_statue") || event.equalsIgnoreCase("r_give_tablet"))
				{
					int[] items = statue_list;
					int item = COMPLETE_STATUE;
					String pieces = "rupio-01.htm";
					String brockes = "rupio-02.htm";
					String complete = "rupio-03.htm";
					if(event.equalsIgnoreCase("r_give_tablet"))
					{
						items = tablet_list;
						item = COMPLETE_TABLET;
						pieces = "rupio-04.htm";
						brockes = "rupio-05.htm";
						complete = "rupio-06.htm";
					}
					int count = 0;
					for(int id = items[0]; id <= items[items.length - 1]; ++id)
						if(st.getQuestItemsCount(id) > 0L)
							++count;
					if(count > 3)
					{
						for(int id = items[0]; id <= items[items.length - 1]; ++id)
							st.takeItems(id, 1L);
						if(Rnd.chance(2))
						{
							st.giveItems(item, 1L);
							return complete;
						}
						return brockes;
					}
					else
					{
						if(count < 4 && count != 0)
							return pieces;
						return "rupio-07.htm";
					}
				}
				else if(event.equalsIgnoreCase("l_give"))
				{
					if(st.getQuestItemsCount(COMPLETE_TABLET) > 0L)
					{
						st.takeItems(COMPLETE_TABLET, 1L);
						st.giveItems(57, 30000L);
						return "lockirin-01.htm";
					}
					return "lockirin-02.htm";
				}
				else if(event.equalsIgnoreCase("u_give"))
				{
					if(st.getQuestItemsCount(COMPLETE_STATUE) > 0L)
					{
						st.takeItems(COMPLETE_STATUE, 1L);
						st.giveItems(57, 30000L);
						return "undiras-01.htm";
					}
					return "undiras-02.htm";
				}
				else if(event.equalsIgnoreCase("m_give"))
				{
					if(st.getQuestItemsCount(CARGO_BOX1) <= 0L)
						return "morgan-03.htm";
					final long coins = st.getQuestItemsCount(GUILD_COIN);
					long count2 = coins / 40L;
					if(count2 > 2L)
						count2 = 2L;
					st.giveItems(GUILD_COIN, 1L);
					st.giveItems(57, (1L + count2) * 100L);
					st.takeItems(CARGO_BOX1, 1L);
					final int rand2 = Rnd.get(0, 3);
					if(rand2 == 0)
						return "morgan-01.htm";
					if(rand2 == 1)
						return "morgan-02.htm";
					return "morgan-02.htm";
				}
				else
				{
					if(event.equalsIgnoreCase("start_parts"))
						return "30735-08.htm";
					if(event.equalsIgnoreCase("m_reward"))
						return "morgan-05.htm";
					if(event.equalsIgnoreCase("u_info"))
						return "undiras-03.htm";
					if(event.equalsIgnoreCase("l_info"))
						return "lockirin-03.htm";
					if(event.equalsIgnoreCase("p_redfoot"))
						return "30735-09.htm";
					if(event.equalsIgnoreCase("p_trader_info"))
						return "30735-10.htm";
					if(event.equalsIgnoreCase("start_chose_parts"))
						return "30735-11.htm";
					if(event.equalsIgnoreCase("p1_explanation"))
						return "30735-12.htm";
					if(event.equalsIgnoreCase("p2_explanation"))
						return "30735-13.htm";
					if(event.equalsIgnoreCase("p3_explanation"))
						return "30735-14.htm";
					if(event.equalsIgnoreCase("p4_explanation"))
						return "30735-15.htm";
					if(event.equalsIgnoreCase("f_more_help"))
						return "red_foor-16.htm";
					if(event.equalsIgnoreCase("r_exit"))
						return "30735-16.htm";
				}
				return event;
			}
			final int text = st.getInt("text");
			if(text < 4)
			{
				st.set("text", String.valueOf(text + 1));
				return "red_foor_text_" + Rnd.get(1, 19) + ".htm";
			}
			return "red_foor-01.htm";
		}
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		final String htmltext = "noquest";
		if(cond == 0)
		{
			st.set("cond", "0");
			st.set("part", "0");
			st.set("text", "0");
			if(npcId == Sophya)
			{
				if(st.getQuestItemsCount(BLACK_LION_MARK) <= 0L)
				{
					st.exitCurrentQuest(true);
					return "30735-19.htm";
				}
				if(st.getPlayer().getLevel() > 24)
					return "30735-17.htm";
				st.exitCurrentQuest(true);
				return "30735-18.htm";
			}
		}
		else
		{
			final int part = st.getInt("part");
			if(npcId == Sophya)
			{
				int item;
				if(part == 1)
					item = UNDEAD_ASH;
				else if(part == 2)
					item = BLOODY_AXE_INSIGNIAS;
				else if(part == 3)
					item = DELU_FANG;
				else
				{
					if(part != 4)
						return "30735-20.htm";
					item = STAKATO_TALONS;
				}
				final long count = st.getQuestItemsCount(item);
				final long box = st.getQuestItemsCount(CARGO_BOX1);
				if(box > 0L && count > 0L)
				{
					giveRewards(st, item, count);
					return "30735-21.htm";
				}
				if(box > 0L)
					return "30735-22.htm";
				if(count > 0L)
				{
					giveRewards(st, item, count);
					return "30735-23.htm";
				}
				return "30735-24.htm";
			}
			else if(npcId == Redfoot)
			{
				if(st.getQuestItemsCount(CARGO_BOX1) > 0L)
					return "red_foor_text_20.htm";
				return "red_foor_text_21.htm";
			}
			else if(npcId == Rupio)
			{
				int count2 = 0;
				for(int i = 3457; i <= 3460; ++i)
					if(st.getQuestItemsCount(i) > 0L)
						++count2;
				for(int i = 3462; i <= 3465; ++i)
					if(st.getQuestItemsCount(i) > 0L)
						++count2;
				if(count2 > 0)
					return "rupio-08.htm";
				return "rupio-07.htm";
			}
			else if(npcId == Undrias)
			{
				if(st.getQuestItemsCount(COMPLETE_STATUE) > 0L)
					return "undiras-04.htm";
				int count2 = 0;
				for(int i = 3457; i <= 3460; ++i)
					if(st.getQuestItemsCount(i) > 0L)
						++count2;
				if(count2 > 0)
					return "undiras-05.htm";
				return "undiras-02.htm";
			}
			else if(npcId == Lockirin)
			{
				if(st.getQuestItemsCount(COMPLETE_TABLET) > 0L)
					return "lockirin-04.htm";
				int count2 = 0;
				for(int i = 3462; i <= 3465; ++i)
					if(st.getQuestItemsCount(i) > 0L)
						++count2;
				if(count2 > 0)
					return "lockirin-05.htm";
				return "lockirin-06.htm";
			}
			else if(npcId == Morgan)
			{
				if(st.getQuestItemsCount(CARGO_BOX1) > 0L)
					return "morgan-06.htm";
				return "morgan-07.htm";
			}
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		boolean on_npc = false;
		int part = 0;
		int allowDrop = 0;
		int chancePartItem = 0;
		int chanceBox = 0;
		int partItem = 0;
		for(int i = 0; i < DROPLIST.length; ++i)
			if(DROPLIST[i][0] == npcId)
			{
				part = DROPLIST[i][1];
				allowDrop = DROPLIST[i][2];
				chancePartItem = DROPLIST[i][3];
				chanceBox = DROPLIST[i][4];
				partItem = DROPLIST[i][5];
				on_npc = true;
			}
		if(on_npc)
		{
			final int rand = Rnd.get(1, 100);
			final int rand2 = Rnd.get(1, 100);
			if(allowDrop == 1 && st.getInt("part") == part && rand < chancePartItem)
			{
				st.giveItems(partItem, npcId == 27152 ? 8L : 1L);
				st.playSound(Quest.SOUND_ITEMGET);
				if(rand2 < chanceBox)
				{
					st.giveItems(CARGO_BOX1, 1L);
					if(rand > chancePartItem)
						st.playSound(Quest.SOUND_ITEMGET);
				}
			}
		}
		if(Rnd.chance(4) && (npcId == 20251 || npcId == 20252 || npcId == 20253))
		{
			st.addSpawn(21105);
			st.addSpawn(21105);
		}
		if(npcId == 20157 || npcId == 20230 || npcId == 20232 || npcId == 20234)
		{
			if(Rnd.chance(2))
				st.addSpawn(27152);
			if(Rnd.chance(15))
				st.giveItems(CARGO_BOX1, 1L);
		}
		return null;
	}
}
