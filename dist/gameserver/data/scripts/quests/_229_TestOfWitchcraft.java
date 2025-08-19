package quests;

import java.util.List;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.ai.CtrlEvent;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.model.quest.QuestTimer;
import l2s.gameserver.scripts.ScriptFile;

public class _229_TestOfWitchcraft extends Quest implements ScriptFile
{
	private static final int Orim = 30630;
	private static final int Alexandria = 30098;
	private static final int Iker = 30110;
	private static final int Kaira = 30476;
	private static final int Lara = 30063;
	private static final int Roderik = 30631;
	private static final int Nestle = 30314;
	private static final int Leopold = 30435;
	private static final int Vasper = 30417;
	private static final int Vadin = 30188;
	private static final int Evert = 30633;
	private static final int Endrigo = 30632;
	private static final int MarkOfWitchcraft = 3307;
	private static final int OrimsDiagram = 3308;
	private static final int AlexandriasBook = 3309;
	private static final int IkersList = 3310;
	private static final int DireWyrmFang = 3311;
	private static final int LetoLizardmanCharm = 3312;
	private static final int EnchantedGolemHeartstone = 3313;
	private static final int LarasMemo = 3314;
	private static final int NestlesMemo = 3315;
	private static final int LeopoldsJournal = 3316;
	private static final int Aklantoth_1stGem = 3317;
	private static final int Aklantoth_2stGem = 3318;
	private static final int Aklantoth_3stGem = 3319;
	private static final int Aklantoth_4stGem = 3320;
	private static final int Aklantoth_5stGem = 3321;
	private static final int Aklantoth_6stGem = 3322;
	private static final int Brimstone_1st = 3323;
	private static final int OrimsInstructions = 3324;
	private static final int Orims1stLetter = 3325;
	private static final int Orims2stLetter = 3326;
	private static final int SirVaspersLetter = 3327;
	private static final int VadinsCrucifix = 3328;
	private static final int TamlinOrcAmulet = 3329;
	private static final int VadinsSanctions = 3330;
	private static final int IkersAmulet = 3331;
	private static final int SoultrapCrystal = 3332;
	private static final int PurgatoryKey = 3333;
	private static final int ZeruelBindCrystal = 3334;
	private static final int Brimstone_2nd = 3335;
	private static final int SwordOfBinding = 3029;
	private static final int DireWyrm = 20557;
	private static final int EnchantedStoneGolem = 20565;
	private static final int LetoLizardman = 20577;
	private static final int LetoLizardmanArcher = 20578;
	private static final int LetoLizardmanSoldier = 20579;
	private static final int LetoLizardmanWarrior = 20580;
	private static final int LetoLizardmanShaman = 20581;
	private static final int LetoLizardmanOverlord = 20582;
	private static final int NamelessRevenant = 27099;
	private static final int SkeletalMercenary = 27100;
	private static final int DrevanulPrinceZeruel = 27101;
	private static final int TamlinOrc = 20601;
	private static final int TamlinOrcArcher = 20602;
	private static final int[][] DROPLIST_COND;
	private static boolean QuestProf;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _229_TestOfWitchcraft()
	{
		super(false);
		this.addStartNpc(30630);
		this.addTalkId(new int[] { 30098 });
		this.addTalkId(new int[] { 30110 });
		this.addTalkId(new int[] { 30476 });
		this.addTalkId(new int[] { 30063 });
		this.addTalkId(new int[] { 30631 });
		this.addTalkId(new int[] { 30314 });
		this.addTalkId(new int[] { 30435 });
		this.addTalkId(new int[] { 30417 });
		this.addTalkId(new int[] { 30188 });
		this.addTalkId(new int[] { 30633 });
		this.addTalkId(new int[] { 30632 });
		for(int i = 0; i < _229_TestOfWitchcraft.DROPLIST_COND.length; ++i)
			this.addKillId(new int[] { _229_TestOfWitchcraft.DROPLIST_COND[i][2] });
		this.addKillId(new int[] { 27100 });
		this.addKillId(new int[] { 27101 });
		addQuestItem(new int[] {
				3308,
				3324,
				3325,
				3326,
				3323,
				3309,
				3310,
				3317,
				3332,
				3331,
				3318,
				3314,
				3315,
				3316,
				3320,
				3321,
				3322,
				3327,
				3029,
				3328,
				3330,
				3335,
				3333,
				3334,
				3311,
				3313,
				3312,
				3319,
				3329 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("30630-08.htm"))
		{
			st.playSound(Quest.SOUND_ACCEPT);
			st.giveItems(3308, 1L);
			if(!st.getPlayer().getVarBoolean("dd3"))
			{
				st.giveItems(7562, 104L);
				st.getPlayer().setVar("dd3", "1");
			}
			st.set("cond", "1");
			st.setState(2);
		}
		else if(event.equalsIgnoreCase("30098-03.htm"))
		{
			st.giveItems(3309, 1L);
			st.takeItems(3308, 1L);
			st.set("cond", "2");
			st.setState(2);
		}
		else if(event.equalsIgnoreCase("30110-03.htm"))
			st.giveItems(3310, 1L);
		else if(event.equalsIgnoreCase("30476-02.htm"))
			st.giveItems(3318, 1L);
		else if(event.equalsIgnoreCase("30063-02.htm"))
			st.giveItems(3314, 1L);
		else if(event.equalsIgnoreCase("30314-02.htm"))
			st.giveItems(3315, 1L);
		else if(event.equalsIgnoreCase("30435-02.htm"))
		{
			st.takeItems(3315, 1L);
			st.giveItems(3316, 1L);
		}
		else if(event.equalsIgnoreCase("30630-14.htm"))
		{
			List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 27101);
			if(!isQuest.isEmpty() && !isQuest.get(0).isDead())
				htmltext = "Drevanul Prince Zeruel is already spawned.";
			else
			{
				st.takeItems(3309, 1L);
				st.takeItems(3317, 1L);
				st.takeItems(3318, 1L);
				st.takeItems(3319, 1L);
				st.takeItems(3320, 1L);
				st.takeItems(3321, 1L);
				st.takeItems(3322, 1L);
				if(st.getQuestItemsCount(3323) == 0L)
					st.giveItems(3323, 1L);
				st.set("cond", "4");
				st.set("id", "1");
				st.startQuestTimer("DrevanulPrinceZeruel_Fail", 300000L);
				final NpcInstance Zeruel = st.addSpawn(27101);
				if(Zeruel != null)
					Zeruel.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, st.getPlayer(), 1);
			}
		}
		else if(event.equalsIgnoreCase("30630-16.htm"))
		{
			htmltext = "30630-16.htm";
			st.takeItems(3323, -1L);
			st.giveItems(3324, 1L);
			st.giveItems(3325, 1L);
			st.giveItems(3326, 1L);
			st.set("cond", "6");
		}
		else if(event.equalsIgnoreCase("30110-08.htm"))
		{
			st.takeItems(3326, 1L);
			st.giveItems(3332, 1L);
			st.giveItems(3331, 1L);
			if(st.getQuestItemsCount(3029) > 0L)
			{
				st.set("cond", "7");
				st.setState(2);
			}
		}
		else if(event.equalsIgnoreCase("30417-03.htm"))
		{
			st.takeItems(3325, 1L);
			st.giveItems(3327, 1L);
		}
		else if(event.equalsIgnoreCase("30633-02.htm"))
		{
			List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 27101);
			if(!isQuest.isEmpty())
				htmltext = "30633-fail.htm";
			else
			{
				st.set("id", "2");
				st.set("cond", "9");
				if(st.getQuestItemsCount(3335) == 0L)
					st.giveItems(3335, 1L);
				st.addSpawn(27101);
				st.startQuestTimer("DrevanulPrinceZeruel_Fail", 300000L);
				List<NpcInstance> Zeruel = GameObjectsStorage.getNpcs(false, 27101);
				if(!Zeruel.isEmpty())
					Zeruel.get(0).getAggroList().addDamageHate(st.getPlayer(), 0, 1);
			}
		}
		else if(event.equalsIgnoreCase("30630-20.htm"))
			st.takeItems(3334, 1L);
		else if(event.equalsIgnoreCase("30630-21.htm"))
			st.takeItems(3333, 1L);
		else if(event.equalsIgnoreCase("30630-22.htm"))
		{
			st.takeItems(3029, -1L);
			st.takeItems(3331, -1L);
			st.takeItems(3324, -1L);
			if(!st.getPlayer().getVarBoolean("prof2.3"))
			{
				st.addExpAndSp(1029122L, 70620L, true);
				st.giveItems(57, 186077L, Config.RATE_QUESTS_OCCUPATION_CHANGE);
				st.getPlayer().setVar("prof2.3", "1");
			}
			st.giveItems(3307, 1L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		if(event.equalsIgnoreCase("DrevanulPrinceZeruel_Fail"))
		{
			List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 27101);
			if(!isQuest.isEmpty())
				isQuest.get(0).deleteMe();
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 30630)
		{
			if(st.getQuestItemsCount(3307) != 0L)
			{
				htmltext = "completed";
				st.exitCurrentQuest(true);
			}
			else if(cond == 0)
			{
				if(st.getPlayer().getClassId().getId() == 11 || st.getPlayer().getClassId().getId() == 4 || st.getPlayer().getClassId().getId() == 32)
				{
					if(st.getPlayer().getLevel() < 39)
					{
						htmltext = "30630-02.htm";
						st.exitCurrentQuest(true);
					}
					else if(st.getPlayer().getClassId().getId() == 11)
						htmltext = "30630-03.htm";
					else
						htmltext = "30630-05.htm";
				}
				else
				{
					htmltext = "30630-01.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "30630-09.htm";
			else if(cond == 2)
				htmltext = "30630-10.htm";
			else if(cond == 3 || st.getInt("id") == 1)
				htmltext = "30630-11.htm";
			else if(cond == 5)
				htmltext = "30630-15.htm";
			else if(cond == 6)
				htmltext = "30630-17.htm";
			else if(cond == 7)
			{
				htmltext = "30630-18.htm";
				st.set("cond", "8");
			}
			else if(cond == 10)
				if(st.getQuestItemsCount(3334) != 0L)
					htmltext = "30630-19.htm";
				else if(st.getQuestItemsCount(3333) != 0L)
					htmltext = "30630-20.htm";
				else
					htmltext = "30630-21.htm";
		}
		else if(npcId == 30098)
		{
			if(cond == 1)
				htmltext = "30098-01.htm";
			else if(cond == 2)
				htmltext = "30098-04.htm";
			else
				htmltext = "30098-05.htm";
		}
		else if(npcId == 30110)
		{
			if(cond == 2)
			{
				if(st.getQuestItemsCount(3317) == 0L && st.getQuestItemsCount(3310) == 0L)
					htmltext = "30110-01.htm";
				else if(st.getQuestItemsCount(3310) > 0L && (st.getQuestItemsCount(3311) < 20L || st.getQuestItemsCount(3312) < 20L || st.getQuestItemsCount(3313) < 20L))
					htmltext = "30110-04.htm";
				else if(st.getQuestItemsCount(3317) == 0L && st.getQuestItemsCount(3310) > 0L)
				{
					st.takeItems(3310, -1L);
					st.takeItems(3311, -1L);
					st.takeItems(3312, -1L);
					st.takeItems(3313, -1L);
					st.giveItems(3317, 1L);
					htmltext = "30110-05.htm";
				}
				else if(st.getQuestItemsCount(3317) == 1L)
					htmltext = "30110-06.htm";
			}
			else if(cond == 6)
				htmltext = "30110-07.htm";
			else if(cond == 10)
				htmltext = "30110-10.htm";
			else
				htmltext = "30110-09.htm";
		}
		else if(npcId == 30476)
		{
			if(cond == 2)
			{
				if(st.getQuestItemsCount(3318) == 0L)
					htmltext = "30476-01.htm";
				else
					htmltext = "30476-03.htm";
			}
			else if(cond > 2)
				htmltext = "30476-04.htm";
		}
		else if(npcId == 30063)
		{
			if(cond == 2)
			{
				if(st.getQuestItemsCount(3314) == 0L && st.getQuestItemsCount(3319) == 0L)
					htmltext = "30063-01.htm";
				else if(st.getQuestItemsCount(3314) == 1L && st.getQuestItemsCount(3319) == 0L)
					htmltext = "30063-03.htm";
				else if(st.getQuestItemsCount(3319) == 1L)
					htmltext = "30063-04.htm";
			}
			else if(cond > 2)
				htmltext = "30063-05.htm";
		}
		else if(npcId == 30631 && cond == 2 && st.getQuestItemsCount(3314) > 0L)
			htmltext = "30631-01.htm";
		else if(npcId == 30314 && cond == 2)
		{
			if(st.getQuestItemsCount(3317) > 0L && st.getQuestItemsCount(3318) > 0L && st.getQuestItemsCount(3319) > 0L)
				htmltext = "30314-01.htm";
			else
				htmltext = "30314-04.htm";
		}
		else if(npcId == 30435)
		{
			if(cond == 2 && st.getQuestItemsCount(3315) > 0L)
			{
				if(st.getQuestItemsCount(3320) + st.getQuestItemsCount(3321) + st.getQuestItemsCount(3322) == 0L)
					htmltext = "30435-01.htm";
				else
					htmltext = "30435-04.htm";
			}
			else
				htmltext = "30435-05.htm";
		}
		else if(npcId == 30417)
		{
			if(cond == 6)
			{
				if(st.getQuestItemsCount(3327) > 0L || st.getQuestItemsCount(3328) > 0L)
					htmltext = "30417-04.htm";
				else if(st.getQuestItemsCount(3330) == 0L)
					htmltext = "30417-01.htm";
				else if(st.getQuestItemsCount(3330) != 0L)
				{
					htmltext = "30417-05.htm";
					st.takeItems(3330, 1L);
					st.giveItems(3029, 1L);
					if(st.getQuestItemsCount(3332) > 0L)
					{
						st.set("cond", "7");
						st.setState(2);
					}
				}
			}
			else if(cond == 7)
				htmltext = "30417-06.htm";
		}
		else if(npcId == 30188)
		{
			if(cond == 6)
			{
				if(st.getQuestItemsCount(3327) != 0L)
				{
					htmltext = "30188-01.htm";
					st.takeItems(3327, 1L);
					st.giveItems(3328, 1L);
				}
				else if(st.getQuestItemsCount(3328) > 0L && st.getQuestItemsCount(3329) < 20L)
					htmltext = "30188-02.htm";
				else if(st.getQuestItemsCount(3329) >= 20L)
				{
					htmltext = "30188-03.htm";
					st.takeItems(3329, -1L);
					st.takeItems(3328, -1L);
					st.giveItems(3330, 1L);
				}
				else if(st.getQuestItemsCount(3330) > 0L)
					htmltext = "30188-04.htm";
			}
			else if(cond == 7)
				htmltext = "30188-05.htm";
		}
		else if(npcId == 30633)
		{
			if(st.getInt("id") == 2 || cond == 8 && st.getQuestItemsCount(3335) == 0L)
				htmltext = "30633-01.htm";
			else
				htmltext = "30633-03.htm";
		}
		else if(npcId == 30632 && cond == 2)
			htmltext = "30632-01.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		for(int i = 0; i < _229_TestOfWitchcraft.DROPLIST_COND.length; ++i)
			if(cond == _229_TestOfWitchcraft.DROPLIST_COND[i][0] && npcId == _229_TestOfWitchcraft.DROPLIST_COND[i][2] && (_229_TestOfWitchcraft.DROPLIST_COND[i][3] == 0 || st.getQuestItemsCount(_229_TestOfWitchcraft.DROPLIST_COND[i][3]) > 0L))
			{
				if(npcId == 27099)
					st.takeItems(3314, -1L);
				if(_229_TestOfWitchcraft.DROPLIST_COND[i][5] == 0)
					st.rollAndGive(_229_TestOfWitchcraft.DROPLIST_COND[i][4], _229_TestOfWitchcraft.DROPLIST_COND[i][7], _229_TestOfWitchcraft.DROPLIST_COND[i][6], _229_TestOfWitchcraft.QuestProf);
				else if(st.rollAndGive(_229_TestOfWitchcraft.DROPLIST_COND[i][4], _229_TestOfWitchcraft.DROPLIST_COND[i][7], _229_TestOfWitchcraft.DROPLIST_COND[i][7], _229_TestOfWitchcraft.DROPLIST_COND[i][5], _229_TestOfWitchcraft.DROPLIST_COND[i][6], _229_TestOfWitchcraft.QuestProf) && _229_TestOfWitchcraft.DROPLIST_COND[i][1] != cond && _229_TestOfWitchcraft.DROPLIST_COND[i][1] != 0)
				{
					st.set("cond", String.valueOf(_229_TestOfWitchcraft.DROPLIST_COND[i][1]));
					st.setState(2);
				}
			}
		if(cond == 2 && st.getQuestItemsCount(3316) > 0L && npcId == 27100)
		{
			if(st.getQuestItemsCount(3320) == 0L && Rnd.chance(50))
				st.giveItems(3320, 1L);
			if(st.getQuestItemsCount(3321) == 0L && Rnd.chance(50))
				st.giveItems(3321, 1L);
			if(st.getQuestItemsCount(3322) == 0L && Rnd.chance(50))
				st.giveItems(3322, 1L);
			if(st.getQuestItemsCount(3320) != 0L && st.getQuestItemsCount(3321) != 0L && st.getQuestItemsCount(3322) != 0L)
			{
				st.takeItems(3316, -1L);
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "3");
				st.setState(2);
			}
		}
		else if(cond == 4 && npcId == 27101)
		{
			final QuestTimer timer = st.getQuestTimer("DrevanulPrinceZeruel_Fail");
			if(timer != null)
				timer.cancel();
			List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 27101);
			if(!isQuest.isEmpty())
				isQuest.get(0).deleteMe();
			st.set("cond", "5");
			st.unset("id");
			st.setState(2);
		}
		else if(cond == 9 && npcId == 27101)
		{
			if(st.getItemEquipped(7) == 3029)
			{
				st.takeItems(3335, 1L);
				st.takeItems(3332, 1L);
				st.giveItems(3333, 1L);
				st.giveItems(3334, 1L);
				st.playSound(Quest.SOUND_MIDDLE);
				st.unset("id");
				st.set("cond", "10");
				st.setState(2);
				return "You trapped the Seal of Drevanul Prince Zeruel";
			}
			final QuestTimer timer = st.getQuestTimer("DrevanulPrinceZeruel_Fail");
			if(timer != null)
				timer.cancel();
			List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 27101);
			if(!isQuest.isEmpty())
				isQuest.get(0).deleteMe();
		}
		return null;
	}

	static
	{
		DROPLIST_COND = new int[][] {
				{ 2, 0, 20557, 3310, 3311, 20, 100, 1 },
				{ 2, 0, 20565, 3310, 3313, 20, 80, 1 },
				{ 2, 0, 20577, 3310, 3312, 20, 50, 1 },
				{ 2, 0, 20578, 3310, 3312, 20, 50, 1 },
				{ 2, 0, 20579, 3310, 3312, 20, 60, 1 },
				{ 2, 0, 20580, 3310, 3312, 20, 60, 1 },
				{ 2, 0, 20581, 3310, 3312, 20, 70, 1 },
				{ 2, 0, 20582, 3310, 3312, 20, 70, 1 },
				{ 2, 0, 27099, 3314, 3319, 1, 100, 1 },
				{ 6, 0, 20601, 3328, 3329, 20, 50, 1 },
				{ 6, 0, 20602, 3328, 3329, 20, 55, 1 } };
		_229_TestOfWitchcraft.QuestProf = true;
	}
}
