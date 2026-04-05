package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        this.timetable = new HashMap<>();
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();
        TimeOfDay timeOfDay = trainingSession.getTimeOfDay();

        Map<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(dayOfWeek);

        if (dayMap == null) {
            dayMap = new TreeMap<>();
            timetable.put(dayOfWeek, dayMap);
        }

        List<TrainingSession> sessions = dayMap.get(timeOfDay);

        if (sessions == null) {
            sessions = new ArrayList<>();
            dayMap.put(timeOfDay,sessions);
        }

        sessions.add(trainingSession);
    }



    public Map<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.getOrDefault(dayOfWeek, Collections.emptyMap());
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        return timetable.getOrDefault(dayOfWeek, Collections.emptyMap()).getOrDefault(timeOfDay,Collections.emptyList());
    }

    public Map<Coach, Integer> getCountByCoaches() {

        Map<Coach, Integer> countByCoaches = new HashMap<>();

        for (Map<TimeOfDay, List<TrainingSession>> dayMap : timetable.values()) {
            for (List<TrainingSession> sessions : dayMap.values()) {
                for (TrainingSession ts : sessions) {
                    Coach coach = ts.getCoach();
                    countByCoaches.merge(coach, 1, Integer::sum);
                }
            }
        }

        List<Map.Entry<Coach, Integer>> list = new ArrayList<>(countByCoaches.entrySet());
        list.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        Map<Coach, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<Coach, Integer> e : list) {
            result.put(e.getKey(), e.getValue());
        }
        return result;
    }
}
