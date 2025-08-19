package commands.admin;

import java.util.StringTokenizer;

import l2s.gameserver.cache.Msg;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.components.IBroadcastPacket;
import l2s.gameserver.network.l2.s2c.PledgeShowInfoUpdate;
import l2s.gameserver.network.l2.s2c.PledgeStatusChanged;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.ClanTable;
import l2s.gameserver.utils.SiegeUtils;

public class AdminPledge implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(activeChar.getPlayerAccess() == null || !activeChar.getPlayerAccess().CanEditCharAll || activeChar.getTarget() == null || !activeChar.getTarget().isPlayer())
			return false;
		Player target = null;
		if(activeChar.getTarget().isPlayer())
			target = (Player) activeChar.getTarget();
		if(target == null)
		{
			activeChar.sendPacket(Msg.INCORRECT_TARGET);
			return false;
		}
		if(command.startsWith("admin_pledge"))
		{
			final StringTokenizer st = new StringTokenizer(command);
			st.nextToken();
			final String action = st.nextToken();
			if(action.equals("create"))
				try
				{
					if(target.getClan() != null)
					{
						activeChar.sendMessage("Target already in clan.");
						return false;
					}
					final String pledgeName = st.nextToken();
					final Clan clan = ClanTable.getInstance().createClan(target, pledgeName);
					if(clan != null)
					{
						target.sendPacket(new PledgeShowInfoUpdate(clan));
						target.updatePledgeClass();
						target.broadcastUserInfo(true);
						target.sendPacket(new SystemMessage(189));
						return true;
					}
				}
				catch(Exception ex)
				{}
			else if(action.equals("dismiss"))
			{
				final Clan clan2 = target.getClan();
				if(clan2 == null)
				{
					activeChar.sendMessage("Target not in clan.");
					return false;
				}
				clan2.setDissolvingExpiryTime(System.currentTimeMillis());
				ClanTable.getInstance().dissolveClan(clan2.getClanId());
				return true;
			}
			else
			{
				if(action.equals("setlevel"))
				{
					if(target.getClan() == null)
					{
						activeChar.sendPacket(Msg.INCORRECT_TARGET);
						return false;
					}
					try
					{
						final byte level = Byte.parseByte(st.nextToken());
						final Clan clan = target.getClan();
						activeChar.sendMessage("You set level " + level + " for clan " + clan.getName());
						clan.setLevel(level);
						clan.updateClanInDB();
						if(level < 4)
							SiegeUtils.removeSiegeSkills(target);
						else if(level > 3)
							SiegeUtils.addSiegeSkills(target);
						if(level == 5)
							target.sendPacket(new SystemMessage(1771));
						final SystemMessage sm = new SystemMessage(274);
						final PledgeShowInfoUpdate pu = new PledgeShowInfoUpdate(clan);
						final PledgeStatusChanged ps = new PledgeStatusChanged(clan);
						for(final Player member : clan.getOnlineMembers(0))
						{
							member.updatePledgeClass();
							member.sendPacket(new IBroadcastPacket[] { sm, pu, ps });
							member.broadcastUserInfo(true);
						}
						return true;
					}
					catch(Exception ex2)
					{
						return false;
					}
				}
				if(action.equals("resetcreate"))
				{
					if(target.getClan() == null)
					{
						activeChar.sendPacket(Msg.INCORRECT_TARGET);
						return false;
					}
					target.getClan().setExpelledMemberTime(0L);
					activeChar.sendMessage("The penalty for creating a clan has been lifted for" + target.getName());
				}
				else if(action.equals("resetwait"))
				{
					target.setLeaveClanTime(0L);
					activeChar.sendMessage("The penalty for leaving a clan has been lifted for " + target.getName());
				}
				else if(action.equals("addrep"))
					try
					{
						final int rep = Integer.parseInt(st.nextToken());
						if(target.getClan() == null || target.getClan().getLevel() < 5)
						{
							activeChar.sendPacket(Msg.INCORRECT_TARGET);
							return false;
						}
						target.getClan().incReputation(rep, false, "admin_manual");
						activeChar.sendMessage("Added " + rep + " clan points to clan " + target.getClan().getName());
					}
					catch(Exception nfe)
					{
						activeChar.sendMessage("Please specify a number of clan points to add.");
					}
			}
		}
		return false;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminPledge._adminCommands;
	}

	@Override
	public void onLoad()
	{
		AdminCommandHandler.getInstance().registerAdminCommandHandler(this);
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	static
	{
		AdminPledge._adminCommands = new String[] { "admin_pledge" };
	}
}
