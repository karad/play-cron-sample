import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.util.Duration;
import akka.util.FiniteDuration;
import controllers.task.TestTaskActorBase;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.Akka;
import play.libs.Time;

import java.text.ParseException;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * DESCRIPTION
 *
 * @author harakazuhiro
 * @since 2013/09/26 17:18
 */
public class Global extends GlobalSettings {

    @Override
    public void onStart(Application app) {
        super.onStart(app);
        task();
        try {
            TestTaskActorBase.getInstance().start();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop(Application app) {
        super.onStop(app);
        TestTaskActorBase.getInstance().shutdown();
    }

    /**
     * 監視用タイマータスク
     */
    private void task() {
        MyTimerTask task = new MyTimerTask();
        java.util.Timer timer = new java.util.Timer();
        timer.schedule(task, 5000, 1000);
    }

    public class MyTimerTask extends TimerTask {
        public void run() {
            System.out.println("watch:" + new Date().toString());
        }
    }

}
