package quests;

import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.events.impl.SiegeEvent;
import l2s.gameserver.model.entity.residence.ClanHall;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.TimeUtils;

public class _504_CompetitionForTheBanditStronghold extends Quest implements ScriptFile
{
	private static final int MESSENGER = 35437;
	private static final int TARLK_BUGBEAR = 20570;
	private static final int TARLK_BUGBEAR_WARRIOR = 20571;
	private static final int TARLK_BUGBEAR_HIGH_WARRIOR = 20572;
	private static final int TARLK_BASILISK = 20573;
	private static final int ELDER_TARLK_BASILISK = 20574;
	private static final int AMULET = 4332;
	private static final int ALIANCE_TROPHEY = 5009;
	private static final int CONTEST_CERTIFICATE = 4333;

	public _504_CompetitionForTheBanditStronghold()
	{
		super(2);
		this.addStartNpc(35437);
		this.addTalkId(new int[] { 35437 });
		this.addKillId(new int[] { 20570 });
		this.addKillId(new int[] { 20571 });
		this.addKillId(new int[] { 20572 });
		this.addKillId(new int[] { 20573 });
		this.addKillId(new int[] { 20574 });
		addQuestItem(new int[] { 4333 });
		addQuestItem(new int[] { 4332 });
		addQuestItem(new int[] { 5009 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("azit_messenger_q0504_02.htm"))
		{
			st.setCond(1);
			st.setState(2);
			st.giveItems(4333, 1L);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int cond = st.getCond();
		final Player player = st.getPlayer();
		final Clan clan = player.getClan();
		final ClanHall clanhall = (ClanHall) ResidenceHolder.getInstance().getResidence(35);
		final SiegeEvent<?, ?> siegeEvent = clanhall.getSiegeEvent();
		if(siegeEvent == null || siegeEvent.isRegistrationOver())
		{
			htmltext = null;
			this.showHtmlFile(player, "azit_messenger_q0504_03.htm", "%siege_time%", TimeUtils.toSimpleFormat(clanhall.getSiegeDate()));
		}
		else if(clan == null || player.getObjectId() != clan.getLeaderId())
			htmltext = "azit_messenger_q0504_05.htm";
		else if(player.getObjectId() == clan.getLeaderId() && clan.getLevel() < 4)
			htmltext = "azit_messenger_q0504_04.htm";
		else if(siegeEvent.getSiegeClan("attackers", player.getClan()) != null)
			htmltext = "azit_messenger_q0504_06.htm";
		else if(clan.getHasHideout() > 0)
			htmltext = "azit_messenger_q0504_10.htm";
		else if(cond == 0)
			htmltext = "azit_messenger_q0504_01.htm";
		else if(st.getQuestItemsCount(4333) == 1L && st.getQuestItemsCount(4332) < 30L)
			htmltext = "azit_messenger_q0504_07.htm";
		else if(st.getQuestItemsCount(5009) >= 1L)
			htmltext = "azit_messenger_q0504_07a.htm";
		else if(st.getQuestItemsCount(4333) == 1L && st.getQuestItemsCount(4332) == 30L)
		{
			st.takeItems(4332, -1L);
			st.takeItems(4333, -1L);
			st.giveItems(5009, 1L);
			st.playSound(Quest.SOUND_FINISH);
			st.setCond(-1);
			htmltext = "azit_messenger_q0504_08.htm";
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getQuestItemsCount(4332) < 30L)
		{
			st.giveItems(4332, 1L);
			st.playSound(Quest.SOUND_ITEMGET);
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
}
