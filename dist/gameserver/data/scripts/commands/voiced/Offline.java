package commands.voiced;

import l2s.gameserver.Config;
import l2s.gameserver.handler.IVoicedCommandHandler;
import l2s.gameserver.handler.VoicedCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.TradeList;
import l2s.gameserver.model.entity.olympiad.Olympiad;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;

public class Offline extends Functions implements IVoicedCommandHandler, ScriptFile
{
	private String[] _commandList;

	public Offline()
	{
		_commandList = new String[] { "offline", "ghost" };
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

	@Override
	public boolean useVoicedCommand(final String command, final Player activeChar, final String target)
	{
		if(!Config.ALLOW_VOICED_COMMANDS)
			return false;

		if(!Config.SERVICES_OFFLINE_TRADE_ALLOW)
		{
			show(new CustomMessage("scripts.commands.user.offline.Disabled"), activeChar);
			return false;
		}
		if(activeChar.getOlympiadObserveId() != -1 || activeChar.getOlympiadGameId() != -1 || Olympiad.isRegistered(activeChar) || activeChar.getKarma() > 0)
		{
			activeChar.sendActionFailed();
			return false;
		}
		if(activeChar.getLevel() < Config.SERVICES_OFFLINE_TRADE_MIN_LEVEL)
		{
			show(new CustomMessage("scripts.commands.user.offline.LowLevel").addNumber(Config.SERVICES_OFFLINE_TRADE_MIN_LEVEL), activeChar);
			return false;
		}
		if(!activeChar.isInStoreMode())
		{
			show(new CustomMessage("scripts.commands.user.offline.IncorrectUse"), activeChar);
			return false;
		}
		if(activeChar.getNoChannelRemained() > 0L)
		{
			show(new CustomMessage("scripts.commands.user.offline.BanChat"), activeChar);
			return false;
		}
		if(activeChar.isActionBlocked("private store"))
		{
			activeChar.sendMessage(new CustomMessage("trade.OfflineNoTradeZone"));
			return false;
		}
		if(Config.SERVICES_OFFLINE_TRADE_PRICE > 0 && Config.SERVICES_OFFLINE_TRADE_PRICE_ITEM > 0)
		{
			if(getItemCount(activeChar, Config.SERVICES_OFFLINE_TRADE_PRICE_ITEM) < Config.SERVICES_OFFLINE_TRADE_PRICE)
			{
				show(new CustomMessage("scripts.commands.user.offline.NotEnough").addItemName(Config.SERVICES_OFFLINE_TRADE_PRICE_ITEM).addNumber(Config.SERVICES_OFFLINE_TRADE_PRICE), activeChar);
				return false;
			}
			removeItem(activeChar, Config.SERVICES_OFFLINE_TRADE_PRICE_ITEM, Config.SERVICES_OFFLINE_TRADE_PRICE);
		}
		TradeList.validateList(activeChar);
		activeChar.offline();
		return true;
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return _commandList;
	}
}
