/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: Jun 29, 2004
 * Time: 12:32:50 AM
 */
package com.technoetic.xplanner.tags.displaytag;

import net.sf.xplanner.domain.UserStory;

import org.displaytag.decorator.TableDecorator;


public class UserStoryDecorator extends TableDecorator
{
    public double getPercentCompleted()
    {
        UserStory story = getUserStory();
        return HoursDecorator.getPercentCompletedScore(story.getEstimatedHours(),
                                                       story.getCachedActualHours(),
                                                       story.getTaskBasedRemainingHours(),
                                                       story.isCompleted());
    }

    public double getRemainingHours()
    {
        UserStory story = getUserStory();
        return HoursDecorator.getRemainingHoursScore(story.getCachedActualHours(),
                                                     story.getTaskBasedRemainingHours(),
                                                     story.isCompleted());
    }

    public String getTrackerName()
    {
        return PersonIdDecorator.getPersonName(getUserStory().getTrackerId());
    }

    public int getTaskCount()
    {
        return getUserStory().getTasks().size();
    }

    public int getOriginalEstimateAndActualPercentDifference()
    {
        return HoursDecorator.getPercentDifference(getUserStory().getTaskBasedEstimatedOriginalHours(),
                                                   getUserStory().getCachedActualHours());
    }

    public String getOriginalEstimateAndActualPercentDifferenceFormatted()
    {
        if (getUserStory().getCachedActualHours() == 0.0d ||
            getUserStory().getTaskBasedEstimatedOriginalHours() == 0.0d)
        {
            return "&nbsp;";
        }
        else
        {
            return HoursDecorator.formatPercentDifference(getUserStory().getTaskBasedEstimatedOriginalHours(),
                                                          getUserStory().getCachedActualHours());
        }
    }

    public double getOriginalEstimateToCurrentEstimateScore()
    {
        double originalHours = getUserStory().getTaskBasedEstimatedOriginalHours();
        double finalHours = getUserStory().getEstimatedHours();
        return getDifference(originalHours, finalHours);
    }

    private double getDifference(double originalHours, double finalHours)
    {
        double diff = Math.abs(originalHours - finalHours);
        if (getUserStory().isCompleted())
        {
            return diff;
        }
        else
        {
            return -diff;
        }
    }

    public double getActualToOriginalEstimateScore()
    {
        double originalHours = getUserStory().getTaskBasedEstimatedOriginalHours();
        double finalHours = getUserStory().getCachedActualHours();
        return getDifference(originalHours, finalHours);
    }

    public double getActualToEstimateScore()
    {
        double originalHours = getUserStory().getEstimatedHours();
        double finalHours = getUserStory().getCachedActualHours();
        return getDifference(originalHours, finalHours);
    }

    private UserStory getUserStory()
    {
        return ((UserStory) getCurrentRowObject());
    }


}