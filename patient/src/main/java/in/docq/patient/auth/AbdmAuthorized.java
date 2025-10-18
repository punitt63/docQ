package in.docq.patient.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("unused")
public @interface AbdmAuthorized {
    /**
     * Specify if the patient ID should be validated from path variable
     * Default is true - validates that the authenticated patient matches the requested patient
     */
    boolean validatePatientId() default true;
    
    /**
     * Name of the path variable containing patient ID (default: "patient-id")
     */
    String patientIdParam() default "patient-id";
    
    /**
     * Resource being accessed (for logging and audit purposes)
     */
    String resource() default "";
}
