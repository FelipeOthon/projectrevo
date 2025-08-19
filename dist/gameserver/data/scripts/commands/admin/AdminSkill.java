package commands.admin;

import java.util.Collection;
import java.util.StringTokenizer;

import l2s.gameserver.cache.Msg;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.SkillLearn;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.network.l2.s2c.NpcHtmlMessage;
import l2s.gameserver.network.l2.s2c.SkillList;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.tables.SkillTree;
import l2s.gameserver.utils.Log;

public class AdminSkill implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;
	private static Skill[] adminSkills;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().CanEditChar)
			return false;
		if(command.equals("admin_show_skills"))
			showSkillsPage(activeChar);
		else if(command.startsWith("admin_remove_skills"))
			try
			{
				final String val = command.substring(20);
				removeSkillsPage(activeChar, Integer.parseInt(val));
			}
			catch(StringIndexOutOfBoundsException ex)
			{}
		else if(command.equals("admin_rs"))
			relock_skill(activeChar);
		else if(command.equals("admin_remove_all_skills"))
			adminRemoveAllSkills(activeChar);
		else if(command.startsWith("admin_skill_list"))
			AdminHelpPage.showHelpPage(activeChar, "skills.htm");
		else if(command.startsWith("admin_skill_index"))
			try
			{
				final String val = command.substring(18);
				AdminHelpPage.showHelpPage(activeChar, "skills/" + val + ".htm");
			}
			catch(StringIndexOutOfBoundsException ex2)
			{}
		else if(command.startsWith("admin_add_skill"))
			try
			{
				final String val = command.substring(15);
				adminAddSkill(activeChar, val);
			}
			catch(StringIndexOutOfBoundsException e)
			{
				activeChar.sendMessage("Error while adding skill.");
			}
		else if(command.startsWith("admin_remove_skill"))
			try
			{
				final String id = command.substring(19);
				final Integer idval = Integer.parseInt(id);
				adminRemoveSkill(activeChar, idval);
				activeChar.sendPacket(new SkillList(activeChar));
			}
			catch(StringIndexOutOfBoundsException e)
			{
				activeChar.sendMessage("Error while removing skill.");
			}
		else if(command.equals("admin_get_skills"))
			adminGetSkills(activeChar);
		else if(command.equals("admin_reset_skills"))
			adminResetSkills(activeChar);
		else if(command.equals("admin_give_all_skills"))
			adminGiveAllSkills(activeChar);
		return true;
	}

	private void relock_skill(final Player activeChar)
	{
		final GameObject target = activeChar.getTarget();
		if(target != null && target.isPlayer() && (activeChar == target || activeChar.getPlayerAccess().CanEditCharAll))
		{
			final Player player = (Player) target;
			player.resetReuse(false);
			activeChar.sendMessage("Renewal skills: " + player.getName());
			return;
		}
		activeChar.sendPacket(Msg.INCORRECT_TARGET);
	}

	private void adminGiveAllSkills(final Player activeChar)
	{
		final GameObject target = activeChar.getTarget();
		Player player = null;
		if(target != null && target.isPlayer() && (activeChar == target || activeChar.getPlayerAccess().CanEditCharAll))
		{
			player = (Player) target;
			int unLearnable = 0;
			int skillCounter = 0;
			for(SkillLearn[] skills = SkillTree.getInstance().getAvailableSkills(player, player.getClassId()); skills.length > unLearnable; skills = SkillTree.getInstance().getAvailableSkills(player, player.getClassId()))
			{
				unLearnable = 0;
				for(final SkillLearn s : skills)
				{
					final Skill sk = SkillTable.getInstance().getInfo(s.getId(), s.getLevel());
					if(sk == null || !sk.getCanLearn(player.getClassId()))
						++unLearnable;
					else
					{
						if(player.getSkillLevel(sk.getId()) == -1)
							++skillCounter;
						player.addSkill(sk, true);
					}
				}
			}
			player.sendPacket(new SkillList(player));
			player.updateStats();
			activeChar.sendMessage("You gave " + skillCounter + " skills to " + player.getName());
			if(activeChar.getObjectId() != player.getObjectId())
				player.sendMessage("Admin gave you " + skillCounter + " skills.");
			Log.addLog(activeChar.toString() + " gave " + skillCounter + " skills to player " + player.getName(), "gm_actions");
			return;
		}
		activeChar.sendPacket(Msg.INCORRECT_TARGET);
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminSkill._adminCommands;
	}

	private void removeSkillsPage(final Player activeChar, int page)
	{
		final GameObject target = activeChar.getTarget();
		if(target != null && target.isPlayer() && (activeChar == target || activeChar.getPlayerAccess().CanEditCharAll))
		{
			final Player player = (Player) target;
			final Skill[] skills = player.getAllSkillsArray();
			final int MaxSkillsPerPage = 10;
			int MaxPages = skills.length / MaxSkillsPerPage;
			if(skills.length > MaxSkillsPerPage * MaxPages)
				++MaxPages;
			if(page > MaxPages)
				page = MaxPages;
			final int SkillsStart = MaxSkillsPerPage * page;
			int SkillsEnd = skills.length;
			if(SkillsEnd - SkillsStart > MaxSkillsPerPage)
				SkillsEnd = SkillsStart + MaxSkillsPerPage;
			final NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
			final StringBuffer replyMSG = new StringBuffer("<html><body>");
			replyMSG.append("<table width=260><tr>");
			replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
			replyMSG.append("<td width=180><center>Character Selection Menu</center></td>");
			replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_show_skills\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
			replyMSG.append("</tr></table>");
			replyMSG.append("<br>");
			replyMSG.append("<center>Editing character: <font color=\"LEVEL\">" + player.getName() + "</font></center>");
			replyMSG.append("<br><table width=270><tr><td>Lv: " + player.getLevel() + " " + player.getTemplate().className + "</td></tr></table>");
			replyMSG.append("<br><center>Click on the skill you wish to remove:</center>");
			replyMSG.append("<br>");
			String pages = "<center><table width=270><tr>";
			for(int x = 0; x < MaxPages; ++x)
			{
				final int pagenr = x + 1;
				pages = pages + "<td><a action=\"bypass -h admin_remove_skills " + x + "\"><font color=\"" + (x == page ? "33ff00" : "ffffff") + "\">P" + pagenr + "</font></a></td>";
			}
			pages += "</tr></table></center>";
			replyMSG.append(pages);
			replyMSG.append("<br><table width=270>");
			replyMSG.append("<tr><td width=80>Name:</td><td width=60>Level:</td><td width=40>Id:</td></tr>");
			for(int i = SkillsStart; i < SkillsEnd; ++i)
				replyMSG.append("<tr><td width=80><a action=\"bypass -h admin_remove_skill " + skills[i].getId() + "\">" + skills[i].getName() + "</a></td><td width=60>" + skills[i].getLevel() + "</td><td width=40>" + skills[i].getId() + "</td></tr>");
			replyMSG.append("</table>");
			replyMSG.append("<br><center>");
			replyMSG.append("Remove skill by ID:");
			replyMSG.append("<edit var=\"id_to_remove\" width=50>");
			replyMSG.append("<button value=\"Remove skill\" action=\"bypass -h admin_remove_skill $id_to_remove\" width=90 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
			replyMSG.append("<br><button value=\"Back\" action=\"bypass -h admin_current_player\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
			replyMSG.append("</body></html>");
			adminReply.setHtml(replyMSG.toString());
			activeChar.sendPacket(adminReply);
			return;
		}
		activeChar.sendPacket(Msg.INCORRECT_TARGET);
	}

	private void showSkillsPage(final Player activeChar)
	{
		final GameObject target = activeChar.getTarget();
		if(target != null && target.isPlayer() && (activeChar == target || activeChar.getPlayerAccess().CanEditCharAll))
		{
			final Player player = (Player) target;
			final NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
			final StringBuffer replyMSG = new StringBuffer("<html><body>");
			replyMSG.append("<table width=260><tr>");
			replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
			replyMSG.append("<td width=180><center>Character Selection Menu</center></td>");
			replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_current_player\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
			replyMSG.append("</tr></table>");
			replyMSG.append("<br><br>");
			replyMSG.append("<center>Editing character: <font color=\"LEVEL\">" + player.getName() + "</font></center>");
			replyMSG.append("<br><table width=270><tr><td>Lv: " + player.getLevel() + " " + player.getTemplate().className + "</td></tr></table>");
			replyMSG.append("<br><table width=270><tr><td>Note: Dont forget that modifying players skills can</td></tr>");
			replyMSG.append("<tr><td>ruin the game...</td></tr></table>");
			replyMSG.append("<br><center><table>");
			replyMSG.append("<tr><td><button value=\"Add skills\" action=\"bypass -h admin_skill_list\" width=85 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
			replyMSG.append("<td><button value=\"Get skills\" action=\"bypass -h admin_get_skills\" width=85 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
			replyMSG.append("<tr><td><button value=\"Delete skills\" action=\"bypass -h admin_remove_skills 0\" width=85 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
			replyMSG.append("<td><button value=\"Reset skills\" action=\"bypass -h admin_reset_skills\" width=85 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
			replyMSG.append("<tr><td><button value=\"Give All Skills\" action=\"bypass -h admin_give_all_skills\" width=85 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
			replyMSG.append("<td><button value=\"Delete All Skills\" action=\"bypass -h admin_remove_all_skills\" width=85 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
			replyMSG.append("</table></center>");
			replyMSG.append("</body></html>");
			adminReply.setHtml(replyMSG.toString());
			activeChar.sendPacket(adminReply);
			return;
		}
		activeChar.sendPacket(Msg.INCORRECT_TARGET);
	}

	private void adminGetSkills(final Player activeChar)
	{
		final GameObject target = activeChar.getTarget();
		if(target != null && target.isPlayer() && (activeChar == target || activeChar.getPlayerAccess().CanEditCharAll))
		{
			final Player player = (Player) target;
			if(player.getName().equals(activeChar.getName()))
				player.sendMessage("There is no point in doing it on your character.");
			else
			{
				final Collection<Skill> skills = player.getAllSkills();
				AdminSkill.adminSkills = activeChar.getAllSkillsArray();
				for(final Skill element : AdminSkill.adminSkills)
					activeChar.removeSkill(element, true);
				for(final Skill element2 : skills)
					activeChar.addSkill(element2, true);
				activeChar.updateStats();
				activeChar.sendMessage("You now have all the skills of  " + player.getName() + ".");
			}
			showSkillsPage(activeChar);
			return;
		}
		activeChar.sendPacket(Msg.INCORRECT_TARGET);
	}

	private void adminResetSkills(final Player activeChar)
	{
		final GameObject target = activeChar.getTarget();
		Player player = null;
		if(target != null && target.isPlayer() && (activeChar == target || activeChar.getPlayerAccess().CanEditCharAll))
		{
			player = (Player) target;
			if(AdminSkill.adminSkills == null)
				activeChar.sendMessage("You must first get the skills of someone to do this.");
			else
			{
				final Skill[] allSkillsArray;
				final Skill[] skills = allSkillsArray = player.getAllSkillsArray();
				for(final Skill element : allSkillsArray)
					player.removeSkill(element, true);
				for(final Skill s : activeChar.getAllSkills())
					player.addSkill(s, true);
				for(final Skill element : skills)
					activeChar.removeSkill(element, true);
				for(final Skill element : AdminSkill.adminSkills)
					activeChar.addSkill(element);
				activeChar.updateStats();
				player.sendMessage("[GM]" + activeChar.getName() + " has updated your skills.");
				activeChar.sendMessage("You now have all your skills back.");
				AdminSkill.adminSkills = null;
			}
			showSkillsPage(activeChar);
			return;
		}
		activeChar.sendPacket(Msg.INCORRECT_TARGET);
	}

	private void adminAddSkill(final Player activeChar, final String val)
	{
		final GameObject target = activeChar.getTarget();
		if(target != null && target.isPlayer() && (activeChar == target || activeChar.getPlayerAccess().CanEditCharAll))
		{
			final Player player = (Player) target;
			final StringTokenizer st = new StringTokenizer(val);
			if(st.countTokens() != 2)
				showSkillsPage(activeChar);
			else
			{
				final String id = st.nextToken();
				final String level = st.nextToken();
				final int idval = Integer.parseInt(id);
				final int levelval = Integer.parseInt(level);
				final Skill skill = SkillTable.getInstance().getInfo(idval, levelval);
				if(skill != null)
				{
					player.sendMessage("Admin gave you the skill " + skill.getName() + ".");
					player.addSkill(skill, true);
					player.sendPacket(new SkillList(player));
					player.updateStats();
					activeChar.sendMessage("You gave the skill " + skill.getName() + " to " + player.getName() + ".");
					Log.addLog(activeChar.toString() + " gave the skill " + skill.getName() + " to player " + player.getName(), "gm_actions");
				}
				else
					activeChar.sendMessage("Error: there is no such skill.");
				showSkillsPage(activeChar);
			}
			return;
		}
		activeChar.sendPacket(Msg.INCORRECT_TARGET);
	}

	private void adminRemoveSkill(final Player activeChar, final Integer idval)
	{
		final GameObject target = activeChar.getTarget();
		if(target != null && target.isPlayer() && (activeChar == target || activeChar.getPlayerAccess().CanEditCharAll))
		{
			final Player player = (Player) target;
			final int level = player.getSkillLevel(idval);
			final Skill skill = SkillTable.getInstance().getInfo(idval, level);
			if(skill != null)
			{
				player.sendMessage("Admin removed the skill " + skill.getName() + ".");
				player.removeSkill(skill, true);
				player.removeSkillFromShortCut(skill.getId());
				activeChar.sendMessage("You removed the skill " + skill.getName() + " from " + player.getName() + ".");
				Log.addLog(activeChar.toString() + " removed the skill " + skill.getName() + " from player " + player.getName(), "gm_actions");
			}
			else
				activeChar.sendMessage("Error: there is no such skill.");
			removeSkillsPage(activeChar, 0);
			return;
		}
		activeChar.sendPacket(Msg.INCORRECT_TARGET);
	}

	private void adminRemoveAllSkills(final Player activeChar)
	{
		final GameObject target = activeChar.getTarget();
		if(target != null && target.isPlayer() && (activeChar == target || activeChar.getPlayerAccess().CanEditCharAll))
		{
			final Player player = (Player) target;
			final Skill[] allSkillsArray;
			final Skill[] skills = allSkillsArray = player.getAllSkillsArray();
			for(final Skill sk : allSkillsArray)
			{
				player.removeSkill(sk, true);
				player.removeSkillFromShortCut(sk.getId());
			}
			player.sendMessage("Admin removed all your skills.");
			return;
		}
		activeChar.sendPacket(Msg.INCORRECT_TARGET);
	}

	public void showSkill(final Player activeChar, final String val)
	{
		final int skillid = Integer.parseInt(val);
		final Skill skill = SkillTable.getInstance().getInfo(skillid, 1);
		if(skill != null)
		{
			if(skill.getTargetType() == Skill.SkillTargetType.TARGET_SELF)
				activeChar.broadcastPacket(new L2GameServerPacket[] {
						new MagicSkillUse(activeChar, activeChar, skillid, 1, skill.getHitTime(), skill.getReuseDelay()) });
		}
		else
			activeChar.broadcastPacket(new L2GameServerPacket[] { Msg.ActionFail });
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
		AdminSkill._adminCommands = new String[] {
				"admin_show_skills",
				"admin_remove_skills",
				"admin_skill_list",
				"admin_skill_index",
				"admin_add_skill",
				"admin_remove_skill",
				"admin_get_skills",
				"admin_reset_skills",
				"admin_give_all_skills",
				"admin_rs",
				"admin_remove_all_skills" };
	}
}
