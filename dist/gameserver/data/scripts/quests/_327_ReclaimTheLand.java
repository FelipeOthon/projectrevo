package quests;

import java.util.HashMap;
import java.util.Map;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Drop;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _327_ReclaimTheLand extends Quest implements ScriptFile
{
	private static int Piotur;
	private static int Iris;
	private static int Asha;
	private static short TUREK_DOGTAG;
	private static short TUREK_MEDALLION;
	private static short CLAY_URN_FRAGMENT;
	private static short BRASS_TRINKET_PIECE;
	private static short BRONZE_MIRROR_PIECE;
	private static short JADE_NECKLACE_BEAD;
	private static short ANCIENT_CLAY_URN;
	private static short ANCIENT_BRASS_TIARA;
	private static short ANCIENT_BRONZE_MIRROR;
	private static short ANCIENT_JADE_NECKLACE;
	private static int Exchange_Chance;
	private static Map<Integer, Drop> DROPLIST;
	private static Map<Short, Integer> EXP;

	public _327_ReclaimTheLand()
	{
		super(false);
		this.addStartNpc(_327_ReclaimTheLand.Piotur);
		this.addTalkId(new int[] { _327_ReclaimTheLand.Iris, _327_ReclaimTheLand.Asha });
		_327_ReclaimTheLand.DROPLIST.put(20495, new Drop(1, 65535, 13).addItem(_327_ReclaimTheLand.TUREK_MEDALLION));
		_327_ReclaimTheLand.DROPLIST.put(20496, new Drop(1, 65535, 9).addItem(_327_ReclaimTheLand.TUREK_DOGTAG));
		_327_ReclaimTheLand.DROPLIST.put(20497, new Drop(1, 65535, 11).addItem(_327_ReclaimTheLand.TUREK_MEDALLION));
		_327_ReclaimTheLand.DROPLIST.put(20498, new Drop(1, 65535, 10).addItem(_327_ReclaimTheLand.TUREK_DOGTAG));
		_327_ReclaimTheLand.DROPLIST.put(20499, new Drop(1, 65535, 8).addItem(_327_ReclaimTheLand.TUREK_DOGTAG));
		_327_ReclaimTheLand.DROPLIST.put(20500, new Drop(1, 65535, 7).addItem(_327_ReclaimTheLand.TUREK_DOGTAG));
		_327_ReclaimTheLand.DROPLIST.put(20501, new Drop(1, 65535, 12).addItem(_327_ReclaimTheLand.TUREK_MEDALLION));
		_327_ReclaimTheLand.EXP.put(_327_ReclaimTheLand.ANCIENT_CLAY_URN, 913);
		_327_ReclaimTheLand.EXP.put(_327_ReclaimTheLand.ANCIENT_BRASS_TIARA, 1065);
		_327_ReclaimTheLand.EXP.put(_327_ReclaimTheLand.ANCIENT_BRONZE_MIRROR, 1065);
		_327_ReclaimTheLand.EXP.put(_327_ReclaimTheLand.ANCIENT_JADE_NECKLACE, 1294);
		for(final int kill_id : _327_ReclaimTheLand.DROPLIST.keySet())
			this.addKillId(new int[] { kill_id });
		addQuestItem(new int[] { _327_ReclaimTheLand.TUREK_MEDALLION });
		addQuestItem(new int[] { _327_ReclaimTheLand.TUREK_DOGTAG });
	}

	private static boolean ExpReward(final QuestState st, final int item_id)
	{
		Integer exp = _327_ReclaimTheLand.EXP.get(item_id);
		if(exp == null)
			exp = 182;
		final long exp_reward = st.getQuestItemsCount(item_id * exp);
		if(exp_reward == 0L)
			return false;
		st.takeItems(item_id, -1L);
		st.addExpAndSp(exp_reward, 0L);
		st.playSound(Quest.SOUND_MIDDLE);
		return true;
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int _state = st.getState();
		if(event.equalsIgnoreCase("piotur_q0327_03.htm") && _state == 1)
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("piotur_q0327_06.htm") && _state == 2)
		{
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		else if(event.equalsIgnoreCase("trader_acellopy_q0327_02.htm") && _state == 2 && st.getQuestItemsCount(_327_ReclaimTheLand.CLAY_URN_FRAGMENT) >= 5L)
		{
			st.takeItems(_327_ReclaimTheLand.CLAY_URN_FRAGMENT, 5L);
			if(!Rnd.chance(_327_ReclaimTheLand.Exchange_Chance))
				return "trader_acellopy_q0327_10.htm";
			st.giveItems(_327_ReclaimTheLand.ANCIENT_CLAY_URN, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
			return "trader_acellopy_q0327_03.htm";
		}
		else if(event.equalsIgnoreCase("trader_acellopy_q0327_04.htm") && _state == 2 && st.getQuestItemsCount(_327_ReclaimTheLand.BRASS_TRINKET_PIECE) >= 5L)
		{
			st.takeItems(_327_ReclaimTheLand.BRASS_TRINKET_PIECE, 5L);
			if(!Rnd.chance(_327_ReclaimTheLand.Exchange_Chance))
				return "trader_acellopy_q0327_10.htm";
			st.giveItems(_327_ReclaimTheLand.ANCIENT_BRASS_TIARA, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
			return "trader_acellopy_q0327_05.htm";
		}
		else if(event.equalsIgnoreCase("trader_acellopy_q0327_06.htm") && _state == 2 && st.getQuestItemsCount(_327_ReclaimTheLand.BRONZE_MIRROR_PIECE) >= 5L)
		{
			st.takeItems(_327_ReclaimTheLand.BRONZE_MIRROR_PIECE, 5L);
			if(!Rnd.chance(_327_ReclaimTheLand.Exchange_Chance))
				return "trader_acellopy_q0327_10.htm";
			st.giveItems(_327_ReclaimTheLand.ANCIENT_BRONZE_MIRROR, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
			return "trader_acellopy_q0327_07.htm";
		}
		else if(event.equalsIgnoreCase("trader_acellopy_q0327_08.htm") && _state == 2 && st.getQuestItemsCount(_327_ReclaimTheLand.JADE_NECKLACE_BEAD) >= 5L)
		{
			st.takeItems(_327_ReclaimTheLand.JADE_NECKLACE_BEAD, 5L);
			if(!Rnd.chance(_327_ReclaimTheLand.Exchange_Chance))
				return "trader_acellopy_q0327_09.htm";
			st.giveItems(_327_ReclaimTheLand.ANCIENT_JADE_NECKLACE, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
			return "trader_acellopy_q0327_07.htm";
		}
		else if(event.equalsIgnoreCase("iris_q0327_03.htm") && _state == 2)
		{
			if(!ExpReward(st, _327_ReclaimTheLand.CLAY_URN_FRAGMENT))
				return "iris_q0327_02.htm";
		}
		else if(event.equalsIgnoreCase("iris_q0327_04.htm") && _state == 2)
		{
			if(!ExpReward(st, _327_ReclaimTheLand.BRASS_TRINKET_PIECE))
				return "iris_q0327_02.htm";
		}
		else if(event.equalsIgnoreCase("iris_q0327_05.htm") && _state == 2)
		{
			if(!ExpReward(st, _327_ReclaimTheLand.BRONZE_MIRROR_PIECE))
				return "iris_q0327_02.htm";
		}
		else if(event.equalsIgnoreCase("iris_q0327_06.htm") && _state == 2)
		{
			if(!ExpReward(st, _327_ReclaimTheLand.JADE_NECKLACE_BEAD))
				return "iris_q0327_02.htm";
		}
		else if(event.equalsIgnoreCase("iris_q0327_07.htm") && _state == 2 && !ExpReward(st, _327_ReclaimTheLand.ANCIENT_CLAY_URN) && !ExpReward(st, _327_ReclaimTheLand.ANCIENT_BRASS_TIARA) && !ExpReward(st, _327_ReclaimTheLand.ANCIENT_BRONZE_MIRROR) && !ExpReward(st, _327_ReclaimTheLand.ANCIENT_JADE_NECKLACE))
			return "iris_q0327_02.htm";
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int _state = st.getState();
		final int npcId = npc.getNpcId();
		if(_state == 1)
		{
			if(npcId != _327_ReclaimTheLand.Piotur)
				return "noquest";
			if(st.getPlayer().getLevel() < 25)
			{
				st.exitCurrentQuest(true);
				return "piotur_q0327_01.htm";
			}
			st.set("cond", "0");
			return "piotur_q0327_02.htm";
		}
		else
		{
			if(_state != 2)
				return "noquest";
			if(npcId == _327_ReclaimTheLand.Piotur)
			{
				final long reward = st.getQuestItemsCount(_327_ReclaimTheLand.TUREK_DOGTAG) * 40L + st.getQuestItemsCount(_327_ReclaimTheLand.TUREK_MEDALLION) * 50L;
				if(reward == 0L)
					return "piotur_q0327_04.htm";
				st.takeItems(_327_ReclaimTheLand.TUREK_DOGTAG, -1L);
				st.takeItems(_327_ReclaimTheLand.TUREK_MEDALLION, -1L);
				st.giveItems(57, reward);
				st.playSound(Quest.SOUND_MIDDLE);
				return "piotur_q0327_05.htm";
			}
			else
			{
				if(npcId == _327_ReclaimTheLand.Iris)
					return "iris_q0327_01.htm";
				if(npcId == _327_ReclaimTheLand.Asha)
					return "trader_acellopy_q0327_01.htm";
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
		final Drop _drop = _327_ReclaimTheLand.DROPLIST.get(npcId);
		if(_drop == null)
			return null;
		if(Rnd.chance(_drop.chance))
		{
			final int n = Rnd.get(4);
			if(n == 0)
				qs.giveItems(_327_ReclaimTheLand.CLAY_URN_FRAGMENT, 1L);
			else if(n == 1)
				qs.giveItems(_327_ReclaimTheLand.BRASS_TRINKET_PIECE, 1L);
			else if(n == 2)
				qs.giveItems(_327_ReclaimTheLand.BRONZE_MIRROR_PIECE, 1L);
			else
				qs.giveItems(_327_ReclaimTheLand.JADE_NECKLACE_BEAD, 1L);
		}
		qs.giveItems(_drop.itemList.get(0), 1L);
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
		_327_ReclaimTheLand.Piotur = 30597;
		_327_ReclaimTheLand.Iris = 30034;
		_327_ReclaimTheLand.Asha = 30313;
		_327_ReclaimTheLand.TUREK_DOGTAG = 1846;
		_327_ReclaimTheLand.TUREK_MEDALLION = 1847;
		_327_ReclaimTheLand.CLAY_URN_FRAGMENT = 1848;
		_327_ReclaimTheLand.BRASS_TRINKET_PIECE = 1849;
		_327_ReclaimTheLand.BRONZE_MIRROR_PIECE = 1850;
		_327_ReclaimTheLand.JADE_NECKLACE_BEAD = 1851;
		_327_ReclaimTheLand.ANCIENT_CLAY_URN = 1852;
		_327_ReclaimTheLand.ANCIENT_BRASS_TIARA = 1853;
		_327_ReclaimTheLand.ANCIENT_BRONZE_MIRROR = 1854;
		_327_ReclaimTheLand.ANCIENT_JADE_NECKLACE = 1855;
		_327_ReclaimTheLand.Exchange_Chance = 80;
		_327_ReclaimTheLand.DROPLIST = new HashMap<Integer, Drop>();
		_327_ReclaimTheLand.EXP = new HashMap<Short, Integer>();
	}
}
