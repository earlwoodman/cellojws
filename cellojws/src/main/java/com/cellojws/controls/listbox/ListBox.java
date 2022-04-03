/*
 * Created on 2010-10-22
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.cellojws.controls.listbox;

import java.awt.Color;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import com.cellojws.adapter.Graphics;
import com.cellojws.controls.Control;
import com.cellojws.controls.VerticalScrollBar;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.general.FontInfo;
import com.cellojws.general.SortDirection;
import com.cellojws.general.core.Environment;
import com.cellojws.general.image.ImageFactory;
import com.cellojws.logging.WorkerLog;
import com.cellojws.special.TextTooltip;
import com.cellojws.token.CommandToken;
import com.cellojws.windowing.DragInfo;
import com.cellojws.windowing.WindowManager;


public class ListBox extends Control
{

	public static final int PADDING = Environment.smallGap();

    private ListBoxContents contents = new ListBoxContents();
    
    private int sortColumn;
    
    private SortDirection sortDirection;
    
    private boolean sortable;
    
    private List<Integer> selected;
    
    private List<Integer> highlighted;
    
    private boolean multiSelect;

    private boolean selectable;
    
    private VerticalScrollBar verticalScrollBar = null;
    
    public final static int SCROLLBAR_WIDTH = Environment.smallGap();
    
    private boolean overrideScrollBar = false;
    
    private String watermark;
    
    private int watermarkRenderLeft = -1;
    
    private int watermarkRenderTop = -1;
    
    /**
     * Denotes that a selection in the box is always required. (i.e. can't unselect)
     */
    private boolean selectionRequiredWhenSelectable;

	private boolean rowBackgroundVisible = true;
	
	private int noEntriesTop;

	private Integer rowHeight = null;

	private boolean showNoEntriesMsg = true;

	private boolean highlightingSelected = true;

	private static final String noEntriesMsg = "No entries...";
	
	public static final int NO_SINGLE_SELECTION = -1;
    
	public ListBox(final AbsDims dims, final CommandToken<?> token, final List<ListBoxItem> headers_)
	{
		this(dims, null, token, true, false, headers_);
	}
	
	public ListBox(final AbsDims dims, final CommandToken<?> token)
	{
		this(dims, null, token, true, false, null);
	}
	
	public ListBox(final AbsDims dims, final List<String> captions_, final CommandToken<?> token, 
			final boolean selectable_, final boolean multiSelect_, final List<ListBoxItem> headers_)
    {
        super(dims, token);
           
        sortable = false;
        sortColumn = 0;
        sortDirection = SortDirection.Ascending;
        sortContents();
        
        selectable = selectable_;
        multiSelect = multiSelect_;
        if( captions_ != null )
        {
            setContents(captions_);
        }
        
        if( headers_ == null )
        {
            contents.recalcColumnWidths();
        }
        else
        {
        	setHeaders(headers_);
        }
        
        reset();
        
        setTooltip(new TextTooltip());
        
        noEntriesTop = hasHeaders() ? contents.getRowHeight() + getStdGap() : 0;
    }

	public int getItemsVisibleExcludeHeader()
	{
		AbsDims dims = getDimensions();
		
		final int rowHeight = getRowHeight();
				
		if( rowHeight > 0 )
		{
			return (dims.bottom - dims.top - (hasHeaders() ? rowHeight : 0)) / rowHeight;
		}
		else
		{
			return 0;
		}
	}
	
    public int getRowHeight()
    {
    	if( rowHeight != null )
    	{
    		return rowHeight;    		
    	}
    	
    	final int rowHeightByContents = contents.getRowHeight();
    	
    	if( rowHeightByContents > 0 ) 
    	{
	    	final int boxHeight = getDimensions().getAbsHeight();
	    	
	    	final int leftoverSpace = boxHeight % rowHeightByContents;
	    	
	    	final int rowCount = boxHeight / rowHeightByContents;
	    	
	    	if( rowCount == 0 )
	    	{
	    		WorkerLog.error("Division by zero error: no row count! boxHeight is " + boxHeight);
	    		rowHeight = 20;
	    	}
	    	else 
    		{
	    		rowHeight = rowHeightByContents + leftoverSpace / rowCount;
    		}
	    	
	    	return rowHeight;
    	}
    	   	
	    return 0;
	}

    @Override
    public void dimensionsChanged()
    {
    	// Invalidate rowHeight. It will be recalculated again later.
    	rowHeight = null;
    	super.dimensionsChanged();
    }
    
	public synchronized void sortContents()
    {
    	if( sortable )
    	{
    		contents.sort(sortColumn, sortDirection);
    	}
    }
    
    /**
     * Call reset() whenever you have changed the contents of the list box
     * 
     */
    public void reset()
    {
    	rowHeight = null;
        clear();
    	removeControl(verticalScrollBar);
    	verticalScrollBar = null;
    	
        selected = new ArrayList<Integer>();
        final AbsDims dims = getDimensions();
        synchronized(this)
        {
	        if( getItemsVisibleExcludeHeader() < contents.countLines() && getItemsVisibleExcludeHeader() > 0 )
	        {
	            final AbsDims verticalScrollBarDims = new AbsDims(dims.getAbsWidth() - SCROLLBAR_WIDTH - 1, 1, dims.getAbsWidth() - 1, dims.getAbsHeight() - 1);
	            verticalScrollBar = new VerticalScrollBar(verticalScrollBarDims, 0, contents.countLines() - 1);
	        }
        
	        if( verticalScrollBar != null )
	        {
	            verticalScrollBar.setBounds(0, contents.countLines() - 1);
	            if( !overrideScrollBar )
	            {
	            	addControl(verticalScrollBar);
	            }
	        }
        }
        
        contents.addControls(this);
    }

    @Override
    public boolean doSpecialClickActions(int x, int y)
    {
            
    	if( isInHeader(y, x) )
    	{
    		return true;
    	}
    	
    	if( !overrideScrollBar && verticalScrollBar != null && x > verticalScrollBar.getDimensions().left )
    	{
    		return true;
    	}
    	
        if( selectable )
        {
            int item = getItemByVerticalCoord(y);
            
            if( multiSelect )
            {
            	synchronized(this)
            	{
	                if( item > contents.countLines() - 1 )
	                {
	                    item = contents.countLines() - 1;
	                }
            	}
                if( selected.contains(item) )
                {
                	if( !selectionRequiredWhenSelectable )
                	{
	                    // Autoboxing won't work here since remove() is overloaded to take an int
	                    selected.remove(new Integer(item));
                	}
                }
                else
                {
                	select(item);
                }
            }
            else
            {
                // If it's single select and someone selects the same item
                // clear it (toggle) 
                boolean clearing = false;
                if( getSelectedIndex() == item )
                {
                    clearing = true;
                }
                
                if( !selectionRequiredWhenSelectable )
                {
                	selected.clear();
                }
                
                if( !clearing )
                {
                	synchronized(this)
                	{
                        select(item);
                	}
                }
            }
            
        }
        
        return true;
        
    }

	protected int getItemByVerticalCoord(final int y) 
	{
		final int yAdjustedForHeader = y - (hasHeaders() ? getRowHeight() : 0);
		final int adjustForScrollBar = verticalScrollBar != null && !overrideScrollBar ? verticalScrollBar.getTopIndex() : 0;
		final int item = yAdjustedForHeader / getRowHeight() + adjustForScrollBar; 
				
		return item;
	}

    @Override
    public synchronized void render(Graphics graphics, final boolean mousedown)
    {
    	super.render(graphics, mousedown);
    	
        final AbsDims dims = getScreenDims();
    	
    	renderWatermark(graphics, dims);
    	
    	graphics.setFontInfo(Environment.getListBoxHeaderFontInfo());
    	
        renderHeader(graphics, dims);
    	
    	graphics.setFontInfo(Environment.getListBoxFontInfo());

        renderEntries(graphics, dims);

        graphics.setHighlight(false);

    }

	private void renderEntries(Graphics graphics, final AbsDims dims)
	{
		final int startPoint = determineStartPoint();
            
        final int max = getItemsVisibleExcludeHeader() + startPoint;
        int row = 0;
        
		if( contents.countLines() == 0 )
		{
			if( !hasHeaders() )
			{
				graphics.drawText(
					showNoEntriesMsg ? noEntriesMsg : "", 
					getScreenDims().left + getStdGap(), //(getScreenDims().getAbsWidth() - noEntriesWidth) / 2, 
					getScreenDims().top + noEntriesTop);
			}
		}
		else
		{
	        for( row = startPoint; row < max; row++ )
	        {
	            boolean highlight = false;
	        	drawRowBackground(graphics, dims, row - startPoint + (hasHeaders() ? 1 : 0));
	
	            if( contents.countLines() > row )
	            {
	                final List<? extends ListBoxItem> wholeLine = contents.getContentsAt(row);
	                
	                if( highlightingSelected  && (selected.contains(row) || (highlighted != null && highlighted.contains(row))) )
	                {
	                    highlight = true;
	                }
	                drawSingleRow(graphics, startPoint, row, highlight, wholeLine, -1, false);
	            }
	        }
		}
		
        drawRowBackground(graphics, dims, (row - startPoint) + (hasHeaders() ? 1 : 0));
	}

	private void renderHeader(Graphics graphics, final AbsDims dims)
	{
		// Draw headers if necessary
        if( hasHeaders() )
        {
        	drawRowBackground(graphics, dims, 0);
            drawSingleRow(graphics, 0, 0, false, contents.getHeaders(), sortColumn, true);
            
            // Draw an underline under the headers if required            
            graphics.drawRect(
            		new AbsDims(
            				dims.left + 2, 
            				dims.top + getRowHeight(), 
            				dims.right - (verticalScrollBar != null ? SCROLLBAR_WIDTH : 0) - 2, 
            				dims.top + getRowHeight()));
    	}
	}

	private void renderWatermark(Graphics graphics, final AbsDims dims)
	{
		if( watermark != null && !watermark.isEmpty() )
    	{
    		graphics.setFontInfo(Environment.getWatermarkFontInfo());
    		if( watermarkRenderLeft == -1 || watermarkRenderTop == -1 )
    		{
    			watermarkRenderTop = dims.bottom - WindowManager.getTextHeight() - 5;
    			watermarkRenderLeft = (dims.getAbsWidth() - graphics.getTextWidth(watermark, Environment.getWatermarkFontInfo())) / 2 
    					+ dims.left;
    		}
    		graphics.drawText(watermark, watermarkRenderLeft, watermarkRenderTop);
    	}
	}

	private void drawRowBackground(Graphics graphics, final AbsDims boxDims, final int index) 
	{
		if( rowBackgroundVisible  )
		{
			final AbsDims rowDims = new AbsDims();
			rowDims.left = boxDims.left;
			rowDims.right = boxDims.right;
			rowDims.top = boxDims.top + index * getRowHeight();
			rowDims.bottom = boxDims.top + (index + 1) * getRowHeight();
					
			if( rowDims.bottom > boxDims.bottom )
			{
				rowDims.bottom = boxDims.bottom;
			}
			if( index % 2 != 0 )
			{
				graphics.drawImage(ImageFactory.getListBoxRow().getBufferedImage(), rowDims);
			}
		}
	}

	public boolean hasHeaders()
	{
		return contents.getHeaders() != null && contents.getHeaders().size() != 0;
	}

    protected int determineStartPoint()
    {
        int startPoint = 0;
        
        if( verticalScrollBar != null && !overrideScrollBar )
        {
            startPoint = verticalScrollBar.getTopIndex();  
        }
        else
        {
            startPoint = 0;
        }
        
        return startPoint;
    }

    protected AbsDims drawSingleRow(final Graphics graphics, 
            final int startPoint, final int row, final boolean highlight,
            final List<? extends ListBoxItem> wholeLine,
            final int highlightColumn, final boolean isHeader)
    {
        
        int drawnColumn = 0;
        AbsDims ret = null;
        if( wholeLine != null )
        {
        	int count = -1;
            for( final ListBoxItem item : wholeLine )
            {   
                count++;
                graphics.setHighlight(false);
                if( highlight  // Someone set the highlight condition for this row
                		|| (count == highlightColumn && sortable)   // Someone wants a specific column highlighted 
                		|| (count == 0 && highlightColumn == -1 && hasHeaders() && sortable ) ) // Header row is highlighted 
                {
                    graphics.setHighlight(true);
                }
                                
                if( item != null )
                {                	
                	final AbsDims temp = drawEntry(graphics, startPoint, row, drawnColumn, item, isHeader);
                	if( ret == null )
                	{
                		ret = temp;
                	}
                	else
                	{
                		ret.merge(temp);
                	}
                }
                
                graphics.setHighlight(false);
                drawnColumn++;
            }
        }
        
        return ret;
    }

    private AbsDims drawEntry(final Graphics graphics, final int startPoint,
            final int drawnRow,
            final int drawnColumn, final ListBoxItem item, final boolean isHeader)
    {
            	
        final AbsDims dims = getScreenDims();
        final List<Float> colLocs = contents.getColumnLocations();
        final List<Float> colWidths = contents.getColumnWidths();
        
        if( colLocs != null )
        {        	        
	        final int workingAreaWidth = getWorkingAreaWidth();
	
	        final int left = (dims.left + getStdGap()) + 
	                         (int)((drawnColumn > 0 ? colLocs.get(drawnColumn - 1) : 0) * workingAreaWidth);// * drawnColumn;
	        
	        final int top = determineTopToDraw(graphics, startPoint, drawnRow, dims, item, isHeader); 
	        final int topCentered = top + (getRowHeight() - item.getHeight()) / 2;  
	        
	        String justification = "Left";
	        if( contents.getJustifications() != null && contents.getJustifications().size() > 0 )
	        {
	            justification = contents.getJustifications().get(drawnColumn);
	        }
	        int widthMax = 0;
	        
	        if( colWidths.size() > 0 )
	        {
	        	final int scrollBarWidth = (verticalScrollBar != null && !overrideScrollBar ? verticalScrollBar.getDimensions().getAbsWidth() : 0); 
	        	widthMax = (int)(((Float)colWidths.toArray()[drawnColumn]) * 
	        			((float)workingAreaWidth - scrollBarWidth));
	        }
	        else
	        {
	        	widthMax = workingAreaWidth; 
	        }
	        
	        if( widthMax > 0 )
	        {
	        	final int leftDraw = determineLeftDraw(item, left, justification, graphics, widthMax);
	        	item.draw(graphics, leftDraw, topCentered, widthMax);
	        	final AbsDims ret = new AbsDims(leftDraw, topCentered, widthMax + leftDraw, topCentered + WindowManager.getTextHeight(graphics.getFontInfo()));
	        	return ret;
	        }
        }
        
        return null;
    }

	private int determineTopToDraw(final Graphics graphics, final int startPoint, final int drawnRow, final AbsDims dims, final ListBoxItem item, final boolean isHeader) 
	{	
		int compensateForHeader = 0;
		if( !isHeader && hasHeaders() )
		{
			compensateForHeader = 1;
		}		
		
		return dims.top + (drawnRow - startPoint + compensateForHeader) * getRowHeight();
	}

	private int determineLeftDraw(ListBoxItem item, final int left, final String justification, final Graphics graphics, final int widthMax) 
	{
		int leftDraw = left;
			
        if( "Center".equals(justification)  )
        {
        	final int width = (widthMax - (item.getWidth() + Control.getStdGap() * 4)) / 2;
        	leftDraw += width > 0 ? width : 0;
        }
        else if( "Right".equals(justification) )
        {
    		final int width = widthMax - (item.getWidth() + Control.getStdGap() * 4);
    		leftDraw += width > 0 ? width : 0;
        }
        
        return leftDraw;
	}

	private int getWorkingAreaWidth()
	{
		return getScreenDims().getAbsWidth() - getStdGap() * 2 - 
				(verticalScrollBar != null && !overrideScrollBar ? SCROLLBAR_WIDTH : 0);
	}    

    public int getSelectedIndex()
    {
        if( multiSelect )
        {
            return NO_SINGLE_SELECTION;
        }
        else
        {
            if( selected.size() > 0 )
            {
                return (Integer)selected.toArray()[0];
            }
            else
            {
                return NO_SINGLE_SELECTION;
            }
        }
    }

    public List<Integer> getAllSelections()
    {
        return selected;
    }

    public List<List<? extends ListBoxItem>> getAllSelectedObjects()
    {
    	final List<List<? extends ListBoxItem>> allObjects = new ArrayList<>();
    	
    	for( Integer selection : selected )
    	{
    		allObjects.add(contents.getContentsAt(selection));    		
    	}
    	
    	return allObjects;
    }
    
    public void setCaptions(final List<String> captions_)
    {
        contents.clear();
        setContents(captions_);
        contents.recalcColumnWidths();
    }

    public synchronized void addContents(final List<? extends ListBoxItem> captions)
    {
    	contents.addContents(captions);
    	
    	/*for( final ListBoxItem item : captions )
    	{
    		if( item instanceof Control )
    		{
    			addControl((Control)item);
    		}
    	}*/
    }
    
    public synchronized void setContents(final List<String> captions_)
    {
        for( String caption : captions_ )
        {
            final List<ListBoxItem> line = new ArrayList<>();
            line.add(new StringListBoxItem(caption));
            contents.addContents(line);
        }
        
        reset();
        sortContents();
    }
    
    public synchronized void setContents(final ListBoxContents contents_)
    {
        contents = contents_;
        reset();
    }
    
    public void setMultiSelect(final boolean multiSelect_)
    {
        multiSelect = multiSelect_;
    }
    
    public void addHighlighted(int index)
    {
    	if( highlighted == null )
    	{
    		highlighted = new ArrayList<>();
    	}
    	
    	highlighted.add(index);
    }
    
    public void setHighlighted(final List<Integer> highlighted_)
    {
        highlighted = highlighted_;
    }
    
    public void clearHighlighted()
    {
        highlighted = null;
    }

    public void clearSelections()
    {
    	selected.clear();
    }
    
    public void setHeaders(final List<ListBoxItem> headers_)
    { 
        contents.setHeaders(headers_);
    }

    public int getSortColumn()
    {
        return sortColumn;
    }

    public void setSortColumn(int sortColumn)
    {
        this.sortColumn = sortColumn;
    }

    public SortDirection getSortDirection()
    {
        return sortDirection;
    }

    public void setSortDirection(SortDirection sortDirection)
    {
        this.sortDirection = sortDirection;
        sortContents();
    }

	@Override
	public void processMouseWheel(final MouseWheelEvent wheelEvent)
	{	
		if( verticalScrollBar != null && !overrideScrollBar )
		{
			if( wheelEvent.getWheelRotation() < 0 )
			{
				verticalScrollBar.scrollUp();
			}
			else if( wheelEvent.getWheelRotation() > 0 )
			{
				verticalScrollBar.scrollDown();
			}
		}
	}

	/**
	 * Selects the given item
	 * 
	 * Works for both single and multiselect. Does not clear if multiselect is on.
	 * 
	 * @param item
	 */
	public void select(int item) 
	{
		if( contents.countLines() > item )
		{
			if( !multiSelect )
			{
				selected.clear();
			}
			
			selected.add(item);
		}
		else
		{
			// If someone clicks outside the area containing items, clear selections
			selected.clear();
		}
			
	}

	public boolean isSelectionRequiredWhenSelectable() 
    {
		return selectionRequiredWhenSelectable;
	}

	public void setSelectionRequiredWhenSelectable(
			boolean selectionRequiredWhenSelectable) 
	{
		this.selectionRequiredWhenSelectable = selectionRequiredWhenSelectable;
	}

	/**
	 * Sets the tooltip based on the y coordinate
	 * 
	 * @param y
	 */
	public void setTooltipByPosition(final int y)
	{		
		final StringBuffer tooltip = new StringBuffer();
		
		if( contents.getHeaders() != null )
		{
			final ArrayList<String> columnNames = contents.getColumnNames();
			final ArrayList<Boolean> hidden = contents.getHiddenColumns();
			
			if( y < getRowHeight() )
			{
				int i = 0;
				for( String name : columnNames )
				{
					if( hidden == null || hidden.size() == 0 || !hidden.get(i) )
					{						
						tooltip.append(name + (columnNames.size() == i + 1 ? "" : " | "));
					}
					i++;
				}
				final TextTooltip textTooltip = (TextTooltip)getTooltip();
				if( textTooltip != null )
				{
					textTooltip.setText(tooltip.toString());
				}
				return;
			}
		}
		
		int item = 0;
		if( getRowHeight() > 0 )
		{
			item = (y - (hasHeaders() ? getRowHeight() : 0)) / getRowHeight() + determineStartPoint(); 
		}
		  
		if( item >= 0 && item < contents.countLines() )
		{
			final List<? extends ListBoxItem> lineItems = contents.getContentsAt(item);
			if( lineItems != null )
			{
				final List<Boolean> hidden = contents.getHiddenColumns();
				int i = 0;
				for( Object lineItem : lineItems )
				{					
					if( hidden == null || hidden.size() == 0 || !hidden.get(i) )
					{
						if( lineItem != null )
						{
							tooltip.append(lineItem.toString() + (lineItems.size() == i + 1 ? "" : " | "));
						}
					}
					i++;
				}
				
				final TextTooltip textTooltip = (TextTooltip)getTooltip();
				if( textTooltip != null )
				{
					textTooltip.setText(tooltip.toString());
				}
			}
		}
		else
		{
			final TextTooltip textTooltip = (TextTooltip)getTooltip();
			if( textTooltip != null )
			{
				textTooltip.setText("");
			}
		}
	}

	/** 
	 * Turns off the overriding of hiding the arrows
	 * 
	 */
	public void unhideScrollBar() 
	{
		overrideScrollBar = false;
		reset();
	}

	/**
	 * Forces the listbox to not show arrows
	 * 
	 */
	public void hideScrollBar() 
	{
		overrideScrollBar = true;
		removeControl(verticalScrollBar);
	}
    
	public boolean isInHeader(final int y, final int x)
	{	
		if( !hasHeaders() )
		{
			return false;			
		}
		
		if( y > getRowHeight() || (verticalScrollBar != null && x > verticalScrollBar.getDimensions().left ) )
		{
			return false;
		}
		
		return true;
	}

	public List<Float> getColumnLocations()
	{
		return contents.getColumnLocations();
	}

	public int getColumnLocationAt(int x)
	{
		final List<Float> colLocs = contents.getColumnLocations();
		int i = 0;
		final int width = getWorkingAreaWidth();
		for( final float loc : colLocs )
		{
			i++;
			if( x < loc * width )
			{
				return i - 1;
			}
		}
		
		return -1;

	}

	public void swapSortDirection()
	{
		if( sortDirection == SortDirection.Ascending )
		{
			sortDirection = SortDirection.Descending;
		}
		else
		{
			sortDirection = SortDirection.Ascending;
		}
	}

	public boolean isSortable()
	{
		return sortable;
	}

	public void setSortable(boolean sortable)
	{
		this.sortable = sortable;
	}

	public List<? extends ListBoxItem> getContentsAt(final int index)
	{
		return contents.getContentsAt(index);
	}

	public String getWatermark()
	{
		return watermark;
	}

	public void setWatermark(String watermark)
	{		
		this.watermark = watermark;
		watermarkRenderLeft = -1;
		watermarkRenderTop = -1;
	}

	public boolean isRowBackgroundVisible() 
	{
		return rowBackgroundVisible;
	}

	public void setRowBackgroundVisible(boolean rowBackgroundVisible) 
	{
		this.rowBackgroundVisible = rowBackgroundVisible;
	}

	@Override
	public void dragRender(Graphics graphics, boolean mousedown, AbsDims nonDraggingScreenDims, DragInfo dragging)
	{
		if( nonDraggingScreenDims != null && dragging != null )
		{
			final int row = getItemByVerticalCoord(dragging.getInitialY() - nonDraggingScreenDims.top);
			final FontInfo savedFont = graphics.getFontInfo();
			graphics.setFontInfo(Environment.getListBoxFontInfo());
			final AbsDims rowDims = drawSingleRow(graphics, determineStartPoint(), row, false, contents.getContentsAt(row), -1, false);
			graphics.drawSolidRect(rowDims, Color.BLACK);
			drawSingleRow(graphics, determineStartPoint(), row, false, contents.getContentsAt(row), -1, false);
			graphics.setFontInfo(savedFont);
			
			select(row);
		}
	}

	public boolean isSelectable()
	{
		return selectable;
	}

	public void setSelectable(boolean selectable)
	{
		this.selectable = selectable;
	}

    public int getTopIndex()
    {
        return verticalScrollBar.getTopIndex();
    }

    public void setTopIndex(final int currentTop)
    {
    	this.verticalScrollBar.setTopIndex(currentTop);
    }
    
    public int displayable()
    {
    	if( hasScrollBar() )
    	{
    		return getItemsVisibleExcludeHeader();
    	}
    	else
    	{
    		return size();
    	}
    }

    public int size()
	{
		return contents.countLines();
	}

	public boolean hasScrollBar()
	{
		return this.verticalScrollBar != null;
	}

	public synchronized void recalcColumnWidths()
	{
		contents.recalcColumnWidths();
	}

	public synchronized void setContentsJustifications(final ArrayList<String> justifications)
	{
		contents.setJustifications(justifications);
	}

	public synchronized void setContentsHiddenColumns(final ArrayList<Boolean> hiddenColumns)
	{
		contents.setHiddenColumns(hiddenColumns);
	}

	public synchronized void setContentsColumnNames(final ArrayList<String> columnNames)
	{
		contents.setColumnNames(columnNames);
	}

	public void removeSelectedItems() 
	{
		final List<Integer> selections = getAllSelections();
		
		if( selections != null && selections.size() > 0 )
		{
			for( final Integer selection : selections )
			{
				contents.remove(selection);				
			}
			reset();
		}
	}

	public void overrideColumnWidths(final List<Float> colWidths)
	{
		contents.overrideColumnWidths(colWidths);
	}

	public ListBoxContents getContents() 
	{
		return contents;
	}

	public List<Integer> getHighlighted() 
	{
		return new ArrayList<>(highlighted);
	}

	public boolean isShowNoEntriesMsg() 
	{
		return showNoEntriesMsg;
	}

	public void setShowNoEntriesMsg(boolean showNoEntriesMsg) 
	{
		this.showNoEntriesMsg = showNoEntriesMsg;
	}

	public boolean isHighlightingSelected()
	{
		return highlightingSelected;
	}

	public void setHighlightingSelected(boolean highlightingSelected)
	{
		this.highlightingSelected = highlightingSelected;
	}
	
}
