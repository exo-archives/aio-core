Summary

    * Status: In LDAPService, InitialContext is not safely closed in authenticate method
    * CCP Issue: N/A, Product Jira Issue: COR-209.
    * Complexity: Low

The Proposal
Problem description

What is the problem to fix?

    * In LDAPService, authenticate method is like this :

      public boolean authenticate(String userDN, String password) throws Exception {
      Hashtable<String, String> props = new Hashtable<String, String>(env);
      props.put(Context.SECURITY_AUTHENTICATION, "simple"); props.put(Context.SECURITY_PRINCIPAL, userDN);
      props.put(Context.SECURITY_CREDENTIALS, password); props.put("com.sun.jndi.ldap.connect.pool", "false");
      new InitialLdapContext(props, null);
      return true;
      }

      InitialLdapContext is not safely closed.

    * It should be something like this :

      public boolean authenticate(String userDN, String password) throws Exception {
      Hashtable<String, String> props = new Hashtable<String, String>(env);
      props.put(Context.SECURITY_AUTHENTICATION, "simple");
      props.put(Context.SECURITY_PRINCIPAL, userDN);
      props.put(Context.SECURITY_CREDENTIALS, password);
      props.put("com.sun.jndi.ldap.connect.pool", "false");
      InitialContext ctx = null;
      try { ctx = new InitialLdapContext(props, null); return true; } finally {
      if (ctx != null) {
      try { ctx.close(); } catch (NamingException ne) { // LOG about exception }
      }
      }
      }

      You can see discussion on forum : http://forums.exoplatform.org/portal/public/classic/forum/topic/topic1d6c4a097f000001487a827568862f3b/post32de136d7f0000012ecb1fbcd83d23b0

Fix description

How is the problem fixed?

    * LDAP context will be closed in finally block.

Patch information:
Patch files:
COR-209.patch

Tests to perform

Reproduction test

    * No

Tests performed at DevLevel

    * Functional tests in COR project

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

    * Approved by the PM

Support Comment
*  Support review : validated

QA Feedbacks
*

