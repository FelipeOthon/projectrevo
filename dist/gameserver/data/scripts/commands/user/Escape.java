package commands.user;

import l2s.gameserver.handler.IUserCommandHandler;
import l2s.gameserver.handler.UserCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.SkillTable;

public class Escape implements IUserCommandHandler, ScriptFile
{
	private static final int[] COMMAND_IDS = new int[] { 52 };

	@Override
	public boolean useUserCommand(final int id, final Player activeChar)
	{
		if(id != Escape.COMMAND_IDS[0])
			return false;
		if(activeChar.isMovementDisabled() || activeChar.isOutOfControl() || activeChar.isInOlympiadMode())
			return false;
		if(activeChar.getTeleMode() != 0)
		{
			activeChar.sendMessage(new CustomMessage("common.TryLater"));
			return false;
		}
		if(activeChar.isInDuel() || activeChar.getTeam() > 0)
		{
			activeChar.sendMessage(new CustomMessage("common.RecallInDuel"));
			return false;
		}
		activeChar.abortAttack(true, true);
		activeChar.abortCast(true, false);
		activeChar.stopMove();
		Skill skill;
		if(activeChar.getPlayerAccess().FastUnstuck)
			skill = SkillTable.getInstance().getInfo(2100, 1);
		else
			skill = SkillTable.getInstance().getInfo(2099, 1);
		if(skill != null && skill.checkCondition(activeChar, activeChar, false, false, true))
			activeChar.getAI().Cast(skill, activeChar, false, true);
		return true;
	}

	@Override
	public final int[] getUserCommandList()
	{
		return Escape.COMMAND_IDS;
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
}
