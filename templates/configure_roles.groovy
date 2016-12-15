#!groovy
import hudson.security.*
import jenkins.model.*
import com.michelin.cio.hudson.plugins.rolestrategy.*
import java.util.*
import java.lang.reflect.*



class BuildPermission {
	static buildNewAccessList(permissions) {
	  def newPermissionsMap = new HashSet<Permission>()
	  permissions.each {
		newPermissionsMap.put(Permission.fromId(it))
	  }
	  return newPermissionsMap
	}
  }


///////////////////////////////////////////////////
Set<Permission> builderPerms = new HashSet<Permission>();
//builderPerms.add(Permission.fromId("com.sonyericsson.hudson.plugins.gerrit.trigger.PluginImpl.Retrigger"));
//builderPerms.add(Permission.fromId("com.sonyericsson.hudson.plugins.gerrit.trigger.PluginImpl.ManualTrigger"));
builderPerms.add(Permission.fromId("hudson.model.Item.Build"));
builderPerms.add(Permission.fromId("hudson.model.Item.Cancel"));
builderPerms.add(Permission.fromId("hudson.model.Item.ExtendedRead"));
builderPerms.add(Permission.fromId("hudson.model.Item.Read"));
// Create configurator role
Set<Permission> configuratorPerms = new HashSet<Permission>();
//configuratorPerms.add(Permission.fromId("com.sonyericsson.hudson.plugins.gerrit.trigger.PluginImpl.Retrigger"));
//configuratorPerms.add(Permission.fromId("com.sonyericsson.hudson.plugins.gerrit.trigger.PluginImpl.ManualTrigger"));
configuratorPerms.add(Permission.fromId("hudson.model.Item.Build"));
configuratorPerms.add(Permission.fromId("hudson.model.Item.Cancel"));
configuratorPerms.add(Permission.fromId("hudson.model.Item.Configure"));
configuratorPerms.add(Permission.fromId("hudson.model.Item.Discover"));
configuratorPerms.add(Permission.fromId("hudson.model.Item.ExtendedRead"));
configuratorPerms.add(Permission.fromId("hudson.model.Item.Read"));
// Create read-only role
Set<Permission> readOnlyPerms = new HashSet<Permission>();
readOnlyPerms.add(Permission.fromId("hudson.model.Item.ExtendedRead"));
readOnlyPerms.add(Permission.fromId("hudson.model.Item.Read"));
// Create self-serve role
Set<Permission> selfServePerms = new HashSet<Permission>();
selfServePerms.add(Permission.fromId("hudson.model.Item.Build"));
selfServePerms.add(Permission.fromId("hudson.model.Item.Cancel"));
selfServePerms.add(Permission.fromId("hudson.model.Item.Discover"));
selfServePerms.add(Permission.fromId("hudson.model.Item.Read"));

Set<Permission> adminPerms = new HashSet<Permission>();
adminPerms.add(Permission.fromId("hudson.model.Item.Build"));


adminPerms = [
	"hudson.model.Hudson.Read",
	"hudson.model.Item.Build",
	"hudson.model.Item.Configure",
	"hudson.model.Item.Create",
	"hudson.model.Item.Delete",
	"hudson.model.Item.Discover",
	"hudson.model.Item.Read",
	"hudson.model.Item.Workspace",
	"hudson.model.Run.Delete",
	"hudson.model.Run.Update",
	"hudson.model.View.Configure",
	"hudson.model.View.Create",
	"hudson.model.View.Delete",
	"hudson.model.View.Read",
	"hudson.model.Item.Cancel"
  ]

/////////////////////////////////////////////
def instance = Jenkins.getInstance()


def authStrategy = instance.getAuthorizationStrategy()
RoleBasedAuthorizationStrategy roleAuthStrategy;

if(authStrategy instanceof RoleBasedAuthorizationStrategy){
  roleAuthStrategy = (RoleBasedAuthorizationStrategy) authStrategy
} else {
	roleAuthStrategy = new RoleBasedAuthorizationStrategy()
}

    // Make constructors available
  Constructor[] constrs = Role.class.getConstructors();
  for (Constructor<?> c : constrs) {
	c.setAccessible(true);
  }
  // Make the method assignRole accessible
  Method assignRoleMethod = RoleBasedAuthorizationStrategy.class.getDeclaredMethod("assignRole", String.class, Role.class, String.class);
  assignRoleMethod.setAccessible(true);
  
////////////////////////////////////////   
  def journeys = "{{ jenkins_role_auth.journeys | join(',') | trim }}"
 
  for (String journey: journeys.split(',')){
  
  String allResources = journey + "-.+"
  String codebase = journey + "-(?!selfserve).+"
  
  String buildUsers = journey + "-builder"
  String adminUsers = journey + "-selfserve"
  String configUsers = journey + "-config"
  String readonlyUsers = journey + "-ro"
  
  roleAuthStrategy.addRole(RoleBasedAuthorizationStrategy.PROJECT, new Role(buildUsers, codebase, builderPerms));
  roleAuthStrategy.addRole(RoleBasedAuthorizationStrategy.PROJECT, new Role(adminUsers, allResources, selfServePerms));
  roleAuthStrategy.addRole(RoleBasedAuthorizationStrategy.PROJECT, new Role(configUsers, codebase, configuratorPerms));
  roleAuthStrategy.addRole(RoleBasedAuthorizationStrategy.PROJECT, new Role(readonlyUsers, codebase, readOnlyPerms));

  }


instance.setAuthorizationStrategy(roleAuthStrategy)
instance.save()
  