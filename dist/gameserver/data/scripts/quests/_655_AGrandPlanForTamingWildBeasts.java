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

public class _655_AGrandPlanForTamingWildBeasts extends Quest implements ScriptFile
{
	private static final int MESSENGER = 35627;
	private static final int STONE = 8084;
	private static final int TRAINER_LICENSE = 8293;

	public _655_AGrandPlanForTamingWildBeasts()
	{
		super(0);
		this.addStartNpc(35627);
		this.addTalkId(new int[] { 35627 });
		addQuestItem(new int[] { 8084 });
		addQuestItem(new int[] { 8293 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("farm_messenger_q0655_06.htm"))
		{
			st.setCond(1);
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmlText = "noquest";
		final int cond = st.getCond();
		final Player player = st.getPlayer();
		final Clan clan = player.getClan();
		final ClanHall clanhall = (ClanHall) ResidenceHolder.getInstance().getResidence(63);
		final SiegeEvent<?, ?> siegeEvent = clanhall.getSiegeEvent();
		if(siegeEvent == null || siegeEvent.isRegistrationOver())
		{
			htmlText = null;
			this.showHtmlFile(player, "farm_messenger_q0655_02.htm", "%siege_time%", TimeUtils.toSimpleFormat(clanhall.getSiegeDate()));
		}
		else if(clan == null || player.getObjectId() != clan.getLeaderId())
			htmlText = "farm_messenger_q0655_03.htm";
		else if(player.getObjectId() == clan.getLeaderId() && clan.getLevel() < 4)
			htmlText = "farm_messenger_q0655_05.htm";
		else if(siegeEvent.getSiegeClan("attackers", player.getClan()) != null)
			htmlText = "farm_messenger_q0655_07.htm";
		else if(clan.getHasHideout() > 0)
			htmlText = "farm_messenger_q0655_04.htm";
		else if(cond == 0)
			htmlText = "farm_messenger_q0655_01.htm";
		else if(cond == 1 && st.getQuestItemsCount(8084) < 10L)
			htmlText = "farm_messenger_q0655_08.htm";
		else if(cond == 1 && st.getQuestItemsCount(8084) == 10L)
		{
			st.setCond(-1);
			st.takeItems(8084, -1L);
			st.giveItems(8293, 1L);
			htmlText = "farm_messenger_q0655_10.htm";
		}
		else if(st.getQuestItemsCount(8293) == 1L)
			htmlText = "farm_messenger_q0655_09.htm";
		return htmlText;
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
