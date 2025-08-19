package quests;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;

public class _022_TragedyInVonHellmannForest extends Quest implements ScriptFile
{
	private static final int Well = 31527;
	private static final int Tifaren = 31334;
	private static final int Innocentin = 31328;
	private static final int SoulOfWell = 27217;
	private static final int GhostOfPriest = 31528;
	private static final int GhostOfAdventurer = 31529;
	private static final int ReportBox = 7147;
	private static final int LostSkullOfElf = 7142;
	private static final int CrossOfEinhasad = 7141;
	private static final int SealedReportBox = 7146;
	private static final int LetterOfInnocentin = 7143;
	private static final int JewelOfAdventurerRed = 7145;
	private static final int JewelOfAdventurerGreen = 7144;
	private static final List<Integer> Mobs;
	private static NpcInstance GhostOfPriestInstance;
	private static NpcInstance SoulOfWellInstance;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _022_TragedyInVonHellmannForest()
	{
		super(false);
		this.addStartNpc(31334);
		this.addTalkId(new int[] { 31334 });
		this.addTalkId(new int[] { 31528 });
		this.addTalkId(new int[] { 31328 });
		this.addTalkId(new int[] { 31529 });
		this.addTalkId(new int[] { 31527 });
		addAttackId(new int[] { 27217 });
		this.addKillId(new int[] { 27217 });
		for(int npcId = 21547; npcId <= 21578; ++npcId)
			this.addKillId(new int[] { npcId });
		addQuestItem(new int[] { 7142 });
	}

	private void spawnGhostOfPriest(final QuestState st)
	{
		_022_TragedyInVonHellmannForest.GhostOfPriestInstance = Functions.spawn(st.getPlayer().getLoc().rnd(50, 100, false), 31528);
	}

	private void spawnSoulOfWell(final QuestState st)
	{
		_022_TragedyInVonHellmannForest.SoulOfWellInstance = Functions.spawn(st.getPlayer().getLoc().rnd(50, 100, false), 27217);
	}

	private void despawnGhostOfPriest()
	{
		if(_022_TragedyInVonHellmannForest.GhostOfPriestInstance != null)
			_022_TragedyInVonHellmannForest.GhostOfPriestInstance.deleteMe();
	}

