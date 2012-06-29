package com.technoetic.xplanner.domain;

import com.technoetic.xplanner.util.StringUtilities;

/**
 * Created by IntelliJ IDEA.
 * User: tkmower
 * Date: Nov 29, 2004
 * Time: 4:47:59 PM
 */
public class SearchResult extends Object
{
    private String matchPrefix = "";
    private String matchSuffix = "";
    private String matchingText = "";
    private String resultType;
    private Nameable matchingObject;
    private boolean matchInDescription = false;
    private String attachedToId;
    private String attachedToDomainType;

    public SearchResult(Nameable matchingObject,
                        String resultType)
    {
        this.matchingObject = matchingObject;
        this.resultType = resultType;
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof SearchResult)) return false;

        final SearchResult searchResult = (SearchResult) o;

        if (getDomainObjectId() != searchResult.getDomainObjectId()) return false;

        return true;
    }

    public String getMatchPrefix()
    {
        return matchPrefix;
    }

    public void setMatchPrefix(String matchPrefix)
    {
        this.matchPrefix = matchPrefix;
    }

    public String getMatchSuffix()
    {
        return matchSuffix;
    }

    public void setMatchSuffix(String matchSuffix)
    {
        this.matchSuffix = matchSuffix;
    }

    public Object getMatchingObject()
    {
        return matchingObject;
    }

    public String getMatchingText()
    {
        return matchingText;
    }

    public void setMatchingText(String matchingText)
    {
        this.matchingText = matchingText;
    }

    public String getResultType()
    {
        return resultType;
    }

    public String getTitle()
    {
        return matchingObject.getName();
    }

    public int hashCode()
    {
        return getDomainObjectId();
    }

    public int getDomainObjectId()
    {
        return matchingObject.getId();
    }

    public boolean isMatchInDescription()
    {
        return matchInDescription;
    }

    public void setMatchInDescription(boolean matchInDescription)
    {
        this.matchInDescription = matchInDescription;
    }

    public void populate(String searchCriteria,
                         int desiredDescriptionLines, int maxSuffixLength)
    {
        if (getLocationOfCriteria(searchCriteria) < 0)
        {
            populateResultWithNoDescriptionMatch(desiredDescriptionLines);
        }
        else
        {
            populateResultWithPrefixSuffixAndResult(searchCriteria, maxSuffixLength);
        }
    }

    private int getLocationOfCriteria(String searchCriteria)
    {
        return matchingObject.getDescription().toLowerCase().indexOf(searchCriteria.toLowerCase());
    }

    private void populateResultWithNoDescriptionMatch(int desiredDescriptionLines)
    {
        setMatchingText(StringUtilities.getFirstNLines(matchingObject.getDescription(), desiredDescriptionLines));
    }

    private void populateResultWithPrefixSuffixAndResult(String searchCriteria,
                                                         int maxSuffixLength)
    {
        populate(searchCriteria, maxSuffixLength);
    }

    public void populate(String searchCriteria, int maxSuffixLength)
    {
        setMatchInDescription(true);
        String description = matchingObject.getDescription();
        setMatchPrefix(StringUtilities.computePrefix(description, searchCriteria, maxSuffixLength));

        int beginIndex = getLocationOfCriteria(searchCriteria);
        setMatchingText(description.substring(beginIndex, beginIndex + searchCriteria.length()));

        setMatchSuffix(StringUtilities.computeSuffix(description, searchCriteria, maxSuffixLength));
    }

    public void setAttachedToId(String attachedToId)
    {
        this.attachedToId = attachedToId;
    }

    public String getAttachedToId()
    {
        return attachedToId;
    }

    public void setAttachedToDomainType(String attachedToDomainType)
    {
        this.attachedToDomainType = attachedToDomainType;
    }

    public String getAttachedToDomainType()
    {
        return attachedToDomainType;
    }
}
