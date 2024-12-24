package org.nuxeo.labs.automation.extended.audit;

import org.nuxeo.audit.api.LogEntry;
import org.nuxeo.audit.api.LogEntryBuilder;
import org.nuxeo.audit.service.AuditBackend;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.util.Properties;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;

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
    protected AuditBackend logger;

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
        List<LogEntry> entries = new ArrayList<>();
        for (DocumentModel doc : docs) {
            entries.add(newEntry(doc));
        }
        logger.addLogEntries(entries);
        return docs;
    }

    protected LogEntry newEntry(DocumentModel doc) {

        LogEntryBuilder builder = LogEntry.builder(event, eventDate.getTime())
                .category(category);

        if (doc!=null) {
            builder.docUUID(doc.getId());
            builder.docPath(doc.getPathAsString());
            builder.docType(doc.getType());
            builder.repositoryId(doc.getRepositoryName());
            builder.docLifeCycle(doc.getCurrentLifeCycleState());
        }

        if (comment!=null) {
            builder.comment(comment);
        }

        if (user != null) {
            builder.principalName(user);
        } else {
            builder.principalName(ctx.getPrincipal().getActingUser());
        }

        for (Map.Entry<String,String> property : properties.entrySet()){
            builder.extended(property.getKey(),convertValue(property.getValue()));
        }

        return builder.build();
    }

    public Serializable convertValue(String str){
        //try integer
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            //continue
        }

        //try float
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            //continue
        }

        return str;
    }



}



