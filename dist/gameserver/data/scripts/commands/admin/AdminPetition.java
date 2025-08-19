package commands.admin;

import java.util.StringTokenizer;

import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.instancemanager.PetitionManager;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;

public class AdminPetition implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().CanEditChar)
			return false;
		if(command.equalsIgnoreCase("admin_view_petitions"))
			PetitionManager.getInstance().sendPendingPetitionList(activeChar);
		else if(command.startsWith("admin_view_petition"))
		{
			int petitionId = -1;
			final StringTokenizer st = new StringTokenizer(command);
			try
			{
				st.nextToken();
				petitionId = Integer.parseInt(st.nextToken());
			}
			catch(Exception e)
			{
				petitionId = -1;
			}
			PetitionManager.getInstance().viewPetition(activeChar, petitionId);
		}
		else if(command.startsWith("admin_accept_petition"))
		{
			int petitionId = -1;
			final StringTokenizer st = new StringTokenizer(command);
			try
			{
				st.nextToken();
				petitionId = Integer.parseInt(st.nextToken());
			}
			catch(Exception e)
			{
				petitionId = -1;
			}
			if(petitionId < 0)
			{
				activeChar.sendMessage("Usage: //accept_petition id");
				return false;
			}
			if(PetitionManager.getInstance().isPlayerInConsultation(activeChar))
			{
				activeChar.sendPacket(new SystemMessage(390));
				return true;
			}
			if(PetitionManager.getInstance().isPetitionInProcess(petitionId))
			{
				activeChar.sendPacket(new SystemMessage(407));
				return true;
			}
			if(!PetitionManager.getInstance().acceptPetition(activeChar, petitionId))
				activeChar.sendPacket(new SystemMessage(388));
		}
		else if(command.startsWith("admin_reject_petition"))
		{
			int petitionId = -1;
			final StringTokenizer st = new StringTokenizer(command);
			try
			{
				st.nextToken();
				petitionId = Integer.parseInt(st.nextToken());
			}
			catch(Exception e)
			{
				petitionId = -1;
			}
			if(petitionId < 0)
			{
				activeChar.sendMessage("Usage: //accept_petition id");
				return false;
			}
			if(!PetitionManager.getInstance().rejectPetition(activeChar, petitionId))
				activeChar.sendPacket(new SystemMessage(393));
			PetitionManager.getInstance().sendPendingPetitionList(activeChar);
		}
		else if(command.equalsIgnoreCase("admin_reset_petitions"))
		{
			if(PetitionManager.getInstance().isPetitionInProcess())
			{
				activeChar.sendPacket(new SystemMessage(407));
				return false;
			}
			PetitionManager.getInstance().clearPendingPetitions();
			PetitionManager.getInstance().sendPendingPetitionList(activeChar);
		}
		else if(command.startsWith("admin_force_peti"))
		{
			final StringTokenizer st2 = new StringTokenizer(command);
			if(st2.countTokens() < 2)
			{
				activeChar.sendMessage("Usage: //force_peti text");
				return false;
			}
			try
			{
				final GameObject targetChar = activeChar.getTarget();
				if(targetChar == null || !(targetChar instanceof Player))
				{
					activeChar.sendPacket(new SystemMessage(109));
					return false;
				}
				final Player targetPlayer = (Player) targetChar;
				st2.nextToken();
				final String text = st2.nextToken();
				final int petitionId2 = PetitionManager.getInstance().submitPetition(targetPlayer, text, 9);
				PetitionManager.getInstance().acceptPetition(activeChar, petitionId2);
			}
			catch(StringIndexOutOfBoundsException e2)
			{
				activeChar.sendMessage("Usage: //force_peti text");
				return false;
			}
		}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminPetition._adminCommands;
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
		AdminPetition._adminCommands = new String[] {
				"admin_view_petitions",
				"admin_view_petition",
				"admin_accept_petition",
				"admin_reject_petition",
				"admin_reset_petitions",
				"admin_force_peti" };
	}
}
