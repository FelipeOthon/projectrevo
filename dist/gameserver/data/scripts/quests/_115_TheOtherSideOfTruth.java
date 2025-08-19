package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;

public class _115_TheOtherSideOfTruth extends Quest implements ScriptFile
{
	private static int Rafforty;
	private static int Misa;
	private static int Kierre;
	private static int Ice_Sculpture1;
	private static int Ice_Sculpture2;
	private static int Ice_Sculpture3;
	private static int Ice_Sculpture4;
	private static int Suspicious_Man;
	private static int Misas_Letter;
	private static int Raffortys_Letter;
	private static int Piece_of_Tablet;
	private static int Report_Piece;

	public _115_TheOtherSideOfTruth()
	{
		super(false);
		this.addStartNpc(_115_TheOtherSideOfTruth.Rafforty);
		this.addTalkId(new int[] { _115_TheOtherSideOfTruth.Misa });
		this.addTalkId(new int[] { _115_TheOtherSideOfTruth.Kierre });
		this.addTalkId(new int[] { _115_TheOtherSideOfTruth.Ice_Sculpture1 });
		this.addTalkId(new int[] { _115_TheOtherSideOfTruth.Ice_Sculpture2 });
		this.addTalkId(new int[] { _115_TheOtherSideOfTruth.Ice_Sculpture3 });
		this.addTalkId(new int[] { _115_TheOtherSideOfTruth.Ice_Sculpture4 });
		addQuestItem(new int[] { _115_TheOtherSideOfTruth.Misas_Letter });
		addQuestItem(new int[] { _115_TheOtherSideOfTruth.Raffortys_Letter });
		addQuestItem(new int[] { _115_TheOtherSideOfTruth.Piece_of_Tablet });
		addQuestItem(new int[] { _115_TheOtherSideOfTruth.Report_Piece });
	}

