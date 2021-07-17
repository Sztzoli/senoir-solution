package activitytracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ActivityDaoIT {

    ActivityDao activityDao;
    AreaDao areaDao;

    @BeforeEach
    void setUp() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("pu");
        activityDao = new ActivityDao(factory);
        areaDao = new AreaDao(factory);
    }

    @Test
    void saveThenFind() {
        Activity activity = new Activity(LocalDateTime.now(), "description", Type.BIKING);
        activityDao.saveActivity(activity);
        Activity loadedActivity = activityDao.findActivityById(activity.getId());

        assertEquals("description", loadedActivity.getDesc());
    }

    @Test
    void update() {
        Activity activity = new Activity(LocalDateTime.now(), "description", Type.BIKING);
        activityDao.saveActivity(activity);
        activityDao.updateActivity(activity.getId(), "new description");
        Activity loadedActivity = activityDao.findActivityById(activity.getId());

        assertEquals("new description", loadedActivity.getDesc());
    }

    @Test
    void getActivityWithLabels() {
        Activity activity = new Activity(LocalDateTime.now(), "description"
                , Type.BIKING, new ArrayList<>(List.of("Label1", "Label2")));
        activityDao.saveActivity(activity);
        Activity loadedActivity = activityDao.findActivityByIdWithLabels(activity.getId());

        assertEquals(2, loadedActivity.getLabels().size());
    }

    @Test
    void getActivityWithTrackPoints() {
        Activity activity = new Activity(LocalDateTime.now(), "description", Type.BIKING);
        activityDao.saveActivity(activity);
        TrackPoint trackPoint = new TrackPoint(LocalDate.now(), 1, -1);
        activityDao.addTrackPointsToActivity(activity.getId(), trackPoint);

        Activity loadedActivity = activityDao.findActivityByIdWithTrackPoints(activity.getId());

        assertEquals(1, loadedActivity.getTrackPoints().get(0).getLat());
    }

    @Test
    void getActivityWithAreas() {
        Activity activity = new Activity(LocalDateTime.now(), "description", Type.BIKING);
        Area area = new Area("area");
//        areaDao.save(area);
        activity.addArea(area);
        activityDao.saveActivity(activity);
        Activity loadedActivity = activityDao.findActivityByIdWithArea(activity.getId());

        assertEquals("area",loadedActivity.getAreas().get(0).getName());
    }

    @Test
    void addAreaToActivity() {
        Activity activity = new Activity(LocalDateTime.now(), "description", Type.BIKING);
        Area area = new Area("area");
        activityDao.saveActivity(activity);
        activityDao.addAreaToActivity(activity.getId(),area);
    }

    @Test
    void saveToSecondaryTable() {
        Activity activity = new Activity(LocalDateTime.now(), "description", Type.BIKING, 10,120);
        activityDao.saveActivity(activity);

        Activity loadedActivity = activityDao.findActivityById(activity.getId());
        assertEquals(10, activity.getDistance());
    }

    @Test
    void findTrackPointCountByActivity(){
        Activity activity = new Activity(LocalDateTime.now(), "description", Type.BIKING, 10,120);
        activityDao.saveActivity(activity);
        activityDao.addTrackPointsToActivity(activity.getId(), new TrackPoint(activity.getStartTime().toLocalDate(),1,-1));
        activityDao.addTrackPointsToActivity(activity.getId(), new TrackPoint(activity.getStartTime().toLocalDate(),2,-2));
        List<Object[]> trackPointCountByActivity = activityDao.findTrackPointCountByActivity(activity);

        assertEquals(1, trackPointCountByActivity.size());
        assertEquals(2L, trackPointCountByActivity.get(0));
    }



}