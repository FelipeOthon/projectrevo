package events.Viktorina;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbcp.DbUtils;
import l2s.commons.util.Rnd;
import l2s.gameserver.Announcements;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.handler.IVoicedCommandHandler;
import l2s.gameserver.handler.VoicedCommandHandler;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.listener.actor.CharListenerList;
import l2s.gameserver.listener.actor.OnPlayerEnterListener;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.components.ChatType;
import l2s.gameserver.network.l2.s2c.Say2;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;

public class Viktorina extends Functions implements ScriptFile, IVoicedCommandHandler, OnPlayerEnterListener
{
	private static final Logger _log;
	private String[] _commandList;
	private ArrayList<String> questions;
	private static ArrayList<Player> playerList;
	static ScheduledFuture<?> _taskViktorinaStart;
	static ScheduledFuture<?> _taskStartQuestion;
	static ScheduledFuture<?> _taskStopQuestion;
	long _timeStopViktorina;
	private static boolean status;
	private static boolean _questionStatus;
	private static int index;
	private static String question;
	private static String answer;
	private static final String GET_LIST_FASTERS = "SELECT `obj_id`,`value` FROM `character_variables` WHERE `name`='viktorinafirst' ORDER BY `value` DESC LIMIT 0,10";
	private static final String GET_LIST_TOP = "SELECT `obj_id`,`value` FROM `character_variables` WHERE `name`='viktorinaschet' ORDER BY `value` DESC LIMIT 0,10";
	private static Viktorina instance;

	public Viktorina()
	{
		_commandList = new String[] { "o", "voff", "von", "vhelp", "vtop", "v", "va" };
		questions = new ArrayList<String>();
		_timeStopViktorina = 0L;
	}

	public static Viktorina getInstance()
	{
		if(Viktorina.instance == null)
			Viktorina.instance = new Viktorina();
		return Viktorina.instance;
	}

	public void loadQuestions()
	{
		final File file = new File(Config.DATAPACK_ROOT + "/data/questions.txt");
		try
		{
			final BufferedReader br = new BufferedReader(new FileReader(file));
			String str;
			while((str = br.readLine()) != null)
				questions.add(str);
			br.close();
			Viktorina._log.info("Viktorina Event: Questions loaded.");
		}
		catch(Exception e)
		{
			Viktorina._log.error("Viktorina Event: Error parsing questions file. " + e);
			e.printStackTrace();
		}
	}

	public void saveQuestions()
	{
		if(!Config.VIKTORINA_REMOVE_QUESTION)
			return;
		final File file = new File(Config.DATAPACK_ROOT + "/data/questions.txt");
		try
		{
			final BufferedWriter br = new BufferedWriter(new FileWriter(file));
			for(final String str : questions)
				br.write(str + "\r\n");
			br.close();
			Viktorina._log.info("Viktorina Event: Questions saved.");
		}
		catch(Exception e)
		{
			Viktorina._log.error("Viktorina Event: Error save questions file. " + e);
			e.printStackTrace();
		}
	}

	public void parseQuestion()
	{
		Viktorina.index = Rnd.get(questions.size());
		final String str = questions.get(Viktorina.index);
		final StringTokenizer st = new StringTokenizer(str, "|");
		Viktorina.question = st.nextToken();
		Viktorina.answer = st.nextToken();
	}

	public void checkAnswer(final String chat, final Player player)
	{
		if(chat.equalsIgnoreCase(Viktorina.answer) && isQuestionStatus() && !Viktorina.playerList.contains(player))
			Viktorina.playerList.add(player);
	}

	public void announseViktorina(final String text)
	{
		final Say2 cs = new Say2(0, ChatType.TELL, "\u0412\u0438\u043a\u0442\u043e\u0440\u0438\u043d\u0430", text);
		for(final Player player : GameObjectsStorage.getPlayers())
			if(player.getVar("viktorina") == "on")
				player.sendPacket(cs);
	}

