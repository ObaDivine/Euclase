package com.entitysc.onex.service;

import com.entitysc.onex.payload.DataListResponsePayload;
import com.entitysc.onex.payload.OneXPayload;
import com.entitysc.onex.payload.PylonPayload;
import com.entitysc.onex.payload.PylonResponsePayload;

/**
 *
 * @author briano
 */
public interface ClientService {

    PylonResponsePayload processSignin(OneXPayload requestPayload);

    PylonResponsePayload changePassword(OneXPayload requestPayload);

    PylonResponsePayload forgotPassword(OneXPayload requestPayload);

    PylonResponsePayload processSignUp(OneXPayload requestPayload);

    PylonResponsePayload processSignUpActivation(OneXPayload requestPayload);

    PylonResponsePayload processQRCodeGenerate(OneXPayload requestPayload);

    PylonResponsePayload validateTwoFactorAuthentication(OneXPayload requestPayload);

    PylonResponsePayload processDashboard(String username);

    PylonResponsePayload changePin(OneXPayload requestPayload);

    PylonResponsePayload changeSecurityQuestion(OneXPayload requestPayload);

    DataListResponsePayload processTransactionHistory(OneXPayload requestPayload);

    PylonResponsePayload processImageUpload(OneXPayload requestPayload);

    /*
        Airtime Transactions
     */
    PylonResponsePayload processSingleAirtime(OneXPayload requestPayload);

    PylonResponsePayload processBulkAirtime(OneXPayload requestPayload);

    PylonResponsePayload processScheduledAirtime(OneXPayload requestPayload);

    DataListResponsePayload processAirtimeTransaction(OneXPayload requestPayload);

    DataListResponsePayload processFetchScheduledAirtime(String username);

    PylonResponsePayload processFetchScheduledAirtimeUsingId(String id);

    PylonResponsePayload processDeleteScheduledAirtime(OneXPayload requestPayload);

    PylonResponsePayload processUpdateScheduledAirtime(OneXPayload requestPayload);

    PylonResponsePayload processUpdateScheduledAirtimeStatus(OneXPayload requestPayload);

    /*
        Data Transactions
     */
    PylonResponsePayload processSingleData(OneXPayload requestPayload);

    PylonResponsePayload processBulkData(OneXPayload requestPayload);

    DataListResponsePayload processFetchScheduledData(String username);

    DataListResponsePayload processDataTransaction(OneXPayload requestPayload);

    DataListResponsePayload processFetchDataPlan(String telco);

    PylonResponsePayload processScheduledData(OneXPayload requestPayload);

    PylonResponsePayload processFetchScheduledDataUsingId(String id);

    PylonResponsePayload processDeleteScheduledData(OneXPayload requestPayload);

    PylonResponsePayload processUpdateScheduledData(OneXPayload requestPayload);

    PylonResponsePayload processUpdateScheduledDataStatus(OneXPayload requestPayload);

    /*
        Cable TV Transactions
     */
    PylonResponsePayload processSingleCableTv(OneXPayload requestPayload);

    PylonResponsePayload processBulkCableTv(OneXPayload requestPayload);

    PylonResponsePayload processScheduledCableTv(OneXPayload requestPayload);

    PylonResponsePayload processFetchScheduledCableTvUsingId(String id);

    PylonResponsePayload processDeleteScheduledCableTv(OneXPayload requestPayload);

    PylonResponsePayload processUpdateScheduledCableTv(OneXPayload requestPayload);

    PylonResponsePayload processUpdateScheduledCableTvStatus(OneXPayload requestPayload);

    DataListResponsePayload processFetchScheduledCableTv(String username);

    DataListResponsePayload processCableTvTransaction(OneXPayload requestPayload);

    DataListResponsePayload processFetchCableTVSubscription(String biller);

    PylonResponsePayload processSmartcardLookup(String biller, String smartcard);

    /*
        Electricity Transactions
     */
    PylonResponsePayload processSingleElectricity(OneXPayload requestPayload);

    PylonResponsePayload processBulkElectricity(OneXPayload requestPayload);

    PylonResponsePayload processScheduledElectricity(OneXPayload requestPayload);

    PylonResponsePayload processFetchScheduledElectricityUsingId(String id);

    PylonResponsePayload processDeleteScheduledElectricity(OneXPayload requestPayload);

    PylonResponsePayload processUpdateScheduledElectricity(OneXPayload requestPayload);

    PylonResponsePayload processUpdateScheduledElectricityStatus(OneXPayload requestPayload);

    DataListResponsePayload processFetchScheduledElectricity(String username);

    DataListResponsePayload processElectricityTransaction(OneXPayload requestPayload);

    PylonResponsePayload processMeterLookup(String disco, String billType, String meterNumber);

    /*
        Funds Transfer Transactions
     */
    PylonResponsePayload processAddTransferBeneficiary(OneXPayload requestPayload);

    PylonResponsePayload processDeleteTransferBeneficiary(OneXPayload requestPayload);

    DataListResponsePayload processUserTransferBeneficiary(String username);

    PylonResponsePayload processSingleFundsTransfer(OneXPayload requestPayload);

    PylonResponsePayload processBulkFundsTransfer(OneXPayload requestPayload);

    PylonResponsePayload processScheduledFundsTransfer(OneXPayload requestPayload);

    PylonResponsePayload processFetchScheduledFundsTransferUsingId(String id);

    PylonResponsePayload processDeleteScheduledFundsTransfer(OneXPayload requestPayload);

    PylonResponsePayload processUpdateScheduledFundsTransfer(OneXPayload requestPayload);

    PylonResponsePayload processUpdateScheduledFundsTransferStatus(OneXPayload requestPayload);

    DataListResponsePayload processFetchScheduledFundsTransfer(String username);

    DataListResponsePayload processFundsTransferTransaction(OneXPayload requestPayload);

    PylonResponsePayload processSendOTP(String recipient, String message);

    PylonResponsePayload processFundWalletUsingCard(OneXPayload requestPayload);

    PylonResponsePayload processFetchProfileDetails(String username);

    void processCallback(PylonPayload requestPayload);

    void processFundWalletPaymentConfirm(String transRef);

    DataListResponsePayload processFetchBankList();

    PylonResponsePayload processValidateTwoFactor(OneXPayload requestPayload);

    PylonResponsePayload processEnableTwoFactor(String username);

    /*
        Capital Market
     */
    PylonResponsePayload processCapitalMarketAccount(OneXPayload requestPayload);

    PylonResponsePayload processCapitalMarketCapitalization(OneXPayload requestPayload);

    PylonResponsePayload processCapitalMarketGainerLoser(OneXPayload requestPayload);

    PylonResponsePayload processCapitalMarketCompanies(OneXPayload requestPayload);

    String processContactUs(OneXPayload requestPayload);
}
