package commands.admin;

import java.util.ArrayList;

import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;

public class AdminMammon implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;
	ArrayList<Integer> npcIds;

	public AdminMammon()
	{
		npcIds = new ArrayList<Integer>();
	}

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		npcIds.clear();
		if(!activeChar.getPlayerAccess().Menu)
			return false;
		if(command.startsWith("admin_find_mammon"))
		{
			npcIds.add(31113);
			npcIds.add(31126);
			npcIds.add(31092);
			int teleportIndex = -1;
			try
			{
				if(command.length() > 16)
					teleportIndex = Integer.parseInt(command.substring(18));
			}
			catch(Exception ex)
			{}
			findAdminNPCs(activeChar, npcIds, teleportIndex, -1);
		}
		else if(command.equals("admin_show_mammon"))
		{
			npcIds.add(31113);
			npcIds.add(31126);
			findAdminNPCs(activeChar, npcIds, -1, 1);
		}
		else if(command.equals("admin_hide_mammon"))
		{
			npcIds.add(31113);
			npcIds.add(31126);
			findAdminNPCs(activeChar, npcIds, -1, 0);
		}
		else if(command.startsWith("admin_list_spawns"))
		{
			int npcId = 0;
			try
			{
				npcId = Integer.parseInt(command.substring(18).trim());
			}
			catch(Exception NumberFormatException)
			{
				activeChar.sendMessage("Command format is //list_spawns <NPC_ID>");
			}
			npcIds.add(npcId);
			findAdminNPCs(activeChar, npcIds, -1, -1);
		}
		else if(command.startsWith("admin_msg"))
			activeChar.sendPacket(new SystemMessage(Integer.parseInt(command.substring(10).trim())));
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminMammon._adminCommands;
	}

	public void findAdminNPCs(final Player activeChar, final ArrayList<Integer> npcIdList, final int teleportIndex, final int makeVisible)
	{
		int index = 0;
		for(final NpcInstance npcInst : GameObjectsStorage.getNpcs())
			if(npcInst != null)
			{
				final int npcId = npcInst.getNpcId();
				if(!npcIdList.contains(npcId))
					continue;
				if(makeVisible == 1)
					npcInst.spawnMe();
				else if(makeVisible == 0)
					npcInst.decayMe();
				if(!npcInst.isVisible())
					continue;
				++index;
				if(teleportIndex > -1)
				{
					if(teleportIndex != index)
						continue;
					activeChar.teleToLocation(npcInst.getLoc());
				}
				else
					activeChar.sendMessage(index + " - " + npcInst.getName() + " (" + npcInst.getObjectId() + "): " + npcInst.getX() + " " + npcInst.getY() + " " + npcInst.getZ());
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
		AdminMammon._adminCommands = new String[] { "admin_find_mammon", "admin_show_mammon", "admin_hide_mammon", "admin_list_spawns" };
	}
}