	public void checkPlayers()
	{
		final Say2 cs = new Say2(0, ChatType.TELL, "\u0412\u0438\u043a\u0442\u043e\u0440\u0438\u043d\u0430", "\u0427\u0442\u043e\u0431\u044b \u043e\u0442\u043a\u0430\u0437\u0430\u0442\u044c\u0441\u044f \u043e\u0442 \u0443\u0447\u0430\u0441\u0442\u0438\u044f \u0432 \u0412\u0438\u043a\u0442\u043e\u0440\u0438\u043d\u0435 \u0432\u0432\u0435\u0434\u0438\u0442\u0435 .voff  \u0414\u043b\u044f \u0441\u043f\u0440\u0430\u0432\u043a\u0438 \u0432\u0432\u0435\u0434\u0438\u0442\u0435 .vhelp");
		for(final Player player : GameObjectsStorage.getPlayers())
			if(player.getVar("viktorina") == null)
			{
				player.sendPacket(cs);
				player.setVar("viktorina", "on", -1L);
			}
	}

	public void viktorinaSay(final Player player, final String text)
	{
		final Say2 cs = new Say2(0, ChatType.TELL, "\u0412\u0438\u043a\u0442\u043e\u0440\u0438\u043d\u0430", text);
		if(player.getVar("viktorina") == "on")
			player.sendPacket(cs);
	}

	public void winners()
	{
		if(!isStatus())
			return;
		if(isQuestionStatus())
			return;
		if(ServerVariables.getString("viktorinaq") == null)
			ServerVariables.set("viktorinaq", 0);
		if(ServerVariables.getString("viktorinaa") == null)
			ServerVariables.set("viktorinaa", 0);
		if(Viktorina.playerList.size() > 0)
		{
			announseViktorina(" \u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u044b\u0445 \u043e\u0442\u0432\u0435\u0442\u043e\u0432: " + Viktorina.playerList.size() + ", \u043f\u0435\u0440\u0432\u044b\u0439 \u043e\u0442\u0432\u0435\u0442\u0438\u043b: " + Viktorina.playerList.get(0).getName() + ", \u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u044b\u0439 \u043e\u0442\u0432\u0435\u0442: " + Viktorina.answer + "");
			ServerVariables.set("viktorinaq", ServerVariables.getInt("viktorinaq") + 1);
			ServerVariables.set("viktorinaa", ServerVariables.getInt("viktorinaa") + 1);
			if(Config.VIKTORINA_REMOVE_QUESTION)
				questions.remove(Viktorina.index);
		}
		else
		{
			if(Config.VIKTORINA_REMOVE_QUESTION_NO_ANSWER)
				announseViktorina(" \u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u043e\u0433\u043e \u043e\u0442\u0432\u0435\u0442\u0430 \u043d\u0435 \u043f\u043e\u0441\u0442\u0443\u043f\u0438\u043b\u043e, \u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u044b\u0439 \u043e\u0442\u0432\u0435\u0442 \u0431\u044b\u043b: " + Viktorina.answer + "");
			if(!Config.VIKTORINA_REMOVE_QUESTION_NO_ANSWER)
				announseViktorina(" \u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u043e\u0433\u043e \u043e\u0442\u0432\u0435\u0442\u0430 \u043d\u0435 \u043f\u043e\u0441\u0442\u0443\u043f\u0438\u043b\u043e");
			ServerVariables.set("viktorinaq", ServerVariables.getInt("viktorinaq") + 1);
			if(Config.VIKTORINA_REMOVE_QUESTION && Config.VIKTORINA_REMOVE_QUESTION_NO_ANSWER)
				questions.remove(Viktorina.index);
		}
	}

