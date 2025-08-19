package commands.admin;

import java.io.File;

import l2s.gameserver.Config;
import l2s.gameserver.geodata.GeoEngine;
import l2s.gameserver.geodata.utils.GeodataUtils;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.scripts.ScriptFile;

public class AdminGeodata implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands = new String[]{
		"admin_geo_z",
		"admin_geo_type",
		"admin_geo_nswe",
		"admin_geo_los",
		"admin_geo_load",
		"admin_geo_dump",
		"admin_geo_trace",
		"admin_geo_map",
		"admin_geo_grid"
	};

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().CanReload)
			return false;

		String[] wordList = command.split(" ");

		if(wordList[0].equals("admin_geo_z"))
		{
			activeChar.sendMessage("GeoEngine: Geo_Lower_Z = " + GeoEngine.getLowerHeight(activeChar.getLoc(), activeChar.getGeoIndex()) + " Geo_Upper_Z = " + GeoEngine.getUpperHeight(activeChar.getLoc(), activeChar.getGeoIndex()) + " Loc_Z = " + activeChar.getZ() + " Corrected_Z = " + activeChar.getGeoZ(activeChar.getLoc()));
		}
		else if(wordList[0].equals("admin_geo_type"))
		{
			int type = GeoEngine.getType(activeChar.getX(), activeChar.getY(), activeChar.getGeoIndex());
			activeChar.sendMessage("GeoEngine: Geo_Type = " + type);
		}
		else if(wordList[0].equals("admin_geo_nswe"))
		{
			String result = "";
			byte nswe = GeoEngine.getLowerNSWE(activeChar.getX(), activeChar.getY(), activeChar.getZ(), activeChar.getGeoIndex());
			if((nswe & 8) == 0)
				result += " N";
			if((nswe & 4) == 0)
				result += " S";
			if((nswe & 2) == 0)
				result += " W";
			if((nswe & 1) == 0)
				result += " E";
			activeChar.sendMessage("GeoEngine: Geo_NSWE -> " + nswe + "->" + result);
		}
		else if(wordList[0].equals("admin_geo_los"))
		{
			if(activeChar.getTarget() != null)
				if(GeoEngine.canSeeTarget(activeChar, activeChar.getTarget()))
					activeChar.sendMessage("GeoEngine: Can See Target");
				else
					activeChar.sendMessage("GeoEngine: Can't See Target");
			else
				activeChar.sendMessage("None Target!");
		}
		else if(wordList[0].equals("admin_geo_load"))
		{
			if(wordList.length != 3)
				activeChar.sendMessage("Usage: //geo_load <regionX> <regionY>");
			else
			{
				try
				{
					int rx = Byte.parseByte(wordList[1]);
					int ry = Byte.parseByte(wordList[2]);

					if(rx < Config.GEO_X_FIRST || ry < Config.GEO_Y_FIRST || rx > Config.GEO_X_LAST || ry > Config.GEO_Y_LAST)
					{
						activeChar.sendMessage("Region [" + rx + "," + ry + "] is out of range!");
						return false;
					}

					File geoDir = new File(Config.GEODATA_ROOT, "");
					if(!geoDir.exists() || !geoDir.isDirectory())
					{
						activeChar.sendMessage("GeoEngine: Files missing, loading aborted.");
						return false;
					}

					int blobOff;
					File geoFile;
					if((geoFile = new File(geoDir, String.format("%2d_%2d" + GeoEngine.L2S_EXTENSION, rx, ry))).exists())
						blobOff = 4;
					else if((geoFile = new File(geoDir, String.format("%2d_%2d" + GeoEngine.L2J_EXTENSION, rx, ry))).exists())
						blobOff = 0;
					else
					{
						activeChar.sendMessage("Region [" + rx + "," + ry + "] not found!");
						return false;
					}

					if(GeoEngine.LoadGeodataFile(rx, ry, geoFile, blobOff))
						activeChar.sendMessage("GeoEngine: Region [" + rx + "," + ry + "] loaded.");
					else
						activeChar.sendMessage("GeoEngine: Region [" + rx + "," + ry + "] not loaded.");
				}
				catch(Exception e)
				{
					activeChar.sendMessage(new CustomMessage("common.Error"));
				}
			}
		}
		else if(wordList[0].equals("admin_geo_dump"))
		{
			if(wordList.length > 2)
			{
				//GeoEngine.DumpGeodataFileMap(Byte.parseByte(wordList[1]), Byte.parseByte(wordList[2]));
				activeChar.sendMessage("Geo square saved " + wordList[1] + "_" + wordList[2]);
			}
			//GeoEngine.DumpGeodataFile(activeChar.getX(), activeChar.getY());
			activeChar.sendMessage("Actual geo square saved.");
		}
		else if(wordList[0].equals("admin_geo_trace"))
		{
			if(wordList.length < 2)
			{
				activeChar.sendMessage("Usage: //geo_trace on|off");
				return false;
			}
			if(wordList[1].equalsIgnoreCase("on"))
				activeChar.setVar("trace", "1", -1);
			else if(wordList[1].equalsIgnoreCase("off"))
				activeChar.unsetVar("trace");
			else
				activeChar.sendMessage("Usage: //geo_trace on|off");
		}
		else if(wordList[0].equals("admin_geo_map"))
		{
			int x = (activeChar.getX() - World.MAP_MIN_X >> 15) + Config.GEO_X_FIRST;
			int y = (activeChar.getY() - World.MAP_MIN_Y >> 15) + Config.GEO_Y_FIRST;

			activeChar.sendMessage("GeoMap: " + x + "_" + y);
		}
		else if(wordList[0].equals("admin_geo_grid"))
		{
			try
			{
				GeodataUtils.debugGrid(activeChar, Integer.parseInt(wordList[1]));
			}
			catch(Exception e)
			{
				GeodataUtils.debugGrid(activeChar, 10);
			}
		}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return _adminCommands;
	}

	@Override
	public void onLoad()
	{
		AdminCommandHandler.getInstance().registerAdminCommandHandler(this);
	}

	@Override
	public void onReload()
	{
		//
	}

	@Override
	public void onShutdown()
	{
		//
	}
}