package org.nuxeo.labs.automation.extended.audit;

import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.util.Properties;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.platform.audit.api.AuditLogger;
import org.nuxeo.ecm.platform.audit.api.ExtendedInfo;
import org.nuxeo.ecm.platform.audit.api.LogEntry;

import java.io.Serializable;
import java.util.*;

@Operation(
        id = ExtendedAuditLog.ID,
        category = Constants.CAT_SERVICES,
        label = "LABS - Extended Log Event In Audit",
        description = "Log events into audit")
public class ExtendedAuditLog {

    public static final String ID = "Audit.LogEventExtended";

    @Context
    protected AuditLogger logger;

    @Context
    protected OperationContext ctx;

    @Param(name = "user", required = false)
    protected String user;

    @Param(name = "event", widget = Constants.W_AUDIT_EVENT)
    protected String event;

    @Param(name = "category", required = false, values = {"Automation"})
    protected String category = "Automation";

    @Param(name = "eventDate", required = false)
    protected Calendar eventDate = Calendar.getInstance();

    @Param(name = "properties", required = false)
    protected Properties properties = new Properties();

    @Param(name = "comment", required = false, widget = Constants.W_MULTILINE_TEXT)
    protected String comment;


    @OperationMethod
    public void run() {
        LogEntry entry = newEntry(null);
        logger.addLogEntries(Collections.singletonList(entry));
    }

    @OperationMethod
    public DocumentModel run(DocumentModel doc) {
        LogEntry entry = newEntry(doc);
        logger.addLogEntries(Collections.singletonList(entry));
        return doc;
    }

    @OperationMethod
    public DocumentModelList run(DocumentModelList docs) {
        List<LogEntry> entries = new ArrayList<LogEntry>();
        for (DocumentModel doc : docs) {
            entries.add(newEntry(doc));
        }
        logger.addLogEntries(entries);
        return docs;
    }

    protected LogEntry newEntry(DocumentModel doc) {

        LogEntry entry = logger.newLogEntry();
        entry.setEventId(event);
        entry.setEventDate(eventDate.getTime());
        entry.setCategory(category);

        if (doc!=null) {
            entry.setDocUUID(doc.getId());
            entry.setDocPath(doc.getPathAsString());
            entry.setDocType(doc.getType());
            entry.setRepositoryId(doc.getRepositoryName());
            entry.setDocLifeCycle(doc.getCurrentLifeCycleState());
        }

        if (comment!=null) {
            entry.setComment(comment);
        }

        if (user != null) {
            entry.setPrincipalName(user);
        } else {
            entry.setPrincipalName(ctx.getPrincipal().getActingUser());
        }

        Map<String, ExtendedInfo> extendedInfoMap = new HashMap<>();

        for (Map.Entry<String,String> property : properties.entrySet()){
            extendedInfoMap.put(property.getKey(),logger.newExtendedInfo(convertValue(property.getValue())));
        }

        entry.setExtendedInfos(extendedInfoMap);

        return entry;
    }

    public Serializable convertValue(String str){

        //try integer
        try {
            long number = Long.parseLong(str);
            return number;
        } catch (NumberFormatException e) {
            //continue
        }

        //try float
        try {
            double number = Double.parseDouble(str);
            return number;
        } catch (NumberFormatException e) {
            //continue
        }

        return str;
    }



}