	public void Start()
	{
		if(Viktorina._taskViktorinaStart != null)
			Viktorina._taskViktorinaStart.cancel(true);
		final Calendar _timeStartViktorina = Calendar.getInstance();
		_timeStartViktorina.set(11, Config.VIKTORINA_START_TIME_HOUR);
		_timeStartViktorina.set(12, Config.VIKTORINA_START_TIME_MIN);
		_timeStartViktorina.set(13, 0);
		_timeStartViktorina.set(14, 0);
		final Calendar _timeStopViktorina = Calendar.getInstance();
		_timeStopViktorina.setTimeInMillis(_timeStartViktorina.getTimeInMillis());
		_timeStopViktorina.add(11, Config.VIKTORINA_WORK_TIME);
		final long currentTime = System.currentTimeMillis();
		if(_timeStartViktorina.getTimeInMillis() >= currentTime)
			Viktorina._taskViktorinaStart = ThreadPoolManager.getInstance().schedule(new ViktorinaStart(_timeStopViktorina.getTimeInMillis()), _timeStartViktorina.getTimeInMillis() - currentTime);
		else if(currentTime > _timeStartViktorina.getTimeInMillis() && currentTime < _timeStopViktorina.getTimeInMillis())
			Viktorina._taskViktorinaStart = ThreadPoolManager.getInstance().schedule(new ViktorinaStart(_timeStopViktorina.getTimeInMillis()), 1000L);
		else
		{
			_timeStartViktorina.add(11, 24);
			_timeStopViktorina.add(11, 24);
			Viktorina._taskViktorinaStart = ThreadPoolManager.getInstance().schedule(new ViktorinaStart(_timeStopViktorina.getTimeInMillis()), _timeStartViktorina.getTimeInMillis() - currentTime);
		}
	}

	public void Continue()
	{
		if(Viktorina._taskViktorinaStart != null)
			Viktorina._taskViktorinaStart.cancel(true);
		final Calendar _timeStartViktorina = Calendar.getInstance();
		_timeStartViktorina.set(11, Config.VIKTORINA_START_TIME_HOUR);
		_timeStartViktorina.set(12, Config.VIKTORINA_START_TIME_MIN);
		_timeStartViktorina.set(13, 0);
		_timeStartViktorina.set(14, 0);
		final Calendar _timeStopViktorina = Calendar.getInstance();
		_timeStopViktorina.setTimeInMillis(_timeStartViktorina.getTimeInMillis());
		_timeStopViktorina.add(11, Config.VIKTORINA_WORK_TIME);
		_timeStartViktorina.add(11, 24);
		_timeStopViktorina.add(11, 24);
		final long currentTime = System.currentTimeMillis();
		Viktorina._taskViktorinaStart = ThreadPoolManager.getInstance().schedule(new ViktorinaStart(_timeStopViktorina.getTimeInMillis()), _timeStartViktorina.getTimeInMillis() - currentTime);
	}

	public void ForseStart()
	{
		if(Viktorina._taskViktorinaStart != null)
			Viktorina._taskViktorinaStart.cancel(true);
		final Calendar _timeStartViktorina = Calendar.getInstance();
		final Calendar _timeStopViktorina = Calendar.getInstance();
		_timeStopViktorina.setTimeInMillis(_timeStartViktorina.getTimeInMillis());
		_timeStopViktorina.add(11, Config.VIKTORINA_WORK_TIME);
		Viktorina._log.info("Viktorina Started.");
		Viktorina._taskViktorinaStart = ThreadPoolManager.getInstance().schedule(new ViktorinaStart(_timeStopViktorina.getTimeInMillis()), 1000L);
	}

	public void stop()
	{
		Viktorina.playerList.clear();
		if(Viktorina._taskStartQuestion != null)
			Viktorina._taskStartQuestion.cancel(true);
		if(Viktorina._taskStopQuestion != null)
			Viktorina._taskStopQuestion.cancel(true);
		setQuestionStatus(false);
		if(isStatus())
			Announcements.getInstance().announceToAll("\u0412\u0438\u043a\u0442\u043e\u0440\u0438\u043d\u0430 \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u0430!");
		setStatus(false);
		Continue();
	}

