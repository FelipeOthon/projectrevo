package commands.voiced;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l2s.commons.dbcp.DbUtils;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.database.mysql;
import l2s.gameserver.handler.IVoicedCommandHandler;
import l2s.gameserver.handler.VoicedCommandHandler;
import l2s.gameserver.instancemanager.CoupleManager;
import l2s.gameserver.listener.actor.player.OnAnswerListener;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.entity.Couple;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.s2c.ConfirmDlg;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.network.l2.s2c.SetupGauge;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.skills.AbnormalEffect;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.utils.Location;

public class Wedding implements IVoicedCommandHandler, ScriptFile
{
	private static String[] _voicedCommands;

	@Override
	public boolean useVoicedCommand(final String command, final Player activeChar, final String target)
	{
		if(!Config.ALLOW_VOICED_COMMANDS)
			return false;

		if(command.startsWith("engage"))
			return engage(activeChar);
		if(command.startsWith("divorce"))
			return divorce(activeChar);
		return command.startsWith("gotolove") && goToLove(activeChar);
	}

	public boolean divorce(final Player activeChar)
	{
		if(activeChar.getPartnerId() == 0)
			return false;
		if(Config.WEDDING_DIVORCE_PRICE > 0)
			if(Config.WEDDING_DIVORCE_ITEM == 57)
			{
				if(activeChar.getAdena() < Config.WEDDING_DIVORCE_PRICE)
				{
					activeChar.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
					return false;
				}
				activeChar.reduceAdena(Config.WEDDING_DIVORCE_PRICE, true);
			}
			else
			{
				final ItemInstance pay = activeChar.getInventory().getItemByItemId(Config.WEDDING_DIVORCE_ITEM);
				if(pay == null || pay.getCount() < Config.WEDDING_DIVORCE_PRICE)
				{
					activeChar.sendPacket(Msg.INCORRECT_ITEM_COUNT);
					return false;
				}
				activeChar.getInventory().destroyItem(pay, Config.WEDDING_DIVORCE_PRICE, true);
			}
		final int _partnerId = activeChar.getPartnerId();
		if(activeChar.isMaried())
		{
			activeChar.sendMessage(new CustomMessage("scripts.commands.voiced.Wedding.Divorced"));
			final ItemInstance item = activeChar.getInventory().findItemByItemId(9140);
			if(item != null)
			{
				if(item.isEquipped())
					activeChar.getInventory().unEquipItem(item);
				activeChar.getInventory().destroyItem(item, 1L, false);
			}
		}
		else
			activeChar.sendMessage(new CustomMessage("scripts.commands.voiced.Wedding.Disengaged"));
		activeChar.setMaried(false);
		activeChar.setPartnerId(0);
		Couple couple = CoupleManager.getInstance().getCouple(activeChar.getCoupleId());
		couple.divorce();
		couple = null;
		final Player partner = GameObjectsStorage.getPlayer(_partnerId);
		if(partner != null)
		{
			partner.setPartnerId(0);
			if(partner.isMaried())
				partner.sendMessage(new CustomMessage("scripts.commands.voiced.Wedding.PartnerDivorce"));
			else
				partner.sendMessage(new CustomMessage("scripts.commands.voiced.Wedding.PartnerDisengage"));
			partner.setMaried(false);
			final ItemInstance item2 = partner.getInventory().findItemByItemId(9140);
			if(item2 != null)
			{
				if(item2.isEquipped())
					partner.getInventory().unEquipItem(item2);
				partner.getInventory().destroyItem(item2, 1L, false);
			}
			if(partner.getSex() == 0)
			{
				if(Config.WEDDING_MALE_COLOR != Config.NORMAL_NAME_COLOUR && partner.getNameColor() == Config.WEDDING_MALE_COLOR)
					partner.unsetVar("namecolor");
			}
			else if(Config.WEDDING_FEMALE_COLOR != Config.NORMAL_NAME_COLOUR && partner.getNameColor() == Config.WEDDING_FEMALE_COLOR)
				partner.unsetVar("namecolor");
		}
		else
			mysql.set("DELETE FROM `items` WHERE `owner_id`=" + _partnerId + " AND `item_id`=9140");
		if(activeChar.getSex() == 0)
		{
			if(Config.WEDDING_MALE_COLOR != Config.NORMAL_NAME_COLOUR && activeChar.getNameColor() == Config.WEDDING_MALE_COLOR)
				activeChar.unsetVar("namecolor");
		}
		else if(Config.WEDDING_FEMALE_COLOR != Config.NORMAL_NAME_COLOUR && activeChar.getNameColor() == Config.WEDDING_FEMALE_COLOR)
			activeChar.unsetVar("namecolor");
		return true;
	}

