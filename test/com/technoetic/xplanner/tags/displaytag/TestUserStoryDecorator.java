package com.technoetic.xplanner.tags.displaytag;

/*
 * $Header$
 * $Revision: 869 $
 * $Date: 2005-12-02 15:11:33 +0200 (Пт, 02 дек 2005) $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */

import junit.framework.TestCase;
import net.sf.xplanner.domain.UserStory;

public class TestUserStoryDecorator extends TestCase
{
    UserStoryDecorator decorator;

    public void testGetPercentCompleted() throws Exception
    {
        UserStory story1 = newStory(10, 10, 0, true);
        UserStory story2 = newStory(10, 9, 0, true);
        UserStory story3 = newStory(5, 5, 0, true);
        UserStory story4 = newStory(2, 0, 2, false);
        UserStory story5 = newStory(0, 0, 0, false);
        decorator = new UserStoryDecorator();
        double pct1 = getPercentCompleted(story1);
        double pct2 = getPercentCompleted(story2);
        double pct3 = getPercentCompleted(story3);
        double pct4 = getPercentCompleted(story4);
        double pct5 = getPercentCompleted(story5);
        assertTrue(pct1 > pct2);
        assertTrue(pct2 > pct3);
        assertTrue(pct3 > pct4);
        assertTrue(pct4 > pct5);
    }

    public void testGetOriginalEstimateAndActualDifferenceFormatted()
    {
        UserStory story = newStory(10, 0, 10, false);
        decorator = new UserStoryDecorator();
        decorator.initRow(story, 0, 0);
        String formatedDiff = decorator.getOriginalEstimateAndActualPercentDifferenceFormatted();
        assertEquals("Column should be blank", "&nbsp;", formatedDiff);
        decorator = new UserStoryDecorator();
        story = newStory(10, 5, 0, false);
        decorator.initRow(story, 0, 0);
        formatedDiff = decorator.getOriginalEstimateAndActualPercentDifferenceFormatted();
        assertEquals("Wrong value", "-50%", formatedDiff);

    }

    private double getPercentCompleted(UserStory story1)
    {
        decorator.initRow(story1, 0, 0);
        return decorator.getPercentCompleted();
    }

    private UserStory newStory(final double estimatedHours,
                               final double actualHours,
                               final double remainingHours,
                               final boolean completed)
    {
        return new UserStory()
        {
            public double getEstimatedHours()
            {
                return estimatedHours;
            }

            public double getTaskBasedEstimatedOriginalHours()
            {
                return estimatedHours;
            }

            public double getCachedActualHours()
            {
                return actualHours;
            }

            public double getTaskBasedRemainingHours()
            {
                return remainingHours;
            }

            public boolean isCompleted()
            {
                return completed;
            }
        };
    }

}