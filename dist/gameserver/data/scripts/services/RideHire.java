package services;

import l2s.gameserver.cache.Msg;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.SetupGauge;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.SiegeUtils;

public class RideHire extends Functions implements ScriptFile
{
	public String DialogAppend_30827(final Integer val)
	{
		if(val != 0)
			return "";
		final Player player = getSelf();
		if(!player.isLangRus())
			return "<br>[scripts_services.RideHire:ride_prices|Ride hire mountable pet.]";
		return "<br>[scripts_services.RideHire:ride_prices|\u0412\u0437\u044f\u0442\u044c \u043d\u0430 \u043f\u0440\u043e\u043a\u0430\u0442 \u0435\u0437\u0434\u043e\u0432\u043e\u0435 \u0436\u0438\u0432\u043e\u0442\u043d\u043e\u0435.]";
	}

	public void ride_prices()
	{
		show("scripts/services/ride-prices.htm", getSelf());
	}

	public void ride(final String[] args)
	{
		final Player player = getSelf();
		if(args.length != 3)
		{
			if(!player.isLangRus())
				show("Incorrect input", player);
			else
				show("\u041d\u0435\u043a\u043e\u0440\u0440\u0435\u043a\u0442\u043d\u044b\u0435 \u0434\u0430\u043d\u043d\u044b\u0435", player);
			return;
		}
		if(player.isActionsDisabled() || player.getLastNpc().getDistance(player) > 250.0)
			return;
		if(!SiegeUtils.getCanRide())
		{
			if(!player.isLangRus())
				show("Can't ride while Siege in progress.", player);
			else
				show("\u041f\u0440\u043e\u043a\u0430\u0442 \u043d\u0435 \u0440\u0430\u0431\u043e\u0442\u0430\u0435\u0442 \u0432\u043e \u0432\u0440\u0435\u043c\u044f \u043e\u0441\u0430\u0434\u044b.", player);
			return;
		}
		if(player.getServitor() != null || player.isMounted())
		{
			player.sendPacket(new SystemMessage(543));
			return;
		}
		final Integer npc_id = Integer.parseInt(args[0]);
		final Integer time = Integer.parseInt(args[1]);
		final Integer price = Integer.parseInt(args[2]);
		if(npc_id != 12621 && npc_id != 12526 && npc_id != 16030)
		{
			if(!player.isLangRus())
				show("Unknown pet.", player);
			else
				show("\u0423 \u043c\u0435\u043d\u044f \u043d\u0435\u0442 \u0442\u0430\u043a\u0438\u0445 \u043f\u0438\u0442\u043e\u043c\u0446\u0435\u0432!", player);
			return;
		}
		if(time > 1800)
		{
			if(!player.isLangRus())
				show("Too long time to ride.", player);
			else
				show("\u0421\u043b\u0438\u0448\u043a\u043e\u043c \u0431\u043e\u043b\u044c\u0448\u043e\u0435 \u0432\u0440\u0435\u043c\u044f.", player);
			return;
		}
		if(player.getAdena() < price)
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}
		player.reduceAdena(price, true);
		doLimitedRide(player, npc_id, time);
	}

	public static void doLimitedRide(final Player player, final Integer npc_id, final Integer time)
	{
		if(!ride(player, npc_id))
			return;
		player.sendPacket(new SetupGauge(3, time * 1000));
		executeTask(player, "services.RideHire", "rideOver", new Object[0], time * 1000);
	}

	public void rideOver()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		unRide(player);
		if(!player.isLangRus())
			show("Ride time is over. Welcome back again!", player);
		else
			show("\u0412\u0440\u0435\u043c\u044f \u043f\u0440\u043e\u043a\u0430\u0442\u0430 \u0437\u0430\u043a\u043e\u043d\u0447\u0438\u043b\u043e\u0441\u044c. \u041f\u0440\u0438\u0445\u043e\u0434\u0438\u0442\u0435 \u0435\u0449\u0435!", player);
	}

	@Override
	public void onLoad()
	{
		ScriptFile._log.info("Loaded Service: Ride Hire");
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