	public boolean engage(final Player activeChar)
	{
		if(activeChar.getTarget() == null)
		{
			activeChar.sendMessage(new CustomMessage("scripts.commands.voiced.Wedding.NoneTargeted"));
			return false;
		}
		if(!activeChar.getTarget().isPlayer())
		{
			activeChar.sendMessage(new CustomMessage("scripts.commands.voiced.Wedding.OnlyAnotherPlayer"));
			return false;
		}
		if(activeChar.getPartnerId() != 0)
		{
			activeChar.sendMessage(new CustomMessage("scripts.commands.voiced.Wedding.AlreadyEngaged"));
			if(Config.WEDDING_PUNISH_INFIDELITY)
			{
				activeChar.startAbnormalEffect(AbnormalEffect.BIG_HEAD);
				int skillLevel = 1;
				if(activeChar.getLevel() > 40)
					skillLevel = 2;
				int skillId;
				if(activeChar.isMageClass())
					skillId = 4361;
				else
					skillId = 4362;
				final Skill skill = SkillTable.getInstance().getInfo(skillId, skillLevel);
				if(activeChar.getAbnormalList().getEffectsBySkill(skill) == null)
				{
					skill.getEffects(activeChar, activeChar, false, false);
					final SystemMessage sm = new SystemMessage(110);
					sm.addSkillName(skillId, (short) skillLevel);
					activeChar.sendPacket(sm);
				}
			}
			return false;
		}
		final Player ptarget = (Player) activeChar.getTarget();
		if(ptarget.getObjectId() == activeChar.getObjectId())
		{
			activeChar.sendMessage(new CustomMessage("scripts.commands.voiced.Wedding.EngagingYourself"));
			return false;
		}
		if(ptarget.isMaried())
		{
			activeChar.sendMessage(new CustomMessage("scripts.commands.voiced.Wedding.PlayerAlreadyMarried"));
			return false;
		}
		if(ptarget.getPartnerId() != 0)
		{
			activeChar.sendMessage(new CustomMessage("scripts.commands.voiced.Wedding.PlayerAlreadyEngaged"));
			return false;
		}
		if(ptarget.isEngageRequest())
		{
			activeChar.sendMessage(new CustomMessage("scripts.commands.voiced.Wedding.PlayerAlreadyAsked"));
			return false;
		}
		if(ptarget.getPartnerId() != 0)
		{
			activeChar.sendMessage(new CustomMessage("scripts.commands.voiced.Wedding.PlayerAlreadyEngaged"));
			return false;
		}
		if(ptarget.getSex() == activeChar.getSex() && !Config.WEDDING_SAMESEX)
		{
			activeChar.sendMessage(new CustomMessage("scripts.commands.voiced.Wedding.SameSex"));
			return false;
		}
		boolean FoundOnFriendList = false;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT friend_id FROM character_friends WHERE char_id=?");
			statement.setInt(1, ptarget.getObjectId());
			rset = statement.executeQuery();
			while(rset.next())
			{
				final int objectId = rset.getInt("friend_id");
				if(objectId == activeChar.getObjectId())
				{
					FoundOnFriendList = true;
					break;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		if(!FoundOnFriendList)
		{
			activeChar.sendMessage(new CustomMessage("scripts.commands.voiced.Wedding.NotInFriendlist"));
			return false;
		}
		ptarget.setEngageRequest(true, activeChar.getObjectId());
		final int id = ptarget.getObjectId();
		ptarget.ask(new ConfirmDlg(2010, 60000).addString("Player " + activeChar.getName() + " asking you to engage. Do you want to start new relationship?"), new OnAnswerListener(){
			@Override
			public void sayYes()
			{
				final Player p = GameObjectsStorage.getPlayer(id);
				if(p != null)
					p.engageAnswer(1);
			}

			@Override
			public void sayNo()
			{
				final Player p = GameObjectsStorage.getPlayer(id);
				if(p != null)
					p.engageAnswer(0);
			}
		});
		return true;
	}

	public boolean goToLove(final Player activeChar)
	{
		if(!activeChar.isMaried())
		{
			activeChar.sendMessage(new CustomMessage("scripts.commands.voiced.Wedding.YoureNotMarried"));
			return false;
		}
		if(activeChar.getPartnerId() == 0)
		{
			activeChar.sendMessage(new CustomMessage("scripts.commands.voiced.Wedding.PartnerNotInDB"));
			return false;
		}
		final Player partner = GameObjectsStorage.getPlayer(activeChar.getPartnerId());
		if(partner == null)
		{
			activeChar.sendMessage(new CustomMessage("scripts.commands.voiced.Wedding.PartnerOffline"));
			return false;
		}
		if(partner.isInOlympiadMode() || partner.isFestivalParticipant() || activeChar.isMovementDisabled() || activeChar.isMMuted() || activeChar.isInOlympiadMode() || activeChar.isInDuel() || activeChar.isFestivalParticipant() || activeChar.isCursedWeaponEquipped() || partner.isCursedWeaponEquipped() || activeChar.inObserverMode() || partner.inObserverMode())
		{
			activeChar.sendMessage(new CustomMessage("common.TryLater"));
			return false;
		}
		if(activeChar.isInParty() && activeChar.getParty().isInDimensionalRift() || partner.isInParty() && partner.getParty().isInDimensionalRift())
		{
			activeChar.sendMessage(new CustomMessage("common.TryLater"));
			return false;
		}
		if(activeChar.getTeleMode() != 0 || activeChar.isAlikeDead() || partner.isDead())
		{
			activeChar.sendMessage(new CustomMessage("common.TryLater"));
			return false;
		}
		if(partner.isInZoneBattle() || partner.isInZone(Zone.ZoneType.Siege) || partner.isInZone(Zone.ZoneType.epic) || partner.isInZone(Zone.ZoneType.no_restart) || partner.isInZone(Zone.ZoneType.OlympiadStadia) || activeChar.isInZoneBattle() || activeChar.isInZone(Zone.ZoneType.Siege) || activeChar.isInZone(Zone.ZoneType.epic) || activeChar.isInZone(Zone.ZoneType.no_restart) || activeChar.isInZone(Zone.ZoneType.OlympiadStadia))
		{
			activeChar.sendPacket(new SystemMessage(1899));
			return false;
		}
		activeChar.abortAttack(true, true);
		activeChar.abortCast(true, false);
		activeChar.sendActionFailed();
		activeChar.stopMove();
		final int teleportTimer = Config.WEDDING_TELEPORT_INTERVAL * 1000;
		if(activeChar.getInventory().getAdena() < Config.WEDDING_TELEPORT_PRICE)
		{
			activeChar.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return false;
		}
		activeChar.reduceAdena(Config.WEDDING_TELEPORT_PRICE, true);
		activeChar.sendMessage(new CustomMessage("scripts.commands.voiced.Wedding.Teleport").addNumber(teleportTimer / 60000));
		activeChar.broadcastPacket(new L2GameServerPacket[] { new MagicSkillUse(activeChar, activeChar, 1050, 1, teleportTimer, 0L) });
		activeChar.sendPacket(new SetupGauge(0, teleportTimer));
		activeChar._skillTask = ThreadPoolManager.getInstance().schedule(new EscapeFinalizer(activeChar, partner.getLoc()), Math.max(teleportTimer, 50));
		return true;
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return Wedding._voicedCommands;
	}

	@Override
	public void onLoad()
	{
		if(Config.ALLOW_WEDDING)
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
		Wedding._voicedCommands = new String[] { "divorce", "engage", "gotolove" };
	}

	static class EscapeFinalizer implements Runnable
	{
		private Player _activeChar;
		private Location _loc;

		EscapeFinalizer(final Player activeChar, final Location loc)
		{
			_activeChar = activeChar;
			_loc = loc;
		}

		@Override
		public void run()
		{
			_activeChar.sendActionFailed();
			_activeChar.clearCastVars();
			if(_activeChar.isDead())
				return;
			_activeChar.teleToLocation(_loc);
		}
	}
}
