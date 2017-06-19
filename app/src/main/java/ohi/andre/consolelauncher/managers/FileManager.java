package ohi.andre.consolelauncher.managers;

import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import ohi.andre.comparestring.Compare;
import ohi.andre.consolelauncher.tuils.ShellUtils;
import ohi.andre.consolelauncher.tuils.Tuils;

public class FileManager {

    public static final int FILE_NOTFOUND = 10;
    public static final int ISDIRECTORY = 11;
    public static final int IOERROR = 12;
    public static final int ISFILE = 13;
    public static final int NOT_WRITEABLE = 14;
    public static final int NOT_READABLE = 15;

    public static final int MIN_FILE_RATE = 4;

    public static final boolean USE_SCROLL_COMPARE = true;

    private static final String ASTERISK = "*";
    private static final String DOT = Tuils.DOT;

    public static String writeOn(File file, String text) {
//        try {
//            ShellUtils.CommandResult result = ShellUtils.execCommand("echo " + "\"" + text + "\"" + " > " + file.getAbsolutePath(), false, null);
//            if(result.result == 0) {
//                return null;
//            } else {
//                result = ShellUtils.execCommand("echo " + "\"" + text + "\"" + " > " + file.getAbsolutePath(), true, null);
//                if(result.result == 0) {
//                    return null;
//                }
//                return result.toString();
//            }
//        } catch (Exception e) {
//            return e.toString();
//        }

        try {
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(text.getBytes());
            stream.flush();
            stream.close();
            return null;
        } catch (FileNotFoundException e) {
            return e.toString();
        } catch (IOException e) {
            return e.toString();
        }
    }

    public static int mv(File[] files, File where, boolean su) throws IOException {
        if (files == null || files.length == 0 || where == null) {
            return FileManager.FILE_NOTFOUND;
        }

        if (!where.isDirectory()) {
            return FileManager.ISFILE;
        }

        ShellUtils.CommandResult r = ShellUtils.execCommand("test -w \"" + where.getAbsolutePath()+ "\"", su, null);
        if(r.result != 0) {
            return NOT_WRITEABLE;
        }

        for (File f : files) {
            mv(f, where, su);
        }

        return 0;
    }

    private static int mv(File f, File where, boolean su) throws IOException {
        ShellUtils.CommandResult rw = ShellUtils.execCommand("test -w \"" + f.getAbsolutePath()+ "\"", su, null);
        if(rw.result != 0) {
            return NOT_WRITEABLE;
        }
        ShellUtils.CommandResult rr = ShellUtils.execCommand("test -r \"" + f.getAbsolutePath()+ "\"", su, null);
        if(rr.result != 0) {
            return NOT_READABLE;
        }

        ShellUtils.CommandResult result = ShellUtils.execCommand("mv " +
                (f.isDirectory() ? "-r" : Tuils.EMPTYSTRING) +
                Tuils.SPACE +
                "\"" + f.getAbsolutePath() + "\"" + Tuils.SPACE +
                "\"" + where.getAbsolutePath() + "\"", su, null);
        return result.result;
    }

    public static int rm(File[] files, boolean su) {
        if (files == null || files.length == 0) {
            return FileManager.FILE_NOTFOUND;
        }

        for (File f : files) {
            rm(f, su);
        }

        return 0;
    }

    public static int rm(File f, boolean su) {
        ShellUtils.CommandResult r = ShellUtils.execCommand("test -w \"" + f.getAbsolutePath() + "\"", su, null);
        if(r.result != 0) {
            return NOT_WRITEABLE;
        }

        ShellUtils.CommandResult result = ShellUtils.execCommand("rm " +
                (f.isDirectory() ? "-r" : Tuils.EMPTYSTRING) +
                Tuils.SPACE +
                "\"" + f.getAbsolutePath() + "\"", su, null);
        return result.result;
    }

    public static int cp(File[] files, File where, boolean su) throws IOException {
        if (files == null || files.length == 0 || where == null) {
            return FileManager.FILE_NOTFOUND;
        }

        if (!where.isDirectory()) {
            return FileManager.ISFILE;
        }

        ShellUtils.CommandResult r = ShellUtils.execCommand("test -w \"" + where.getAbsolutePath()+ "\"", su, null);
        if(r.result != 0) {
            return NOT_WRITEABLE;
        }

        for (File f : files) {
            cp(f, where, su);
        }

        return 0;
    }

    private static int cp(File f, File where, boolean su) throws IOException {
        ShellUtils.CommandResult rr = ShellUtils.execCommand("test -r \"" + f.getAbsolutePath()+ "\"", su, null);
        if(rr.result != 0) {
            return NOT_READABLE;
        }

        ShellUtils.CommandResult result = ShellUtils.execCommand("cp " +
                (f.isDirectory() ? "-r" : Tuils.EMPTYSTRING) +
                Tuils.SPACE +
                "\"" + f.getAbsolutePath() + "\"" + Tuils.SPACE +
                "\"" + where.getAbsolutePath() + "\"", su, null);
        return result.result;
    }

