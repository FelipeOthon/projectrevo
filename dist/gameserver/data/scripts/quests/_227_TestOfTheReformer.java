package quests;

import java.util.List;

import l2s.gameserver.Config;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.model.quest.QuestTimer;
import l2s.gameserver.scripts.ScriptFile;

public class _227_TestOfTheReformer extends Quest implements ScriptFile
{
	private static final int Pupina = 30118;
	private static final int Sla = 30666;
	private static final int Katari = 30668;
	private static final int OlMahumPilgrimNPC = 30732;
	private static final int Kakan = 30669;
	private static final int Nyakuri = 30670;
	private static final int Ramus = 30667;
	private static final int BookOfReform = 2822;
	private static final int LetterOfIntroduction = 2823;
	private static final int SlasLetter = 2824;
	private static final int Greetings = 2825;
	private static final int OlMahumMoney = 2826;
	private static final int KatarisLetter = 2827;
	private static final int NyakurisLetter = 2828;
	private static final int KakansLetter = 3037;
	private static final int UndeadList = 2829;
	private static final int RamussLetter = 2830;
	private static final int RippedDiary = 2831;
	private static final int HugeNail = 2832;
	private static final int LetterOfBetrayer = 2833;
	private static final int BoneFragment1 = 2834;
	private static final int BoneFragment2 = 2835;
	private static final int BoneFragment3 = 2836;
	private static final int BoneFragment4 = 2837;
	private static final int BoneFragment5 = 2838;
	private static final int MarkOfReformer = 2821;
	private static final int NamelessRevenant = 27099;
	private static final int Aruraune = 27128;
	private static final int OlMahumInspector = 27129;
	private static final int OlMahumBetrayer = 27130;
	private static final int CrimsonWerewolf = 27131;
	private static final int KrudelLizardman = 27132;
	private static final int SilentHorror = 20404;
	private static final int SkeletonLord = 20104;
	private static final int SkeletonMarksman = 20102;
	private static final int MiserySkeleton = 20022;
	private static final int SkeletonArcher = 20100;
	public final int[][] DROPLIST_COND;
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

