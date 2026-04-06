package ru.yandex.practicum.gym;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        Assert.assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
        Assert.assertEquals(0, timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size());

        //Проверить, что за понедельник вернулось одно занятие
        //Проверить, что за вторник не вернулось занятий
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        Assert.assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00

        Map<TimeOfDay, List<TrainingSession>> thursdayMap = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assert.assertEquals(2, thursdayMap.size()); // Два ключа времени

        List<TimeOfDay> times = new ArrayList<>(thursdayMap.keySet());
        Assert.assertEquals(new TimeOfDay(13, 0), times.get(0));
        Assert.assertEquals(new TimeOfDay(20, 0), times.get(1));

        Assert.assertEquals(1, thursdayMap.get(new TimeOfDay(13, 0)).size());
        Assert.assertEquals(1, thursdayMap.get(new TimeOfDay(20, 0)).size());
        // Проверить, что за вторник не вернулось занятий
        Assert.assertEquals(0, timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size());

    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> sessionsAt13 =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        Assert.assertNotNull(sessionsAt13);
        Assert.assertEquals(1, sessionsAt13.size());
        Assert.assertEquals(singleTrainingSession, sessionsAt13.get(0));
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> sessionsAt14 =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        Assert.assertNotNull(sessionsAt14);
        Assert.assertTrue(sessionsAt14.isEmpty());

    }

    @Test
    void testGetTrainingSessionsForDayAndTimeMultipleSessions() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Иванова", "Мария", "Петровна");
        Group group1 = new Group("Йога утро", Age.ADULT, 60);
        Group group2 = new Group("Йога вечер", Age.ADULT, 75);

        TrainingSession session1 =
                new TrainingSession(group1, coach, DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));
        TrainingSession session2 =
                new TrainingSession(group2, coach, DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        List<TrainingSession> result =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(session1));
        Assert.assertTrue(result.contains(session2));
    }

    @Test
    void testGetTrainingSessionsForDayAndTimeNoSuchDay() {
        Timetable timetable = new Timetable();

        List<TrainingSession> result =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.FRIDAY, new TimeOfDay(10, 0));
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    void testAddTrainingSessionToExistingTimeSlot() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Смирнов", "Алексей", "Викторович");
        Group groupA = new Group("Пилатес", Age.ADULT, 50);
        Group groupB = new Group("Стретчинг", Age.ADULT, 45);

        TrainingSession sessionA =
                new TrainingSession(groupA, coach, DayOfWeek.TUESDAY, new TimeOfDay(19, 0));
        TrainingSession sessionB =
                new TrainingSession(groupB, coach, DayOfWeek.TUESDAY, new TimeOfDay(19, 0));

        timetable.addNewTrainingSession(sessionA);
        timetable.addNewTrainingSession(sessionB);

        Map<TimeOfDay, List<TrainingSession>> dayMap = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        List<TrainingSession> sessionsAt19 = dayMap.get(new TimeOfDay(19, 0));
        Assert.assertEquals(2, sessionsAt19.size());

        List<TrainingSession> result =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.TUESDAY, new TimeOfDay(19, 0));
        Assert.assertEquals(2, result.size());
    }

    @Test
    void testGetCountByCoachesMultipleCoaches() {
        Timetable timetable = new Timetable();
        Coach coachIvanov = new Coach("Иванов", "Иван", "Иванович");
        Coach coachPetrov = new Coach("Петров", "Петр", "Петрович");
        Coach coachSidorov = new Coach("Сидоров", "Сидор", "Сидорович");
        Group group = new Group("Фитнес", Age.ADULT, 60);

        // Иванов: 3 тренировки
        timetable.addNewTrainingSession(
                new TrainingSession(group, coachIvanov, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(
                new TrainingSession(group, coachIvanov, DayOfWeek.WEDNESDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(
                new TrainingSession(group, coachIvanov, DayOfWeek.FRIDAY, new TimeOfDay(15, 0)));
        // Петров: 2 тренировки
        timetable.addNewTrainingSession(
                new TrainingSession(group, coachPetrov, DayOfWeek.TUESDAY, new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(
                new TrainingSession(group, coachPetrov, DayOfWeek.THURSDAY, new TimeOfDay(14, 0)));
        // Сидоров: 1 тренировка
        timetable.addNewTrainingSession(
                new TrainingSession(group, coachSidorov, DayOfWeek.SATURDAY, new TimeOfDay(9, 0)));

        Map<Coach, Integer> result = timetable.getCountByCoaches();

        // Проверяем размер
        Assert.assertEquals(3, result.size());

        // Проверяем порядок: сначала Иванов (3), потом Петров (2), потом Сидоров (1)
        List<Coach> coachesInOrder = new ArrayList<>(result.keySet());
        Assert.assertEquals(coachIvanov, coachesInOrder.get(0));
        Assert.assertEquals(coachPetrov, coachesInOrder.get(1));
        Assert.assertEquals(coachSidorov, coachesInOrder.get(2));

        // Проверяем значения
        Assert.assertEquals(3, result.get(coachIvanov).intValue());
        Assert.assertEquals(2, result.get(coachPetrov).intValue());
        Assert.assertEquals(1, result.get(coachSidorov).intValue());
    }

    @Test
    void testGetCountByCoachesEmptyTimetable() {
        Timetable timetable = new Timetable(); // без добавления тренировок

        Map<Coach, Integer> result = timetable.getCountByCoaches();

        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    void testGetCountByCoachesSingleCoachMultipleSessions() {
        //Один тренер, несколько тренировок в разное время и дни
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Смирнова", "Анна", "Викторовна");
        Group group = new Group("Йога", Age.ADULT, 75);

        timetable.addNewTrainingSession(
                new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(8, 0)));
        timetable.addNewTrainingSession(
                new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(18, 0)));
        timetable.addNewTrainingSession(
                new TrainingSession(group, coach, DayOfWeek.WEDNESDAY, new TimeOfDay(8, 0)));
        timetable.addNewTrainingSession(
                new TrainingSession(group, coach, DayOfWeek.FRIDAY, new TimeOfDay(10, 0)));

        Map<Coach, Integer> result = timetable.getCountByCoaches();

        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.containsKey(coach));
        Assert.assertEquals(4, result.get(coach).intValue());
    }

}
