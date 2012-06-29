package com.technoetic.xplanner.importer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.UserStory;

import com.technoetic.xplanner.importer.spreadsheet.SpreadsheetHeaderConfiguration;
import com.technoetic.xplanner.importer.spreadsheet.SpreadsheetStoryFilter;
import com.technoetic.xplanner.importer.spreadsheet.SpreadsheetStoryReader;

public class SpreadsheetStoryImporter
{
   private final SpreadsheetStoryFactory spreadsheetStoryFactory;

   public SpreadsheetStoryImporter(SpreadsheetStoryFactory spreadsheetStoryFactory)
   {
      this.spreadsheetStoryFactory = spreadsheetStoryFactory;
   }

   public List importStories(Iteration iteration, SpreadsheetHeaderConfiguration headerConfiguration,
                             InputStream inputStream, boolean onlyUncompleted) throws IOException
   {
      List newStories = new ArrayList();
      List stories = readStoriesFromSpreadsheet(headerConfiguration, inputStream);
      SpreadsheetStoryFilter storyFilter = new SpreadsheetStoryFilter(iteration.getStartDate(),
                                                                      iteration.getEndDate());
      for (Iterator iterator = stories.iterator(); iterator.hasNext();)
      {
         SpreadsheetStory spreadsheetStory = (SpreadsheetStory) iterator.next();
         if (!storyFilter.matches(spreadsheetStory) || (onlyUncompleted && spreadsheetStory.getStatus().equalsIgnoreCase("C")))
            continue;
         UserStory userStory = new UserStory();
         if (spreadsheetStory.getTitle() == null || "".equals(spreadsheetStory.getTitle().trim()))
            throw new MissingFieldSpreadsheetImporterException("name", "missing field");
         userStory.setName(spreadsheetStory.getTitle());
         userStory.setEstimatedHoursField(spreadsheetStory.getEstimate());
         userStory.setIteration(iteration);
         userStory.setPriority(spreadsheetStory.getPriority());
         iteration.getUserStories().add(userStory);
         userStory.setIteration(iteration);
         newStories.add(userStory);
      }
      return newStories;
   }

   protected List readStoriesFromSpreadsheet(SpreadsheetHeaderConfiguration headerConfiguration,
                                             InputStream inputStream) throws IOException
   {
      return new SpreadsheetStoryReader(spreadsheetStoryFactory).readStories(headerConfiguration, inputStream);
   }
}
