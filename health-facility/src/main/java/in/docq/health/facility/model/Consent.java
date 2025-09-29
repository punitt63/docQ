package in.docq.health.facility.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Builder(toBuilder = true)
@Getter
public class Consent {
    private final String schemaVersion;
    private final String consentId;
    private final String createdAt;
    private final Patient patient;
    private final List<CareContext> careContexts;
    private final Purpose purpose;
    private final Hip hip;
    private final Hiu hiu;
    private final ConsentManager consentManager;
    private final Requester requester;
    private final List<String> hiTypes;
    private final Permission permission;
    private final String status;

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
        private final String dataEraseAt;
        private final Frequency frequency;

        public Instant getDataEraseAtInstant() {
            return dataEraseAt != null ? Instant.parse(dataEraseAt) : null;
        }
    }

    @Builder
    @Getter
    public static class DateRange {
        private final String from;
        private final String to;

        public Instant getFromAsInstant() {
            return from != null ? Instant.parse(from) : null;
        }

        public Instant getToAsInstant() {
            return to != null ? Instant.parse(to) : null;
        }
    }

    @Builder
    @Getter
    public static class Frequency {
        private final String unit;
        private final int value;
        private final int repeats;
    }
}

