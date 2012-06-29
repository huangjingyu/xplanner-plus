package com.technoetic.xplanner.tags.displaytag;

import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.model.RowSorter;
import org.displaytag.properties.TableProperties;


/**
 * @author fgiust
 * @version $Revision: 408 $ ($Author: sg0897500 $)
 */
public class TableModel extends org.displaytag.model.TableModel
{
    private static Log log = LogFactory.getLog(org.displaytag.model.TableModel.class);

    String id;

    public TableModel(TableProperties tableProperties) {
        super(tableProperties, null);
    }

    /**
     * sorts the given list of Rows. The method is called internally by sortFullList() and sortPageList().
     * @param list List
     */
    private void sortRowList(List list)
    {
        if (isSorted())
        {
            HeaderCell sortedHeaderCell = (HeaderCell) getSortedColumnHeader();

            if (sortedHeaderCell != null)
            {
                // If it is an explicit value, then sort by that, otherwise sort by the property...
                int sortedColumn = this.getSortedColumnNumber();
                if (sortedHeaderCell.getBeanSortPropertyName() != null
                    || (sortedColumn != -1 && sortedColumn < getHeaderCellList().size()))
                {
                    Collections.sort(list, new RowSorter(
                        sortedColumn,
                        sortedHeaderCell.getBeanSortPropertyName(),
                        getTableDecorator(),
                        this.isSortOrderAscending()));
                }
            }

        }

    }

    public void setId(String tableId) {
        this.id = tableId;
        super.setId(tableId);
    }

    /**
     * sort the list displayed in page.
     */
    public void sortPageList()
    {
        if (log.isDebugEnabled())
        {
            log.debug("[" + this.id + "] sorting page list");
        }
        sortRowList(this.getRowListPage());

    }

    /**
     * sort the full list of data.
     */
    public void sortFullList()
    {
        if (log.isDebugEnabled())
        {
            log.debug("[" + this.id + "] sorting full data");
        }
        sortRowList(this.getRowListFull());
    }

}