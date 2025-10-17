package in.docq.health.facility.model;

import in.docq.health.facility.controller.OPDController;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@Getter
public class OPD {
    private final String id;
    private final String healthFacilityID;
    private final String healthProfessionalID;
    private final String name;
    private final int startHour;
    private final int endHour;
    private final int startMinute;
    private final int endMinute;
    private final LocalDate date;
    private final State state;
    private final int maxSlots;
    private final int minutesPerSlot;
    private final long activateTime;
    private final Long actualStartTime;
    private final Long actualEndTime;
    private final int appointmentsCount;

    public HealthProfessional getDoctor() {
        return HealthProfessional.builder()
                .healthFacilityID(healthFacilityID)
                .id(healthProfessionalID)
                .type(HealthProfessionalType.DOCTOR)
                .build();
    }

    public String getSequenceName() {
        return "sequence" + "_" + id.replace("-", "_");
    }

    public static String getOPDSequenceName(String id) {
        return "sequence" + "_" + id.replace("-", "_");
    }

    public static List<OPD> getEffectiveOPDs(String healthFacilityID, String healthProfessionalID, OPDController.CreateOPDRequestBody createOPDRequestBody) {
        if(createOPDRequestBody.getScheduleType().equals(ScheduleType.WEEKLY)) {
            List<LocalDate> opdDates = getDaysMatchingWeeklyTemplate(createOPDRequestBody.getStartDateAsLocalDate(), createOPDRequestBody.getEndDateAsLocalDate(), createOPDRequestBody.getWeeklyTemplate());
            return opdDates.stream()
                    .map(date -> OPD.builder().from(healthFacilityID, healthProfessionalID, date, createOPDRequestBody).build())
                    .collect(Collectors.toList());
        }
        throw new RuntimeException("Schedule Type NOt Supported Exception");
    }

    public static List<LocalDate> getDaysMatchingWeeklyTemplate(LocalDate startDate, LocalDate endDate, List<Boolean> weeklyTemplate) {
        List<LocalDate> result = new ArrayList<>();

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        // Loop through each day
        for (int i = 0; i < daysBetween; i++) {
            LocalDate currentDate = startDate.plusDays(i);

            // Get day of week (1 = Monday, 7 = Sunday in Java DayOfWeek enum)
            int dayOfWeekValue = currentDate.getDayOfWeek().getValue();

            // Check if this day of week is included in our template (adjust index for zero-based array)
            if (Boolean.TRUE.equals(weeklyTemplate.get(dayOfWeekValue - 1))) {
                result.add(currentDate);
            }
        }

        return result;
    }

    public enum ScheduleType {
        WEEKLY
    }

    public enum State {
        INACTIVE,
        ACTIVE,
        PAUSED,
        COMPLETED
    }

    public static class OPDBuilder {
        public OPDBuilder from(String healthFacilityID, String healthProfessionalID, LocalDate date, OPDController.CreateOPDRequestBody createOPDRequestBody) {
            Instant activateInstant = date.atStartOfDay(ZoneId.of("Asia/Kolkata")).toInstant()
                    .plus(startHour, ChronoUnit.HOURS)
                    .plus(startMinute, ChronoUnit.MINUTES)
                    .minus(createOPDRequestBody.getMinutesToActivate(), ChronoUnit.MINUTES);
            return this
                    .id(UUID.randomUUID().toString())
                    .healthFacilityID(healthFacilityID)
                    .healthProfessionalID(healthProfessionalID)
                    .name(createOPDRequestBody.getName())
                    .startHour(createOPDRequestBody.getStartHour())
                    .endHour(createOPDRequestBody.getEndHour())
                    .startMinute(createOPDRequestBody.getStartMinute())
                    .endMinute(createOPDRequestBody.getEndMinute())
                    .date(date)
                    .state(State.INACTIVE)
                    .maxSlots(createOPDRequestBody.getMaxSlots())
                    .minutesPerSlot(createOPDRequestBody.getMinutesPerSlot())
                    .activateTime(activateInstant.toEpochMilli());
        }

        public OPDBuilder from(OPD existingOPD, OPDController.UpdateOPDRequestBody updateOPDRequestBody) {
            long activateTimeInEpochMilli = Optional.ofNullable(updateOPDRequestBody.getMinutesToActivate())
                    .map(minutesToActivate ->  existingOPD.getDate().atStartOfDay(ZoneId.of("Asia/Kolkata")).toInstant()
                            .plus(ObjectUtils.firstNonNull(updateOPDRequestBody.getStartHour(), existingOPD.getStartHour()), ChronoUnit.HOURS)
                            .plus(ObjectUtils.firstNonNull(updateOPDRequestBody.getStartMinute(), existingOPD.getStartMinute()), ChronoUnit.MINUTES)
                            .minus(minutesToActivate, ChronoUnit.MINUTES).toEpochMilli())
                    .orElse(existingOPD.activateTime);
            return this
                    .id(existingOPD.getId())
                    .healthFacilityID(existingOPD.getHealthFacilityID())
                    .healthProfessionalID(existingOPD.getHealthProfessionalID())
                    .name(existingOPD.getName())
                    .date(existingOPD.getDate())
                    .startHour(ObjectUtils.firstNonNull(updateOPDRequestBody.getStartHour(), existingOPD.getStartHour()))
                    .endHour(ObjectUtils.firstNonNull(updateOPDRequestBody.getEndHour(), existingOPD.getEndHour()))
                    .startMinute(ObjectUtils.firstNonNull(updateOPDRequestBody.getStartMinute(), existingOPD.getStartMinute()))
                    .endMinute(ObjectUtils.firstNonNull(updateOPDRequestBody.getEndMinute(), existingOPD.getEndMinute()))
                    .state(ObjectUtils.firstNonNull(updateOPDRequestBody.getState(), existingOPD.getState()))
                    .maxSlots(ObjectUtils.firstNonNull(updateOPDRequestBody.getMaxSlots(), existingOPD.getMaxSlots()))
                    .minutesPerSlot(ObjectUtils.firstNonNull(updateOPDRequestBody.getMinutesPerSlot(), existingOPD.getMinutesPerSlot()))
                    .activateTime(activateTimeInEpochMilli)
                    .actualStartTime(ObjectUtils.firstNonNull(updateOPDRequestBody.getActualStartTime(), existingOPD.getActualStartTime()))
                    .actualEndTime(ObjectUtils.firstNonNull(updateOPDRequestBody.getActualEndTime(), existingOPD.getActualEndTime()));
        }
    }
}
