package com.technoetic.xplanner.metrics;


@Deprecated
public class UserStoryMetrics {
//    private final Logger log = Logger.getLogger(getClass());
//    private final HashMap developerMetrics = new HashMap();
//    private double totalHours;
//    private int userStoryId;
//    private double maxDeveloperHours;
//
//    public void setUserStoryId(int userStoryId) {
//        this.userStoryId = userStoryId;
//    }

//    public void analyze() {
//        Map<Object, String> names = new HashMap<Object, String>();
//        try {
////            Session session = GlobalSessionFactory.get().openSession();
//            Session session = ThreadSession.get();
//            try {
//                names.clear();
//                List nameResults = session.getNamedQuery("namesQuery").list();
//                Iterator iter = nameResults.iterator();
//                while (iter.hasNext()) {
//                    Object[] result = (Object[])iter.next();
//                    names.put(result[1], result[0]);
//                }
//
//                developerMetrics.clear();
//                List acceptedTasks =
//                    session.getNamedQuery("acceptedTaskInStoryQuery").setInteger("storyId", userStoryId).list();
//                Iterator acceptedTaskIter = acceptedTasks.iterator();
//                while (acceptedTaskIter.hasNext()) {
//                    Object[] result = (Object[])acceptedTaskIter.next();
//                    double acceptedHours = toDouble(result[2]);
//                    if (acceptedHours > 0.0) {
//                        getDeveloperMetrics(
//                                names.get(result[1]),
//                                toInt(result[1]),
//                                userStoryId).setAcceptedTaskHours(
//                                        acceptedHours);
//                    }
//                }
//
//                totalHours = 0.0;
//                maxDeveloperHours = 0.0;
//                List hoursResults =
//                    session.getNamedQuery("storyHoursWorkedQuery").setInteger("storyId", userStoryId).list();
//                Iterator hoursIterator = hoursResults.iterator();
//                while (hoursIterator.hasNext()) {
//                    Object[] result = (Object[])hoursIterator.next();
//                    int person1Id = toInt(result[0]);
//                    int person2Id = toInt(result[1]);
//                    Date startTime = (Date)result[2];
//                    Date endTime = (Date)result[3];
//                    double duration = toDouble(result[5]);
//                    if ((endTime != null && startTime != null) || duration != 0) {
//                        double hours =
//                                duration == 0 ? (endTime.getTime() - startTime.getTime()) / 3600000.0 : duration;
//                        boolean isPaired = person1Id != 0 && person2Id != 0;
//                        if (person1Id != 0) {
//                            updateWorkedHours(
//                                    userStoryId,
//                                    names.get(result[0]),
//                                    person1Id,
//                                    hours,
//                                    isPaired
//                            );
//                            totalHours += hours;
//                        }
//                        if (person2Id != 0) {
//                            updateWorkedHours(
//                                    userStoryId,
//                                    names.get(result[1]),
//                                    person2Id,
//                                    hours,
//                                    isPaired
//                            );
//                            totalHours += hours;
//                        }
//                    }
//                }
//            } catch (Exception ex) {
//                if (session.isConnected()) {
//                    session.connection().rollback();
//                }
//                log.error("error", ex);
//            } finally {
//                session.close();
//            }
//        } catch (Exception ex) {
//            log.error("error", ex);
//        }
//    }
//
//    private int toInt(Object object) {
//        return ((Integer)object).intValue();
//    }
//
//    private double toDouble(Object object) {
//        return ((Double)object).doubleValue();
//    }
//
//    private DeveloperMetrics getDeveloperMetrics(String name, int id, int userStoryId) {
//        DeveloperMetrics dm = (DeveloperMetrics)developerMetrics.get(name);
//        if (dm == null) {
//            dm = new DeveloperMetrics();
//            dm.setId(id);
//            dm.setName(name);
//            dm.setIterationId(userStoryId);
//            developerMetrics.put(name, dm);
//        }
//        return dm;
//    }
//
//    private void updateWorkedHours(int userStoryId, String name, int id, double hours,
//            boolean isPaired) {
//        DeveloperMetrics dm = getDeveloperMetrics(name, id, userStoryId);
//        dm.setHours(dm.getHours() + hours);
//        if (dm.getHours() > maxDeveloperHours) {
//            maxDeveloperHours = dm.getHours();
//        }
//        if (isPaired) {
//            dm.setPairedHours(dm.getPairedHours() + hours);
//        }
//    }
//
//    public double getTotalHours() {
//        return totalHours;
//    }
//
//    public void setTotalHours(double totalHours) {
//        this.totalHours = totalHours;
//    }
//
//    public Collection getDeveloperTotalTime() {
//        ArrayList metrics = new ArrayList(developerMetrics.values());
//        Collections.sort(metrics, new Comparator() {
//            public int compare(Object object1, Object object2) {
//                DeveloperMetrics dm1 = (DeveloperMetrics)object1;
//                DeveloperMetrics dm2 = (DeveloperMetrics)object2;
//                return (dm1.getHours() < dm2.getHours()) ? 1 : (dm1.getHours() == dm2.getHours() ? 0 : -1);
//            }
//        });
//        return metrics;
//    }
//
//    public double getMaxTotalTime() {
//        double maxTotalTime = 0;
//        Iterator itr = developerMetrics.values().iterator();
//        while (itr.hasNext()) {
//            double hours = ((DeveloperMetrics)itr.next()).getHours();
//            if (hours > maxTotalTime) {
//                maxTotalTime = hours;
//            }
//        }
//        return maxTotalTime;
//    }
//
//    public Collection getDeveloperAcceptedTime() {
//        ArrayList metrics = new ArrayList(developerMetrics.values());
//        Collections.sort(metrics, new Comparator() {
//            public int compare(Object object1, Object object2) {
//                DeveloperMetrics dm1 = (DeveloperMetrics)object1;
//                DeveloperMetrics dm2 = (DeveloperMetrics)object2;
//                return (dm1.getAcceptedHours() < dm2.getAcceptedHours())
//                        ? 1
//                        : (dm1.getAcceptedHours() == dm2.getAcceptedHours() ? 0 : -1);
//            }
//        });
//        return metrics;
//    }
//
//    public double getMaxAcceptedTime() {
//        double maxAcceptedTime = 0;
//        Iterator itr = developerMetrics.values().iterator();
//        while (itr.hasNext()) {
//            double hours = ((DeveloperMetrics)itr.next()).getAcceptedHours();
//            if (hours > maxAcceptedTime) {
//                maxAcceptedTime = hours;
//            }
//        }
//        return maxAcceptedTime;
//    }
//
//    public int getUserStoryId() {
//        return userStoryId;
//    }

}