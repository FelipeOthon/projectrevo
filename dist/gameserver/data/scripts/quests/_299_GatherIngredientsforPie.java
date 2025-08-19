package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _299_GatherIngredientsforPie extends Quest implements ScriptFile
{
	private static int Emily;
	private static int Lara;
	private static int Bright;
	private static int Wasp_Worker;
	private static int Wasp_Leader;
	private static int Varnish;
	private static short Fruit_Basket;
	private static short Avellan_Spice;
	private static short Honey_Pouch;
	private static int Wasp_Worker_Chance;
	private static int Wasp_Leader_Chance;
	private static int Reward_Varnish_Chance;

	public _299_GatherIngredientsforPie()
	{
		super(false);
		this.addStartNpc(_299_GatherIngredientsforPie.Emily);
		this.addTalkId(new int[] { _299_GatherIngredientsforPie.Lara });
		this.addTalkId(new int[] { _299_GatherIngredientsforPie.Bright });
		this.addKillId(new int[] { _299_GatherIngredientsforPie.Wasp_Worker });
		this.addKillId(new int[] { _299_GatherIngredientsforPie.Wasp_Leader });
		addQuestItem(new int[] { _299_GatherIngredientsforPie.Fruit_Basket });
		addQuestItem(new int[] { _299_GatherIngredientsforPie.Avellan_Spice });
		addQuestItem(new int[] { _299_GatherIngredientsforPie.Honey_Pouch });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int _state = st.getState();
		final int cond = st.getInt("cond");
		if(event.equalsIgnoreCase("emilly_q0299_0104.htm") && _state == 1)
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("emilly_q0299_0201.htm") && _state == 2)
		{
			if(st.getQuestItemsCount(_299_GatherIngredientsforPie.Honey_Pouch) < 100L)
				return "emilly_q0299_0202.htm";
			st.takeItems(_299_GatherIngredientsforPie.Honey_Pouch, -1L);
			st.set("cond", "3");
		}
		else if(event.equalsIgnoreCase("lars_q0299_0301.htm") && _state == 2 && cond == 3)
		{
			st.giveItems(_299_GatherIngredientsforPie.Avellan_Spice, 1L);
			st.set("cond", "4");
		}
		else if(event.equalsIgnoreCase("emilly_q0299_0401.htm") && _state == 2)
		{
			if(st.getQuestItemsCount(_299_GatherIngredientsforPie.Avellan_Spice) < 1L)
				return "emilly_q0299_0402.htm";
			st.takeItems(_299_GatherIngredientsforPie.Avellan_Spice, -1L);
			st.set("cond", "5");
		}
		else if(event.equalsIgnoreCase("guard_bright_q0299_0501.htm") && _state == 2 && cond == 5)
		{
			st.giveItems(_299_GatherIngredientsforPie.Fruit_Basket, 1L);
			st.set("cond", "6");
		}
		else if(event.equalsIgnoreCase("emilly_q0299_0601.htm") && _state == 2)
		{
			if(st.getQuestItemsCount(_299_GatherIngredientsforPie.Fruit_Basket) < 1L)
				return "emilly_q0299_0602.htm";
			st.takeItems(_299_GatherIngredientsforPie.Fruit_Basket, -1L);
			if(Rnd.chance(_299_GatherIngredientsforPie.Reward_Varnish_Chance))
				st.giveItems(_299_GatherIngredientsforPie.Varnish, 50L, true);
			else
				st.giveItems(57, 25000L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
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
			if(npcId != _299_GatherIngredientsforPie.Emily)
				return "noquest";
			if(st.getPlayer().getLevel() >= 34)
			{
				st.set("cond", "0");
				return "emilly_q0299_0101.htm";
			}
			st.exitCurrentQuest(true);
			return "emilly_q0299_0102.htm";
		}
		else
		{
			final int cond = st.getInt("cond");
			if(npcId == _299_GatherIngredientsforPie.Emily && _state == 2)
			{
				if(cond == 1 && st.getQuestItemsCount(_299_GatherIngredientsforPie.Honey_Pouch) <= 99L)
					return "emilly_q0299_0106.htm";
				if(cond == 2 && st.getQuestItemsCount(_299_GatherIngredientsforPie.Honey_Pouch) >= 100L)
					return "emilly_q0299_0105.htm";
				if(cond == 3 && st.getQuestItemsCount(_299_GatherIngredientsforPie.Avellan_Spice) == 0L)
					return "emilly_q0299_0203.htm";
				if(cond == 4 && st.getQuestItemsCount(_299_GatherIngredientsforPie.Avellan_Spice) == 1L)
					return "emilly_q0299_0301.htm";
				if(cond == 5 && st.getQuestItemsCount(_299_GatherIngredientsforPie.Fruit_Basket) == 0L)
					return "emilly_q0299_0403.htm";
				if(cond == 6 && st.getQuestItemsCount(_299_GatherIngredientsforPie.Fruit_Basket) == 1L)
					return "emilly_q0299_0501.htm";
			}
			if(npcId == _299_GatherIngredientsforPie.Lara && _state == 2 && cond == 3)
				return "lars_q0299_0201.htm";
			if(npcId == _299_GatherIngredientsforPie.Lara && _state == 2 && cond == 4)
				return "lars_q0299_0302.htm";
			if(npcId == _299_GatherIngredientsforPie.Bright && _state == 2 && cond == 5)
				return "guard_bright_q0299_0401.htm";
			if(npcId == _299_GatherIngredientsforPie.Bright && _state == 2 && cond == 5)
				return "guard_bright_q0299_0502.htm";
			return "noquest";
		}
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState qs)
	{
		if(qs.getState() != 2 || qs.getInt("cond") != 1 || qs.getQuestItemsCount(_299_GatherIngredientsforPie.Honey_Pouch) >= 100L)
			return null;
		final int npcId = npc.getNpcId();
		if(npcId == _299_GatherIngredientsforPie.Wasp_Worker && Rnd.chance(_299_GatherIngredientsforPie.Wasp_Worker_Chance) || npcId == _299_GatherIngredientsforPie.Wasp_Leader && Rnd.chance(_299_GatherIngredientsforPie.Wasp_Leader_Chance))
		{
			qs.giveItems(_299_GatherIngredientsforPie.Honey_Pouch, 1L);
			if(qs.getQuestItemsCount(_299_GatherIngredientsforPie.Honey_Pouch) < 100L)
				qs.playSound(Quest.SOUND_ITEMGET);
			else
			{
				qs.set("cond", "2");
				qs.playSound(Quest.SOUND_MIDDLE);
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
		_299_GatherIngredientsforPie.Emily = 30620;
		_299_GatherIngredientsforPie.Lara = 30063;
		_299_GatherIngredientsforPie.Bright = 30466;
		_299_GatherIngredientsforPie.Wasp_Worker = 20934;
		_299_GatherIngredientsforPie.Wasp_Leader = 20935;
		_299_GatherIngredientsforPie.Varnish = 1865;
		_299_GatherIngredientsforPie.Fruit_Basket = 7136;
		_299_GatherIngredientsforPie.Avellan_Spice = 7137;
		_299_GatherIngredientsforPie.Honey_Pouch = 7138;
		_299_GatherIngredientsforPie.Wasp_Worker_Chance = 55;
		_299_GatherIngredientsforPie.Wasp_Leader_Chance = 70;
		_299_GatherIngredientsforPie.Reward_Varnish_Chance = 50;
	}
}