	public void help(final Player player)
	{
		int schet;
		if(player.getVar("viktorinaschet") == null)
			schet = 0;
		else
			schet = Integer.parseInt(player.getVar("viktorinaschet"));
		int first;
		if(player.getVar("viktorinafirst") == null)
			first = 0;
		else
			first = Integer.parseInt(player.getVar("viktorinafirst"));
		int vq;
		if(ServerVariables.getString("viktorinaq", "0") == "0")
		{
			ServerVariables.set("viktorinaq", 0);
			vq = 0;
		}
		else
			vq = Integer.parseInt(ServerVariables.getString("viktorinaq"));
		int va;
		if(ServerVariables.getString("viktorinaa", "0") == "0")
		{
			ServerVariables.set("viktorinaa", 0);
			va = 0;
		}
		else
			va = Integer.parseInt(ServerVariables.getString("viktorinaa"));
		String vstatus;
		if(player.getVar("viktorina") == "on")
			vstatus = "<font color=\"00FF00\">\u0412\u044b \u0443\u0447\u0430\u0441\u0442\u0432\u0443\u0435\u0442\u0435 \u0432 \u0412\u0438\u043a\u0442\u043e\u0440\u0438\u043d\u0435</font><br>";
		else
			vstatus = "<font color=\"FF0000\">\u0412\u044b \u043d\u0435 \u0443\u0447\u0430\u0441\u0442\u0432\u0443\u0435\u0442\u0435 \u0432 \u0412\u0438\u043a\u0442\u043e\u0440\u0438\u043d\u0435</font><br>";
		final String begin = Config.VIKTORINA_ENABLED ? Config.VIKTORINA_START_TIME_HOUR + ":" + Config.VIKTORINA_START_TIME_MIN : "\u043d\u0435 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u043e";
		final StringBuffer help = new StringBuffer("<html><body>");
		help.append("<center>\u041f\u043e\u043c\u043e\u0449\u044c \u043f\u043e \u0412\u0438\u043a\u0442\u043e\u0440\u0438\u043d\u0435<br></center>");
		help.append(vstatus);
		help.append("\u0412\u0440\u0435\u043c\u044f \u043d\u0430\u0447\u0430\u043b\u0430 \u0412\u0438\u043a\u0442\u043e\u0440\u0438\u043d\u044b: " + begin + "<br>");
		help.append("\u0414\u043b\u0438\u0442\u0435\u043b\u044c\u043d\u043e\u0441\u0442\u044c \u0440\u0430\u0431\u043e\u0442\u044b \u0412\u0438\u043a\u0442\u043e\u0440\u0438\u043d\u044b: " + Config.VIKTORINA_WORK_TIME + " \u0447.<br>");
		help.append("\u0412\u0440\u0435\u043c\u044f \u0432 \u0442\u0435\u0447\u0435\u043d\u0438\u0438 \u043a\u043e\u0442\u043e\u0440\u043e\u0433\u043e \u043c\u043e\u0436\u043d\u043e \u0434\u0430\u0442\u044c \u043e\u0442\u0432\u0435\u0442: " + Config.VIKTORINA_TIME_ANSER + " \u0441\u0435\u043a.<br>");
		help.append("\u0412\u0440\u0435\u043c\u044f \u043c\u0435\u0436\u0434\u0443 \u0432\u043e\u043f\u0440\u043e\u0441\u0430\u043c\u0438: " + (Config.VIKTORINA_TIME_ANSER + Config.VIKTORINA_TIME_PAUSE) + " \u0441\u0435\u043a.<br>");
		help.append("\u0412\u043e\u043f\u0440\u043e\u0441\u043e\u0432 \u0431\u044b\u043b\u043e \u0437\u0430\u0434\u0430\u043d\u043e: " + vq + ".<br>");
		help.append("\u0412\u0435\u0440\u043d\u043e \u043e\u0442\u0432\u0435\u0442\u0438\u043b\u0438 \u043d\u0430: " + va + ".<br>");
		help.append("\u0412\u044b \u0432\u0435\u0440\u043d\u043e \u043e\u0442\u0432\u0435\u0442\u0438\u043b\u0438 \u043d\u0430: " + schet + ", \u0432 " + first + " \u0432\u044b \u0431\u044b\u043b\u0438 \u043f\u0435\u0440\u0432\u044b\u043c.<br>");
		help.append("<br>");
		help.append("<center>\u041a\u043e\u043c\u0430\u043d\u0434\u044b \u0412\u0438\u043a\u0442\u043e\u0440\u0438\u043d\u044b:<br></center>");
		help.append("<font color=\"LEVEL\">.von</font> - \u043a\u043e\u043c\u0430\u043d\u0434\u0430 \u0434\u043b\u044f \u0443\u0447\u0430\u0441\u0442\u0438\u044f \u0432 \u0412\u0438\u043a\u0442\u043e\u0440\u0438\u043d\u0435.<br>");
		help.append("<font color=\"LEVEL\">.voff</font> - \u043a\u043e\u043c\u0430\u043d\u0434\u0430 \u0434\u043b\u044f \u043e\u0442\u043a\u0430\u0437\u0430 \u043e\u0442 \u0443\u0447\u0430\u0441\u0442\u0438\u044f.<br>");
		help.append("<font color=\"LEVEL\">.vtop</font> - \u043a\u043e\u043c\u0430\u043d\u0434\u0430 \u0434\u043b\u044f \u043f\u0440\u043e\u0441\u043c\u043e\u0442\u0440\u0430 \u0440\u0435\u0437\u0443\u043b\u044c\u0442\u0430\u0442\u043e\u0432.<br>");
		help.append("<font color=\"LEVEL\">.vhelp</font> - \u043a\u043e\u043c\u0430\u043d\u0434\u0430 \u0434\u043b\u044f \u0432\u044b\u0437\u043e\u0432\u0430 \u044d\u0442\u043e\u0439 \u0441\u0442\u0440\u0430\u043d\u0438\u0446\u044b.<br>");
		help.append("<font color=\"LEVEL\">.v</font> - \u043f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u0442\u0435\u043a\u0443\u0449\u0438\u0439 \u0432\u043e\u043f\u0440\u043e\u0441.<br>");
		help.append("<font color=\"LEVEL\">\u041e\u0442\u0432\u0435\u0442</font> - \u0432\u0432\u043e\u0434\u0438\u0442\u0441\u044f \u0432 \u043b\u044e\u0431\u043e\u0439 \u0432\u0438\u0434 \u0447\u0430\u0442\u0430.<br>");
		help.append("</body></html>");
		show(help.toString(), player);
	}

