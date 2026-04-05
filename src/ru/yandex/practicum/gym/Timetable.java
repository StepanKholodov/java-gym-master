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
}
