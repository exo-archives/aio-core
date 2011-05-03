Summary

    Status: java.lang.NullPointerException exception when copy/paste pdf file in webdav drive
    CCP Issue: N/A, Product Jira Issue: COR-222.
    Fixes also: ECM-5523
    Complexity: N/A

The Proposal
Problem description

What is the problem to fix?
Create a webdav drive with this url: http://127.0.0.1:8080/rest/private/jcr/repository/collaboration/sites%20content/live/acme/documents
Copy/paste metro.pdf file

On Windows (client OS), this error is observed on the server console

java.io.IOException: PDF header signature not found.
        at com.lowagie.text.pdf.PRTokeniser.checkPdfHeader(Unknown Source)
        at com.lowagie.text.pdf.PdfReader.readPdf(Unknown Source)
        at com.lowagie.text.pdf.PdfReader.<init>(Unknown Source)
        at org.exoplatform.services.document.impl.PDFDocumentReader.getProperties(PDFDocumentReader.java:101)
        at org.exoplatform.services.jcr.ext.metadata.AddMetadataAction.execute(AddMetadataAction.java:98)
        at org.exoplatform.services.jcr.impl.ext.action.SessionActionInterceptor.launch(SessionActionInterceptor.java:373)
        at org.exoplatform.services.jcr.impl.ext.action.SessionActionInterceptor.postSetProperty(SessionActionInterceptor.java:266)
        at org.exoplatform.services.jcr.impl.core.ItemImpl.doUpdateProperty(ItemImpl.java:482)
...

Fix description

How is the problem fixed?
Two problems are:

    properties extraction: remove iText, use jempbox-0.2.0 instead.
    text extraction: patch pdfbox-0.7.2.

Patch information:
COR-222.patch

Tests to perform

Reproduction test
    Cf. above

Tests performed at DevLevel
    TestPropertiesExtraction and TestPDFDocumentReader

Tests performed at QA/Support Level
*
Documentation changes

Documentation changes:
    none

Configuration changes

Configuration changes:
    none

Will previous configuration continue to work?
    yes

Risks and impacts

Can this bug fix have any side effects on current client projects?
    Function or ClassName change: no

Is there a performance risk/cost?
* N/A

Validation (PM/Support/QA)

PM Comment
* Patch validated by the PM

Support Comment
* Patch validated

QA Feedbacks
*