	public void top(final Player player)
	{
		final StringBuffer top = new StringBuffer("<html><body>");
		top.append("<center>\u0422\u043e\u043f \u0421\u0430\u043c\u044b\u0445 \u0411\u044b\u0441\u0442\u0440\u044b\u0445");
		top.append("<img src=\"L2UI.SquareWhite\" width=270 height=1><img src=\"L2UI.SquareBlank\" width=1 height=3>");
		final List<Scores> fasters = getList(true);
		if(fasters.size() != 0)
		{
			top.append("<table width=300 border=0 bgcolor=\"000000\">");
			int index = 1;
			for(final Scores faster : fasters)
			{
				top.append("<tr>");
				top.append("<td><center>" + index + "<center></td>");
				top.append("<td><center>" + faster.getName() + "<center></td>");
				top.append("<td><center>" + faster.getScore() + "<center></td>");
				top.append("</tr>");
				++index;
			}
			top.append("<tr><td><br></td><td></td></tr>");
			top.append("</table>");
		}
		top.append("<img src=\"L2UI.SquareWhite\" width=270 height=1> <img src=\"L2UI.SquareBlank\" width=1 height=3>");
		top.append("</center>");
		top.append("<center>\u041e\u0431\u0449\u0438\u0439 \u0442\u043e\u043f");
		top.append("<img src=\"L2UI.SquareWhite\" width=270 height=1><img src=\"L2UI.SquareBlank\" width=1 height=3>");
		final List<Scores> top2 = getList(false);
		if(top2.size() != 0)
		{
			top.append("<table width=300 border=0 bgcolor=\"000000\">");
			int index2 = 1;
			for(final Scores top3 : top2)
			{
				top.append("<tr>");
				top.append("<td><center>" + index2 + "<center></td>");
				top.append("<td><center>" + top3.getName() + "<center></td>");
				top.append("<td><center>" + top3.getScore() + "<center></td>");
				top.append("</tr>");
				++index2;
			}
			top.append("<tr><td><br></td><td></td></tr>");
			top.append("</table>");
		}
		top.append("<img src=\"L2UI.SquareWhite\" width=270 height=1> <img src=\"L2UI.SquareBlank\" width=1 height=3>");
		top.append("</center>");
		top.append("</body></html>");
		show(top.toString(), player);
	}