	private void despawnSoulOfWell()
	{
		if(_022_TragedyInVonHellmannForest.SoulOfWellInstance != null)
			_022_TragedyInVonHellmannForest.SoulOfWellInstance.deleteMe();
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("31334-03.htm"))
		{
			st.setState(2);
			st.setCond(3);
			st.takeItems(7141, -1L);
		}
		else if(event.equalsIgnoreCase("31334-06.htm"))
			st.setCond(4);
		else if(event.equalsIgnoreCase("31334-09.htm"))
		{
			st.setCond(6);
			st.takeItems(7142, 1L);
			despawnGhostOfPriest();
			spawnGhostOfPriest(st);
		}
		else if(event.equalsIgnoreCase("31528-07.htm"))
		{
			despawnGhostOfPriest();
			st.setCond(7);
		}
		else if(event.equalsIgnoreCase("31328-06.htm"))
		{
			st.setCond(8);
			st.giveItems(7143, 1L);
		}
		else if(event.equalsIgnoreCase("31529-09.htm"))
		{
			st.setCond(9);
			st.takeItems(7143, 1L);
		}
		else if(event.equalsIgnoreCase("explore"))
		{
			despawnSoulOfWell();
			spawnSoulOfWell(st);
			st.setCond(10);
			st.giveItems(7144, 1L);
			htmltext = "<html><body>Attack Soul of Well but do not kill while stone will not change colour...</body></html>";
		}
		else if(event.equalsIgnoreCase("attack_timer"))
		{
			despawnSoulOfWell();
			st.giveItems(7145, 1L);
			st.takeItems(7144, -1L);
			st.setCond(11);
			if(st.getQuestTimer("attack_timer") != null)
				st.getQuestTimer("attack_timer").cancel();
		}
		else if(event.equalsIgnoreCase("31328-08.htm"))
		{
			st.startQuestTimer("wait_timer", 600000L);
			st.setCond(15);
			st.takeItems(7147, 1L);
		}
		else if(event.equalsIgnoreCase("wait_timer"))
		{
			st.setCond(16);
			htmltext = "<html><body>Innocentin wants with you to speak...</body></html>";
		}
		else if(event.equalsIgnoreCase("31328-16.htm"))
		{
			st.startQuestTimer("next_wait_timer", 300000L);
			st.setCond(17);
		}
		else if(event.equalsIgnoreCase("next_wait_timer"))
			st.setCond(18);
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getCond();
		String htmltext = "noquest";
		if(npcId == 31334)
		{
			if(cond == 0)
			{
				final QuestState hiddenTruth = st.getPlayer().getQuestState(21);
				if(hiddenTruth != null)
				{
					if(hiddenTruth.isCompleted())
						htmltext = "31334-01.htm";
					else
						htmltext = "<html><head><body>You not complite quest Hidden Truth...</body></html>";
				}
				else
					htmltext = "<html><head><body>You not complite quest Hidden Truth...</body></html>";
			}
			else
			{
				if(cond == 3)
					return "31334-04.htm";
				if(cond == 4)
					htmltext = "31334-06.htm";
				else if(cond == 5)
				{
					if(st.getQuestItemsCount(7142) != 0L)
						htmltext = "31334-07.htm";
					else
					{
						st.setCond(4);
						htmltext = "31334-06.htm";
					}
				}
				else if(cond == 6)
				{
					despawnGhostOfPriest();
					spawnGhostOfPriest(st);
					htmltext = "31334-09.htm";
				}
			}
		}
		else if(npcId == 31528)
		{
			if(cond == 6)
				htmltext = "31528-00.htm";
			else if(cond == 7)
				htmltext = "31528-07.htm";
		}
		else if(npcId == 31328)
		{
			if(cond == 0)
				htmltext = "31328-17.htm";
			if(cond == 7)
				htmltext = "31328-00.htm";
			else if(cond == 8)
				htmltext = "31328-06.htm";
			else if(cond == 14)
			{
				if(st.getQuestItemsCount(7147) != 0L)
					htmltext = "31328-07.htm";
				else
				{
					st.setCond(13);
					htmltext = "Go away!";
				}
			}
			else if(cond == 15)
			{
				if(st.getQuestTimer("wait_timer") == null)
					st.setCond(16);
				htmltext = "31328-09.htm";
			}
			else if(cond == 16)
				htmltext = "31328-08a.htm";
			else if(cond == 17)
			{
				if(st.getQuestTimer("next_wait_timer") == null)
					st.setCond(18);
				htmltext = "31328-16a.htm";
			}
			else if(cond == 18)
			{
				htmltext = "31328-17.htm";
				st.addExpAndSp(345966L, 31578L);
				st.exitCurrentQuest(false);
			}
		}
		else if(npcId == 31529)
		{
			if(cond == 8)
			{
				if(st.getQuestItemsCount(7143) != 0L)
					htmltext = "31529-00.htm";
				else
					htmltext = "You have no Letter of Innocentin! Are they Please returned to High Priest Innocentin...";
			}
			else if(cond == 9)
				htmltext = "31529-09.htm";
			else if(cond == 11)
			{
				if(st.getQuestItemsCount(7145) != 0L)
				{
					htmltext = "31529-10.htm";
					st.takeItems(7145, 1L);
					st.setCond(12);
				}
				else
				{
					st.setCond(9);
					htmltext = "31529-09.htm";
				}
			}
			else if(cond == 13)
				if(st.getQuestItemsCount(7146) != 0L)
				{
					htmltext = "31529-11.htm";
					st.setCond(14);
					st.takeItems(7146, 1L);
					st.giveItems(7147, 1L);
				}
				else
				{
					st.setCond(12);
					htmltext = "31529-10.htm";
				}
		}
		else if(npcId == 31527)
			if(cond == 9)
				htmltext = "31527-00.htm";
			else if(cond == 10)
			{
				despawnSoulOfWell();
				spawnSoulOfWell(st);
				st.setCond(10);
				if(st.getQuestTimer("attack_timer") != null)
					st.getQuestTimer("attack_timer").cancel();
				st.takeItems(7144, -1L);
				st.takeItems(7145, -1L);
				st.giveItems(7144, 1L);
				htmltext = "<html><body>Attack Soul of Well but do not kill while stone will not change colour...</body></html>";
			}
			else if(cond == 12)
			{
				htmltext = "31527-01.htm";
				st.setCond(13);
				st.giveItems(7146, 1L);
			}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getCond();
		if(_022_TragedyInVonHellmannForest.Mobs.contains(npcId) && cond == 4 && Rnd.chance(99))
		{
			st.giveItems(7142, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
			st.setCond(5);
		}
		if(npcId == 27217 && cond == 10)
		{
			st.setCond(9);
			st.takeItems(7144, -1L);
			st.takeItems(7145, -1L);
			if(st.getQuestTimer("attack_timer") != null)
				st.getQuestTimer("attack_timer").cancel();
		}
		return null;
	}

	@Override
	public String onAttack(final NpcInstance npc, final QuestState qs)
	{
		final int npcId = npc.getNpcId();
		final int cond = qs.getCond();
		if(npcId == 27217 && cond == 10 && qs.getQuestTimer("attack_timer") == null)
			qs.startQuestTimer("attack_timer", 60000L);
		return null;
	}

	static
	{
		Mobs = new ArrayList<Integer>();
		for(final int i : new int[] {
				21547,
				21548,
				21549,
				21550,
				21551,
				21552,
				21553,
				21554,
				21555,
				21556,
				21557,
				21558,
				21559,
				21560,
				21561,
				21562,
				21563,
				21564,
				21565,
				21566,
				21567,
				21568,
				21569,
				21570,
				21571,
				21572,
				21573,
				21574,
				21575,
				21576,
				21577,
				21578 })
			_022_TragedyInVonHellmannForest.Mobs.add(i);
		_022_TragedyInVonHellmannForest.GhostOfPriestInstance = null;
		_022_TragedyInVonHellmannForest.SoulOfWellInstance = null;
	}
}
