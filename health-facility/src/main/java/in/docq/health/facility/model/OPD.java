package in.docq.health.facility.model;

import in.docq.health.facility.controller.OPDController;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class OPD {
    private String healthFacilityID;
    private String healthProfessionalID;
    private String name;
    private int startHour;
    private int endHour;
    private int startMinute;
    private int endMinute;
    private boolean recurring;
    private LocalDate startDate;
    private ScheduleType scheduleType;
    private List<Boolean> weeklyTemplate;
    private State state;
    private int maxSlots;
    private int minutesPerSlot;
    private int instanceCreationMinutesBeforeStart;

    public enum ScheduleType {
        WEEKLY
    }

    public enum State {
        ACTIVE,
        DELETED
    }

    public static class OPDBuilder {
        public OPDBuilder from(String healthFacilityID, String healthProfessionalID, OPDController.CreateOPDRequestBody createOPDRequestBody) {
            return this
                    .healthFacilityID(healthFacilityID)
                    .healthProfessionalID(healthProfessionalID)
                    .name(createOPDRequestBody.getName())
                    .startHour(createOPDRequestBody.getStartHour())
                    .endHour(createOPDRequestBody.getEndHour())
                    .startMinute(createOPDRequestBody.getStartMinute())
                    .endMinute(createOPDRequestBody.getEndMinute())
                    .recurring(createOPDRequestBody.isRecurring())
                    .startDate(LocalDate.parse(createOPDRequestBody.getStartDate()))
                    .scheduleType(createOPDRequestBody.getScheduleType())
                    .weeklyTemplate(createOPDRequestBody.getWeeklyTemplate())
                    .state(State.ACTIVE)
                    .maxSlots(createOPDRequestBody.getMaxSlots())
                    .minutesPerSlot(createOPDRequestBody.getMinutesPerSlot())
                    .instanceCreationMinutesBeforeStart(createOPDRequestBody.getInstanceCreationMinutesBeforeStart());
        }

        public OPDBuilder from(OPD existingOPD, OPDController.UpdateOPDRequestBody updateOPDRequestBody) {
            return this
                    .healthFacilityID(existingOPD.getHealthFacilityID())
                    .healthProfessionalID(existingOPD.getHealthProfessionalID())
                    .name(existingOPD.getName())
                    .startHour(ObjectUtils.firstNonNull(updateOPDRequestBody.getStartHour(), existingOPD.getStartHour()))
                    .endHour(ObjectUtils.firstNonNull(updateOPDRequestBody.getEndHour(), existingOPD.getEndHour()))
                    .startMinute(ObjectUtils.firstNonNull(updateOPDRequestBody.getStartMinute(), existingOPD.getStartMinute()))
                    .endMinute(ObjectUtils.firstNonNull(updateOPDRequestBody.getEndMinute(), existingOPD.getEndMinute()))
                    .recurring(ObjectUtils.firstNonNull(updateOPDRequestBody.isRecurring(), existingOPD.isRecurring()))
                    .scheduleType(ObjectUtils.firstNonNull(updateOPDRequestBody.getScheduleType(), existingOPD.getScheduleType()))
                    .weeklyTemplate(ObjectUtils.firstNonNull(updateOPDRequestBody.getWeeklyTemplate(), existingOPD.getWeeklyTemplate()))
                    .state(ObjectUtils.firstNonNull(updateOPDRequestBody.getState(), existingOPD.getState()))
                    .maxSlots(ObjectUtils.firstNonNull(updateOPDRequestBody.getMaxSlots(), existingOPD.getMaxSlots()))
                    .minutesPerSlot(ObjectUtils.firstNonNull(updateOPDRequestBody.getMinutesPerSlot(), existingOPD.getMinutesPerSlot()))
                    .instanceCreationMinutesBeforeStart(ObjectUtils.firstNonNull(updateOPDRequestBody.getInstanceCreationMinutesBeforeStart(), existingOPD.getInstanceCreationMinutesBeforeStart()));
        }
    }
}
