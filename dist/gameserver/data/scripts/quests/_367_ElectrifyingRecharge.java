package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.SkillTable;

public class _367_ElectrifyingRecharge extends Quest implements ScriptFile
{
	private static int LORAIN;
	private static int CATHEROK;
	private static int Titan_Lamp_First;
	private static int Titan_Lamp_Last;
	private static int Broken_Titan_Lamp;
	private static int broke_chance;
	private static int uplight_chance;

	public _367_ElectrifyingRecharge()
	{
		super(false);
		this.addStartNpc(_367_ElectrifyingRecharge.LORAIN);
		this.addKillId(new int[] { _367_ElectrifyingRecharge.CATHEROK });
		for(int Titan_Lamp_id = _367_ElectrifyingRecharge.Titan_Lamp_First; Titan_Lamp_id <= _367_ElectrifyingRecharge.Titan_Lamp_Last; ++Titan_Lamp_id)
			addQuestItem(new int[] { Titan_Lamp_id });
		addQuestItem(new int[] { _367_ElectrifyingRecharge.Broken_Titan_Lamp });
	}

	private static boolean takeAllLamps(final QuestState st)
	{
		boolean result = false;
		for(int Titan_Lamp_id = _367_ElectrifyingRecharge.Titan_Lamp_First; Titan_Lamp_id <= _367_ElectrifyingRecharge.Titan_Lamp_Last; ++Titan_Lamp_id)
			if(st.getQuestItemsCount(Titan_Lamp_id) > 0L)
			{
				result = true;
				st.takeItems(Titan_Lamp_id, -1L);
			}
		if(st.getQuestItemsCount(_367_ElectrifyingRecharge.Broken_Titan_Lamp) > 0L)
		{
			result = true;
			st.takeItems(_367_ElectrifyingRecharge.Broken_Titan_Lamp, -1L);
		}
		return result;
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int _state = st.getState();
		if(event.equalsIgnoreCase("30673-03.htm") && _state == 1)
		{
			takeAllLamps(st);
			st.giveItems(_367_ElectrifyingRecharge.Titan_Lamp_First, 1L);
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30673-07.htm") && _state == 2)
		{
			takeAllLamps(st);
			st.giveItems(_367_ElectrifyingRecharge.Titan_Lamp_First, 1L);
		}
		else if(event.equalsIgnoreCase("30673-08.htm") && _state == 2)
		{
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		if(npc.getNpcId() != _367_ElectrifyingRecharge.LORAIN)
			return htmltext;
		final int _state = st.getState();
		if(_state == 1)
		{
			if(st.getPlayer().getLevel() < 37)
			{
				htmltext = "30673-02.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "30673-01.htm";
				st.set("cond", "0");
			}
		}
		else if(_state == 2)
			if(st.getQuestItemsCount(_367_ElectrifyingRecharge.Titan_Lamp_Last) > 0L)
			{
				htmltext = "30673-06.htm";
				takeAllLamps(st);
				st.giveItems(4553 + Rnd.get(12), 1L);
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else if(st.getQuestItemsCount(_367_ElectrifyingRecharge.Broken_Titan_Lamp) > 0L)
			{
				htmltext = "30673-05.htm";
				takeAllLamps(st);
				st.giveItems(_367_ElectrifyingRecharge.Titan_Lamp_First, 1L);
			}
			else
				htmltext = "30673-04.htm";
		return htmltext;
	}

	@Override
	public String onAttack(final NpcInstance npc, final QuestState qs)
	{
		if(qs.getState() != 2)
			return null;
		if(qs.getQuestItemsCount(_367_ElectrifyingRecharge.Broken_Titan_Lamp) > 0L)
			return null;
		if(Rnd.chance(_367_ElectrifyingRecharge.uplight_chance))
			for(int Titan_Lamp_id = _367_ElectrifyingRecharge.Titan_Lamp_First; Titan_Lamp_id < _367_ElectrifyingRecharge.Titan_Lamp_Last; ++Titan_Lamp_id)
			{
				if(qs.getQuestItemsCount(Titan_Lamp_id) > 0L)
				{
					final int Titan_Lamp_Next = Titan_Lamp_id + 1;
					takeAllLamps(qs);
					qs.giveItems(Titan_Lamp_Next, 1L);
					if(Titan_Lamp_Next == _367_ElectrifyingRecharge.Titan_Lamp_Last)
					{
						qs.set("cond", "2");
						qs.playSound(Quest.SOUND_MIDDLE);
					}
					else
						qs.playSound(Quest.SOUND_ITEMGET);
					npc.doCast(SkillTable.getInstance().getInfo(4072, 4), qs.getPlayer(), true);
					return null;
				}
				if(Rnd.chance(_367_ElectrifyingRecharge.broke_chance) && takeAllLamps(qs))
					qs.giveItems(_367_ElectrifyingRecharge.Broken_Titan_Lamp, 1L);
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
		_367_ElectrifyingRecharge.LORAIN = 30673;
		_367_ElectrifyingRecharge.CATHEROK = 21035;
		_367_ElectrifyingRecharge.Titan_Lamp_First = 5875;
		_367_ElectrifyingRecharge.Titan_Lamp_Last = 5879;
		_367_ElectrifyingRecharge.Broken_Titan_Lamp = 5880;
		_367_ElectrifyingRecharge.broke_chance = 3;
		_367_ElectrifyingRecharge.uplight_chance = 7;
	}
}
