package com.entitysc.euclase.controller;

import com.asprise.imaging.core.Imaging;
import com.asprise.imaging.core.Request;
import com.asprise.imaging.core.Result;
import com.asprise.imaging.core.scan.twain.Source;
import com.asprise.imaging.scan.ui.workbench.AspriseScanUI;
import com.entitysc.euclase.constant.ResponseCodes;
import com.entitysc.euclase.payload.DataListResponsePayload;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.EuclaseResponsePayload;
import com.entitysc.euclase.service.DocumentService;
import com.entitysc.euclase.service.DocumentTypeService;
import com.entitysc.euclase.service.PushNotificationService;
import com.entitysc.euclase.service.SLAService;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author briano
 */
@Controller
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    DocumentService documentService;
    @Autowired
    DocumentTypeService documentTypeService;
    @Autowired
    SLAService slaService;
    @Autowired
    PushNotificationService notificationService;
    private String alertMessage = "";
    private String alertMessageType = "";
    @Value("${euclase.document.id.allowuser}")
    private String allowUserDocumentId;
    @Value("${euclase.document.allowkeyword.date}")
    private String allowDateKeywords;
    @Value("${euclase.document.allowkeyword.amount}")
    private String allowAmountKeywords;
    @Autowired
    MessageSource messageSource;

    @GetMapping("/")
    @Secured("ROLE_CREATE_DOCUMENT")
    public String document(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("documentTypes", documentTypeService.fetchDocumentTypeList("Company", httpSession.getAttribute("companyId").toString()).getData());
        model.addAttribute("addDate", false);
        model.addAttribute("addAmount", false);
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "document";
    }

    @GetMapping("/new")
    @Secured("ROLE_CREATE_DOCUMENT")
    public String newDocument(@RequestParam("seid") String seid, Model model, HttpSession httpSession, Principal principal) {
        EuclaseResponsePayload response = documentTypeService.fetchDocumentType(seid);
        EuclasePayload requestPayload = new EuclasePayload();
        requestPayload.setDocumentWorkflowBody(response.getData().getDocumentWorkflowBody());
        requestPayload.setDocumentTypeName(response.getData().getDocumentTypeName());
        requestPayload.setDocumentType(String.valueOf(response.getData().getId())); //Pass document type id for lookup
        //Check if the signature is set. Index 11 holds the signature
        Object signature = httpSession.getAttribute("signatureLink");
        if (signature == null || (signature instanceof String && signature.toString().equalsIgnoreCase("NA"))) {
            alertMessage = messageSource.getMessage("appMessages.signature.notset", new Object[0], Locale.ENGLISH);
            alertMessageType = "error";
            return "redirect:/document/";
        }

        //Check if the template is well formatted
        if (!response.getData().isDocumentTemplateFormatted()) {
            alertMessage = messageSource.getMessage("appMessages.template.notformatted", new Object[0], Locale.ENGLISH);
            alertMessageType = "error";
            return "redirect:/document/";
        }

        //Check if the template is well formatted
        if (!response.getData().isDocumentWorkflowFormatted()) {
            alertMessage = messageSource.getMessage("appMessages.template.notformatted", new Object[0], Locale.ENGLISH);
            alertMessageType = "error";
            return "redirect:/document/";
        }

        requestPayload.setDocumentType(seid);
        //Generate document id
        String documentId = documentService.generateDocumentId(response.getData().getDocumentGroupCode());
        requestPayload.setDocumentId(documentId);
        requestPayload.setAllowUserDocumentId(allowUserDocumentId);
        model.addAttribute("addDate", addDateField(response.getData().getDocumentTypeName()));
        model.addAttribute("addAmount", addAmountField(response.getData().getDocumentTypeName()));
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("slaList", slaService.fetchSLAList().getData());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "documentinit";
    }

    @PostMapping("/init")
    public String documentInit(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        EuclaseResponsePayload response = documentService.processCreateDocument(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            requestPayload.setEditorData(response.getData().getDocumentTemplateBody());
            requestPayload.setEditor(response.getData().getDocumentTemplateBody());
            requestPayload.setDocumentTemplateName(response.getData().getDocumentTemplateName());
            requestPayload.setDocumentTypeName(response.getData().getDocumentTypeName());
            model.addAttribute("euclasePayload", requestPayload);
            model.addAttribute("slaList", slaService.fetchSLAList().getData());
            model.addAttribute("alertMessage", response.getResponseMessage());
            model.addAttribute("alertMessageType", alertMessageType);
            resetAlertMessage();
            return "documentprocess";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("slaList", slaService.fetchSLAList().getData());
        model.addAttribute("addDate", addDateField(requestPayload.getDocumentType()));
        model.addAttribute("addAmount", addAmountField(requestPayload.getDocumentType()));
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "documentinit";
    }

    @PostMapping("/process")
    public String processDocument(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        EuclaseResponsePayload response = documentService.processDocument(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/document/";
        }

        model.addAttribute("euclasePayload", requestPayload);
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        resetAlertMessage();
        return "documentprocess";
    }

    @GetMapping("/my")
    @Secured("ROLE_CREATE_DOCUMENT")
    public String myDocument(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("dataList", documentService.fetchMyDocuments(principal.getName()).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "mydocument";
    }

    @GetMapping("/details")
    @Secured("ROLE_CREATE_DOCUMENT")
    public String documentDetails(@RequestParam("seid") String seid, Model model, Principal principal, HttpSession httpSession, HttpServletRequest httpRequest, RedirectAttributes redirAttr) {
        EuclasePayload requestPayload = new EuclasePayload();
        DataListResponsePayload response = documentService.fetchDocumentDetails(seid, principal.getName());
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            String requestUrl = httpRequest.getHeader("Referer");
            redirAttr.addFlashAttribute(alertMessage, response.getResponseMessage());
            redirAttr.addFlashAttribute(alertMessageType, "error");
            return "redirect:" + requestUrl;
        }
        requestPayload.setDocumentTemplateName(response.getPayload().getDocumentTemplateName());
        requestPayload.setDocumentType(response.getPayload().getDocumentType());
        requestPayload.setDocumentId(response.getPayload().getDocumentId());
        BeanUtils.copyProperties(response.getPayload(), requestPayload);
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("workflowList", response.getWorkflowData());
        model.addAttribute("documentList", response.getData());
        model.addAttribute("versionList", response.getVersions());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "documentdetails";
    }

    @GetMapping("/pending")
    @Secured("ROLE_APPROVE_DOCUMENT")
    public String pendingDocument(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("dataList", documentService.fetchPendingDocuments(principal.getName()).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "pendingdocument";
    }

    @GetMapping("/draft")
    @Secured("ROLE_CREATE_DOCUMENT")
    public String draftDocument(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("dataList", documentService.fetchDraftDocuments(principal.getName()).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "draftdocument";
    }

    @GetMapping("/draft/details")
    @Secured("ROLE_CREATE_DOCUMENT")
    public String draftDocumentDetails(@RequestParam("seid") String seid, Model model, Principal principal, HttpSession httpSession) {
        DataListResponsePayload dataRecord = documentService.fetchDocumentDetails(seid, principal.getName());
        if (dataRecord.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            EuclasePayload requestPayload = new EuclasePayload();
            BeanUtils.copyProperties(dataRecord.getPayload(), requestPayload);
            requestPayload.setUsername(principal.getName());
            requestPayload.setEditorData(dataRecord.getPayload().getDocumentTemplateBody());
            requestPayload.setEditor(dataRecord.getPayload().getDocumentTemplateBody());
            requestPayload.setDocumentTemplateName(dataRecord.getPayload().getDocumentTemplateName());
            requestPayload.setDocumentTypeName(dataRecord.getPayload().getDocumentTypeName());
            model.addAttribute("euclasePayload", requestPayload);
            model.addAttribute("slaList", slaService.fetchSLAList().getData());
            model.addAttribute("alertMessage", dataRecord.getResponseMessage());
            model.addAttribute("alertMessageType", alertMessageType);
            resetAlertMessage();
            return "documentprocess";
        }
        //Return to draft
        alertMessage = dataRecord.getResponseMessage();
        alertMessageType = "error";
        return "redirect:/document/draft";
    }

    @GetMapping("/draft/delete")
    @Secured("ROLE_CREATE_DOCUMENT")
    public String deleteDraftDocument(@RequestParam("seid") String seid, Model model, Principal principal, HttpSession httpSession) {
        EuclaseResponsePayload response = documentService.processDeleteDraftDocument(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
        return "redirect:/document/draft";
    }

    @GetMapping("/approve/details")
    @Secured("ROLE_APPROVE_DOCUMENT")
    public String approveDocument(@RequestParam("seid") String seid, Model model, Principal principal, HttpSession httpSession) {
        EuclasePayload requestPayload = new EuclasePayload();
        DataListResponsePayload response = documentService.fetchDocumentDetails(seid, principal.getName());
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "error";
            return "redirect:/document/pending";
        }

        //Check if the signature is set. Index 11 holds the signature
        Object signature = httpSession.getAttribute("signatureLink");
        if (signature == null || (signature instanceof String && signature.toString().equalsIgnoreCase("NA"))) {
            alertMessage = messageSource.getMessage("appMessages.signature.notset", new Object[0], Locale.ENGLISH);
            alertMessageType = "error";
            return "redirect:/document/pending";
        }

        requestPayload.setEditorData(response.getPayload().getDocumentTemplateBody());
        requestPayload.setEditor(response.getPayload().getDocumentTemplateBody());
        requestPayload.setDocumentTemplateName(response.getPayload().getDocumentTemplateName());
        requestPayload.setDocumentType(response.getPayload().getDocumentType());
        requestPayload.setDocumentId(response.getPayload().getDocumentId());
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("workflowList", response.getWorkflowData());
        model.addAttribute("documentList", response.getData());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "approvedocument";
    }

    @PostMapping("/approve")
    public String approveDocument(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        EuclaseResponsePayload response = documentService.processApproveDocument(requestPayload);
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/document/pending";
    }

    @GetMapping("/workflow")
    @Secured("ROLE_MANAGE_DOCUMENT_WORKFLOW")
    public String documentWorkflow(@RequestParam("seid") String seid, Model model, Principal principal, HttpSession httpSession) {
        EuclasePayload requestPayload = new EuclasePayload();
        DataListResponsePayload response = documentService.fetchDocumentWorkflow(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "error";
            return "redirect:/document/pending";
        }

        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("workflowList", response.getWorkflowData());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "workflowdocument";
    }

    @GetMapping("/search")
    @Secured("ROLE_SEARCH_DOCUMENT")
    public String documentSearch(@RequestParam("search") String search, Model model, Principal principal, HttpSession httpSession) {
        EuclasePayload requestPayload = new EuclasePayload();
        model.addAttribute("dataList", documentService.processSearchDocument(search, principal.getName()).getData());
        model.addAttribute("euclasePayload", requestPayload);
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "mydocument";
    }

    @PostMapping("/advanced/search")
    public String documentAdvancedSearch(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, Model model, Principal principal, HttpSession httpSession) {
        requestPayload.setUsername(principal.getName());
        model.addAttribute("dataList", documentService.processAdvancedSearchDocument(requestPayload).getData());
        model.addAttribute("euclasePayload", requestPayload);
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "mydocument";
    }

    @GetMapping("/signature")
    @Secured("ROLE_CREATE_DOCUMENT")
    public String signature(Model model, Principal principal, HttpSession httpSession) {
        EuclasePayload requestPayload = new EuclasePayload();
        model.addAttribute("euclasePayload", requestPayload);
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "signature";
    }

    @PostMapping("/signature/process")
    public String uploadSignature(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        EuclaseResponsePayload response = documentService.processDocumentSignature(requestPayload);
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/document/signature";
    }

    @GetMapping("/scan")
    @Secured("ROLE_CREATE_DOCUMENT")
    public String scan(Model model, Principal principal, HttpSession httpSession) {
        try {
            Imaging imaging = new Imaging("myApp", 0);
            List<Source> sourcesWithCaps = imaging.scanListSources(false, "all", true, true);
            Result result = new AspriseScanUI().setRequest(Request.fromJson(
                    "{"
                    + "\"output_settings\" : [ {"
                    + "  \"type\" : \"save\","
                    + "  \"format\" : \"pdf\","
                    + "  \"save_path\" : \"${TMP}\\\\${TMS}${EXT}\""
                    + "} ]"
                    + "}"))
                    .setInstruction("Scan <strong>test</strong>")
                    .showDialog(null, "Dialog Title", true, null);
            String okon = "The main man";

        } catch (Exception ex) {
            String kalis = "The man";
        }

        return "documentscan";
    }

    @GetMapping("/archive")
    @Secured("ROLE_CREATE_DOCUMENT")
    public String upload(Model model, Principal principal, HttpSession httpSession) {
        EuclasePayload requestPayload = new EuclasePayload();
        requestPayload.setUsername(principal.getName());
        String documentId = documentService.generateDocumentId((String) httpSession.getAttribute("documentArchiveGroupCode"));
        requestPayload.setDocumentId(documentId);
        requestPayload.setAllowUserDocumentId(String.valueOf(false));
        model.addAttribute("euclasePayload", requestPayload);
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "documentarchive";
    }

    @PostMapping("/archive/process")
    public String uploadDocument(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        EuclaseResponsePayload response = documentService.processDocumentArchiving(requestPayload);
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/document/archive";
    }

    private boolean addDateField(String documentType) {
        String[] keyword = allowDateKeywords.split(",");
        boolean keywordMatch = false;
        if (keyword.length > 0) {
            for (String s : keyword) {
                if (documentType.toUpperCase().contains(s.trim())) {
                    keywordMatch = true;
                }
            }
        }
        return keywordMatch;
    }

    private boolean addAmountField(String documentType) {
        String[] keyword = allowAmountKeywords.split(",");
        boolean keywordMatch = false;
        if (keyword.length > 0) {
            for (String s : keyword) {
                if (documentType.toUpperCase().contains(s.trim())) {
                    keywordMatch = true;
                }
            }
        }
        return keywordMatch;
    }

    /**
     * Document notification
     *
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/notification")
    @Secured("ROLE_DOCUMENT_NOTIFICATION")
    public String notification(Model model, HttpSession session, Principal principal) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        model.addAttribute("notificationList", notificationService.fetchNotificationList(principal.getName()).getData());
        model.addAttribute("documentCount", notificationService.fetchNotificationList(principal.getName()).getData().size());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        resetAlertMessage();
        return "documentnotification";
    }

    @PostMapping("/notification/create")
    public String notification(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        EuclaseResponsePayload response = notificationService.processCreateNotification(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/document/notification";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("documentCount", notificationService.fetchNotificationList(principal.getName()).getData().size());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "documentnotification";
    }

    @GetMapping(value = "/notification/list")
    @Secured("ROLE_DOCUMENT_NOTIFICATION")
    public String notificationList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        model.addAttribute("dataList", notificationService.fetchNotificationList(principal.getName()).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "documentnotificationlist";
    }

    @GetMapping("/notification/edit")
    @Secured("ROLE_DOCUMENT_NOTIFICATION")
    public String editNotification(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        EuclaseResponsePayload response = notificationService.fetchNotification(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/sla/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("documentCount", notificationService.fetchNotificationList(principal.getName()).getData().size());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "documentnotification";
    }

    @GetMapping("/notification/delete")
    @Secured("ROLE_DOCUMENT_NOTIFICATION")
    public String deleteNotification(@RequestParam("seid") String seid, Model model, Principal principal) {
        EuclaseResponsePayload response = notificationService.processDeleteNotification(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
        return "redirect:/document/notification/list";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
