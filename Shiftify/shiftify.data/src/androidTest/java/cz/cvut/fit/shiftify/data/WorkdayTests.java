package cz.cvut.fit.shiftify.data;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;

import org.greenrobot.greendao.database.Database;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import cz.cvut.fit.shiftify.data.managers.ScheduleTypeManager;
import cz.cvut.fit.shiftify.data.managers.UserManager;
import cz.cvut.fit.shiftify.data.models.ExceptionInSchedule;
import cz.cvut.fit.shiftify.data.models.Schedule;
import cz.cvut.fit.shiftify.data.models.ScheduleShift;
import cz.cvut.fit.shiftify.data.models.ScheduleType;
import cz.cvut.fit.shiftify.data.models.User;
import cz.cvut.fit.shiftify.data.models.UserDao;

import static org.junit.Assert.assertEquals;

/**
 * Created by lukas on 16.11.2016.
 */

@RunWith(AndroidJUnit4.class)
public class WorkdayTests {
    @Before
    public void setUp() throws Exception {
        UserManager userManager = new UserManager();
        userManager.deleteScheduleAll();
        userManager.deleteAll();
        ScheduleTypeManager scheduleTypeManager = new ScheduleTypeManager();
        scheduleTypeManager.deleteAll();
    }

    @Test
    public void testCalculateWorkDays() throws Exception {
        ScheduleTypeManager scheduleTypeManager = new ScheduleTypeManager();
        List<ScheduleType> scheduleTypes = new ArrayList<>();
        List<ScheduleShift> shifts = new ArrayList<>();
        shifts.add(new ScheduleShift("1. ranní", new LocalTime(6, 0), new Period(8, 0, 0, 0), 2, "To se bude blbě vstávat."));
        shifts.add(new ScheduleShift("1. odpolední", new LocalTime(14, 0), new Period(8, 0, 0, 0), 3));
        shifts.add(new ScheduleShift("noční", new LocalTime(22, 0), new Period(6, 0, 0, 0), 5));
        shifts.add(new ScheduleShift("2. odpolední", new LocalTime(14, 0), new Period(8, 0, 0, 0),  6));
        ScheduleType scheduleType = new ScheduleType("Test Železárny", 6, "Slouží jako rozpis pro vrátnýho.");
        scheduleType.setShifts(shifts);
        scheduleTypes.add(scheduleType);

        scheduleTypeManager.add(scheduleType);

        shifts = new ArrayList<>();
        shifts.add(new ScheduleShift("1. ranní", new LocalTime(6, 0), new Period(8, 0, 0, 0), 1, "To se bude blbě vstávat."));
        shifts.add(new ScheduleShift("2. ranní", new LocalTime(6, 0), new Period(8, 0, 0, 0), 2));
        shifts.add(new ScheduleShift("1. odpolední", new LocalTime(14, 0), new Period(8, 0, 0, 0), 3));
        shifts.add(new ScheduleShift("2. odpolední", new LocalTime(14, 0), new Period(8, 0, 0, 0), 4, "Já jsem poznámka."));
        shifts.add(new ScheduleShift("1. noční", new LocalTime(22, 0), new Period(8, 0, 0, 0), 5));
        shifts.add(new ScheduleShift("2. noční", new LocalTime(22, 0), new Period(8, 0, 0, 0), 6, "komentář"));
        scheduleType = new ScheduleType("Test Železárny hasič", 8);
        scheduleType.setShifts(shifts);
        scheduleTypes.add(scheduleType);

        scheduleTypeManager.add(scheduleType);

        UserManager userManager = new UserManager();
        User user = new User("Lukáš", "Komárek");
        userManager.add(user);

        List<Schedule> schedules = new ArrayList<>();
        Schedule schedule = new Schedule(user.getId(), scheduleTypes.get(0).getId(), new LocalDate(2017, 1, 2), new LocalDate(2017, 2, 9), 3);
        schedules.add(schedule);

        userManager.addSchedule(schedule);

        schedule = new Schedule(user.getId(), scheduleTypes.get(1).getId(), new LocalDate(2017, 2, 10), new LocalDate(2017, 3, 9), 4);
        schedules.add(schedule);

        userManager.addSchedule(schedule);

        WorkDay workDay = userManager.shiftsForDate(user.getId(), new LocalDate(2017, 2, 10));
        List<WorkDay> workDays = new WorkDayFactory(schedules, new ArrayList<ExceptionInSchedule>())
                .createWorkDayList(new LocalDate(2017, 2, 9), new LocalDate(2017, 2, 11));
    }
}