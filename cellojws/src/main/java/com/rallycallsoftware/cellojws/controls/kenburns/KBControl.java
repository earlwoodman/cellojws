package com.rallycallsoftware.cellojws.controls.kenburns;

import java.util.ArrayList;
import java.util.List;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.controls.CompletionCallback;
import com.rallycallsoftware.cellojws.controls.Control;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.FileHelper;
import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.logging.WorkerLog;
import com.rallycallsoftware.cellojws.token.CommandToken;

public class KBControl extends Control {

	private boolean running = false;

	private List<KBCommand> commands;

	private long startTimeInMillis;

	private final static int overlap = 150;

	public KBControl(final String filename, final AbsDims dims, final CommandToken<?> token) {
		super(dims, token);

		clearCommands();

		loadCommands(filename);
	}

	public KBControl(final AbsDims dims, final CommandToken<?> token) {
		super(dims, token);

		clearCommands();
	}

	private void loadCommands(final String filename) {
		final List<String> fileContent = FileHelper.getAllLinesInFile(environment.getExecutionPath() + "/" + filename);
		int totalTime = 0;

		for (final String line : fileContent) {
			final String[] items = line.split(" ");
			final StraightLineCommand command = new StraightLineCommand();

			command.setImage(new Image("/" + items[9], true));

			command.setStart(new AbsDims(Integer.parseInt(items[0]), Integer.parseInt(items[1]),
					Integer.parseInt(items[2]), Integer.parseInt(items[3])), this);

			command.setFinish(new AbsDims(Integer.parseInt(items[4]), Integer.parseInt(items[5]),
					Integer.parseInt(items[6]), Integer.parseInt(items[7])), this);

			command.setElapsed(Integer.parseInt(items[8]) * 1000);
			command.setStartTime(totalTime);
			totalTime += command.getElapsed();
			command.setFinishTime(totalTime);
			totalTime += overlap;
			commands.add(command);
		}
	}

	public void addCommand(KBCommand command) {
		long totalTime = 0;
		if (commands.size() > 0) {
			totalTime = commands.get(commands.size() - 1).getFinishTime();
		}

		command.setStartTime(totalTime);
		command.setFinishTime(command.getElapsed() + totalTime);
		commands.add(command);
	}

	public static KBCommand createCommand(final Control owner, final Image image, final AbsDims startDims,
			final AbsDims finishDims, final int milliseconds) {
		final CircleCommand command = new CircleCommand(owner);

		command.setImage(image);
		command.setElapsed(milliseconds);

		return command;
	}

	public void start() {
		running = true;

		startTimeInMillis = System.currentTimeMillis();
	}

	public void stop() {
		running = false;

		if (getParent() instanceof CompletionCallback) {
			((CompletionCallback) getParent()).completed();
		}
	}

	@Override
	public void render(Graphics graphics, boolean mousedown) {
		super.render(graphics, mousedown);

		long time1 = 0;
		long time2 = 0;
		if (running) {
			final long snapshot = System.currentTimeMillis() - startTimeInMillis;
			for (int i = 0; i < commands.size(); i++) {
				if (snapshot >= commands.get(i).getStartTime() && snapshot <= commands.get(i).getFinishTime()) {
					time2 = commands.get(i).getFinishTime();
					time1 = commands.get(i).getStartTime();
					commands.get(i).render(graphics, (float) (snapshot - time1) / (time2 - time1), getScreenDims());
				} else if (i < commands.size() - 1) {
					if (snapshot >= commands.get(i).getFinishTime() && snapshot <= commands.get(i + 1).getStartTime()) {
						time2 = commands.get(i + 1).getStartTime();
						time1 = commands.get(i).getFinishTime();
						renderCommandTransition(graphics, commands.get(i), commands.get(i + 1),
								(float) (snapshot - time1) / (time2 - time1));
					}
				} else if (snapshot >= commands.get(commands.size() - 1).getFinishTime()) {
					stop();
				}
			}
		}
	}

	private void renderCommandTransition(final Graphics graphics, final KBCommand first, final KBCommand second,
			final float percentage) {
		WorkerLog.info("renderCommandTransition percentage" + Float.toString(percentage));
		second.getImage().load();

		graphics.drawImage(first.getFinishImage(), getScreenDims());
		graphics.drawImage(second.getStartImage(), getScreenDims(), makeComposite(percentage));

	}

	@Override
	public boolean doSpecialClickActions(int x, int y) {
		return true;
	}

	public void clearCommands() {
		commands = new ArrayList<KBCommand>();
	}

	public boolean isRunning() {
		return running;
	}

}
