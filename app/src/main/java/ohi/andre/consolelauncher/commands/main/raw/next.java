package ohi.andre.consolelauncher.commands.main.raw;

import ohi.andre.consolelauncher.R;
import ohi.andre.consolelauncher.commands.ExecutePack;
import ohi.andre.consolelauncher.commands.main.MainPack;
import ohi.andre.consolelauncher.commands.main.generals.music;

public class next extends music {

    @Override
    public String exec(ExecutePack pack) {
        MainPack info = (MainPack) pack;
        String output = super.exec(info);
        if (output != null)
            return output;

        output = info.player.next();
        return info.res.getString(R.string.output_playing) + " " + output;
    }

    @Override
    public int helpRes() {
        return R.string.help_next;
    }

    @Override
    public int minArgs() {
        return 0;
    }

    @Override
    public int maxArgs() {
        return 0;
    }

    @Override
    public int[] argType() {
        return new int[0];
    }

    @Override
    public int priority() {
        return 5;
    }

    @Override
    public String onNotArgEnough(ExecutePack info, int nArgs) {
        return null;
    }

    @Override
    public String onArgNotFound(ExecutePack info, int index) {
        return null;
    }

}
