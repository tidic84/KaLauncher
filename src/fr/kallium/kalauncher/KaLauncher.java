package fr.kallium.kalauncher;

import fr.litarvan.openauth.AuthPoints;
import fr.litarvan.openauth.AuthenticationException;
import fr.litarvan.openauth.Authenticator;
import fr.litarvan.openauth.model.AuthAgent;
import fr.litarvan.openauth.model.response.AuthResponse;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.swinger.Swinger;

import java.io.File;

public class KaLauncher {

        public static final GameVersion KA_VERSION = new GameVersion("1.12.2", GameType.V1_8_HIGHER);
        public static final GameInfos KA_INFOS = new GameInfos("Kallium", KA_VERSION, new GameTweak[]{GameTweak.FORGE});
        public static final File KA_DIR = KA_INFOS.getGameDir();

        private static AuthInfos authInfos;
        private static Thread updateThread = new Thread() {
                private int val;
                private int max;

                @Override
                public void run() {
                        while(!this.isInterrupted()){
                                val = (int) (BarAPI.getNumberOfTotalDownloadedBytes() / 1000);
                                max = (int) (BarAPI.getNumberOfTotalBytesToDownload() / 1000);

                                KaFrame.getInstance().getKaPanel().getProgressBar().setMaximum(max);
                                KaFrame.getInstance().getKaPanel().getProgressBar().setValue(val);

                                KaFrame.getInstance().getKaPanel().setInfoText("Telchargements des fichiers " +
                                        BarAPI.getNumberOfDownloadedFiles() + "/" + BarAPI.getNumberOfFileToDownload() + "  " +
                                        Swinger.percentage(val, max) + "%");
                        }
                }
        };

        public static void auth(String username, String password) throws AuthenticationException {
                Authenticator authenticator = new Authenticator(Authenticator.MOJANG_AUTH_URL, AuthPoints.NORMAL_AUTH_POINTS);
                AuthResponse response = authenticator.authenticate(AuthAgent.MINECRAFT, username, password, "");
                authInfos = new AuthInfos(response.getSelectedProfile().getName(), response.getAccessToken(), response.getSelectedProfile().getId());

        }

        public static void update() throws Exception{
                SUpdate su = new SUpdate("https://kallium.000webhostapp.com/update-server-launcher/", KA_DIR);

                updateThread.start();

                su.start();
                updateThread.interrupt();
        }

        public static void interruptThread() {
                updateThread.interrupt();
        }
}
