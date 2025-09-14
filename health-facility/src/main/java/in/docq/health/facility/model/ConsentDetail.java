package in.docq.health.facility.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Builder(toBuilder = true)
@Getter
public class ConsentDetail {
    private final String schemaVersion;
    private final String consentId;
    private final Instant createdAt;
    private final Patient patient;
    private final List<CareContext> careContexts;
    private final Purpose purpose;
    private final Hip hip;
    private final Hiu hiu;
    private final ConsentManager consentManager;
    private final Requester requester;
    private final List<String> hiTypes;
    private final Permission permission;

    @Builder
    @Getter
    public static class Patient {
        private final String id;
    }

    @Builder
    @Getter
    public static class CareContext {
        private final String patientReference;
        private final String careContextReference;
    }

    @Builder
    @Getter
    public static class Purpose {
        private final String text;
        private final String code;
        private final String refUri;
    }

    @Builder
    @Getter
    public static class Hip {
        private final String id;
        private final String name;
        private final String type;
    }

    @Builder
    @Getter
    public static class Hiu {
        private final String id;
        private final String name;
        private final String type;
    }

    @Builder
    @Getter
    public static class ConsentManager {
        private final String id;
    }

    @Builder
    @Getter
    public static class Requester {
        private final String name;
        private final Identifier identifier;
    }

    @Builder
    @Getter
    public static class Identifier {
        private final String value;
        private final String type;
        private final String system;
    }

    @Builder
    @Getter
    public static class Permission {
        private final String accessMode;
        private final DateRange dateRange;
        private final Instant dataEraseAt;
        private final Frequency frequency;
    }

    @Builder
    @Getter
    public static class DateRange {
        private final Instant from;
        private final Instant to;
    }

    @Builder
    @Getter
    public static class Frequency {
        private final String unit;
        private final int value;
        private final int repeats;
    }
}

