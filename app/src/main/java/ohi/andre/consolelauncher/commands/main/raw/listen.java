package ohi.andre.consolelauncher.commands.main.raw;

import ohi.andre.consolelauncher.R;
import ohi.andre.consolelauncher.commands.CommandAbstraction;
import ohi.andre.consolelauncher.commands.ExecutePack;
import ohi.andre.consolelauncher.commands.main.MainPack;
import ohi.andre.consolelauncher.commands.main.generals.music;

public class listen extends music {

    @Override
    public String exec(ExecutePack pack) {
        MainPack info = (MainPack) pack;
        String output = super.exec(info);
        if (output != null)
            return output;

        String name = info.get(String.class, 0);
        String path = info.player.getPath(name);
        if (info.player.jukebox(path))
            return info.res.getString(R.string.output_playing) + " " + name;
        return info.res.getString(R.string.output_nothingfound);
    }

    @Override
    public int helpRes() {
        return R.string.help_listen;
    }

    @Override
    public int minArgs() {
        return 1;
    }

    @Override
    public int maxArgs() {
        return 1;
    }

    @Override
    public int[] argType() {
        return new int[]{CommandAbstraction.SONG};
    }

    @Override
    public int priority() {
        return 2;
    }

    @Override
    public String onNotArgEnough(ExecutePack pack, int nArgs) {
        MainPack info = (MainPack) pack;
        return info.res.getString(helpRes());
    }

    @Override
    public String onArgNotFound(ExecutePack pack, int index) {
        MainPack info = (MainPack) pack;
        return info.res.getString(R.string.output_nothingfound);
    }

}
