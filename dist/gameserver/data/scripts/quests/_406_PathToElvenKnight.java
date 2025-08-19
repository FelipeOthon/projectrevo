package quests;

import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _406_PathToElvenKnight extends Quest implements ScriptFile
{
	private static final int Sorius = 30327;
	private static final int Kluto = 30317;
	private static final int SoriussLetter = 1202;
	private static final int KlutoBox = 1203;
	private static final int TopazPiece = 1205;
	private static final int EmeraldPiece = 1206;
	private static final int KlutosMemo = 1276;
	private static final int ElvenKnightBrooch = 1204;
	private static final int TrackerSkeleton = 20035;
	private static final int TrackerSkeletonLeader = 20042;
	private static final int SkeletonScout = 20045;
	private static final int SkeletonBowman = 20051;
	private static final int RagingSpartoi = 20060;
	private static final int OlMahumNovice = 20782;
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

	public _406_PathToElvenKnight()
	{
		super(false);
		this.addStartNpc(30327);
		this.addTalkId(new int[] { 30317 });
		for(int i = 0; i < _406_PathToElvenKnight.DROPLIST_COND.length; ++i)
			this.addKillId(new int[] { _406_PathToElvenKnight.DROPLIST_COND[i][2] });
		addQuestItem(new int[] { 1205, 1206, 1202, 1276, 1203 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("master_sorius_q0406_05.htm"))
		{
			if(st.getPlayer().getClassId().getId() == 18)
			{
				if(st.getQuestItemsCount(1204) > 0L)
				{
					htmltext = "master_sorius_q0406_04.htm";
					st.exitCurrentQuest(true);
				}
				else if(st.getPlayer().getLevel() < 18)
				{
					htmltext = "master_sorius_q0406_03.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(st.getPlayer().getClassId().getId() == 19)
			{
				htmltext = "master_sorius_q0406_02a.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "master_sorius_q0406_02.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(event.equalsIgnoreCase("master_sorius_q0406_06.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("blacksmith_kluto_q0406_02.htm"))
		{
			st.takeItems(1202, -1L);
			st.giveItems(1276, 1L);
			st.set("cond", "4");
			st.setState(2);
		}
		else
			htmltext = "noquest";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(npcId == 30327)
		{
			if(cond == 0)
				htmltext = "master_sorius_q0406_01.htm";
			else if(cond == 1)
			{
				if(st.getQuestItemsCount(1205) == 0L)
					htmltext = "master_sorius_q0406_07.htm";
				else
					htmltext = "master_sorius_q0406_08.htm";
			}
			else if(cond == 2)
			{
				st.takeItems(1205, -1L);
				st.giveItems(1202, 1L);
				htmltext = "master_sorius_q0406_09.htm";
				st.set("cond", "3");
				st.setState(2);
			}
			else if(cond == 3 || cond == 4 || cond == 5)
				htmltext = "master_sorius_q0406_11.htm";
			else if(cond == 6)
			{
				st.takeItems(1203, -1L);
				if(st.getPlayer().getClassId().getLevel() == 1)
				{
					st.giveItems(1204, 1L);
					if(!st.getPlayer().getVarBoolean("prof1"))
					{
						st.getPlayer().setVar("prof1", "1");
						st.addExpAndSp(228064L, 14925L, true);
						st.giveItems(57, 81900L, Config.RATE_QUESTS_OCCUPATION_CHANGE);
					}
				}
				st.exitCurrentQuest(true);
				st.playSound(Quest.SOUND_FINISH);
				htmltext = "master_sorius_q0406_10.htm";
			}
		}
		else if(npcId == 30317)
			if(cond == 3)
				htmltext = "blacksmith_kluto_q0406_01.htm";
			else if(cond == 4)
			{
				if(st.getQuestItemsCount(1206) == 0L)
					htmltext = "blacksmith_kluto_q0406_03.htm";
				else
					htmltext = "blacksmith_kluto_q0406_04.htm";
			}
			else if(cond == 5)
			{
				st.takeItems(1206, -1L);
				st.takeItems(1276, -1L);
				st.giveItems(1203, 1L);
				htmltext = "blacksmith_kluto_q0406_05.htm";
				st.set("cond", "6");
				st.setState(2);
			}
			else if(cond == 6)
				htmltext = "blacksmith_kluto_q0406_06.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		for(int i = 0; i < _406_PathToElvenKnight.DROPLIST_COND.length; ++i)
			if(cond == _406_PathToElvenKnight.DROPLIST_COND[i][0] && npcId == _406_PathToElvenKnight.DROPLIST_COND[i][2] && (_406_PathToElvenKnight.DROPLIST_COND[i][3] == 0 || st.getQuestItemsCount(_406_PathToElvenKnight.DROPLIST_COND[i][3]) > 0L))
				if(_406_PathToElvenKnight.DROPLIST_COND[i][5] == 0)
					st.rollAndGive(_406_PathToElvenKnight.DROPLIST_COND[i][4], _406_PathToElvenKnight.DROPLIST_COND[i][7], _406_PathToElvenKnight.DROPLIST_COND[i][6], _406_PathToElvenKnight.QuestProf);
				else if(st.rollAndGive(_406_PathToElvenKnight.DROPLIST_COND[i][4], _406_PathToElvenKnight.DROPLIST_COND[i][7], _406_PathToElvenKnight.DROPLIST_COND[i][7], _406_PathToElvenKnight.DROPLIST_COND[i][5], _406_PathToElvenKnight.DROPLIST_COND[i][6], _406_PathToElvenKnight.QuestProf) && _406_PathToElvenKnight.DROPLIST_COND[i][1] != cond && _406_PathToElvenKnight.DROPLIST_COND[i][1] != 0)
				{
					st.set("cond", String.valueOf(_406_PathToElvenKnight.DROPLIST_COND[i][1]));
					st.setState(2);
				}
		return null;
	}

	static
	{
		DROPLIST_COND = new int[][] {
				{ 1, 2, 20035, 0, 1205, 20, 70, 1 },
				{ 1, 2, 20042, 0, 1205, 20, 70, 1 },
				{ 1, 2, 20045, 0, 1205, 20, 70, 1 },
				{ 1, 2, 20051, 0, 1205, 20, 70, 1 },
				{ 1, 2, 20060, 0, 1205, 20, 70, 1 },
				{ 4, 5, 20782, 0, 1206, 20, 50, 1 } };
		_406_PathToElvenKnight.QuestProf = true;
	}
}
