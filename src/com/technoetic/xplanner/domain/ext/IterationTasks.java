package com.technoetic.xplanner.domain.ext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.UserStory;

//DEBT: We may need to fold this into Iteration. See if there are other places that use this otherwise keep it this way
public class IterationTasks extends Iteration {

    Iteration iteration;

    public IterationTasks(Iteration iteration){
        this.iteration = iteration;
    }

    public Collection getIterationTasks(){
       Collection iterationTasks = new HashSet();
       Iterator storyIterator = iteration.getUserStories().iterator();
       while(storyIterator.hasNext()){
          UserStory userStory = (UserStory)storyIterator.next();
          Collection storyTasks = userStory.getTasks();
          iterationTasks.addAll(storyTasks);
       }
       return iterationTasks;
    }
}
