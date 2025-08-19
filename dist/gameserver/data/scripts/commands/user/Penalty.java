package commands.user;

import java.text.SimpleDateFormat;

import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.handler.IUserCommandHandler;
import l2s.gameserver.handler.UserCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;

public class Penalty extends Functions implements IUserCommandHandler, ScriptFile
{
	private static final int[] COMMAND_IDS;

	@Override
	public boolean useUserCommand(final int id, final Player activeChar)
	{
		if(Penalty.COMMAND_IDS[0] != id)
			return false;
		long _leaveclan = 0L;
		if(activeChar.getLeaveClanTime() != 0L)
			_leaveclan = activeChar.getLeaveClanTime() + 86400000L;
		long _deleteclan = 0L;
		if(activeChar.getDeleteClanTime() != 0L)
			_deleteclan = activeChar.getDeleteClanTime() + 864000000L;
		final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		String html = HtmCache.getInstance().getHtml("scripts/commands/user/penalty.htm", activeChar);
		if(activeChar.getClanId() == 0)
		{
			if(_leaveclan == 0L && _deleteclan == 0L)
			{
				html = html.replaceFirst("%reason%", "No penalty is imposed.");
				html = html.replaceFirst("%expiration%", " ");
			}
			else if(_leaveclan > 0L && _deleteclan == 0L)
			{
				html = html.replaceFirst("%reason%", "Penalty for leaving clan.");
				html = html.replaceFirst("%expiration%", format.format(_leaveclan));
			}
			else if(_deleteclan > 0L)
			{
				html = html.replaceFirst("%reason%", "Penalty for dissolving clan.");
				html = html.replaceFirst("%expiration%", format.format(_deleteclan));
			}
		}
		else if(activeChar.getClan().canInvite())
		{
			html = html.replaceFirst("%reason%", "No penalty is imposed.");
			html = html.replaceFirst("%expiration%", " ");
		}
		else
		{
			html = html.replaceFirst("%reason%", "Penalty for expelling clan member.");
			html = html.replaceFirst("%expiration%", format.format(activeChar.getClan().getExpelledMemberTime()));
		}
		show(html, activeChar);
		return false;
	}

	@Override
	public final int[] getUserCommandList()
	{
		return Penalty.COMMAND_IDS;
	}

	@Override
	public void onLoad()
	{
		UserCommandHandler.getInstance().registerUserCommandHandler(this);
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	static
	{
		COMMAND_IDS = new int[] { 100 };
	}
}
