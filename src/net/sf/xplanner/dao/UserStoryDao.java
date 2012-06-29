package net.sf.xplanner.dao;

import java.util.List;

import net.sf.xplanner.domain.UserStory;

public interface UserStoryDao extends Dao<UserStory> {
	List<UserStory> getAllUserStrories(int iterationId);

	List<UserStory> getStoriesForPersonWhereCustomer(int personId);

	List<UserStory> getStoriesForPersonWhereTracker(int personId);
}
