package io.anuke.arc.backends.headless;

import io.anuke.arc.Files;
import io.anuke.arc.files.Fi;

import java.io.File;

public final class HeadlessFiles implements Files{
    public static final String externalPath = System.getProperty("user.home") + File.separator;
    public static final String localPath = new File("").getAbsolutePath() + File.separator;

    @Override
    public Fi getFileHandle(String fileName, FileType type){
        return new Fi(fileName, type);
    }

    @Override
    public Fi classpath(String path){
        return new Fi(path, FileType.Classpath);
    }

    @Override
    public Fi internal(String path){
        return new Fi(path, FileType.Internal);
    }

    @Override
    public Fi external(String path){
        return new Fi(path, FileType.External);
    }

    @Override
    public Fi absolute(String path){
        return new Fi(path, FileType.Absolute);
    }

    @Override
    public Fi local(String path){
        return new Fi(path, FileType.Local);
    }

    @Override
    public String getExternalStoragePath(){
        return externalPath;
    }

    @Override
    public boolean isExternalStorageAvailable(){
        return true;
    }

    @Override
    public String getLocalStoragePath(){
        return localPath;
    }

    @Override
    public boolean isLocalStorageAvailable(){
        return true;
    }
}
