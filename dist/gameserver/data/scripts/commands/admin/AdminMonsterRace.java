package commands.admin;

import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.MonsterRace;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.DeleteObject;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.MonRaceInfo;
import l2s.gameserver.network.l2.s2c.PlaySound;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.Location;

public class AdminMonsterRace implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;
	protected static int state;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(command.equalsIgnoreCase("admin_mons"))
		{
			if(!activeChar.getPlayerAccess().MonsterRace)
				return false;
			handleSendPacket(activeChar);
		}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminMonsterRace._adminCommands;
	}

	private void handleSendPacket(final Player activeChar)
	{
		final int[][] codes = { { -1, 0 }, { 0, 15322 }, { 13765, -1 }, { -1, 0 } };
		final MonsterRace race = MonsterRace.getInstance();
		if(AdminMonsterRace.state == -1)
		{
			++AdminMonsterRace.state;
			race.newRace();
			race.newSpeeds();
			activeChar.broadcastPacket(new L2GameServerPacket[] {
					new MonRaceInfo(codes[AdminMonsterRace.state][0], codes[AdminMonsterRace.state][1], race.getMonsters(), race.getSpeeds()) });
		}
		else if(AdminMonsterRace.state == 0)
		{
			++AdminMonsterRace.state;
			activeChar.sendPacket(new SystemMessage(824));
			activeChar.broadcastPacket(new L2GameServerPacket[] { new PlaySound("S_Race") });
			activeChar.broadcastPacket(new L2GameServerPacket[] {
					new PlaySound(0, "ItemSound2.race_start", 1, 121209259, new Location(12125, 182487, -3559)) });
			activeChar.broadcastPacket(new L2GameServerPacket[] {
					new MonRaceInfo(codes[AdminMonsterRace.state][0], codes[AdminMonsterRace.state][1], race.getMonsters(), race.getSpeeds()) });
			ThreadPoolManager.getInstance().schedule(new RunRace(codes, activeChar), 5000L);
		}
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
		AdminMonsterRace._adminCommands = new String[] { "admin_mons" };
		AdminMonsterRace.state = -1;
	}

	class RunRace implements Runnable
	{
		private int[][] codes;
		private Player activeChar;

		public RunRace(final int[][] codes, final Player activeChar)
		{
			this.codes = codes;
			this.activeChar = activeChar;
		}

		@Override
		public void run()
		{
			activeChar.broadcastPacket(new L2GameServerPacket[] {
					new MonRaceInfo(codes[2][0], codes[2][1], MonsterRace.getInstance().getMonsters(), MonsterRace.getInstance().getSpeeds()) });
			ThreadPoolManager.getInstance().schedule(new RunEnd(activeChar), 30000L);
		}
	}

	class RunEnd implements Runnable
	{
		private Player activeChar;

		public RunEnd(final Player activeChar)
		{
			this.activeChar = activeChar;
		}

		@Override
		public void run()
		{
			NpcInstance obj = null;
			for(int i = 0; i < 8; ++i)
			{
				obj = MonsterRace.getInstance().getMonsters()[i];
				activeChar.broadcastPacket(new L2GameServerPacket[] { new DeleteObject(obj) });
			}
			AdminMonsterRace.state = -1;
		}
	}
}
