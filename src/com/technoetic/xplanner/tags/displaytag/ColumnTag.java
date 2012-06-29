package com.technoetic.xplanner.tags.displaytag;

import org.displaytag.util.TagConstants;


/**
 * <p>
 * This tag works hand in hand with the TableTag to display a list of objects. This describes a column of data in the
 * TableTag. There can be any number of columns that make up the list.
 * </p>
 * <p>
 * This tag does no work itself, it is simply a container of information. The TableTag does all the work based on the
 * information provided in the attributes of this tag.
 * <p>
 * @author mraible
 * @version $Revision: 408 $ ($Author: sg0897500 $)
 */
public class ColumnTag extends org.displaytag.tags.ColumnTag
{

    public void setHtmlTitle(String htmlTitle)
    {
        this.getHeaderAttributeMap().put(TagConstants.ATTRIBUTE_TITLE, htmlTitle);
    }

}