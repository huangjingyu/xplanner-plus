package com.technoetic.xplanner.file;

// DEBT Apply the same naming convention for service locator like SystemAuthorizer
public class DefaultFileSystem {
    private static FileSystem fileSystem;

    public static FileSystem get() {
        return fileSystem;
    }

    public static void set(FileSystem fileSystem) {
        DefaultFileSystem.fileSystem = fileSystem;
    }
}
