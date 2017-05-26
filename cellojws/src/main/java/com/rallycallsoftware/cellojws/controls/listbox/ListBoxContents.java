/*
 * Created on 2011-06-21
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.rallycallsoftware.cellojws.controls.listbox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.rallycallsoftware.cellojws.controls.Control;
import com.rallycallsoftware.cellojws.general.DateTime;
import com.rallycallsoftware.cellojws.general.SortDirection;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.windowing.WindowManager;

public class ListBoxContents {

	private static final int MINIMUM_WIDTH = 2;

	private List<List<? extends ListBoxItem>> contents;

	// private List<List<? extends ListBoxItem>> sortedContents;

	private List<? extends ListBoxItem> headers;

	private List<String> justifications;

	private List<Boolean> hiddenColumns;

	private List<String> columnNames;

	private List<Float> colWidths;

	public ListBoxContents() {
		clear();
	}

	public void addContents(final List<? extends ListBoxItem> newContents) {
		synchronized (this) {
			contents.add(newContents);
		}
	}

	public void addContentRow(final ListBoxItem contentRow) {
		synchronized (this) {
			final List<ListBoxItem> rowContainer = new ArrayList<>();
			rowContainer.add(contentRow);
			contents.add(rowContainer);
		}
	}

	public void addContentsByStrings(final List<String> newContents) {
		final List<ListBoxItem> objectContents = new ArrayList<>();

		for (String content : newContents) {
			objectContents.add(new StringListBoxItem(content));
		}
		addContents(objectContents);
	}

	public void recalcColumnWidths() {
		final Collection<Integer> longestEntriesByCol = getLongestEntriesByCol();
		float totalWidth = 0;

		// Calculate the sum of the longest entry for each col
		for (Integer entry : longestEntriesByCol) {
			totalWidth += entry + MINIMUM_WIDTH;
		}

		synchronized (this) {
			colWidths = new ArrayList<>();

			// Now, record the ratio of each one to the total
			for (Integer entry : longestEntriesByCol) {
				colWidths.add((entry + MINIMUM_WIDTH) / totalWidth);
			}
		}
	}

	/**
	 * Returns the longest entry for each column
	 * 
	 * @return
	 */
	private Collection<Integer> getLongestEntriesByCol() {
		final Map<Integer, Integer> entries = new HashMap<Integer, Integer>();

		synchronized (this) {
			if (contents != null) {
				for (List<? extends ListBoxItem> row : contents) {
					int column = 0;
					for (final ListBoxItem entry : row) {
						if (hiddenColumns.size() > column && (Boolean) hiddenColumns.toArray()[column] == true) {
							entries.put(column, 0);
						} else {
							int width = entry.getWidth();

							if (entries.get(column) == null || (entry != null && width > entries.get(column))) {
								entries.put(column, entry != null ? width : 0);
							}
						}
						column++;

					}
				}
			}

			// Now consider the headers
			if (headers != null) {
				int colIndex = 0;
				for (Object column : headers) {
					if (hiddenColumns.size() > colIndex && (Boolean) hiddenColumns.toArray()[colIndex] == true) {
						entries.put(colIndex, 0);
					} else {
						if (entries.get(colIndex) == null || entries.get(colIndex) < column.toString().length()) {
							entries.put(colIndex, column.toString().length());
						}
					}
					colIndex++;
				}
			}
		}

		return entries.values();
	}

	public int countLines() {
		synchronized (this) {
			return contents.size();
		}
	}

	/**
	 * Return the contents at the given location trying to get it from the
	 * sorted list first
	 * 
	 * @param line
	 * @return
	 */
	public List<? extends ListBoxItem> getContentsAt(final int line) {
		if (line == ListBox.NO_SINGLE_SELECTION) {
			return null;
		}

		synchronized (this) {
			/*
			 * if( sortedContents != null && sortedContents.size() > line ) {
			 * return sortedContents.get(line); }
			 */
			if (contents.size() > line) {
				return contents.get(line);
			} else {
				return null;
			}
		}
	}

	public void clear() {
		synchronized (this) {
			contents = new ArrayList<>();
		}
		setHiddenColumns(new ArrayList<>());
	}

	/**
	 * Returns the number of columns
	 * 
	 * For now, just use the number of columns in the first row
	 * 
	 * @return
	 */
	public int countAllColumns() {
		synchronized (this) {
			if (contents.size() > 0) {
				return contents.get(0).size();
			}
		}

		return 0;

	}

	public List<Float> getColumnWidths() {
		final List<Float> widths = new ArrayList<Float>();

		synchronized (this) {
			if (colWidths != null) {
				widths.addAll(colWidths);
			}
		}

		return widths;
	}

	public void setHeaders(final List<ListBoxItem> headers) {
		synchronized (this) {
			this.headers = headers;
		}
		recalcColumnWidths();
	}

	public void setJustifications(final ArrayList<String> justifications) {
		synchronized (this) {
			this.justifications = justifications;
		}
	}

	public List<ListBoxItem> getHeaders() {
		final List<ListBoxItem> headersRet = new ArrayList<>();

		synchronized (this) {
			if (headers != null) {
				headersRet.addAll(headers);
			}
		}

		return headersRet;
	}

	public ArrayList<String> getJustifications() {
		final ArrayList<String> justificationsRet = new ArrayList<String>();

		synchronized (this) {
			if (justifications != null) {
				justificationsRet.addAll(justifications);
			}
		}

		return justificationsRet;
	}

	public void sort(final int sortColumn, final SortDirection sortDirection) {
		synchronized (this) {
			final List<List<? extends ListBoxItem>> sortedContents = new ArrayList<>();

			final int originalSize = contents.size();

			do {
				// Must be able to sort alphabetically or numerically
				String chooseStr;
				double chooseNum;
				DateTime chooseDate;

				if (sortDirection == SortDirection.Ascending) {
					chooseStr = "A";
					chooseNum = -Double.MAX_VALUE;
					chooseDate = new DateTime(Integer.MIN_VALUE, 1, 1, 0, 0, 0);
				} else if (sortDirection == SortDirection.Descending) {
					chooseStr = "ZZZZZZZ";
					chooseNum = Double.MAX_VALUE;
					chooseDate = new DateTime(Integer.MAX_VALUE, 1, 1, 0, 0, 0);
				} else {
					return;
				}

				int column;
				List<? extends ListBoxItem> chosenRow = null;
				double cellVal = 0D;

				for (List<? extends ListBoxItem> row : contents) {
					column = 0;
					for (ListBoxItem cell : row) {
						if (column == sortColumn) {
							if (cell instanceof NumberListBoxItem) {
								if (cell == null || cell.toString().isEmpty()) {
									if (sortDirection == SortDirection.Ascending) {
										cellVal = 0;
									} else if (sortDirection == SortDirection.Descending) {
										cellVal = Double.MAX_VALUE;
									}
								} else {
									cellVal = Double.parseDouble(cell.toString());
								}

								if (sortDirection == SortDirection.Ascending) {
									if (cellVal >= chooseNum) {
										chooseNum = cellVal;
										chosenRow = row;
									}
								}
								if (sortDirection == SortDirection.Descending) {
									if (cellVal <= chooseNum) {
										chooseNum = cellVal;
										chosenRow = row;
									}
								}
							} else if (cell instanceof StringListBoxItem) {
								if (sortDirection == SortDirection.Ascending) {
									if (cell.toString().toUpperCase().compareTo(chooseStr) > 0) {
										chooseStr = cell.toString();
										chosenRow = row;
									}
								}
								if (sortDirection == SortDirection.Descending) {
									if (cell.toString().toUpperCase().compareTo(chooseStr) <= 0) {
										chooseStr = cell.toString();
										chosenRow = row;
									}
								}
							} else if (cell instanceof DateTimeListBoxItem) {
								if (sortDirection == SortDirection.Ascending) {
									if (!((DateTime) (Object) cell).isEarlier(chooseDate)) {
										chooseDate = (DateTime) (Object) cell;
										chosenRow = row;
									}
								}
								if (sortDirection == SortDirection.Descending) {
									if (((DateTime) (Object) cell).isEarlier(chooseDate)) {
										chooseDate = (DateTime) (Object) cell;
										chosenRow = row;
									}
								}
							} else {
								return;
							}
						}
						column++;
					}
				}
				if (chosenRow != null) {
					sortedContents.add(chosenRow);
					contents.remove(chosenRow);
				}
			} while (sortedContents.size() < originalSize);

			contents.clear();
			contents.addAll(sortedContents);
		}
	}

	public void setHiddenColumns(ArrayList<Boolean> hiddenColumns) {
		synchronized (this) {
			this.hiddenColumns = hiddenColumns;
		}
	}

	public ArrayList<Boolean> getHiddenColumns() {
		final ArrayList<Boolean> hidden = new ArrayList<Boolean>();

		synchronized (this) {
			if (hiddenColumns != null) {
				hidden.addAll(hiddenColumns);
			}
		}

		return hidden;
	}

	public ArrayList<String> getColumnNames() {
		final ArrayList<String> columns = new ArrayList<String>();

		synchronized (this) {
			if (columnNames != null) {
				columns.addAll(columnNames);
			}
		}
		return columns;
	}

	public void setColumnNames(final ArrayList<String> columnNames) {
		synchronized (this) {
			this.columnNames = columnNames;
		}
	}

	public List<Float> getColumnLocations() {
		final List<Float> colWidths = getColumnWidths();

		if (colWidths == null || colWidths.size() == 0) {
			return null;
		}

		final List<Float> colLocs = new ArrayList<Float>();
		float total = 0;
		for (Float width : colWidths) {
			total += width;
			colLocs.add(total);
		}

		return colLocs;
	}

	public int getRowHeight() {

		if (contents != null && contents.size() > 0) {
			final List<ListBoxItem> allItems = contents.stream().flatMap(x -> x.stream()).collect(Collectors.toList());

			return allItems.stream().mapToInt(x -> x.getHeight()).max().getAsInt();
		}

		return WindowManager.getTextHeight(Environment.getEnvironment().getListBoxFontInfo());
	}

	public void addControls(final ListBox listBox) {
		/*
		 * if( sortedContents != null ) { addControls(sortedContents, listBox);
		 * } else
		 */if (contents != null) {
			addControls(contents, listBox);
		}

	}

	private void addControls(final List<List<? extends ListBoxItem>> contents, final ListBox listBox) {
		for (final List<? extends ListBoxItem> itemList : contents) {
			for (final ListBoxItem item : itemList) {
				if (item instanceof Control && !(item instanceof ListBoxItem)) {
					listBox.addControl((Control) item);
				}
			}
		}

	}

	public void remove(final int selection) {
		if (selection >= 0 && selection < contents.size()) {
			contents.remove(selection);
		}
	}

}
