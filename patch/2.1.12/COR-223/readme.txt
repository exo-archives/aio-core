Summary

    * Status: The list of users does not appear in the Community Management portlet when using Oracle Virtual Directory LDAP
    * CCP Issue: N/A, Product Jira Issue: COR-223.
    * Complexity: Low

The Proposal
Problem description

What is the problem to fix?
When we use OpenLDAP 2.4-20, the list of users appears in the portlet "Community Management". 
However, when we connect Oracle Virtual Directory to OpenLDAP 2.4-20 through Oracle Virtual Directory Manager, this list no longer appears.
When we debug the class "LDAPUserPageList" that is available in the package "org.exoplatform.services.organization.ldap", we note that "responseControls" has the value "null", which was not predicted in the code. 

Fix description

How is the problem fixed?
* Skip when responseControls[] array is null.

Patch file: COR-223.patch

Tests to perform

Reproduction test
   * No

Tests performed at DevLevel
  * functional tests in core project

Tests performed at QA/Support Level
*

Documentation changes

Documentation changes:
  * none

Configuration changes

Configuration changes:
  * none

Will previous configuration continue to work?
  * yes

Risks and impacts

Can this bug fix have any side effects on current client projects?

    * none

Is there a performance risk/cost?
  * none

Validation (PM/Support/QA)

PM Comment
* Patch approved by the PM

Support Comment
* Support review: patch validated

QA Feedbacks
*