	public void setQuestionStatus(final boolean b)
	{
		Viktorina._questionStatus = b;
	}

	public boolean isQuestionStatus()
	{
		return Viktorina._questionStatus;
	}

	@Override
	public void onLoad()
	{
		CharListenerList.addGlobal(this);
		executeTask("events.Viktorina.Viktorina", "preLoad", new Object[0], 20000L);
		VoicedCommandHandler.getInstance().registerVoicedCommandHandler(this);
		Viktorina._log.info("Loaded Event: Viktorina");
	}

	@Override
	public void onReload()
	{
		stop();
	}

	@Override
	public void onShutdown()
	{
		stop();
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return _commandList;
	}

	@Override
	public boolean useVoicedCommand(final String command, final Player player, final String args)
	{
		if(command.equals("o"))
		{
			if(args.equalsIgnoreCase(Viktorina.answer) && isQuestionStatus() && !Viktorina.playerList.contains(player))
				Viktorina.playerList.add(player);
			if(!isQuestionStatus())
				viktorinaSay(player, "\u0412\u043e\u0437\u043c\u043e\u0436\u043d\u043e \u0432\u043e\u043f\u0440\u043e\u0441 \u043d\u0435 \u0431\u044b\u043b \u0437\u0430\u0434\u0430\u043d, \u0438\u043b\u0438 \u0436\u0435 \u0432\u0440\u0435\u043c\u044f \u043e\u0442\u0432\u0435\u0442\u0430 \u0438\u0441\u0442\u0435\u043a\u043b\u043e.");
		}
		if(command.equals("von"))
		{
			player.setVar("viktorina", "on", -1L);
			player.sendMessage("\u0412\u044b \u043f\u0440\u0438\u043d\u0438\u043c\u0430\u0435\u0442\u0435 \u0443\u0447\u0430\u0441\u0442\u0438\u0435 \u0432 \u0412\u0438\u043a\u0442\u043e\u0440\u0438\u043d\u0435!");
			player.sendMessage("\u041e\u0436\u0438\u0434\u0430\u0439\u0442\u0435 \u043f\u043e\u0441\u0442\u0443\u043f\u043b\u0435\u043d\u0438\u044f \u0432\u043e\u043f\u0440\u043e\u0441\u0430 \u0432 \u041f\u041c!");
		}
		if(command.equals("voff"))
		{
			player.setVar("viktorina", "off", -1L);
			player.sendMessage("\u0412\u044b \u043e\u0442\u043a\u0430\u0437\u0430\u043b\u0438\u0441\u044c \u043e\u0442 \u0443\u0447\u0430\u0441\u0442\u0438\u044f \u0432 \u0412\u0438\u043a\u0442\u043e\u0440\u0438\u043d\u0435!");
			player.sendMessage("\u0414\u043e \u043d\u043e\u0432\u044b\u0445 \u0432\u0441\u0442\u0440\u0435\u0447!");
		}
		if(command.equals("vhelp"))
			help(player);
		if(command.equals("vtop"))
			top(player);
		if(command.equals("v"))
			viktorinaSay(player, Viktorina.question);
		if(command.equals("va") && player.isGM())
			viktorinaSay(player, Viktorina.answer);
		return true;
	}

