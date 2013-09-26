package controllers.task;

import akka.actor.*;
import akka.util.Duration;
import akka.util.FiniteDuration;
import play.Logger;
import play.Play;
import play.libs.Time;

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TestTaskActorBase {

    private static TestTaskActorBase instance = new TestTaskActorBase();
    private ActorSystem system = ActorSystem.create("app");
    private ActorRef myTaskActor = system.actorOf(new Props(MyTaskActor.class));
    private Cancellable cancellable;

    private TestTaskActorBase() {
        super();
    }

    public static TestTaskActorBase getInstance() {
        return instance;
    }

    /**
     * 起動時呼び出し
     */
    public void start() throws ParseException {
        if(!Play.isTest()) {

            // 1分に1回
            //Time.CronExpression e = new Time.CronExpression("1 * * * * ?");
            // 1時間に1回
            //Time.CronExpression e = new Time.CronExpression("1 1 20 * * ?");
            // 1日に1回
            //Time.CronExpression e = new Time.CronExpression("1 1 20 * * ?");
            // 1月に1回
            //Time.CronExpression e = new Time.CronExpression("30 5 20 26 * ?");
            // 1週間に1回
            Time.CronExpression e = new Time.CronExpression("55 29 20 ? * 5");
            Date nextValidTimeAfter = e.getNextValidTimeAfter(new Date());
            FiniteDuration d = Duration.create(
                    nextValidTimeAfter.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);

            cancellable = system.scheduler().scheduleOnce(
                    d,
                    myTaskActor,
                    "Call");
        }
    }

    /**
     * 終了時呼び出し
     */
    public void shutdown() {
        if(!Play.isTest()) {
            cancellable.cancel();
        }
    }

    /**
     * アクター
     */
    public static class MyTaskActor extends UntypedActor {
        @Override
        public void onReceive(Object message) {
            if (message.equals("Call")) {
                Logger.info("TODAY IS ... : " + new Date().toString());
                try {
                    TestTaskActorBase.getInstance().start();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                unhandled(message);
            }
        }
    }



}