	@Override
	public String onEvent(String event, final QuestState st, final NpcInstance npc)
	{
		final int _state = st.getState();
		if(event.equalsIgnoreCase("32020-02.htm") && _state == 1)
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		if(_state != 2)
			return event;
		if(event.equalsIgnoreCase("32020-06.htm") || event.equalsIgnoreCase("32020-08a.htm"))
		{
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		else if(event.equalsIgnoreCase("32020-05.htm"))
		{
			st.set("cond", "3");
			st.takeItems(_115_TheOtherSideOfTruth.Misas_Letter, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("32020-08.htm") || event.equalsIgnoreCase("32020-07a.htm"))
		{
			st.set("cond", "4");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("32020-12.htm"))
		{
			st.set("cond", "5");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("32018-04.htm"))
		{
			st.set("cond", "7");
			st.takeItems(_115_TheOtherSideOfTruth.Raffortys_Letter, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else
		{
			if(event.equalsIgnoreCase("Sculpture-04a.htm"))
			{
				st.set("cond", "8");
				st.playSound(Quest.SOUND_MIDDLE);
				if(st.getInt("32021") == 0 && st.getInt("32077") == 0)
					st.giveItems(_115_TheOtherSideOfTruth.Piece_of_Tablet, 1L);
				Functions.npcSay(st.addSpawn(_115_TheOtherSideOfTruth.Suspicious_Man, 117890, -126478, -2584, 0, 0, 300000), "This looks like the right place...");
				return "Sculpture-04.htm";
			}
			if(event.equalsIgnoreCase("32022-02.htm"))
			{
				st.set("cond", "9");
				st.giveItems(_115_TheOtherSideOfTruth.Report_Piece, 1L);
				st.playSound(Quest.SOUND_MIDDLE);
				Functions.npcSay(st.addSpawn(_115_TheOtherSideOfTruth.Suspicious_Man, 104562, -107598, -3688, 0, 0, 300000), "We meet again.");
			}
			else if(event.equalsIgnoreCase("32020-16.htm"))
			{
				st.set("cond", "10");
				st.takeItems(_115_TheOtherSideOfTruth.Report_Piece, 1L);
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else if(event.equalsIgnoreCase("32020-18.htm"))
			{
				if(st.getQuestItemsCount(_115_TheOtherSideOfTruth.Piece_of_Tablet) <= 0L)
				{
					st.set("cond", "11");
					st.playSound(Quest.SOUND_MIDDLE);
					return "32020-19.htm";
				}
				st.giveItems(57, 60044L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
			else if(event.equalsIgnoreCase("32020-19.htm"))
			{
				st.set("cond", "11");
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else if(event.startsWith("32021") || event.startsWith("32077"))
			{
				if(event.contains("-pick"))
				{
					st.set("talk", "1");
					event = event.replace("-pick", "");
				}
				st.set(event, "1");
				return "Sculpture-05.htm";
			}
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int _state = st.getState();
		if(_state == 3)
			return "completed";
		final int npcId = npc.getNpcId();
		if(_state != 1)
		{
			final int cond = st.getInt("cond");
			if(npcId == _115_TheOtherSideOfTruth.Rafforty && _state == 2)
			{
				if(cond == 1)
					return "32020-03.htm";
				if(cond == 2)
					return "32020-04.htm";
				if(cond == 3)
					return "32020-05.htm";
				if(cond == 4)
					return "32020-11.htm";
				if(cond == 5)
				{
					st.set("cond", "6");
					st.giveItems(_115_TheOtherSideOfTruth.Raffortys_Letter, 1L);
					st.playSound(Quest.SOUND_MIDDLE);
					return "32020-13.htm";
				}
				if(cond == 6)
					return "32020-14.htm";
				if(cond == 9)
					return "32020-15.htm";
				if(cond == 10)
					return "32020-17.htm";
				if(cond == 11)
					return "32020-20.htm";
				if(cond == 12)
				{
					st.giveItems(57, 115673L);
					st.addExpAndSp(493595L, 40442L);
					st.playSound(Quest.SOUND_FINISH);
					st.exitCurrentQuest(false);
					return "32020-18.htm";
				}
			}
			else if(npcId == _115_TheOtherSideOfTruth.Misa && _state == 2)
			{
				if(cond == 1)
				{
					st.set("cond", "2");
					st.giveItems(_115_TheOtherSideOfTruth.Misas_Letter, 1L);
					st.playSound(Quest.SOUND_MIDDLE);
					return "32018-01.htm";
				}
				if(cond == 2)
					return "32018-02.htm";
				if(cond == 6)
					return "32018-03.htm";
				if(cond == 7)
					return "32018-05.htm";
			}
			else if(npcId == _115_TheOtherSideOfTruth.Kierre && _state == 2)
			{
				if(cond == 8)
					return "32022-01.htm";
				if(cond == 9)
					return "32022-03.htm";
			}
			else if((npcId == _115_TheOtherSideOfTruth.Ice_Sculpture1 || npcId == _115_TheOtherSideOfTruth.Ice_Sculpture2 || npcId == _115_TheOtherSideOfTruth.Ice_Sculpture3 || npcId == _115_TheOtherSideOfTruth.Ice_Sculpture4) && _state == 2)
				if(cond == 7)
				{
					final String _npcId = String.valueOf(npcId);
					final int npcId_flag = st.getInt(_npcId);
					if(npcId == _115_TheOtherSideOfTruth.Ice_Sculpture1 || npcId == _115_TheOtherSideOfTruth.Ice_Sculpture2)
					{
						final int talk_flag = st.getInt("talk");
						return npcId_flag == 1 ? "Sculpture-02.htm" : talk_flag == 1 ? "Sculpture-06.htm" : "Sculpture-03-" + _npcId + ".htm";
					}
					if(npcId_flag == 1)
						return "Sculpture-02.htm";
					st.set(_npcId, "1");
					return "Sculpture-01.htm";
				}
				else
				{
					if(cond == 8)
						return "Sculpture-04.htm";
					if(cond == 11)
					{
						st.set("cond", "12");
						st.giveItems(_115_TheOtherSideOfTruth.Piece_of_Tablet, 1L);
						st.playSound(Quest.SOUND_MIDDLE);
						return "Sculpture-07.htm";
					}
					if(cond == 12)
						return "Sculpture-08.htm";
				}
			return "noquest";
		}
		if(npcId != _115_TheOtherSideOfTruth.Rafforty)
			return "noquest";
		if(st.getPlayer().getLevel() >= 53)
		{
			st.set("cond", "0");
			return "32020-01.htm";
		}
		st.exitCurrentQuest(true);
		return "32020-00.htm";
	}

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	static
	{
		_115_TheOtherSideOfTruth.Rafforty = 32020;
		_115_TheOtherSideOfTruth.Misa = 32018;
		_115_TheOtherSideOfTruth.Kierre = 32022;
		_115_TheOtherSideOfTruth.Ice_Sculpture1 = 32021;
		_115_TheOtherSideOfTruth.Ice_Sculpture2 = 32077;
		_115_TheOtherSideOfTruth.Ice_Sculpture3 = 32078;
		_115_TheOtherSideOfTruth.Ice_Sculpture4 = 32079;
		_115_TheOtherSideOfTruth.Suspicious_Man = 32019;
		_115_TheOtherSideOfTruth.Misas_Letter = 8079;
		_115_TheOtherSideOfTruth.Raffortys_Letter = 8080;
		_115_TheOtherSideOfTruth.Piece_of_Tablet = 8081;
		_115_TheOtherSideOfTruth.Report_Piece = 8082;
	}
}
