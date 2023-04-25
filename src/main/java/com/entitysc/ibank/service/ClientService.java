package com.entitysc.ibank.service;

import com.entitysc.ibank.payload.IBankPayload;
import com.entitysc.ibank.payload.PylonPayload;
import java.util.List;

/**
 *
 * @author briano
 */
public interface ClientService {

    PylonPayload processSignin(IBankPayload requestPayload);

    PylonPayload changePassword(IBankPayload requestPayload);

    PylonPayload processSignUp(IBankPayload requestPayload);

    PylonPayload processSignUpActivation(String id);

    PylonPayload validateTwoFactorAuthentication(IBankPayload requestPayload);

    PylonPayload processDashboard(String username);

    PylonPayload processTransferBeneficiary(String username);

    PylonPayload processSingleAirtime(IBankPayload requestPayload);

    PylonPayload processBulkAirtime(IBankPayload requestPayload);

    PylonPayload processScheduledAirtime(IBankPayload requestPayload);

    PylonPayload processSingleData(IBankPayload requestPayload);

    PylonPayload processBulkData(IBankPayload requestPayload);

    PylonPayload processScheduledData(IBankPayload requestPayload);

    PylonPayload processSingleCableTv(IBankPayload requestPayload);

    PylonPayload processBulkCableTv(IBankPayload requestPayload);

    PylonPayload processScheduledCableTv(IBankPayload requestPayload);

    PylonPayload processSingleElectricity(IBankPayload requestPayload);

    PylonPayload processBulkElectricity(IBankPayload requestPayload);

    PylonPayload processScheduledElectricity(IBankPayload requestPayload);

    PylonPayload processSingleFundsTransfer(IBankPayload requestPayload);

    PylonPayload processSingleBulkTransfer(IBankPayload requestPayload);

    PylonPayload processScheduledFundsTransfer(IBankPayload requestPayload);
    
    PylonPayload processFetchDataPlan(String telco);
    
    PylonPayload processAirtimeDetails(IBankPayload requestPayload);
}