	private void rewarding()
	{
		if(!isStatus())
			return;
		if(isQuestionStatus())
			return;
		for(final Player player : Viktorina.playerList)
		{
			int schet;
			if(player.getVar("viktorinaschet") == null)
				schet = 0;
			else
				schet = Integer.parseInt(player.getVar("viktorinaschet"));
			int first;
			if(player.getVar("viktorinafirst") == null)
				first = 0;
			else
				first = Integer.parseInt(player.getVar("viktorinafirst"));
			if(player == Viktorina.playerList.get(0))
			{
				giveItemByChance(player, true);
				player.setVar("viktorinafirst", "" + (first + 1) + "", -1L);
			}
			else
				giveItemByChance(player, false);
			player.setVar("viktorinaschet", "" + (schet + 1) + "", -1L);
		}
	}

	private void giveItemByChance(final Player player, final boolean first)
	{
		final String[] st = first ? Config.VIKTORINA_REWARD_FIRST.split(";") : Config.VIKTORINA_REWARD_OTHER.split(";");
		for(int i = 0; i < st.length; ++i)
		{
			final String[] s = st[i].split(",");
			if(s.length == 3)
			{
				final int itemId = Integer.parseInt(s[0]);
				final int count = Integer.parseInt(s[1]);
				final int chance = Integer.parseInt(s[2]);
				if(Rnd.chance(chance))
					addItem(player, itemId, count);
			}
		}
	}

	private static boolean isStatus()
	{
		return Viktorina.status;
	}

	public static boolean isRunned()
	{
		return Viktorina.status;
	}

	private static void setStatus(final boolean param)
	{
		Viktorina.status = param;
	}

	private String getName(final int char_id)
	{
		String name = null;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT char_name FROM characters WHERE obj_Id=?");
			statement.setInt(1, char_id);
			rset = statement.executeQuery();
			rset.next();
			name = rset.getString("char_name");
		}
		catch(SQLException e)
		{
			Viktorina._log.error("Victorina can not find char obj_Id: " + char_id + " " + e.getMessage());
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return name;
	}

	private List<Scores> getList(final boolean first)
	{
		final List<Scores> names = new ArrayList<Scores>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		String GET_LIST = null;
		if(first)
			GET_LIST = "SELECT `obj_id`,`value` FROM `character_variables` WHERE `name`='viktorinafirst' ORDER BY `value` DESC LIMIT 0,10";
		else
			GET_LIST = "SELECT `obj_id`,`value` FROM `character_variables` WHERE `name`='viktorinaschet' ORDER BY `value` DESC LIMIT 0,10";
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(GET_LIST);
			rset = statement.executeQuery();
			while(rset.next())
			{
				final String name = getName(rset.getInt("obj_id"));
				final int score = rset.getInt("value");
				final Scores scores = new Scores();
				scores.setName(name);
				scores.setScore(score);
				names.add(scores);
			}
			return names;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return names;
	}

	public static void preLoad()
	{
		if(Config.VIKTORINA_ENABLED)
			executeTask("events.Viktorina.Viktorina", "Start", new Object[0], 5000L);
	}

	@Override
	public void onPlayerEnter(final Player player)
	{
		if(isStatus())
			player.sendPacket(new Say2(0, ChatType.CRITICAL_ANNOUNCE, "\u0412\u0438\u043a\u0442\u043e\u0440\u0438\u043d\u0430", "\u0410\u043a\u0442\u0438\u0432\u0435\u043d \u044d\u0432\u0435\u043d\u0442 \u0412\u0438\u043a\u0442\u043e\u0440\u0438\u043d\u0430! \u0414\u043b\u044f \u0443\u0447\u0430\u0441\u0442\u0438\u044f \u043d\u0430\u0431\u0435\u0440\u0438\u0442\u0435 \u043a\u043e\u043c\u0430\u043d\u0434\u0443 .von  \u0414\u043b\u044f \u0441\u043f\u0440\u0430\u0432\u043a\u0438 .vhelp"));
	}

	static
	{
		_log = LoggerFactory.getLogger(Viktorina.class);
		Viktorina.playerList = new ArrayList<Player>();
		Viktorina.status = false;
		Viktorina._questionStatus = false;
	}

	public class ViktorinaStart implements Runnable
	{
		public ViktorinaStart(final long timeStopViktorina)
		{
			_timeStopViktorina = timeStopViktorina;
		}

