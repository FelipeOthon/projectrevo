package commands.voiced;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import l2s.gameserver.Config;
import l2s.gameserver.handler.IVoicedCommandHandler;
import l2s.gameserver.handler.VoicedCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.scripts.ScriptFile;

public class ServerTime implements IVoicedCommandHandler, ScriptFile
{
	private static final String[] _commandList;
	private static final DateFormat DATE_FORMAT;

	@Override
	public boolean useVoicedCommand(final String command, final Player activeChar, final String target)
	{
		if(!Config.ALLOW_VOICED_COMMANDS)
			return false;

		if(command.equals("servertime"))
		{
			activeChar.sendMessage(ServerTime.DATE_FORMAT.format(new Date(System.currentTimeMillis())));
			return true;
		}
		if(command.equals("serverdate"))
		{
			activeChar.sendMessage(ServerTime.DATE_FORMAT.format(new Date(Calendar.getInstance().getTimeInMillis())));
			return true;
		}
		return false;
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return ServerTime._commandList;
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

	static
	{
		_commandList = new String[] { "servertime", "serverdate" };
		DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	}
}
