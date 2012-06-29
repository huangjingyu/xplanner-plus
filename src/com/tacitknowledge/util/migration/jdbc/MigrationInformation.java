/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Feb 27, 2006
 * Time: 8:11:56 PM
 */
package com.tacitknowledge.util.migration.jdbc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Launches the migration process as a standalone application.
 * <p>
 * This class expects the following Java environment parameters:
 * <ul>
 *    <li>migration.systemname - the name of the logical system being migrated</li>
 * </ul>
 * <p>
 * Below is an example of how this class can be configured in an Ant build.xml file:
 * <pre>
 *   ...
 *  &lt;target name="patch.information" description="Prints out information about patch levels"&gt;
 *   &lt;java
 *       fork="true"
 *       classpathref="patch.classpath"
 *       failonerror="true"
 *       classname="com.tacitknowledge.util.migration.jdbc.MigrationInformation"&gt;
 *     &lt;sysproperty key="migration.systemname" value="${application.name}"/&gt;
 *   &lt;/java&gt;
 * &lt;/target&gt;
 *   ...
 * </pre>
 *
 * @author  Mike Hardy (mike@tacitknowledge.com)
 * @version $Id: MigrationInformation.java,v 1.10 2005/09/07 22:20:34 chrisa Exp $
 * @see     com.tacitknowledge.util.migration.MigrationProcess
 */
public class MigrationInformation
{
    /** Class logger */
    private static Log log = LogFactory.getLog(MigrationInformation.class);

    /**
     * Private constructor - this object shouldn't be instantiated
     */
    private MigrationInformation()
    {
        // does nothing
    }

    /**
     * Get the migration level information for the given system name
     *
     * @param arguments the command line arguments, if any (none are used)
     * @exception Exception if anything goes wrong
     */
    public static void main(String[] arguments) throws Exception
    {
        MigrationInformation info = new MigrationInformation();
        String migrationName = System.getProperty("migration.systemname");
        if (migrationName == null)
        {
            if ((arguments != null) && (arguments.length > 0))
            {
                migrationName = arguments[0].trim();
            }
        }
        info.getMigrationInformation(migrationName);
    }

    /**
     * Get the migration level information for the given system name
     *
     * @param systemName the name of the system
     * @throws Exception if anything goes wrong
     */
    public void getMigrationInformation(String systemName) throws Exception
    {
        if (systemName == null)
        {
            throw new IllegalArgumentException("The migration.systemname "
                                               + "system property is required");
        }

        // The MigrationLauncher is responsible for handling the interaction
        // between the PatchTable and the underlying MigrationTasks; as each
        // task is executed, the patch level is incremented, etc.
        try
        {
            JdbcMigrationLauncherFactory launcherFactory = MigrationLauncherFactoryLoader.createFactory();
            JdbcMigrationLauncher launcher
                = launcherFactory.createMigrationLauncher(systemName);
            log.info("Current Database patch level is        : "
                     + launcher.getDatabasePatchLevel());
            int unappliedPatches = launcher.getNextPatchLevel()
                                   - launcher.getDatabasePatchLevel() - 1;
            log.info("Current number of unapplied patches is : " + unappliedPatches);
            log.info("The next patch to author should be     : " + launcher.getNextPatchLevel());
        }
        catch (Exception e)
        {
            log.error(e);
            throw e;
        }
    }
}