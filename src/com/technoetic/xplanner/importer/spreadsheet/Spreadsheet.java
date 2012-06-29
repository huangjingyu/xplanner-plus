package com.technoetic.xplanner.importer.spreadsheet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.technoetic.xplanner.importer.SpreadsheetStory;
import com.technoetic.xplanner.importer.SpreadsheetStoryFactory;
import com.technoetic.xplanner.importer.util.IOStreamFactory;

public class Spreadsheet
{
   protected String path;
   protected List/*<Story>*/ stories = new ArrayList();
   private IOStreamFactory streamFactory;
   private SpreadsheetStoryFactory spreadsheetStoryFactory;

   public Spreadsheet(IOStreamFactory streamFactory, SpreadsheetStoryFactory spreadsheetStoryFactory)
   {
      this.streamFactory = streamFactory;
      this.spreadsheetStoryFactory = spreadsheetStoryFactory;
   }

   public List getStories()
   {
      return Collections.unmodifiableList(stories);
   }

   public String getPath()
   {
      return path;
   }

   public void open(String path) throws IOException
   {
      this.path = path;

      try
      {
         InputStream stream = streamFactory.newInputStream(path);
         //TODO
         stories = new SpreadsheetStoryReader(spreadsheetStoryFactory).readStories(null, stream);
      }
      catch (IOException e)
      {
      }
   }

   public void save() throws IOException
   {
      new SpreadsheetStoryWriter(new FileOutputStream(path)).writeStories(stories);
   }

   public void saveAs(String path) throws IOException
   {
      this.path = path;
      save();
   }

   public void setStories(List stories)
   {
      this.stories = new ArrayList(stories);
   }

   public SpreadsheetStoryFactory getStoryFactory()
   {
      return spreadsheetStoryFactory;
   }

   public void setStoryFactory(SpreadsheetStoryFactory spreadsheetStoryFactory)
   {
      this.spreadsheetStoryFactory = spreadsheetStoryFactory;
   }

   public void addStory(SpreadsheetStory spreadsheetStory)
   {
      stories.add(spreadsheetStory);
   }

}
