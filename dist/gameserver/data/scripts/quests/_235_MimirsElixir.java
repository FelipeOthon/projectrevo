package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _235_MimirsElixir extends Quest implements ScriptFile
{
	private static final int chance = 45;
	private static final int STAR_OF_DESTINY = 5011;
	private static final int MINLEVEL = 75;
	private static final int PURE_SILVER = 6320;
	private static final int TRUE_GOLD = 6321;
	private static final int SAGES_STONE = 6322;
	private static final int BLOOD_FIRE = 6318;
	private static final int MIMIRS_ELIXIR = 6319;
	private static final int SCROLL_ENCHANT_WEAPON_A = 729;
	private static final String _default = "noquest";
	private static final int LADD = 30721;
	private static final int JOAN = 30718;
	private static final int Chimera_Piece = 20965;
	private static final int Bloody_Guardian = 21090;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _235_MimirsElixir()
	{
		super(false);
		this.addStartNpc(30721);
		this.addTalkId(new int[] { 30718 });
		this.addTalkId(new int[] { 30721 });
		this.addKillId(new int[] { 20965 });
		this.addKillId(new int[] { 21090 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("1"))
		{
			st.takeItems(5011, -1L);
			st.setState(2);
			st.set("cond", "1");
			htmltext = "30721-02a.htm";
		}
		else if(event.equalsIgnoreCase("30718_1"))
		{
			st.set("cond", "3");
			htmltext = "30718-01a.htm";
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int id = st.getState();
		if(id == 3)
			return "completed";
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 30721)
		{
			if(id == 1)
			{
				st.set("cond", "0");
				if(st.getPlayer().getLevel() < 75)
				{
					st.exitCurrentQuest(true);
					htmltext = "30721-01.htm";
				}
				else if(st.getPlayer().getQuestState(234) != null && st.getPlayer().getQuestState(234).isCompleted() || st.getQuestItemsCount(5011) > 0L)
				{
					if(st.getQuestItemsCount(5011) > 0L)
					{
						st.takeItems(5011, 1L);
						htmltext = "30721-02.htm";
					}
					else
						htmltext = "30721-02.htm";
				}
				else
				{
					st.exitCurrentQuest(true);
					htmltext = "30721-01a.htm";
				}
			}
			else if(cond == 1 && st.getQuestItemsCount(6320) < 1L)
				htmltext = "30721-03.htm";
			else if(cond == 1 && st.getQuestItemsCount(6320) > 0L)
			{
				st.set("cond", "2");
				htmltext = "30721-04.htm";
			}
			else if(1 < cond && cond < 5)
				htmltext = "30721-05.htm";
			else if(cond == 5)
			{
				st.set("cond", "6");
				htmltext = "30721-06.htm";
			}
			else if(cond == 6)
				htmltext = "30721-07.htm";
			else if(cond == 7 && st.getQuestItemsCount(6320) > 0L && st.getQuestItemsCount(6321) > 0L)
				htmltext = "30721-08.htm";
			else if(cond == 7)
			{
				htmltext = "30721-09.htm";
				st.set("cond", "3");
			}
			else if(cond == 8)
			{
				htmltext = "30721-10.htm";
				st.takeItems(6319, -1L);
				st.giveItems(729, 1L, Config.RATE_QUESTS_OCCUPATION_CHANGE);
				st.unset("cond");
				st.exitCurrentQuest(false);
			}
		}
		else if(npcId == 30718)
			if(cond == 2)
				htmltext = "30718-01.htm";
			else if(cond == 3)
				htmltext = "30718-02.htm";
			else if(cond == 4)
			{
				st.takeItems(6322, -1L);
				st.giveItems(6321, 1L);
				st.set("cond", "5");
				htmltext = "30718-03.htm";
			}
			else if(cond >= 5)
				htmltext = "30718-04.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 20965 && cond == 3 && st.getQuestItemsCount(6322) == 0L && Rnd.chance(45))
		{
			st.giveItems(6322, 1L);
			st.playSound(Quest.SOUND_ITEMGET);
			st.set("cond", str(cond + 1));
		}
		if(npcId == 21090 && cond == 6 && st.getQuestItemsCount(6318) == 0L && Rnd.chance(45))
		{
			st.giveItems(6318, 1L);
			st.playSound(Quest.SOUND_ITEMGET);
			st.set("cond", str(cond + 1));
		}
		return null;
	}
}
