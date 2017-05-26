package com.rallycallsoftware.cellojws.controls;

import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Collection;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.DateTime;
import com.rallycallsoftware.cellojws.general.image.ImageFactory;
import com.rallycallsoftware.cellojws.token.CommandToken;
import com.rallycallsoftware.cellojws.windowing.WindowManager;

public class Calendar extends Control {

	private int year;

	private int month;

	private int dayWidth;

	private Collection<CalendarObject> objects;

	private DateTime lastDateTime;

	private String watermark;

	private int watermarkRenderLeft = -1;

	private int watermarkRenderTop = -1;

	private Object calendarObjLock = new Object();

	private static final int daysPerWeek = 7;

	private static final int maxRows = 6;

	public Calendar(final int year_, final int month_, final AbsDims dim, final CommandToken<?> token_) {
		super(dim, token_);

		year = year_;
		month = month_;

		dayWidth = dim.getAbsWidth() / daysPerWeek;

		clearObjects();
	}

	@Override
	public void render(Graphics graphics, boolean mousedown) {
		super.render(graphics, mousedown);

		final AbsDims controlDims = getScreenDims();

		graphics.setFontInfo(environment.getLabelFontInfo());
		final int daysPerMonth = (int) DateTime.getDaysPerMonth(month, year);
		final int dayOfWeekMonthStarts = dayOfWeekMonthStarts();

		boolean started = false;
		final int blockHeight = blockHeight();
		for (int i = 0, days = 0; i < maxRows * daysPerWeek; i++) {
			if (!started && i < daysPerWeek) {
				if (i >= dayOfWeekMonthStarts) {
					started = true;
				}
			}

			if (started) {
				days++;
			}
			final AbsDims dayDims = determineDayDims(controlDims, blockHeight, i - dayOfWeekMonthStarts);

			renderCalendarDay(graphics, daysPerMonth, started, days, dayDims);
		}

		renderWatermark(graphics, controlDims);
	}

	private int dayOfWeekMonthStarts() {
		return DateTime.getDayOfWeekMonthStarts(month, year);
	}

	public int blockHeight() {
		return getDimensions().getAbsHeight() / maxRows;
	}

	private void renderWatermark(Graphics graphics, final AbsDims controlDims) {
		if (watermark != null && !watermark.isEmpty()) {
			graphics.setFontInfo(environment.getWatermarkFontInfo());
			if (watermarkRenderLeft == -1 || watermarkRenderTop == -1) {
				watermarkRenderTop = controlDims.bottom - WindowManager.getTextHeight(graphics.getFontInfo()) - 5;
				watermarkRenderLeft = (controlDims.getAbsWidth()
						- graphics.getTextWidth(watermark, environment.getWatermarkFontInfo())) / 2 + controlDims.left;
			}
			graphics.drawText(watermark, watermarkRenderLeft, watermarkRenderTop);
		}
	}

	private void renderCalendarDay(final Graphics graphics, final int daysPerMonth, final boolean started,
			final int days, final AbsDims dayDims) {

		if (started && days <= daysPerMonth) {
			if (lastDateTime != null && lastDateTime.getDay() == days && lastDateTime.getMonth() == month) {
				graphics.drawImage(ImageFactory.getImage("CalendarObjectToday.png").getBufferedImage(), dayDims);
			} else {
				graphics.drawImage(ImageFactory.getImage("CalendarObject.png").getBufferedImage(), dayDims);
			}
			graphics.drawText(Integer.valueOf(days).toString(), dayDims.left + 2, dayDims.top + 2);
		} else {
			graphics.drawImage(ImageFactory.getImage("CalendarObject.png").getBufferedImage(), dayDims);
		}

	}

	public AbsDims determineDayDims(final AbsDims controlDims, final int blockHeight, final int day) {
		int column;
		int row;

		int cell = day + dayOfWeekMonthStarts();
		column = cell % daysPerWeek;
		row = cell / daysPerWeek;
		final AbsDims dayDims = new AbsDims(controlDims.left + (column * dayWidth), controlDims.top + blockHeight * row,
				controlDims.left + ((column + 1) * dayWidth), controlDims.top + blockHeight * (row + 1));
		return dayDims;
	}

	@Override
	public boolean doSpecialClickActions(int x, int y) {
		return true;
	}

	@Override
	public void processMouseWheel(MouseWheelEvent wheelEvent) {

	}

	public void setYear(final int year) {
		this.year = year;
		watermarkRenderLeft = -1;
		watermarkRenderTop = -1;
	}

	public void setMonth(final int month) {
		this.month = month;
		watermarkRenderLeft = -1;
		watermarkRenderTop = -1;
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public void clearObjects() {
		synchronized (calendarObjLock) {
			if (objects != null) {
				for (CalendarObject object : objects) {
					removeControl(object);
				}
			}
			objects = new ArrayList<CalendarObject>();
		}
	}

	public void addObject(final CalendarObject object_) {

		synchronized (calendarObjLock) {
			objects.add(object_);
		}
		addControl(object_);
	}

	public void refresh(final DateTime dateTime) {
		lastDateTime = dateTime;
	}

	public String getWatermark() {
		return watermark;
	}

	public void setWatermark(String watermark) {
		this.watermark = watermark;
	}

}
