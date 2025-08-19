package quests;

import java.util.HashMap;
import java.util.Map;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Drop;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _340_SubjugationofLizardmen extends Quest implements ScriptFile
{
	private static int WEITSZ;
	private static int LEVIAN;
	private static int ADONIUS;
	private static int CHEST_OF_BIFRONS;
	private static int LANGK_LIZARDMAN;
	private static int LANGK_LIZARDMAN_SCOUT;
	private static int LANGK_LIZARDMAN_WARRIOR;
	private static int LANGK_LIZARDMAN_SHAMAN;
	private static int LANGK_LIZARDMAN_LEADER;
	private static int LANGK_LIZARDMAN_SENTINEL;
	private static int LANGK_LIZARDMAN_LIEUTENANT;
	private static int SERPENT_DEMON_BIFRONS;
	private static short ROSARY;
	private static short HOLY_SYMBOL;
	private static short TRADE_CARGO;
	private static short EVIL_SPIRIT_OF_DARKNESS;
	private static Map<Integer, Drop> DROPLIST;

	public _340_SubjugationofLizardmen()
	{
		super(false);
		this.addStartNpc(_340_SubjugationofLizardmen.WEITSZ);
		this.addTalkId(new int[] { _340_SubjugationofLizardmen.LEVIAN });
		this.addTalkId(new int[] { _340_SubjugationofLizardmen.ADONIUS });
		this.addTalkId(new int[] { _340_SubjugationofLizardmen.CHEST_OF_BIFRONS });
		_340_SubjugationofLizardmen.DROPLIST.put(_340_SubjugationofLizardmen.LANGK_LIZARDMAN, new Drop(1, 30, 30).addItem(_340_SubjugationofLizardmen.TRADE_CARGO));
		_340_SubjugationofLizardmen.DROPLIST.put(_340_SubjugationofLizardmen.LANGK_LIZARDMAN_SCOUT, new Drop(1, 30, 33).addItem(_340_SubjugationofLizardmen.TRADE_CARGO));
		_340_SubjugationofLizardmen.DROPLIST.put(_340_SubjugationofLizardmen.LANGK_LIZARDMAN_WARRIOR, new Drop(1, 30, 36).addItem(_340_SubjugationofLizardmen.TRADE_CARGO));
		_340_SubjugationofLizardmen.DROPLIST.put(_340_SubjugationofLizardmen.LANGK_LIZARDMAN_SHAMAN, new Drop(3, 1, 12).addItem(_340_SubjugationofLizardmen.HOLY_SYMBOL).addItem(_340_SubjugationofLizardmen.ROSARY));
		_340_SubjugationofLizardmen.DROPLIST.put(_340_SubjugationofLizardmen.LANGK_LIZARDMAN_LEADER, new Drop(3, 1, 12).addItem(_340_SubjugationofLizardmen.HOLY_SYMBOL).addItem(_340_SubjugationofLizardmen.ROSARY));
		_340_SubjugationofLizardmen.DROPLIST.put(_340_SubjugationofLizardmen.LANGK_LIZARDMAN_SENTINEL, new Drop(3, 1, 12).addItem(_340_SubjugationofLizardmen.HOLY_SYMBOL).addItem(_340_SubjugationofLizardmen.ROSARY));
		_340_SubjugationofLizardmen.DROPLIST.put(_340_SubjugationofLizardmen.LANGK_LIZARDMAN_LIEUTENANT, new Drop(3, 1, 12).addItem(_340_SubjugationofLizardmen.HOLY_SYMBOL).addItem(_340_SubjugationofLizardmen.ROSARY));
		this.addKillId(new int[] { _340_SubjugationofLizardmen.SERPENT_DEMON_BIFRONS });
		for(final int kill_id : _340_SubjugationofLizardmen.DROPLIST.keySet())
			this.addKillId(new int[] { kill_id });
		addQuestItem(new int[] { _340_SubjugationofLizardmen.TRADE_CARGO });
		addQuestItem(new int[] { _340_SubjugationofLizardmen.HOLY_SYMBOL });
		addQuestItem(new int[] { _340_SubjugationofLizardmen.ROSARY });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int _state = st.getState();
		final int cond = st.getInt("cond");
		if(event.equalsIgnoreCase("30385-4.htm") && _state == 1)
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30385-6.htm") && _state == 2 && cond == 1 && st.getQuestItemsCount(_340_SubjugationofLizardmen.TRADE_CARGO) >= 30L)
		{
			st.set("cond", "2");
			st.takeItems(_340_SubjugationofLizardmen.TRADE_CARGO, -1L);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("30375-2.htm") && _state == 2 && cond == 2)
		{
			st.set("cond", "3");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("30989-2.htm") && _state == 2 && cond == 5)
		{
			st.set("cond", "6");
			st.giveItems(_340_SubjugationofLizardmen.EVIL_SPIRIT_OF_DARKNESS, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("30037-4.htm") && _state == 2 && cond == 6 && st.getQuestItemsCount(_340_SubjugationofLizardmen.EVIL_SPIRIT_OF_DARKNESS) > 0L)
		{
			st.set("cond", "7");
			st.takeItems(_340_SubjugationofLizardmen.EVIL_SPIRIT_OF_DARKNESS, -1L);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("30385-10.htm") && _state == 2 && cond == 7)
		{
			st.giveItems(57, 14700L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		else if(event.equalsIgnoreCase("30385-7.htm") && _state == 2 && cond == 1 && st.getQuestItemsCount(_340_SubjugationofLizardmen.TRADE_CARGO) >= 30L)
		{
			st.takeItems(_340_SubjugationofLizardmen.TRADE_CARGO, -1L);
			st.giveItems(57, 4090L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(false);
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
			if(npcId != _340_SubjugationofLizardmen.WEITSZ)
				return "noquest";
			if(st.getPlayer().getLevel() < 17)
			{
				st.exitCurrentQuest(true);
				return "30385-1.htm";
			}
			st.set("cond", "0");
			return "30385-2.htm";
		}
		else
		{
			if(_state != 2)
				return "noquest";
			final int cond = st.getInt("cond");
			if(npcId == _340_SubjugationofLizardmen.WEITSZ && cond == 1)
				return st.getQuestItemsCount(_340_SubjugationofLizardmen.TRADE_CARGO) < 30L ? "30385-8.htm" : "30385-5.htm";
			if(npcId == _340_SubjugationofLizardmen.WEITSZ && cond == 2)
				return "30385-11.htm";
			if(npcId == _340_SubjugationofLizardmen.WEITSZ && cond == 7)
				return "30385-9.htm";
			if(npcId == _340_SubjugationofLizardmen.ADONIUS && cond == 2)
				return "30375-1.htm";
			if(npcId == _340_SubjugationofLizardmen.ADONIUS && cond == 3)
			{
				if(st.getQuestItemsCount(_340_SubjugationofLizardmen.ROSARY) == 0L || st.getQuestItemsCount(_340_SubjugationofLizardmen.HOLY_SYMBOL) == 0L)
					return "30375-4.htm";
				st.takeItems(_340_SubjugationofLizardmen.ROSARY, -1L);
				st.takeItems(_340_SubjugationofLizardmen.HOLY_SYMBOL, -1L);
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "4");
				return "30375-3.htm";
			}
			else
			{
				if(npcId == _340_SubjugationofLizardmen.ADONIUS && cond == 4)
					return "30375-5.htm";
				if(npcId == _340_SubjugationofLizardmen.LEVIAN && cond == 4)
				{
					st.set("cond", "5");
					st.playSound(Quest.SOUND_MIDDLE);
					return "30037-1.htm";
				}
				if(npcId == _340_SubjugationofLizardmen.LEVIAN && cond == 5)
					return "30037-2.htm";
				if(npcId == _340_SubjugationofLizardmen.LEVIAN && cond == 6 && st.getQuestItemsCount(_340_SubjugationofLizardmen.EVIL_SPIRIT_OF_DARKNESS) > 0L)
					return "30037-3.htm";
				if(npcId == _340_SubjugationofLizardmen.LEVIAN && cond == 7)
					return "30037-5.htm";
				if(npcId == _340_SubjugationofLizardmen.CHEST_OF_BIFRONS && cond == 5)
					return "30989-1.htm";
				return "noquest";
			}
		}
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState qs)
	{
		if(qs.getState() != 2)
			return null;
		final int npcId = npc.getNpcId();
		if(npcId == _340_SubjugationofLizardmen.SERPENT_DEMON_BIFRONS)
		{
			qs.addSpawn(_340_SubjugationofLizardmen.CHEST_OF_BIFRONS);
			return null;
		}
		final Drop _drop = _340_SubjugationofLizardmen.DROPLIST.get(npcId);
		if(_drop == null)
			return null;
		final int cond = qs.getInt("cond");
		for(final short item_id : _drop.itemList)
		{
			final long _count = qs.getQuestItemsCount(item_id);
			if(cond == _drop.condition && _count < _drop.maxcount && Rnd.chance(_drop.chance))
			{
				qs.giveItems(item_id, 1L);
				if(_count + 1L == _drop.maxcount)
					qs.playSound(Quest.SOUND_MIDDLE);
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
		_340_SubjugationofLizardmen.WEITSZ = 30385;
		_340_SubjugationofLizardmen.LEVIAN = 30037;
		_340_SubjugationofLizardmen.ADONIUS = 30375;
		_340_SubjugationofLizardmen.CHEST_OF_BIFRONS = 30989;
		_340_SubjugationofLizardmen.LANGK_LIZARDMAN = 20008;
		_340_SubjugationofLizardmen.LANGK_LIZARDMAN_SCOUT = 20010;
		_340_SubjugationofLizardmen.LANGK_LIZARDMAN_WARRIOR = 20014;
		_340_SubjugationofLizardmen.LANGK_LIZARDMAN_SHAMAN = 21101;
		_340_SubjugationofLizardmen.LANGK_LIZARDMAN_LEADER = 20356;
		_340_SubjugationofLizardmen.LANGK_LIZARDMAN_SENTINEL = 21100;
		_340_SubjugationofLizardmen.LANGK_LIZARDMAN_LIEUTENANT = 20357;
		_340_SubjugationofLizardmen.SERPENT_DEMON_BIFRONS = 25146;
		_340_SubjugationofLizardmen.ROSARY = 4257;
		_340_SubjugationofLizardmen.HOLY_SYMBOL = 4256;
		_340_SubjugationofLizardmen.TRADE_CARGO = 4255;
		_340_SubjugationofLizardmen.EVIL_SPIRIT_OF_DARKNESS = 7190;
		_340_SubjugationofLizardmen.DROPLIST = new HashMap<Integer, Drop>();
	}
}
