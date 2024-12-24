package org.nuxeo.labs.automation.extended.audit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationChain;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.core.util.Properties;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.DefaultRepositoryInit;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.audit.test.AuditFeature;

import jakarta.inject.Inject;
import java.util.Calendar;

@RunWith(FeaturesRunner.class)
@Features({ AuditFeature.class, AutomationFeature.class })
@RepositoryConfig(init = DefaultRepositoryInit.class, cleanup = Granularity.METHOD)
@Deploy({
    "nuxeo-audit-extended-automation-core"
})
public class TestOperation {

    @Inject
    protected CoreSession coreSession;

    @Inject
    protected AutomationService automationService;


    @Test
    public void testAuditOperation() throws OperationException {

        DocumentModel input = coreSession.createDocumentModel("/", "test-audit", "File");
        input = coreSession.createDocument(input);

        Properties properties = new Properties();
        properties.put("workflowModel","Test");
        properties.put("duration","3421");

        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(100);

        OperationContext ctx = new OperationContext(coreSession);
        ctx.setInput(input);
        OperationChain chain = new OperationChain("testChain");
        chain.add(ExtendedAuditLog.ID).
                set("event","test").
                set("properties",properties).
                set("eventDate", date);

        input = (DocumentModel) automationService.run(ctx, chain);
    }


}
