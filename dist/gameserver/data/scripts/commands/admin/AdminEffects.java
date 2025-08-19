package commands.admin;

import java.util.StringTokenizer;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.model.WorldRegion;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.Earthquake;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.NpcInfo;
import l2s.gameserver.network.l2.s2c.SocialAction;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.skills.AbnormalEffect;
import l2s.gameserver.skills.EffectType;
import l2s.gameserver.tables.SkillTable;

public class AdminEffects implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().GodMode)
			return false;
		if(command.startsWith("admin_social"))
		{
			final String[] wordList = command.split(" ");
			int val;
			if(wordList.length == 1)
				val = Rnd.get(1, 7);
			else
				try
				{
					val = Integer.parseInt(wordList[1]);
				}
				catch(NumberFormatException nfe)
				{
					activeChar.sendMessage("Specify a valid social action number.");
					return false;
				}
			final Creature target = (Creature) activeChar.getTarget();
			if(target == null || target == activeChar)
				activeChar.broadcastPacket(new L2GameServerPacket[] { new SocialAction(activeChar.getObjectId(), val) });
			else
				target.broadcastPacket(new L2GameServerPacket[] { new SocialAction(target.getObjectId(), val) });
		}
		else if(command.startsWith("admin_gmspeed"))
			try
			{
				final int val2 = Integer.parseInt(command.substring(14));
				if(val2 == 0)
				{
					activeChar.getAbnormalList().stop(7029);
					activeChar.unsetVar("gm_gmspeed");
				}
				else if(val2 >= 1 && val2 <= 4)
				{
					activeChar.getAbnormalList().stop(7029);
					if(Config.SAVE_GM_EFFECTS)
						activeChar.setVar("gm_gmspeed", String.valueOf(val2));
					activeChar.doCast(SkillTable.getInstance().getInfo(7029, val2), activeChar, true);
				}
				else
					activeChar.sendMessage("Use //gmspeed value = [0...4].");
			}
			catch(Exception e)
			{
				activeChar.sendMessage("Use //gmspeed value = [0...4].");
			}
			finally
			{
				activeChar.updateEffectIcons();
			}
		else if(command.equalsIgnoreCase("admin_invis") || command.equalsIgnoreCase("admin_vis"))
		{
			if(activeChar.isInvisible())
			{
				activeChar.setInvisible(false);
				activeChar.broadcastUserInfo(true);
				if(activeChar.getServitor() != null)
					activeChar.getServitor().broadcastCharInfo();
			}
			else
			{
				activeChar.setInvisible(true);
				activeChar.sendUserInfo(true);
				if(activeChar.getCurrentRegion() != null)
					for(final WorldRegion neighbor : activeChar.getCurrentRegion().getNeighbors())
						if(neighbor != null)
							neighbor.removePlayerFromOtherPlayers(activeChar);
			}
		}
		else if(command.equalsIgnoreCase("admin_earthquake"))
			try
			{
				final String val3 = command.substring(17);
				final StringTokenizer st = new StringTokenizer(val3);
				final String val4 = st.nextToken();
				final int intensity = Integer.parseInt(val4);
				final String val5 = st.nextToken();
				final int duration = Integer.parseInt(val5);
				activeChar.broadcastPacket(new L2GameServerPacket[] { new Earthquake(activeChar.getLoc(), intensity, duration) });
			}
			catch(Exception ex)
			{}
		else if(command.equalsIgnoreCase("admin_para"))
		{
			String type = "1";
			final StringTokenizer st = new StringTokenizer(command);
			try
			{
				st.nextToken();
				type = st.nextToken();
			}
			catch(Exception ex2)
			{}
			try
			{
				final GameObject target2 = activeChar.getTarget();
				if(target2.isCreature())
				{
					final Creature player = (Creature) target2;
					if(type.equals("1"))
						player.startAbnormalEffect(AbnormalEffect.HOLD_1);
					else
						player.startAbnormalEffect(AbnormalEffect.HOLD_2);
					player.startParalyzed();
				}
			}
			catch(Exception ex3)
			{}
		}
		else if(command.equalsIgnoreCase("admin_unpara"))
			try
			{
				final GameObject target3 = activeChar.getTarget();
				Creature player2 = null;
				if(target3 != null && target3.isCreature())
				{
					player2 = (Creature) target3;
					player2.getAbnormalList().stop(EffectType.Paralyze);
					player2.getAbnormalList().stop(EffectType.Petrification);
					player2.stopAbnormalEffect(AbnormalEffect.HOLD_1);
					player2.stopAbnormalEffect(AbnormalEffect.HOLD_2);
					player2.stopParalyzed();
				}
			}
			catch(Exception ex4)
			{}
		else if(command.equalsIgnoreCase("admin_para_all"))
			try
			{
				for(final Player player3 : World.getAroundPlayers(activeChar, 1250, 200))
					if(player3 != null && !player3.isGM())
					{
						player3.startAbnormalEffect(AbnormalEffect.HOLD_1);
						player3.startParalyzed();
					}
			}
			catch(Exception ex5)
			{}
		else if(command.equalsIgnoreCase("admin_unpara_all"))
			try
			{
				for(final Player player3 : World.getAroundPlayers(activeChar, 1250, 200))
				{
					player3.getAbnormalList().stop(EffectType.Paralyze);
					player3.getAbnormalList().stop(EffectType.Petrification);
					player3.stopAbnormalEffect(AbnormalEffect.HOLD_1);
					player3.stopAbnormalEffect(AbnormalEffect.HOLD_2);
					player3.stopParalyzed();
				}
			}
			catch(Exception ex6)
			{}
		else
		{
			if(!command.equalsIgnoreCase("admin_bighead"))
				if(!command.equalsIgnoreCase("admin_shrinkhead"))
				{
					if(command.equalsIgnoreCase("admin_changename"))
					{
						try
						{
							final String name = command.substring(17);
							String oldName = "null";
							try
							{
								final GameObject target2 = activeChar.getTarget();
								Creature player = null;
								if(target2 == null)
								{
									player = activeChar;
									oldName = activeChar.getName();
								}
								else
								{
									if(!target2.isCreature())
										return false;
									player = (Creature) target2;
									oldName = player.getName();
								}
								player.setName(name);
								if(player.isPlayer())
									player.broadcastUserInfo(true);
								else if(player.isNpc())
									player.broadcastPacket(new L2GameServerPacket[] { new NpcInfo((NpcInstance) player, (Creature) null) });
								activeChar.sendMessage("Changed name from " + oldName + " to " + name + ".");
							}
							catch(Exception ex7)
							{}
						}
						catch(StringIndexOutOfBoundsException ex8)
						{}
						return true;
					}
					if(command.equalsIgnoreCase("admin_invul"))
					{
						handleInvul(activeChar);
						if(!activeChar.isInvul())
						{
							activeChar.unsetVar("gm_invul");
							return true;
						}
						if(Config.SAVE_GM_EFFECTS)
						{
							activeChar.setVar("gm_invul", "true");
							return true;
						}
						return true;
					}
					else
					{
						if(command.equalsIgnoreCase("admin_setinvul"))
						{
							final GameObject target3 = activeChar.getTarget();
							if(target3.isPlayer())
								handleInvul((Player) target3);
							return true;
						}
						if(command.startsWith("admin_abnormal"))
						{
							final GameObject target3 = activeChar.getTarget();
							final String[] wordList2 = command.split(" ");
							AbnormalEffect ae = AbnormalEffect.NULL;
							try
							{
								if(wordList2.length > 1)
									ae = AbnormalEffect.getByName(wordList2[1]);
							}
							catch(Exception e2)
							{
								activeChar.sendMessage("USAGE: //abnormal name");
								activeChar.sendMessage("//abnormal - Clears all abnormal effects");
								return false;
							}
							final Creature effectTarget = target3 == null ? activeChar : (Creature) target3;
							if(ae == AbnormalEffect.NULL)
							{
								effectTarget.startAbnormalEffect(AbnormalEffect.NULL);
								effectTarget.sendMessage("Abnormal effects clearned by admin.");
								if(effectTarget != activeChar)
									effectTarget.sendMessage("Abnormal effects clearned.");
							}
							else
							{
								effectTarget.startAbnormalEffect(ae);
								effectTarget.sendMessage("Admin added abnormal effect: " + ae.getName());
								if(effectTarget != activeChar)
									effectTarget.sendMessage("Added abnormal effect: " + ae.getName());
							}
							effectTarget.sendChanges();
							return true;
						}
						return true;
					}
				}
			try
			{
				final Creature target4 = (Creature) activeChar.getTarget();
				if(target4 == null)
					activeChar.sendPacket(Msg.INCORRECT_TARGET);
				else if(target4.isCreature())
				{
					if(target4.getAbnormalEffect() == AbnormalEffect.BIG_HEAD.getMask())
						target4.stopAbnormalEffect(AbnormalEffect.BIG_HEAD);
					else
						target4.startAbnormalEffect(AbnormalEffect.BIG_HEAD);
					if(target4.isPlayer())
						target4.sendMessage("Admin changed your head type.");
				}
			}
			catch(Exception ex9)
			{}
		}
		return true;
	}

	private void handleInvul(final Player activeChar)
	{
		AbnormalEffect ae = AbnormalEffect.NULL;
		if(!Config.InvulEffect.equalsIgnoreCase("none"))
			ae = AbnormalEffect.getByName(Config.InvulEffect);
		if(activeChar.isInvul())
		{
			activeChar.setIsInvul(false);
			if(activeChar.getServitor() != null)
				activeChar.getServitor().setIsInvul(false);
			activeChar.sendMessage(activeChar.getName() + " is now mortal.");
			if(ae != AbnormalEffect.NULL)
				activeChar.stopAbnormalEffect(ae);
		}
		else
		{
			activeChar.setIsInvul(true);
			if(activeChar.getServitor() != null)
				activeChar.getServitor().setIsInvul(true);
			activeChar.sendMessage(activeChar.getName() + " is now immortal.");
			if(ae != AbnormalEffect.NULL)
				activeChar.startAbnormalEffect(ae);
		}
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminEffects._adminCommands;
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
		AdminEffects._adminCommands = new String[] {
				"admin_invis",
				"admin_vis",
				"admin_earthquake",
				"admin_bighead",
				"admin_shrinkhead",
				"admin_unpara_all",
				"admin_para_all",
				"admin_unpara",
				"admin_para",
				"admin_changename",
				"admin_gmspeed",
				"admin_invul",
				"admin_setinvul",
				"admin_social",
				"admin_abnormal" };
	}
}
