package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _306_CrystalOfFireice extends Quest implements ScriptFile
{
	private static int Katerina;
	private static int Salamander;
	private static int Undine;
	private static int Salamander_Elder;
	private static int Undine_Elder;
	private static int Salamander_Noble;
	private static int Undine_Noble;
	private static int Flame_Shard;
	private static int Ice_Shard;
	private static int Chance;
	private static int Elder_Chance;
	private static int Noble_Chance;

	public _306_CrystalOfFireice()
	{
		super(false);
		this.addStartNpc(_306_CrystalOfFireice.Katerina);
		this.addKillId(new int[] { _306_CrystalOfFireice.Salamander });
		this.addKillId(new int[] { _306_CrystalOfFireice.Undine });
		this.addKillId(new int[] { _306_CrystalOfFireice.Salamander_Elder });
		this.addKillId(new int[] { _306_CrystalOfFireice.Undine_Elder });
		this.addKillId(new int[] { _306_CrystalOfFireice.Salamander_Noble });
		this.addKillId(new int[] { _306_CrystalOfFireice.Undine_Noble });
		addQuestItem(new int[] { _306_CrystalOfFireice.Flame_Shard });
		addQuestItem(new int[] { _306_CrystalOfFireice.Ice_Shard });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int _state = st.getState();
		if(event.equalsIgnoreCase("katrine_q0306_04.htm") && _state == 1)
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("katrine_q0306_08.htm") && _state == 2)
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
		if(npc.getNpcId() != _306_CrystalOfFireice.Katerina)
			return htmltext;
		final int _state = st.getState();
		if(_state == 1)
		{
			if(st.getPlayer().getLevel() < 17)
			{
				htmltext = "katrine_q0306_02.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "katrine_q0306_03.htm";
				st.set("cond", "0");
			}
		}
		else if(_state == 2)
		{
			final long Shrads_count = st.getQuestItemsCount(_306_CrystalOfFireice.Flame_Shard) + st.getQuestItemsCount(_306_CrystalOfFireice.Ice_Shard);
			final long Reward = Shrads_count * 30L + (Shrads_count >= 10L ? 5000 : 0);
			if(Reward > 0L)
			{
				htmltext = "katrine_q0306_07.htm";
				st.takeItems(_306_CrystalOfFireice.Flame_Shard, -1L);
				st.takeItems(_306_CrystalOfFireice.Ice_Shard, -1L);
				st.giveItems(57, Reward);
			}
			else
				htmltext = "katrine_q0306_05.htm";
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState qs)
	{
		if(qs.getState() != 2)
			return null;
		final int npcId = npc.getNpcId();
		if((npcId == _306_CrystalOfFireice.Salamander || npcId == _306_CrystalOfFireice.Undine) && !Rnd.chance(_306_CrystalOfFireice.Chance))
			return null;
		if((npcId == _306_CrystalOfFireice.Salamander_Elder || npcId == _306_CrystalOfFireice.Undine_Elder) && !Rnd.chance(_306_CrystalOfFireice.Elder_Chance))
			return null;
		if((npcId == _306_CrystalOfFireice.Salamander_Noble || npcId == _306_CrystalOfFireice.Undine_Noble) && !Rnd.chance(_306_CrystalOfFireice.Noble_Chance))
			return null;
		qs.giveItems(npcId == _306_CrystalOfFireice.Salamander || npcId == _306_CrystalOfFireice.Salamander_Elder || npcId == _306_CrystalOfFireice.Salamander_Noble ? _306_CrystalOfFireice.Flame_Shard : _306_CrystalOfFireice.Ice_Shard, 1L);
		qs.playSound(Quest.SOUND_ITEMGET);
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
		_306_CrystalOfFireice.Katerina = 30004;
		_306_CrystalOfFireice.Salamander = 20109;
		_306_CrystalOfFireice.Undine = 20110;
		_306_CrystalOfFireice.Salamander_Elder = 20112;
		_306_CrystalOfFireice.Undine_Elder = 20113;
		_306_CrystalOfFireice.Salamander_Noble = 20114;
		_306_CrystalOfFireice.Undine_Noble = 20115;
		_306_CrystalOfFireice.Flame_Shard = 1020;
		_306_CrystalOfFireice.Ice_Shard = 1021;
		_306_CrystalOfFireice.Chance = 30;
		_306_CrystalOfFireice.Elder_Chance = 40;
		_306_CrystalOfFireice.Noble_Chance = 50;
	}
}
