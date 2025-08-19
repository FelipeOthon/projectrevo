package commands.voiced;

import l2s.gameserver.Config;
import l2s.gameserver.handler.IVoicedCommandHandler;
import l2s.gameserver.handler.VoicedCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.authcomm.AuthServerCommunication;
import l2s.gameserver.network.authcomm.gs2as.ChangePassword;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.Util;

public class Password extends Functions implements IVoicedCommandHandler, ScriptFile
{
	private String[] _commandList;

	public Password()
	{
		_commandList = new String[] { "password" };
	}

	@Override
	public void onLoad()
	{
		VoicedCommandHandler.getInstance().registerVoicedCommandHandler(this);
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public void check(final String[] var)
	{
		if(!Config.SERVICES_CHANGE_PASSWORD)
			return;
		final Player player = getSelf();
		if(player == null)
			return;
		if(var.length != 3)
		{
			show(new CustomMessage("scripts.commands.user.password.IncorrectValues"), player);
			return;
		}
		useVoicedCommand("password", player, var[0] + " " + var[1] + " " + var[2]);
	}

	@Override
	public boolean useVoicedCommand(final String command, final Player activeChar, final String target)
	{
		if(!Config.ALLOW_VOICED_COMMANDS)
			return false;

		if(!Config.SERVICES_CHANGE_PASSWORD)
			return false;
		if(command.equals("password") && (target == null || target.equals("")))
		{
			show("scripts/commands/voiced/password.html", activeChar);
			return true;
		}
		final String[] parts = target.split(" ");
		if(parts.length != 3)
		{
			show(new CustomMessage("scripts.commands.user.password.IncorrectValues"), activeChar);
			return false;
		}
		if(!parts[1].equals(parts[2]))
		{
			show(new CustomMessage("scripts.commands.user.password.IncorrectConfirmation"), activeChar);
			return false;
		}
		if(parts[1].equals(parts[0]))
		{
			show(new CustomMessage("scripts.commands.user.password.NewPassIsOldPass"), activeChar);
			return false;
		}
		if(parts[1].length() < 5 || parts[1].length() > 20)
		{
			show(new CustomMessage("scripts.commands.user.password.IncorrectSize"), activeChar);
			return false;
		}
		if(!Util.isMatchingRegexp(parts[1], Config.APASSWD_TEMPLATE))
		{
			show(new CustomMessage("scripts.commands.user.password.IncorrectInput"), activeChar);
			return false;
		}
		AuthServerCommunication.getInstance().sendPacket(new ChangePassword(activeChar.getAccountName(), parts[0], parts[1], activeChar.getHWID()));
		show(new CustomMessage("scripts.commands.user.password.ResultTrue"), activeChar);
		return true;
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return _commandList;
	}
}
