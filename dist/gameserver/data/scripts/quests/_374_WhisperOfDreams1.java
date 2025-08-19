package quests;

import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.Util;

public class _374_WhisperOfDreams1 extends Quest implements ScriptFile
{
	private static final double DROP_CHANCE_SEALD_MSTONE = 0.4;
	private static final int DROP_CHANCE_ITEMS = 20;
	private static final String[][] SHOP_LIST;
	private static final int[][] SHOP_PRICES;
	private static final int CB_TOOTH = 5884;
	private static final int DW_LIGHT = 5885;
	private static final int SEALD_MSTONE = 5886;
	private static final int MSTONE = 5887;
	private static final String _default = "noquest";
	private static final int MANAKIA = 30515;
	private static final int TORAI = 30557;
	private static final int CB = 20620;
	private static final int DW = 20621;

	private String render_shop()
	{
		String html = "<html><head><body><font color=\"LEVEL\">Robe Armor Fabrics:</font><table border=0 width=300>";
		for(int i = 0; i < _374_WhisperOfDreams1.SHOP_LIST.length; ++i)
		{
			html = html + "<tr><td width=35 height=45><img src=icon." + _374_WhisperOfDreams1.SHOP_LIST[i][0] + " width=32 height=32 align=left></td><td width=365 valign=top><table border=0 width=100%>";
			html = html + "<tr><td><a action=\"bypass -h Quest _374_WhisperOfDreams1 " + _374_WhisperOfDreams1.SHOP_PRICES[i][0] + "\"><font color=\"FFFFFF\">" + _374_WhisperOfDreams1.SHOP_LIST[i][1] + " x" + _374_WhisperOfDreams1.SHOP_PRICES[i][1] + "</font></a></td></tr>";
			html = html + "<tr><td><a action=\"bypass -h Quest _374_WhisperOfDreams1 " + _374_WhisperOfDreams1.SHOP_PRICES[i][0] + "\"><font color=\"B09878\">" + (int) (_374_WhisperOfDreams1.SHOP_PRICES[i][2] * Config.getRateAdena((Player) null)) + " adena</font></a></td></tr></table></td></tr>";
		}
		html += "</table></body></html>";
		return html;
	}

	public _374_WhisperOfDreams1()
	{
		super(true);
		this.addStartNpc(30515);
		this.addTalkId(new int[] { 30557 });
		this.addKillId(new int[] { 20620, 20621 });
		addQuestItem(new int[] { 5884, 5885 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("30515-4.htm"))
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30515-5.htm"))
		{
			st.takeItems(5884, -1L);
			st.takeItems(5885, -1L);
			st.exitCurrentQuest(true);
		}
		else if(event.equalsIgnoreCase("30515-6.htm"))
		{
			if(st.getQuestItemsCount(5884) == 65L && st.getQuestItemsCount(5885) == 65L)
			{
				st.set("allow", "1");
				st.takeItems(5884, -1L);
				st.takeItems(5885, -1L);
				htmltext = "30515-7.htm";
			}
		}
		else if(event.equalsIgnoreCase("30515-8.htm"))
		{
			if(st.getQuestItemsCount(5886) > 0L && st.getInt("cond") < 2)
			{
				st.set("cond", "2");
				htmltext = "30515-9.htm";
			}
			else if(st.getInt("cond") == 2)
				htmltext = "30515-10.htm";
		}
		else if(event.equalsIgnoreCase("buy"))
			htmltext = render_shop();
		else if(Util.isNumber(event))
		{
			final int evt = Integer.parseInt(event);
			for(final int[] element : _374_WhisperOfDreams1.SHOP_PRICES)
				if(evt == element[0])
				{
					final int adena = (int) (element[2] * Config.getRateAdena((Player) null));
					st.giveItems(57, adena);
					st.giveItems(element[0], element[1]);
					st.playSound(Quest.SOUND_FINISH);
					st.exitCurrentQuest(true);
					htmltext = "30515-11.htm";
					break;
				}
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int id = st.getState();
		final int npcid = npc.getNpcId();
		if(npcid == 30515)
		{
			if(id == 1)
			{
				st.set("cond", "0");
				st.set("allow", "0");
				htmltext = "30515-1.htm";
				if(st.getPlayer().getLevel() < 56)
				{
					st.exitCurrentQuest(true);
					htmltext = "30515-2.htm";
				}
			}
			else if(st.getInt("allow") == 1)
				htmltext = "30515-3.htm";
			else
				htmltext = "30515-3a.htm";
		}
		else if(npcid == 30557 && st.getQuestItemsCount(5886) > 0L)
		{
			htmltext = "30557-1.htm";
			st.takeItems(5886, 1L);
			st.giveItems(5887, 1L);
			st.set("cond", "3");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcid = npc.getNpcId();
		if(npcid == 20620)
			st.rollAndGive(5884, 1, 1, 65, 20.0);
		else if(npcid == 20621)
			st.rollAndGive(5885, 1, 1, 65, 20.0);
		if(st.getState() == 2 && st.getQuestItemsCount(5886) < 1L)
			st.rollAndGive(5886, 1, 1, 0.4);
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
		SHOP_LIST = new String[][] {
				{ "etc_leather_yellow_i00", "Sealed Tallum Tunic Textures" },
				{ "etc_leather_gray_i00", "Sealed Dark Crystal Robe Fabrics" },
				{ "etc_leather_gray_i00", "Sealed Nightmare Robe Fabric" },
				{ "etc_leather_gray_i00", "Sealed Majestic Robe Frabrics" },
				{ "etc_leather_gray_i00", "Sealed Tallum Stockings Fabrics" } };
		SHOP_PRICES = new int[][] { { 5485, 4, 10450 }, { 5486, 3, 2950 }, { 5487, 2, 18050 }, { 5488, 2, 18050 }, { 5489, 6, 15550 } };
	}
}
