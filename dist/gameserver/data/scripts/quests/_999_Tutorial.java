package quests;

import java.util.HashMap;

import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _999_Tutorial extends Quest implements ScriptFile
{
	private static class Event
	{
		public String htm;
		public int radarX;
		public int radarY;
		public int radarZ;
		public int item;
		public int classId1;
		public int gift1;
		public int count1;
		public int classId2;
		public int gift2;
		public int count2;

		public Event(final String htm, final int radarX, final int radarY, final int radarZ, final int item, final int classId1, final int gift1, final int count1, final int classId2, final int gift2, final int count2)
		{
			this.htm = htm;
			this.radarX = radarX;
			this.radarY = radarY;
			this.radarZ = radarZ;
			this.item = item;
			this.classId1 = classId1;
			this.gift1 = gift1;
			this.count1 = count1;
			this.classId2 = classId2;
			this.gift2 = gift2;
			this.count2 = count2;
		}
	}

	private static class Talk
	{
		public int raceId;
		public String[] htmlfiles;
		public int npcTyp;
		public int item;

		public Talk(final int raceId, final String[] htmlfiles, final int npcTyp, final int item)
		{
			this.raceId = raceId;
			this.htmlfiles = htmlfiles;
			this.npcTyp = npcTyp;
			this.item = item;
		}
	}

	private static int qnTutorial = 255;
	private static int RECOMMENDATION_01 = 1067;
	private static int RECOMMENDATION_02 = 1068;
	private static int LEAF_OF_MOTHERTREE = 1069;
	private static int BLOOD_OF_JUNDIN = 1070;
	private static int LICENSE_OF_MINER = 1498;
	private static int VOUCHER_OF_FLAME = 1496;
	private static int SOULSHOT_NOVICE = 5789;
	private static int SPIRITSHOT_NOVICE = 5790;
	private static int BLUE_GEM = 6353;
	private static int TOKEN = 8542;
	private static int SCROLL = 8594;
	private static HashMap<String, Event> events = new HashMap<String, Event>();
	private static HashMap<Integer, Talk> talks = new HashMap<Integer, Talk>();

	static
	{
		events.put("30008_02", new Event("30008-03.htm", 0, 0, 0, RECOMMENDATION_01, 0, SOULSHOT_NOVICE, 200, 0, 0, 0));
		events.put("30017_02", new Event("30017-03.htm", 0, 0, 0, RECOMMENDATION_02, 10, SPIRITSHOT_NOVICE, 100, 0, 0, 0));
		events.put("30370_02", new Event("30370-03.htm", 0, 0, 0, LEAF_OF_MOTHERTREE, 25, SPIRITSHOT_NOVICE, 100, 18, SOULSHOT_NOVICE, 200));
		events.put("30129_02", new Event("30129-03.htm", 0, 0, 0, BLOOD_OF_JUNDIN, 38, SPIRITSHOT_NOVICE, 100, 31, SOULSHOT_NOVICE, 200));
		events.put("30528_02", new Event("30528-03.htm", 0, 0, 0, LICENSE_OF_MINER, 53, SOULSHOT_NOVICE, 200, 0, 0, 0));
		events.put("30573_02", new Event("30573-03.htm", 0, 0, 0, VOUCHER_OF_FLAME, 49, SPIRITSHOT_NOVICE, 100, 44, SOULSHOT_NOVICE, 200));
		talks.put(30017, new Talk(0, new String[] { "30017-01.htm", "30017-02.htm", "30017-04.htm" }, 0, 0));
		talks.put(30008, new Talk(0, new String[] { "30008-01.htm", "30008-02.htm", "30008-04.htm" }, 0, 0));
		talks.put(30370, new Talk(1, new String[] { "30370-01.htm", "30370-02.htm", "30370-04.htm" }, 0, 0));
		talks.put(30129, new Talk(2, new String[] { "30129-01.htm", "30129-02.htm", "30129-04.htm" }, 0, 0));
		talks.put(30573, new Talk(3, new String[] { "30573-01.htm", "30573-02.htm", "30573-04.htm" }, 0, 0));
		talks.put(30528, new Talk(4, new String[] { "30528-01.htm", "30528-02.htm", "30528-04.htm" }, 0, 0));
		talks.put(30018, new Talk(0, new String[] { "30131-01.htm", "", "30019-03a.htm", "30019-04.htm" }, 1, RECOMMENDATION_02));
		talks.put(30019, new Talk(0, new String[] { "30131-01.htm", "", "30019-03a.htm", "30019-04.htm" }, 1, RECOMMENDATION_02));
		talks.put(30020, new Talk(0, new String[] { "30131-01.htm", "", "30019-03a.htm", "30019-04.htm" }, 1, RECOMMENDATION_02));
		talks.put(30021, new Talk(0, new String[] { "30131-01.htm", "", "30019-03a.htm", "30019-04.htm" }, 1, RECOMMENDATION_02));
		talks.put(30009, new Talk(0, new String[] { "30530-01.htm", "30009-03.htm", "", "30009-04.htm" }, 1, RECOMMENDATION_01));
		talks.put(30011, new Talk(0, new String[] { "30530-01.htm", "30009-03.htm", "", "30009-04.htm" }, 1, RECOMMENDATION_01));
		talks.put(30012, new Talk(0, new String[] { "30530-01.htm", "30009-03.htm", "", "30009-04.htm" }, 1, RECOMMENDATION_01));
		talks.put(30056, new Talk(0, new String[] { "30530-01.htm", "30009-03.htm", "", "30009-04.htm" }, 1, RECOMMENDATION_01));
		talks.put(30400, new Talk(1, new String[] { "30131-01.htm", "30400-03.htm", "30400-03a.htm", "30400-04.htm" }, 1, LEAF_OF_MOTHERTREE));
		talks.put(30401, new Talk(1, new String[] { "30131-01.htm", "30400-03.htm", "30400-03a.htm", "30400-04.htm" }, 1, LEAF_OF_MOTHERTREE));
		talks.put(30402, new Talk(1, new String[] { "30131-01.htm", "30400-03.htm", "30400-03a.htm", "30400-04.htm" }, 1, LEAF_OF_MOTHERTREE));
		talks.put(30403, new Talk(1, new String[] { "30131-01.htm", "30400-03.htm", "30400-03a.htm", "30400-04.htm" }, 1, LEAF_OF_MOTHERTREE));
		talks.put(30131, new Talk(2, new String[] { "30131-01.htm", "30131-03.htm", "30131-03a.htm", "30131-04.htm" }, 1, BLOOD_OF_JUNDIN));
		talks.put(30404, new Talk(2, new String[] { "30131-01.htm", "30131-03.htm", "30131-03a.htm", "30131-04.htm" }, 1, BLOOD_OF_JUNDIN));
		talks.put(30574, new Talk(3, new String[] { "30575-01.htm", "30575-03.htm", "30575-03a.htm", "30575-04.htm" }, 1, VOUCHER_OF_FLAME));
		talks.put(30575, new Talk(3, new String[] { "30575-01.htm", "30575-03.htm", "30575-03a.htm", "30575-04.htm" }, 1, VOUCHER_OF_FLAME));
		talks.put(30530, new Talk(4, new String[] { "30530-01.htm", "30530-03.htm", "", "30530-04.htm" }, 1, LICENSE_OF_MINER));
	}

	@Override
	public void onLoad()
	{
		//
	}

	@Override
	public void onReload()
	{
		//
	}

	@Override
	public void onShutdown()
	{
		//
	}

	public _999_Tutorial()
	{
		super("Tutorial", false);
		addFirstTalkId(30008, 30009, 30017, 30019, 30129, 30131, 30573, 30575, 30370, 30528, 30530, 30400, 30401, 30402, 30403, 30404);
		addFirstTalkId(30018, 30020, 30021, 30011, 30012, 30056, 30574);
		addFirstTalkId(30600, 30601, 30602, 30598, 30599);
		addKillId(18342);
		addKillId(20001);
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final QuestState qs = st.getPlayer().getQuestState(qnTutorial);
		if(qs == null)
			return null;
		final Player player = st.getPlayer();
		final int Ex = qs.getInt("Ex");
		final int classId = player.getClassId().getId();
		if(event.equalsIgnoreCase("TimerEx_NewbieHelper"))
		{
			if(Ex == 0)
			{
				if(player.isMageClass())
					st.playTutorialVoice("tutorial_voice_009b");
				else
					st.playTutorialVoice("tutorial_voice_009a");
				qs.set("Ex", "1");
			}
			else if(Ex == 3)
			{
				st.playTutorialVoice("tutorial_voice_010a");
				qs.set("Ex", "4");
			}
			return null;
		}
		if(event.equalsIgnoreCase("TimerEx_GrandMaster"))
		{
			if(Ex >= 4)
			{
				st.showQuestionMark(7);
				st.playSound(Quest.SOUND_TUTORIAL);
				st.playTutorialVoice("tutorial_voice_025");
			}
			return null;
		}
		String htmltext;
		if(event.equalsIgnoreCase("isle"))
		{
			st.addRadar(-119692, 44504, 380);
			player.teleToLocation(-120050, 44500, 360);
			htmltext = "<html><body><br>Go to the <font color=\"LEVEL\">Isle of Souls</font> and meet the <font color=\"LEVEL\">Newbie Guide</font> there to learn a number of important tips. He will also give you an item to assist your development. <br>Follow the direction arrow above your head and it will lead you to the Newbie Guide. Good luck!</body></html>";
		}
		else
		{
			final Event e = events.get(event);
			if(e.radarX != 0)
				st.addRadar(e.radarX, e.radarY, e.radarZ);
			htmltext = e.htm;
			if(st.getQuestItemsCount(e.item) > 0L)
			{
				st.set("step", "3");
				st.addExpAndSp(0L, 50L);
				st.startQuestTimer("TimerEx_GrandMaster", 60000L);
				st.takeItems(e.item, 1L);
				if(Ex <= 3)
					qs.set("Ex", "4");
				if(classId == e.classId1)
				{
					st.giveItems(e.gift1, e.count1);
					if(e.gift1 == SPIRITSHOT_NOVICE)
						st.playTutorialVoice("tutorial_voice_027");
					else
						st.playTutorialVoice("tutorial_voice_026");
				}
				else if(classId == e.classId2 && e.gift2 != 0)
				{
					st.giveItems(e.gift2, e.count2);
					st.playTutorialVoice("tutorial_voice_026");
				}
			}
		}
		return htmltext;
	}

	@Override
	public String onFirstTalk(final NpcInstance npc, final Player player)
	{
		final QuestState qs = player.getQuestState(qnTutorial);
		if(qs == null)
			return "";

		final int npcId = npc.getNpcId();
		String htmltext = "";
		if(npcId == 30600 || npcId == 30601 || npcId == 30602 || npcId == 30598 || npcId == 30599)
		{
			if(!qs.isCompleted())
			{
				if(player.isMageClass())
				{
					qs.playTutorialVoice("tutorial_voice_027");
					qs.giveItems(SPIRITSHOT_NOVICE, 100L);
				}
				else
				{
					qs.playTutorialVoice("tutorial_voice_026");
					qs.giveItems(SOULSHOT_NOVICE, 200L);
				}
				qs.giveItems(TOKEN, 12L);
				qs.giveItems(SCROLL, 2L);
				qs.exitCurrentQuest(false);
			}
		}
		else
		{
			final Talk t = talks.get(npcId);
			if(t == null)
				return "";

			QuestState st = player.getQuestState(getId());
			if(st == null)
				st = newQuestState(player, 1);

			final int Ex = qs.getInt("Ex");
			final int step = st.getInt("step");
			final int level = player.getLevel();
			final boolean isMage = player.isMageClass();
			if(level >= 10 && t.npcTyp == 1)
				htmltext = "30575-05.htm";
			else
			{
				if(player.getRace().ordinal() == t.raceId)
				{
					if(t.npcTyp == 1)
					{
						if(step == 0)
						{
							htmltext = t.htmlfiles[0];
							if(Ex < 0)
							{
								qs.set("Ex", "0");
								st.startQuestTimer("TimerEx_NewbieHelper", 30000L);
								if(isMage)
								{
									st.set("step", "1");
									st.setState(2);
								}
								else
								{
									htmltext = "30530-01.htm";
									st.set("step", "1");
									st.setState(2);
								}
							}
						}
						else if(step == 1)
						{
							htmltext = t.htmlfiles[0];
							if(st.getQuestItemsCount(t.item) == 0L && Ex <= 2)
							{
								if(st.getQuestItemsCount(BLUE_GEM) > 0L)
								{
									st.takeItems(BLUE_GEM, st.getQuestItemsCount(BLUE_GEM));
									st.giveItems(t.item, 1L);
									st.set("step", "2");
									qs.set("Ex", "3");
									st.startQuestTimer("TimerEx_NewbieHelper", 30000L);
									qs.set("ucMemo", "3");
									if(isMage)
									{
										st.playTutorialVoice("tutorial_voice_027");
										st.giveItems(SPIRITSHOT_NOVICE, 100L);
										htmltext = t.htmlfiles[2];
										if(htmltext.isEmpty())
											htmltext = "<html><body>I am sorry. I only help warriors. Please go to another Newbie Helper who may assist you.</body></html>";
									}
									else
									{
										st.playTutorialVoice("tutorial_voice_026");
										st.giveItems(SOULSHOT_NOVICE, 200L);
										htmltext = t.htmlfiles[1];
										if(htmltext.isEmpty())
											htmltext = "<html><body>I am sorry. I only help mystics. Please go to another Newbie Helper who may assist you.</body></html>";
									}
								}
								else if(isMage)
								{
									htmltext = "30131-02.htm";
									if(player.getRace().ordinal() == 3)
										htmltext = "30575-02.htm";
								}
								else
									htmltext = "30530-02.htm";
							}
						}
						else if(step == 2)
							htmltext = t.htmlfiles[3];
					}
					else if(t.npcTyp == 0)
					{
						if(step == 0)
							htmltext = t.htmlfiles[0];
						else if(step == 1)
							htmltext = t.htmlfiles[0];
						else if(step == 2)
							htmltext = t.htmlfiles[1];
					}
				}
			}
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final QuestState qs = st.getPlayer().getQuestState(qnTutorial);
		if(qs == null)
			return null;

		final int Ex = qs.getInt("Ex");
		if(Ex <= 1)
		{
			st.playTutorialVoice("tutorial_voice_011");
			st.showQuestionMark(3);
			qs.set("Ex", "2");
		}
		if(Ex <= 2 && st.getQuestItemsCount(BLUE_GEM) < 1L && Rnd.chance(25))
		{
			ThreadPoolManager.getInstance().schedule(() -> {
				npc.dropItem(st.getPlayer(), BLUE_GEM, 1);
				st.playSound(Quest.SOUND_ITEMGET);
				st.playSound(Quest.SOUND_TUTORIAL);
			}, 3000L);
		}
		return null;
	}

	@Override
	public boolean isVisible(Player player)
	{
		return false;
	}
}
