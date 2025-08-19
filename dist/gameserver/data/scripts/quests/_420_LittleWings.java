package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _420_LittleWings extends Quest implements ScriptFile
{
	private static final int Cooper = 30829;
	private static final int Cronos = 30610;
	private static final int Byron = 30711;
	private static final int Maria = 30608;
	private static final int Mimyu = 30747;
	private static final int Exarion = 30748;
	private static final int Zwov = 30749;
	private static final int Kalibran = 30750;
	private static final int Suzet = 30751;
	private static final int Shamhai = 30752;
	private static final int Enchanted_Valey_First = 20589;
	private static final int Enchanted_Valey_Last = 20599;
	private static final int Toad_Lord = 20231;
	private static final int Marsh_Spider = 20233;
	private static final int Leto_Lizardman_Warrior = 20580;
	private static final int Road_Scavenger = 20551;
	private static final int Breka_Orc_Overlord = 20270;
	private static final int Dead_Seeker = 20202;
	private static short Coal;
	private static short Charcoal;
	private static short Silver_Nugget;
	private static short Stone_of_Purity;
	private static short GemstoneD;
	private static short GemstoneC;
	private static short Dragonflute_of_Wind;
	private static short Dragonflute_of_Twilight;
	private static short Hatchlings_Soft_Leather;
	private static short Hatchlings_Mithril_Coat;
	private static short Food_For_Hatchling;
	private static short Fairy_Dust;
	private static short Fairy_Stone;
	private static short Deluxe_Fairy_Stone;
	private static short Fairy_Stone_List;
	private static short Deluxe_Fairy_Stone_List;
	private static short Toad_Lord_Back_Skin;
	private static short Juice_of_Monkshood;
	private static short Scale_of_Drake_Exarion;
	private static short Scale_of_Drake_Zwov;
	private static short Scale_of_Drake_Kalibran;
	private static short Scale_of_Wyvern_Suzet;
	private static short Scale_of_Wyvern_Shamhai;
	private static short Egg_of_Drake_Exarion;
	private static short Egg_of_Drake_Zwov;
	private static short Egg_of_Drake_Kalibran;
	private static short Egg_of_Wyvern_Suzet;
	private static short Egg_of_Wyvern_Shamhai;
	private static final int Toad_Lord_Back_Skin_Chance = 30;
	private static final int Egg_Chance = 50;
	private static final int Pet_Armor_Chance = 35;
	private static short[][] Fairy_Stone_Items;
	private static short[][] Delux_Fairy_Stone_Items;
	private static final int[][] wyrms;

	public _420_LittleWings()
	{
		super(false);
		this.addStartNpc(30829);
		this.addTalkId(new int[] { 30610 });
		this.addTalkId(new int[] { 30747 });
		this.addTalkId(new int[] { 30711 });
		this.addTalkId(new int[] { 30608 });
		this.addKillId(new int[] { 20231 });
		for(int Enchanted_Valey_id = 20589; Enchanted_Valey_id <= 20599; ++Enchanted_Valey_id)
			this.addKillId(new int[] { Enchanted_Valey_id });
		for(final int[] wyrm : _420_LittleWings.wyrms)
		{
			this.addTalkId(new int[] { wyrm[1] });
			this.addKillId(new int[] { wyrm[0] });
		}
		addQuestItem(new int[] { _420_LittleWings.Fairy_Dust });
		addQuestItem(new int[] { _420_LittleWings.Fairy_Stone });
		addQuestItem(new int[] { _420_LittleWings.Deluxe_Fairy_Stone });
		addQuestItem(new int[] { _420_LittleWings.Fairy_Stone_List });
		addQuestItem(new int[] { _420_LittleWings.Deluxe_Fairy_Stone_List });
		addQuestItem(new int[] { _420_LittleWings.Toad_Lord_Back_Skin });
		addQuestItem(new int[] { _420_LittleWings.Juice_of_Monkshood });
		addQuestItem(new int[] { _420_LittleWings.Scale_of_Drake_Exarion });
		addQuestItem(new int[] { _420_LittleWings.Scale_of_Drake_Zwov });
		addQuestItem(new int[] { _420_LittleWings.Scale_of_Drake_Kalibran });
		addQuestItem(new int[] { _420_LittleWings.Scale_of_Wyvern_Suzet });
		addQuestItem(new int[] { _420_LittleWings.Scale_of_Wyvern_Shamhai });
		addQuestItem(new int[] { _420_LittleWings.Egg_of_Drake_Exarion });
		addQuestItem(new int[] { _420_LittleWings.Egg_of_Drake_Zwov });
		addQuestItem(new int[] { _420_LittleWings.Egg_of_Drake_Kalibran });
		addQuestItem(new int[] { _420_LittleWings.Egg_of_Wyvern_Suzet });
		addQuestItem(new int[] { _420_LittleWings.Egg_of_Wyvern_Shamhai });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int _state = st.getState();
		final int cond = st.getInt("cond");
		if(event.equalsIgnoreCase("30829-02.htm") && _state == 1)
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if((event.equalsIgnoreCase("30610-05.htm") || event.equalsIgnoreCase("30610-12.htm")) && _state == 2 && cond == 1)
		{
			st.set("cond", "2");
			st.takeItems(_420_LittleWings.Fairy_Stone, -1L);
			st.takeItems(_420_LittleWings.Deluxe_Fairy_Stone, -1L);
			st.takeItems(_420_LittleWings.Fairy_Stone_List, -1L);
			st.takeItems(_420_LittleWings.Deluxe_Fairy_Stone_List, -1L);
			st.giveItems(_420_LittleWings.Fairy_Stone_List, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if((event.equalsIgnoreCase("30610-06.htm") || event.equalsIgnoreCase("30610-13.htm")) && _state == 2 && cond == 1)
		{
			st.set("cond", "2");
			st.takeItems(_420_LittleWings.Fairy_Stone, -1L);
			st.takeItems(_420_LittleWings.Deluxe_Fairy_Stone, -1L);
			st.takeItems(_420_LittleWings.Fairy_Stone_List, -1L);
			st.takeItems(_420_LittleWings.Deluxe_Fairy_Stone_List, -1L);
			st.giveItems(_420_LittleWings.Deluxe_Fairy_Stone_List, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("30608-03.htm") && _state == 2 && cond == 2 && st.getQuestItemsCount(_420_LittleWings.Fairy_Stone_List) > 0L)
		{
			if(!CheckFairyStoneItems(st, _420_LittleWings.Fairy_Stone_Items))
				return "30608-01.htm";
			st.set("cond", "3");
			TakeFairyStoneItems(st, _420_LittleWings.Fairy_Stone_Items);
			st.giveItems(_420_LittleWings.Fairy_Stone, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("30608-03a.htm") && _state == 2 && cond == 2 && st.getQuestItemsCount(_420_LittleWings.Deluxe_Fairy_Stone_List) > 0L)
		{
			if(!CheckFairyStoneItems(st, _420_LittleWings.Delux_Fairy_Stone_Items))
				return "30608-01a.htm";
			st.set("cond", "3");
			TakeFairyStoneItems(st, _420_LittleWings.Delux_Fairy_Stone_Items);
			st.giveItems(_420_LittleWings.Deluxe_Fairy_Stone, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("30711-03.htm") && _state == 2 && cond == 3 && st.getQuestItemsCount(_420_LittleWings.Fairy_Stone) + st.getQuestItemsCount(_420_LittleWings.Deluxe_Fairy_Stone) > 0L)
		{
			st.set("cond", "4");
			st.playSound(Quest.SOUND_MIDDLE);
			if(st.getQuestItemsCount(_420_LittleWings.Deluxe_Fairy_Stone) > 0L)
				return st.getInt("broken") == 1 ? "30711-04a.htm" : "30711-03a.htm";
			if(st.getInt("broken") == 1)
				return "30711-04.htm";
		}
		else if(event.equalsIgnoreCase("30747-02.htm") && _state == 2 && cond == 4 && st.getQuestItemsCount(_420_LittleWings.Fairy_Stone) > 0L)
		{
			st.takeItems(_420_LittleWings.Fairy_Stone, -1L);
			st.set("takedStone", "1");
		}
		else if(event.equalsIgnoreCase("30747-02a.htm") && _state == 2 && cond == 4 && st.getQuestItemsCount(_420_LittleWings.Deluxe_Fairy_Stone) > 0L)
		{
			st.takeItems(_420_LittleWings.Deluxe_Fairy_Stone, -1L);
			st.set("takedStone", "2");
			st.giveItems(_420_LittleWings.Fairy_Dust, 1L);
			st.playSound(Quest.SOUND_ITEMGET);
		}
		else if(event.equalsIgnoreCase("30747-04.htm") && _state == 2 && cond == 4 && st.getInt("takedStone") > 0)
		{
			st.set("cond", "5");
			st.unset("takedStone");
			st.giveItems(_420_LittleWings.Juice_of_Monkshood, 1L);
			st.playSound(Quest.SOUND_ITEMGET);
		}
		else if(event.equalsIgnoreCase("30748-02.htm") && cond == 5 && _state == 2 && st.getQuestItemsCount(_420_LittleWings.Juice_of_Monkshood) > 0L)
		{
			st.set("cond", "6");
			st.takeItems(_420_LittleWings.Juice_of_Monkshood, -1L);
			st.giveItems(3822, 1L);
			st.playSound(Quest.SOUND_ITEMGET);
		}
		else if(event.equalsIgnoreCase("30749-02.htm") && cond == 5 && _state == 2 && st.getQuestItemsCount(_420_LittleWings.Juice_of_Monkshood) > 0L)
		{
			st.set("cond", "6");
			st.takeItems(_420_LittleWings.Juice_of_Monkshood, -1L);
			st.giveItems(3824, 1L);
			st.playSound(Quest.SOUND_ITEMGET);
		}
		else if(event.equalsIgnoreCase("30750-02.htm") && cond == 5 && _state == 2 && st.getQuestItemsCount(_420_LittleWings.Juice_of_Monkshood) > 0L)
		{
			st.set("cond", "6");
			st.takeItems(_420_LittleWings.Juice_of_Monkshood, -1L);
			st.giveItems(3826, 1L);
			st.playSound(Quest.SOUND_ITEMGET);
		}
		else if(event.equalsIgnoreCase("30751-02.htm") && cond == 5 && _state == 2 && st.getQuestItemsCount(_420_LittleWings.Juice_of_Monkshood) > 0L)
		{
			st.set("cond", "6");
			st.takeItems(_420_LittleWings.Juice_of_Monkshood, -1L);
			st.giveItems(3828, 1L);
			st.playSound(Quest.SOUND_ITEMGET);
		}
		else if(event.equalsIgnoreCase("30752-02.htm") && cond == 5 && _state == 2 && st.getQuestItemsCount(_420_LittleWings.Juice_of_Monkshood) > 0L)
		{
			st.set("cond", "6");
			st.takeItems(_420_LittleWings.Juice_of_Monkshood, -1L);
			st.giveItems(3830, 1L);
			st.playSound(Quest.SOUND_ITEMGET);
		}
		else if(event.equalsIgnoreCase("30747-09.htm") && _state == 2 && cond == 7)
		{
			int egg_id = 0;
			for(final int[] wyrm : _420_LittleWings.wyrms)
				if(st.getQuestItemsCount(wyrm[2]) == 0L && st.getQuestItemsCount(wyrm[3]) >= 1L)
				{
					egg_id = wyrm[3];
					break;
				}
			if(egg_id == 0)
				return "noquest";
			st.takeItems(egg_id, -1L);
			st.giveItems(Rnd.get(_420_LittleWings.Dragonflute_of_Wind, _420_LittleWings.Dragonflute_of_Twilight), 1L);
			if(st.getQuestItemsCount(_420_LittleWings.Fairy_Dust) > 0L)
			{
				st.playSound(Quest.SOUND_MIDDLE);
				return "30747-09a.htm";
			}
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		else if(event.equalsIgnoreCase("30747-10.htm") && _state == 2 && cond == 7)
		{
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		else if(event.equalsIgnoreCase("30747-11.htm") && _state == 2 && cond == 7)
		{
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
			if(st.getQuestItemsCount(_420_LittleWings.Fairy_Dust) == 0L)
				return "30747-10.htm";
			st.takeItems(_420_LittleWings.Fairy_Dust, -1L);
			if(Rnd.chance(35))
			{
				int armor_id = _420_LittleWings.Hatchlings_Soft_Leather + Rnd.get((int) st.getRateQuestsReward());
				if(armor_id > _420_LittleWings.Hatchlings_Mithril_Coat)
					armor_id = _420_LittleWings.Hatchlings_Mithril_Coat;
				st.giveItems(armor_id, 1L);
			}
			else
				st.giveItems(_420_LittleWings.Food_For_Hatchling, 20L, true);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int _state = st.getState();
		final int npcId = npc.getNpcId();
		if(_state == 1)
		{
			if(npcId != 30829)
				return "noquest";
			if(st.getPlayer().getLevel() < 35)
			{
				st.exitCurrentQuest(true);
				return "30829-00.htm";
			}
			st.set("cond", "0");
			return "30829-01.htm";
		}
		else
		{
			if(_state != 2)
				return "noquest";
			final int cond = st.getInt("cond");
			final int broken = st.getInt("broken");
			if(npcId != 30829)
			{
				if(npcId == 30610)
				{
					if(cond == 1)
						return broken == 1 ? "30610-10.htm" : "30610-01.htm";
					if(cond == 2)
						return "30610-07.htm";
					if(cond == 3)
						return broken == 1 ? "30610-14.htm" : "30610-08.htm";
					if(cond == 4)
						return "30610-09.htm";
					if(cond > 4)
						return "30610-11.htm";
				}
				if(npcId == 30608)
					if(cond == 2)
					{
						if(st.getQuestItemsCount(_420_LittleWings.Deluxe_Fairy_Stone_List) > 0L)
							return CheckFairyStoneItems(st, _420_LittleWings.Delux_Fairy_Stone_Items) ? "30608-02a.htm" : "30608-01a.htm";
						if(st.getQuestItemsCount(_420_LittleWings.Fairy_Stone_List) > 0L)
							return CheckFairyStoneItems(st, _420_LittleWings.Fairy_Stone_Items) ? "30608-02.htm" : "30608-01.htm";
					}
					else if(cond > 2)
						return "30608-04.htm";
				if(npcId == 30711)
				{
					if(cond == 1 && broken == 1)
						return "30711-06.htm";
					if(cond == 2 && broken == 1)
						return "30711-07.htm";
					if(cond == 3 && st.getQuestItemsCount(_420_LittleWings.Fairy_Stone) + st.getQuestItemsCount(_420_LittleWings.Deluxe_Fairy_Stone) > 0L)
						return "30711-01.htm";
					if(cond >= 4 && st.getQuestItemsCount(_420_LittleWings.Deluxe_Fairy_Stone) > 0L)
						return "30711-05a.htm";
					if(cond >= 4 && st.getQuestItemsCount(_420_LittleWings.Fairy_Stone) > 0L)
						return "30711-05.htm";
				}
				if(npcId == 30747)
				{
					if(cond == 4 && st.getQuestItemsCount(_420_LittleWings.Deluxe_Fairy_Stone) > 0L)
						return "30747-01a.htm";
					if(cond == 4 && st.getQuestItemsCount(_420_LittleWings.Fairy_Stone) > 0L)
						return "30747-01.htm";
					if(cond == 5)
						return "30747-05.htm";
					if(cond == 6)
					{
						for(final int[] wyrm : _420_LittleWings.wyrms)
							if(st.getQuestItemsCount(wyrm[2]) == 0L && st.getQuestItemsCount(wyrm[3]) >= 20L)
								return "30747-07.htm";
						return "30747-06.htm";
					}
					if(cond == 7)
						for(final int[] wyrm : _420_LittleWings.wyrms)
							if(st.getQuestItemsCount(wyrm[2]) == 0L && st.getQuestItemsCount(wyrm[3]) >= 1L)
								return "30747-08.htm";
				}
				if(npcId >= 30748 && npcId <= 30752)
				{
					if(cond == 5 && st.getQuestItemsCount(_420_LittleWings.Juice_of_Monkshood) > 0L)
						return String.valueOf(npcId) + "-01.htm";
					if(cond == 6 && st.getQuestItemsCount(getWyrmScale(npcId)) > 0L)
					{
						final int egg_id = getWyrmEgg(npcId);
						if(st.getQuestItemsCount(egg_id) < 20L)
							return String.valueOf(npcId) + "-03.htm";
						st.takeItems(getWyrmScale(npcId), -1L);
						st.takeItems(egg_id, -1L);
						st.giveItems(egg_id, 1L);
						st.set("cond", "7");
						return String.valueOf(npcId) + "-04.htm";
					}
					else if(cond == 7 && st.getQuestItemsCount(getWyrmEgg(npcId)) == 1L)
						return String.valueOf(npcId) + "-05.htm";
				}
				return "noquest";
			}
			if(cond == 1)
				return "30829-02.htm";
			return "30829-03.htm";
		}
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getState() != 2)
			return null;
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(cond == 2 && npcId == 20231)
		{
			final int needed_skins = getNeededSkins(st);
			if(st.getQuestItemsCount(_420_LittleWings.Toad_Lord_Back_Skin) < needed_skins && Rnd.chance(30))
			{
				st.giveItems(_420_LittleWings.Toad_Lord_Back_Skin, 1L);
				st.playSound(st.getQuestItemsCount(_420_LittleWings.Toad_Lord_Back_Skin) < needed_skins ? Quest.SOUND_ITEMGET : Quest.SOUND_MIDDLE);
			}
			return null;
		}
		if(npcId >= 20589 && npcId <= 20599 && st.getQuestItemsCount(_420_LittleWings.Deluxe_Fairy_Stone) > 0L)
		{
			st.takeItems(_420_LittleWings.Deluxe_Fairy_Stone, 1L);
			st.set("broken", "1");
			st.set("cond", "1");
			return "You lost fairy stone deluxe!";
		}
		if(cond == 6)
		{
			final int wyrm_id = isWyrmStoler(npcId);
			if(wyrm_id > 0 && st.getQuestItemsCount(getWyrmScale(wyrm_id)) > 0L && st.getQuestItemsCount(getWyrmEgg(wyrm_id)) < 20L && Rnd.chance(50))
			{
				st.giveItems(getWyrmEgg(wyrm_id), 1L);
				st.playSound(st.getQuestItemsCount(getWyrmEgg(wyrm_id)) < 20L ? Quest.SOUND_ITEMGET : Quest.SOUND_MIDDLE);
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

	private static int getWyrmScale(final int npc_id)
	{
		for(final int[] wyrm : _420_LittleWings.wyrms)
			if(npc_id == wyrm[1])
				return wyrm[2];
		return 0;
	}

	private static int getWyrmEgg(final int npc_id)
	{
		for(final int[] wyrm : _420_LittleWings.wyrms)
			if(npc_id == wyrm[1])
				return wyrm[3];
		return 0;
	}

	private static int isWyrmStoler(final int npc_id)
	{
		for(final int[] wyrm : _420_LittleWings.wyrms)
			if(npc_id == wyrm[0])
				return wyrm[1];
		return 0;
	}

	public static int getNeededSkins(final QuestState st)
	{
		if(st.getQuestItemsCount(_420_LittleWings.Deluxe_Fairy_Stone_List) > 0L)
			return 20;
		if(st.getQuestItemsCount(_420_LittleWings.Fairy_Stone_List) > 0L)
			return 10;
		return -1;
	}

	public static boolean CheckFairyStoneItems(final QuestState st, final short[][] item_list)
	{
		for(final short[] _item : item_list)
			if(st.getQuestItemsCount(_item[0]) < _item[1])
				return false;
		return true;
	}

	public static void TakeFairyStoneItems(final QuestState st, final short[][] item_list)
	{
		for(final short[] _item : item_list)
			st.takeItems(_item[0], _item[1]);
	}

	static
	{
		_420_LittleWings.Coal = 1870;
		_420_LittleWings.Charcoal = 1871;
		_420_LittleWings.Silver_Nugget = 1873;
		_420_LittleWings.Stone_of_Purity = 1875;
		_420_LittleWings.GemstoneD = 2130;
		_420_LittleWings.GemstoneC = 2131;
		_420_LittleWings.Dragonflute_of_Wind = 3500;
		_420_LittleWings.Dragonflute_of_Twilight = 3502;
		_420_LittleWings.Hatchlings_Soft_Leather = 3912;
		_420_LittleWings.Hatchlings_Mithril_Coat = 3918;
		_420_LittleWings.Food_For_Hatchling = 4038;
		_420_LittleWings.Fairy_Dust = 3499;
		_420_LittleWings.Fairy_Stone = 3816;
		_420_LittleWings.Deluxe_Fairy_Stone = 3817;
		_420_LittleWings.Fairy_Stone_List = 3818;
		_420_LittleWings.Deluxe_Fairy_Stone_List = 3819;
		_420_LittleWings.Toad_Lord_Back_Skin = 3820;
		_420_LittleWings.Juice_of_Monkshood = 3821;
		_420_LittleWings.Scale_of_Drake_Exarion = 3822;
		_420_LittleWings.Scale_of_Drake_Zwov = 3824;
		_420_LittleWings.Scale_of_Drake_Kalibran = 3826;
		_420_LittleWings.Scale_of_Wyvern_Suzet = 3828;
		_420_LittleWings.Scale_of_Wyvern_Shamhai = 3830;
		_420_LittleWings.Egg_of_Drake_Exarion = 3823;
		_420_LittleWings.Egg_of_Drake_Zwov = 3825;
		_420_LittleWings.Egg_of_Drake_Kalibran = 3827;
		_420_LittleWings.Egg_of_Wyvern_Suzet = 3829;
		_420_LittleWings.Egg_of_Wyvern_Shamhai = 3831;
		_420_LittleWings.Fairy_Stone_Items = new short[][] {
				{ _420_LittleWings.Coal, 10 },
				{ _420_LittleWings.Charcoal, 10 },
				{ _420_LittleWings.GemstoneD, 1 },
				{ _420_LittleWings.Silver_Nugget, 3 },
				{ _420_LittleWings.Toad_Lord_Back_Skin, 10 } };
		_420_LittleWings.Delux_Fairy_Stone_Items = new short[][] {
				{ _420_LittleWings.Coal, 10 },
				{ _420_LittleWings.Charcoal, 10 },
				{ _420_LittleWings.GemstoneC, 1 },
				{ _420_LittleWings.Stone_of_Purity, 1 },
				{ _420_LittleWings.Silver_Nugget, 5 },
				{ _420_LittleWings.Toad_Lord_Back_Skin, 20 } };
		wyrms = new int[][] {
				{ 20580, 30748, _420_LittleWings.Scale_of_Drake_Exarion, _420_LittleWings.Egg_of_Drake_Exarion },
				{ 20233, 30749, _420_LittleWings.Scale_of_Drake_Zwov, _420_LittleWings.Egg_of_Drake_Zwov },
				{ 20551, 30750, _420_LittleWings.Scale_of_Drake_Kalibran, _420_LittleWings.Egg_of_Drake_Kalibran },
				{ 20270, 30751, _420_LittleWings.Scale_of_Wyvern_Suzet, _420_LittleWings.Egg_of_Wyvern_Suzet },
				{ 20202, 30752, _420_LittleWings.Scale_of_Wyvern_Shamhai, _420_LittleWings.Egg_of_Wyvern_Shamhai } };
	}
}
