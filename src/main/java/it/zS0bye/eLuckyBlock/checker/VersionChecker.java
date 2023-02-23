package it.zS0bye.eLuckyBlock.checker;

import org.bukkit.Bukkit;

public class VersionChecker {

    public static boolean getV1_8() {
        if (Bukkit.getVersion().contains("1.8")) {
            return true;
        } else if (Bukkit.getVersion().contains("1.8.3")) {
            return true;
        } else if (Bukkit.getVersion().contains("1.8.4")) {
            return true;
        } else if (Bukkit.getVersion().contains("1.8.5")) {
            return true;
        } else if (Bukkit.getVersion().contains("1.8.6")) {
            return true;
        } else if (Bukkit.getVersion().contains("1.8.7")) {
            return true;
        } else return Bukkit.getVersion().contains("1.8.8");
    }

    public static boolean getV1_9() {
        if (Bukkit.getVersion().contains("1.9")) {
            return true;
        } else if (Bukkit.getVersion().contains("1.9.2")) {
            return true;
        } else return (Bukkit.getVersion().contains("1.9.4"));
    }

    public static boolean getV1_10() {
        if(Bukkit.getVersion().contains("1.10")) {
            return true;
        }else return(Bukkit.getVersion().contains("1.10.2"));
    }

    public static boolean getV1_11() {
        if(Bukkit.getVersion().contains("1.11")) {
            return true;
        }else if(Bukkit.getVersion().contains("1.11.1")) {
            return true;
        } else return(Bukkit.getVersion().contains("1.11.2"));
    }

    public static boolean getV1_12() {
        if(Bukkit.getVersion().contains("1.12")) {
            return true;
        }else if(Bukkit.getVersion().contains("1.12.1")) {
            return true;
        } else return(Bukkit.getVersion().contains("1.12.2"));
    }

    public static boolean getV1_13() {
        if(Bukkit.getVersion().contains("1.13")) {
            return true;
        }else if(Bukkit.getVersion().contains("1.13.1")) {
            return true;
        } else return(Bukkit.getVersion().contains("1.13.2"));
    }

    public static boolean getV1_14() {
        if(Bukkit.getVersion().contains("1.14")) {
            return true;
        }else if(Bukkit.getVersion().contains("1.14.1")) {
            return true;
        } else if(Bukkit.getVersion().contains("1.14.2")) {
            return true;
        }else if (Bukkit.getVersion().contains("1.14.3")) {
            return true;
        }else return(Bukkit.getVersion().contains("1.14.4"));
    }

    public static boolean getV1_15() {
        if(Bukkit.getVersion().contains("1.15")) {
            return true;
        }else if(Bukkit.getVersion().contains("1.15.1")) {
            return true;
        } else return(Bukkit.getVersion().contains("1.15.2"));
    }
}