		@Override
		public void run()
		{
			try
			{
				if(Viktorina._taskStartQuestion != null)
					Viktorina._taskStartQuestion.cancel(true);
				Viktorina._taskStartQuestion = ThreadPoolManager.getInstance().schedule(new startQuestion(_timeStopViktorina), 5000L);
				Announcements.getInstance().announceToAll("\u0412\u0438\u043a\u0442\u043e\u0440\u0438\u043d\u0430 \u043d\u0430\u0447\u0430\u043b\u0430\u0441\u044c!");
				Announcements.getInstance().announceToAll("\u0414\u043b\u044f \u0441\u043f\u0440\u0430\u0432\u043a\u0438 \u0432\u0432\u0435\u0434\u0438\u0442\u0435 .vhelp");
				loadQuestions();
				setStatus(true);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public class startQuestion implements Runnable
	{
		long _timeStopViktorina;

		public startQuestion(final long timeStopViktorina)
		{
			_timeStopViktorina = 0L;
			_timeStopViktorina = timeStopViktorina;
		}

		@Override
		public void run()
		{
			final long currentTime = Calendar.getInstance().getTimeInMillis();
			if(currentTime > _timeStopViktorina)
			{
				Viktorina._log.info("Viktorina time off...");
				Viktorina.playerList.clear();
				setStatus(false);
				setQuestionStatus(false);
				announseViktorina("\u0412\u0440\u0435\u043c\u044f \u0440\u0430\u0431\u043e\u0442\u044b \u0412\u0438\u043a\u0442\u043e\u0440\u0438\u043d\u044b \u0438\u0441\u0442\u0435\u043a\u043b\u043e. \u0412\u0441\u0435\u043c \u0443\u0447\u0430\u0441\u0442\u043d\u0438\u043a\u0430\u043c \u043f\u0440\u0438\u044f\u0442\u043d\u043e\u0433\u043e \u0432\u0440\u0435\u043c\u044f\u043f\u0440\u0435\u043f\u0440\u043e\u0432\u043e\u0436\u0434\u0435\u043d\u0438\u044f!");
				Announcements.getInstance().announceToAll("\u0412\u0440\u0435\u043c\u044f \u0412\u0438\u043a\u0442\u043e\u0440\u0438\u043d\u044b \u0437\u0430\u043a\u043e\u043d\u0447\u0438\u043b\u043e\u0441\u044c!");
				return;
			}
			if(!Viktorina.playerList.isEmpty())
			{
				Viktorina.playerList.clear();
				return;
			}
			if(!isStatus())
				return;
			if(!isQuestionStatus())
			{
				parseQuestion();
				checkPlayers();
				announseViktorina(Viktorina.question);
				if(Viktorina._taskStopQuestion != null)
					Viktorina._taskStopQuestion.cancel(true);
				Viktorina._taskStopQuestion = ThreadPoolManager.getInstance().schedule(new stopQuestion(_timeStopViktorina), Config.VIKTORINA_TIME_ANSER * 1000);
				setQuestionStatus(true);
			}
		}
	}

	public class stopQuestion implements Runnable
	{
		long _timeStopViktorina;

		public stopQuestion(final long timeStopViktorina)
		{
			_timeStopViktorina = 0L;
			_timeStopViktorina = timeStopViktorina;
		}

		@Override
		public void run()
		{
			if(!isStatus())
				return;
			setQuestionStatus(false);
			winners();
			rewarding();
			Viktorina.playerList.clear();
			if(Viktorina._taskStartQuestion != null)
				Viktorina._taskStartQuestion.cancel(true);
			Viktorina._taskStartQuestion = ThreadPoolManager.getInstance().schedule(new startQuestion(_timeStopViktorina), Config.VIKTORINA_TIME_PAUSE * 1000);
		}
	}

	private class Scores
	{
		public String _name;
		public int _score;

		private void setName(final String name)
		{
			_name = name;
		}

		private void setScore(final int score)
		{
			_score = score;
		}

		private String getName()
		{
			return _name;
		}

		private int getScore()
		{
			return _score;
		}
	}
}
