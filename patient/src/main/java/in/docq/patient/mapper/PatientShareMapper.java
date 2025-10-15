package in.docq.patient.mapper;

import in.docq.abha.rest.client.model.*;
import in.docq.patient.controller.PatientShareController;

import java.math.BigDecimal;
import java.util.UUID;

public class PatientShareMapper {

    public static AbdmPatientShareHip1Request toAbdmPatientShareHip1Request(PatientShareController.AbdmPatientShareHip1RequestBody requestBody) {
        AbdmPatientShareHip1Request request = new AbdmPatientShareHip1Request();
        
        // Set intent enum from requestBody.getIntent()
        if (requestBody.getIntent() != null) {
            try {
                AbdmPatientShareHip1Request.IntentEnum intentEnum = AbdmPatientShareHip1Request.IntentEnum.fromValue(requestBody.getIntent());
                request.setIntent(intentEnum);
            } catch (Exception e) {
                request.setIntent(AbdmPatientShareHip1Request.IntentEnum.PROFILE_SHARE);
            }
        }
        
        // Create PatientShare1RequestMetaData from nested metaData
        if (requestBody.getMetaData() != null) {
            PatientShare1RequestMetaData metaData = new PatientShare1RequestMetaData();
            PatientShareController.MetaData reqMetaData = requestBody.getMetaData();
            if (reqMetaData.getHipId() != null) metaData.setHipId(reqMetaData.getHipId());
            if (reqMetaData.getContext() != null) metaData.setContext(reqMetaData.getContext());
            if (reqMetaData.getHprId() != null) metaData.setHprId(reqMetaData.getHprId());
            if (reqMetaData.getLatitude() != null) metaData.setLatitude(reqMetaData.getLatitude());
            if (reqMetaData.getLongitude() != null) metaData.setLongitude(reqMetaData.getLongitude());
            request.setMetaData(metaData);
        }
        
        // Create AbdmPatientShareHip1RequestProfile from nested profile
        if (requestBody.getProfile() != null && requestBody.getProfile().getPatient() != null) {
            AbdmPatientShareHip1RequestProfile profile = new AbdmPatientShareHip1RequestProfile();
            AbdmPatientShareHip1RequestProfilePatient patient = new AbdmPatientShareHip1RequestProfilePatient();
            
            PatientShareController.Patient reqPatient = requestBody.getProfile().getPatient();
            
            // Set patient basic info
            if (reqPatient.getAbhaNumber() != null) patient.setAbhaNumber(reqPatient.getAbhaNumber());
            if (reqPatient.getAbhaAddress() != null) patient.setAbhaAddress(reqPatient.getAbhaAddress());
            if (reqPatient.getName() != null) patient.setName(reqPatient.getName());
            if (reqPatient.getGender() != null) {
                try {
                    patient.setGender(AbdmPatientShareHip1RequestProfilePatient.GenderEnum.valueOf(reqPatient.getGender()));
                } catch (Exception e) {
                    patient.setGender(AbdmPatientShareHip1RequestProfilePatient.GenderEnum.M);
                }
            }
            if (reqPatient.getDayOfBirth() != null) patient.setDayOfBirth(BigDecimal.valueOf(Long.parseLong(reqPatient.getDayOfBirth())));
            if (reqPatient.getMonthOfBirth() != null) patient.setMonthOfBirth(BigDecimal.valueOf(Long.parseLong(reqPatient.getMonthOfBirth())));
            if (reqPatient.getYearOfBirth() != null) patient.setYearOfBirth(BigDecimal.valueOf(Long.parseLong(reqPatient.getYearOfBirth())));
            if (reqPatient.getPhoneNumber() != null) patient.setPhoneNumber(reqPatient.getPhoneNumber());
            
            // Set patient address
            if (reqPatient.getAddress() != null) {
                PatientShareController.Address reqAddress = reqPatient.getAddress();
                PatientShare1RequestProfilePatientAddress address = new PatientShare1RequestProfilePatientAddress();
                if (reqAddress.getLine() != null) address.setLine(reqAddress.getLine());
                if (reqAddress.getDistrict() != null) address.setDistrict(reqAddress.getDistrict());
                if (reqAddress.getState() != null) address.setState(reqAddress.getState());
                if (reqAddress.getPincode() != null) address.setPincode(reqAddress.getPincode());
                patient.setAddress(address);
            }
            
            profile.setPatient(patient);
            request.setProfile(profile);
        }
        
        return request;
    }

    public static PatientShare2Request toPatientShare2Request(PatientShareController.PatientShare2RequestBody requestBody) {
        PatientShare2Request request = new PatientShare2Request();
        PatientShare2RequestOneOf oneOf = new PatientShare2RequestOneOf();
        
        // Based on available fields, create either acknowledgement, error, or response
        if (requestBody.getStatus() != null && requestBody.getAbhaAddress() != null) {
            // Create PatientShare2RequestOneOfAcknowledgement (if status and abhaAddress are present)
            PatientShare2RequestOneOfAcknowledgement acknowledgement = new PatientShare2RequestOneOfAcknowledgement();
            acknowledgement.setStatus(requestBody.getStatus());
            acknowledgement.setAbhaAddress(requestBody.getAbhaAddress());
            
            // Create profile within acknowledgement
            PatientShare2RequestOneOfAcknowledgementProfile profile = new PatientShare2RequestOneOfAcknowledgementProfile();
            acknowledgement.setProfile(profile);
            
            oneOf.setAcknowledgement(acknowledgement);
        } else if (requestBody.getErrorCode() != null && requestBody.getErrorMessage() != null) {
            // Create PatientShare2RequestOneOfError (if errorCode and errorMessage are present)
            PatientShare2RequestOneOfError error = new PatientShare2RequestOneOfError();
            error.setCode(requestBody.getErrorCode());
            error.setMessage(requestBody.getErrorMessage());
            oneOf.setError(error);
        } else if (requestBody.getRequestId() != null) {
            // Create PatientShare2RequestOneOfResponse (if requestId is present)
            PatientShare2RequestOneOfResponse response = new PatientShare2RequestOneOfResponse();
            response.setRequestId(UUID.fromString(requestBody.getRequestId()));
            oneOf.setResponse(response);
        }
        
        // Set the appropriate instance and return the request object
        request.setActualInstance(oneOf);
        return request;
    }
}
