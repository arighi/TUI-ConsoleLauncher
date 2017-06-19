package ohi.andre.consolelauncher.commands.main.raw;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ohi.andre.consolelauncher.R;
import ohi.andre.consolelauncher.commands.CommandAbstraction;
import ohi.andre.consolelauncher.commands.ExecutePack;
import ohi.andre.consolelauncher.commands.main.MainPack;
import ohi.andre.consolelauncher.commands.specific.ParamCommand;
import ohi.andre.consolelauncher.managers.AliasManager;
import ohi.andre.consolelauncher.managers.XMLPrefsManager;
import ohi.andre.consolelauncher.tuils.Tuils;

/**
 * Created by andre on 15/11/15.
 */
public class alias extends ParamCommand {

    private enum Param implements ohi.andre.consolelauncher.commands.main.Param {

        add {
            @Override
            public String exec(ExecutePack pack) {
                ArrayList<String> args = pack.get(ArrayList.class, 1);
                if(args.size() < 2) return pack.context.getString(R.string.output_lessarg);
                return ((MainPack) pack).aliasManager.add(args.remove(0), Tuils.toPlanString(args, Tuils.SPACE));
            }

            @Override
            public int[] args() {
                return new int[] {CommandAbstraction.TEXTLIST};
            }
        },
        remove {
            @Override
            public String exec(ExecutePack pack) {
                ArrayList<String> args = pack.get(ArrayList.class, 1);
                if(args.size() < 1) return pack.context.getString(R.string.output_lessarg);
                if(((MainPack) pack).aliasManager.remove(args.get(0))) return null;
                else return pack.context.getString(R.string.output_aliasnotfound) + Tuils.SPACE + args.get(0);
            }

            @Override
            public int[] args() {
                return new int[] {CommandAbstraction.TEXTLIST};
            }
        },
        file {
            @Override
            public String exec(ExecutePack pack) {
                pack.context.startActivity(Tuils.openFile(new File(Tuils.getFolder(), AliasManager.PATH)));
                return null;
            }

            @Override
            public int[] args() {
                return new int[0];
            }
        },
        ls {
            @Override
            public String exec(ExecutePack pack) {
                return ((MainPack) pack).aliasManager.printAliases();
            }

            @Override
            public int[] args() {
                return new int[0];
            }
        };

        static Param get(String p) {
            p = p.toLowerCase();
            Param[] ps = values();
            for (Param p1 : ps)
                if (p.endsWith(p1.label()))
                    return p1;
            return null;
        }

        static String[] labels() {
            Param[] ps = values();
            String[] ss = new String[ps.length];

            for (int count = 0; count < ps.length; count++) {
                ss[count] = ps[count].label();
            }

            return ss;
        }

        @Override
        public String label() {
            return Tuils.MINUS + name();
        }
    }


    @Override
    public String[] params() {
        return Param.labels();
    }

    @Override
    protected ohi.andre.consolelauncher.commands.main.Param paramForString(String param) {
        return Param.get(param);
    }

    @Override
    protected String doThings(ExecutePack pack) {
        return null;
    }

    @Override
    public int helpRes() {
        return R.string.help_aliases;
    }

    @Override
    public int minArgs() {
        return 1;
    }

    @Override
    public int maxArgs() {
        return CommandAbstraction.UNDEFINIED;
    }

    @Override
    public int priority() {
        return 2;
    }

    @Override
    public String onNotArgEnough(ExecutePack info, int nArgs) {
        return ((MainPack) info).aliasManager.printAliases();
    }

    @Override
    public String onArgNotFound(ExecutePack info, int index) {
        return null;
    }
}
