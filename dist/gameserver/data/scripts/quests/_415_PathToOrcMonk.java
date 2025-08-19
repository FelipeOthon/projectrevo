package quests;

import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _415_PathToOrcMonk extends Quest implements ScriptFile
{
	private static final int Urutu = 30587;
	private static final int Rosheek = 30590;
	private static final int Kasman = 30501;
	private static final int Toruku = 30591;
	private static final int Pomegranate = 1593;
	private static final int KashaBearClaw = 1600;
	private static final int KashaBladeSpiderTalon = 1601;
	private static final int ScarletSalamanderScale = 1602;
	private static final int LeatherPouch1st = 1594;
	private static final int LeatherPouchFull1st = 1597;
	private static final int LeatherPouch2st = 1595;
	private static final int LeatherPouchFull2st = 1598;
	private static final int LeatherPouch3st = 1596;
	private static final int LeatherPouchFull3st = 1599;
	private static final int LeatherPouch4st = 1607;
	private static final int LeatherPouchFull4st = 1608;
	private static final int FierySpiritScroll = 1603;
	private static final int RosheeksLetter = 1604;
	private static final int GantakisLetterOfRecommendation = 1605;
	private static final int Fig = 1606;
	private static final int VukuOrcTusk = 1609;
	private static final int RatmanFang = 1610;
	private static final int LangkLizardmanTooth = 1611;
	private static final int FelimLizardmanTooth = 1612;
	private static final int IronWillScroll = 1613;
	private static final int TorukusLetter = 1614;
	private static final int KhavatariTotem = 1615;
	private static final int KashaBear = 20479;
	private static final int KashaBladeSpider = 20478;
	private static final int ScarletSalamander = 20415;
	private static final int VukuOrcFighter = 20017;
	private static final int RatmanWarrior = 20359;
	private static final int LangkLizardmanWarrior = 20024;
	private static final int FelimLizardmanWarrior = 20014;
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

	public _415_PathToOrcMonk()
	{
		super(false);
		this.addStartNpc(30587);
		this.addTalkId(new int[] { 30590 });
		this.addTalkId(new int[] { 30501 });
		this.addTalkId(new int[] { 30591 });
		for(int i = 0; i < _415_PathToOrcMonk.DROPLIST_COND.length; ++i)
		{
			this.addKillId(new int[] { _415_PathToOrcMonk.DROPLIST_COND[i][2] });
			addQuestItem(new int[] { _415_PathToOrcMonk.DROPLIST_COND[i][4] });
		}
		addQuestItem(new int[] { 1593, 1594, 1597, 1595, 1598, 1596, 1599, 1606, 1603, 1604, 1605, 1607, 1608, 1613, 1614 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("gantaki_zu_urutu_q0415_06.htm"))
		{
			st.giveItems(1593, 1L);
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(npcId == 30587)
		{
			if(st.getQuestItemsCount(1615) != 0L)
			{
				htmltext = "gantaki_zu_urutu_q0415_04.htm";
				st.exitCurrentQuest(true);
			}
			else if(cond == 0)
			{
				if(st.getPlayer().getClassId().getId() != 44)
				{
					if(st.getPlayer().getClassId().getId() == 47)
						htmltext = "gantaki_zu_urutu_q0415_02a.htm";
					else
						htmltext = "gantaki_zu_urutu_q0415_02.htm";
					st.exitCurrentQuest(true);
				}
				else if(st.getPlayer().getLevel() < 18)
				{
					htmltext = "gantaki_zu_urutu_q0415_03.htm";
					st.exitCurrentQuest(true);
				}
				else
					htmltext = "gantaki_zu_urutu_q0415_01.htm";
			}
			else if(cond == 1)
				htmltext = "gantaki_zu_urutu_q0415_07.htm";
			else if(cond >= 2 && cond <= 7)
				htmltext = "gantaki_zu_urutu_q0415_08.htm";
			else if(cond == 8)
			{
				st.takeItems(1604, 1L);
				st.giveItems(1605, 1L);
				htmltext = "gantaki_zu_urutu_q0415_09.htm";
				st.set("cond", "9");
				st.setState(2);
			}
			else if(cond == 9)
				htmltext = "gantaki_zu_urutu_q0415_10.htm";
			else if(cond >= 10)
				htmltext = "gantaki_zu_urutu_q0415_11.htm";
		}
		else if(npcId == 30590)
		{
			if(cond == 1)
			{
				st.takeItems(1593, -1L);
				st.giveItems(1594, 1L);
				htmltext = "khavatari_rosheek_q0415_01.htm";
				st.set("cond", "2");
				st.setState(2);
			}
			else if(cond == 2)
				htmltext = "khavatari_rosheek_q0415_02.htm";
			else if(cond == 3)
			{
				htmltext = "khavatari_rosheek_q0415_03.htm";
				st.takeItems(1597, -1L);
				st.giveItems(1595, 1L);
				st.set("cond", "4");
				st.setState(2);
			}
			else if(cond == 4)
				htmltext = "khavatari_rosheek_q0415_04.htm";
			else if(cond == 5)
			{
				st.takeItems(1598, -1L);
				st.giveItems(1596, 1L);
				htmltext = "khavatari_rosheek_q0415_05.htm";
				st.set("cond", "6");
				st.setState(2);
			}
			else if(cond == 6)
				htmltext = "khavatari_rosheek_q0415_06.htm";
			else if(cond == 7)
			{
				st.takeItems(1599, -1L);
				st.giveItems(1603, 1L);
				st.giveItems(1604, 1L);
				htmltext = "khavatari_rosheek_q0415_07.htm";
				st.set("cond", "8");
				st.setState(2);
			}
			else if(cond == 8)
				htmltext = "khavatari_rosheek_q0415_08.htm";
			else if(cond == 9)
				htmltext = "khavatari_rosheek_q0415_09.htm";
		}
		else if(npcId == 30501)
		{
			if(cond == 9)
			{
				st.takeItems(1605, -1L);
				st.giveItems(1606, 1L);
				htmltext = "prefect_kasman_q0415_01.htm";
				st.set("cond", "10");
				st.setState(2);
			}
			else if(cond == 10)
				htmltext = "prefect_kasman_q0415_02.htm";
			else if(cond == 11 || cond == 12)
				htmltext = "prefect_kasman_q0415_03.htm";
			else if(cond == 13)
			{
				st.takeItems(1603, -1L);
				st.takeItems(1613, -1L);
				st.takeItems(1614, -1L);
				htmltext = "prefect_kasman_q0415_04.htm";
				if(st.getPlayer().getClassId().getLevel() == 1)
				{
					st.giveItems(1615, 1L);
					if(!st.getPlayer().getVarBoolean("prof1"))
					{
						st.getPlayer().setVar("prof1", "1");
						st.addExpAndSp(295862L, 19344L, true);
						st.giveItems(57, 81900L, Config.RATE_QUESTS_OCCUPATION_CHANGE);
					}
				}
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
		}
		else if(npcId == 30591)
			if(cond == 10)
			{
				st.takeItems(1606, -1L);
				st.giveItems(1607, 1L);
				htmltext = "khavatari_toruku_q0415_01.htm";
				st.set("cond", "11");
				st.setState(2);
			}
			else if(cond == 11)
				htmltext = "khavatari_toruku_q0415_02.htm";
			else if(cond == 12)
			{
				st.takeItems(1608, -1L);
				st.giveItems(1613, 1L);
				st.giveItems(1614, 1L);
				htmltext = "khavatari_toruku_q0415_03.htm";
				st.set("cond", "13");
				st.setState(2);
			}
			else if(cond == 13)
				htmltext = "khavatari_toruku_q0415_04.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		for(int i = 0; i < _415_PathToOrcMonk.DROPLIST_COND.length; ++i)
			if(cond == _415_PathToOrcMonk.DROPLIST_COND[i][0] && npcId == _415_PathToOrcMonk.DROPLIST_COND[i][2] && (_415_PathToOrcMonk.DROPLIST_COND[i][3] == 0 || st.getQuestItemsCount(_415_PathToOrcMonk.DROPLIST_COND[i][3]) > 0L))
				if(_415_PathToOrcMonk.DROPLIST_COND[i][5] == 0)
					st.rollAndGive(_415_PathToOrcMonk.DROPLIST_COND[i][4], _415_PathToOrcMonk.DROPLIST_COND[i][7], _415_PathToOrcMonk.DROPLIST_COND[i][6], _415_PathToOrcMonk.QuestProf);
				else if(st.rollAndGive(_415_PathToOrcMonk.DROPLIST_COND[i][4], _415_PathToOrcMonk.DROPLIST_COND[i][7], _415_PathToOrcMonk.DROPLIST_COND[i][7], _415_PathToOrcMonk.DROPLIST_COND[i][5], _415_PathToOrcMonk.DROPLIST_COND[i][6], _415_PathToOrcMonk.QuestProf) && _415_PathToOrcMonk.DROPLIST_COND[i][1] != cond && _415_PathToOrcMonk.DROPLIST_COND[i][1] != 0)
				{
					st.set("cond", String.valueOf(_415_PathToOrcMonk.DROPLIST_COND[i][1]));
					st.setState(2);
				}
		if(cond == 3 && st.getQuestItemsCount(1597) == 0L)
		{
			st.takeItems(1600, -1L);
			st.takeItems(1594, -1L);
			st.giveItems(1597, 1L);
		}
		else if(cond == 5 && st.getQuestItemsCount(1598) == 0L)
		{
			st.takeItems(1601, -1L);
			st.takeItems(1595, -1L);
			st.giveItems(1598, 1L);
		}
		else if(cond == 7 && st.getQuestItemsCount(1599) == 0L)
		{
			st.takeItems(1602, -1L);
			st.takeItems(1596, -1L);
			st.giveItems(1599, 1L);
		}
		else if(cond == 11 && st.getQuestItemsCount(1610) >= 3L && st.getQuestItemsCount(1611) >= 3L && st.getQuestItemsCount(1612) >= 3L && st.getQuestItemsCount(1609) >= 3L)
		{
			st.takeItems(1609, -1L);
			st.takeItems(1610, -1L);
			st.takeItems(1611, -1L);
			st.takeItems(1612, -1L);
			st.takeItems(1607, -1L);
			st.giveItems(1608, 1L);
			st.set("cond", "12");
			st.setState(2);
		}
		return null;
	}

	static
	{
		DROPLIST_COND = new int[][] {
				{ 2, 3, 20479, 1594, 1600, 5, 70, 1 },
				{ 4, 5, 20478, 1595, 1601, 5, 70, 1 },
				{ 6, 7, 20415, 1596, 1602, 5, 70, 1 },
				{ 11, 0, 20017, 1607, 1609, 3, 70, 1 },
				{ 11, 0, 20359, 1607, 1610, 3, 70, 1 },
				{ 11, 0, 20024, 1607, 1611, 3, 70, 1 },
				{ 11, 0, 20014, 1607, 1612, 3, 70, 1 } };
		_415_PathToOrcMonk.QuestProf = true;
	}
}
