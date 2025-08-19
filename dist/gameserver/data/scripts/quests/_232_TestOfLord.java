package quests;

import java.util.HashMap;
import java.util.Map;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Drop;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _232_TestOfLord extends Quest implements ScriptFile
{
	private static int Somak;
	private static int Manakia;
	private static int Jakal;
	private static int Sumari;
	private static int Kakai;
	private static int Varkees;
	private static int Tantus;
	private static int Hatos;
	private static int Takuna;
	private static int Chianta;
	private static int First_Orc;
	private static int Ancestor_Martankus;
	private static int Marsh_Spider;
	private static int Breka_Orc_Shaman;
	private static int Breka_Orc_Overlord;
	private static int Enchanted_Monstereye;
	private static int Timak_Orc;
	private static int Timak_Orc_Archer;
	private static int Timak_Orc_Soldier;
	private static int Timak_Orc_Warrior;
	private static int Timak_Orc_Shaman;
	private static int Timak_Orc_Overlord;
	private static int Ragna_Orc_Overlord;
	private static int Ragna_Orc_Seer;
	private static short MARK_OF_LORD;
	private static short BONE_ARROW;
	private static short Dimensional_Diamond;
	private static short TIMAK_ORC_SKULL;
	private static short BREKA_ORC_FANG;
	private static short RAGNA_ORC_HEAD;
	private static short RAGNA_CHIEF_NOTICE;
	private static short MARSH_SPIDER_FEELER;
	private static short MARSH_SPIDER_FEET;
	private static short CORNEA_OF_EN_MONSTEREYE;
	private static short ORDEAL_NECKLACE;
	private static short VARKEES_CHARM;
	private static short TANTUS_CHARM;
	private static short HATOS_CHARM;
	private static short TAKUNA_CHARM;
	private static short CHIANTA_CHARM;
	private static short MANAKIAS_ORDERS;
	private static short MANAKIAS_AMULET;
	private static short HUGE_ORC_FANG;
	private static short SUMARIS_LETTER;
	private static short URUTU_BLADE;
	private static short SWORD_INTO_SKULL;
	private static short NERUGA_AXE_BLADE;
	private static short AXE_OF_CEREMONY;
	private static short HANDIWORK_SPIDER_BROOCH;
	private static short MONSTEREYE_WOODCARVING;
	private static short BEAR_FANG_NECKLACE;
	private static short MARTANKUS_CHARM;
	private static short IMMORTAL_FLAME;
	private static Map<Integer, Drop> DROPLIST;

	public _232_TestOfLord()
	{
		super(false);
		this.addStartNpc(_232_TestOfLord.Kakai);
		this.addTalkId(new int[] { _232_TestOfLord.Somak });
		this.addTalkId(new int[] { _232_TestOfLord.Manakia });
		this.addTalkId(new int[] { _232_TestOfLord.Jakal });
		this.addTalkId(new int[] { _232_TestOfLord.Sumari });
		this.addTalkId(new int[] { _232_TestOfLord.Varkees });
		this.addTalkId(new int[] { _232_TestOfLord.Tantus });
		this.addTalkId(new int[] { _232_TestOfLord.Hatos });
		this.addTalkId(new int[] { _232_TestOfLord.Takuna });
		this.addTalkId(new int[] { _232_TestOfLord.Chianta });
		this.addTalkId(new int[] { _232_TestOfLord.First_Orc });
		this.addTalkId(new int[] { _232_TestOfLord.Ancestor_Martankus });
		_232_TestOfLord.DROPLIST.put(_232_TestOfLord.Timak_Orc, new Drop(1, 10, 50).addItem(_232_TestOfLord.TIMAK_ORC_SKULL));
		_232_TestOfLord.DROPLIST.put(_232_TestOfLord.Timak_Orc_Archer, new Drop(1, 10, 55).addItem(_232_TestOfLord.TIMAK_ORC_SKULL));
		_232_TestOfLord.DROPLIST.put(_232_TestOfLord.Timak_Orc_Soldier, new Drop(1, 10, 60).addItem(_232_TestOfLord.TIMAK_ORC_SKULL));
		_232_TestOfLord.DROPLIST.put(_232_TestOfLord.Timak_Orc_Warrior, new Drop(1, 10, 65).addItem(_232_TestOfLord.TIMAK_ORC_SKULL));
		_232_TestOfLord.DROPLIST.put(_232_TestOfLord.Timak_Orc_Shaman, new Drop(1, 10, 70).addItem(_232_TestOfLord.TIMAK_ORC_SKULL));
		_232_TestOfLord.DROPLIST.put(_232_TestOfLord.Timak_Orc_Overlord, new Drop(1, 10, 75).addItem(_232_TestOfLord.TIMAK_ORC_SKULL));
		_232_TestOfLord.DROPLIST.put(_232_TestOfLord.Breka_Orc_Shaman, new Drop(1, 20, 40).addItem(_232_TestOfLord.BREKA_ORC_FANG));
		_232_TestOfLord.DROPLIST.put(_232_TestOfLord.Breka_Orc_Overlord, new Drop(1, 20, 50).addItem(_232_TestOfLord.BREKA_ORC_FANG));
		_232_TestOfLord.DROPLIST.put(_232_TestOfLord.Ragna_Orc_Overlord, new Drop(4, 1, 100).addItem(_232_TestOfLord.RAGNA_ORC_HEAD));
		_232_TestOfLord.DROPLIST.put(_232_TestOfLord.Ragna_Orc_Seer, new Drop(4, 1, 100).addItem(_232_TestOfLord.RAGNA_CHIEF_NOTICE));
		_232_TestOfLord.DROPLIST.put(_232_TestOfLord.Marsh_Spider, new Drop(1, 10, 100).addItem(_232_TestOfLord.MARSH_SPIDER_FEELER).addItem(_232_TestOfLord.MARSH_SPIDER_FEET));
		_232_TestOfLord.DROPLIST.put(_232_TestOfLord.Enchanted_Monstereye, new Drop(1, 20, 90).addItem(_232_TestOfLord.CORNEA_OF_EN_MONSTEREYE));
		for(final int kill_id : _232_TestOfLord.DROPLIST.keySet())
			this.addKillId(new int[] { kill_id });
		for(final Drop _drop : _232_TestOfLord.DROPLIST.values())
			for(final int item_id : _drop.itemList)
				if(!getItems().contains(item_id))
					addQuestItem(new int[] { item_id });
		addQuestItem(new int[] { _232_TestOfLord.ORDEAL_NECKLACE });
		addQuestItem(new int[] { _232_TestOfLord.VARKEES_CHARM });
		addQuestItem(new int[] { _232_TestOfLord.TANTUS_CHARM });
		addQuestItem(new int[] { _232_TestOfLord.HATOS_CHARM });
		addQuestItem(new int[] { _232_TestOfLord.TAKUNA_CHARM });
		addQuestItem(new int[] { _232_TestOfLord.CHIANTA_CHARM });
		addQuestItem(new int[] { _232_TestOfLord.MANAKIAS_ORDERS });
		addQuestItem(new int[] { _232_TestOfLord.MANAKIAS_AMULET });
		addQuestItem(new int[] { _232_TestOfLord.HUGE_ORC_FANG });
		addQuestItem(new int[] { _232_TestOfLord.SUMARIS_LETTER });
		addQuestItem(new int[] { _232_TestOfLord.URUTU_BLADE });
		addQuestItem(new int[] { _232_TestOfLord.SWORD_INTO_SKULL });
		addQuestItem(new int[] { _232_TestOfLord.NERUGA_AXE_BLADE });
		addQuestItem(new int[] { _232_TestOfLord.AXE_OF_CEREMONY });
		addQuestItem(new int[] { _232_TestOfLord.HANDIWORK_SPIDER_BROOCH });
		addQuestItem(new int[] { _232_TestOfLord.MONSTEREYE_WOODCARVING });
		addQuestItem(new int[] { _232_TestOfLord.BEAR_FANG_NECKLACE });
		addQuestItem(new int[] { _232_TestOfLord.MARTANKUS_CHARM });
		addQuestItem(new int[] { _232_TestOfLord.IMMORTAL_FLAME });
	}

	private static void spawn_First_Orc(final QuestState st)
	{
		st.addSpawn(_232_TestOfLord.First_Orc, 21036, -107690, -3038);
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int state = st.getState();
		if(state == 1)
		{
			if(event.equalsIgnoreCase("30565-05.htm"))
			{
				st.giveItems(_232_TestOfLord.ORDEAL_NECKLACE, 1L);
				if(!st.getPlayer().getVarBoolean("dd3"))
				{
					st.giveItems(_232_TestOfLord.Dimensional_Diamond, 92L);
					st.getPlayer().setVar("dd3", "1");
				}
				st.setState(2);
				st.set("cond", "1");
				st.playSound(Quest.SOUND_ACCEPT);
			}
		}
		else if(state == 2)
			if(event.equalsIgnoreCase("30565-12.htm") && st.getQuestItemsCount(_232_TestOfLord.IMMORTAL_FLAME) > 0L)
			{
				st.takeItems(_232_TestOfLord.IMMORTAL_FLAME, -1L);
				st.giveItems(_232_TestOfLord.MARK_OF_LORD, 1L);
				if(!st.getPlayer().getVarBoolean("prof2.3"))
				{
					st.addExpAndSp(434000L, 60000L, true);
					st.giveItems(57, 100000L, Config.RATE_QUESTS_OCCUPATION_CHANGE);
					st.getPlayer().setVar("prof2.3", "1");
				}
				st.playSound(Quest.SOUND_FINISH);
				st.unset("cond");
				st.exitCurrentQuest(true);
			}
			else if(event.equalsIgnoreCase("30565-08.htm"))
			{
				st.takeItems(_232_TestOfLord.SWORD_INTO_SKULL, -1L);
				st.takeItems(_232_TestOfLord.AXE_OF_CEREMONY, -1L);
				st.takeItems(_232_TestOfLord.MONSTEREYE_WOODCARVING, -1L);
				st.takeItems(_232_TestOfLord.HANDIWORK_SPIDER_BROOCH, -1L);
				st.takeItems(_232_TestOfLord.ORDEAL_NECKLACE, -1L);
				st.takeItems(_232_TestOfLord.HUGE_ORC_FANG, -1L);
				st.giveItems(_232_TestOfLord.BEAR_FANG_NECKLACE, 1L);
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "3");
			}
			else if(event.equalsIgnoreCase("30566-02.htm"))
				st.giveItems(_232_TestOfLord.VARKEES_CHARM, 1L);
			else if(event.equalsIgnoreCase("30567-02.htm"))
				st.giveItems(_232_TestOfLord.TANTUS_CHARM, 1L);
			else if(event.equalsIgnoreCase("30558-02.htm") && st.getQuestItemsCount(57) >= 1000L)
			{
				st.takeItems(57, 1000L);
				st.giveItems(_232_TestOfLord.NERUGA_AXE_BLADE, 1L);
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else if(event.equalsIgnoreCase("30568-02.htm"))
				st.giveItems(_232_TestOfLord.HATOS_CHARM, 1L);
			else if(event.equalsIgnoreCase("30641-02.htm"))
				st.giveItems(_232_TestOfLord.TAKUNA_CHARM, 1L);
			else if(event.equalsIgnoreCase("30642-02.htm"))
				st.giveItems(_232_TestOfLord.CHIANTA_CHARM, 1L);
			else if(event.equalsIgnoreCase("30649-04.htm") && st.getQuestItemsCount(_232_TestOfLord.BEAR_FANG_NECKLACE) > 0L)
			{
				st.takeItems(_232_TestOfLord.BEAR_FANG_NECKLACE, -1L);
				st.giveItems(_232_TestOfLord.MARTANKUS_CHARM, 1L);
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "4");
			}
			else if(event.equalsIgnoreCase("30649-07.htm"))
			{
				st.set("cond", "6");
				spawn_First_Orc(st);
			}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		if(st.getQuestItemsCount(_232_TestOfLord.MARK_OF_LORD) > 0L)
		{
			st.exitCurrentQuest(true);
			return "completed";
		}
		final int _state = st.getState();
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(_state == 1)
		{
			if(npcId != _232_TestOfLord.Kakai)
				return "noquest";
			if(st.getPlayer().getRace() != Race.orc)
			{
				st.exitCurrentQuest(true);
				return "30565-01.htm";
			}
			if(st.getPlayer().getClassId().getId() != 50)
			{
				st.exitCurrentQuest(true);
				return "30565-02.htm";
			}
			if(st.getPlayer().getLevel() < 39)
			{
				st.exitCurrentQuest(true);
				return "30565-03.htm";
			}
			st.set("cond", "0");
			return "30565-04.htm";
		}
		else
		{
			if(_state != 2 || cond < 1)
				return "noquest";
			final long ORDEAL_NECKLACE_COUNT = st.getQuestItemsCount(_232_TestOfLord.ORDEAL_NECKLACE);
			final long HUGE_ORC_FANG_COUNT = st.getQuestItemsCount(_232_TestOfLord.HUGE_ORC_FANG);
			final long SWORD_INTO_SKULL_COUNT = st.getQuestItemsCount(_232_TestOfLord.SWORD_INTO_SKULL);
			final long AXE_OF_CEREMONY_COUNT = st.getQuestItemsCount(_232_TestOfLord.AXE_OF_CEREMONY);
			final long MONSTEREYE_WOODCARVING_COUNT = st.getQuestItemsCount(_232_TestOfLord.MONSTEREYE_WOODCARVING);
			final long HANDIWORK_SPIDER_BROOCH_COUNT = st.getQuestItemsCount(_232_TestOfLord.HANDIWORK_SPIDER_BROOCH);
			final long BEAR_FANG_NECKLACE_COUNT = st.getQuestItemsCount(_232_TestOfLord.BEAR_FANG_NECKLACE);
			final long MARTANKUS_CHARM_COUNT = st.getQuestItemsCount(_232_TestOfLord.MARTANKUS_CHARM);
			final long IMMORTAL_FLAME_COUNT = st.getQuestItemsCount(_232_TestOfLord.IMMORTAL_FLAME);
			final long VARKEES_CHARM_COUNT = st.getQuestItemsCount(_232_TestOfLord.VARKEES_CHARM);
			final long MANAKIAS_AMULET_COUNT = st.getQuestItemsCount(_232_TestOfLord.MANAKIAS_AMULET);
			final long MANAKIAS_ORDERS_COUNT = st.getQuestItemsCount(_232_TestOfLord.MANAKIAS_ORDERS);
			final long BREKA_ORC_FANG_COUNT = st.getQuestItemsCount(_232_TestOfLord.BREKA_ORC_FANG);
			final long TANTUS_CHARM_COUNT = st.getQuestItemsCount(_232_TestOfLord.TANTUS_CHARM);
			final long NERUGA_AXE_BLADE_COUNT = st.getQuestItemsCount(_232_TestOfLord.NERUGA_AXE_BLADE);
			final long HATOS_CHARM_COUNT = st.getQuestItemsCount(_232_TestOfLord.HATOS_CHARM);
			final long URUTU_BLADE_COUNT = st.getQuestItemsCount(_232_TestOfLord.URUTU_BLADE);
			final long TIMAK_ORC_SKULL_COUNT = st.getQuestItemsCount(_232_TestOfLord.TIMAK_ORC_SKULL);
			final long SUMARIS_LETTER_COUNT = st.getQuestItemsCount(_232_TestOfLord.SUMARIS_LETTER);
			final long TAKUNA_CHARM_COUNT = st.getQuestItemsCount(_232_TestOfLord.TAKUNA_CHARM);
			if(npcId == _232_TestOfLord.Kakai)
			{
				if(ORDEAL_NECKLACE_COUNT > 0L)
					return cond1Complete(st) ? "30565-07.htm" : "30565-06.htm";
				if(BEAR_FANG_NECKLACE_COUNT > 0L)
					return "30565-09.htm";
				if(MARTANKUS_CHARM_COUNT > 0L)
					return "30565-10.htm";
				if(IMMORTAL_FLAME_COUNT > 0L)
					return "30565-11.htm";
			}
			if(npcId == _232_TestOfLord.Varkees && ORDEAL_NECKLACE_COUNT > 0L)
			{
				if(HUGE_ORC_FANG_COUNT > 0L)
					return "30566-05.htm";
				if(VARKEES_CHARM_COUNT == 0L)
					return "30566-01.htm";
				if(MANAKIAS_AMULET_COUNT == 0L)
					return "30566-03.htm";
				st.takeItems(_232_TestOfLord.VARKEES_CHARM, -1L);
				st.takeItems(_232_TestOfLord.MANAKIAS_AMULET, -1L);
				st.giveItems(_232_TestOfLord.HUGE_ORC_FANG, 1L);
				if(cond1Complete(st))
				{
					st.playSound(Quest.SOUND_JACKPOT);
					st.set("cond", "2");
				}
				else
					st.playSound(Quest.SOUND_MIDDLE);
				return "30566-04.htm";
			}
			else
			{
				if(npcId == _232_TestOfLord.Manakia && ORDEAL_NECKLACE_COUNT > 0L)
					if(VARKEES_CHARM_COUNT > 0L && HUGE_ORC_FANG_COUNT == 0L)
					{
						if(MANAKIAS_AMULET_COUNT == 0L)
						{
							if(MANAKIAS_ORDERS_COUNT == 0L)
							{
								st.giveItems(_232_TestOfLord.MANAKIAS_ORDERS, 1L);
								return "30515-01.htm";
							}
							if(BREKA_ORC_FANG_COUNT < 20L)
								return "30515-02.htm";
							st.takeItems(_232_TestOfLord.MANAKIAS_ORDERS, -1L);
							st.takeItems(_232_TestOfLord.BREKA_ORC_FANG, -1L);
							st.giveItems(_232_TestOfLord.MANAKIAS_AMULET, 1L);
							st.playSound(Quest.SOUND_MIDDLE);
							return "30515-03.htm";
						}
						else if(MANAKIAS_ORDERS_COUNT == 0L)
							return "30515-04.htm";
					}
					else if(VARKEES_CHARM_COUNT == 0L && HUGE_ORC_FANG_COUNT > 0L && MANAKIAS_AMULET_COUNT == 0L && MANAKIAS_ORDERS_COUNT == 0L)
						return "30515-05.htm";
				if(npcId == _232_TestOfLord.Tantus)
					if(AXE_OF_CEREMONY_COUNT == 0L)
					{
						if(TANTUS_CHARM_COUNT == 0L)
							return "30567-01.htm";
						if(NERUGA_AXE_BLADE_COUNT == 0L || st.getQuestItemsCount(_232_TestOfLord.BONE_ARROW) < 1000L)
							return "30567-03.htm";
						st.takeItems(_232_TestOfLord.TANTUS_CHARM, -1L);
						st.takeItems(_232_TestOfLord.NERUGA_AXE_BLADE, -1L);
						st.takeItems(_232_TestOfLord.BONE_ARROW, 1000L);
						st.giveItems(_232_TestOfLord.AXE_OF_CEREMONY, 1L);
						if(cond1Complete(st))
						{
							st.playSound(Quest.SOUND_JACKPOT);
							st.set("cond", "2");
						}
						else
							st.playSound(Quest.SOUND_MIDDLE);
						return "30567-04.htm";
					}
					else if(TANTUS_CHARM_COUNT == 0L)
						return "30567-05.htm";
				if(npcId == _232_TestOfLord.Jakal)
					if(TANTUS_CHARM_COUNT > 0L && AXE_OF_CEREMONY_COUNT == 0L)
					{
						if(NERUGA_AXE_BLADE_COUNT > 0L)
							return "30558-04.htm";
						return st.getQuestItemsCount(57) < 1000L ? "30558-03.htm" : "30558-01.htm";
					}
					else if(TANTUS_CHARM_COUNT == 0L && AXE_OF_CEREMONY_COUNT > 0L)
						return "30558-05.htm";
				if(npcId == _232_TestOfLord.Hatos)
					if(SWORD_INTO_SKULL_COUNT == 0L)
					{
						if(HATOS_CHARM_COUNT == 0L)
							return "30568-01.htm";
						if(URUTU_BLADE_COUNT == 0L || TIMAK_ORC_SKULL_COUNT < 10L)
							return "30568-03.htm";
						st.takeItems(_232_TestOfLord.HATOS_CHARM, -1L);
						st.takeItems(_232_TestOfLord.URUTU_BLADE, -1L);
						st.takeItems(_232_TestOfLord.TIMAK_ORC_SKULL, -1L);
						st.giveItems(_232_TestOfLord.SWORD_INTO_SKULL, 1L);
						if(cond1Complete(st))
						{
							st.playSound(Quest.SOUND_JACKPOT);
							st.set("cond", "2");
						}
						else
							st.playSound(Quest.SOUND_MIDDLE);
						return "30568-04.htm";
					}
					else if(HATOS_CHARM_COUNT == 0L)
						return "30568-05.htm";
				if(npcId == _232_TestOfLord.Sumari)
					if(HATOS_CHARM_COUNT > 0L && SWORD_INTO_SKULL_COUNT == 0L)
					{
						if(URUTU_BLADE_COUNT == 0L)
						{
							if(SUMARIS_LETTER_COUNT > 0L)
								return "30564-02.htm";
							st.giveItems(_232_TestOfLord.SUMARIS_LETTER, 1L);
							st.playSound(Quest.SOUND_MIDDLE);
							return "30564-01.htm";
						}
						else if(SUMARIS_LETTER_COUNT == 0L)
							return "30564-03.htm";
					}
					else if(HATOS_CHARM_COUNT == 0L && SWORD_INTO_SKULL_COUNT > 0L && URUTU_BLADE_COUNT == 0L && SUMARIS_LETTER_COUNT == 0L)
						return "30564-04.htm";
				if(npcId == _232_TestOfLord.Somak)
					if(SWORD_INTO_SKULL_COUNT == 0L)
					{
						if(URUTU_BLADE_COUNT == 0L && HATOS_CHARM_COUNT > 0L && SUMARIS_LETTER_COUNT > 0L)
						{
							st.takeItems(_232_TestOfLord.SUMARIS_LETTER, -1L);
							st.giveItems(_232_TestOfLord.URUTU_BLADE, 1L);
							st.playSound(Quest.SOUND_MIDDLE);
							return "30510-01.htm";
						}
						if(URUTU_BLADE_COUNT > 0L && HATOS_CHARM_COUNT > 0L && SUMARIS_LETTER_COUNT == 0L)
							return "30510-02.htm";
					}
					else if(URUTU_BLADE_COUNT == 0L && HATOS_CHARM_COUNT == 0L && SUMARIS_LETTER_COUNT == 0L)
						return "30510-03.htm";
				if(npcId == _232_TestOfLord.Takuna)
					if(HANDIWORK_SPIDER_BROOCH_COUNT == 0L)
					{
						if(TAKUNA_CHARM_COUNT == 0L)
							return "30641-01.htm";
						if(st.getQuestItemsCount(_232_TestOfLord.MARSH_SPIDER_FEELER) < 10L || st.getQuestItemsCount(_232_TestOfLord.MARSH_SPIDER_FEET) < 10L)
							return "30641-03.htm";
						st.takeItems(_232_TestOfLord.MARSH_SPIDER_FEELER, -1L);
						st.takeItems(_232_TestOfLord.MARSH_SPIDER_FEET, -1L);
						st.takeItems(_232_TestOfLord.TAKUNA_CHARM, -1L);
						st.giveItems(_232_TestOfLord.HANDIWORK_SPIDER_BROOCH, 1L);
						if(cond1Complete(st))
						{
							st.playSound(Quest.SOUND_JACKPOT);
							st.set("cond", "2");
						}
						else
							st.playSound(Quest.SOUND_MIDDLE);
						return "30641-04.htm";
					}
					else if(TAKUNA_CHARM_COUNT == 0L)
						return "30641-05.htm";
				if(npcId == _232_TestOfLord.Chianta)
				{
					final long CHIANTA_CHARM_COUNT = st.getQuestItemsCount(_232_TestOfLord.CHIANTA_CHARM);
					if(MONSTEREYE_WOODCARVING_COUNT == 0L)
					{
						if(CHIANTA_CHARM_COUNT == 0L)
							return "30642-01.htm";
						if(st.getQuestItemsCount(_232_TestOfLord.CORNEA_OF_EN_MONSTEREYE) < 20L)
							return "30642-03.htm";
						st.takeItems(_232_TestOfLord.CORNEA_OF_EN_MONSTEREYE, -1L);
						st.takeItems(_232_TestOfLord.CHIANTA_CHARM, -1L);
						st.giveItems(_232_TestOfLord.MONSTEREYE_WOODCARVING, 1L);
						if(cond1Complete(st))
						{
							st.playSound(Quest.SOUND_JACKPOT);
							st.set("cond", "2");
						}
						else
							st.playSound(Quest.SOUND_MIDDLE);
						return "30642-04.htm";
					}
					else if(CHIANTA_CHARM_COUNT == 0L)
						return "30642-05.htm";
				}
				if(npcId == _232_TestOfLord.Ancestor_Martankus)
				{
					if(BEAR_FANG_NECKLACE_COUNT > 0L)
						return "30649-01.htm";
					if(MARTANKUS_CHARM_COUNT > 0L)
					{
						if(cond == 5 || st.getQuestItemsCount(_232_TestOfLord.RAGNA_CHIEF_NOTICE) > 0L && st.getQuestItemsCount(_232_TestOfLord.RAGNA_ORC_HEAD) > 0L)
						{
							st.takeItems(_232_TestOfLord.MARTANKUS_CHARM, -1L);
							st.takeItems(_232_TestOfLord.RAGNA_ORC_HEAD, -1L);
							st.takeItems(_232_TestOfLord.RAGNA_CHIEF_NOTICE, -1L);
							st.giveItems(_232_TestOfLord.IMMORTAL_FLAME, 1L);
							st.playSound(Quest.SOUND_MIDDLE);
							return "30649-06.htm";
						}
						return "30649-05.htm";
					}
					else if(cond == 6 || cond == 7)
						return "30649-08.htm";
				}
				if(npcId == _232_TestOfLord.First_Orc && st.getQuestItemsCount(_232_TestOfLord.IMMORTAL_FLAME) > 0L)
				{
					st.set("cond", "7");
					return "30643-01.htm";
				}
				return "noquest";
			}
		}
	}

	private boolean cond1Complete(final QuestState st)
	{
		final long HUGE_ORC_FANG_COUNT = st.getQuestItemsCount(_232_TestOfLord.HUGE_ORC_FANG);
		final long SWORD_INTO_SKULL_COUNT = st.getQuestItemsCount(_232_TestOfLord.SWORD_INTO_SKULL);
		final long AXE_OF_CEREMONY_COUNT = st.getQuestItemsCount(_232_TestOfLord.AXE_OF_CEREMONY);
		final long MONSTEREYE_WOODCARVING_COUNT = st.getQuestItemsCount(_232_TestOfLord.MONSTEREYE_WOODCARVING);
		final long HANDIWORK_SPIDER_BROOCH_COUNT = st.getQuestItemsCount(_232_TestOfLord.HANDIWORK_SPIDER_BROOCH);
		return HUGE_ORC_FANG_COUNT > 0L && SWORD_INTO_SKULL_COUNT > 0L && AXE_OF_CEREMONY_COUNT > 0L && MONSTEREYE_WOODCARVING_COUNT > 0L && HANDIWORK_SPIDER_BROOCH_COUNT > 0L;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState qs)
	{
		if(qs.getState() != 2)
			return null;
		final int npcId = npc.getNpcId();
		final Drop _drop = _232_TestOfLord.DROPLIST.get(npcId);
		if(_drop == null)
			return null;
		final int cond = qs.getInt("cond");
		for(final short item_id : _drop.itemList)
		{
			final long ORDEAL_NECKLACE_COUNT = qs.getQuestItemsCount(_232_TestOfLord.ORDEAL_NECKLACE);
			if(item_id == _232_TestOfLord.TIMAK_ORC_SKULL)
			{
				if(ORDEAL_NECKLACE_COUNT <= 0L || qs.getQuestItemsCount(_232_TestOfLord.HATOS_CHARM) <= 0L)
					continue;
				if(qs.getQuestItemsCount(_232_TestOfLord.SWORD_INTO_SKULL) != 0L)
					continue;
			}
			if(item_id == _232_TestOfLord.BREKA_ORC_FANG)
			{
				if(ORDEAL_NECKLACE_COUNT <= 0L || qs.getQuestItemsCount(_232_TestOfLord.VARKEES_CHARM) <= 0L)
					continue;
				if(qs.getQuestItemsCount(_232_TestOfLord.MANAKIAS_ORDERS) <= 0L)
					continue;
			}
			if(npcId == _232_TestOfLord.Marsh_Spider)
			{
				if(ORDEAL_NECKLACE_COUNT <= 0L)
					continue;
				if(qs.getQuestItemsCount(_232_TestOfLord.TAKUNA_CHARM) <= 0L)
					continue;
			}
			if(npcId == _232_TestOfLord.Enchanted_Monstereye)
			{
				if(ORDEAL_NECKLACE_COUNT <= 0L)
					continue;
				if(qs.getQuestItemsCount(_232_TestOfLord.CHIANTA_CHARM) <= 0L)
					continue;
			}
			final long count = qs.getQuestItemsCount(item_id);
			if(cond == _drop.condition && count < _drop.maxcount && Rnd.chance(_drop.chance))
			{
				qs.giveItems(item_id, 1L);
				if(count + 1L == _drop.maxcount)
				{
					if(cond == 4 && qs.getQuestItemsCount(_232_TestOfLord.RAGNA_ORC_HEAD) > 0L && qs.getQuestItemsCount(_232_TestOfLord.RAGNA_CHIEF_NOTICE) > 0L)
						qs.set("cond", "5");
					qs.playSound(Quest.SOUND_MIDDLE);
				}
				else
					qs.playSound(Quest.SOUND_ITEMGET);
			}
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
		_232_TestOfLord.Somak = 30510;
		_232_TestOfLord.Manakia = 30515;
		_232_TestOfLord.Jakal = 30558;
		_232_TestOfLord.Sumari = 30564;
		_232_TestOfLord.Kakai = 30565;
		_232_TestOfLord.Varkees = 30566;
		_232_TestOfLord.Tantus = 30567;
		_232_TestOfLord.Hatos = 30568;
		_232_TestOfLord.Takuna = 30641;
		_232_TestOfLord.Chianta = 30642;
		_232_TestOfLord.First_Orc = 30643;
		_232_TestOfLord.Ancestor_Martankus = 30649;
		_232_TestOfLord.Marsh_Spider = 20233;
		_232_TestOfLord.Breka_Orc_Shaman = 20269;
		_232_TestOfLord.Breka_Orc_Overlord = 20270;
		_232_TestOfLord.Enchanted_Monstereye = 20564;
		_232_TestOfLord.Timak_Orc = 20583;
		_232_TestOfLord.Timak_Orc_Archer = 20584;
		_232_TestOfLord.Timak_Orc_Soldier = 20585;
		_232_TestOfLord.Timak_Orc_Warrior = 20586;
		_232_TestOfLord.Timak_Orc_Shaman = 20587;
		_232_TestOfLord.Timak_Orc_Overlord = 20588;
		_232_TestOfLord.Ragna_Orc_Overlord = 20778;
		_232_TestOfLord.Ragna_Orc_Seer = 20779;
		_232_TestOfLord.MARK_OF_LORD = 3390;
		_232_TestOfLord.BONE_ARROW = 1341;
		_232_TestOfLord.Dimensional_Diamond = 7562;
		_232_TestOfLord.TIMAK_ORC_SKULL = 3403;
		_232_TestOfLord.BREKA_ORC_FANG = 3398;
		_232_TestOfLord.RAGNA_ORC_HEAD = 3414;
		_232_TestOfLord.RAGNA_CHIEF_NOTICE = 3415;
		_232_TestOfLord.MARSH_SPIDER_FEELER = 3407;
		_232_TestOfLord.MARSH_SPIDER_FEET = 3408;
		_232_TestOfLord.CORNEA_OF_EN_MONSTEREYE = 3410;
		_232_TestOfLord.ORDEAL_NECKLACE = 3391;
		_232_TestOfLord.VARKEES_CHARM = 3392;
		_232_TestOfLord.TANTUS_CHARM = 3393;
		_232_TestOfLord.HATOS_CHARM = 3394;
		_232_TestOfLord.TAKUNA_CHARM = 3395;
		_232_TestOfLord.CHIANTA_CHARM = 3396;
		_232_TestOfLord.MANAKIAS_ORDERS = 3397;
		_232_TestOfLord.MANAKIAS_AMULET = 3399;
		_232_TestOfLord.HUGE_ORC_FANG = 3400;
		_232_TestOfLord.SUMARIS_LETTER = 3401;
		_232_TestOfLord.URUTU_BLADE = 3402;
		_232_TestOfLord.SWORD_INTO_SKULL = 3404;
		_232_TestOfLord.NERUGA_AXE_BLADE = 3405;
		_232_TestOfLord.AXE_OF_CEREMONY = 3406;
		_232_TestOfLord.HANDIWORK_SPIDER_BROOCH = 3409;
		_232_TestOfLord.MONSTEREYE_WOODCARVING = 3411;
		_232_TestOfLord.BEAR_FANG_NECKLACE = 3412;
		_232_TestOfLord.MARTANKUS_CHARM = 3413;
		_232_TestOfLord.IMMORTAL_FLAME = 3416;
		_232_TestOfLord.DROPLIST = new HashMap<Integer, Drop>();
	}
}
