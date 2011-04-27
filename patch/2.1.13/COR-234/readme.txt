Summary

    * Status: Only User names are returned with UIUserSelector Using LDAP
    * CCP Issue: CCP-911, Product Jira Issue: COR-234.
    * Complexity: Low

The Proposal
Problem description

What is the problem to fix?
org.exoplatform.services.organization.ldap.LDAPUserPageList#getAll() returns only the list of usernames
Fix description

How is the problem fixed?

    * Add to returned attributes others fields such as Display Name, Last Name, First Name, email, password

Patch file: COR-234.patch

Tests to perform

Reproduction test
* Steps to reproduce using Allinone 1.6.8 :

   1. Go to Community Management Portlet
   2. Open group Management
   3. Click on select user
   4. The list will show only usernames

Tests performed at DevLevel

    * Manual testing: tomcat + LDAP Organization services + AD

Tests performed at QA/Support Level
*
Documentation changes

Documentation changes:

    * No

Configuration changes

Configuration changes:

    * No

Will previous configuration continue to work?

    * Yes

Risks and impacts

Can this bug fix have any side effects on current client projects?

    * No

Is there a performance risk/cost?

    * No

Validation (PM/Support/QA)

PM Comment
* Validated

Support Comment
* Validated

QA Feedbacks
*

