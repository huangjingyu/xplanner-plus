/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 13, 2006
 * Time: 9:28:03 PM
 */
package com.tacitknowledge.util.migration.jdbc;

import com.technoetic.xplanner.db.hsqldb.HsqlServer;

/**
 * Launches the migration process as a standalone application.
 * <p>
 * This class expects the following Java environment parameters:
 * <ul>
 *    <li>migration.systemname - the name of the logical system being migrated</li>
 * </ul>
 * <p>
 * Below is an example of how this class can be configured in build.xml:
 * <pre>
 *   ...
 *  &lt;target name="patch.database" description="Runs the migration system"&gt;
 *   &lt;java
 *       fork="true"
 *       classpathref="patch.classpath"
 *       failonerror="true"
 *       classname="com.tacitknowledge.util.migration.jdbc.StandaloneMigrationLauncher"&gt;
 *     &lt;sysproperty key="migration.systemname" value="${application.name}"/&gt;
 *   &lt;/java&gt;
 * &lt;/target&gt;
 *   ...
 * </pre>
 *
 * @author  Mike Hardy (mike@tacitknowledge.com)
 * @version $Id: StandaloneMigrationLauncher.java,v 1.7 2005/09/07 22:20:34 chrisa Exp $
 * @see     com.tacitknowledge.util.migration.MigrationProcess
 */
public class HsqlAwareStandaloneMigrationLauncher
{
    /**
     * Run the migrations for the given system name
     *
     * @param arguments the command line arguments, if any (none are used)
     * @exception Exception if anything goes wrong
     */
    public static void main(String[] arguments) throws Exception {
      HsqlServer.start();
      StandaloneMigrationLauncher.main(arguments);
      HsqlServer.shutdown();
    }

}