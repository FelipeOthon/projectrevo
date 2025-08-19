package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _218_TestimonyOfLife extends Quest implements ScriptFile
{
	private static final int MARK_OF_LIFE = 3140;
	private static final int CARDIENS_LETTER = 3141;
	private static final int CAMOMILE_CHARM = 3142;
	private static final int HIERARCHS_LETTER = 3143;
	private static final int MOONFLOWER_CHARM = 3144;
	private static final int GRAIL_DIAGRAM = 3145;
	private static final int THALIAS_LETTER1 = 3146;
	private static final int THALIAS_LETTER2 = 3147;
	private static final int THALIAS_INSTRUCTIONS = 3148;
	private static final int PUSHKINS_LIST = 3149;
	private static final int PURE_MITHRIL_CUP = 3150;
	private static final int ARKENIAS_CONTRACT = 3151;
	private static final int ARKENIAS_INSTRUCTIONS = 3152;
	private static final int ADONIUS_LIST = 3153;
	private static final int ANDARIEL_SCRIPTURE_COPY = 3154;
	private static final int STARDUST = 3155;
	private static final int ISAELS_INSTRUCTIONS = 3156;
	private static final int ISAELS_LETTER = 3157;
	private static final int GRAIL_OF_PURITY = 3158;
	private static final int TEARS_OF_UNICORN = 3159;
	private static final int WATER_OF_LIFE = 3160;
	private static final int PURE_MITHRIL_ORE = 3161;
	private static final int ANT_SOLDIER_ACID = 3162;
	private static final int WYRMS_TALON1 = 3163;
	private static final int SPIDER_ICHOR = 3164;
	private static final int HARPYS_DOWN = 3165;
	private static final int TALINS_SPEAR_BLADE = 3166;
	private static final int TALINS_SPEAR_SHAFT = 3167;
	private static final int TALINS_RUBY = 3168;
	private static final int TALINS_AQUAMARINE = 3169;
	private static final int TALINS_AMETHYST = 3170;
	private static final int TALINS_PERIDOT = 3171;
	private static final int TALINS_SPEAR = 3026;
	private static final int RewardExp = 943416;
	private static final int RewardSP = 62959;
	private static final int RewardAdena = 171144;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _218_TestimonyOfLife()
	{
		super(false);
		this.addStartNpc(30460);
		this.addTalkId(new int[] { 30154 });
		this.addTalkId(new int[] { 30300 });
		this.addTalkId(new int[] { 30371 });
		this.addTalkId(new int[] { 30375 });
		this.addTalkId(new int[] { 30419 });
		this.addTalkId(new int[] { 30460 });
		this.addTalkId(new int[] { 30655 });
		this.addKillId(new int[] { 20145 });
		this.addKillId(new int[] { 20176 });
		this.addKillId(new int[] { 20233 });
		this.addKillId(new int[] { 27077 });
		this.addKillId(new int[] { 20550 });
		this.addKillId(new int[] { 20581 });
		this.addKillId(new int[] { 20582 });
		this.addKillId(new int[] { 20082 });
		this.addKillId(new int[] { 20084 });
		this.addKillId(new int[] { 20086 });
		this.addKillId(new int[] { 20087 });
		this.addKillId(new int[] { 20088 });
		addQuestItem(new int[] {
				3142,
				3141,
				3160,
				3144,
				3143,
				3155,
				3150,
				3148,
				3157,
				3159,
				3145,
				3149,
				3146,
				3151,
				3154,
				3152,
				3153,
				3147,
				3166,
				3167,
				3168,
				3169,
				3170,
				3171,
				3156,
				3158 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("1"))
		{
			htmltext = "master_cardien_q0218_04.htm";
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
			st.giveItems(3141, 1L);
			if(!st.getPlayer().getVarBoolean("dd2"))
			{
				st.giveItems(7562, 102L);
				st.getPlayer().setVar("dd2", "1");
			}
		}
		else if(event.equalsIgnoreCase("30154_1"))
			htmltext = "ozzy_q0218_02.htm";
		else if(event.equalsIgnoreCase("30154_2"))
			htmltext = "ozzy_q0218_03.htm";
		else if(event.equalsIgnoreCase("30154_3"))
			htmltext = "ozzy_q0218_04.htm";
		else if(event.equalsIgnoreCase("30154_4"))
			htmltext = "ozzy_q0218_05.htm";
		else if(event.equalsIgnoreCase("30154_5"))
			htmltext = "ozzy_q0218_06.htm";
		else if(event.equalsIgnoreCase("30154_6"))
		{
			htmltext = "ozzy_q0218_07.htm";
			st.takeItems(3141, 1L);
			st.giveItems(3144, 1L);
			st.giveItems(3143, 1L);
		}
		else if(event.equalsIgnoreCase("30371_1"))
			htmltext = "thalya_q0218_02.htm";
		else if(event.equalsIgnoreCase("30371_2"))
		{
			htmltext = "thalya_q0218_03.htm";
			st.takeItems(3143, 1L);
			st.giveItems(3145, 1L);
		}
		else if(event.equalsIgnoreCase("30371_3"))
		{
			if(st.getPlayer().getLevel() < 38)
			{
				htmltext = "thalya_q0218_10.htm";
				st.takeItems(3155, 1L);
				st.giveItems(3148, 1L);
			}
			else
			{
				htmltext = "thalya_q0218_11.htm";
				st.takeItems(3155, 1L);
				st.giveItems(3147, 1L);
			}
		}
		else if(event.equalsIgnoreCase("30300_1"))
			htmltext = "blacksmith_pushkin_q0218_02.htm";
		else if(event.equalsIgnoreCase("30300_2"))
			htmltext = "blacksmith_pushkin_q0218_03.htm";
		else if(event.equalsIgnoreCase("30300_3"))
			htmltext = "blacksmith_pushkin_q0218_04.htm";
		else if(event.equalsIgnoreCase("30300_4"))
			htmltext = "blacksmith_pushkin_q0218_05.htm";
		else if(event.equalsIgnoreCase("30300_5"))
		{
			htmltext = "blacksmith_pushkin_q0218_06.htm";
			st.takeItems(3145, 1L);
			st.giveItems(3149, 1L);
		}
		else if(event.equalsIgnoreCase("30300_6"))
			htmltext = "blacksmith_pushkin_q0218_09.htm";
		else if(event.equalsIgnoreCase("30300_7"))
		{
			htmltext = "blacksmith_pushkin_q0218_10.htm";
			st.takeItems(3161, -1L);
			st.takeItems(3162, -1L);
			st.takeItems(3163, -1L);
			st.takeItems(3149, 1L);
			st.giveItems(3150, 1L);
		}
		else if(event.equalsIgnoreCase("30419_1"))
			htmltext = "arkenia_q0218_02.htm";
		else if(event.equalsIgnoreCase("30419_2"))
			htmltext = "arkenia_q0218_03.htm";
		else if(event.equalsIgnoreCase("30419_3"))
		{
			htmltext = "arkenia_q0218_04.htm";
			st.takeItems(3146, 1L);
			st.giveItems(3151, 1L);
			st.giveItems(3152, 1L);
		}
		else if(event.equalsIgnoreCase("30375_1"))
		{
			htmltext = "priest_adonius_q0218_02.htm";
			st.takeItems(3152, 1L);
			st.giveItems(3153, 1L);
		}
		else if(event.equalsIgnoreCase("30655_1"))
		{
			htmltext = "isael_silvershadow_q0218_02.htm";
			st.takeItems(3147, 1L);
			st.giveItems(3156, 1L);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		if(st.getQuestItemsCount(3140) > 0L)
		{
			st.exitCurrentQuest(true);
			return "completed";
		}
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int id = st.getState();
		final int cond = st.getInt("cond");
		if(id == 1)
		{
			st.setState(2);
			st.set("cond", "0");
			st.set("id", "0");
		}
		if(npcId == 30460 && cond == 0)
		{
			if(cond < 15)
			{
				if(st.getPlayer().getRace() != Race.elf)
					htmltext = "master_cardien_q0218_01.htm";
				else
				{
					if(st.getPlayer().getLevel() >= 37)
					{
						htmltext = "master_cardien_q0218_03.htm";
						st.set("cond", "1");
						return htmltext;
					}
					htmltext = "master_cardien_q0218_02.htm";
					st.exitCurrentQuest(true);
				}
			}
			else
			{
				htmltext = "master_cardien_q0218_01a.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(npcId == 30460 && cond == 1 && st.getQuestItemsCount(3141) == 1L)
			htmltext = "master_cardien_q0218_05.htm";
		else if(npcId == 30460 && cond == 1 && st.getQuestItemsCount(3144) == 1L)
			htmltext = "master_cardien_q0218_06.htm";
		else if(npcId == 30460 && cond == 1 && st.getQuestItemsCount(3142) == 1L)
		{
			htmltext = "master_cardien_q0218_07.htm";
			st.takeItems(3142, -1L);
			st.giveItems(3140, 1L);
			if(!st.getPlayer().getVarBoolean("prof2.2"))
			{
				st.addExpAndSp(943416L, 62959L, true);
				st.giveItems(57, 171144L, Config.RATE_QUESTS_OCCUPATION_CHANGE);
				st.getPlayer().setVar("prof2.2", "1");
			}
			st.playSound(Quest.SOUND_FINISH);
			st.unset("cond");
			st.exitCurrentQuest(false);
		}
		else if(npcId == 30154 && cond == 1 && st.getQuestItemsCount(3141) == 1L)
			htmltext = "ozzy_q0218_01.htm";
		else if(npcId == 30154 && cond == 1 && st.getQuestItemsCount(3144) == 1L && st.getQuestItemsCount(3160) == 0L)
			htmltext = "ozzy_q0218_08.htm";
		else if(npcId == 30154 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3160) > 0L)
		{
			htmltext = "ozzy_q0218_09.htm";
			st.takeItems(3160, 1L);
			st.takeItems(3144, 1L);
			st.giveItems(3142, 1L);
		}
		else if(npcId == 30154 && cond == 1 && st.getQuestItemsCount(3142) == 1L)
			htmltext = "ozzy_q0218_10.htm";
		else if(npcId == 30371 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3143) > 0L)
			htmltext = "thalya_q0218_01.htm";
		else if(npcId == 30371 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3145) > 0L)
			htmltext = "thalya_q0218_04.htm";
		else if(npcId == 30371 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3149) > 0L)
			htmltext = "thalya_q0218_05.htm";
		else if(npcId == 30371 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3150) > 0L)
		{
			htmltext = "thalya_q0218_06.htm";
			st.takeItems(3150, 1L);
			st.giveItems(3146, 1L);
		}
		else if(npcId == 30371 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3146) > 0L)
			htmltext = "thalya_q0218_07.htm";
		else if(npcId == 30371 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3151) > 0L)
			htmltext = "thalya_q0218_08.htm";
		else if(npcId == 30371 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3155) > 0L)
			htmltext = "thalya_q0218_09.htm";
		else if(npcId == 30371 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3148) > 0L)
		{
			if(st.getPlayer().getLevel() < 38)
				htmltext = "thalya_q0218_12.htm";
			else
			{
				htmltext = "thalya_q0218_13.htm";
				st.takeItems(3148, 1L);
				st.giveItems(3147, 1L);
			}
		}
		else if(npcId == 30371 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3147) > 0L)
			htmltext = "thalya_q0218_14.htm";
		else if(npcId == 30371 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3156) > 0L)
			htmltext = "thalya_q0218_15.htm";
		else if(npcId == 30371 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3026) > 0L && st.getQuestItemsCount(3157) > 0L)
		{
			htmltext = "thalya_q0218_16.htm";
			st.takeItems(3157, 1L);
			st.giveItems(3158, 1L);
		}
		else if(npcId == 30371 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3026) > 0L && st.getQuestItemsCount(3158) > 0L)
			htmltext = "thalya_q0218_17.htm";
		else if(npcId == 30371 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3159) > 0L)
		{
			htmltext = "thalya_q0218_18.htm";
			st.takeItems(3159, 1L);
			st.giveItems(3160, 1L);
		}
		else if(npcId == 30371 && cond == 1 && st.getQuestItemsCount(3142) > 0L || st.getQuestItemsCount(3160) > 0L && st.getQuestItemsCount(3144) == 1L)
			htmltext = "thalya_q0218_19.htm";
		else if(npcId == 30300 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3145) > 0L)
			htmltext = "blacksmith_pushkin_q0218_01.htm";
		else if(npcId == 30300 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3149) > 0L)
		{
			if(st.getQuestItemsCount(3161) >= 10L && st.getQuestItemsCount(3162) >= 20L && st.getQuestItemsCount(3163) >= 20L)
				htmltext = "blacksmith_pushkin_q0218_08.htm";
			else
				htmltext = "blacksmith_pushkin_q0218_07.htm";
		}
		else if(npcId == 30300 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3150) > 0L)
			htmltext = "blacksmith_pushkin_q0218_11.htm";
		else if(npcId == 30300 && cond == 1 && st.getQuestItemsCount(3145) == 0L && st.getQuestItemsCount(3149) == 0L && st.getQuestItemsCount(3150) == 0L && st.getQuestItemsCount(3144) == 1L)
			htmltext = "blacksmith_pushkin_q0218_12.htm";
		else if(npcId == 30419 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3146) > 0L)
			htmltext = "arkenia_q0218_01.htm";
		else if(npcId == 30419 && cond == 1 && (st.getQuestItemsCount(3152) > 0L || st.getQuestItemsCount(3153) > 0L) && st.getQuestItemsCount(3144) == 1L)
			htmltext = "arkenia_q0218_05.htm";
		else if(npcId == 30419 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3154) > 0L)
		{
			htmltext = "arkenia_q0218_06.htm";
			st.takeItems(3151, 1L);
			st.takeItems(3154, 1L);
			st.giveItems(3155, 1L);
		}
		else if(npcId == 30419 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3155) > 0L)
			htmltext = "arkenia_q0218_07.htm";
		else if(npcId == 30419 && cond == 1 && st.getQuestItemsCount(3146) == 0L && st.getQuestItemsCount(3151) == 0L && st.getQuestItemsCount(3154) == 0L && st.getQuestItemsCount(3155) == 0L && st.getQuestItemsCount(3144) == 1L)
			htmltext = "arkenia_q0218_08.htm";
		else if(npcId == 30375 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3152) > 0L)
			htmltext = "priest_adonius_q0218_01.htm";
		else if(npcId == 30375 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3153) > 0L)
		{
			if(st.getQuestItemsCount(3164) >= 20L && st.getQuestItemsCount(3165) >= 20L)
			{
				htmltext = "priest_adonius_q0218_04.htm";
				st.takeItems(3164, st.getQuestItemsCount(3164));
				st.takeItems(3165, st.getQuestItemsCount(3165));
				st.takeItems(3153, 1L);
				st.giveItems(3154, 1L);
			}
			else
				htmltext = "priest_adonius_q0218_03.htm";
		}
		else if(npcId == 30375 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3154) > 0L)
			htmltext = "priest_adonius_q0218_05.htm";
		else if(npcId == 30375 && cond == 1 && st.getQuestItemsCount(3152) == 0L && st.getQuestItemsCount(3153) == 0L && st.getQuestItemsCount(3154) == 0L && st.getQuestItemsCount(3144) == 1L)
			htmltext = "priest_adonius_q0218_06.htm";
		else if(npcId == 30655 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3147) > 0L)
			htmltext = "isael_silvershadow_q0218_01.htm";
		else if(npcId == 30655 && cond == 1 && st.getQuestItemsCount(3144) > 0L && st.getQuestItemsCount(3156) > 0L)
		{
			if(st.getQuestItemsCount(3166) > 0L && st.getQuestItemsCount(3167) > 0L && st.getQuestItemsCount(3168) > 0L && st.getQuestItemsCount(3169) > 0L && st.getQuestItemsCount(3170) > 0L && st.getQuestItemsCount(3171) > 0L)
			{
				htmltext = "isael_silvershadow_q0218_04.htm";
				st.takeItems(3166, 1L);
				st.takeItems(3167, 1L);
				st.takeItems(3168, 1L);
				st.takeItems(3169, 1L);
				st.takeItems(3170, 1L);
				st.takeItems(3171, 1L);
				st.takeItems(3156, 1L);
				st.giveItems(3157, 1L);
				st.giveItems(3026, 1L);
			}
			else
				htmltext = "isael_silvershadow_q0218_03.htm";
		}
		else if(npcId == 30655 && cond == 1 && st.getQuestItemsCount(3026) > 0L && st.getQuestItemsCount(3157) > 0L)
			htmltext = "isael_silvershadow_q0218_05.htm";
		else if(npcId == 30655 && cond == 1 && st.getQuestItemsCount(3158) > 0L || st.getQuestItemsCount(3160) > 0L || st.getQuestItemsCount(3142) > 0L && st.getQuestItemsCount(3144) == 1L)
			htmltext = "isael_silvershadow_q0218_06.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 20550)
		{
			st.set("id", "0");
			if(cond > 0 && st.getQuestItemsCount(3144) == 1L && st.getQuestItemsCount(3149) == 1L && st.getQuestItemsCount(3161) < 10L && Rnd.chance(50))
			{
				st.giveItems(3161, 1L);
				if(st.getQuestItemsCount(3161) < 10L)
					st.playSound(Quest.SOUND_ITEMGET);
				else
					st.playSound(Quest.SOUND_MIDDLE);
			}
		}
		else if(npcId == 20176)
		{
			st.set("id", "0");
			if(cond > 0 && st.getQuestItemsCount(3144) == 1L && st.getQuestItemsCount(3149) == 1L && st.getQuestItemsCount(3163) < 20L && Rnd.chance(50))
			{
				st.giveItems(3163, 1L);
				if(st.getQuestItemsCount(3163) < 20L)
					st.playSound(Quest.SOUND_ITEMGET);
				else
					st.playSound(Quest.SOUND_MIDDLE);
			}
		}
		else if(npcId == 20082 || npcId == 20084 || npcId == 20086)
		{
			st.set("id", "0");
			if(cond > 0 && st.getQuestItemsCount(3144) == 1L && st.getQuestItemsCount(3149) == 1L && st.getQuestItemsCount(3162) < 20L && Rnd.chance(80))
			{
				st.giveItems(3162, 1L);
				if(st.getQuestItemsCount(3162) < 20L)
					st.playSound(Quest.SOUND_ITEMGET);
				else
					st.playSound(Quest.SOUND_MIDDLE);
			}
		}
		else if(npcId == 20087 || npcId == 20088)
		{
			st.set("id", "0");
			if(cond > 0 && st.getQuestItemsCount(3144) == 1L && st.getQuestItemsCount(3149) == 1L && st.getQuestItemsCount(3162) < 20L && Rnd.chance(50))
			{
				st.giveItems(3162, 1L);
				if(st.getQuestItemsCount(3162) < 20L)
					st.playSound(Quest.SOUND_ITEMGET);
				else
					st.playSound(Quest.SOUND_MIDDLE);
			}
		}
		else if(npcId == 20233)
		{
			st.set("id", "0");
			if(cond > 0 && st.getQuestItemsCount(3144) == 1L && st.getQuestItemsCount(3153) == 1L && st.getQuestItemsCount(3164) < 20L && Rnd.chance(50))
			{
				st.giveItems(3164, 1L);
				if(st.getQuestItemsCount(3164) < 20L)
					st.playSound(Quest.SOUND_ITEMGET);
				else
					st.playSound(Quest.SOUND_MIDDLE);
			}
		}
		else if(npcId == 20145)
		{
			st.set("id", "0");
			if(cond > 0 && st.getQuestItemsCount(3144) == 1L && st.getQuestItemsCount(3153) == 1L && st.getQuestItemsCount(3165) < 20L && Rnd.chance(50))
			{
				st.giveItems(3165, 1L);
				if(st.getQuestItemsCount(3165) < 20L)
					st.playSound(Quest.SOUND_ITEMGET);
				else
					st.playSound(Quest.SOUND_MIDDLE);
			}
		}
		else if(npcId == 27077)
		{
			if(cond > 0 && st.getQuestItemsCount(3144) == 1L && st.getItemEquipped(7) == 3026 && st.getQuestItemsCount(3158) == 1L && st.getQuestItemsCount(3159) == 0L && st.getQuestItemsCount(3026) > 0L)
			{
				st.takeItems(3158, 1L);
				st.takeItems(3026, 1L);
				st.giveItems(3159, 1L);
			}
		}
		else if(npcId == 20581 || npcId == 20582)
		{
			st.set("id", "0");
			if(cond > 0 && st.getQuestItemsCount(3156) == 1L && Rnd.chance(50))
				if(st.getQuestItemsCount(3166) == 0L)
				{
					st.giveItems(3166, 1L);
					st.playSound(Quest.SOUND_ITEMGET);
				}
				else if(st.getQuestItemsCount(3167) == 0L)
				{
					st.giveItems(3167, 1L);
					st.playSound(Quest.SOUND_ITEMGET);
				}
				else if(st.getQuestItemsCount(3168) == 0L)
				{
					st.giveItems(3168, 1L);
					st.playSound(Quest.SOUND_ITEMGET);
				}
				else if(st.getQuestItemsCount(3169) == 0L)
				{
					st.giveItems(3169, 1L);
					st.playSound(Quest.SOUND_ITEMGET);
				}
				else if(st.getQuestItemsCount(3170) == 0L)
				{
					st.giveItems(3170, 1L);
					st.playSound(Quest.SOUND_ITEMGET);
				}
				else if(st.getQuestItemsCount(3171) == 0L)
				{
					st.giveItems(3171, 1L);
					st.playSound(Quest.SOUND_MIDDLE);
				}
		}
		return null;
	}
}