	public _227_TestOfTheReformer()
	{
		super(false);
		DROPLIST_COND = new int[][] {
				{ 18, 0, 20404, 0, 2834, 1, 70, 1 },
				{ 18, 0, 20104, 0, 2835, 1, 70, 1 },
				{ 18, 0, 20102, 0, 2836, 1, 70, 1 },
				{ 18, 0, 20022, 0, 2837, 1, 70, 1 },
				{ 18, 0, 20100, 0, 2838, 1, 70, 1 } };
		this.addStartNpc(30118);
		this.addTalkId(new int[] { 30666 });
		this.addTalkId(new int[] { 30668 });
		this.addTalkId(new int[] { 30732 });
		this.addTalkId(new int[] { 30669 });
		this.addTalkId(new int[] { 30670 });
		this.addTalkId(new int[] { 30667 });
		this.addKillId(new int[] { 27099 });
		this.addKillId(new int[] { 27128 });
		this.addKillId(new int[] { 27129 });
		this.addKillId(new int[] { 27130 });
		this.addKillId(new int[] { 27131 });
		this.addKillId(new int[] { 27132 });
		for(int i = 0; i < DROPLIST_COND.length; ++i)
		{
			this.addKillId(new int[] { DROPLIST_COND[i][2] });
			addQuestItem(new int[] { DROPLIST_COND[i][4] });
		}
		addQuestItem(new int[] { 2822, 2832, 2823, 2824, 2827, 2833, 2826, 2828, 2829, 2825, 3037, 2830, 2831 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("30118-04.htm"))
		{
			st.giveItems(2822, 1L);
			if(!st.getPlayer().getVarBoolean("dd3"))
			{
				st.giveItems(7562, 60L);
				st.getPlayer().setVar("dd3", "1");
			}
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30118-06.htm"))
		{
			st.takeItems(2832, -1L);
			st.takeItems(2822, -1L);
			st.giveItems(2823, 1L);
			st.set("cond", "4");
			st.setState(2);
		}
		else if(event.equalsIgnoreCase("30666-04.htm"))
		{
			st.takeItems(2823, -1L);
			st.giveItems(2824, 1L);
			st.set("cond", "5");
			st.setState(2);
		}
		else if(event.equalsIgnoreCase("30669-03.htm"))
		{
			if(GameObjectsStorage.getNpcs(false, 27131).isEmpty())
			{
				st.set("cond", "12");
				st.setState(2);
				st.addSpawn(27131);
				st.startQuestTimer("Wait4", 300000L);
			}
			else
			{
				if(st.getQuestTimer("Wait4") == null)
					st.startQuestTimer("Wait4", 300000L);
				htmltext = "<html><head><body>Plees wait 5 minutes</body></html>";
			}
		}
		else if(event.equalsIgnoreCase("30670-03.htm"))
		{
			if(GameObjectsStorage.getNpcs(false, 27132).isEmpty())
			{
				st.set("cond", "15");
				st.setState(2);
				st.addSpawn(27132);
				st.startQuestTimer("Wait5", 300000L);
			}
			else
			{
				if(st.getQuestTimer("Wait5") == null)
					st.startQuestTimer("Wait5", 300000L);
				htmltext = "<html><head><body>Plees wait 5 minutes</body></html>";
			}
		}
		else
		{
			if(event.equalsIgnoreCase("Wait1"))
			{
				List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 27128);
				if(!isQuest.isEmpty())
					isQuest.get(0).deleteMe();
				if(st.getInt("cond") == 2)
					st.set("cond", "1");
				return null;
			}
			if(event.equalsIgnoreCase("Wait2"))
			{
				List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 27129);
				if(!isQuest.isEmpty())
					isQuest.get(0).deleteMe();
				isQuest = GameObjectsStorage.getNpcs(false, 30732);
				if(!isQuest.isEmpty())
					isQuest.get(0).deleteMe();
				if(st.getInt("cond") == 6)
					st.set("cond", "5");
				return null;
			}
			if(event.equalsIgnoreCase("Wait3"))
			{
				List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 27130);
				if(!isQuest.isEmpty())
					isQuest.get(0).deleteMe();
				return null;
			}
			if(event.equalsIgnoreCase("Wait4"))
			{
				List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 27131);
				if(!isQuest.isEmpty())
					isQuest.get(0).deleteMe();
				if(st.getInt("cond") == 12)
					st.set("cond", "11");
				return null;
			}
			if(event.equalsIgnoreCase("Wait5"))
			{
				List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 27132);
				if(!isQuest.isEmpty())
					isQuest.get(0).deleteMe();
				if(st.getInt("cond") == 15)
					st.set("cond", "14");
				return null;
			}
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(npcId == 30118)
		{
			if(st.getQuestItemsCount(2821) != 0L)
			{
				htmltext = "completed";
				st.exitCurrentQuest(true);
			}
			else if(cond == 0)
			{
				if(st.getPlayer().getClassId().getId() == 15 || st.getPlayer().getClassId().getId() == 42)
				{
					if(st.getPlayer().getLevel() >= 39)
						htmltext = "30118-03.htm";
					else
					{
						htmltext = "30118-01.htm";
						st.exitCurrentQuest(true);
					}
				}
				else
				{
					htmltext = "30118-02.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 3)
				htmltext = "30118-05.htm";
			else if(cond >= 4)
				htmltext = "30118-07.htm";
		}
		else if(npcId == 30666)
		{
			if(cond == 4)
				htmltext = "30666-01.htm";
			else if(cond == 5)
				htmltext = "30666-05.htm";
			else if(cond == 10)
			{
				st.takeItems(2826, -1L);
				st.giveItems(2825, 3L);
				htmltext = "30666-06.htm";
				st.set("cond", "11");
				st.setState(2);
			}
			else if(cond == 20)
			{
				st.takeItems(2827, -1L);
				st.takeItems(3037, -1L);
				st.takeItems(2828, -1L);
				st.takeItems(2830, -1L);
				st.giveItems(2821, 1L);
				if(!st.getPlayer().getVarBoolean("prof2.3"))
				{
					st.addExpAndSp(626422L, 42986L, true);
					st.giveItems(57, 113264L, Config.RATE_QUESTS_OCCUPATION_CHANGE);
					st.getPlayer().setVar("prof2.3", "1");
				}
				htmltext = "30666-07.htm";
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
		}
		else if(npcId == 30668)
		{
			if(cond == 5 || cond == 6)
			{
				List<NpcInstance> NPC = GameObjectsStorage.getNpcs(false, 30732);
				List<NpcInstance> Mob = GameObjectsStorage.getNpcs(false, 27129);
				if(NPC.isEmpty() && Mob.isEmpty())
				{
					st.takeItems(2824, -1L);
					htmltext = "30668-01.htm";
					st.set("cond", "6");
					st.setState(2);
					st.addSpawn(30732);
					st.addSpawn(27129);
					st.startQuestTimer("Wait2", 300000L);
				}
				else
				{
					if(st.getQuestTimer("Wait2") == null)
						st.startQuestTimer("Wait2", 300000L);
					htmltext = "<html><head><body>Plees wait 5 minutes</body></html>";
				}
			}
			else if(cond == 8)
			{
				if(GameObjectsStorage.getNpcs(false, 27130).isEmpty())
				{
					htmltext = "30668-02.htm";
					st.addSpawn(27130);
					st.startQuestTimer("Wait3", 300000L);
				}
				else
				{
					if(st.getQuestTimer("Wait3") == null)
						st.startQuestTimer("Wait3", 300000L);
					htmltext = "<html><head><body>Plees wait 5 minutes</body></html>";
				}
			}
			else if(cond == 9)
			{
				st.takeItems(2833, -1L);
				st.giveItems(2827, 1L);
				htmltext = "30668-03.htm";
				st.set("cond", "10");
				st.setState(2);
			}
		}
		else if(npcId == 30732)
		{
			if(cond == 7)
			{
				st.giveItems(2826, 1L);
				htmltext = "30732-01.htm";
				st.set("cond", "8");
				st.setState(2);
				List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 27129);
				if(!isQuest.isEmpty())
					isQuest.get(0).deleteMe();
				isQuest = GameObjectsStorage.getNpcs(false, 30732);
				if(!isQuest.isEmpty())
					isQuest.get(0).deleteMe();
				final QuestTimer timer = st.getQuestTimer("Wait2");
				if(timer != null)
					timer.cancel();
			}
		}
		else if(npcId == 30669)
		{
			if(cond == 11 || cond == 12)
				htmltext = "30669-01.htm";
			else if(cond == 13)
			{
				st.takeItems(2825, 1L);
				st.giveItems(3037, 1L);
				htmltext = "30669-04.htm";
				st.set("cond", "14");
				st.setState(2);
			}
		}
		else if(npcId == 30670)
		{
			if(cond == 14 || cond == 15)
				htmltext = "30670-01.htm";
			else if(cond == 16)
			{
				st.takeItems(2825, 1L);
				st.giveItems(2828, 1L);
				htmltext = "30670-04.htm";
				st.set("cond", "17");
				st.setState(2);
			}
		}
		else if(npcId == 30667)
			if(cond == 17)
			{
				st.takeItems(2825, -1L);
				st.giveItems(2829, 1L);
				htmltext = "30667-01.htm";
				st.set("cond", "18");
				st.setState(2);
			}
			else if(cond == 19)
			{
				st.takeItems(2834, -1L);
				st.takeItems(2835, -1L);
				st.takeItems(2836, -1L);
				st.takeItems(2837, -1L);
				st.takeItems(2838, -1L);
				st.takeItems(2829, -1L);
				st.giveItems(2830, 1L);
				htmltext = "30667-03.htm";
				st.set("cond", "20");
				st.setState(2);
			}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		for(int i = 0; i < DROPLIST_COND.length; ++i)
			if(cond == DROPLIST_COND[i][0] && npcId == DROPLIST_COND[i][2] && (DROPLIST_COND[i][3] == 0 || st.getQuestItemsCount(DROPLIST_COND[i][3]) > 0L))
				if(DROPLIST_COND[i][5] == 0)
					st.rollAndGive(DROPLIST_COND[i][4], DROPLIST_COND[i][7], DROPLIST_COND[i][6], _227_TestOfTheReformer.QuestProf);
				else if(st.rollAndGive(DROPLIST_COND[i][4], DROPLIST_COND[i][7], DROPLIST_COND[i][7], DROPLIST_COND[i][5], DROPLIST_COND[i][6], _227_TestOfTheReformer.QuestProf) && DROPLIST_COND[i][1] != cond && DROPLIST_COND[i][1] != 0)
				{
					st.set("cond", String.valueOf(DROPLIST_COND[i][1]));
					st.setState(2);
				}
		if(cond == 18 && st.getQuestItemsCount(2834) != 0L && st.getQuestItemsCount(2835) != 0L && st.getQuestItemsCount(2836) != 0L && st.getQuestItemsCount(2837) != 0L && st.getQuestItemsCount(2838) != 0L)
		{
			st.set("cond", "19");
			st.setState(2);
		}
		else if(npcId == 27099 && (cond == 1 || cond == 2))
		{
			if(st.getQuestItemsCount(2831) < 6L)
				st.giveItems(2831, 1L);
			else if(GameObjectsStorage.getNpcs(false, 27128).isEmpty())
			{
				st.takeItems(2831, -1L);
				st.set("cond", "2");
				st.setState(2);
				st.addSpawn(27128);
				st.startQuestTimer("Wait1", 300000L);
			}
			else if(st.getQuestTimer("Wait1") == null)
				st.startQuestTimer("Wait1", 300000L);
		}
		else if(npcId == 27128)
		{
			List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 27128);
			if(!isQuest.isEmpty())
				isQuest.get(0).deleteMe();
			if(cond == 2)
			{
				if(st.getQuestItemsCount(2832) == 0L)
					st.giveItems(2832, 1L);
				st.set("cond", "3");
				st.setState(2);
				final QuestTimer timer = st.getQuestTimer("Wait1");
				if(timer != null)
					timer.cancel();
			}
		}
		else if(npcId == 27129)
		{
			List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 27129);
			if(!isQuest.isEmpty())
				isQuest.get(0).deleteMe();
			final QuestTimer timer = st.getQuestTimer("Wait2");
			if(timer != null)
				timer.cancel();
			if(cond == 6)
			{
				st.set("cond", "7");
				st.setState(2);
			}
		}
		else if(npcId == 27130)
		{
			List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 27130);
			if(!isQuest.isEmpty())
				isQuest.get(0).deleteMe();
			final QuestTimer timer = st.getQuestTimer("Wait3");
			if(timer != null)
				timer.cancel();
			if(cond == 8)
			{
				if(st.getQuestItemsCount(2833) == 0L)
					st.giveItems(2833, 1L);
				st.set("cond", "9");
				st.setState(2);
			}
		}
		else if(npcId == 27131)
		{
			List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 27131);
			if(!isQuest.isEmpty())
				isQuest.get(0).deleteMe();
			final QuestTimer timer = st.getQuestTimer("Wait4");
			if(timer != null)
				timer.cancel();
			if(cond == 12)
			{
				st.set("cond", "13");
				st.setState(2);
			}
		}
		else if(npcId == 27132)
		{
			List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 27132);
			if(!isQuest.isEmpty())
				isQuest.get(0).deleteMe();
			final QuestTimer timer = st.getQuestTimer("Wait5");
			if(timer != null)
				timer.cancel();
			if(cond == 15)
			{
				st.set("cond", "16");
				st.setState(2);
			}
		}
		return null;
	}

	static
	{
		_227_TestOfTheReformer.QuestProf = true;
	}
}
