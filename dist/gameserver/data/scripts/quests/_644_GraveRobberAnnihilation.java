package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _644_GraveRobberAnnihilation extends Quest implements ScriptFile
{
	private static final int KARUDA = 32017;
	private static int ORC_GOODS;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _644_GraveRobberAnnihilation()
	{
		super(true);
		this.addStartNpc(32017);
		this.addKillId(new int[] { 22003 });
		this.addKillId(new int[] { 22004 });
		this.addKillId(new int[] { 22005 });
		this.addKillId(new int[] { 22006 });
		this.addKillId(new int[] { 22008 });
		addQuestItem(new int[] { _644_GraveRobberAnnihilation.ORC_GOODS });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("karuda_q0644_0103.htm"))
		{
			st.takeItems(_644_GraveRobberAnnihilation.ORC_GOODS, -1L);
			if(st.getPlayer().getLevel() < 20)
			{
				htmltext = "karuda_q0644_0102.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				st.set("cond", "1");
				st.setState(2);
				st.playSound(Quest.SOUND_ACCEPT);
			}
		}
		if(st.getInt("cond") == 2 && st.getQuestItemsCount(_644_GraveRobberAnnihilation.ORC_GOODS) >= 120L)
		{
			if(event.equalsIgnoreCase("varn"))
			{
				st.takeItems(_644_GraveRobberAnnihilation.ORC_GOODS, -1L);
				st.giveItems(1865, 30L, true);
				htmltext = null;
			}
			else if(event.equalsIgnoreCase("an_s"))
			{
				st.takeItems(_644_GraveRobberAnnihilation.ORC_GOODS, -1L);
				st.giveItems(1867, 40L, true);
				htmltext = null;
			}
			else if(event.equalsIgnoreCase("an_b"))
			{
				st.takeItems(_644_GraveRobberAnnihilation.ORC_GOODS, -1L);
				st.giveItems(1872, 40L, true);
				htmltext = null;
			}
			else if(event.equalsIgnoreCase("char"))
			{
				st.takeItems(_644_GraveRobberAnnihilation.ORC_GOODS, -1L);
				st.giveItems(1871, 30L, true);
				htmltext = null;
			}
			else if(event.equalsIgnoreCase("coal"))
			{
				st.takeItems(_644_GraveRobberAnnihilation.ORC_GOODS, -1L);
				st.giveItems(1870, 30L, true);
				htmltext = null;
			}
			else if(event.equalsIgnoreCase("i_o"))
			{
				st.takeItems(_644_GraveRobberAnnihilation.ORC_GOODS, -1L);
				st.giveItems(1869, 30L, true);
				htmltext = null;
			}
			if(htmltext == null)
			{
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(cond == 0)
			htmltext = "karuda_q0644_0101.htm";
		else if(cond == 1)
			htmltext = "karuda_q0644_0106.htm";
		else if(cond == 2)
			if(st.getQuestItemsCount(_644_GraveRobberAnnihilation.ORC_GOODS) >= 120L)
				htmltext = "karuda_q0644_0105.htm";
			else
			{
				st.set("cond", "1");
				htmltext = "karuda_q0644_0106.htm";
			}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getInt("cond") == 1 && Rnd.chance(90))
		{
			st.giveItems(_644_GraveRobberAnnihilation.ORC_GOODS, (long) st.getRateQuestsDrop(false));
			if(st.getQuestItemsCount(_644_GraveRobberAnnihilation.ORC_GOODS) >= 120L)
			{
				st.set("cond", "2");
				st.setState(2);
			}
		}
		return null;
	}

	static
	{
		_644_GraveRobberAnnihilation.ORC_GOODS = 8088;
	}
}
