package quests;

import java.util.HashMap;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.MultiSellHolder;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _382_KailsMagicCoin extends Quest implements ScriptFile
{
	private static int ROYAL_MEMBERSHIP;
	private static int VERGARA;
	private static final HashMap<Integer, int[]> MOBS;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _382_KailsMagicCoin()
	{
		super(false);
		this.addStartNpc(_382_KailsMagicCoin.VERGARA);
		for(final int mobId : _382_KailsMagicCoin.MOBS.keySet())
			this.addKillId(new int[] { mobId });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("head_blacksmith_vergara_q0382_03.htm"))
		{
			if(st.getPlayer().getLevel() >= 55 && st.getQuestItemsCount(_382_KailsMagicCoin.ROYAL_MEMBERSHIP) > 0L)
			{
				st.set("cond", "1");
				st.setState(2);
				st.playSound(Quest.SOUND_ACCEPT);
			}
			else
			{
				htmltext = "head_blacksmith_vergara_q0382_01.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(event.equalsIgnoreCase("list"))
		{
			st.getPlayer().setLastNpcId(npc.getNpcId());
			MultiSellHolder.getInstance().SeparateAndSend(Config.ALT_100_RECIPES_A ? 383 : 382, st.getPlayer(), 0.0);
			htmltext = null;
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(st.getQuestItemsCount(_382_KailsMagicCoin.ROYAL_MEMBERSHIP) == 0L || st.getPlayer().getLevel() < 55)
		{
			htmltext = "head_blacksmith_vergara_q0382_01.htm";
			st.exitCurrentQuest(true);
		}
		else if(cond == 0)
			htmltext = "head_blacksmith_vergara_q0382_02.htm";
		else
			htmltext = "head_blacksmith_vergara_q0382_04.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getState() != 2 || st.getQuestItemsCount(_382_KailsMagicCoin.ROYAL_MEMBERSHIP) == 0L)
			return null;
		final int[] droplist = _382_KailsMagicCoin.MOBS.get(npc.getNpcId());
		st.rollAndGive(droplist[Rnd.get(droplist.length)], 1, 10.0);
		return null;
	}

	static
	{
		_382_KailsMagicCoin.ROYAL_MEMBERSHIP = 5898;
		_382_KailsMagicCoin.VERGARA = 30687;
		(MOBS = new HashMap<Integer, int[]>()).put(21017, new int[] { 5961 });
		_382_KailsMagicCoin.MOBS.put(21019, new int[] { 5962 });
		_382_KailsMagicCoin.MOBS.put(21020, new int[] { 5963 });
		_382_KailsMagicCoin.MOBS.put(21022, new int[] { 5961, 5962, 5963 });
	}
}
