package quests;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _233_TestOfWarspirit extends Quest implements ScriptFile
{
	private static int Somak;
	private static int Vivyan;
	private static int Sarien;
	private static int Racoy;
	private static int Manakia;
	private static int Orim;
	private static int Ancestor_Martankus;
	private static int Pekiron;
	private static int Porta;
	private static int Excuro;
	private static int Mordeo;
	private static int Noble_Ant;
	private static int Noble_Ant_Leader;
	private static int Leto_Lizardman_Shaman;
	private static int Leto_Lizardman_Overlord;
	private static int Medusa;
	private static int Stenoa_Gorgon_Queen;
	private static int Tamlin_Orc;
	private static int Tamlin_Orc_Archer;
	private static short Dimensional_Diamond;
	private static short MARK_OF_WARSPIRIT;
	private static short VENDETTA_TOTEM;
	private static short TAMLIN_ORC_HEAD;
	private static short WARSPIRIT_TOTEM;
	private static short ORIMS_CONTRACT;
	private static short PORTAS_EYE;
	private static short EXCUROS_SCALE;
	private static short MORDEOS_TALON;
	private static short BRAKIS_REMAINS1;
	private static short PEKIRONS_TOTEM;
	private static short TONARS_SKULL;
	private static short TONARS_RIB_BONE;
	private static short TONARS_SPINE;
	private static short TONARS_ARM_BONE;
	private static short TONARS_THIGH_BONE;
	private static short TONARS_REMAINS1;
	private static short MANAKIAS_TOTEM;
	private static short HERMODTS_SKULL;
	private static short HERMODTS_RIB_BONE;
	private static short HERMODTS_SPINE;
	private static short HERMODTS_ARM_BONE;
	private static short HERMODTS_THIGH_BONE;
	private static short HERMODTS_REMAINS1;
	private static short RACOYS_TOTEM;
	private static short VIVIANTES_LETTER;
	private static short INSECT_DIAGRAM_BOOK;
	private static short KIRUNAS_SKULL;
	private static short KIRUNAS_RIB_BONE;
	private static short KIRUNAS_SPINE;
	private static short KIRUNAS_ARM_BONE;
	private static short KIRUNAS_THIGH_BONE;
	private static short KIRUNAS_REMAINS1;
	private static short BRAKIS_REMAINS2;
	private static short TONARS_REMAINS2;
	private static short HERMODTS_REMAINS2;
	private static short KIRUNAS_REMAINS2;
	private static short[] Noble_Ant_Drops;
	private static short[] Leto_Lizardman_Drops;
	private static short[] Medusa_Drops;

	public _233_TestOfWarspirit()
	{
		super(false);
		this.addStartNpc(_233_TestOfWarspirit.Somak);
		this.addTalkId(new int[] { _233_TestOfWarspirit.Vivyan });
		this.addTalkId(new int[] { _233_TestOfWarspirit.Sarien });
		this.addTalkId(new int[] { _233_TestOfWarspirit.Racoy });
		this.addTalkId(new int[] { _233_TestOfWarspirit.Manakia });
		this.addTalkId(new int[] { _233_TestOfWarspirit.Orim });
		this.addTalkId(new int[] { _233_TestOfWarspirit.Ancestor_Martankus });
		this.addTalkId(new int[] { _233_TestOfWarspirit.Pekiron });
		this.addKillId(new int[] { _233_TestOfWarspirit.Porta });
		this.addKillId(new int[] { _233_TestOfWarspirit.Excuro });
		this.addKillId(new int[] { _233_TestOfWarspirit.Mordeo });
		this.addKillId(new int[] { _233_TestOfWarspirit.Noble_Ant });
		this.addKillId(new int[] { _233_TestOfWarspirit.Noble_Ant_Leader });
		this.addKillId(new int[] { _233_TestOfWarspirit.Leto_Lizardman_Shaman });
		this.addKillId(new int[] { _233_TestOfWarspirit.Leto_Lizardman_Overlord });
		this.addKillId(new int[] { _233_TestOfWarspirit.Medusa });
		this.addKillId(new int[] { _233_TestOfWarspirit.Stenoa_Gorgon_Queen });
		this.addKillId(new int[] { _233_TestOfWarspirit.Tamlin_Orc });
		this.addKillId(new int[] { _233_TestOfWarspirit.Tamlin_Orc_Archer });
		addQuestItem(new int[] { _233_TestOfWarspirit.VENDETTA_TOTEM });
		addQuestItem(new int[] { _233_TestOfWarspirit.TAMLIN_ORC_HEAD });
		addQuestItem(new int[] { _233_TestOfWarspirit.WARSPIRIT_TOTEM });
		addQuestItem(new int[] { _233_TestOfWarspirit.ORIMS_CONTRACT });
		addQuestItem(new int[] { _233_TestOfWarspirit.PORTAS_EYE });
		addQuestItem(new int[] { _233_TestOfWarspirit.EXCUROS_SCALE });
		addQuestItem(new int[] { _233_TestOfWarspirit.MORDEOS_TALON });
		addQuestItem(new int[] { _233_TestOfWarspirit.BRAKIS_REMAINS1 });
		addQuestItem(new int[] { _233_TestOfWarspirit.PEKIRONS_TOTEM });
		addQuestItem(new int[] { _233_TestOfWarspirit.TONARS_SKULL });
		addQuestItem(new int[] { _233_TestOfWarspirit.TONARS_RIB_BONE });
		addQuestItem(new int[] { _233_TestOfWarspirit.TONARS_SPINE });
		addQuestItem(new int[] { _233_TestOfWarspirit.TONARS_ARM_BONE });
		addQuestItem(new int[] { _233_TestOfWarspirit.TONARS_THIGH_BONE });
		addQuestItem(new int[] { _233_TestOfWarspirit.TONARS_REMAINS1 });
		addQuestItem(new int[] { _233_TestOfWarspirit.MANAKIAS_TOTEM });
		addQuestItem(new int[] { _233_TestOfWarspirit.HERMODTS_SKULL });
		addQuestItem(new int[] { _233_TestOfWarspirit.HERMODTS_RIB_BONE });
		addQuestItem(new int[] { _233_TestOfWarspirit.HERMODTS_SPINE });
		addQuestItem(new int[] { _233_TestOfWarspirit.HERMODTS_ARM_BONE });
		addQuestItem(new int[] { _233_TestOfWarspirit.HERMODTS_THIGH_BONE });
		addQuestItem(new int[] { _233_TestOfWarspirit.HERMODTS_REMAINS1 });
		addQuestItem(new int[] { _233_TestOfWarspirit.RACOYS_TOTEM });
		addQuestItem(new int[] { _233_TestOfWarspirit.VIVIANTES_LETTER });
		addQuestItem(new int[] { _233_TestOfWarspirit.INSECT_DIAGRAM_BOOK });
		addQuestItem(new int[] { _233_TestOfWarspirit.KIRUNAS_SKULL });
		addQuestItem(new int[] { _233_TestOfWarspirit.KIRUNAS_RIB_BONE });
		addQuestItem(new int[] { _233_TestOfWarspirit.KIRUNAS_SPINE });
		addQuestItem(new int[] { _233_TestOfWarspirit.KIRUNAS_ARM_BONE });
		addQuestItem(new int[] { _233_TestOfWarspirit.KIRUNAS_THIGH_BONE });
		addQuestItem(new int[] { _233_TestOfWarspirit.KIRUNAS_REMAINS1 });
		addQuestItem(new int[] { _233_TestOfWarspirit.BRAKIS_REMAINS2 });
		addQuestItem(new int[] { _233_TestOfWarspirit.TONARS_REMAINS2 });
		addQuestItem(new int[] { _233_TestOfWarspirit.HERMODTS_REMAINS2 });
		addQuestItem(new int[] { _233_TestOfWarspirit.KIRUNAS_REMAINS2 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int _state = st.getState();
		if(event.equalsIgnoreCase("30510-05.htm") && _state == 1)
		{
			if(!st.getPlayer().getVarBoolean("dd3"))
			{
				st.giveItems(_233_TestOfWarspirit.Dimensional_Diamond, 92L);
				st.getPlayer().setVar("dd3", "1");
			}
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30630-04.htm") && _state == 2)
			st.giveItems(_233_TestOfWarspirit.ORIMS_CONTRACT, 1L);
		else if(event.equalsIgnoreCase("30682-02.htm") && _state == 2)
			st.giveItems(_233_TestOfWarspirit.PEKIRONS_TOTEM, 1L);
		else if(event.equalsIgnoreCase("30515-02.htm") && _state == 2)
			st.giveItems(_233_TestOfWarspirit.MANAKIAS_TOTEM, 1L);
		else if(event.equalsIgnoreCase("30507-02.htm") && _state == 2)
			st.giveItems(_233_TestOfWarspirit.RACOYS_TOTEM, 1L);
		else if(event.equalsIgnoreCase("30030-04.htm") && _state == 2)
			st.giveItems(_233_TestOfWarspirit.VIVIANTES_LETTER, 1L);
		else if(event.equalsIgnoreCase("30649-03.htm") && _state == 2 && st.getQuestItemsCount(_233_TestOfWarspirit.WARSPIRIT_TOTEM) > 0L)
		{
			st.takeItems(_233_TestOfWarspirit.WARSPIRIT_TOTEM, -1L);
			st.takeItems(_233_TestOfWarspirit.BRAKIS_REMAINS2, -1L);
			st.takeItems(_233_TestOfWarspirit.HERMODTS_REMAINS2, -1L);
			st.takeItems(_233_TestOfWarspirit.KIRUNAS_REMAINS2, -1L);
			st.takeItems(_233_TestOfWarspirit.TAMLIN_ORC_HEAD, -1L);
			st.takeItems(_233_TestOfWarspirit.TONARS_REMAINS2, -1L);
			st.giveItems(_233_TestOfWarspirit.MARK_OF_WARSPIRIT, 1L);
			if(!st.getPlayer().getVarBoolean("prof2.3"))
			{
				st.addExpAndSp(447444L, 30704L, true);
				st.giveItems(57, 100000L, Config.RATE_QUESTS_OCCUPATION_CHANGE);
				st.getPlayer().setVar("prof2.3", "1");
			}
			st.playSound(Quest.SOUND_FINISH);
			st.unset("cond");
			st.exitCurrentQuest(true);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		if(st.getQuestItemsCount(_233_TestOfWarspirit.MARK_OF_WARSPIRIT) > 0L)
		{
			st.exitCurrentQuest(true);
			return "completed";
		}
		final int _state = st.getState();
		final int npcId = npc.getNpcId();
		if(_state == 1)
		{
			if(npcId != _233_TestOfWarspirit.Somak)
				return "noquest";
			if(st.getPlayer().getRace() != Race.orc)
			{
				st.exitCurrentQuest(true);
				return "30510-01.htm";
			}
			if(st.getPlayer().getClassId().getId() != 50)
			{
				st.exitCurrentQuest(true);
				return "30510-02.htm";
			}
			if(st.getPlayer().getLevel() < 39)
			{
				st.exitCurrentQuest(true);
				return "30510-03.htm";
			}
			st.set("cond", "0");
			return "30510-04.htm";
		}
		else
		{
			if(_state != 2 || st.getInt("cond") != 1)
				return "noquest";
			if(npcId == _233_TestOfWarspirit.Somak)
			{
				if(st.getQuestItemsCount(_233_TestOfWarspirit.VENDETTA_TOTEM) > 0L)
				{
					if(st.getQuestItemsCount(_233_TestOfWarspirit.TAMLIN_ORC_HEAD) < 13L)
						return "30510-08.htm";
					st.takeItems(_233_TestOfWarspirit.VENDETTA_TOTEM, -1L);
					st.giveItems(_233_TestOfWarspirit.WARSPIRIT_TOTEM, 1L);
					st.giveItems(_233_TestOfWarspirit.BRAKIS_REMAINS2, 1L);
					st.giveItems(_233_TestOfWarspirit.HERMODTS_REMAINS2, 1L);
					st.giveItems(_233_TestOfWarspirit.KIRUNAS_REMAINS2, 1L);
					st.giveItems(_233_TestOfWarspirit.TONARS_REMAINS2, 1L);
					st.playSound(Quest.SOUND_MIDDLE);
					return "30510-09.htm";
				}
				else
				{
					if(st.getQuestItemsCount(_233_TestOfWarspirit.WARSPIRIT_TOTEM) > 0L)
						return "30510-10.htm";
					if(st.getQuestItemsCount(_233_TestOfWarspirit.BRAKIS_REMAINS1) == 0L || st.getQuestItemsCount(_233_TestOfWarspirit.HERMODTS_REMAINS1) == 0L || st.getQuestItemsCount(_233_TestOfWarspirit.KIRUNAS_REMAINS1) == 0L || st.getQuestItemsCount(_233_TestOfWarspirit.TONARS_REMAINS1) == 0L)
						return "30510-06.htm";
					st.takeItems(_233_TestOfWarspirit.BRAKIS_REMAINS1, -1L);
					st.takeItems(_233_TestOfWarspirit.HERMODTS_REMAINS1, -1L);
					st.takeItems(_233_TestOfWarspirit.KIRUNAS_REMAINS1, -1L);
					st.takeItems(_233_TestOfWarspirit.TONARS_REMAINS1, -1L);
					st.giveItems(_233_TestOfWarspirit.VENDETTA_TOTEM, 1L);
					st.playSound(Quest.SOUND_MIDDLE);
					return "30510-07.htm";
				}
			}
			else if(npcId == _233_TestOfWarspirit.Orim)
			{
				if(st.getQuestItemsCount(_233_TestOfWarspirit.ORIMS_CONTRACT) > 0L)
				{
					if(st.getQuestItemsCount(_233_TestOfWarspirit.PORTAS_EYE) < 10L || st.getQuestItemsCount(_233_TestOfWarspirit.EXCUROS_SCALE) < 10L || st.getQuestItemsCount(_233_TestOfWarspirit.MORDEOS_TALON) < 10L)
						return "30630-05.htm";
					st.takeItems(_233_TestOfWarspirit.ORIMS_CONTRACT, -1L);
					st.takeItems(_233_TestOfWarspirit.PORTAS_EYE, -1L);
					st.takeItems(_233_TestOfWarspirit.EXCUROS_SCALE, -1L);
					st.takeItems(_233_TestOfWarspirit.MORDEOS_TALON, -1L);
					st.giveItems(_233_TestOfWarspirit.BRAKIS_REMAINS1, 1L);
					st.playSound(Quest.SOUND_MIDDLE);
					return "30630-06.htm";
				}
				else
				{
					if(st.getQuestItemsCount(_233_TestOfWarspirit.BRAKIS_REMAINS1) == 0L && st.getQuestItemsCount(_233_TestOfWarspirit.BRAKIS_REMAINS2) == 0L && st.getQuestItemsCount(_233_TestOfWarspirit.VENDETTA_TOTEM) == 0L)
						return "30630-01.htm";
					return "30630-07.htm";
				}
			}
			else if(npcId == _233_TestOfWarspirit.Pekiron)
			{
				if(st.getQuestItemsCount(_233_TestOfWarspirit.PEKIRONS_TOTEM) > 0L)
				{
					for(final short drop_id : _233_TestOfWarspirit.Leto_Lizardman_Drops)
						if(st.getQuestItemsCount(drop_id) == 0L)
							return "30682-03.htm";
					st.takeItems(_233_TestOfWarspirit.PEKIRONS_TOTEM, -1L);
					for(final short drop_id : _233_TestOfWarspirit.Leto_Lizardman_Drops)
						if(st.getQuestItemsCount(drop_id) == 0L)
							st.takeItems(drop_id, -1L);
					st.giveItems(_233_TestOfWarspirit.TONARS_REMAINS1, 1L);
					st.playSound(Quest.SOUND_MIDDLE);
					return "30682-04.htm";
				}
				if(st.getQuestItemsCount(_233_TestOfWarspirit.TONARS_REMAINS1) == 0L && st.getQuestItemsCount(_233_TestOfWarspirit.TONARS_REMAINS2) == 0L && st.getQuestItemsCount(_233_TestOfWarspirit.VENDETTA_TOTEM) == 0L)
					return "30682-01.htm";
				return "30682-05.htm";
			}
			else
			{
				if(npcId == _233_TestOfWarspirit.Manakia)
					if(st.getQuestItemsCount(_233_TestOfWarspirit.MANAKIAS_TOTEM) > 0L)
					{
						if(st.getQuestItemsCount(_233_TestOfWarspirit.HERMODTS_SKULL) == 0L)
							return "30515-03.htm";
						for(final short drop_id : _233_TestOfWarspirit.Medusa_Drops)
							if(st.getQuestItemsCount(drop_id) == 0L)
								return "30515-03.htm";
						st.takeItems(_233_TestOfWarspirit.MANAKIAS_TOTEM, -1L);
						st.takeItems(_233_TestOfWarspirit.HERMODTS_SKULL, -1L);
						for(final short drop_id : _233_TestOfWarspirit.Medusa_Drops)
							if(st.getQuestItemsCount(drop_id) == 0L)
								st.takeItems(drop_id, -1L);
						st.giveItems(_233_TestOfWarspirit.HERMODTS_REMAINS1, 1L);
						st.playSound(Quest.SOUND_MIDDLE);
						return "30515-04.htm";
					}
					else
					{
						if(st.getQuestItemsCount(_233_TestOfWarspirit.HERMODTS_REMAINS1) == 0L && st.getQuestItemsCount(_233_TestOfWarspirit.HERMODTS_REMAINS2) == 0L && st.getQuestItemsCount(_233_TestOfWarspirit.VENDETTA_TOTEM) == 0L)
							return "30515-01.htm";
						if(st.getQuestItemsCount(_233_TestOfWarspirit.RACOYS_TOTEM) == 0L && (st.getQuestItemsCount(_233_TestOfWarspirit.KIRUNAS_REMAINS2) > 0L || st.getQuestItemsCount(_233_TestOfWarspirit.WARSPIRIT_TOTEM) > 0L || st.getQuestItemsCount(_233_TestOfWarspirit.BRAKIS_REMAINS2) > 0L || st.getQuestItemsCount(_233_TestOfWarspirit.HERMODTS_REMAINS2) > 0L || st.getQuestItemsCount(_233_TestOfWarspirit.TAMLIN_ORC_HEAD) > 0L || st.getQuestItemsCount(_233_TestOfWarspirit.TONARS_REMAINS2) > 0L))
							return "30515-05.htm";
					}
				if(npcId == _233_TestOfWarspirit.Racoy)
					if(st.getQuestItemsCount(_233_TestOfWarspirit.RACOYS_TOTEM) > 0L)
					{
						if(st.getQuestItemsCount(_233_TestOfWarspirit.INSECT_DIAGRAM_BOOK) == 0L)
							return st.getQuestItemsCount(_233_TestOfWarspirit.VIVIANTES_LETTER) == 0L ? "30507-03.htm" : "30507-04.htm";
						if(st.getQuestItemsCount(_233_TestOfWarspirit.VIVIANTES_LETTER) == 0L)
						{
							for(final short drop_id : _233_TestOfWarspirit.Noble_Ant_Drops)
								if(st.getQuestItemsCount(drop_id) == 0L)
									return "30507-05.htm";
							st.takeItems(_233_TestOfWarspirit.RACOYS_TOTEM, -1L);
							st.takeItems(_233_TestOfWarspirit.INSECT_DIAGRAM_BOOK, -1L);
							for(final short drop_id : _233_TestOfWarspirit.Noble_Ant_Drops)
								if(st.getQuestItemsCount(drop_id) == 0L)
									st.takeItems(drop_id, -1L);
							st.giveItems(_233_TestOfWarspirit.KIRUNAS_REMAINS1, 1L);
							st.playSound(Quest.SOUND_MIDDLE);
							return "30507-06.htm";
						}
					}
					else
					{
						if(st.getQuestItemsCount(_233_TestOfWarspirit.KIRUNAS_REMAINS1) == 0L && st.getQuestItemsCount(_233_TestOfWarspirit.KIRUNAS_REMAINS2) == 0L && st.getQuestItemsCount(_233_TestOfWarspirit.VENDETTA_TOTEM) == 0L)
							return "30507-01.htm";
						return "30507-07.htm";
					}
				if(npcId == _233_TestOfWarspirit.Vivyan)
					if(st.getQuestItemsCount(_233_TestOfWarspirit.RACOYS_TOTEM) > 0L)
					{
						if(st.getQuestItemsCount(_233_TestOfWarspirit.INSECT_DIAGRAM_BOOK) == 0L)
							return st.getQuestItemsCount(_233_TestOfWarspirit.VIVIANTES_LETTER) == 0L ? "30030-01.htm" : "30030-05.htm";
						if(st.getQuestItemsCount(_233_TestOfWarspirit.VIVIANTES_LETTER) == 0L)
							return "30030-06.htm";
					}
					else if(st.getQuestItemsCount(_233_TestOfWarspirit.KIRUNAS_REMAINS1) == 0L && st.getQuestItemsCount(_233_TestOfWarspirit.KIRUNAS_REMAINS2) == 0L && st.getQuestItemsCount(_233_TestOfWarspirit.VENDETTA_TOTEM) == 0L)
						return "30030-07.htm";
				if(npcId == _233_TestOfWarspirit.Sarien)
					if(st.getQuestItemsCount(_233_TestOfWarspirit.RACOYS_TOTEM) > 0L)
					{
						if(st.getQuestItemsCount(_233_TestOfWarspirit.INSECT_DIAGRAM_BOOK) == 0L && st.getQuestItemsCount(_233_TestOfWarspirit.VIVIANTES_LETTER) > 0L)
						{
							st.takeItems(_233_TestOfWarspirit.VIVIANTES_LETTER, -1L);
							st.giveItems(_233_TestOfWarspirit.INSECT_DIAGRAM_BOOK, 1L);
							st.playSound(Quest.SOUND_MIDDLE);
							return "30436-01.htm";
						}
						if(st.getQuestItemsCount(_233_TestOfWarspirit.VIVIANTES_LETTER) == 0L && st.getQuestItemsCount(_233_TestOfWarspirit.INSECT_DIAGRAM_BOOK) > 0L)
							return "30436-02.htm";
					}
					else if(st.getQuestItemsCount(_233_TestOfWarspirit.KIRUNAS_REMAINS1) == 0L && st.getQuestItemsCount(_233_TestOfWarspirit.KIRUNAS_REMAINS2) == 0L && st.getQuestItemsCount(_233_TestOfWarspirit.VENDETTA_TOTEM) == 0L)
						return "30436-03.htm";
				if(npcId == _233_TestOfWarspirit.Ancestor_Martankus && st.getQuestItemsCount(_233_TestOfWarspirit.WARSPIRIT_TOTEM) > 0L)
					return "30649-01.htm";
				return "noquest";
			}
		}
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState qs)
	{
		if(qs.getState() != 2 || qs.getInt("cond") < 1)
			return null;
		final int npcId = npc.getNpcId();
		if(npcId == _233_TestOfWarspirit.Porta && qs.getQuestItemsCount(_233_TestOfWarspirit.ORIMS_CONTRACT) > 0L && qs.getQuestItemsCount(_233_TestOfWarspirit.PORTAS_EYE) < 10L)
		{
			qs.giveItems(_233_TestOfWarspirit.PORTAS_EYE, 1L);
			qs.playSound(qs.getQuestItemsCount(_233_TestOfWarspirit.PORTAS_EYE) == 10L ? Quest.SOUND_MIDDLE : Quest.SOUND_ITEMGET);
		}
		else if(npcId == _233_TestOfWarspirit.Excuro && qs.getQuestItemsCount(_233_TestOfWarspirit.ORIMS_CONTRACT) > 0L && qs.getQuestItemsCount(_233_TestOfWarspirit.EXCUROS_SCALE) < 10L)
		{
			qs.giveItems(_233_TestOfWarspirit.EXCUROS_SCALE, 1L);
			qs.playSound(qs.getQuestItemsCount(_233_TestOfWarspirit.EXCUROS_SCALE) == 10L ? Quest.SOUND_MIDDLE : Quest.SOUND_ITEMGET);
		}
		else if(npcId == _233_TestOfWarspirit.Mordeo && qs.getQuestItemsCount(_233_TestOfWarspirit.ORIMS_CONTRACT) > 0L && qs.getQuestItemsCount(_233_TestOfWarspirit.MORDEOS_TALON) < 10L)
		{
			qs.giveItems(_233_TestOfWarspirit.MORDEOS_TALON, 1L);
			qs.playSound(qs.getQuestItemsCount(_233_TestOfWarspirit.MORDEOS_TALON) == 10L ? Quest.SOUND_MIDDLE : Quest.SOUND_ITEMGET);
		}
		else if((npcId == _233_TestOfWarspirit.Noble_Ant || npcId == _233_TestOfWarspirit.Noble_Ant_Leader) && qs.getQuestItemsCount(_233_TestOfWarspirit.RACOYS_TOTEM) > 0L)
		{
			List<Integer> drops = new ArrayList<Integer>();
			for(final short drop_id : _233_TestOfWarspirit.Noble_Ant_Drops)
				if(qs.getQuestItemsCount(drop_id) == 0L)
					drops.add((int) drop_id);
			if(drops.size() > 0 && Rnd.chance(30))
			{
				final int drop_id2 = drops.get(Rnd.get(drops.size()));
				qs.giveItems(drop_id2, 1L);
				qs.playSound(drops.size() == 1 ? Quest.SOUND_MIDDLE : Quest.SOUND_ITEMGET);
			}
			drops.clear();
			drops = null;
		}
		else if((npcId == _233_TestOfWarspirit.Leto_Lizardman_Shaman || npcId == _233_TestOfWarspirit.Leto_Lizardman_Overlord) && qs.getQuestItemsCount(_233_TestOfWarspirit.PEKIRONS_TOTEM) > 0L)
		{
			List<Integer> drops = new ArrayList<Integer>();
			for(final short drop_id : _233_TestOfWarspirit.Leto_Lizardman_Drops)
				if(qs.getQuestItemsCount(drop_id) == 0L)
					drops.add((int) drop_id);
			if(drops.size() > 0 && Rnd.chance(25))
			{
				final int drop_id2 = drops.get(Rnd.get(drops.size()));
				qs.giveItems(drop_id2, 1L);
				qs.playSound(drops.size() == 1 ? Quest.SOUND_MIDDLE : Quest.SOUND_ITEMGET);
			}
			drops.clear();
			drops = null;
		}
		else if(npcId == _233_TestOfWarspirit.Medusa && qs.getQuestItemsCount(_233_TestOfWarspirit.MANAKIAS_TOTEM) > 0L)
		{
			List<Integer> drops = new ArrayList<Integer>();
			for(final short drop_id : _233_TestOfWarspirit.Medusa_Drops)
				if(qs.getQuestItemsCount(drop_id) == 0L)
					drops.add((int) drop_id);
			if(drops.size() > 0 && Rnd.chance(30))
			{
				final int drop_id2 = drops.get(Rnd.get(drops.size()));
				qs.giveItems(drop_id2, 1L);
				qs.playSound(drops.size() == 1 && qs.getQuestItemsCount(_233_TestOfWarspirit.HERMODTS_SKULL) > 0L ? Quest.SOUND_MIDDLE : Quest.SOUND_ITEMGET);
			}
			drops.clear();
			drops = null;
		}
		else if(npcId == _233_TestOfWarspirit.Stenoa_Gorgon_Queen && qs.getQuestItemsCount(_233_TestOfWarspirit.MANAKIAS_TOTEM) > 0L && qs.getQuestItemsCount(_233_TestOfWarspirit.HERMODTS_SKULL) == 0L && Rnd.chance(30))
		{
			qs.giveItems(_233_TestOfWarspirit.HERMODTS_SKULL, 1L);
			boolean _allset = true;
			for(final short drop_id : _233_TestOfWarspirit.Medusa_Drops)
				if(qs.getQuestItemsCount(drop_id) == 0L)
				{
					_allset = false;
					break;
				}
			qs.playSound(_allset ? Quest.SOUND_MIDDLE : Quest.SOUND_ITEMGET);
		}
		else if((npcId == _233_TestOfWarspirit.Tamlin_Orc || npcId == _233_TestOfWarspirit.Tamlin_Orc_Archer) && qs.getQuestItemsCount(_233_TestOfWarspirit.VENDETTA_TOTEM) > 0L && qs.getQuestItemsCount(_233_TestOfWarspirit.TAMLIN_ORC_HEAD) < 13L && Rnd.chance(npcId == _233_TestOfWarspirit.Tamlin_Orc ? 30 : 50))
		{
			qs.giveItems(_233_TestOfWarspirit.TAMLIN_ORC_HEAD, 1L);
			qs.playSound(qs.getQuestItemsCount(_233_TestOfWarspirit.TAMLIN_ORC_HEAD) == 13L ? Quest.SOUND_MIDDLE : Quest.SOUND_ITEMGET);
		}
		return null;
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
		_233_TestOfWarspirit.Somak = 30510;
		_233_TestOfWarspirit.Vivyan = 30030;
		_233_TestOfWarspirit.Sarien = 30436;
		_233_TestOfWarspirit.Racoy = 30507;
		_233_TestOfWarspirit.Manakia = 30515;
		_233_TestOfWarspirit.Orim = 30630;
		_233_TestOfWarspirit.Ancestor_Martankus = 30649;
		_233_TestOfWarspirit.Pekiron = 30682;
		_233_TestOfWarspirit.Porta = 20213;
		_233_TestOfWarspirit.Excuro = 20214;
		_233_TestOfWarspirit.Mordeo = 20215;
		_233_TestOfWarspirit.Noble_Ant = 20089;
		_233_TestOfWarspirit.Noble_Ant_Leader = 20090;
		_233_TestOfWarspirit.Leto_Lizardman_Shaman = 20581;
		_233_TestOfWarspirit.Leto_Lizardman_Overlord = 20582;
		_233_TestOfWarspirit.Medusa = 20158;
		_233_TestOfWarspirit.Stenoa_Gorgon_Queen = 27108;
		_233_TestOfWarspirit.Tamlin_Orc = 20601;
		_233_TestOfWarspirit.Tamlin_Orc_Archer = 20602;
		_233_TestOfWarspirit.Dimensional_Diamond = 7562;
		_233_TestOfWarspirit.MARK_OF_WARSPIRIT = 2879;
		_233_TestOfWarspirit.VENDETTA_TOTEM = 2880;
		_233_TestOfWarspirit.TAMLIN_ORC_HEAD = 2881;
		_233_TestOfWarspirit.WARSPIRIT_TOTEM = 2882;
		_233_TestOfWarspirit.ORIMS_CONTRACT = 2883;
		_233_TestOfWarspirit.PORTAS_EYE = 2884;
		_233_TestOfWarspirit.EXCUROS_SCALE = 2885;
		_233_TestOfWarspirit.MORDEOS_TALON = 2886;
		_233_TestOfWarspirit.BRAKIS_REMAINS1 = 2887;
		_233_TestOfWarspirit.PEKIRONS_TOTEM = 2888;
		_233_TestOfWarspirit.TONARS_SKULL = 2889;
		_233_TestOfWarspirit.TONARS_RIB_BONE = 2890;
		_233_TestOfWarspirit.TONARS_SPINE = 2891;
		_233_TestOfWarspirit.TONARS_ARM_BONE = 2892;
		_233_TestOfWarspirit.TONARS_THIGH_BONE = 2893;
		_233_TestOfWarspirit.TONARS_REMAINS1 = 2894;
		_233_TestOfWarspirit.MANAKIAS_TOTEM = 2895;
		_233_TestOfWarspirit.HERMODTS_SKULL = 2896;
		_233_TestOfWarspirit.HERMODTS_RIB_BONE = 2897;
		_233_TestOfWarspirit.HERMODTS_SPINE = 2898;
		_233_TestOfWarspirit.HERMODTS_ARM_BONE = 2899;
		_233_TestOfWarspirit.HERMODTS_THIGH_BONE = 2900;
		_233_TestOfWarspirit.HERMODTS_REMAINS1 = 2901;
		_233_TestOfWarspirit.RACOYS_TOTEM = 2902;
		_233_TestOfWarspirit.VIVIANTES_LETTER = 2903;
		_233_TestOfWarspirit.INSECT_DIAGRAM_BOOK = 2904;
		_233_TestOfWarspirit.KIRUNAS_SKULL = 2905;
		_233_TestOfWarspirit.KIRUNAS_RIB_BONE = 2906;
		_233_TestOfWarspirit.KIRUNAS_SPINE = 2907;
		_233_TestOfWarspirit.KIRUNAS_ARM_BONE = 2908;
		_233_TestOfWarspirit.KIRUNAS_THIGH_BONE = 2909;
		_233_TestOfWarspirit.KIRUNAS_REMAINS1 = 2910;
		_233_TestOfWarspirit.BRAKIS_REMAINS2 = 2911;
		_233_TestOfWarspirit.TONARS_REMAINS2 = 2912;
		_233_TestOfWarspirit.HERMODTS_REMAINS2 = 2913;
		_233_TestOfWarspirit.KIRUNAS_REMAINS2 = 2914;
		_233_TestOfWarspirit.Noble_Ant_Drops = new short[] {
				_233_TestOfWarspirit.KIRUNAS_THIGH_BONE,
				_233_TestOfWarspirit.KIRUNAS_ARM_BONE,
				_233_TestOfWarspirit.KIRUNAS_SPINE,
				_233_TestOfWarspirit.KIRUNAS_RIB_BONE,
				_233_TestOfWarspirit.KIRUNAS_SKULL };
		_233_TestOfWarspirit.Leto_Lizardman_Drops = new short[] {
				_233_TestOfWarspirit.TONARS_SKULL,
				_233_TestOfWarspirit.TONARS_RIB_BONE,
				_233_TestOfWarspirit.TONARS_SPINE,
				_233_TestOfWarspirit.TONARS_ARM_BONE,
				_233_TestOfWarspirit.TONARS_THIGH_BONE };
		_233_TestOfWarspirit.Medusa_Drops = new short[] {
				_233_TestOfWarspirit.HERMODTS_RIB_BONE,
				_233_TestOfWarspirit.HERMODTS_SPINE,
				_233_TestOfWarspirit.HERMODTS_THIGH_BONE,
				_233_TestOfWarspirit.HERMODTS_ARM_BONE };
	}
}
