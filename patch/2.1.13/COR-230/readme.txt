Summary

    * Status: Very high response time when loading a page in the "Community Management" portlet with LDAP.
    * CCP Issue: CCP-866, Product Jira Issues: COR-230.
    * Complexity: Low

The Proposal
Problem description

The "populateCurrentPage (int page)" method of the "org.exoplatform.services.organization.ldap.LDAPUserPageList" class is supposed to charge only the current page. This is used by the "Community Management" portlet to display the list of users for each page number.

However, we note that all users are checked, from the first user to the last one, at each request for a certain page.

For example if one works with 100 users, if he clicks on the page 5, he is supposed to have users from number 41 to 50, the "populateCurrentPage" method runs through the whole list of users beginning with the user number 1, and even when users of the current page are found (41 to 50), this method continues to check the remaining users (51 to 100).

This makes the response time in actual conditions of the customer (23000 users) very high.

Fix description

How is the problem fixed?

    * When users of the current page are found, populateCurrentPage method doesn't continue to check the remaining users.

Patch file: COR-230.patch

Tests to perform

Reproduction test

    * No

Tests performed at DevLevel

    * Manual testing with Tomcat on LDAP org service

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
* Patch approved by the PM

Support Comment
* Patch validated

QA Feedbacks
*
