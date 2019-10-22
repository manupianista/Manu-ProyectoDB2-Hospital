package gio.co.hospitales;


import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
 import static java.util.concurrent.TimeUnit.*;


 public class sendMails {
    private final ScheduledExecutorService scheduler =
       Executors.newScheduledThreadPool(1);

    public void beepForAnHour() {
        final Runnable beeper = new Runnable() {
                public void run() { 
                    
                     try {
                            String url1 = "http://192.168.69.4:8888/proyectoDB2-Hospital1/sendStuff";
                                URL urlr1 = new URL(url1);
                                HttpURLConnection connr = (HttpURLConnection) urlr1.openConnection();
                                connr.setRequestMethod("GET");
                                connr.setDoOutput(true);
                                

                                Thread.sleep(15000);
                           } catch (Exception e){
                                System.err.println(e);
                           }
                
                }
            };
        final ScheduledFuture<?> beeperHandle =
            scheduler.scheduleAtFixedRate(beeper, 10, 10, SECONDS);
        scheduler.schedule(new Runnable() {
                public void run() { beeperHandle.cancel(true); }
            }, 60 * 60, SECONDS);
    }
 }







