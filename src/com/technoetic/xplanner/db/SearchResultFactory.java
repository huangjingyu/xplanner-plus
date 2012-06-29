package com.technoetic.xplanner.db;

import java.util.HashMap;
import java.util.Map;

import net.sf.xplanner.domain.Note;

import com.technoetic.xplanner.domain.Nameable;
import com.technoetic.xplanner.domain.SearchResult;

public class SearchResultFactory
{
    //DEBT factor the type mapping in the RelationshipMappingRegistry or similar metadata registry.
    private Map types = new HashMap();
    private int desiredDescriptionLines = 3;
    private int maxSuffixLength = 20;

    public SearchResultFactory(Map types)
    {
        this.types = types;
    }

    public SearchResult convertObjectToSearchResult(Nameable titled, String searchCriteria) throws Exception
    {
        String resultType = convertClassToType(titled.getClass().getName());

        SearchResult searchResult = new SearchResult(titled, resultType);
        if ("note".equals(resultType)) {
            Note note = (Note) titled;
            searchResult.setAttachedToId(String.valueOf(note.getAttachedToId()));
            Object objectToWhichImAttached = null;
            objectToWhichImAttached = note.getParent();
            Class classOfAttachedTo = objectToWhichImAttached.getClass();
            searchResult.setAttachedToDomainType(convertClassToType(classOfAttachedTo.getName()));
        }
        searchResult.populate(searchCriteria, desiredDescriptionLines, maxSuffixLength);
        return searchResult;
    }

    private String convertClassToType(String className)
    {
        return (String) types.get(className);
    }

    public void setDesiredDescriptionLines(int desiredDescriptionLines)
    {
        this.desiredDescriptionLines = desiredDescriptionLines;
    }

    public void setMaxSuffixLength(int maxSuffixLength)
    {
        this.maxSuffixLength = maxSuffixLength;
    }
}
