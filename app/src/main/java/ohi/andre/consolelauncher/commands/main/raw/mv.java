package ohi.andre.consolelauncher.commands.main.raw;

import java.io.File;
import java.util.ArrayList;

import ohi.andre.consolelauncher.R;
import ohi.andre.consolelauncher.commands.CommandAbstraction;
import ohi.andre.consolelauncher.commands.ExecutePack;
import ohi.andre.consolelauncher.commands.main.MainPack;
import ohi.andre.consolelauncher.managers.FileManager;

/**
 * Created by andre on 03/12/15.
 */
public class mv implements CommandAbstraction {

    @Override
    public String exec(ExecutePack pack) throws Exception {
        MainPack info = (MainPack) pack;

        ArrayList<File> args = info.get(ArrayList.class, 0);

        File where = args.remove(args.size() - 1);
        File[] files = new File[args.size()];
        args.toArray(files);

        int result = FileManager.mv(files, where, info.getSu());
        switch (result) {
            case FileManager.ISFILE:
                return info.res.getString(R.string.output_isfile);
            case FileManager.IOERROR:
                return info.res.getString(R.string.output_error);
            case FileManager.NOT_READABLE:
                return info.res.getString(R.string.output_noreadable);
            case FileManager.NOT_WRITEABLE:
                return info.res.getString(R.string.output_nowriteable);
        }
        return null;
    }

    @Override
    public int minArgs() {
        return 2;
    }

    @Override
    public int maxArgs() {
        return CommandAbstraction.UNDEFINIED;
    }

    @Override
    public int[] argType() {
        return new int[]{CommandAbstraction.FILE_LIST};
    }

    @Override
    public int priority() {
        return 4;
    }

    @Override
    public int helpRes() {
        return R.string.help_mv;
    }

    @Override
    public String onArgNotFound(ExecutePack pack, int index) {
        MainPack info = (MainPack) pack;
        return info.res.getString(R.string.output_filenotfound);
    }

    @Override
    public String onNotArgEnough(ExecutePack pack, int nArgs) {
        MainPack info = (MainPack) pack;
        return info.res.getString(R.string.output_filenotfound);
    }
}