    public static List<File> lsFile(File f, boolean showHidden) {
//        ShellUtils.CommandResult r = ShellUtils.execCommand("test -w \"" + f.getAbsolutePath()+ "\"", false, null);
//        if(r.result != 0) {
//            return null;
//        }

        if(!f.isDirectory()) {
            return null;
        }

//        ShellUtils.CommandResult rr = ShellUtils.execCommand("test -r \"" + f.getAbsolutePath()+ "\"", false, null);
//        if(rr.result != 0) {
//            return null;
//        }

        File[] content = f.listFiles();

        Arrays.sort(content, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                if (lhs.isDirectory() && !rhs.isDirectory())
                    return -1;
                if (rhs.isDirectory() && !lhs.isDirectory())
                    return 1;

                return Compare.alphabeticCompare(lhs.getName(), rhs.getName());
            }
        });

        List<File> files = new ArrayList<>();
        for (File u : content) {
            if (!u.isHidden() || showHidden) {
                files.add(u);
            }
        }

        return files;
    }

    public static int openFile(Context c, File file) {
        if (file == null) {
            return FileManager.FILE_NOTFOUND;
        }
        if (file.isDirectory()) {
            return FileManager.ISDIRECTORY;
        }

        Intent intent = Tuils.openFile(file);

        c.startActivity(intent);
        return 0;
    }

    public static DirInfo cd(File currentDirectory, String path) {
        File file;
        String notFound = "";

//        path == "/" (root folder)
        if (path.equals(File.separator))
            return new DirInfo(new File(path), null);

//        remove the useless "/" from the end of path
        if (path.endsWith(File.separator))
            path = path.substring(0, path.length() - 1);

//        absolute path
        if (path.startsWith(File.separator))
            file = new File(path);
//        relative path
        else {
//            create a file from the path
            file = new File(currentDirectory, path);
//            assign path
            path = file.getAbsolutePath();
        }

//        cycle on path until file exists
        String toAdd;
        while (!file.exists()) {
//            find the last slash
            int slash = path.lastIndexOf(File.separator);
            if (slash == -1)
                slash = 0;

            toAdd = path.substring(slash, path.length());
//            add a "/" to be sure
            if (!toAdd.startsWith(File.separator))
                toAdd = toAdd.concat(File.separator);
//            toadd is concatenated at the end of not found
            notFound = toAdd.concat(notFound);

//            adjust path
            file = file.getParentFile();
            path = file.getAbsolutePath();
        }

//        ok, now file exists, path = file absolute path

//        check if path contains ".."
        String cut, pathSection;
        for (int count = path.length(); path.contains(".."); ) {

            pathSection = path.substring(0, count);
            count = pathSection.lastIndexOf(File.separator);

//            get the part between "/" and end of path or "/"
            cut = pathSection.substring(count + 1, pathSection.length());
//            if cut is ..
            if (cut.equals("..")) {
//                find the slash before count
                int preSlash = path.substring(0, count).lastIndexOf(File.separator);
//                find the part after ".."
                String rightPart = path.substring(count + cut.length() + 1);

                path = path.substring(0, preSlash + 1).concat(rightPart);
            }
        }
        file = new File(path);

        if (notFound.length() <= 0)
            notFound = null;
        else if (notFound.length() > 1) {
            if (notFound.startsWith(File.separator))
                notFound = notFound.substring(1);
            if (notFound.endsWith(File.separator))
                notFound = notFound.substring(0, notFound.length() - 1);
        }

        return new DirInfo(file, notFound);
    }

    public static WildcardInfo wildcard(String path) {
        if (path == null || !path.contains(ASTERISK) || path.contains(File.separator)) {
            return null;
        }

        if(path.trim().equals(ASTERISK)) {
            return new WildcardInfo(true);
        }

        int dot = path.lastIndexOf(DOT);
        try {
            String beforeDot = path.substring(0, dot);
            String afterDot = path.substring(dot + 1);
            return new WildcardInfo(beforeDot, afterDot);
        } catch (Exception e) {
            return null;
        }
    }

    public static class DirInfo {
        public File file;
        public String notFound;

        public DirInfo(File f, String nF) {
            this.file = f;
            this.notFound = nF;
        }

        public String getCompletePath() {
            return file.getAbsolutePath() + "/" + notFound;
        }
    }

    public static class WildcardInfo {

        public boolean allNames;
        public boolean allExtensions;
        public String name;
        public String extension;

        public WildcardInfo(String name, String extension) {
            this.name = name;
            this.extension = extension;

            allNames = name.length() == 0 || name.equals(ASTERISK);
            allExtensions = extension.length() == 0 || extension.equals(ASTERISK);
        }

        public WildcardInfo(boolean all) {
            if(all) {
                this.allExtensions = all;
                this.allNames = all;
            }
        }
    }

    public static class SpecificNameFileFilter implements FilenameFilter {

        private String name;

        public void setName(String name) {
            this.name = name.toLowerCase();
        }

        @Override
        public boolean accept(File dir, String filename) {
            int dot = filename.lastIndexOf(Tuils.DOT);
            if(dot == -1) {
                return false;
            }

            filename = filename.substring(0, dot);
            return filename.toLowerCase().equals(name);
        }
    }

    public static class SpecificExtensionFileFilter implements FilenameFilter {

        private String extension;

        public void setExtension(String extension) {
            this.extension = extension.toLowerCase();
        }

        @Override
        public boolean accept(File dir, String filename) {
            int dot = filename.lastIndexOf(Tuils.DOT);
            if(dot == -1) {
                return false;
            }

            String fileExtension = filename.substring(dot + 1);
            return fileExtension.toLowerCase().equals(extension);
        }
    }
}
