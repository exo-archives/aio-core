This EJB work with ExoEJBLoginModule (in organization.api.auth).

For use it do next:

1. Build and deploy exo.core.organization.loginbean-2.0.3.jar.

NOTE: You need update core.organization.api.

2. When you have deployed both of EJB (secirity and business) :
  
  as example try ws/rest/ejb/example/client21
  
  set %JONAS_ROOT%=_jonas_dir_
  and run file : run.bat
  user/password = exo/exo